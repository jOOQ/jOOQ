/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...

import org.jooq.conf.Settings;

/**
 * A SQL identifier <code>QueryPart</code>.
 * <p>
 * A <code>Name</code> is a {@link QueryPart} that renders a SQL identifier
 * according to the settings specified in {@link Settings#getRenderNameStyle()}.
 *
 * @author Lukas Eder
 */
public interface Name extends QueryPart {

    /**
     * The qualified name of this SQL identifier.
     */
    String[] getName();

    /**
     * Create a {@link WindowDefinition} from this name.
     * <p>
     * This creates a window definition that can be
     * <ul>
     * <li>declared in the <code>WINDOW</code> clause (see
     * {@link SelectWindowStep#window(WindowDefinition...)}</li>
     * <li>referenced from the <code>OVER</code> clause (see
     * {@link AggregateFunction#over(WindowDefinition)}</li>
     * </ul>
     */
    @Support({ CUBRID, POSTGRES })
    WindowDefinition as(WindowSpecification window);

    /**
     * Specify a subselect to refer to by the <code>Name</code> to form a common
     * table expression.
     * <p>
     * Column names are implicitly inherited from the <code>SELECT</code>
     * statement.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    <R extends Record> CommonTableExpression<R> as(Select<R> select);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    DerivedColumnList fields(String... fieldNames);

}
