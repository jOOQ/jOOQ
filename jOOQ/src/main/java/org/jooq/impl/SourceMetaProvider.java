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
package org.jooq.impl;

import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import org.jooq.Configuration;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.SQLDialect;
import org.jooq.Source;


/**
 * A {@link MetaProvider} implementation that can handle different types of
 * {@link Source} content.
 *
 * @author Lukas Eder
 */
final class SourceMetaProvider implements MetaProvider {

    private final Configuration configuration;
    private final Source[]      sources;

    SourceMetaProvider(Configuration configuration, Source... sources) {
        this.configuration = configuration;
        this.sources = sources;
    }

    @Override
    public final Meta provide() {
        if (sources.length > 0) {
            StringWriter w = new StringWriter();
            Reader r = null;

            try {
                r = sources[0].reader();
                char[] buffer = new char[8192];
                int nRead;
                while ((nRead = r.read(buffer, 0, 8192)) >= 0) {
                    w.write(buffer, 0, nRead);
                }
            }
            catch (IOException e) {
                throw new org.jooq.exception.IOException("Could not read source", e);
            }
            finally {
                safeClose(r);
            }

            String s = w.toString();
            sources[0] = Source.of(w.toString());

            // TODO: Implement more thorough and reusable "isXML()" check in MiniJAXB
            if (s.startsWith("<?xml") || s.startsWith("<information_schema") || s.startsWith("<!--"))
                return new InformationSchemaMetaProvider(configuration, sources).provide();
        }

        SQLDialect dialect = configuration.settings().getInterpreterDialect();
        switch (defaultIfNull(dialect, DEFAULT)) {
            case DEFAULT:
                return new DDLInterpreterMetaProvider(configuration, sources).provide();

            case DERBY:
            case H2:
            case HSQLDB:
            case SQLITE:
                return new DDLMetaProvider(configuration, sources).provide();

            default:
                throw new UnsupportedOperationException("Interpreter dialect not yet supported: " + dialect);
        }
    }
}
