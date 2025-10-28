/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.EventListener;
import java.util.stream.Collector;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.CallbackExecuteListener;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.tools.LoggerListener;

import org.reactivestreams.Subscriber;

/**
 * An event listener for {@link Query}, {@link Routine}, or {@link ResultSet}
 * render, prepare, bind, execute, fetch steps.
 * <p>
 * <code>ExecuteListener</code> is a base type for loggers, debuggers,
 * profilers, data collectors that can be hooked into a jOOQ {@link DSLContext}
 * using the {@link Configuration#executeListenerProviders()} property, passing
 * <code>Settings</code> to
 * {@link DSL#using(java.sql.Connection, SQLDialect, Settings)}. jOOQ will use
 * that configuration at the beginning of a query execution event to get a hold
 * of all provided listeners via {@link ExecuteListenerProvider#provide()}. The
 * {@link DefaultExecuteListenerProvider} will always return the same
 * {@link ExecuteListener} instance, but user defined providers can define any
 * custom listener lifecycle, e.g. one instance per execution to store state
 * between the moment when a query execution starts, and the moment when a query
 * execution finishes in the listener. Alternatively, such data can be stored in
 * {@link ExecuteContext#data()}.
 * <p>
 * Advanced <code>ExecuteListeners</code> can also provide custom
 * implementations of {@link Connection}, {@link PreparedStatement},
 * {@link ResultSet}, {@link SQLException} or {@link RuntimeException} to jOOQ
 * in appropriate methods.
 * <p>
 * The following table explains how every type of statement / operation invokes
 * callback methods in the correct order for all registered
 * <code>ExecuteListeners</code>. Find a legend below the table for the various
 * use cases.
 * <table border="1">
 * <tr>
 * <th>Callback method</th>
 * <th>Use case [1]</th>
 * <th>Use case [2]</th>
 * <th>Use case [3]</th>
 * <th>Use case [4]</th>
 * <th>Use case [5]</th>
 * <th>Use case [6]</th>
 * </tr>
 * <tr>
 * <td>{@link #start(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #transformStart(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, Nx (for every query)</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #transformEnd(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, Nx (for every query)</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #renderStart(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, Nx (for every query)</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #renderEnd(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, Nx (for every query)</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #prepareStart(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, Nx (for every query)</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #prepareEnd(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, Nx (for every query)</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #bindStart(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes, Nx (for every value set)</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #bindEnd(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes, Nx (for every value set)</td>
 * <td>No</td>
 * <td>Yes, 1
 * <tr>
 * <td>{@link #executeStart(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #executeEnd(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #outStart(ExecuteContext)}</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #outEnd(ExecuteContext)}</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #fetchStart(ExecuteContext)}</td>
 * <td>Yes, 1x (Nx for {@link ResultQuery#fetchMany()}</td>
 * <td>Yes, 1x (Nx for {@link ResultQuery#fetchMany()}</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>{@link #resultStart(ExecuteContext)}</td>
 * <td>Maybe, 1x (Nx for {@link Cursor#fetchNext(int)}</td>
 * <td>Maybe, 1x (Nx for {@link Cursor#fetchNext(int)}</td>
 * <td>Maybe, 1x</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>{@link #recordStart(ExecuteContext)}<br>
 * </td>
 * <td>Yes, Nx</td>
 * <td>Yes, Nx</td>
 * <td>Yes, Nx</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>{@link #recordEnd(ExecuteContext)}</td>
 * <td>Yes, Nx</td>
 * <td>Yes, Nx</td>
 * <td>Yes, Nx</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>{@link #resultEnd(ExecuteContext)}</td>
 * <td>Maybe, 1x (Nx for {@link Cursor#fetchNext(int)}</td>
 * <td>Maybe, 1x (Nx for {@link Cursor#fetchNext(int)}</td>
 * <td>Maybe, 1x</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>{@link #fetchEnd(ExecuteContext)}</td>
 * <td>Yes, 1x (Nx for {@link ResultQuery#fetchMany()}</td>
 * <td>Yes, 1x (Nx for {@link ResultQuery#fetchMany()}</td>
 * <td>Yes, 1x</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>{@link #end(ExecuteContext)}</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * <td>Yes, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #warning(ExecuteContext)}</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * </tr>
 * <tr>
 * <td>{@link #exception(ExecuteContext)}</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * <td>Maybe, 1x</td>
 * </tr>
 * </table>
 * <br>
 * <p>
 * <h5>Legend:</h5>
 * <p>
 * <ol>
 * <li>Used with {@link ResultQuery} of statement type
 * {@link StatementType#PREPARED_STATEMENT}</li>
 * <li>Used with {@link ResultQuery} of statement type
 * {@link StatementType#STATIC_STATEMENT}</li>
 * <li>Used with {@link DSLContext#fetch(ResultSet)} or with
 * {@link InsertResultStep#fetch()}</li>
 * <li>Used with {@link DSLContext#batch(Query)}</li>
 * <li>Used with {@link DSLContext#batch(Query[])}</li>
 * <li>Used with {@link DSLContext#batch(Queries)}</li>
 * <li>Used with a {@link Routine} standalone call</li>
 * </ol>
 * <p>
 * If nothing is specified, the default is to use {@link LoggerListener} as the
 * only event listener, as configured in {@link Settings#isExecuteLogging()}
 *
 * @author Lukas Eder
 */
