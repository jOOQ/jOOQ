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
