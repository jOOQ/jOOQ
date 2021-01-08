package org.jooq.codegen;

import static java.util.stream.Collectors.toList;
import static org.jooq.codegen.GenerationUtil.PLAIN_GENERIC_TYPE_PATTERN;
import static org.jooq.codegen.GenerationUtil.TYPE_REFERENCE_PATTERN;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jooq.meta.jaxb.GeneratedSerialVersionUID;
import org.jooq.tools.StringUtils;

/**
 * A wrapper for a {@link PrintWriter}
 * <p>
 * This wrapper adds Java printing features to the general
 * {@link GeneratorWriter}
 *
 * @author Lukas Eder
 */
public class JavaWriter extends GeneratorWriter<JavaWriter> {

    private static final String             SERIAL_STATEMENT = "__SERIAL_STATEMENT__";
    private static final String             IMPORT_STATEMENT = "__IMPORT_STATEMENT__";

    private final Pattern                   fullyQualifiedTypes;
    private final boolean                   javadoc;
    private final Set<String>               refConflicts;
    private final Set<String>               qualifiedTypes   = new TreeSet<>(qualifiedTypeComparator());
    private final Map<String, String>       unqualifiedTypes = new TreeMap<>();
    private final String                    className;
    private String                          packageName;
    private final boolean                   isJava;
    private final boolean                   isScala;
    private final boolean                   isKotlin;
    private final GeneratedSerialVersionUID generatedSerialVersionUID;

    public JavaWriter(File file, String fullyQualifiedTypes) {
        this(file, fullyQualifiedTypes, null);
    }

    public JavaWriter(File file, String fullyQualifiedTypes, String encoding) {
        this(file, fullyQualifiedTypes, encoding, true);
    }

    public JavaWriter(File file, String fullyQualifiedTypes, String encoding, boolean javadoc) {
        this(file, fullyQualifiedTypes, encoding, javadoc, null);
    }

    public JavaWriter(File file, String fullyQualifiedTypes, String encoding, boolean javadoc, Files files) {
        this(file, fullyQualifiedTypes, encoding, javadoc, files, GeneratedSerialVersionUID.CONSTANT);
    }

    public JavaWriter(File file, String fullyQualifiedTypes, String encoding, boolean javadoc, Files files, GeneratedSerialVersionUID generatedSerialVersionUID) {
        super(file, encoding, files);

        this.className = file.getName().replaceAll("\\.(java|scala|kt)$", "");
        this.isJava = file.getName().endsWith(".java");
        this.isScala = file.getName().endsWith(".scala");
        this.isKotlin = file.getName().endsWith(".kt");
        this.refConflicts = new HashSet<>();
        this.fullyQualifiedTypes = fullyQualifiedTypes == null ? null : Pattern.compile(fullyQualifiedTypes);
        this.javadoc = javadoc;
        this.generatedSerialVersionUID = generatedSerialVersionUID;

        if (isJava || isKotlin)
            tabString("    ");
        else if (isScala)
            tabString("  ");
    }

    public JavaWriter print(Class<?> clazz) {
        printClass(clazz.getCanonicalName());
        return this;
    }

    public JavaWriter printClass(String clazz) {
        print(ref(clazz));
        return this;
    }