public interface ExecuteListener extends EventListener, Serializable {

    /**
     * Called to initialise an <code>ExecuteListener</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * </ul>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connectionProvider(ConnectionProvider)}: The
     * connection provider used for execution. This may be particularly
     * interesting if a {@link Query} was de-serialised and is thus lacking the
     * underlying connection</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void start(ExecuteContext ctx) {}

    /**
     * Called before rendering SQL from a <code>QueryPart</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void renderStart(ExecuteContext ctx) {}

    /**
     * Called after rendering SQL from a <code>QueryPart</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#sql(String)}: The rendered <code>SQL</code>
     * statement that is about to be executed. You can modify this statement
     * freely.</li>
     * <li>{@link ExecuteContext#params(Param[])}: Bind values that are to be
     * bound to the {@link PreparedStatement}.</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void renderEnd(ExecuteContext ctx) {}





































































    /**
     * Called before preparing / creating the SQL statement.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#sql(String)}: The rendered <code>SQL</code>
     * statement that is about to be executed. You can modify this statement
     * freely.</li>
     * <li>{@link ExecuteContext#params(Param[])}: Bind values that are to be
     * bound to the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The {@link PreparedStatement}
     * about to be executed. At this stage, no such statement is available yet,
     * but if provided, the execution lifecycle will skip preparing a statement.
     * This can be used e.g. to implement a transaction-bound prepared statement
     * cache.
     * <p>
     * A custom {@link PreparedStatement} needs to take into account
     * {@link Settings#getStatementType()}, and adefault void bind variable
     * markers for {@link StatementType#STATIC_STATEMENT}.
     * <p>
     * Flags such as {@link Query#queryTimeout(int)},
     * {@link Query#poolable(boolean)}, {@link ResultQuery#maxRows(int)} or
     * {@link ResultQuery#largeMaxRows(long)}, which correspond to mutable flags
     * on a {@link PreparedStatement}, are set by jOOQ even if a listener
     * provides the statement.
     * <p>
     * Flags such as {@link ResultQuery#resultSetConcurrency(int)},
     * {@link ResultQuery#resultSetHoldability(int)},
     * {@link ResultQuery#resultSetType(int)}, which correspond to immutable
     * flags that are set on the statement at statement creation are not set on
     * a statement provided by a listener.</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void prepareStart(ExecuteContext ctx) {}

    /**
     * Called after preparing / creating the SQL statement.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#params(Param[])}: Bind values that are to be
     * bound to the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement(PreparedStatement)}: The
     * <code>Statement</code>, <code>PreparedStatement</code>, or
     * <code>CallableStatement</code> that is about to be executed. You can
     * modify this statement freely, or wrap {@link ExecuteContext#statement()}
     * with your enriched statement wrapper</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void prepareEnd(ExecuteContext ctx) {}

    /**
     * Called before bind variables to the <code>PreparedStatement</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#params(Param[])}: Bind values that are to be
     * bound to the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement(PreparedStatement)}: The
     * <code>PreparedStatement</code>, or <code>CallableStatement</code> that is
     * about to be executed. You can modify this statement freely, or wrap
     * {@link ExecuteContext#statement()} with your enriched statement
     * wrapper</li>
     * </ul>
     * <p>
     * Note that this method is not called when executing queries of type
     * {@link StatementType#STATIC_STATEMENT}
     *
     * @param ctx The context containing information about the execution.
     */
    default void bindStart(ExecuteContext ctx) {}

