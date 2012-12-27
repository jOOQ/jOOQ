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
package org.jooq.test;

import static junit.framework.Assert.assertEquals;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.UIManager;

import org.jooq.ArrayRecord;
import org.jooq.DAO;
import org.jooq.DataType;
import org.jooq.ExecuteType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdatableTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.debug.DebugListener;
import org.jooq.debug.Debugger;
import org.jooq.debug.console.Console;
import org.jooq.debug.console.remote.ClientDebugger;
import org.jooq.debug.console.remote.RemoteDebuggerServer;
import org.jooq.impl.Factory;
import org.jooq.test._.TestStatisticsListener;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;
import org.jooq.test._.testcases.AggregateWindowFunctionTests;
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
import org.jooq.test._.testcases.OrderByTests;
import org.jooq.test._.testcases.PlainSQLTests;
import org.jooq.test._.testcases.PredicateTests;
import org.jooq.test._.testcases.RenderAndBindTests;
import org.jooq.test._.testcases.ResultTests;
import org.jooq.test._.testcases.RoutineAndUDTTests;
import org.jooq.test._.testcases.SchemaAndMappingTests;
import org.jooq.test._.testcases.SelectTests;
import org.jooq.test._.testcases.ThreadSafetyTests;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.unsigned.UByte;
import org.jooq.tools.unsigned.UInteger;
import org.jooq.tools.unsigned.ULong;
import org.jooq.tools.unsigned.UShort;
import org.jooq.util.GenerationTool;

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
        A extends UpdatableRecord<A>,

        // T_AUTHOR pojo
        AP,

        // T_BOOK table
        B extends UpdatableRecord<B>,

        // T_BOOK_STORE table
        S extends UpdatableRecord<S>,

        // T_BOOK_TO_BOOK_STORE table
        B2S extends UpdatableRecord<B2S>,

        // MULTI_SCHEMA.T_BOOK_SALE table
        BS extends UpdatableRecord<BS>,

        // V_LIBRARY view
        L extends TableRecord<L>,

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

        // T_IDENTITY table
        I extends TableRecord<I>,

        // T_IDENTITY_PK table
        IPK extends UpdatableRecord<IPK>,

        // Various tables related to trac ticket numbers
        T658 extends TableRecord<T658>,
        T725 extends UpdatableRecord<T725>,
        T639 extends UpdatableRecord<T639>,
        T785 extends TableRecord<T785>> {

    protected static final List<Short>     BOOK_IDS_SHORT     = Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4);
    protected static final List<Integer>   BOOK_IDS           = Arrays.asList(1, 2, 3, 4);
    protected static final List<Integer>   BOOK_AUTHOR_IDS    = Arrays.asList(1, 1, 2, 2);
    protected static final List<String>    BOOK_TITLES        = Arrays.asList("1984", "Animal Farm", "O Alquimista", "Brida");
    protected static final List<String>    BOOK_FIRST_NAMES   = Arrays.asList("George", "George", "Paulo", "Paulo");
    protected static final List<String>    BOOK_LAST_NAMES    = Arrays.asList("Orwell", "Orwell", "Coelho", "Coelho");
    protected static final List<Integer>   AUTHOR_IDS         = Arrays.asList(1, 2);
    protected static final List<String>    AUTHOR_FIRST_NAMES = Arrays.asList("George", "Paulo");
    protected static final List<String>    AUTHOR_LAST_NAMES  = Arrays.asList("Orwell", "Coelho");

    private static final String           JDBC_SCHEMA        = "jdbc.Schema";
    private static final String           JDBC_PASSWORD      = "jdbc.Password";
    private static final String           JDBC_USER          = "jdbc.User";
    private static final String           JDBC_URL           = "jdbc.URL";
    private static final String           JDBC_DRIVER        = "jdbc.Driver";
    private static final int              DEBUGGER_PORT      = 5533;

    public static final JooqLogger        log                = JooqLogger.getLogger(jOOQAbstractTest.class);
    public static final StopWatch         testSQLWatch       = new StopWatch();
    public static boolean                 initialised;
    public static boolean                 reset;
    public static Connection              connection;
    public static boolean                 connectionInitialised;
    public static Connection              connectionMultiSchema;
    public static boolean                 connectionMultiSchemaInitialised;
    public static Connection              connectionMultiSchemaUnused;
    public static boolean                 connectionMultiSchemaUnusedInitialised;
    public static boolean                 autocommit;
    public static String                  jdbcURL;
    public static String                  jdbcSchema;
    public static Map<String, String>     scripts            = new HashMap<String, String>();

    private static RemoteDebuggerServer   SERVER;

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
                    sql = sql.replace("{" + JDBC_SCHEMA + "}", jdbcSchema);

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
                    if ("42883".equals(((PSQLException) e).getSQLState())) {
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

    @BeforeClass
    public static void sqlConsole() throws Exception {
        SERVER = new RemoteDebuggerServer(DEBUGGER_PORT);
    }

    @Before
    public void setUp() throws Exception {
        connection = getConnection();
        // connectionMultiSchema = getConnectionMultiSchema();

        autocommit = connection.getAutoCommit();

        if (!initialised) {
            initialised = true;
            execute(getCreateScript());
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

    @AfterClass
    public static void quit() throws Exception {
        SERVER.close();

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
    }

    public final Connection getConnection() {
        if (!connectionInitialised) {
            connectionInitialised = true;
            connection = getConnection0(null, null);

            boolean runConsoleInProcess = false;
            if (runConsoleInProcess) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    Debugger debugger = new ClientDebugger("127.0.0.1", DEBUGGER_PORT);
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

    final Connection getConnection0(String jdbcUser, String jdbcPassword) {
        try {
            String configuration = System.getProperty("jdbc.properties");
            if (configuration == null) {
                log.error("No system property 'jdbc.properties' found");
                log.error("-----------");
                log.error("Please be sure property is set; example: -Djdbc.properties=/org/jooq/configuration/${env_var:USERNAME}/db2/library.properties");
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

            Properties properties = new Properties();

            try {
                properties.load(in);
            }
            finally {
                in.close();
            }

            String driver = properties.getProperty(JDBC_DRIVER);
            jdbcURL = properties.getProperty(JDBC_URL) + getSchemaSuffix();
            jdbcUser = jdbcUser != null ? jdbcUser : properties.getProperty(JDBC_USER);
            jdbcPassword = jdbcPassword != null ? jdbcPassword : properties.getProperty(JDBC_PASSWORD);
            jdbcSchema = properties.getProperty(JDBC_SCHEMA) + getSchemaSuffix();

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
     *
     * @return
     */
    protected String getJdbcURL() {
        return jdbcURL;
    }

    protected final String getCreateScript() throws Exception {
        return "/org/jooq/test/" + getDialect().getName().toLowerCase() + "/create.sql";
    }

    protected final String getResetScript() throws Exception {
        return "/org/jooq/test/" + getDialect().getName().toLowerCase() + "/reset.sql";
    }

    protected abstract Table<T658> T658();
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

    protected abstract UpdatableTable<A> TAuthor();
    protected abstract TableField<A, String> TAuthor_LAST_NAME();
    protected abstract TableField<A, String> TAuthor_FIRST_NAME();
    protected abstract TableField<A, Date> TAuthor_DATE_OF_BIRTH();
    protected abstract TableField<A, Integer> TAuthor_YEAR_OF_BIRTH();
    protected abstract TableField<A, Integer> TAuthor_ID();
    protected abstract TableField<A, ? extends UDTRecord<?>> TAuthor_ADDRESS();

    protected abstract Class<? extends UDTRecord<?>> cUAddressType();
    protected abstract Class<? extends UDTRecord<?>> cUStreetType();
    protected abstract UpdatableTable<B> TBook();

    protected abstract TableField<B, Integer> TBook_ID();
    protected abstract TableField<B, Integer> TBook_AUTHOR_ID();
    protected abstract TableField<B, String> TBook_TITLE();
    protected abstract TableField<B, ?> TBook_LANGUAGE_ID();
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

    protected abstract UpdatableTable<S> TBookStore();
    protected abstract TableField<S, String> TBookStore_NAME();
    protected abstract Table<L> VLibrary();
    protected abstract Table<?> VAuthor();
    protected abstract Table<?> VBook();
    protected abstract TableField<L, String> VLibrary_TITLE();
    protected abstract TableField<L, String> VLibrary_AUTHOR();

    protected abstract UpdatableTable<B2S> TBookToBookStore();
    protected abstract TableField<B2S, Integer> TBookToBookStore_BOOK_ID();
    protected abstract TableField<B2S, String> TBookToBookStore_BOOK_STORE_NAME();
    protected abstract TableField<B2S, Integer> TBookToBookStore_STOCK();

    protected abstract UpdatableTable<BS> TBookSale();
    protected final TableField<BS, Integer> TBookSale_ID() {
        return (TableField<BS, Integer>) TBookSale().getField("ID");
    }
    protected final TableField<BS, Integer> TBookSale_BOOK_ID() {
        return (TableField<BS, Integer>) TBookSale().getField("BOOK_ID");
    }
    protected final TableField<BS, String> TBookSale_BOOK_STORE_NAME() {
        return (TableField<BS, String>) TBookSale().getField("BOOK_STORE_NAME");
    }
    protected final TableField<BS, Date> TBookSale_SOLD_AT() {
        return (TableField<BS, Date>) TBookSale().getField("SOLD_AT");
    }
    protected final TableField<BS, BigDecimal> TBookSale_SOLD_FOR() {
        return (TableField<BS, BigDecimal>) TBookSale().getField("SOLD_FOR");
    }

    protected abstract UpdatableTable<BOOL> TBooleans();
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

    protected abstract UpdatableTable<D> TDirectory();
    protected abstract TableField<D, Integer> TDirectory_ID();
    protected abstract TableField<D, Integer> TDirectory_PARENT_ID();
    protected abstract TableField<D, Integer> TDirectory_IS_DIRECTORY();
    protected abstract TableField<D, String> TDirectory_NAME();

    protected abstract UpdatableTable<T> TTriggers();
    protected abstract TableField<T, Integer> TTriggers_ID_GENERATED();
    protected abstract TableField<T, Integer> TTriggers_ID();
    protected abstract TableField<T, Integer> TTriggers_COUNTER();

    protected abstract Table<I> TIdentity();
    protected abstract TableField<I, Integer> TIdentity_ID();
    protected abstract TableField<I, Integer> TIdentity_VAL();
    protected abstract UpdatableTable<IPK> TIdentityPK();
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
    protected abstract Factory create(Settings settings);

    @SuppressWarnings("deprecation")
    protected final Schema schema() {
        return create().getSchemaMapping().map(TAuthor().getSchema());
    }

    protected final Factory create() {
        String defaultSchema = System.getProperty("org.jooq.settings.defaultSchema", "");
        Boolean renderSchema = Boolean.valueOf(System.getProperty("org.jooq.settings.renderSchema", "true"));

        Settings settings = SettingsTools.defaultSettings()
            .withRenderSchema(renderSchema)
            .withRenderMapping(new RenderMapping()
                .withDefaultSchema(defaultSchema))
            .withExecuteListeners(
                TestStatisticsListener.class.getName(),
                DebugListener.class.getName());

        return create(settings);
    }

    protected final SQLDialect getDialect() throws Exception {
        return create().getDialect();
    }

    protected String getSchemaSuffix() {
        return "";
    }

    // IMPORTANT! Make this the first test, to prevent side-effects
    @Test
    public void testInsertIdentity() throws Exception {
        new InsertUpdateTests(this).testInsertIdentity();
    }


    // IMPORTANT! Make this the an early test, to check for attaching side-effects
    @Test
    public void testUse() throws Exception {
        new SchemaAndMappingTests(this).testUse();
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
    public void testFetchResultSet() throws Exception {
        new FetchTests(this).testFetchResultSet();
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
    public void testFetchGroups() throws Exception {
        new FetchTests(this).testFetchGroups();
    }

    @Test
    public void testFetchArray() throws Exception {
        new FetchTests(this).testFetchArray();
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
        new GeneralTests(this).testMetaModel();
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
    public void testLimit() throws Exception {
        new OrderByTests(this).testLimit();
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
    public void testQuantifiedPredicates() throws Exception {
        new PredicateTests(this).testQuantifiedPredicates();
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
    public void testDistinctQuery() throws Exception {
        new SelectTests(this).testDistinctQuery();
    }

    @Test
    public void testResultSort() throws Exception {
        new ResultTests(this).testResultSort();
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
    public void testFetchIntoTableRecords() throws Exception {
        new FetchTests(this).testFetchIntoTableRecords();
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
    public void testFetchLater() throws Exception {
        new FetchTests(this).testFetchLater();
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
    public void testUpdateSelect() throws Exception {
        new InsertUpdateTests(this).testUpdateSelect();
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

    @Test
    public void testMerge() throws Exception {
        new InsertUpdateTests(this).testMerge();
    }

    @Test
    public void testH2Merge() throws Exception {
        new InsertUpdateTests(this).testH2Merge();
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
    public void testRelations() throws Exception {
        new CRUDTests(this).testRelations();
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
    public void testNonUpdatables() throws Exception {
        new CRUDTests(this).testNonUpdatables();
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
    public void testLimitBindValues() throws Exception {
        new OrderByTests(this).testLimitBindValues();
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
    public void testAliasing() throws Exception {
        Table<B> b = TBook().as("b");
        Field<Integer> b_ID = b.getField(TBook_ID());

        List<Integer> ids = create().select(b_ID).from(b).orderBy(b_ID).fetch(b_ID);
        assertEquals(4, ids.size());
        assertEquals(BOOK_IDS, ids);

        Result<Record> books = create().select().from(b).orderBy(b_ID).fetch();
        assertEquals(4, books.size());
        assertEquals(BOOK_IDS, books.getValues(b_ID));
    }

    // @Test // TODO [#579] re-enable this test when fixing this bug
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
    public void testListAgg() throws Exception {
        new AggregateWindowFunctionTests(this).testListAgg();
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
    public void testMasterData() throws Exception {
        new EnumTests(this).testMasterData();
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
    public void testWindowFunctions() throws Exception {
        new AggregateWindowFunctionTests(this).testWindowFunctions();
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
        new GeneralTests(this).testBatchSingle();
    }

    @Test
    public void testBatchMultiple() throws Exception {
        new GeneralTests(this).testBatchMultiple();
    }

    @Test
    public void testBatchStore() throws Exception {
        new GeneralTests(this).testBatchStore();
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
    public void testPivotClause() throws Exception {
        new ExoticTests(this).testPivotClause();
    }

    @Test
    public void testRelationalDivision() throws Exception {
        new ExoticTests(this).testRelationalDivision();
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
    public void testBenchmark() throws Exception {
        new BenchmarkTests(this).testBenchmark();
    }

    @Test
    public void testVoid() {
        // A final test case to clean up the test database
    }
}
