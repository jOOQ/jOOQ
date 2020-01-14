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

import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.jooq.exception.IOException;

/**
 * A source of text data.
 */
public final class Source {

    private final String         string;
    private final byte[]         bytes;
    private final String         charsetName;
    private final Charset        charset;
    private final CharsetDecoder charsetDecoder;
    private final Reader         reader;
    private final InputStream    inputStream;
    private final File           file;

    private Source(
        String string,
        byte[] bytes,
        String charsetName,
        Charset charset,
        CharsetDecoder charsetDecoder,
        Reader reader,
        InputStream inputStream,
        File file
    ) {
        this.string = string;
        this.bytes = bytes;
        this.charsetName = charsetName;
        this.charset = charset;
        this.charsetDecoder = charsetDecoder;
        this.reader = reader;
        this.inputStream = inputStream;
        this.file = file;
    }

    /**
     * Create a source from a string.
     */
    public static final Source of(String string) {
        return new Source(string, null, null, null, null, null, null, null);
    }

    /**
     * Create a source from binary data.
     */
    public static final Source of(byte[] bytes) {
        return of(bytes, (Charset) null);
    }

    /**
     * Create a source from binary data using a specific character set.
     */
    public static final Source of(byte[] bytes, String charsetName) {
        return new Source(null, bytes, charsetName, null, null, null, null, null);
    }

    /**
     * Create a source from binary data using a specific character set.
     */
    public static final Source of(byte[] bytes, Charset charset) {
        return new Source(null, bytes, null, charset, null, null, null, null);
    }

    /**
     * Create a source from binary data using a specific character set.
     */
    public static final Source of(byte[] bytes, CharsetDecoder charsetDecoder) {
        return new Source(null, bytes, null, null, charsetDecoder, null, null, null);
    }

    /**
     * Create a source from a file.
     */
    public static final Source of(File file) {
        return new Source(null, null, null, null, null, null, null, file);
    }

    /**
     * Create a source from a file using a specific character set.
     */
    public static final Source of(File file, String charsetName) {
        return new Source(null, null, charsetName, null, null, null, null, file);
    }

    /**
     * Create a source from a file using a specific character set.
     */
    public static final Source of(File file, Charset charset) {
        return new Source(null, null, null, charset, null, null, null, file);
    }

    /**
     * Create a source from a file using a specific character set.
     */
    public static final Source of(File file, CharsetDecoder charsetDecoder) {
        return new Source(null, null, null, null, charsetDecoder, null, null, file);
    }

    /**
     * Create a source from a reader.
     */
    public static final Source of(Reader reader) {
        return new Source(null, null, null, null, null, reader, null, null);
    }

    /**
     * Create a source from an input stream.
     */
    public static final Source of(InputStream inputStream) {
        return new Source(null, null, null, null, null, null, inputStream, null);
    }

    /**
     * Create a source from an input stream using a specific character set.
     */
    public static final Source of(InputStream inputStream, String charsetName) {
        return new Source(null, null, charsetName, null, null, null, inputStream, null);
    }

    /**
     * Create a source from an input stream using a specific character set.
     */
    public static final Source of(InputStream inputStream, Charset charset) {
        return new Source(null, null, null, charset, null, null, inputStream, null);
    }

    /**
     * Create a source from an input stream using a specific character set.
     */
    public static final Source of(InputStream inputStream, CharsetDecoder charsetDecoder) {
        return new Source(null, null, null, null, charsetDecoder, null, inputStream, null);
    }

    /**
     * Produce a reader from this source.
     *
     * @throws IOException When something goes wrong creating a reader from this
     *             source.
     */
    public final Reader reader() throws IOException {
        try {
            if (string != null)
                return new StringReader(string);
            else if (bytes != null)
                return inputStreamReader(new ByteArrayInputStream(bytes));
            else if (reader != null)
                return reader;
            else if (inputStream != null)
                return inputStreamReader(inputStream);
            else if (file != null)
                return new BufferedReader(inputStreamReader(new FileInputStream(file)));
            else
                throw new IllegalStateException("Could not produce a reader from this source");
        }
        catch (java.io.IOException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * Read the entire {@link #reader()} into a String, for convenience.
     *
     * @throws IOException When something goes wrong creating a reader from this
     *             source.
     */
    public final String readString() throws IOException {
        StringWriter w = new StringWriter();
        Reader r = null;

        try {
            r = reader();
            char[] buffer = new char[8192];
            int read;
            while ((read = r.read(buffer, 0, 8192)) >= 0)
                w.write(buffer, 0, read);
        }
        catch (java.io.IOException e) {
            throw new IOException("Could not read source", e);
        }
        finally {
            safeClose(r);
        }

        return w.toString();
    }

    private final Reader inputStreamReader(InputStream is) throws UnsupportedEncodingException {
        if (charsetName != null)
            return new InputStreamReader(is, charsetName);
        else if (charset != null)
            return new InputStreamReader(is, charset);
        else if (charsetDecoder != null)
            return new InputStreamReader(is, charsetDecoder);
        else
            return new InputStreamReader(is);
    }

    @Override
    public String toString() {
        if (string != null)
            return string;
        else if (bytes != null)
            return readString();
        else if (reader != null)
            return "Source (Reader)";
        else if (inputStream != null)
            return "Source (InputStream)";
        else if (file != null)
            return "Source (" + file + ")";
        else
            return "Source (other)";
    }
}
