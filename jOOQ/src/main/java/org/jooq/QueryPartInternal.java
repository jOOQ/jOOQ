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

package org.jooq;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Base functionality declaration for all query objects
 * <p>
 * This interface is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public interface QueryPartInternal extends QueryPart {

    /**
     * Transform this object into SQL, such that it can be used as a reference.
     * This always results in calling
     * <code>toSQLReference(configuration, false)</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @return SQL representation of this QueryPart
     * @see #toSQLReference(Configuration, boolean)
     * @deprecated - 1.6.4 [#758] - Use {@link #toSQL(RenderContext)} instead
     */
    @Deprecated
    String toSQLReference(Configuration configuration);

    /**
     * Transform this object into SQL, such that it can be used as a reference.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param inlineParameters if set to true, all parameters are inlined, not
     *            replaced by "?"
     * @return SQL representation of this QueryPart
     * @deprecated - 1.6.4 [#758] - Use {@link #toSQL(RenderContext)} instead
     */
    @Deprecated
    String toSQLReference(Configuration configuration, boolean inlineParameters);

    /**
     * Transform this object into SQL, such that it can be used as a
     * declaration. Usually, this is the same as calling
     * <code>toSQLReference(configuration, false)</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @return SQL representation of this QueryPart
     * @see #toSQLDeclaration(Configuration, boolean)
     * @deprecated - 1.6.4 [#758] - Use {@link #toSQL(RenderContext)} instead
     */
    @Deprecated
    String toSQLDeclaration(Configuration configuration);

    /**
     * Transform this object into SQL, such that it can be used as a
     * declaration. Usually, this is the same as calling
     * <code>toSQLReference(Configuration, boolean)</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param inlineParameters if set to true, all parameters are inlined, not
     *            replaced by "?"
     * @return SQL representation of this QueryPart
     * @deprecated - 1.6.4 [#758] - Use {@link #toSQL(RenderContext)} instead
     */
    @Deprecated
    String toSQLDeclaration(Configuration configuration, boolean inlineParameters);

    /**
     * Render this {@link QueryPart} to a SQL string contained in
     * <code>context.sql()</code>. The <code>context</code> will contain
     * additional information about how to render this <code>QueryPart</code>,
     * e.g. whether this <code>QueryPart</code> should be rendered as a
     * declaration or reference, whether this <code>QueryPart</code>'s contained
     * bind variables should be inlined or replaced by <code>'?'</code>, etc.
     */
    void toSQL(RenderContext context);

    /**
     * Retrieve the bind values that will be bound by this QueryPart
     * <p>
     * This method is exposed publicly in {@link Query#getBindValues()}
     */
    List<Object> getBindValues();

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement. This always
     * results in calling <code>bind(configuration, stmt, 1)</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param stmt The statement to bind values to
     * @return The index of the next binding variable
     * @throws SQLException
     * @see #bind(Configuration, PreparedStatement, int)
     * @deprecated - Use
     *             {@link #bindReference(Configuration, PreparedStatement)}
     *             instead
     */
    @Deprecated
    int bind(Configuration configuration, PreparedStatement stmt) throws SQLException;

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param stmt The statement to bind values to
     * @param initialIndex The index of the next binding variable
     * @return The index of the next binding variable
     * @throws SQLException
     * @deprecated - Use
     *             {@link #bindReference(Configuration, PreparedStatement, int)}
     *             instead
     */
    @Deprecated
    int bind(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException;

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement. This always
     * results in calling <code>bind(configuration, stmt, 1)</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param stmt The statement to bind values to
     * @return The index of the next binding variable
     * @throws SQLException
     * @see #bind(Configuration, PreparedStatement, int)
     * @deprecated - 1.6.5 [#758] - Use {@link #bind(BindContext)} instead
     */
    @Deprecated
    int bindReference(Configuration configuration, PreparedStatement stmt) throws SQLException;

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param stmt The statement to bind values to
     * @param initialIndex The index of the next binding variable
     * @return The index of the next binding variable
     * @throws SQLException
     * @deprecated - 1.6.5 [#758] - Use {@link #bind(BindContext)} instead
     */
    @Deprecated
    int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException;

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement. This always
     * results in calling <code>bind(configuration, stmt, 1)</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param stmt The statement to bind values to
     * @return The index of the next binding variable
     * @throws SQLException
     * @see #bind(Configuration, PreparedStatement, int)
     * @deprecated - 1.6.5 [#758] - Use {@link #bind(BindContext)} instead
     */
    @Deprecated
    int bindDeclaration(Configuration configuration, PreparedStatement stmt) throws SQLException;

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param configuration The configuration overriding dialects and schemata
     * @param stmt The statement to bind values to
     * @param initialIndex The index of the next binding variable
     * @return The index of the next binding variable
     * @throws SQLException
     * @deprecated - 1.6.5 [#758] - Use {@link #bind(BindContext)} instead
     */
    @Deprecated
    int bindDeclaration(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException;

    /**
     * Bind all parameters of this QueryPart to a PreparedStatement
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param context The context holding the next bind index and other
     *            information for variable binding
     */
    void bind(BindContext context) throws SQLException;

    /**
     * Reproduce the SQL dialect this QueryPart was created with
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @return The SQL dialect
     */
    SQLDialect getDialect();

    /**
     * Check whether this QueryPart is able to declare fields in a
     * <code>SELECT</code> clause.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean declaresFields();

    /**
     * Check whether this QueryPart is able to declare tables in a
     * <code>FROM</code> clause or <code>JOIN</code> clause.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean declaresTables();
}
