/*
 * $Id: JSONArray.java,v 1.1 2006/04/15 14:10:48 platform Exp $
 * Created on 2006-4-10
 */
package org.jooq.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class JSONArray {

    /**
     * Encode a list into JSON text and write it to out. If this list is also a
     * JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific
     * behaviours will be ignored at this top level.
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
     * Convert a list to JSON text. The result is a JSON array. If this list is
     * also a JSONAware, JSONAware specific behaviours will be omitted at this
     * top level.
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
}
