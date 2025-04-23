/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.impl;

import static java.lang.Boolean.FALSE;
import static org.jooq.conf.SettingsTools.parseLocale;

import java.util.Locale;

import org.jooq.DSLContext;

/**
 * A shared base class for recursive descent parsers.
 *
 * @author Lukas Eder
 */
abstract class AbstractParseContext extends AbstractScope {

    final DSLContext dsl;
    final Locale     locale;
    char[]           chars;
    int              position = 0;
    int              positionBeforeWhitespace;

    public AbstractParseContext(DSLContext dsl, String chars) {
        super(dsl.configuration());

        this.dsl = dsl;
        this.locale = parseLocale(dsl.settings());
        this.chars = chars != null ? chars.toCharArray() : new char[0];
    }

    final boolean hasMore() {
        return position < chars.length;
    }

    final boolean hasMore(int offset) {
        return position + offset < chars.length;
    }

    /* final */ boolean done() {
        return position >= chars.length;
    }

    final String mark() {
        int[] line = line();
        return "[" + line[0] + ":" + line[1] + "] "
              + (position > 50 ? "..." : "")
              + substring(Math.max(0, position - 50), position)
              + "[*]"
              + substring(position, Math.min(chars.length, position + 80))
              + (chars.length > position + 80 ? "..." : "");
    }

    final int[] line() {
        int line = 1;
        int column = 1;

        for (int i = 0; i < position; i++) {
            if (chars[i] == '\r') {
                line++;
                column = 1;

                if (i + 1 < chars.length && chars[i + 1] == '\n')
                    i++;
            }
            else if (chars[i] == '\n') {
                line++;
                column = 1;
            }
            else {
                column++;
            }
        }

        return new int[] { line, column };
    }

    final double parseDouble(String string) {
        try {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException e) {
            throw expected("Double literal");
        }
    }

    final int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e) {
            throw expected("Integer literal");
        }
    }

    final int parseInt(String string, int base) {
        try {
            return Integer.parseInt(string, base);
        }
        catch (NumberFormatException e) {
            throw expected("Integer literal of base: " + base);
        }
    }

    final String substring(int startPosition, int endPosition) {
        startPosition = Math.max(0, startPosition);
        endPosition = Math.min(chars.length, endPosition);
        return new String(chars, startPosition, Math.min(endPosition - startPosition, chars.length - startPosition));
    }

    final ParserException internalError() {
        return exception("Internal Error");
    }

    final ParserException expected(String object) {
        return init(new ParserException(mark(), object + " expected"));
    }

    final ParserException expected(String... objects) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < objects.length; i++)
            if (i == 0)
                sb.append(objects[i]);
            // [#10169] Correct application of Oxford comma 🧐
            else if (i == 1 && objects.length == 2)
                sb.append(" or ").append(objects[i]);
            else if (i == objects.length - 1)
                sb.append(", or ").append(objects[i]);
            else
                sb.append(", ").append(objects[i]);

        return init(new ParserException(mark(), sb.toString() + " expected"));
    }

    public final ParserException exception(String message) {
        return init(new ParserException(mark(), message));
    }

    final ParserException init(ParserException e) {
        int[] line = line();
        return e.position(position).line(line[0]).column(line[1]);
    }

    final char characterUpper() {
        return Character.toUpperCase(character());
    }

    public final char character() {
        return character(position);
    }

    public final char character(int pos) {
        return pos >= 0 && pos < chars.length ? chars[pos] : ' ';
    }

    final char characterNextUpper() {
        return Character.toUpperCase(characterNext());
    }

    final char characterNext() {
        return character(position + 1);
    }

    public final char[] characters() {
        return chars;
    }

    public final int position() {
        return position;
    }

    public final boolean position(int newPosition) {
        position = newPosition;
        return true;
    }

    final boolean positionInc() {
        return positionInc(1);
    }

    final boolean positionInc(int inc) {
        return position(position + inc);
    }

    final boolean parseWhitespaceIf() {
        positionBeforeWhitespace = position();
        position(afterWhitespace(positionBeforeWhitespace));
        return positionBeforeWhitespace != position();
    }

    abstract int afterWhitespace(int p);

    final char upper(char c) {
        return c >= 'a' && c <= 'z' ? (char) (c - ('a' - 'A')) : c;
    }

    final boolean parseTokens(char... tokens) {
        boolean result = parseTokensIf(tokens);

        if (!result)
            throw expected(new String(tokens));

        return result;
    }

    final boolean parseTokensIf(char... tokens) {
        int p = position();

        for (char token : tokens) {
            if (!parseIf(token)) {
                position(p);
                return false;
            }
        }

        return true;
    }

    final boolean peekTokens(char... tokens) {
        int p = position();

        for (char token : tokens) {
            if (!parseIf(token)) {
                position(p);
                return false;
            }
        }

        position(p);
        return true;
    }

    public final boolean parse(String string) {
        boolean result = parseIf(string);

        if (!result)
            throw expected(string);

        return result;
    }

    public final boolean parseIf(String string) {
        return parseIf(string, true);
    }

    final boolean parseIf(String string, boolean skipAfterWhitespace) {
        boolean result = peek(string);

        if (result) {
            positionInc(string.length());

            if (skipAfterWhitespace)
                parseWhitespaceIf();
        }

        return result;
    }

    public final boolean parse(char c) {
        return parse(c, true);
    }

    final boolean parse(char c, boolean skipAfterWhitespace) {
        if (!parseIf(c, skipAfterWhitespace))
            throw expected("Token '" + c + "'");

        return true;
    }

    public final boolean parseIf(char c) {
        return parseIf(c, true);
    }

    final boolean parseIf(char c, boolean skipAfterWhitespace) {
        boolean result = peek(c);

        if (result) {
            positionInc();

            if (skipAfterWhitespace)
                parseWhitespaceIf();
        }

        return result;
    }

    final boolean parseIf(char c, char peek, boolean skipAfterWhitespace) {
        if (character() != c)
            return false;

        if (characterNext() != peek)
            return false;

        positionInc();

        if (skipAfterWhitespace)
            parseWhitespaceIf();

        return true;
    }

    public final boolean peek(char c) {
        return character() == c;
    }

    public final boolean peek(String string) {
        return peek(string, position());
    }

    final boolean peek(String string, int p) {
        int length = string.length();

        if (chars.length < p + length)
            return false;

        for (int i = 0; i < length; i++)
            if (chars[p + i] != string.charAt(i))
                return false;

        return true;
    }
}
