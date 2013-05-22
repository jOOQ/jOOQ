/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.tools.reflect.Reflect.on;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.swing.UIManager;

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
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.debug.Debugger;
import org.jooq.debug.console.Console;
import org.jooq.debug.impl.DebuggerFactory;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.test._.LifecycleWatcherListener;
import org.jooq.test._.PrettyPrinter;
import org.jooq.test._.TestStatisticsListener;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;
import org.jooq.test._.testcases.AggregateWindowFunctionTests;
import org.jooq.test._.testcases.AliasTests;
import org.jooq.test._.testcases.BatchTests;
import org.jooq.test._.testcases.BenchmarkTests;
import org.jooq.test._.testcases.CRUDTests;
import org.jooq.test._.testcases.DaoTests;
import org.jooq.test._.testcases.DataTypeTests;
import org.jooq.test._.testcases.EnumTests;
import org.jooq.test._.testcases.ExecuteListenerTests;
import org.jooq.test._.testcases.ExoticTests;
import org.jooq.test._.testcases.FetchTests;
import org.jooq.test._.testcases.FormatTests;
import org.jooq.test._.testcases.FunctionTests;
import org.jooq.test._.testcases.GeneralTests;
import org.jooq.test._.testcases.GroupByTests;
import org.jooq.test._.testcases.InsertUpdateTests;
import org.jooq.test._.testcases.JoinTests;
import org.jooq.test._.testcases.LoaderTests;
import org.jooq.test._.testcases.MetaDataTests;
import org.jooq.test._.testcases.OrderByTests;
import org.jooq.test._.testcases.PlainSQLTests;
import org.jooq.test._.testcases.PredicateTests;
import org.jooq.test._.testcases.RecordTests;
import org.jooq.test._.testcases.ReferentialTests;
import org.jooq.test._.testcases.RenderAndBindTests;
import org.jooq.test._.testcases.ResultTests;
import org.jooq.test._.testcases.RoutineAndUDTTests;
import org.jooq.test._.testcases.RowValueExpressionTests;
import org.jooq.test._.testcases.SchemaAndMappingTests;
import org.jooq.test._.testcases.SelectTests;
import org.jooq.test._.testcases.StatementTests;
import org.jooq.test._.testcases.ThreadSafetyTests;
import org.jooq.test._.testcases.ValuesConstructorTests;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
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
import org.postgresql.util.PSQLException;

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
        A extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,

        // T_AUTHOR pojo
        AP,

        // T_BOOK table
        B extends UpdatableRecord<B>,

        // T_BOOK_STORE table
        S extends UpdatableRecord<S> & Record1<String>,

        // T_BOOK_TO_BOOK_STORE table
        B2S extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,

        // MULTI_SCHEMA.T_BOOK_SALE table
        BS extends UpdatableRecord<BS>,

        // V_LIBRARY view
        L extends TableRecord<L> & Record2<String, String>,

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

        // T_1624_UUID table
        UU extends UpdatableRecord<UU>,

        // T_IDENTITY table
        I extends TableRecord<I>,

        // T_IDENTITY_PK table
        IPK extends UpdatableRecord<IPK>,

        // Various tables related to trac ticket numbers
        T725 extends UpdatableRecord<T725>,
        T639 extends UpdatableRecord<T639>,
        T785 extends TableRecord<T785>> {

    protected static final List<Short>   BOOK_IDS_SHORT         = Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4);
    protected static final List<Integer> BOOK_IDS               = Arrays.asList(1, 2, 3, 4);
    protected static final List<Integer> BOOK_AUTHOR_IDS        = Arrays.asList(1, 1, 2, 2);
    protected static final List<String>  BOOK_TITLES            = Arrays.asList("1984", "Animal Farm", "O Alquimista", "Brida");
    protected static final List<String>  BOOK_FIRST_NAMES       = Arrays.asList("George", "George", "Paulo", "Paulo");
    protected static final List<String>  BOOK_LAST_NAMES        = Arrays.asList("Orwell", "Orwell", "Coelho", "Coelho");
    protected static final List<Integer> AUTHOR_IDS             = Arrays.asList(1, 2);
    protected static final List<String>  AUTHOR_FIRST_NAMES     = Arrays.asList("George", "Paulo");
    protected static final List<String>  AUTHOR_LAST_NAMES      = Arrays.asList("Orwell", "Coelho");

    public static final JooqLogger       log                    = JooqLogger.getLogger(jOOQAbstractTest.class);
    public static final StopWatch        testSQLWatch           = new StopWatch();
    public static boolean                initialised;
    public static boolean                reset;
    public static Connection             connection;
    public static boolean                connectionInitialised;
    public static Connection             connectionMultiSchema;
    public static boolean                connectionMultiSchemaInitialised;
    public static Connection             connectionMultiSchemaUnused;
    public static boolean                connectionMultiSchemaUnusedInitialised;
    public static boolean                autocommit;
    public static String                 jdbcURL;
    public static String                 jdbcSchema;
    public static Map<String, String>    scripts                = new HashMap<String, String>();

    public static final int              DEBUGGER_PORT          = 5533;
    public static boolean                RUN_CONSOLE_IN_PROCESS = false;

    protected void execute(String script) throws Exception {
        Statement stmt = null;

        String allSQL = scripts.get(script);
        if (allSQL == null) {
            try {
                log.info("Loading", script);
                File file = new File(getClass().getResource(script).toURI());
                allSQL = FileUtils.readFileToString(file);
                testSQLWatch.splitDebug("Loaded SQL file");
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
                    testSQLWatch.splitDebug(StringUtils.abbreviate(sql.trim().replaceAll("[\\n\\r]|\\s+", " "), 25));
                }
            }
            catch (Exception e) {
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

                // There is no DROP ** IF EXISTS statement in SQL Server
                else if (e.getClass().getName().startsWith("com.microsoft")) {
                    switch (((SQLServerException)e).getErrorCode()) {
                        case 3701: // Tables
                        case 218:  // Types
                        continue;
                    }
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

                // There are no IF EXISTS clauses in Sybase ASE
                else if (e.getMessage().contains("doesn't exist") && getDialect() == SQLDialect.ASE) {
                    continue;
                }

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

    @Before
    public void setUp() throws Exception {
        connection = getConnection();
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
    }

    @After
    public void tearDown() throws Exception {
        connection.setAutoCommit(autocommit);
    }

    @BeforeClass
    public static void testStart() {
        log.info("STARTING");
    }

    @AfterClass
    public static void quit() throws Exception {
        log.info("QUITTING");

        // Issue a log dump on adaptive server. Don't know why this is needed
        // http://www.faqs.org/faqs/databases/sybase-faq/part6/
        if (connection.getClass().getPackage().getName().contains("jtds")) {
            log.info("RUNNING", "dump tran TEST with truncate_only");
            connection.createStatement().execute("dump tran TEST with truncate_only");
        }

        connection.close();

        log.info("TEST STATISTICS");
        log.info("---------------");

        int total = 0;
        for (ExecuteType type : ExecuteType.values()) {
            Integer count = TestStatisticsListener.STATISTICS.get(type);
            if (count == null) count = 0;
            total += count;

            log.info(type.name(), count + " executions");
        }

        log.info("---------------");
        log.info("Total", total);

        log.info("");
        log.info("EXECUTE LIFECYCLE STATS");
        log.info("-----------------------");

        int unbalanced = 0;
        for (Method m : LifecycleWatcherListener.START_COUNT.keySet()) {
            Integer starts = LifecycleWatcherListener.START_COUNT.get(m);
            Integer ends = LifecycleWatcherListener.END_COUNT.get(m);

            if (!StringUtils.equals(starts, ends)) {
                unbalanced++;

                log.info(
                    "Unbalanced", String.format("(start, end): (%1$3s, %2$3s) at %3$s",
                        starts,
                        ends == null ? 0 : ends,
                        m.toString().replace("public void ", "").replaceAll("( throws.*)?", "")));
            }
        }

        log.info("Unbalanced test: ", unbalanced);
    }

    @SuppressWarnings("unused")
    public final Connection getConnection() {
        if (!connectionInitialised) {
            connectionInitialised = true;
            connection = getConnection0(null, null);
            final Connection c = connection;

            // Reactivate this, to enable mock connections
            if (false)
            connection = new MockConnection(new MockDataProvider() {

                @Override
                public MockResult[] execute(MockExecuteContext context) throws SQLException {
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
                        List<Result<Record>> result = executor.fetchMany(context.sql(), context.bindings());
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
                }
            });

            if (RUN_CONSOLE_IN_PROCESS) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    Debugger debugger = DebuggerFactory.remoteDebugger("127.0.0.1", DEBUGGER_PORT);
                    Console console = new Console(debugger, true, true);
                    console.setLoggingActive(true);
                    console.setVisible(true);
                }
                catch (Exception ignore) {}
            }
        }

        return connection;
    }

    public final Connection getConnectionMultiSchema() {
        if (!connectionMultiSchemaInitialised) {
            connectionMultiSchemaInitialised = true;
            connectionMultiSchema = getConnection0("MULTI_SCHEMA", "MULTI_SCHEMA");
        }

        return connectionMultiSchema;
    }

    public final Connection getConnectionMultiSchemaUnused() {
        if (!connectionMultiSchemaUnusedInitialised) {
            connectionMultiSchemaUnusedInitialised = true;
            connectionMultiSchemaUnused = getConnection0("MULTI_SCHEMA_UNUSED", "MULTI_SCHEMA_UNUSED");
        }

        return connectionMultiSchemaUnused;
    }

    private final String getProperty(List<Property> properties, String key) {
        for (Property p : properties) {
            if (p.getKey().equals(key)) {
                return p.getValue();
            }
        }

        return null;
    }

    final Connection getConnection0(String jdbcUser, String jdbcPassword) {
        try {
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

            Class.forName(driver);
            if (StringUtils.isBlank(jdbcUser)) {
                Properties p = new Properties();

                if (getClass().getSimpleName().toLowerCase().contains("ingres")) {
                    p.setProperty("timezone", "EUROPE-CENTRAL");
                }

                return DriverManager.getConnection(getJdbcURL(), p);
            }
            else {
                return DriverManager.getConnection(getJdbcURL(), jdbcUser, jdbcPassword);
            }
        }
        catch (Exception e) {
            throw new Error(e);
        }
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

    protected abstract Table<U> TUnsigned();
    protected abstract TableField<U, UByte> TUnsigned_U_BYTE();
    protected abstract TableField<U, UShort> TUnsigned_U_SHORT();
    protected abstract TableField<U, UInteger> TUnsigned_U_INT();
    protected abstract TableField<U, ULong> TUnsigned_U_LONG();

    protected abstract Table<UU> TExoticTypes();
    protected abstract TableField<UU, Integer> TExoticTypes_ID();
    protected abstract TableField<UU, UUID> TExoticTypes_UUID();

    protected abstract Table<DATE> TDates();

    protected abstract Table<X> TArrays();
    protected abstract TableField<X, Integer> TArrays_ID();
    protected abstract TableField<X, String[]> TArrays_STRING();
    protected abstract TableField<X, Integer[]> TArrays_NUMBER();
    protected abstract TableField<X, Date[]> TArrays_DATE();
    protected abstract TableField<X, ? extends UDTRecord<?>[]> TArrays_UDT();
    protected abstract TableField<X, ? extends ArrayRecord<String>> TArrays_STRING_R();
    protected abstract TableField<X, ? extends ArrayRecord<Integer>> TArrays_NUMBER_R();
    protected abstract TableField<X, ? extends ArrayRecord<Long>> TArrays_NUMBER_LONG_R();
    protected abstract TableField<X, ? extends ArrayRecord<Date>> TArrays_DATE_R();

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
    protected abstract <Z extends ArrayRecord<Integer>> Field<Z> FArrays1Field_R(Field<Z> array);
    protected abstract <Z extends ArrayRecord<Long>> Field<Z> FArrays2Field_R(Field<Z> array);
    protected abstract <Z extends ArrayRecord<String>> Field<Z> FArrays3Field_R(Field<Z> array);

    protected abstract boolean supportsOUTParameters();
    protected abstract boolean supportsReferences();
    protected abstract boolean supportsRecursiveQueries();
    protected abstract Class<?> cRoutines();
    protected abstract Class<?> cLibrary();
    protected abstract Class<?> cSequences();
    protected abstract DataType<?>[] getCastableDataTypes();
    protected abstract DSLContext create0(Settings settings);

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

        return DSL.using(create0(settings).configuration().derive(
            DefaultExecuteListenerProvider.providers(
                new TestStatisticsListener(),
                new PrettyPrinter(),
                new LifecycleWatcherListener()
            )
        ));
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
    public void testInsertIdentity() throws Exception {
        new InsertUpdateTests(this).testInsertIdentity();
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
    public void testFetchResultSetWithCoercedTypes() throws Exception {
        new FetchTests(this).testFetchResultSetWithCoercedTypes();
    }

    @Test
    public void testFetchIntoResultSet() throws Exception {
        new FetchTests(this).testFetchIntoResultSet();
    }

    @Test
    public void testFetchLazy() throws Exception {
        new FetchTests(this).testFetchLazy();
    }

    @Test
    public void testFetchMap() throws Exception {
        new FetchTests(this).testFetchMap();
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
    public void testFetchArray() throws Exception {
        new FetchTests(this).testFetchArray();
    }

    @Test
    public void testFetchGroupsPOJO() throws Exception {
        new FetchTests(this).testFetchGroupsPOJO();
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
    public void testTruncate() throws Exception {
        new InsertUpdateTests(this).testTruncate();
    }

    @Test
    public void testMetaModel() throws Exception {
        new MetaDataTests(this).testMetaModel();
    }

    @Test
    public void testMetaData() throws Exception {
        new MetaDataTests(this).testMetaData();
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
    public void testPlainSQLResultQuery() throws Exception {
        new PlainSQLTests(this).testPlainSQLResultQuery();
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
    public void testForUpdateClauses() throws Exception {
        new SelectTests(this).testForUpdateClauses();
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
    public void testConditionsAsFields() throws Exception {
        new PredicateTests(this).testConditionsAsFields();
    }

    @Test
    public void testQuantifiedPredicates() throws Exception {
        new PredicateTests(this).testQuantifiedPredicates();
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
    public void testRowValueExpressionOrderingConditions() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionOrderingConditions();
    }

    @Test
    public void testRowValueExpressionOrderingSubselects() throws Exception {
        new RowValueExpressionTests(this).testRowValueExpressionOrderingSubselects();
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
    public void testIgnoreCase() throws Exception {
        new PredicateTests(this).testIgnoreCase();
    }

    @Test
    public void testLargeINCondition() throws Exception {
        new PredicateTests(this).testLargeINCondition();
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
    public void testFetchIntoTableRecordsWithUDTs() throws Exception {
        new FetchTests(this).testFetchIntoTableRecordsWithUDTs();
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
    public void testFetchIntoCustomTable() throws Exception {
        new FetchTests(this).testFetchIntoCustomTable();
    }

    @Test
    public void testFetchIntoGeneratedPojos() throws Exception {
        new FetchTests(this).testFetchIntoGeneratedPojos();
    }

    @Test
    public void testFetchIntoRecordHandler() throws Exception {
        new FetchTests(this).testFetchIntoRecordHandler();
    }

    @Test
    public void testFetchIntoRecordMapper() throws Exception {
        new FetchTests(this).testFetchIntoRecordMapper();
    }

    @Test
    public void testFetchLater() throws Exception {
        new FetchTests(this).testFetchLater();
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
    public void testRecordReset() throws Exception {
        new RecordTests(this).testRecordReset();
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
    public void testUpdateJoin() throws Exception {
        new InsertUpdateTests(this).testUpdateJoin();
    }

    @Test
    public void testInsertOnDuplicateKeyUpdate() throws Exception {
        new InsertUpdateTests(this).testInsertOnDuplicateKeyUpdate();
    }

    @Test
    public void testInsertOnDuplicateKeyIgnore() throws Exception {
        new InsertUpdateTests(this).testInsertOnDuplicateKeyIgnore();
    }

    @Test
    public void testInsertReturning() throws Exception {
        new InsertUpdateTests(this).testInsertReturning();
    }

    // @Test
    public void testInsertReturningWithPlainSQL() throws Exception {
        new InsertUpdateTests(this).testInsertReturningWithPlainSQL();
    }

    @Test
    public void testUpdateReturning() throws Exception {
        new InsertUpdateTests(this).testUpdateReturning();
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
    public void testDateTimeArithmetic() throws Exception {
        new DataTypeTests(this).testDateTimeArithmetic();
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
    public void testUpdatablesKeysMethod() throws Exception {
        new CRUDTests(this).testUpdatablesKeysMethod();
    }

    @Test
    public void testUpdatablesInsertUpdate() throws Exception {
        new CRUDTests(this).testUpdatablesInsertUpdate();
    }

    @Test
    public void testUpdatablesPK() throws Exception {
        new CRUDTests(this).testUpdatablesPK();
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
    public void testUpdatablesVersionAndTimestamp() throws Exception {
        new CRUDTests(this).testUpdatablesVersionAndTimestamp();
    }

    @Test
    public void testStoreWithOptimisticLock() throws Exception {
        new CRUDTests(this).testStoreWithOptimisticLock();
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
    public void testFormatXML() throws Exception {
        new FormatTests(this).testFormatXML();
    }

    @Test
    public void testIntoXML() throws Exception {
        new FormatTests(this).testIntoXML();
    }

    @Test
    public void testCombinedSelectQuery() throws Exception {
        new SelectTests(this).testCombinedSelectQuery();
    }

    @Test
    public void testComplexUnions() throws Exception {
        new SelectTests(this).testComplexUnions();
    }

    @Test
    public void testValuesConstructor() throws Exception {
        new ValuesConstructorTests(this).testValuesConstructor();
    }

    @Test
    public void testOrderByInSubquery() throws Exception {
        new OrderByTests(this).testOrderByInSubquery();
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
    public void testLimit() throws Exception {
        new OrderByTests(this).testLimit();
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
    public void testInverseAndNestedJoin() throws Exception {
        new JoinTests(this).testInverseAndNestedJoin();
    }

    @Test
    public void testOuterJoin() throws Exception {
        new JoinTests(this).testOuterJoin();
    }

    @Test
    public void testAliasingSimple() throws Exception {
        new AliasTests(this).testAliasingSimple();
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
    public void testAggregateFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testAggregateFunctions();
    }

    @Test
    public void testFetchCount() throws Exception {
        new AggregateWindowFunctionTests(this).testFetchCount();
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
    public void testStoredFunctions() throws Exception {
        new RoutineAndUDTTests(this).testStoredFunctions();
    }

    @Test
    public void testFunctionsOnDates() throws Exception {
        new FunctionTests(this).testFunctionsOnDates();
    }

    @Test
    public void testExtractInSubselect() throws Exception {
        new FunctionTests(this).testExtractInSubselect();
    }

    @Test
    public void testFunctionsOnNumbers() throws Exception {
        new FunctionTests(this).testFunctionsOnNumbers();
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
    public void testFunctionsOnStrings_RPAD_LPAD() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_RPAD_LPAD();
    }

    @Test
    public void testFunctionsOnStrings_SUBSTRING() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_SUBSTRING();
    }

    @Test
    public void testFunctionsOnStrings_REPEAT() throws Exception {
        new FunctionTests(this).testFunctionsOnStrings_REPEAT();
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
    public void testCaseStatement() throws Exception {
        new FunctionTests(this).testCaseStatement();
    }

    @Test
    public void testEnums() throws Exception {
        new EnumTests(this).testEnums();
    }

    @Test
    public <R extends TableRecord<R>> void testCustomEnums() throws Exception {
        new EnumTests(this).testCustomEnums();
    }

    @Test
    public void testSerialisation() throws Exception {
        new GeneralTests(this).testSerialisation();
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
    public void testLikeRegex() throws Exception {
        new PredicateTests(this).testLikeRegex();
    }

    @Test
    public void testDual() throws Exception {
        new GeneralTests(this).testDual();
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
    public void testStoredProcedureWithDefaultParameters() throws Exception {
        new RoutineAndUDTTests(this).testStoredProcedureWithDefaultParameters();
    }

    @Test
    public void testArrayTables() throws Exception {
        new RoutineAndUDTTests(this).testArrayTables();
    }

    @Test
    public void testArrayTableSimulation() throws Exception {
        new RoutineAndUDTTests(this).testArrayTableSimulation();
    }

    @Test
    public void testStoredProceduresWithCursorParameters() throws Exception {
        new RoutineAndUDTTests(this).testStoredProceduresWithCursorParameters();
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
    public void testUUIDDataType() throws Exception {
        new DataTypeTests(this).testUUIDDataType();
    }

    @Test
    public void testUUIDArrayDataType() throws Exception {
        new DataTypeTests(this).testUUIDArrayDataType();
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
    public void testExecuteListenerCustomException() throws Exception {
        new ExecuteListenerTests(this).testExecuteListenerCustomException();
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
    public void testRenderNameStyle() throws Exception {
        new RenderAndBindTests(this).testRenderNameStyle();
    }

    @Test
    public void testRenderKeywordStyle() throws Exception {
        new RenderAndBindTests(this).testRenderKeywordStyle();
    }

    @Test
    public void testLoader() throws Exception {
        new LoaderTests(this).testLoader();
    }

    @Test
    public void testBenchmarkNewRecord() throws Exception {
        new BenchmarkTests(this).testBenchmarkNewRecord();
    }

    @Test
    public void testBenchmarkRecordInto() throws Exception {
        new BenchmarkTests(this).testBenchmarkRecordInto();
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
    public void testKeepStatement() throws Exception {
        new StatementTests(this).testKeepStatement();
    }

    @Test
    public void testCancelStatement() throws Exception {
        new StatementTests(this).testCancelStatement();
    }
}
