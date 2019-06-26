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
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong&lt;fangyidong@yahoo.com.cn&gt;
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class JSONArray extends ArrayList {

    /**
     * Constructs an empty JSONArray.
     */
    public JSONArray(){
        super();
    }

    /**
     * Constructs a JSONArray containing the elements of the specified
     * collection, in the order they are returned by the collection's iterator.
     *
     * @param c the collection whose elements are to be placed into this JSONArray
     */
    public JSONArray(Collection c){
        super(c);
    }

    /**
     * Encode a list into JSON text and write it to out.
     *
     * @see JSONValue#writeJSONString(Object, Writer)
     */
    public static void writeJSONString(List<?> list, Writer out) throws IOException {
        if (list == null) {
            out.write("null");
            return;
        }

        boolean first = true;
        Iterator<?> iter = list.iterator();

        out.write('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                out.write(',');

            Object value = iter.next();
            if (value == null) {
                out.write("null");
                continue;
            }

            JSONValue.writeJSONString(value, out);
        }
        out.write(']');
    }

    /**
     * Convert a list to JSON text. The result is a JSON array.
     *
     * @see JSONValue#toJSONString(Object)
     * @return JSON text, or "null" if list is null.
     */
    public static String toJSONString(List<?> list) {
        if (list == null)
            return "null";

        boolean first = true;
        StringBuffer sb = new StringBuffer();
        Iterator<?> iter = list.iterator();

        sb.append('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');

            Object value = iter.next();
            if (value == null) {
                sb.append("null");
                continue;
            }
            sb.append(JSONValue.toJSONString(value));
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public String toString() {
        return toJSONString(this);
    }
}
