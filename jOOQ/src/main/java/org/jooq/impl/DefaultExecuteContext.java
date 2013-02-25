/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.Delete;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.Insert;
import org.jooq.Merge;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Routine;
import org.jooq.Truncate;
import org.jooq.Update;

/**
 * A default iplementation for the {@link ExecuteContext}
 *
 * @author Lukas Eder
 */
class DefaultExecuteContext extends AbstractConfiguration implements ExecuteContext {

    /**
     * Generated UID
     */
    private static final long                    serialVersionUID = -6653474082935089963L;

    // Persistent attributes (repeatable)
    private final Query                          query;
    private final Routine<?>                     routine;
    private String                               sql;

    private final Query[]                        batchQueries;
    private final String[]                       batchSQL;

    // Transient attributes (created afresh per execution)
    private transient PreparedStatement          statement;
    private transient ResultSet                  resultSet;
    private transient Record                     record;
    private transient Result<?>                  result;
    private transient SQLException               sqlException;
    private transient RuntimeException           exception;

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling blob / clob lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<List<Blob>> BLOBS            = new ThreadLocal<List<Blob>>();
    private static final ThreadLocal<List<Clob>> CLOBS            = new ThreadLocal<List<Clob>>();

    /**
     * Clean up blobs and clobs.
     * <p>
     * [#1326] This is necessary in those dialects that have long-lived
     * temporary lob objects, which can cause memory leaks in certain contexts,
     * where the lobs' underlying session / connection is long-lived as well.
     * Specifically, Oracle and ojdbc have some trouble when streaming temporary
     * lobs to UDTs:
     * <ol>
     * <li>The lob cannot have a call-scoped life time with UDTs</li>
     * <li>Freeing the lob after binding will cause an ORA-22275</li>
     * <li>Not freeing the lob after execution will cause an
     * {@link OutOfMemoryError}</li>
     * </ol>
     */
    static final void clean() {
        List<Blob> blobs = BLOBS.get();
        List<Clob> clobs = CLOBS.get();

        if (blobs != null) {
            for (Blob blob : blobs) {
                Utils.safeFree(blob);
            }

            BLOBS.remove();
        }

        if (clobs != null) {
            for (Clob clob : clobs) {
                Utils.safeFree(clob);
            }

            CLOBS.remove();
        }
    }

    /**
     * Register a blob for later cleanup with {@link #clean()}
     */
    static final void register(Blob blob) {
        BLOBS.get().add(blob);
    }

    /**
     * Register a clob for later cleanup with {@link #clean()}
     */
    static final void register(Clob clob) {
        CLOBS.get().add(clob);
    }

    // ------------------------------------------------------------------------
    // XXX: Constructors
    // ------------------------------------------------------------------------

    DefaultExecuteContext(Configuration configuration) {
        this(configuration, null, null, null);
    }

    DefaultExecuteContext(Configuration configuration, Query[] batchQueries) {
        this(configuration, null, batchQueries, null);
    }

    DefaultExecuteContext(Configuration configuration, Query query) {
        this(configuration, query, new Query[] { query }, null);
    }

    DefaultExecuteContext(Configuration configuration, Routine<?> routine) {
        this(configuration, null, null, routine);
    }

    private DefaultExecuteContext(Configuration configuration, Query query, Query[] batchQueries, Routine<?> routine) {
        super(configuration);

        this.query = query;
        this.batchQueries = (batchQueries == null ? new Query[0] : batchQueries);
        this.routine = routine;

        if (this.batchQueries.length > 0) {
            this.batchSQL = new String[this.batchQueries.length];
        }
        else if (routine != null) {
            this.batchSQL = new String[1];
        }
        else {
            this.batchSQL = new String[0];
        }

        clean();
        BLOBS.set(new ArrayList<Blob>());
        CLOBS.set(new ArrayList<Clob>());
    }

    @Override
    public final ExecuteType type() {

        // This can only be a routine
        if (routine != null) {
            return ExecuteType.ROUTINE;
        }

        // This can only be a BatchSingle execution
        else if (batchQueries.length == 1 && query == null) {
            return ExecuteType.BATCH;
        }

        // This can only be a BatchMultiple execution
        else if (batchQueries.length > 1) {
            return ExecuteType.BATCH;
        }

        // Any other type of query
        else if (query != null) {
            if (query instanceof ResultQuery) {
                return ExecuteType.READ;
            }
            else if (query instanceof Insert
                || query instanceof Update
                || query instanceof Delete
                || query instanceof Merge) {

                return ExecuteType.WRITE;
            }
            else if (query instanceof Truncate) {
                return ExecuteType.DDL;
            }

            // Analyse SQL in plain SQL queries:
            else {
                String s = query.getSQL().toLowerCase();

                // TODO: Use a simple lexer to parse SQL here. Potentially, the
                // SQL Console's SQL formatter could be used...?
                if (s.matches("^(with\\b.*?\\bselect|select|explain)\\b.*?")) {
                    return ExecuteType.READ;
                }

                // These are sample DML statements. There may be many more
                else if (s.matches("^(insert|update|delete|merge|replace|upsert|lock)\\b.*?")) {
                    return ExecuteType.WRITE;
                }

                // These are only sample DDL statements. There may be many more
                else if (s.matches("^(create|alter|drop|truncate|grant|revoke|analyze|comment|flashback|enable|disable)\\b.*?")) {
                    return ExecuteType.DDL;
                }

                // JDBC escape syntax for routines
                else if (s.matches("^\\s*\\{\\s*(\\?\\s*=\\s*)call.*?")) {
                    return ExecuteType.ROUTINE;
                }

                // Vendor-specific calling of routines / procedural blocks
                else if (s.matches("^(call|begin|declare)\\b.*?")) {
                    return ExecuteType.ROUTINE;
                }
            }
        }

        // Fetching JDBC result sets, e.g. with Factory.fetch(ResultSet)
        else if (resultSet != null) {
            return ExecuteType.READ;
        }

        // No query available
        return ExecuteType.OTHER;
    }

    @Override
    public final Query query() {
        return query;
    }

    @Override
    public final Query[] batchQueries() {
        return batchQueries;
    }

    @Override
    public final Routine<?> routine() {
        return routine;
    }

    @Override
    public final void sql(String s) {
        this.sql = s;

        // If this isn't a batch query
        if (batchSQL.length == 1) {
            batchSQL[0] = s;
        }
    }

    @Override
    public final String sql() {
        return sql;
    }

    @Override
    public final String[] batchSQL() {
        return batchSQL;
    }

    @Override
    public final void statement(PreparedStatement s) {
        this.statement = s;
    }

    @Override
    public final PreparedStatement statement() {
        return statement;
    }

    @Override
    public final void resultSet(ResultSet rs) {
        this.resultSet = rs;
    }

    @Override
    public final ResultSet resultSet() {
        return resultSet;
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final void record(Record r) {
        this.record = r;
    }

    @Override
    public final Record record() {
        return record;
    }

    @Override
    public final void result(Result<?> r) {
        this.result = r;
    }

    @Override
    public final Result<?> result() {
        return result;
    }

    @Override
    public final RuntimeException exception() {
        return exception;
    }

    @Override
    public final void exception(RuntimeException e) {
        this.exception = e;
    }

    @Override
    public final SQLException sqlException() {
        return sqlException;
    }

    @Override
    public final void sqlException(SQLException e) {
        this.sqlException = e;
        exception(Utils.translate(sql(), e));
    }
}
