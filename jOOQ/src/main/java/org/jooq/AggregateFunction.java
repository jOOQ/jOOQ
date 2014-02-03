/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;


/**
 * An aggregate function is a special field that is usually used in a
 * <code>GROUP BY</code> context. It is also the base for window function
 * construction.
 *
 * @author Lukas Eder
 */
@State(
    aliases = {
        "StatisticalFunction",
        "OrderedAggregateFunction",
        "LinearRegressionFunction"
    },
    terminal = true
)
public interface AggregateFunction<T> extends Field<T>, WindowOverStep<T> {

    /**
     * Turn this aggregate function into a window function.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER (PARTITION BY 1)
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Override
    @Support({ CUBRID, POSTGRES })
    @Transition(
        name = "OVER"
    )
    WindowPartitionByStep<T> over();

    /**
     * Turn this aggregate function into a window function referencing a window
     * name.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase. If the <code>WINDOW</code> clause is not supported
     * (see {@link SelectWindowStep#window(WindowDefinition...)}, then
     * referenced windows will be inlined.
     */
    @Override
    @Support({ CUBRID, POSTGRES })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(Name name);

    /**
     * Turn this aggregate function into a window function referencing a window
     * name.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase. If the <code>WINDOW</code> clause is not supported
     * (see {@link SelectWindowStep#window(WindowDefinition...)}, then
     * referenced windows will be inlined.
     */
    @Override
    @Support({ CUBRID, POSTGRES })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(String name);

    /**
     * Turn this aggregate function into a window function.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER (PARTITION BY 1)
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Override
    @Support({ CUBRID, POSTGRES })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(WindowSpecification specification);

    /**
     * Turn this aggregate function into a window function referencing a window
     * definition.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase. If the <code>WINDOW</code> clause is not supported
     * (see {@link SelectWindowStep#window(WindowDefinition...)}, then
     * referenced windows will be inlined.
     */
    @Override
    @Support({ CUBRID, POSTGRES })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(WindowDefinition definition);

    /* [pro] xx
    xxx
     x xxxxxxxx xxxx xxxxxxxxx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xxxxxx
     x xxx
     x xx xxxxxxxx xxxxxxxxxxx
     x xxxxxxx xxxx xxxxxxxxxxx xxxxx xxxxx xx xx
     x xxxxxxxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxx xxxxxxxxx xx
     x xxxxxxxxxx xxxx xxxx xxxx xxxxxx xxxxxxxxx xx xxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxxxxxxxx
        xxxx x xxxxx xxxxxxxxxx xxxxx xxxxx xxxx
        xxxx x xxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx

    xxx
     x xxxxxxxx xxxx xxxxxxxxx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xxxxxx
     x xxx
     x xx xxxxxxxx xxxxxxxxxxx
     x xxxxxxx xxxx xxxxxxxxxxx xxxxx xxxxx xx xx
     x xxxxxxxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxx xxxxxxxxx xx
     x xxxxxxxxxx xxxx xxxx xxxx xxxxxx xxxxxxxxx xx xxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxxxxxxxx
        xxxx x xxxxx xxxxxxxxxx xxxxx xxxxx xxxx
        xxxx x xxxxxxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx

    xxx
     x xxxxxxxx xxxx xxxxxxxxx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xxxxxx
     x xxx
     x xx xxxxxxxx xxxxxxxxxxx
     x xxxxxxx xxxx xxxxxxxxxxx xxxxx xxxxx xx xx
     x xxxxxxxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxx xxxxxxxxx xx
     x xxxxxxxxxx xxxx xxxx xxxx xxxxxx xxxxxxxxx xx xxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxxxxxxxx
        xxxx x xxxxx xxxxxxxxxx xxxxx xxxxx xxxx
        xxxx x xxxxxxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxx xxxxxxxx

    xxx
     x xxxxxxxx xxxx xxxxxxxxx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xxxxxx
     x xxx
     x xx xxxxxxxx xxxxxxxxxxx
     x xxxxxxx xxxx xxxxxxxxxxx xxxx xxxxx xx xx
     x xxxxxxxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxx xxxxxxxxx xx
     x xxxxxxxxxx xxxx xxxx xxxx xxxxxx xxxxxxxxx xx xxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxxxxxxxx
        xxxx x xxxxx xxxxxxxxxx xxxx xxxxx xxxx
        xxxx x xxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx

    xxx
     x xxxxxxxx xxxx xxxxxxxxx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xxxxxx
     x xxx
     x xx xxxxxxxx xxxxxxxxxxx
     x xxxxxxx xxxx xxxxxxxxxxx xxxx xxxxx xx xx
     x xxxxxxxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxx xxxxxxxxx xx
     x xxxxxxxxxx xxxx xxxx xxxx xxxxxx xxxxxxxxx xx xxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxxxxxxxx
        xxxx x xxxxx xxxxxxxxxx xxxx xxxxx xxxx
        xxxx x xxxxxxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx

    xxx
     x xxxxxxxx xxxx xxxxxxxxx xxxxxxxx xx xxxxxxxxxxxxxxxxxx xxxxxx
     x xxx
     x xx xxxxxxxx xxxxxxxxxxx
     x xxxxxxx xxxx xxxxxxxxxxx xxxx xxxxx xx xx
     x xxxxxxxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxx xxxxxxxxx xx
     x xxxxxxxxxx xxxx xxxx xxxx xxxxxx xxxxxxxxx xx xxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxxxxxxxx
        xxxx x xxxxx xxxxxxxxxx xxxx xxxxx xxxx
        xxxx x xxxxxxxxxxxx
    x
    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxx xxxxxxxx
    xx [/pro] */
}
