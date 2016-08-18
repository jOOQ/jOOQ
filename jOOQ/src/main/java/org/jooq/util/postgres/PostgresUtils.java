/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util.postgres;

import static java.lang.Integer.toOctalString;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.tools.reflect.Reflect.on;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Converter;
import org.jooq.Record;
import org.jooq.exception.DataTypeException;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;

/**
 * A collection of utilities to cover the Postgres JDBC driver's missing
 * implementations.
 * <p>
 * The Postgres JDBC driver is known to miss out on quite a few JDBC feature
 * implementations. This class should fill those gaps.
 *
 * @author Lukas Eder
 * @author Peter Ertl
 */
public class PostgresUtils {

    private static final String POSTGRESQL_HEX_STRING_PREFIX = "\\x";

    // PGobject parsing state machine
    private static final int    PG_OBJECT_INIT               = 0;
    private static final int    PG_OBJECT_BEFORE_VALUE       = 1;
    private static final int    PG_OBJECT_QUOTED_VALUE       = 2;
    private static final int    PG_OBJECT_UNQUOTED_VALUE     = 3;
    private static final int    PG_OBJECT_AFTER_VALUE        = 4;
    private static final int    PG_OBJECT_END                = 5;

    /**
     * Parse a Postgres-encoded <code>bytea</code> string
     */
    public static byte[] toBytes(final String string) {

        // Hex encoding is the default since Postgres 9.0
        if (string.startsWith(POSTGRESQL_HEX_STRING_PREFIX)) {
            return toBytesFromHexEncoding(string);
        } else {
            return toBytesFromOctalEncoding(string);
        }
    }

    private static byte[] toBytesFromOctalEncoding(final String string) {
        final Reader reader = new StringReader(string);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {
            convertOctalToBytes(reader, bytes);
            return bytes.toByteArray();
        } catch (IOException x) {
            throw new DataTypeException("failed to parse octal hex string: " + x.getMessage(), x);
        }
    }

    private static void convertOctalToBytes(final Reader reader, final OutputStream bytes) throws IOException {
        int ch;
        while ((ch = reader.read()) != -1) {
            if (ch == '\\') {
                ch = reader.read();
                if (ch == -1) {
                    throw new DataTypeException("unexpected end of stream after initial backslash");
                }
                if (ch == '\\') {
                    bytes.write('\\');
                    continue;
                }
                int val = octalValue(ch);
                ch = reader.read();
                if (ch == -1) {
                    throw new DataTypeException("unexpected end of octal value");
                }
                val <<= 3;
                val += octalValue(ch);
                ch = reader.read();
                if (ch == -1) {
                    throw new DataTypeException("unexpected end of octal value");
                }
                val <<= 3;
                val += octalValue(ch);
                bytes.write(val);
            } else {
                bytes.write(ch);
            }
        }
    }

    private static byte[] toBytesFromHexEncoding(String string) {
        String hex = string.substring(POSTGRESQL_HEX_STRING_PREFIX.length());

        final StringReader input = new StringReader(hex);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream(hex.length() / 2);
        int hexDigit;
        int byteValue;

        try {
            while ((hexDigit = input.read()) != -1) {
                byteValue = (hexValue(hexDigit) << 4);
                if ((hexDigit = input.read()) == -1) {
                    break;
                }
                byteValue += hexValue(hexDigit);
                bytes.write(byteValue);
            }
        }

        // should never happen for a string reader
        catch (IOException e) {
            throw new DataTypeException("Error while decoding hex string", e);
        }

        input.close();
        return bytes.toByteArray();
    }

    /**
     * Get the hex value of a <code>char</code> digit
     */
    private static int hexValue(final int hexDigit) {
        if (hexDigit >= '0' && hexDigit <= '9') {
            return hexDigit - '0';
        }
        else if (hexDigit >= 'a' && hexDigit <= 'f') {
            return hexDigit - 'a' + 10;
        }
        else if (hexDigit >= 'A' && hexDigit <= 'F') {
            return hexDigit - 'A' + 10;
        }

        throw new DataTypeException("unknown postgresql character format for hexValue: " + hexDigit);
    }

