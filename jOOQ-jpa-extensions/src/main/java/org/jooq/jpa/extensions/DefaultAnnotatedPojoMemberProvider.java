package org.jooq.jpa.extensions;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.jooq.impl.Internal.getInstanceMembers;
import static org.jooq.impl.Internal.getInstanceMethods;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jooq.Configuration;
import org.jooq.conf.Settings;
import org.jooq.impl.AnnotatedPojoMemberProvider;
import org.jooq.tools.JooqLogger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The default {@link AnnotatedPojoMemberProvider} implementation that looks up
 * Jakarta persistence annotated methods and members from pojos.
 * <p>
 * While implementations may choose to cache their results, caching is also
 * taken care of in the calling {@link Configuration} according to
 * {@link Configuration#cacheProvider()} and
 * {@link Settings#isReflectionCaching()} and related settings.
 *
 * @author Lukas Eder
 */
public class DefaultAnnotatedPojoMemberProvider implements AnnotatedPojoMemberProvider {

    /**
     * A lock for the initialisation of other static members
     */
    private static final Object          initLock          = new Object();

    /**
     * Indicating whether JPA (<code>jakarta.persistence</code>) is on the
     * classpath.
     */
    private static volatile JPANamespace jpaNamespace;

    static final JooqLogger              logHasAnnotations = JooqLogger
        .getLogger(DefaultAnnotatedPojoMemberProvider.class, "hasAnnotations", 1);

    static final JooqLogger              logJpaAvailable = JooqLogger
        .getLogger(DefaultAnnotatedPojoMemberProvider.class, "jpaAvailable", 1);

    @Override
    public List<Field> getMembers(Class<?> type, String name) {
        List<Field> result = new ArrayList<>();

        for (Field member : getInstanceMembers(type)) {
            Column column = member.getAnnotation(Column.class);

            if (column != null) {
                if (namesMatch(name, column.name()))
                    result.add(member);
            }

            else {
                Id id = member.getAnnotation(Id.class);

                if (id != null)
                    if (namesMatch(name, member.getName()))
                        result.add(member);
            }
        }

        return result;
    }

    @Override
    public List<Method> getGetters(Class<?> type, String name) {
        for (Method method : getInstanceMethods(type)) {
            Column column = method.getAnnotation(Column.class);

            if (column != null && namesMatch(name, column.name())) {

                // Annotated getter
                if (method.getParameterTypes().length == 0) {
                    return singletonList(method);
                }

                // Annotated setter with matching getter
                else if (method.getParameterTypes().length == 1) {
                    String m = method.getName();

                    if (m.startsWith("set")) {
                        try {
                            Method getter1 = type.getMethod("get" + m.substring(3));

                            // Getter annotation is more relevant
                            if (getter1.getAnnotation(Column.class) == null)
                                return singletonList(getter1);
                        }
                        catch (NoSuchMethodException ignore1) {}

                        try {
                            Method getter2 = type.getMethod("is" + m.substring(3));

                            // Getter annotation is more relevant
                            if (getter2.getAnnotation(Column.class) == null)
                                return singletonList(getter2);
                        }
                        catch (NoSuchMethodException ignore2) {}
                    }
                }
            }
        }

        return emptyList();
    }

    @Override
    public List<Method> getSetters(Class<?> type, String name) {
        List<Method> result = new ArrayList<>();

        for (Method method : getInstanceMethods(type)) {
            Column column = method.getAnnotation(Column.class);

            if (column != null && namesMatch(name, column.name())) {

                // Annotated setter
                if (method.getParameterTypes().length == 1) {
                    result.add(method);
                }

                // Annotated getter with matching setter
                else if (method.getParameterTypes().length == 0) {
                    String m = method.getName();
                    String suffix = m.startsWith("get")
                                  ? m.substring(3)
                                  : m.startsWith("is")
                                  ? m.substring(2)
                                  : null;

                    if (suffix != null) {
                        try {

                            // [#7953] [#8496] Search the hierarchy for a matching setter
                            Method setter = getInstanceMethod(type, "set" + suffix, new Class[] { method.getReturnType() });

                            // Setter annotation is more relevant
                            if (setter.getAnnotation(Column.class) == null)
                                result.add(setter);
                        }
                        catch (NoSuchMethodException ignore) {}
                    }
                }
            }
        }

        return result;
    }

    private static final Method getInstanceMethod(Class<?> type, String name, Class<?>[] parameters) throws NoSuchMethodException {

        // first priority: find a public method with exact signature match in class hierarchy
        try {
            return type.getMethod(name, parameters);
        }

        // second priority: find a private method with exact signature match on declaring class
        catch (NoSuchMethodException e) {
            do {
                try {
                    return type.getDeclaredMethod(name, parameters);
                }
                catch (NoSuchMethodException ignore) {}

                type = type.getSuperclass();
            }
            while (type != null);

            throw new NoSuchMethodException();
        }
    }

    private static final boolean namesMatch(String name, String annotation) {

        // [#4128] JPA @Column.name() properties are case-insensitive, unless
        // the names are quoted using double quotes.
        return annotation.startsWith("\"")
            ? ('"' + name + '"').equals(annotation)
            : name.equalsIgnoreCase(annotation);
    }

    /**
     * Check if JPA classes can be loaded. This is only done once per JVM!
     */
    static final JPANamespace jpaNamespace() {
        if (jpaNamespace == null) {
            synchronized (initLock) {
                if (jpaNamespace == null) {
                    try {
                        Class.forName(Column.class.getName());
                        jpaNamespace = JPANamespace.JAKARTA;
                    }
                    catch (Throwable e) {
                        try {

                            // [#14180] Break the maven-bundle-plugin class analyser, to prevent
                            //          adding a package import to MANIFEST.MF for this lookup
                            Class.forName(new String("javax.persistence.") + new String("Column"));
                            jpaNamespace = JPANamespace.JAVAX;
                            logJpaAvailable.info("javax.persistence.Column was found on the classpath instead of jakarta.persistence.Column. jOOQ 3.16 requires you to upgrade to Jakarta EE if you wish to use JPA annotations in your DefaultRecordMapper");
                        }
                        catch (Throwable ignore) {
                            jpaNamespace = JPANamespace.NONE;
                        }
                    }
                }
            }
        }

        return jpaNamespace;
    }

    @Override
    public boolean hasAnnotations(Class<?> type) {
        switch (jpaNamespace()) {
            case JAVAX:
                if (hasJavaxPersistenceAnnotation().test(type))
                    logHasAnnotations.warn("Type " + type + " is annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                if (Stream.of(type.getMethods()).anyMatch(hasJavaxPersistenceAnnotation())
                    || Stream.of(type.getDeclaredMethods()).anyMatch(hasJavaxPersistenceAnnotation()))
                    logHasAnnotations.warn("Type " + type + " has methods annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                if (Stream.of(type.getFields()).anyMatch(hasJavaxPersistenceAnnotation())
                    || Stream.of(type.getDeclaredFields()).anyMatch(hasJavaxPersistenceAnnotation()))
                    logHasAnnotations.warn("Type " + type + " has fields annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                return false;

            case JAKARTA:

                // An @Entity or @Table usually has @Column annotations, too
                if (type.getAnnotation(Entity.class) != null)
                    return true;

                if (type.getAnnotation(Table.class) != null)
                    return true;

                if (getInstanceMembers(type).stream().anyMatch(m ->
                        m.getAnnotation(Column.class) != null
                     || m.getAnnotation(Id.class) != null))
                    return true;
                else
                    return getInstanceMethods(type).stream().anyMatch(m -> m.getAnnotation(Column.class) != null);

            case NONE:
            default:
                return false;
        }
    }

    private static final Predicate<? super AnnotatedElement> hasJavaxPersistenceAnnotation() {
        return a -> Stream.of(a.getAnnotations()).anyMatch(isJavaxPersistenceAnnotation());
    }


    private static final Predicate<? super Annotation> isJavaxPersistenceAnnotation() {
        return a -> a.annotationType().getName().startsWith("javax.persistence.");
    }

    enum JPANamespace {
        JAVAX, JAKARTA, NONE
    }
}
