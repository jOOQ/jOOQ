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
 *
 *
 *
 */
package org.jooq;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * A source of text data.
 */
public final class Source {

    private final String      string;
    private final byte[]      bytes;
    private final Charset     charset;
    private final Reader      reader;
    private final InputStream inputStream;

    private Source(
        String string,
        byte[] bytes,
        Charset charset,
        Reader reader,
        InputStream inputStream
    ) {
        this.string = string;
        this.bytes = bytes;
        this.charset = charset;
        this.reader = reader;
        this.inputStream = inputStream;
    }

    /**
     * Create a source from a string.
     */
    public static final Source of(String string) {
        return new Source(string, null, null, null, null);
    }

    /**
     * Create a source from binary data.
     */
    public static final Source of(byte[] bytes) {
        return new Source(null, bytes, null, null, null);
    }

    /**
     * Create a source from binary data using a specific character set.
     */
    public static final Source of(byte[] bytes, Charset charset) {
        return new Source(null, bytes, charset, null, null);
    }

    /**
     * Create a source from a reader.
     */
    public static final Source of(Reader reader) {
        return new Source(null, null, null, reader, null);
    }

    /**
     * Create a source from an input stream.
     */
    public static final Source of(InputStream inputStream) {
        return new Source(null, null, null, null, inputStream);
    }

    /**
     * Create a source from an input stream using a specific character set.
     */
    public static final Source of(InputStream inputStream, Charset charset) {
        return new Source(null, null, charset, null, inputStream);
    }

    /**
     * Produce a reader from this source.
     */
    public final Reader reader() {
        if (string != null)
            return new StringReader(string);
        else if (bytes != null)
            return inputStreamReader(new ByteArrayInputStream(bytes));
        else if (reader != null)
            return reader;
        else if (inputStream != null)
            return inputStreamReader(inputStream);
        else
            throw new IllegalStateException("Could not produce a reader from this source");
    }

    private final Reader inputStreamReader(InputStream is) {
        if (charset != null)
            return new InputStreamReader(is, charset);
        else
            return new InputStreamReader(is);
    }
}
