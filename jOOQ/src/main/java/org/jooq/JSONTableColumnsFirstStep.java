/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import org.jetbrains.annotations.*;


// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

/**
 * A step in the construction of an <code>JSON_TABLE</code> expression.
 *
 * @author Lukas Eder
 */
public interface JSONTableColumnsFirstStep {

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>JSON_TABLE</code> expression.
     */
    @NotNull
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    JSONTableColumnForOrdinalityStep column(String name);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>JSON_TABLE</code> expression.
     */
    @NotNull
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    JSONTableColumnForOrdinalityStep column(Name name);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>JSON_TABLE</code> expression.
     */
    @NotNull
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    JSONTableColumnPathStep column(Field<?> name);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>JSON_TABLE</code> expression.
     */
    @NotNull
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    JSONTableColumnPathStep column(String name, DataType<?> type);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>JSON_TABLE</code> expression.
     */
    @NotNull
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    JSONTableColumnPathStep column(Name name, DataType<?> type);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>JSON_TABLE</code> expression.
     */
    @NotNull
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    JSONTableColumnPathStep column(Field<?> name, DataType<?> type);

}
