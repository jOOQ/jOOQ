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
package org.jooq;

// ...
import static org.jooq.SQLDialect.POSTGRES;

/**
 * A step in the construction of an <code>XMLTABLE</code> expression.
 *
 * @author Lukas Eder
 */
public interface XMLTableColumnsFirstStep {

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>XMLTABLE</code> expression.
     */
    @Support({ POSTGRES })
    XMLTableColumnForOrdinalityStep column(String name);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>XMLTABLE</code> expression.
     */
    @Support({ POSTGRES })
    XMLTableColumnForOrdinalityStep column(Name name);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>XMLTABLE</code> expression.
     */
    @Support({ POSTGRES })
    XMLTableColumnPathStep column(Field<?> name);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>XMLTABLE</code> expression.
     */
    @Support({ POSTGRES })
    XMLTableColumnPathStep column(String name, DataType<?> type);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>XMLTABLE</code> expression.
     */
    @Support({ POSTGRES })
    XMLTableColumnPathStep column(Name name, DataType<?> type);

    /**
     * Add a column to the <code>COLUMNS</code> clause of the
     * <code>XMLTABLE</code> expression.
     */
    @Support({ POSTGRES })
    XMLTableColumnPathStep column(Field<?> name, DataType<?> type);

}
