/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
 * . Neither the name "jOOQ" nor the names of its contributors may be
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
package org.jooq.util.oracle;

import java.sql.Connection;

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;
import org.jooq.impl.CustomField;
import org.jooq.impl.Factory;
import org.jooq.impl.SQLDataType;

/**
 * A {@link SQLDialect#ORACLE} specific factory
 *
 * @author Lukas Eder
 */
public class OracleFactory extends Factory {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1934939784130217163L;

    /**
     * Create a factory with connection and a schema mapping configured
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param mapping The schema mapping to use with objects created from this
     *            factory
     */
    public OracleFactory(Connection connection, SchemaMapping mapping) {
        super(connection, SQLDialect.ORACLE, mapping);
    }

    /**
     * Create a factory with connection
     *
     * @param connection The connection to use with objects created from this
     *            factory
     */
    public OracleFactory(Connection connection) {
        super(connection, SQLDialect.ORACLE);
    }

    // -------------------------------------------------------------------------
    // General pseudo-columns
    // -------------------------------------------------------------------------

    /**
     * Retrieve the Oracle-specific <code>ROWNUM</code> pseudo-field
     */
    public static Field<Integer> rownum() {
        return field("rownum", Integer.class);
    }

    /**
     * Retrieve the Oracle-specific <code>ROWID</code> pseudo-field
     */
    public static Field<String> rowid() {
        return field("rowid", String.class);
    }

    // -------------------------------------------------------------------------
    // Oracle-specific functions
    // -------------------------------------------------------------------------

    /**
     * The Oracle-specific <code>SYS_CONTEXT</code> function
     */
    public static Field<String> sysContext(String namespace, String parameter) {
        return function("sys_context", SQLDataType.VARCHAR, val(namespace), val(parameter));
    }

    /**
     * The Oracle-specific <code>SYS_CONTEXT</code> function
     */
    public static Field<String> sysContext(String namespace, String parameter, int length) {
        return function("sys_context", SQLDataType.VARCHAR, val(namespace), val(parameter), val(length));
    }

    // -------------------------------------------------------------------------
    // Pseudo-and functions for use in the context of a CONNECT BY clause
    // -------------------------------------------------------------------------

    /**
     * Retrieve the Oracle-specific <code>LEVEL</code> pseudo-field (to be used
     * along with <code>CONNECT BY</code> clauses)
     */
    public static Field<Integer> level() {
        return field("level", Integer.class);
    }

    /**
     * Retrieve the Oracle-specific <code>CONNECT_BY_ISCYCLE</code> pseudo-field
     * (to be used along with <code>CONNECT BY</code> clauses)
     */
    public static Field<Boolean> connectByIsCycle() {
        return field("connect_by_iscycle", Boolean.class);
    }

    /**
     * Retrieve the Oracle-specific <code>CONNECT_BY_ISLEAF</code> pseudo-field
     * (to be used along with <code>CONNECT BY</code> clauses)
     */
    public static Field<Boolean> connectByIsLeaf() {
        return field("connect_by_isleaf", Boolean.class);
    }

    /**
     * Retrieve the Oracle-specific
     * <code>SYS_CONNECT_BY_PATH(field, separator)</code> function (to be used
     * along with <code>CONNECT BY</code> clauses).
     */
    public static Field<String> sysConnectByPath(Field<?> field, String separator) {
        String escaped = "'" + separator.replace("'", "''") + "'";
        return function("sys_connect_by_path", String.class, field, literal(escaped));
    }

    /**
     * Add the Oracle-specific <code>PRIOR</code> unary operator before a field
     * (to be used along with <code>CONNECT BY</code> clauses)
     */
    public static <T> Field<T> prior(Field<T> field) {
        return new Prior<T>(field);
    }

    private static class Prior<T> extends CustomField<T> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -530284767039331529L;

        private final Field<T>    field;

        Prior(Field<T> field) {
            super("prior", field.getDataType());

            this.field = field;
        }

        @Override
        public final void toSQL(RenderContext context) {
            context.sql("prior ").sql(field);
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(field);
        }
    }
}
