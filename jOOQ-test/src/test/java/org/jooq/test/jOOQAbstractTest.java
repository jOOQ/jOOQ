/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.test.all.listeners.ConnectionProviderLifecycleListener.ACQUIRE_COUNT;
import static org.jooq.test.all.listeners.ConnectionProviderLifecycleListener.RELEASE_COUNT;
import static org.jooq.test.all.listeners.JDBCLifecycleListener.RS_CLOSE_COUNT;
import static org.jooq.test.all.listeners.JDBCLifecycleListener.RS_START_COUNT;
import static org.jooq.test.all.listeners.JDBCLifecycleListener.STMT_CLOSE_COUNT;
import static org.jooq.test.all.listeners.JDBCLifecycleListener.STMT_START_COUNT;
import static org.jooq.test.all.listeners.LifecycleWatcherListener.LISTENER_END_COUNT;
import static org.jooq.test.all.listeners.LifecycleWatcherListener.LISTENER_START_COUNT;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isBlank;
import static org.jooq.tools.reflect.Reflect.on;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.jooq.AggregateFunction;
import org.jooq.ArrayRecord;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.ExecuteType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.all.listeners.ConnectionProviderLifecycleListener;
import org.jooq.test.all.listeners.JDBCLifecycleListener;
import org.jooq.test.all.listeners.Lifecycle;
import org.jooq.test.all.listeners.LifecycleWatcherListener;
import org.jooq.test.all.listeners.PrettyPrinter;
import org.jooq.test.all.listeners.TestStatisticsListener;
import org.jooq.test.all.pojos.jaxb.JAXBBook;
import org.jooq.test.all.testcases.AggregateWindowFunctionTests;
import org.jooq.test.all.testcases.AliasTests;
import org.jooq.test.all.testcases.AsyncTest;
import org.jooq.test.all.testcases.BatchTests;
import org.jooq.test.all.testcases.BenchmarkTests;
import org.jooq.test.all.testcases.CRUDTests;
import org.jooq.test.all.testcases.CTETests;
import org.jooq.test.all.testcases.CollationTests;
import org.jooq.test.all.testcases.ConnectionProviderTests;
import org.jooq.test.all.testcases.CsvLoaderTests;
import org.jooq.test.all.testcases.DDLTests;
import org.jooq.test.all.testcases.DaoTests;
import org.jooq.test.all.testcases.DataTypeTests;
import org.jooq.test.all.testcases.EnumTests;
import org.jooq.test.all.testcases.ExecuteListenerTests;
import org.jooq.test.all.testcases.ExoticTests;
import org.jooq.test.all.testcases.FetchTests;
import org.jooq.test.all.testcases.FormatTests;
import org.jooq.test.all.testcases.FunctionTests;
import org.jooq.test.all.testcases.GeneralTests;
import org.jooq.test.all.testcases.GroupByTests;
import org.jooq.test.all.testcases.InsertUpdateTests;
import org.jooq.test.all.testcases.JDBCTests;
import org.jooq.test.all.testcases.JPAIntegrationTests;
import org.jooq.test.all.testcases.JoinTests;
import org.jooq.test.all.testcases.JsonLoaderTests;
import org.jooq.test.all.testcases.MetaDataTests;
import org.jooq.test.all.testcases.MockTests;
import org.jooq.test.all.testcases.OrderByTests;
import org.jooq.test.all.testcases.PlainSQLTests;
import org.jooq.test.all.testcases.PredicateTests;
import org.jooq.test.all.testcases.RecordListenerTests;
import org.jooq.test.all.testcases.RecordLoaderTests;
import org.jooq.test.all.testcases.RecordMapperTests;
import org.jooq.test.all.testcases.RecordTests;
import org.jooq.test.all.testcases.ReferentialTests;
import org.jooq.test.all.testcases.RenderAndBindTests;
import org.jooq.test.all.testcases.ResultSetTests;
import org.jooq.test.all.testcases.ResultTests;
import org.jooq.test.all.testcases.RoutineAndUDTTests;
import org.jooq.test.all.testcases.RowLoaderTests;
import org.jooq.test.all.testcases.RowValueExpressionTests;
import org.jooq.test.all.testcases.SchemaAndMappingTests;
import org.jooq.test.all.testcases.SelectTests;
import org.jooq.test.all.testcases.SerializationTests;
import org.jooq.test.all.testcases.StatementTests;
import org.jooq.test.all.testcases.StreamsTest;
import org.jooq.test.all.testcases.TableFunctionTests;
import org.jooq.test.all.testcases.ThreadSafetyTests;
import org.jooq.test.all.testcases.TransactionTests;
import org.jooq.test.all.testcases.TruncateTests;
import org.jooq.test.all.testcases.UnionTests;
import org.jooq.test.all.testcases.ValuesConstructorTests;
import org.jooq.test.all.testcases.VisitListenerTests;
import org.jooq.test.utils.LoggingConnection;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockResult;
import org.jooq.tools.reflect.ReflectException;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Property;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.postgresql.util.PSQLException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.w3c.dom.Node;

import com.microsoft.sqlserver.jdbc.SQLServerException;

