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

import org.jooq.DSLContext;

/**
 * A JSON parser for jOOQ's internal purposes.
 *
 * @author Lukas Eder
 */
final class JSONParser extends AbstractParseContext {

    final JSONContentHandler handler;

    JSONParser(DSLContext dsl, String chars, JSONContentHandler handler) {
        super(dsl, chars);

        this.handler = handler;
        parseWhitespaceIf();
    }

    final void parse() {
        parseValue();

        if (!done())
            throw exception("Unexpected content");
    }

    private final void parseValue() {
        if (!parseValueIf())
            throw expected("Value");
    }

    private final boolean parseValueIf() {
        switch (character()) {
            case '{':
                parseObject();
                return true;

            case '[':
                parseArray();
                return true;

            case 'n':
                parseNull();
                return true;

            case 't':
                parseTrue();
                return true;

            case 'f':
                parseFalse();
                return true;

            case '"':
                parseString();
                return true;

            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                parseNumber();
                return true;

            default:
                return false;
        }
    }

    private final void parseNumber() {
        handler.valueNumber(parseNumberLiteral());
    }

    private final String parseNumberLiteral() {
        int p = position();
        int i;

        numberLoop:
        for (i = position(); i < chars.length; i++) {

            // Actual number format will be parsed and checked later by content handler.
            switch (character(i)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                case '-':
                case '+':
                case 'E':
                case 'e': {
                    break;
                }

                default:
                    break numberLoop;
            }
        }

        position(i);
        parseWhitespaceIf();
        return new String(chars, p, i - p);
    }

    private final void parseString() {
        handler.valueString(parseStringLiteral());
    }

    private final String parseStringLiteral() {
        parse('"', false);

        StringBuilder sb = new StringBuilder();

        for (int i = position(); i < chars.length; i++) {
            char c1 = character(i);

            switch (c1) {
                case '\\': {

                    i++;
                    char c2 = character(i);
                    switch (c2) {

                        // Escaped whitespace characters
                        case 'b':
                            c1 = '\b';
                            break;
                        case 'n':
                            c1 = '\n';
                            break;
                        case 't':
                            c1 = '\t';
                            break;
                        case 'r':
                            c1 = '\r';
                            break;
                        case 'f':
                            c1 = '\f';
                            break;

                        // Unicode character value UTF-16
                        case 'u':
                            c1 = (char) parseInt(substring(i + 1, i + 5), 16);
                            i += 4;
                            break;

                        default:
                            c1 = c2;
                            break;
                    }

                    break;
                }

                case '"': {
                    position(i + 1);
                    parseWhitespaceIf();
                    return sb.toString();
                }
            }

            sb.append(c1);
        }

        throw exception("String literal not terminated");
    }

    private final void parseFalse() {
        parse("false");
        handler.valueFalse();
    }

    private final void parseTrue() {
        parse("true");
        handler.valueTrue();
    }

    private final void parseNull() {
        parse("null");
        handler.valueNull();
    }

    private final void parseObject() {
        parse('{');
        handler.startObject();

        if (!parseIf('}')) {
            do {
                handler.property(parseStringLiteral());
                parse(':');
                parseValue();
            }
            while (parseIf(','));

            parse('}');
        }

        handler.endObject();
    }

    private final void parseArray() {
        parse('[');
        handler.startArray();

        while (parseValueIf() && parseIf(','))
            ;

        parse(']');
        handler.endArray();
    }

    @Override
    final int afterWhitespace(int p) {
        loop:
        for (int i = p; i < chars.length; i++) {
            switch (chars[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    p = i + 1;
                    continue loop;

                default:
                    p = i;
                    break loop;
            }
        }

        return p;
    }

    @Override
    public String toString() {
        return mark();
    }
}
