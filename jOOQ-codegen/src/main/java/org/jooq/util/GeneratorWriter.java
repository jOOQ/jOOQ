/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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
import java.util.Collections;
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


    private final File           file;
    private final String         encoding;
    private final StringBuilder  sb;
    private int                  indentTabs;
    private String               tabString    = "    ";
    private boolean              newline      = true;

    protected GeneratorWriter(File file) {
        this(file, null);
    }

    protected GeneratorWriter(File file, String encoding) {
        file.getParentFile().mkdirs();

        this.file = file;
        this.encoding = encoding;
        this.sb = new StringBuilder();
    }

    public void tabString(String string) {
        this.tabString = string;
    }

    public File file() {
        return file;
    }

    @SuppressWarnings("unchecked")
    public W print(char value) {
        print("" + value);
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W print(int value) {
        print("" + value);
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W print(String string) {
        print(string, new Object[0]);
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W print(String string, Object... args) {
        string = string.replaceAll("\t", tabString);

        if (newline && indentTabs > 0) {
            for (int i = 0; i < indentTabs; i++)
                sb.append(tabString);

            newline = false;
            indentTabs = 0;
        }

        if (args.length > 0) {
            List<Object> originals = Arrays.asList(args);
            List<Object> translated = new ArrayList<Object>();

            for (;;) {
                for (Object arg : originals) {
                    if (arg instanceof Class) {
                        translated.add(ref((Class<?>) arg));
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
    public W println() {

        // Don't add empty lines at the beginning of files
        if (sb.length() > 0) {
            sb.append("\n");
            newline = true;
        }

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W println(int value) {
        print(value);
        println();

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W println(String string) {
        print(string);
        println();

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W println(String string, Object... args) {
        print(string, args);
        println();

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W tab(int tabs) {
        this.indentTabs = tabs;
        return (W) this;
    }

    public int tab() {
        return indentTabs;
    }

    public boolean close() {
        String newContent = beforeClose(sb.toString());

        // [#4626] Don't write empty files
        if (StringUtils.isBlank(newContent))
            return false;

        try {
            // [#3756] Regenerate files only if there is a difference
            String oldContent = null;
            if (file.exists()) {
                RandomAccessFile old = null;

                try {
                    old = new RandomAccessFile(file, "r");
                    byte[] oldBytes = new byte[(int) old.length()];
                    old.readFully(oldBytes);
                    oldContent = new String(oldBytes, encoding());
                }
                finally {
                    if (old != null)
                        old.close();
                }
            }

            if (oldContent == null || !oldContent.equals(newContent)) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), encoding()));

                writer.append(newContent);
                writer.flush();
                writer.close();

            }

            return true;
        }
        catch (IOException e) {
            throw new GeneratorException("Error writing " + file.getAbsolutePath(), e);
        }
    }

    protected String encoding() {
        return encoding != null ? encoding : "UTF-8";
    }

    protected String beforeClose(String string) {
        return string;
    }

    /**
     * Get a reference to a {@link Class}.
     */
    public String ref(Class<?> clazz) {
        return clazz == null ? null : ref(clazz.getName());
    }

    /**
     * Get a reference to a {@link Class}.
     */
    public String ref(String clazzOrId) {
        return clazzOrId == null ? null : ref(Arrays.asList(clazzOrId), 1).get(0);
    }

    /**
     * Get a reference to a list of {@link Class}.
     */
    public String[] ref(String[] clazzOrId) {
        return clazzOrId == null ? new String[0] : ref(Arrays.asList(clazzOrId), 1).toArray(new String[clazzOrId.length]);
    }

    /**
     * Get a reference to a list of {@link Class}.
     * <p>
     * Subtypes may override this to generate import statements.
     */
    public List<String> ref(List<String> clazzOrId) {
        return clazzOrId == null ? Collections.<String>emptyList() : ref(clazzOrId, 1);
    }

    /**
     * Get a reference to a {@link Class}.
     */
    protected String ref(String clazzOrId, int keepSegments) {
        return clazzOrId == null ? null : ref(Arrays.asList(clazzOrId), keepSegments).get(0);
    }

    /**
     * Get a reference to a list of {@link Class}.
     */
    protected String[] ref(String[] clazzOrId, int keepSegments) {
        return clazzOrId == null ? new String[0] : ref(Arrays.asList(clazzOrId), keepSegments).toArray(new String[clazzOrId.length]);
    }

    /**
     * Get a reference to a list of {@link Class}.
     * <p>
     * Subtypes may override this to generate import statements.
     */
    protected List<String> ref(List<String> clazzOrId, int keepSegments) {
        return clazzOrId == null ? Collections.<String>emptyList() : clazzOrId;
    }

    @Override
    public String toString() {
        return "GenerationWriter [" + file + "]";
    }
}
