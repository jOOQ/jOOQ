/*
 * Copyright (c) 2013 by Yidong Fang
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jooq.tools.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author FangYidong&lt;fangyidong@yahoo.com.cn&gt;
 * @deprecated - 3.21.0 - [#18329] - This shaded third party dependency will be
 *             removed without replacement. Please use any other JSON parser,
 *             instead - e.g. Jackson.
 */
@Deprecated(forRemoval = true)
public class JSONValue {

    /**
     * Encode an object into JSON text and write it to out.
     *
     * @see JSONObject#writeJSONString(Map, Writer)
     * @see JSONArray#writeJSONString(List, Writer)
     */
    public static void writeJSONString(Object value, Writer out) throws IOException {
        if (value == null) {
            out.write("null");
            return;
        }

        if (value instanceof String s) {
            out.write('\"');
            out.write(escape(s));
            out.write('\"');
            return;
        }

        if (value instanceof Double d) {
            if (d.isInfinite() || d.isNaN())
                out.write("null");
            else
                out.write(value.toString());

            return;
        }

        if (value instanceof Float f) {
            if (f.isInfinite() || f.isNaN())
                out.write("null");
            else
                out.write(value.toString());

            return;
        }

        if (value instanceof Number) {
            out.write(value.toString());
            return;
        }

        if (value instanceof Boolean) {
            out.write(value.toString());
            return;
        }

        if (value instanceof Map<?, ?> m) {
            JSONObject.writeJSONString(m, out);
            return;
        }

        if (value instanceof List<?> l) {
            JSONArray.writeJSONString(l, out);
            return;
        }

        if (value instanceof Object[] a) {
            JSONArray.writeJSONString(Arrays.asList(a), out);
            return;
        }

        // Patched original according to issue 27 of JSON-simple
        // http://code.google.com/p/json-simple/issues/detail?id=27
        out.write('\"');
        out.write(escape(value.toString()));
        out.write('\"');
    }

    /**
     * Convert an object to JSON text.
     *
     * @see JSONObject#toJSONString(Map)
     * @see JSONArray#toJSONString(List)
     * @return JSON text, or "null" if value is null or it's an NaN or an INF
     *         number.
     */
    public static String toJSONString(Object value) {
        Writer w = new StringWriter();

        try {
            writeJSONString(value, w);
        }
        catch (IOException ignore) {}

        return w.toString();
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
     * (U+0000 through U+001F).
     */
    public static String escape(String s) {
        if (s == null)
            return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }

    /**
     * @param s - Must not be null.
     */
    static void escape(String s, StringBuffer sb) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    // Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
                        || (ch >= '\u2000' && ch <= '\u20FF')) {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    }
                    else {
                        sb.append(ch);
                    }
            }
        }// for
    }

    /**
     * Parse JSON text into java object from the input source.
     *
     * @return Instance of the following:
     *      org.json.simple.JSONObject,
     *      org.json.simple.JSONArray,
     *      java.lang.String,
     *      java.lang.Number,
     *      java.lang.Boolean,
     *      null
     */
    public static Object parseWithException(Reader in) throws IOException, ParseException{
        JSONParser parser=new JSONParser();
        return parser.parse(in);
    }

    public static Object parseWithException(String s) throws ParseException{
        JSONParser parser=new JSONParser();
        return parser.parse(s);
    }

}
