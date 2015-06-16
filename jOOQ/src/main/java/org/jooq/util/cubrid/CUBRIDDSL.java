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
package org.jooq.util.cubrid;

import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * The {@link SQLDialect#CUBRID} specific DSL.
 *
 * @author Lukas Eder
 */
public class CUBRIDDSL extends DSL {

    /**
     * No instances
     */
    protected CUBRIDDSL() {
    }

    // -------------------------------------------------------------------------
    // MySQL-specific functions
    // -------------------------------------------------------------------------

    /**
     * Use the CUBRID-specific <code>INCR()</code> function.
     * <p>
     * This function can be used to increment a field value in a
     * <code>SELECT</code> statement as such: <code><pre>
     * SELECT article, INCR(read_count)
     * FROM article_table
     * WHERE article_id = 130,987</pre></code>
     */
    public static <T> Field<T> incr(Field<T> field) {
        return field("{incr}({0})", field.getDataType(), field);
    }

    /**
     * Use the CUBRID-specific <code>DECR()</code> function.
     * <p>
     * This function can be used to increment a field value in a
     * <code>SELECT</code> statement as such: <code><pre>
     * SELECT article, DECR(read_count)
     * FROM article_table
     * WHERE article_id = 130,987</pre></code>
     */
    public static <T> Field<T> decr(Field<T> field) {
        return field("{decr}({0})", field.getDataType(), field);
    }
}
