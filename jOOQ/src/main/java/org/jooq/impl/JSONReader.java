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

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
// ...
// ...
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.allMatch;
import static org.jooq.impl.Tools.convertHexToBytes;
import static org.jooq.impl.Tools.converterContext;
import static org.jooq.impl.Tools.fields;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.tools.StringUtils.defaultIfBlank;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.ContextConverter;
import org.jooq.ConverterContext;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SQLDialectCategory;
import org.jooq.Source;
import org.jooq.tools.json.JSONValue;

/**
 * A very simple JSON reader based on Simple JSON.
 *
 * @author Johannes Bühler
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
final class JSONReader<R extends Record> {

    private final DSLContext         ctx;
    private final AbstractRow<R>     row;
    private final Class<? extends R> recordType;

    JSONReader(DSLContext ctx, AbstractRow<R> row, Class<? extends R> recordType, boolean multiset) {
        this.ctx = ctx;
        this.row = row;
        this.recordType = recordType != null ? recordType : (Class<? extends R>) Record.class;
    }

    final Result<R> read(final Source source) {
        return read(source, false);
    }

    final Result<R> read(final Source source, boolean ms) {
        try {
            DefaultJSONContentHandler handler = new DefaultJSONContentHandler();
            new JSONParser(ctx, source.readString(), handler).parse();
            return read(ctx, row, recordType, ms, handler.result());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final <R extends Record> Result<R> read(
        DSLContext ctx,
        AbstractRow<R> actualRow,
        Class<? extends R> recordType,
        boolean multiset,
        Object root
    ) {
        List<Field<?>> header = new ArrayList<>();

        List<?> records;
        Result<R> result = null;

        if (root instanceof Map) {
            Map<String, Object> o1 = (Map<String, Object>) root;
            List<Map<String, String>> fields = (List<Map<String, String>>) o1.get("fields");

            if (fields != null) {
                for (Map<String, String> field : fields) {
                    String catalog = field.get("catalog");
                    String schema = field.get("schema");
                    String table = field.get("table");
                    String name = field.get("name");
                    String type = field.get("type");

                    // [#13426] Don't use the dialect specific data type, because that isn't what's being exported, either.
                    header.add(field(name(catalog, schema, table, name), getDataType(null, defaultIfBlank(type, "VARCHAR"))));
                }
            }

            records = (List<?>) o1.get("records");
        }
        else
            records = (List<?>) root;

        if (actualRow == null && !header.isEmpty())
            actualRow = (AbstractRow<R>) Tools.row0(header);

        if (actualRow != null)
            result = new ResultImpl<>(ctx.configuration(), actualRow);

        if (records != null) {
            for (Object o3 : records) {
                if (o3 instanceof Map) {
                    Map<String, Object> record = (Map<String, Object>) o3;

                    if (result == null) {
                        if (header.isEmpty())
                            for (String name : record.keySet())
                                header.add(field(name(name), VARCHAR));

                        result = new ResultImpl<>(ctx.configuration(), actualRow = (AbstractRow<R>) Tools.row0(header));
                    }

                    List<Object> list = multiset
                        ? patchRecord(
                            ctx,
                            multiset,
                            actualRow,

                            // This sort is required if we use the JSONFormat.RecordFormat.OBJECT encoding (e.g. in SQL Server)
                            sortedValues(record)
                        )
                        : null;

                    result.add(newRecord(true, ctx.configuration(), recordType, actualRow).operate(r -> {
                        if (multiset)
                            r.from(list);
                        else
                            r.fromMap(record);

                        r.changed(false);
                        return r;
                    }));
                }
                else {
                    List<Object> record = (List<Object>) o3;

                    if (result == null) {
                        if (header.isEmpty())
                            header.addAll(asList(fields(record.size())));

                        result = new ResultImpl<>(ctx.configuration(), actualRow = (AbstractRow<R>) Tools.row0(header));
                    }

                    patchRecord(ctx, multiset, actualRow, record);

                    // [#12930] NULL records are possible when nested ROW is
                    //          returned from an empty scalar subquery.
                    if (record == null)
                        result.add(null);
                    else
                        result.add(newRecord(true, ctx.configuration(), recordType, actualRow).operate(r -> {
                            r.from(record);
                            r.changed(false);
                            return r;
                        }));
                }
            }
        }

        return result;
    }

    private static final List<Object> sortedValues(Map<String, Object> record) {

        // [#13200] The MULTISET map keys are always of the form v0, v1, v2, ...
        List<Object> result = asList(new Object[record.size()]);

        // [#13200] Knowing the key format, we can "sort" the contents in O(N)
        //          rather than with a more name-agnostic O(N log N) approach
        for (Entry<String, Object> e : record.entrySet())
            result.set(parseInt(e.getKey().substring(1)), e.getValue());

        return result;
    }

    static final Set<SQLDialect> ENCODE_BINARY_AS_HEX  = SQLDialect.supportedBy(H2, POSTGRES, SQLITE, TRINO, YUGABYTEDB);
    static final Set<SQLDialect> ENCODE_BINARY_AS_TEXT = SQLDialect.supportedBy(MARIADB);
    private static final Pattern P_MYSQL_BINARY_PREFIX = Pattern.compile("^base64:type\\d+:(.*)$");
    static final Pattern         P_NON_STRING_PREFIX   = Pattern.compile("^(?s:[\\[{\"0-9].*|true|false|null)$");

    private static final List<Object> patchRecord(DSLContext ctx, boolean multiset, Fields result, List<Object> record) {
        ConverterContext cc = null;

        for (int i = 0; i < result.fields().length; i++) {
            Field<?> field = result.field(i);
            Object value = record.get(i);
            DataType<?> t = field.getDataType();

            // [#19606] String values need to be quoted
            if (t.isJSON() && value instanceof String s) {










                record.set(i, JSONValue.toJSONString(s));
            }

            // [#8829] LoaderImpl expects binary data to be encoded in base64,
            //         not according to org.jooq.tools.Convert
            else if (t.isBinary() && value instanceof String s) {
                if (multiset) {
                    Matcher m;

                    // [#12134] PostgreSQL encodes binary data as hex
                    // TODO [#13427] This doesn't work if bytea_output is set to escape
                    if (ENCODE_BINARY_AS_HEX.contains(ctx.dialect()))
                        if (s.startsWith("\\x"))
                            record.set(i, convertHexToBytes(s, 1, Integer.MAX_VALUE));
                        else
                            record.set(i, convertHexToBytes(s));

                    // [#12134] [#18955] MariaDB encodes binary data as text (?) but we
                    //                   encode it explicitly as base64 for MULTISET usage
                    // [#12134] [#18876] MySQL encodes binary data as prefixed base64
                    // - type15: VARBINARY
                    // - type16: BIT
                    // - type249: TINYBLOB
                    // - type250: MEDIUMBLOB and LONG VARBINARY
                    // - type251: LONGBLOB
                    // - type252: BLOB
                    // - type254: BINARY
                    else if (ctx.family().category() == SQLDialectCategory.MYSQL
                            && (m = P_MYSQL_BINARY_PREFIX.matcher(s)).matches())
                        record.set(i, Base64.getDecoder().decode(m.replaceFirst("$1")));
                    else
                        record.set(i, Base64.getDecoder().decode(s));
                }
                else
                    record.set(i, Base64.getDecoder().decode(s));
            }

            // [#18190] For historic reasons, Record.from() will not apply Converter<T, T>, so any potential
            //          Converter<String, String> should be applied eagerly, before loading data into the record.
            else if (multiset
                && t instanceof ConvertedDataType
                && t.getFromType() == String.class
                && t.getToType() == String.class
                && (value == null || value instanceof String)
            ) {
                record.set(i, ((ContextConverter<String, String>) t.getConverter()).from(
                    (String) value,
                    cc == null ? (cc = converterContext(ctx.configuration())) : cc
                ));
            }

            // [#12155] Recurse for nested MULTISET
            else if (multiset && t.isMultiset()) {
                record.set(i, read(
                    ctx,
                    (AbstractRow) t.getRow(),
                    (Class) t.getRecordType(),
                    multiset,
                    value
                ));
            }

            // [#14657] Recurse for nested ROW
            // [#18152] Handle also the Map encoding of nested ROW values
            else if (multiset && t.isRecord() && (value instanceof List || value instanceof Map)) {
                AbstractRow<? extends Record> actualRow = (AbstractRow) t.getRow();
                Class<? extends Record> recordType = t.getRecordType();

                List<Object> l;

                if (value instanceof List) {
                    l = patchRecord(ctx, multiset, actualRow, (List<Object>) value);
                }

                // [#18681] The Map encoding of nested ROW values can happen for 2 reasons:
                //          - We create it ourselves with "v1", "v2", ... keys because json objects work better than arrays in some RDBMS
                //          - It's a UDT or similar, serialised into a JSON object, where keys are attribute names, not in order!
                else if (value instanceof Map<?, ?> map) {
                    if (QualifiedRecord.class.isAssignableFrom(recordType) && allMatch(actualRow.fields.fields, f -> map.containsKey(f.getName())))
                        l = patchRecord(ctx, multiset, actualRow, map(actualRow.fields.fields, f -> map.get(f.getName())));
                    else
                        l = patchRecord(ctx, multiset, actualRow, new ArrayList<>(map.values()));
                }
                else
                    throw new IllegalStateException();

                record.set(i, newRecord(true, ctx.configuration(), recordType, actualRow).operate(r -> {
                    r.from(l);
                    r.changed(false);
                    return r;
                }));
            }
        }

        return record;
    }
}

