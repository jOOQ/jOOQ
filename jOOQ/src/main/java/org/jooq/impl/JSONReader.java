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
package org.jooq.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.tools.StringUtils.defaultIfBlank;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.json.ContainerFactory;
import org.jooq.tools.json.JSONParser;

/**
 * A very simple JSON reader based on Simple JSON.
 *
 * @author Johannes Bühler
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked" })
final class JSONReader {

    private final DSLContext ctx;

    JSONReader(DSLContext ctx) {
        this.ctx = ctx;
    }

    final Result<Record> read(String string) {
        return read(new StringReader(string));
    }

    final Result<Record> read(final Reader reader) {
        try {

            @SuppressWarnings("rawtypes")
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

            List<Field<?>> f = new ArrayList<>();

            List<?> records;
            Result<Record> result = null;
            Map<String, Integer> fieldIndexes = null;

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

                        f.add(field(name(catalog, schema, table, name), getDataType(ctx.dialect(), defaultIfBlank(type, "VARCHAR"))));
                    }

                    result = ctx.newResult(f);
                }

                records = (List<?>) o1.get("records");
            }
            else {
                records = (List<?>) root;
            }

            for (Object o3 : records) {
                if (o3 instanceof Map) {
                    Map<String, Object> record = (Map<String, Object>) o3;
                    String[] values = new String[record.size()];

                    if (result == null) {
                        if (f.isEmpty())
                            for (String name : record.keySet())
                                f.add(field(name(name), VARCHAR));

                        result = ctx.newResult(f);
                    }

                    if (fieldIndexes == null) {
                        fieldIndexes = new HashMap<>();

                        int i = 0;
                        for (String name : record.keySet())
                            fieldIndexes.put(name, i++);
                    }

                    for (Entry<String, Object> entry : record.entrySet())
                        values[fieldIndexes.get(entry.getKey())] = "" + entry.getValue();

                    Record r = ctx.newRecord(f);
                    r.from(values);
                    result.add(r);
                }
                else {
                    List<?> record = (List<?>) o3;

                    if (result == null) {
                        if (f.isEmpty())
                            f.addAll(Arrays.asList(Tools.fields(record.size())));

                        result = ctx.newResult(f);
                    }

                    Record r = ctx.newRecord(f);
                    r.from(record);
                    result.add(r);
                }
            }

            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