    /**
     * Called after bind variables to the <code>PreparedStatement</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#statement(PreparedStatement)}: The
     * <code>Statement</code>, <code>PreparedStatement</code>, or
     * <code>CallableStatement</code> that is about to be executed. You can
     * modify this statement freely, or wrap {@link ExecuteContext#statement()}
     * with your enriched statement wrapper</li>
     * </ul>
     * <p>
     * Note that this method is not called when executing queries of type
     * {@link StatementType#STATIC_STATEMENT}
     *
     * @param ctx The context containing information about the execution.
     */
    default void bindEnd(ExecuteContext ctx) {}

    /**
     * Called before executing a statement.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#statement(PreparedStatement)}: The
     * <code>Statement</code>, <code>PreparedStatement</code>, or
     * <code>CallableStatement</code> that is about to be executed. You can
     * modify this statement freely, or wrap {@link ExecuteContext#statement()}
     * with your enriched statement wrapper</li>
     * </ul>
     * <p>
     * Other attributes in <code>ExecuteContext</code>, affected by this
     * lifecycle phase:
     * <ul>
     * <li>{@link ExecuteContext#statementExecutionCount()} is incremented.</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void executeStart(ExecuteContext ctx) {}

    /**
     * Called after executing a statement.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * is about to be fetched or <code>null</code>, if the <code>Query</code>
     * returns no result set, or if a <code>Routine</code> is being
     * executed.</li>
     * <li>{@link ExecuteContext#rows()}: The number of affected rows if
     * applicable. In case a {@link ResultSet} is fetched, this number is only
     * available at the {@link #fetchEnd(ExecuteContext)} event.</li>
     * <li>{@link ExecuteContext#serverOutput()}: The server output if
     * available. This may be fetched when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code></li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#resultSet(ResultSet)}: The
     * <code>ResultSet</code> that is about to be fetched. You can modify this
     * result set freely, or wrap {@link ExecuteContext#resultSet()} with your
     * enriched result set wrapper</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void executeEnd(ExecuteContext ctx) {}

    /**
     * Called before fetching out parameter values from a
     * <code>CallableStatement</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * Note that this method is called only when executing standalone routine
     * calls.
     *
     * @param ctx The context containing information about the execution.
     */
    default void outStart(ExecuteContext ctx) {}

    /**
     * Called after fetching out parameter values from a
     * <code>CallableStatement</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * Note that this method is called only when executing standalone routine
     * calls.
     *
     * @param ctx The context containing information about the execution.
     */
    default void outEnd(ExecuteContext ctx) {}

    /**
     * Called before fetching data from a <code>ResultSet</code> into a
     * {@link Result} type.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * is about to be fetched.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * Overridable attributes in <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#resultSet(ResultSet)}: The
     * <code>ResultSet</code> that is about to be fetched. You can modify this
     * result set freely, or wrap {@link ExecuteContext#resultSet()} with your
     * enriched result set wrapper</li>
     * </ul>
     * <p>
     * In case of multiple <code>ResultSets</code> with
     * {@link ResultQuery#fetchMany()}, this is called several times, once per
     * <code>ResultSet</code>
     * <p>
     * Note that this method is not called when executing queries that do not
     * return a result, or when executing routines.
     *
     * @param ctx The context containing information about the execution.
     */
    default void fetchStart(ExecuteContext ctx) {}

