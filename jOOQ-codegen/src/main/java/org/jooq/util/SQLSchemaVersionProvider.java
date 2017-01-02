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
class SQLSchemaVersionProvider implements SchemaVersionProvider {

    private Connection connection;
    private String     sql;

    SQLSchemaVersionProvider(Connection connection, String sql) {
        this.connection = connection;
        this.sql = sql;
    }

    @Override
    public String version(SchemaDefinition schema) {
        return "" +
            DSL.using(
                new DefaultConfiguration()
                    .set(connection)
                    .set(new Settings().withStatementType(STATIC_STATEMENT))
            ).fetchValue(
                // [#2906] TODO Plain SQL statements do not yet support named parameters
                sql.replace(":schema_name", "?"), param("schema_name", schema.getInputName())
            );
    }
}
