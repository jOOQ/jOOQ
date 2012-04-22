/*
 * $Id: JSONObject.java,v 1.1 2006/04/15 14:10:48 platform Exp $
 * Created on 2006-4-10
 */
package org.jooq.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

/**
 * A JSON object. Key value pairs are unordered. JSONObject supports
 * java.util.Map interface.
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class JSONObject {

    /**
     * Encode a map into JSON text and write it to out. If this map is also a
     * JSONAware or JSONStreamAware, JSONAware or JSONStreamAware specific
     * behaviours will be ignored at this top level.
     *
     * @see JSONValue#writeJSONString(Object, Writer)
     */
    public static void writeJSONString(Map<?, ?> map, Writer out) throws IOException {
        if (map == null) {
            out.write("null");
            return;
        }

        boolean first = true;
        Iterator<?> iter = map.entrySet().iterator();

        out.write('{');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                out.write(',');
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
            out.write('\"');
            out.write(escape(String.valueOf(entry.getKey())));
            out.write('\"');
            out.write(':');
            JSONValue.writeJSONString(entry.getValue(), out);
        }
        out.write('}');
    }

    /**
     * Convert a map to JSON text. The result is a JSON object. If this map is
     * also a JSONAware, JSONAware specific behaviours will be omitted at this
     * top level.
     *
     * @see JSONValue#toJSONString(Object)
     * @return JSON text, or "null" if map is null.
     */
    public static String toJSONString(Map<?, ?> map) {
        if (map == null)
            return "null";

        StringBuffer sb = new StringBuffer();
        boolean first = true;
        Iterator<?> iter = map.entrySet().iterator();

        sb.append('{');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
            toJSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
        }
        sb.append('}');
        return sb.toString();
    }

    private static String toJSONString(String key, Object value, StringBuffer sb) {
        sb.append('\"');
        if (key == null)
            sb.append("null");
        else
            JSONValue.escape(key, sb);
        sb.append('\"').append(':');

        sb.append(JSONValue.toJSONString(value));

        return sb.toString();
    }

    public static String toString(String key, Object value) {
        StringBuffer sb = new StringBuffer();
        toJSONString(key, value, sb);
        return sb.toString();
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
     * (U+0000 through U+001F). It's the same as JSONValue.escape() only for
     * compatibility here.
     *
     * @see JSONValue#escape(String)
     */
    public static String escape(String s) {
        return JSONValue.escape(s);
    }
}