    /**
     * Called before fetching a set of records from a <code>ResultSet</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * is about to be fetched.</li>
     * <li>{@link ExecuteContext#result()}: The set of records that are about to
     * be fetched.</li>
     * <li>{@link ExecuteContext#resultLevel()}: The result nesting level, in
     * case the upcoming {@link ExecuteContext#result()} is a
     * {@link DSL#multiset(TableLike)} or other type of nested result.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * <h3>Executions without {@link Result}</h3>
     * <p>
     * Not all types of execution produce results of type {@link Result}. For
     * example, these do not:
     * <ul>
     * <li>{@link ResultQuery#iterator()}</li>
     * <li>{@link ResultQuery#stream()}</li>
     * <li>{@link ResultQuery#collect(Collector)} (including all
     * {@link Collector} based fetches, such as e.g. a
     * {@link ResultQuery#fetchMap(Field, Field)},
     * {@link ResultQuery#fetchGroups(Field, Field)},
     * {@link ResultQuery#fetchSet(Field)}, and all the overloads)</li>
     * <li>{@link Publisher#subscribe(Subscriber)}</li>
     * </ul>
     * In any of these cases, no {@link #resultStart(ExecuteContext)} event is
     * fired.
     * <p>
     * Note that this method is also not called when executing queries that do
     * not return a result, or when executing routines. This is also not called
     * when fetching single records, with {@link Cursor#fetchNext()} for
     * instance.
     *
     * @param ctx The context containing information about the execution.
     */
    default void resultStart(ExecuteContext ctx) {}

    /**
     * Called before fetching a record from a <code>ResultSet</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * is about to be fetched.</li>
     * <li>{@link ExecuteContext#record()}: The <code>Record</code> that is
     * about to be fetched.</li>
     * <li>{@link ExecuteContext#recordLevel()}: The record nesting level, in
     * case the upcoming {@link ExecuteContext#record()} is a {@link Row} or
     * other type of nested record. The level is also increased if a record is
     * contained in a nested {@link ExecuteContext#result()}.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * Note that this method is not called when executing queries that do not
     * return a result, or when executing routines.
     *
     * @param ctx The context containing information about the execution.
     */
    default void recordStart(ExecuteContext ctx) {}

    /**
     * Called after fetching a record from a <code>ResultSet</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * is about to be fetched.</li>
     * <li>{@link ExecuteContext#record()}: The last <code>Record</code> that
     * was fetched.</li>
     * <li>{@link ExecuteContext#recordLevel()}: The record nesting level, in
     * case the upcoming {@link ExecuteContext#record()} is a {@link Row} or
     * other type of nested record. The level is also increased if a record is
     * contained in a nested {@link ExecuteContext#result()}.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * Note that this method is not called when executing queries that do not
     * return a result, or when executing routines.
     *
     * @param ctx The context containing information about the execution.
     */
    default void recordEnd(ExecuteContext ctx) {}

    /**
     * Called after fetching a set of records from a <code>ResultSet</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * </li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * is about to be fetched.</li>
     * <li>{@link ExecuteContext#record()}: The last <code>Record</code> that
     * was fetched.</li>
     * <li>{@link ExecuteContext#result()}: The set of records that were
     * fetched.</li>
     * <li>{@link ExecuteContext#resultLevel()}: The result nesting level, in
     * case the upcoming {@link ExecuteContext#result()} is a
     * {@link DSL#multiset(TableLike)} or other type of nested result.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * <h3>Executions without {@link Result}</h3>
     * <p>
     * Not all types of execution produce results of type {@link Result}. For
     * example, these do not:
     * <ul>
     * <li>{@link ResultQuery#iterator()}</li>
     * <li>{@link ResultQuery#stream()}</li>
     * <li>{@link ResultQuery#collect(Collector)} (including all
     * {@link Collector} based fetches, such as e.g. a
     * {@link ResultQuery#fetchMap(Field, Field)},
     * {@link ResultQuery#fetchGroups(Field, Field)},
     * {@link ResultQuery#fetchSet(Field)}, and all the overloads)</li>
     * <li>{@link Publisher#subscribe(Subscriber)}</li>
     * </ul>
     * In any of these cases, no {@link #resultEnd(ExecuteContext)} event is
     * fired.
     * <p>
     * Note that this method is also not called when executing queries that do
     * not return a result, or when executing routines. This is also not called
     * when fetching single records, with {@link Cursor#fetchNext()} for
     * instance.
     *
     * @param ctx The context containing information about the execution.
     */
    default void resultEnd(ExecuteContext ctx) {}

