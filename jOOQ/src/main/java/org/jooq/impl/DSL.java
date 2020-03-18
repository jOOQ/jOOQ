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
 *
 *
 *
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Operator.AND;
import static org.jooq.Operator.OR;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.impl.Names.N_SYSTEM_TIME;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.FIRST_VALUE;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.LAG;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.LAST_VALUE;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.LEAD;
import static org.jooq.impl.PositionalWindowFunction.PositionalFunctionType.NTH_VALUE;
import static org.jooq.impl.RankingFunction.RankingType.CUME_DIST;
import static org.jooq.impl.RankingFunction.RankingType.DENSE_RANK;
import static org.jooq.impl.RankingFunction.RankingType.PERCENT_RANK;
import static org.jooq.impl.RankingFunction.RankingType.RANK;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.combine;
import static org.jooq.impl.Tools.configuration;
import static org.jooq.tools.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import javax.sql.DataSource;

import org.jooq.AggregateFunction;
import org.jooq.AlterIndexOnStep;
import org.jooq.AlterIndexStep;
import org.jooq.AlterSchemaStep;
import org.jooq.AlterSequenceStep;
import org.jooq.AlterTableStep;
import org.jooq.AlterTypeStep;
import org.jooq.AlterViewStep;
import org.jooq.ArrayAggOrderByStep;
// ...
import org.jooq.Asterisk;
import org.jooq.Block;
import org.jooq.Case;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.Catalog;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.CommentOnIsStep;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.ConstraintForeignKeyReferencesStep1;
import org.jooq.ConstraintForeignKeyReferencesStep10;
import org.jooq.ConstraintForeignKeyReferencesStep11;
import org.jooq.ConstraintForeignKeyReferencesStep12;
import org.jooq.ConstraintForeignKeyReferencesStep13;
import org.jooq.ConstraintForeignKeyReferencesStep14;
import org.jooq.ConstraintForeignKeyReferencesStep15;
import org.jooq.ConstraintForeignKeyReferencesStep16;
import org.jooq.ConstraintForeignKeyReferencesStep17;
import org.jooq.ConstraintForeignKeyReferencesStep18;
import org.jooq.ConstraintForeignKeyReferencesStep19;
import org.jooq.ConstraintForeignKeyReferencesStep2;
import org.jooq.ConstraintForeignKeyReferencesStep20;
import org.jooq.ConstraintForeignKeyReferencesStep21;
import org.jooq.ConstraintForeignKeyReferencesStep22;
import org.jooq.ConstraintForeignKeyReferencesStep3;
import org.jooq.ConstraintForeignKeyReferencesStep4;
import org.jooq.ConstraintForeignKeyReferencesStep5;
import org.jooq.ConstraintForeignKeyReferencesStep6;
import org.jooq.ConstraintForeignKeyReferencesStep7;
import org.jooq.ConstraintForeignKeyReferencesStep8;
import org.jooq.ConstraintForeignKeyReferencesStep9;
import org.jooq.ConstraintForeignKeyReferencesStepN;
import org.jooq.ConstraintTypeStep;
// ...
import org.jooq.CreateIndexStep;
import org.jooq.CreateSchemaFinalStep;
import org.jooq.CreateSequenceFlagsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTypeStep;
import org.jooq.CreateViewAsStep;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DatePart;
// ...
import org.jooq.Delete;
import org.jooq.DeleteUsingStep;
import org.jooq.DerivedColumnList;
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropSequenceFinalStep;
import org.jooq.DropTableStep;
import org.jooq.DropTypeStep;
import org.jooq.DropViewFinalStep;
// ...
import org.jooq.False;
import org.jooq.Field;
import org.jooq.FieldOrRow;
// ...
import org.jooq.GrantOnStep;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.GroupConcatSeparatorStep;
import org.jooq.GroupField;
// ...
import org.jooq.Index;
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
import org.jooq.JSON;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONArrayNullStep;
import org.jooq.JSONB;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectAggNullStep;
import org.jooq.JSONObjectNullStep;
import org.jooq.Keyword;
// ...
// ...
// ...
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
import org.jooq.Name.Quoted;
import org.jooq.Operator;
import org.jooq.OrderField;
import org.jooq.OrderedAggregateFunction;
import org.jooq.OrderedAggregateFunctionOfDeferredType;
import org.jooq.Param;
// ...
import org.jooq.PlainSQL;
import org.jooq.Privilege;
// ...
import org.jooq.QuantifiedSelect;
import org.jooq.Queries;
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
import org.jooq.RecordType;
// ...
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.RevokeOnStep;
import org.jooq.Role;
import org.jooq.Row;
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
import org.jooq.RowCountQuery;
import org.jooq.RowN;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.Sequence;
import org.jooq.Statement;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.True;
import org.jooq.TruncateIdentityStep;
import org.jooq.UDTRecord;
import org.jooq.Update;
import org.jooq.UpdateSetFirstStep;
import org.jooq.User;
// ...
import org.jooq.WindowFromFirstLastStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationExcludeStep;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.WindowSpecificationRowsStep;
import org.jooq.WithAsStep;
import org.jooq.WithAsStep1;
import org.jooq.WithAsStep10;
import org.jooq.WithAsStep11;
import org.jooq.WithAsStep12;
import org.jooq.WithAsStep13;
import org.jooq.WithAsStep14;
import org.jooq.WithAsStep15;
import org.jooq.WithAsStep16;
import org.jooq.WithAsStep17;
import org.jooq.WithAsStep18;
import org.jooq.WithAsStep19;
import org.jooq.WithAsStep2;
import org.jooq.WithAsStep20;
import org.jooq.WithAsStep21;
import org.jooq.WithAsStep22;
import org.jooq.WithAsStep3;
import org.jooq.WithAsStep4;
import org.jooq.WithAsStep5;
import org.jooq.WithAsStep6;
import org.jooq.WithAsStep7;
import org.jooq.WithAsStep8;
import org.jooq.WithAsStep9;
import org.jooq.WithStep;
import org.jooq.XML;
import org.jooq.conf.Settings;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

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
     * Clients must ensure connections are closed properly by calling
     * {@link DSLContext#close()} on the resulting {@link DSLContext}. For
     * example:
     * <p>
     * <code><pre>
     * // Auto-closing DSLContext instance to free resources
     * try (DSLContext ctx = DSL.using("jdbc:h2:~/test")) {
     *
     *     // ...
     * }
     * </pre></code>
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
            throw Tools.translate("Error when initialising Connection", e);
        }
    }

    /**
     * Create an executor from a JDBC connection URL.
     * <p>
     * Clients must ensure connections are closed properly by calling
     * {@link DSLContext#close()} on the resulting {@link DSLContext}. For
     * example:
     * <p>
     * <code><pre>
     * // Auto-closing DSLContext instance to free resources
     * try (DSLContext ctx = DSL.using("jdbc:h2:~/test", "sa", "")) {
     *
     *     // ...
     * }
     * </pre></code>
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
            throw Tools.translate("Error when initialising Connection", e);
        }
    }

    /**
     * Create an executor from a JDBC connection URL.
     * <p>
     * Clients must ensure connections are closed properly by calling
     * {@link DSLContext#close()} on the resulting {@link DSLContext}. For
     * example:
     * <p>
     * <code><pre>
     * // Auto-closing DSLContext instance to free resources
     * try (DSLContext ctx = DSL.using("jdbc:h2:~/test", properties)) {
     *
     *     // ...
     * }
     * </pre></code>
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
            throw Tools.translate("Error when initialising Connection", e);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep with(String alias, String... fieldAliases) {
        return new WithImpl(null, false).with(alias, fieldAliases);
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
     * {@link #with(Name)} for strictly non-recursive CTE
     * and {@link #withRecursive(Name)} for strictly
     * recursive CTE.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep with(Name alias) {
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
     * {@link #with(Name, Name...)} for strictly non-recursive CTE
     * and {@link #withRecursive(Name, Name...)} for strictly
     * recursive CTE.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep with(Name alias, Name... fieldAliases) {
        return new WithImpl(null, false).with(alias, fieldAliases);
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
     * {@link #with(String, String...)} for strictly non-recursive CTE and
     * {@link #withRecursive(String, String...)} for strictly recursive CTE.
     * <p>
     * This works in a similar way as {@link #with(String, String...)}, except
     * that all column names are produced by a function that receives the CTE's
     * {@link Select} columns as input.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep with(String alias, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return new WithImpl(null, false).with(alias, fieldNameFunction);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep1 with(String alias, String fieldAlias1) {
        return new WithImpl(null, false).with(alias, fieldAlias1);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep2 with(String alias, String fieldAlias1, String fieldAlias2) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep3 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep4 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep5 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep6 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep7 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep8 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep9 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep10 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep11 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep12 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep13 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep14 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep15 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep16 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep17 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep18 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep19 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep20 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep21 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep22 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21, String fieldAlias22) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep1 with(Name alias, Name fieldAlias1) {
        return new WithImpl(null, false).with(alias, fieldAlias1);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep2 with(Name alias, Name fieldAlias1, Name fieldAlias2) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep3 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep4 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep5 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep6 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep7 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep8 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep9 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep10 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep11 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep12 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep13 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep14 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep15 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep16 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep17 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep18 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep19 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep20 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep21 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep22 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21, Name fieldAlias22) {
        return new WithImpl(null, false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep withRecursive(String alias, String... fieldAliases) {
        return new WithImpl(null, true).with(alias, fieldAliases);
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
     * {@link #with(Name)} for strictly non-recursive CTE
     * and {@link #withRecursive(Name)} for strictly
     * recursive CTE.
     * <p>
     * Note that the {@link SQLDialect#H2} database only supports single-table,
     * <code>RECURSIVE</code> common table expression lists.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep withRecursive(Name alias) {
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
     * {@link #with(Name, Name...)} for strictly non-recursive CTE
     * and {@link #withRecursive(Name, Name...)} for strictly
     * recursive CTE.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep withRecursive(Name alias, Name... fieldAliases) {
        return new WithImpl(null, true).with(alias, fieldAliases);
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
     * <p>
     * This works in a similar way as {@link #with(String, String...)}, except
     * that all column names are produced by a function that receives the CTE's
     * {@link Select} columns as input.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep withRecursive(String alias, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return new WithImpl(null, true).with(alias, fieldNameFunction);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep1 withRecursive(String alias, String fieldAlias1) {
        return new WithImpl(null, true).with(alias, fieldAlias1);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep2 withRecursive(String alias, String fieldAlias1, String fieldAlias2) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep3 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep4 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep5 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep6 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep7 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep8 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep9 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep10 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep11 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep12 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep13 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep14 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep15 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep16 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep17 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep18 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep19 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep20 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep21 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep22 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21, String fieldAlias22) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep1 withRecursive(Name alias, Name fieldAlias1) {
        return new WithImpl(null, true).with(alias, fieldAlias1);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep2 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep3 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep4 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep5 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep6 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep7 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep8 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep9 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep10 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep11 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep12 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep13 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep14 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep15 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep16 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep17 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep18 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep19 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep20 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep21 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithAsStep22 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21, Name fieldAlias22) {
        return new WithImpl(null, true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
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
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WithStep withRecursive(CommonTableExpression<?>... tables) {
        return new WithImpl(null, true).with(tables);
    }

    /**
     * Create a new DSL select statement, projecting the known columns from a
     * table.
     * <p>
     * This will project the known columns from the argument table querying
     * {@link Table#fields()}. If no known columns are available (e.g. because
     * the table has been created using {@link DSL#table(String)}), then
     * <code>SELECT *</code> is projected.
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
     * Example:
     * <p>
     * <code><pre>
     * SELECT table.col1, table.col2 FROM table
     * </pre></code>
     */
    @Support
    public static <R extends Record> SelectWhereStep<R> selectFrom(Table<R> table) {
        return dsl().selectFrom(table);
    }

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(Table)}), this will project <code>SELECT *</code>.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * Example:
     * <p>
     * <code><pre>
     * SELECT * FROM table
     * </pre></code>
     *
     * @see DSL#table(Name)
     */
    @Support
    public static SelectWhereStep<Record> selectFrom(Name table) {
        return dsl().selectFrom(table);
    }

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(Table)}), this will project <code>SELECT *</code>.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * Example:
     * <p>
     * <code><pre>
     * SELECT * FROM table
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    public static SelectWhereStep<Record> selectFrom(SQL sql) {
        return dsl().selectFrom(sql);
    }

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(Table)}), this will project <code>SELECT *</code>.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * Example:
     * <p>
     * <code><pre>
     * SELECT * FROM table
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see DSL#sql(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    public static SelectWhereStep<Record> selectFrom(String sql) {
        return dsl().selectFrom(sql);
    }

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(Table)}), this will project <code>SELECT *</code>.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * Example:
     * <p>
     * <code><pre>
     * SELECT * FROM table
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    public static SelectWhereStep<Record> selectFrom(String sql, Object... bindings) {
        return dsl().selectFrom(sql, bindings);
    }

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(Table)}), this will project <code>SELECT *</code>.
     * <p>
     * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
     * creates an unattached, and thus not directly renderable or executable
     * <code>SELECT</code> statement. You can use this statement in two ways:
     * <ul>
     * <li>As a subselect within another select</li>
     * <li>As a statement, after attaching it using
     * {@link Select#attach(org.jooq.Configuration)}</li>
     * </ul>
     * Example:
     * <p>
     * <code><pre>
     * SELECT * FROM table
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    public static SelectWhereStep<Record> selectFrom(String sql, QueryPart... parts) {
        return dsl().selectFrom(sql, parts);
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
     * <p>
     * Note that passing an empty collection conveniently produces
     * <code>SELECT *</code> semantics, i.e. it:
     * <ul>
     * <li>Renders <code>SELECT tab1.col1, tab1.col2, ..., tabN.colN</code> if
     * all columns are known</li>
     * <li>Renders <code>SELECT *</code> if not all columns are known, e.g. when
     * using plain SQL</li>
     * </ul>
     *
     * @see DSLContext#select(Collection)
     */
    @Support
    public static SelectSelectStep<Record> select(Collection<? extends SelectFieldOrAsterisk> fields) {
        return dsl().select(fields);
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
     * <p>
     * Note that passing an empty collection conveniently produces
     * <code>SELECT *</code> semantics, i.e. it:
     * <ul>
     * <li>Renders <code>SELECT tab1.col1, tab1.col2, ..., tabN.colN</code> if
     * all columns are known</li>
     * <li>Renders <code>SELECT *</code> if not all columns are known, e.g. when
     * using plain SQL</li>
     * </ul>
     *
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     */
    @Support
    public static SelectSelectStep<Record> select(SelectFieldOrAsterisk... fields) {
        return dsl().select(fields);
    }



    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1> SelectSelectStep<Record1<T1>> select(SelectField<T1> field1) {
        return (SelectSelectStep) select(new SelectField[] { field1 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2> SelectSelectStep<Record2<T1, T2>> select(SelectField<T1> field1, SelectField<T2> field2) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it declares
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
     * @see DSLContext#select(SelectFieldOrAsterisk...)
     * @see #select(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
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
     * selectDistinct(fields)
     *  .from(table1)
     *  .join(table2).on(field1.equal(field2))
     *  .where(field1.greaterThan(100))
     *  .orderBy(field2);
     * </pre></code>
     * <p>
     * Note that passing an empty collection conveniently produces
     * <code>SELECT DISTINCT *</code> semantics, i.e. it:
     * <ul>
     * <li>Renders <code>SELECT DISTINCT tab1.col1, tab1.col2, ..., tabN.colN</code> if
     * all columns are known</li>
     * <li>Renders <code>SELECT DISTINCT *</code> if not all columns are known, e.g. when
     * using plain SQL</li>
     * </ul>
     *
     * @see DSLContext#selectDistinct(Collection)
     */
    @Support
    public static SelectSelectStep<Record> selectDistinct(Collection<? extends SelectFieldOrAsterisk> fields) {
        return dsl().selectDistinct(fields);
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
     * <p>
     * Note that passing an empty collection conveniently produces
     * <code>SELECT DISTINCT *</code> semantics, i.e. it:
     * <ul>
     * <li>Renders <code>SELECT DISTINCT tab1.col1, tab1.col2, ..., tabN.colN</code> if
     * all columns are known</li>
     * <li>Renders <code>SELECT DISTINCT *</code> if not all columns are known, e.g. when
     * using plain SQL</li>
     * </ul>
     *
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static SelectSelectStep<Record> selectDistinct(SelectFieldOrAsterisk... fields) {
        return dsl().selectDistinct(fields);
    }



    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1> SelectSelectStep<Record1<T1>> selectDistinct(SelectField<T1> field1) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2> SelectSelectStep<Record2<T1, T2>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    /**
     * Create a new DSL subselect statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
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
     * @see DSLContext#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



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
        return dsl().selectZero();
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
        return dsl().selectOne();
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
        return dsl().selectCount();
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
        return dsl().insertInto(into);
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
    @Support
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (InsertValuesStep22) insertInto(into, new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
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
     * @see DSLContext#insertInto(Table, Field...)
     */
    @Support
    public static <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Field<?>... fields) {
        return dsl().insertInto(into, fields);
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
        return dsl().insertInto(into, fields);
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
     * simple table references. In MySQL, for instance, you can write
     * <code><pre>
     * update(t1.join(t2).on(t1.id.eq(t2.id)))
     *   .set(t1.value, value1)
     *   .set(t2.value, value2)
     *   .where(t1.id.eq(10))
     * </pre></code>
     */
    @Support
    public static <R extends Record> UpdateSetFirstStep<R> update(Table<R> table) {
        return dsl().update(table);
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return dsl().mergeInto(table);
    }



    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1> MergeKeyStep1<R, T1> mergeInto(Table<R> table, Field<T1> field1) {
        return using(new DefaultConfiguration()).mergeInto(table, field1);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2> MergeKeyStep2<R, T1, T2> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3> MergeKeyStep3<R, T1, T2, T3> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4> MergeKeyStep4<R, T1, T2, T3, T4> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5> MergeKeyStep5<R, T1, T2, T3, T4, T5> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6> MergeKeyStep6<R, T1, T2, T3, T4, T5, T6> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7> MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return using(new DefaultConfiguration()).mergeInto(table, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
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
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Field<?>... fields) {
        return dsl().mergeInto(table, fields);
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
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields) {
        return dsl().mergeInto(table, fields);
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
     * deleteFrom(table)
     *   .where(field1.greaterThan(100))
     * </pre></code>
     * <p>
     * Some but not all databases support aliased tables in delete statements.
     *
     * @see DSLContext#deleteFrom(Table)
     */
    @Support
    public static <R extends Record> DeleteUsingStep<R> deleteFrom(Table<R> table) {
        return dsl().deleteFrom(table);
    }

    /**
     * Create a new DSL delete statement.
     * <p>
     * This is an alias for {@link #deleteFrom(Table)}
     */
    @Support
    public static <R extends Record> DeleteUsingStep<R> delete(Table<R> table) {
        return dsl().deleteFrom(table);
    }

    // -------------------------------------------------------------------------
    // XXX Comments
    // -------------------------------------------------------------------------

    /**
     * Create a comment.
     */
    @Support
    public static Comment comment(String comment) {
        return isEmpty(comment) ? CommentImpl.NO_COMMENT : new CommentImpl(comment);
    }

    // -------------------------------------------------------------------------
    // XXX DDL Clauses
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL <code>COMMENT ON TABLE</code> statement.
     *
     * @see DSLContext#commentOnTable(String)
     * @see AlterTableStep#comment(Comment)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static CommentOnIsStep commentOnTable(String tableName) {
        return dsl().commentOnTable(tableName);
    }

    /**
     * Create a new DSL <code>COMMENT ON TABLE</code> statement.
     *
     * @see DSLContext#commentOnTable(Name)
     * @see AlterTableStep#comment(Comment)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static CommentOnIsStep commentOnTable(Name tableName) {
        return dsl().commentOnTable(tableName);
    }

    /**
     * Create a new DSL <code>COMMENT ON TABLE</code> statement.
     *
     * @see DSLContext#commentOnTable(Table)
     * @see AlterTableStep#comment(Comment)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static CommentOnIsStep commentOnTable(Table<?> table) {
        return dsl().commentOnTable(table);
    }

    /**
     * Create a new DSL <code>COMMENT ON VIEW</code> statement.
     *
     * @see DSLContext#commentOnView(String)
     * @see AlterViewStep#comment(Comment)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static CommentOnIsStep commentOnView(String viewName) {
        return dsl().commentOnView(viewName);
    }

    /**
     * Create a new DSL <code>COMMENT ON VIEW</code> statement.
     *
     * @see DSLContext#commentOnView(Name)
     * @see AlterViewStep#comment(Comment)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static CommentOnIsStep commentOnView(Name viewName) {
        return dsl().commentOnView(viewName);
    }

    /**
     * Create a new DSL <code>COMMENT ON VIEW</code> statement.
     *
     * @see DSLContext#commentOnView(Table)
     * @see AlterViewStep#comment(Comment)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static CommentOnIsStep commentOnView(Table<?> view) {
        return dsl().commentOnView(view);
    }

    /**
     * Create a new DSL <code>COMMENT ON COLUMN</code> statement.
     *
     * @see DSLContext#commentOnColumn(Name)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CommentOnIsStep commentOnColumn(Name columnName) {
        return dsl().commentOnColumn(columnName);
    }

    /**
     * Create a new DSL <code>COMMENT ON COLUMN</code> statement.
     *
     * @see DSLContext#commentOnColumn(Field)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CommentOnIsStep commentOnColumn(Field<?> field) {
        return dsl().commentOnColumn(field);
    }

    /**
     * Create an unnamed (system named) <code>CONSTRAINT</code> specification.
     */
    @Support
    public static ConstraintTypeStep constraint() {
        return new ConstraintImpl();
    }

    /**
     * Create a <code>CONSTRAINT</code> specification.
     */
    @Support
    public static ConstraintTypeStep constraint(Name name) {
        return new ConstraintImpl(name);
    }

    /**
     * Create a <code>CONSTRAINT</code> specification.
     */
    @Support
    public static ConstraintTypeStep constraint(String name) {
        return constraint(name(name));
    }

    /**
     * Create an unnamed (system named) <code>PRIMARY KEY</code> constraint.
     */
    @Support
    public static ConstraintEnforcementStep primaryKey(String... fields) {
        return constraint().primaryKey(fields);
    }

    /**
     * Create an unnamed (system named) <code>PRIMARY KEY</code> constraint.
     */
    @Support
    public static ConstraintEnforcementStep primaryKey(Name... fields) {
        return constraint().primaryKey(fields);
    }

    /**
     * Create an unnamed (system named) <code>PRIMARY KEY</code> constraint.
     */
    @Support
    public static ConstraintEnforcementStep primaryKey(Field<?>... fields) {
        return constraint().primaryKey(fields);
    }

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStepN foreignKey(String... fields) {
        return constraint().foreignKey(fields);
    }

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStepN foreignKey(Name... fields) {
        return constraint().foreignKey(fields);
    }

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStepN foreignKey(Field<?>... fields) {
        return constraint().foreignKey(fields);
    }



    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1> ConstraintForeignKeyReferencesStep1<T1> foreignKey(Field<T1> field1) {
        return constraint().foreignKey(field1);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2> ConstraintForeignKeyReferencesStep2<T1, T2> foreignKey(Field<T1> field1, Field<T2> field2) {
        return constraint().foreignKey(field1, field2);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3> ConstraintForeignKeyReferencesStep3<T1, T2, T3> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return constraint().foreignKey(field1, field2, field3);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4> ConstraintForeignKeyReferencesStep4<T1, T2, T3, T4> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return constraint().foreignKey(field1, field2, field3, field4);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5> ConstraintForeignKeyReferencesStep5<T1, T2, T3, T4, T5> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return constraint().foreignKey(field1, field2, field3, field4, field5);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6> ConstraintForeignKeyReferencesStep6<T1, T2, T3, T4, T5, T6> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> ConstraintForeignKeyReferencesStep7<T1, T2, T3, T4, T5, T6, T7> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> ConstraintForeignKeyReferencesStep8<T1, T2, T3, T4, T5, T6, T7, T8> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> ConstraintForeignKeyReferencesStep9<T1, T2, T3, T4, T5, T6, T7, T8, T9> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> ConstraintForeignKeyReferencesStep10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ConstraintForeignKeyReferencesStep11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ConstraintForeignKeyReferencesStep12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ConstraintForeignKeyReferencesStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ConstraintForeignKeyReferencesStep14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ConstraintForeignKeyReferencesStep15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ConstraintForeignKeyReferencesStep16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ConstraintForeignKeyReferencesStep17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ConstraintForeignKeyReferencesStep18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ConstraintForeignKeyReferencesStep19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ConstraintForeignKeyReferencesStep20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ConstraintForeignKeyReferencesStep21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ConstraintForeignKeyReferencesStep22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep1<?> foreignKey(Name field1) {
        return constraint().foreignKey(field1);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep2<?, ?> foreignKey(Name field1, Name field2) {
        return constraint().foreignKey(field1, field2);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep3<?, ?, ?> foreignKey(Name field1, Name field2, Name field3) {
        return constraint().foreignKey(field1, field2, field3);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep4<?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4) {
        return constraint().foreignKey(field1, field2, field3, field4);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep5<?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5) {
        return constraint().foreignKey(field1, field2, field3, field4, field5);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep6<?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep7<?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep8<?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep9<?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep14<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep16<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep17<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep18<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep19<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep21<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep22<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21, Name field22) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep1<?> foreignKey(String field1) {
        return constraint().foreignKey(field1);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep2<?, ?> foreignKey(String field1, String field2) {
        return constraint().foreignKey(field1, field2);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep3<?, ?, ?> foreignKey(String field1, String field2, String field3) {
        return constraint().foreignKey(field1, field2, field3);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep4<?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4) {
        return constraint().foreignKey(field1, field2, field3, field4);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep5<?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5) {
        return constraint().foreignKey(field1, field2, field3, field4, field5);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep6<?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep7<?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep8<?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep9<?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep14<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep16<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep17<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep18<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep19<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep21<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    /**
     * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    public static ConstraintForeignKeyReferencesStep22<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21, String field22) {
        return constraint().foreignKey(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
    }



    /**
     * Create an unnamed (system named) <code>UNIQUE</code> constraint.
     */
    @Support
    public static ConstraintEnforcementStep unique(String... fields) {
        return constraint().unique(fields);
    }

    /**
     * Create an unnamed (system named) <code>UNIQUE</code> constraint.
     */
    @Support
    public static ConstraintEnforcementStep unique(Name... fields) {
        return constraint().unique(fields);
    }

    /**
     * Create an unnamed (system named) <code>UNIQUE</code> constraint.
     */
    @Support
    public static ConstraintEnforcementStep unique(Field<?>... fields) {
        return constraint().unique(fields);
    }

    /**
     * Create an unnamed (system named) <code>CHECK</code> constraint.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE })
    public static ConstraintEnforcementStep check(Condition condition) {
        return constraint().check(condition);
    }

    // -------------------------------------------------------------------------
    // XXX Session Statements
    // -------------------------------------------------------------------------

    /**
     * Set the current catalog to a new value.
     *
     * @see DSL#catalog(Name)
     */
    @Support({ MARIADB, MYSQL })
    public static RowCountQuery setCatalog(String catalog) {
        return dsl().setCatalog(catalog);
    }

    /**
     * Set the current catalog to a new value.
     *
     * @see DSL#catalog(Name)
     */
    @Support({ MARIADB, MYSQL })
    public static RowCountQuery setCatalog(Name catalog) {
        return dsl().setCatalog(catalog);
    }

    /**
     * Set the current catalog to a new value.
     */
    @Support({ MARIADB, MYSQL })
    public static RowCountQuery setCatalog(Catalog catalog) {
        return dsl().setCatalog(catalog);
    }

    /**
     * Set the current schema to a new value.
     *
     * @see DSL#schema(Name)
     * @see DSLContext#setSchema(String)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static RowCountQuery setSchema(String schema) {
        return dsl().setSchema(schema);
    }

    /**
     * Set the current schema to a new value.
     *
     * @see DSL#schema(Name)
     * @see DSLContext#setSchema(Name)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static RowCountQuery setSchema(Name schema) {
        return dsl().setSchema(schema);
    }

    /**
     * Set the current schema to a new value.
     *
     * @see DSLContext#setSchema(Schema)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static RowCountQuery setSchema(Schema schema) {
        return dsl().setSchema(schema);
    }

    /**
     * Set a vendor specific flag to a new value.
     *
     * @see DSLContext#set(Name, Param)
     */
    @Support({ MYSQL })
    public static RowCountQuery set(Name name, Param<?> param) {
        return dsl().set(name, param);
    }

    // -------------------------------------------------------------------------
    // XXX DDL Statements
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL <code>CREATE SCHEMA</code> statement.
     *
     * @see DSLContext#createSchema(String)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static CreateSchemaFinalStep createSchema(String schema) {
        return dsl().createSchema(schema);
    }

    /**
     * Create a new DSL <code>CREATE SCHEMA</code> statement.
     *
     * @see DSLContext#createSchema(Name)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static CreateSchemaFinalStep createSchema(Name schema) {
        return dsl().createSchema(schema);
    }

    /**
     * Create a new DSL <code>CREATE SCHEMA</code> statement.
     *
     * @see DSLContext#createSchema(Schema)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static CreateSchemaFinalStep createSchema(Schema schema) {
        return dsl().createSchema(schema);
    }

    /**
     * Create a new DSL <code>CREATE SCHEMA</code> statement.
     *
     * @see DSLContext#createSchemaIfNotExists(String)
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static CreateSchemaFinalStep createSchemaIfNotExists(String schema) {
        return dsl().createSchemaIfNotExists(schema);
    }

    /**
     * Create a new DSL <code>CREATE SCHEMA</code> statement.
     *
     * @see DSLContext#createSchemaIfNotExists(Name)
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static CreateSchemaFinalStep createSchemaIfNotExists(Name table) {
        return dsl().createSchemaIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE SCHEMA</code> statement.
     *
     * @see DSLContext#createSchemaIfNotExists(Schema)
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static CreateSchemaFinalStep createSchemaIfNotExists(Schema schema) {
        return dsl().createSchemaIfNotExists(schema);
    }


    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTable(String)
     */
    @Support
    public static CreateTableColumnStep createTable(String table) {
        return dsl().createTable(table);
    }

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTable(Name)
     */
    @Support
    public static CreateTableColumnStep createTable(Name table) {
        return dsl().createTable(table);
    }

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTable(Table)
     */
    @Support
    public static CreateTableColumnStep createTable(Table<?> table) {
        return dsl().createTable(table);
    }

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTableIfNotExists(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateTableColumnStep createTableIfNotExists(String table) {
        return dsl().createTableIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTableIfNotExists(Name)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateTableColumnStep createTableIfNotExists(Name table) {
        return dsl().createTableIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE TABLE</code> statement.
     *
     * @see DSLContext#createTableIfNotExists(Table)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateTableColumnStep createTableIfNotExists(Table<?> table) {
        return dsl().createTableIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createTemporaryTable(String)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createTemporaryTable(String table) {
        return dsl().createTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>CREATE TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createTemporaryTable(Name)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createTemporaryTable(Name table) {
        return dsl().createTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>CREATE TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createTemporaryTable(Table)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createTemporaryTable(Table<?> table) {
        return dsl().createTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>CREATE TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createTemporaryTableIfNotExists(String)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createTemporaryTableIfNotExists(String table) {
        return dsl().createTemporaryTableIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createTemporaryTableIfNotExists(Name)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createTemporaryTableIfNotExists(Name table) {
        return dsl().createTemporaryTableIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createTemporaryTableIfNotExists(Table)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createTemporaryTableIfNotExists(Table<?> table) {
        return dsl().createTemporaryTableIfNotExists(table);
    }

    /**
     * Create a new DSL <code>CREATE GLOBAL TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createGlobalTemporaryTable(String)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createGlobalTemporaryTable(String table) {
        return dsl().createGlobalTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>CREATE GLOBAL TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createGlobalTemporaryTable(Name)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createGlobalTemporaryTable(Name table) {
        return dsl().createGlobalTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>CREATE GLOBAL TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#createGlobalTemporaryTable(Table)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static CreateTableColumnStep createGlobalTemporaryTable(Table<?> table) {
        return dsl().createGlobalTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     *
     * @see DSLContext#createView(String, String...)
     */
    @Support
    public static CreateViewAsStep createView(String view, String... fields) {
        return dsl().createView(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     *
     * @see DSLContext#createView(Name, Name...)
     */
    @Support
    public static CreateViewAsStep createView(Name view, Name... fields) {
        return dsl().createView(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     *
     * @see DSLContext#createView(Table, Field...)
     */
    @Support
    public static CreateViewAsStep createView(Table<?> view, Field<?>... fields) {
        return dsl().createView(view, fields);
    }


    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     * <p>
     * This works like {@link #createView(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createView(String, String...)
     */
    @Support
    public static CreateViewAsStep createView(String view, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return dsl().createView(view, fieldNameFunction);
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     * <p>
     * This works like {@link #createView(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createView(Name, Name...)
     */
    @Support
    public static CreateViewAsStep createView(Name view, Function<? super Field<?>, ? extends Name> fieldNameFunction) {
        return dsl().createView(view, fieldNameFunction);
    }

    /**
     * Create a new DSL <code>CREATE VIEW</code> statement.
     * <p>
     * This works like {@link #createView(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createView(Table, Field...)
     */
    @Support
    public static CreateViewAsStep createView(Table<?> view, Function<? super Field<?>, ? extends Field<?>> fieldNameFunction) {
        return dsl().createView(view, fieldNameFunction);
    }


    /**
     * Create a new DSL <code>CREATE OR REPLACE VIEW</code> statement.
     *
     * @see DSLContext#createOrReplaceView(String, String...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static CreateViewAsStep createOrReplaceView(String view, String... fields) {
        return dsl().createOrReplaceView(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE OR REPLACE VIEW</code> statement.
     *
     * @see DSLContext#createOrReplaceView(Name, Name...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static CreateViewAsStep createOrReplaceView(Name view, Name... fields) {
        return dsl().createOrReplaceView(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE OR REPLACE VIEW</code> statement.
     *
     * @see DSLContext#createOrReplaceView(Table, Field...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static CreateViewAsStep createOrReplaceView(Table<?> view, Field<?>... fields) {
        return dsl().createOrReplaceView(view, fields);
    }


    /**
     * Create a new DSL <code>CREATE OR REPLACE VIEW</code> statement.
     * <p>
     * This works like {@link #createOrReplaceView(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createOrReplaceView(String, String...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static CreateViewAsStep createOrReplaceView(String view, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return dsl().createOrReplaceView(view, fieldNameFunction);
    }

    /**
     * Create a new DSL <code>CREATE OR REPLACE VIEW</code> statement.
     * <p>
     * This works like {@link #createOrReplaceView(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createOrReplaceView(Name, Name...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static CreateViewAsStep createOrReplaceView(Name view, Function<? super Field<?>, ? extends Name> fieldNameFunction) {
        return dsl().createOrReplaceView(view, fieldNameFunction);
    }

    /**
     * Create a new DSL <code>CREATE OR REPLACE VIEW</code> statement.
     * <p>
     * This works like {@link #createOrReplaceView(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createOrReplaceView(Table, Field...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static CreateViewAsStep createOrReplaceView(Table<?> view, Function<? super Field<?>, ? extends Field<?>> fieldNameFunction) {
        return dsl().createOrReplaceView(view, fieldNameFunction);
    }


    /**
     * Create a new DSL <code>CREATE VIEW IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createViewIfNotExists(String, String...)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateViewAsStep createViewIfNotExists(String view, String... fields) {
        return dsl().createViewIfNotExists(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE VIEW IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createViewIfNotExists(Name, Name...)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateViewAsStep createViewIfNotExists(Name view, Name... fields) {
        return dsl().createViewIfNotExists(view, fields);
    }

    /**
     * Create a new DSL <code>CREATE VIEW IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createViewIfNotExists(Table, Field...)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateViewAsStep createViewIfNotExists(Table<?> view, Field<?>... fields) {
        return dsl().createViewIfNotExists(view, fields);
    }


    /**
     * Create a new DSL <code>CREATE VIEW IF NOT EXISTS</code> statement.
     * <p>
     * This works like {@link #createViewIfNotExists(String, String...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createViewIfNotExists(String, String...)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateViewAsStep createViewIfNotExists(String view, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return dsl().createViewIfNotExists(view, fieldNameFunction);
    }

    /**
     * Create a new DSL <code>CREATE VIEW IF NOT EXISTS</code> statement.
     * <p>
     * This works like {@link #createViewIfNotExists(Name, Name...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createViewIfNotExists(Name, Name...)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateViewAsStep createViewIfNotExists(Name view, Function<? super Field<?>, ? extends Name> fieldNameFunction) {
        return dsl().createViewIfNotExists(view, fieldNameFunction);
    }

    /**
     * Create a new DSL <code>CREATE VIEW IF NOT EXISTS</code> statement.
     * <p>
     * This works like {@link #createViewIfNotExists(Table, Field...)} except that the
     * view's field names are derived from the view's {@link Select} statement
     * using a function.
     *
     * @see DSLContext#createViewIfNotExists(Table, Field...)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static CreateViewAsStep createViewIfNotExists(Table<?> view, Function<? super Field<?>, ? extends Field<?>> fieldNameFunction) {
        return dsl().createViewIfNotExists(view, fieldNameFunction);
    }


    /**
     * Create a new DSL <code>CREATE TYPE</code> statement.
     *
     * @see DSLContext#createType(String)
     */
    @Support({ H2, POSTGRES })
    public static CreateTypeStep createType(String type) {
        return dsl().createType(type);
    }

    /**
     * Create a new DSL <code>CREATE TYPE</code> statement.
     *
     * @see DSLContext#createType(Name)
     */
    @Support({ H2, POSTGRES })
    public static CreateTypeStep createType(Name type) {
        return dsl().createType(type);
    }

    /**
     * Create a new DSL <code>ALTER TYPE</code> statement.
     *
     * @see DSLContext#alterType(String)
     */
    @Support({ POSTGRES })
    public static AlterTypeStep alterType(String type) {
        return dsl().alterType(type);
    }

    /**
     * Create a new DSL <code>ALTER TYPE</code> statement.
     *
     * @see DSLContext#alterType(Name)
     */
    @Support({ POSTGRES })
    public static AlterTypeStep alterType(Name type) {
        return dsl().alterType(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropType(String)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropType(String type) {
        return dsl().dropType(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropType(Name)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropType(Name type) {
        return dsl().dropType(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropType(String...)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropType(String... type) {
        return dsl().dropType(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropType(Name...)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropType(Name... type) {
        return dsl().dropType(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropType(Collection)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropType(Collection<?> type) {
        return dsl().dropType(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropTypeIfExists(String)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropTypeIfExists(String type) {
        return dsl().dropTypeIfExists(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropTypeIfExists(Name)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropTypeIfExists(Name type) {
        return dsl().dropTypeIfExists(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropTypeIfExists(String...)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropTypeIfExists(String... type) {
        return dsl().dropTypeIfExists(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropTypeIfExists(Name...)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropTypeIfExists(Name... type) {
        return dsl().dropTypeIfExists(type);
    }

    /**
     * Create a new DSL <code>DROP TYPE</code> statement.
     *
     * @see DSL#dropTypeIfExists(Collection)
     */
    @Support({ H2, POSTGRES })
    public static DropTypeStep dropTypeIfExists(Collection<?> type) {
        return dsl().dropTypeIfExists(type);
    }

    /**
     * Create a new DSL <code>CREATE INDEX</code> statement.
     *
     * @see DSLContext#createIndex()
     */
    @Support
    public static CreateIndexStep createIndex() {
        return dsl().createIndex();
    }

    /**
     * Create a new DSL <code>CREATE INDEX</code> statement.
     *
     * @see DSLContext#createIndex(String)
     */
    @Support
    public static CreateIndexStep createIndex(String index) {
        return dsl().createIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE INDEX</code> statement.
     *
     * @see DSLContext#createIndex(Name)
     */
    @Support
    public static CreateIndexStep createIndex(Name index) {
        return dsl().createIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE INDEX</code> statement.
     *
     * @see DSLContext#createIndex(Index)
     */
    @Support
    public static CreateIndexStep createIndex(Index index) {
        return dsl().createIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE INDEX IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createIndexIfNotExists(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES, SQLITE })
    public static CreateIndexStep createIndexIfNotExists(String index) {
        return dsl().createIndexIfNotExists(index);
    }

    /**
     * Create a new DSL <code>CREATE INDEX IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createIndexIfNotExists(Name)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES, SQLITE })
    public static CreateIndexStep createIndexIfNotExists(Name index) {
        return dsl().createIndexIfNotExists(index);
    }

    /**
     * Create a new DSL <code>CREATE INDEX IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createIndexIfNotExists(Index)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES, SQLITE })
    public static CreateIndexStep createIndexIfNotExists(Index index) {
        return dsl().createIndexIfNotExists(index);
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX</code> statement.
     *
     * @see DSLContext#createUniqueIndex()
     */
    @Support
    public static CreateIndexStep createUniqueIndex() {
        return dsl().createUniqueIndex();
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX</code> statement.
     *
     * @see DSLContext#createUniqueIndex(String)
     */
    @Support
    public static CreateIndexStep createUniqueIndex(String index) {
        return dsl().createUniqueIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX</code> statement.
     *
     * @see DSLContext#createUniqueIndex(Name)
     */
    @Support
    public static CreateIndexStep createUniqueIndex(Name index) {
        return dsl().createUniqueIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX</code> statement.
     *
     * @see DSLContext#createUniqueIndex(Index)
     */
    @Support
    public static CreateIndexStep createUniqueIndex(Index index) {
        return dsl().createUniqueIndex(index);
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createUniqueIndexIfNotExists(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE })
    public static CreateIndexStep createUniqueIndexIfNotExists(String index) {
        return dsl().createUniqueIndexIfNotExists(index);
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createUniqueIndexIfNotExists(Name)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE })
    public static CreateIndexStep createUniqueIndexIfNotExists(Name index) {
        return dsl().createUniqueIndexIfNotExists(index);
    }

    /**
     * Create a new DSL <code>CREATE UNIQUE INDEX IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createUniqueIndexIfNotExists(Index)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE })
    public static CreateIndexStep createUniqueIndexIfNotExists(Index index) {
        return dsl().createUniqueIndexIfNotExists(index);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE</code> statement.
     *
     * @see DSLContext#createSequence(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CreateSequenceFlagsStep createSequence(String sequence) {
        return dsl().createSequence(sequence);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE</code> statement.
     *
     * @see DSLContext#createSequence(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CreateSequenceFlagsStep createSequence(Name sequence) {
        return dsl().createSequence(sequence);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE</code> statement.
     *
     * @see DSLContext#createSequence(Sequence)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CreateSequenceFlagsStep createSequence(Sequence<?> sequence) {
        return dsl().createSequence(sequence);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createSequenceIfNotExists(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CreateSequenceFlagsStep createSequenceIfNotExists(String sequence) {
        return dsl().createSequenceIfNotExists(sequence);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createSequenceIfNotExists(Name)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CreateSequenceFlagsStep createSequenceIfNotExists(Name sequence) {
        return dsl().createSequenceIfNotExists(sequence);
    }

    /**
     * Create a new DSL <code>CREATE SEQUENCE IF NOT EXISTS</code> statement.
     *
     * @see DSLContext#createSequenceIfNotExists(Sequence)
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static CreateSequenceFlagsStep createSequenceIfNotExists(Sequence<?> sequence) {
        return dsl().createSequenceIfNotExists(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequence(String)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static AlterSequenceStep<BigInteger> alterSequence(String sequence) {
        return dsl().alterSequence(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequence(Name)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static AlterSequenceStep<BigInteger> alterSequence(Name sequence) {
        return dsl().alterSequence(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequence(Sequence)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> AlterSequenceStep<T> alterSequence(Sequence<T> sequence) {
        return dsl().alterSequence(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequenceIfExists(String)
     */
    @Support({ H2, MARIADB, POSTGRES })
    public static AlterSequenceStep<BigInteger> alterSequenceIfExists(String sequence) {
        return dsl().alterSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequenceIfExists(Name)
     */
    @Support({ H2, MARIADB, POSTGRES })
    public static AlterSequenceStep<BigInteger> alterSequenceIfExists(Name sequence) {
        return dsl().alterSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL <code>ALTER SEQUENCE</code> statement.
     *
     * @see DSLContext#alterSequenceIfExists(Sequence)
     */
    @Support({ H2, MARIADB, POSTGRES })
    public static <T extends Number> AlterSequenceStep<T> alterSequenceIfExists(Sequence<T> sequence) {
        return dsl().alterSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTable(String)
     */
    @Support
    public static AlterTableStep alterTable(String table) {
        return dsl().alterTable(table);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTable(Name)
     */
    @Support
    public static AlterTableStep alterTable(Name table) {
        return dsl().alterTable(table);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTable(Table)
     */
    @Support
    public static AlterTableStep alterTable(Table<?> table) {
        return dsl().alterTable(table);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTableIfExists(String)
     */
    @Support({ H2, MARIADB, POSTGRES })
    public static AlterTableStep alterTableIfExists(String table) {
        return dsl().alterTableIfExists(table);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTableIfExists(Name)
     */
    @Support({ H2, MARIADB, POSTGRES })
    public static AlterTableStep alterTableIfExists(Name table) {
        return dsl().alterTableIfExists(table);
    }

    /**
     * Create a new DSL <code>ALTER TABLE</code> statement.
     *
     * @see DSLContext#alterTableIfExists(Table)
     */
    @Support({ H2, MARIADB, POSTGRES })
    public static AlterTableStep alterTableIfExists(Table<?> table) {
        return dsl().alterTableIfExists(table);
    }

    /**
     * Create a new DSL <code>ALTER SCHEMA</code> statement.
     *
     * @see DSLContext#alterSchema(String)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static AlterSchemaStep alterSchema(String schema) {
        return dsl().alterSchema(schema);
    }

    /**
     * Create a new DSL <code>ALTER SCHEMA</code> statement.
     *
     * @see DSLContext#alterSchema(Name)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static AlterSchemaStep alterSchema(Name schema) {
        return dsl().alterSchema(schema);
    }

    /**
     * Create a new DSL <code>ALTER SCHEMA</code> statement.
     *
     * @see DSLContext#alterSchema(Schema)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static AlterSchemaStep alterSchema(Schema schema) {
        return dsl().alterSchema(schema);
    }

    /**
     * Create a new DSL <code>ALTER SCHEMA</code> statement.
     *
     * @see DSLContext#alterSchemaIfExists(String)
     */
    @Support({ H2 })
    public static AlterSchemaStep alterSchemaIfExists(String schema) {
        return dsl().alterSchemaIfExists(schema);
    }

    /**
     * Create a new DSL <code>ALTER SCHEMA</code> statement.
     *
     * @see DSLContext#alterSchemaIfExists(Name)
     */
    @Support({ H2 })
    public static AlterSchemaStep alterSchemaIfExists(Name schema) {
        return dsl().alterSchemaIfExists(schema);
    }

    /**
     * Create a new DSL <code>ALTER SCHEMA</code> statement.
     *
     * @see DSLContext#alterSchemaIfExists(Schema)
     */
    @Support({ H2 })
    public static AlterSchemaStep alterSchemaIfExists(Schema schema) {
        return dsl().alterSchemaIfExists(schema);
    }

    /**
     * Create a new DSL <code>ALTER VIEW</code> statement.
     *
     * @see DSLContext#alterView(String)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static AlterViewStep alterView(String view) {
        return dsl().alterView(view);
    }

    /**
     * Create a new DSL <code>ALTER VIEW</code> statement.
     *
     * @see DSLContext#alterView(Name)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static AlterViewStep alterView(Name view) {
        return dsl().alterView(view);
    }

    /**
     * Create a new DSL <code>ALTER VIEW</code> statement.
     *
     * @see DSLContext#alterView(Table)
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    public static AlterViewStep alterView(Table<?> view) {
        return dsl().alterView(view);
    }

    /**
     * Create a new DSL <code>ALTER VIEW</code> statement.
     *
     * @see DSLContext#alterViewIfExists(String)
     */
    @Support({ H2, POSTGRES })
    public static AlterViewStep alterViewIfExists(String view) {
        return dsl().alterViewIfExists(view);
    }

    /**
     * Create a new DSL <code>ALTER VIEW</code> statement.
     *
     * @see DSLContext#alterViewIfExists(Name)
     */
    @Support({ H2, POSTGRES })
    public static AlterViewStep alterViewIfExists(Name view) {
        return dsl().alterViewIfExists(view);
    }

    /**
     * Create a new DSL <code>ALTER VIEW</code> statement.
     *
     * @see DSLContext#alterViewIfExists(Table)
     */
    @Support({ H2, POSTGRES })
    public static AlterViewStep alterViewIfExists(Table<?> view) {
        return dsl().alterViewIfExists(view);
    }

    /**
     * Create a new DSL <code>ALTER INDEX</code> statement.
     *
     * @see DSLContext#alterIndex(String)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AlterIndexOnStep alterIndex(String index) {
        return dsl().alterIndex(index);
    }

    /**
     * Create a new DSL <code>ALTER INDEX</code> statement.
     *
     * @see DSLContext#alterIndex(Name)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AlterIndexOnStep alterIndex(Name index) {
        return dsl().alterIndex(index);
    }

    /**
     * Create a new DSL <code>ALTER INDEX</code> statement.
     *
     * @see DSLContext#alterIndex(Index)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AlterIndexOnStep alterIndex(Index index) {
        return dsl().alterIndex(index);
    }

    /**
     * Create a new DSL <code>ALTER INDEX</code> statement.
     *
     * @see DSLContext#alterIndexIfExists(String)
     */
    @Support({ H2, POSTGRES })
    public static AlterIndexStep alterIndexIfExists(String index) {
        return dsl().alterIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>ALTER INDEX</code> statement.
     *
     * @see DSLContext#alterIndexIfExists(Name)
     */
    @Support({ H2, POSTGRES })
    public static AlterIndexStep alterIndexIfExists(Name index) {
        return dsl().alterIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>ALTER INDEX</code> statement.
     *
     * @see DSLContext#alterIndexIfExists(Index)
     */
    @Support({ H2, POSTGRES })
    public static AlterIndexStep alterIndexIfExists(Index index) {
        return dsl().alterIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>DROP SCHEMA</code> statement.
     *
     * @see DSLContext#dropSchema(String)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static DropSchemaStep dropSchema(String schema) {
        return dsl().dropSchema(schema);
    }

    /**
     * Create a new DSL <code>DROP SCHEMA</code> statement.
     *
     * @see DSLContext#dropSchema(Name)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static DropSchemaStep dropSchema(Name schema) {
        return dsl().dropSchema(schema);
    }

    /**
     * Create a new DSL <code>DROP SCHEMA</code> statement.
     *
     * @see DSLContext#dropSchema(Schema)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static DropSchemaStep dropSchema(Schema schema) {
        return dsl().dropSchema(schema);
    }

    /**
     * Create a new DSL <code>DROP SCHEMA</code> statement.
     *
     * @see DSLContext#dropSchemaIfExists(String)
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static DropSchemaStep dropSchemaIfExists(String schema) {
        return dsl().dropSchemaIfExists(schema);
    }

    /**
     * Create a new DSL <code>DROP SCHEMA</code> statement.
     *
     * @see DSLContext#dropSchemaIfExists(Name)
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static DropSchemaStep dropSchemaIfExists(Name schema) {
        return dsl().dropSchemaIfExists(schema);
    }

    /**
     * Create a new DSL <code>DROP SCHEMA</code> statement.
     *
     * @see DSLContext#dropSchemaIfExists(Schema)
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static DropSchemaStep dropSchemaIfExists(Schema schema) {
        return dsl().dropSchemaIfExists(schema);
    }

    /**
     * Create a new DSL <code>DROP VIEW</code> statement.
     *
     * @see DSLContext#dropView(String)
     */
    @Support
    public static DropViewFinalStep dropView(String view) {
        return dsl().dropView(view);
    }

    /**
     * Create a new DSL <code>DROP VIEW</code> statement.
     *
     * @see DSLContext#dropView(Name)
     */
    @Support
    public static DropViewFinalStep dropView(Name view) {
        return dsl().dropView(view);
    }

    /**
     * Create a new DSL <code>DROP VIEW</code> statement.
     *
     * @see DSLContext#dropView(Table)
     */
    @Support
    public static DropViewFinalStep dropView(Table<?> view) {
        return dsl().dropView(view);
    }

    /**
     * Create a new DSL <code>DROP VIEW IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropViewIfExists(String)
     */
    @Support
    public static DropViewFinalStep dropViewIfExists(String view) {
        return dsl().dropViewIfExists(view);
    }

    /**
     * Create a new DSL <code>DROP VIEW IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropViewIfExists(Name)
     */
    @Support
    public static DropViewFinalStep dropViewIfExists(Name view) {
        return dsl().dropViewIfExists(view);
    }

    /**
     * Create a new DSL <code>DROP VIEW IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropViewIfExists(Table)
     */
    @Support
    public static DropViewFinalStep dropViewIfExists(Table<?> view) {
        return dsl().dropViewIfExists(view);
    }

    /**
     * Create a new DSL <code>DROP TABLE</code> statement.
     *
     * @see DSLContext#dropTable(String)
     */
    @Support
    public static DropTableStep dropTable(String table) {
        return dsl().dropTable(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE</code> statement.
     *
     * @see DSLContext#dropTable(Name)
     */
    @Support
    public static DropTableStep dropTable(Name table) {
        return dsl().dropTable(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE</code> statement.
     *
     * @see DSLContext#dropTable(Table)
     */
    @Support
    public static DropTableStep dropTable(Table<?> table) {
        return dsl().dropTable(table);
    }

    /**
     * Create a new DSL <code>DROP TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#dropTemporaryTable(String)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static DropTableStep dropTemporaryTable(String table) {
        return dsl().dropTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>DROP TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#dropTemporaryTable(Name)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static DropTableStep dropTemporaryTable(Name table) {
        return dsl().dropTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>DROP TEMPORARY TABLE</code> statement.
     *
     * @see DSLContext#dropTemporaryTable(Table)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static DropTableStep dropTemporaryTable(Table<?> table) {
        return dsl().dropTemporaryTable(table);
    }

    /**
     * Create a new DSL <code>DROP TEMPORARY TABLE IF EXISTS</code> statement.
     *
     * @see DSLContext#dropTemporaryTableIfExists(String)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static DropTableStep dropTemporaryTableIfExists(String table) {
        return dsl().dropTemporaryTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TEMPORARY TABLE IF EXISTS</code> statement.
     *
     * @see DSLContext#dropTemporaryTableIfExists(Name)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static DropTableStep dropTemporaryTableIfExists(Name table) {
        return dsl().dropTemporaryTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TEMPORARY TABLE IF EXISTS</code> statement.
     *
     * @see DSLContext#dropTemporaryTableIfExists(Table)
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    public static DropTableStep dropTemporaryTableIfExists(Table<?> table) {
        return dsl().dropTemporaryTableIfExists(table);
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
    public static DropTableStep dropTableIfExists(String table) {
        return dsl().dropTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropTableIfExists(Name)
     */
    @Support
    public static DropTableStep dropTableIfExists(Name table) {
        return dsl().dropTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP TABLE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropTableIfExists(Table)
     */
    @Support
    public static DropTableStep dropTableIfExists(Table<?> table) {
        return dsl().dropTableIfExists(table);
    }

    /**
     * Create a new DSL <code>DROP INDEX</code> statement.
     *
     * @see DSLContext#dropIndex(String)
     */
    @Support
    public static DropIndexOnStep dropIndex(String index) {
        return dsl().dropIndex(index);
    }

    /**
     * Create a new DSL <code>DROP INDEX</code> statement.
     *
     * @see DSLContext#dropIndex(Name)
     */
    @Support
    public static DropIndexOnStep dropIndex(Name index) {
        return dsl().dropIndex(index);
    }

    /**
     * Create a new DSL <code>DROP INDEX</code> statement.
     *
     * @see DSLContext#dropIndex(Index)
     */
    @Support
    public static DropIndexOnStep dropIndex(Index index) {
        return dsl().dropIndex(index);
    }

    /**
     * Create a new DSL <code>DROP INDEX IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropIndexIfExists(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES, SQLITE })
    public static DropIndexOnStep dropIndexIfExists(String index) {
        return dsl().dropIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>DROP INDEX IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropIndexIfExists(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES, SQLITE })
    public static DropIndexOnStep dropIndexIfExists(Name index) {
        return dsl().dropIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>DROP INDEX IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropIndexIfExists(Index)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES, SQLITE })
    public static DropIndexOnStep dropIndexIfExists(Index index) {
        return dsl().dropIndexIfExists(index);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE</code> statement.
     *
     * @see DSLContext#dropSequence(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequence(String sequence) {
        return dsl().dropSequence(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE</code> statement.
     *
     * @see DSLContext#dropSequence(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequence(Name sequence) {
        return dsl().dropSequence(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE</code> statement.
     *
     * @see DSLContext#dropSequence(Sequence)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequence(Sequence<?> sequence) {
        return dsl().dropSequence(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropSequenceIfExists(String)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequenceIfExists(String sequence) {
        return dsl().dropSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropSequenceIfExists(Name)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequenceIfExists(Name sequence) {
        return dsl().dropSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL <code>DROP SEQUENCE IF EXISTS</code> statement.
     * <p>
     * If your database doesn't natively support <code>IF EXISTS</code>, this is
     * emulated by catching (and ignoring) the relevant {@link SQLException}.
     *
     * @see DSLContext#dropSequenceIfExists(Sequence)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> DropSequenceFinalStep dropSequenceIfExists(Sequence<?> sequence) {
        return dsl().dropSequenceIfExists(sequence);
    }

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Synonym for {@link #truncateTable(String)}
     */
    @Support
    public static TruncateIdentityStep<Record> truncate(String table) {
        return truncateTable(table);
    }

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Synonym for {@link #truncateTable(Name)}
     */
    @Support
    public static TruncateIdentityStep<Record> truncate(Name table) {
        return truncateTable(table);
    }

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Synonym for {@link #truncateTable(Table)}
     */
    @Support
    public static <R extends Record> TruncateIdentityStep<R> truncate(Table<R> table) {
        return truncateTable(table);
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
     * supported, it is emulated using an equivalent <code>DELETE</code>
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
     * These vendor-specific extensions are currently not emulated for those
     * dialects that do not support them natively.
     *
     * @see DSLContext#truncate(String)
     */
    @Support
    public static TruncateIdentityStep<Record> truncateTable(String table) {
        return dsl().truncateTable(table);
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
     * supported, it is emulated using an equivalent <code>DELETE</code>
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
     * These vendor-specific extensions are currently not emulated for those
     * dialects that do not support them natively.
     *
     * @see DSLContext#truncate(Name)
     */
    @Support
    public static TruncateIdentityStep<Record> truncateTable(Name table) {
        return dsl().truncateTable(table);
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
     * supported, it is emulated using an equivalent <code>DELETE</code>
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
     * These vendor-specific extensions are currently not emulated for those
     * dialects that do not support them natively.
     *
     * @see DSLContext#truncate(Table)
     */
    @Support
    public static <R extends Record> TruncateIdentityStep<R> truncateTable(Table<R> table) {
        return dsl().truncateTable(table);
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> QuantifiedSelect<R> all(Select<R> select) {
        return new QuantifiedSelectImpl<>(Quantifier.ALL, select);
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> all(T... array) {
        if (array instanceof Field[])
            return all((Field<T>[]) array);
        return new QuantifiedSelectImpl<>(Quantifier.ALL, val(array));
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> all(Field<T[]> array) {
        return new QuantifiedSelectImpl<>(Quantifier.ALL, array);
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support
    @SafeVarargs
    public static <T> QuantifiedSelect<Record1<T>> all(Field<T>... fields) {
        return new QuantifiedSelectImpl<>(Quantifier.ALL, fields);
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <R extends Record> QuantifiedSelect<R> any(Select<R> select) {
        return new QuantifiedSelectImpl<>(Quantifier.ANY, select);
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> any(T... array) {
        if (array instanceof Field[])
            return any((Field<T>[]) array);
        return new QuantifiedSelectImpl<>(Quantifier.ANY, val(array));
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> QuantifiedSelect<Record1<T>> any(Field<T[]> array) {
        return new QuantifiedSelectImpl<>(Quantifier.ANY, array);
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
     * @see Field#like(QuantifiedSelect)
     */
    @Support
    @SafeVarargs
    public static <T> QuantifiedSelect<Record1<T>> any(Field<T>... fields) {
        return new QuantifiedSelectImpl<>(Quantifier.ANY, fields);
    }

    // -------------------------------------------------------------------------
    // XXX Access control
    // -------------------------------------------------------------------------

    /**
     * Grant a privilege on table to user or role.
     *
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * grant(privilege)
     *   .on(table)
     *   .to(user)
     *   .execute();
     *
     * grant(privilege)
     *   .on(table)
     *   .to(role)
     *   .execute();
     * </pre></code>
     *
     *
     * @see #grant(Collection)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static GrantOnStep grant(Privilege privilege) {
        return dsl().grant(privilege);
    }

    /**
     * Grant privileges on table to user or role.
     *
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * grant(privilege)
     *   .on(table)
     *   .to(user)
     *   .execute();
     *
     * grant(privilege)
     *   .on(table)
     *   .to(role)
     *   .execute();
     * </pre></code>
     *
     *
     * @see #grant(Collection)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static GrantOnStep grant(Privilege... privileges) {
        return dsl().grant(privileges);
    }

    /**
     * Grant privileges on table to user or role.
     *
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * grant(privileges)
     *   .on(table)
     *   .to(user)
     *   .execute();
     *
     * grant(privileges)
     *   .on(table)
     *   .to(role)
     *   .execute();
     * </pre></code>
     * <p>
     *
     * @see #grant(Privilege...)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static GrantOnStep grant(Collection<? extends Privilege> privileges) {
        return dsl().grant(privileges);
    }

    /**
     * Revoke a privilege on table from user or role.
     *
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * revoke(privilege)
     *   .on(table)
     *   .from(user)
     *   .execute();
     *
     * revoke(privilege)
     *   .on(table)
     *   .from(role)
     *   .execute();
     * </pre></code>
     * <p>
     *
     * @see #revoke(Collection)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static RevokeOnStep revoke(Privilege privilege) {
        return dsl().revoke(privilege);
    }

    /**
     * Revoke privileges on table from user or role.
     *
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * revoke(privilege)
     *   .on(table)
     *   .from(user)
     *   .execute();
     *
     * revoke(privilege)
     *   .on(table)
     *   .from(role)
     *   .execute();
     * </pre></code>
     * <p>
     *
     * @see #revoke(Collection)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static RevokeOnStep revoke(Privilege... privileges) {
        return dsl().revoke(privileges);
    }

    /**
     * Revoke privileges on table from user or role.
     *
     * <p>
     * Example: <code><pre>
     * import static org.jooq.impl.DSL.*;
     *
     * revoke(privileges)
     *   .on(table)
     *   .from(user)
     *   .execute();
     *
     * revoke(privileges)
     *   .on(table)
     *   .from(role)
     *   .execute();
     * </pre></code>
     * <p>
     *
     * @see #revoke(Privilege...)
     */
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static RevokeOnStep revoke(Collection<? extends Privilege> privileges) {
        return dsl().revoke(privileges);
    }

    /**
     * Revoke grant option for a privilege on a table from user or role.
     */
    @Support({ HSQLDB, POSTGRES })
    public static RevokeOnStep revokeGrantOptionFor(Privilege privilege) {
        return dsl().revoke(privilege);
    }

    /**
     * Revoke grant option for some privileges on a table from user or role.
     */
    @Support({ HSQLDB, POSTGRES })
    public static RevokeOnStep revokeGrantOptionFor(Privilege... privileges) {
        return dsl().revoke(privileges);
    }

    /**
     * Revoke grant option for some privileges on a table from user or role.
     */
    @Support({ HSQLDB, POSTGRES })
    public static RevokeOnStep revokeGrantOptionFor(Collection<? extends Privilege> privileges) {
        return dsl().revoke(privileges);
    }

    // -------------------------------------------------------------------------
    // XXX Other objects
    // -------------------------------------------------------------------------

    /**
     * Create a collation by its unqualified name.
     */
    @Support({ HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Collation collation(String collation) {
        return collation(name(collation));
    }

    /**
     * Create a collation by its qualified name.
     */
    @Support({ HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Collation collation(Name collation) {
        return new CollationImpl(collation);
    }

    /**
     * Create a character set by its unqualified name.
     */
    @Support({ MARIADB, MYSQL })
    public static CharacterSet characterSet(String characterSet) {
        return characterSet(name(characterSet));
    }

    /**
     * Create a character set by its qualified name.
     */
    @Support({ MARIADB, MYSQL })
    public static CharacterSet characterSet(Name characterSet) {
        return new CharacterSetImpl(characterSet);
    }

    /**
     * Create a new privilege reference.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @PlainSQL
    @Support
    public static Privilege privilege(String privilege) {
        return privilege(keyword(privilege));
    }

    /**
     * Create a new privilege reference.
     */
    @Support
    static Privilege privilege(Keyword privilege) {
        return new PrivilegeImpl(privilege);
    }

    /**
     * Create a new user reference.
     *
     * @see #user(Name)
     */
    @Support
    public static User user(String name) {
        return user(name(name));
    }

    /**
     * Create a new user reference.
     */
    @Support
    public static User user(Name name) {
        return new UserImpl(name);
    }

    /**
     * Create a new role reference.
     *
     * @see #role(Name)
     */
    @Support
    public static Role role(String name) {
        return role(name(name));
    }

    /**
     * Create a new role reference.
     */
    @Support
    public static Role role(Name name) {
        return new RoleImpl(name);
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

        Row[] rows = new Row[size];
        for (int i = 0; i < size; i++)
            rows[i] = result.get(i).valuesRow();

        Field<?>[] fields = result.fields();
        String[] columns = new String[fields.length];
        for (int i = 0; i < fields.length; i++)
            columns[i] = fields[i].getName();

        // TODO [#2986] Coerce the record type upon the resulting table.
        return (Table<R>) values0(rows).as("v", columns);
    }

    /**
     * Use a previously obtained record as a new Table
     */
    @Support
    public static <R extends Record> Table<R> table(R record) {
        return table((R[]) new Record[] { record });
    }

    /**
     * Use a previously obtained set of records as a new Table
     */
    @Support
    public static <R extends Record> Table<R> table(R... records) {
        if (records == null || records.length == 0)
            return (Table<R>) new Dual();

        Result<R> result = new ResultImpl(configuration(records[0]), records[0].fields());
        result.addAll(Arrays.asList(records));

        return table(result);
    }

    /**
     * A synonym for {@link #unnest(Collection)}.
     *
     * @see #unnest(Collection)
     */
    @Support
    public static Table<?> table(Collection<?> list) {
        return table(list.toArray());
    }

    /**
     * A synonym for {@link #unnest(Object[])}.
     *
     * @see #unnest(Object[])
     */
    @Support
    public static Table<?> table(Object[] array) {
        return unnest0(val(array));
    }














    /**
     * A synonym for {@link #unnest(Field)}.
     *
     * @see #unnest(Field)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Table<?> table(Field<?> cursor) {
        return unnest0(cursor);
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
     * In all other dialects, unnesting of arrays is emulated using several
     * <code>UNION ALL</code> connected subqueries.
     */
    @Support
    public static Table<?> unnest(Collection<?> list) {
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
     * In all other dialects, unnesting of arrays is emulated using several
     * <code>UNION ALL</code> connected subqueries.
     */
    @Support
    public static Table<?> unnest(Object[] array) {
        return unnest0(val(array));
    }















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
     * emulated using several <code>UNION ALL</code> connected subqueries.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Table<?> unnest(Field<?> cursor) {
        return unnest0(cursor);
    }

    private static Table<?> unnest0(Field<?> cursor) {
        if (cursor == null)
            throw new IllegalArgumentException();

        // The field is an actual CURSOR or REF CURSOR returned from a stored
        // procedure or from a NESTED TABLE
        else if (cursor.getType() == Result.class)
            return new FunctionTable<Record>(cursor);











        // The field is a regular array
        else if (cursor.getType().isArray() && cursor.getType() != byte[].class)
            return new ArrayTable(cursor);

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
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects
     * may be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c- 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(int from, int to, int step) {
        return generateSeries(val(from), val(to), val(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c - 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(int from, Field<Integer> to, int step) {
        return generateSeries(val(from), nullSafe(to), val(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c - 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(Field<Integer> from, int to, int step) {
        return new GenerateSeries(nullSafe(from), val(to), val(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c - 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(Field<Integer> from, Field<Integer> to, int step) {
        return new GenerateSeries(nullSafe(from), nullSafe(to), val(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects
     * may be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c- 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(int from, int to, Field<Integer> step) {
        return generateSeries(val(from), val(to), nullSafe(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c - 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(int from, Field<Integer> to, Field<Integer> step) {
        return generateSeries(val(from), nullSafe(to), nullSafe(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c - 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(Field<Integer> from, int to, Field<Integer> step) {
        return new GenerateSeries(nullSafe(from), val(to), nullSafe(step));
    }

    /**
     * A table function generating a series of values from <code>from</code> to
     * <code>to</code> (inclusive), increasing values by <code>step</code>.
     * <p>
     * This function is inspired by PostgreSQL's
     * <code>GENERATE_SERIES(from, to, step)</code> function. Other SQL dialects may
     * be capable of emulating this behaviour, e.g. Oracle: <code><pre>
     * -- PostgreSQL
     * SELECT * FROM GENERATE_SERIES(a, b, c)
     *
     * -- Oracle
     * SELECT * FROM (SELECT a + LEVEL * c - 1 FROM DUAL CONNECT BY a + LEVEL * c - 1 &lt;= b)
     * </pre></code>
     */
    @Support({ CUBRID, POSTGRES })
    public static Table<Record1<Integer>> generateSeries(Field<Integer> from, Field<Integer> to, Field<Integer> step) {
        return new GenerateSeries(nullSafe(from), nullSafe(to), nullSafe(step));
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
    @Support({ MYSQL, POSTGRES })
    public static <R extends Record> Table<R> lateral(TableLike<R> table) {
        return new Lateral<>(table.asTable());
    }

    /**
     * Create a <code>ROWS FROM (tables...)</code> expression.
     * <p>
     * Example: <code><pre>
     * SELECT *
     * FROM ROWS FROM (function1('a', 'b'), function2('c', 'd'));
     * </pre></code>
     * <p>
     * This allows for full outer joining several table-valued functions on the
     * row number of each function's produced rows.
     */
    @Support({ POSTGRES })
    public static Table<Record> rowsFrom(Table<?>... tables) {
        return new RowsFrom(tables);
    }

    // -------------------------------------------------------------------------
    // XXX SQL keywords
    // -------------------------------------------------------------------------

    /**
     * Create a SQL keyword.
     * <p>
     * A <code>Keyword</code> is a {@link QueryPart} that renders a SQL keyword
     * according to the settings specified in
     * {@link Settings#getRenderKeywordCase()}. It can be embedded in other
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
    @Support
    public static Keyword keyword(String keyword) {
        return new KeywordImpl(keyword);
    }

    // -------------------------------------------------------------------------
    // XXX Names
    // -------------------------------------------------------------------------

    /**
     * Create a new SQL identifier using an unqualified name.
     * <p>
     * Use this method to construct syntax-safe, SQL-injection-safe SQL
     * identifiers for use in plain SQL where {@link QueryPart} objects are
     * accepted. For instance, this can be used with any of these methods:
     * <ul>
     * <li> {@link #field(Name)}</li>
     * <li> {@link #field(Name, Class)}</li>
     * <li> {@link #field(Name, DataType)}</li>
     * </ul>
     * <p>
     * An example: <code><pre>
     * // This unqualified name here
     * name("book");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [book].[title]
     * </pre></code>
     *
     * @param unqualifiedName The SQL identifier's unqualified name
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name name(String unqualifiedName) {
        return new UnqualifiedName(unqualifiedName);
    }

    /**
     * Create a new SQL identifier using a qualified name.
     * <p>
     * Use this method to construct syntax-safe, SQL-injection-safe SQL
     * identifiers for use in plain SQL where {@link QueryPart} objects are
     * accepted. For instance, this can be used with any of these methods:
     * <ul>
     * <li> {@link #field(Name)}</li>
     * <li> {@link #field(Name, Class)}</li>
     * <li> {@link #field(Name, DataType)}</li>
     * </ul>
     * <p>
     * An example: <code><pre>
     * // This qualified name here
     * name("book", "title");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [book].[title]
     * </pre></code>
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name name(String... qualifiedName) {
        if (qualifiedName == null || qualifiedName.length != 1)
            return new QualifiedName(qualifiedName);
        else
            return new UnqualifiedName(qualifiedName[0]);
    }

    /**
     * Create a new SQL identifier using a qualified name.
     * <p>
     * Unlike other {@link #name(String...)} constructors, this one constructs a
     * name from its argument {@link Name#unqualifiedName()} parts, retaining
     * the quoted flag, to construct a new name.
     * <p>
     * Use this method to construct syntax-safe, SQL-injection-safe SQL
     * identifiers for use in plain SQL where {@link QueryPart} objects are
     * accepted. For instance, this can be used with any of these methods:
     * <ul>
     * <li>{@link #field(Name)}</li>
     * <li>{@link #field(Name, Class)}</li>
     * <li>{@link #field(Name, DataType)}</li>
     * </ul>
     * <p>
     * An example: <code><pre>
     * // This qualified name here
     * name(quotedName("book"), unquotedName("title"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [book].title
     * </pre></code>
     *
     * @param nameParts The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name name(Name... nameParts) {
        return new QualifiedName(nameParts);
    }

    /**
     * Create a new SQL identifier using a qualified name.
     * <p>
     * Use this method to construct syntax-safe, SQL-injection-safe SQL
     * identifiers for use in plain SQL where {@link QueryPart} objects are
     * accepted. For instance, this can be used with any of these methods:
     * <ul>
     * <li> {@link #field(Name)}</li>
     * <li> {@link #field(Name, Class)}</li>
     * <li> {@link #field(Name, DataType)}</li>
     * </ul>
     * <p>
     * An example: <code><pre>
     * // This qualified name here
     * name("book", "title");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [book].[title]
     * </pre></code>
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name name(Collection<String> qualifiedName) {
        return name(qualifiedName.toArray(Tools.EMPTY_STRING));
    }

    /**
     * Create a new SQL identifier using an unqualified, quoted name.
     * <p>
     * This works like {@link #name(String...)}, except that generated
     * identifiers will be guaranteed to be quoted in databases that support
     * quoted identifiers.
     *
     * @param unqualifiedName The SQL identifier's unqualified name
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name quotedName(String unqualifiedName) {
        return new UnqualifiedName(unqualifiedName, Quoted.QUOTED);
    }

    /**
     * Create a new SQL identifier using a qualified, quoted name.
     * <p>
     * This works like {@link #name(String...)}, except that generated
     * identifiers will be guaranteed to be quoted in databases that support
     * quoted identifiers.
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name quotedName(String... qualifiedName) {
        return new QualifiedName(qualifiedName, Quoted.QUOTED);
    }

    /**
     * Create a new SQL identifier using a qualified, quoted name.
     * <p>
     * This works like {@link #name(Collection)}, except that generated
     * identifiers will be guaranteed to be quoted in databases that support
     * quoted identifiers.
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name quotedName(Collection<String> qualifiedName) {
        return quotedName(qualifiedName.toArray(Tools.EMPTY_STRING));
    }

    /**
     * Create a new SQL identifier using an unqualified, quoted name.
     * <p>
     * This works like {@link #name(String...)}, except that generated
     * identifiers will be guaranteed to be quoted in databases that support
     * quoted identifiers.
     *
     * @param unqualifiedName The SQL identifier's unqualified name
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name unquotedName(String unqualifiedName) {
        return new UnqualifiedName(unqualifiedName, Quoted.UNQUOTED);
    }

    /**
     * Create a new SQL identifier using a qualified, quoted name.
     * <p>
     * This works like {@link #name(String...)}, except that generated
     * identifiers will be guaranteed to be quoted in databases that support
     * quoted identifiers.
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name unquotedName(String... qualifiedName) {
        if (qualifiedName == null || qualifiedName.length != 1)
            return new QualifiedName(qualifiedName, Quoted.UNQUOTED);
        else
            return new UnqualifiedName(qualifiedName[0], Quoted.UNQUOTED);
    }

    /**
     * Create a new SQL identifier using a qualified, quoted name.
     * <p>
     * This works like {@link #name(Collection)}, except that generated
     * identifiers will be guaranteed to be quoted in databases that support
     * quoted identifiers.
     *
     * @param qualifiedName The SQL identifier's qualified name parts
     * @return A {@link QueryPart} that will render the SQL identifier
     */
    @Support
    public static Name unquotedName(Collection<String> qualifiedName) {
        return unquotedName(qualifiedName.toArray(Tools.EMPTY_STRING));
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart composition
    // -------------------------------------------------------------------------

    /**
     * Compose a list of <code>QueryParts</code> into a new
     * <code>QueryPart</code>, with individual parts being comma-separated.
     */
    @Support
    public static QueryPart list(QueryPart... parts) {
        return list(Arrays.asList(parts));
    }

    /**
     * Compose a list of <code>QueryParts</code> into a new
     * <code>QueryPart</code>, with individual parts being comma-separated.
     */
    @Support
    public static QueryPart list(Collection<? extends QueryPart> parts) {
        return new QueryPartList<>(parts);
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
     * <p>
     * This is an alias for {@link #default_()}.
     *
     * @see #default_()
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Object> defaultValue() {
        return default_();
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     * <p>
     * This is an alias for {@link #default_(Class)}.
     *
     * @see #default_(Class)
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> Field<T> defaultValue(Class<T> type) {
        return default_(type);
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     * <p>
     * This is an alias for {@link #default_(DataType)}.
     *
     * @see #default_(DataType)
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> Field<T> defaultValue(DataType<T> type) {
        return default_(type);
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     * <p>
     * This is an alias for {@link #default_(Field)}.
     *
     * @see #default_(Field)
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> Field<T> defaultValue(Field<T> field) {
        return default_(field);
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     * <p>
     * While the <code>DEFAULT</code> keyword works with all data types, you may
     * still prefer to associate a {@link Field} type with your
     * <code>DEFAULT</code> value. In that case, use
     * {@link #defaultValue(Class)} or {@link #defaultValue(DataType)} instead.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Object> default_() {
        return default_(Object.class);
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> Field<T> default_(Class<T> type) {
        return default_(getDataType(type));
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> Field<T> default_(DataType<T> type) {
        return new SQLField<>(type, keyword("default"));
    }

    /**
     * Create a <code>DEFAULT</code> keyword for use with <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>MERGE</code> statements.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T> Field<T> default_(Field<T> field) {
        return new SQLField<>(field.getDataType(), keyword("default"));
    }

    /**
     * Create a qualified schema, given its schema name.
     * <p>
     * This constructs a schema reference given the schema's qualified name.
     * <p>
     * Example: <code><pre>
     * // This schema...
     * schemaByName("MY_SCHEMA");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA]
     * </pre></code>
     *
     * @param name The schema's reference name.
     * @return A schema referenced by <code>name</code>
     * @deprecated - [#3843] - 3.6.0 - use {@link #schema(Name)} instead
     */
    @Deprecated
    @Support
    public static Schema schemaByName(String name) {
        return new SchemaImpl(name);
    }

    /**
     * Create a qualified catalog, given its catalog name.
     * <p>
     * This constructs a catalog reference given the catalog's qualified name.
     * <p>
     * Example: <code><pre>
     * // This catalog...
     * catalog(name("MY_CATALOG"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_CATALOG]
     * </pre></code>
     */
    @Support
    public static Catalog catalog(Name name) {
        return new CatalogImpl(name);
    }

    /**
     * Create a qualified schema, given its schema name.
     * <p>
     * This constructs a schema reference given the schema's qualified name.
     * <p>
     * Example: <code><pre>
     * // This schema...
     * schema(name("MY_CATALOG", "MY_SCHEMA"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_CATALOG].[MY_SCHEMA]
     * </pre></code>
     */
    @Support
    public static Schema schema(Name name) {
        return new SchemaImpl(name);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequenceByName("MY_SCHEMA", "MY_SEQUENCE");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your sequence's
     *            reference name.
     * @return A sequence referenced by <code>sequenceName</code>
     * @deprecated - [#3843] - 3.6.0 - use {@link #sequence(Name)} instead
     */
    @Deprecated
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static Sequence<BigInteger> sequenceByName(String... qualifiedName) {
        return sequenceByName(BigInteger.class, qualifiedName);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequenceByName("MY_SCHEMA", "MY_SEQUENCE");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your sequence's
     *            reference name.
     * @param type The type of the returned field
     * @return A sequence referenced by <code>sequenceName</code>
     * @deprecated - [#3843] - 3.6.0 - use {@link #sequence(Name, Class)} instead
     */
    @Deprecated
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> Sequence<T> sequenceByName(Class<T> type, String... qualifiedName) {
        return sequenceByName(getDataType(type), qualifiedName);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequenceByName("MY_SCHEMA", "MY_SEQUENCE");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your sequence's
     *            reference name.
     * @param type The type of the returned field
     * @return A sequence referenced by <code>sequenceName</code>
     * @deprecated - [#3843] - 3.6.0 - use {@link #sequence(Name, DataType)} instead
     */
    @Deprecated
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> Sequence<T> sequenceByName(DataType<T> type, String... qualifiedName) {
        if (qualifiedName == null)
            throw new NullPointerException();

        if (qualifiedName.length < 1 || qualifiedName.length > 2)
            throw new IllegalArgumentException("Must provide a qualified name of length 1 or 2 : " + name(qualifiedName));

        String name = qualifiedName[qualifiedName.length - 1];
        Schema schema = qualifiedName.length == 2 ? schemaByName(qualifiedName[0]) : null;

        return new SequenceImpl<>(name, schema, type);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequence(name("MY_SCHEMA", "MY_SEQUENCE"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static Sequence<BigInteger> sequence(Name name) {
        return sequence(name, BigInteger.class);
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequence(name("MY_SCHEMA", "MY_SEQUENCE"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> Sequence<T> sequence(Name name, Class<T> type) {
        return sequence(name, getDataType(type));
    }

    /**
     * Create a qualified sequence, given its sequence name.
     * <p>
     * This constructs a sequence reference given the sequence's qualified name.
     * <p>
     * Example: <code><pre>
     * // This sequence...
     * sequence(name("MY_SCHEMA", "MY_SEQUENCE"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_SEQUENCE]
     * </pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    public static <T extends Number> Sequence<T> sequence(Name name, DataType<T> type) {
        if (name == null)
            throw new NullPointerException();

        if (name.getName().length < 1 || name.getName().length > 2)
            throw new IllegalArgumentException("Must provide a qualified name of length 1 or 2 : " + name);

        Name n = name.unqualifiedName();
        Schema s = name.parts().length == 2 ? schema(name.qualifier()) : null;

        return new SequenceImpl<>(n, s, type, false);
    }

    /**
     * Create a qualified table, given its table name.
     * <p>
     * This constructs a table reference given the table's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This table...
     * tableByName("MY_SCHEMA", "MY_TABLE");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE]
     * </pre></code>
     *
     * @param qualifiedName The various parts making up your table's reference
     *            name.
     * @return A table referenced by <code>tableName</code>
     * @deprecated - [#3843] - 3.6.0 - use {@link #table(Name)} instead
     */
    @Deprecated
    @Support
    public static Table<Record> tableByName(String... qualifiedName) {
        return table(name(qualifiedName));
    }

    /**
     * Create a qualified table, given its table name.
     * <p>
     * This constructs a table reference given the table's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This table...
     * tableByName("MY_SCHEMA", "MY_TABLE");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE]
     * </pre></code>
     * <p>
     * The returned table does not know its field references, i.e.
     * {@link Table#fields()} returns an empty array.
     */
    @Support
    public static Table<Record> table(Name name) {
        return new TableImpl<>(name);
    }

    /**
     * Create a qualified table, given its table name.
     * <p>
     * This constructs a table reference given the table's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This table...
     * tableByName("MY_SCHEMA", "MY_TABLE");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE]
     * </pre></code>
     * <p>
     * The returned table does not know its field references, i.e.
     * {@link Table#fields()} returns an empty array.
     */
    @Support
    public static Table<Record> table(Name name, Comment comment) {
        return new TableImpl<>(name, null, null, null, comment);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * fieldByName("MY_SCHEMA", "MY_TABLE", "MY_FIELD");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
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
     * @deprecated - [#3843] - 3.6.0 - use {@link #field(Name)} instead
     */
    @Deprecated
    @Support
    public static Field<Object> fieldByName(String... qualifiedName) {
        return fieldByName(Object.class, qualifiedName);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * fieldByName("MY_SCHEMA", "MY_TABLE", "MY_FIELD");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
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
     * @deprecated - [#3843] - 3.6.0 - use {@link #field(Name, Class)} instead
     */
    @Deprecated
    @Support
    public static <T> Field<T> fieldByName(Class<T> type, String... qualifiedName) {
        return fieldByName(getDataType(type), qualifiedName);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * fieldByName("MY_SCHEMA", "MY_TABLE", "MY_FIELD");
     *
     * // ... will render this SQL by default, using the SQL Server dialect
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
     * @deprecated - [#3843] - 3.6.0 - use {@link #field(Name, DataType)} instead
     */
    @Deprecated
    @Support
    public static <T> Field<T> fieldByName(DataType<T> type, String... qualifiedName) {
        return field(name(qualifiedName), type);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * field(name("MY_SCHEMA", "MY_TABLE", "MY_FIELD"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, field(name("TITLE"))))
     *       .from(table(name("T_BOOK")))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     */
    @Support
    public static Field<Object> field(Name name) {
        return field(name, Object.class);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * field(name("MY_SCHEMA", "MY_TABLE", "MY_FIELD"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, field(name("TITLE"))))
     *       .from(table(name("T_BOOK")))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     */
    @Support
    public static <T> Field<T> field(Name name, Class<T> type) {
        return field(name, getDataType(type));
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * field(name("MY_SCHEMA", "MY_TABLE", "MY_FIELD"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, field(name("TITLE"))))
     *       .from(table(name("T_BOOK")))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     */
    @Support
    public static <T> Field<T> field(Name name, DataType<T> type) {
        return new QualifiedField<>(name, type);
    }

    /**
     * Create a qualified field, given its (qualified) field name.
     * <p>
     * This constructs a field reference given the field's qualified name. jOOQ
     * <p>
     * Example: <code><pre>
     * // This field...
     * field(name("MY_SCHEMA", "MY_TABLE", "MY_FIELD"));
     *
     * // ... will render this SQL by default, using the SQL Server dialect
     * [MY_SCHEMA].[MY_TABLE].[MY_FIELD]
     * </pre></code>
     * <p>
     * Another example: <code><pre>
     * create.select(field("length({1})", Integer.class, field(name("TITLE"))))
     *       .from(table(name("T_BOOK")))
     *       .fetch();
     *
     * // ... will execute this SQL on SQL Server:
     * select length([TITLE]) from [T_BOOK]
     * </pre></code>
     */
    @Support
    public static <T> Field<T> field(Name name, DataType<T> type, Comment comment) {
        return new QualifiedField<>(name, type, comment);
    }

    /**
     * Create a qualified index reference by name.
     */
    @Support
    public static Index index(Name name) {
        return new IndexImpl(name);
    }



























































    // -------------------------------------------------------------------------
    // XXX: Queries
    // -------------------------------------------------------------------------

    /**
     * Wrap a collection of queries.
     *
     * @see DSLContext#queries(Query...)
     */
    @Support
    public static Queries queries(Query... queries) {
        return queries(Arrays.asList(queries));
    }

    /**
     * Wrap a collection of queries.
     *
     * @see DSLContext#queries(Collection)
     */
    @Support
    public static Queries queries(Collection<? extends Query> queries) {
        return DSL.using(new DefaultConfiguration()).queries(queries);
    }

    /**
     * Wrap a collection of statements in an anonymous procedural block.
     *
     * @see DSLContext#begin(Statement...)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static Block begin(Statement... statements) {
        return begin(Arrays.asList(statements));
    }

    /**
     * Wrap a collection of statements in an anonymous procedural block.
     *
     * @see DSLContext#begin(Collection)
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES })
    public static Block begin(Collection<? extends Statement> statements) {
        return DSL.using(new DefaultConfiguration()).begin(statements);
    }


































































































































































































































































































































































































































































































    // -------------------------------------------------------------------------
    // XXX Plain SQL object factory
    // -------------------------------------------------------------------------

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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static SQL sql(String sql) {
        return sql(sql, new Object[0]);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions.
     * <p>
     * A plain SQL <code>QueryPart</code> is a <code>QueryPart</code> that can
     * contain user-defined plain SQL, because sometimes it is easier to express
     * things directly in SQL.
     * <p>
     * This overload takes a set of {@link QueryPart} arguments which are
     * replaced into the SQL string template at the appropriate index. Example:
     * <p>
     * <code><pre>
     * // Argument QueryParts are replaced into the SQL string at the appropriate index
     * sql("select {0}, {1} from {2}", TABLE.COL1, TABLE.COL2, TABLE);
     *
     * // Bind variables are supported as well, for backwards compatibility
     * sql("select col1, col2 from table where col1 = ?", val(1));
     * </pre></code>
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static SQL sql(String sql, QueryPart... parts) {
        return sql(sql, (Object[]) parts);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions.
     * <p>
     * A plain SQL <code>QueryPart</code> is a <code>QueryPart</code> that can
     * contain user-defined plain SQL, because sometimes it is easier to express
     * things directly in SQL. There must be as many bind variables contained
     * in the SQL, as passed in the bindings parameter
     * <p>
     * This overload takes a set of bind value arguments which are replaced our
     * bound into the SQL string template at the appropriate index. Example:
     * <p>
     * <code><pre>
     * sql("select col1, col2 from table where col1 = ?", 1);
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query part wrapping the plain SQL
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static SQL sql(String sql, Object... bindings) {
        return new SQLImpl(sql, bindings);
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
     * @deprecated - 3.6.0 - [#3854] - Use {@link #sql(String)} instead
     * @see SQL
     */
    @Deprecated
    @Support
    @PlainSQL
    public static QueryPart queryPart(String sql) {
        return sql(sql);
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
     * @deprecated - 3.6.0 - [#3854] - Use {@link #sql(String, QueryPart...)} instead
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Deprecated
    @Support
    @PlainSQL
    public static QueryPart queryPart(String sql, QueryPart... parts) {
        return sql(sql, parts);
    }

    /**
     * A custom SQL clause that can render arbitrary expressions.
     * <p>
     * A plain SQL <code>QueryPart</code> is a <code>QueryPart</code> that can
     * contain user-defined plain SQL, because sometimes it is easier to express
     * things directly in SQL. There must be as many bind variables contained
     * in the SQL, as passed in the bindings parameter
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query part wrapping the plain SQL
     * @deprecated - 3.6.0 - [#3854] - Use {@link #sql(String, Object...)} instead
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Deprecated
    @Support
    @PlainSQL
    public static QueryPart queryPart(String sql, Object... bindings) {
        return sql(sql, bindings);
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static RowCountQuery query(SQL sql) {
        return dsl().query(sql);
    }

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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static RowCountQuery query(String sql) {
        return dsl().query(sql);
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
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static RowCountQuery query(String sql, Object... bindings) {
        return dsl().query(sql, bindings);
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
     * // Will render this SQL by default, using Oracle SQL dialect
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static RowCountQuery query(String sql, QueryPart... parts) {
        return dsl().query(sql, parts);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * There must not be any bind variables contained in the SQL
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
     * String sql = "FETCH ALL IN \"&lt;unnamed cursor 1&gt;\"";</pre></code> Example
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static ResultQuery<Record> resultQuery(SQL sql) {
        return dsl().resultQuery(sql);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * There must not be any bind variables contained in the SQL
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
     * String sql = "FETCH ALL IN \"&lt;unnamed cursor 1&gt;\"";</pre></code> Example
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static ResultQuery<Record> resultQuery(String sql) {
        return dsl().resultQuery(sql);
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
     * String sql = "FETCH ALL IN \"&lt;unnamed cursor 1&gt;\"";</pre></code> Example
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
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static ResultQuery<Record> resultQuery(String sql, Object... bindings) {
        return dsl().resultQuery(sql, bindings);
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
     * // Will render this SQL by default, using Oracle SQL dialect
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static ResultQuery<Record> resultQuery(String sql, QueryPart... parts) {
        return dsl().resultQuery(sql, parts);
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
     * The returned table does not know its field references, i.e.
     * {@link Table#fields()} returns an empty array.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     * @see SQL
     */
    @Support
    @PlainSQL
    public static Table<Record> table(SQL sql) {
        return new SQLTable(sql);
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
     * The returned table does not know its field references, i.e.
     * {@link Table#fields()} returns an empty array.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     * @see SQL
     */
    @Support
    @PlainSQL
    public static Table<Record> table(String sql) {
        return table(sql, new Object[0]);
    }

    /**
     * A custom SQL clause that can render arbitrary table expressions.
     * <p>
     * A plain SQL table is a table that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex, but static subqueries or tables from different schemas.
     * There must be as many bind variables contained in the SQL, as passed
     * in the bindings parameter
     * <p>
     * Example
     * <p>
     * <code><pre>
     * String sql = "SELECT * FROM USER_TABLES WHERE OWNER = ?";
     * Object[] bindings = new Object[] { "MY_SCHEMA" };
     * </pre></code>
     * <p>
     * The returned table does not know its field references, i.e.
     * {@link Table#fields()} returns an empty array.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static Table<Record> table(String sql, Object... bindings) {
        return table(sql(sql, bindings));
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
     * The returned table does not know its field references, i.e.
     * {@link Table#fields()} returns an empty array.
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static Table<Record> table(String sql, QueryPart... parts) {
        return table(sql, (Object[]) parts);
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
     * @see SQL
     * @deprecated - 3.10 - [#6162] - Use {@link #sequence(Name)} instead.
     */
    @Deprecated
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    @PlainSQL
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
     * @see SQL
     * @deprecated - 3.10 - [#6162] - Use {@link #sequence(Name, Class)} instead.
     */
    @Deprecated
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    @PlainSQL
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
     * @see SQL
     * @deprecated - 3.10 - [#6162] - Use {@link #sequence(Name, DataType)}
     *             instead.
     */
    @Deprecated
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    @PlainSQL
    public static <T extends Number> Sequence<T> sequence(String sql, DataType<T> type) {
        return new SequenceImpl<>(sql, null, type, true);
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static Field<Object> field(SQL sql) {
        return field(sql, Object.class);
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static Field<Object> field(String sql) {
        return field(sql, new Object[0]);
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
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static Field<Object> field(String sql, Object... bindings) {
        return field(sql, Object.class, bindings);
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(SQL sql, Class<T> type) {
        return field(sql, getDataType(type));
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(String sql, Class<T> type) {
        return field(sql, type, new Object[0]);
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
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(String sql, Class<T> type, Object... bindings) {
        return field(sql, getDataType(type), bindings);
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(SQL sql, DataType<T> type) {
        return new SQLField(type, sql);
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(String sql, DataType<T> type) {
        return field(sql, type, new Object[0]);
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
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(String sql, DataType<T> type, Object... bindings) {
        return field(sql(sql, bindings), type);
    }

    /**
     * Create a "plain SQL" field.
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
     * field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC SEPARATOR '-')", expr1, expr2);
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link #name(String...)} and similar methods
     *
     * @param sql The SQL
     * @param type The field type
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A field wrapping the plain SQL
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(String sql, DataType<T> type, QueryPart... parts) {
        return field(sql(sql, parts), type);
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
     * field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC SEPARATOR '-')", expr1, expr2);
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static Field<Object> field(String sql, QueryPart... parts) {
        return field(sql, (Object[]) parts);
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
     * field("GROUP_CONCAT(DISTINCT {0} ORDER BY {1} ASC SEPARATOR '-')", expr1, expr2);
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static <T> Field<T> field(String sql, Class<T> type, QueryPart... parts) {
        return field(sql, getDataType(type), (Object[]) parts);
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
     * @see SQL
     */
    @Support
    @PlainSQL
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static <T> Field<T> function(String name, DataType<T> type, Field<?>... arguments) {
        return new org.jooq.impl.Function<>(name, type, nullSafe(arguments));
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
        return new org.jooq.impl.Function<>(name, type, nullSafe(arguments));
    }

    /**
     * Create a new condition holding plain SQL.
     * <p>
     * There must not be any bind variables contained in the SQL.
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static Condition condition(SQL sql) {
        return new SQLCondition(sql);
    }

    /**
     * Create a new condition holding plain SQL.
     * <p>
     * There must not be any bind variables contained in the SQL.
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
     * @see SQL
     */
    @Support
    @PlainSQL
    public static Condition condition(String sql) {
        return condition(sql, new Object[0]);
    }

    /**
     * Create a new condition holding plain SQL.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed
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
     * @see SQL
     * @see DSL#sql(String, Object...)
     */
    @Support
    @PlainSQL
    public static Condition condition(String sql, Object... bindings) {
        return condition(sql(sql, bindings));
    }

    /**
     * A custom SQL clause that can render arbitrary SQL elements.
     * <p>
     * This is useful for constructing more complex SQL syntax elements wherever
     * <code>Condition</code> types are expected. An example for this are
     * Postgres's various operators, some of which are missing in the jOOQ API.
     * For instance, the "overlap" operator for arrays:
     * <code><pre>ARRAY[1,4,3] &amp;&amp; ARRAY[2,1]</pre></code>
     * <p>
     * The above Postgres operator can be expressed as such: <code><pre>
     * condition("{0} &amp;&amp; {1}", array1, array2);
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
     * @see SQL
     * @see DSL#sql(String, QueryPart...)
     */
    @Support
    @PlainSQL
    public static Condition condition(String sql, QueryPart... parts) {
        return condition(sql, (Object[]) parts);
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
        return condition(Tools.field(value));
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
        return field instanceof ConditionAsField
            ? ((ConditionAsField) field).condition
            : new FieldCondition(field);
    }

    /**
     * Create a condition from a map.
     * <p>
     * The result is a condition generated from keys and values of the argument <code>map</code>, such that:
     *
     * <code><pre>
     * key1 = value1 AND key2 = value2 AND ... AND keyN = valueN
     * </pre></code>
     *
     * @param map A map containing keys and values to form predicates.
     * @return A condition comparing keys with values.
     */
    @Support
    public static Condition condition(Map<Field<?>, ?> map) {
        return new MapCondition(map);
    }

    /**
     * Create a "Query By Example" (QBE) {@link Condition} from a {@link Record}
     * .
     * <p>
     * This will take all the non-null values in the argument
     * <code>record</code> to form a predicate from them. If all values in the
     * <code>record</code> are <code>null</code>, the predicate will be the
     * {@link #trueCondition()}.
     *
     * @param record The record from which to create a condition.
     * @return The condition.
     * @see <a href="https://en.wikipedia.org/wiki/Query_by_Example">https://en.
     *      wikipedia.org/wiki/Query_by_Example</a>
     */
    @Support
    public static Condition condition(Record record) {
        return new RecordCondition(record);
    }

    // -------------------------------------------------------------------------
    // XXX Global Condition factory
    // -------------------------------------------------------------------------

    /**
     * Return a <code>Condition</code> that behaves like no condition being
     * present.
     * <p>
     * This is useful as an "identity" condition for reduction operations, for
     * both <code>AND</code> and <code>OR</code> reductions, e.g.
     * <p>
     * <code><pre>
     * Condition combined =
     * Stream.of(cond1, cond2, cond3)
     *       .reduce(noCondition(), Condition::and);
     * </pre></code>
     * <p>
     * When this condition is passed to SQL clauses, such as the
     * <code>WHERE</code> clause, the entire clause is omitted:
     * <p>
     * <code><pre>
     * selectFrom(T).where(noCondition())
     * </pre></code>
     * <p>
     * ... will produce
     * <p>
     * <code><pre>
     * SELECT * FROM t
     * </pre></code>
     */
    @Support
    public static Condition noCondition() {
        return NoCondition.INSTANCE;
    }

    /**
     * Return a <code>Condition</code> that will always evaluate to true.
     */
    @Support
    public static True trueCondition() {
        return TrueCondition.INSTANCE;
    }

    /**
     * Return a <code>Condition</code> that will always evaluate to false.
     */
    @Support
    public static False falseCondition() {
        return FalseCondition.INSTANCE;
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with {@link Operator#AND}.
     */
    @Support
    public static Condition and(Condition left, Condition right) {
        return condition(AND, left, right);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with {@link Operator#AND}.
     */
    @Support
    public static Condition and(Condition... conditions) {
        return condition(AND, conditions);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with {@link Operator#AND}.
     */
    @Support
    public static Condition and(Collection<? extends Condition> conditions) {
        return condition(AND, conditions);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with {@link Operator#OR}.
     */
    @Support
    public static Condition or(Condition left, Condition right) {
        return condition(OR, left, right);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with {@link Operator#OR}.
     */
    @Support
    public static Condition or(Condition... conditions) {
        return condition(OR, conditions);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with {@link Operator#OR}.
     */
    @Support
    public static Condition or(Collection<? extends Condition> conditions) {
        return condition(OR, conditions);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with <code>Operator</code>.
     */
    @Support
    public static Condition condition(Operator operator, Condition left, Condition right) {
        return CombinedCondition.of(operator, left, right);
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with <code>Operator</code>.
     */
    @Support
    public static Condition condition(Operator operator, Condition... conditions) {
        return condition(operator, asList(conditions));
    }

    /**
     * Return a <code>Condition</code> that connects all argument
     * <code>conditions</code> with <code>Operator</code>.
     */
    @Support
    public static Condition condition(Operator operator, Collection<? extends Condition> conditions) {
        return CombinedCondition.of(operator, conditions);
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
     * Create a unique condition.
     * <p>
     * <code>UNIQUE ([query])</code>
     */
    @Support
    public static Condition unique(Select<?> query) {
        return new UniqueCondition(query, true);
    }

    /**
     * Create a not unique condition.
     * <p>
     * <code>NOT UNIQUE ([query])</code>
     */
    @Support
    public static Condition notUnique(Select<?> query) {
        return new UniqueCondition(query, false);
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
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #not(Condition)} instead. Due
     *             to ambiguity between calling this method using
     *             {@link Field#equals(Object)} argument, vs. calling the other
     *             method via a {@link Field#equal(Object)} argument, this
     *             method will be removed in the future.
     */
    @Deprecated
    @Support
    public static Field<Boolean> not(Boolean value) {
        return not(Tools.field(value));
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
        return condition instanceof FieldCondition
            ? ((FieldCondition) condition).field
            : new ConditionAsField(condition);
    }

    // -------------------------------------------------------------------------
    // XXX Global Field and Function factory
    // -------------------------------------------------------------------------

    /**
     * Wrap a {@link SelectField} in a general-purpose {@link Field}
     */
    @Support
    public static <T> Field<T> field(SelectField<T> field) {
        return field instanceof Field ? (Field<T>) field : field("{0}", field.getDataType(), field);
    }



    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>1</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1> Field<Record1<T1>> rowField(Row1<T1> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>2</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2> Field<Record2<T1, T2>> rowField(Row2<T1, T2> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>3</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3> Field<Record3<T1, T2, T3>> rowField(Row3<T1, T2, T3> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>4</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4> Field<Record4<T1, T2, T3, T4>> rowField(Row4<T1, T2, T3, T4> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>5</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5> Field<Record5<T1, T2, T3, T4, T5>> rowField(Row5<T1, T2, T3, T4, T5> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>6</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6> Field<Record6<T1, T2, T3, T4, T5, T6>> rowField(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>7</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> Field<Record7<T1, T2, T3, T4, T5, T6, T7>> rowField(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>8</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Field<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> rowField(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>9</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Field<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> rowField(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>10</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Field<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> rowField(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>11</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Field<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> rowField(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>12</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Field<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> rowField(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>13</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Field<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> rowField(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>14</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Field<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> rowField(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>15</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Field<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> rowField(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>16</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Field<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> rowField(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>17</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Field<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> rowField(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>18</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Field<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> rowField(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>19</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Field<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> rowField(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>20</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Field<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> rowField(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>21</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Field<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> rowField(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowField<>(row);
    }

    /**
     * EXPERIMENTAL: Turn a row value expression of degree <code>22</code> into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Field<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> rowField(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowField<>(row);
    }



    /**
     * EXPERIMENTAL: Turn a row value expression of arbitrary degree into a {@code Field}.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static Field<Record> rowField(RowN row) {
        return new RowField<>(row);
    }

    /**
     * Transform a subquery into a correlated subquery.
     */
    @Support
    public static <T> Field<T> field(Select<? extends Record1<T>> select) {
        if (select == null)
            return (Field) NULL();
        else
            return select.<T>asField();
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * Choose is used as a method name to avoid name clashes with Java's
     * reserved literal "case"
     *
     * @see Case
     */
    @Support
    public static Case choose() {
        return decode();
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE value WHEN 1 THEN 'one'
     *            WHEN 2 THEN 'two'
     *            ELSE        'three'
     * END
     * </pre></code>
     * <p>
     * Choose is used as a method name to avoid name clashes with Java's
     * reserved literal "case".
     *
     * @see Case
     */
    @Support
    public static <V> CaseValueStep<V> choose(V value) {
        return decode().value(value);
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE value WHEN 1 THEN 'one'
     *            WHEN 2 THEN 'two'
     *            ELSE        'three'
     * END
     * </pre></code>
     * <p>
     * Choose is used as a method name to avoid name clashes with Java's
     * reserved literal "case".
     *
     * @see Case
     */
    @Support
    public static <V> CaseValueStep<V> choose(Field<V> value) {
        return decode().value(value);
    }

    /**
     * The T-SQL <code>CHOOSE()</code> function.
     */
    @Support
    public static <T> Field<T> choose(int index, T... values) {
        return choose(val(index), (Field<T>[]) Tools.fields(values).toArray(EMPTY_FIELD));
    }

    /**
     * The T-SQL <code>CHOOSE()</code> function.
     */
    @Support
    @SafeVarargs
    public static <T> Field<T> choose(int index, Field<T>... values) {
        return choose(val(index), values);
    }

    /**
     * The T-SQL <code>CHOOSE()</code> function.
     */
    @Support
    public static <T> Field<T> choose(Field<Integer> index, T... values) {
        return choose(index, (Field<T>[]) Tools.fields(values).toArray(EMPTY_FIELD));
    }

    /**
     * The T-SQL <code>CHOOSE()</code> function.
     */
    @Support
    @SafeVarargs
    public static <T> Field<T> choose(Field<Integer> index, Field<T>... values) {
        return new Choose<>(index, values);
    }

    /**
     * Initialise a {@link Case} statement.
     *
     * @see Case
     */
    @Support
    public static Case case_() {
        return decode();
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE value WHEN 1 THEN 'one'
     *            WHEN 2 THEN 'two'
     *            ELSE        'three'
     * END
     * </pre></code>
     *
     * @see Case
     */
    @Support
    public static <V> CaseValueStep<V> case_(V value) {
        return decode().value(value);
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE value WHEN 1 THEN 'one'
     *            WHEN 2 THEN 'two'
     *            ELSE        'three'
     * END
     * </pre></code>
     *
     * @see Case
     */
    @Support
    public static <V> CaseValueStep<V> case_(Field<V> value) {
        return decode().value(value);
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE WHEN x &lt; 1  THEN 'one'
     *      WHEN x &gt;= 2 THEN 'two'
     *      ELSE            'three'
     * END
     * </pre></code>
     */
    @Support
    public static <T> CaseConditionStep<T> when(Condition condition, T result) {
        return decode().when(condition, result);
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE WHEN x &lt; 1  THEN 'one'
     *      WHEN x &gt;= 2 THEN 'two'
     *      ELSE            'three'
     * END
     * </pre></code>
     */
    @Support
    public static <T> CaseConditionStep<T> when(Condition condition, Field<T> result) {
        return decode().when(condition, result);
    }

    /**
     * Initialise a {@link Case} statement.
     * <p>
     * This API can be used to create expressions of the type <code><pre>
     * CASE WHEN x &lt; 1  THEN 'one'
     *      WHEN x &gt;= 2 THEN 'two'
     *      ELSE            'three'
     * END
     * </pre></code>
     */
    @Support
    public static <T> CaseConditionStep<T> when(Condition condition, Select<? extends Record1<T>> result) {
        return decode().when(condition, result);
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
        return decode(Tools.field(value), Tools.field(search), Tools.field(result), Tools.fields(more).toArray(EMPTY_FIELD));
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
        return decode(nullSafe(value), nullSafe(search), nullSafe(result), EMPTY_FIELD);
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
        return new Decode<>(nullSafe(value), nullSafe(search), nullSafe(result), nullSafe(more));
    }

    /**
     * Coerce this field to the type of another field.
     *
     * @see #coerce(Field, Field)
     */
    @Support
    public static <T> Field<T> coerce(Object value, Field<T> as) {
        return Tools.field(value).coerce(as);
    }

    /**
     * Coerce this field to another type.
     *
     * @see #coerce(Field, Class)
     */
    @Support
    public static <T> Field<T> coerce(Object value, Class<T> as) {
        return Tools.field(value).coerce(as);
    }

    /**
     * Coerce a field to another type.
     *
     * @see #coerce(Field, DataType)
     */
    @Support
    public static <T> Field<T> coerce(Object value, DataType<T> as) {
        return Tools.field(value).coerce(as);
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
        return Tools.field(value, as).cast(as);
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
        return Tools.field(value, type).cast(type);
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
        return Tools.field(value).cast(type);
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
     * The <code>COALESCE(value1, value2, ... , value n)</code> function.
     *
     * @see #coalesce(Field, Field...)
     */
    @Support
    public static <T> Field<T> coalesce(T value, T... values) {
        return coalesce0(Tools.field(value), Tools.fields(values).toArray(EMPTY_FIELD));
    }

    /**
     * The <code>COALESCE(field, value)</code> function.
     *
     * @see #coalesce(Field, Field...)
     */
    @Support
    public static <T> Field<T> coalesce(Field<T> field, T value) {
        return coalesce0(field, Tools.field(value, field));
    }

    /**
     * The <code>COALESCE(field1, field2, ... , field n)</code> function.
     */
    @Support
    public static <T> Field<T> coalesce(Field<T> field, Field<?>... fields) {
        return coalesce0(field, fields);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T> coalesce0(Field<T> field, Field<?>... fields) {
        return new Coalesce<>(nullSafeDataType(field), nullSafe(combine(field, fields)));
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
        return nvl0(Tools.field(value), Tools.field(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> nvl(T value, Field<T> defaultValue) {
        return nvl0(Tools.field(value, defaultValue), nullSafe(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> nvl(Field<T> value, T defaultValue) {
        return nvl0(nullSafe(value), Tools.field(defaultValue, value));
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
        return nvl0(value, defaultValue);
    }

    /**
     * The <code>IFNULL()</code> function, a synonym of <code>NVL()</code>.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> ifnull(T value, T defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * The <code>IFNULL()</code> function, a synonym of <code>NVL()</code>.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> ifnull(T value, Field<T> defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * The <code>IFNULL()</code> function, a synonym of <code>NVL()</code>.
     *
     * @see #nvl(Field, Object)
     */
    @Support
    public static <T> Field<T> ifnull(Field<T> value, T defaultValue) {
        return nvl(value, defaultValue);
    }

    /**
     * The <code>IFNULL()</code> function, a synonym of <code>NVL()</code>.
     *
     * @see #nvl(Field, Field)
     */
    @Support
    public static <T> Field<T> ifnull(Field<T> value, Field<T> defaultValue) {
        return nvl(value, defaultValue);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T> nvl0(Field<T> value, Field<T> defaultValue) {
        return new Nvl<>(nullSafe(value), nullSafe(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     *
     * @see #nvl2(Field, Field, Field)
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Z valueIfNotNull, Z valueIfNull) {
        return nvl20(nullSafe(value), Tools.field(valueIfNotNull), Tools.field(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     *
     * @see #nvl2(Field, Field, Field)
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Z valueIfNotNull, Field<Z> valueIfNull) {
        return nvl20(nullSafe(value), Tools.field(valueIfNotNull, valueIfNull), nullSafe(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function.
     *
     * @see #nvl2(Field, Field, Field)
     */
    @Support
    public static <Z> Field<Z> nvl2(Field<?> value, Field<Z> valueIfNotNull, Z valueIfNull) {
        return nvl20(nullSafe(value), nullSafe(valueIfNotNull), Tools.field(valueIfNull, valueIfNotNull));
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
        return nvl20(value, valueIfNotNull, valueIfNull);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <Z> Field<Z> nvl20(Field<?> value, Field<Z> valueIfNotNull, Field<Z> valueIfNull) {
        return new Nvl2<>(nullSafe(value), nullSafe(valueIfNotNull), nullSafe(valueIfNull));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     *
     * @see #nullif(Field, Field)
     */
    @Support
    public static <T> Field<T> nullif(T value, T other) {
        return nullif0(Tools.field(value), Tools.field(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     *
     * @see #nullif(Field, Field)
     */
    @Support
    public static <T> Field<T> nullif(T value, Field<T> other) {
        return nullif0(Tools.field(value, other), nullSafe(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function.
     *
     * @see #nullif(Field, Field)
     */
    @Support
    public static <T> Field<T> nullif(Field<T> value, T other) {
        return nullif0(nullSafe(value), Tools.field(other, value));
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
        return nullif0(value, other);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T> nullif0(Field<T> value, Field<T> other) {
        return new NullIf<>(nullSafe(value), nullSafe(other));
    }

    /**
     * Gets the SQL Server style IIF(condition, ifTrue, ifFalse) function.
     *
     * @see #iif(Condition, Field, Field)
     */
    @Support
    public static <T> Field<T> iif(Condition condition, T ifTrue, T ifFalse) {
        return iif0(condition, Tools.field(ifTrue), Tools.field(ifFalse));
    }

    /**
     * Gets the SQL Server style IIF(condition, ifTrue, ifFalse) function.
     *
     * @see #iif(Condition, Field, Field)
     */
    @Support
    public static <T> Field<T> iif(Condition condition, T ifTrue, Field<T> ifFalse) {
        return iif0(condition, Tools.field(ifTrue, ifFalse), nullSafe(ifFalse));
    }

    /**
     * Gets the SQL Server style IIF(condition, ifTrue, ifFalse) function.
     *
     * @see #iif(Condition, Field, Field)
     */
    @Support
    public static <T> Field<T> iif(Condition condition, Field<T> ifTrue, T ifFalse) {
        return iif0(condition, nullSafe(ifTrue), Tools.field(ifFalse, ifTrue));
    }

    /**
     * Gets the SQL Server style IIF(condition, ifTrue, ifFalse) function.
     */
    @Support
    public static <T> Field<T> iif(Condition condition, Field<T> ifTrue, Field<T> ifFalse) {
        return iif0(condition, ifTrue, ifFalse);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T> iif0(Condition condition, Field<T> ifTrue, Field<T> ifFalse) {
        return new Iif<>(condition, nullSafe(ifTrue), nullSafe(ifFalse));
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
        return upper(Tools.field(value));
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
        return lower(Tools.field(value));
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
        return trim(Tools.field(value));
    }

    /**
     * Get the trim(field) function.
     * <p>
     * This renders the trim function where available:
     * <code><pre>trim([field])</pre></code> ... or emulates it elsewhere using
     * rtrim and ltrim: <code><pre>ltrim(rtrim([field]))</pre></code>
     */
    @Support
    public static Field<String> trim(Field<String> field) {
        return new Trim(nullSafe(field));
    }

    /**
     * Get the <code>trim(field, characters)</code> or
     * <code>trim(both characters from field)</code> function.
     *
     * @see #trim(Field, Field)
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> trim(String value, String characters) {
        return trim(Tools.field(value), Tools.field(characters));
    }

    /**
     * Get the <code>trim(field, characters)</code> or
     * <code>trim(both characters from field)</code> function.
     * <p>
     * This renders the trim function where available:
     * <code><pre>trim([field])</pre></code> ... or emulates it elsewhere using
     * rtrim and ltrim: <code><pre>ltrim(rtrim([field]))</pre></code>
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> trim(Field<String> field, Field<String> characters) {
        return new Trim(nullSafe(field), nullSafe(characters));
    }

    /**
     * Get the rtrim(field) function.
     *
     * @see #rtrim(Field)
     */
    @Support
    public static Field<String> rtrim(String value) {
        return rtrim(Tools.field(value));
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
     * Get the <code>rtrim(field, characters)</code> or
     * <code>trim(trailing characters from field)</code> function.
     *
     * @see #rtrim(Field, Field)
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rtrim(String value, String characters) {
        return rtrim(Tools.field(value), Tools.field(characters));
    }

    /**
     * Get the <code>rtrim(field, characters)</code> or
     * <code>trim(trailing characters from field)</code> function.
     * <p>
     * This renders the rtrim function in all dialects:
     * <code><pre>rtrim([field])</pre></code>
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rtrim(Field<String> field, Field<String> characters) {
        return new RTrim(nullSafe(field), nullSafe(characters));
    }

    /**
     * Get the ltrim(field) function.
     *
     * @see #ltrim(Field)
     */
    @Support
    public static Field<String> ltrim(String value) {
        return ltrim(Tools.field(value));
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
     * Get the <code>ltrim(field, characters)</code> or
     * <code>trim(leading characters from field)</code> function.
     *
     * @see #ltrim(Field, Field)
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> ltrim(String value, String characters) {
        return ltrim(Tools.field(value), Tools.field(characters));
    }

    /**
     * Get the <code>ltrim(field, characters)</code> or
     * <code>trim(leading characters from field)</code> function.
     * <p>
     * This renders the ltrim function in all dialects:
     * <code><pre>ltrim([field])</pre></code>
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> ltrim(Field<String> field, Field<String> characters) {
        return new LTrim(nullSafe(field), nullSafe(characters));
    }

    /**
     * Get the rpad(field, length) function.
     *
     * @see #rpad(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> rpad(Field<String> field, int length) {
        return rpad(nullSafe(field), Tools.field(length));
    }

    /**
     * Get the rpad(field, length) function.
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([field], [length])</pre></code> ... or emulates it
     * elsewhere using concat, repeat, and length, which may be emulated as
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
        return rpad(nullSafe(field), Tools.field(length), Tools.field(character));
    }

    /**
     * Get the rpad(field, length, character) function.
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([field], [length])</pre></code> ... or emulates it
     * elsewhere using concat, repeat, and length, which may be emulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([field], repeat([character], [length] - length([field])))</pre></code>
     * <p>
     * In {@link SQLDialect#SQLITE}, this is emulated as such:
     * <code><pre>[field] || replace(replace(substr(quote(zeroblob(([length] + 1) / 2)), 3, ([length] - length([field]))), '\''', ''), '0', [character])</pre></code>
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
        return lpad(nullSafe(field), Tools.field(length));
    }

    /**
     * Get the lpad(field, length) function.
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([field], [length])</pre></code> ... or emulates it
     * elsewhere using concat, repeat, and length, which may be emulated as
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
        return lpad(nullSafe(field), Tools.field(length), Tools.field(character));
    }

    /**
     * Get the lpad(field, length, character) function.
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([field], [length])</pre></code> ... or emulates it
     * elsewhere using concat, repeat, and length, which may be emulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat([character], [length] - length([field])), [field])</pre></code>
     * <p>
     * In {@link SQLDialect#SQLITE}, this is emulated as such:
     * <code><pre>replace(replace(substr(quote(zeroblob(([length] + 1) / 2)), 3, ([length] - length([field]))), '\''', ''), '0', [character]) || [field]</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> lpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        return new Lpad(nullSafe(field), nullSafe(length), nullSafe(character));
    }

    /**
     * Get the translate(field, from, to) function.
     *
     * @see #translate(Field, Field, Field)
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<String> translate(Field<String> text, String from, String to) {
        return translate(text, Tools.field(from), Tools.field(to));
    }

    /**
     * Get the translate(field, from, to) function.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<String> translate(Field<String> text, Field<String> from, Field<String> to) {
        return new Translate(text, from, to);
    }

    /**
     * Get the repeat(field, count) function.
     *
     * @see #repeat(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(String field, int count) {
        return repeat(Tools.field(field), Tools.field(count));
    }

    /**
     * Get the repeat(field, count) function.
     *
     * @see #repeat(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(String field, Field<? extends Number> count) {
        return repeat(Tools.field(field), nullSafe(count));
    }

    /**
     * Get the repeat(count) function.
     *
     * @see #repeat(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> repeat(Field<String> field, int count) {
        return repeat(nullSafe(field), Tools.field(count));
    }

    /**
     * Get the repeat(field, count) function.
     * <p>
     * This renders the repeat or replicate function where available:
     * <code><pre>repeat([field], [count]) or
     * replicate([field], [count])</pre></code> ... or emulates it elsewhere
     * using rpad and length, which may be emulated as well, depending on the
     * RDBMS:
     * <code><pre>rpad([field], length([field]) * [count], [field])</pre></code>
     * <p>
     * In {@link SQLDialect#SQLITE}, this is emulated as such:
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
    @Support
    public static String escape(String value, char escape) {
        String esc = "" + escape;
        return StringUtils.replace(
                   StringUtils.replace(
                       StringUtils.replace(value, esc, esc + esc), "%", esc + "%"
                   ), "_", esc + "_"
               );
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
    @Support
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
        return replace(nullSafe(field), Tools.field(search));
    }

    /**
     * Get the replace(field, search) function.
     * <p>
     * This renders the replace or str_replace function where available:
     * <code><pre>replace([field], [search]) or
     * str_replace([field], [search])</pre></code> ... or emulates it elsewhere
     * using the three-argument replace function:
     * <code><pre>replace([field], [search], '')</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> replace(Field<String> field, Field<String> search) {
        return new Replace(nullSafe(field), nullSafe(search), null);
    }

    /**
     * Get the replace(field, search, replace) function.
     *
     * @see #replace(Field, Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<String> replace(Field<String> field, String search, String replace) {
        return replace(nullSafe(field), Tools.field(search), Tools.field(replace));
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
    @Support
    public static Field<Integer> position(String in, String search) {
        return position(Tools.field(in), Tools.field(search));
    }

    /**
     * Get the position(in, search) function.
     *
     * @see #position(Field, Field)
     */
    @Support
    public static Field<Integer> position(String in, Field<String> search) {
        return position(Tools.field(in), nullSafe(search));
    }

    /**
     * Get the position(in, search) function.
     *
     * @see #position(Field, Field)
     */
    @Support
    public static Field<Integer> position(Field<String> in, String search) {
        return position(nullSafe(in), Tools.field(search));
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
    @Support
    public static Field<Integer> position(Field<String> in, Field<String> search) {
        return new Position(nullSafe(search), nullSafe(in));
    }

    /**
     * Get the position(in, search, startindex) function.
     *
     * @see #position(Field, Field, Field)
     */
    @Support
    public static Field<Integer> position(String in, String search, int startIndex) {
        return position(Tools.field(in), Tools.field(search), Tools.field(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     *
     * @see #position(Field, Field, Field)
     */
    @Support
    public static Field<Integer> position(String in, Field<String> search, int startIndex) {
        return position(Tools.field(in), nullSafe(search), Tools.field(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     *
     * @see #position(Field, Field)
     */
    @Support
    public static Field<Integer> position(Field<String> in, String search, int startIndex) {
        return position(nullSafe(in), Tools.field(search), Tools.field(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     * <p>
     * This renders the position or any equivalent function:
     * <code><pre>position([search] in [in]) or
     * locate([in], [search]) or
     * locate([search], [in]) or
     * instr([in], [search]) or
     * charindex([search], [in])</pre></code>
     */
    @Support
    public static Field<Integer> position(Field<String> in, Field<String> search, int startIndex) {
        return position(nullSafe(search), nullSafe(in), Tools.field(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     *
     * @see #position(Field, Field, Field)
     */
    @Support
    public static Field<Integer> position(String in, String search, Field<? extends Number> startIndex) {
        return position(Tools.field(in), Tools.field(search), nullSafe(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     *
     * @see #position(Field, Field, Field)
     */
    @Support
    public static Field<Integer> position(String in, Field<String> search, Field<? extends Number> startIndex) {
        return position(Tools.field(in), nullSafe(search), nullSafe(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     *
     * @see #position(Field, Field)
     */
    @Support
    public static Field<Integer> position(Field<String> in, String search, Field<? extends Number> startIndex) {
        return position(nullSafe(in), Tools.field(search), nullSafe(startIndex));
    }

    /**
     * Get the position(in, search, startindex) function.
     * <p>
     * This renders the position or any equivalent function:
     * <code><pre>position([search] in [in]) or
     * locate([in], [search]) or
     * locate([search], [in]) or
     * instr([in], [search]) or
     * charindex([search], [in])</pre></code>
     */
    @Support
    public static Field<Integer> position(Field<String> in, Field<String> search, Field<? extends Number> startIndex) {
        return new Position(nullSafe(search), nullSafe(in), nullSafe(startIndex));
    }

    /**
     * Get the overlay(in, placing, startIndex) function.
     */
    @Support
    public static Field<String> overlay(Field<String> in, String placing, Number startIndex) {
        return new Overlay(nullSafe(in), Tools.field(placing), Tools.field(startIndex));
    }

    /**
     * Get the overlay(in, placing, startIndex) function.
     */
    @Support
    public static Field<String> overlay(Field<String> in, Field<String> placing, Field<? extends Number> startIndex) {
        return new Overlay(nullSafe(in), nullSafe(placing), nullSafe(startIndex));
    }

    /**
     * Get the overlay(in, placing, startIndex, length) function.
     */
    @Support
    public static Field<String> overlay(Field<String> in, String placing, Number startIndex, Number length) {
        return new Overlay(nullSafe(in), Tools.field(placing), Tools.field(startIndex), Tools.field(length));
    }

    /**
     * Get the overlay(in, placing, startIndex, length) function.
     */
    @Support
    public static Field<String> overlay(Field<String> in, Field<String> placing, Field<? extends Number> startIndex, Field<? extends Number> length) {
        return new Overlay(nullSafe(in), nullSafe(placing), nullSafe(startIndex), nullSafe(length));
    }

    /**
     * Get the ascii(field) function.
     *
     * @see #ascii(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> ascii(String field) {
        return ascii(Tools.field(field));
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
        return concat(nullSafe(field), Tools.field(value));
    }

    /**
     * Get the <code>concat(value, field)</code> function.
     *
     * @see #concat(Field...)
     */
    @Support
    public static Field<String> concat(String value, Field<String> field) {
        return concat(Tools.field(value), nullSafe(field));
    }

    /**
     * Get the concat(value[, value, ...]) function.
     *
     * @see #concat(Field...)
     */
    @Support
    public static Field<String> concat(String... values) {
        return concat(Tools.fields(values).toArray(EMPTY_FIELD));
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
        return substring(nullSafe(field), Tools.field(startingPosition));
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
        return substring(nullSafe(field), Tools.field(startingPosition), Tools.field(length));
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
        return substring(nullSafe(field), Tools.field(startingPosition), Tools.field(length));
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
        return left(Tools.field(field), Tools.field(length));
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
        return left(Tools.field(field), nullSafe(length));
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
        return left(nullSafe(field), Tools.field(length));
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
        return right(Tools.field(field), Tools.field(length));
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
        return right(Tools.field(field), nullSafe(length));
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
        return right(nullSafe(field), Tools.field(length));
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
        return length(Tools.field(value));
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
        return charLength(Tools.field(value));
    }

    /**
     * Get the char_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> charLength(Field<String> field) {
        return new DefaultAggregateFunction<>(Term.CHAR_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the bit_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> bitLength(String value) {
        return bitLength(Tools.field(value));
    }

    /**
     * Get the bit_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> bitLength(Field<String> field) {
        return new DefaultAggregateFunction<>(Term.BIT_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the octet_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> octetLength(String value) {
        return octetLength(Tools.field(value));
    }

    /**
     * Get the octet_length(field) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> octetLength(Field<String> field) {
        return new DefaultAggregateFunction<>(Term.OCTET_LENGTH, SQLDataType.INTEGER, nullSafe(field));
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
     * <td>Oracle 11g</td>
     * <td>
     * <code>LOWER(RAWTOHEX(SYS.DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW( ... ), SYS.DBMS_CRYPTO.HASH_MD5)))</code>
     * </td>
     * </tr>
     * <tr>
     * <td>Oracle 12c</td>
     * <td>
     * <code>LOWER(STANDARD_HASH( ... , 'MD5'))</code>
     * </td>
     * </tr>
     * </table>
     */
    @Support({ MARIADB, MYSQL, POSTGRES })
    public static Field<String> md5(String string) {
        return md5(Tools.field(string));
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
     * <td>Oracle 11g</td>
     * <td>
     * <code>LOWER(RAWTOHEX(SYS.DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW( ... ), SYS.DBMS_CRYPTO.HASH_MD5)))</code>
     * </td>
     * </tr>
     * <tr>
     * <td>Oracle 12c</td>
     * <td>
     * <code>LOWER(STANDARD_HASH( ... , 'MD5'))</code>
     * </td>
     * </tr>
     * </table>
     */
    @Support({ MARIADB, MYSQL, POSTGRES })
    public static Field<String> md5(Field<String> string) {
        return new MD5(nullSafe(string));
    }

    // ------------------------------------------------------------------------
    // XXX Date and time functions
    // ------------------------------------------------------------------------

    /**
     * Get the current_date() function returning a SQL standard
     * {@link SQLDataType#DATE} type.
     * <p>
     * Note, while there is a <code>CURRENT_DATE</code> function in
     * {@link SQLDialect#ORACLE}, that function returns a seconds-precision
     * {@link SQLDataType#TIMESTAMP}, which is undesired from a vendor
     * agnosticity perspective. This function thus produces an expression that
     * conforms to the SQL standard idea of a {@link SQLDataType#DATE} type.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> currentDate() {
        return new CurrentDate<>(SQLDataType.DATE);
    }

    /**
     * Get the current_time() function returning a SQL standard
     * {@link SQLDataType#TIME} type.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Time> currentTime() {
        return new CurrentTime<>(SQLDataType.TIME);
    }

    /**
     * Get the current_timestamp() function returning a SQL standard
     * {@link SQLDataType#TIMESTAMP} type.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> currentTimestamp() {
        return new CurrentTimestamp<>(SQLDataType.TIMESTAMP);
    }

    /**
     * Get the current_timestamp() function returning a SQL standard
     * {@link SQLDataType#TIMESTAMP} type with the specified fractional
     * seconds precision.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Timestamp> currentTimestamp(Field<Integer> precision) {
        return new CurrentTimestamp<>(SQLDataType.TIMESTAMP, precision);
    }

    /**
     * Synonym for {@link #currentTimestamp()}.
     */
    @Support
    public static Field<Timestamp> now() {
        return currentTimestamp();
    }

    /**
     * Synonym for {@link #currentTimestamp(Field)}.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Timestamp> now(Field<Integer> precision) {
        return currentTimestamp(precision);
    }


    /**
     * Get the current_date() function returning a SQL standard
     * {@link SQLDataType#DATE} type.
     * <p>
     * Note, while there is a <code>CURRENT_DATE</code> function in
     * {@link SQLDialect#ORACLE}, that function returns a seconds-precision
     * {@link SQLDataType#TIMESTAMP}, which is undesired from a vendor
     * agnosticity perspective. This function thus produces an expression that
     * conforms to the SQL standard idea of a {@link SQLDataType#DATE} type.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> currentLocalDate() {
        return new CurrentDate<>(SQLDataType.LOCALDATE);
    }

    /**
     * Get the current_time() function returning a SQL standard
     * {@link SQLDataType#TIME} type.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalTime> currentLocalTime() {
        return new CurrentTime<>(SQLDataType.LOCALTIME);
    }

    /**
     * Get the current_timestamp() function returning a SQL standard
     * {@link SQLDataType#TIMESTAMP} type.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> currentLocalDateTime() {
        return new CurrentTimestamp<>(SQLDataType.LOCALDATETIME);
    }

    /**
     * Get the current_time() function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<OffsetTime> currentOffsetTime() {
        return currentTime().cast(SQLDataType.OFFSETTIME);
    }

    /**
     * Get the current_timestamp() function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<OffsetDateTime> currentOffsetDateTime() {
        return currentTimestamp().cast(SQLDataType.OFFSETDATETIME);
    }

    /**
     * Get the current_timestamp() function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Instant> currentInstant() {
        return currentTimestamp().cast(SQLDataType.INSTANT);
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
        return dateDiff(Tools.field(date1), Tools.field(date2));
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
        return dateDiff(nullSafe(date1), Tools.field(date2));
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
        return dateDiff(Tools.field(date1), nullSafe(date2));
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
        return new DateDiff<>(nullSafe(date1), nullSafe(date2));
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
        return dateAdd(Tools.field(date), Tools.field(interval));
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
        return dateAdd(Tools.field(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Date date, Field<? extends Number> interval, DatePart datePart) {
        return dateAdd(Tools.field(date), nullSafe(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Field<Date> date, Number interval, DatePart datePart) {
        return dateAdd(nullSafe(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateAdd(Field<Date> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval), datePart);
    }

    /**
     * Subtract an interval from a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Number)
     */
    @Support
    public static Field<Date> dateSub(Date date, Number interval) {
        return dateSub(Tools.field(date), Tools.field(interval));
    }

    /**
     * Subtract an interval from a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Field)
     */
    @Support
    public static Field<Date> dateSub(Field<Date> date, Field<? extends Number> interval) {
        return nullSafe(date).sub(interval);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateSub(Date date, Number interval, DatePart datePart) {
        return dateSub(Tools.field(date), Tools.field(interval), datePart);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateSub(Date date, Field<? extends Number> interval, DatePart datePart) {
        return dateSub(Tools.field(date), nullSafe(interval), datePart);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateSub(Field<Date> date, Number interval, DatePart datePart) {
        return dateSub(nullSafe(date), Tools.field(interval), datePart);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Date> dateSub(Field<Date> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval).neg(), datePart);
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
        return timestampAdd(Tools.field(timestamp), Tools.field(interval));
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
        return new DateAdd<>(Tools.field(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Timestamp date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), nullSafe(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Field<Timestamp> date, Number interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampAdd(Field<Timestamp> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval), datePart);
    }

    /**
     * Subtract an interval from a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Number)
     */
    @Support
    public static Field<Timestamp> timestampSub(Timestamp timestamp, Number interval) {
        return timestampSub(Tools.field(timestamp), Tools.field(interval));
    }

    /**
     * Subtract an interval from a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Timestamp> timestampSub(Field<Timestamp> timestamp, Field<? extends Number> interval) {
        return nullSafe(timestamp).sub(interval);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampSub(Timestamp date, Number interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), Tools.field(interval).neg(), datePart);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampSub(Timestamp date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), nullSafe(interval).neg(), datePart);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampSub(Field<Timestamp> date, Number interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), Tools.field(interval).neg(), datePart);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Timestamp> timestampSub(Field<Timestamp> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval).neg(), datePart);
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
        return timestampDiff(Tools.field(timestamp1), Tools.field(timestamp2));
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
        return timestampDiff(nullSafe(timestamp1), Tools.field(timestamp2));
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
        return timestampDiff(Tools.field(timestamp1), nullSafe(timestamp2));
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
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> localDateDiff(LocalDate date1, LocalDate date2) {
        return localDateDiff(Tools.field(date1), Tools.field(date2));
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> localDateDiff(Field<LocalDate> date1, LocalDate date2) {
        return localDateDiff(nullSafe(date1), Tools.field(date2));
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> localDateDiff(LocalDate date1, Field<LocalDate> date2) {
        return localDateDiff(Tools.field(date1), nullSafe(date2));
    }

    /**
     * Get the date difference in number of days.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<Integer> localDateDiff(Field<LocalDate> date1, Field<LocalDate> date2) {
        return new DateDiff<>(nullSafe(date1), nullSafe(date2));
    }

    /**
     * Add an interval to a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Number)
     */
    @Support
    public static Field<LocalDate> localDateAdd(LocalDate date, Number interval) {
        return localDateAdd(Tools.field(date), Tools.field(interval));
    }

    /**
     * Add an interval to a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Field)
     */
    @Support
    public static Field<LocalDate> localDateAdd(Field<LocalDate> date, Field<? extends Number> interval) {
        return nullSafe(date).add(interval);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateAdd(LocalDate date, Number interval, DatePart datePart) {
        return localDateAdd(Tools.field(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateAdd(LocalDate date, Field<? extends Number> interval, DatePart datePart) {
        return localDateAdd(Tools.field(date), nullSafe(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateAdd(Field<LocalDate> date, Number interval, DatePart datePart) {
        return localDateAdd(nullSafe(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateAdd(Field<LocalDate> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval), datePart);
    }

    /**
     * Subtract an interval from a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Number)
     */
    @Support
    public static Field<LocalDate> localDateSub(LocalDate date, Number interval) {
        return localDateSub(Tools.field(date), Tools.field(interval));
    }

    /**
     * Subtract an interval from a date.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Field)
     */
    @Support
    public static Field<LocalDate> localDateSub(Field<LocalDate> date, Field<? extends Number> interval) {
        return nullSafe(date).sub(interval);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateSub(LocalDate date, Number interval, DatePart datePart) {
        return localDateSub(Tools.field(date), Tools.field(interval), datePart);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateSub(LocalDate date, Field<? extends Number> interval, DatePart datePart) {
        return localDateSub(Tools.field(date), nullSafe(interval), datePart);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateSub(Field<LocalDate> date, Number interval, DatePart datePart) {
        return localDateSub(nullSafe(date), Tools.field(interval), datePart);
    }

    /**
     * Subtract an interval from a date, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDate> localDateSub(Field<LocalDate> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval).neg(), datePart);
    }

    /**
     * Add an interval to a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Number)
     */
    @Support
    public static Field<LocalDateTime> localDateTimeAdd(LocalDateTime timestamp, Number interval) {
        return localDateTimeAdd(Tools.field(timestamp), Tools.field(interval));
    }

    /**
     * Add an interval to a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#add(Field)
     */
    @Support
    public static Field<LocalDateTime> localDateTimeAdd(Field<LocalDateTime> timestamp, Field<? extends Number> interval) {
        return nullSafe(timestamp).add(interval);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeAdd(LocalDateTime date, Number interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeAdd(LocalDateTime date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), nullSafe(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeAdd(Field<LocalDateTime> date, Number interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), Tools.field(interval), datePart);
    }

    /**
     * Add an interval to a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeAdd(Field<LocalDateTime> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval), datePart);
    }

    /**
     * Subtract an interval from a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Number)
     */
    @Support
    public static Field<LocalDateTime> localDateTimeSub(LocalDateTime timestamp, Number interval) {
        return localDateTimeSub(Tools.field(timestamp), Tools.field(interval));
    }

    /**
     * Subtract an interval from a timestamp.
     * <p>
     * This translates into any dialect
     *
     * @see Field#sub(Field)
     */
    @Support
    public static Field<LocalDateTime> localDateTimeSub(Field<LocalDateTime> timestamp, Field<? extends Number> interval) {
        return nullSafe(timestamp).sub(interval);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeSub(LocalDateTime date, Number interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), Tools.field(interval).neg(), datePart);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeSub(LocalDateTime date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(Tools.field(date), nullSafe(interval).neg(), datePart);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeSub(Field<LocalDateTime> date, Number interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), Tools.field(interval).neg(), datePart);
    }

    /**
     * Subtract an interval from a timestamp, given a date part.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<LocalDateTime> localDateTimeSub(Field<LocalDateTime> date, Field<? extends Number> interval, DatePart datePart) {
        return new DateAdd<>(nullSafe(date), nullSafe(interval).neg(), datePart);
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
    public static Field<DayToSecond> localDateTimeDiff(LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return localDateTimeDiff(Tools.field(timestamp1), Tools.field(timestamp2));
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
    public static Field<DayToSecond> localDateTimeDiff(Field<LocalDateTime> timestamp1, LocalDateTime timestamp2) {
        return localDateTimeDiff(nullSafe(timestamp1), Tools.field(timestamp2));
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
    public static Field<DayToSecond> localDateTimeDiff(LocalDateTime timestamp1, Field<LocalDateTime> timestamp2) {
        return localDateTimeDiff(Tools.field(timestamp1), nullSafe(timestamp2));
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
    public static Field<DayToSecond> localDateTimeDiff(Field<LocalDateTime> timestamp1, Field<LocalDateTime> timestamp2) {
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
        return trunc(Tools.field(date), part);
    }



    /**
     * Truncate a date to the beginning of the day.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<LocalDate> trunc(LocalDate date) {
        return trunc(date, DatePart.DAY);
    }

    /**
     * Truncate a date to a given datepart.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<LocalDate> trunc(LocalDate date, DatePart part) {
        return trunc(Tools.field(date), part);
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
        return trunc(Tools.field(timestamp), part);
    }



    /**
     * Truncate a timestamp to the beginning of the day.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<LocalDateTime> trunc(LocalDateTime timestamp) {
        return trunc(timestamp, DatePart.DAY);
    }

    /**
     * Truncate a timestamp to a given datepart.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static Field<LocalDateTime> trunc(LocalDateTime timestamp, DatePart part) {
        return trunc(Tools.field(timestamp), part);
    }



    /**
     * Truncate a date or a timestamp to the beginning of the day.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static <T> Field<T> trunc(Field<T> date) {
        return trunc(date, DatePart.DAY);
    }

    /**
     * Truncate a date or a timestamp to a given datepart.
     */
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    public static <T> Field<T> trunc(Field<T> date, DatePart part) {
        return new TruncDate<>(date, part);
    }

    // -------------------------------------------------------------------------

    /**
     * Get the extract(field, datePart) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> extract(java.util.Date value, DatePart datePart) {
        return extract(Tools.field(Convert.convert(value, Timestamp.class)), datePart);
    }



    /**
     * Get the extract(field, datePart) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> extract(Temporal value, DatePart datePart) {
        return extract(Tools.field(value), datePart);
    }



    /**
     * Get the extract(field, datePart) function.
     * <p>
     * This translates into any dialect
     */
    @Support
    public static Field<Integer> extract(Field<?> field, DatePart datePart) {
        return new Extract(nullSafe(field), datePart);
    }

    /**
     * Get the epoch of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#EPOCH}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> epoch(java.util.Date value) {
        return extract(value, DatePart.EPOCH);
    }



    /**
     * Get the epoch of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#EPOCH}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> epoch(Temporal value) {
        return extract(value, DatePart.EPOCH);
    }



    /**
     * Get the epoch of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#EPOCH}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> epoch(Field<?> field) {
        return extract(field, DatePart.EPOCH);
    }

    /**
     * Get the millennium of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MILLENNIUM}
     */
    @Support
    public static Field<Integer> millennium(java.util.Date value) {
        return extract(value, DatePart.MILLENNIUM);
    }



    /**
     * Get the millennium of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MILLENNIUM}
     */
    @Support
    public static Field<Integer> millennium(Temporal value) {
        return extract(value, DatePart.MILLENNIUM);
    }



    /**
     * Get the millennium of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MILLENNIUM}
     */
    @Support
    public static Field<Integer> millennium(Field<?> field) {
        return extract(field, DatePart.MILLENNIUM);
    }

    /**
     * Get the century of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#CENTURY}
     */
    @Support
    public static Field<Integer> century(java.util.Date value) {
        return extract(value, DatePart.CENTURY);
    }



    /**
     * Get the century of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#CENTURY}
     */
    @Support
    public static Field<Integer> century(Temporal value) {
        return extract(value, DatePart.CENTURY);
    }



    /**
     * Get the century of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#CENTURY}
     */
    @Support
    public static Field<Integer> century(Field<?> field) {
        return extract(field, DatePart.CENTURY);
    }

    /**
     * Get the decade of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DECADE}
     */
    @Support
    public static Field<Integer> decade(java.util.Date value) {
        return extract(value, DatePart.DECADE);
    }



    /**
     * Get the decade of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DECADE}
     */
    @Support
    public static Field<Integer> decade(Temporal value) {
        return extract(value, DatePart.DECADE);
    }



    /**
     * Get the decade of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DECADE}
     */
    @Support
    public static Field<Integer> decade(Field<?> field) {
        return extract(field, DatePart.DECADE);
    }

    /**
     * Get the quarter of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#QUARTER}
     */
    @Support
    public static Field<Integer> quarter(java.util.Date value) {
        return extract(value, DatePart.QUARTER);
    }



    /**
     * Get the quarter of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#QUARTER}
     */
    @Support
    public static Field<Integer> quarter(Temporal value) {
        return extract(value, DatePart.QUARTER);
    }



    /**
     * Get the quarter of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#QUARTER}
     */
    @Support
    public static Field<Integer> quarter(Field<?> field) {
        return extract(field, DatePart.QUARTER);
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
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#YEAR}
     */
    @Support
    public static Field<Integer> year(Temporal value) {
        return extract(value, DatePart.YEAR);
    }



    /**
     * Get the year part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#YEAR}
     */
    @Support
    public static Field<Integer> year(Field<?> field) {
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
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#MONTH}
     */
    @Support
    public static Field<Integer> month(Temporal value) {
        return extract(value, DatePart.MONTH);
    }



    /**
     * Get the month part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MONTH}
     */
    @Support
    public static Field<Integer> month(Field<?> field) {
        return extract(field, DatePart.MONTH);
    }

    /**
     * Get the week part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> week(java.util.Date value) {
        return extract(value, DatePart.WEEK);
    }



    /**
     * Get the week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> week(Temporal value) {
        return extract(value, DatePart.WEEK);
    }



    /**
     * Get the week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> week(Field<?> field) {
        return extract(field, DatePart.WEEK);
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
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#DAY}
     */
    @Support
    public static Field<Integer> day(Temporal value) {
        return extract(value, DatePart.DAY);
    }



    /**
     * Get the day part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DAY}
     */
    @Support
    public static Field<Integer> day(Field<?> field) {
        return extract(field, DatePart.DAY);
    }

    /**
     * Get the day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#DAY_OF_WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> dayOfWeek(java.util.Date value) {
        return extract(value, DatePart.DAY_OF_WEEK);
    }



    /**
     * Get the day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#DAY_OF_WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> dayOfWeek(Temporal value) {
        return extract(value, DatePart.DAY_OF_WEEK);
    }



    /**
     * Get the day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DAY_OF_WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> dayOfWeek(Field<?> field) {
        return extract(field, DatePart.DAY_OF_WEEK);
    }

    /**
     * Get the ISO day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#ISO_DAY_OF_WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> isoDayOfWeek(java.util.Date value) {
        return extract(value, DatePart.ISO_DAY_OF_WEEK);
    }



    /**
     * Get the ISO day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#ISO_DAY_OF_WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> isoDayOfWeek(Temporal value) {
        return extract(value, DatePart.ISO_DAY_OF_WEEK);
    }



    /**
     * Get the ISO day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#ISO_DAY_OF_WEEK}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> isoDayOfWeek(Field<?> field) {
        return extract(field, DatePart.ISO_DAY_OF_WEEK);
    }

    /**
     * Get the day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#DAY_OF_YEAR}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> dayOfYear(java.util.Date value) {
        return extract(value, DatePart.DAY_OF_YEAR);
    }



    /**
     * Get the day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#DAY_OF_YEAR}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> dayOfYear(Temporal value) {
        return extract(value, DatePart.DAY_OF_YEAR);
    }



    /**
     * Get the day of week part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#DAY_OF_YEAR}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> dayOfYear(Field<?> field) {
        return extract(field, DatePart.DAY_OF_YEAR);
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
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#HOUR}
     */
    @Support
    public static Field<Integer> hour(Temporal value) {
        return extract(value, DatePart.HOUR);
    }



    /**
     * Get the hour part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#HOUR}
     */
    @Support
    public static Field<Integer> hour(Field<?> field) {
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
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#MINUTE}
     */
    @Support
    public static Field<Integer> minute(Temporal value) {
        return extract(value, DatePart.MINUTE);
    }



    /**
     * Get the minute part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MINUTE}
     */
    @Support
    public static Field<Integer> minute(Field<?> field) {
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
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#SECOND}
     */
    @Support
    public static Field<Integer> second(Temporal value) {
        return extract(value, DatePart.SECOND);
    }



    /**
     * Get the second part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#SECOND}
     */
    @Support
    public static Field<Integer> second(Field<?> field) {
        return extract(field, DatePart.SECOND);
    }

    /**
     * Get the millisecond part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#MILLISECOND}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> millisecond(java.util.Date value) {
        return extract(value, DatePart.MILLISECOND);
    }



    /**
     * Get the millisecond part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#MILLISECOND}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> millisecond(Temporal value) {
        return extract(value, DatePart.MILLISECOND);
    }



    /**
     * Get the millisecond part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MILLISECOND}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> millisecond(Field<?> field) {
        return extract(field, DatePart.MILLISECOND);
    }

    /**
     * Get the microsecond part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#MICROSECOND}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> microsecond(java.util.Date value) {
        return extract(value, DatePart.MICROSECOND);
    }



    /**
     * Get the microsecond part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#MICROSECOND}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> microsecond(Temporal value) {
        return extract(value, DatePart.MICROSECOND);
    }



    /**
     * Get the microsecond part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#MICROSECOND}
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<Integer> microsecond(Field<?> field) {
        return extract(field, DatePart.MICROSECOND);
    }

    /**
     * Get the timezone part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#TIMEZONE}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezone(java.util.Date value) {
        return extract(value, DatePart.TIMEZONE);
    }



    /**
     * Get the timezone part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#TIMEZONE}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezone(Temporal value) {
        return extract(value, DatePart.TIMEZONE);
    }



    /**
     * Get the timezone part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#TIMEZONE}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezone(Field<?> field) {
        return extract(field, DatePart.TIMEZONE);
    }

    /**
     * Get the timezoneHour part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#TIMEZONE_HOUR}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezoneHour(java.util.Date value) {
        return extract(value, DatePart.TIMEZONE_HOUR);
    }



    /**
     * Get the timezoneHour part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#TIMEZONE_HOUR}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezoneHour(Temporal value) {
        return extract(value, DatePart.TIMEZONE_HOUR);
    }



    /**
     * Get the timezoneHour part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#TIMEZONE_HOUR}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezoneHour(Field<?> field) {
        return extract(field, DatePart.TIMEZONE_HOUR);
    }

    /**
     * Get the timezoneMinute part of a date.
     * <p>
     * This is the same as calling {@link #extract(java.util.Date, DatePart)}
     * with {@link DatePart#TIMEZONE_MINUTE}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezoneMinute(java.util.Date value) {
        return extract(value, DatePart.TIMEZONE_MINUTE);
    }



    /**
     * Get the timezoneMinute part of a date.
     * <p>
     * This is the same as calling {@link #extract(Temporal, DatePart)}
     * with {@link DatePart#TIMEZONE_MINUTE}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezoneMinute(Temporal value) {
        return extract(value, DatePart.TIMEZONE_MINUTE);
    }



    /**
     * Get the timezoneMinute part of a date.
     * <p>
     * This is the same as calling {@link #extract(Field, DatePart)}
     * with {@link DatePart#TIMEZONE_MINUTE}
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Integer> timezoneMinute(Field<?> field) {
        return extract(field, DatePart.TIMEZONE_MINUTE);
    }

    /**
     * Convert a string value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Date> date(String value) {
        return Tools.field(Convert.convert(value, Date.class));
    }

    /**
     * Convert a temporal value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Date> date(java.util.Date value) {
        return Tools.field(Convert.convert(value, Date.class));
    }

    /**
     * Convert a temporal value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Date> date(Field<? extends java.util.Date> field) {
        return new DateOrTime<>(field, SQLDataType.DATE);
    }

    /**
     * Convert a string value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Time> time(String value) {
        return Tools.field(Convert.convert(value, Time.class));
    }

    /**
     * Convert a temporal value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Time> time(java.util.Date value) {
        return Tools.field(Convert.convert(value, Time.class));
    }

    /**
     * Convert a temporal value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Time> time(Field<? extends java.util.Date> field) {
        return new DateOrTime<>(field, SQLDataType.TIME);
    }

    /**
     * Convert a string value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Timestamp> timestamp(String value) {
        return Tools.field(Convert.convert(value, Timestamp.class));
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Timestamp> timestamp(java.util.Date value) {
        return Tools.field(Convert.convert(value, Timestamp.class));
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Timestamp> timestamp(Field<? extends java.util.Date> field) {
        return new DateOrTime<>(field, SQLDataType.TIMESTAMP);
    }


    /**
     * Convert a string value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalDate> localDate(String value) {
        return Tools.field(Convert.convert(value, LocalDate.class));
    }

    /**
     * Convert a temporal value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalDate> localDate(LocalDate value) {
        return Tools.field(value);
    }

    /**
     * Convert a temporal value to a <code>DATE</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalDate> localDate(Field<LocalDate> field) {
        return new DateOrTime<>(field, SQLDataType.LOCALDATE);
    }

    /**
     * Convert a string value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalTime> localTime(String value) {
        return Tools.field(Convert.convert(value, LocalTime.class));
    }

    /**
     * Convert a temporal value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalTime> localTime(LocalTime value) {
        return Tools.field(value);
    }

    /**
     * Convert a temporal value to a <code>TIME</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalTime> localTime(Field<LocalTime> field) {
        return new DateOrTime<>(field, SQLDataType.LOCALTIME);
    }

    /**
     * Convert a string value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalDateTime> localDateTime(String value) {
        return Tools.field(Convert.convert(value, LocalDateTime.class));
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalDateTime> localDateTime(LocalDateTime value) {
        return Tools.field(value);
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP</code>.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<LocalDateTime> localDateTime(Field<LocalDateTime> field) {
        return new DateOrTime<>(field, SQLDataType.LOCALDATETIME);
    }

    /**
     * Convert a string value to a <code>TIME WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<OffsetTime> offsetTime(String value) {
        return Tools.field(Convert.convert(value, OffsetTime.class));
    }

    /**
     * Convert a temporal value to a <code>TIME WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<OffsetTime> offsetTime(OffsetTime value) {
        return Tools.field(value);
    }

    /**
     * Convert a temporal value to a <code>TIME WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<OffsetTime> offsetTime(Field<OffsetTime> field) {
        return new DateOrTime<>(field, SQLDataType.OFFSETTIME);
    }

    /**
     * Convert a string value to a <code>TIMESTAMP WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<OffsetDateTime> offsetDateTime(String value) {
        return Tools.field(Convert.convert(value, OffsetDateTime.class));
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<OffsetDateTime> offsetDateTime(OffsetDateTime value) {
        return Tools.field(value);
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<OffsetDateTime> offsetDateTime(Field<OffsetDateTime> field) {
        return new DateOrTime<>(field, SQLDataType.OFFSETDATETIME);
    }

    /**
     * Convert a string value to a <code>TIMESTAMP WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<Instant> instant(String value) {
        return Tools.field(Convert.convert(value, Instant.class));
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<Instant> instant(Instant value) {
        return Tools.field(value);
    }

    /**
     * Convert a temporal value to a <code>TIMESTAMP WITH TIME ZONE</code>.
     * <p>
     * Depending on whether the database preserves the time zone information
     * (e.g. {@link SQLDialect#ORACLE}) or not (e.g.
     * {@link SQLDialect#POSTGRES}), the resulting value might be converted to
     * UTC. Regardless of this fact, the result should be the same
     * {@link Instant} (in UTC) as the input.
     */
    @Support({ H2, HSQLDB, POSTGRES, SQLITE })
    public static Field<Instant> instant(Field<Instant> field) {
        return new DateOrTime<>(field, SQLDataType.INSTANT);
    }


    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Date> toDate(String value, String format) {
        return toDate(Tools.field(value), Tools.field(format));
    }

    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Date> toDate(String value, Field<String> format) {
        return toDate(Tools.field(value), nullSafe(format));
    }

    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Date> toDate(Field<String> value, String format) {
        return toDate(nullSafe(value), Tools.field(format));
    }

    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Date> toDate(Field<String> value, Field<String> format) {
        return function("to_date", SQLDataType.DATE, value, format);
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Timestamp> toTimestamp(String value, String format) {
        return toTimestamp(Tools.field(value), Tools.field(format));
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Timestamp> toTimestamp(String value, Field<String> format) {
        return toTimestamp(Tools.field(value), nullSafe(format));
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Timestamp> toTimestamp(Field<String> value, String format) {
        return toTimestamp(nullSafe(value), Tools.field(format));
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<Timestamp> toTimestamp(Field<String> value, Field<String> format) {
        return function("to_timestamp", SQLDataType.TIMESTAMP, value, format);
    }



    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDate> toLocalDate(String value, String format) {
        return toDate(value, format).coerce(SQLDataType.LOCALDATE);
    }

    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDate> toLocalDate(String value, Field<String> format) {
        return toDate(value, format).coerce(SQLDataType.LOCALDATE);
    }

    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDate> toLocalDate(Field<String> value, String format) {
        return toDate(value, format).coerce(SQLDataType.LOCALDATE);
    }

    /**
     * Parse a value to a <code>DATE</code>.
     *
     * @param value The formatted <code>DATE</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDate> toLocalDate(Field<String> value, Field<String> format) {
        return toDate(value, format).coerce(SQLDataType.LOCALDATE);
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDateTime> toLocalDateTime(String value, String format) {
        return toTimestamp(value, format).coerce(SQLDataType.LOCALDATETIME);
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDateTime> toLocalDateTime(String value, Field<String> format) {
        return toTimestamp(value, format).coerce(SQLDataType.LOCALDATETIME);
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDateTime> toLocalDateTime(Field<String> value, String format) {
        return toTimestamp(value, format).coerce(SQLDataType.LOCALDATETIME);
    }

    /**
     * Parse a value to a <code>TIMESTAMP</code>.
     *
     * @param value The formatted <code>TIMESTAMP</code> value.
     * @param format The vendor-specific formatting string.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static Field<LocalDateTime> toLocalDateTime(Field<String> value, Field<String> format) {
        return toTimestamp(value, format).coerce(SQLDataType.LOCALDATETIME);
    }



    // ------------------------------------------------------------------------
    // XXX Construction of GROUPING SET functions
    // ------------------------------------------------------------------------

    /**
     * Create a ROLLUP(field1, field2, .., fieldn) grouping field.
     *
     * @see #rollup(FieldOrRow...)
     */
    @Support({ CUBRID, MARIADB, MYSQL, POSTGRES })
    public static GroupField rollup(Field<?>... fields) {
        return rollup((FieldOrRow[]) nullSafe(fields));
    }

    /**
     * Create a ROLLUP(field1, field2, .., fieldn) grouping field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>MySQL (emulated using the GROUP BY .. WITH ROLLUP clause)</li>
     * <li>Oracle</li>
     * <li>PostgreSQL 9.5</li>
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
    @Support({ CUBRID, MARIADB, MYSQL, POSTGRES })
    public static GroupField rollup(FieldOrRow... fields) {
        return new Rollup(fields);
    }

    /**
     * Create a CUBE(field1, field2, .., fieldn) grouping field.
     *
     * @see #cube(Field...)
     */
    @Support({ POSTGRES })
    public static GroupField cube(Field<?>... fields) {
        return cube((FieldOrRow[]) nullSafe(fields));
    }

    /**
     * Create a CUBE(field1, field2, .., fieldn) grouping field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>PostgreSQL 9.5</li>
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
     * @param fields The fields that are part of the <code>CUBE</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @Support({ POSTGRES })
    public static GroupField cube(FieldOrRow... fields) {
        return field("{cube}({0})", Object.class, new QueryPartList<>(fields));
    }

    /**
     * Create a GROUPING SETS(field1, field2, .., fieldn) grouping field where
     * each grouping set only consists of a single field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>PostgreSQL 9.5</li>
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
     * @param fields The fields that are part of the <code>GROUPING SETS</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @Support({ POSTGRES })
    public static GroupField groupingSets(Field<?>... fields) {
        List<Field<?>>[] array = new List[fields.length];

        for (int i = 0; i < fields.length; i++)
            array[i] = Arrays.<Field<?>>asList(fields[i]);

        return groupingSets(array);
    }

    /**
     * Create a GROUPING SETS((field1a, field1b), (field2a), .., (fieldna,
     * fieldnb)) grouping field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>PostgreSQL 9.5</li>
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
     * @param fieldSets The fields that are part of the <code>GROUPING SETS</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @Support({ POSTGRES })
    public static GroupField groupingSets(Field<?>[]... fieldSets) {
        List<Field<?>>[] array = new List[fieldSets.length];

        for (int i = 0; i < fieldSets.length; i++)
            array[i] = Arrays.asList(fieldSets[i]);

        return groupingSets(array);
    }

    /**
     * Create a GROUPING SETS((field1a, field1b), (field2a), .., (fieldna,
     * fieldnb)) grouping field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>PostgreSQL 9.5</li>
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
     * @param fieldSets The fields that are part of the <code>GROUPING SETS</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @Support({ POSTGRES })
    public static GroupField groupingSets(Collection<? extends Field<?>>... fieldSets) {
        QueryPartList<WrappedList> arg = new QueryPartList<>();

        for (Collection<? extends Field<?>> fieldsSet : fieldSets)
            arg.add(new WrappedList(new QueryPartList<>(fieldsSet)));

        return field("grouping sets({0})", SQLDataType.OTHER, arg);
    }









































    /**
     * Create a GROUPING(field) aggregation field to be used along with
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * groupings.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>PostgreSQL 9.5</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     *
     * @param field The function argument
     * @return The <code>GROUPING</code> aggregation field
     * @see #cube(Field...)
     * @see #rollup(Field...)
     */
    @Support({ POSTGRES })
    public static Field<Integer> grouping(Field<?> field) {
        return function("grouping", Integer.class, field);
    }

    // ------------------------------------------------------------------------
    // XXX Bitwise operations
    // ------------------------------------------------------------------------

    /**
     * The MySQL <code>BIT_COUNT(field)</code> function, counting the number of
     * bits that are set in this number.
     *
     * @see #bitCount(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static Field<Integer> bitCount(Number value) {
        return bitCount(Tools.field(value));
    }

    /**
     * The MySQL <code>BIT_COUNT(field)</code> function, counting the number of
     * bits that are set in this number.
     * <p>
     * This function is emulated in most other databases like this (for a
     * TINYINT field): <code><pre>
     * ([field] &amp;   1) +
     * ([field] &amp;   2) &gt;&gt; 1 +
     * ([field] &amp;   4) &gt;&gt; 2 +
     * ([field] &amp;   8) &gt;&gt; 3 +
     * ([field] &amp;  16) &gt;&gt; 4 +
     *  ...
     * ([field] &amp; 128) &gt;&gt; 7
     * </pre></code>
     * <p>
     * More efficient algorithms are very welcome
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
        return bitNot(Tools.field(value));
    }

    /**
     * The bitwise not operator.
     * <p>
     * Most dialects natively support this using <code>~[field]</code>. jOOQ
     * emulates this operator in some dialects using <code>-[field] - 1</code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNot(Field<T> field) {
        return new Neg<>(nullSafe(field), ExpressionOperator.BIT_NOT);
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(T value1, T value2) {
        return bitAnd(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(T value1, Field<T> value2) {
        return bitAnd(Tools.field(value1, value2), nullSafe(value2));
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(Field<T> value1, T value2) {
        return bitAnd(nullSafe(value1), Tools.field(value2, value1));
    }

    /**
     * The bitwise and operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the and operation where available:
     * <code><pre>[field1] &amp; [field2]</pre></code>
     * ... or the and function elsewhere:
     * <code><pre>bitand([field1], [field2])</pre></code>
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitAnd(Field<T> field1, Field<T> field2) {
        return new Expression<>(ExpressionOperator.BIT_AND, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(T value1, T value2) {
        return bitNand(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(T value1, Field<T> value2) {
        return bitNand(Tools.field(value1, value2), nullSafe(value2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(Field<T> value1, T value2) {
        return bitNand(nullSafe(value1), Tools.field(value2, value1));
    }

    /**
     * The bitwise not and operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the not and operation where available:
     * <code><pre>~([field1] &amp; [field2])</pre></code>
     * ... or the not and function elsewhere:
     * <code><pre>bitnot(bitand([field1], [field2]))</pre></code>
     *
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNand(Field<T> field1, Field<T> field2) {
        return new Expression<>(ExpressionOperator.BIT_NAND, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(T value1, T value2) {
        return bitOr(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(T value1, Field<T> value2) {
        return bitOr(Tools.field(value1, value2), nullSafe(value2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitOr(Field<T> value1, T value2) {
        return bitOr(nullSafe(value1), Tools.field(value2, value1));
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
        return new Expression<>(ExpressionOperator.BIT_OR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(T value1, T value2) {
        return bitNor(Tools.field(value1), Tools.field(value2));
    }
    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(T value1, Field<T> value2) {
        return bitNor(Tools.field(value1, value2), nullSafe(value2));
    }
    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitNor(Field<T> value1, T value2) {
        return bitNor(nullSafe(value1), Tools.field(value2, value1));
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
        return new Expression<>(ExpressionOperator.BIT_NOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(T value1, T value2) {
        return bitXor(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(T value1, Field<T> value2) {
        return bitXor(Tools.field(value1, value2), nullSafe(value2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXor(Field<T> value1, T value2) {
        return bitXor(nullSafe(value1), Tools.field(value2, value1));
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
        return new Expression<>(ExpressionOperator.BIT_XOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(T value1, T value2) {
        return bitXNor(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(T value1, Field<T> value2) {
        return bitXNor(Tools.field(value1, value2), nullSafe(value2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> bitXNor(Field<T> value1, T value2) {
        return bitXNor(nullSafe(value1), Tools.field(value2, value1));
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
        return new Expression<>(ExpressionOperator.BIT_XNOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(T value1, Number value2) {
        return shl(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(T value1, Field<? extends Number> value2) {
        return shl(Tools.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(Field<T> value1, Number value2) {
        return shl(nullSafe(value1), Tools.field(value2));
    }

    /**
     * The bitwise left shift operator.
     * <p>
     * Some dialects natively support this using <code>[field1] &lt;&lt; [field2]</code>.
     * jOOQ emulates this operator in some dialects using
     * <code>[field1] * power(2, [field2])</code>, where power might also be emulated.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shl(Field<T> field1, Field<? extends Number> field2) {
        return new Expression<>(ExpressionOperator.SHL, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(T value1, Number value2) {
        return shr(Tools.field(value1), Tools.field(value2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(T value1, Field<? extends Number> value2) {
        return shr(Tools.field(value1), nullSafe(value2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(Field<T> value1, Number value2) {
        return shr(nullSafe(value1), Tools.field(value2));
    }

    /**
     * The bitwise right shift operator.
     * <p>
     * Some dialects natively support this using <code>[field1] &gt;&gt; [field2]</code>.
     * jOOQ emulates this operator in some dialects using
     * <code>[field1] / power(2, [field2])</code>, where power might also be emulated.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T extends Number> Field<T> shr(Field<T> field1, Field<? extends Number> field2) {
        return new Expression<>(ExpressionOperator.SHR, nullSafe(field1), nullSafe(field2));
    }

    // ------------------------------------------------------------------------
    // XXX Mathematical functions
    // ------------------------------------------------------------------------

    /**
     * Get the rand() function.
     */
    @Support
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
        return greatest(Tools.field(value), Tools.fields(values).toArray(EMPTY_FIELD));
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
        return new Greatest<>(nullSafeDataType(field), nullSafe(combine(field, others)));
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
        return least(Tools.field(value), Tools.fields(values).toArray(EMPTY_FIELD));
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
        return new Least<>(nullSafeDataType(field), nullSafe(combine(field, others)));
    }

    /**
     * Negate a field to get its negative value.
     *
     * @see Field#neg()
     */
    @Support
    public static <T extends Number> Field<T> neg(Field<T> field) {
        return field.neg();
    }

    /**
     * Negate a field to get its negative value.
     *
     * @see Field#neg()
     */
    @Support
    public static <T extends Number> Field<T> minus(Field<T> field) {
        return field.neg();
    }

    /**
     * Get the sign of a numeric field: sign(field).
     *
     * @see #sign(Field)
     */
    @Support
    public static Field<Integer> sign(Number value) {
        return sign(Tools.field(value));
    }

    /**
     * Get the sign of a numeric field: sign(field).
     * <p>
     * This renders the sign function where available:
     * <code><pre>sign([field])</pre></code>
     * ... or emulates it elsewhere (without bind variables on values -1, 0, 1):
     * <code><pre>
     * CASE WHEN [this] &gt; 0 THEN 1
     *      WHEN [this] &lt; 0 THEN -1
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
        return abs(Tools.field(value));
    }

    /**
     * Get the absolute value of a numeric field: abs(field).
     * <p>
     * This renders the same on all dialects:
     * <code><pre>abs([field])</pre></code>
     */
    @Support
    public static <T extends Number> Field<T> abs(Field<T> field) {
        return function("abs", nullSafeDataType(field), field);
    }

    /**
     * Get rounded value of a numeric field: round(field).
     *
     * @see #round(Field)
     */
    @Support
    public static <T extends Number> Field<T> round(T value) {
        return round(Tools.field(value));
    }

    /**
     * Get rounded value of a numeric field: round(field).
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field]) or
     * round([field], 0)</pre></code>
     * ... or emulates it elsewhere using floor and ceil
     */
    @Support
    public static <T extends Number> Field<T> round(Field<T> field) {
        return new Round<>(nullSafe(field));
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals).
     *
     * @see #round(Field, int)
     */
    @Support
    public static <T extends Number> Field<T> round(T value, int decimals) {
        return round(Tools.field(value), decimals);
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals).
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field], [decimals])</pre></code>
     * ... or emulates it elsewhere using floor and ceil
     */
    @Support
    public static <T extends Number> Field<T> round(Field<T> field, int decimals) {
        return new Round<>(nullSafe(field), Tools.field(decimals));
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals).
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field], [decimals])</pre></code>
     * ... or emulates it elsewhere using floor and ceil
     */
    @Support
    public static <T extends Number> Field<T> round(Field<T> field, Field<Integer> decimals) {
        return new Round<>(nullSafe(field), decimals);
    }

    /**
     * Get the largest integer value not greater than [this].
     *
     * @see #floor(Field)
     */
    @Support
    public static <T extends Number> Field<T> floor(T value) {
        return floor(Tools.field(value));
    }

    /**
     * Get the largest integer value not greater than [this].
     * <p>
     * This renders the floor function where available:
     * <code><pre>floor([this])</pre></code>
     * ... or emulates it elsewhere using round:
     * <code><pre>round([this] - 0.499999999999999)</pre></code>
     */
    @Support
    public static <T extends Number> Field<T> floor(Field<T> field) {
        return new Floor<>(nullSafe(field));
    }

    /**
     * Get the smallest integer value not less than [this].
     *
     * @see #ceil(Field)
     */
    @Support
    public static <T extends Number> Field<T> ceil(T value) {
        return ceil(Tools.field(value));
    }

    /**
     * Get the smallest integer value not less than [field].
     * <p>
     * This renders the ceil or ceiling function where available:
     * <code><pre>ceil([field]) or
     * ceiling([field])</pre></code>
     * ... or emulates it elsewhere using round:
     * <code><pre>round([field] + 0.499999999999999)</pre></code>
     */
    @Support
    public static <T extends Number> Field<T> ceil(Field<T> field) {
        return new Ceil<>(nullSafe(field));
    }

    /**
     * Truncate a number to a given number of decimals.
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(T number) {
        return trunc(Tools.field(number), inline(0));
    }

    /**
     * Truncate a number to a given number of decimals.
     *
     * @see #trunc(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static <T extends Number> Field<T> trunc(T number, int decimals) {
        return trunc(Tools.field(number), inline(decimals));
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
        return trunc(Tools.field(number), nullSafe(decimals));
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
        return new Trunc<>(nullSafe(number), nullSafe(decimals));
    }

    /**
     * Get the sqrt(field) function.
     *
     * @see #sqrt(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sqrt(Number value) {
        return sqrt(Tools.field(value));
    }

    /**
     * Get the sqrt(field) function.
     * <p>
     * This renders the sqrt function where available:
     * <code><pre>sqrt([field])</pre></code> ... or emulates it elsewhere using
     * power (which in turn may also be emulated using ln and exp functions):
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
        return exp(Tools.field(value));
    }

    /**
     * Get the exp(field) function, taking this field as the power of e.
     * <p>
     * This renders the same on all dialects:
     * <code><pre>exp([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> exp(Field<? extends Number> field) {
        return new Exp(nullSafe(field));
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field.
     *
     * @see #ln(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> ln(Number value) {
        return ln(Tools.field(value));
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
        return log(Tools.field(value), base);
    }

    /**
     * Get the log(field, base) function.
     * <p>
     * This renders the log function where available:
     * <code><pre>log([field])</pre></code> ... or emulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([field]) / ln([base])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> log(Field<? extends Number> field, int base) {
        return new Ln(nullSafe(field), Tools.field(base));
    }

    /**
     * Get the log(field, base) function.
     * <p>
     * This renders the log function where available:
     * <code><pre>log([field])</pre></code> ... or emulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([field]) / ln([base])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> log(Field<? extends Number> field, Field<? extends Number> base) {
        return new Ln(nullSafe(field), base);
    }

    /**
     * Get the power(field, exponent) function.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Number value, Number exponent) {
        return power(Tools.field(value), Tools.field(exponent));
    }

    /**
     * Get the power(field, exponent) function.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Field<? extends Number> field, Number exponent) {
        return power(nullSafe(field), Tools.field(exponent));
    }

    /**
     * Get the power(field, exponent) function.
     *
     * @see #power(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> power(Number value, Field<? extends Number> exponent) {
        return power(Tools.field(value), nullSafe(exponent));
    }

    /**
     * Get the power(field, exponent) function.
     * <p>
     * This renders the power function where available:
     * <code><pre>power([field], [exponent])</pre></code> ... or emulates it
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
        return acos(Tools.field(value));
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
        return asin(Tools.field(value));
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
        return atan(Tools.field(value));
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
        return atan2(Tools.field(x), Tools.field(y));
    }

    /**
     * Get the atan2(field, y) function.
     *
     * @see #atan2(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan2(Number x, Field<? extends Number> y) {
        return atan2(Tools.field(x), nullSafe(y));
    }

    /**
     * Get the atan2(field, y) function.
      *
     * @see #atan2(Field, Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Number y) {
        return atan2(nullSafe(x), Tools.field(y));
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
        return new DefaultAggregateFunction<>(Term.ATAN2, SQLDataType.NUMERIC, nullSafe(x), nullSafe(y));
    }

    /**
     * Get the cosine(field) function.
     *
     * @see #cos(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cos(Number value) {
        return cos(Tools.field(value));
    }

    /**
     * Get the cosine(field) function.
     * <p>
     * This renders the cos function where available:
     * <code><pre>cos([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cos(Field<? extends Number> field) {
        return function("cos", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the sine(field) function.
     *
     * @see #sin(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sin(Number value) {
        return sin(Tools.field(value));
    }

    /**
     * Get the sine(field) function.
     * <p>
     * This renders the sin function where available:
     * <code><pre>sin([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> sin(Field<? extends Number> field) {
        return function("sin", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the tangent(field) function.
     *
     * @see #tan(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> tan(Number value) {
        return tan(Tools.field(value));
    }

    /**
     * Get the tangent(field) function.
     * <p>
     * This renders the tan function where available:
     * <code><pre>tan([field])</pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> tan(Field<? extends Number> field) {
        return function("tan", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the cotangent(field) function.
     *
     * @see #cot(Field)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static Field<BigDecimal> cot(Number value) {
        return cot(Tools.field(value));
    }

    /**
     * Get the cotangent(field) function.
     * <p>
     * This renders the cot function where available:
     * <code><pre>cot([field])</pre></code> ... or emulates it elsewhere using
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
        return sinh(Tools.field(value));
    }

    /**
     * Get the hyperbolic sine function: sinh(field).
     * <p>
     * This renders the sinh function where available:
     * <code><pre>sinh([field])</pre></code> ... or emulates it elsewhere using
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
        return cosh(Tools.field(value));
    }

    /**
     * Get the hyperbolic cosine function: cosh(field).
     * <p>
     * This renders the cosh function where available:
     * <code><pre>cosh([field])</pre></code> ... or emulates it elsewhere using
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
        return tanh(Tools.field(value));
    }

    /**
     * Get the hyperbolic tangent function: tanh(field).
     * <p>
     * This renders the tanh function where available:
     * <code><pre>tanh([field])</pre></code> ... or emulates it elsewhere using
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
        return coth(Tools.field(value));
    }

    /**
     * Get the hyperbolic cotangent function: coth(field).
     * <p>
     * This is not supported by any RDBMS, but emulated using exp exp:
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
        return deg(Tools.field(value));
    }

    /**
     * Calculate degrees from radians from this field.
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([field])</pre></code> ... or emulates it elsewhere:
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
        return rad(Tools.field(value));
    }

    /**
     * Calculate radians from degrees from this field.
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([field])</pre></code> ... or emulates it elsewhere:
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
        return field("{connect_by_root} {0}", nullSafeDataType(field), field);
    }

    /**
     * Retrieve the Oracle-specific
     * <code>SYS_CONNECT_BY_PATH(field, separator)</code> function (to be used
     * along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static Field<String> sysConnectByPath(Field<?> field, String separator) {
        return function("sys_connect_by_path", String.class, field, inline(separator));
    }

    /**
     * Add the Oracle-specific <code>PRIOR</code> unary operator before a field
     * (to be used along with <code>CONNECT BY</code> clauses).
     */
    @Support({ CUBRID })
    public static <T> Field<T> prior(Field<T> field) {
        return new Prior<>(field);
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
    // XXX XML functions
    // -------------------------------------------------------------------------

    /**
     * The XML comment constructor.
     */
    @Support({ POSTGRES })
    public static Field<XML> xmlcomment(String comment) {
        return xmlcomment(val(comment));
    }

    /**
     * The XML comment constructor.
     */
    @Support({ POSTGRES })
    public static Field<XML> xmlcomment(Field<String> comment) {
        return new XMLComment(comment);
    }

    /**
     * The XML concat function.
     */
    @Support({ POSTGRES })
    @SafeVarargs
    public static Field<XML> xmlconcat(Field<XML>... fields) {
        return xmlconcat(asList(fields));
    }

    /**
     * The XML concat function.
     */
    @Support({ POSTGRES })
    public static Field<XML> xmlconcat(Collection<? extends Field<XML>> fields) {
        return new XMLConcat(fields);
    }

    // -------------------------------------------------------------------------
    // XXX JSON functions
    // -------------------------------------------------------------------------

    /**
     * The JSON array constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONArrayNullStep<JSON> jsonArray(Field<?>... fields) {
        return jsonArray(Arrays.asList(fields));
    }

    /**
     * The JSON array constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONArrayNullStep<JSON> jsonArray(Collection<? extends Field<?>> fields) {
        return new JSONArray<>(JSON, fields);
    }

    /**
     * The JSONB array constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONArrayNullStep<JSONB> jsonbArray(Field<?>... fields) {
        return jsonbArray(Arrays.asList(fields));
    }

    /**
     * The JSONB array constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONArrayNullStep<JSONB> jsonbArray(Collection<? extends Field<?>> fields) {
        return new JSONArray<>(JSONB, fields);
    }

    /**
     * A constructor for JSON entries to be used with {@link #jsonObject(JSONEntry...)}.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static <T> JSONEntry<T> jsonEntry(String key, Field<T> value) {
        return jsonEntry(Tools.field(key), value);
    }

    /**
     * A constructor for JSON entries to be used with {@link #jsonObject(JSONEntry...)}.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static <T> JSONEntry<T> jsonEntry(Field<String> key, Field<T> value) {
        return new JSONEntryImpl<>(key, value);
    }

    /**
     * The JSON object constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectNullStep<JSON> jsonObject(String key, Field<?> value) {
        return jsonObject(jsonEntry(key, value));
    }

    /**
     * The JSON object constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectNullStep<JSON> jsonObject(Field<String> key, Field<?> value) {
        return jsonObject(jsonEntry(key, value));
    }

    /**
     * The JSON object constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectNullStep<JSON> jsonObject(JSONEntry<?>... entries) {
        return jsonObject(Arrays.asList(entries));
    }

    /**
     * The JSON object constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectNullStep<JSON> jsonObject(Collection<? extends JSONEntry<?>> entries) {
        return new JSONObject<>(JSON, entries);
    }

    /**
     * The JSONB object constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectNullStep<JSONB> jsonbObject(JSONEntry<?>... entries) {
        return jsonbObject(Arrays.asList(entries));
    }

    /**
     * The JSONB object constructor.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectNullStep<JSONB> jsonbObject(Collection<? extends JSONEntry<?>> entries) {
        return new JSONObject<>(JSONB, entries);
    }

    /**
     * The JSON array aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONArrayAggOrderByStep<JSON> jsonArrayAgg(Field<?> value) {
        return new JSONArrayAgg<JSON>(JSON, value);
    }

    /**
     * The JSON array aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONArrayAggOrderByStep<JSONB> jsonbArrayAgg(Field<?> value) {
        return new JSONArrayAgg<JSONB>(JSONB, value);
    }

    /**
     * The JSON object aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectAggNullStep<JSON> jsonObjectAgg(String key, Field<?> value) {
        return jsonObjectAgg(Tools.field(key), value);
    }

    /**
     * The JSON object aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectAggNullStep<JSON> jsonObjectAgg(Field<String> key, Field<?> value) {
        return jsonObjectAgg(jsonEntry(key, value));
    }

    /**
     * The JSON object aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectAggNullStep<JSON> jsonObjectAgg(JSONEntry<?> entry) {
        return new JSONObjectAgg<JSON>(JSON, entry);
    }

    /**
     * The JSONB object aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectAggNullStep<JSONB> jsonbObjectAgg(String key, Field<?> value) {
        return jsonbObjectAgg(Tools.field(key), value);
    }

    /**
     * The JSONB object aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectAggNullStep<JSONB> jsonbObjectAgg(Field<String> key, Field<?> value) {
        return jsonbObjectAgg(jsonEntry(key, value));
    }

    /**
     * The JSONB object aggregate function.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    public static JSONObjectAggNullStep<JSONB> jsonbObjectAgg(JSONEntry<?> entry) {
        return new JSONObjectAgg<JSONB>(JSONB, entry);
    }

    // -------------------------------------------------------------------------
    // XXX Aggregate functions
    // -------------------------------------------------------------------------

    /**
     * Get the count(*) function.
     */
    @Support
    public static AggregateFunction<Integer> count() {
        return count(DefaultAggregateFunction.ASTERISK);
    }

    /**
     * Get the count(field) function.
     */
    @Support
    public static AggregateFunction<Integer> count(Field<?> field) {
        return new DefaultAggregateFunction<>("count", SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the count(field) function.
     */
    @Support
    public static AggregateFunction<Integer> count(SelectFieldOrAsterisk field) {
        return new DefaultAggregateFunction<>("count", SQLDataType.INTEGER, field("{0}", field));
    }

    /**
     * Get the count(table) function.
     * <p>
     * If this is not supported by a given database (i.e. non
     * {@link SQLDialect#POSTGRES}, then the primary key is used with
     * {@link #count(Field)}, instead.
     */
    @Support
    public static AggregateFunction<Integer> count(Table<?> table) {
        return new CountTable(table, false);
    }

    /**
     * Get the count(distinct field) function.
     */
    @Support
    public static AggregateFunction<Integer> countDistinct(Field<?> field) {
        return new DefaultAggregateFunction<>(true, "count", SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the count(distinct field) function.
     */
    @Support
    public static AggregateFunction<Integer> countDistinct(SelectFieldOrAsterisk field) {
        return new DefaultAggregateFunction<>(true, "count", SQLDataType.INTEGER, field("{0}", field));
    }

    /**
     * Get the count(distinct table) function.
     * <p>
     * If this is not supported by a given database (i.e. non
     * {@link SQLDialect#POSTGRES}, then the primary key is used with
     * {@link #count(Field)}, instead.
     */
    @Support
    public static AggregateFunction<Integer> countDistinct(Table<?> table) {
        return new CountTable(table, true);
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
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<Integer> countDistinct(Field<?>... fields) {
        fields = nullSafe(fields);
        return fields.length == 0 ? countDistinct(asterisk()) : new DefaultAggregateFunction<>(true, "count", SQLDataType.INTEGER, fields);
    }

    /**
     * Get the every value over a field: every(field).
     * <p>
     * This is a synonym for {@link #boolAnd(Field)}.
     */
    @Support
    public static AggregateFunction<Boolean> every(Field<Boolean> field) {
        return boolAnd(field);
    }

    /**
     * Get the every value over a condition: every(condition).
     * <p>
     * This is a synonym for {@link #boolAnd(Condition)}.
     */
    @Support
    public static AggregateFunction<Boolean> every(Condition condition) {
        return boolAnd(condition);
    }

    /**
     * Get the every value over a field: bool_and(field).
     */
    @Support
    public static AggregateFunction<Boolean> boolAnd(Field<Boolean> field) {
        return boolAnd(condition(nullSafe(field)));
    }

    /**
     * Get the every value over a condition: bool_and(condition).
     */
    @Support
    public static AggregateFunction<Boolean> boolAnd(Condition condition) {
        return new BoolAnd(condition);
    }

    /**
     * Get the every value over a field: bool_and(field).
     */
    @Support
    public static AggregateFunction<Boolean> boolOr(Field<Boolean> field) {
        return boolOr(condition(nullSafe(field)));
    }

    /**
     * Get the every value over a condition: bool_and(condition).
     */
    @Support
    public static AggregateFunction<Boolean> boolOr(Condition condition) {
        return new BoolOr(condition);
    }

    /**
     * Get the <code>array_agg()</code> aggregate function.
     */
    @Support({ HSQLDB, POSTGRES })
    public static <T> ArrayAggOrderByStep<T[]> arrayAgg(Field<T> field) {
        return new ArrayAgg(false, nullSafe(field));
    }

    /**
     * Get the <code>array_agg()</code> aggregate function.
     */
    @Support({ HSQLDB, POSTGRES })
    public static <T> ArrayAggOrderByStep<T[]> arrayAggDistinct(Field<T> field) {
        return new ArrayAgg(true, nullSafe(field));
    }





























































    /**
     * Create an array literal.
     * <p>
     * This translates to the following databases and syntaxes:
     * <table>
     * <tr>
     * <th><code>SQLDialect</code></th>
     * <th>Java</th>
     * <th>SQL</th>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#H2}</td>
     * <td>array(1, 2)</td>
     * <td>(1, 2)</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#HSQLDB}, {@link SQLDialect#POSTGRES}</td>
     * <td>array(1, 2)</td>
     * <td>array[1, 2]</td>
     * </tr>
     * </table>
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> Field<T[]> array(T... values) {
        return array(Tools.fields(values));
    }

    /**
     * Create an array literal.
     * <p>
     * This translates to the following databases and syntaxes:
     * <table>
     * <tr>
     * <th><code>SQLDialect</code></th>
     * <th>Java</th>
     * <th>SQL</th>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#H2}</td>
     * <td>array(1, 2)</td>
     * <td>(1, 2)</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#HSQLDB}, {@link SQLDialect#POSTGRES}</td>
     * <td>array(1, 2)</td>
     * <td>array[1, 2]</td>
     * </tr>
     * </table>
     */
    @SafeVarargs
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> Field<T[]> array(Field<T>... fields) {
        return array(Arrays.asList(fields));
    }

    /**
     * Create an array literal.
     * <p>
     * This translates to the following databases and syntaxes:
     * <table>
     * <tr>
     * <th><code>SQLDialect</code></th>
     * <th>Java</th>
     * <th>SQL</th>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#H2}</td>
     * <td>array(1, 2)</td>
     * <td>(1, 2)</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#HSQLDB}, {@link SQLDialect#POSTGRES}</td>
     * <td>array(1, 2)</td>
     * <td>array[1, 2]</td>
     * </tr>
     * </table>
     */
    @Support({ H2, HSQLDB, POSTGRES })
    public static <T> Field<T[]> array(Collection<? extends Field<T>> fields) {
        return new Array<>(fields);
    }

    /**
     * Get the max value over a field: max(field).
     */
    @Support
    public static <T> AggregateFunction<T> max(Field<T> field) {
        return new DefaultAggregateFunction<>("max", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the max value over a field: max(distinct field).
     */
    @Support
    public static <T> AggregateFunction<T> maxDistinct(Field<T> field) {
        return new DefaultAggregateFunction<>(true, "max", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the min value over a field: min(field).
     */
    @Support
    public static <T> AggregateFunction<T> min(Field<T> field) {
        return new DefaultAggregateFunction<>("min", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the min value over a field: min(distinct field).
     */
    @Support
    public static <T> AggregateFunction<T> minDistinct(Field<T> field) {
        return new DefaultAggregateFunction<>(true, "min", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the sum over a numeric field: sum(field).
     */
    @Support
    public static AggregateFunction<BigDecimal> sum(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>("sum", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sum over a numeric field: sum(distinct field).
     */
    @Support
    public static AggregateFunction<BigDecimal> sumDistinct(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>(true, "sum", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the product over a numeric field: product(field).
     * <p>
     * No database currently supports multiplicative aggregation natively. jOOQ
     * emulates this using <code>exp(sum(log(arg)))</code> for strictly positive
     * numbers, and does some additional handling for zero and negative numbers.
     * <p>
     * Note that this implementation may introduce rounding errors, even for
     * integer multiplication.
     * <p>
     * More information here: <a href=
     * "https://blog.jooq.org/2018/09/21/how-to-write-a-multiplication-aggregate-function-in-sql">https://blog.jooq.org/2018/09/21/how-to-write-a-multiplication-aggregate-function-in-sql</a>.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> product(Field<? extends Number> field) {
        return new Product(false, nullSafe(field));
    }

    /**
     * Get the sum over a numeric field: product(distinct field).
     * <p>
     * No database currently supports multiplicative aggregation natively. jOOQ
     * emulates this using <code>exp(sum(log(arg)))</code> for strictly positive
     * numbers, and does some additional handling for zero and negative numbers.
     * <p>
     * Note that this implementation may introduce rounding errors, even for
     * integer multiplication.
     * <p>
     * More information here: <a href=
     * "https://blog.jooq.org/2018/09/21/how-to-write-a-multiplication-aggregate-function-in-sql">https://blog.jooq.org/2018/09/21/how-to-write-a-multiplication-aggregate-function-in-sql</a>.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> productDistinct(Field<? extends Number> field) {
        return new Product(true, nullSafe(field));
    }

    /**
     * Get the average over a numeric field: avg(field).
     */
    @Support
    public static AggregateFunction<BigDecimal> avg(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>("avg", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the average over a numeric field: avg(distinct field).
     */
    @Support
    public static AggregateFunction<BigDecimal> avgDistinct(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>(true, "avg", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * The <code>mode(field)</code> aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static <T> AggregateFunction<T> mode(Field<T> field) {
        return new Mode(nullSafe(field));
    }

    /**
     * Get the median over a numeric field: median(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, POSTGRES })
    public static AggregateFunction<BigDecimal> median(Field<? extends Number> field) {
        return new Median(nullSafe(field));
    }

    /**
     * Get the population standard deviation of a numeric field: stddev_pop(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> stddevPop(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>(Term.STDDEV_POP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sample standard deviation of a numeric field: stddev_samp(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> stddevSamp(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>(Term.STDDEV_SAMP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the population variance of a numeric field: var_pop(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> varPop(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>(Term.VAR_POP, SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sample variance of a numeric field: var_samp(field).
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    public static AggregateFunction<BigDecimal> varSamp(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>(Term.VAR_SAMP, SQLDataType.NUMERIC, nullSafe(field));
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
        return new DefaultAggregateFunction<>("regr_slope", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_intercept", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_count", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_r2", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_avgx", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_avgy", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_sxx", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_syy", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
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
        return new DefaultAggregateFunction<>("regr_sxy", SQLDataType.NUMERIC, nullSafe(y), nullSafe(x));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by {@link SQLDialect#ORACLE11G} upwards. It is
     * emulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#AURORA_MYSQL}: Using <code>GROUP_CONCAT</code></li>
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
        return new ListAgg(false, nullSafe(field));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by {@link SQLDialect#ORACLE11G} upwards. It is
     * emulated by the following dialects:
     * <ul>
     * <li> {@link SQLDialect#AURORA_MYSQL}: Using <code>GROUP_CONCAT</code></li>
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
        return new ListAgg(false, nullSafe(field), inline(separator));
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by
     * <ul>
     * <li> {@link SQLDialect#AURORA_MYSQL}</li>
     * <li> {@link SQLDialect#H2}</li>
     * <li> {@link SQLDialect#HSQLDB}</li>
     * <li> {@link SQLDialect#MEMSQL} (but without <code>ORDER BY</code>)</li>
     * <li> {@link SQLDialect#MYSQL}</li>
     * <li> {@link SQLDialect#SQLITE} (but without <code>ORDER BY</code>)</li>
     * </ul>
     * <p>
     * It is emulated by the following dialects:
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
     * <li>{@link SQLDialect#AURORA_MYSQL}</li>
     * <li>{@link SQLDialect#H2}</li>
     * <li>{@link SQLDialect#HSQLDB}</li>
     * <li>{@link SQLDialect#MEMSQL}</li>
     * <li>{@link SQLDialect#MYSQL}</li>
     * <li>{@link SQLDialect#SQLITE}</li>
     * </ul>
     * <p>
     * It is emulated by the following dialects:
     * <ul>
     * <li>{@link SQLDialect#DB2}: Using <code>XMLAGG()</code></li>
     * <li>{@link SQLDialect#ORACLE}: Using <code>LISTAGG()</code></li>
     * <li>{@link SQLDialect#POSTGRES}: Using <code>STRING_AGG()</code></li>
     * <li>{@link SQLDialect#SYBASE}: Using <code>LIST()</code></li>
     * </ul>
     *
     * @see #listAgg(Field)
     * @deprecated - [#7956] - 3.12.0 - Use {@link #groupConcat(Field)} and
     *             {@link GroupConcatSeparatorStep#separator(String)} instead.
     */
    @Deprecated
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static AggregateFunction<String> groupConcat(Field<?> field, String separator) {
        return new GroupConcat(nullSafe(field)).separator(separator);
    }

    /**
     * Get the aggregated concatenation for a field.
     * <p>
     * This is natively supported by
     * <ul>
     * <li> {@link SQLDialect#AURORA_MYSQL}</li>
     * <li> {@link SQLDialect#H2}</li>
     * <li> {@link SQLDialect#HSQLDB}</li>
     * <li> {@link SQLDialect#MYSQL}</li>
     * </ul>
     * <p>
     * It is emulated by the following dialects:
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
    // XXX Ordered-set aggregate functions and hypothetical set functions
    // -------------------------------------------------------------------------

    /**
     * The <code>mode() within group (oder by [order clause])</code> ordered
     * aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunctionOfDeferredType mode() {
        return new ModeDeferred();
    }

    /**
     * The <code>rank(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<Integer> rank(Field<?>... fields) {
        return new DefaultAggregateFunction<>("rank", SQLDataType.INTEGER, fields);
    }

    /**
     * The <code>rank(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<Integer> rank(Collection<? extends Field<?>> fields) {
        return new DefaultAggregateFunction<>("rank", SQLDataType.INTEGER, fields.toArray(EMPTY_FIELD));
    }

    /**
     * The <code>dense_rank(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<Integer> denseRank(Field<?>... fields) {
        return new DefaultAggregateFunction<>("dense_rank", SQLDataType.INTEGER, fields);
    }

    /**
     * The <code>dense_rank(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<Integer> denseRank(Collection<? extends Field<?>> fields) {
        return new DefaultAggregateFunction<>("dense_rank", SQLDataType.INTEGER, fields.toArray(EMPTY_FIELD));
    }

    /**
     * The <code>percent_rank(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<Integer> percentRank(Field<?>... fields) {
        return new DefaultAggregateFunction<>("percent_rank", SQLDataType.INTEGER, fields);
    }

    /**
     * The <code>percent_rank(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<Integer> percentRank(Collection<? extends Field<?>> fields) {
        return new DefaultAggregateFunction<>("percent_rank", SQLDataType.INTEGER, fields.toArray(EMPTY_FIELD));
    }

    /**
     * The <code>cume_dist(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<BigDecimal> cumeDist(Field<?>... fields) {
        return new DefaultAggregateFunction<>("cume_dist", SQLDataType.NUMERIC, fields);
    }

    /**
     * The <code>cume_dist(expr) within group (order by [order clause])</code>
     * ordered-set aggregate function.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<BigDecimal> cumeDist(Collection<? extends Field<?>> fields) {
        return new DefaultAggregateFunction<>("cume_dist", SQLDataType.NUMERIC, fields.toArray(EMPTY_FIELD));
    }

    /**
     * The
     * <code>percentile_cont([number]) within group (order by [column])</code>
     * function.
     * <p>
     * While {@link SQLDialect#ORACLE} and {@link SQLDialect#POSTGRES} support
     * this as an aggregate function, {@link SQLDialect#SQLSERVER} and
     * {@link SQLDialect#REDSHIFT} support only its window function variant.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<BigDecimal> percentileCont(Number number) {
        return percentileCont(val(number));
    }

    /**
     * The
     * <code>percentile_cont([number]) within group (order by [column])</code>
     * function.
     * <p>
     * While {@link SQLDialect#ORACLE} and {@link SQLDialect#POSTGRES} support
     * this as an aggregate function, {@link SQLDialect#SQLSERVER} and
     * {@link SQLDialect#REDSHIFT} support only its window function variant.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<BigDecimal> percentileCont(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>("percentile_cont", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * The
     * <code>percentile_disc([number]) within group (order by [column])</code>
     * function.
     * <p>
     * While {@link SQLDialect#ORACLE} and {@link SQLDialect#POSTGRES} support
     * this as an aggregate function, {@link SQLDialect#SQLSERVER} and
     * {@link SQLDialect#REDSHIFT} support only its window function variant.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<BigDecimal> percentileDisc(Number number) {
        return percentileDisc(val(number));
    }

    /**
     * The
     * <code>percentile_disc([number]) within group (order by [column])</code>
     * function.
     * <p>
     * While {@link SQLDialect#ORACLE} and {@link SQLDialect#POSTGRES} support
     * this as an aggregate function, {@link SQLDialect#SQLSERVER} and
     * {@link SQLDialect#REDSHIFT} support only its window function variant.
     */
    @Support({ H2, POSTGRES })
    public static OrderedAggregateFunction<BigDecimal> percentileDisc(Field<? extends Number> field) {
        return new DefaultAggregateFunction<>("percentile_disc", SQLDataType.NUMERIC, nullSafe(field));
    }

    // -------------------------------------------------------------------------
    // XXX Window clauses
    // -------------------------------------------------------------------------

    /**
     * Create a {@link WindowSpecification} with a <code>PARTITION BY</code> clause.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationOrderByStep partitionBy(Field<?>... fields) {
        return new WindowSpecificationImpl().partitionBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>PARTITION BY</code> clause.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationOrderByStep partitionBy(Collection<? extends Field<?>> fields) {
        return new WindowSpecificationImpl().partitionBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with an <code>ORDER BY</code> clause.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsStep orderBy(Field<?>... fields) {
        return new WindowSpecificationImpl().orderBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with an <code>ORDER BY</code> clause.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsStep orderBy(OrderField<?>... fields) {
        return new WindowSpecificationImpl().orderBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with an <code>ORDER BY</code> clause.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsStep orderBy(Collection<? extends OrderField<?>> fields) {
        return new WindowSpecificationImpl().orderBy(fields);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rowsUnboundedPreceding() {
        return new WindowSpecificationImpl().rowsUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rowsPreceding(int number) {
        return new WindowSpecificationImpl().rowsPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rowsCurrentRow() {
        return new WindowSpecificationImpl().rowsCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rowsUnboundedFollowing() {
        return new WindowSpecificationImpl().rowsUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rowsFollowing(int number) {
        return new WindowSpecificationImpl().rowsFollowing(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl().rowsBetweenUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rowsBetweenPreceding(int number) {
        return new WindowSpecificationImpl().rowsBetweenPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rowsBetweenCurrentRow() {
        return new WindowSpecificationImpl().rowsBetweenCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl().rowsBetweenUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>ROWS</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rowsBetweenFollowing(int number) {
        return new WindowSpecificationImpl().rowsBetweenFollowing(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rangeUnboundedPreceding() {
        return new WindowSpecificationImpl().rangeUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rangePreceding(int number) {
        return new WindowSpecificationImpl().rangePreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rangeCurrentRow() {
        return new WindowSpecificationImpl().rangeCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rangeUnboundedFollowing() {
        return new WindowSpecificationImpl().rangeUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep rangeFollowing(int number) {
        return new WindowSpecificationImpl().rangeFollowing(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rangeBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl().rangeBetweenUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rangeBetweenPreceding(int number) {
        return new WindowSpecificationImpl().rangeBetweenPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rangeBetweenCurrentRow() {
        return new WindowSpecificationImpl().rangeBetweenCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rangeBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl().rangeBetweenUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>RANGE</code> clause.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep rangeBetweenFollowing(int number) {
        return new WindowSpecificationImpl().rangeBetweenFollowing(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep groupsUnboundedPreceding() {
        return new WindowSpecificationImpl().groupsUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep groupsPreceding(int number) {
        return new WindowSpecificationImpl().groupsPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep groupsCurrentRow() {
        return new WindowSpecificationImpl().groupsCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep groupsUnboundedFollowing() {
        return new WindowSpecificationImpl().groupsUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationExcludeStep groupsFollowing(int number) {
        return new WindowSpecificationImpl().groupsFollowing(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep groupsBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl().groupsBetweenUnboundedPreceding();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep groupsBetweenPreceding(int number) {
        return new WindowSpecificationImpl().groupsBetweenPreceding(number);
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep groupsBetweenCurrentRow() {
        return new WindowSpecificationImpl().groupsBetweenCurrentRow();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep groupsBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl().groupsBetweenUnboundedFollowing();
    }

    /**
     * Create a {@link WindowSpecification} with a <code>GROUPS</code> clause.
     */
    @Support({ H2, POSTGRES, SQLITE })
    public static WindowSpecificationRowsAndStep groupsBetweenFollowing(int number) {
        return new WindowSpecificationImpl().groupsBetweenFollowing(number);
    }

    // -------------------------------------------------------------------------
    // XXX Window functions
    // -------------------------------------------------------------------------

    /**
     * The <code>row_number() over ([analytic clause])</code> function.
     * <p>
     * Newer versions of {@link SQLDialect#DERBY} and {@link SQLDialect#H2} also
     * support the <code>ROW_NUMBER() OVER()</code> window function without any
     * window clause. See the respective docs for details.
     * {@link SQLDialect#HSQLDB} can emulate this function using
     * <code>ROWNUM()</code>
     */
    @Support
    public static WindowOverStep<Integer> rowNumber() {
        return new RowNumber();
    }

    /**
     * The <code>rank() over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<Integer> rank() {
        return new RankingFunction<>(RANK, SQLDataType.INTEGER);
    }

    /**
     * The <code>dense_rank() over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<Integer> denseRank() {
        return new RankingFunction<>(DENSE_RANK, SQLDataType.INTEGER);
    }

    /**
     * The <code>precent_rank() over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<BigDecimal> percentRank() {
        return new RankingFunction<>(PERCENT_RANK, SQLDataType.NUMERIC);
    }

    /**
     * The <code>cume_dist() over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<BigDecimal> cumeDist() {
        return new RankingFunction<>(CUME_DIST, SQLDataType.NUMERIC);
    }

    /**
     * The <code>ntile([number]) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<Integer> ntile(int number) {
        return new Ntile(inline(number));
    }

    /**
     * The <code>ratio_to_report([expression]) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<BigDecimal> ratioToReport(Number number) {
        return ratioToReport(Tools.field(number));
    }

    /**
     * The <code>ratio_to_report([expression]) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static WindowOverStep<BigDecimal> ratioToReport(Field<? extends Number> field) {
        return new RatioToReport(nullSafe(field));
    }

    /**
     * The <code>first_value(field) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> firstValue(Field<T> field) {
        return new PositionalWindowFunction(FIRST_VALUE, nullSafe(field));
    }

    /**
     * The <code>last_value(field) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lastValue(Field<T> field) {
        return new PositionalWindowFunction(LAST_VALUE, nullSafe(field));
    }

    /**
     * The <code>nth_value(field) over ([analytic clause])</code> function.
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowFromFirstLastStep<T> nthValue(Field<T> field, int nth) {
        return nthValue(field, val(nth));
    }

    /**
     * The <code>nth_value(field) over ([analytic clause])</code> function.
     */
    @Support({ FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowFromFirstLastStep<T> nthValue(Field<T> field, Field<Integer> nth) {
        return new PositionalWindowFunction(NTH_VALUE, nullSafe(field), nullSafe(nth), null);
    }

    /**
     * The <code>lead(field) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field) {
        return new PositionalWindowFunction(LEAD, nullSafe(field));
    }

    /**
     * The <code>lead(field, offset) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset) {
        return new PositionalWindowFunction(LEAD, nullSafe(field), inline(offset), null);
    }

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset, T defaultValue) {
        return lead(nullSafe(field), offset, Tools.field(defaultValue, field));
    }

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset, Field<T> defaultValue) {
        return new PositionalWindowFunction(LEAD, nullSafe(field), inline(offset), nullSafe(defaultValue));
    }

    /**
     * The <code>lag(field) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field) {
        return new PositionalWindowFunction(LAG, nullSafe(field));
    }

    /**
     * The <code>lag(field, offset) over ([analytic clause])</code> function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset) {
        return new PositionalWindowFunction(LAG, nullSafe(field), inline(offset), null);
    }

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset, T defaultValue) {
        return lag(nullSafe(field), offset, Tools.field(defaultValue, field));
    }

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     */
    @Support({ CUBRID, FIREBIRD, H2, MYSQL, POSTGRES, SQLITE })
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset, Field<T> defaultValue) {
        return new PositionalWindowFunction(LAG, nullSafe(field), inline(offset), nullSafe(defaultValue));
    }

    // -------------------------------------------------------------------------
    // XXX Bind values
    // -------------------------------------------------------------------------

    /**
     * Create an unnamed parameter with a generic type ({@link Object} /
     * {@link SQLDataType#OTHER}) and no initial value.
     * <p>
     * Try to avoid this method when using any of these databases, as these
     * databases may have trouble inferring the type of the bind value. Use
     * typed named parameters instead, using {@link #param(Class)} or
     * {@link #param(DataType)}
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
    public static <T> Param<Object> param() {
        return param(Object.class);
    }

    /**
     * Create an unnamed parameter with a defined type and no initial value.
     *
     * @see #param(String, Object)
     */
    @Support
    public static <T> Param<T> param(Class<T> type) {
        return param(DefaultDataType.getDataType(null, type));
    }

    /**
     * Create an unnamed parameter with a defined type and no initial value.
     *
     * @see #param(String, Object)
     */
    @Support
    public static <T> Param<T> param(DataType<T> type) {
        return new Val<>(null, type);
    }

    /**
     * Create an unnamed parameter with the defined type of another field and no
     * initial value.
     *
     * @see #param(String, Object)
     */
    @Support
    public static <T> Param<T> param(Field<T> field) {
        return param(field.getDataType());
    }

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
        return new Val<>(null, type, name);
    }

    /**
     * Create a named parameter with a defined type of another field and no
     * initial value.
     *
     * @see #param(String, Object)
     */
    @Support
    public static <T> Param<T> param(String name, Field<T> type) {
        return param(name, type.getDataType());
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
        return new Val<>(value, Tools.field(value).getDataType(), name);
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
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Byte> value(byte value) {
        return value((Object) value, SQLDataType.TINYINT);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Byte> value(Byte value) {
        return value((Object) value, SQLDataType.TINYINT);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UByte> value(UByte value) {
        return value((Object) value, SQLDataType.TINYINTUNSIGNED);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Short> value(short value) {
        return value((Object) value, SQLDataType.SMALLINT);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Short> value(Short value) {
        return value((Object) value, SQLDataType.SMALLINT);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UShort> value(UShort value) {
        return value((Object) value, SQLDataType.SMALLINTUNSIGNED);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Integer> value(int value) {
        return value((Object) value, SQLDataType.INTEGER);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Integer> value(Integer value) {
        return value((Object) value, SQLDataType.INTEGER);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UInteger> value(UInteger value) {
        return value((Object) value, SQLDataType.INTEGERUNSIGNED);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Long> value(long value) {
        return value((Object) value, SQLDataType.BIGINT);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Long> value(Long value) {
        return value((Object) value, SQLDataType.BIGINT);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<ULong> value(ULong value) {
        return value((Object) value, SQLDataType.BIGINTUNSIGNED);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Float> value(float value) {
        return value((Object) value, SQLDataType.REAL);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Float> value(Float value) {
        return value((Object) value, SQLDataType.REAL);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Double> value(double value) {
        return value((Object) value, SQLDataType.DOUBLE);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Double> value(Double value) {
        return value((Object) value, SQLDataType.DOUBLE);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Boolean> value(boolean value) {
        return value((Object) value, SQLDataType.BOOLEAN);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Boolean> value(Boolean value) {
        return value((Object) value, SQLDataType.BOOLEAN);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<BigDecimal> value(BigDecimal value) {
        return value((Object) value, SQLDataType.DECIMAL);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<BigInteger> value(BigInteger value) {
        return value((Object) value, SQLDataType.DECIMAL_INTEGER);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<byte[]> value(byte[] value) {
        return value((Object) value, SQLDataType.VARBINARY);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<String> value(String value) {
        return value((Object) value, SQLDataType.VARCHAR);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Date> value(Date value) {
        return value((Object) value, SQLDataType.DATE);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Time> value(Time value) {
        return value((Object) value, SQLDataType.TIME);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Timestamp> value(Timestamp value) {
        return value((Object) value, SQLDataType.TIMESTAMP);
    }


    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<LocalDate> value(LocalDate value) {
        return value((Object) value, SQLDataType.LOCALDATE);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<LocalTime> value(LocalTime value) {
        return value((Object) value, SQLDataType.LOCALTIME);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<LocalDateTime> value(LocalDateTime value) {
        return value((Object) value, SQLDataType.LOCALDATETIME);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<OffsetTime> value(OffsetTime value) {
        return value((Object) value, SQLDataType.OFFSETTIME);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<OffsetDateTime> value(OffsetDateTime value) {
        return value((Object) value, SQLDataType.OFFSETDATETIME);
    }

    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Instant> value(Instant value) {
        return value((Object) value, SQLDataType.INSTANT);
    }


    /**
     * A synonym for {@link #val(Object)} to be used in Scala and Groovy, where
     * <code>val</code> is a reserved keyword.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UUID> value(UUID value) {
        return value((Object) value, SQLDataType.UUID);
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
    @SuppressWarnings("deprecation")
    @Support
    public static <T> Param<T> inline(T value) {
        Param<T> val = val(value);
        val.setInline(true);
        return val;
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Byte> inline(byte value) {
        return inline((Object) value, SQLDataType.TINYINT);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Byte> inline(Byte value) {
        return inline((Object) value, SQLDataType.TINYINT);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<UByte> inline(UByte value) {
        return inline((Object) value, SQLDataType.TINYINTUNSIGNED);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Short> inline(short value) {
        return inline((Object) value, SQLDataType.SMALLINT);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Short> inline(Short value) {
        return inline((Object) value, SQLDataType.SMALLINT);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<UShort> inline(UShort value) {
        return inline((Object) value, SQLDataType.SMALLINTUNSIGNED);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Integer> inline(int value) {
        return inline((Object) value, SQLDataType.INTEGER);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Integer> inline(Integer value) {
        return inline((Object) value, SQLDataType.INTEGER);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<UInteger> inline(UInteger value) {
        return inline((Object) value, SQLDataType.INTEGERUNSIGNED);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Long> inline(long value) {
        return inline((Object) value, SQLDataType.BIGINT);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Long> inline(Long value) {
        return inline((Object) value, SQLDataType.BIGINT);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<ULong> inline(ULong value) {
        return inline((Object) value, SQLDataType.BIGINTUNSIGNED);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Float> inline(float value) {
        return inline((Object) value, SQLDataType.REAL);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Float> inline(Float value) {
        return inline((Object) value, SQLDataType.REAL);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Double> inline(double value) {
        return inline((Object) value, SQLDataType.DOUBLE);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Double> inline(Double value) {
        return inline((Object) value, SQLDataType.DOUBLE);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Boolean> inline(boolean value) {
        return inline((Object) value, SQLDataType.BOOLEAN);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Boolean> inline(Boolean value) {
        return inline((Object) value, SQLDataType.BOOLEAN);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<BigDecimal> inline(BigDecimal value) {
        return inline((Object) value, SQLDataType.DECIMAL);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<BigInteger> inline(BigInteger value) {
        return inline((Object) value, SQLDataType.DECIMAL_INTEGER);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<byte[]> inline(byte[] value) {
        return inline((Object) value, SQLDataType.VARBINARY);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<String> inline(String value) {
        return inline((Object) value, SQLDataType.VARCHAR);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Date> inline(Date value) {
        return inline((Object) value, SQLDataType.DATE);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Time> inline(Time value) {
        return inline((Object) value, SQLDataType.TIME);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Timestamp> inline(Timestamp value) {
        return inline((Object) value, SQLDataType.TIMESTAMP);
    }


    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<LocalDate> inline(LocalDate value) {
        return inline((Object) value, SQLDataType.LOCALDATE);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<LocalTime> inline(LocalTime value) {
        return inline((Object) value, SQLDataType.LOCALTIME);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<LocalDateTime> inline(LocalDateTime value) {
        return inline((Object) value, SQLDataType.LOCALDATETIME);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<OffsetTime> inline(OffsetTime value) {
        return inline((Object) value, SQLDataType.OFFSETTIME);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<OffsetDateTime> inline(OffsetDateTime value) {
        return inline((Object) value, SQLDataType.OFFSETDATETIME);
    }

    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<Instant> inline(Instant value) {
        return inline((Object) value, SQLDataType.INSTANT);
    }


    /**
     * Create a bind value that is always inlined.
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
     * @see #inline(Object)
     * @see #val(Object)
     */
    @Support
    public static Param<UUID> inline(UUID value) {
        return inline((Object) value, SQLDataType.UUID);
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
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
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
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Byte> val(byte value) {
        return val((Object) value, SQLDataType.TINYINT);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Byte> val(Byte value) {
        return val((Object) value, SQLDataType.TINYINT);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UByte> val(UByte value) {
        return val((Object) value, SQLDataType.TINYINTUNSIGNED);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Short> val(short value) {
        return val((Object) value, SQLDataType.SMALLINT);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Short> val(Short value) {
        return val((Object) value, SQLDataType.SMALLINT);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UShort> val(UShort value) {
        return val((Object) value, SQLDataType.SMALLINTUNSIGNED);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Integer> val(int value) {
        return val((Object) value, SQLDataType.INTEGER);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Integer> val(Integer value) {
        return val((Object) value, SQLDataType.INTEGER);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UInteger> val(UInteger value) {
        return val((Object) value, SQLDataType.INTEGERUNSIGNED);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Long> val(long value) {
        return val((Object) value, SQLDataType.BIGINT);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Long> val(Long value) {
        return val((Object) value, SQLDataType.BIGINT);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<ULong> val(ULong value) {
        return val((Object) value, SQLDataType.BIGINTUNSIGNED);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Float> val(float value) {
        return val((Object) value, SQLDataType.REAL);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Float> val(Float value) {
        return val((Object) value, SQLDataType.REAL);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Double> val(double value) {
        return val((Object) value, SQLDataType.DOUBLE);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Double> val(Double value) {
        return val((Object) value, SQLDataType.DOUBLE);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Boolean> val(boolean value) {
        return val((Object) value, SQLDataType.BOOLEAN);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Boolean> val(Boolean value) {
        return val((Object) value, SQLDataType.BOOLEAN);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<BigDecimal> val(BigDecimal value) {
        return val((Object) value, SQLDataType.DECIMAL);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<BigInteger> val(BigInteger value) {
        return val((Object) value, SQLDataType.DECIMAL_INTEGER);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<byte[]> val(byte[] value) {
        return val((Object) value, SQLDataType.VARBINARY);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<String> val(String value) {
        return val((Object) value, SQLDataType.VARCHAR);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Date> val(Date value) {
        return val((Object) value, SQLDataType.DATE);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Time> val(Time value) {
        return val((Object) value, SQLDataType.TIME);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Timestamp> val(Timestamp value) {
        return val((Object) value, SQLDataType.TIMESTAMP);
    }


    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<LocalDate> val(LocalDate value) {
        return val((Object) value, SQLDataType.LOCALDATE);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<LocalTime> val(LocalTime value) {
        return val((Object) value, SQLDataType.LOCALTIME);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<LocalDateTime> val(LocalDateTime value) {
        return val((Object) value, SQLDataType.LOCALDATETIME);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<OffsetTime> val(OffsetTime value) {
        return val((Object) value, SQLDataType.OFFSETTIME);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<OffsetDateTime> val(OffsetDateTime value) {
        return val((Object) value, SQLDataType.OFFSETDATETIME);
    }

    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<Instant> val(Instant value) {
        return val((Object) value, SQLDataType.INSTANT);
    }


    /**
     * Get a bind value.
     *
     * @see #val(Object)
     */
    @Support
    public static Param<UUID> val(UUID value) {
        return val((Object) value, SQLDataType.UUID);
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






        // The default behaviour
        else {
            T converted = type.convert(value);
            return new Val<>(converted, mostSpecific(converted, type));
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
    @SuppressWarnings("deprecation")
    private static <T> DataType<T> mostSpecific(T value, DataType<T> dataType) {

        // [#3888] With custom data type conversion, users may wish to
        // allow for a less specific data type than the actual value. Example:
        //   data type: Serializable
        //   value    : byte[]
        // [#3889] TODO: Improve this once DataType.getBinding() is available

        if (value != null && !(dataType instanceof ConvertedDataType)) {
            Class<T> valueType = (Class<T>) value.getClass();
            Class<T> coercionType = dataType.getType();

            if (valueType != coercionType && coercionType.isAssignableFrom(valueType)) {
                return DefaultDataType.getDataType(null, valueType, dataType);
            }
        }

        return dataType;
    }

    /**
     * Create a {@link RecordType} of an arbitrary degree.
     */
    public static <T1> RecordType<Record> recordType(Field<?>[] fields) {
        return new Fields(fields);
    }

    /**
     * Create a {@link RecordType} of an arbitrary degree.
     */
    public static <T1> RecordType<Record> recordType(Collection<? extends Field<?>> fields) {
        return new Fields(fields);
    }



    /**
     * Create a {@link RecordType} of degree <code>1</code>.
     */
    public static <T1> RecordType<Record1<T1>> recordType(Field<T1> field1) {
        return new Fields(field1);
    }

    /**
     * Create a {@link RecordType} of degree <code>2</code>.
     */
    public static <T1, T2> RecordType<Record2<T1, T2>> recordType(Field<T1> field1, Field<T2> field2) {
        return new Fields(field1, field2);
    }

    /**
     * Create a {@link RecordType} of degree <code>3</code>.
     */
    public static <T1, T2, T3> RecordType<Record3<T1, T2, T3>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return new Fields(field1, field2, field3);
    }

    /**
     * Create a {@link RecordType} of degree <code>4</code>.
     */
    public static <T1, T2, T3, T4> RecordType<Record4<T1, T2, T3, T4>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return new Fields(field1, field2, field3, field4);
    }

    /**
     * Create a {@link RecordType} of degree <code>5</code>.
     */
    public static <T1, T2, T3, T4, T5> RecordType<Record5<T1, T2, T3, T4, T5>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return new Fields(field1, field2, field3, field4, field5);
    }

    /**
     * Create a {@link RecordType} of degree <code>6</code>.
     */
    public static <T1, T2, T3, T4, T5, T6> RecordType<Record6<T1, T2, T3, T4, T5, T6>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return new Fields(field1, field2, field3, field4, field5, field6);
    }

    /**
     * Create a {@link RecordType} of degree <code>7</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7> RecordType<Record7<T1, T2, T3, T4, T5, T6, T7>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7);
    }

    /**
     * Create a {@link RecordType} of degree <code>8</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> RecordType<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8);
    }

    /**
     * Create a {@link RecordType} of degree <code>9</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> RecordType<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    /**
     * Create a {@link RecordType} of degree <code>10</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> RecordType<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    /**
     * Create a {@link RecordType} of degree <code>11</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> RecordType<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    /**
     * Create a {@link RecordType} of degree <code>12</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> RecordType<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    /**
     * Create a {@link RecordType} of degree <code>13</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> RecordType<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    /**
     * Create a {@link RecordType} of degree <code>14</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> RecordType<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    /**
     * Create a {@link RecordType} of degree <code>15</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> RecordType<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    /**
     * Create a {@link RecordType} of degree <code>16</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> RecordType<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    /**
     * Create a {@link RecordType} of degree <code>17</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> RecordType<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    /**
     * Create a {@link RecordType} of degree <code>18</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> RecordType<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    /**
     * Create a {@link RecordType} of degree <code>19</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> RecordType<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    /**
     * Create a {@link RecordType} of degree <code>20</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> RecordType<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    /**
     * Create a {@link RecordType} of degree <code>21</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> RecordType<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    /**
     * Create a {@link RecordType} of degree <code>22</code>.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> RecordType<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> recordType(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return new Fields(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
    }





    /**
     * Create a row value expression of degree <code>1</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1> Row1<T1> row(T1 t1) {
        return row(Tools.field(t1));
    }

    /**
     * Create a row value expression of degree <code>2</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2> Row2<T1, T2> row(T1 t1, T2 t2) {
        return row(Tools.field(t1), Tools.field(t2));
    }

    /**
     * Create a row value expression of degree <code>3</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3> Row3<T1, T2, T3> row(T1 t1, T2 t2, T3 t3) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3));
    }

    /**
     * Create a row value expression of degree <code>4</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4> Row4<T1, T2, T3, T4> row(T1 t1, T2 t2, T3 t3, T4 t4) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4));
    }

    /**
     * Create a row value expression of degree <code>5</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5> Row5<T1, T2, T3, T4, T5> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5));
    }

    /**
     * Create a row value expression of degree <code>6</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6> Row6<T1, T2, T3, T4, T5, T6> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6));
    }

    /**
     * Create a row value expression of degree <code>7</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> Row7<T1, T2, T3, T4, T5, T6, T7> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7));
    }

    /**
     * Create a row value expression of degree <code>8</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Row8<T1, T2, T3, T4, T5, T6, T7, T8> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8));
    }

    /**
     * Create a row value expression of degree <code>9</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9));
    }

    /**
     * Create a row value expression of degree <code>10</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10));
    }

    /**
     * Create a row value expression of degree <code>11</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11));
    }

    /**
     * Create a row value expression of degree <code>12</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12));
    }

    /**
     * Create a row value expression of degree <code>13</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13));
    }

    /**
     * Create a row value expression of degree <code>14</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14));
    }

    /**
     * Create a row value expression of degree <code>15</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15));
    }

    /**
     * Create a row value expression of degree <code>16</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16));
    }

    /**
     * Create a row value expression of degree <code>17</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16), Tools.field(t17));
    }

    /**
     * Create a row value expression of degree <code>18</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16), Tools.field(t17), Tools.field(t18));
    }

    /**
     * Create a row value expression of degree <code>19</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16), Tools.field(t17), Tools.field(t18), Tools.field(t19));
    }

    /**
     * Create a row value expression of degree <code>20</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16), Tools.field(t17), Tools.field(t18), Tools.field(t19), Tools.field(t20));
    }

    /**
     * Create a row value expression of degree <code>21</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16), Tools.field(t17), Tools.field(t18), Tools.field(t19), Tools.field(t20), Tools.field(t21));
    }

    /**
     * Create a row value expression of degree <code>22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return row(Tools.field(t1), Tools.field(t2), Tools.field(t3), Tools.field(t4), Tools.field(t5), Tools.field(t6), Tools.field(t7), Tools.field(t8), Tools.field(t9), Tools.field(t10), Tools.field(t11), Tools.field(t12), Tools.field(t13), Tools.field(t14), Tools.field(t15), Tools.field(t16), Tools.field(t17), Tools.field(t18), Tools.field(t19), Tools.field(t20), Tools.field(t21), Tools.field(t22));
    }



    /**
     * Create a row value expression of degree <code>N &gt; 22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static RowN row(Object... values) {
        return row(Tools.fields(values).toArray(EMPTY_FIELD));
    }



    /**
     * Create a row value expression of degree <code>1</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1> Row1<T1> row(Field<T1> t1) {
        return new RowImpl1<>(t1);
    }

    /**
     * Create a row value expression of degree <code>2</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2> Row2<T1, T2> row(Field<T1> t1, Field<T2> t2) {
        return new RowImpl2<>(t1, t2);
    }

    /**
     * Create a row value expression of degree <code>3</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3> Row3<T1, T2, T3> row(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return new RowImpl3<>(t1, t2, t3);
    }

    /**
     * Create a row value expression of degree <code>4</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4> Row4<T1, T2, T3, T4> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return new RowImpl4<>(t1, t2, t3, t4);
    }

    /**
     * Create a row value expression of degree <code>5</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5> Row5<T1, T2, T3, T4, T5> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return new RowImpl5<>(t1, t2, t3, t4, t5);
    }

    /**
     * Create a row value expression of degree <code>6</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6> Row6<T1, T2, T3, T4, T5, T6> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return new RowImpl6<>(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Create a row value expression of degree <code>7</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7> Row7<T1, T2, T3, T4, T5, T6, T7> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return new RowImpl7<>(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Create a row value expression of degree <code>8</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Row8<T1, T2, T3, T4, T5, T6, T7, T8> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return new RowImpl8<>(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    /**
     * Create a row value expression of degree <code>9</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return new RowImpl9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    /**
     * Create a row value expression of degree <code>10</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return new RowImpl10<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Create a row value expression of degree <code>11</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return new RowImpl11<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    /**
     * Create a row value expression of degree <code>12</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return new RowImpl12<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    /**
     * Create a row value expression of degree <code>13</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return new RowImpl13<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    /**
     * Create a row value expression of degree <code>14</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return new RowImpl14<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    /**
     * Create a row value expression of degree <code>15</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return new RowImpl15<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    /**
     * Create a row value expression of degree <code>16</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return new RowImpl16<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Create a row value expression of degree <code>17</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return new RowImpl17<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    /**
     * Create a row value expression of degree <code>18</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return new RowImpl18<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    /**
     * Create a row value expression of degree <code>19</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return new RowImpl19<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    /**
     * Create a row value expression of degree <code>20</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return new RowImpl20<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    /**
     * Create a row value expression of degree <code>21</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return new RowImpl21<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    /**
     * Create a row value expression of degree <code>22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return new RowImpl22<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }



    /**
     * Create a row value expression of degree <code>N &gt; 22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static RowN row(Field<?>... values) {
        return new RowImplN(values);
    }

    /**
     * Create a row value expression of degree <code>N &gt; 22</code>.
     * <p>
     * Note: Not all databases support row value expressions, but many row value
     * expression operations can be emulated on all databases. See relevant row
     * value expression method Javadocs for details.
     */
    @Support
    public static RowN row(Collection<?> values) {
        Collection<Field<?>> fields = new ArrayList<>(values.size());

        for (Object o : values)
            fields.add(o instanceof Field<?> ? (Field<?>) o : val(o));

        return new RowImplN(fields);
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
        return values0(rows);
    }

    /**
     * Internal values constructor.
     * <p>
     * [#6003] TODO: Make this public
     */
    @Support
    static Table<Record> values0(Row... rows) {
        Values.assertNotEmpty(rows);
        int size = rows[0].size();

        String[] columns = new String[size];

        for (int i = 0; i < size; i++)
            columns[i] = "c" + (i + 1);

        return new Values<Record>(rows).as("v", columns);
    }



    /**
     * Create a <code>VALUES()</code> expression of degree <code>1</code>.
     * <p>
     * The <code>VALUES()</code> constructor is a tool supported by some
     * databases to allow for constructing tables from constant values.
     * <p>
     * If a database doesn't support the <code>VALUES()</code> constructor, it
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
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
     * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
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
    @SafeVarargs
    @Support
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Table<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> values(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... rows) {
        return new Values<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>(rows).as("v", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19", "c20", "c21", "c22");
    }



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
     * Get the null field.
     */
    static <T> Field<T> NULL(Field<T> field) {
        return NULL(field.getDataType());
    }

    /**
     * Get the null field.
     */
    static <T> Field<T> NULL(DataType<T> type) {
        return field("null", type);
    }

    /**
     * Get the null field.
     */
    static <T> Field<T> NULL(Class<T> type) {
        return field("null", type);
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
    protected static <T> Field<T> nullSafe(Field<T> field, DataType<?> type) {
        return field == null ? (Field<T>) val((T) null, type) : field;
    }

    /**
     * Null-safety of a field.
     */
    protected static Field<?>[] nullSafe(Field<?>... fields) {
        if (fields == null)
            return EMPTY_FIELD;

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
     * The asterisk (<code>*</code>) to be used in <code>SELECT</code> clauses.
     */
    @Support
    public static Asterisk asterisk() {
        return AsteriskImpl.INSTANCE;
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
    public static Param<Integer> zero() {
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
    public static Param<Integer> one() {
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
    public static Param<Integer> two() {
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
     * Get the <code>current_user()</code> function.
     */
    @Support
    public static Field<String> currentUser() {
        return new CurrentUser();
    }

    /**
     * Get the <code>current_schema()</code> function.
     */
    @Support
    public static Field<String> currentSchema() {
        return new CurrentSchema();
    }

    @Support
    public static <T extends Number> Field<T> widthBucket(Field<T> field, T low, T high, int buckets) {
        return widthBucket(field, Tools.field(low, field), Tools.field(high, field), Tools.field(buckets));
    }

    @Support
    public static <T extends Number> Field<T> widthBucket(Field<T> field, Field<T> low, Field<T> high, Field<Integer> buckets) {
        return new WidthBucket<>(field, low, high, buckets);
    }

    // -------------------------------------------------------------------------
    // XXX utility API
    // -------------------------------------------------------------------------

    /**
     * Get the default data type for the {@link DSLContext}'s underlying
     * {@link SQLDialect} and a given Java type.
     *
     * @param <T> The generic type
     * @param type The Java type
     * @return The <code>DSL</code>'s underlying default data type.
     * @deprecated - 3.11.0 - [#7483] - The (indirect) use of the internal
     *             static data type registry is not recommended.
     */
    @Deprecated
    @Support
    public static <T> DataType<T> getDataType(Class<T> type) {
        return DefaultDataType.getDataType(SQLDialect.DEFAULT, type);
    }

    private static final DSLContext dsl() {
        return using(new DefaultConfiguration());
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