    /**
     * Get the octal value of a {@code char} digit
     */
    private static int octalValue(final int octalDigit) {
        if (octalDigit < '0' || octalDigit > '7') {
            throw new DataTypeException("unknown postgresql character format for octalValue: " + octalDigit);
        }

        return octalDigit - '0';
    }


    /**
     * Convert a jOOQ <code>DAY TO SECOND</code> interval to a Postgres representation
     */
    public static Object toPGInterval(DayToSecond interval) {
        return on("org.postgresql.util.PGInterval").create(0, 0,
            interval.getSign() * interval.getDays(),
            interval.getSign() * interval.getHours(),
            interval.getSign() * interval.getMinutes(),
            interval.getSign() * interval.getSeconds() +
            interval.getSign() * interval.getNano() / 1000000000.0).get();
    }

    /**
     * Convert a jOOQ <code>YEAR TO MONTH</code> interval to a Postgres representation
     */
    public static Object toPGInterval(YearToMonth interval) {
        return on("org.postgresql.util.PGInterval").create(
            interval.getSign() * interval.getYears(),
            interval.getSign() * interval.getMonths(),
            0, 0, 0, 0.0).get();
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>DAY TO SECOND</code> interval
     */
    public static DayToSecond toDayToSecond(Object pgInterval) {
        boolean negative = pgInterval.toString().contains("-");

        Reflect i = on(pgInterval);
        if (negative) {
            i.call("scale", -1);
        }

        Double seconds = i.call("getSeconds").<Double>get();
        DayToSecond result = new DayToSecond(
            i.call("getDays").<Integer>get(),
            i.call("getHours").<Integer>get(),
            i.call("getMinutes").<Integer>get(),
            seconds.intValue(),
            (int) (1000000000 * (seconds - seconds.intValue())));

        if (negative) {
            result = result.neg();
        }

        return result;
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>YEAR TO MONTH</code> interval
     */
    public static YearToMonth toYearToMonth(Object pgInterval) {
        boolean negative = pgInterval.toString().contains("-");

        Reflect i = on(pgInterval);
        if (negative) {
            i.call("scale", -1);
        }

        YearToMonth result = new YearToMonth(
            i.call("getYears").<Integer>get(),
            i.call("getMonths").<Integer>get());

        if (negative) {
            result = result.neg();
        }

        return result;
    }

    /**
     * Tokenize a PGObject input string
     */
    @SuppressWarnings("null")
    public static List<String> toPGObject(String input) {
        List<String> values = new ArrayList<String>();
        int i = 0;
        int state = PG_OBJECT_INIT;
        StringBuilder sb = null;

        while (i < input.length()) {
            char c = input.charAt(i);

            switch (state) {
                // Initial state
                case PG_OBJECT_INIT:

                    // Consume the opening parenthesis
                    if (c == '(') {
                        state = PG_OBJECT_BEFORE_VALUE;
                    }

                    break;

                // Before a new value
                case PG_OBJECT_BEFORE_VALUE:
                    sb = new StringBuilder();

                    // Consume "empty"
                    if (c == ',') {
                        values.add(null);
                        state = PG_OBJECT_BEFORE_VALUE;
                    }

                    // Consume "empty"
                    else if (c == ')') {
                        values.add(null);
                        state = PG_OBJECT_END;
                    }

                    // Consume the opening quote
                    else if (c == '"') {
                        state = PG_OBJECT_QUOTED_VALUE;
                    }

                    // Consume "null"
                    else if ((c == 'n' || c == 'N') && (i + 4 < input.length())
                        && input.substring(i, i + 4).equalsIgnoreCase("null")) {
                        values.add(null);
                        i += 3;
                        state = PG_OBJECT_AFTER_VALUE;
                    }

                    // Consume a character
                    else {
                        sb.append(c);
                        state = PG_OBJECT_UNQUOTED_VALUE;
                    }

                    break;

                // A "value" is being created
                case PG_OBJECT_QUOTED_VALUE:

                    // Consume a quote
                    if (c == '"') {

                        // Consume an escaped quote
                        if (input.charAt(i + 1) == '"') {
                            sb.append(c);
                            i++;
                        }

                        // Consume the closing quote
                        else {
                            values.add(sb.toString());
                            state = PG_OBJECT_AFTER_VALUE;
                        }
                    }

                    // Consume a backslash
                    else if (c == '\\') {

                        // Consume an escaped backslash
                        if (input.charAt(i + 1) == '\\') {
                            sb.append(c);
                            i++;
                        }

                        // Consume an "illegal" backslash (?)
                        else {
                            sb.append(c);
                        }
                    }

                    // Consume any other character
                    else {
                        sb.append(c);
                    }

                    break;

                // A value is being created
                case PG_OBJECT_UNQUOTED_VALUE:

                    // Consume the closing parenthesis
                    if (c == ')') {
                        values.add(sb.toString());
                        state = PG_OBJECT_END;
                    }

                    // Consume the value separator
                    else if (c == ',') {
                        values.add(sb.toString());
                        state = PG_OBJECT_BEFORE_VALUE;
                    }

                    // Consume any other character
                    else {
                        sb.append(c);
                    }

                    break;

                // A value was just added
                case PG_OBJECT_AFTER_VALUE:

                    // Consume the closing parenthesis
                    if (c == ')') {
                        state = PG_OBJECT_END;
                    }

                    // Consume the value separator
                    else if (c == ',') {
                        state = PG_OBJECT_BEFORE_VALUE;
                    }

                    break;
            }

            // Consume next character
            i++;
        }

        return values;
    }

    /**
     * Create a Postgres string representation of an array
     */
    public static String toPGArrayString(Object[] value) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        String separator = "";
        for (Object o : value) {
            sb.append(separator);

            // [#753] null must be set as a literal
            if (o == null)
                sb.append(o);
            else if (o instanceof byte[])
                sb.append(toPGString((byte[]) o));
            else
                sb.append("\"")
                  .append(toPGString(o).replace("\\", "\\\\").replace("\"", "\\\""))
                  .append("\"");

            separator = ",";
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Create a PostgreSQL string representation of any object.
     */
    public static String toPGString(Object o) {
        if (o instanceof byte[]) {
            return toPGString((byte[]) o);
        }
        else if (o instanceof Object[]) {
            return toPGArrayString((Object[]) o);
        }
        else if (o instanceof Record) {
            return toPGString((Record) o);
        }
        else {
            return "" + o;
        }
    }

    /**
     * Create a PostgreSQL string representation of a record.
     */
    public static String toPGString(Record r) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");

        String separator = "";
        for (int i = 0; i < r.size(); i++) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Object a = ((Converter) r.field(i).getConverter()).to(r.get(i));
            sb.append(separator);

            // [#753] null must not be set as a literal
            if (a != null) {
                if (a instanceof byte[])
                    sb.append(toPGString((byte[]) a));
                else
                    sb.append("\"")
                      .append(toPGString(a).replace("\\", "\\\\").replace("\"", "\\\""))
                      .append("\"");
            }

            separator = ",";
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * Create a PostgreSQL string representation of a binary.
     */
    public static String toPGString(byte[] binary) {
        StringBuilder sb = new StringBuilder();

        for (byte b : binary) {

            // [#3924] Beware of signed vs unsigned bytes!
            sb.append("\\\\");
            sb.append(leftPad(toOctalString(b & 0x000000ff), 3, '0'));
        }

        return sb.toString();
    }
}
