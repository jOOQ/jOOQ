package org.jooq.tools.json;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A very simple JSON reader based on Simple JSON.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JSONReader implements Closeable {


    private final BufferedReader br;
    private final JSONParser parser;

    public JSONReader(BufferedReader reader) {
        this.br = reader;
        this.parser = new JSONParser();
    }

    public List<String[]> readAll() throws IOException {
        List<String[]> all;
        try {
            Object parse = parser.parse(br);
            JSONObject obj2 = (JSONObject) parse;
            JSONArray records = (JSONArray) obj2.get("records");
            all = new ArrayList(records.size());
            for (Object record : records) {
                JSONArray values = (JSONArray) record;
                List<String> v = new ArrayList<String>(values.size());
                for (Object value : values) {
                    v.add(String.valueOf(value));
                }
                all.add(v.toArray(new String[v.size()]));
            }
        } catch (ParseException e1) {
            throw new RuntimeException(e1);
        } catch (IOException e1) {
            throw e1;
        }
        return all;
    }


    @Override
    public void close() throws IOException {
        br.close();

    }

}