/**
 * The abstract test suite uses generic types to model the generated test schema
 * types, such as <code>T_AUTHOR</code>, <code>T_BOOK</code>, etc
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class jOOQAbstractTest<

        // T_AUTHOR table
        A extends UpdatableRecord<A> & Record6<Integer, String, String, ? extends java.util.Date, Integer, ?>,

        // T_AUTHOR pojo
        AP,

        // T_BOOK table
        B extends UpdatableRecord<B>,

        // T_BOOK_STORE table
        S extends UpdatableRecord<S> & Record1<String>,

        // T_BOOK_TO_BOOK_STORE table
        B2S extends TableRecord<B2S> & Record3<String, Integer, Integer>,

        // MULTI_SCHEMA.T_BOOK_SALE table
        BS extends UpdatableRecord<BS>,

        // V_LIBRARY view
        L extends TableRecord<L>/* & Record2<String, String>*/,

        // T_ARRAYS table
        X extends TableRecord<X>,

        // T_DATES table
        DATE extends UpdatableRecord<DATE>,

        // T_BOOLEANS table
        BOOL extends UpdatableRecord<BOOL>,

        // T_DIRECTORY table
        D extends UpdatableRecord<D>,

        // T_TRIGGERS table
        T extends UpdatableRecord<T>,

        // T_UNSIGNED table
        U extends TableRecord<U>,

        // T_EXOTIC_TYPES table
        UU extends UpdatableRecord<UU>,

        // T_CHARSETS table
        CS extends UpdatableRecord<CS>,

        // T_IDENTITY table
        I extends TableRecord<I>,

        // T_IDENTITY_PK table
        IPK extends UpdatableRecord<IPK>,

        // Various tables related to trac ticket numbers
        T725 extends UpdatableRecord<T725>,
        T639 extends UpdatableRecord<T639>,
        T785 extends TableRecord<T785>,
        CASE extends UpdatableRecord<CASE>> {

    protected static final List<Short>      BOOK_IDS_SHORT     = Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4);
    protected static final List<Integer>    BOOK_IDS           = Arrays.asList(1, 2, 3, 4);
    protected static final List<Integer>    BOOK_AUTHOR_IDS    = Arrays.asList(1, 1, 2, 2);
    protected static final List<String>     BOOK_TITLES        = Arrays.asList("1984", "Animal Farm", "O Alquimista", "Brida");
    protected static final List<String>     BOOK_FIRST_NAMES   = Arrays.asList("George", "George", "Paulo", "Paulo");
    protected static final List<String>     BOOK_LAST_NAMES    = Arrays.asList("Orwell", "Orwell", "Coelho", "Coelho");
    protected static final List<Integer>    AUTHOR_IDS         = Arrays.asList(1, 2);
    protected static final List<String>     AUTHOR_FIRST_NAMES = Arrays.asList("George", "Paulo");
    protected static final List<String>     AUTHOR_LAST_NAMES  = Arrays.asList("Orwell", "Coelho");

    public static final JooqLogger          log                = JooqLogger.getLogger(jOOQAbstractTest.class);
    public static final StopWatch           testSQLWatch       = new StopWatch();
    public static boolean                   initialised;
    public static boolean                   reset;
    public static DataSource                datasource;
    public static Connection                connection;
    public static boolean                   connectionInitialised;
    public static Connection                connectionMultiSchema;
    public static boolean                   connectionMultiSchemaInitialised;
    public static Connection                connectionMultiSchemaUnused;
    public static boolean                   connectionMultiSchemaUnusedInitialised;
    public static boolean                   autocommit;
    public static String                    jdbcURL;
    public static String                    jdbcSchema;
    public static Map<String, String>       scripts            = new HashMap<String, String>();
    public static Table<?>[]                clean;
    public static Map<String, SQLDialect[]> coveredMethods     = new TreeMap<String, SQLDialect[]>();
    public static SQLDialect                coveredDialect;
    public static Properties                properties;

    /**
     * Used by instrumentation to register a call to a {@link Support}-annotated method.
     */
    public static void call(String methodName, SQLDialect... dialects) {
        coveredMethods.put(methodName, dialects);
    }

    protected void execute(String script) throws Exception {
        Statement stmt = null;

        String allSQL = scripts.get(script);
        if (allSQL == null) {
            try {
                log.info("Loading", script);
                File file = new File(getClass().getResource(script).toURI());
                allSQL = FileUtils.readFileToString(file);
                testSQLWatch.splitInfo("Loaded SQL file");
            }
            catch (Exception ignore) {
                allSQL = "";
            }

            scripts.put(script, allSQL);
        }

        for (String sql : allSQL.split("/")) {
            try {
                if (!StringUtils.isBlank(sql)) {
                    sql = sql.replace("{jdbc.Schema}", jdbcSchema);

                    if (sql.toLowerCase().contains("multi_schema_unused.") &&
                       !sql.toLowerCase().contains("references multi_schema_unused")) {
                        stmt = getConnectionMultiSchemaUnused().createStatement();
                    }
                    else if (sql.toLowerCase().contains("multi_schema.") &&
                            !sql.toLowerCase().contains("references multi_schema.")) {
                        stmt = getConnectionMultiSchema().createStatement();
                    }
                    else {
                        stmt = getConnection().createStatement();
                    }

                    stmt.execute(sql.trim());
                    testSQLWatch.splitInfo(StringUtils.abbreviate(sql.trim().replaceAll("[\\n\\r]|\\s+", " "), 25));
                }
            }
            catch (Exception e) {
                if (e.getMessage() == null)
                    throw e;

                log.debug("Ignoring", e.getMessage().replaceAll("\n", " "));

                // Ignore all errors on DROP statements
                if (sql.trim().startsWith("DROP")) {
                    continue;
                }

                // There is no DROP TABLE IF EXISTS statement in Oracle
                if (e.getMessage().contains("ORA-00942")) {
                    continue;
                }

                // There is no DROP MATERIALIZED VIEW IF EXISTS statement in Oracle
                else if (e.getMessage().contains("ORA-12003")) {
                    continue;
                }

                // There is no DROP SEQUENCE IF EXISTS statement in Oracle
                else if (e.getMessage().contains("ORA-02289")) {
                    continue;
                }

                // There is no DROP {PROCEDURE|FUNCTION} IF EXISTS statement in
                // Oracle
                else if (e.getMessage().contains("ORA-04043")) {
                    continue;
                }

                // There is no DROP TRIGGER IF EXISTS statement in Oracle
                else if (e.getMessage().contains("ORA-04080")) {
                    continue;
                }

                // There is no DROP TABLE IF EXISTS statement in DB2
                else if (e.getMessage().contains("SQLCODE=-204") && e.getMessage().contains("SQLSTATE=42704")) {
                    continue;
                }

                // There is no DROP TRANSFORM IF EXISTS statement in DB2
                else if (e.getMessage().contains("SQLCODE=-20012") && e.getMessage().contains("SQLSTATE=42740")) {
                    continue;
                }

                // There is no DROP FUNCTION IF EXISTS statement in Postgres
                else if (e.getClass().getName().startsWith("org.postgresql")) {
                    if (asList("42704", "42883", "42P01").contains(((PSQLException) e).getSQLState())) {
                        continue;
                    }
                }

                // There is no DROP ** IF EXISTS statement in Derby
                else if (e.getCause() instanceof org.apache.derby.client.am.SqlException) {
                    if (sql.contains("DROP") || sql.contains("CREATE SCHEMA")) {
                        continue;
                    }
                }

                /* [pro] */
                // There is no DROP ** IF EXISTS statement in SQL Server
                else if (e.getClass().getName().startsWith("com.microsoft")) {
                    switch (((SQLServerException)e).getErrorCode()) {
                        case 3701: // Tables
                        case 218:  // Types
                        continue;
                    }
                }
                /* [/pro] */

                else if (e.getMessage().startsWith("Cannot drop")) {
                    continue;
                }

                // There is no DROP SEQUENCE IF EXISTS statement in Sybase
                else if (e.getClass().getName().startsWith("com.sybase")) {
                    if (sql.contains("DROP SEQUENCE")) {
                        continue;
                    }
                }

                // There are no DROP SEQUENCE IF EXISTS and
                // DROP RULE IF EXISTS statements in Ingres
                else if (e instanceof SQLSyntaxErrorException) {
                    if (sql.contains("DROP SEQUENCE") || sql.contains("DROP RULE")) {
                        continue;
                    }
                }

                /* [pro] */
                // There are no IF EXISTS clauses in Sybase ASE
                else if (e.getMessage().contains("doesn't exist") && getDialect() == SQLDialect.ASE) {
                    continue;
                }

                // Ucanaccess has missing table exceptions
                else if (e.getMessage().contains("missing table")) {
                    continue;
                }

                /* [/pro] */
                // There is no IF EXISTS clause in CUBRID's DROP VIEW statement
                else if (getDialect() == CUBRID && sql.trim().startsWith("DROP")) {
                    continue;
                }

                // There is no IF EXISTS clause in Firebird's DROP statements
                else if (getDialect() == FIREBIRD && sql.trim().startsWith("DROP")) {
                    continue;
                }

                // All other errors
                System.out.println("Error while executing : " + sql.trim());
                System.out.println();
                System.out.println();
                e.printStackTrace();

                System.exit(-1);
            }
            finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        // Sybase throws an exception: JZ0S2: Statement object has already been closed
                    }
                }
            }
        }
    }

    static final String DEFAULT_MESSAGE = "Test failed";

    static <T> void assertThat(T actual, Predicate<T> expected) {
        assertThat((Supplier<T>) () -> actual, expected, DEFAULT_MESSAGE);
    }

    static <T> void assertThat(T actual, Predicate<T> expected, String message) {
        assertThat((Supplier<T>) () -> actual, expected, message);
    }

    static <T> void assertThat(Supplier<T> actual, Predicate<T> expected) {
        assertThat(actual, expected, DEFAULT_MESSAGE);
    }

    static <T> void assertThat(Supplier<T> actual, Predicate<T> expected, String message) {
        if (!expected.test(actual.get()))
            throw new AssertionError(message);
    }

    static void assertThat(double actual, DoublePredicate expected) {
        assertThat((DoubleSupplier) () -> actual, expected, DEFAULT_MESSAGE);
    }

    static void assertThat(double actual, DoublePredicate expected, String message) {
        assertThat((DoubleSupplier) () -> actual, expected, message);
    }

    static void assertThat(DoubleSupplier actual, DoublePredicate expected) {
        assertThat(actual, expected, DEFAULT_MESSAGE);
    }

    static void assertThat(DoubleSupplier actual, DoublePredicate expected, String message) {
        if (!expected.test(actual.getAsDouble()))
            throw new AssertionError(message);
    }

    static <T> void assume(T actual, Predicate<T> expected) {
        assume((Supplier<T>) () -> actual, expected, DEFAULT_MESSAGE);
    }

    static <T> void assume(T actual, Predicate<T> expected, String message) {
        assume((Supplier<T>) () -> actual, expected, message);
    }

    static <T> void assume(Supplier<T> actual, Predicate<T> expected) {
        assume(actual, expected, DEFAULT_MESSAGE);
    }

    static <T> void assume(Supplier<T> actual, Predicate<T> expected, String message) {
        if (!expected.test(actual.get()))
            throw new AssumptionViolatedException(message);
    }

    static void assume(double actual, DoublePredicate expected) {
        assume((DoubleSupplier) () -> actual, expected, DEFAULT_MESSAGE);
    }

    static void assume(double actual, DoublePredicate expected, String message) {
        assume((DoubleSupplier) () -> actual, expected, message);
    }

    static void assume(DoubleSupplier actual, DoublePredicate expected) {
        assume(actual, expected, DEFAULT_MESSAGE);
    }

    static void assume(DoubleSupplier actual, DoublePredicate expected, String message) {
        if (!expected.test(actual.getAsDouble()))
            throw new AssumptionViolatedException(message);
    }

    @Before
    public void setUp() throws Exception {

        // Skip integration tests, for all dialects that are not in this property
        String dialectString = System.getProperty("org.jooq.test-dialects");
        assume(dialectString, s -> s != null && s.length() > 0);
        assume(
            dialect().family().name().toLowerCase(),

            d -> stream(dialectString.split("[,;]"))
                .map(String::trim)
                .map(String::toLowerCase)
                .anyMatch(d::equals)
        );

        connection = getConnection();
        coveredDialect = dialect();
        // connectionMultiSchema = getConnectionMultiSchema();

        autocommit = connection.getAutoCommit();

        if (!initialised) {
            initialised = true;
            execute(getCreateScript());
            // execute(getLargeScript());
        }

        if (!reset) {
            reset = true;
            execute(getResetScript());
        }

        if (clean != null && clean.length > 0) {
            for (Table<?> table : clean) {
                try {
                    create().delete(table).execute();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null)
            connection.setAutoCommit(autocommit);
    }

    public void clean(Table<?>... tables) {
        clean = tables;
    }

    @BeforeClass
    public static void testStart() throws Exception {
        log.info("STARTING");
    }

    @AfterClass
    public static void quit() throws Exception {
        log.info("QUITTING");

        if (connection != null) {

            // Issue a log dump on adaptive server. Don't know why this is needed
            // http://www.faqs.org/faqs/databases/sybase-faq/part6/
            if (connection.getClass().getPackage().getName().contains("jtds") &&
                !connection.getMetaData().getURL().contains("sqlserver")) {

                log.info("RUNNING", "dump tran TEST with truncate_only");
                try (Statement s = connection.createStatement()) {
                    s.execute("dump tran TEST with truncate_only");
                }
            }

            if (connection.getClass().getPackage().getName().contains("hsqldb")) {
                log.info("RUNNING", "SHUTDOWN");
                try (Statement s = connection.createStatement()) {
                    s.execute("shutdown");
                }
            }

            connection.close();
            connection = null;
            datasource = null;
        }

        JooqLogger logStat = JooqLogger.getLogger(TestStatisticsListener.class);
        logStat.info("TEST STATISTICS");
        logStat.info("---------------");

        int total = 0;
        for (ExecuteType type : ExecuteType.values()) {
            Integer count = TestStatisticsListener.STATISTICS.get(type);
            if (count == null) count = 0;
            total += count;

            logStat.info(type.name(), count + " executions");
        }

        logStat.info("---------------");
        logStat.info("Total", total);

        int unbalanced = 0;
        JooqLogger logLife = JooqLogger.getLogger(Lifecycle.class);

        logLife.info("");
        logLife.info("CONNECTION PROVIDER LIFECYCLE STATS");
        logLife.info("-----------------------------------");
        unbalanced = extracted(logLife, unbalanced, ACQUIRE_COUNT, RELEASE_COUNT);

        logLife.info("");
        logLife.info("EXECUTE LISTENER LIFECYCLE STATS");
        logLife.info("--------------------------------");
        unbalanced = extracted(logLife, unbalanced, LISTENER_START_COUNT, LISTENER_END_COUNT);

        logLife.info("");
        logLife.info("JDBC STATEMENT LIFECYCLE STATS");
        logLife.info("------------------------------");
        unbalanced = extracted(logLife, unbalanced, STMT_START_COUNT, STMT_CLOSE_COUNT);

        logLife.info("");
        logLife.info("JDBC RESULTSET LIFECYCLE STATS");
        logLife.info("------------------------------");
        unbalanced = extracted(logLife, unbalanced, RS_START_COUNT, RS_CLOSE_COUNT);

        logLife.info("");
        logLife.info("Unbalanced test: ", unbalanced);

        log.info("");
        log.info("INSTRUMENTED @Support METHOD STATS");
        log.info("----------------------------------");
        log.info("");
        log.info("Instrumented method calls", coveredMethods.size());

        coveredMethods.forEach((methodName, dialects) -> {
            EnumSet<SQLDialect> all = dialects.length == 0
                ? EnumSet.allOf(SQLDialect.class)
                : EnumSet.copyOf(Arrays.asList(dialects));

            all.addAll(all.stream().map(d -> d.family()).collect(Collectors.toList()));

            if (!all.contains(coveredDialect) &&
                !all.contains(coveredDialect.family()))
                log.info("No " + coveredDialect + " support on " + methodName);
        });

        log.info("");
        log.info("LOGGING CONNECTION SQL STATEMENTS");
        log.info("---------------------------------");
        log.info("");
        LoggingConnection.statements.forEach(sql -> log.info("  " + sql));
    }

    private static int extracted(JooqLogger logger, int unbalanced, Map<Method, Integer> startCount, Map<Method, Integer> endCount) {
        for (Method m : startCount.keySet()) {
            Integer starts = startCount.get(m);
            Integer ends = endCount.get(m);

            if (!StringUtils.equals(starts, ends)) {
                unbalanced++;

                logger.info(
                    "Unbalanced", String.format("(open, close): (%1$3s, %2$3s) at %3$s",
                        starts,
                        ends == null ? 0 : ends,
                        m.toString().replace("public void ", "").replaceAll("( throws.*)?", "")));
            }
        }
        return unbalanced;
    }

    @SuppressWarnings("unused")
    public final Connection getConnection() {
        try {
            if (connectionInitialised && connection != null && connection.isClosed()) {
                log.info("CONNECTION CLOSED", "Reconnecting...");

                connectionInitialised = false;
                connection = null;
                datasource = null;
            }
        }
        catch (SQLException e) {
        }

        if (!connectionInitialised) {
            try {

                connectionInitialised = true;
                connection = getConnection0(null, null);

                /* [pro] */
                // Informix. Only Informix...
                if (dialect().family() == INFORMIX)
                    DSL.using(connection).execute("execute procedure ifx_allow_newline('t');");
                /* [/pro] */

                final Connection c = connection;

                // Reactivate this, to enable mock connections
                if (false)
                connection = new MockConnection(context -> {
                    DSLContext executor = DSL.using(c, getDialect());

                    if (context.batchSingle()) {
                        Query query = executor.query(context.sql(), new Object[context.batchBindings()[0].length]);
                        int[] result =
                        executor.batch(query)
                                .bind(context.batchBindings())
                                .execute();

                        MockResult[] r = new MockResult[result.length];
                        for (int i = 0; i < r.length; i++) {
                            r[i] = new MockResult(result[i], null);
                        }

                        return r;
                    }
                    else if (context.batchMultiple()) {
                        List<Query> queries = new ArrayList<Query>();

                        for (String sql : context.batchSQL()) {
                            queries.add(executor.query(sql));
                        }

                        int[] result =
                        executor.batch(queries)
                                .execute();

                        MockResult[] r = new MockResult[result.length];
                        for (int i = 0; i < r.length; i++) {
                            r[i] = new MockResult(result[i], null);
                        }

                        return r;
                    }
                    else if (context.sql().toLowerCase().matches("(?s:\\W*(select|with).*)")) {
                        Results result = executor.fetchMany(context.sql(), context.bindings());
                        MockResult[] r = new MockResult[result.size()];

                        for (int i = 0; i < result.size(); i++) {
                            r[i] = new MockResult(result.get(i).size(), result.get(i));
                        }

                        return r;
                    }
                    else {
                        int result = executor.execute(context.sql(), context.bindings());

                        MockResult[] r = new MockResult[1];
                        r[0] = new MockResult(result, null);

                        return r;
                    }
                });
            }
            finally {
                datasource = new SingleConnectionDataSource(new LoggingConnection(connection), true);
            }
        }

        return connection;
    }

    public final Connection getConnectionMultiSchema() {
        if (!connectionMultiSchemaInitialised) {
            connectionMultiSchemaInitialised = true;
            connectionMultiSchema = getConnection0("MULTI_SCHEMA", getPassword("MULTI_SCHEMA"));
        }

        return connectionMultiSchema;
    }

    public final Connection getConnectionMultiSchemaUnused() {
        if (!connectionMultiSchemaUnusedInitialised) {
            connectionMultiSchemaUnusedInitialised = true;
            connectionMultiSchemaUnused = getConnection0("MULTI_SCHEMA_UNUSED", getPassword("MULTI_SCHEMA_UNUSED"));
        }

        return connectionMultiSchemaUnused;
    }

    public static  final String getProperty(List<Property> properties, String key) {
        for (Property p : properties) {
            if (p.getKey().equals(key)) {
                return p.getValue();
            }
        }

        return null;
    }

    public static final Properties getProperties() {

        if (properties == null) {
            // [#682] We reuse the config.properties that is also used for the Maven pom.xml
            try (InputStream config = GenerationTool.class.getResourceAsStream("/config.properties")) {
                if (config != null) {
                    properties = new Properties();
                    properties.load(config);

                    String computerName = System.getenv("COMPUTERNAME");

                    for (String key : new ArrayList<String>((Set) properties.keySet())) {
                        String override = key + "." + computerName;

                        if (properties.containsKey(override)) {
                            properties.setProperty(key, properties.getProperty(override));
                        }
                    }
                }
            }

            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return properties;
    }

    public static final String getDriver(SQLDialect dialect) {
        return StringUtils.defaultIfNull(
            getProperties().getProperty("db." + dialect.name().toLowerCase() + ".driver"),
            getProperties().getProperty("db." + dialect.family().name().toLowerCase() + ".driver"));
    }

    public static final String getURL(SQLDialect dialect) {
        return getURL(dialect, "");
    }

    public static final String getURL(SQLDialect dialect, String schemaSuffix) {
        return StringUtils.defaultIfNull(
            getProperties().getProperty("db." + dialect.name().toLowerCase() + ".url"),
            getProperties().getProperty("db." + dialect.family().name().toLowerCase() + ".url")) + schemaSuffix;
    }

    public static final String getSchema(SQLDialect dialect) {
        return getSchema(dialect, "");
    }

    public static final String getSchema(SQLDialect dialect, String schemaSuffix) {
        return StringUtils.defaultIfNull(
            getProperties().getProperty("db." + dialect.name().toLowerCase() + ".schema"),
            getProperties().getProperty("db." + dialect.family().name().toLowerCase() + ".schema")) + schemaSuffix;
    }

    public static final String getUsername(SQLDialect dialect) {
        return StringUtils.defaultIfNull(
            getProperties().getProperty("db." + dialect.name().toLowerCase() + ".username"),
            getProperties().getProperty("db." + dialect.family().name().toLowerCase() + ".username"));
    }

    public static final String getPassword(SQLDialect dialect) {
        return getPassword(dialect, null);
    }

    public static final String getPassword(SQLDialect dialect, String username) {
        String result = defaultIfNull(
            getProperties().getProperty("db." + dialect         .name().toLowerCase() + ".password" + (username == null ? "" : ("." + username))),
            getProperties().getProperty("db." + dialect.family().name().toLowerCase() + ".password" + (username == null ? "" : ("." + username))));

        if (isBlank(result) && !isBlank(username))
            result = username;

        return result;
    }

    public final String getDriver() {
        return getDriver(dialect());
    }

    public final String getURL() {
        return getURL(dialect(), getSchemaSuffix());
    }

    public final String getSchema() {
        return getSchema(dialect(), getSchemaSuffix());
    }

    public final String getUsername() {
        return getUsername(dialect());
    }

    public final String getPassword() {
        return getPassword(dialect());
    }

    public final String getPassword(String username) {
        return getPassword(dialect(), username);
    }

    final Connection getConnection0(String jdbcUser, String jdbcPassword) {
        try {
            if (getDriver() != null) {
                jdbcURL = getURL();
                jdbcSchema = getSchema();

                if (jdbcUser == null)
                    jdbcUser = getUsername();

                if (jdbcPassword == null)
                    jdbcPassword = getPassword();

                return getConnection1(jdbcUser, jdbcPassword, getDriver());
            }

            String configuration = System.getProperty("org.jooq.configuration");
            if (configuration == null) {
                log.error("No system property 'org.jooq.configuration' found");
                log.error("-----------");
                log.error("Please be sure property is set; example: -Dorg.jooq.configuration=/org/jooq/configuration/${env_var:USERNAME}/db2/library.xml");
                throw new Error();
            }
            InputStream in = GenerationTool.class.getResourceAsStream(configuration);
            if (in == null) {
                log.error("Cannot find " + configuration);
                log.error("-----------");
                log.error("Please be sure it is located on the classpath and qualified as a classpath location.");
                log.error("If it is located at the current working directory, try adding a '/' to the path");
                throw new Error();
            }

            Configuration c = GenerationTool.load(in);
            Jdbc jdbc = c.getJdbc();

            String driver = jdbc.getDriver();
            jdbcURL = jdbc.getUrl() + getSchemaSuffix();
            jdbcSchema = jdbc.getSchema() + getSchemaSuffix();
            jdbcUser =
                jdbcUser != null ? jdbcUser :
                jdbc.getUser() != null ? jdbc.getUser() :
                getProperty(jdbc.getProperties(), "user");
            jdbcPassword =
                jdbcPassword != null ? jdbcPassword :
                jdbc.getPassword() != null ? jdbc.getPassword() :
                getProperty(jdbc.getProperties(), "password");

            return getConnection1(jdbcUser, jdbcPassword, driver);
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }

    private Connection getConnection1(String jdbcUser, String jdbcPassword, String driver) throws Exception {
        Properties info = new Properties();
        if (getClass().getSimpleName().toLowerCase().contains("ingres")) {
            info.setProperty("timezone", "EUROPE-CENTRAL");
        }
        Driver d = ((Driver) Class.forName(driver).newInstance());
        if (!StringUtils.isBlank(jdbcUser)) {
            info.put("user", jdbcUser);
            info.put("password", jdbcPassword);
        }
        else {
            return DriverManager.getConnection(getJdbcURL(), jdbcUser, jdbcPassword);
        }

        // d.connect() doesn't work for Derby, for some reason
        return d != null
            ? d.connect(getJdbcURL(), info)
            : DriverManager.getConnection(getJdbcURL(), info);
    }

    /**
     * Gets the jdbc url.
     *
     * Subclasses can override this method to add special parameters to the url
     */
    protected String getJdbcURL() {
        return jdbcURL;
    }

    protected final String getLargeScript() throws Exception {
        return "/org/jooq/test/" + getDialect().getName().toLowerCase() + "/large-schema.sql";
    }

    protected final String getCreateScript() throws Exception {
        return "/org/jooq/test/" + getDialect().getName().toLowerCase() + "/create.sql";
    }

    protected final String getResetScript() throws Exception {
        return "/org/jooq/test/" + getDialect().getName().toLowerCase() + "/reset.sql";
    }

    protected abstract Table<T725> T725();
    protected abstract TableField<T725, Integer> T725_ID();
    protected abstract TableField<T725, byte[]> T725_LOB();
    protected abstract Table<T639> T639();
    protected abstract TableField<T639, Integer> T639_ID();
    protected abstract TableField<T639, BigDecimal> T639_BIG_DECIMAL();
    protected abstract TableField<T639, BigInteger> T639_BIG_INTEGER();
    protected abstract TableField<T639, Byte> T639_BYTE();
    protected abstract TableField<T639, Byte> T639_BYTE_DECIMAL();
    protected abstract TableField<T639, Short> T639_SHORT();
    protected abstract TableField<T639, Short> T639_SHORT_DECIMAL();
    protected abstract TableField<T639, Integer> T639_INTEGER();
    protected abstract TableField<T639, Integer> T639_INTEGER_DECIMAL();
    protected abstract TableField<T639, Long> T639_LONG();
    protected abstract TableField<T639, Long> T639_LONG_DECIMAL();
    protected abstract TableField<T639, Double> T639_DOUBLE();
    protected abstract TableField<T639, Float> T639_FLOAT();

    protected abstract Table<T785> T785();
    protected abstract TableField<T785, Integer> T785_ID();
    protected abstract TableField<T785, String> T785_NAME();
    protected abstract TableField<T785, String> T785_VALUE();

    protected abstract Table<CASE> CASE();
    protected TableField<CASE, Integer> CASE_ID() {
        return (TableField<CASE, Integer>) CASE().field(0);
    }
    protected TableField<CASE, Integer> CASE_insensitive() {
        return (TableField<CASE, Integer>) CASE().field(1);
    }
    protected TableField<CASE, Integer> CASE_UPPER() {
        return (TableField<CASE, Integer>) CASE().field(2);
    }
    protected TableField<CASE, Integer> CASE_lower() {
        return (TableField<CASE, Integer>) CASE().field(3);
    }
    protected TableField<CASE, Integer> CASE_Mixed() {
        return (TableField<CASE, Integer>) CASE().field(4);
    }

    protected abstract Table<U> TUnsigned();
    protected abstract TableField<U, UByte> TUnsigned_U_BYTE();
    protected abstract TableField<U, UShort> TUnsigned_U_SHORT();
    protected abstract TableField<U, UInteger> TUnsigned_U_INT();
    protected abstract TableField<U, ULong> TUnsigned_U_LONG();

    protected abstract Table<UU> TExoticTypes();
    protected abstract TableField<UU, Integer> TExoticTypes_ID();
    protected abstract TableField<UU, UUID> TExoticTypes_UU();
    protected TableField<UU, Node> TExoticTypes_UNTYPED_XML_AS_DOM() {
        return null;
    }

    protected TableField<UU, JAXBBook> TExoticTypes_UNTYPED_XML_AS_JAXB() {
        return null;
    }

    protected TableField<UU, String> TExoticTypes_PLAIN_SQL_CONVERTER_XML() {
        return null;
    }

    protected TableField<UU, String> TExoticTypes_PLAIN_SQL_BINDING_XML() {
        return null;
    }

    protected Table<CS> TCharsets() {
        return null;
    }

    protected TableField<CS, Integer> TCharsets_ID() {
        return null;
    }

    protected TableField<CS, String> TCharsets_UTF8() {
        return null;
    }

    protected abstract Table<DATE> TDates();

    protected abstract Table<X> TArrays();
    protected abstract TableField<X, Integer> TArrays_ID();
    protected abstract TableField<X, String[]> TArrays_STRING();
    protected abstract TableField<X, Integer[]> TArrays_NUMBER();
    protected abstract TableField<X, Date[]> TArrays_DATE();
    protected abstract TableField<X, ? extends UDTRecord<?>[]> TArrays_UDT();
    /* [pro] */
    protected abstract TableField<X, ? extends ArrayRecord<String>> TArrays_STRING_R();
    protected abstract TableField<X, ? extends ArrayRecord<Integer>> TArrays_NUMBER_R();
    protected abstract TableField<X, ? extends ArrayRecord<Long>> TArrays_NUMBER_LONG_R();
    protected abstract TableField<X, ? extends ArrayRecord<Date>> TArrays_DATE_R();
    /* [/pro] */

    protected DAO<A, AP, Integer> TAuthorDao() {
        return null;
    }

    protected final Class<AP> TAuthorPojo() {

        // Not all test configurations have generated POJOs. Discover them dynamically
        try {
            return on(TBook().getClass().getPackage().getName() + ".pojos." + TAuthor().getClass().getSimpleName()).get();
        }
        catch (ReflectException ignore) {
            return null;
        }
    }

    protected final Class<?> TBookPojo() {

        // Not all test configurations have generated POJOs. Discover them dynamically
        try {
            return on(TBook().getClass().getPackage().getName() + ".pojos." + TBook().getClass().getSimpleName()).get();
        }
        catch (ReflectException ignore) {
            return null;
        }
    }

    protected final Class<?> TBooleansPojo() {

        // Not all test configurations have generated POJOs. Discover them dynamically
        try {
            return on(TBook().getClass().getPackage().getName() + ".pojos." + TBooleans().getClass().getSimpleName()).get();
        }
        catch (ReflectException ignore) {
            return null;
        }
    }

    protected abstract Table<A> TAuthor();
    protected abstract TableField<A, String> TAuthor_LAST_NAME();
    protected abstract TableField<A, String> TAuthor_FIRST_NAME();
    protected abstract TableField<A, Date> TAuthor_DATE_OF_BIRTH();
    protected abstract TableField<A, Integer> TAuthor_YEAR_OF_BIRTH();
    protected abstract TableField<A, Integer> TAuthor_ID();
    protected abstract TableField<A, ? extends UDTRecord<?>> TAuthor_ADDRESS();

    protected abstract Class<? extends UDTRecord<?>> cUAddressType();
    protected abstract Class<? extends UDTRecord<?>> cUStreetType();
    protected abstract Table<B> TBook();

    protected abstract TableField<B, Integer> TBook_ID();
    protected abstract TableField<B, Integer> TBook_AUTHOR_ID();
    protected abstract TableField<B, Integer> TBook_CO_AUTHOR_ID();
    protected abstract TableField<B, String> TBook_TITLE();
    protected abstract TableField<B, Integer> TBook_LANGUAGE_ID();
    protected abstract TableField<B, Integer> TBook_PUBLISHED_IN();
    protected abstract TableField<B, String> TBook_CONTENT_TEXT();
    protected abstract TableField<B, byte[]> TBook_CONTENT_PDF();
    protected abstract TableField<B, ? extends Enum<?>> TBook_STATUS();
    protected TableField<B, Integer> TBook_REC_VERSION() {
        return null;
    }
    protected TableField<B, Timestamp> TBook_REC_TIMESTAMP() {
        return null;
    }
    protected abstract ForeignKey<B, A> FK_T_BOOK_AUTHOR_ID();
    protected abstract ForeignKey<B, A> FK_T_BOOK_CO_AUTHOR_ID();

    protected abstract Table<S> TBookStore();
    protected abstract TableField<S, String> TBookStore_NAME();
    protected abstract Table<L> VLibrary();
    protected abstract Table<?> VAuthor();
    protected abstract Table<?> VBook();
    protected abstract TableField<L, String> VLibrary_TITLE();
    protected abstract TableField<L, String> VLibrary_AUTHOR();

    protected abstract Table<B2S> TBookToBookStore();
    protected abstract TableField<B2S, Integer> TBookToBookStore_BOOK_ID();
    protected abstract TableField<B2S, String> TBookToBookStore_BOOK_STORE_NAME();
    protected abstract TableField<B2S, Integer> TBookToBookStore_STOCK();

    protected abstract Table<BS> TBookSale();
    protected final TableField<BS, Integer> TBookSale_ID() {
        return (TableField<BS, Integer>) TBookSale().field("ID");
    }
    protected final TableField<BS, Integer> TBookSale_BOOK_ID() {
        return (TableField<BS, Integer>) TBookSale().field("BOOK_ID");
    }
    protected final TableField<BS, String> TBookSale_BOOK_STORE_NAME() {
        return (TableField<BS, String>) TBookSale().field("BOOK_STORE_NAME");
    }
    protected final TableField<BS, Date> TBookSale_SOLD_AT() {
        return (TableField<BS, Date>) TBookSale().field("SOLD_AT");
    }
    protected final TableField<BS, BigDecimal> TBookSale_SOLD_FOR() {
        return (TableField<BS, BigDecimal>) TBookSale().field("SOLD_FOR");
    }

    protected abstract Table<BOOL> TBooleans();
    protected abstract TableField<BOOL, Integer> TBooleans_ID();
    protected abstract TableField<BOOL, Boolean_10> TBooleans_BOOLEAN_10();
    protected abstract TableField<BOOL, Boolean_TF_LC> TBooleans_Boolean_TF_LC();
    protected abstract TableField<BOOL, Boolean_TF_UC> TBooleans_Boolean_TF_UC();
    protected abstract TableField<BOOL, Boolean_YN_LC> TBooleans_Boolean_YN_LC();
    protected abstract TableField<BOOL, Boolean_YN_UC> TBooleans_Boolean_YN_UC();
    protected abstract TableField<BOOL, Boolean_YES_NO_LC> TBooleans_Boolean_YES_NO_LC();
    protected abstract TableField<BOOL, Boolean_YES_NO_UC> TBooleans_Boolean_YES_NO_UC();
    protected abstract TableField<BOOL, Boolean> TBooleans_VC();
    protected abstract TableField<BOOL, Boolean> TBooleans_C();
    protected abstract TableField<BOOL, Boolean> TBooleans_N();

    protected abstract Table<D> TDirectory();
    protected abstract TableField<D, Integer> TDirectory_ID();
    protected abstract TableField<D, Integer> TDirectory_PARENT_ID();
    protected abstract TableField<D, Integer> TDirectory_IS_DIRECTORY();
    protected abstract TableField<D, String> TDirectory_NAME();

    protected abstract Table<T> TTriggers();
    protected abstract TableField<T, Integer> TTriggers_ID_GENERATED();
    protected abstract TableField<T, Integer> TTriggers_ID();
    protected abstract TableField<T, Integer> TTriggers_COUNTER();

    protected abstract Table<I> TIdentity();
    protected abstract TableField<I, Integer> TIdentity_ID();
    protected abstract TableField<I, Integer> TIdentity_VAL();
    protected abstract Table<IPK> TIdentityPK();
    protected abstract TableField<IPK, Integer> TIdentityPK_ID();
    protected abstract TableField<IPK, Integer> TIdentityPK_VAL();

    protected <N extends Number> AggregateFunction<N> secondMax(Field<N> val) {
        return null;
    }
    protected abstract Field<? extends Number> FAuthorExistsField(String authorName);
    protected abstract Field<? extends Number> FOneField();
    protected abstract Field<? extends Number> FNumberField(Number n);
    protected abstract Field<? extends Number> FNumberField(Field<? extends Number> n);
    protected abstract Field<? extends Number> F317Field(Number n1, Number n2, Number n3, Number n4);
    protected abstract Field<? extends Number> F317Field(Field<? extends Number> n1, Field<? extends Number> n2, Field<? extends Number> n3, Field<? extends Number> n4);
    protected abstract Field<Result<Record>> FGetOneCursorField(Integer[] array);

    protected abstract Field<Integer[]> FArrays1Field(Field<Integer[]> array);
    protected abstract Field<Long[]> FArrays2Field(Field<Long[]> array);
    protected abstract Field<String[]> FArrays3Field(Field<String[]> array);
    /* [pro] */
    protected abstract <Z extends ArrayRecord<Integer>> Field<Z> FArrays1Field_R(Field<Z> array);
    protected abstract <Z extends ArrayRecord<Long>> Field<Z> FArrays2Field_R(Field<Z> array);
    protected abstract <Z extends ArrayRecord<String>> Field<Z> FArrays3Field_R(Field<Z> array);
    /* [/pro] */

    protected abstract boolean supportsOUTParameters();
    protected abstract boolean supportsReferences();
    protected abstract boolean supportsRecursiveQueries();
    protected abstract Class<?> cRoutines();
    protected abstract Class<?> cLibrary();
    protected abstract Class<?> cSequences();
    protected abstract DataType<?>[] getCastableDataTypes();
    protected abstract SQLDialect dialect();
    protected DSLContext create0(Settings settings) {
        return DSL.using(getConnection(), dialect(), settings);
    }

    protected final Schema schema() {
        return create().map(TAuthor().getSchema());
    }

    protected final DSLContext create() {
        String defaultSchema = System.getProperty("org.jooq.settings.defaultSchema", "");
        Boolean renderSchema = Boolean.valueOf(System.getProperty("org.jooq.settings.renderSchema", "true"));

        Settings settings = SettingsTools.defaultSettings()
            .withRenderSchema(renderSchema)
            .withRenderMapping(new RenderMapping()
                .withDefaultSchema(defaultSchema));

        org.jooq.Configuration c = create0(settings).configuration();
        ConnectionProviderLifecycleListener cp = new ConnectionProviderLifecycleListener(c.connectionProvider());

        return DSL.using(c
            .derive(cp)
            .derive(
                DefaultExecuteListenerProvider.providers(
                    new JDBCLifecycleListener(),
                    new LifecycleWatcherListener(),
                    new TestStatisticsListener(),
                    new PrettyPrinter()
                )
            )
        );
    }

    protected final DSLContext create(Settings settings) {
        DSLContext create = create0(settings);
        return create(create.configuration());
    }

    protected final DSLContext create(org.jooq.Configuration configuration) {
        return DSL.using(configuration.derive(combine(
            configuration.executeListenerProviders(),
            new DefaultExecuteListenerProvider(new TestStatisticsListener())
        )));
    }

    protected final DSLContext create(ExecuteListener... listeners) {
        ExecuteListenerProvider[] providers = new ExecuteListenerProvider[listeners.length];
        for (int i = 0; i < listeners.length; i++)
            providers[i] = new DefaultExecuteListenerProvider(listeners[i]);

        return create(create().configuration().derive(providers));
    }

    protected static final <T> T[] combine(T[] array, T value) {
        T[] result = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + 1);

        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = value;

        return result;
    }

    protected final SQLDialect getDialect() {
        return create().configuration().dialect();
    }

    protected String getSchemaSuffix() {
        return "";
    }

    @Test
    public void testInsertIntoView() throws Exception {
        new InsertUpdateTests(this).testInsertIntoView();
    }

    @Test
    public void testInsertIdentity() throws Exception {
        new InsertUpdateTests(this).testInsertIdentity();
    }

    @Test
    public void testInsertDefaultValues() throws Exception {
        new InsertUpdateTests(this).testInsertDefaultValues();
    }

    @Test
    public void testInsertDefaultValue() throws Exception {
        new InsertUpdateTests(this).testInsertDefaultValue();
    }

    @Test
    public void testInsertSetWithNulls() throws Exception {
        new InsertUpdateTests(this).testInsertSetWithNulls();
    }

    @Test
    public void testUpdateDefaultValue() throws Exception {
        new InsertUpdateTests(this).testUpdateDefaultValue();
    }

    @Test
    public void testTableMapping() throws Exception {
        new SchemaAndMappingTests(this).testTableMapping();
    }

    @Test
    public void testSchemaMapping() throws Exception {
        new SchemaAndMappingTests(this).testSchemaMapping();
    }

    @Test
    public void testMultiSchemaQueries() throws Exception {
        new SchemaAndMappingTests(this).testMultiSchemaQueries();
    }

    @Test
    public void testSystemFunctions() throws Exception {
        new FunctionTests(this).testSystemFunctions();
    }

    @Test
    public void testInterning() throws Exception {
        new FetchTests(this).testInterning();
    }

    @Test
    public void testFetchResultSet() throws Exception {
        new FetchTests(this).testFetchResultSet();
    }

    @Test
    public void testFetchResultSetValue() throws Exception {
        new FetchTests(this).testFetchResultSetValue();
    }

    @Test
    public void testFetchResultSetWithCoercedTypes() throws Exception {
        new FetchTests(this).testFetchResultSetWithCoercedTypes();
    }

    @Test
    public void testFetchIntoResultSet() throws Exception {
        new FetchTests(this).testFetchIntoResultSet();
    }

    @Test
    public void testResultQueryStream() throws Exception {
        new FetchTests(this).testResultQueryStream();
    }

    @Test
    public void testResultQueryStreamWithAutoCloseable() throws Exception {
        new FetchTests(this).testResultQueryStreamWithAutoCloseable();
    }

    @Test
    public void testFetchLazy() throws Exception {
        new FetchTests(this).testFetchLazy();
    }

    @Test
    public void testFetchLazyWithAutoCloseable() throws Exception {
        new FetchTests(this).testFetchLazyWithAutoCloseable();
    }

    @Test
    public void testFetchViaIterable() throws Exception {
        new FetchTests(this).testFetchViaIterable();
    }

    @Test
    public void testFetchMap() throws Exception {
        new FetchTests(this).testFetchMap();
    }

    @Test
    public void testFetchMapTable() throws Exception {
        new FetchTests(this).testFetchMapTable();
    }

    @Test
    public void testFetchMapPOJO() throws Exception {
        new FetchTests(this).testFetchMapPOJO();
    }

    @Test
    public void testFetchGroups() throws Exception {
        new FetchTests(this).testFetchGroups();
    }

    @Test
    public void testFetchGroupsTable() throws Exception {
        new FetchTests(this).testFetchGroupsTable();
    }

    @Test
    public void testFetchArray() throws Exception {
        new FetchTests(this).testFetchArray();
    }

    @Test
    public void testFetchSet() throws Exception {
        new FetchTests(this).testFetchSet();
    }

    @Test
    public void testFetchGroupsPOJO() throws Exception {
        new FetchTests(this).testFetchGroupsPOJO();
    }

    @Test
    public void testFetchGroupsPOJOandPOJO() throws Exception {
        new FetchTests(this).testFetchGroupsPOJOandPOJO();
    }

    @Test
    public void testFetchGroupsMapper() throws Exception {
        new RecordMapperTests(this).testFetchGroupsMapper();
    }

    @Test
    public void testFetchWithMaxRows() throws Exception {
        new FetchTests(this).testFetchWithMaxRows();
    }

    @Test
    public void testFetchWithTimeout() throws Exception {
        new FetchTests(this).testFetchWithTimeout();
    }

    @Test
    public void testDAOMethods() throws Exception {
        new DaoTests(this).testDAOMethods();
    }

    @Test
    public void testSelectGetSQLAndGetBindValues() throws Exception {
        new RenderAndBindTests(this).testSelectGetSQLAndGetBindValues();
    }

    @Test
    public void testInsertUpdateGetSQLAndGetBindValues() throws Exception {
        new RenderAndBindTests(this).testInsertUpdateGetSQLAndGetBindValues();
    }

    @Test
    public void testCreateView() throws Exception {
        new DDLTests(this).testCreateView();
    }

    @Test
    public void testDropViewIfExists() throws Exception {
        new DDLTests(this).testDropViewIfExists();
    }

    @Test
    public void testCreateIndex() throws Exception {
        new DDLTests(this).testCreateIndex();
    }

    @Test
    public void testDropIndex() throws Exception {
        new DDLTests(this).testDropIndex();
    }

    @Test
    public void testDropIndexIfExists() throws Exception {
        new DDLTests(this).testDropIndexIfExists();
    }

    @Test
    public void testCreateSequence() throws Exception {
        new DDLTests(this).testCreateSequence();
    }

    @Test
    public void testAlterSequence() throws Exception {
        new DDLTests(this).testAlterSequence();
    }

    @Test
    public void testDropSequence() throws Exception {
        new DDLTests(this).testDropSequence();
    }

    @Test
    public void testDropSequenceIfExists() throws Exception {
        new DDLTests(this).testDropSequenceIfExists();
    }

    @Test
    public void testAlterTableAdd() throws Exception {
        new DDLTests(this).testAlterTableAdd();
    }

    @Test
    public void testAlterTableAlterType() throws Exception {
        new DDLTests(this).testAlterTableAlterType();
    }

    @Test
    public void testAlterTableAlterDefault() throws Exception {
        new DDLTests(this).testAlterTableAlterDefault();
    }

    @Test
    public void testAlterTableDrop() throws Exception {
        new DDLTests(this).testAlterTableDrop();
    }

    @Test
    public void testAlterTableAddConstraint_CHECK() throws Exception {
        new DDLTests(this).testAlterTableAddConstraint_CHECK();
    }

    @Test
    public void testAlterTableAddConstraint_UNIQUE() throws Exception {
        new DDLTests(this).testAlterTableAddConstraint_UNIQUE();
    }

    @Test
    public void testAlterTableAddConstraint_PRIMARY_KEY() throws Exception {
        new DDLTests(this).testAlterTableAddConstraint_PRIMARY_KEY();
    }

    @Test
    public void testAlterTableAddConstraint_FOREIGN_KEY() throws Exception {
        new DDLTests(this).testAlterTableAddConstraint_FOREIGN_KEY();
    }

    @Test
    public void testAlterTableAddConstraint_FOREIGN_KEY_ON_CLAUSES() throws Exception {
        new DDLTests(this).testAlterTableAddConstraint_FOREIGN_KEY_ON_CLAUSES();
    }

    @Test
    public void testAlterTableDropConstraint() throws Exception {
        new DDLTests(this).testAlterTableDropConstraint();
    }

    @Test
    public void testDropTable() throws Exception {
        new DDLTests(this).testDropTable();
    }

    @Test
    public void testDropTableIfExists() throws Exception {
        new DDLTests(this).testDropTableIfExists();
    }

    @Test
    public void testCreateTable() throws Exception {
        new DDLTests(this).testCreateTable();
    }

    @Test
    public void testCreateTableAllDataTypes() throws Exception {
        new DDLTests(this).testCreateTableAllDataTypes();
    }

    @Test
    public void testCreateTableAsSelect() throws Exception {
        new DDLTests(this).testCreateTableAsSelect();
    }

    @Test
    public void testCreateGlobalTemporaryTable() throws Exception {
        new DDLTests(this).testCreateGlobalTemporaryTable();
    }

    @Test
    public void testSelectInto() throws Exception {
        new DDLTests(this).testSelectInto();
    }

    @Test
    public void testTruncate() throws Exception {
        new TruncateTests(this).testTruncate();
    }

    @Test
    public void testTruncateCascade() throws Exception {
        new TruncateTests(this).testTruncateCascade();
    }

    @Test
    public void testTruncateRestartIdentity() throws Exception {
        new TruncateTests(this).testTruncateRestartIdentity();
    }

    @Test
    public void testMetaModel() throws Exception {
        new MetaDataTests(this).testMetaModel();
    }

    @Test
    public void testMetaFieldTypes() throws Exception {
        new MetaDataTests(this).testMetaFieldTypes();
    }

    @Test
    public void testMetaData() throws Exception {
        new MetaDataTests(this).testMetaData();
    }

    @Test
    public void testMetaReferences() throws Exception {
        new MetaDataTests(this).testMetaReferences();
    }

    @Test
    public void testNumbers() throws Exception {
        new DataTypeTests(this).testNumbers();
    }

    @Test
    public void testLiterals() throws Exception {
        new GeneralTests(this).testLiterals();
    }

    @Test
    public void testQualifiedSQL() throws Exception {
        new PlainSQLTests(this).testQualifiedSQL();
    }

    @Test
    public void testPlainSQL() throws Exception {
        new PlainSQLTests(this).testPlainSQL();
    }

    @Test
    public void testPlainSQLInsert() throws Exception {
        new PlainSQLTests(this).testPlainSQLInsert();
    }

    @Test
    public void testPlainSQLAmbiguousColumnNames() throws Exception {
        new PlainSQLTests(this).testPlainSQLAmbiguousColumnNames();
    }

    @Test
    public void testPlainSQLWithSelfJoins() throws Exception {
        new PlainSQLTests(this).testPlainSQLWithSelfJoins();
    }

    @Test
    public void testPlainSQLExecuteWithResults() throws Exception {
        new PlainSQLTests(this).testPlainSQLExecuteWithResults();
    }

    @Test
    public void testPlainSQLAndComments() throws Exception {
        new PlainSQLTests(this).testPlainSQLAndComments();
    }

    @Test
    public void testPlainSQLCRUD() throws Exception {
        new PlainSQLTests(this).testPlainSQLCRUD();
    }

    @Test
    public void testPlainSQLWithQueryParts() throws Exception {
        new PlainSQLTests(this).testPlainSQLWithQueryParts();
    }

    @Test
    public void testPlainSQLFetchValue() throws Exception {
        new PlainSQLTests(this).testPlainSQLFetchValue();
    }

    @Test
    public void testPlainSQLResultQuery() throws Exception {
        new PlainSQLTests(this).testPlainSQLResultQuery();
    }

    @Test
    public void testPlainSQLBlobAndClob() throws Exception {
        new PlainSQLTests(this).testPlainSQLBlobAndClob();
    }

    @Test
    public void testPlainSQLLimitOffset() throws Exception {
        new PlainSQLTests(this).testPlainSQLLimitOffset();
    }

    @Test
    public void testPlainSQLAndJDBCEscapeSyntax() throws Exception {
        new PlainSQLTests(this).testPlainSQLAndJDBCEscapeSyntax();
    }

    @Test
    public void testCustomSQL() throws Exception {
        new PlainSQLTests(this).testCustomSQL();
    }

    @Test
    public void testUnsignedDataTypes() throws Exception {
        new DataTypeTests(this).testUnsignedDataTypes();
    }

    @Test
    public void testConversion() throws Exception {
        new DataTypeTests(this).testConversion();
    }

    @Test
    public void testConversionResult() throws Exception {
        new DataTypeTests(this).testConversionResult();
    }

    @Test
    public void testCustomConversion() throws Exception {
        new DataTypeTests(this).testCustomConversion();
    }

    @Test
    public void testJava8TimeWithConverter() throws Exception {
        new DataTypeTests(this).testJava8TimeWithConverter();
    }

    @Test
    public void testJava8TimeWithBinding() throws Exception {
        new DataTypeTests(this).testJava8TimeWithBinding();
    }

    @Test
    public void testForUpdateClauses() throws Exception {
        new SelectTests(this).testForUpdateClauses();
    }

    @Test
    public void testCoerce() throws Exception {
        new DataTypeTests(this).testCoerce();
    }

    @Test
    public void testCoerceAfterFetch() throws Exception {
        new DataTypeTests(this).testCoerceAfterFetch();
    }

    @Test
    public void testCastingToJavaClass() throws Exception {
        new DataTypeTests(this).testCastingToJavaClass();
    }

    @Test
    public void testCharCasts() throws Exception {
        new DataTypeTests(this).testCharCasts();
    }

    @Test
    public void testNestedCasting() throws Exception {
        new DataTypeTests(this).testNestedCasting();
    }

    @Test
    public void testCastingToSQLDataType() throws Exception {
        new DataTypeTests(this).testCastingToSQLDataType();
    }

    @Test
    public void testCastingToDialectDataType() throws Exception {
        new DataTypeTests(this).testCastingToDialectDataType();
    }

    @Test
    public void testSequences() throws Exception {
        new GeneralTests(this).testSequences();
    }

    @Test
    public void testSequenceByName() throws Exception {
        new GeneralTests(this).testSequenceByName();
    }

    @Test
    public void testSelectSimpleQuery() throws Exception {
        new SelectTests(this).testSelectSimpleQuery();
    }

    @Test
    public void testSelectCountQuery() throws Exception {
        new AggregateWindowFunctionTests(this).testSelectCountQuery();
    }

    @Test
    public void testSelectQuery() throws Exception {
        new SelectTests(this).testSelectQuery();
    }

    @Test
    public void testAccessInternalRepresentation() throws Exception {
        new GeneralTests(this).testAccessInternalRepresentation();
    }

    @Test
    public void testTypeConversions() throws Exception {
        new DataTypeTests(this).testTypeConversions();
    }

    @Test
    public void testConditionalSelect() throws Exception {
        new PredicateTests(this).testConditionalSelect();
    }

    @Test
    public void testConditions() throws Exception {
        new PredicateTests(this).testConditions();
    }

    @Test
    public void testInPredicateWithResult() throws Exception {
        new PredicateTests(this).testInPredicateWithResult();
    }

    @Test
    public void testInPredicateWithPlainSQL() throws Exception {
        new PredicateTests(this).testInPredicateWithPlainSQL();
    }

    @Test
    public void testInPredicateWithSubselectAndLimitOffset() throws Exception {
        new PredicateTests(this).testInPredicateWithSubselectAndLimitOffset();
    }

    @Test
    public void testBetweenConditions() throws Exception {
        new PredicateTests(this).testBetweenConditions();
    }

    @Test
    public void testConditionsAsFields() throws Exception {
        new PredicateTests(this).testConditionsAsFields();
    }

    @Test
    public void testFieldsAsConditions() throws Exception {
        new PredicateTests(this).testFieldsAsConditions();
    }

    @Test
    public void testNotField() throws Exception {
        new PredicateTests(this).testNotField();
    }

    @Test
    public void testQuantifiedPredicates() throws Exception {
        new PredicateTests(this).testQuantifiedPredicates();
    }

    @Test
    public void testBigDecimalPredicates() throws Exception {
        new PredicateTests(this).testBigDecimalPredicates();
    }

    @Test
    public void testRowValueExpressionConditions() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionConditions();
    }

    @Test
    public void testRowValueExpressionBetweenConditions() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionBetweenConditions();
    }

    @Test
    public void testRowValueExpressionInConditions() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionInConditions();
    }

    @Test
    public void testRowValueExpressionInConditionsWithEmptyList() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionInConditionsWithEmptyList();
    }

    @Test
    public void testRowValueExpressionOrderingConditions() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionOrderingConditions();
    }

    @Test
    public void testRowValueExpressionOrderingSubselects() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionOrderingSubselects();
    }

    @Test
    public void testRowValueExpressionQuantifiedComparisonPredicates_EQ_NE() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionQuantifiedComparisonPredicates_EQ_NE();
    }

    @Test
    public void testRowValueExpressionQuantifiedComparisonPredicates_LE_LT_GE_GT() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionQuantifiedComparisonPredicates_LE_LT_GE_GT();
    }

    @Test
    public void testRowValueExpressionNULLPredicate() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionNULLPredicate();
    }

    @Test
    public void testRowValueExpressionOverlapsCondition() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionOverlapsCondition();
    }

    @Test
    public void testRowValueExpressionRecords() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionRecords();
    }

    @Test
    public void testRowValueExpressionTableRecords() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionTableRecords();
    }

    @Test
    public void testRowValueExpressionValuesConstructor() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionValuesConstructor();
    }

    @Test
    public void testRowValueExpressionInSelectClause() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionInSelectClause();
    }

    @Test
    public void testIgnoreCase() throws Exception {
        new PredicateTests(this).testIgnoreCase();
    }

    @Test
    public void testIgnoreCaseForLongStrings() throws Exception {
        new PredicateTests(this).testIgnoreCaseForLongStrings();
    }

    @Test
    public void testLargeINCondition() throws Exception {
        new PredicateTests(this).testLargeINCondition();
    }

    @Test
    public void testLargeINConditionWithExecuteListener() throws Exception {
        new PredicateTests(this).testLargeINConditionWithExecuteListener();
    }

    @Test
    public void testSubSelect() throws Exception {
        new SelectTests(this).testSubSelect();
    }

    @Test
    public void testSelectWithINPredicate() throws Exception {
        new SelectTests(this).testSelectWithINPredicate();
    }

    @Test
    public void testSelectWithExistsPredicate() throws Exception {
        new SelectTests(this).testSelectWithExistsPredicate();
    }

    @Test
    public void testSelectFromSelect() throws Exception {
        new SelectTests(this).testSelectFromSelect();
    }

    @Test
    public void testSelfJoin() throws Exception {
        new SelectTests(this).testSelfJoin();
    }

    @Test
    public void testSelectWithSubselectProjection() throws Exception {
        new SelectTests(this).testSelectWithSubselectProjection();
    }

    @Test
    public void testDistinctQuery() throws Exception {
        new SelectTests(this).testDistinctQuery();
    }

    @Test
    public void testResultSort() throws Exception {
        new ResultTests(this).testResultSort();
    }

    @Test
    public void testFetchParentAndChildren() throws Exception {
        new ReferentialTests(this).testFetchParentAndChildren();
    }

    @Test
    public void testFetch() throws Exception {
        new FetchTests(this).testFetch();
    }

    @Test
    public void testFetchOptional() throws Exception {
        new FetchTests(this).testFetchOptional();
    }

    @Test
    public void testFetchStream() throws Exception {
        new FetchTests(this).testFetchStream();
    }

    @Test
    public void testFetchValue() throws Exception {
        new FetchTests(this).testFetchValue();
    }

    @Test
    public void testFetchExists() throws Exception {
        new FetchTests(this).testFetchExists();
    }

    @Test
    public void testFetchAny() throws Exception {
        new FetchTests(this).testFetchAny();
    }

    @Test
    public void testFetchMany() throws Exception {
        new FetchTests(this).testFetchMany();
    }

    @Test
    public void testFetchWithoutResults() throws Exception {
        new FetchTests(this).testFetchWithoutResults();
    }

    @Test
    public void testFetchIntoWithAnnotations() throws Exception {
        new FetchTests(this).testFetchIntoWithAnnotations();
    }

    @Test
    public void testFetchIntoWithoutAnnotations() throws Exception {
        new FetchTests(this).testFetchIntoWithoutAnnotations();
    }

    @Test
    public void testRecordFromWithAnnotations() throws Exception {
        new FetchTests(this).testRecordFromWithAnnotations();
    }

    @Test
    public void testRecordFromWithoutAnnotations() throws Exception {
        new FetchTests(this).testRecordFromWithoutAnnotations();
    }

    // @Test TODO [#1818] Re-enable this test
    public void testRecordFromWithIdentity() throws Exception {
        new FetchTests(this).testRecordFromWithIdentity();
    }

    @Test
    public void testRecordFromUpdatePK() throws Exception {
        new FetchTests(this).testRecordFromUpdatePK();
    }

    @Test
    public void testRecordFrom() throws Exception {
        new FetchTests(this).testRecordFrom();
    }

    @Test
    public void testReflectionWithAnnotations() throws Exception {
        new FetchTests(this).testReflectionWithAnnotations();
    }

    @Test
    public void testReflectionWithoutAnnotations() throws Exception {
        new FetchTests(this).testReflectionWithoutAnnotations();
    }

    @Test
    public void testReflectionWithImmutables() throws Exception {
        new FetchTests(this).testReflectionWithImmutables();
    }

    @Test
    public void testReflectionWithImmutablesAndConstructorProperties() throws Exception {
        new FetchTests(this).testReflectionWithImmutablesAndConstructorProperties();
    }

    @Test
    public void testFetchIntoTableRecords() throws Exception {
        new FetchTests(this).testFetchIntoTableRecords();
    }

    @Test
    public void testFetchIntoTableRecordsWithColumnAmbiguities() throws Exception {
        new FetchTests(this).testFetchIntoTableRecordsWithColumnAmbiguities();
    }

    @Test
    public void testFetchAttachables() throws Exception {
        new FetchTests(this).testFetchAttachables();
    }

    @Test
    public void testFetchIntoTableRecordsWithUDTs() throws Exception {
        new FetchTests(this).testFetchIntoTableRecordsWithUDTs();
    }

    @Test
    public void testFetchIntoRecordClass() throws Exception {
        new FetchTests(this).testFetchIntoRecordClass();
    }

    @Test
    public void testFetchIntoTable() throws Exception {
        new FetchTests(this).testFetchIntoTable();
    }

    @Test
    public void testFetchIntoTables() throws Exception {
        new FetchTests(this).testFetchIntoTables();
    }

    @Test
    public void testFetchIntoAliasedTables() throws Exception {
        new FetchTests(this).testFetchIntoAliasedTables();
    }

    @Test
    public void testFetchIntoCustomTable() throws Exception {
        new FetchTests(this).testFetchIntoCustomTable();
    }

    @Test
    public void testFetchIntoGeneratedPojos() throws Exception {
        new FetchTests(this).testFetchIntoGeneratedPojos();
    }

    @Test
    public void testFetchIntoValueType() throws Exception {
        new FetchTests(this).testFetchIntoValueType();
    }

    @Test
    public void testFetchIntoRecordHandler() throws Exception {
        new RecordMapperTests(this).testFetchIntoRecordHandler();
    }

    @Test
    public void testFetchIntoRecordMapper() throws Exception {
        new RecordMapperTests(this).testFetchIntoRecordMapper();
    }

    @Test
    public void testFetchIntoWithRecordMapperProvider() throws Exception {
        new RecordMapperTests(this).testFetchIntoWithRecordMapperProvider();
    }

    @Test
    public void testRecordOriginals() throws Exception {
        new RecordTests(this).testRecordOriginals();
    }

    @Test
    public void testRecordChanged() throws Exception {
        new RecordTests(this).testRecordChanged();
    }

    @Test
    public void testRecordChangedOnGeneratedMethods() throws Exception {
        new RecordTests(this).testRecordChangedOnGeneratedMethods();
    }

    @Test
    public void testRecordReset() throws Exception {
        new RecordTests(this).testRecordReset();
    }

    @Test
    public void testRecordListenerLoad() throws Exception {
        new RecordListenerTests(this).testRecordListenerLoad();
    }

    @Test
    public void testRecordListenerRefresh() throws Exception {
        new RecordListenerTests(this).testRecordListenerRefresh();
    }

    @Test
    public void testRecordListenerStore() throws Exception {
        new RecordListenerTests(this).testRecordListenerStore();
    }

    @Test
    public void testRecordListenerWithException() throws Exception {
        new RecordListenerTests(this).testRecordListenerWithException();
    }

    @Test
    public void testRecordListenerBatchStore() throws Exception {
        new RecordListenerTests(this).testRecordListenerBatchStore();
    }

    @Test
    public void testVisitListenerOnSELECT() throws Exception {
        new VisitListenerTests(this).testVisitListenerOnSELECT();
    }

    @Test
    public void testVisitListenerOnDML() throws Exception {
        new VisitListenerTests(this).testVisitListenerOnDML();
    }

    @Test
    public void testVisitListenerFailOnMissingWhere() throws Exception {
        new VisitListenerTests(this).testVisitListenerFailOnMissingWhere();
    }

    @Test
    public void testResultSetType() throws Exception {
        new ResultSetTests(this).testResultSetType();
    }

    @Test
    public void testResultSetTypeWithListener() throws Exception {
        new ResultSetTests(this).testResultSetTypeWithListener();
    }

    @Test
    public void testResultSetConcurrency() throws Exception {
        new ResultSetTests(this).testResultSetConcurrency();
    }

    @Test
    public void testConcurrentExecution() throws Exception {
        new ThreadSafetyTests(this).testConcurrentExecution();
    }

    @Test
    public void testEmptyGrouping() throws Exception {
        new GroupByTests(this).testEmptyGrouping();
    }

    @Test
    public void testGrouping() throws Exception {
        new GroupByTests(this).testGrouping();
    }

    @Test
    public void testGroupByCubeRollup() throws Exception {
        new GroupByTests(this).testGroupByCubeRollup();
    }

    @Test
    public void testHavingWithoutGrouping() throws Exception {
        new GroupByTests(this).testHavingWithoutGrouping();
    }

    @Test
    public void testInsertUpdateDelete() throws Exception {
        new CRUDTests(this).testInsertUpdateDelete();
    }

    @Test
    public void testInsertImplicit() throws Exception {
        new InsertUpdateTests(this).testInsertImplicit();
    }

    @Test
    public void testInsertMultiple() throws Exception {
        new InsertUpdateTests(this).testInsertMultiple();
    }

    @Test
    public void testInsertMultipleWithDifferentChangedFlags() throws Exception {
        new InsertUpdateTests(this).testInsertMultipleWithDifferentChangedFlags();
    }

    @Test
    public void testInsertConvert() throws Exception {
        new InsertUpdateTests(this).testInsertConvert();
    }

    @Test
    public void testInsertSelect() throws Exception {
        new InsertUpdateTests(this).testInsertSelect();
    }

    @Test
    public void testInsertWithSelectAsField() throws Exception {
        new InsertUpdateTests(this).testInsertWithSelectAsField();
    }

    @Test
    public void testUpdateWithRowValueExpression() throws Exception {
        new InsertUpdateTests(this).testUpdateWithRowValueExpression();
    }

    @Test
    public void testUpdateSelect() throws Exception {
        new InsertUpdateTests(this).testUpdateSelect();
    }

    @Test
    public void testUpdateSetRecord() throws Exception {
        new InsertUpdateTests(this).testUpdateSetRecord();
    }

    @Test
    public void testUpdateJoin() throws Exception {
        new InsertUpdateTests(this).testUpdateJoin();
    }

    @Test
    public void testUpdateFrom() throws Exception {
        new InsertUpdateTests(this).testUpdateFrom();
    }

    @Test
    public void testUpdateFromWithAlias() throws Exception {
        new InsertUpdateTests(this).testUpdateFromWithAlias();
    }

    @Test
    public void testInsertValuesOnDuplicateKeyUpdate() throws Exception {
        new InsertUpdateTests(this).testInsertValuesOnDuplicateKeyUpdate();
    }

    @Test
    public void testInsertSelectOnDuplicateKeyUpdate() throws Exception {
        new InsertUpdateTests(this).testInsertSelectOnDuplicateKeyUpdate();
    }

    @Test
    public void testInsertOnDuplicateKeyIgnore() throws Exception {
        new InsertUpdateTests(this).testInsertOnDuplicateKeyIgnore();
    }

    @Test
    public void testInsertReturning() throws Exception {
        new InsertUpdateTests(this).testInsertReturning();
    }

    @Test
    public void testInsertSelectReturning() throws Exception {
        new InsertUpdateTests(this).testInsertSelectReturning();
    }

    @Test
    public void testInsertReturningWithModelAPI() throws Exception {
        new InsertUpdateTests(this).testInsertReturningWithModelAPI();
    }

    @Test
    public void testInsertReturningWithSetClause() throws Exception {
        new InsertUpdateTests(this).testInsertReturningWithSetClause();
    }

    @Test
    public void testInsertReturningWithCaseSensitiveColumns() throws Exception {
        new InsertUpdateTests(this).testInsertReturningWithCaseSensitiveColumns();
    }

    @Test
    public void testInsertReturningWithRenderNameStyleAS_IS() throws Exception {
        new InsertUpdateTests(this).testInsertReturningWithRenderNameStyleAS_IS();
    }

    // @Test [#2374]
    public void testInsertReturningWithPlainSQL() throws Exception {
        new InsertUpdateTests(this).testInsertReturningWithPlainSQL();
    }

    @Test
    public void testUpdateReturning() throws Exception {
        new InsertUpdateTests(this).testUpdateReturning();
    }

    @Test
    public void testDeleteReturning() throws Exception {
        new InsertUpdateTests(this).testDeleteReturning();
    }

    @Test
    public void testMerge() throws Exception {
        new InsertUpdateTests(this).testMerge();
    }

    @Test
    public void testMergeWithH2SyntaxExtension() throws Exception {
        new InsertUpdateTests(this).testMergeWithH2SyntaxExtension();
    }

    @Test
    public void testMergeWithOracleSyntaxExtension() throws Exception {
        new InsertUpdateTests(this).testMergeWithOracleSyntaxExtension();
    }

    @Test
    public void testBlobAndClob() throws Exception {
        new DataTypeTests(this).testBlobAndClob();
    }

    @Test
    public void testDateTime() throws Exception {
        new DataTypeTests(this).testDateTime();
    }

    @Test
    public void testJava8TimeAPIQueries() throws Exception {
        new DataTypeTests(this).testJava8TimeAPIQueries();
    }

    @Test
    public void testJava8TimeAPIProcedures() throws Exception {
        new DataTypeTests(this).testJava8TimeAPIProcedures();
    }

    @Test
    public void testDateTimeFractionalSeconds() throws Exception {
        new DataTypeTests(this).testDateTimeFractionalSeconds();
    }

    @Test
    public void testDateTimeArithmetic() throws Exception {
        new DataTypeTests(this).testDateTimeArithmetic();
    }

    @Test
    public void testDateTimeArithmeticAndOperatorPrecedence() throws Exception {
        new DataTypeTests(this).testDateTimeArithmeticAndOperatorPrecedence();
    }

    @Test
    public void testFunctionsOnDates_DATE_ADD() throws Exception {
        new DataTypeTests(this).testFunctionsOnDates_DATE_ADD();
    }

    @Test
    public void testFunctionsOnDates_DATE_ADD_WithCast() throws Exception {
        new DataTypeTests(this).testFunctionsOnDates_DATE_ADD_WithCast();
    }

    @Test
    public void testFunctionsOnDates_DATE_DIFF_AND_DATE_ADD() throws Exception {
        new DataTypeTests(this).testFunctionsOnDates_DATE_DIFF_AND_DATE_ADD();
    }

    @Test
    public void testFunctionsOnDates_TRUNC() throws Exception {
        new DataTypeTests(this).testFunctionsOnDates_TRUNC();
    }

    @Test
    public void testCurrentDate() throws Exception {
        new DataTypeTests(this).testCurrentDate();
    }

    @Test
    public void testManager() throws Exception {
        new CRUDTests(this).testManager();
    }

    @Test
    public void testUpdatablesCopy() throws Exception {
        new CRUDTests(this).testUpdatablesCopy();
    }

    @Test
    public void testUpdatablesCopyAndInsert() throws Exception {
        new CRUDTests(this).testUpdatablesCopyAndInsert();
    }

    @Test
    public void testUpdatablesKeysMethod() throws Exception {
        new CRUDTests(this).testUpdatablesKeysMethod();
    }

    @Test
    public void testUpdatablesInsertUpdate() throws Exception {
        new CRUDTests(this).testUpdatablesInsertUpdate();
    }

    @Test
    public void testUpdatablesWithUpdatablePK() throws Exception {
        new CRUDTests(this).testUpdatablesWithUpdatablePK();
    }

    @Test
    public void testUpdatablesPK() throws Exception {
        new CRUDTests(this).testUpdatablesPK();
    }

    @Test
    public void testUpdatablesPartialUpdates() throws Exception {
        new CRUDTests(this).testUpdatablesPartialUpdates();
    }

    @Test
    public void testUpdatablesPKChangePK() throws Exception {
        new CRUDTests(this).testUpdatablesPKChangePK();
    }

    @Test
    public void testUpdatablesUK() throws Exception {
        new CRUDTests(this).testUpdatablesUK();
    }

    @Test
    public void testUpdatablesNoKey() throws Exception {
        new CRUDTests(this).testUpdatablesNoKey();
    }

    @Test
    public void testUpdatablesVersionAndTimestamp() throws Exception {
        new CRUDTests(this).testUpdatablesVersionAndTimestamp();
    }

    @Test
    public void testStoreWithOptimisticLock() throws Exception {
        new CRUDTests(this).testStoreWithOptimisticLock();
    }

    // [#3862] @Test
    public void testExecuteWithOptimisticLock() throws Exception {
        new CRUDTests(this).testExecuteWithOptimisticLock();
    }

    @Test
    public void testStoreVsExecuteInsert() throws Exception {
        new CRUDTests(this).testStoreVsExecuteInsert();
    }

    @Test
    public void testInsertWithRecordFrom() throws Exception {
        new CRUDTests(this).testInsertWithRecordFrom();
    }

    @Test
    public void testFetchFromTXT() throws Exception {
        new FormatTests(this).testFetchFromTXT();
    }

    @Test
    public void testFormat() throws Exception {
        new FormatTests(this).testFormat();
    }

    @Test
    public void testFormatHTML() throws Exception {
        new FormatTests(this).testFormatHTML();
    }

    @Test
    public void testFormatHTMLXSS() throws Exception {
        new FormatTests(this).testFormatHTMLXSS();
    }

    @Test
    public void testFetchFromCSV() throws Exception {
        new FormatTests(this).testFetchFromCSV();
    }

    @Test
    public void testFormatCSV() throws Exception {
        new FormatTests(this).testFormatCSV();
    }

    @Test
    public void testFormatJSON() throws Exception {
        new FormatTests(this).testFormatJSON();
    }

    @Test
    public void testFetchFromJSON() throws Exception {
        new FormatTests(this).testFetchFromJSON();
    }

    @Test
    public void testFormatXML() throws Exception {
        new FormatTests(this).testFormatXML();
    }

    @Test
    public void testFormatInsert() throws Exception {
        new FormatTests(this).testFormatInsert();
    }

    @Test
    public void testIntoXML() throws Exception {
        new FormatTests(this).testIntoXML();
    }

    @Test
    public void testIntoXMLContentHandler() throws Exception {
        new FormatTests(this).testIntoXMLContentHandler();
    }

    @Test
    public void testCombinedSelectQuery() throws Exception {
        new UnionTests(this).testCombinedSelectQuery();
    }

    @Test
    public void testUnionAndOrderBy() throws Exception {
        new UnionTests(this).testUnionAndOrderBy();
    }

    @Test
    public void testUnionAndOrderByFieldQualification() throws Exception {
        new UnionTests(this).testUnionAndOrderByFieldQualification();
    }

    @Test
    public void testUnionWithOrderByInSubselect() throws Exception {
        new UnionTests(this).testUnionWithOrderByInSubselect();
    }

    // @Test [#3676] TODO
    public void testUnionAssociativityGeneratedSQL() throws Exception {
        new UnionTests(this).testUnionAssociativityGeneratedSQL();
    }

    @Test
    public void testUnionAssociativityExecutedSQL() throws Exception {
        new UnionTests(this).testUnionAssociativityExecutedSQL();
    }

    @Test
    public void testUnionExceptIntersectAndOrderBy() throws Exception {
        new UnionTests(this).testUnionExceptIntersectAndOrderBy();
    }

    @Test
    public void testComplexUnions() throws Exception {
        new UnionTests(this).testComplexUnions();
    }

    @Test
    public void testIntersectAndExcept() throws Exception {
        new UnionTests(this).testIntersectAndExcept();
    }

    @Test
    public void testIntersectAllAndExceptAll() throws Exception {
        new UnionTests(this).testIntersectAllAndExceptAll();
    }

    @Test
    public void testValuesConstructor() throws Exception {
        new ValuesConstructorTests(this).testValuesConstructor();
    }

    @Test
    public void testResultConstructor() throws Exception {
        new ValuesConstructorTests(this).testResultConstructor();
    }

    @Test
    public void testRecordConstructor() throws Exception {
        new ValuesConstructorTests(this).testRecordConstructor();
    }

    @Test
    public void testOrderByInSubquery() throws Exception {
        new OrderByTests(this).testOrderByInSubquery();
    }

    // TODO [#2815] @Test
    public void testOrderByWithDual() throws Exception {
        new OrderByTests(this).testOrderByWithDual();
    }

    @Test
    public void testOrderByNulls() throws Exception {
        new OrderByTests(this).testOrderByNulls();
    }

    @Test
    public void testOrderByIndexes() throws Exception {
        new OrderByTests(this).testOrderByIndexes();
    }

    @Test
    public void testOrderByIndirection() throws Exception {
        new OrderByTests(this).testOrderByIndirection();
    }

    @Test
    public void testOrderByAndLimit() throws Exception {
        new OrderByTests(this).testOrderByAndLimit();
    }

    @Test
    public void testOrderByAndSeek() throws Exception {
        new OrderByTests(this).testOrderByAndSeek();
    }

    @Test
    public void testLimit() throws Exception {
        new OrderByTests(this).testLimit();
    }

    @Test
    public void testLimitWithAmbiguousColumnNames() throws Exception {
        new OrderByTests(this).testLimitWithAmbiguousColumnNames();
    }

    @Test
    public void testLimitDistinct() throws Exception {
        new OrderByTests(this).testLimitDistinct();
    }

    @Test
    public void testLimitWithLOBs() throws Exception {
        new OrderByTests(this).testLimitWithLOBs();
    }

    @Test
    public void testLimitAliased() throws Exception {
        new OrderByTests(this).testLimitAliased();
    }

    @Test
    public void testLimitBindValues() throws Exception {
        new OrderByTests(this).testLimitBindValues();
    }

    @Test
    public void testLimitNested() throws Exception {
        new OrderByTests(this).testLimitNested();
    }

    @Test
    public void testLimitNamedParams() throws Exception {
        new OrderByTests(this).testLimitNamedParams();
    }

    @Test
    public void testCTESimple() throws Exception {
        new CTETests(this).testCTESimple();
    }

    @Test
    public void testCTEMultiple() throws Exception {
        new CTETests(this).testCTEMultiple();
    }

    @Test
    public void testCTEAliasing() throws Exception {
        new CTETests(this).testCTEAliasing();
    }

    @Test
    public void testCTEWithNoExplicitColumnLists() throws Exception {
        new CTETests(this).testCTEWithNoExplicitColumnLists();
    }

    @Test
    public void testRecursiveCTESimple() throws Exception {
        new CTETests(this).testRecursiveCTESimple();
    }

    @Test
    public void testRecursiveCTEMultiple() throws Exception {
        new CTETests(this).testRecursiveCTEMultiple();
    }

    @Test
    public void testCTEWithLimit() throws Exception {
        new CTETests(this).testCTEWithLimit();
    }

    @Test
    public void testCTEWithLimitOffset() throws Exception {
        new CTETests(this).testCTEWithLimitOffset();
    }

    @Test
    public void testJoinDuplicateFieldNames() throws Exception {
        new JoinTests(this).testJoinDuplicateFieldNames();
    }

    @Test
    public void testJoinQuery() throws Exception {
        new JoinTests(this).testJoinQuery();
    }

    @Test
    public void testCrossJoin() throws Exception {
        new JoinTests(this).testCrossJoin();
    }

    @Test
    public void testCrossApply() throws Exception {
        new JoinTests(this).testCrossApply();
    }

    @Test
    public void testLateralJoin() throws Exception {
        new JoinTests(this).testLateralJoin();
    }

    @Test
    public void testNaturalJoin() throws Exception {
        new JoinTests(this).testNaturalJoin();
    }

    @Test
    public void testJoinUsing() throws Exception {
        new JoinTests(this).testJoinUsing();
    }

    @Test
    public void testJoinOnKey() throws Exception {
        new JoinTests(this).testJoinOnKey();
    }

    @Test
    public void testJoinOnKeyWithAlias() throws Exception {
        new JoinTests(this).testJoinOnKeyWithAlias();
    }

    @Test
    public void testInverseAndNestedJoin() throws Exception {
        new JoinTests(this).testInverseAndNestedJoin();
    }

    @Test
    public void testOuterJoin() throws Exception {
        new JoinTests(this).testOuterJoin();
    }

    @Test
    public void testFullOuterJoin() throws Exception {
        new JoinTests(this).testFullOuterJoin();
    }

    @Test
    public void testAliasingSimple() throws Exception {
        new AliasTests(this).testAliasingSimple();
    }

    @Test
    public void testDerivedColumnListsWithAmbiguousColumnNames() throws Exception {
        new AliasTests(this).testDerivedColumnListsWithAmbiguousColumnNames();
    }

    @Test
    public void testAliasingTablesAndFields() throws Exception {
        new AliasTests(this).testAliasingTablesAndFields();
    }

    @Test
    public void testAliasingSelectAndFields() throws Exception {
        new AliasTests(this).testAliasingSelectAndFields();
    }

    // @Test TODO [#579]: Reenable this test
    public void testAliasingJoins() throws Exception {
        new AliasTests(this).testAliasingJoins();
    }

    @Test
    public void testAliasingDelete() throws Exception {
        new AliasTests(this).testAliasingDelete();
    }

    @Test
    public void testAliasingPivot() throws Exception {
        new ExoticTests(this).testAliasingPivot();
    }

    // @Test TODO [#579]: Reenable this test
    public void testUnaliasedSubqueryProjections() throws Exception {
        new SelectTests(this).testUnaliasedSubqueryProjections();
    }

    @Test
    public void testArithmeticOperations() throws Exception {
        new FunctionTests(this).testArithmeticOperations();
    }

    @Test
    public void testBitwiseOperations() throws Exception {
        new FunctionTests(this).testBitwiseOperations();
    }

    @Test
    public void testUserDefinedAggregateFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testUserDefinedAggregateFunctions();
    }

    @Test
    public void testAggregateFunctionsSimple() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunctionsSimple();
    }

    @Test
    public void testAggregateFunctions_FILTER_CLAUSE() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunctions_FILTER_CLAUSE();
    }

    @Test
    public void testWindowFunctions_FILTER_CLAUSE() throws Exception {
        new AggregateWindowFunctionTests(this).testWindowFunctions_FILTER_CLAUSE();
    }

    @Test
    public void testAggregateFunction_MEDIAN() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunction_MEDIAN();
    }

    @Test
    public void testAggregateFunction_EVERY() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunction_EVERY();
    }

    @Test
    public void testAggregateFunction_ARRAY_AGG() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunction_ARRAY_AGG();
    }

    @Test
    public void testAggregateFunctionsStatistics() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunctionsStatistics();
    }

    @Test
    public void testOrderedAggregateFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testOrderedAggregateFunctions();
    }

    @Test
    public void testInverseDistributionFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testInverseDistributionFunctions();
    }

    @Test
    public void testFetchCount() throws Exception {
        new AggregateWindowFunctionTests(this).testFetchCount();
    }

    @Test
    public void testFetchCountWithLimitOffset() throws Exception {
        new AggregateWindowFunctionTests(this).testFetchCountWithLimitOffset();
    }

    @Test
    public void testCountDistinct() throws Exception {
        new AggregateWindowFunctionTests(this).testCountDistinct();
    }

    @Test
    public void testLinearRegressionFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testLinearRegressionFunctions();
    }

    @Test
    public void testListAgg() throws Exception {
        new AggregateWindowFunctionTests(this).testListAgg();
    }

    @Test
    public void testWindowFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testWindowFunctions();
    }

    @Test
    public void testWindowFunctionsWithRowValueExpressions_LEAD_LAG() throws Exception {
        new AggregateWindowFunctionTests(this).testWindowFunctionsWithRowValueExpressions_LEAD_LAG();
    }

    @Test
    public void testWindowClause() throws Exception {
        new AggregateWindowFunctionTests(this).testWindowClause();
    }

    @Test
    public void testStoredFunctions() throws Exception {
        new RoutineAndUDTTests(this).testStoredFunctions();
    }

    @Test
    public void testScalarSubqueryCaching() throws Exception {
        new RoutineAndUDTTests(this).testScalarSubqueryCaching();
    }

    @Test
    public void testStoredFunctionsWithNoSchema() throws Exception {
        new RoutineAndUDTTests(this).testStoredFunctionsWithNoSchema();
    }

    @Test
    public void testDateOrTimeFunction() throws Exception {
        new FunctionTests(this).testDateOrTimeFunction();
    }

    @Test
    public void testCurrentDateTime() throws Exception {
        new FunctionTests(this).testCurrentDateTime();
    }

    @Test
    public void testFunctionsOnDates_EXTRACT() throws Exception {
        new FunctionTests(this).testFunctionsOnDates_EXTRACT();
    }

    @Test
    public void testFunctionsOnDates_ARITHMETIC() throws Exception {
        new FunctionTests(this).testFunctionsOnDates_ARITHMETIC();
    }

    @Test
    public void testExtractInSubselect() throws Exception {
        new FunctionTests(this).testExtractInSubselect();
    }

    @Test
    public void testFunctionsOnNumbers_RAND() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers_RAND();
    }

    @Test
    public void testFunctionsOnNumbers_ROUND_FLOOR_CEIL_TRUNC() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers_ROUND_FLOOR_CEIL_TRUNC();
    }

    @Test
    public void testFunctionsOnNumbers_GREATEST_LEAST() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers_GREATEST_LEAST();
    }

    @Test
    public void testFunctionsOnNumbers_TRIGONOMETRY() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers_TRIGONOMETRY();
    }

    @Test
    public void testFunctionsOnNumbers_SIGN() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers_SIGN();
    }

    @Test
    public void testFunctionsOnNumbers_ABS() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers_ABS();
    }

    @Test
    public void testFunctionsOnStrings_TRIM() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_TRIM();
    }

    @Test
    public void testFunctionsOnStrings_UPPER_LOWER() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_UPPER_LOWER();
    }

    @Test
    public void testFunctionsOnStrings_CONCAT() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_CONCAT();
    }

    @Test
    public void testFunctionsOnStrings_REPLACE() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_REPLACE();
    }

    @Test
    public void testFunctionsOnStrings_LENGTH() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_LENGTH();
    }

    @Test
    public void testFunctionsOnStrings_RPAD_LPAD() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_RPAD_LPAD();
    }

    @Test
    public void testFunctionsOnStrings_SUBSTRING() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_SUBSTRING();
    }

    @Test
    public void testFunctionsOnStrings_LEFT_RIGHT() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_LEFT_RIGHT();
    }

    @Test
    public void testFunctionsOnStrings_REPEAT() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_REPEAT();
    }

    @Test
    public void testFunctionsOnStrings_SPACE() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_SPACE();
    }

    @Test
    public void testFunctionsOnStrings_REVERSE() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_REVERSE();
    }

    @Test
    public void testFunctionsOnStrings_ASCII() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_ASCII();
    }

    @Test
    public void testFunctionsOnStrings_HashFunctions() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_HashFunctions();
    }

    @Test
    public void testFunctionPosition() throws Exception {
        new FunctionTests(this).testFunctionPosition();
    }

    @Test
    public void testFunctionsLikeDecode() throws Exception {
        new FunctionTests(this).testFunctionsLikeDecode();
    }

    @Test
    public void testCaseExpression() throws Exception {
        new FunctionTests(this).testCaseExpression();
    }

    @Test
    public void testCaseExpressionWithSubquery() throws Exception {
        new FunctionTests(this).testCaseExpressionWithSubquery();
    }

    @Test
    public void testEnums() throws Exception {
        new EnumTests(this).testEnums();
    }

    @Test
    public void testFetchIntoConvertedType() throws Exception {
        new EnumTests(this).testFetchIntoConvertedType();
    }

    @Test
    public void testFetchCustomTypeIntoPOJO() throws Exception {
        new EnumTests(this).testFetchCustomTypeIntoPOJO();
    }

    @Test
    public void testUnknownEnumValue() throws Exception {
        new EnumTests(this).testUnknownEnumValue();
    }

    @Test
    public <R extends TableRecord<R>> void testCustomEnums() throws Exception {
        new EnumTests(this).testCustomEnums();
    }

    @Test
    public <R extends TableRecord<R>> void testCustomEnumsWithInline() throws Exception {
        new EnumTests(this).testCustomEnumsWithInline();
    }

    @Test
    public void testSerialisation() throws Exception {
        new SerializationTests(this).testSerialisation();
    }

    @Test
    public void testARRAYType() throws Exception {
        new RoutineAndUDTTests(this).testARRAYType();
    }

    @Test
    public void testARRAYProcedure() throws Exception {
        new RoutineAndUDTTests(this).testARRAYProcedure();
    }

    @Test
    public void testUDTs() throws Exception {
        new RoutineAndUDTTests(this).testUDTs();
    }

    @Test
    public void testUDTProcedure() throws Exception {
        new RoutineAndUDTTests(this).testUDTProcedure();
    }

    @Test
    public void testAttachable() throws Exception {
        new GeneralTests(this).testAttachable();
    }

    @Test
    public void testNULL() throws Exception {
        new GeneralTests(this).testNULL();
    }

    @Test
    public void testIsTrue() throws Exception {
        new PredicateTests(this).testIsTrue();
    }

    @Test
    public void testIsDistinctFrom() throws Exception {
        new PredicateTests(this).testIsDistinctFrom();
    }

    @Test
    public void testLike() throws Exception {
        new PredicateTests(this).testLike();
    }

    @Test
    public void testLikeWithNumbers() throws Exception {
        new PredicateTests(this).testLikeWithNumbers();
    }

    @Test
    public void testLikeRegex() throws Exception {
        new PredicateTests(this).testLikeRegex();
    }

    @Test
    public void testDualImplicit() throws Exception {
        new TableFunctionTests(this).testDualImplicit();
    }

    @Test
    public void testDualExplicit() throws Exception {
        new TableFunctionTests(this).testDualExplicit();
    }

    @Test
    public void testPackage() throws Exception {
        new RoutineAndUDTTests(this).testPackage();
    }

    @Test
    public void testStoredProcedure() throws Exception {
        new RoutineAndUDTTests(this).testStoredProcedure();
    }

    @Test
    public void testStoredProcedureWithResultSets() throws Exception {
        new RoutineAndUDTTests(this).testStoredProcedureWithResultSets();
    }

    @Test
    public void testStoredProcedureWithDefaultParameters() throws Exception {
        new RoutineAndUDTTests(this).testStoredProcedureWithDefaultParameters();
    }

    @Test
    public void testArrayTables() throws Exception {
        new RoutineAndUDTTests(this).testArrayTables();
    }

    @Test
    public void testArrayTableEmulation() throws Exception {
        new RoutineAndUDTTests(this).testArrayTableEmulation();
    }

    @Test
    public void testStoredProceduresWithCursorParameters() throws Exception {
        new RoutineAndUDTTests(this).testStoredProceduresWithCursorParameters();
    }

    @Test
    public void testGenerateSeries() throws Exception {
        new TableFunctionTests(this).testGenerateSeries();
    }

    @Test
    public void testEquals() throws Exception {
        new GeneralTests(this).testEquals();
    }

    @Test
    public void testBatchSingle() throws Exception {
        new BatchTests(this).testBatchSingle();
    }

    @Test
    public void testBatchSingleNamedParameters() throws Exception {
        new BatchTests(this).testBatchSingleNamedParameters();
    }

    @Test
    public void testBatchSingleMerge() throws Exception {
        new BatchTests(this).testBatchSingleMerge();
    }

    @Test
    public void testBatchSingleWithInlineVariables() throws Exception {
        new BatchTests(this).testBatchSingleWithInlineVariables();
    }

    @Test
    public void testBatchSinglePlainSQL() throws Exception {
        new BatchTests(this).testBatchSinglePlainSQL();
    }

    @Test
    public void testBatchSingleWithNulls() throws Exception {
        new BatchTests(this).testBatchSingleWithNulls();
    }

    @Test
    public void testBatchMultiple() throws Exception {
        new BatchTests(this).testBatchMultiple();
    }

    @Test
    public void testBatchStore() throws Exception {
        new BatchTests(this).testBatchStore();
    }

    @Test
    public void testBatchInsertUpdate() throws Exception {
        new BatchTests(this).testBatchInsertUpdate();
    }

    @Test
    public void testBatchStoreWithUDTs() throws Exception {
        new BatchTests(this).testBatchStoreWithUDTs();
    }

    @Test
    public void testBatchDelete() throws Exception {
        new BatchTests(this).testBatchDelete();
    }

    @Test
    public void testBatchDeleteWithExecuteListener() throws Exception {
        new BatchTests(this).testBatchDeleteWithExecuteListener();
    }

    @Test
    public void testRenderFormattedAndInlinedWithNewlines() throws Exception {
        new RenderAndBindTests(this).testRenderFormattedAndInlinedWithNewlines();
    }

    @Test
    public void testNamedParams() throws Exception {
        new RenderAndBindTests(this).testNamedParams();
    }

    @Test
    public void testUnknownBindTypes() throws Exception {
        new RenderAndBindTests(this).testUnknownBindTypes();
    }

    @Test
    public void testManyVarcharBindValues() throws Exception {
        new RenderAndBindTests(this).testManyVarcharBindValues();
    }

    @Test
    public void testSelectBindValues() throws Exception {
        new RenderAndBindTests(this).testSelectBindValues();
    }

    @Test
    public void testReuseNamedBindValues() throws Exception {
        new RenderAndBindTests(this).testReuseNamedBindValues();
    }

    @Test
    public void testSelectBindValuesWithPlainSQL() throws Exception {
        new RenderAndBindTests(this).testSelectBindValuesWithPlainSQL();
    }

    @Test
    public void testInlinedBindValues() throws Exception {
        new RenderAndBindTests(this).testInlinedBindValues();
    }

    @Test
    public void testInlinedBindValuesForNumberTypes() throws Exception {
        new RenderAndBindTests(this).testInlinedBindValuesForNumberTypes();
    }

    @Test
    public void testInlinedBindValuesForDatetime() throws Exception {
        new RenderAndBindTests(this).testInlinedBindValuesForDatetime();
    }

    @Test
    public void testReusingBindValueReference() throws Exception {
        new RenderAndBindTests(this).testReusingBindValueReference();
    }

    @Test
    public void testUUIDDataType() throws Exception {
        new DataTypeTests(this).testUUIDDataType();
    }

    @Test
    public void testUUIDArrayDataType() throws Exception {
        new DataTypeTests(this).testUUIDArrayDataType();
    }

    @Test
    public void testXMLasDOM() throws Exception {
        new DataTypeTests(this).testXMLasDOM();
    }

    @Test
    public void testXMLasJAXB() throws Exception {
        new DataTypeTests(this).testXMLasJAXB();
    }

    @Test
    public void testXMLusingPlainSQLConverters() throws Exception {
        new DataTypeTests(this).testXMLusingPlainSQLConverters();
    }

    @Test
    public void testXMLusingPlainSQLBindings() throws Exception {
        new DataTypeTests(this).testXMLusingPlainSQLBindings();
    }

    @Test
    public void testTableWithHint() throws Exception {
        new ExoticTests(this).testTableWithHint();
    }

    @Test
    public void testPivotClause() throws Exception {
        new ExoticTests(this).testPivotClause();
    }

    @Test
    public void testRelationalDivision() throws Exception {
        new ExoticTests(this).testRelationalDivision();
    }

    @Test
    public void testAliasingRelationalDivision() throws Exception {
        new ExoticTests(this).testAliasingRelationalDivision();
    }

    @Test
    public void testConnectBySimple() throws Exception {
        new ExoticTests(this).testConnectBySimple();
    }

    @Test
    public void testConnectByDirectory() throws Exception {
        new ExoticTests(this).testConnectByDirectory();
    }

    @Test
    public void testWithCheckOption() throws Exception {
        new ExoticTests(this).testWithCheckOption();
    }

    @Test
    public void testWithReadOnly() throws Exception {
        new ExoticTests(this).testWithReadOnly();
    }

    @Test
    public void testExecuteListenerRows() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerRows();
    }

    @Test
    public void testExecuteListenerOnResultQuery() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerOnResultQuery();
    }

    @Test
    public void testExecuteListenerWithData() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerWithData();
    }

    @Test
    public void testExecuteListenerException() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerException();
    }

    @Test
    public void testExecuteListenerCustomExceptionOnSyntaxError() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerCustomExceptionOnSyntaxError();
    }

    @Test
    public void testExecuteListenerCustomExceptionOnConstraintViolation() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerCustomExceptionOnConstraintViolation();
    }

    @Test
    public void testExecuteListenerOnBatchSingle() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerOnBatchSingle();
    }

    @Test
    public void testExecuteListenerOnBatchMultiple() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerOnBatchMultiple();
    }

    // @Test TODO [#1868] Re-enable this test
    public void testExecuteListenerFetchLazyTest() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerFetchLazyTest();
    }

    @Test
    public void testExecuteListenerDELETEorUPDATEwithoutWHERE() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerDELETEorUPDATEwithoutWHERE();
    }

    @Test
    public void testRenderNameStyle() throws Exception {
        new RenderAndBindTests(this).testRenderNameStyle();
    }

    @Test
    public void testRenderKeywordStyle() throws Exception {
        new RenderAndBindTests(this).testRenderKeywordStyle();
    }

    @Test
    public void testRowLoader() throws Exception {
        new RowLoaderTests(this).testLoader();
    }

    @Test
    public void testRecordLoader() throws Exception {
        new RecordLoaderTests(this).testLoader();
    }

    @Test
    public void testCsvLoader() throws Exception {
        new CsvLoaderTests(this).testLoader();
    }

    @Test
    public void testCsvLoaderConverter() throws Exception {
        new CsvLoaderTests(this).testLoaderConverter();
    }

    @Test
    public void testCsvLoaderBulkAll() throws Exception {
        new CsvLoaderTests(this).testCsvLoaderBulkAll();
    }

    @Test
    public void testCsvLoaderBulkAfter() throws Exception {
        new CsvLoaderTests(this).testCsvLoaderBulkAfter();
    }

    @Test
    public void testCsvLoaderBatchAll() throws Exception {
        new CsvLoaderTests(this).testCsvLoaderBatchAll();
    }

    @Test
    public void testCsvLoaderBatchAfter() throws Exception {
        new CsvLoaderTests(this).testCsvLoaderBatchAfter();
    }

    @Test
    public void testCsvLoaderBatchAndBulk() throws Exception {
        new CsvLoaderTests(this).testCsvLoaderBatchAndBulk();
    }

    @Test
    public void testJsonLoader() throws Exception {
        new JsonLoaderTests(this).testLoader();
    }

    @Test
    public void testBenchmarkNewRecord() throws Exception {
        new BenchmarkTests(this).testBenchmarkNewRecord();
    }

    @Test
    public void testBenchmarkRecordIntoWithAnnotations() throws Exception {
        new BenchmarkTests(this).testBenchmarkRecordIntoWithAnnotations();
    }

    @Test
    public void testBenchmarkRecordIntoWithoutAnnotations() throws Exception {
        new BenchmarkTests(this).testBenchmarkRecordIntoWithoutAnnotations();
    }

    @Test
    public void testBenchmarkRecordIntoTableRecord() throws Exception {
        new BenchmarkTests(this).testBenchmarkRecordIntoTableRecord();
    }

    @Test
    public void testBenchmarkFieldAccess() throws Exception {
        new BenchmarkTests(this).testBenchmarkFieldAccess();
    }

    @Test
    public void testBenchmarkSelect() throws Exception {
        new BenchmarkTests(this).testBenchmarkSelect();
    }

    @Test
    public void testBenchmarkPlainSQL() throws Exception {
        new BenchmarkTests(this).testBenchmarkPlainSQL();
    }

    @Test
    public void testKeepStatement() throws Exception {
        new StatementTests(this).testKeepStatement();
    }

    @Test
    public void testKeepStatementWithConnectionPool() throws Exception {
        new StatementTests(this).testKeepStatementWithConnectionPool();
    }

    @Test
    public void testCancelStatement() throws Exception {
        new StatementTests(this).testCancelStatement();
    }

    @Test
    public void testDialectGuessing() throws Exception {
        new JDBCTests(this).testDialectGuessing();
    }

    @Test
    public void testDriverGuessing() throws Exception {
        new JDBCTests(this).testDriverGuessing();
    }

    @Test
    public void testTransactionsWithJDBCSimple() throws Exception {
        new TransactionTests(this).testTransactionsWithJDBCSimple();
    }

    @Test
    public void testTransactionsWithJDBCCheckedException() throws Exception {
        new TransactionTests(this).testTransactionsWithJDBCCheckedException();
    }

    @Test
    public void testTransactionsWithJDBCNestedWithSavepoints() throws Exception {
        new TransactionTests(this).testTransactionsWithJDBCNestedWithSavepoints();
    }

    @Test
    public void testTransactionsWithJDBCNestedWithoutSavepoints() throws Exception {
        new TransactionTests(this).testTransactionsWithJDBCNestedWithoutSavepoints();
    }

    @Test
    public void testTransactionsWithExceptionInRollback() throws Exception {
        new TransactionTests(this).testTransactionsWithExceptionInRollback();
    }

    @Test
    public void testStreamsCollectRecords() throws Exception {
        new StreamsTest(this).testStreamsCollectRecords();
    }

    @Test
    public void testStreamsReduceResultsIntoBatch() throws Exception {
        new StreamsTest(this).testStreamsReduceResultsIntoBatch();
    }

    @Test
    public void testStreamsCollectPOJOs() throws Exception {
        new StreamsTest(this).testStreamsCollectPOJOs();
    }

    @Test
    public void testCompletableFuture() throws Exception {
        new AsyncTest(this).testCompletableFuture();
    }

    @Test
    public void testResultCache() throws Exception {
        new MockTests(this).testResultCache();
    }

    @Test
    public void testResultCacheWithMockAPI() throws Exception {
        new MockTests(this).testResultCacheWithMockAPI();
    }

    @Test
    public void testJPANativeQuery() throws Exception {
        new JPAIntegrationTests(this).testJPANativeQuery();
    }

    @Test
    public void testJPANativeQueryAndEntites() throws Exception {
        new JPAIntegrationTests(this).testJPANativeQueryAndEntites();
    }

    @Test
    public void testJPANativeQueryAndSqlResultSetMapping() throws Exception {
        new JPAIntegrationTests(this).testJPANativeQueryAndSqlResultSetMapping();
    }

    @Test
    public void testCollations() throws Exception {
        new CollationTests(this).testCollations();
    }

    @Test
    public void testConnectionProviderForInsertReturning() throws Exception {
        new ConnectionProviderTests(this).testConnectionProviderForInsertReturning();
    }
}
