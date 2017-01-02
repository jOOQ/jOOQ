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
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES_9_5;
// ...
// ...

import java.sql.Connection;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * Add options to for the loading behaviour. For performance reasons, you can
 * fine-tune three different types of measures:
 * <ul>
 * <li><strong>The bulk statement size</strong>. This specifies how many rows
 * will be inserted in a single bulk statement / multi-row <code>INSERT</code>
 * statement.</li>
 * <li><strong>The batch statement size</strong>. This specifies how many bulk
 * statements will be sent to the server as a single JDBC batch statement.</li>
 * <li><strong>The commit size</strong>. This specifies how many batch
 * statements will be committed in a single transaction.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface LoaderOptionsStep<R extends Record> extends LoaderSourceStep<R> {

    // -------------------------------------------------------------------------
    // Duplicate handling
    // -------------------------------------------------------------------------

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
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
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

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Commit strategy
    // -------------------------------------------------------------------------

    /**
     * Commit each batch.
     * <p>
     * This is the same as calling {@link #commitAfter(int)} with <code>1</code>
     * as parameter.
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
     * Commit after a certain number of batches.
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
     * Commit only after inserting all batches. If this is used together with
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

    // -------------------------------------------------------------------------
    // Batch strategy
    // -------------------------------------------------------------------------

    /**
     * Batch all bulk statements in one JDBC batch statement.
     * <p>
     * If {@link #commitEach()} or {@link #commitAfter(int)} are set, this will
     * force the <code>COMMIT</code> option to {@link #commitAll()}.
     */
    @Support
    LoaderOptionsStep<R> batchAll();

    /**
     * Do not batch bulk statements together.
     * <p>
     * If you don't specify a BATCH OPTION, this will be the default.
     */
    @Support
    LoaderOptionsStep<R> batchNone();

    /**
     * Batch a given number of bulk statements together.
     *
     * @param number The number of records that are batched together.
     */
    @Support
    LoaderOptionsStep<R> batchAfter(int number);

    // -------------------------------------------------------------------------
    // Bulk strategy
    // -------------------------------------------------------------------------

    /**
     * Bulk-insert all rows in a single multi-row bulk statement.
     * <p>
     * If {@link #commitEach()} or {@link #commitAfter(int)} are set, this will
     * force the <code>COMMIT</code> option to {@link #commitAll()}.
     */
    @Support
    LoaderOptionsStep<R> bulkAll();

    /**
     * Do not bulk-insert rows in multi-row bulk statements.
     * <p>
     * If you don't specify a BULK OPTION, this will be the default.
     */
    @Support
    LoaderOptionsStep<R> bulkNone();

    /**
     * Bulk-insert a given number of statements in a single multi-row bulk
     * statement.
     * <p>
     * If {@link #commitEach()} is set, each bulk statement will be committed.
     * If {@link #commitAfter(int)} is set, the given number of bulk statements
     * are committed.
     *
     * @param number The number of records that are put together in one bulk
     *            statement.
     */
    @Support
    LoaderOptionsStep<R> bulkAfter(int number);
}
