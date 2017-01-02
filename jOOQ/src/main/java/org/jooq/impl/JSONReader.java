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
package org.jooq.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.tools.json.ContainerFactory;
import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.ParseException;

/**
 * A very simple JSON reader based on Simple JSON.
 *
 * @author Johannes Bühler
 */
@SuppressWarnings({ "unchecked" })
final class JSONReader implements Closeable {

    private final BufferedReader br;
    private final JSONParser     parser;
    private String[]             fieldNames;
    private Map<String, Integer> fieldIndexes;
    private List<String[]>       records;

    JSONReader(Reader reader) {
        this.br = new BufferedReader(reader);
        this.parser = new JSONParser();
    }

    final List<String[]> readAll() throws IOException {
        if (records == null) {
            try {
                LinkedHashMap<String, LinkedList<?>> jsonRoot = getJsonRoot();

                readFields(jsonRoot);
                readRecords(jsonRoot);
            }
            catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        return records;
    }

    final String[] getFields() throws IOException {
        if (fieldNames == null)
            readAll();

        return fieldNames;
    }

    @Override
    public final void close() throws IOException {
        br.close();
    }

    private final void readRecords(LinkedHashMap<String, LinkedList<?>> jsonRoot) {
        records = new ArrayList<String[]>();

        for (Object record : jsonRoot.get("records")) {
            String[] v = new String[fieldNames.length];
            int i = 0;

            // [#5372] Serialisation mode ARRAY
            if (record instanceof LinkedList)
                for (Object value : (LinkedList<Object>) record)
                    v[i++] = value == null ? null : String.valueOf(value);

            // [#5372] Serialisation mode OBJECT
            else if (record instanceof LinkedHashMap)
                for (Entry<String, Object> entry : ((LinkedHashMap<String, Object>) record).entrySet())
                    v[fieldIndexes.get(entry.getKey())] = entry.getValue() == null ? null : String.valueOf(entry.getValue());

            else
                throw new IllegalArgumentException("Ill formed JSON : " + jsonRoot);

            records.add(v);
        }
    }

    private LinkedHashMap<String, LinkedList<?>> getJsonRoot() throws IOException, ParseException {
        Object parse = parser.parse(br, new ContainerFactory() {
            @Override
            public LinkedHashMap<String, Object> createObjectContainer() {
                return new LinkedHashMap<String, Object>();
            }

            @Override
            public List<Object> createArrayContainer() {
                return new LinkedList<Object>();
            }
        });
        return (LinkedHashMap<String, LinkedList<?>>) parse;
    }

    private final void readFields(LinkedHashMap<String, LinkedList<?>> jsonRoot) {
        LinkedList<LinkedHashMap<String, String>> fieldEntries =
            (LinkedList<LinkedHashMap<String, String>>) jsonRoot.get("fields");

        fieldNames = new String[fieldEntries.size()];
        fieldIndexes = new HashMap<String, Integer>();
        int i = 0;
        for (LinkedHashMap<String, String> key : fieldEntries) {
            String name = key.get("name");

            fieldNames[i] = name;
            fieldIndexes.put(name, i);

            i++;
        }
    }
}

