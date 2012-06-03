package org.jooq.util;

import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.TreeSet;
import java.util.regex.Matcher;

import org.jooq.tools.StringUtils;

/**
 * A wrapper for a {@link PrintWriter}
 * <p>
 * This wrapper postpones the actual write to the wrapped {@link PrintWriter}
 * until all information about the target Java class is available. This way, the
 * import dependencies can be calculated at the end.
 *
 * @author Lukas Eder
 */
public class GenerationWriter {

    private static final String SUPPRESS_WARNINGS_STATEMENT     = "__SUPPRESS_WARNINGS_STATEMENT__";
    private static final String STATIC_INITIALISATION_STATEMENT = "__STATIC_INITIALISATION_STATEMENT__";
    private static final String INITIALISATION_STATEMENT        = "__INITIALISATION_STATEMENT__";
    private static final String SERIAL_STATEMENT                = "__SERIAL_STATEMENT__";

    private final PrintWriter   writer;
    private final StringBuilder sb;
    private final List<String>  staticInitialisationStatements;
    private final List<String>  initialisationStatements;
    private final Set<Object>   alreadyPrinted;
    private final Set<String>   suppressWarnings;

    public GenerationWriter(File file) throws FileNotFoundException {
        file.getParentFile().mkdirs();

        this.writer = new PrintWriter(file);
        this.sb = new StringBuilder();
        this.staticInitialisationStatements = new ArrayList<String>();
        this.initialisationStatements = new ArrayList<String>();
        this.alreadyPrinted = new HashSet<Object>();
        this.suppressWarnings = new TreeSet<String>();
    }

    public void printStaticInitialisationStatementsPlaceholder() {
        println(STATIC_INITIALISATION_STATEMENT);
    }

    public void printInitialisationStatementsPlaceholder() {
        println(INITIALISATION_STATEMENT);
    }

    public void printSuppressWarningsPlaceholder() {
        println(SUPPRESS_WARNINGS_STATEMENT);
    }

    public void printStaticInitialisationStatement(String statement) {
        staticInitialisationStatements.add(statement);
    }

    public void printInitialisationStatement(String statement) {
        initialisationStatements.add(statement);
    }

    public void print(char value) {
        sb.append(value);
    }

    public void print(int value) {
        sb.append(value);
    }

    public void print(CharSequence string) {
        sb.append(string);
    }

    public void println(CharSequence string) {
        sb.append(string + "\n");
    }

    public void println() {
        sb.append("\n");
    }

    public void print(Class<?> clazz) {
        sb.append(clazz.getCanonicalName());
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
        println("\tprivate static final long serialVersionUID = " + SERIAL_STATEMENT + ";");
    }

    public void close() {
        String string = sb.toString();

        StringBuilder staticInits = new StringBuilder();
        StringBuilder inits = new StringBuilder();
        StringBuilder warnings = new StringBuilder();

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

        if (!suppressWarnings.isEmpty()) {
            warnings.append("@SuppressWarnings({");

            String separator = "";
            for (String warning : suppressWarnings) {
                warnings.append(separator);
                warnings.append("\"");
                warnings.append(warning);
                warnings.append("\"");

                separator = ", ";
            }

            warnings.append("})\n");
        }

        string = string.replaceAll(STATIC_INITIALISATION_STATEMENT + "\\n",
            Matcher.quoteReplacement(staticInits.toString()));
        string = string.replaceAll(INITIALISATION_STATEMENT + "\\n",
            Matcher.quoteReplacement(inits.toString()));
        string = string.replaceAll(SUPPRESS_WARNINGS_STATEMENT + "\\n",
            Matcher.quoteReplacement(warnings.toString()));
        string = string.replaceAll(SERIAL_STATEMENT,
            Matcher.quoteReplacement(String.valueOf(string.hashCode())));

        writer.append(string);
        writer.close();
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

    public void suppressWarnings(String string) {
        suppressWarnings.add(string);
    }
}