    /**
     * Called after fetching data from a <code>ResultSet</code>.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * Note that the <code>Statement</code> is already closed!</li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * was fetched. Note that the <code>ResultSet</code> is already closed!</li>
     * <li>{@link ExecuteContext#rows()}: The number of affected rows if
     * applicable.</li>
     * <li>{@link ExecuteContext#serverOutput()}: The server output if
     * available. This may be fetched when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code></li>
     * <li>{@link ExecuteContext#record()}: The last <code>Record</code> that
     * was fetched.</li>
     * <li>{@link ExecuteContext#result()}: The last set of records that were
     * fetched.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     * <p>
     * In case of multiple <code>ResultSets</code> with
     * {@link ResultQuery#fetchMany()}, this is called several times, once per
     * <code>ResultSet</code>
     * <p>
     * Note that this method is not called when executing queries that do not
     * return a result, or when executing routines.
     *
     * @param ctx The context containing information about the execution.
     */
    default void fetchEnd(ExecuteContext ctx) {}

    /**
     * Called at the end of the execution lifecycle.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * Note that the <code>Statement</code> is already closed!</li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * was fetched or <code>null</code>, if no result set was fetched. Note that
     * the <code>ResultSet</code> may already be closed!</li>
     * <li>{@link ExecuteContext#rows()}: The number of affected rows if
     * applicable.</li>
     * <li>{@link ExecuteContext#serverOutput()}: The server output if
     * available. This may be fetched when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code></li>
     * <li>{@link ExecuteContext#record()}: The last <code>Record</code> that
     * was fetched or null if no records were fetched.</li>
     * <li>{@link ExecuteContext#result()}: The last set of records that were
     * fetched or null if no records were fetched.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database or <code>null</code> if no warning was
     * emitted.</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void end(ExecuteContext ctx) {}

    /**
     * Called in the event of an exception at any moment of the execution
     * lifecycle.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * Note that the <code>Statement</code> may be closed!</li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * was fetched or <code>null</code>, if no result set was fetched. Note that
     * the <code>ResultSet</code> may already be closed!</li>
     * <li>{@link ExecuteContext#rows()}: The number of affected rows if
     * applicable.</li>
     * <li>{@link ExecuteContext#serverOutput()}: The server output if
     * available. This may be fetched when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code></li>
     * <li>{@link ExecuteContext#record()}: The last <code>Record</code> that
     * was fetched or null if no records were fetched.</li>
     * <li>{@link ExecuteContext#result()}: The last set of records that were
     * fetched or null if no records were fetched.</li>
     * <li>{@link ExecuteContext#exception()}: The {@link RuntimeException} that
     * is about to be thrown</li>
     * <li>{@link ExecuteContext#sqlException()}: The {@link SQLException} that
     * was thrown by the database</li>
     * </ul>
     *
     * @param ctx The context containing information about the execution.
     */
    default void exception(ExecuteContext ctx) {}

