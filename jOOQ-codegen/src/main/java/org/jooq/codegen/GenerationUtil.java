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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static org.jooq.codegen.Language.JAVA;
import static org.jooq.codegen.Language.KOTLIN;
import static org.jooq.codegen.Language.SCALA;
import static org.jooq.codegen.GenerationUtil.ExpressionType.CONSTRUCTOR_REFERENCE;
import static org.jooq.codegen.GenerationUtil.ExpressionType.EXPRESSION;
import static org.jooq.impl.DSL.name;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.h2.H2DataType;

/**
 * @author Lukas Eder
 * @author Eric Peters
 */
class GenerationUtil {

    static final Pattern       TYPE_REFERENCE_PATTERN     = Pattern.compile("^((?:[\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*)((?:<.*>|\\[.*])*)$");
    static final Pattern       PLAIN_GENERIC_TYPE_PATTERN = Pattern.compile("[<\\[]((?:[\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*)[>\\]]");
    static final Pattern       UNDERSCORE_PATTERN         = Pattern.compile("_+");




    private static final Set<String> JAVA_KEYWORDS = unmodifiableSet(new HashSet<>(asList(
        "abstract",
        "assert",
        "boolean",
        "break",
        "byte",
        "case",
        "catch",
        "char",
        "class",
        "const",
        "continue",
        "default",
        "double",
        "do",
        "else",
        "enum",
        "extends",
        "false",
        "final",
        "finally",
        "float",
        "for",
        "goto",
        "if",
        "implements",
        "import",
        "instanceof",
        "interface",
        "int",
        "long",
        "native",
        "new",
        "null",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "strictfp",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "true",
        "try",
        "void",
        "volatile",
        "while")));

    private static final Set<String> SCALA_KEYWORDS = unmodifiableSet(new HashSet<>(asList(
        "abstract",
        "case",
        "catch",
        "class",
        "def",
        "do",
        "else",
        "extends",
        "false",
        "final",
        "finally",
        "for",
        "forSome",
        "if",
        "implicit",
        "import",
        "lazy",
        "match",
        "new",
        "null",
        "object",
        "override",
        "package",
        "private",
        "protected",
        "return",
        "sealed",
        "super",
        "this",
        "throw",
        "trait",
        "try",
        "true",
        "type",
        "val",
        "var",
        "while",
        "with",
        "yield"/*,
        "_",
        ":",
        "=",
        "=>",
        "<-",
        "<:",
        "<%",
        ">:",
        "#",
        "@"*/
    )));

    private static final Set<String> KOTLIN_KEYWORDS = unmodifiableSet(new HashSet<>(asList(

        // Hard keywords https://kotlinlang.org/docs/reference/keyword-reference.html
        "as",
        "break",
        "class",
        "continue",
        "do",
        "else",
        "false",
        "for",
        "fun",
        "if",
        "in",
        "interface",
        "is",
        "null",
        "object",
        "package",
        "return",
        "super",
        "this",
        "throw",
        "true",
        "try",
        "typealias",
        "typeof",
        "val",
        "var",
        "when",
        "while",
        "yield",

        // Soft keywords (most seem not to produce conflicts in generated code)
        // "catch",
        "constructor",
        // "delegate",
        // "dynamic",
        // "field",
        // "file",
        // "finally",
        // "get",
        // "import",
        "init",
        // "param",
        // "property",
        // "receiver",
        // "set",
        // "setparam",
        // "where",

        // Modifier keywords
        "actual",
        "abstract",
        "annotation",
        "companion",
        "const",
        "crossinline",
        "data",
        "enum",
        "expect",
        "external",
        "final",
        "infix",
        "inline",
        "inner",
        "internal",
        "lateinit",
        "noinline",
        "open",
        "operator",
        "out",
        "override",
        "private",
        "protected",
        "public",
        "reified",
        "sealed",
        "suspend",
        "tailrec",
        "vararg"
    )));

    private static final Set<Character> SCALA_WHITESPACE = unmodifiableSet(new HashSet<>(asList(
        (char)0x0020,
        (char)0x0009,
        (char)0x000D,
        (char)0x000A
    )));

