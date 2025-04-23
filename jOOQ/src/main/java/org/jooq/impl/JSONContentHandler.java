package org.jooq.impl;

import org.xml.sax.ContentHandler;

/**
 * A SAX {@link ContentHandler} style streaming API.
 *
 * @author Lukas Eder
 */
interface JSONContentHandler {

    /**
     * A JSON object is started.
     */
    void startObject();

    /**
     * A JSON object is finished.
     */
    void endObject();

    /**
     * A JSON object property key is encountered.
     */
    void property(String key);

    /**
     * A JSON array is started.
     */
    void startArray();

    /**
     * A JSON array is finished.
     */
    void endArray();

    /**
     * A JSON <code>null</code> value is encountered.
     */
    void valueNull();

    /**
     * A JSON <code>false</code> value is encountered.
     */
    void valueFalse();

    /**
     * A JSON <code>true</code> value is encountered.
     */
    void valueTrue();

    /**
     * A JSON <code>number</code> value is encountered.
     */
    void valueNumber(String string);

    /**
     * A JSON <code>string</code> value is encountered.
     */
    void valueString(String string);
}