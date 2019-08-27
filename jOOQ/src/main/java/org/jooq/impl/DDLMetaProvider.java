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

import java.sql.Connection;

import org.jooq.Configuration;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.SQLDialect;
import org.jooq.Source;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * {@link MetaProvider} implementation which can {@link MetaProvider#provide()
 * provide} a {@link Meta} implementation based on a set of DDL scripts as the
 * input.
 *
 * @author Knut Wannheden
 */
final class DDLMetaProvider implements MetaProvider {

    private final Configuration configuration;
    private final Source[] scripts;

    public DDLMetaProvider(Configuration configuration, Source... scripts) {
        this.configuration = configuration == null ? new DefaultConfiguration() : configuration;
        this.scripts = scripts;
    }

    @Override
    public Meta provide() {
        Configuration localConfiguration = configuration.derive();
        Connection connection = DDLDatabaseInitializer.initializeUsing(localConfiguration.settings(), scripts);
        try {
            localConfiguration.set(connection);
            localConfiguration.set(SQLDialect.H2);
            MetaProvider defaultProvider = new DefaultMetaProvider(localConfiguration);
            Meta meta = DetachedMeta.copyOf(defaultProvider.provide());
            return meta;
        } finally {
            JDBCUtils.safeClose(connection);
        }
    }
}
