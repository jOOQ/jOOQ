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

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Parser;
import org.jooq.Source;
import org.jooq.Version;
import org.jooq.VersionProvider;
import org.jooq.Versions;

/**
 * A default implementation of the {@link VersionProvider} SPI, which provides
 * a materialisation of the currently available database version graph.
 * <p>
 * It is based
 *
 * @author Lukas Eder
 */
@org.jooq.Internal // TODO This is work in progress. The current implementation is not useful yet.
public class DefaultVersionProvider implements VersionProvider {

    private final DSLContext ctx;
    private final Source[]   sources;

    public DefaultVersionProvider(Configuration configuration, Source... sources) {
        this.ctx = configuration.dsl();
        this.sources = sources;
    }

    @Override
    public Versions provide() {
        Version parent;
        VersionsImpl result = new VersionsImpl(parent = ctx.version("initial"));

        Parser parser = ctx.parser();
        for (int i = 0; i < sources.length; i++)
            parent = parent.apply("version" + (i + 1), parser.parse(sources[i].readString()));

        return result;
    }
}