    public JavaWriter javadoc(String string, Object... args) {
        println();

        if (javadoc) {

            // [#3450] [#4575] [#7693] Must not print */ inside Javadoc
            String escaped = escapeJavadoc(string);
            Object[] escapedArgs = Arrays.copyOf(args, args.length);
            for (int i = 0; i < escapedArgs.length; i++)
                escapedArgs[i] = escapeJavadoc(escapedArgs[i]);

            println("/**");
            println(escaped, escapedArgs);
            println(" */");
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    static Object escapeJavadoc(Object object) {
        if (object instanceof String)
            return escapeJavadoc((String) object);
        else if (object instanceof List)
            return ((List<Object>) object).stream().map(JavaWriter::escapeJavadoc).collect(toList());
        else
            return object;
    }

    static String escapeJavadoc(String string) {

        // [#3450] [#4880] [#7693] Must not print */ inside Javadoc
        return string
            .replace("/*", "/ *")
            .replace("*/", "* /")
            .replace("\\u002a/", "\\u002a /")
            .replace("*\\u002f", "* \\u002f")
            .replace("\\u002a\\u002f", "\\u002a \\u002f");
    }

    public JavaWriter header(String header, Object... args) {
        println();
        println("// -------------------------------------------------------------------------");
        println("// " + header, args);
        println("// -------------------------------------------------------------------------");

        return this;
    }

    public JavaWriter override() {
        println("@%s", Override.class);
        return this;
    }

    public JavaWriter overrideIf(boolean override) {
        if (override)
            println("@%s", Override.class);

        return this;
    }

    public JavaWriter overrideInherit() {
        println();
        override();
        return this;
    }

    public JavaWriter overrideInheritIf(boolean override) {
        println();
        if (override)
            override();

        return this;
    }

    public void printSerial() {
        if (isJava && generatedSerialVersionUID != GeneratedSerialVersionUID.OFF) {
            println();
            println("private static final long serialVersionUID = %s;", SERIAL_STATEMENT);
        }
    }

    @SuppressWarnings("hiding")
    public void printPackageSpecification(String packageName) {
        this.packageName = packageName;

        if (isScala || isKotlin)
            println("package %s", packageName);
        else
            println("package %s;", packageName);
    }

    public void printImports() {
        println(IMPORT_STATEMENT);
    }

    /**
     * Subclasses may override this to specify their own order of qualified types.
     */
    protected Comparator<String> qualifiedTypeComparator() {
        return null;
    }

    @Override
    protected String beforeClose(String string) {
        string = super.beforeClose(string);
        StringBuilder importString = new StringBuilder();
        Pattern samePackagePattern = Pattern.compile(packageName + "\\.[^.]+");
        String dotClassName = "." + className;

        String previous = "";
        for (String imp : qualifiedTypes) {

            // [#4021] For Scala interoperability, we better also import
            // java.lang types
            if ((isJava || isKotlin) && imp.startsWith("java.lang."))
                continue;

            // [#6248] java.lang.Integer is converted to kotlin.Int, and shouldn't be imported
            if (isKotlin && imp.startsWith("kotlin.") && !imp.substring("kotlin.".length()).contains("."))
                continue;

            // Don't import the class itself
            if (imp.endsWith(dotClassName))
                continue;

            // [#4229] [#4531] [#11103] Avoid warnings due to unnecessary same-package imports
            if (packageName != null && packageName.length() > 0 && samePackagePattern.matcher(imp).matches())
                continue;

            String topLevelPackage = imp.split("\\.")[0];

            if (!topLevelPackage.equals(previous))
                importString.append(newlineString());

            importString.append("import ")
                        .append(imp)
                        .append(isScala || isKotlin ? "" : ";").append(newlineString());

            previous = topLevelPackage;
        }

        string = string.replaceAll(IMPORT_STATEMENT, Matcher.quoteReplacement(importString.toString()));

        if (isJava) {
            switch (StringUtils.defaultIfNull(generatedSerialVersionUID, GeneratedSerialVersionUID.CONSTANT)) {
                case HASH:
                    string = string.replaceAll(SERIAL_STATEMENT, Matcher.quoteReplacement(String.valueOf(string.hashCode())));
                    break;

                case OFF:
                    break;

                case CONSTANT:
                default:
                    string = string.replaceAll(SERIAL_STATEMENT, Matcher.quoteReplacement("1L"));
                    break;
            }
        }

        return string;
    }

    public JavaWriter refConflicts(List<String> conflicts) {
        this.refConflicts.addAll(conflicts);
        return this;
    }

    @Override
    protected List<String> ref(List<String> clazz, int keepSegments) {
        List<String> result = new ArrayList<>(clazz == null ? 0 : clazz.size());

        if (clazz != null) {
            for (String c : clazz) {

                // Skip unqualified and primitive types
                checks: {
                    if (!c.contains("."))
                        break checks;

                    c = patchKotlinClasses(c);

                    // com.example.Table.TABLE.COLUMN (with keepSegments = 3)
                    if (fullyQualifiedTypes != null && fullyQualifiedTypes.matcher(c).matches())
                        break checks;

                    Matcher m = TYPE_REFERENCE_PATTERN.matcher(c);
                    if (!m.find())
                        break checks;

                    // [com, example, Table, TABLE, COLUMN]
                    List<String> split = Arrays.asList(m.group(1).split("\\."));

                    // com.example.Table
                    String qualifiedType = StringUtils.join(split.subList(0, split.size() - keepSegments + 1).toArray(), ".");

                    // Table
                    String unqualifiedType = split.get(split.size() - keepSegments);

                    // Table.TABLE.COLUMN
                    String remainder = StringUtils.join(split.subList(split.size() - keepSegments, split.size()).toArray(), ".");

                    // [#9697] Don't import a class from a different package by the same name as this class
                    if ((className.equals(unqualifiedType) && (packageName == null || !qualifiedType.equals(packageName + "." + className)))
                        || (unqualifiedTypes.containsKey(unqualifiedType) && !qualifiedType.equals(unqualifiedTypes.get(unqualifiedType))))
                        break checks;

                    // [#10561] Don't import type that conflicts with a local identifier
                    if (refConflicts.contains(unqualifiedType))
                        break checks;

                    // [#10561] Don't import a class that conflicts with a local identifier
                    unqualifiedTypes.put(unqualifiedType, qualifiedType);
                    qualifiedTypes.add(qualifiedType);
                    String generic = m.group(2);

                    // Consider importing generic type arguments, recursively
                    c = remainder
                      + (PLAIN_GENERIC_TYPE_PATTERN.matcher(generic).matches()
                      ?  generic.substring(0, 1) + ref(generic.substring(1, generic.length() - 1)) + generic.substring(generic.length() - 1)
                      :  generic);
                }

                // If any of the above tests fail, c will remain the unchanged,
                // fully qualified type name.
                result.add(c);
            }
        }

        return result;
    }

    private static final Pattern KOTLIN_ARRAY_PATTERN = Pattern.compile("kotlin.Array<([^?>]*)\\?>");

    private String patchKotlinClasses(String c) {
        // [#10768] TODO: Is this the right place to patch these classes?

        Matcher m;
        if (isKotlin) {
            if (c.endsWith("[]"))
                c = "kotlin.Array<" + patchKotlinClasses(c.substring(0, c.length() - 2)) + "?>";
            else if (Byte.class.getName().equals(c))
                c = "kotlin.Byte";
            else if (Short.class.getName().equals(c))
                c = "kotlin.Short";
            else if (Integer.class.getName().equals(c))
                c = "kotlin.Int";
            else if (Long.class.getName().equals(c))
                c = "kotlin.Long";
            else if (Float.class.getName().equals(c))
                c = "kotlin.Float";
            else if (Double.class.getName().equals(c))
                c = "kotlin.Double";
            else if (Boolean.class.getName().equals(c))
                c = "kotlin.Boolean";
            else if (Character.class.getName().equals(c))
                c = "kotlin.Char";
            else if (String.class.getName().equals(c))
                c = "kotlin.String";
            else if (Object.class.getName().equals(c))
                c = "kotlin.Any";
            else if ((m = KOTLIN_ARRAY_PATTERN.matcher(c)).matches())
                c = m.replaceAll("kotlin.Array<" + ref(patchKotlinClasses(m.group(1))) + "?>");
        }

        return c;
    }
}
