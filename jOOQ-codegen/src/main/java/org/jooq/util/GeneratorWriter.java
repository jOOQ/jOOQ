/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public abstract class GeneratorWriter<W extends GeneratorWriter<W>> {

    /**
     * A pattern to be used with "list" expressions
     */
    private static final Pattern PATTERN_LIST = Pattern.compile(
        "\\[" +
           "(?:\\[before=([^\\]]+)\\])?" +
           "(?:\\[separator=([^\\]]+)\\])?" +
           "(?:\\[after=([^\\]]+)\\])?" +
           "(?:\\[(.*)\\])" +
        "\\]", Pattern.DOTALL);


    private final File          file;
    private final PrintWriter   writer;
    private final StringBuilder sb;
    private int                 indentTabs;
    private boolean             newline = true;

    protected GeneratorWriter(File file) {
        file.getParentFile().mkdirs();

        this.file = file;
        try {
            this.writer = new PrintWriter(file);
        }
        catch (FileNotFoundException e) {
            throw new GeneratorException("Error writing " + file.getAbsolutePath());
        }
        this.sb = new StringBuilder();
    }

    @SuppressWarnings("unchecked")
    public final W print(char value) {
        print("" + value);
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W print(int value) {
        print("" + value);
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W print(String string) {
        print(string, new Object[0]);
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W print(String string, Object... args) {
        if (newline && indentTabs > 0) {
            sb.append(StringUtils.leftPad("", indentTabs, '\t'));
            newline = false;
            indentTabs = 0;
        }

        if (args.length > 0) {
            List<Object> originals = Arrays.asList(args);
            List<Object> translated = new ArrayList<Object>();

            for (;;) {
                for (Object arg : originals) {
                    if (arg instanceof Class) {
                        translated.add(((Class<?>) arg).getName());
                    }
                    else if (arg instanceof Object[] || arg instanceof Collection) {
                        if (arg instanceof Collection) {
                            arg = ((Collection<?>) arg).toArray();
                        }

                        int start = string.indexOf("[[");
                        int end = string.indexOf("]]");

                        String expression = string.substring(start, end + 2);
                        StringBuilder replacement = new StringBuilder();

                        Matcher m = PATTERN_LIST.matcher(expression);
                        m.find();

                        String gBefore = StringUtils.defaultString(m.group(1));
                        String gSeparator = StringUtils.defaultString(m.group(2), ", ");
                        String gAfter = StringUtils.defaultString(m.group(3));
                        String gContent = m.group(4);

                        String separator = gBefore;

                        for (Object o : ((Object[]) arg)) {
                            translated.add(o);

                            replacement.append(separator);
                            replacement.append(gContent);
                            separator = gSeparator;
                        }

                        if (((Object[]) arg).length > 0) {
                            replacement.append(gAfter);
                        }

                        string = string.substring(0, start) + replacement + string.substring(end + 2);
                    }
                    else {
                        translated.add(arg);
                    }
                }

                if (!string.contains("[[")) {
                    break;
                }

                originals = translated;
                translated = new ArrayList<Object>();
            }

            sb.append(String.format(string, translated.toArray()));
        }
        else {
            sb.append(string);
        }

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W println() {
        sb.append("\n");
        newline = true;

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W println(int value) {
        print(value);
        println();

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W println(String string) {
        print(string);
        println();

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public final W println(String string, Object... args) {
        print(string, args);
        println();

        return (W) this;
    }

    @Deprecated
    public final boolean println(boolean doPrint) {
        if (doPrint) {
            println();
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public final W tab(int tabs) {
        this.indentTabs = tabs;
        return (W) this;
    }

    public final int tab() {
        return indentTabs;
    }

    public final void close() {
        String string = beforeClose(sb.toString());

        writer.append(string);
        writer.flush();
        writer.close();
    }

    protected String beforeClose(String string) {
        return string;
    }

    @Override
    public String toString() {
        return "GenerationWriter [" + file + "]";
    }
}
