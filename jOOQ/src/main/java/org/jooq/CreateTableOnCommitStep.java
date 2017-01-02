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
package org.jooq;

// ...
import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.impl.DSL;

/**
 * A {@link Query} that can create tables.
 *
 * @author Lukas Eder
 */
public interface CreateTableOnCommitStep extends CreateTableFinalStep {

    /**
     * Add an <code>ON COMMIT DELETE ROWS</code> clause.
     * <p>
     * This clause will only be rendered when used with a
     * <code>GLOBAL TEMPORARY TABLE</code>
     *
     * @see DSL#createGlobalTemporaryTable(Table)
     */
    @Support({ POSTGRES })
    CreateTableFinalStep onCommitDeleteRows();

    /**
     * Add an <code>ON COMMIT PRESERVE ROWS</code> clause.
     * <p>
     * This clause will only be rendered when used with a
     * <code>GLOBAL TEMPORARY TABLE</code>
     *
     * @see DSL#createGlobalTemporaryTable(Table)
     */
    @Support({ POSTGRES })
    CreateTableFinalStep onCommitPreserveRows();

    /**
     * Add an <code>ON COMMIT DROP</code> clause.
     * <p>
     * This clause will only be rendered when used with a
     * <code>GLOBAL TEMPORARY TABLE</code>
     *
     * @see DSL#createGlobalTemporaryTable(Table)
     */
    @Support({ POSTGRES })
    CreateTableFinalStep onCommitDrop();
}
