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
package org.jooq.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.jooq.tools.json.ContainerFactory;
import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.ParseException;

/**
 * A very simple JSON reader based on Simple JSON.
 *
 * @author Johannes Bühler
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class JSONReader implements Closeable {

    private final BufferedReader br;
    private final JSONParser     parser;
    private String[]             fieldMetaData;
    private List<String[]>       records;

    public JSONReader(Reader reader) {
        this.br = new BufferedReader(reader);
        this.parser = new JSONParser();
    }

    public List<String[]> readAll() throws IOException {
        if (this.records != null) {
            return this.records;
        }
        try {
            LinkedHashMap jsonRoot = getJsonRoot();
            readFields(jsonRoot);
            records = readRecords(jsonRoot);
        }
        catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        return records;
    }

    public String[] getFields() throws IOException {
        if (fieldMetaData == null) {
            readAll();
        }
        return fieldMetaData;
    }

    @Override
    public void close() throws IOException {
        br.close();
    }

    private List<String[]> readRecords(LinkedHashMap jsonRoot) {
        LinkedList jsonRecords = (LinkedList) jsonRoot.get("records");
        records = new ArrayList();
        for (Object record : jsonRecords) {
            LinkedList values = (LinkedList) record;
            List<String> v = new ArrayList<String>();
            for (Object value : values) {
                String asString = value == null ? null : String.valueOf(value);
                v.add(asString);
            }
            records.add(v.toArray(new String[0]));
        }

        return records;
    }

    private LinkedHashMap getJsonRoot() throws IOException, ParseException {
        Object parse = parser.parse(br, new ContainerFactory() {
            @Override
            public LinkedHashMap createObjectContainer() {
                return new LinkedHashMap();
            }

            @Override
            public List createArrayContainer() {
                return new LinkedList();
            }
        });
        return (LinkedHashMap) parse;
    }

    private void readFields(LinkedHashMap jsonRoot) {
        if (fieldMetaData != null) {
            return;
        }
        LinkedList fieldEntries = (LinkedList) jsonRoot.get("fields");
        fieldMetaData = new String[fieldEntries.size()];
        int i = 0;
        for (Object key : fieldEntries) {
            fieldMetaData[i] = (String) ((LinkedHashMap) key).get("name");
            i++;
        }
    }
}

