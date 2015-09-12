/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.SQLDialect.ORACLE;

import java.util.Collection;

/**
 * An intermediate type for the construction of a partitioned
 * {@link SQLDialect#ORACLE} <code>OUTER JOIN</code> clause.
 * <p>
 * This step allows for adding Oracle-specific <code>PARTITION BY</code> clauses
 * to the right of an <code>OUTER JOIN</code> keyword. See the Oracle
 * documentation for more details here: <a href=
 * "http://docs.oracle.com/cd/B28359_01/server.111/b28286/queries006.htm#i2054062"
 * >http://docs.oracle.com/cd/B28359_01/server.111/b28286/queries006.htm#
 * i2054062</a>
 *
 * @author Lukas Eder
 */
public interface TablePartitionByStep<R extends Record> extends TableOnStep<R> {

    /* [pro] */
    /**
     * Add a <code>PARTITION BY</code> clause to the right hand side of the
     * <code>OUTER JOIN</code> keywords
     */
    @Support(ORACLE)
    TableOnStep<R> partitionBy(Field<?>... fields);

    /**
     * Add a <code>PARTITION BY</code> clause to the right hand side of the
     * <code>OUTER JOIN</code> keywords
     */
    @Support(ORACLE)
    TableOnStep<R> partitionBy(Collection<? extends Field<?>> fields);
    /* [/pro] */
}
