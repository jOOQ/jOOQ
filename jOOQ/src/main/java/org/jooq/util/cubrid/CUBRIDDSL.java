/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
    private CUBRIDDSL() {
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
