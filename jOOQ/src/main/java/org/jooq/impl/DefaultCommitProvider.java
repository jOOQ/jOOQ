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

import org.jooq.CommitProvider;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.migrations.xml.jaxb.MigrationsType;

/**
 * A default implementation of the {@link CommitProvider} SPI, which provides
 * a materialisation of the currently available database version graph.
 * <p>
 * It is based
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class DefaultCommitProvider implements CommitProvider {

    private final DSLContext     ctx;
    private final MigrationsType migrations;

    public DefaultCommitProvider(Configuration configuration, MigrationsType migrations) {
        this.ctx = configuration.dsl();
        this.migrations = migrations;
    }

    @Override
    public Commits provide() {
        return ctx.commits().load(migrations);
    }
}
