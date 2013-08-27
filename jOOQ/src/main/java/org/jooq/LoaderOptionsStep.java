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
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.sql.Connection;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * Add options to for the loading behaviour
 *
 * @author Lukas Eder
 */
public interface LoaderOptionsStep<R extends TableRecord<R>> extends LoaderSourceStep<R> {

    /**
     * Instruct the <code>Loader</code> to update duplicate records if the main
     * unique key's value is already in the database. This is only supported if
     * {@link InsertQuery#onDuplicateKeyUpdate(boolean)} is supported, too.
     * <p>
     * If the loaded table does not have a primary key, then all records are
     * inserted and this clause behaves like {@link #onDuplicateKeyIgnore()}
     * <p>
     * If you don't specify a behaviour, {@link #onDuplicateKeyError()} will be
     * the default. This cannot be combined with {@link #onDuplicateKeyError()}
     * or {@link #onDuplicateKeyIgnore()}
     */
    @Support({ CUBRID, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    LoaderOptionsStep<R> onDuplicateKeyUpdate();

    /**
     * Instruct the <code>Loader</code> to skip duplicate records if the main
     * unique key's value is already in the database.
     * <p>
     * If the loaded table does not have a primary key, then all records are
     * inserted. This may influence the JDBC driver's outcome on
     * {@link Connection#getWarnings()}, depending on your JDBC driver's
     * implementation
     * <p>
     * If you don't specify a behaviour, {@link #onDuplicateKeyError()} will be
     * the default. This cannot be combined with {@link #onDuplicateKeyError()}
     * or {@link #onDuplicateKeyUpdate()}
     */
    @Support
    LoaderOptionsStep<R> onDuplicateKeyIgnore();

    /**
     * Instruct the <code>Loader</code> to cause an error in loading if there
     * are any duplicate records.
     * <p>
     * If this is combined with {@link #onErrorAbort()} and {@link #commitAll()}
     * in a later step of <code>Loader</code>, then loading is rollbacked on
     * abort.
     * <p>
     * If you don't specify a behaviour, this will be the default. This cannot
     * be combined with {@link #onDuplicateKeyIgnore()} or
     * {@link #onDuplicateKeyUpdate()}
     */
    @Support
    LoaderOptionsStep<R> onDuplicateKeyError();

    /**
     * Instruct the <code>Loader</code> to ignore any errors that might occur
     * when inserting a record. The <code>Loader</code> will then skip the
     * record and try inserting the next one. After loading, you can access
     * errors with {@link Loader#errors()}
     * <p>
     * If you don't specify a behaviour, {@link #onErrorAbort()} will be the
     * default. This cannot be combined with {@link #onErrorAbort()}
     */
    @Support
    LoaderOptionsStep<R> onErrorIgnore();

    /**
     * Instruct the <code>Loader</code> to abort loading after the first error
     * that might occur when inserting a record. After loading, you can access
     * errors with {@link Loader#errors()}
     * <p>
     * If this is combined with {@link #commitAll()} in a later step of
     * <code>Loader</code>, then loading is rollbacked on abort.
     * <p>
     * If you don't specify a behaviour, this will be the default. This cannot
     * be combined with {@link #onErrorIgnore()}
     */
    @Support
    LoaderOptionsStep<R> onErrorAbort();

    /**
     * Commit each loaded record. This will prevent batch <code>INSERT</code>'s
     * altogether. Otherwise, this is the same as calling
     * {@link #commitAfter(int)} with <code>1</code> as parameter.
     * <p>
     * With this clause, errors will never result in a rollback, even when you
     * specify {@link #onDuplicateKeyError()} or {@link #onErrorAbort()}
     * <p>
     * The COMMIT OPTIONS might be useful for fine-tuning performance behaviour
     * in some RDBMS, where large commits lead to a high level of concurrency in
     * the database. Use this on fresh transactions only. Commits/Rollbacks are
     * executed directly upon the connection returned by
     * {@link Configuration#connectionProvider()}. This might not work with
     * container-managed transactions, or when
     * {@link Connection#getAutoCommit()} is set to true.
     * <p>
     * If you don't specify a COMMIT OPTION, {@link #commitNone()} will be the
     * default, leaving transaction handling up to you.
     */
    @Support
    LoaderOptionsStep<R> commitEach();

    /**
     * Commit after a certain number of inserted records. This may enable batch
     * <code>INSERT</code>'s for at most <code>number</code> records.
     * <p>
     * With this clause, errors will never result in a rollback, even when you
     * specify {@link #onDuplicateKeyError()} or {@link #onErrorAbort()}
     * <p>
     * The COMMIT OPTIONS might be useful for fine-tuning performance behaviour
     * in some RDBMS, where large commits lead to a high level of concurrency in
     * the database. Use this on fresh transactions only. Commits/Rollbacks are
     * executed directly upon the connection returned by
     * {@link Configuration#connectionProvider()}. This might not work with
     * container-managed transactions, or when
     * {@link Connection#getAutoCommit()} is set to true.
     * <p>
     * If you don't specify a COMMIT OPTION, {@link #commitNone()} will be the
     * default, leaving transaction handling up to you.
     *
     * @param number The number of records that are committed together.
     */
    @Support
    LoaderOptionsStep<R> commitAfter(int number);

    /**
     * Commit only after inserting all records. If this is used together with
     * {@link #onDuplicateKeyError()} or {@link #onErrorAbort()}, an abort will
     * result in a rollback of previously loaded records.
     * <p>
     * The COMMIT OPTIONS might be useful for fine-tuning performance behaviour
     * in some RDBMS, where large commits lead to a high level of concurrency in
     * the database. Use this on fresh transactions only. Commits/Rollbacks are
     * executed directly upon the connection returned by
     * {@link Configuration#connectionProvider()}. This might not work with
     * container-managed transactions, or when
     * {@link Connection#getAutoCommit()} is set to true.
     * <p>
     * If you don't specify a COMMIT OPTION, {@link #commitNone()} will be the
     * default, leaving transaction handling up to you.
     */
    @Support
    LoaderOptionsStep<R> commitAll();

    /**
     * Leave committing / rollbacking up to client code.
     * <p>
     * The COMMIT OPTIONS might be useful for fine-tuning performance behaviour
     * in some RDBMS, where large commits lead to a high level of concurrency in
     * the database.
     * <p>
     * If you don't specify a COMMIT OPTION, this will be the default, leaving
     * transaction handling up to you. This should be your choice, when you use
     * container-managed transactions, too, or your
     * {@link Connection#getAutoCommit()} value is set to true.
     */
    @Support
    LoaderOptionsStep<R> commitNone();

}