    /**
     * Called in the event of a warning at any moment of the execution
     * lifecycle.
     * <p>
     * Available attributes from <code>ExecuteContext</code>:
     * <ul>
     * <li>{@link ExecuteContext#connection()}: The connection used for
     * execution</li>
     * <li>{@link ExecuteContext#configuration()}: The execution
     * configuration</li>
     * <li>{@link ExecuteContext#query()}: The <code>Query</code> object, if a
     * jOOQ query is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#routine()}: The <code>Routine</code> object, if
     * a jOOQ routine is being executed or <code>null</code> otherwise</li>
     * <li>{@link ExecuteContext#sql()}: The rendered <code>SQL</code> statement
     * that is about to be executed, or <code>null</code> if the
     * <code>SQL</code> statement is unknown.</li>
     * <li>{@link ExecuteContext#params()}: The bind values that are bound to
     * the {@link PreparedStatement}.</li>
     * <li>{@link ExecuteContext#statement()}: The
     * <code>PreparedStatement</code> that is about to be executed, or
     * <code>null</code> if no statement is known to jOOQ. This can be any of
     * the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     * Note that the <code>Statement</code> may be closed!</li>
     * <li>{@link ExecuteContext#resultSet()}: The <code>ResultSet</code> that
     * was fetched or <code>null</code>, if no result set was fetched. Note that
     * the <code>ResultSet</code> may already be closed!</li>
     * <li>{@link ExecuteContext#rows()}: The number of affected rows if
     * applicable.</li>
     * <li>{@link ExecuteContext#serverOutput()}: The server output if
     * available. This may be fetched when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code></li>
     * <li>{@link ExecuteContext#record()}: The last <code>Record</code> that
     * was fetched or null if no records were fetched.</li>
     * <li>{@link ExecuteContext#result()}: The last set of records that were
     * fetched or null if no records were fetched.</li>
     * <li>{@link ExecuteContext#sqlWarning()}: The {@link SQLWarning} that was
     * emitted by the database</li>
     * <li>{@link ExecuteContext#exception()}: The {@link RuntimeException} that
     * is about to be thrown or <code>null</code>, if no exception is being
     * thrown.</li>
     * <li>{@link ExecuteContext#sqlException()}: The {@link SQLException} that
     * was thrown by the database or <code>null</code>, if no exception is being
     * thrown.</li>
     * </ul>
     * <p>
     * This method is only invoked if a warning appears. Note that fetching of
     * warnings can be disabled using {@link Settings#isFetchWarnings()}
     *
     * @param ctx The context containing information about the execution.
     */
    default void warning(ExecuteContext ctx) {}

    /**
     * Create an {@link ExecuteListener} with a {@link #start(ExecuteContext)}
     * implementation.
     */
    static CallbackExecuteListener onStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a {@link #end(ExecuteContext)}
     * implementation.
     */
    static CallbackExecuteListener onEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onEnd(handler);
    }





















    /**
     * Create an {@link ExecuteListener} with a
     * {@link #renderStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onRenderStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onRenderStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #renderEnd(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onRenderEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onRenderEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #prepareStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onPrepareStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onPrepareStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #prepareEnd(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onPrepareEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onPrepareEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #bindStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onBindStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onBindStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a {@link #bindEnd(ExecuteContext)}
     * implementation.
     */
    static CallbackExecuteListener onBindEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onBindEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #executeStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onExecuteStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onExecuteStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #executeEnd(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onExecuteEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onExecuteEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #outStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onOutStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onOutStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a {@link #outEnd(ExecuteContext)}
     * implementation.
     */
    static CallbackExecuteListener onOutEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onOutEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #fetchStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onFetchStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onFetchStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #fetchEnd(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onFetchEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onFetchEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #resultStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onResultStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onResultStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #resultEnd(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onResultEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onResultEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #recordStart(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onRecordStart(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onRecordStart(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #recordEnd(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onRecordEnd(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onRecordEnd(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a
     * {@link #exception(ExecuteContext)} implementation.
     */
    static CallbackExecuteListener onException(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onException(handler);
    }

    /**
     * Create an {@link ExecuteListener} with a {@link #warning(ExecuteContext)}
     * implementation.
     */
    static CallbackExecuteListener onWarning(ExecuteEventHandler handler) {
        return new CallbackExecuteListener().onWarning(handler);
    }
}
