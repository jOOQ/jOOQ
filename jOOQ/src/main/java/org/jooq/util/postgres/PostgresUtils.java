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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.util.postgres;

import static java.lang.Integer.toOctalString;
import static org.jooq.impl.Internal.converterContext;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.util.postgres.PostgresUtils.PGState.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.ContextConverter;
import org.jooq.EnumType;
import org.jooq.Record;
import org.jooq.exception.DataTypeException;
import org.jooq.tools.StringUtils;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

import org.jetbrains.annotations.ApiStatus.Internal;


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
@Internal
public class PostgresUtils {

    private static final String POSTGRESQL_HEX_STRING_PREFIX  = "\\x";

    // PGobject parsing state machine
    enum PGState {
        PG_OBJECT_INIT,
        PG_OBJECT_BEFORE_VALUE,
        PG_OBJECT_QUOTED_VALUE,
        PG_OBJECT_UNQUOTED_VALUE,
        PG_OBJECT_NESTED_VALUE,
        PG_OBJECT_NESTED_QUOTED_VALUE,
        PG_OBJECT_AFTER_VALUE,
        PG_OBJECT_END
    }

    /**
     * Parse a Postgres-encoded <code>bytea</code> string
     */
    public static byte[] toBytes(final String string) {

        // Hex encoding is the default since Postgres 9.0
        if (string.startsWith(POSTGRESQL_HEX_STRING_PREFIX))
            return toBytesFromHexEncoding(string);
        else
            return toBytesFromOctalEncoding(string);
    }

    private static byte[] toBytesFromOctalEncoding(final String string) {
        final Reader reader = new StringReader(string);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {
            convertOctalToBytes(reader, bytes);
            return bytes.toByteArray();
        }
        catch (IOException x) {
            throw new DataTypeException("failed to parse octal hex string: " + x.getMessage(), x);
        }
    }

