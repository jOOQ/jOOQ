package org.jooq.tools.json;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * A very simple JSON reader based on Simple JSON.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JSONReader implements Closeable {


    private final BufferedReader br;
    private final JSONParser parser;
    private String[] fieldMetaData;
    private List<String[]> records;


    public JSONReader(Reader reader) throws IOException {
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
        } catch (ParseException ex) {
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
            records.add(v.toArray(new String[v.size()]));
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
