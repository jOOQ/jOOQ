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
package org.jooq.impl;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
// ...
// ...
// ...
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.convertHexToBytes;
import static org.jooq.impl.Tools.fields;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.tools.StringUtils.defaultIfBlank;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.tools.json.ContainerFactory;
import org.jooq.tools.json.JSONParser;

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
    private final boolean            multiset;

    JSONReader(DSLContext ctx, AbstractRow<R> row, Class<? extends R> recordType, boolean multiset) {
        this.ctx = ctx;
        this.row = row;
        this.recordType = recordType != null ? recordType : (Class<? extends R>) Record.class;
        this.multiset = multiset;
    }

    final Result<R> read(String string) {
        return read(new StringReader(string));
    }

    final Result<R> read(final Reader reader) {
        return read(reader, false);
    }

    final Result<R> read(final Reader reader, boolean multiset) {
        try {
            Object root = new JSONParser().parse(reader, new ContainerFactory() {
                @Override
                public Map createObjectContainer() {
                    return new LinkedHashMap();
                }

                @Override
                public List createArrayContainer() {
                    return new ArrayList();
                }
            });

            return read(ctx, row, recordType, multiset, root);
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

                    result.add(newRecord(true, recordType, actualRow, ctx.configuration()).operate(r -> {
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
                        result.add(newRecord(true, recordType, actualRow, ctx.configuration()).operate(r -> {
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

    private static final Set<SQLDialect> ENCODE_BINARY_AS_HEX  = SQLDialect.supportedBy(H2, POSTGRES, SQLITE, YUGABYTEDB);
    private static final Set<SQLDialect> ENCODE_BINARY_AS_TEXT = SQLDialect.supportedBy(MARIADB);

    private static final List<Object> patchRecord(DSLContext ctx, boolean multiset, Fields result, List<Object> record) {
        for (int i = 0; i < result.fields().length; i++) {
            Field<?> field = result.field(i);

            // [#8829] LoaderImpl expects binary data to be encoded in base64,
            //         not according to org.jooq.tools.Convert
            if (field.getType() == byte[].class && record.get(i) instanceof String) {
                String s = (String) record.get(i);

                if (multiset) {

                    // [#12134] PostgreSQL encodes binary data as hex
                    // TODO [#13427] This doesn't work if bytea_output is set to escape
                    if (ENCODE_BINARY_AS_HEX.contains(ctx.dialect()))
                        if (s.startsWith("\\x"))
                            record.set(i, convertHexToBytes(s, 1, Integer.MAX_VALUE));
                        else
                            record.set(i, convertHexToBytes(s));

                    // [#12134] MariaDB encodes binary data as text (?)
                    else if (ENCODE_BINARY_AS_TEXT.contains(ctx.dialect()))
                        record.set(i, s);

                    // [#12134] MySQL encodes binary data as prefixed base64
                    else if (s.startsWith("base64:type15:"))
                        record.set(i, Base64.getDecoder().decode(s.substring(14)));
                    else
                        record.set(i, Base64.getDecoder().decode(s));
                }
                else
                    record.set(i, Base64.getDecoder().decode(s));
            }

            // [#12155] Recurse for nested MULTISET
            else if (multiset && field.getDataType().isMultiset()) {
                record.set(i, read(
                    ctx,
                    (AbstractRow) field.getDataType().getRow(),
                    (Class) field.getDataType().getRecordType(),
                    multiset,
                    record.get(i)
                ));
            }

            // [#14657] Recurse for nested ROW
            else if (multiset && field.getDataType().isRecord() && record.get(i) instanceof List) {
                AbstractRow<? extends Record> actualRow = (AbstractRow) field.getDataType().getRow();
                Class<? extends Record> recordType = field.getDataType().getRecordType();
                List<Object> l = (List<Object>) record.get(i);
                patchRecord(ctx, multiset, actualRow, l);

                record.set(i, newRecord(true, recordType, actualRow, ctx.configuration()).operate(r -> {
                    r.from(l);
                    r.changed(false);
                    return r;
                }));
            }
        }

        return record;
    }
}