    private static void convertOctalToBytes(final Reader reader, final OutputStream bytes) throws IOException {
        int ch;
        while ((ch = reader.read()) != -1) {
            if (ch == '\\') {
                ch = reader.read();

                if (ch == -1)
                    throw new DataTypeException("unexpected end of stream after initial backslash");
                if (ch == '\\') {
                    bytes.write('\\');
                    continue;
                }

                int val = octalValue(ch);
                ch = reader.read();

                if (ch == -1)
                    throw new DataTypeException("unexpected end of octal value");

                val <<= 3;
                val += octalValue(ch);
                ch = reader.read();

                if (ch == -1)
                    throw new DataTypeException("unexpected end of octal value");

                val <<= 3;
                val += octalValue(ch);
                bytes.write(val);
            }
            else {
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

                if ((hexDigit = input.read()) == -1)
                    break;

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
        if (hexDigit >= '0' && hexDigit <= '9')
            return hexDigit - '0';
        else if (hexDigit >= 'a' && hexDigit <= 'f')
            return hexDigit - 'a' + 10;
        else if (hexDigit >= 'A' && hexDigit <= 'F')
            return hexDigit - 'A' + 10;

        throw new DataTypeException("unknown postgresql character format for hexValue: " + hexDigit);
    }

    /**
     * Get the octal value of a {@code char} digit
     */
    private static int octalValue(final int octalDigit) {
        if (octalDigit < '0' || octalDigit > '7')
            throw new DataTypeException("unknown postgresql character format for octalValue: " + octalDigit);

        return octalDigit - '0';
    }


    /**
     * Convert a jOOQ <code>DAY TO SECOND</code> interval to a Postgres representation
     */
    public static PGInterval toPGInterval(DayToSecond interval) {
        return new PGInterval(0, 0,
            interval.getSign() * interval.getDays(),
            interval.getSign() * interval.getHours(),
            interval.getSign() * interval.getMinutes(),
            interval.getSign() * interval.getSeconds() +
            interval.getSign() * interval.getNano() / 1000000000.0
        );
    }

    /**
     * Convert a jOOQ <code>YEAR TO SECOND</code> interval to a Postgres representation
     */
    public static PGInterval toPGInterval(YearToSecond interval) {
        return new PGInterval(
            interval.getSign() * interval.getYears(),
            interval.getSign() * interval.getMonths(),
            interval.getSign() * interval.getDays(),
            interval.getSign() * interval.getHours(),
            interval.getSign() * interval.getMinutes(),
            interval.getSign() * interval.getSeconds() +
            interval.getSign() * interval.getNano() / 1000000000.0
        );
    }

    /**
     * Convert a jOOQ <code>YEAR TO MONTH</code> interval to a Postgres representation
     */
    public static PGInterval toPGInterval(YearToMonth interval) {
        return new PGInterval(
            interval.getSign() * interval.getYears(),
            interval.getSign() * interval.getMonths(),
            0, 0, 0, 0.0
        );
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>DAY TO SECOND</code> interval
     */
    public static DayToSecond toDayToSecond(Object pgInterval) {
        if (pgInterval == null)
            return null;
        else if (pgInterval instanceof PGInterval i)
            return toDayToSecond(i);
        else
            return toDayToSecond(new PGInterval(pgInterval.toString()));
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>DAY TO SECOND</code> interval
     */
    public static DayToSecond toDayToSecond(PGInterval pgInterval) {
        boolean negative = pgInterval.toString().contains("-");

        if (negative)
            pgInterval.scale(-1);

        Double seconds = pgInterval.getSeconds();
        DayToSecond result = new DayToSecond(
            pgInterval.getDays(),
            pgInterval.getHours(),
            pgInterval.getMinutes(),
            seconds.intValue(),
            (int) (1000000000 * (seconds - seconds.intValue()))
        );

        if (negative)
            result = result.neg();

        return result;
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>YEAR TO MONTH</code> interval
     */
    public static YearToMonth toYearToMonth(Object pgInterval) {
        if (pgInterval == null)
            return null;
        else if (pgInterval instanceof PGInterval i)
            return toYearToMonth(i);
        else
            return toYearToMonth(new PGInterval(pgInterval.toString()));
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>YEAR TO MONTH</code> interval
     */
    public static YearToMonth toYearToMonth(PGInterval pgInterval) {
        boolean negative = pgInterval.toString().contains("-");

        if (negative)
            pgInterval.scale(-1);

        YearToMonth result = new YearToMonth(pgInterval.getYears(), pgInterval.getMonths());

        if (negative)
            result = result.neg();

        return result;
    }

    /**
     * Convert a Postgres interval to a jOOQ <code>YEAR TO SECOND</code> interval
     */
    public static YearToSecond toYearToSecond(Object pgInterval) {
        return new YearToSecond(toYearToMonth(pgInterval), toDayToSecond(pgInterval));
    }

    /**
     * Tokenize a PGObject input string.
     */
    public static List<String> toPGArray(String input) {
        if ("{}".equals(input))
            return Collections.emptyList();

        return toPGObjectOrArray(input, '{', '}');
    }

    /**
     * Tokenize a PGObject input string.
     */
    public static List<String> toPGObject(String input) {
        return toPGObjectOrArray(input, '(', ')');
    }

    /**
     * Tokenize a PGObject input string.
     */
    @SuppressWarnings("null")
    private static List<String> toPGObjectOrArray(String input, char open, char close) {
        List<String> values = new ArrayList<String>();
        int i = 0;
        PGState state = PG_OBJECT_INIT;
        int nestLevel = 0;
        StringBuilder sb = null;

        while (i < input.length()) {
            char c = input.charAt(i);

            switch (state) {
                case PG_OBJECT_INIT:

                    if (c == open)
                        state = PG_OBJECT_BEFORE_VALUE;

                    break;

                case PG_OBJECT_BEFORE_VALUE:
                    sb = new StringBuilder();

                    if (c == ',') {
                        values.add(null);
                        state = PG_OBJECT_BEFORE_VALUE;
                    }
                    else if (c == close) {
                        values.add(null);
                        state = PG_OBJECT_END;
                    }
                    else if (c == '"') {
                        state = PG_OBJECT_QUOTED_VALUE;
                    }

                    // [#13169] Consume "null", if this is an array literal
                    else if ((c == 'n' || c == 'N') && (i + 4 < input.length() && open == '{')
                        && input.substring(i, i + 4).equalsIgnoreCase("null")) {
                        values.add(null);
                        i += 3;
                        state = PG_OBJECT_AFTER_VALUE;
                    }

                    // [#252] Consume nested array
                    else if (c == open) {
                        sb.append(c);
                        state = PG_OBJECT_NESTED_VALUE;
                        nestLevel++;
                    }
                    else {
                        sb.append(c);
                        state = PG_OBJECT_UNQUOTED_VALUE;
                    }

                    break;

                case PG_OBJECT_QUOTED_VALUE:
                    if (c == '"') {
                        if (input.charAt(i + 1) == '"') {
                            sb.append(c);
                            i++;
                        }
                        else {
                            values.add(sb.toString());
                            state = PG_OBJECT_AFTER_VALUE;
                        }
                    }
                    else if (c == '\\') {
                        char n = input.charAt(i + 1);

                        // [#10467] Consume an escaped backslash or quote
                        if (n == '\\' || n == '"') {
                            sb.append(n);
                            i++;
                        }

                        // Consume an "illegal" backslash (?)
                        else
                            sb.append(c);
                    }
                    else
                        sb.append(c);

                    break;

                case PG_OBJECT_UNQUOTED_VALUE:
                    if (c == close) {
                        values.add(sb.toString());
                        state = PG_OBJECT_END;
                    }
                    else if (c == ',') {
                        values.add(sb.toString());
                        state = PG_OBJECT_BEFORE_VALUE;
                    }
                    else
                        sb.append(c);

                    break;

                case PG_OBJECT_NESTED_VALUE:
                    if (c == close) {
                        nestLevel--;
                        sb.append(c);

                        if (nestLevel == 0) {
                            values.add(sb.toString());
                            state = PG_OBJECT_AFTER_VALUE;
                        }
                    }
                    else if (c == open) {
                        nestLevel++;
                        sb.append(c);
                    }
                    else if (c == '"') {
                        state = PG_OBJECT_NESTED_QUOTED_VALUE;
                        sb.append(c);
                    }
                    else {
                        sb.append(c);
                    }

                    break;

                case PG_OBJECT_NESTED_QUOTED_VALUE:
                    if (c == '"') {
                        if (input.charAt(i + 1) == '"') {
                            sb.append(c);
                            sb.append(c);
                            i++;
                        }
                        else {
                            sb.append(c);
                            state = PG_OBJECT_NESTED_VALUE;
                        }
                    }
                    else if (c == '\\') {
                        char n = input.charAt(i + 1);

                        // [#10467] Consume an escaped backslash or quote
                        if (n == '\\' || n == '"') {
                            sb.append(c);
                            sb.append(n);
                            i++;
                        }

                        // Consume an "illegal" backslash (?)
                        else
                            sb.append(c);
                    }
                    else
                        sb.append(c);

                    break;

                case PG_OBJECT_AFTER_VALUE:
                    if (c == close)
                        state = PG_OBJECT_END;
                    else if (c == ',')
                        state = PG_OBJECT_BEFORE_VALUE;

                    break;
            }

            i++;
        }

        return values;
    }

    /**
     * Create a Postgres string representation of an array
     */
    public static String toPGArrayString(Object[] value) {
        return toPGArrayString0(value, new StringBuilder()).toString();
    }

    private static StringBuilder toPGArrayString0(Object[] value, StringBuilder sb) {
        sb.append("{");

        String separator = "";
        for (Object o : value) {
            sb.append(separator);

            // [#753] null must be set as a literal
            if (o == null)
                sb.append(o);

            // [#6359] Avoid quotes for numbers
            else if (o instanceof Number)
                sb.append(toPGString(o));

            else if (o instanceof byte[])
                toPGString0((byte[]) o, sb);

            // [#252] Multi dimensional array support
            else if (o instanceof Object[] a) {

                // [#252] In PostgreSQL "maths" ∅ = {∅}...
                if (isDeepEmpty(a))
                    ;
                else
                    toPGArrayString0(a, sb);
            }
            else
                sb.append("\"")
                  .append(StringUtils.replace(StringUtils.replace(toPGString(o), "\\", "\\\\"), "\"", "\\\""))
                  .append("\"");

            separator = ",";
        }

        sb.append("}");
        return sb;
    }

    private static boolean isDeepEmpty(Object[] a) {
        if (a.length == 0)
            return true;
        else if (a.length == 1 && a[0] instanceof Object[] b)
            return isDeepEmpty(b);
        else
            return false;
    }

    /**
     * Create a PostgreSQL string representation of any object.
     */
    public static String toPGString(Object o) {
        if (o instanceof byte[])
            return toPGString((byte[]) o);
        else if (o instanceof Object[])
            return toPGArrayString((Object[]) o);
        else if (o instanceof Record)
            return toPGString((Record) o);
        else if (o instanceof EnumType)
            return ((EnumType) o).getLiteral();
        else
            return "" + o;
    }

    /**
     * Create a PostgreSQL string representation of a record.
     */
    public static String toPGString(Record r) {
        return toPGString0(r, new StringBuilder()).toString();
    }

    private static StringBuilder toPGString0(Record r, StringBuilder sb) {
        sb.append("(");

        String separator = "";
        for (int i = 0; i < r.size(); i++) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Object a = ((ContextConverter) r.field(i).getConverter()).to(r.get(i), converterContext());
            sb.append(separator);

            // [#753] null must not be set as a literal
            if (a != null) {
                if (a instanceof byte[])
                    toPGString0((byte[]) a, sb);
                else
                    sb.append("\"")
                      .append(StringUtils.replace(StringUtils.replace(toPGString(a), "\\", "\\\\"), "\"", "\\\""))
                      .append("\"");
            }

            separator = ",";
        }

        sb.append(")");
        return sb;
    }

    /**
     * Create a PostgreSQL string representation of a binary.
     */
    public static String toPGString(byte[] binary) {
        return toPGString0(binary, new StringBuilder()).toString();
    }

    private static StringBuilder toPGString0(byte[] binary, StringBuilder sb) {
        for (byte b : binary) {

            // [#3924] Beware of signed vs unsigned bytes!
            sb.append("\\\\");
            sb.append(leftPad(toOctalString(b & 0x000000ff), 3, '0'));
        }

        return sb;
    }
}
