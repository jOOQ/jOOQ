/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name of the "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
