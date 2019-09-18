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
package org.jooq.tools.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

import org.jooq.Source;

/**
 * A configuration object for the {@link MockFileDatabase}.
 *
 * @author Lukas Eder
 */
public final class MockFileDatabaseConfiguration {

    final LineNumberReader in;
    final boolean          patterns;
    final String           nullLiteral;

    public MockFileDatabaseConfiguration() {
        this(new LineNumberReader(new StringReader("")), false, null);
    }

    private MockFileDatabaseConfiguration(
        LineNumberReader in,
        boolean patterns,
        String nullLiteral
    ) {
        this.in = in;
        this.patterns = patterns;
        this.nullLiteral = nullLiteral;
    }

    public final MockFileDatabaseConfiguration source(File file) throws IOException {
        return source(Source.of(file, "UTF-8"));
    }

    public final MockFileDatabaseConfiguration source(File file, String encoding) throws IOException {
        return source(Source.of(file, encoding));
    }

    public final MockFileDatabaseConfiguration source(InputStream stream) throws IOException {
        return source(Source.of(stream, "UTF-8"));
    }

    public final MockFileDatabaseConfiguration source(InputStream stream, String encoding) throws IOException {
        return source(Source.of(stream, encoding));
    }

    public final MockFileDatabaseConfiguration source(Source source) {
        return source(source.reader());
    }

    public final MockFileDatabaseConfiguration source(Reader reader) {
        return new MockFileDatabaseConfiguration(new LineNumberReader(reader), patterns, nullLiteral);
    }

    public final MockFileDatabaseConfiguration source(String string) {
        return source(Source.of(string));
    }

    public final MockFileDatabaseConfiguration patterns(boolean newPatterns) {
        return new MockFileDatabaseConfiguration(in, newPatterns, nullLiteral);
    }

    public final MockFileDatabaseConfiguration nullLiteral(String newNullLiteral) {
        return new MockFileDatabaseConfiguration(in, patterns, newNullLiteral);
    }
}
