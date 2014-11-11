/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
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
    private final StringBuilder sb;
    private int                 indentTabs;
    private boolean             newline = true;

    protected GeneratorWriter(File file) {
        file.getParentFile().mkdirs();

        this.file = file;
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

        // Don't add empty lines at the beginning of files
        if (sb.length() > 0) {
            sb.append("\n");
            newline = true;
        }

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

    @SuppressWarnings("unchecked")
    public final W tab(int tabs) {
        this.indentTabs = tabs;
        return (W) this;
    }

    public final int tab() {
        return indentTabs;
    }

    public final void close() {
        String newContent = beforeClose(sb.toString());

        try {
            // [#3756] Regenerate files only if there is a difference
            String oldContent = null;
            if (file.exists()) {
                RandomAccessFile old = null;

                try {
                    old = new RandomAccessFile(file, "r");
                    byte[] oldBytes = new byte[(int) old.length()];
                    old.readFully(oldBytes);
                    oldContent = new String(oldBytes, "UTF-8");
                }
                finally {
                    if (old != null)
                        old.close();
                }
            }

            if (oldContent == null || !oldContent.equals(newContent)) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

                writer.append(newContent);
                writer.flush();
                writer.close();
            }
        }
        catch (IOException e) {
            throw new GeneratorException("Error writing " + file.getAbsolutePath());
        }
    }

    protected String beforeClose(String string) {
        return string;
    }

    @Override
    public String toString() {
        return "GenerationWriter [" + file + "]";
    }
}
