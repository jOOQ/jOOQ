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
package org.jooq.util.sqlserver;

import static org.jooq.SQLDialect.SQLSERVER;

import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.impl.DSL;

/**
 * The {@link SQLDialect#SQLSERVER} specific DSL.
 *
 * @author Lukas Eder
 */
public class SQLServerDSL extends DSL {

    /**
     * No instances
     */
    protected SQLServerDSL() {
    }

    /**
     * The SQL Server specific <code>SOUNDEX()</code> function.
     *
     * @see <a href="http://technet.microsoft.com/en-us/library/ms187384.aspx">http://technet.microsoft.com/en-us/library/ms187384.aspx</a>
     */
    @Support(SQLSERVER)
    public static Field<String> soundex(String string) {
        return soundex(val(string, String.class));
    }

    /**
     * The SQL Server specific <code>SOUNDEX()</code> function.
     *
     * @see <a href="http://technet.microsoft.com/en-us/library/ms187384.aspx">http://technet.microsoft.com/en-us/library/ms187384.aspx</a>
     */
    @Support(SQLSERVER)
    public static Field<String> soundex(Field<String> field) {
        return field("{soundex}({0})", String.class, nullSafe(field));
    }

    /**
     * The SQL Server specific <code>DIFFERENCE()</code> function.
     *
     * @see <a href="http://technet.microsoft.com/en-us/library/ms188753.aspx">http://technet.microsoft.com/en-us/library/ms188753.aspx</a>
     */
    @Support(SQLSERVER)
    public static Field<Integer> difference(String value1, String value2) {
        return difference(val(value1, String.class), val(value2, String.class));
    }

    /**
     * The SQL Server specific <code>DIFFERENCE()</code> function.
     *
     * @see <a href="http://technet.microsoft.com/en-us/library/ms188753.aspx">http://technet.microsoft.com/en-us/library/ms188753.aspx</a>
     */
    @Support(SQLSERVER)
    public static Field<Integer> difference(Field<String> value1, String value2) {
        return difference(nullSafe(value1), val(value2, String.class));
    }

    /**
     * The SQL Server specific <code>DIFFERENCE()</code> function.
     *
     * @see <a href="http://technet.microsoft.com/en-us/library/ms188753.aspx">http://technet.microsoft.com/en-us/library/ms188753.aspx</a>
     */
    @Support(SQLSERVER)
    public static Field<Integer> difference(String value1, Field<String> value2) {
        return difference(val(value1, String.class), nullSafe(value2));
    }

    /**
     * The SQL Server specific <code>DIFFERENCE()</code> function.
     *
     * @see <a href="http://technet.microsoft.com/en-us/library/ms188753.aspx">http://technet.microsoft.com/en-us/library/ms188753.aspx</a>
     */
    @Support(SQLSERVER)
    public static Field<Integer> difference(Field<String> value1, Field<String> value2) {
        return field("{difference}({0}, {1})", Integer.class, nullSafe(value1), nullSafe(value2));
    }
}
