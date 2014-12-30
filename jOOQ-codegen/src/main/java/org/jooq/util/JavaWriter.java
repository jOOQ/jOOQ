package org.jooq.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String       SERIAL_STATEMENT = "__SERIAL_STATEMENT__";
    private static final String       IMPORT_STATEMENT = "__IMPORT_STATEMENT__";

    private final Pattern             fullyQualifiedTypes;
    private final Set<String>         qualifiedTypes   = new TreeSet<String>();
    private final Map<String, String> unqualifiedTypes = new TreeMap<String, String>();
    private final String              className;

    public JavaWriter(File file, String fullyQualifiedTypes) {
        super(file);

        this.className = file.getName().replace(".java", "");
        this.fullyQualifiedTypes = fullyQualifiedTypes == null ? null : Pattern.compile(fullyQualifiedTypes);
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
        final int t = tab();

        // [#3450] Must not print */ inside Javadoc
        String escaped = string.replace("*/", "* /");

        tab(t).println();
        tab(t).println("/**");
        tab(t).println(" * " + escaped, args);
        tab(t).println(" */");

        return this;
    }

    public JavaWriter header(String header, Object... args) {
        int t = tab();

        tab(t).println();
        tab(t).println("// -------------------------------------------------------------------------");
        tab(t).println("// " + header, args);
        tab(t).println("// -------------------------------------------------------------------------");

        return this;
    }

    public JavaWriter override() {
        println("@Override");
        return this;
    }

    public JavaWriter overrideIf(boolean override) {
        if (override) {
            println("@Override");
        }

        return this;
    }

    public JavaWriter overrideInherit() {
        final int t = tab();

        tab(t).javadoc("{@inheritDoc}");
        tab(t).println("@Override");

        return this;
    }

    public void printSerial() {
        println();
        println("\tprivate static final long serialVersionUID = %s;", SERIAL_STATEMENT);
    }

    public void printImports() {
        println(IMPORT_STATEMENT);
    }

    @Override
    protected String beforeClose(String string) {
        StringBuilder importString = new StringBuilder();

        String previous = "";
        for (String imp : qualifiedTypes) {
            if (imp.startsWith("java.lang."))
                continue;

            String topLevelPackage = imp.split("\\.")[0];

            if (!topLevelPackage.equals(previous))
                importString.append("\n");

            importString.append("import ")
                        .append(imp)
                        .append(";\n");

            previous = topLevelPackage;
        }

        string = string.replaceAll(IMPORT_STATEMENT, Matcher.quoteReplacement(importString.toString()));
        string = string.replaceAll(SERIAL_STATEMENT, Matcher.quoteReplacement(String.valueOf(string.hashCode())));
        return string;
    }

    @Override
    protected List<String> ref(List<String> clazz, int keepSegments) {
        List<String> result = new ArrayList<String>();

        if (clazz != null) {
            for (String c : clazz) {

                // Skip unqualified and primitive types
                if (c.contains(".")) {

                    // com.example.Table.TABLE.COLUMN (with keepSegments = 3)
                    if (fullyQualifiedTypes == null || !fullyQualifiedTypes.matcher(c).matches()) {

                        // That's a unicode-safe \w (http://stackoverflow.com/a/4307261/521799)
                        Pattern p = Pattern.compile("([\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]\\.]+)((?:<.*>|\\[\\])*)");
                        Matcher m = p.matcher(c);

                        if (m.find()) {

                            // [com, example, Table, TABLE, COLUMN]
                            List<String> split = Arrays.asList(m.group(1).split("\\."));

                            // com.example.Table
                            String qualifiedType = StringUtils.join(split.subList(0, split.size() - keepSegments + 1).toArray(), ".");

                            // Table
                            String unqualifiedType = split.get(split.size() - keepSegments);

                            // Table.TABLE.COLUMN
                            String remainder = StringUtils.join(split.subList(split.size() - keepSegments, split.size()).toArray(), ".");

                            if (!className.equals(unqualifiedType) &&
                               (!unqualifiedTypes.containsKey(unqualifiedType) || qualifiedType.equals(unqualifiedTypes.get(unqualifiedType)))) {

                            unqualifiedTypes.put(unqualifiedType, qualifiedType);
                            qualifiedTypes.add(qualifiedType);
                            c = remainder + m.group(2);
                        }
                        }
                    }
                }

                // If any of the above tests fail, c will remain the unchanged,
                // fully qualified type name.
                result.add(c);
            }
        }

        return result;
    }
}
