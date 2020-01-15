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

import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.io.Reader;
import java.util.Scanner;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.Query;
import org.jooq.Source;
import org.jooq.tools.JooqLogger;

/**
 * {@link MetaProvider} implementation which can {@link MetaProvider#provide()
 * provide} a {@link Meta} implementation based on a set of DDL scripts as the
 * input.
 * <p>
 * In contrast to {@link DDLMetaProvider} this implementation interprets the DDL
 * scripts.
 *
 * @author Knut Wannheden
 */
final class InterpreterMetaProvider implements MetaProvider {

    private static final JooqLogger log = JooqLogger.getLogger(InterpreterMetaProvider.class);

    private final Configuration     configuration;
    private final Source[]          sources;
    private final Query[]           queries;

    public InterpreterMetaProvider(Configuration configuration, Source... sources) {
        this.configuration = configuration == null ? new DefaultConfiguration() : configuration;
        this.sources = sources;
        this.queries = null;
    }

    public InterpreterMetaProvider(Configuration configuration, Query... queries) {
        this.configuration = configuration == null ? new DefaultConfiguration() : configuration;
        this.sources = null;
        this.queries = queries;
    }

    @Override
    public Meta provide() {
        final Interpreter interpreter = new Interpreter(configuration);
        Configuration localConfiguration = configuration.derive();
        DSLContext ctx = DSL.using(localConfiguration);

        if (sources != null)
            for (Source source : sources)
                loadSource(ctx, source, interpreter);
        else
            for (Query query : queries)
                interpreter.accept(query);

        return interpreter.meta();
    }

    private final void loadSource(DSLContext ctx, Source source, Interpreter interpreter) {
        Reader reader = null;

        try {
            Scanner s = new Scanner(reader = source.reader()).useDelimiter("\\A");

            for (Query query : ctx.parser().parse(s.hasNext() ? s.next() : ""))
                interpreter.accept(query);
        }
        catch (ParserException e) {
            log.error("An exception occurred while parsing a DDL script: " + e.getMessage()
                + ". Please report this error to https://github.com/jOOQ/jOOQ/issues/new", e);
            throw e;
        }
        finally {
            safeClose(reader);
        }
    }
}
