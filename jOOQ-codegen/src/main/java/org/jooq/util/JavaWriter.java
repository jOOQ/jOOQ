package org.jooq.util;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

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

    private static final String STATIC_INITIALISATION_STATEMENT = "__STATIC_INITIALISATION_STATEMENT__";
    private static final String INITIALISATION_STATEMENT        = "__INITIALISATION_STATEMENT__";
    private static final String SERIAL_STATEMENT                = "__SERIAL_STATEMENT__";

    private final List<String>  staticInitialisationStatements;
    private final List<String>  initialisationStatements;
    private final Set<Object>   alreadyPrinted;

    public JavaWriter(File file) {
        super(file);

        this.staticInitialisationStatements = new ArrayList<String>();
        this.initialisationStatements = new ArrayList<String>();
        this.alreadyPrinted = new HashSet<Object>();
    }

    public void printStaticInitialisationStatementsPlaceholder() {
        println(STATIC_INITIALISATION_STATEMENT);
    }

    public void printInitialisationStatementsPlaceholder() {
        println(INITIALISATION_STATEMENT);
    }

    public void printStaticInitialisationStatement(String statement) {
        staticInitialisationStatements.add(statement);
    }

    public void printInitialisationStatement(String statement) {
        initialisationStatements.add(statement);
    }

    public JavaWriter print(Class<?> clazz) {
        print(clazz.getCanonicalName());
        return this;
    }

    public JavaWriter javadoc(String string, Object... args) {
        final int t = tab();

        tab(t).println();
        tab(t).println("/**");
        tab(t).println(" * " + string, args);
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

    public JavaWriter overrideInherit() {
        final int t = tab();

        tab(t).javadoc("{@inheritDoc}");
        tab(t).println("@Override");

        return this;
    }

    public JavaWriter suppress(String... warnings) {
        if (warnings.length == 1) {
            println("@SuppressWarnings(\"%s\")", warnings[0]);
        }
        else {
            println("@SuppressWarnings({ [[join|\"%s\"]] })", (Object) warnings);
        }

        return this;
    }

    public boolean printOnlyOnce(Object object) {
        if (!alreadyPrinted.contains(object)) {
            alreadyPrinted.add(object);
            return true;
        }

        return false;
    }

    public void printSerial() {
        println();
        println("\tprivate static final long serialVersionUID = %s;", SERIAL_STATEMENT);
    }

    @Override
    protected String beforeClose(String string) {
        StringBuilder staticInits = new StringBuilder();
        StringBuilder inits = new StringBuilder();

        boolean hasStaticInits = false;
        boolean hasInits = false;

        for (String statement : staticInitialisationStatements) {
            if (!StringUtils.isBlank(statement)) {
                hasStaticInits = true;
                break;
            }
        }

        for (String statement : initialisationStatements) {
            if (!StringUtils.isBlank(statement)) {
                hasInits = true;
                break;
            }
        }

        if (hasStaticInits) {
            staticInits.append("\n");
            staticInits.append("\t/*\n");
            staticInits.append("\t * static initialiser\n");
            staticInits.append("\t */\n");
            staticInits.append("\tstatic {\n");
            for (String statement : staticInitialisationStatements) {
                staticInits.append("\t\t" + statement + "\n");
            }
            staticInits.append("\t}\n");
        }

        if (hasInits) {
            inits.append("\n");
            inits.append("\t/*\n");
            inits.append("\t * instance initialiser\n");
            inits.append("\t */\n");
            inits.append("\t{\n");
            for (String statement : initialisationStatements) {
                inits.append("\t\t" + statement + "\n");
            }
            inits.append("\t}\n");
        }

        string = string.replaceAll(STATIC_INITIALISATION_STATEMENT + "\\n",
            Matcher.quoteReplacement(staticInits.toString()));
        string = string.replaceAll(INITIALISATION_STATEMENT + "\\n", Matcher.quoteReplacement(inits.toString()));
        string = string.replaceAll(SERIAL_STATEMENT, Matcher.quoteReplacement(String.valueOf(string.hashCode())));

        return string;
    }

    public <T> void printNewJavaObject(String type, Object value) {
        print(getNewJavaObject(type, value));
    }

    private <T> String getNewJavaObject(String type, Object value) {
        if (value == null) {
            return "null";
        }

        if (type == Blob.class.getName()) {
            // Not supported
        }
        else if (type == Boolean.class.getName()) {
            return Boolean.toString(getIsTrue(String.valueOf(value)));
        }
        else if (type == BigInteger.class.getName()) {
            return "new java.math.BigInteger(\"" + value + "\")";
        }
        else if (type == BigDecimal.class.getName()) {
            return "new java.math.BigDecimal(\"" + value + "\")";
        }
        else if (type == Byte.class.getName()) {
            return "(byte) " + value;
        }
        else if (type == byte[].class.getName()) {
            // Not supported
        }
        else if (type == Clob.class.getName()) {
            // Not supported
        }
        else if (type == Date.class.getName()) {
            return "new java.sql.Date(" + ((Date) value).getTime() + "L)";
        }
        else if (type == Double.class.getName()) {
            return Double.toString(Double.valueOf("" + value));
        }
        else if (type == Float.class.getName()) {
            return Float.toString(Float.valueOf("" + value)) + "f";
        }
        else if (type == Integer.class.getName()) {
            return Integer.toString(Integer.valueOf("" + value));
        }
        else if (type == Long.class.getName()) {
            return Long.toString(Long.valueOf("" + value)) + "L";
        }
        else if (type == Short.class.getName()) {
            return "(short) " + value;
        }
        else if (type == String.class.getName()) {
            return "\"" + value.toString().replace("\"", "\\\"") + "\"";
        }
        else if (type == Time.class.getName()) {
            return "new java.sql.Time(" + ((Time) value).getTime() + "L)";
        }
        else if (type == Timestamp.class.getName()) {
            return "new java.sql.Timestamp(" + ((Timestamp) value).getTime() + "L)";
        }
        else {
            // Not supported
        }

        throw new UnsupportedOperationException("Class " + type + " is not supported");
    }

    private boolean getIsTrue(String string) {
        if ("1".equals(string)) {
            return true;
        }
        else if ("true".equalsIgnoreCase(string)) {
            return true;
        }
        else {
            return false;
        }
    }
}
