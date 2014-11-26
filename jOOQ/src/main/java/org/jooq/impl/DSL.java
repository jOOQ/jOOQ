/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.impl;

// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.impl.Term.ROW_NUMBER;
import static org.jooq.impl.Utils.combine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.annotation.Generated;
import javax.sql.DataSource;

import org.jooq.AggregateFunction;
import org.jooq.AlterSequenceRestartStep;
import org.jooq.AlterTableStep;
// ...
import org.jooq.Case;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.CreateIndexStep;
import org.jooq.CreateSequenceFinalStep;
import org.jooq.CreateTableAsStep;
import org.jooq.CreateViewAsStep;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Delete;
import org.jooq.DeleteWhereStep;
import org.jooq.DerivedColumnList;
import org.jooq.DropIndexFinalStep;
import org.jooq.DropSequenceFinalStep;
import org.jooq.DropTableStep;
import org.jooq.DropViewFinalStep;
import org.jooq.Field;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.GroupField;
import org.jooq.Insert;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep1;
import org.jooq.InsertValuesStep10;
import org.jooq.InsertValuesStep11;
import org.jooq.InsertValuesStep12;
import org.jooq.InsertValuesStep13;
import org.jooq.InsertValuesStep14;
import org.jooq.InsertValuesStep15;
import org.jooq.InsertValuesStep16;
import org.jooq.InsertValuesStep17;
import org.jooq.InsertValuesStep18;
import org.jooq.InsertValuesStep19;
import org.jooq.InsertValuesStep2;
import org.jooq.InsertValuesStep20;
import org.jooq.InsertValuesStep21;
import org.jooq.InsertValuesStep22;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep4;
import org.jooq.InsertValuesStep5;
import org.jooq.InsertValuesStep6;
import org.jooq.InsertValuesStep7;
import org.jooq.InsertValuesStep8;
import org.jooq.InsertValuesStep9;
import org.jooq.InsertValuesStepN;
import org.jooq.Keyword;
import org.jooq.Merge;
import org.jooq.MergeKeyStep1;
import org.jooq.MergeKeyStep10;
import org.jooq.MergeKeyStep11;
import org.jooq.MergeKeyStep12;
import org.jooq.MergeKeyStep13;
import org.jooq.MergeKeyStep14;
import org.jooq.MergeKeyStep15;
import org.jooq.MergeKeyStep16;
import org.jooq.MergeKeyStep17;
import org.jooq.MergeKeyStep18;
import org.jooq.MergeKeyStep19;
import org.jooq.MergeKeyStep2;
import org.jooq.MergeKeyStep20;
import org.jooq.MergeKeyStep21;
import org.jooq.MergeKeyStep22;
import org.jooq.MergeKeyStep3;
import org.jooq.MergeKeyStep4;
import org.jooq.MergeKeyStep5;
import org.jooq.MergeKeyStep6;
import org.jooq.MergeKeyStep7;
import org.jooq.MergeKeyStep8;
import org.jooq.MergeKeyStep9;
import org.jooq.MergeKeyStepN;
import org.jooq.MergeUsingStep;
import org.jooq.Name;
import org.jooq.OrderedAggregateFunction;
import org.jooq.Param;
import org.jooq.QuantifiedSelect;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.RecordHandler;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Row1;
import org.jooq.Row10;
import org.jooq.Row11;
import org.jooq.Row12;
import org.jooq.Row13;
import org.jooq.Row14;
import org.jooq.Row15;
import org.jooq.Row16;
import org.jooq.Row17;
import org.jooq.Row18;
import org.jooq.Row19;
import org.jooq.Row2;
import org.jooq.Row20;
import org.jooq.Row21;
import org.jooq.Row22;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.Row9;
import org.jooq.RowN;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TruncateIdentityStep;
import org.jooq.UDTRecord;
import org.jooq.Update;
import org.jooq.UpdateSetFirstStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationFinalStep;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.WindowSpecificationRowsStep;
import org.jooq.WithAsStep;
import org.jooq.WithStep;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.types.DayToSecond;

/**
 * A DSL "entry point" providing implementations to the <code>org.jooq</code>
 * interfaces.
 * <p>
 * The {@link DSLContext} and this <code>DSL</code> are the main entry point for
 * client code, to access jOOQ classes and functionality. Here, you can
 * instantiate all of those objects that cannot be accessed through other
 * objects. For example, to create a {@link Field} representing a constant
 * value, you can write:
 * <p>
 * <code><pre>
 * Field&lt;String&gt; field = DSL.val("Hello World")
 * </pre></code>
 * <p>
 * Another example is the <code>EXISTS</code> clause, which you can apply to any
 * <code>SELECT</code> to form a {@link Condition}:
 * <p>
 * <code><pre>
 * Condition condition = DSL.exists(DSL.select(...));
 * </pre></code>
 * <p>
 * <h5>DSL and static imports</h5>
 * <p>
 * For increased fluency and readability of your jOOQ client code, it is
 * recommended that you static import all methods from the <code>DSL</code>. For
 * example: <code><pre>
 * import static org.jooq.impl.DSL.*;
 *
 * public class Main {
 *   public static void main(String[] args) {
 *     DSL.select(val("Hello"), inline("World"));
 *     // DSL.val ^^^           ^^^^^^ DSL.inline
 *   }
 * }
 * </pre></code>
 * <p>
 * In order to use the "contextual DSL", call one of the various overloaded
 * {@link #using(Configuration)} methods:
 * <p>
 * <code><pre>
 * // Create and immediately execute a SELECT statement:
 * DSL.using(connection, dialect)
 *    .selectOne()
 *    .fetch();
 * </pre></code>
 *
 * @see DSLContext
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DSL {

    // -------------------------------------------------------------------------
    // XXX Contextual factory methods
    // -------------------------------------------------------------------------

    /**
     * Create an executor with a dialect configured.
     * <p>
     * Without a connection or data source, this executor cannot execute
     * queries. Use it to render SQL only.
     *
     * @param dialect The dialect to use with objects created from this executor
     */
    public static DSLContext using(SQLDialect dialect) {
        return new DefaultDSLContext(dialect, null);
    }

    /**
     * Create an executor with a dialect and settings configured.
     * <p>
     * Without a connection or data source, this executor cannot execute
     * queries. Use it to render SQL only.
     *
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     */
    public static DSLContext using(SQLDialect dialect, Settings settings) {
        return new DefaultDSLContext(dialect, settings);
    }

    /**
     * Create an executor from a JDBC connection URL.
     * <p>
     * The connections created this way will be closed upon finalization. This
     * is useful for standalone scripts, but not for managed connections.
     *
     * @param url The connection URL.
     * @see DefaultConnectionProvider
     * @see JDBCUtils#dialect(String)
     */
    public static DSLContext using(String url) {
        try {
            Connection connection = DriverManager.getConnection(url);
            return using(new DefaultConnectionProvider(connection, true), JDBCUtils.dialect(connection));
        }
        catch (SQLException e) {
            throw Utils.translate("Error when initialising Connection", e);
        }
    }

    /**
     * Create an executor from a JDBC connection URL.
     * <p>
     * The connections created this way will be closed upon finalization. This
     * is useful for standalone scripts, but not for managed connections.
     *
     * @param url The connection URL.
     * @param username The connection user name.
     * @param password The connection password.
     * @see DefaultConnectionProvider
     * @see JDBCUtils#dialect(String)
     */
    public static DSLContext using(String url, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return using(new DefaultConnectionProvider(connection, true), JDBCUtils.dialect(connection));
        }
        catch (SQLException e) {
            throw Utils.translate("Error when initialising Connection", e);
        }
    }

    /**
     * Create an executor from a JDBC connection URL.
     * <p>
     * The connections created this way will be closed upon finalization. This
     * is useful for standalone scripts, but not for managed connections.
     *
     * @param url The connection URL.
     * @param properties The connection properties.
     * @see DefaultConnectionProvider
     * @see JDBCUtils#dialect(String)
     */
    public static DSLContext using(String url, Properties properties) {
        try {
            Connection connection = DriverManager.getConnection(url, properties);
            return using(new DefaultConnectionProvider(connection, true), JDBCUtils.dialect(connection));
        }
        catch (SQLException e) {
            throw Utils.translate("Error when initialising Connection", e);
        }
    }

    /**
     * Create an executor with a connection configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Configuration, jOOQ will use
     * that connection directly for creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #using(Connection, Settings)}, guessing the {@link SQLDialect}
     * using {@link JDBCUtils#dialect(Connection)}
     *
     * @param connection The connection to use with objects created from this
     *            executor
     * @see DefaultConnectionProvider
     * @see JDBCUtils#dialect(Connection)
     */
    public static DSLContext using(Connection connection) {
        return new DefaultDSLContext(connection, JDBCUtils.dialect(connection), null);
    }

    /**
     * Create an executor with a connection and a dialect configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Configuration, jOOQ will use
     * that connection directly for creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #using(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DefaultConnectionProvider}
     *
     * @param connection The connection to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @see DefaultConnectionProvider
     */
    public static DSLContext using(Connection connection, SQLDialect dialect) {
        return new DefaultDSLContext(connection, dialect, null);
    }

    /**
     * Create an executor with a connection, a dialect and settings configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Configuration, jOOQ will use
     * that connection directly for creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #using(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DefaultConnectionProvider} and guessing the {@link SQLDialect}
     * using {@link JDBCUtils#dialect(Connection)}
     *
     * @param connection The connection to use with objects created from this
     *            executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     * @see DefaultConnectionProvider
     * @see JDBCUtils#dialect(Connection)
     */
    public static DSLContext using(Connection connection, Settings settings) {
        return new DefaultDSLContext(connection, JDBCUtils.dialect(connection), settings);
    }

    /**
     * Create an executor with a connection, a dialect and settings configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Configuration, jOOQ will use
     * that connection directly for creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #using(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DefaultConnectionProvider}
     *
     * @param connection The connection to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     * @see DefaultConnectionProvider
     */
    public static DSLContext using(Connection connection, SQLDialect dialect, Settings settings) {
        return new DefaultDSLContext(connection, dialect, settings);
    }

    /**
     * Create an executor with a data source and a dialect configured.
     * <p>
     * If you provide a JDBC data source to a jOOQ Configuration, jOOQ will use
     * that data source for initialising connections, and creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #using(ConnectionProvider, SQLDialect)} using a
     * {@link DataSourceConnectionProvider}
     *
     * @param datasource The data source to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @see DataSourceConnectionProvider
     */
    public static DSLContext using(DataSource datasource, SQLDialect dialect) {
        return new DefaultDSLContext(datasource, dialect);
    }

    /**
     * Create an executor with a data source, a dialect and settings configured.
     * <p>
     * If you provide a JDBC data source to a jOOQ Configuration, jOOQ will use
     * that data source for initialising connections, and creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #using(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DataSourceConnectionProvider}
     *
     * @param datasource The data source to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     * @see DataSourceConnectionProvider
     */
    public static DSLContext using(DataSource datasource, SQLDialect dialect, Settings settings) {
        return new DefaultDSLContext(datasource, dialect, settings);
    }

    /**
     * Create an executor with a custom connection provider and a dialect
     * configured.
     *
     * @param connectionProvider The connection provider providing jOOQ with
     *            JDBC connections
     * @param dialect The dialect to use with objects created from this executor
     */
    public static DSLContext using(ConnectionProvider connectionProvider, SQLDialect dialect) {
        return new DefaultDSLContext(connectionProvider, dialect);
    }

    /**
     * Create an executor with a custom connection provider, a dialect and settings
     * configured.
     *
     * @param connectionProvider The connection provider providing jOOQ with
     *            JDBC connections
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     */
    public static DSLContext using(ConnectionProvider connectionProvider, SQLDialect dialect, Settings settings) {
        return new DefaultDSLContext(connectionProvider, dialect, settings);
    }

    /**
     * Create an executor from a custom configuration.
     *
     * @param configuration The configuration
     */
    public static DSLContext using(Configuration configuration) {
        return new DefaultDSLContext(configuration);
    }

    // -------------------------------------------------------------------------
    // XXX Static subselect factory methods
    // -------------------------------------------------------------------------

    /**
     * Create a <code>WITH</code> clause to supply subsequent
     * <code>SELECT</code>, <code>UPDATE</code>, <code>INSERT</code>,
     * <code>DELETE</code>, and <code>MERGE</code> statements with
     * {@link CommonTableExpression}s.
     * <p>
     * The <code>RECURSIVE</code> keyword may be optional or unsupported in some
     * databases, in case of which it will not be rendered. For optimal database
     * interoperability and readability, however, it is suggested that you use
     * {@link #with(String)} for strictly non-recursive CTE
     * and {@link #withRecursive(String)} for strictly
     * recursive CTE.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    public static WithAsStep with(String alias) {
        return new WithImpl(null, false).with(alias);
    }

    /**
     * Create a <code>WITH</code> clause to supply subsequent
     * <code>SELECT</code>, <code>UPDATE</code>, <code>INSERT</code>,
     * <code>DELETE</code>, and <code>MERGE</code> statements with
     * {@link CommonTableExpression}s.
     * <p>
     * The <code>RECURSIVE</code> keyword may be optional or unsupported in some
     * databases, in case of which it will not be rendered. For optimal database
     * interoperability and readability, however, it is suggested that you use
     * {@link #with(String, String...)} for strictly non-recursive CTE
     * and {@link #withRecursive(String, String...)} for strictly
     * recursive CTE.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    public static WithAsStep with(String alias, String... fieldAliases) {
        return new WithImpl(null, false).with(alias, fieldAliases);
    }

    /**
     * Create a <code>WITH</code> clause to supply subsequent
     * <code>SELECT</code>, <code>UPDATE</code>, <code>INSERT</code>,
     * <code>DELETE</code>, and <code>MERGE</code> statements with
     * {@link CommonTableExpression}s.
     * <p>
     * Reusable {@link CommonTableExpression} types can be constructed through
     * <ul>
     * <li>{@link #name(String...)}</li>
     * <li>{@link Name#fields(String...)}</li>
     * <li>
     * {@link DerivedColumnList#as(Select)}</li>
     * </ul>
     * <p>
     * The <code>RECURSIVE</code> keyword may be optional or unsupported in some
     * databases, in case of which it will not be rendered. For optimal database
     * interoperability and readability, however, it is suggested that you use
     * {@link #with(CommonTableExpression...)} for strictly non-recursive CTE
     * and {@link #withRecursive(CommonTableExpression...)} for strictly
     * recursive CTE.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    public static WithStep with(CommonTableExpression<?>... tables) {
        return new WithImpl(null, false).with(tables);
    }

    /**
     * Create a <code>WITH</code> clause to supply subsequent
     * <code>SELECT</code>, <code>UPDATE</code>, <code>INSERT</code>,
     * <code>DELETE</code>, and <code>MERGE</code> statements with
     * {@link CommonTableExpression}s.
     * <p>
     * The <code>RECURSIVE</code> keyword may be optional or unsupported in some
     * databases, in case of which it will not be rendered. For optimal database
     * interoperability and readability, however, it is suggested that you use
     * {@link #with(String)} for strictly non-recursive CTE
     * and {@link #withRecursive(String)} for strictly
     * recursive CTE.
     * <p>
     * Note that the {@link SQLDialect#H2} database only supports single-table,
     * <code>RECURSIVE</code> common table expression lists.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static WithAsStep withRecursive(String alias) {
        return new WithImpl(null, true).with(alias);
    }

    /**
     * Create a <code>WITH</code> clause to supply subsequent
     * <code>SELECT</code>, <code>UPDATE</code>, <code>INSERT</code>,
     * <code>DELETE</code>, and <code>MERGE</code> statements with
     * {@link CommonTableExpression}s.
     * <p>
     * The <code>RECURSIVE</code> keyword may be optional or unsupported in some
     * databases, in case of which it will not be rendered. For optimal database
     * interoperability and readability, however, it is suggested that you use
     * {@link #with(String, String...)} for strictly non-recursive CTE
     * and {@link #withRecursive(String, String...)} for strictly
     * recursive CTE.
     * <p>
     * Note that the {@link SQLDialect#H2} database only supports single-table,
     * <code>RECURSIVE</code> common table expression lists.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static WithAsStep withRecursive(String alias, String... fieldAliases) {
        return new WithImpl(null, true).with(alias, fieldAliases);
    }

    /**
     * Create a <code>WITH</code> clause to supply subsequent
     * <code>SELECT</code>, <code>UPDATE</code>, <code>INSERT</code>,
     * <code>DELETE</code>, and <code>MERGE</code> statements with
     * {@link CommonTableExpression}s.
     * <p>
     * Reusable {@link CommonTableExpression} types can be constructed through
     * <ul>
     * <li>{@link #name(String...)}</li>
     * <li>{@link Name#fields(String...)}</li>
     * <li>
     * {@link DerivedColumnList#as(Select)}</li>
     * </ul>
     * <p>
     * The <code>RECURSIVE</code> keyword may be optional or unsupported in some
     * databases, in case of which it will not be rendered. For optimal database
     * interoperability and readability, however, it is suggested that you use
     * {@link #with(CommonTableExpression...)} for strictly non-recursive CTE
     * and {@link #withRecursive(CommonTableExpression...)} for strictly
     * recursive CTE.
     * <p>
     * Note that the {@link SQLDialect#H2} database only supports single-table,
     * <code>RECURSIVE</code> common table expression lists.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static WithStep withRecursive(CommonTableExpression<?>... tables) {
        return new WithImpl(null, true).with(tables);
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * SELECT * FROM [table] WHERE [conditions] ORDER BY [ordering] LIMIT [limit clause]
     * </pre></code>
     */
    @Support
    public static <R extends Record> SelectWhereStep<R> selectFrom(Table<R> table) {
        return new SelectImpl(null, new DefaultConfiguration()).from(table);
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(fields)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Collection)
     */
    @Support
    public static SelectSelectStep<Record> select(Collection<? extends Field<?>> fields) {
        return new SelectImpl(null, new DefaultConfiguration()).select(fields);
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     */
    @Support
    public static SelectSelectStep<Record> select(Field<?>... fields) {
        return new SelectImpl(null, new DefaultConfiguration()).select(fields);
    }

// [jooq-tools] START [select]

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Field#in(Select)}, {@link Field#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1> SelectSelectStep<Record1<T1>> select(Field<T1> field1) {
        return (SelectSelectStep) select(new Field[] { field1 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row2#in(Select)}, {@link Row2#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2> SelectSelectStep<Record2<T1, T2>> select(Field<T1> field1, Field<T2> field2) {
        return (SelectSelectStep) select(new Field[] { field1, field2 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row3#in(Select)}, {@link Row3#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row4#in(Select)}, {@link Row4#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, field4)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row5#in(Select)}, {@link Row5#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, field4, field5)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row6#in(Select)}, {@link Row6#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field5, field6)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row7#in(Select)}, {@link Row7#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field6, field7)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row8#in(Select)}, {@link Row8#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field7, field8)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row9#in(Select)}, {@link Row9#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field8, field9)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row10#in(Select)}, {@link Row10#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field9, field10)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row11#in(Select)}, {@link Row11#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field10, field11)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row12#in(Select)}, {@link Row12#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field11, field12)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row13#in(Select)}, {@link Row13#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field12, field13)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row14#in(Select)}, {@link Row14#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field13, field14)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row15#in(Select)}, {@link Row15#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field14, field15)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row16#in(Select)}, {@link Row16#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field15, field16)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row17#in(Select)}, {@link Row17#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field16, field17)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row18#in(Select)}, {@link Row18#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field17, field18)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row19#in(Select)}, {@link Row19#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field18, field19)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row20#in(Select)}, {@link Row20#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field19, field20)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row21#in(Select)}, {@link Row21#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field20, field21)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it declares
     * additional record-level typesafety, which is needed by
     * {@link Row22#in(Select)}, {@link Row22#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * select(field1, field2, field3, .., field21, field22)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#select(Field...)
     * @see #select(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (SelectSelectStep) select(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

// [jooq-tools] END [select]

    /**
     * Create a new DSL subselect statement.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(fields)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Collection)
     */
    @Support
    public static SelectSelectStep<Record> selectDistinct(Collection<? extends Field<?>> fields) {
        return new SelectImpl(null, new DefaultConfiguration(), true).select(fields);
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     */
    @Support
    public static SelectSelectStep<Record> selectDistinct(Field<?>... fields) {
        return new SelectImpl(null, new DefaultConfiguration(), true).select(fields);
    }

// [jooq-tools] START [selectDistinct]

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Field#in(Select)}, {@link Field#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1> SelectSelectStep<Record1<T1>> selectDistinct(Field<T1> field1) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row2#in(Select)}, {@link Row2#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2> SelectSelectStep<Record2<T1, T2>> selectDistinct(Field<T1> field1, Field<T2> field2) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row3#in(Select)}, {@link Row3#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row4#in(Select)}, {@link Row4#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, field4)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row5#in(Select)}, {@link Row5#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, field4, field5)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row6#in(Select)}, {@link Row6#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field5, field6)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row7#in(Select)}, {@link Row7#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field6, field7)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row8#in(Select)}, {@link Row8#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field7, field8)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row9#in(Select)}, {@link Row9#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field8, field9)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row10#in(Select)}, {@link Row10#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field9, field10)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row11#in(Select)}, {@link Row11#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field10, field11)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row12#in(Select)}, {@link Row12#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field11, field12)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row13#in(Select)}, {@link Row13#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field12, field13)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row14#in(Select)}, {@link Row14#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field13, field14)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row15#in(Select)}, {@link Row15#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field14, field15)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row16#in(Select)}, {@link Row16#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field15, field16)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row17#in(Select)}, {@link Row17#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field16, field17)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row18#in(Select)}, {@link Row18#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field17, field18)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row19#in(Select)}, {@link Row19#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field18, field19)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row20#in(Select)}, {@link Row20#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field19, field20)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row21#in(Select)}, {@link Row21#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field20, field21)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row22#in(Select)}, {@link Row22#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectDistinct(field1, field2, field3, .., field21, field22)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSLContext#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (SelectSelectStep) selectDistinct(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

// [jooq-tools] END [selectDistinct]

    /**
     * Create a new DSL subselect statement for a constant <code>0</code>
     * literal.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectZero()
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#zero()
     * @see DSLContext#selectZero()
     */
    @Support
    public static SelectSelectStep<Record1<Integer>> selectZero() {
        return new SelectImpl(null, new DefaultConfiguration()).select(zero().as("zero"));
    }

    /**
     * Create a new DSL subselect statement for a constant <code>1</code>
     * literal.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectOne()
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#one()
     * @see DSLContext#selectOne()
     */
    @Support
    public static SelectSelectStep<Record1<Integer>> selectOne() {
        return new SelectImpl(null, new DefaultConfiguration()).select(one().as("one"));
    }

    /**
     * Create a new DSL subselect statement for <code>COUNT(*)</code>.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * selectCount()
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#count()
     * @see DSLContext#selectCount()
     */
    @Support
    public static SelectSelectStep<Record1<Integer>> selectCount() {
        return new SelectImpl(null, new DefaultConfiguration()).select(count());
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * This type of insert may feel more convenient to some users, as it uses
     * the <code>UPDATE</code> statement's <code>SET a = b</code> syntax.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table)
     *   .set(field1, value1)
     *   .set(field2, value2)
     *   .newRecord()
     *   .set(field1, value3)
     *   .set(field2, value4)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table)
     */
    @Support
    public static <R extends Record> InsertSetStep<R> insertInto(Table<R> into) {
        return using(new DefaultConfiguration()).insertInto(into);
    }

    // [jooq-tools] START [insert]

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1)
     *   .values(field1)
     *   .values(field1)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1> InsertValuesStep1<R, T1> insertInto(Table<R> into, Field<T1> field1) {
        return (InsertValuesStep1) insertInto(into, new Field[] { field1 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2)
     *   .values(field1, field2)
     *   .values(field1, field2)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2> InsertValuesStep2<R, T1, T2> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2) {
        return (InsertValuesStep2) insertInto(into, new Field[] { field1, field2 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3)
     *   .values(field1, field2, field3)
     *   .values(field1, field2, field3)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3> InsertValuesStep3<R, T1, T2, T3> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (InsertValuesStep3) insertInto(into, new Field[] { field1, field2, field3 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, field4)
     *   .values(field1, field2, field3, field4)
     *   .values(field1, field2, field3, field4)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4> InsertValuesStep4<R, T1, T2, T3, T4> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (InsertValuesStep4) insertInto(into, new Field[] { field1, field2, field3, field4 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, field4, field5)
     *   .values(field1, field2, field3, field4, field5)
     *   .values(field1, field2, field3, field4, field5)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5> InsertValuesStep5<R, T1, T2, T3, T4, T5> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (InsertValuesStep5) insertInto(into, new Field[] { field1, field2, field3, field4, field5 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field5, field6)
     *   .values(valueA1, valueA2, valueA3, .., valueA5, valueA6)
     *   .values(valueB1, valueB2, valueB3, .., valueB5, valueB6)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6> InsertValuesStep6<R, T1, T2, T3, T4, T5, T6> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (InsertValuesStep6) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field6, field7)
     *   .values(valueA1, valueA2, valueA3, .., valueA6, valueA7)
     *   .values(valueB1, valueB2, valueB3, .., valueB6, valueB7)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7> InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (InsertValuesStep7) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field7, field8)
     *   .values(valueA1, valueA2, valueA3, .., valueA7, valueA8)
     *   .values(valueB1, valueB2, valueB3, .., valueB7, valueB8)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (InsertValuesStep8) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field8, field9)
     *   .values(valueA1, valueA2, valueA3, .., valueA8, valueA9)
     *   .values(valueB1, valueB2, valueB3, .., valueB8, valueB9)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (InsertValuesStep9) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field9, field10)
     *   .values(valueA1, valueA2, valueA3, .., valueA9, valueA10)
     *   .values(valueB1, valueB2, valueB3, .., valueB9, valueB10)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (InsertValuesStep10) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field10, field11)
     *   .values(valueA1, valueA2, valueA3, .., valueA10, valueA11)
     *   .values(valueB1, valueB2, valueB3, .., valueB10, valueB11)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (InsertValuesStep11) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field11, field12)
     *   .values(valueA1, valueA2, valueA3, .., valueA11, valueA12)
     *   .values(valueB1, valueB2, valueB3, .., valueB11, valueB12)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (InsertValuesStep12) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field12, field13)
     *   .values(valueA1, valueA2, valueA3, .., valueA12, valueA13)
     *   .values(valueB1, valueB2, valueB3, .., valueB12, valueB13)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (InsertValuesStep13) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field13, field14)
     *   .values(valueA1, valueA2, valueA3, .., valueA13, valueA14)
     *   .values(valueB1, valueB2, valueB3, .., valueB13, valueB14)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (InsertValuesStep14) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field14, field15)
     *   .values(valueA1, valueA2, valueA3, .., valueA14, valueA15)
     *   .values(valueB1, valueB2, valueB3, .., valueB14, valueB15)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (InsertValuesStep15) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field15, field16)
     *   .values(valueA1, valueA2, valueA3, .., valueA15, valueA16)
     *   .values(valueB1, valueB2, valueB3, .., valueB15, valueB16)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (InsertValuesStep16) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field16, field17)
     *   .values(valueA1, valueA2, valueA3, .., valueA16, valueA17)
     *   .values(valueB1, valueB2, valueB3, .., valueB16, valueB17)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (InsertValuesStep17) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field17, field18)
     *   .values(valueA1, valueA2, valueA3, .., valueA17, valueA18)
     *   .values(valueB1, valueB2, valueB3, .., valueB17, valueB18)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (InsertValuesStep18) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field18, field19)
     *   .values(valueA1, valueA2, valueA3, .., valueA18, valueA19)
     *   .values(valueB1, valueB2, valueB3, .., valueB18, valueB19)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (InsertValuesStep19) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field19, field20)
     *   .values(valueA1, valueA2, valueA3, .., valueA19, valueA20)
     *   .values(valueB1, valueB2, valueB3, .., valueB19, valueB20)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (InsertValuesStep20) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field20, field21)
     *   .values(valueA1, valueA2, valueA3, .., valueA20, valueA21)
     *   .values(valueB1, valueB2, valueB3, .., valueB20, valueB21)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (InsertValuesStep21) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2, field3, .., field21, field22)
     *   .values(valueA1, valueA2, valueA3, .., valueA21, valueA22)
     *   .values(valueB1, valueB2, valueB3, .., valueB21, valueB22)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (InsertValuesStep22) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

