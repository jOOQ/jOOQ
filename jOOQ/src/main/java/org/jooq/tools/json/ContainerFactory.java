package org.jooq.tools.json;

import java.util.List;
import java.util.Map;

/**
 * Container factory for creating containers for JSON object and JSON array.
 *
 * @see JSONParser#parse(java.io.Reader, ContainerFactory)
 *
 * @author FangYidong&lt;fangyidong@yahoo.com.cn&gt;
 */
@SuppressWarnings({ "rawtypes" })
public interface ContainerFactory {
    /**
     * @return A Map instance to store JSON object, or null if you want to use org.json.simple.JSONObject.
     */
    Map createObjectContainer();

    /**
     * @return A List instance to store JSON array, or null if you want to use org.json.simple.JSONArray.
     */
    List createArrayContainer();
}