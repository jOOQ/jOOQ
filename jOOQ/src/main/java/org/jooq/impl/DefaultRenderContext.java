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
package org.jooq.impl;

import java.sql.Connection;

import org.jooq.Configuration;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;

/**
 * @author Lukas Eder
 */
class DefaultRenderContext implements RenderContext {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -8358225526567622252L;

    private final Configuration configuration;
    private final StringBuilder sql;
    private boolean             inline;
    private boolean             declareFields;
    private boolean             declareTables;

    DefaultRenderContext(Configuration configuration) {
        this.configuration = configuration;
        this.sql = new StringBuilder();
    }

    DefaultRenderContext(Configuration configuration, boolean inline, boolean declareFields, boolean declareTables) {
        this(configuration);

        this.inline = inline;
        this.declareFields = declareFields;
        this.declareTables = declareTables;
    }

    DefaultRenderContext(RenderContext context) {
        this(context, context.inline(), context.declareFields(), context.declareTables());
    }

    @Override
    public final String render() {
        return sql.toString();
    }

    @Override
    public final String render(QueryPart part) {
        return new DefaultRenderContext(this, inline, declareFields, declareTables).sql(part).render();
    }

    @Override
    public final RenderContext sql(String s) {
        sql.append(s);
        return this;
    }

    @Override
    public final RenderContext sql(char c) {
        sql.append(c);
        return this;
    }

    @Override
    public final RenderContext sql(int i) {
        sql.append(i);
        return this;
    }

    @Override
    public final RenderContext literal(String literal) {
        switch (configuration.getDialect()) {
            case MYSQL:
                sql("`").sql(literal).sql("`");
                break;

            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case ORACLE:
            case POSTGRES:
                sql('"').sql(literal).sql('"');
                break;

            // SQLite is supposed to support all sorts of delimiters, but it
            // seems too buggy
            case SQLITE:
                sql(literal);
                break;

            case SQLSERVER:
            case SYBASE:
                sql("[").sql(literal).sql("]");
                break;

            default:
                sql(literal);
                break;
        }

        return this;
    }

    @Override
    public final RenderContext sql(QueryPart part) {
        QueryPartInternal internal = part.internalAPI(QueryPartInternal.class);

        // If this is supposed to be a declaration section and the part isn't
        // able to declare anything, then disable declaration temporarily

        // We're declaring fields, but "part" does not declare fields
        if (declareFields() && !internal.declaresFields()) {
            declareFields(false);
            internal.toSQL(this);
            declareFields(true);
        }

        // We're declaring tables, but "part" does not declare tables
        else if (declareTables() && !internal.declaresTables()) {
            declareTables(false);
            internal.toSQL(this);
            declareTables(true);
        }

        // We're not declaring, or "part" can declare
        else {
            internal.toSQL(this);
        }

        return this;
    }

    @Override
    public final boolean inline() {
        return inline;
    }

    @Override
    public final RenderContext inline(boolean i) {
        this.inline = i;
        return this;
    }

    @Override
    public final boolean declareFields() {
        return declareFields;
    }

    @Override
    public final RenderContext declareFields(boolean d) {
        this.declareFields = d;
        return this;
    }

    @Override
    public final boolean declareTables() {
        return declareTables;
    }

    @Override
    public final RenderContext declareTables(boolean d) {
        this.declareTables = d;
        return this;
    }

    // ------------------------------------------------------------------------
    // Configuration API
    // ------------------------------------------------------------------------

    @Override
    public final SQLDialect getDialect() {
        return configuration.getDialect();
    }

    @Override
    public final Connection getConnection() {
        return configuration.getConnection();
    }

    @Override
    public final SchemaMapping getSchemaMapping() {
        return configuration.getSchemaMapping();
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("rendering [");
        sb.append(render());
        sb.append("]");
        sb.append("\ninlining  [");
        sb.append(inline);
        sb.append("]\ndeclaring [");

        if (declareFields) {
            sb.append("fields");
        }
        else if (declareTables) {
            sb.append("tables");
        }
        else {
            sb.append("-");
        }

        sb.append("]");

        return sb.toString();
    }
}