// [jooq-tools] END [insert]

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2)
     *   .values(valueA1, valueA2)
     *   .values(valueB1, valueB2)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Field...)
     */
    @Support
    public static <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Field<?>... fields) {
        return using(new DefaultConfiguration()).insertInto(into, fields);
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>INSERT</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * insertInto(table, field1, field2)
     *   .values(valueA1, valueA2)
     *   .values(valueB1, valueB2)
     *   .onDuplicateKeyUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     * </pre></code>
     *
     * @see DSLContext#insertInto(Table, Collection)
     */
    @Support
    public static <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return using(new DefaultConfiguration()).insertInto(into, fields);
    }

    /**
     * Create a new DSL update statement.
     * <p>
     * Unlike {@link Update} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>UPDATE</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * update(table)
     *   .set(field1, value1)
     *   .set(field2, value2)
     *   .where(field1.greaterThan(100))
     * </pre></code>
     * <p>
     * Note that some databases support table expressions more complex than
     * simple table references. In CUBRID and MySQL, for instance, you can write
     * <code><pre>
     * update(t1.join(t2).on(t1.id.eq(t2.id)))
     *   .set(t1.value, value1)
     *   .set(t2.value, value2)
     *   .where(t1.id.eq(10))
     * </pre></code>
     */
    @Support
    public static <R extends Record> UpdateSetFirstStep<R> update(Table<R> table) {
        return using(new DefaultConfiguration()).update(table);
    }

    /**
     * Create a new DSL SQL standard MERGE statement.
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <th>dialect</th>
     * <th>support type</th>
     * <th>documentation</th>
     * </tr>
     * <tr>
     * <td>CUBRID</td>
     * <td>SQL:2008 standard and some enhancements</td>
     * <td><a href="http://www.cubrid.org/manual/90/en/MERGE"
     * >http://www.cubrid.org/manual/90/en/MERGE</a></td>
     * </tr>
     * <tr>
     * <tr>
     * <td>DB2</td>
     * <td>SQL:2008 standard and major enhancements</td>
     * <td><a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.admin.doc/doc/r0010873.htm"
     * >http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.
     * ibm.db2.udb.admin.doc/doc/r0010873.htm</a></td>
     * </tr>
     * <tr>
     * <td>HSQLDB</td>
     * <td>SQL:2008 standard</td>
     * <td><a
     * href="http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA"
     * >http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA</a></td>
     * </tr>
     * <tr>
     * <td>Oracle</td>
     * <td>SQL:2008 standard and minor enhancements</td>
     * <td><a href=
     * "http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/statements_9016.htm"
     * >http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/
     * statements_9016.htm</a></td>
     * </tr>
     * <tr>
     * <td>SQL Server</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href= "http://msdn.microsoft.com/de-de/library/bb510625.aspx"
     * >http://msdn.microsoft.com/de-de/library/bb510625.aspx</a></td>
     * </tr>
     * <tr>
     * <td>Sybase</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href=
     * "http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html"
     * >http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html</a></td>
     * </tr>
     * </table>
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * mergeInto(table)
     *   .using(select)
     *   .on(condition)
     *   .whenMatchedThenUpdate()
     *   .set(field1, value1)
     *   .set(field2, value2)
     *   .whenNotMatchedThenInsert(field1, field2)
     *   .values(value1, value2)
     * </pre></code>
     * <p>
     * Note: Using this method, you can also create an H2-specific MERGE
     * statement without field specification. See also
     * {@link #mergeInto(Table, Field...)}
     *
     * @see DSLContext#mergeInto(Table)
     */
    @Support({ CUBRID, HSQLDB })
    public static <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return using(new DefaultConfiguration()).mergeInto(table);
    }

    // [jooq-tools] START [merge]

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1> MergeKeyStep1<R, T1> mergeInto(Table<R> table, Field<T1> field1) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2> MergeKeyStep2<R, T1, T2> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3> MergeKeyStep3<R, T1, T2, T3> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4> MergeKeyStep4<R, T1, T2, T3, T4> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5> MergeKeyStep5<R, T1, T2, T3, T4, T5> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6> MergeKeyStep6<R, T1, T2, T3, T4, T5, T6> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7> MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
    	return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
    }

