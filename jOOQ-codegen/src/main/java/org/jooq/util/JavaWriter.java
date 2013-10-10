package org.jooq.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.regex.Matcher;

/**
 * A wrapper for a {@link PrintWriter}
 * <p>
 * This wrapper adds Java printing features to the general
 * {@link GeneratorWriter}
 *
 * @author Lukas Eder
 */
public class JavaWriter extends GeneratorWriter<JavaWriter> {

    private static final String SERIAL_STATEMENT                = "__SERIAL_STATEMENT__";

    public JavaWriter(File file) {
        super(file);
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

    @Override
    protected String beforeClose(String string) {
        string = string.replaceAll(SERIAL_STATEMENT, Matcher.quoteReplacement(String.valueOf(string.hashCode())));
        return string;
    }
}
