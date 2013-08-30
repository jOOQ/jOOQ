/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
            this.writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        }
        catch (IOException e) {
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
