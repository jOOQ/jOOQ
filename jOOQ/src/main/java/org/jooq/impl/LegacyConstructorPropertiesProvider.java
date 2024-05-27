/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.jooq.ConstructorPropertiesProvider;
import org.jooq.tools.JooqLogger;

/**
 * Class maintaining backwards compatible behaviour, without formal declaration
 * of <code>java.beans</code> dependency in module info.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
final class LegacyConstructorPropertiesProvider implements ConstructorPropertiesProvider {

    static final JooqLogger                    log = JooqLogger.getLogger(LegacyConstructorPropertiesProvider.class, 1);
    static final ConstructorPropertiesProvider DELEGATE;
    static final Class<Annotation>             P;
    static final Method                        V;

    static {
        Class<Annotation> p;
        Method v;

        try {

            // [#14180] Break the maven-bundle-plugin class analyser, to prevent
            //          adding a package import to MANIFEST.MF for this lookup
            p = (Class<Annotation>) Class.forName(new String("java.beans.") + new String("ConstructorProperties"));
            v = p.getMethod("value");
        }
        catch (Exception e) {
            p = null;
            v = null;
        }

        P = p;
        V = v;

        DELEGATE = c -> {
            if (V != null) {
                Annotation a = c.getAnnotation(P);

                try {
                    if (a != null) {
                        log.warn("ConstructorProperties", """
                            No explicit ConstructorPropertiesProvider configuration present.

                            ConstructorProperties annotation is present on POJO {pojo}
                            without any explicit ConstructorPropertiesProvider configuration.

                            Starting from jOOQ 3.20, the Configuration.constructorPropertiesProvider() SPI is required for the
                            DefaultRecordMapper to map a Record to a POJO that is annotated with ConstructorProperties. For
                            backwards compatibility, This LegacyConstructorPropertiesProvider keeps the historic behaviour in
                            place using reflection. This implementation is due for removal in a future version of jOOQ.

                            If you wish to continue working with the java.beans.ConstructorProperties annotation, use the
                            jOOQ-beans-extensions module and its DefaultConstructorPropertiesProvider implementation
                            """.replace("{pojo}", c.getDeclaringClass().getName())
                        );

                        return (String[]) V.invoke(a);
                    }
                }
                catch (Exception e) {
                    log.error(e);
                }
            }

            return null;
        };
    }

    @Override
    public String[] properties(Constructor<?> constructor) {
        return DELEGATE.properties(constructor);
    }
}
