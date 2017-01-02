/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util;

import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.param;

import java.sql.Connection;

import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

/**
 * @author Lukas Eder
 */
class SQLCatalogVersionProvider implements CatalogVersionProvider {

    private Connection connection;
    private String     sql;

    SQLCatalogVersionProvider(Connection connection, String sql) {
        this.connection = connection;
        this.sql = sql;
    }

    @Override
    public String version(CatalogDefinition catalog) {
        return "" +
            DSL.using(
                new DefaultConfiguration()
                    .set(connection)
                    .set(new Settings().withStatementType(STATIC_STATEMENT))
            ).fetchValue(
                // [#2906] TODO Plain SQL statements do not yet support named parameters
                sql.replace(":catalog_name", "?"), param("catalog_name", catalog.getInputName())
            );
    }
}