    private static final Set<Character> SCALA_PARENTHESES = unmodifiableSet(new HashSet<>(asList(
        '(',
        ')',
        '[',
        ']',
        '{',
        '}'
    )));

    private static final Set<Character> SCALA_DELIMITER = unmodifiableSet(new HashSet<>(asList(
        '`',
        '\'',
        '"',
        '.',
        ';',
        ','
    )));

    private static final Set<String> WINDOWS_FORBIDDEN = unmodifiableSet(new HashSet<>(asList(
        "CON",
        "PRN",
        "AUX",
        "CLOCK$",
        "NUL",
        "COM1",
        "COM2",
        "COM3",
        "COM4",
        "COM5",
        "COM6",
        "COM7",
        "COM8",
        "COM9",
        "LPT1",
        "LPT2",
        "LPT3",
        "LPT4",
        "LPT5",
        "LPT6",
        "LPT7",
        "LPT8",
        "LPT9"
    )));

    /**
     * Check if a character is a valid "scala operator".
     * <p>
     * See <a href=
     * "http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html">http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html</a>
     */
    private static Boolean isScalaOperator(char c) {
        return (c >= 0x0020 && c <= 0x007F && !Character.isLetter(c) && !Character.isDigit(c) && !SCALA_DELIMITER.contains(c) && !SCALA_PARENTHESES.contains(c) && !SCALA_WHITESPACE.contains(c)) || Character.getType(c) == Character.MATH_SYMBOL /* Sm */ || Character.getType(c) == Character.OTHER_SYMBOL /* So */;
    }

    /**
     * Check if a character is a valid "scala letter".
     * <p>
     * See <a href=
     * "http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html">http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html</a>
     */
    private static Boolean isScalaLetter(char c) {
        return Character.isLetter(c) || c == '_' || c == '$';
    }

    /**
     * Check if a character can be used in a scala identifier.
     * <p>
     * See <a href=
     * "http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html">http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html</a>
     */
    private static Boolean isScalaIdentifierStart(char c) {
        return isScalaLetter(c);
    }

    /**
     * Check if a character can be used in a scala identifier.
     * <p>
     * See <a href=
     * "http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html">http://www.scala-lang.org/files/archive/spec/2.11/01-lexical-syntax.html</a>
     */
    private static Boolean isScalaIdentifierPart(char c) {
        return isScalaIdentifierStart(c) || Character.isDigit(c);
    }

    /**
     * Check if a character can be used in a kotlin identifier.
     * <p>
     * See <a href=
     * "https://kotlinlang.org/spec/syntax-and-grammar.html#grammar-rule-QuotedSymbol">https://kotlinlang.org/spec/syntax-and-grammar.html#grammar-rule-QuotedSymbol</a>
     */
    private static boolean isKotlinIdentifierPart(char c) {
        return c != '\r'
            && c != '\n'
            && c != '`'
            && c != '('
            && c != ')'
            && c != '{'
            && c != '}'
            && c != '['
            && c != ']'
            && c != '.';
    }

    /**
     * Take a name and escape it if it is a Windows forbidden name like
     * <code>CON</code> or <code>AUX</code>.
     *
     * @see <a href="https://github.com/jOOQ/jOOQ/issues/5596">#5596</a>
     */
    public static String escapeWindowsForbiddenNames(String name) {
        return name == null
             ? null
             : WINDOWS_FORBIDDEN.contains(name.toUpperCase())
             ? name + "_"
             : name;
    }

