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
 *
 *
 *
 */
package org.jooq.codegen;

import static java.util.Collections.emptyList;

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
        ("" +
        "\\[\n" +
        "   (?:\\[before=([^]]+)])?\n" +
        "   (?:\\[separator=([^]]+)])?\n" +
        "   (?:\\[after=([^]]+)])?\n" +
        "   (?:\\[(.*)])\n" +
        "]\n" +
        ""),
        Pattern.DOTALL | Pattern.COMMENTS
    );

    private final Files          files;
    private final File           file;
    private final String         encoding;
    private final StringBuilder  sb;
    private int                  indentTabsThisLine;
    private int                  indentTabsAllLines;
    private String               tabString     = "    ";
    private String               newlineString = "\n";
    private int                  printMarginForBlockComment   = 80;
    private boolean              newline       = true;
    private boolean              blockComment  = false;

    protected GeneratorWriter(File file) {
        this(file, null, null);
    }

    protected GeneratorWriter(File file, String encoding) {
        this(file, encoding, null);
    }

    protected GeneratorWriter(File file, Files files) {
        this(file, null, files);
    }

    protected GeneratorWriter(File file, String encoding, Files files) {
        this.files = files == null ? new Files() : files;
        this.file = file;
        this.encoding = encoding;
        this.sb = new StringBuilder();

        this.files.mkdirs(file.getParentFile());
    }

    public String tabString() {
        return tabString;
    }

    public void tabString(String string) {
        this.tabString = string.replace("\\t", "\t").replace("\\s", " ");
    }

    public String newlineString() {
        return newlineString;
    }

    public void newlineString(String string) {
        this.newlineString = string.replace("\\n", "\n").replace("\\r", "\r");
    }

    public int printMarginForBlockComment() {
        return printMarginForBlockComment;
    }

    public void printMarginForBlockComment(int i) {
        this.printMarginForBlockComment = i;
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
        string = string.replace("\n", newlineString).replace("\t", tabString);

        // [#10196] The following auto-indentation logic works well in most cases
        // There are known caveats:
        //
        // - When formatting is done outside of the GeneratorWriter (e.g. currently
        //   for JPA annotations, then it may fail, e.g. by producing an indentation of -1
        // - When a single line is printed in steps, and a step contains such characters,
        //   the character is interpreted erroneously as being semantic.
        if (string.startsWith("}") || string.startsWith("]") || string.startsWith(")"))
            indentTabsAllLines--;
        else if (string.endsWith("*/"))
            blockComment = false;

        if (indentTabsAllLines < 0 && !Boolean.getBoolean("mute-indentation-error"))
            new IllegalStateException("A formatting error has been produced by https://github.com/jOOQ/jOOQ/issues/10196").printStackTrace(System.err);

        int indentTabsThisLine0 = indentTabsThisLine;
        StringBuilder indent = new StringBuilder();

        if (newline) {
            newline = false;

            if (indentTabsThisLine + indentTabsAllLines > 0) {
                for (int i = 0; i < indentTabsThisLine + indentTabsAllLines; i++)
                    indent.append(tabString);

                indentTabsThisLine = 0;
            }

            if (blockComment)
                indent.append(" * ");

            sb.append(indent);
        }

        if (string.endsWith("{") || string.endsWith("[") || string.endsWith("("))
            indentTabsAllLines++;
        else if (string.startsWith("if") || string.startsWith("else") || string.startsWith("for") || string.startsWith("while"))
            indentTabsThisLine = indentTabsThisLine0 + 1;
        else if (string.startsWith("/*"))
            blockComment = true;

        if (args.length > 0) {
            List<Object> originals = Arrays.asList(args);
            List<Object> translated = new ArrayList<>();

            for (;;) {
                for (Object arg : originals) {
                    if (arg instanceof Class) {
                        translated.add(ref((Class<?>) arg));
                    }
                    else if (arg instanceof Object[] || arg instanceof Collection) {
                        if (arg instanceof Collection)
                            arg = ((Collection<?>) arg).toArray();

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

                        if (((Object[]) arg).length > 0)
                            replacement.append(gAfter);

                        string = string.substring(0, start) + replacement + string.substring(end + 2);
                    }
                    else
                        translated.add(arg);
                }

                if (!string.contains("[["))
                    break;

                originals = translated;
                translated = new ArrayList<>();
            }

            appendWrapped(String.format(string, translated.toArray()), indent.toString());
        }
        else
            appendWrapped(string, indent.toString());

        return (W) this;
    }

    private void appendWrapped(String string, String indent) {
        if (blockComment) {
            final int indentLength = indent.length();
            final int stringLength = string.length();
            final int newlineStringLength = newlineString.length();
            int lineLength = indentLength;

            // [#11194] Edge case when the first word of a line is longer than the print margin
            boolean whitespaceEncountered = false;

            stringLoop:
            for (int i = 0; i < stringLength; i++) {
                if (peek(string, i, newlineString)) {
                    sb.append(newlineString).append(indent);
                    lineLength = indentLength;
                    i += newlineStringLength - 1;
                    whitespaceEncountered = false;
                }

                // [#9728] TODO A more sophisticated way to handle Javadoc tags would be interesting.
                else if (i > 0 && peek(string, i, "@deprecated")) {
                    final int l = "@deprecated".length();

                    sb.append(newlineString).append(indent).append("@deprecated");
                    lineLength = indentLength + l;
                    i += l - 1;
                }
                else {
                    int p;

                    for (int j = 0; (p = i + j) < stringLength; j++) {
                        final boolean end = p + 1 >= stringLength;
                        final boolean whitespace = Character.isWhitespace(string.charAt(p));
                        whitespaceEncountered |= whitespace;

                        if (j > 0 && whitespace || end) {
                            lineLength += (end ? j + 1 : j);

                            if (printMarginForBlockComment > 0 && lineLength > printMarginForBlockComment && (!end || whitespaceEncountered)) {
                                sb.append(newlineString).append(indent);
                                lineLength = indentLength + j - 1;
                                i++;
                            }

                            sb.append(string.subSequence(i, p));
                            i = p - 1;

                            if (end) {
                                sb.append(string.charAt(p));
                                break stringLoop;
                            }
                            else
                                continue stringLoop;
                        }
                    }
                }
            }
        }
        else
            sb.append(string.replace(newlineString, newlineString + indent));
    }

    private boolean peek(String string, int i, String peek) {
        for (int j = 0; j < peek.length(); j++)
            if (i + j >= string.length() || string.charAt(i + j) != peek.charAt(j))
                return false;

        return true;
    }

    @SuppressWarnings("unchecked")
    public W printlnIf(boolean condition) {
        if (condition)
            println();

        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W println() {

        // Don't add empty lines at the beginning of files
        if (sb.length() > 0) {
            sb.append(newlineString);
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
    public W indentInc() {
        this.indentTabsAllLines++;
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W indentDec() {
        this.indentTabsAllLines--;
        return (W) this;
    }

    @SuppressWarnings("unchecked")
    public W indent(int tabs) {
        this.indentTabsAllLines = tabs;
        return (W) this;
    }

    public int indent() {
        return indentTabsAllLines;
    }

    @SuppressWarnings("unchecked")
    public W tab(int tabs) {
        this.indentTabsThisLine = tabs;
        return (W) this;
    }

    public int tab() {
        return indentTabsThisLine;
    }

    public static class CloseResult {

        /**
         * Whether closing the file affected any files at all.
         */
        public final boolean affected;

        /**
         * Whether closing the file modified the file.
         */
        public final boolean modified;

        CloseResult(boolean affected, boolean modified) {
            this.affected = affected;
            this.modified = modified;
        }
    }

    public CloseResult close() {
        String newContent = beforeClose(sb.toString());

        // [#4626] Don't write empty files
        if (StringUtils.isBlank(newContent))
            return new CloseResult(false, false);

        try {

            // [#3756] Regenerate files only if there is a difference
            String oldContent = null;
            if (file.exists() && file.length() == newContent.getBytes(encoding()).length) {
                try (RandomAccessFile old = new RandomAccessFile(file, "r")) {
                    byte[] oldBytes = new byte[(int) old.length()];
                    old.readFully(oldBytes);
                    oldContent = new String(oldBytes, encoding());
                }
            }

            if (oldContent == null || !oldContent.equals(newContent)) {

                // [#5892] [#8363] On Windows FAT or NTFS and other case-insensitive
                //                 file systems, we must explicitly replace files whose
                //                 case-sensitive file name has changed
                if (oldContent != null)
                    file.delete();

                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), encoding()));

                writer.append(newContent);
                writer.flush();
                writer.close();

                return new CloseResult(true, true);
            }

            // [#10648] Check all modified files by this run
            else
                return new CloseResult(true, false);
        }
        catch (IOException e) {
            throw new GeneratorException("Error writing " + file.getAbsolutePath(), e);
        }
    }

    protected String encoding() {
        return encoding != null ? encoding : "UTF-8";
    }

    protected String beforeClose(String string) {
        if (indentTabsAllLines > 0 && !Boolean.getBoolean("mute-indentation-error"))
            new IllegalStateException("A formatting error has been produced by https://github.com/jOOQ/jOOQ/issues/10196").printStackTrace(System.err);

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
        return clazzOrId == null ? emptyList() : ref(clazzOrId, 1);
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
        return clazzOrId == null ? emptyList() : clazzOrId;
    }

    public String content() {
        return sb.toString();
    }

    @Override
    public String toString() {
        return "GenerationWriter [" + file + "]";
    }
}