// [jooq-tools] END [merge]

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Field<?>... fields) {
        return using(new DefaultConfiguration()).mergeInto(table, fields);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>MERGE</code> statement.
     *
     * @see DSLContext#mergeInto(Table, Collection)
     */
    @Support({ CUBRID, H2, HSQLDB })
    public static <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields) {
        return using(new DefaultConfiguration()).mergeInto(table, fields);
    }

    /**
     * Create a new DSL delete statement.
     * <p>
     * Unlike {@link Delete} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>DELETE</code> statement.
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * delete(table)
     *   .where(field1.greaterThan(100))
     * </pre></code>
     * <p>
     * Some but not all databases support aliased tables in delete statements.
     *
     * @see DSLContext#delete(Table)
     */
    @Support
    public static <R extends Record> DeleteWhereStep<R> delete(Table<R> table) {
        return using(new DefaultConfiguration()).delete(table);
    }

    // -------------------------------------------------------------------------
    // XXX DDL Statements
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTable(String)
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateTableAsStep<Record> createTable(String tableName) {
        return createTable(tableByName(tableName));
    }

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTable(Table)
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateTableAsStep<Record> createTable(Table<?> table) {
        return using(new DefaultConfiguration()).createTable(table);
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     *
     * @see DSLContext#createView(String, String...)
     */
    @Support
    public static CreateViewAsStep createView(String viewName, String... fieldNames) {
        return createView(tableByName(viewName), Utils.fieldsByName(viewName, fieldNames));
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     *
     * @see DSLContext#createView(Table, Field...)
     */
    @Support
    public static CreateViewAsStep createView(Table<?> view, Field<?>... fields) {
        return using(new DefaultConfiguration()).createView(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE INDEX</code> statement.
     *
     * @see DSLContext#createIndex(String)
     */
    @Support
    public static CreateIndexStep createIndex(String index) {
        return using(new DefaultConfiguration()).createIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE</code> statement.
     *
     * @see DSLContext#createSequence(Sequence)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    public static CreateSequenceFinalStep createSequence(Sequence<?> sequence) {
        return using(new DefaultConfiguration()).createSequence(sequence);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE</code> statement.
     *
     * @see DSLContext#createSequence(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    public static CreateSequenceFinalStep createSequence(String sequence) {
        return using(new DefaultConfiguration()).createSequence(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequence(Sequence)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static <T extends Number> AlterSequenceRestartStep<T> alterSequence(Sequence<T> sequence) {
        return using(new DefaultConfiguration()).alterSequence(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequence(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static AlterSequenceRestartStep<BigInteger> alterSequence(String sequence) {
        return using(new DefaultConfiguration()).alterSequence(sequence);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTable(Table)
     */
    @Support
    public static AlterTableStep alterTable(Table<?> table) {
        return using(new DefaultConfiguration()).alterTable(table);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTable(String)
     */
    @Support
    public static AlterTableStep alterTable(String table) {
        return using(new DefaultConfiguration()).alterTable(table);
    }

    /**
     * Create a new DSL <code>DROP VIEW</code> statement.
     *
     * @see DSLContext#dropView(Table)
     */
    @Support
    public static DropViewFinalStep dropView(Table<?> table) {
        return using(new DefaultConfiguration()).dropView(table);
    }

    /**
     * Create a new DSL <code>DROP VIEW</code> statement.
     *
     * @see DSLContext#dropView(String)
     */
    @Support
    public static DropViewFinalStep dropView(String table) {
        return using(new DefaultConfiguration()).dropView(table);
    }

    /**
     * Create a new DSL <code>DROP VIEW IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropViewIfExists(Table)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static DropViewFinalStep dropViewIfExists(Table<?> table) {
        return using(new DefaultConfiguration()).dropViewIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP VIEW IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropViewIfExists(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static DropViewFinalStep dropViewIfExists(String table) {
        return using(new DefaultConfiguration()).dropViewIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE</code> statement.
     *
     * @see DSLContext#dropTable(Table)
     */
    @Support
    public static DropTableStep dropTable(Table<?> table) {
        return using(new DefaultConfiguration()).dropTable(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE</code> statement.
     *
     * @see DSLContext#dropTable(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static DropTableStep dropTableIfExists(String table) {
        return using(new DefaultConfiguration()).dropTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropTableIfExists(Table)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static DropTableStep dropTableIfExists(Table<?> table) {
        return using(new DefaultConfiguration()).dropTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropTableIfExists(String)
     */
    @Support
    public static DropTableStep dropTable(String table) {
        return using(new DefaultConfiguration()).dropTable(table);
    }

    /**
     * Create a new DSL <code>DROP INDEX</code> statement.
     *
     * @see DSLContext#dropIndex(String)
     */
    @Support
    public static DropIndexFinalStep dropIndex(String index) {
        return using(new DefaultConfiguration()).dropIndex(index);
    }

    /**
     * Create a new DSL <code>DROP INDEX IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropIndexIfExists(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static DropIndexFinalStep dropIndexIfExists(String index) {
        return using(new DefaultConfiguration()).dropIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE</code> statement.
     *
     * @see DSLContext#dropSequence(Sequence)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequence(Sequence<?> sequence) {
        return using(new DefaultConfiguration()).dropSequence(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE</code> statement.
     *
     * @see DSLContext#dropSequence(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequence(String sequence) {
        return using(new DefaultConfiguration()).dropSequence(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropSequenceIfExists(Sequence)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequenceIfExists(Sequence<?> sequence) {
        return using(new DefaultConfiguration()).dropSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropSequenceIfExists(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequenceIfExists(String sequence) {
        return using(new DefaultConfiguration()).dropSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Unlike {@link Delete} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>DELETE</code> statement.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * // [...]
     *
     * truncate(table);
     * </pre></code>
     * <h3>Simulation of <code>TRUNCATE</code></h3>
     * <p>
     * Most dialects implement the <code>TRUNCATE</code> statement. If it is not
     * supported, it is simulated using an equivalent <code>DELETE</code>
     * statement. This is particularly true for these dialects:
     * <ul>
     * <li> {@link SQLDialect#FIREBIRD}</li>
     * <li> {@link SQLDialect#INGRES}</li>
     * <li> {@link SQLDialect#SQLITE}</li>
     * </ul>
     * <h3>Vendor-specific extensions of <code>TRUNCATE</code></h3>
     * <p>
     * Some statements also support extensions of the <code>TRUNCATE</code>
     * statement, such as Postgres:
     * <p>
     * <code><pre>
     * truncate(table)
     *   .restartIdentity()
     *   .cascade()
     * </pre></code>
     * <p>
     * These vendor-specific extensions are currently not simulated for those
     * dialects that do not support them natively.
     *
     * @see DSLContext#truncate(Table)
     */
    @Support
    public static <R extends Record> TruncateIdentityStep<R> truncate(Table<R> table) {
        return using(new DefaultConfiguration()).truncate(table);
    }

    // -------------------------------------------------------------------------
    // XXX Quantified comparison predicate expressions
    // -------------------------------------------------------------------------

    /**
     * Create an <code>ALL</code> quantified select to be used in quantified
     * comparison predicate expressions.
     *
     * @see Field#equal(QuantifiedSelect)
     * @see Field#notEqual(QuantifiedSelect)
     * @see Field#greaterThan(QuantifiedSelect)
     * @see Field#greaterOrEqual(QuantifiedSelect)
     * @see Field#lessThan(QuantifiedSelect)
     * @see Field#lessOrEqual(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> QuantifiedSelect<R> all(Select<R> select) {
        return new QuantifiedSelectImpl<R>(Quantifier.ALL, select);
    }

    /**
     * Create an <code>ALL</code> quantified select to be used in quantified
     * comparison predicate expressions.
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     *
     * @see Field#equal(QuantifiedSelect)
     * @see Field#notEqual(QuantifiedSelect)
     * @see Field#greaterThan(QuantifiedSelect)
     * @see Field#greaterOrEqual(QuantifiedSelect)
     * @see Field#lessThan(QuantifiedSelect)
     * @see Field#lessOrEqual(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> all(T... array) {
        return all(val(array));
    }

    /**
     * Create an <code>ALL</code> quantified select to be used in quantified
     * comparison predicate expressions.
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     *
     * @see Field#equal(QuantifiedSelect)
     * @see Field#notEqual(QuantifiedSelect)
     * @see Field#greaterThan(QuantifiedSelect)
     * @see Field#greaterOrEqual(QuantifiedSelect)
     * @see Field#lessThan(QuantifiedSelect)
     * @see Field#lessOrEqual(QuantifiedSelect)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> all(Field<T[]> array) {
        return new QuantifiedSelectImpl<Record1<T>>(Quantifier.ALL, array);
    }

    /**
     * Create an <code>ANY</code> quantified select to be used in quantified
     * comparison predicate expressions.
     *
     * @see Field#equal(QuantifiedSelect)
     * @see Field#notEqual(QuantifiedSelect)
     * @see Field#greaterThan(QuantifiedSelect)
     * @see Field#greaterOrEqual(QuantifiedSelect)
     * @see Field#lessThan(QuantifiedSelect)
     * @see Field#lessOrEqual(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> QuantifiedSelect<R> any(Select<R> select) {
        return new QuantifiedSelectImpl<R>(Quantifier.ANY, select);
    }

    /**
     * Create an <code>ANY</code> quantified select to be used in quantified
     * comparison predicate expressions.
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     *
     * @see Field#equal(QuantifiedSelect)
     * @see Field#notEqual(QuantifiedSelect)
     * @see Field#greaterThan(QuantifiedSelect)
     * @see Field#greaterOrEqual(QuantifiedSelect)
     * @see Field#lessThan(QuantifiedSelect)
     * @see Field#lessOrEqual(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> any(T... array) {
        return any(val(array));
    }

    /**
     * Create an <code>ANY</code> quantified select to be used in quantified
     * comparison predicate expressions.
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     *
     * @see Field#equal(QuantifiedSelect)
     * @see Field#notEqual(QuantifiedSelect)
     * @see Field#greaterThan(QuantifiedSelect)
     * @see Field#greaterOrEqual(QuantifiedSelect)
     * @see Field#lessThan(QuantifiedSelect)
     * @see Field#lessOrEqual(QuantifiedSelect)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> any(Field<T[]> array) {
        return new QuantifiedSelectImpl<Record1<T>>(Quantifier.ANY, array);
    }

    // -------------------------------------------------------------------------
    // XXX Conversion of objects into tables
    // -------------------------------------------------------------------------

    /**
     * A synonym for {@link Select#asTable()}, which might look a bit more fluent
     * like this, to some users.
     *
     * @see Select#asTable()
     */
    @Support
    public static <R extends Record> Table<R> table(Select<R> select) {
        return select.asTable();
    }

    /**
     * Use a previously obtained result as a new {@link Table} that can be used
     * in SQL statements through {@link #values(RowN...)}.
     *
     * @see #values(RowN...)
     */
    @Support
    public static <R extends Record> Table<R> table(Result<R> result) {
        int size = result.size();

        RowN[] rows = new RowN[size];
        for (int i = 0; i < size; i++)
            rows[i] = (RowN) result.get(i).valuesRow();

        Field<?>[] fields = result.fields();
        String[] columns = new String[fields.length];
        for (int i = 0; i < fields.length; i++)
            columns[i] = fields[i].getName();

        // TODO [#2986] Coerce the record type upon the resulting table.
        return (Table<R>) values(rows).as("v", columns);
    }

    /**
     * A synonym for {@link #unnest(List)}.
     *
     * @see #unnest(List)
     */
    @Support
    public static Table<?> table(List<?> list) {
        return table(list.toArray());
    }

    /**
     * A synonym for {@link #unnest(Object[])}.
     *
     * @see #unnest(Object[])
     */
    @Support
    public static Table<?> table(Object[] array) {
        return table(val(array));
    }

    /* [pro] xx
    xxx
     x x xxxxxxx xxx xxxxxx xxxxxxxxxxxxxxxxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxx xxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxx
    x

    xx [/pro] */
    /**
     * A synonym for {@link #unnest(Field)}.
     *
     * @see #unnest(Field)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Table<?> table(Field<?> cursor) {
        return unnest(cursor);
    }

    /**
     * Create a table from a list of values.
     * <p>
     * This is equivalent to the <code>TABLE</code> function for H2, or the
     * <code>UNNEST</code> function in HSQLDB and Postgres
     * <p>
     * For Oracle, use {@link #table(ArrayRecord)} instead, as Oracle knows only
     * typed arrays
     * <p>
     * In all other dialects, unnesting of arrays is simulated using several
     * <code>UNION ALL</code> connected subqueries.
     */
    @Support
    public static Table<?> unnest(List<?> list) {
        return unnest(list.toArray());
    }

    /**
     * Create a table from an array of values.
     * <p>
     * This is equivalent to the <code>TABLE</code> function for H2, or the
     * <code>UNNEST</code> function in HSQLDB and Postgres
     * <p>
     * For Oracle, use {@link #table(ArrayRecord)} instead, as Oracle knows only
     * typed arrays
     * <p>
     * In all other dialects, unnesting of arrays is simulated using several
     * <code>UNION ALL</code> connected subqueries.
     */
    @Support
    public static Table<?> unnest(Object[] array) {
        return unnest(val(array));
    }

    /* [pro] xx
    xxx
     x xxxxxx x xxxxx xxxx xx xxxxx xx xxxxxxx
     x xxx
     x xxxx xxxxx xxx xxxxxxxx xxxxx xx x xxxxxxxxxxxxxxxxxx xxxxxxxx xxx
     x xxxxxxx xxxxxxxxxx xxxx xxxxxx xxxxx xxxxx xxxxxx
     xx
    xxxxxxxxxxxxxxxx
    xxxxxx xxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxx
    x

    xx [/pro] */
    /**
     * Create a table from a field.
     * <p>
     * The supplied field can have any of these types:
     * <ul>
     * <li> {@link Result}: For <code>CURSOR</code> or <code>REF CURSOR</code>
     * fields, typically fetched from stored functions or from nested tables</li>
     * <li> {@link ArrayRecord}: For Oracle-style <code>VARRAY</code> types.</li>
     * <li> {@link Object}[]: Array types, for other RDBMS's ARRAY types (e.g.
     * H2, HSQLDB, and Postgres)</li>
     * <li> {@link Object}: Any other type that jOOQ will try to convert in an
     * array first, before converting that array into a table</li>
     * </ul>
     * <p>
     * This functionality has only limited scope when used in H2, as ARRAY types
     * involved with stored functions can only be of type <code>Object[]</code>.
     * Such arrays are converted into <code>VARCHAR</code> arrays by jOOQ.
     * <p>
     * In all dialects where arrays are not supported, unnesting of arrays is
     * simulated using several <code>UNION ALL</code> connected subqueries.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Table<?> unnest(Field<?> cursor) {
        if (cursor == null) {
            throw new IllegalArgumentException();
        }

        // The field is an actual CURSOR or REF CURSOR returned from a stored
        // procedure or from a NESTED TABLE
        else if (cursor.getType() == Result.class) {
            return new FunctionTable<Record>(cursor);
        }

        /* [pro] xx
        xx xxx xxxxx xx xx xxxxxxxxxxxx xxxxxx xxxxxxxx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxx xxx xxxxxxxxxxxxxxxxxxx
        x

        xx xxx xxxxx xx xx xxxxxxxxxxxx xxxxxx xxxxx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxx xxx xxxxxxxxxxxxxxxxxxx
        x

        xx [/pro] */
        // The field is a regular array
        else if (cursor.getType().isArray() && cursor.getType() != byte[].class) {
            return new ArrayTable(cursor);
        }

        // The field has any other type. Try to make it an array
        throw new SQLDialectNotSupportedException("Converting arbitrary types into array tables is currently not supported");
    }

    // -------------------------------------------------------------------------
    // XXX Table functions
    // -------------------------------------------------------------------------

    /**
     * The <code>DUAL</code> table to be used for syntactic completeness.
     * <p>
     * In general, jOOQ will generate a <code>DUAL</code> table (or any
     * equivalent construct, see below), if this is needed syntactically in
     * generated SQL. You may still wish to explicitly use a <code>DUAL</code>
     * construct in jOOQ code for various reasons. This <code>DUAL</code> table
     * will always contain a single record with a single varchar(1) value:
     * <code><pre>
     * +-------+
     * | DUMMY |
     * +-------+
     * | X     |
     * +-------+
     * </pre></code>
     */
    @Support
    public static Table<Record> dual() {
        return new Dual(true);
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive).
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL - 1 FROM DUAL CONNECT BY a + LEVEL - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(int from, int to) {
        return generateSeries(val(from), val(to));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive).
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL - 1 FROM DUAL CONNECT BY a + LEVEL - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(int from, Field<Integer> to) {
        return generateSeries(val(from), nullSafe(to));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive).
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL - 1 FROM DUAL CONNECT BY a + LEVEL - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(Field<Integer> from, int to) {
        return new GenerateSeries(nullSafe(from), val(to));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive).
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL - 1 FROM DUAL CONNECT BY a + LEVEL - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(Field<Integer> from, Field<Integer> to) {
        return new GenerateSeries(nullSafe(from), nullSafe(to));
    }

    /**
     * Create a <code>LATERAL</code> joined table.
     * <p>
     * Example:
     * <code><pre>
     * SELECT *
     * FROM employees e,
     *      LATERAL(SELECT * FROM departments d
     *              WHERE e.department_id = d.department_id);
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <R extends Record> Table<R> lateral(TableLike<R> table) {
        return new Lateral<R>(table.asTable());
    }

    // -------------------------------------------------------------------------
    // XXX SQL keywords
    // -------------------------------------------------------------------------

    /**
     * Create a SQL keyword.
     * <p>
     * A <code>Keyword</code> is a {@link QueryPart} that renders a SQL keyword
     * according to the settings specified in
     * {@link Settings#getRenderKeywordStyle()}. It can be embedded in other
     * plain SQL <code>QueryParts</code> as shown in this example:
     * <p>
     * <code><pre>
     * Condition c = condition("{0} {1} {2} {3} {4}",
     *     value1, keyword("between")
     *     value2, keyword("and")
     *     value3
     * );
     * </pre></code>
     */
    public static Keyword keyword(String keyword) {
        return new KeywordImpl(keyword);
    }

    // -------------------------------------------------------------------------
    // XXX SQL identifiers
    // -------------------------------------------------------------------------

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     * <p>
     * While the <code>DEFAULT</code> keyword works with all data types, you may
     * still prefer to associate a {@link Field} type with your
     * <code>DEFAULT</code> value. In that case, use
     * {@link #defaultValue(Class)} or {@link #defaultValue(DataType)} instead.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Object> defaultValue() {
        return defaultValue(Object.class);
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> Field<T> defaultValue(Class<T> type) {
        return defaultValue(getDataType(type));
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> Field<T> defaultValue(DataType<T> type) {
        return new SQLField<T>(type, keyword("default"));
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> Field<T> defaultValue(Field<T> field) {
        return new SQLField<T>(field.getDataType(), keyword("default"));
    }

    /**
     * Create a new SQL identifier using a qualified name.
     * <p>
     * Use this method to construct syntax-safe, SQL-injection-safe SQL
     * identifiers for use in plain SQL where {@link QueryPart} objects are
     * accepted. For instance, this can be used with any of these methods:
     * <ul>
     * <li> {@link #field(String, QueryPart...)}</li>
     * <li> {@link #field(String, Class, QueryPart...)}</li>
     * <li> {@link #field(String, DataType, QueryPart...)}</li>
     * </ul>
     * <p>
     * An example: <code><pre>
     * // This qualified name here
     * name("book", "title");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [book].[title]
     * </pre></code>
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    public static Name name(String... qualifiedName) {
        return new NameImpl(qualifiedName);
    }

    /**
     * Create a qualified schema, given its schema name.
     * <p>
     * This constructs a schema reference given the schema's qualified name.
     * jOOQ will render the schema name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This schema...
     * schemaByName("MY_SCHEMA");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA]
     * </pre></code>
     *
     * @param name The schema's reference name.
     * @return A schema referenced by <code>name</code>
     */
    @Support
    public static Schema schemaByName(String name) {
        return new SchemaImpl(name);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * jOOQ will render the sequence name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequenceByName("MY_SCHEMA", "MY_SEQUENCE");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your sequence's
     *            reference name.
     * @return A sequence referenced by <code>sequenceName</code>
     */
    @Support
    public static Sequence<BigInteger> sequenceByName(String... qualifiedName) {
        return sequenceByName(BigInteger.class, qualifiedName);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * jOOQ will render the sequence name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequenceByName("MY_SCHEMA", "MY_SEQUENCE");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your sequence's
     *            reference name.
     * @param type The type of the returned field
     * @return A sequence referenced by <code>sequenceName</code>
     */
    @Support
    public static <T extends Number> Sequence<T> sequenceByName(Class<T> type, String... qualifiedName) {
        return sequenceByName(getDataType(type), qualifiedName);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * jOOQ will render the sequence name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequenceByName("MY_SCHEMA", "MY_SEQUENCE");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your sequence's
     *            reference name.
     * @param type The type of the returned field
     * @return A sequence referenced by <code>sequenceName</code>
     */
    @Support
    public static <T extends Number> Sequence<T> sequenceByName(DataType<T> type, String... qualifiedName) {
        if (qualifiedName == null)
            throw new NullPointerException();

        if (qualifiedName.length < 1 || qualifiedName.length > 2)
            throw new IllegalArgumentException("Must provide a qualified name of length 1 or 2 : " + name(qualifiedName));

        String name = qualifiedName[qualifiedName.length - 1];
        Schema schema = qualifiedName.length == 2 ? schemaByName(qualifiedName[0]) : null;

        return new SequenceImpl<T>(name, schema, type);
    }

    /**
     * Create a qualified table, given its table name.
     * <p>
     * This constructs a table reference given the table's qualified name. jOOQ
     * will render the table name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This table...
     * tableByName("MY_SCHEMA", "MY_TABLE");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_TABLE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your table's reference
     *            name.
     * @return A table referenced by <code>tableName</code>
     */
    @Support
    public static Table<Record> tableByName(String... qualifiedName) {
        return new QualifiedTable(qualifiedName);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * will render the field name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This field...
     * fieldByName("MY_SCHEMA", "MY_TABLE", "MY_FIELD");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, fieldByName("TITLE")))
     *       .from(tableByName("T_BOOK"))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your field's reference
     *            name.
     * @return A field referenced by <code>fieldName</code>
     */
    @Support
    public static Field<Object> fieldByName(String... qualifiedName) {
        return fieldByName(Object.class, qualifiedName);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * will render the field name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This field...
     * fieldByName("MY_SCHEMA", "MY_TABLE", "MY_FIELD");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, fieldByName("TITLE")))
     *       .from(tableByName("T_BOOK"))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your field's reference
     *            name.
     * @param type The type of the returned field
     * @return A field referenced by <code>fieldName</code>
     */
    @Support
    public static <T> Field<T> fieldByName(Class<T> type, String... qualifiedName) {
        return fieldByName(getDataType(type), qualifiedName);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * will render the field name according to your
     * {@link Settings#getRenderNameStyle()} settings. Choose
     * {@link RenderNameStyle#QUOTED} to prevent syntax errors and/or SQL
     * injection.
     * <p>
     * Example: <code><pre>
     * // This field...
     * fieldByName("MY_SCHEMA", "MY_TABLE", "MY_FIELD");
     *
     * // ... will render this SQL on SQL Server with RenderNameStyle.QUOTED set
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, fieldByName("TITLE")))
     *       .from(tableByName("T_BOOK"))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your field's reference
     *            name.
     * @param type The type of the returned field
     * @return A field referenced by <code>fieldName</code>
     */
    @Support
    public static <T> Field<T> fieldByName(DataType<T> type, String... qualifiedName) {
        return new QualifiedField<T>(type, qualifiedName);
    }

    // -------------------------------------------------------------------------
    // XXX Plain SQL object factory
    // -------------------------------------------------------------------------

    /**
     * Create a new {@link org.jooq.Template} that can transform parameter
     * objects into a {@link QueryPart}.
     *
     * @param sql The input SQL.
     * @return A template that can transform parameter objects into a
     *         {@link QueryPart}.
     */
    @SuppressWarnings("deprecation")
    static org.jooq.Template template(String sql) {
        return new SQLTemplate(sql);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions.
     * <p>
     * A plain SQL <code>QueryPart</code> is a <code>QueryPart</code> that can
     * contain user-defined plain SQL, because sometimes it is easier to express
     * things directly in SQL.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query part wrapping the plain SQL
     */
    @Support
    public static QueryPart queryPart(String sql) {
        return queryPart(template(sql), new Object[0]);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions.
     * <p>
     * A plain SQL <code>QueryPart</code> is a <code>QueryPart</code> that can
     * contain user-defined plain SQL, because sometimes it is easier to express
     * things directly in SQL.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query part wrapping the plain SQL
     */
    @Support
    public static QueryPart queryPart(String sql, QueryPart... parts) {
        return queryPart(template(sql), (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions.
     * <p>
     * A plain SQL <code>QueryPart</code> is a <code>QueryPart</code> that can
     * contain user-defined plain SQL, because sometimes it is easier to express
     * things directly in SQL. There must be as many binding variables contained
     * in the SQL, as passed in the bindings parameter
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query part wrapping the plain SQL
     */
    @Support
    public static QueryPart queryPart(String sql, Object... bindings) {
        return queryPart(template(sql), bindings);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions from a
     * template.
     *
     * @param template The template generating a delegate query part
     * @param parameters The parameters provided to the template
     * @return A query part wrapping the plain SQL
     */
    @SuppressWarnings("deprecation")
    @Support
    static QueryPart queryPart(org.jooq.Template template, Object... parameters) {
        return template.transform(parameters);
    }
    // -------------------------------------------------------------------------
    // XXX Plain SQL API
    // -------------------------------------------------------------------------

    /**
     * Create a new query holding plain SQL. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "SET SCHEMA 'abc'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query wrapping the plain SQL
     */
    @Support
    public static Query query(String sql) {
        return using(new DefaultConfiguration()).query(sql);
    }

    /**
     * Create a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "SET SCHEMA 'abc'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A query wrapping the plain SQL
     */
    @Support
    public static Query query(String sql, Object... bindings) {
        return using(new DefaultConfiguration()).query(sql, bindings);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * Unlike {@link #query(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * query("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will render this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query wrapping the plain SQL
     */
    @Support
    public static Query query(String sql, QueryPart... parts) {
        return using(new DefaultConfiguration()).query(sql, parts);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * There must not be any binding variables contained in the SQL
     * <p>
     * Use this method, when you want to take advantage of the many ways to
     * fetch results in jOOQ, using {@link ResultQuery}. Some examples:
     * <p>
     * <table border="1">
     * <tr>
     * <td> {@link ResultQuery#fetchLazy()}</td>
     * <td>Open a cursor and fetch records one by one</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(Class)}</td>
     * <td>Fetch records into a custom POJO (optionally annotated with JPA
     * annotations)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(RecordHandler)}</td>
     * <td>Fetch records into a custom callback (similar to Spring's RowMapper)</td>
     * </tr>
     * </table>
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return An executable query
     */
    @Support
    public static ResultQuery<Record> resultQuery(String sql) {
        return using(new DefaultConfiguration()).resultQuery(sql);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * Use this method, when you want to take advantage of the many ways to
     * fetch results in jOOQ, using {@link ResultQuery}. Some examples:
     * <p>
     * <table border="1">
     * <tr>
     * <td> {@link ResultQuery#fetchLazy()}</td>
     * <td>Open a cursor and fetch records one by one</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(Class)}</td>
     * <td>Fetch records into a custom POJO (optionally annotated with JPA
     * annotations)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(RecordHandler)}</td>
     * <td>Fetch records into a custom callback (similar to Spring's RowMapper)</td>
     * </tr>
     * </table>
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A query wrapping the plain SQL
     */
    @Support
    public static ResultQuery<Record> resultQuery(String sql, Object... bindings) {
        return using(new DefaultConfiguration()).resultQuery(sql, bindings);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * Unlike {@link #resultQuery(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * resultQuery("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will render this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query wrapping the plain SQL
     */
    @Support
    public static ResultQuery<Record> resultQuery(String sql, QueryPart... parts) {
        return using(new DefaultConfiguration()).resultQuery(sql, parts);
    }

    /**
     * A custom SQL clause that can render arbitrary table expressions.
     * <p>
     * A plain SQL table is a table that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex, but static subqueries or tables from different schemas.
     * <p>
     * Example
     * <p>
     * <code><pre>
     * String sql = "SELECT * FROM USER_TABLES WHERE OWNER = 'MY_SCHEMA'";
     * </pre></code>
     * <p>
     * The provided SQL must evaluate as a table whose type can be dynamically
     * discovered using JDBC's {@link ResultSetMetaData} methods. That way, you
     * can be sure that calling methods, such as {@link Table#fieldsRow()} will
     * list the actual fields returned from your result set.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     */
    @Support
    public static Table<Record> table(String sql) {
        return table(sql, new Object[0]);
    }

    /**
     * A custom SQL clause that can render arbitrary table expressions.
     * <p>
     * A plain SQL table is a table that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex, but static subqueries or tables from different schemas.
     * There must be as many binding variables contained in the SQL, as passed
     * in the bindings parameter
     * <p>
     * Example
     * <p>
     * <code><pre>
     * String sql = "SELECT * FROM USER_TABLES WHERE OWNER = ?";
     * Object[] bindings = new Object[] { "MY_SCHEMA" };
     * </pre></code>
     * <p>
     * The provided SQL must evaluate as a table whose type can be dynamically
     * discovered using JDBC's {@link ResultSetMetaData} methods. That way, you
     * can be sure that calling methods, such as {@link Table#fieldsRow()} will
     * list the actual fields returned from your result set.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     */
    @Support
    public static Table<Record> table(String sql, Object... bindings) {
        return table(template(sql), bindings);
    }

    /**
     * A custom SQL clause that can render arbitrary table expressions.
     * <p>
     * A plain SQL table is a table that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex, but static subqueries or tables from different schemas.
     * <p>
     * Example
     * <p>
     * <code><pre>
     * String sql = "SELECT * FROM USER_TABLES WHERE {0}";
     * QueryPart[] parts = new QueryPart[] { USER_TABLES.OWNER.equal("MY_SCHEMA") };
     * </pre></code>
     * <p>
     * The provided SQL must evaluate as a table whose type can be dynamically
     * discovered using JDBC's {@link ResultSetMetaData} methods. That way, you
     * can be sure that calling methods, such as {@link Table#fieldsRow()} will
     * list the actual fields returned from your result set.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A table wrapping the plain SQL
     */
    @Support
    public static Table<Record> table(String sql, QueryPart... parts) {
        return table(template(sql), (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary table expressions from a
     * template.
     *
     * @param template The template generating a delegate query part
     * @param parameters The parameters provided to the template
     * @return A query part wrapping the plain SQL
     */
    @SuppressWarnings("deprecation")
    @Support
    static Table<Record> table(org.jooq.Template template, Object... parameters) {
        return new SQLTable(queryPart(template, parameters));
    }

    /**
     * Create a "plain SQL" sequence.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A field wrapping the plain SQL
     */
    @Support
    public static Sequence<BigInteger> sequence(String sql) {
        return sequence(sql, BigInteger.class);
    }

    /**
     * Create a "plain SQL" sequence.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @return A field wrapping the plain SQL
     */
    @Support
    public static <T extends Number> Sequence<T> sequence(String sql, Class<T> type) {
        return sequence(sql, getDataType(type));
    }

    /**
     * Create a "plain SQL" sequence.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @return A field wrapping the plain SQL
     */
    @Support
    public static <T extends Number> Sequence<T> sequence(String sql, DataType<T> type) {
        return new SequenceImpl<T>(sql, null, type, true);
    }

    /**
     * Create a "plain SQL" field.
     * <p>
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, 1, 100, 200)";
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A field wrapping the plain SQL
     */
    @Support
    public static Field<Object> field(String sql) {
        return field(template(sql), new Object[0]);
    }

    /**
     * Create a "plain SQL" field.
     * <p>
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, ?, ?, ?)";
     * Object[] bindings = new Object[] { 1, 100, 200 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings for the field
     * @return A field wrapping the plain SQL
     */
    @Support
    public static Field<Object> field(String sql, Object... bindings) {
        return field(template(sql), Object.class, bindings);
    }

    /**
     * Create a "plain SQL" field.
     * <p>
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, 1, 100, 200)";
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @return A field wrapping the plain SQL
     */
    @Support
    public static <T> Field<T> field(String sql, Class<T> type) {
        return field(template(sql), type, new Object[0]);
    }

    /**
     * Create a "plain SQL" field.
     * <p>
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, ?, ?, ?)";
     * Object[] bindings = new Object[] { 1, 100, 200 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @param bindings The bindings for the field
     * @return A field wrapping the plain SQL
     */
    @Support
    public static <T> Field<T> field(String sql, Class<T> type, Object... bindings) {
        return field(template(sql), getDataType(type), bindings);
    }

    /**
     * Create a "plain SQL" field.
     * <p>
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, 1, 100, 200)";
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @return A field wrapping the plain SQL
     */
    @Support
    public static <T> Field<T> field(String sql, DataType<T> type) {
        return field(template(sql), type, new Object[0]);
    }

    /**
     * Create a "plain SQL" field.
     * <p>
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, ?, ?, ?)";
     * Object[] bindings = new Object[] { 1, 100, 200 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @param bindings The bindings for the field
     * @return A field wrapping the plain SQL
     */
    @Support
    public static <T> Field<T> field(String sql, DataType<T> type, Object... bindings) {
        return field(template(sql), type, bindings);
    }

    /**
     * A custom SQL clause that can render arbitrary SQL elements.
     * <p>
     * This is useful for constructing more complex SQL syntax elements wherever
     * <code>Field</code> types are expected. An example for this is MySQL's
     * <code>GROUP_CONCAT</code> aggregate function, which has MySQL-specific
     * keywords that are hard to reflect in jOOQ's DSL: <code><pre>
     * GROUP_CONCAT([DISTINCT] expr [,expr ...]
     *       [ORDER BY {unsigned_integer | col_name | expr}
     *           [ASC | DESC] [,col_name ...]]
     *       [SEPARATOR str_val])
     *       </pre></code>
     * <p>
     * The above MySQL function can be expressed as such: <code><pre>
     * field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC DEPARATOR '-')", expr1, expr2);
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link #name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A field wrapping the plain SQL
     */
    public static Field<Object> field(String sql, QueryPart... parts) {
        return field(template(sql), (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary SQL elements.
     * <p>
     * This is useful for constructing more complex SQL syntax elements wherever
     * <code>Field</code> types are expected. An example for this is MySQL's
     * <code>GROUP_CONCAT</code> aggregate function, which has MySQL-specific
     * keywords that are hard to reflect in jOOQ's DSL: <code><pre>
     * GROUP_CONCAT([DISTINCT] expr [,expr ...]
     *       [ORDER BY {unsigned_integer | col_name | expr}
     *           [ASC | DESC] [,col_name ...]]
     *       [SEPARATOR str_val])
     *       </pre></code>
     * <p>
     * The above MySQL function can be expressed as such: <code><pre>
     * field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC DEPARATOR '-')", expr1, expr2);
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link #name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param type The field type
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A field wrapping the plain SQL
     */
    public static <T> Field<T> field(String sql, Class<T> type, QueryPart... parts) {
        return field(template(sql), type, (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary SQL elements.
     * <p>
     * This is useful for constructing more complex SQL syntax elements wherever
     * <code>Field</code> types are expected. An example for this is MySQL's
     * <code>GROUP_CONCAT</code> aggregate function, which has MySQL-specific
     * keywords that are hard to reflect in jOOQ's DSL: <code><pre>
     * GROUP_CONCAT([DISTINCT] expr [,expr ...]
     *       [ORDER BY {unsigned_integer | col_name | expr}
     *           [ASC | DESC] [,col_name ...]]
     *       [SEPARATOR str_val])
     *       </pre></code>
     * <p>
     * The above MySQL function can be expressed as such: <code><pre>
     * field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC DEPARATOR '-')", expr1, expr2);
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link #name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param type The field type
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A field wrapping the plain SQL
     */
    public static <T> Field<T> field(String sql, DataType<T> type, QueryPart... parts) {
        return field(template(sql), type, (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary field expressions from a
     * template.
     *
     * @param template The template generating a delegate query part
     * @param parameters The parameters provided to the template
     * @return A query part wrapping the plain SQL
     */
    @SuppressWarnings("deprecation")
    @Support
    static Field<Object> field(org.jooq.Template template, Object... parameters) {
        return field(template, Object.class, parameters);
    }

    /**
     * A custom SQL clause that can render arbitrary field expressions from a
     * template.
     *
     * @param template The template generating a delegate query part
     * @param type The field type
     * @param parameters The parameters provided to the template
     * @return A query part wrapping the plain SQL
     */
    @SuppressWarnings("deprecation")
    @Support
    static <T> Field<T> field(org.jooq.Template template, Class<T> type, Object... parameters) {
        return field(template, getDataType(type), parameters);
    }

    /**
     * A custom SQL clause that can render arbitrary field expressions from a
     * template.
     *
     * @param template The template generating a delegate query part
     * @param type The field type
     * @param parameters The parameters provided to the template
     * @return A query part wrapping the plain SQL
     */
    @SuppressWarnings("deprecation")
    @Support
    static <T> Field<T> field(org.jooq.Template template, DataType<T> type, Object... parameters) {
        return new SQLField<T>(type, queryPart(template, parameters));
    }

    /**
     * <code>function()</code> can be used to access native or user-defined
     * functions that are not yet or insufficiently supported by jOOQ.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param name The function name (without parentheses)
     * @param type The function return type
     * @param arguments The function arguments
     */
    @Support
    public static <T> Field<T> function(String name, Class<T> type, Field<?>... arguments) {
        return function(name, getDataType(type), nullSafe(arguments));
    }

    /**
     * <code>function()</code> can be used to access native or user-defined
     * functions that are not yet or insufficiently supported by jOOQ.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param name The function name (without parentheses)
     * @param type The function return type
     * @param arguments The function arguments
     */
    @Support
    public static <T> Field<T> function(String name, DataType<T> type, Field<?>... arguments) {
        return new Function<T>(name, type, nullSafe(arguments));
    }

    /**
     * <code>function()</code> can be used to access native or user-defined
     * functions that are not yet or insufficiently supported by jOOQ.
     *
     * @param name The function name (possibly qualified)
     * @param type The function return type
     * @param arguments The function arguments
     */
    @Support
    public static <T> Field<T> function(Name name, Class<T> type, Field<?>... arguments) {
        return function(name, getDataType(type), nullSafe(arguments));
    }

    /**
     * <code>function()</code> can be used to access native or user-defined
     * functions that are not yet or insufficiently supported by jOOQ.
     *
     * @param name The function name (possibly qualified)
     * @param type The function return type
     * @param arguments The function arguments
     */
    @Support
    public static <T> Field<T> function(Name name, DataType<T> type, Field<?>... arguments) {
        return new Function<T>(name, type, nullSafe(arguments));
    }

    /**
     * Create a new condition holding plain SQL.
     * <p>
     * There must not be any binding variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "(X = 1 and Y = 2)";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A condition wrapping the plain SQL
     */
    @Support
    public static Condition condition(String sql) {
        return condition(template(sql), new Object[0]);
    }

    /**
     * Create a new condition holding plain SQL.
     * <p>
     * There must be as many binding variables contained in the SQL, as passed
     * in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "(X = ? and Y = ?)";
     * Object[] bindings = new Object[] { 1, 2 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A condition wrapping the plain SQL
     */
    @Support
    public static Condition condition(String sql, Object... bindings) {
        return condition(template(sql), bindings);
    }

    /**
     * A custom SQL clause that can render arbitrary SQL elements.
     * <p>
     * This is useful for constructing more complex SQL syntax elements wherever
     * <code>Condition</code> types are expected. An example for this are
     * Postgres's various operators, some of which are missing in the jOOQ API.
     * For instance, the "overlap" operator for arrays:
     * <code><pre>ARRAY[1,4,3] && ARRAY[2,1]</pre></code>
     * <p>
     * The above Postgres operator can be expressed as such: <code><pre>
     * condition("{0} && {1}", array1, array2);
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link #name(String...)} and similar methods
     *
     * @param sql The SQL
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A condition wrapping the plain SQL
     */
    @Support
    public static Condition condition(String sql, QueryPart... parts) {
        return condition(template(sql), (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary condition expressions from
     * a template.
     *
     * @param template The template generating a delegate query part
     * @param parameters The parameters provided to the template
     * @return A query part wrapping the plain SQL
     */
    @SuppressWarnings("deprecation")
    @Support
    static Condition condition(org.jooq.Template template, Object... parameters) {
        return new SQLCondition(queryPart(template, parameters));
    }

    /**
     * Create a condition from a boolean field.
     * <p>
     * Databases that support boolean data types can use boolean expressions
     * as predicates or as columns interchangeably. This extends to any type
     * of field, including functions. A Postgres example:
     * <p>
     * <code><pre>
     * select 1 where texteq('a', 'a');
     * </pre></code>
     *
     * @param value The boolean expression.
     * @return A condition wrapping the boolean expression
     */
    @Support
    public static Condition condition(Boolean value) {
        return condition(Utils.field(value, Boolean.class));
    }

    /**
     * Create a condition from a boolean field.
     * <p>
     * Databases that support boolean data types can use boolean expressions
     * as predicates or as columns interchangeably. This extends to any type
     * of field, including functions. A Postgres example:
     * <p>
     * <code><pre>
     * select 1 where texteq('a', 'a');
     * </pre></code>
     *
     * @param field The boolean expression.
     * @return A condition wrapping the boolean expression
     */
    @Support
    public static Condition condition(Field<Boolean> field) {
        return new FieldCondition(field);
    }

    // -------------------------------------------------------------------------
    // XXX Global Condition factory
    // -------------------------------------------------------------------------

    /**
     * Return a <code>Condition</code> that will always evaluate to true.
     */
    @Support
    public static Condition trueCondition() {
        return new TrueCondition();
    }

    /**
     * Return a <code>Condition</code> that will always evaluate to false.
     */
    @Support
    public static Condition falseCondition() {
        return new FalseCondition();
    }

    /**
     * Create an exists condition.
     * <p>
     * <code>EXISTS ([query])</code>
     */
    @Support
    public static Condition exists(Select<?> query) {
        return new ExistsCondition(query, true);
    }

    /**
     * Create a not exists condition.
     * <p>
     * <code>NOT EXISTS ([query])</code>
     */
    @Support
    public static Condition notExists(Select<?> query) {
        return new ExistsCondition(query, false);
    }

    /**
     * Invert a condition.
     * <p>
     * This is the same as calling {@link Condition#not()}
     */
    @Support
    public static Condition not(Condition condition) {
        return condition.not();
    }

    /**
     * Invert a boolean value.
     * <p>
     * This is convenience for calling {@link #field(Condition)},
     * {@link #not(Condition)}, {@link #condition(Field)}, i.e. <code><pre>
     * field(not(condition(field)));
     * </pre></code>
     */
    @Support
    public static Field<Boolean> not(Boolean value) {
        return not(Utils.field(value, Boolean.class));
    }

    /**
     * Invert a boolean value.
     * <p>
     * This is convenience for calling {@link #field(Condition)},
     * {@link #not(Condition)}, {@link #condition(Field)}, i.e. <code><pre>
     * field(not(condition(field)));
     * </pre></code>
     */
    @Support
    public static Field<Boolean> not(Field<Boolean> field) {
        return new NotField(field);
    }

    /**
     * Transform a condition into a boolean field.
     */
    @Support
    public static Field<Boolean> field(Condition condition) {
        return new ConditionAsField(condition);
    }

    // -------------------------------------------------------------------------
    // XXX Global Field and Function factory
    // -------------------------------------------------------------------------

    /**
     * Transform a subquery into a correlated subquery.
     */
    @Support
    public static <T> Field<T> field(Select<? extends Record1<T>> select) {
        return select.<T>asField();
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * Decode is used as a method name to avoid name clashes with Java's
     * reserved literal "case"
     *
     * @see Case
     */
    @Support
    public static Case decode() {
        return new CaseImpl();
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function.
     *
     * @see #decode(Field, Field, Field, Field[])
     */
    @Support
    public static <Z, T> Field<Z> decode(T value, T search, Z result) {
        return decode(value, search, result, new Object[0]);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function.
     *
     * @see #decode(Field, Field, Field, Field[])
     */
    @Support
    public static <Z, T> Field<Z> decode(T value, T search, Z result, Object... more) {
        return decode(Utils.field(value), Utils.field(search), Utils.field(result), Utils.fields(more).toArray(new Field[0]));
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function.
     *
     * @see #decode(Field, Field, Field, Field[])
     */
    @Support
    public static <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result) {
        return decode(nullSafe(value), nullSafe(search), nullSafe(result), new Field[0]);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function.
     * <p>
     * Returns the dialect's equivalent to DECODE:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/decode.php">DECODE</a>
     * </li>
     * </ul>
     * <p>
     * Other dialects: <code><pre>
     * CASE WHEN [this IS NOT DISTINCT FROM search] THEN [result],
     *     [WHEN more...                            THEN more...]
     *     [ELSE more...]
     * END
     * </pre></code>
     * <p>
     * Note the use of the <code>DISTINCT</code> predicate to produce the same,
     * conveniently <code>NULL</code>-agnostic behaviour as Oracle.
     *
     * @param value The value to decode
     * @param search the mandatory first search parameter
     * @param result the mandatory first result candidate parameter
     * @param more the optional parameters. If <code>more.length</code> is even,
     *            then it is assumed that it contains more search/result pairs.
     *            If <code>more.length</code> is odd, then it is assumed that it
     *            contains more search/result pairs plus a default at the end.
     */
    @Support
    public static <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result, Field<?>... more) {
        return new Decode<T, Z>(nullSafe(value), nullSafe(search), nullSafe(result), nullSafe(more));
    }

    /**
     * Coerce this field to the type of another field.
     *
     * @see #coerce(Field, Field)
     */
    @Support
    public static <T> Field<T> coerce(Object value, Field<T> as) {
        return Utils.field(value).coerce(as);
    }

    /**
     * Coerce this field to another type.
     *
     * @see #coerce(Field, Class)
     */
    @Support
    public static <T> Field<T> coerce(Object value, Class<T> as) {
        return Utils.field(value).coerce(as);
    }

    /**
     * Coerce a field to another type.
     *
     * @see #coerce(Field, DataType)
     */
    @Support
    public static <T> Field<T> coerce(Object value, DataType<T> as) {
        return Utils.field(value).coerce(as);
    }

    /**
     * Coerce this field to the type of another field.
     * <p>
     * Unlike with casting, coercing doesn't affect the way the database sees a
     * <code>Field</code>'s type. This is how coercing affects your SQL:
     * <h3>Bind values</h3> <code><pre>
     * // This binds an int value to a JDBC PreparedStatement
     * DSL.val(1).coerce(String.class);
     *
     * // This binds an int value to a JDBC PreparedStatement
     * // and casts it to VARCHAR in SQL
     * DSL.val(1).cast(String.class);
     * </pre></code>
     * <h3>Other Field types</h3> <code><pre>
     * // This fetches a String value for the BOOK.ID field from JDBC
     * BOOK.ID.coerce(String.class);
     *
     * // This fetches a String value for the BOOK.ID field from JDBC
     * // after casting it to VARCHAR in the database
     * BOOK.ID.cast(String.class);
     * </pre></code>
     *
     * @param <T> The generic type of the coerced field
     * @param field The field to be coerced
     * @param as The field whose type is used for the coercion
     * @return The coerced field
     * @see Field#coerce(DataType)
     * @see Field#cast(Field)
     */
    @Support
    public static <T> Field<T> coerce(Field<?> field, Field<T> as) {
        return nullSafe(field).coerce(as);
    }

    /**
     * Coerce this field to another type.
     * <p>
     * Unlike with casting, coercing doesn't affect the way the database sees a
     * <code>Field</code>'s type. This is how coercing affects your SQL:
     * <h3>Bind values</h3> <code><pre>
     * // This binds an int value to a JDBC PreparedStatement
     * DSL.val(1).coerce(String.class);
     *
     * // This binds an int value to a JDBC PreparedStatement
     * // and casts it to VARCHAR in SQL
     * DSL.val(1).cast(String.class);
     * </pre></code>
     * <h3>Other Field types</h3> <code><pre>
     * // This fetches a String value for the BOOK.ID field from JDBC
     * BOOK.ID.coerce(String.class);
     *
     * // This fetches a String value for the BOOK.ID field from JDBC
     * // after casting it to VARCHAR in the database
     * BOOK.ID.cast(String.class);
     * </pre></code>
     *
     * @param <T> The generic type of the coerced field
     * @param field The field to be coerced
     * @param as The type that is used for the coercion
     * @return The coerced field
     * @see Field#coerce(DataType)
     * @see Field#cast(Class)
     */
    @Support
    public static <T> Field<T> coerce(Field<?> field, Class<T> as) {
        return nullSafe(field).coerce(as);
    }

    /**
     * Coerce a field to another type.
     * <p>
     * Unlike with casting, coercing doesn't affect the way the database sees a
     * <code>Field</code>'s type. This is how coercing affects your SQL:
     * <h3>Bind values</h3> <code><pre>
     * // This binds an int value to a JDBC PreparedStatement
     * DSL.val(1).coerce(String.class);
     *
     * // This binds an int value to a JDBC PreparedStatement
     * // and casts it to VARCHAR in SQL
     * DSL.val(1).cast(String.class);
     * </pre></code>
     * <h3>Other Field types</h3> <code><pre>
     * // This fetches a String value for the BOOK.ID field from JDBC
     * BOOK.ID.coerce(String.class);
     *
     * // This fetches a String value for the BOOK.ID field from JDBC
     * // after casting it to VARCHAR in the database
     * BOOK.ID.cast(String.class);
     * </pre></code>
     *
     * @param <T> The generic type of the coerced field
     * @param field The field to be coerced
     * @param as The type that is used for the coercion
     * @return The coerced field
     * @see Field#coerce(DataType)
     * @see Field#cast(DataType)
     */
    @Support
    public static <T> Field<T> coerce(Field<?> field, DataType<T> as) {
        return nullSafe(field).coerce(as);
    }

    /**
     * Cast a value to the type of another field.
     *
     * @param <T> The generic type of the cast field
     * @param value The value to cast
     * @param as The field whose type is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> cast(Object value, Field<T> as) {
        return Utils.field(value, as).cast(as);
    }

    /**
     * Cast a field to the type of another field.
     *
     * @param <T> The generic type of the cast field
     * @param field The field to cast
     * @param as The field whose type is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> cast(Field<?> field, Field<T> as) {
        return nullSafe(field).cast(as);
    }

    /**
     * Cast null to the type of another field.
     *
     * @param <T> The generic type of the cast field
     * @param as The field whose type is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> castNull(Field<T> as) {
        return NULL().cast(as);
    }

    /**
     * Cast a value to another type.
     *
     * @param <T> The generic type of the cast field
     * @param value The value to cast
     * @param type The type that is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> cast(Object value, Class<T> type) {
        return Utils.field(value, type).cast(type);
    }

    /**
     * Cast a field to another type.
     *
     * @param <T> The generic type of the cast field
     * @param field The field to cast
     * @param type The type that is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> cast(Field<?> field, Class<T> type) {
        return nullSafe(field).cast(type);
    }

    /**
     * Cast null to a type.
     *
     * @param <T> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> castNull(DataType<T> type) {
        return NULL().cast(type);
    }

    /**
     * Cast a value to another type.
     *
     * @param <T> The generic type of the cast field
     * @param value The value to cast
     * @param type The type that is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> cast(Object value, DataType<T> type) {
        return Utils.field(value, type).cast(type);
    }

    /**
     * Cast a field to another type.
     *
     * @param <T> The generic type of the cast field
     * @param field The value to cast
     * @param type The type that is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> cast(Field<?> field, DataType<T> type) {
        return nullSafe(field).cast(type);
    }

    /**
     * Cast null to a type.
     *
     * @param <T> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     */
    @Support
    public static <T> Field<T> castNull(Class<T> type) {
        return NULL().cast(type);
    }

    /**
     * Cast all fields that need casting.
     *
     * @param <T> The generic field type
     * @param type The type to cast to
     * @param fields The fields to be cast to a uniform type
     * @return The cast fields
     */
    static <T> Field<T>[] castAll(Class<T> type, Field<?>... fields) {
        Field<?>[] castFields = new Field<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            castFields[i] = fields[i].cast(type);
        }

        return (Field<T>[]) castFields;
    }

    /**
     * Gets the Oracle-style <code>COALESCE(value1, value2, ... , value n)</code>
     * function.
     *
     * @see #coalesce(Field, Field...)
     */
    @Support
    public static <T> Field<T> coalesce(T value, T... values) {
        return coalesce(Utils.field(value), Utils.fields(values).toArray(new Field[0]));
    }

    /**
     * Gets the Oracle-style <code>COALESCE(field1, field2, ... , field n)</code>
     * function.
     * <p>
     * Returns the dialect's equivalent to COALESCE:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/coalesce.php">COALESCE</a>
     * </li>
     * </ul>
     */
    @Support
    public static <T> Field<T> coalesce(Field<T> field, Field<?>... fields) {
        return new Coalesce<T>(nullSafeDataType(field), nullSafe(combine(field, fields)));
    }

    /**
     * Gets the SQL Server-style ISNULL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> isnull(T value, T defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * Gets the SQL Server-style ISNULL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> isnull(T value, Field<T> defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * Gets the SQL Server-style ISNULL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> isnull(Field<T> value, T defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * Gets the SQL Server-style ISNULL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> isnull(Field<T> value, Field<T> defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> nvl(T value, T defaultValue) {
        return nvl(Utils.field(value), Utils.field(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> nvl(T value, Field<T> defaultValue) {
        return nvl(Utils.field(value), nullSafe(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> nvl(Field<T> value, T defaultValue) {
        return nvl(nullSafe(value), Utils.field(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function.
     * <p>
     * Returns the dialect's equivalent to NVL:
     * <ul>
     * <li>DB2 <a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9r7/index.jsp?topic=/com.ibm.db2.luw.sql.ref.doc/doc/r0052627.html"
     * >NVL</a></li>
     * <li>Derby <a
     * href="http://db.apache.org/derby/docs/10.7/ref/rreffunccoalesce.html"
     * >COALESCE</a></li>
     * <li>H2 <a
     * href="http://www.h2database.com/html/functions.html#ifnull">IFNULL</a></li>
     * <li>HSQLDB <a
     * href="http://hsqldb.org/doc/2.0/guide/builtinfunctions-chapt.html"
     * >NVL</a></li>
     * <li>MySQL <a href=
     * "http://dev.mysql.com/doc/refman/5.0/en/control-flow-functions.html"
     * >IFNULL</a></li>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nvl.php">NVL</a></li>
     * <li>Postgres <a href=
     * "http://www.postgresql.org/docs/8.1/static/functions-conditional.html"
     * >COALESCE</a></li>
     * <li>SQLite <a
     * href="http://www.sqlite.org/lang_corefunc.html#ifnull">IFNULL</a></li>
     * </ul>
     */
    @Support
    public static <T> Field<T> nvl(Field<T> value, Field<T> defaultValue) {
        return new Nvl<T>(nullSafe(value), nullSafe(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     *
     * @see #nvl2(Field, Field, Field)
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Z valueIfNotNull, Z valueIfNull) {
        return nvl2(nullSafe(value), Utils.field(valueIfNotNull), Utils.field(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     *
     * @see #nvl2(Field, Field, Field)
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Z valueIfNotNull, Field<Z> valueIfNull) {
        return nvl2(nullSafe(value), Utils.field(valueIfNotNull), nullSafe(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     *
     * @see #nvl2(Field, Field, Field)
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Field<Z> valueIfNotNull, Z valueIfNull) {
        return nvl2(nullSafe(value), nullSafe(valueIfNotNull), Utils.field(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     * <p>
     * Returns the dialect's equivalent to NVL2:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nvl2.php">NVL2</a></li>
     * </ul>
     * <p>
     * Other dialects:
     * <code>CASE WHEN [value] IS NULL THEN [valueIfNull] ELSE [valueIfNotNull] END</code>
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Field<Z> valueIfNotNull, Field<Z> valueIfNull) {
        return new Nvl2<Z>(nullSafe(value), nullSafe(valueIfNotNull), nullSafe(valueIfNull));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     *
     * @see #nullif(Field, Field)
     */
    @Support
    public static <T> Field<T> nullif(T value, T other) {
        return nullif(Utils.field(value), Utils.field(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     *
     * @see #nullif(Field, Field)
     */
    @Support
    public static <T> Field<T> nullif(T value, Field<T> other) {
        return nullif(Utils.field(value), nullSafe(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     *
     * @see #nullif(Field, Field)
     */
    @Support
    public static <T> Field<T> nullif(Field<T> value, T other) {
        return nullif(nullSafe(value), Utils.field(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     * <p>
     * Returns the dialect's equivalent to NULLIF:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nullif.php">NULLIF</a></li>
     * </ul>
     * <p>
     */
    @Support
    public static <T> Field<T> nullif(Field<T> value, Field<T> other) {
        return new NullIf<T>(nullSafe(value), nullSafe(other));
    }

    // -------------------------------------------------------------------------
    // XXX String function factory
    // -------------------------------------------------------------------------

    /**
     * Get the upper(field) function.
     *
     * @see #upper(Field)
     */
    @Support
    public static Field<String> upper(String value) {
        return upper(Utils.field(value));
    }

    /**
     * Get the upper(field) function.
     * <p>
     * This renders the upper function in all dialects:
     * <code><pre>upper([field])</pre></code>
     */
    @Support
    public static Field<String> upper(Field<String> field) {
        return new Upper(nullSafe(field));
    }

    /**
     * Get the lower(field) function.
     *
     * @see #lower(Field)
     */
    @Support
    public static Field<String> lower(String value) {
        return lower(Utils.field(value, String.class));
    }

    /**
     * Get the lower(field) function.
     * <p>
     * This renders the lower function in all dialects:
     * <code><pre>lower([field])</pre></code>
     */
    @Support
    public static Field<String> lower(Field<String> field) {
        return new Lower(nullSafe(field));
    }

    /**
     * Get the trim(field) function.
     *
     * @see #trim(Field)
     */
    @Support
    public static Field<String> trim(String value) {
        return trim(Utils.field(value, String.class));
    }

    /**
     * Get the trim(field) function.
     * <p>
     * This renders the trim function where available:
     * <code><pre>trim([field])</pre></code> ... or simulates it elsewhere using
     * rtrim and ltrim: <code><pre>ltrim(rtrim([field]))</pre></code>
     */
    @Support
    public static Field<String> trim(Field<String> field) {
        return new Trim(nullSafe(field));
    }

    /**
     * Get the rtrim(field) function.
     *
     * @see #rtrim(Field)
     */
    @Support
    public static Field<String> rtrim(String value) {
        return rtrim(Utils.field(value));
    }

    /**
     * Get the rtrim(field) function.
     * <p>
     * This renders the rtrim function in all dialects:
     * <code><pre>rtrim([field])</pre></code>
     */
    @Support
    public static Field<String> rtrim(Field<String> field) {
        return new RTrim(nullSafe(field));
    }

    /**
     * Get the ltrim(field) function.
     *
     * @see #ltrim(Field)
     */
    @Support
    public static Field<String> ltrim(String value) {
        return ltrim(Utils.field(value, String.class));
    }

    /**
     * Get the ltrim(field) function.
     * <p>
     * This renders the ltrim function in all dialects:
     * <code><pre>ltrim([field])</pre></code>
     */
    @Support
    public static Field<String> ltrim(Field<String> field) {
        return new LTrim(nullSafe(field));
    }

    /**
     * Get the rpad(field, length) function.
     *
     * @see #rpad(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rpad(Field<String> field, int length) {
        return rpad(nullSafe(field), Utils.field(length));
    }

    /**
     * Get the rpad(field, length) function.
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([field], repeat(' ', [length] - length([field])))</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rpad(Field<String> field, Field<? extends Number> length) {
        return new Rpad(nullSafe(field), nullSafe(length));
    }

    /**
     * Get the rpad(field, length, character) function.
     *
     * @see #rpad(Field, Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rpad(Field<String> field, int length, char character) {
        return rpad(field, length, Character.toString(character));
    }

    /**
     * Get the rpad(field, length, character) function.
     *
     * @see #rpad(Field, Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rpad(Field<String> field, int length, String character) {
        return rpad(nullSafe(field), Utils.field(length), Utils.field(character, String.class));
    }

    /**
     * Get the rpad(field, length, character) function.
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([field], repeat([character], [length] - length([field])))</pre></code>
     * <p>
     * In {@link SQLDialect#SQLITE}, this is simulated as such:
     * <code><pre>[field] || replace(replace(substr(quote(zeroblob(([length] + 1) / 2)), 3, ([length] - length([field]))), '''', ''), '0', [character])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        return new Rpad(nullSafe(field), nullSafe(length), nullSafe(character));
    }

    /**
     * Get the lpad(field, length) function.
     *
     * @see #lpad(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> lpad(Field<String> field, int length) {
        return lpad(nullSafe(field), Utils.field(length));
    }

    /**
     * Get the lpad(field, length) function.
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat(' ', [length] - length([field])), [field])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> lpad(Field<String> field, Field<? extends Number> length) {
        return new Lpad(nullSafe(field), nullSafe(length));
    }

    /**
     * Get the lpad(field, length, character) function.
     *
     * @see #lpad(Field, Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> lpad(Field<String> field, int length, char character) {
        return lpad(field, length, Character.toString(character));
    }

    /**
     * Get the lpad(field, length, character) function.
     *
     * @see #lpad(Field, Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> lpad(Field<String> field, int length, String character) {
        return lpad(nullSafe(field), Utils.field(length), Utils.field(character, String.class));
    }

    /**
     * Get the lpad(field, length, character) function.
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat([character], [length] - length([field])), [field])</pre></code>
     * <p>
     * In {@link SQLDialect#SQLITE}, this is simulated as such:
     * <code><pre>replace(replace(substr(quote(zeroblob(([length] + 1) / 2)), 3, ([length] - length([field]))), '''', ''), '0', [character]) || [field]</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> lpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        return new Lpad(nullSafe(field), nullSafe(length), nullSafe(character));
    }

    /**
     * Get the repeat(field, count) function.
     *
     * @see #repeat(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(String field, int count) {
        return repeat(Utils.field(field, String.class), Utils.field(count));
    }

    /**
     * Get the repeat(field, count) function.
     *
     * @see #repeat(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(String field, Field<? extends Number> count) {
        return repeat(Utils.field(field, String.class), nullSafe(count));
    }

    /**
     * Get the repeat(count) function.
     *
     * @see #repeat(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(Field<String> field, int count) {
        return repeat(nullSafe(field), Utils.field(count));
    }

    /**
     * Get the repeat(field, count) function.
     * <p>
     * This renders the repeat or replicate function where available:
     * <code><pre>repeat([field], [count]) or
     * replicate([field], [count])</pre></code> ... or simulates it elsewhere
     * using rpad and length, which may be simulated as well, depending on the
     * RDBMS:
     * <code><pre>rpad([field], length([field]) * [count], [field])</pre></code>
     * <p>
     * In {@link SQLDialect#SQLITE}, this is simulated as such:
     * <code><pre>replace(substr(quote(zeroblob(([count] + 1) / 2)), 3, [count]), '0', [field])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(Field<String> field, Field<? extends Number> count) {
        return new Repeat(nullSafe(field), nullSafe(count));
    }

    /**
     * Get the SQL Server specific <code>SPACE()</code> function.
     * <p>
     * This function can be emulated using {@link #repeat(String, int)} in
     * dialects that do not ship with a native <code>SPACE()</code> function.
     *
     * @see <a
     *      href="http://technet.microsoft.com/en-us/library/ms187950.aspx">http://technet.microsoft.com/en-us/library/ms187950.aspx</a>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> space(int value) {
        return space(val(value));
    }

    /**
     * Get the SQL Server specific <code>SPACE()</code> function.
     * <p>
     * This function can be emulated using {@link #repeat(String, int)} in
     * dialects that do not ship with a native <code>SPACE()</code> function.
     *
     * @see <a
     *      href="http://technet.microsoft.com/en-us/library/ms187950.aspx">http://technet.microsoft.com/en-us/library/ms187950.aspx</a>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> space(Field<Integer> value) {
        return new Space(nullSafe(value));
    }

    /**
     * Get the <code>reverse(field)</code> function.
     */
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<String> reverse(String value) {
        return reverse(val(value));
    }

    /**
     * Get the <code>reverse(field)</code> function.
     */
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<String> reverse(Field<String> field) {
        return new Reverse(nullSafe(field));
    }

    /**
     * Convenience method for {@link #replace(Field, String, String)} to escape
     * data for use with {@link Field#like(Field, char)}.
     * <p>
     * Essentially, this escapes <code>%</code> and <code>_</code> characters
     *
     * @see #replace(Field, String, String)
     * @see Field#like(Field, char)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static String escape(String value, char escape) {
        String esc = "" + escape;
        return value.replace(esc, esc + esc).replace("%", esc + "%").replace("_", esc + "_");
    }

    /**
     * Convenience method for {@link #replace(Field, String, String)} to escape
     * data for use with {@link Field#like(Field, char)}.
     * <p>
     * Essentially, this escapes <code>%</code> and <code>_</code> characters
     *
     * @see #replace(Field, String, String)
     * @see Field#like(Field, char)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> escape(Field<String> field, char escape) {
        Field<String> replace = field;

        String esc = "" + escape;
        replace = replace(replace, inline(esc), inline(esc + esc));
        replace = replace(replace, inline("%"), inline(esc + "%"));
        replace = replace(replace, inline("_"), inline(esc + "_"));

        return replace;
    }

    /**
     * Get the replace(field, search) function.
     *
     * @see #replace(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> replace(Field<String> field, String search) {
        return replace(nullSafe(field), Utils.field(search, String.class));
    }

    /**
     * Get the replace(field, search) function.
     * <p>
     * This renders the replace or str_replace function where available:
     * <code><pre>replace([field], [search]) or
     * str_replace([field], [search])</pre></code> ... or simulates it elsewhere
     * using the three-argument replace function:
     * <code><pre>replace([field], [search], '')</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> replace(Field<String> field, Field<String> search) {
        return new Replace(nullSafe(field), nullSafe(search));
    }

    /**
     * Get the replace(field, search, replace) function.
     *
     * @see #replace(Field, Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> replace(Field<String> field, String search, String replace) {
        return replace(nullSafe(field), Utils.field(search, String.class), Utils.field(replace, String.class));
    }

    /**
     * Get the replace(field, search, replace) function.
     * <p>
     * This renders the replace or str_replace function:
     * <code><pre>replace([field], [search]) or
     * str_replace([field], [search])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> replace(Field<String> field, Field<String> search, Field<String> replace) {
        return new Replace(nullSafe(field), nullSafe(search), nullSafe(replace));
    }

    /**
     * Get the position(in, search) function.
     *
     * @see #position(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> position(String in, String search) {
        return position(Utils.field(in, String.class), Utils.field(search, String.class));
    }

    /**
     * Get the position(in, search) function.
     *
     * @see #position(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> position(String in, Field<String> search) {
        return position(Utils.field(in, String.class), nullSafe(search));
    }

    /**
     * Get the position(in, search) function.
     *
     * @see #position(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> position(Field<String> in, String search) {
        return position(nullSafe(in), Utils.field(search, String.class));
    }

    /**
     * Get the position(in, search) function.
     * <p>
     * This renders the position or any equivalent function:
     * <code><pre>position([search] in [in]) or
     * locate([in], [search]) or
     * locate([search], [in]) or
     * instr([in], [search]) or
     * charindex([search], [in])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> position(Field<String> in, Field<String> search) {
        return new Position(nullSafe(search), nullSafe(in));
    }

    /**
     * Get the ascii(field) function.
     *
     * @see #ascii(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> ascii(String field) {
        return ascii(Utils.field(field, String.class));
    }

    /**
     * Get the ascii(field) function.
     * <p>
     * This renders the ascii function:
     * <code><pre>ascii([field])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> ascii(Field<String> field) {
        return new Ascii(nullSafe(field));
    }

    /**
     * Get the <code>concat(field, value)</code> function.
     *
     * @see #concat(Field...)
     */
    @Support
    public static Field<String> concat(Field<String> field, String value) {
        return concat(nullSafe(field), Utils.field(value, String.class));
    }

    /**
     * Get the <code>concat(value, field)</code> function.
     *
     * @see #concat(Field...)
     */
    @Support
    public static Field<String> concat(String value, Field<String> field) {
        return concat(Utils.field(value, String.class), nullSafe(field));
    }

    /**
     * Get the concat(value[, value, ...]) function.
     *
     * @see #concat(Field...)
     */
    @Support
    public static Field<String> concat(String... values) {
        return concat(Utils.fields(values).toArray(new Field[0]));
    }

    /**
     * Get the concat(field[, field, ...]) function.
     * <p>
     * This creates <code>fields[0] || fields[1] || ...</code> as an
     * expression, or <code>concat(fields[0], fields[1], ...)</code>,
     * depending on the dialect.
     * <p>
     * If any of the given fields is not a {@link String} field, they are cast
     * to <code>Field&lt;String&gt;</code> first using {@link #cast(Object, Class)}
     */
    @Support
    public static Field<String> concat(Field<?>... fields) {
        return new Concat(nullSafe(fields));
    }

    /**
     * Get the substring(field, startingPosition) function.
     *
     * @see #substring(Field, Field)
     */
    @Support
    public static Field<String> substring(Field<String> field, int startingPosition) {
        return substring(nullSafe(field), Utils.field(startingPosition));
    }

    /**
     * Get the substring(field, startingPosition) function.
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([field], [startingPosition]) or
     * substring([field], [startingPosition])</pre></code>
     */
    @Support
    public static Field<String> substring(Field<String> field, Field<? extends Number> startingPosition) {
        return new Substring(nullSafe(field), nullSafe(startingPosition));
    }

    /**
     * Get the substring(field, startingPosition, length) function.
     *
     * @see #substring(Field, Field, Field)
     */
    @Support
    public static Field<String> substring(Field<String> field, int startingPosition, int length) {
        return substring(nullSafe(field), Utils.field(startingPosition), Utils.field(length));
    }

    /**
     * Get the substring(field, startingPosition, length) function.
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([field], [startingPosition], [length]) or
     * substring([field], [startingPosition], [length])</pre></code>
     */
    @Support
    public static Field<String> substring(Field<String> field, Field<? extends Number> startingPosition, Field<? extends Number> length) {
        return new Substring(nullSafe(field), nullSafe(startingPosition), nullSafe(length));
    }

    /**
     * Get the mid(field, startingPosition, length) function.
     *
     * @see #substring(Field, Field, Field)
     */
    @Support
    public static Field<String> mid(Field<String> field, int startingPosition, int length) {
        return substring(nullSafe(field), Utils.field(startingPosition), Utils.field(length));
    }

    /**
     * Get the mid(field, startingPosition, length) function.
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([field], [startingPosition], [length]) or
     * substring([field], [startingPosition], [length])</pre></code>
     */
    @Support
    public static Field<String> mid(Field<String> field, Field<? extends Number> startingPosition, Field<? extends Number> length) {
        return substring(nullSafe(field), nullSafe(startingPosition), nullSafe(length));
    }

    /**
     * Get the left outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'abc' = LEFT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> left(String field, int length) {
        return left(Utils.field(field), Utils.field(length));
    }

    /**
     * Get the left outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'abc' = LEFT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> left(String field, Field<? extends Number> length) {
        return left(Utils.field(field), nullSafe(length));
    }

    /**
     * Get the left outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'abc' = LEFT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> left(Field<String> field, int length) {
        return left(nullSafe(field), Utils.field(length));
    }

    /**
     * Get the left outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'abc' = LEFT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> left(Field<String> field, Field<? extends Number> length) {
        return new Left(field, length);
    }

    /**
     * Get the right outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'cde' = RIGHT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> right(String field, int length) {
        return right(Utils.field(field), Utils.field(length));
    }

    /**
     * Get the right outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'cde' = RIGHT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> right(String field, Field<? extends Number> length) {
        return right(Utils.field(field), nullSafe(length));
    }

    /**
     * Get the right outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'cde' = RIGHT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> right(Field<String> field, int length) {
        return right(nullSafe(field), Utils.field(length));
    }

    /**
     * Get the right outermost characters from a string.
     * <p>
     * Example:
     * <code><pre>
     * 'cde' = RIGHT('abcde', 3)
     * </pre></code>
     */
    @Support
    public static Field<String> right(Field<String> field, Field<? extends Number> length) {
        return new Right(field, length);
    }

    /**
     * Get the length of a <code>VARCHAR</code> type. This is a synonym for
     * {@link #charLength(String)}.
     *
     * @see #charLength(String)
     */
    @Support
    public static Field<Integer> length(String value) {
        return length(Utils.field(value, String.class));
    }

    /**
     * Get the length of a <code>VARCHAR</code> type. This is a synonym for
     * {@link #charLength(Field)}.
     *
     * @see #charLength(Field)
     */
    @Support
    public static Field<Integer> length(Field<String> field) {
        return charLength(field);
    }

    /**
     * Get the char_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> charLength(String value) {
        return charLength(Utils.field(value));
    }

    /**
     * Get the char_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> charLength(Field<String> field) {
        return new Function<Integer>(Term.CHAR_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the bit_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> bitLength(String value) {
        return bitLength(Utils.field(value));
    }

    /**
     * Get the bit_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> bitLength(Field<String> field) {
        return new Function<Integer>(Term.BIT_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the octet_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> octetLength(String value) {
        return octetLength(Utils.field(value, String.class));
    }

    /**
     * Get the octet_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> octetLength(Field<String> field) {
        return new Function<Integer>(Term.OCTET_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    // ------------------------------------------------------------------------
    // XXX Hash function factory
    // ------------------------------------------------------------------------

    /**
     * Get the MySQL-specific <code>MD5()</code> function.
     * <p>
     * These are the implementations for various databases:
     * <p>
     * <table border="1">
     * <tr>
     * <th>Database</th>
     * <th>Implementation</th>
     * </tr>
     * <tr>
     * <td>MySQL</td>
     * <td><code>MD5( ... )</code></td>
     * </tr>
     * <tr>
     * <td>Oracle</td>
     * <td>
     * <code>LOWER(RAWTOHEX(SYS.DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW( ... ), SYS.DBMS_CRYPTO.HASH_MD5)))</code>
     * </td>
     * </tr>
     * </table>
     */
    @Support({ MARIADB, MYSQL })
    public static Field<String> md5(String string) {
        return md5(Utils.field(string));
    }

    /**
     * Get the MySQL-specific <code>MD5()</code> function.
     * <p>
     * These are the implementations for various databases:
     * <p>
     * <table border="1">
     * <tr>
     * <th>Database</th>
     * <th>Implementation</th>
     * </tr>
     * <tr>
     * <td>MySQL</td>
     * <td><code>MD5( ... )</code></td>
     * </tr>
     * <tr>
     * <td>Oracle</td>
     * <td>
     * <code>LOWER(RAWTOHEX(SYS.DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW( ... ), SYS.DBMS_CRYPTO.HASH_MD5)))</code>
     * </td>
     * </tr>
     * </table>
     */
    @Support({ MARIADB, MYSQL })
    public static Field<String> md5(Field<String> string) {
        return new MD5(nullSafe(string));
    }

    // ------------------------------------------------------------------------
    // XXX Date and time functions
    // ------------------------------------------------------------------------

    /**
     * Get the current_date() function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> currentDate() {
        return new CurrentDate();
    }

    /**
     * Get the current_time() function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Time> currentTime() {
        return new CurrentTime();
    }

    /**
     * Get the current_timestamp() function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> currentTimestamp() {
        return new CurrentTimestamp();
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> dateDiff(Date date1, Date date2) {
        return dateDiff(Utils.field(date1), Utils.field(date2));
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> dateDiff(Field<Date> date1, Date date2) {
        return dateDiff(nullSafe(date1), Utils.field(date2));
    }

    /**
     * Add an interval to a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Number)
     */
    @Support
    public static Field<Date> dateAdd(Date date, Number interval) {
        return dateAdd(Utils.field(date), Utils.field(interval));
    }

    /**
     * Add an interval to a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Field)
     */
    @Support
    public static Field<Date> dateAdd(Field<Date> date, Field<? extends Number> interval) {
        return nullSafe(date).add(interval);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Date date, Number interval, DatePart datePart) {
        return new DateAdd<Date>(Utils.field(date), Utils.field(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Date date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<Date>(Utils.field(date), nullSafe(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Field<Date> date, Number interval, DatePart datePart) {
        return new DateAdd<Date>(nullSafe(date), Utils.field(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Field<Date> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<Date>(nullSafe(date), nullSafe(interval), datePart);
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> dateDiff(Date date1, Field<Date> date2) {
        return dateDiff(Utils.field(date1), nullSafe(date2));
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> dateDiff(Field<Date> date1, Field<Date> date2) {
        return new DateDiff(nullSafe(date1), nullSafe(date2));
    }

    /**
     * Add an interval to a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Number)
     */
    @Support
    public static Field<Timestamp> timestampAdd(Timestamp timestamp, Number interval) {
        return timestampAdd(Utils.field(timestamp), Utils.field(interval));
    }

    /**
     * Add an interval to a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Field)
     */
    @Support
    public static Field<Timestamp> timestampAdd(Field<Timestamp> timestamp, Field<? extends Number> interval) {
        return nullSafe(timestamp).add(interval);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Timestamp date, Number interval, DatePart datePart) {
        return new DateAdd<Timestamp>(Utils.field(date), Utils.field(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Timestamp date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<Timestamp>(Utils.field(date), nullSafe(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Field<Timestamp> date, Number interval, DatePart datePart) {
        return new DateAdd<Timestamp>(nullSafe(date), Utils.field(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Field<Timestamp> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<Timestamp>(nullSafe(date), nullSafe(interval), datePart);
    }

    /**
     * Get the timestamp difference as a <code>INTERVAL DAY TO SECOND</code>
     * type.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<DayToSecond> timestampDiff(Timestamp timestamp1, Timestamp timestamp2) {
        return timestampDiff(Utils.field(timestamp1), Utils.field(timestamp2));
    }

    /**
     * Get the timestamp difference as a <code>INTERVAL DAY TO SECOND</code>
     * type.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<DayToSecond> timestampDiff(Field<Timestamp> timestamp1, Timestamp timestamp2) {
        return timestampDiff(nullSafe(timestamp1), Utils.field(timestamp2));
    }

    /**
     * Get the timestamp difference as a <code>INTERVAL DAY TO SECOND</code>
     * type.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<DayToSecond> timestampDiff(Timestamp timestamp1, Field<Timestamp> timestamp2) {
        return timestampDiff(Utils.field(timestamp1), nullSafe(timestamp2));
    }

    /**
     * Get the timestamp difference as a <code>INTERVAL DAY TO SECOND</code>
     * type.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<DayToSecond> timestampDiff(Field<Timestamp> timestamp1, Field<Timestamp> timestamp2) {
        return new TimestampDiff(nullSafe(timestamp1), nullSafe(timestamp2));
    }

    /**
     * Truncate a date to the beginning of the day.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<Date> trunc(Date date) {
        return trunc(date, DatePart.DAY);
    }

    /**
     * Truncate a date to a given datepart.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<Date> trunc(Date date, DatePart part) {
        return trunc(Utils.field(date), part);
    }

    /**
     * Truncate a timestamp to the beginning of the day.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<Timestamp> trunc(Timestamp timestamp) {
        return trunc(timestamp, DatePart.DAY);
    }

    /**
     * Truncate a timestamp to a given datepart.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<Timestamp> trunc(Timestamp timestamp, DatePart part) {
        return trunc(Utils.field(timestamp), part);
    }

    /**
     * Truncate a date or a timestamp to the beginning of the day.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static <T extends java.util.Date> Field<T> trunc(Field<T> date) {
        return trunc(date, DatePart.DAY);
    }

    /**
     * Truncate a date or a timestamp to a given datepart.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static <T extends java.util.Date> Field<T> trunc(Field<T> date, DatePart part) {
        return new TruncDate<T>(date, part);
    }

    // -------------------------------------------------------------------------

    /**
     * Get the extract(field, datePart) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> extract(java.util.Date value, DatePart datePart) {
        return extract(Utils.field(value), datePart);
    }

    /**
     * Get the extract(field, datePart) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> extract(Field<? extends java.util.Date> field, DatePart datePart) {
        return new Extract(nullSafe(field), datePart);
    }

    /**
     * Get the year part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#YEAR}
     */
    @Support
    public static Field<Integer> year(java.util.Date value) {
        return extract(value, DatePart.YEAR);
    }

    /**
     * Get the year part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#YEAR}
     */
    @Support
    public static Field<Integer> year(Field<? extends java.util.Date> field) {
        return extract(field, DatePart.YEAR);
    }

    /**
     * Get the month part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#MONTH}
     */
    @Support
    public static Field<Integer> month(java.util.Date value) {
        return extract(value, DatePart.MONTH);
    }

    /**
     * Get the month part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MONTH}
     */
    @Support
    public static Field<Integer> month(Field<? extends java.util.Date> field) {
        return extract(field, DatePart.MONTH);
    }

    /**
     * Get the day part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#DAY}
     */
    @Support
    public static Field<Integer> day(java.util.Date value) {
        return extract(value, DatePart.DAY);
    }

    /**
     * Get the day part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DAY}
     */
    @Support
    public static Field<Integer> day(Field<? extends java.util.Date> field) {
        return extract(field, DatePart.DAY);
    }

    /**
     * Get the hour part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#HOUR}
     */
    @Support
    public static Field<Integer> hour(java.util.Date value) {
        return extract(value, DatePart.HOUR);
    }

    /**
     * Get the hour part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#HOUR}
     */
    @Support
    public static Field<Integer> hour(Field<? extends java.util.Date> field) {
        return extract(field, DatePart.HOUR);
    }

    /**
     * Get the minute part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#MINUTE}
     */
    @Support
    public static Field<Integer> minute(java.util.Date value) {
        return extract(value, DatePart.MINUTE);
    }

    /**
     * Get the minute part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MINUTE}
     */
    @Support
    public static Field<Integer> minute(Field<? extends java.util.Date> field) {
        return extract(field, DatePart.MINUTE);
    }

    /**
     * Get the second part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#SECOND}
     */
    @Support
    public static Field<Integer> second(java.util.Date value) {
        return extract(value, DatePart.SECOND);
    }

    /**
     * Get the second part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#SECOND}
     */
    @Support
    public static Field<Integer> second(Field<? extends java.util.Date> field) {
        return extract(field, DatePart.SECOND);
    }

    /**
     * Convert a string value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Date> date(String value) {
        return Utils.field(Convert.convert(value, Date.class), Date.class);
    }

    /**
     * Convert a temporal value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Date> date(java.util.Date value) {
        return date(Utils.field(value));
    }

    /**
     * Convert a temporal value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Date> date(Field<? extends java.util.Date> field) {
        return new DateOrTime<Date>(field, SQLDataType.DATE);
    }

    /**
     * Convert a string value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Time> time(String value) {
        return Utils.field(Convert.convert(value, Time.class), Time.class);
    }

    /**
     * Convert a temporal value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Time> time(java.util.Date value) {
        return time(Utils.field(value));
    }

    /**
     * Convert a temporal value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Time> time(Field<? extends java.util.Date> field) {
        return new DateOrTime<Time>(field, SQLDataType.TIME);
    }

    /**
     * Convert a string value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Timestamp> timestamp(String value) {
        return Utils.field(Convert.convert(value, Timestamp.class), Timestamp.class);
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Timestamp> timestamp(java.util.Date value) {
        return timestamp(Utils.field(value));
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Timestamp> timestamp(Field<? extends java.util.Date> field) {
        return new DateOrTime<Timestamp>(field, SQLDataType.TIMESTAMP);
    }

    // ------------------------------------------------------------------------
    // XXX Construction of special grouping functions
    // ------------------------------------------------------------------------

    /**
     * Create a ROLLUP(field1, field2, .., fieldn) grouping field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>CUBRID (simulated using the GROUP BY .. WITH ROLLUP clause)</li>
     * <li>DB2</li>
     * <li>MySQL (simulated using the GROUP BY .. WITH ROLLUP clause)</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     * <p>
     * Please check the SQL Server documentation for a very nice explanation of
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * clauses in grouping contexts: <a
     * href="http://msdn.microsoft.com/en-US/library/bb522495.aspx"
     * >http://msdn.microsoft.com/en-US/library/bb522495.aspx</a>
     *
     * @param fields The fields that are part of the <code>ROLLUP</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @Support({ CUBRID, MARIADB, MYSQL })
    public static GroupField rollup(Field<?>... fields) {
        return new Rollup(nullSafe(fields));
    }

    /* [pro] xx
    xxx
     x xxxxxx x xxxxxxxxxxxx xxxxxxx xxx xxxxxxx xxxxxxxx xxxxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx xxx xxxxxxxxx xxxxxxxxxx
     x xxxx
     x xxxxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxxxx
     x xxxxxxxxxx xxx xxxxxxxxxxxxx
     x xxxxx
     x xxx
     x xxxxxx xxxxx xxx xxx xxxxxx xxxxxxxxxxxxx xxx x xxxx xxxx xxxxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x xxxxxxx xx xxxxxxxx xxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x
     x xxxxxx xxxxxx xxx xxxxxx xxxx xxx xxxx xx xxx xxxxxxxxxxxxxxxxx
     x            xxxxxxxx
     x xxxxxxx x xxxxx xx xx xxxx xx x xxxxxxxxxxx xxxxxxxxx xxxxxx
     xx
    xxxxxxxxxx xxxx xxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
    x

    xxx
     x xxxxxx x xxxxxxxx xxxxxxxxxxxx xxxxxxx xxx xxxxxxx xxxxxxxx xxxxx xxxxx
     x xxxx xxxxxxxx xxx xxxx xxxxxxxx xx x xxxxxx xxxxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx xxx xxxxxxxxx xxxxxxxxxx
     x xxxx
     x xxxxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxxxx
     x xxxxxxxxxx xxx xxxxxxxxxxxxx
     x xxxxx
     x xxx
     x xxxxxx xxxxx xxx xxx xxxxxx xxxxxxxxxxxxx xxx x xxxx xxxx xxxxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x xxxxxxx xx xxxxxxxx xxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x
     x xxxxxx xxxxxx xxx xxxxxx xxxx xxx xxxx xx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x            xxxxxxxx
     x xxxxxxx x xxxxx xx xx xxxx xx x xxxxxxxxxxx xxxxxxxxx xxxxxx
     xx
    xxxxxxxxxx xxxx xxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxxxxxx

        xxx xxxx x x xx x x xxxxxxxxxxxxxx xxxx x
            xxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x

        xxxxxx xxxxxxxxxxxxxxxxxxxx
    x

    xxx
     x xxxxxx x xxxxxxxx xxxxxxxxxxxxxx xxxxxxxxx xxxxxxxxxx xxx xxxxxxxxx
     x xxxxxxxxx xxxxxxxx xxxxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx xxx xxxxxxxxx xxxxxxxxxx
     x xxxx
     x xxxxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxxxx
     x xxxxxxxxxx xxx xxxxxxxxxxxxx
     x xxxxx
     x xxx
     x xxxxxx xxxxx xxx xxx xxxxxx xxxxxxxxxxxxx xxx x xxxx xxxx xxxxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x xxxxxxx xx xxxxxxxx xxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x
     x xxxxxx xxxxxxxxx xxx xxxxxx xxxx xxx xxxx xx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x            xxxxxxxx
     x xxxxxxx x xxxxx xx xx xxxx xx x xxxxxxxxxxx xxxxxxxxx xxxxxx
     xx
    xxxxxxxxxx xxxx xxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx x
        xxxxxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxx

        xxx xxxx x x xx x x xxxxxxxxxxxxxxxxx xxxx x
            xxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x

        xxxxxx xxxxxxxxxxxxxxxxxxxx
    x

    xxx
     x xxxxxx x xxxxxxxx xxxxxxxxxxxxxx xxxxxxxxx xxxxxxxxxx xxx xxxxxxxxx
     x xxxxxxxxx xxxxxxxx xxxxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx xxx xxxxxxxxx xxxxxxxxxx
     x xxxx
     x xxxxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxxxx
     x xxxxxxxxxx xxx xxxxxxxxxxxxx
     x xxxxx
     x xxx
     x xxxxxx xxxxx xxx xxx xxxxxx xxxxxxxxxxxxx xxx x xxxx xxxx xxxxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x xxxxxxx xx xxxxxxxx xxxxxxxxx xx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     x
     x xxxxxx xxxxxxxxx xxx xxxxxx xxxx xxx xxxx xx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x            xxxxxxxx
     x xxxxxxx x xxxxx xx xx xxxx xx x xxxxxxxxxxx xxxxxxxxx xxxxxx
     xx
    xxxxxxxxxx xxxx xxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxx xxxxxxxxxx x
        xxxxxxxxxxxxx xxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxx xxxx x x xx x x xxxxxxxxxxxxxxxxx xxxx x
            xxxxxxxx x xxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x

        xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxxxxxxx xxxxxxx
    x

    xxx
     x xxxxxx x xxxxxxxxxxxxxxx xxxxxxxxxxx xxxxx xx xx xxxx xxxxx xxxx
     x xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxxxxx xxxxxxxxxxx
     x xxxxxxxxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx xxx xxxxxxxxx xxxxxxxxxx
     x xxxx
     x xxxxxxxxxxxx
     x xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxxxx
     x xxxxxxxxxx xxx xxxxxxxxxxxxx
     x xxxxx
     x
     x xxxxxx xxxxx xxx xxxxxxxx xxxxxxxx
     x xxxxxxx xxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx xxxxx
     x xxxx xxxxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxx xxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx
    x

    xxx
     x xxxxxx x xxxxxxxxxxxxxxxxxxx xxxxxxx xxx xxxxxxx xxxxxxxxxxx xxxxx xx xx
     x xxxx xxxxx xxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxx
     x xxxxxxxxxxxxxx xxxxxxxxxxx xxxxxxxxxx
     x xxx
     x xxxx xxx xxxx xxxxxxxx xx xxxx xxxx xxx xxxxxxxxx xxxxxxxxxx
     x xxxx
     x xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxxxx
     x xxxxx
     x
     x xxxxxx xxxxxx xxx xxxxxxxx xxxxxxxxx
     x xxxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx xxxxx
     x xxxx xxxxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxx xxxxxxxxxxx
    xxxxxx xxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
    x

    xx [/pro] */
    // ------------------------------------------------------------------------
    // XXX Bitwise operations
    // ------------------------------------------------------------------------

    /**
     * The MySQL <code>BIT_COUNT(field)</code> function, counting the number of
     * bits that are set in this number.
     *
     * @see #bitCount(Field)
     */
    @Support({ CUBRID, FIREBIRD, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> bitCount(Number value) {
        return bitCount(Utils.field(value));
    }

    /**
     * The MySQL <code>BIT_COUNT(field)</code> function, counting the number of
     * bits that are set in this number.
     * <p>
     * This function is simulated in most other databases like this (for a
     * TINYINT field): <code><pre>
     * ([field] &   1) +
     * ([field] &   2) >> 1 +
     * ([field] &   4) >> 2 +
     * ([field] &   8) >> 3 +
     * ([field] &  16) >> 4 +
     *  ...
     * ([field] & 128) >> 7
     * </pre></code>
     * <p>
     * More efficient algorithms are very welcome
     */
    @Support({ CUBRID, FIREBIRD, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> bitCount(Field<? extends Number> field) {
        return new BitCount(nullSafe(field));
    }

    /**
     * The bitwise not operator.
     *
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNot(T value) {
        return bitNot(Utils.field(value));
    }

    /**
     * The bitwise not operator.
     * <p>
     * Most dialects natively support this using <code>~[field]</code>. jOOQ
     * simulates this operator in some dialects using <code>-[field] - 1</code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNot(Field<T> field) {
        return new Neg<T>(nullSafe(field), ExpressionOperator.BIT_NOT);
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(T value1, T value2) {
        return bitAnd(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(T value1, Field<T> value2) {
        return bitAnd(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(Field<T> value1, T value2) {
        return bitAnd(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise and operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the and operation where available:
     * <code><pre>[field1] & [field2]</pre></code>
     * ... or the and function elsewhere:
     * <code><pre>bitand([field1], [field2])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_AND, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(T value1, T value2) {
        return bitNand(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(T value1, Field<T> value2) {
        return bitNand(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(Field<T> value1, T value2) {
        return bitNand(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise not and operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the not and operation where available:
     * <code><pre>~([field1] & [field2])</pre></code>
     * ... or the not and function elsewhere:
     * <code><pre>bitnot(bitand([field1], [field2]))</pre></code>
     *
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_NAND, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(T value1, T value2) {
        return bitOr(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(T value1, Field<T> value2) {
        return bitOr(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(Field<T> value1, T value2) {
        return bitOr(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise or operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the or operation where available:
     * <code><pre>[field1] | [field2]</pre></code>
     * ... or the or function elsewhere:
     * <code><pre>bitor([field1], [field2])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_OR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(T value1, T value2) {
        return bitNor(Utils.field(value1), Utils.field(value2));
    }
    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(T value1, Field<T> value2) {
        return bitNor(Utils.field(value1), nullSafe(value2));
    }
    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(Field<T> value1, T value2) {
        return bitNor(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise not or operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the not or operation where available:
     * <code><pre>~([field1] | [field2])</pre></code>
     * ... or the not or function elsewhere:
     * <code><pre>bitnot(bitor([field1], [field2]))</pre></code>
     *
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_NOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(T value1, T value2) {
        return bitXor(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(T value1, Field<T> value2) {
        return bitXor(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(Field<T> value1, T value2) {
        return bitXor(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise xor operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the or operation where available:
     * <code><pre>[field1] ^ [field2]</pre></code>
     * ... or the xor function elsewhere:
     * <code><pre>bitxor([field1], [field2])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_XOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(T value1, T value2) {
        return bitXNor(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(T value1, Field<T> value2) {
        return bitXNor(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(Field<T> value1, T value2) {
        return bitXNor(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise not xor operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the or operation where available:
     * <code><pre>~([field1] ^ [field2])</pre></code>
     * ... or the not xor function elsewhere:
     * <code><pre>bitnot(bitxor([field1], [field2]))</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_XNOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(T value1, T value2) {
        return shl(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(T value1, Field<T> value2) {
        return shl(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(Field<T>value1, T value2) {
        return shl(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise left shift operator.
     * <p>
     * Some dialects natively support this using <code>[field1] << [field2]</code>.
     * jOOQ simulates this operator in some dialects using
     * <code>[field1] * power(2, [field2])</code>, where power might also be simulated.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.SHL, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(T value1, T value2) {
        return shr(Utils.field(value1), Utils.field(value2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(T value1, Field<T> value2) {
        return shr(Utils.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(Field<T> value1, T value2) {
        return shr(nullSafe(value1), Utils.field(value2));
    }

    /**
     * The bitwise right shift operator.
     * <p>
     * Some dialects natively support this using <code>[field1] >> [field2]</code>.
     * jOOQ simulates this operator in some dialects using
     * <code>[field1] / power(2, [field2])</code>, where power might also be simulated.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, H2, FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.SHR, nullSafe(field1), nullSafe(field2));
    }

    // ------------------------------------------------------------------------
    // XXX Mathematical functions
    // ------------------------------------------------------------------------

    /**
     * Get the rand() function.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<BigDecimal> rand() {
        return new Rand();
    }

    /**
     * Find the greatest among all values.
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     *
     * @see #greatest(Field, Field...)
     */
    @Support
    public static <T> Field<T> greatest(T value, T... values) {
        return greatest(Utils.field(value), Utils.fields(values).toArray(new Field[0]));
    }

    /**
     * Find the greatest among all values.
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     */
    @Support
    public static <T> Field<T> greatest(Field<T> field, Field<?>... others) {
        return new Greatest<T>(nullSafeDataType(field), nullSafe(combine(field, others)));
    }

    /**
     * Find the least among all values.
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     *
     * @see #least(Field, Field...)
     */
    @Support
    public static <T> Field<T> least(T value, T... values) {
        return least(Utils.field(value), Utils.fields(values).toArray(new Field[0]));
    }

    /**
     * Find the least among all values.
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     */
    @Support
    public static <T> Field<T> least(Field<T> field, Field<?>... others) {
        return new Least<T>(nullSafeDataType(field), nullSafe(combine(field, others)));
    }

    /**
     * Get the sign of a numeric field: sign(field).
     *
     * @see #sign(Field)
     */
    @Support
    public static Field<Integer> sign(Number value) {
        return sign(Utils.field(value));
    }

    /**
     * Get the sign of a numeric field: sign(field).
     * <p>
     * This renders the sign function where available:
     * <code><pre>sign([field])</pre></code>
     * ... or simulates it elsewhere (without bind variables on values -1, 0, 1):
     * <code><pre>
     * CASE WHEN [this] > 0 THEN 1
     *      WHEN [this] < 0 THEN -1
     *      ELSE 0
     * END
     */
    @Support
    public static Field<Integer> sign(Field<? extends Number> field) {
        return new Sign(nullSafe(field));
    }

    /**
     * Get the absolute value of a numeric field: abs(field).
     *
     * @see #abs(Field)
     */
    @Support
    public static <T extends Number> Field<T> abs(T value) {
        return abs(Utils.field(value));
    }

    /**
     * Get the absolute value of a numeric field: abs(field).
     * <p>
     * This renders the same on all dialects:
     * <code><pre>abs([field])</pre></code>
     */
    @Support
    public static <T extends Number> Field<T> abs(Field<T> field) {
        return function("abs", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get rounded value of a numeric field: round(field).
     *
     * @see #round(Field)
     */
    @Support
    public static <T extends Number> Field<T> round(T value) {
        return round(Utils.field(value));
    }

    /**
     * Get rounded value of a numeric field: round(field).
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field]) or
     * round([field], 0)</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    @Support
    public static <T extends Number> Field<T> round(Field<T> field) {
        return new Round<T>(nullSafe(field));
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals).
     *
     * @see #round(Field, int)
     */
    @Support
    public static <T extends Number> Field<T> round(T value, int decimals) {
        return round(Utils.field(value), decimals);
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals).
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field], [decimals])</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    @Support
    public static <T extends Number> Field<T> round(Field<T> field, int decimals) {
        return new Round<T>(nullSafe(field), decimals);
    }

    /**
     * Get the largest integer value not greater than [this].
     *
     * @see #floor(Field)
     */
    @Support
    public static <T extends Number> Field<T> floor(T value) {
        return floor(Utils.field(value));
    }

    /**
     * Get the largest integer value not greater than [this].
     * <p>
     * This renders the floor function where available:
     * <code><pre>floor([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] - 0.499999999999999)</pre></code>
     */
    @Support
    public static <T extends Number> Field<T> floor(Field<T> field) {
        return new Floor<T>(nullSafe(field));
    }

    /**
     * Get the smallest integer value not less than [this].
     *
     * @see #ceil(Field)
     */
    @Support
    public static <T extends Number> Field<T> ceil(T value) {
        return ceil(Utils.field(value));
    }

    /**
     * Get the smallest integer value not less than [field].
     * <p>
     * This renders the ceil or ceiling function where available:
     * <code><pre>ceil([field]) or
     * ceiling([field])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([field] + 0.499999999999999)</pre></code>
     */
    @Support
    public static <T extends Number> Field<T> ceil(Field<T> field) {
        return new Ceil<T>(nullSafe(field));
    }

    /**
     * Truncate a number to a given number of decimals.
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(T number) {
        return trunc(Utils.field(number), inline(0));
    }

    /**
     * Truncate a number to a given number of decimals.
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(T number, int decimals) {
        return trunc(Utils.field(number), inline(decimals));
    }

    /**
     * Truncate a number to a given number of decimals.
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(Field<T> number, int decimals) {
        return trunc(nullSafe(number), inline(decimals));
    }

    /**
     * Truncate a number to a given number of decimals.
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(T number, Field<Integer> decimals) {
        return trunc(Utils.field(number), nullSafe(decimals));
    }

    /**
     * Truncate a number to a given number of decimals.
     * <p>
     * This function truncates <code>number</code> to the amount of decimals
     * specified in <code>decimals</code>. Passing <code>decimals = 0</code> to
     * this function is the same as using {@link #floor(Field)}. Passing
     * positive values for <code>decimal</code> has a similar effect as
     * {@link #round(Field, int)}. Passing negative values for
     * <code>decimal</code> will truncate <code>number</code> to a given power
     * of 10. Some examples
     * <table border="1">
     * <tr>
     * <th>Function call</th>
     * <th>yields...</th>
     * </tr>
     * <tr>
     * <td>trunc(125.815)</td>
     * <td>125</td>
     * </tr>
     * <tr>
     * <td>trunc(125.815, 0)</td>
     * <td>125</td>
     * </tr>
     * <tr>
     * <td>trunc(125.815, 1)</td>
     * <td>125.8</td>
     * </tr>
     * <tr>
     * <td>trunc(125.815, 2)</td>
     * <td>125.81</td>
     * </tr>
     * <tr>
     * <td>trunc(125.815, -1)</td>
     * <td>120</td>
     * </tr>
     * <tr>
     * <td>trunc(125.815, -2)</td>
     * <td>100</td>
     * </tr>
     * </table>
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(Field<T> number, Field<Integer> decimals) {
        return new Trunc<T>(nullSafe(number), nullSafe(decimals));
    }

    /**
     * Get the sqrt(field) function.
     *
     * @see #sqrt(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sqrt(Number value) {
        return sqrt(Utils.field(value));
    }

    /**
     * Get the sqrt(field) function.
     * <p>
     * This renders the sqrt function where available:
     * <code><pre>sqrt([field])</pre></code> ... or simulates it elsewhere using
     * power (which in turn may also be simulated using ln and exp functions):
     * <code><pre>power([field], 0.5)</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sqrt(Field<? extends Number> field) {
        return new Sqrt(nullSafe(field));
    }

    /**
     * Get the exp(field) function, taking this field as the power of e.
     *
     * @see #exp(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> exp(Number value) {
        return exp(Utils.field(value));
    }

    /**
     * Get the exp(field) function, taking this field as the power of e.
     * <p>
     * This renders the same on all dialects:
     * <code><pre>exp([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> exp(Field<? extends Number> field) {
        return function("exp", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field.
     *
     * @see #ln(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> ln(Number value) {
        return ln(Utils.field(value));
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field.
     * <p>
     * This renders the ln or log function where available:
     * <code><pre>ln([field]) or
     * log([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> ln(Field<? extends Number> field) {
        return new Ln(nullSafe(field));
    }

    /**
     * Get the log(field, base) function.
     *
     * @see #log(Field, int)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> log(Number value, int base) {
        return log(Utils.field(value), base);
    }

    /**
     * Get the log(field, base) function.
     * <p>
     * This renders the log function where available:
     * <code><pre>log([field])</pre></code> ... or simulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([field]) / ln([base])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> log(Field<? extends Number> field, int base) {
        return new Ln(nullSafe(field), base);
    }

    /**
     * Get the power(field, exponent) function.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Number value, Number exponent) {
        return power(Utils.field(value), Utils.field(exponent));
    }

    /**
     * Get the power(field, exponent) function.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Field<? extends Number> field, Number exponent) {
        return power(nullSafe(field), Utils.field(exponent));
    }

    /**
     * Get the power(field, exponent) function.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Number value, Field<? extends Number> exponent) {
        return power(Utils.field(value), nullSafe(exponent));
    }

    /**
     * Get the power(field, exponent) function.
     * <p>
     * This renders the power function where available:
     * <code><pre>power([field], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([field]) * [exponent])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Field<? extends Number> field, Field<? extends Number> exponent) {
        return new Power(nullSafe(field), nullSafe(exponent));
    }

    /**
     * Get the arc cosine(field) function.
     *
     * @see #acos(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> acos(Number value) {
        return acos(Utils.field(value));
    }

    /**
     * Get the arc cosine(field) function.
     * <p>
     * This renders the acos function where available:
     * <code><pre>acos([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> acos(Field<? extends Number> field) {
        return new Acos(nullSafe(field));
    }

    /**
     * Get the arc sine(field) function.
     *
     * @see #asin(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> asin(Number value) {
        return asin(Utils.field(value));
    }

    /**
     * Get the arc sine(field) function.
     * <p>
     * This renders the asin function where available:
     * <code><pre>asin([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> asin(Field<? extends Number> field) {
        return new Asin(nullSafe(field));
    }

    /**
     * Get the arc tangent(field) function.
     *
     * @see #atan(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan(Number value) {
        return atan(Utils.field(value));
    }

    /**
     * Get the arc tangent(field) function.
     * <p>
     * This renders the atan function where available:
     * <code><pre>atan([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan(Field<? extends Number> field) {
        return new Atan(nullSafe(field));
    }

    /**
     * Get the atan2(field, y) function.
     *
     * @see #atan2(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan2(Number x, Number y) {
        return atan2(Utils.field(x), Utils.field(y));
    }

    /**
     * Get the atan2(field, y) function.
     *
     * @see #atan2(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan2(Number x, Field<? extends Number> y) {
        return atan2(Utils.field(x), nullSafe(y));
    }

    /**
     * Get the atan2(field, y) function.
      *
     * @see #atan2(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Number y) {
        return atan2(nullSafe(x), Utils.field(y));
    }

    /**
     * Get the atan2(field, y) function.
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([x], [y]) or
     * atn2([x], [y])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Field<? extends Number> y) {
        return new Function<BigDecimal>(Term.ATAN2, SQLDataType.NUMERIC, nullSafe(x), nullSafe(y));
    }

    /**
     * Get the cosine(field) function.
     *
     * @see #cos(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cos(Number value) {
        return cos(Utils.field(value));
    }

    /**
     * Get the cosine(field) function.
     * <p>
     * This renders the cos function where available:
     * <code><pre>cos([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cos(Field<? extends Number> field) {
        return function("cos", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sine(field) function.
     *
     * @see #sin(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sin(Number value) {
        return sin(Utils.field(value));
    }

    /**
     * Get the sine(field) function.
     * <p>
     * This renders the sin function where available:
     * <code><pre>sin([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sin(Field<? extends Number> field) {
        return function("sin", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the tangent(field) function.
     *
     * @see #tan(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> tan(Number value) {
        return tan(Utils.field(value));
    }

    /**
     * Get the tangent(field) function.
     * <p>
     * This renders the tan function where available:
     * <code><pre>tan([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> tan(Field<? extends Number> field) {
        return function("tan", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the cotangent(field) function.
     *
     * @see #cot(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cot(Number value) {
        return cot(Utils.field(value));
    }

    /**
     * Get the cotangent(field) function.
     * <p>
     * This renders the cot function where available:
     * <code><pre>cot([field])</pre></code> ... or simulates it elsewhere using
     * sin and cos: <code><pre>cos([field]) / sin([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cot(Field<? extends Number> field) {
        return new Cot(nullSafe(field));
    }

    /**
     * Get the hyperbolic sine function: sinh(field).
     *
     * @see #sinh(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sinh(Number value) {
        return sinh(Utils.field(value));
    }

    /**
     * Get the hyperbolic sine function: sinh(field).
     * <p>
     * This renders the sinh function where available:
     * <code><pre>sinh([field])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([field] * 2) - 1) / (exp([field] * 2))</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sinh(Field<? extends Number> field) {
        return new Sinh(nullSafe(field));
    }

    /**
     * Get the hyperbolic cosine function: cosh(field).
     *
     * @see #cosh(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cosh(Number value) {
        return cosh(Utils.field(value));
    }

    /**
     * Get the hyperbolic cosine function: cosh(field).
     * <p>
     * This renders the cosh function where available:
     * <code><pre>cosh([field])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([field] * 2) + 1) / (exp([field] * 2))</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cosh(Field<? extends Number> field) {
        return new Cosh(nullSafe(field));
    }

    /**
     * Get the hyperbolic tangent function: tanh(field).
     *
     * @see #tanh(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> tanh(Number value) {
        return tanh(Utils.field(value));
    }

    /**
     * Get the hyperbolic tangent function: tanh(field).
     * <p>
     * This renders the tanh function where available:
     * <code><pre>tanh([field])</pre></code> ... or simulates it elsewhere using
     * exp:
     * <code><pre>(exp([field] * 2) - 1) / (exp([field] * 2) + 1)</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> tanh(Field<? extends Number> field) {
        return new Tanh(nullSafe(field));
    }

    /**
     * Get the hyperbolic cotangent function: coth(field).
     *
     * @see #coth(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> coth(Number value) {
        return coth(Utils.field(value));
    }

    /**
     * Get the hyperbolic cotangent function: coth(field).
     * <p>
     * This is not supported by any RDBMS, but simulated using exp exp:
     * <code><pre>(exp([field] * 2) + 1) / (exp([field] * 2) - 1)</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> coth(Field<? extends Number> field) {
        field = nullSafe(field);
        return exp(field.mul(2)).add(1).div(exp(field.mul(2)).sub(1));
    }

    /**
     * Calculate degrees from radians from this field.
     *
     * @see #deg(Field)
     */
    @Support
    public static Field<BigDecimal> deg(Number value) {
        return deg(Utils.field(value));
    }

    /**
     * Calculate degrees from radians from this field.
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([field])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[field] * 180 / PI</pre></code>
     */
    @Support
    public static Field<BigDecimal> deg(Field<? extends Number> field) {
        return new Degrees(nullSafe(field));
    }

    /**
     * Calculate radians from degrees from this field.
     *
     * @see #rad(Field)
     */
    @Support
    public static Field<BigDecimal> rad(Number value) {
        return rad(Utils.field(value));
    }

    /**
     * Calculate radians from degrees from this field.
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([field])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[field] * PI / 180</pre></code>
     */
    @Support
    public static Field<BigDecimal> rad(Field<? extends Number> field) {
        return new Radians(nullSafe(field));
    }

    // -------------------------------------------------------------------------
    // Pseudo-fields and functions for use in the context of a CONNECT BY clause
    // -------------------------------------------------------------------------

    /**
     * Retrieve the Oracle-specific <code>LEVEL</code> pseudo-field (to be used
     * along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static Field<Integer> level() {
        return field("level", Integer.class);
    }

    /**
     * Retrieve the Oracle-specific <code>CONNECT_BY_ISCYCLE</code> pseudo-field
     * (to be used along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static Field<Boolean> connectByIsCycle() {
        return field("connect_by_iscycle", Boolean.class);
    }

    /**
     * Retrieve the Oracle-specific <code>CONNECT_BY_ISLEAF</code> pseudo-field
     * (to be used along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static Field<Boolean> connectByIsLeaf() {
        return field("connect_by_isleaf", Boolean.class);
    }

    /**
     * Retrieve the Oracle-specific <code>CONNECT_BY_ROOT</code> pseudo-column
     * (to be used along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static <T> Field<T> connectByRoot(Field<T> field) {
        return field("{connect_by_root} {0}", nullSafe(field).getDataType(), field);
    }

    /**
     * Retrieve the Oracle-specific
     * <code>SYS_CONNECT_BY_PATH(field, separator)</code> function (to be used
     * along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static Field<String> sysConnectByPath(Field<?> field, String separator) {
        return field("{sys_connect_by_path}({0}, {1})", String.class, field, inline(separator));
    }

    /**
     * Add the Oracle-specific <code>PRIOR</code> unary operator before a field
     * (to be used along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static <T> Field<T> prior(Field<T> field) {
        return new Prior<T>(field);
    }

    // -------------------------------------------------------------------------
    // Other pseudo-fields and functions
    // -------------------------------------------------------------------------

    /**
     * Retrieve the Oracle-specific <code>ROWNUM</code> pseudo-field.
     */
    @Support({ CUBRID })
    public static Field<Integer> rownum() {
        return field("rownum", Integer.class);
    }

    // -------------------------------------------------------------------------
    // XXX Aggregate functions
    // -------------------------------------------------------------------------

    /**
     * Get the count(*) function.
     */
    @Support
    public static AggregateFunction<Integer> count() {
        return count(field("*", Integer.class));
    }

    /**
     * Get the count(field) function.
     */
    @Support
    public static AggregateFunction<Integer> count(Field<?> field) {
        return new Function<Integer>("count", SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the count(table) function.
     */
    @Support(POSTGRES)
    public static AggregateFunction<Integer> count(Table<?> table) {
        return new Function<Integer>("count", SQLDataType.INTEGER, tableByName(table.getName()));
    }

    /**
     * Get the count(distinct field) function.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, FIREBIRD, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static AggregateFunction<Integer> countDistinct(Field<?> field) {
        return new Function<Integer>("count", true, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the count(distinct table) function.
     */
    @Support(POSTGRES)
    public static AggregateFunction<Integer> countDistinct(Table<?> table) {
        return new Function<Integer>("count", true, SQLDataType.INTEGER, tableByName(table.getName()));
    }

    /**
     * Get the count(distinct field1, field2) function.
     * <p>
     * Some dialects support several expressions in the
     * <code>COUNT(DISTINCT expr1, expr2)</code> aggregate function.
     * <p>
     * {@link SQLDialect#POSTGRES} supports this as
     * <code>COUNT(DISTINCT(expr1, expr2))</code>.
     */
    @Support({ HSQLDB, MYSQL, POSTGRES })
    public static AggregateFunction<Integer> countDistinct(Field<?>... fields) {
        return new Function<Integer>("count", true, SQLDataType.INTEGER, nullSafe(fields));
    }

    /**
     * Get the max value over a field: max(field).
     */
    @Support
    public static <T> AggregateFunction<T> max(Field<T> field) {
        return new Function<T>("max", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the max value over a field: max(distinct field).
     */
    @Support
    public static <T> AggregateFunction<T> maxDistinct(Field<T> field) {
        return new Function<T>("max", true, nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the min value over a field: min(field).
     */
    @Support
    public static <T> AggregateFunction<T> min(Field<T> field) {
        return new Function<T>("min", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the min value over a field: min(distinct field).
     */
    @Support
    public static <T> AggregateFunction<T> minDistinct(Field<T> field) {
        return new Function<T>("min", true, nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the sum over a numeric field: sum(field).
     */
    @Support
    public static AggregateFunction<BigDecimal> sum(Field<? extends Number> field) {
        return new Function<BigDecimal>("sum", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sum over a numeric field: sum(distinct field).
     */
    @Support
    public static AggregateFunction<BigDecimal> sumDistinct(Field<? extends Number> field) {
        return new Function<BigDecimal>("sum", true, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the average over a numeric field: avg(field).
     */
    @Support
    public static AggregateFunction<BigDecimal> avg(Field<? extends Number> field) {
        return new Function<BigDecimal>("avg", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the average over a numeric field: avg(distinct field).
     */
    @Support
    public static AggregateFunction<BigDecimal> avgDistinct(Field<? extends Number> field) {
        return new Function<BigDecimal>("avg", true, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the median over a numeric field: median(field).
     */
    @Support({ CUBRID, HSQLDB })
    public static AggregateFunction<BigDecimal> median(Field<? extends Number> field) {
        return new Function<BigDecimal>("median", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the population standard deviation of a numeric field: stddev_pop(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> stddevPop(Field<? extends Number> field) {
        return new Function<BigDecimal>(Term.STDDEV_POP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sample standard deviation of a numeric field: stddev_samp(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> stddevSamp(Field<? extends Number> field) {
        return new Function<BigDecimal>(Term.STDDEV_SAMP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the population variance of a numeric field: var_pop(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> varPop(Field<? extends Number> field) {
        return new Function<BigDecimal>(Term.VAR_POP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sample variance of a numeric field: var_samp(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> varSamp(Field<? extends Number> field) {
        return new Function<BigDecimal>(Term.VAR_SAMP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the <code>REGR_SLOPE</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrSlope(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_slope", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_INTERCEPT</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrIntercept(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_intercept", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_COUNT</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrCount(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_count", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_R2</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrR2(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_r2", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_AVGX</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrAvgX(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_avgx", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_AVGY</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrAvgY(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_avgy", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_SXX</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrSXX(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_sxx", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_SYY</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrSYY(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_syy", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the <code>REGR_SXY</code> linear regression function.
     * <p>
     * The linear regression functions fit an ordinary-least-squares regression
     * line to a set of number pairs. You can use them as both aggregate and
     * window functions, where this is supported.
     * <p>
     * Note that {@link SQLDialect#DB2} does not support linear regression
     * window functions.
     */
    @Support({ POSTGRES })
    public static AggregateFunction<BigDecimal> regrSXY(Field<? extends Number> y, Field<? extends Number> x) {
        return new Function<BigDecimal>("regr_sxy", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by {@link SQLDialect#ORACLE11G} upwards. It is
     * simulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#CUBRID}: Using <code>GROUP_CONCAT()</code></li>
     * <li> {@link SQLDialect#DB2}: Using <code>XMLAGG()</code></li>
     * <li> {@link SQLDialect#H2}: Using <code>GROUP_CONCAT()</code></li>
     * <li> {@link SQLDialect#HSQLDB}: Using <code>GROUP_CONCAT()</code></li>
     * <li> {@link SQLDialect#MYSQL}: Using <code>GROUP_CONCAT()</code></li>
     * <li> {@link SQLDialect#POSTGRES}: Using <code>STRING_AGG()</code></li>
     * <li> {@link SQLDialect#SYBASE}: Using <code>LIST()</code></li>
     * </ul>
     *
     * @see #groupConcat(Field)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static OrderedAggregateFunction<String> listAgg(Field<?> field) {
        return new Function<String>(Term.LIST_AGG, SQLDataType.VARCHAR, nullSafe(field));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by {@link SQLDialect#ORACLE11G} upwards. It is
     * simulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#CUBRID}: Using <code>GROUP_CONCAT</code></li>
     * <li> {@link SQLDialect#DB2}: Using <code>XMLAGG()</code></li>
     * <li> {@link SQLDialect#H2}: Using <code>GROUP_CONCAT</code></li>
     * <li> {@link SQLDialect#HSQLDB}: Using <code>GROUP_CONCAT</code></li>
     * <li> {@link SQLDialect#MYSQL}: Using <code>GROUP_CONCAT</code></li>
     * <li> {@link SQLDialect#POSTGRES}: Using <code>STRING_AGG()</code></li>
     * <li> {@link SQLDialect#SYBASE}: Using <code>LIST()</code></li>
     * </ul>
     *
     * @see #groupConcat(Field)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static OrderedAggregateFunction<String> listAgg(Field<?> field, String separator) {
        return new Function<String>(Term.LIST_AGG, SQLDataType.VARCHAR, nullSafe(field), inline(separator));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by
     * <ul>
     * <li> {@link SQLDialect#CUBRID}</li>
     * <li> {@link SQLDialect#H2}</li>
     * <li> {@link SQLDialect#HSQLDB}</li>
     * <li> {@link SQLDialect#MYSQL}</li>
     * <li> {@link SQLDialect#SQLITE} (but without <code>ORDER BY</code>)</li>
     * </ul>
     * <p>
     * It is simulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#DB2}: Using <code>XMLAGG()</code></li>
     * <li> {@link SQLDialect#ORACLE}: Using <code>LISTAGG()</code></li>
     * <li> {@link SQLDialect#POSTGRES}: Using <code>STRING_AGG()</code></li>
     * <li> {@link SQLDialect#SYBASE}: Using <code>LIST()</code></li>
     * </ul>
     *
     * @see #listAgg(Field)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static GroupConcatOrderByStep groupConcat(Field<?> field) {
        return new GroupConcat(nullSafe(field));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by
     * <ul>
     * <li> {@link SQLDialect#CUBRID}</li>
     * <li> {@link SQLDialect#H2}</li>
     * <li> {@link SQLDialect#HSQLDB}</li>
     * <li> {@link SQLDialect#MYSQL}</li>
     * <li> {@link SQLDialect#SQLITE}</li>
     * </ul>
     * <p>
     * It is simulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#DB2}: Using <code>XMLAGG()</code></li>
     * <li> {@link SQLDialect#ORACLE}: Using <code>LISTAGG()</code></li>
     * <li> {@link SQLDialect#POSTGRES}: Using <code>STRING_AGG()</code></li>
     * <li> {@link SQLDialect#SYBASE}: Using <code>LIST()</code></li>
     * </ul>
     *
     * @see #listAgg(Field)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static AggregateFunction<String> groupConcat(Field<?> field, String separator) {
        return new GroupConcat(nullSafe(field)).separator(separator);
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by
     * <ul>
     * <li> {@link SQLDialect#CUBRID}</li>
     * <li> {@link SQLDialect#H2}</li>
     * <li> {@link SQLDialect#HSQLDB}</li>
     * <li> {@link SQLDialect#MYSQL}</li>
     * </ul>
     * <p>
     * It is simulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#SYBASE}: Using <code>LIST()</code></li>
     * <li> {@link SQLDialect#POSTGRES}: Using <code>STRING_AGG()</code></li>
     * </ul>
     *
     * @see #listAgg(Field)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static GroupConcatOrderByStep groupConcatDistinct(Field<?> field) {
        return new GroupConcat(nullSafe(field), true);
    }

    // -------------------------------------------------------------------------
    // XXX Window clauses
    // -------------------------------------------------------------------------

    /**
     * Create a {@link WindowSpecification} with a <code>PARTITION BY</code> clause.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowSpecificationOrderByStep partitionBy(Field<?>... fields) {
        return new WindowSpecificationImpl().partitionBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>PARTITION BY</code> clause.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowSpecificationOrderByStep partitionBy(Collection<? extends Field<?>> fields) {
        return new WindowSpecificationImpl().partitionBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with an <code>ORDER BY</code> clause.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowSpecificationOrderByStep orderBy(Field<?>... fields) {
        return new WindowSpecificationImpl().orderBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with an <code>ORDER BY</code> clause.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowSpecificationRowsStep orderBy(SortField<?>... fields) {
        return new WindowSpecificationImpl().orderBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with an <code>ORDER BY</code> clause.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowSpecificationRowsStep orderBy(Collection<? extends SortField<?>> fields) {
        return new WindowSpecificationImpl().orderBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationFinalStep rowsUnboundedPreceding() {
        return new WindowSpecificationImpl().rowsUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationFinalStep rowsPreceding(int number) {
        return new WindowSpecificationImpl().rowsPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationFinalStep rowsCurrentRow() {
        return new WindowSpecificationImpl().rowsCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationFinalStep rowsUnboundedFollowing() {
        return new WindowSpecificationImpl().rowsUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationFinalStep rowsFollowing(int number) {
        return new WindowSpecificationImpl().rowsFollowing(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl().rowsBetweenUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationRowsAndStep rowsBetweenPreceding(int number) {
        return new WindowSpecificationImpl().rowsBetweenPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationRowsAndStep rowsBetweenCurrentRow() {
        return new WindowSpecificationImpl().rowsBetweenCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl().rowsBetweenUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ POSTGRES })
    public static WindowSpecificationRowsAndStep rowsBetweenFollowing(int number) {
        return new WindowSpecificationImpl().rowsBetweenFollowing(number);
    }

    // -------------------------------------------------------------------------
    // XXX Window functions
    // -------------------------------------------------------------------------

    /**
     * The <code>row_number() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     * <p>
     * Newer versions of {@link SQLDialect#DERBY} and {@link SQLDialect#H2} also
     * support the <code>ROW_NUMBER() OVER()</code> window function without any
     * window clause. See the respective docs for details.
     * {@link SQLDialect#HSQLDB} can simulate this function using
     * <code>ROWNUM()</code>
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, POSTGRES })
    public static WindowOverStep<Integer> rowNumber() {
        return new Function<Integer>(ROW_NUMBER, SQLDataType.INTEGER);
    }

    /**
     * The <code>rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowOverStep<Integer> rank() {
        return new Function<Integer>("rank", SQLDataType.INTEGER);
    }

    /* [pro] xx
    xxx
     x xxx xxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxx xx xxxxxx xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxx xxxxxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x
    xx [/pro] */

    /**
     * The <code>dense_rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowOverStep<Integer> denseRank() {
        return new Function<Integer>("dense_rank", SQLDataType.INTEGER);
    }

    /* [pro] xx
    xxx
     x xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxx xx xxxxxx xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxx xxxxxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x
    xx [/pro] */

    /**
     * The <code>precent_rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowOverStep<BigDecimal> percentRank() {
        return new Function<BigDecimal>("percent_rank", SQLDataType.NUMERIC);
    }

    /* [pro] xx
    xxx
     x xxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxx xx xxxxxx xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxx xxxxxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x
    xx [/pro] */

    /**
     * The <code>cume_dist() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowOverStep<BigDecimal> cumeDist() {
        return new Function<BigDecimal>("cume_dist", SQLDataType.NUMERIC);
    }

    /* [pro] xx
    xxx
     x xxx xxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxx xx xxxxxx xxxxxxxxxxxxxxx
     x xxxxxxx xxxxxxxxx xxxxxxxxx
     xx
    xxxxxxxxxx xxxxxx xx
    xxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x
    xx [/pro] */

    /**
     * The <code>ntile([number]) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static WindowOverStep<Integer> ntile(int number) {
        return new Function<Integer>("ntile", SQLDataType.INTEGER, inline(number));
    }

    /**
     * The <code>first_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> firstValue(Field<T> field) {
        return new Function<T>("first_value", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>last_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lastValue(Field<T> field) {
        return new Function<T>("last_value", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>lead(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field) {
        return new Function<T>("lead", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>lead(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset) {
        return new Function<T>("lead", nullSafeDataType(field), nullSafe(field), inline(offset));
    }

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset, T defaultValue) {
        return lead(nullSafe(field), offset, Utils.field(defaultValue));
    }

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset, Field<T> defaultValue) {
        return new Function<T>("lead", nullSafeDataType(field), nullSafe(field), inline(offset), nullSafe(defaultValue));
    }

    /**
     * The <code>lag(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field) {
        return new Function<T>("lag", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>lag(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset) {
        return new Function<T>("lag", nullSafeDataType(field), nullSafe(field), inline(offset));
    }

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset, T defaultValue) {
        return lag(nullSafe(field), offset, Utils.field(defaultValue));
    }

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Support({ CUBRID, POSTGRES })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset, Field<T> defaultValue) {
        return new Function<T>("lag", nullSafeDataType(field), nullSafe(field), inline(offset), nullSafe(defaultValue));
    }

    // -------------------------------------------------------------------------
    // XXX Bind values
    // -------------------------------------------------------------------------

    /**
     * Create a named parameter with a generic type ({@link Object} /
     * {@link SQLDataType#OTHER}) and no initial value.
     * <p>
     * Try to avoid this method when using any of these databases, as these
     * databases may have trouble inferring the type of the bind value. Use
     * typed named parameters instead, using {@link #param(String, Class)} or
     * {@link #param(String, DataType)}
     * <ul>
     * <li> {@link SQLDialect#DB2}</li>
     * <li> {@link SQLDialect#DERBY}</li>
     * <li> {@link SQLDialect#H2}</li>
     * <li> {@link SQLDialect#HSQLDB}</li>
     * <li> {@link SQLDialect#INGRES}</li>
     * <li> {@link SQLDialect#SYBASE}</li>
     * </ul>
     *
     * @see #param(String, Object)
     */
    @Support
    public static Param<Object> param(String name) {
        return param(name, Object.class);
    }

    /**
     * Create a named parameter with a defined type and no initial value.
     *
     * @see #param(String, Object)
     */
    @Support
    public static <T> Param<T> param(String name, Class<T> type) {
        return param(name, DefaultDataType.getDataType(null, type));
    }

    /**
     * Create a named parameter with a defined type and no initial value.
     *
     * @see #param(String, Object)
     */
    @Support
    public static <T> Param<T> param(String name, DataType<T> type) {
        return new Val<T>(null, type, name);
    }

    /**
     * Create a named parameter with an initial value.
     * <p>
     * Named parameters are useful for several use-cases:
     * <ul>
     * <li>They can be used with Spring's <code>JdbcTemplate</code>, which
     * supports named parameters. Use
     * {@link DSLContext#renderNamedParams(QueryPart)} to render
     * parameter names in SQL</li>
     * <li>Named parameters can be retrieved using a well-known name from
     * {@link Query#getParam(String)} and {@link Query#getParams()}.</li>
     * </ul>
     *
     * @see Query#getParam(String)
     * @see Query#getParams()
     * @see DSLContext#renderNamedParams(QueryPart)
     */
    @Support
    public static <T> Param<T> param(String name, T value) {
        return new Val<T>(value, Utils.field(value).getDataType(), name);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static <T> Param<T> value(T value) {
        return val(value);
    }

    /**
     * A synonym for {@link #val(Object, Class)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object, Class)
     */
    @Support
    public static <T> Param<T> value(Object value, Class<T> type) {
        return val(value, type);
    }

    /**
     * A synonym for {@link #val(Object, Field)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object, Field)
     */
    @Support
    public static <T> Param<T> value(Object value, Field<T> field) {
        return val(value, field);
    }

    /**
     * A synonym for {@link #val(Object, DataType)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object, DataType)
     */
    @Support
    public static <T> Param<T> value(Object value, DataType<T> type) {
        return val(value, type);
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * The resulting bind value is always inlined, regardless of the
     * {@link Settings#getStatementType()} property of the rendering factory.
     * Unlike with {@link #field(String)}, you can expect <code>value</code> to
     * be properly escaped for SQL syntax correctness and SQL injection
     * prevention. For example:
     * <ul>
     * <li><code>inline("abc'def")</code> renders <code>'abc''def'</code></li>
     * <li><code>field("abc'def")</code> renders <code>abc'def</code></li>
     * </ul>
     *
     * @see #val(Object)
     */
    @Support
    public static <T> Param<T> inline(T value) {
        Param<T> val = val(value);
        val.setInline(true);
        return val;
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * This is a convenience method for {@link #inline(Object)}, returning
     * <code>Field&lt;String&gt;</code>, rather than
     * <code>Field&lt;Character&gt;</code>
     *
     * @see #inline(Object)
     */
    @Support
    public static Param<String> inline(char character) {
        return inline("" + character);
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * This is a convenience method for {@link #inline(Object)}, returning
     * <code>Field&lt;String&gt;</code>, rather than
     * <code>Field&lt;Character&gt;</code>
     *
     * @see #inline(Object)
     */
    @Support
    public static Param<String> inline(Character character) {
        return inline((character == null) ? null : ("" + character));
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * This is a convenience method for {@link #inline(Object)}, returning
     * <code>Field&lt;String&gt;</code>, rather than
     * <code>Field&lt;CharSequence&gt;</code>
     *
     * @see #inline(Object)
     */
    @Support
    public static Param<String> inline(CharSequence character) {

        // Delegate to inline(T)
        return (Param) inline((Object) ((character == null) ? null : ("" + character)));
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * The resulting bind value is always inlined, regardless of the
     * {@link Settings#getStatementType()} property of the rendering factory.
     * Unlike with {@link #field(String, Class)}, you can expect
     * <code>value</code> to be properly escaped for SQL syntax correctness and
     * SQL injection prevention. For example:
     * <ul>
     * <li><code>inline("abc'def")</code> renders <code>'abc''def'</code></li>
     * <li><code>field("abc'def")</code> renders <code>abc'def</code></li>
     * </ul>
     *
     * @see #val(Object, Class)
     */
    @Support
    public static <T> Param<T> inline(Object value, Class<T> type) {
        Param<T> val = val(value, type);
        val.setInline(true);
        return val;
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * The resulting bind value is always inlined, regardless of the
     * {@link Settings#getStatementType()} property of the rendering factory.
     * Unlike with {@link #field(String, DataType)}, you can expect
     * <code>value</code> to be properly escaped for SQL syntax correctness and
     * SQL injection prevention. For example:
     * <ul>
     * <li><code>inline("abc'def")</code> renders <code>'abc''def'</code></li>
     * <li><code>field("abc'def")</code> renders <code>abc'def</code></li>
     * </ul>
     *
     * @see #val(Object, Field)
     */
    @Support
    public static <T> Param<T> inline(Object value, Field<T> field) {
        Param<T> val = val(value, field);
        val.setInline(true);
        return val;
    }

    /**
     * Create a bind value, that is always inlined.
     * <p>
     * The resulting bind value is always inlined, regardless of the
     * {@link Settings#getStatementType()} property of the rendering factory.
     * Unlike with {@link #field(String, DataType)}, you can expect
     * <code>value</code> to be properly escaped for SQL syntax correctness and
     * SQL injection prevention. For example:
     * <ul>
     * <li><code>inline("abc'def")</code> renders <code>'abc''def'</code></li>
     * <li><code>field("abc'def")</code> renders <code>abc'def</code></li>
     * </ul>
     *
     * @see #val(Object, DataType)
     */
    @Support
    public static <T> Param<T> inline(Object value, DataType<T> type) {
        Param<T> val = val(value, type);
        val.setInline(true);
        return val;
    }

    /**
     * Get a bind value.
     * <p>
     * jOOQ tries to derive the RDBMS {@link DataType} from the provided Java
     * type <code>&lt;T&gt;</code>. This may not always be accurate, which can
     * lead to problems in some strongly typed RDMBS, especially when value is
     * <code>null</code>. These databases are namely:
     * <ul>
     * <li>{@link SQLDialect#DERBY}</li>
     * <li>{@link SQLDialect#DB2}</li>
     * <li>{@link SQLDialect#H2}</li>
     * <li>{@link SQLDialect#HSQLDB}</li>
     * <li>{@link SQLDialect#INGRES}</li>
     * <li>{@link SQLDialect#SYBASE}</li>
     * </ul>
     * <p>
     * If you need more type-safety, please use {@link #val(Object, DataType)}
     * instead, and provide the precise RDMBS-specific data type, that is
     * needed.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @return A field representing the constant value
     */
    @Support
    public static <T> Param<T> val(T value) {
        Class<?> type = (value == null) ? Object.class : value.getClass();
        return (Param<T>) val(value, getDataType(type));
    }

    /**
     * Get a bind value with an associated type, taken from a field.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param type The data type to enforce upon the value
     * @return A field representing the constant value
     * @see #val(Object, DataType)
     */
    @Support
    public static <T> Param<T> val(Object value, Class<T> type) {
        return val(value, getDataType(type));
    }

    /**
     * Get a bind value with an associated type, taken from a field.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param field The field whose data type to enforce upon the value
     * @return A field representing the constant value
     * @see #val(Object, DataType)
     */
    @Support
    public static <T> Param<T> val(Object value, Field<T> field) {
        return val(value, nullSafeDataType(field));
    }

    /**
     * Get a bind value with an associated type.
     * <p>
     * This will try to bind <code>value</code> as <code>type</code> in a
     * <code>PreparedStatement</code>. If <code>value</code> and
     * <code>type</code> are not compatible, jOOQ will first try to convert and
     * then to cast <code>value</code> to <code>type</code>.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param type The data type to enforce upon the value
     * @return A field representing the constant value
     */
    @Support
    public static <T> Param<T> val(Object value, DataType<T> type) {

        // Advanced data types have dedicated constant types
        if (value instanceof UDTRecord) {
            return new UDTConstant((UDTRecord) value);
        }
        /* [pro] xx
        xxxx xx xxxxxx xxxxxxxxxx xxxxxxxxxxxx x
            xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx
        x

        xx [/pro] */
        // The default behaviour
        else {
            T converted = type.convert(value);
            return new Val<T>(converted, mostSpecific(converted, type));
        }
    }

    /**
     * Get the "most specific" data type between a concrete value and an actual
     * coercion data type.
     * <p>
     * [#2007] When coercing a (previously converted) value to a type, it may be that
     * the type is still more general than the actual type. This is typically
     * the case when <code>dataType == SQLDataType.OTHER</code>, i.e. when
     * <code>dataType.getType() == Object.class</code>. In that case, it is wise
     * to keep the additional type information of the <code>value</code>
     *
     * @param value The value
     * @param dataType The coercion data type
     * @return The most specific data type
     */
    private static <T> DataType<T> mostSpecific(T value, DataType<T> dataType) {
        if (value != null) {
            Class<T> valueType = (Class<T>) value.getClass();
            Class<T> coercionType = dataType.getType();

            if (valueType != coercionType && coercionType.isAssignableFrom(valueType)) {
                return DefaultDataType.getDataType(null, valueType, dataType);
            }
        }

        return dataType;
    }

    // [jooq-tools] START [row-value]

    /**
     * Create a row value expression of degree <code>1</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1> Row1<T1> row(T1 t1) {
        return row(Utils.field(t1));
    }

    /**
     * Create a row value expression of degree <code>2</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2> Row2<T1, T2> row(T1 t1, T2 t2) {
        return row(Utils.field(t1), Utils.field(t2));
    }

    /**
     * Create a row value expression of degree <code>3</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3> Row3<T1, T2, T3> row(T1 t1, T2 t2, T3 t3) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3));
    }

    /**
     * Create a row value expression of degree <code>4</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4> Row4<T1, T2, T3, T4> row(T1 t1, T2 t2, T3 t3, T4 t4) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4));
    }

    /**
     * Create a row value expression of degree <code>5</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5> Row5<T1, T2, T3, T4, T5> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5));
    }

    /**
     * Create a row value expression of degree <code>6</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6> Row6<T1, T2, T3, T4, T5, T6> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6));
    }

    /**
     * Create a row value expression of degree <code>7</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> Row7<T1, T2, T3, T4, T5, T6, T7> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7));
    }

    /**
     * Create a row value expression of degree <code>8</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Row8<T1, T2, T3, T4, T5, T6, T7, T8> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8));
    }

    /**
     * Create a row value expression of degree <code>9</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9));
    }

    /**
     * Create a row value expression of degree <code>10</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10));
    }

    /**
     * Create a row value expression of degree <code>11</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11));
    }

    /**
     * Create a row value expression of degree <code>12</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12));
    }

    /**
     * Create a row value expression of degree <code>13</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13));
    }

    /**
     * Create a row value expression of degree <code>14</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14));
    }

    /**
     * Create a row value expression of degree <code>15</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15));
    }

    /**
     * Create a row value expression of degree <code>16</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16));
    }

    /**
     * Create a row value expression of degree <code>17</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16), Utils.field(t17));
    }

    /**
     * Create a row value expression of degree <code>18</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16), Utils.field(t17), Utils.field(t18));
    }

    /**
     * Create a row value expression of degree <code>19</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16), Utils.field(t17), Utils.field(t18), Utils.field(t19));
    }

    /**
     * Create a row value expression of degree <code>20</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16), Utils.field(t17), Utils.field(t18), Utils.field(t19), Utils.field(t20));
    }

    /**
     * Create a row value expression of degree <code>21</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16), Utils.field(t17), Utils.field(t18), Utils.field(t19), Utils.field(t20), Utils.field(t21));
    }

    /**
     * Create a row value expression of degree <code>22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return row(Utils.field(t1), Utils.field(t2), Utils.field(t3), Utils.field(t4), Utils.field(t5), Utils.field(t6), Utils.field(t7), Utils.field(t8), Utils.field(t9), Utils.field(t10), Utils.field(t11), Utils.field(t12), Utils.field(t13), Utils.field(t14), Utils.field(t15), Utils.field(t16), Utils.field(t17), Utils.field(t18), Utils.field(t19), Utils.field(t20), Utils.field(t21), Utils.field(t22));
    }

// [jooq-tools] END [row-value]

    /**
     * Create a row value expression of degree <code>N > 22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static RowN row(Object... values) {
        return row(Utils.fields(values).toArray(new Field[0]));
    }

// [jooq-tools] START [row-field]

    /**
     * Create a row value expression of degree <code>1</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1> Row1<T1> row(Field<T1> t1) {
        return new RowImpl(t1);
    }

    /**
     * Create a row value expression of degree <code>2</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2> Row2<T1, T2> row(Field<T1> t1, Field<T2> t2) {
        return new RowImpl(t1, t2);
    }

    /**
     * Create a row value expression of degree <code>3</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3> Row3<T1, T2, T3> row(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return new RowImpl(t1, t2, t3);
    }

    /**
     * Create a row value expression of degree <code>4</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4> Row4<T1, T2, T3, T4> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return new RowImpl(t1, t2, t3, t4);
    }

    /**
     * Create a row value expression of degree <code>5</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5> Row5<T1, T2, T3, T4, T5> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return new RowImpl(t1, t2, t3, t4, t5);
    }

    /**
     * Create a row value expression of degree <code>6</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6> Row6<T1, T2, T3, T4, T5, T6> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return new RowImpl(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Create a row value expression of degree <code>7</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> Row7<T1, T2, T3, T4, T5, T6, T7> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Create a row value expression of degree <code>8</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Row8<T1, T2, T3, T4, T5, T6, T7, T8> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    /**
     * Create a row value expression of degree <code>9</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    /**
     * Create a row value expression of degree <code>10</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Create a row value expression of degree <code>11</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    /**
     * Create a row value expression of degree <code>12</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    /**
     * Create a row value expression of degree <code>13</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    /**
     * Create a row value expression of degree <code>14</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    /**
     * Create a row value expression of degree <code>15</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    /**
     * Create a row value expression of degree <code>16</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Create a row value expression of degree <code>17</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    /**
     * Create a row value expression of degree <code>18</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    /**
     * Create a row value expression of degree <code>19</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    /**
     * Create a row value expression of degree <code>20</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    /**
     * Create a row value expression of degree <code>21</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    /**
     * Create a row value expression of degree <code>22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return new RowImpl(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

// [jooq-tools] END [row-field]

    /**
     * Create a row value expression of degree <code>N > 22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static RowN row(Field<?>... values) {
        return new RowImpl(values);
    }

    /**
     * Create a row value expression of degree <code>N > 22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be simulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static RowN row(Collection<?> values) {
        Collection<Field<?>> fields = new ArrayList<Field<?>>();

        for (Object o : values)
            fields.add(o instanceof Field<?> ? (Field<?>) o : val(o));

        return new RowImpl(fields);
    }

    // -------------------------------------------------------------------------
    // XXX [#915] VALUES() table constructors
    // -------------------------------------------------------------------------

    /**
     * Create a <code>VALUES()</code> expression of arbitrary degree.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2),
     *       (val2_1, val2_2),
     *       (val3_1, val3_2)
     * AS "v"("c1"  , "c2"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Support
    public static Table<Record> values(RowN... rows) {
        Values.assertNotEmpty(rows);
        int size = rows[0].size();

        String[] columns = new String[size];

        for (int i = 0; i < size; i++)
            columns[i] = "c" + i;

        return new Values<Record>(rows).as("v", columns);
    }

// [jooq-tools] START [values]

    /**
     * Create a <code>VALUES()</code> expression of degree <code>1</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1),
     *       (val2_1),
     *       (val3_1)
     * AS "v"("c1"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1") UNION ALL
     * SELECT val1_1 AS "c1") UNION ALL
     * SELECT val1_1 AS "c1")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1> Table<Record1<T1>> values(Row1<T1>... rows) {
        return new Values<Record1<T1>>(rows).as("v", "c1");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>2</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2),
     *       (val2_1, val2_2),
     *       (val3_1, val3_2)
     * AS "v"("c1"  , "c2"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2> Table<Record2<T1, T2>> values(Row2<T1, T2>... rows) {
        return new Values<Record2<T1, T2>>(rows).as("v", "c1", "c2");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>3</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3),
     *       (val2_1, val2_2, val2_3),
     *       (val3_1, val3_2, val3_3)
     * AS "v"("c1"  , "c2"  , "c3"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3> Table<Record3<T1, T2, T3>> values(Row3<T1, T2, T3>... rows) {
        return new Values<Record3<T1, T2, T3>>(rows).as("v", "c1", "c2", "c3");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>4</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4),
     *       (val2_1, val2_2, val2_3, val2_4),
     *       (val3_1, val3_2, val3_3, val3_4)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4> Table<Record4<T1, T2, T3, T4>> values(Row4<T1, T2, T3, T4>... rows) {
        return new Values<Record4<T1, T2, T3, T4>>(rows).as("v", "c1", "c2", "c3", "c4");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>5</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5> Table<Record5<T1, T2, T3, T4, T5>> values(Row5<T1, T2, T3, T4, T5>... rows) {
        return new Values<Record5<T1, T2, T3, T4, T5>>(rows).as("v", "c1", "c2", "c3", "c4", "c5");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>6</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6> Table<Record6<T1, T2, T3, T4, T5, T6>> values(Row6<T1, T2, T3, T4, T5, T6>... rows) {
        return new Values<Record6<T1, T2, T3, T4, T5, T6>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>7</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> Table<Record7<T1, T2, T3, T4, T5, T6, T7>> values(Row7<T1, T2, T3, T4, T5, T6, T7>... rows) {
        return new Values<Record7<T1, T2, T3, T4, T5, T6, T7>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>8</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Table<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> values(Row8<T1, T2, T3, T4, T5, T6, T7, T8>... rows) {
        return new Values<Record8<T1, T2, T3, T4, T5, T6, T7, T8>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>9</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Table<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> values(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... rows) {
        return new Values<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>10</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Table<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> values(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... rows) {
        return new Values<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>11</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Table<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> values(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... rows) {
        return new Values<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>12</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Table<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> values(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... rows) {
        return new Values<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>13</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Table<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> values(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... rows) {
        return new Values<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>14</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Table<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> values(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... rows) {
        return new Values<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>15</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Table<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> values(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... rows) {
        return new Values<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>16</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Table<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> values(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... rows) {
        return new Values<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>17</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16, val1_17),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16, val2_17),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16, val3_17)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  , "c17"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Table<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> values(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... rows) {
        return new Values<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>18</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16, val1_17, val1_18),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16, val2_17, val2_18),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16, val3_17, val3_18)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  , "c17"  , "c18"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Table<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> values(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... rows) {
        return new Values<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>19</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16, val1_17, val1_18, val1_19),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16, val2_17, val2_18, val2_19),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16, val3_17, val3_18, val3_19)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  , "c17"  , "c18"  , "c19"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Table<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> values(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... rows) {
        return new Values<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>20</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16, val1_17, val1_18, val1_19, val1_20),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16, val2_17, val2_18, val2_19, val2_20),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16, val3_17, val3_18, val3_19, val3_20)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  , "c17"  , "c18"  , "c19"  , "c20"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Table<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> values(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... rows) {
        return new Values<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19", "c20");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>21</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16, val1_17, val1_18, val1_19, val1_20, val1_21),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16, val2_17, val2_18, val2_19, val2_20, val2_21),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16, val3_17, val3_18, val3_19, val3_20, val3_21)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  , "c17"  , "c18"  , "c19"  , "c20"  , "c21"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20", val1_21 AS "c21") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20", val1_21 AS "c21") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20", val1_21 AS "c21")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Table<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> values(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... rows) {
        return new Values<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19", "c20", "c21");
    }

    /**
     * Create a <code>VALUES()</code> expression of degree <code>22</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
     * expressions are equivalent:
     * <p>
     * <pre><code>
     * -- Using VALUES() constructor
     * VALUES(val1_1, val1_2, val1_3, val1_4, val1_5, val1_6, val1_7, val1_8, val1_9, val1_10, val1_11, val1_12, val1_13, val1_14, val1_15, val1_16, val1_17, val1_18, val1_19, val1_20, val1_21, val1_22),
     *       (val2_1, val2_2, val2_3, val2_4, val2_5, val2_6, val2_7, val2_8, val2_9, val2_10, val2_11, val2_12, val2_13, val2_14, val2_15, val2_16, val2_17, val2_18, val2_19, val2_20, val2_21, val2_22),
     *       (val3_1, val3_2, val3_3, val3_4, val3_5, val3_6, val3_7, val3_8, val3_9, val3_10, val3_11, val3_12, val3_13, val3_14, val3_15, val3_16, val3_17, val3_18, val3_19, val3_20, val3_21, val3_22)
     * AS "v"("c1"  , "c2"  , "c3"  , "c4"  , "c5"  , "c6"  , "c7"  , "c8"  , "c9"  , "c10"  , "c11"  , "c12"  , "c13"  , "c14"  , "c15"  , "c16"  , "c17"  , "c18"  , "c19"  , "c20"  , "c21"  , "c22"  )
     *
     * -- Using UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20", val1_21 AS "c21", val1_22 AS "c22") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20", val1_21 AS "c21", val1_22 AS "c22") UNION ALL
     * SELECT val1_1 AS "c1", val1_2 AS "c2", val1_3 AS "c3", val1_4 AS "c4", val1_5 AS "c5", val1_6 AS "c6", val1_7 AS "c7", val1_8 AS "c8", val1_9 AS "c9", val1_10 AS "c10", val1_11 AS "c11", val1_12 AS "c12", val1_13 AS "c13", val1_14 AS "c14", val1_15 AS "c15", val1_16 AS "c16", val1_17 AS "c17", val1_18 AS "c18", val1_19 AS "c19", val1_20 AS "c20", val1_21 AS "c21", val1_22 AS "c22")
     * </code></pre>
     * <p>
     * Use {@link Table#as(String, String...)} to rename the resulting table and
     * its columns.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Table<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> values(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... rows) {
        return new Values<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19", "c20", "c21", "c22");
    }

// [jooq-tools] END [values]

    // -------------------------------------------------------------------------
    // XXX Literals
    // -------------------------------------------------------------------------

    /**
     * Get the null field.
     */
    static Field<?> NULL() {
        return field("null");
    }

    /**
     * Null-safety of a field.
     */
    protected static <T> Field<T> nullSafe(Field<T> field) {
        return field == null ? val((T) null) : field;
    }

    /**
     * Null-safety of a field.
     */
    protected static Field<?>[] nullSafe(Field<?>... fields) {
        if (fields == null)
            return new Field[0];

        Field<?>[] result = new Field<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            result[i] = nullSafe(fields[i]);
        }

        return result;
    }

    /**
     * Get a default data type if a field is null.
     */
    protected static <T> DataType<T> nullSafeDataType(Field<T> field) {
        return (DataType<T>) (field == null ? SQLDataType.OTHER : field.getDataType());
    }

    /**
     * A <code>0</code> literal.
     * <p>
     * This is useful for mathematical functions or for
     * <code>EXISTS (SELECT 0 ...)</code> or <code>PARTITION BY 0</code> clauses
     * and similar constructs. The <code>0</code> literal will not generate a
     * bind variable.
     *
     * @return A <code>0</code> literal as a <code>Field</code>
     */
    @Support
    public static Field<Integer> zero() {
        return inline(0);
    }

    /**
     * A <code>1</code> literal.
     * <p>
     * This is useful for mathematical functions or for
     * <code>EXISTS (SELECT 1 ...)</code> or <code>PARTITION BY 1</code> clauses
     * and similar constructs. The <code>1</code> literal will not generate a
     * bind variable.
     *
     * @return A <code>1</code> literal as a <code>Field</code>
     */
    @Support
    public static Field<Integer> one() {
        return inline(1);
    }

    /**
     * A <code>2</code> literal.
     * <p>
     * This is useful for mathematical functions. The <code>1</code> literal
     * will not generate a bind variable.
     *
     * @return A <code>2</code> literal as a <code>Field</code>
     */
    @Support
    public static Field<Integer> two() {
        return inline(2);
    }

    /**
     * The <code>PI</code> literal.
     * <p>
     * This will be any of the following:
     * <ul>
     * <li>The underlying RDBMS' <code>PI</code> literal or <code>PI()</code> function</li>
     * <li>{@link Math#PI}</li>
     * </ul>
     */
    @Support
    public static Field<BigDecimal> pi() {
        return new Pi();
    }

    /**
     * The <code>E</code> literal (Euler number).
     * <p>
     * This will be any of the following:
     * <ul>
     * <li>The underlying RDBMS' <code>E</code> literal or <code>E()</code> function</li>
     * <li>{@link Math#E}</li>
     * </ul>
     */
    @Support
    public static Field<BigDecimal> e() {
        return new Euler();
    }

    // -------------------------------------------------------------------------
    // XXX other functions
    // -------------------------------------------------------------------------

    /**
     * Get the current_user() function.
     * <p>
     * This translates into any dialect
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<String> currentUser() {
        return new CurrentUser();
    }

    /**
     * Get the default data type for the {@link DSLContext}'s underlying
     * {@link SQLDialect} and a given Java type.
     *
     * @param <T> The generic type
     * @param type The Java type
     * @return The <code>DSL</code>'s underlying default data type.
     */
    @SuppressWarnings("deprecation")
    @Support
    public static <T> DataType<T> getDataType(Class<T> type) {
        return DefaultDataType.getDataType(SQLDialect.SQL99, type);
    }

    /**
     * No instances.
     */
    protected DSL() {
        throw new UnsupportedOperationException();
    }

    /**
     * This constructor is no longer available in jOOQ 3.0.
     * <p>
     * <a href="http://www.jooq.org/doc/3.0/manual/reference/migrating-to-3.0/"
     * >See the jOOQ manual's section about migrating to jOOQ 3.0</a>
     */
    @SuppressWarnings("unused")
    private DSL(Connection connection, SQLDialect dialect) {
        throw new UnsupportedOperationException();
    }

    /**
     * This constructor is no longer available in jOOQ 3.0.
     * <p>
     * <a href="http://www.jooq.org/doc/3.0/manual/reference/migrating-to-3.0/"
     * >See the jOOQ manual's section about migrating to jOOQ 3.0</a>
     */
    @SuppressWarnings("unused")
    private DSL(Connection connection, SQLDialect dialect, Settings settings) {
        throw new UnsupportedOperationException();
    }

    /**
     * This constructor is no longer available in jOOQ 3.0.
     * <p>
     * <a href="http://www.jooq.org/doc/3.0/manual/reference/migrating-to-3.0/"
     * >See the jOOQ manual's section about migrating to jOOQ 3.0</a>
     */
    @SuppressWarnings("unused")
    private DSL(DataSource datasource, SQLDialect dialect) {
        throw new UnsupportedOperationException();
    }

    /**
     * This constructor is no longer available in jOOQ 3.0.
     * <p>
     * <a href="http://www.jooq.org/doc/3.0/manual/reference/migrating-to-3.0/"
     * >See the jOOQ manual's section about migrating to jOOQ 3.0</a>
     */
    @SuppressWarnings("unused")
    private DSL(DataSource datasource, SQLDialect dialect, Settings settings) {
        throw new UnsupportedOperationException();
    }

    /**
     * This constructor is no longer available in jOOQ 3.0.
     * <p>
     * <a href="http://www.jooq.org/doc/3.0/manual/reference/migrating-to-3.0/"
     * >See the jOOQ manual's section about migrating to jOOQ 3.0</a>
     */
    @SuppressWarnings("unused")
    private DSL(SQLDialect dialect) {
        throw new UnsupportedOperationException();
    }

    /**
     * This constructor is no longer available in jOOQ 3.0.
     * <p>
     * <a href="http://www.jooq.org/doc/3.0/manual/reference/migrating-to-3.0/"
     * >See the jOOQ manual's section about migrating to jOOQ 3.0</a>
     */
    @SuppressWarnings("unused")
    private DSL(SQLDialect dialect, Settings settings) {
        throw new UnsupportedOperationException();
    }
}