    /**
     * Take a literal (e.g. database column) and make it a Java identifier to be
     * used without case-change as an enum identifier
     * <p>
     * [#959] These literals are escaped if they collide with reserved words.
     * This implementation is meant as a fix for [#959]. These types of
     * collisions have to be generally reviewed again, when allowing for more
     * control over generated source code, as of [#408][#911]
     * <p>
     *
     */
    public static String convertToIdentifier(String literal, Language language) {
        if (language == JAVA && JAVA_KEYWORDS.contains(literal))
            return literal + "_";
        if (language == SCALA && SCALA_KEYWORDS.contains(literal))
            return "`" + literal + "`";
        if (language == KOTLIN && KOTLIN_KEYWORDS.contains(literal))
            return "`" + literal + "`";

        // [#4703] If an identifier consists only of underscores add one more. _ -> __, __ -> ___
        if (UNDERSCORE_PATTERN.matcher(literal).matches())
            return literal + "_";

        StringBuilder sb = new StringBuilder();

        if ("".equals(literal))
            if (language == SCALA)
                return "`_`";
            else if (language == KOTLIN)
                return "`_`";
            else
                return "_";

        for (int i = 0; i < literal.length(); i++) {
            char c = literal.charAt(i);

            // [#5424] Scala setters, by convention, end in "property_=", where "=" is an operator and "_" precedes it
            if (language == SCALA && i == literal.length() - 1 && literal.length() >= 2 && literal.charAt(i - 1) == '_' && isScalaOperator(c))
                sb.append(c);
            else if (language == SCALA && !isScalaIdentifierPart(c))
                sb.append(escape(c));
            else if (language == JAVA && !Character.isJavaIdentifierPart(c))
                sb.append(escape(c));
            else if (language == SCALA && i == 0 && !isScalaIdentifierStart(c))
                sb.append("_").append(c);
            else if (language == JAVA && i == 0 && !Character.isJavaIdentifierStart(c))
                sb.append("_").append(c);

            // [#10837] Some characters are not allowed, even in kotlin quoted identifiers
            else if (language == KOTLIN && !isKotlinIdentifierPart(c))
                sb.append(escape(c));

            // TODO: Should we do this for Scala as well?
            else if (language == KOTLIN && !Character.isJavaIdentifierPart(c))
                return "`" + literal + "`";
            else if (language == KOTLIN && i == 0 && !Character.isJavaIdentifierStart(c))
                return "`" + literal + "`";

            // [#10867] The $ character is not allowed in Kotlin unquoted identifiers
            else if (language == KOTLIN && c == '$')
                return "`" + literal + "`";
            else
                sb.append(c);
        }

        return sb.toString();
    }

    /**
     * @deprecated - Use {@link #convertToIdentifier(String, Language)} instead.
     */
    @Deprecated
    public static String convertToJavaIdentifier(String literal) {
        return convertToIdentifier(literal, Language.JAVA);
    }

    private static String escape(char c) {
        if (c == ' ' || c == '-' || c == '.')
            return "_";
        else
            return "_" + Integer.toHexString(c);
    }

    /**
     * Take a qualified Java type and make it a simple type
     *
     * @see Class#getSimpleName()
     */
    static String getSimpleJavaType(String qualifiedJavaType) {
        if (qualifiedJavaType == null)
            return null;

        return qualifiedJavaType.replaceAll(".*\\.", "");
    }

    /**
     * Gets the base type for an array type, depending on the RDBMS dialect
     */
    static Name getArrayBaseType(SQLDialect dialect, String t, Name u) {

        // [#4388] TODO: Improve array handling
        switch (dialect.family()) {


















            case POSTGRES: {

                // The convention is to prepend a "_" to a type to get an array type
                if (u != null && u.last().startsWith("_")) {
                    String[] name = u.getName();
                    name[name.length - 1] = name[name.length - 1].substring(1);
                    return name(name);
                }

                // But there are also arrays with a "vector" suffix
                else
                    return u;
            }

            case H2:
                return name(H2DataType.OTHER.getTypeName());


            case HSQLDB:
            default: {

                // In HSQLDB 2.2.5, there has been an incompatible INFORMATION_SCHEMA change around the
                // ELEMENT_TYPES view. Arrays are now described much more explicitly
                if ("ARRAY".equalsIgnoreCase(t))
                    return name("OTHER");

                // This is for backwards compatibility
                else
                    return name(t.replaceFirst("(?i: ARRAY)", ""));
            }
        }
    }

    static ExpressionType expressionType(String expression) {
        if (TYPE_REFERENCE_PATTERN.matcher(expression).matches())
            return CONSTRUCTOR_REFERENCE;
        else
            return EXPRESSION;
    }

    enum ExpressionType {
        CONSTRUCTOR_REFERENCE,
        EXPRESSION
    }
}
