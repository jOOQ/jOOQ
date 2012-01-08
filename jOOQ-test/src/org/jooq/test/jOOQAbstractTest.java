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
package org.jooq.test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.*;
import static org.jooq.tools.unsigned.Unsigned.ubyte;
import static org.jooq.tools.unsigned.Unsigned.uint;
import static org.jooq.tools.unsigned.Unsigned.ulong;
import static org.jooq.tools.unsigned.Unsigned.ushort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConfigurationProvider;
import org.jooq.ConfigurationRegistry;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.FutureResult;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.InsertSetMoreStep;
import org.jooq.Loader;
import org.jooq.MasterDataType;
import org.jooq.MergeFinalStep;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.SchemaMapping;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Sequence;
import org.jooq.SimpleSelectQuery;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdatableTable;
import org.jooq.UpdateQuery;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.DetachedException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.impl.CustomCondition;
import org.jooq.impl.CustomField;
import org.jooq.impl.Factory;
import org.jooq.impl.SQLDataType;
import org.jooq.test.$.AuthorWithoutAnnotations;
import org.jooq.test.$.BookRecord;
import org.jooq.test.$.BookTable;
import org.jooq.test.$.BookWithAnnotations;
import org.jooq.test.$.BookWithoutAnnotations;
import org.jooq.test.$.CharWithAnnotations;
import org.jooq.test.$.DatesWithAnnotations;
import org.jooq.test.$.FinalWithAnnotations;
import org.jooq.test.$.FinalWithoutAnnotations;
import org.jooq.test.$.StaticWithAnnotations;
import org.jooq.test.$.StaticWithoutAnnotations;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.unsigned.UByte;
import org.jooq.tools.unsigned.UInteger;
import org.jooq.tools.unsigned.ULong;
import org.jooq.tools.unsigned.UShort;
import org.jooq.tools.unsigned.Unsigned;
import org.jooq.util.GenerationTool;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PSQLException;
import org.w3c.dom.Document;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.sybase.jdbc3.jdbc.SybSQLException;

/**
 * The abstract test suite uses generic types to model the generated test schema
 * types, such as <code>T_AUTHOR</code>, <code>T_BOOK</code>, etc
 *
 * @author Lukas Eder
 */
public abstract class jOOQAbstractTest<

        // T_AUTHOR table
        A extends UpdatableRecord<A>,

        // T_BOOK table
        B extends UpdatableRecord<B>,

        // T_BOOK_STORE table
        S extends UpdatableRecord<S>,

        // T_BOOK_TO_BOOK_STORE table
        B2S extends UpdatableRecord<B2S>,

        // V_LIBRARY view
        L extends TableRecord<L>,

        // X_UNUSED table
        X extends TableRecord<X>,

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
    protected static final List<String>    BOOK_TITLES        = Arrays.asList("1984", "Animal Farm", "O Alquimista", "Brida");
    protected static final List<String>    BOOK_FIRST_NAMES   = Arrays.asList("George", "George", "Paulo", "Paulo");
    protected static final List<String>    BOOK_LAST_NAMES    = Arrays.asList("Orwell", "Orwell", "Coelho", "Coelho");
    protected static final List<Integer>   AUTHOR_IDS         = Arrays.asList(1, 2);
    protected static final List<String>    AUTHOR_FIRST_NAMES = Arrays.asList("George", "Paulo");
    protected static final List<String>    AUTHOR_LAST_NAMES  = Arrays.asList("Orwell", "Coelho");

    private static final String          JDBC_SCHEMA        = "jdbc.Schema";
    private static final String          JDBC_PASSWORD      = "jdbc.Password";
    private static final String          JDBC_USER          = "jdbc.User";
    private static final String          JDBC_URL           = "jdbc.URL";
    private static final String          JDBC_DRIVER        = "jdbc.Driver";

    protected static final JooqLogger    log                = JooqLogger.getLogger(jOOQAbstractTest.class);
    protected static final StopWatch     testSQLWatch       = new StopWatch();
    protected static boolean             initialised;
    protected static boolean             reset;
    protected static Connection          connection;
    protected static boolean             autocommit;
    protected static String              jdbcURL;
    protected static String              jdbcSchema;
    protected static Map<String, String> scripts            = new HashMap<String, String>();

    protected void execute(String script) throws Exception {
        Statement stmt = null;

        String allSQL = scripts.get(script);
        if (allSQL == null) {
            File file = new File(getClass().getResource(script).toURI());
            allSQL = FileUtils.readFileToString(file);
            scripts.put(script, allSQL);
            testSQLWatch.splitDebug("Loaded SQL file");
        }

        for (String sql : allSQL.split("/")) {
            try {
                if (!StringUtils.isBlank(sql)) {
                    sql = sql.replace("{" + JDBC_SCHEMA + "}", jdbcSchema);
                    stmt = connection.createStatement();
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
                else if (e instanceof PSQLException) {
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
                else if (e instanceof SQLServerException) {
                    switch (((SQLServerException)e).getErrorCode()) {
                        case 3701: // Tables
                        case 218:  // Types
                        continue;
                    }
                }

                // There is no DROP SEQUENCE IF EXISTS statement in Sybase
                else if (e instanceof SybSQLException) {
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
        log.info("QUITTING");

        // Issue a log dump on adaptive server. Don't know why this is needed
        // http://www.faqs.org/faqs/databases/sybase-faq/part6/
        if (connection.getClass().getPackage().getName().contains("jtds")) {
            log.info("RUNNING", "dump tran TEST with truncate_only");
            connection.createStatement().execute("dump tran TEST with truncate_only");
        }

        connection.close();
    }

    protected final void register(final Configuration configuration) {
        ConfigurationRegistry.setProvider(new ConfigurationProvider() {

            @Override
            public Configuration provideFor(Configuration c) {
                return configuration;
            }

            @Override
            public String toString() {
                return "Test Provider";
            }
        });
    }

    protected final Connection getConnection() {
        if (connection == null) {
            try {
                String property = System.getProperty("jdbc.properties");
                if (property == null) {
                    log.error("No system property 'jdbc.properties' found");
                    log.error("-----------");
                    log.error("Please be sure property is set; example: -Djdbc.properties=/org/jooq/configuration/${env_var:USERNAME}/db2/library.properties");
                    throw new Error();
                }
                InputStream in = GenerationTool.class.getResourceAsStream(property);
                if (in == null) {
                    log.error("Cannot find " + property);
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
                String jdbcUser = properties.getProperty(JDBC_USER);
                String jdbcPassword = properties.getProperty(JDBC_PASSWORD);
                jdbcSchema = properties.getProperty(JDBC_SCHEMA) + getSchemaSuffix();

                Class.forName(driver);
                if (StringUtils.isBlank(jdbcUser)) {
                    Properties p = new Properties();

                    if (getClass().getSimpleName().toLowerCase().contains("ingres")) {
                        p.setProperty("timezone", "EUROPE-CENTRAL");
                    }

                    connection = DriverManager.getConnection(getJdbcURL(), p);
                }
                else {
                    connection = DriverManager.getConnection(getJdbcURL(), jdbcUser, jdbcPassword);
                }
            }
            catch (Exception e) {
                throw new Error(e);
            }
        }

        return connection;
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

    protected abstract UpdatableTable<D> TDirectory();
    protected abstract TableField<D, Integer> TDirectory_ID();
    protected abstract TableField<D, Integer> TDirectory_PARENT_ID();
    protected abstract TableField<D, Byte> TDirectory_IS_DIRECTORY();
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
    protected abstract Factory create(SchemaMapping mapping);

    protected final Schema schema() {
        return create().getSchemaMapping().map(TAuthor().getSchema());
    }

    private final Table<?> getTable(String name) throws Exception {
        Schema schema = TAuthor().getSchema();

        if (schema == null) {
            Class<?> tables = Class.forName("org.jooq.test." + getDialect().getName().toLowerCase() + ".generatedclasses.Tables");
            return (Table<?>) tables.getField(name).get(tables);
        }
        else {
            Table<?> result = schema.getTable(name);

            if (result == null) {
                result = schema.getTable(name.toUpperCase());
            }

            if (result == null) {
                result = schema.getTable(name.toLowerCase());
            }

            return result;
        }
    }

    private final Field<?> getField(Table<?> table, String name) {
        Field<?> result = table.getField(name);

        if (result == null) {
            result = table.getField(name.toUpperCase());
        }

        if (result == null) {
            result = table.getField(name.toLowerCase());
        }

        return result;
    }

    protected final Factory create() {
        return create(null);
    }

    protected final SQLDialect getDialect() throws Exception {
        return create().getDialect();
    }

    protected final QueryPartInternal internal(QueryPart q) {
        return q.internalAPI(QueryPartInternal.class);
    }

    protected String getSchemaSuffix() {
        return "";
    }

    // IMPORTANT! Make this the first test, to prevent side-effects
    @Test
    public void testInsertIdentity() throws Exception {

        // Oracle and SQLite don't support identity columns
        if (TIdentity() == null && TIdentityPK() == null) {
            log.info("SKIPPING", "IDENTITY tests");
            return;
        }

        reset = false;

        // Identity tables with primary key
        if (TIdentityPK() != null) {
            testInsertIdentity0(TIdentityPK(), TIdentityPK_ID(), TIdentityPK_VAL());
        }

        // Identity tables without primary key
        if (TIdentity() != null) {
            testInsertIdentity0(TIdentity(), TIdentity_ID(), TIdentity_VAL());
        }
    }

    /**
     * Extracted method for very similar tests with T_IDENTITY, T_IDENTITY_PK
     */
    @SuppressWarnings("unchecked")
    private <R extends TableRecord<R>> void testInsertIdentity0(Table<R> table, TableField<R, Integer> id, TableField<R, Integer> val) throws Exception {

        // Plain insert
        // ------------
        assertEquals(1,
        create().insertInto(table, val)
                .values(10)
                .execute());

        if (getDialect() != POSTGRES &&
            getDialect() != DB2) {

            assertEquals(new BigInteger("1"), create().lastID());
        }

        R r1 = create().selectFrom(table).fetchOne();

        assertEquals(1, (int) r1.getValue(id));
        assertEquals(10, (int) r1.getValue(val));

        // INSERT .. RETURNING
        // -------------------
        R r2 =
        create().insertInto(table, val)
                .values(11)
                .returning()
                .fetchOne();

        if (getDialect() != POSTGRES &&
            getDialect() != DB2) {

            assertEquals(new BigInteger("2"), create().lastID());
            assertEquals(new BigInteger("2"), create().lastID());
        }

        assertEquals(2, (int) r2.getValue(id));
        assertEquals(11, (int) r2.getValue(val));

        // INSERT MULTIPLE .. RETURNING
        // ----------------------------
        // TODO [#832] Make this work for Sybase also
        // TODO [#1004] Make this work for SQL Server also
        // TODO ... and then, think about Ingres, H2 and Derby as well
        if (getDialect() == SYBASE ||
            getDialect() == SQLSERVER ||
            getDialect() == INGRES ||
            getDialect() == H2 ||
            getDialect() == DERBY ||
            getDialect() == ASE) {

            log.info("SKIPPING", "Multi-record INSERT .. RETURNING statement");
        }
        else {
            Result<R> r3 =
            create().insertInto(table, val)
                    .values(12)
                    .values(13)
                    .returning(id)
                    .fetch();

            assertEquals(2, r3.size());
            assertNull(r3.getValue(0, val));
            assertNull(r3.getValue(1, val));
            assertEquals(3, (int) r3.getValue(0, id));
            assertEquals(4, (int) r3.getValue(1, id));

            // Record.storeUsing()
            R r4 = create().newRecord(table);
            r4.setValue(val, 20);
            assertEquals(1, r4.storeUsing(table.getIdentity().getField()));

            if (getDialect() != POSTGRES &&
                getDialect() != DB2) {

                assertEquals(new BigInteger("5"), create().lastID());
                assertEquals(new BigInteger("5"), create().lastID());
            }

            // TODO [#1002] Fix this
            R r5 = create().fetchOne(table, id.equal(5));
            assertEquals(r5, r4);
        }
    }

    // IMPORTANT! Make this the an early test, to check for attaching side-effects
    @Test
    public void testUse() throws Exception {
        switch (getDialect()) {
            case ASE:
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "USE test");
                return;
        }

        Factory factory = create();
        factory.use(schema().getName());

        Result<?> result =
        factory.select(TBook_AUTHOR_ID(), count())
               .from(TBook())
               .join(TAuthor())
               .on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
               .where(TAuthor_YEAR_OF_BIRTH().greaterOrEqual(TAuthor_ID()))
               .groupBy(TBook_AUTHOR_ID())
               .having(count().greaterOrEqual(1))
               .orderBy(TBook_AUTHOR_ID().desc())
               .fetch();

        assertEquals(Arrays.asList(2, 1), result.getValues(TBook_AUTHOR_ID()));
        assertEquals(Arrays.asList(2, 2), result.getValues(count()));
    }

    @Test
    public void testTableMapping() throws Exception {
        SchemaMapping mapping = new SchemaMapping();
        mapping.add(TAuthor(), VAuthor());
        mapping.add(TBook(), VBook().getName());

        Select<Record> q =
        create(mapping).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        // Assure T_* is replaced by V_*
        assertTrue(create(mapping).render(q).contains(VAuthor().getName()));
        assertTrue(create(mapping).render(q).contains(VBook().getName()));
        assertFalse(create(mapping).render(q).contains(TAuthor().getName()));
        assertFalse(create(mapping).render(q).contains(TBook().getName()));

        // Assure that results are correct
        Result<Record> result = q.fetch();
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));
    }

    @Test
    public void testSchemaMapping() throws Exception {
        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "SchemaMapping tests");
                return;
        }

        // Map to self. This will work even for single-schema RDBMS
        // ---------------------------------------------------------------------
        SchemaMapping mapping = new SchemaMapping();
        mapping.add(TAuthor(), TAuthor());
        mapping.add(TBook(), TBook().getName());
        mapping.add(TAuthor().getSchema(), TAuthor().getSchema().getName());

        Select<Record> query =
        create(mapping).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        Result<Record> result = query.fetch();

        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));

        // Check for consistency when executing SQL manually
        String sql = query.getSQL();
        log.info("Executing", sql);
        assertEquals(result, create().fetch(sql, query.getBindValues().toArray()));

        // Schema mapping is supported in many RDBMS. But maintaining several
        // databases is non-trivial in some of them.
        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case ORACLE:
            case POSTGRES:
            case SQLITE:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "Schema mapping test");
                return;

            // Currently, only MySQL is tested with SchemaMapping
            case MYSQL:

                // But not when the schema is already re-written
                if (getClass() == jOOQMySQLTestSchemaRewrite.class) {
                    log.info("SKIPPING", "Schema mapping test");
                    return;
                }
        }

        // Map to a second schema
        // ---------------------------------------------------------------------
        mapping = new SchemaMapping();
        mapping.add(TAuthor().getSchema(), TAuthor().getSchema().getName() + "2");

        Select<Record> q =
        create(mapping).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        // Assure that schema is replaced
        assertTrue(create(mapping).render(q).contains(TAuthor().getSchema().getName() + "2"));
        assertTrue(q.getSQL().contains(TAuthor().getSchema().getName() + "2"));
        assertEquals(create(mapping).render(q), q.getSQL());

        // Assure that results are correct
        result = q.fetch();
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));

        // [#995] Schema mapping in stored functions
        // -----------------------------------------
        Field<Integer> f1 = FOneField().cast(Integer.class);
        Field<Integer> f2 = FNumberField(42).cast(Integer.class);

        q =
        create(mapping).select(f1, f2);

        // Assure that schema is replaced
        assertTrue(create(mapping).render(q).contains(TAuthor().getSchema().getName() + "2"));
        assertTrue(q.getSQL().contains(TAuthor().getSchema().getName() + "2"));
        assertEquals(create(mapping).render(q), q.getSQL());

        // Assure that results are correct
        Record record = q.fetchOne();
        assertEquals(1, (int) record.getValue(f1));
        assertEquals(42, (int) record.getValue(f2));

        // Map both schema AND tables
        // --------------------------
        mapping = new SchemaMapping();
        mapping.add(TAuthor(), VAuthor());
        mapping.add(TBook(), VBook().getName());
        mapping.add(TAuthor().getSchema(), TAuthor().getSchema().getName() + "2");

        q =
        create(mapping).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        // Assure T_* is replaced by V_*
        assertTrue(create(mapping).render(q).contains(VAuthor().getName()));
        assertTrue(create(mapping).render(q).contains(VBook().getName()));
        assertTrue(create(mapping).render(q).contains("test2"));
        assertFalse(create(mapping).render(q).contains(TAuthor().getName()));
        assertFalse(create(mapping).render(q).contains(TBook().getName()));

        // Assure that results are correct
        result = q.fetch();
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));
    }

    @Test
    public void testSystemFunctions() throws Exception {
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "System functions test");
            return;
        }

        Field<?> user = trim(lower(currentUser()));
        Record record = create().select(user).fetchOne();

        assertTrue(Arrays.asList("test", "db2admin", "sa", "root@localhost", "postgres", "dbo", "dba")
            .contains(record.getValue(user)));
    }

    @Test
    public void testLazyFetching() throws Exception {

        // ---------------------------------------------------------------------
        // A regular pass through the cursor
        // ---------------------------------------------------------------------
        Cursor<B> cursor = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLazy();

        assertTrue(cursor.hasNext());
        assertTrue(cursor.hasNext());
        assertEquals(Integer.valueOf(1), cursor.fetchOne().getValue(TBook_ID()));
        assertEquals(Integer.valueOf(2), cursor.fetchOne().getValue(TBook_ID()));

        assertTrue(cursor.hasNext());
        assertTrue(cursor.hasNext());
        assertFalse(cursor.isClosed());

        Iterator<B> it = cursor.iterator();
        assertTrue(it.hasNext());
        assertTrue(cursor.hasNext());
        assertTrue(it.hasNext());
        assertTrue(cursor.hasNext());
        assertTrue(it.hasNext());
        assertTrue(cursor.hasNext());
        assertEquals(Integer.valueOf(3), it.next().getValue(TBook_ID()));
        assertEquals(Integer.valueOf(4), it.next().getValue(TBook_ID()));
        assertFalse(cursor.isClosed());

        assertFalse(it.hasNext());
        assertFalse(cursor.hasNext());
        assertFalse(it.hasNext());
        assertFalse(cursor.hasNext());
        assertFalse(it.hasNext());
        assertFalse(cursor.hasNext());
        assertTrue(cursor.isClosed());

        assertEquals(null, it.next());
        assertEquals(null, it.next());
        assertEquals(null, cursor.fetchOne());
        assertEquals(null, cursor.fetchOne());

        cursor.close();
        cursor.close();
        assertTrue(cursor.isClosed());

        // ---------------------------------------------------------------------
        // Prematurely closing the cursor
        // ---------------------------------------------------------------------
        cursor = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLazy();

        assertTrue(cursor.hasNext());
        assertTrue(cursor.hasNext());
        assertEquals(Integer.valueOf(1), cursor.fetchOne().getValue(TBook_ID()));
        assertEquals(Integer.valueOf(2), cursor.fetchOne().getValue(TBook_ID()));
        assertFalse(cursor.isClosed());

        cursor.close();
        assertTrue(cursor.isClosed());
        assertFalse(cursor.hasNext());
        assertNull(cursor.fetchOne());

        // ---------------------------------------------------------------------
        // Fetching several records at once
        // ---------------------------------------------------------------------
        cursor = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLazy();

        assertTrue(cursor.fetch(0).isEmpty());
        assertTrue(cursor.fetch(0).isEmpty());
        List<B> list = cursor.fetch(1);

        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(1), list.get(0).getValue(TBook_ID()));

        list = cursor.fetch(2);
        assertEquals(2, list.size());
        assertEquals(Integer.valueOf(2), list.get(0).getValue(TBook_ID()));
        assertEquals(Integer.valueOf(3), list.get(1).getValue(TBook_ID()));

        list = cursor.fetch(2);
        assertTrue(cursor.isClosed());
        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(4), list.get(0).getValue(TBook_ID()));
    }

    @Test
    public void testFetchMap() throws Exception {
        try {
            create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_AUTHOR_ID());
            fail();
        } catch (InvalidResultException expected) {}

        // Key -> Record Map
        // -----------------
        Map<Integer, B> map1 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_ID());
        for (Entry<Integer, B> entry : map1.entrySet()) {
            assertEquals(entry.getKey(), entry.getValue().getValue(TBook_ID()));
        }
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map1.keySet()));

        // Key -> Value Map
        // ----------------
        Map<Integer, String> map2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_ID(), TBook_TITLE());
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map2.keySet()));
        assertEquals(BOOK_TITLES, new ArrayList<String>(map2.values()));

        // List of Map
        // -----------
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        List<Map<String, Object>> list =
            create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMaps();
        assertEquals(4, list.size());

        for (int i = 0; i < books.size(); i++) {
            for (Field<?> field : books.getFields()) {
                assertEquals(books.getValue(i, field), list.get(i).get(field.getName()));
            }
        }

        // Single Map
        // ----------
        B book = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOne();
        Map<String, Object> map3 = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOneMap();

        for (Field<?> field : books.getFields()) {
            assertEquals(book.getValue(field), map3.get(field.getName()));
        }

        // Maps with two times the same field
        // ----------------------------------
        try {
            create().select(val("a"), val("a")).fetchMaps();
            fail();
        }
        catch (InvalidResultException expected) {}

        try {
            create().select(val("a"), val("a")).fetchOneMap();
            fail();
        }
        catch (InvalidResultException expected) {}
    }

    @Test
    public void testFetchArray() throws Exception {

        // fetchOne
        // --------
        B book = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOne();
        Object[] bookArray = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOneArray();
        for (int i = 0; i < TBook().getFields().size(); i++) {
            assertEquals(book.getValue(i), bookArray[i]);
        }

        // fetch
        // -----
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        Object[][] booksArray = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArrays();

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < TBook().getFields().size(); i++) {
                assertEquals(books.getValue(j, i), booksArray[j][i]);
                assertEquals(books.getValue(j, i), books.intoArray()[j][i]);
                assertEquals(books.get(j).getValue(i), books.get(j).intoArray()[i]);
            }
        }

        // fetch single field
        // ------------------
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(TBook_TITLE()),
        Arrays.asList(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(TBook_TITLE())));

        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(1),
        Arrays.asList(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(1)));

        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(TBook_ID().getName()),
        Arrays.asList(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(TBook_ID().getName())));

    }

    @Test
    public void testGetSQLAndGetBindValues() throws Exception {
        Select<?> select =
        create().select(TBook_ID(), TBook_ID().mul(6).div(2).div(3))
                .from(TBook())
                .orderBy(TBook_ID(), TBook_ID().mod(2));

        assertEquals(
            Arrays.asList(6, 2, 3, 2),
            select.getBindValues());

        log.info("Executing", select.getSQL());
        PreparedStatement stmt = connection.prepareStatement(select.getSQL());
        int i = 0;
        for (Object value : select.getBindValues()) {
            stmt.setObject(++i, value);
        }

        ResultSet rs = stmt.executeQuery();
        Result<Record> result = create().fetch(rs);
        assertEquals(BOOK_IDS, result.getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_IDS, result.getValues(1, Integer.class));

        try {
            assertEquals(BOOK_IDS, result.getValues(2, Integer.class));
            fail();
        } catch (IllegalArgumentException expected) {}

        stmt.close();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testTruncate() throws Exception {
        reset = false;

        try {
            create().truncate(TAuthor()).execute();

            // The above should fail if foreign keys are supported
            if (supportsReferences()) {
                fail();
            }
        } catch (Exception expected) {
        }

        // This is being tested with an unreferenced table as some RDBMS don't
        // Allow this
        create().truncate((Table) table("t_book_to_book_store")).execute();
        assertEquals(0, create().fetch(table("t_book_to_book_store")).size());
    }

    @Test
    public void testMetaModel() throws Exception {

        // Test correct source code generation for the meta model
        Schema schema = TAuthor().getSchema();
        if (schema != null) {
            int sequences = 0;

            if (cSequences() != null) {
                sequences++;

                // DB2 has an additional sequence for the T_TRIGGERS table
                if (getDialect() == SQLDialect.DB2 ||
                    getDialect() == SQLDialect.H2) {

                    sequences++;
                }

                // Oracle has additional sequences for [#961]
                else if (getDialect() == ORACLE) {
                    sequences += 5;
                }
            }

            assertEquals(sequences, schema.getSequences().size());
            for (Table<?> table : schema.getTables()) {
                assertEquals(table, schema.getTable(table.getName()));
            }
            for (UDT<?> udt : schema.getUDTs()) {
                assertEquals(udt, schema.getUDT(udt.getName()));
            }
            for (Sequence<?> sequence : schema.getSequences()) {
                assertEquals(sequence, schema.getSequence(sequence.getName()));
            }

            int tables = 16;

            // The additional T_DIRECTORY table for recursive queries
            if (supportsRecursiveQueries()) {
                tables++;
            }

            // The additional T_TRIGGERS table for INSERT .. RETURNING
            if (TTriggers() != null) {
                tables++;
            }

            // The additional T_UNSIGNED table
            if (TUnsigned() != null) {
                tables++;
            }

            // The additional T_IDENTITY table
            if (TIdentity() != null) {
                tables++;
            }

            // The additional T_IDENTITY_PK table
            if (TIdentityPK() != null) {
                tables++;
            }

            // [#959] The T_959 table for enum collisions with Java keywords
            if (getDialect() == MYSQL ||
                getDialect() == POSTGRES) {
                tables++;
            }

            // [#986] Some foreign key name collision checks
            if (getDialect() == ASE ||
                getDialect() == DB2 ||
                getDialect() == POSTGRES ||
                getDialect() == SQLITE ||
                getDialect() == SYBASE) {

                tables += 2;
            }

            if (TArrays() == null) {
                assertEquals(tables, schema.getTables().size());
            }

            // [#624] The V_INCOMPLETE view is only available in Oracle
            // [#877] The T_877 table is only available in H2
            else if (getDialect() == ORACLE ||
                     getDialect() == H2) {
                assertEquals(tables + 2, schema.getTables().size());
            }

            // [#610] Collision-prone entities are only available in HSQLDB
            else if (getDialect() == HSQLDB) {
                assertEquals(tables + 9, schema.getTables().size());
            }

            else {
                assertEquals(tables + 1, schema.getTables().size());
            }

            if (cUAddressType() == null) {
                assertEquals(0, schema.getUDTs().size());
            }
            // [#643] The U_INVALID types are only available in Oracle
            // [#799] The member procedure UDT's too
            else if (getDialect() == ORACLE) {
                assertEquals(7, schema.getUDTs().size());
            }
            else {
                assertEquals(2, schema.getUDTs().size());
            }
        }

        // Test correct source code generation for identity columns
        assertNull(TAuthor().getIdentity());
        assertNull(TBook().getIdentity());

        if (TIdentity() != null || TIdentityPK() != null) {
            if (TIdentity() != null) {
                assertEquals(TIdentity(), TIdentity().getIdentity().getTable());
                assertEquals(TIdentity_ID(), TIdentity().getIdentity().getField());
            }

            if (TIdentityPK() != null) {
                assertEquals(TIdentityPK(), TIdentityPK().getIdentity().getTable());
                assertEquals(TIdentityPK_ID(), TIdentityPK().getIdentity().getField());
            }
        }
        else {
            log.info("SKIPPING", "Identity tests");
        }

        // Test correct source code generation for relations
        assertNotNull(TAuthor().getMainKey());
        assertNotNull(TAuthor().getKeys());
        assertTrue(TAuthor().getKeys().contains(TAuthor().getMainKey()));
        assertEquals(1, TAuthor().getKeys().size());
        assertEquals(1, TAuthor().getMainKey().getFields().size());
        assertEquals(TAuthor_ID(), TAuthor().getMainKey().getFields().get(0));

        if (supportsReferences()) {
            assertEquals(0, TAuthor().getReferences().size());
            assertEquals(2, TAuthor().getMainKey().getReferences().size());
            assertEquals(TBook(), TAuthor().getMainKey().getReferences().get(0).getTable());
            assertEquals(TBook(), TAuthor().getMainKey().getReferences().get(1).getTable());
            assertEquals(Arrays.asList(), TAuthor().getReferencesTo(TBook()));
            assertTrue(TBook().getReferences().containsAll(TAuthor().getReferencesFrom(TBook())));
            assertTrue(TBook().getReferences().containsAll(TBook().getReferencesFrom(TAuthor())));
            assertEquals(TBook().getReferencesTo(TAuthor()), TAuthor().getReferencesFrom(TBook()));
        }
        else {
            log.info("SKIPPING", "References tests");
        }

        for (Field<?> field : T639().getFields()) {
            if ("BYTE".equalsIgnoreCase(field.getName())) {
                assertEquals(Byte.class, field.getType());
                assertEquals(SQLDataType.TINYINT, field.getDataType());
            }
            else if ("SHORT".equalsIgnoreCase(field.getName())) {
                assertEquals(Short.class, field.getType());
                assertEquals(SQLDataType.SMALLINT, field.getDataType());
            }
            else if ("INTEGER".equalsIgnoreCase(field.getName())) {
                assertEquals(Integer.class, field.getType());
                assertEquals(SQLDataType.INTEGER, field.getDataType());
            }
            else if ("LONG".equalsIgnoreCase(field.getName())) {
                assertEquals(Long.class, field.getType());
                assertEquals(SQLDataType.BIGINT, field.getDataType());
            }
            else if ("BYTE_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Byte.class, field.getType());
                assertEquals(SQLDataType.TINYINT, field.getDataType());
            }
            else if ("SHORT_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Short.class, field.getType());
                assertEquals(SQLDataType.SMALLINT, field.getDataType());
            }
            else if ("INTEGER_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Integer.class, field.getType());
                assertEquals(SQLDataType.INTEGER, field.getDataType());
            }
            else if ("LONG_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Long.class, field.getType());
                assertEquals(SQLDataType.BIGINT, field.getDataType());
            }
            else if ("BIG_INTEGER".equalsIgnoreCase(field.getName())) {
                assertEquals(BigInteger.class, field.getType());
                assertEquals(SQLDataType.DECIMAL_INTEGER, field.getDataType());
            }

            // [#745] TODO: Unify distinction between NUMERIC and DECIMAL
            else if ("BIG_DECIMAL".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.ORACLE
                    && getDialect() != SQLDialect.POSTGRES
                    && getDialect() != SQLDialect.SQLITE
                    && getDialect() != SQLDialect.SQLSERVER) {

                assertEquals(BigDecimal.class, field.getType());
                assertEquals(SQLDataType.DECIMAL, field.getDataType());
            }
            else if ("BIG_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(BigDecimal.class, field.getType());
                assertEquals(SQLDataType.NUMERIC, field.getDataType());
            }

            // [#746] TODO: Interestingly, HSQLDB and MySQL match REAL with DOUBLE.
            // There is no matching type for java.lang.Float...
            else if ("FLOAT".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.HSQLDB
                    && getDialect() != SQLDialect.MYSQL
                    && getDialect() != SQLDialect.SYBASE) {

                assertEquals(Float.class, field.getType());
                assertEquals(SQLDataType.REAL, field.getDataType());
            }
            else if ("FLOAT".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.MYSQL
                    && getDialect() != SQLDialect.SYBASE) {

                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.DOUBLE, field.getDataType());
            }
            else if ("FLOAT".equalsIgnoreCase(field.getName())) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.FLOAT, field.getDataType());
            }

            // [#746] TODO: Fix this, too
            else if ("DOUBLE".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.SQLSERVER
                    && getDialect() != SQLDialect.ASE) {

                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.DOUBLE, field.getDataType());
            }
            else if ("DOUBLE".equalsIgnoreCase(field.getName())) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.FLOAT, field.getDataType());
            }
        }
    }

    @Test
    public void testNumbers() throws Exception {
        reset = false;

        // Insert some numbers
        // -------------------
        InsertSetMoreStep<T639> set =
        create().insertInto(T639())
                .set(T639_ID(), 1)
                .set(T639_BIG_DECIMAL(), new BigDecimal("1234.5670"))
                .set(T639_BIG_INTEGER(), new BigInteger("1234567890"))
                .set(T639_BYTE_DECIMAL(), (byte) 2)
                .set(T639_INTEGER(), 3)
                .set(T639_INTEGER_DECIMAL(), 4)
                .set(T639_LONG(), 5L)
                .set(T639_LONG_DECIMAL(), 6L)
                .set(T639_SHORT(), (short) 7)
                .set(T639_SHORT_DECIMAL(), (short) 8);

        if (T639_BYTE() != null) set.set(T639_BYTE(), (byte) 9);
        if (T639_DOUBLE() != null) set.set(T639_DOUBLE(), 10.125);
        if (T639_FLOAT() != null) set.set(T639_FLOAT(), 11.375f);

        assertEquals(1, set.execute());

        T639 record = create().fetchOne(T639());
        assertEquals(1, (int) record.getValue(T639_ID()));
        assertTrue(new BigDecimal("1234.567").compareTo(record.getValue(T639_BIG_DECIMAL())) == 0);
        assertEquals(new BigInteger("1234567890"), record.getValue(T639_BIG_INTEGER()));
        assertEquals(2, (byte) record.getValue(T639_BYTE_DECIMAL()));
        assertEquals(3, (int) record.getValue(T639_INTEGER()));
        assertEquals(4, (int) record.getValue(T639_INTEGER_DECIMAL()));
        assertEquals(5L, (long) record.getValue(T639_LONG()));
        assertEquals(6L, (long) record.getValue(T639_LONG_DECIMAL()));
        assertEquals(7, (short) record.getValue(T639_SHORT()));
        assertEquals(8, (short) record.getValue(T639_SHORT_DECIMAL()));

        if (T639_BYTE() != null) assertEquals(9, (byte) record.getValue(T639_BYTE()));
        if (T639_DOUBLE() != null) assertEquals(10.125, (double) record.getValue(T639_DOUBLE()));
        if (T639_FLOAT() != null) assertEquals(11.375f, (float) record.getValue(T639_FLOAT()));

        // Various BigDecimal tests
        // ------------------------
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "Advanced BigDecimal tests");
        }
        else {
            create().insertInto(T639(), T639_ID(), T639_BIG_DECIMAL())
                    .values(2, new BigDecimal("123456789012345.67899"))
                    .values(3, new BigDecimal("999999999999999.99999"))
                    .values(4, new BigDecimal("1.00001"))
                    .values(5, new BigDecimal("0.00001"))
                    .values(6, new BigDecimal("0.00001"))
                    .execute();

            Result<Record> result =
            create().select(T639_ID(), T639_BIG_DECIMAL())
                    .from(T639())
                    .where(T639_ID().between(2, 6))
                    .orderBy(T639_ID())
                    .fetch();

            assertEquals(Arrays.asList(2, 3, 4, 5, 6), result.getValues(0));
            assertEquals(new BigDecimal("123456789012345.67899"), result.getValue(0, 1));
            assertEquals(new BigDecimal("999999999999999.99999"), result.getValue(1, 1));
            assertEquals(new BigDecimal("1.00001"), result.getValue(2, 1));
            assertEquals(new BigDecimal("0.00001"), result.getValue(3, 1));
            assertEquals(new BigDecimal("0.00001"), result.getValue(4, 1));
        }
    }

    @Test
    public void testLiterals() throws Exception {
        Record record = create().select(zero(), one(), two(), pi(), e(), rad(deg(pi()))).fetchOne();

        assertEquals(0, record.getValue(0));
        assertEquals(1, record.getValue(1));
        assertEquals(2, record.getValue(2));
        assertEquals("3.141", record.getValueAsString(3).substring(0, 5));
        assertEquals("2.718", record.getValueAsString(4).substring(0, 5));
        assertEquals("3.141", record.getValueAsString(5).substring(0, 5));
    }

    @Test
    public void testPlainSQL() throws Exception {
        reset = false;

        // Field and Table
        // ---------------
        Field<Integer> ID = field(TBook_ID().getName(), Integer.class);
        Result<Record> result = create().select().from("t_book").orderBy(ID).fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(ID));
        assertEquals(BOOK_TITLES, result.getValues(TBook_TITLE()));

        // [#271] Aliased plain SQL table
        result = create().select(ID).from("(select * from t_book) b").orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(ID));

        // [#271] Aliased plain SQL table
        result = create().select().from("(select * from t_book) b").orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(
            Arrays.asList(1, 2, 3, 4),
            result.getValues(ID));

        // [#836] Aliased plain SQL table
        result = create().select().from(table("t_book").as("b")).orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(ID));

        // [#271] Check for aliased nested selects. The DescribeQuery does not seem to work
        // [#836] Aliased plain SQL nested select
        result = create().select().from(table("(select * from t_book)").as("b")).orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(
            Arrays.asList(1, 2, 3, 4),
            result.getValues(ID));


        // Field, Table and Condition
        // --------------------------
        Field<?> LAST_NAME = field(TAuthor_LAST_NAME().getName());
        Field<?> COUNT1 = field("count(*) x");
        Field<?> COUNT2 = field("count(*) y", Integer.class);

        result = create()
            .select(LAST_NAME, COUNT1, COUNT2)
            .from("t_author a")
            .join("t_book b").on("a.id = b.author_id")
            .where("b.title != 'Brida'")
            .groupBy(LAST_NAME)
            .orderBy(LAST_NAME).fetch();

        assertEquals(2, result.size());
        assertEquals("Coelho", result.getValue(0, LAST_NAME));
        assertEquals("Orwell", result.getValue(1, LAST_NAME));
        assertEquals("1", result.getValueAsString(0, COUNT1));
        assertEquals("2", result.getValueAsString(1, COUNT1));
        assertEquals(Integer.valueOf(1), result.getValue(0, COUNT2));
        assertEquals(Integer.valueOf(2), result.getValue(1, COUNT2));

        // Field, Table and Condition
        // --------------------------
        result = create().select(LAST_NAME, COUNT1, COUNT2)
            .from("t_author a")
            .join("t_book b").on("a.id = b.author_id")
            .where("b.title != 'Brida'")
            .groupBy(LAST_NAME)
            .having("count(*) = ?", 1).fetch();

        assertEquals(1, result.size());
        assertEquals("Coelho", result.getValue(0, LAST_NAME));
        assertEquals("1", result.getValueAsString(0, COUNT1));
        assertEquals(Integer.valueOf(1), result.getValue(0, COUNT2));

        // Query
        // -----
        assertEquals(1, create()
            .query("insert into t_author (id, first_name, last_name) values (?, ?, ?)", 3, "Michèle", "Roten")
            .execute());
        A author = create().fetchOne(TAuthor(), TAuthor_ID().equal(3));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Michèle", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // [#724] Check for correct binding when passing
        // ---------------------------------------------
        // - (Object[]) null: API misuse
        // - (Object) null: Single null bind value
        assertEquals(1, create()
            .query("update t_author set first_name = ? where id = 3", (Object[]) null)
            .execute());
        author.refresh();
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals(null, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // Reset name
        assertEquals(1, create()
            .query("update t_author set first_name = ? where id = 3", "Michèle")
            .execute());
        author.refresh();
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Michèle", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // [#724] Check for correct binding when passing (Object) null
        assertEquals(1, create()
            .query("update t_author set first_name = ? where id = 3", (Object) null)
            .execute());
        author.refresh();
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals(null, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // Function
        // --------
        assertEquals("ABC", create().select(function("upper", String.class, val("aBc"))).fetchOne(0));
        assertEquals("abc", create().select(function("lower", SQLDataType.VARCHAR, val("aBc"))).fetchOne(0));

        // Fetch
        // -----
        Result<Record> books = create().fetch("select * from t_book where id in (?, ?) order by id", 1, 2);
        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals(Integer.valueOf(1), books.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(2), books.getValue(1, TBook_ID()));
        assertEquals(Integer.valueOf(1), books.getValue(0, TBook_AUTHOR_ID()));
        assertEquals(Integer.valueOf(1), books.getValue(1, TBook_AUTHOR_ID()));
    }

    @Test
    public void testPlainSQLCRUD() throws Exception {
        reset = false;

        // CRUD with plain SQL
        Table<Record> table = table(TAuthor().getName());
        Field<Integer> id = field(TAuthor_ID().getName(), Integer.class);
        Field<String> firstName = field(TAuthor_FIRST_NAME().getName(), String.class);
        Field<String> lastName = field(TAuthor_LAST_NAME().getName(), String.class);

        assertEquals(2,
        create().insertInto(table, id, firstName, lastName)
                .values(10, "Herbert", "Meier")
                .values(11, "Friedrich", "Glauser")
                .execute());

        Result<Record> authors1 = create()
                .select(id, firstName, lastName)
                .from(table)
                .where(id.in(10, 11))
                .orderBy(id)
                .fetch();

        assertEquals(2, authors1.size());
        assertEquals(10, (int) authors1.getValue(0, id));
        assertEquals(11, (int) authors1.getValue(1, id));
        assertEquals("Herbert", authors1.getValue(0, firstName));
        assertEquals("Friedrich", authors1.getValue(1, firstName));
        assertEquals("Meier", authors1.getValue(0, lastName));
        assertEquals("Glauser", authors1.getValue(1, lastName));

        assertEquals(2,
        create().update(table)
                .set(firstName, "Friedrich")
                .set(lastName, "Schiller")
                .where(id.in(10, 11))
                .execute());

        Result<Record> authors2 =
        create().select(id, firstName, lastName)
                .from(table)
                .where(id.in(10, 11))
                .orderBy(id)
                .fetch();

        assertEquals(2, authors2.size());
        assertEquals(10, (int) authors2.getValue(0, id));
        assertEquals(11, (int) authors2.getValue(1, id));
        assertEquals("Friedrich", authors2.getValue(0, firstName));
        assertEquals("Friedrich", authors2.getValue(1, firstName));
        assertEquals("Schiller", authors2.getValue(0, lastName));
        assertEquals("Schiller", authors2.getValue(1, lastName));

        assertEquals(2,
        create().delete(table)
                .where(id.in(10, 11))
                .execute());

        assertEquals(0,
        create().selectCount()
                .from(table)
                .where(id.in(10, 11))
                .fetchOne(0));
    }

    @Test
    public void testPlainSQLResultQuery() throws Exception {
        String sql = create().select(param("p", String.class).as("p")).getSQL();
        ResultQuery<Record> q = create().resultQuery(sql, "10");

        Result<Record> fetch1 = q.fetch();
        assertEquals(1, fetch1.size());
        assertEquals(1, fetch1.getFields().size());
        assertEquals("p", fetch1.getField(0).getName());
        assertEquals("p", fetch1.getField("p").getName());
        assertEquals("10", fetch1.getValue(0, 0));
        assertEquals("10", fetch1.getValue(0, "p"));
        assertEquals("10", fetch1.getValue(0, fetch1.getField("p")));

        List<?> fetch2 = q.fetch("p");
        assertEquals(1, fetch2.size());
        assertEquals("10", fetch2.get(0));

        List<Long> fetch3 = q.fetch(0, Long.class);
        assertEquals(1, fetch3.size());
        assertEquals(10L, (long) fetch3.get(0));

        Record fetch4 = q.fetchAny();
        assertEquals(1, fetch4.getFields().size());
        assertEquals("p", fetch4.getField(0).getName());
        assertEquals("p", fetch4.getField("p").getName());
        assertEquals("10", fetch4.getValue(0));
        assertEquals("10", fetch4.getValue("p"));
        assertEquals("10", fetch4.getValue(fetch4.getField("p")));

        Object[] fetch5 = q.fetchArray("p");
        assertEquals(1, fetch5.length);
        assertEquals("10", fetch5[0]);

        Object[] fetch6 = q.fetchArray(0);
        assertEquals(1, fetch6.length);
        assertEquals("10", fetch6[0]);

        Long[] fetch7 = q.fetchArray(0, Long.class);
        assertEquals(1, fetch7.length);
        assertEquals(10L, (long) fetch7[0]);

        List<TestPlainSQLResultQuery> fetch8 = q.fetchInto(TestPlainSQLResultQuery.class);
        assertEquals(1, fetch8.size());
        assertEquals(10, fetch8.get(0).p);

        final Integer[] count = new Integer[] { 0 };
        q.fetchInto(new RecordHandler<Record>() {
            @Override
            public void next(Record record) {
                assertEquals(1, record.getFields().size());
                assertEquals("10", record.getValue(0));
                count[0] += 1;
            }
        });

        assertEquals(1, (int) count[0]);

        FutureResult<Record> fetch9 = q.fetchLater();
        Thread.sleep(50);
        assertTrue(fetch9.isDone());
        assertEquals(1, fetch9.get().size());
        assertEquals("10", fetch9.get().getValue(0, 0));

        Cursor<Record> fetch10 = q.fetchLazy();
        assertFalse(fetch10.isClosed());
        assertTrue(fetch10.hasNext());
        assertEquals(1, fetch10.getFields().size());
        assertEquals("p", fetch10.getField(0).getName());
        assertEquals("10", fetch10.fetchOne().getValue(0));
        assertFalse(fetch10.isClosed());
        assertFalse(fetch10.hasNext());
        assertTrue(fetch10.isClosed());

        assertEquals(fetch1.get(0), q.fetchOne());
    }

    public static class TestPlainSQLResultQuery {
        public int p;
    }

    @Test
    public void testCustomSQL() throws Exception {
        final Field<Integer> IDx2 = new CustomField<Integer>(TBook_ID().getName(), TBook_ID().getDataType()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void toSQL(RenderContext context) {
                if (context.inline()) {
                    context.sql(TBook_ID().getName() + " * 2");
                }
                else {
                    context.sql(TBook_ID().getName() + " * ?");
                }
            }

            @Override
            public void bind(BindContext context) {
                try {
                    context.statement().setInt(context.nextIndex(), 2);
                }
                catch (SQLException e) {
                    throw translate("CustomCondition.bind", getSQL(), e);
                }
            }
        };

        Condition c = new CustomCondition() {
            private static final long serialVersionUID = -629253722638033620L;

            @Override
            public void toSQL(RenderContext context) {
                context.sql(IDx2);
                context.sql(" > ");

                if (context.inline()) {
                    context.sql("3");
                }
                else {
                    context.sql("?");
                }
            }

            @Override
            public void bind(BindContext context) {
                try {
                    context.bind(IDx2);
                    context.statement().setInt(context.nextIndex(), 3);
                }
                catch (SQLException e) {
                    throw translate("CustomCondition.bind", getSQL(), e);
                }
            }
        };

        Result<Record> result = create()
            .select(TBook_ID(), IDx2)
            .from(TBook())
            .where(c)
            .orderBy(IDx2)
            .fetch();

        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
        assertEquals(Integer.valueOf(4), result.getValue(2, TBook_ID()));

        assertEquals(Integer.valueOf(4), result.getValue(0, IDx2));
        assertEquals(Integer.valueOf(6), result.getValue(1, IDx2));
        assertEquals(Integer.valueOf(8), result.getValue(2, IDx2));
    }

    @Test
    public void testUnsignedDataTypes() throws Exception {
        if (TUnsigned() == null) {
            log.info("SKIPPING", "Unsigned tests");
            return;
        }

        reset = false;

        // unsigned null values
        // --------------------
        assertEquals(1,
        create().insertInto(TUnsigned(),
                            TUnsigned_U_BYTE(),
                            TUnsigned_U_SHORT(),
                            TUnsigned_U_INT(),
                            TUnsigned_U_LONG())
                .values(null, null, null, null)
                .execute());

        assertEquals(1, create().selectCount().from(TUnsigned()).fetchOne(0));
        U u = create().selectFrom(TUnsigned()).fetchOne();
        assertNotNull(u);
        assertNull(u.getValue(TUnsigned_U_BYTE()));
        assertNull(u.getValue(TUnsigned_U_SHORT()));
        assertNull(u.getValue(TUnsigned_U_INT()));
        assertNull(u.getValue(TUnsigned_U_LONG()));

        // unsigned 1
        // ----------
        assertEquals(1,
        create().insertInto(TUnsigned())
                .set(TUnsigned_U_BYTE(), Unsigned.ubyte((byte) 1))
                .set(TUnsigned_U_SHORT(), Unsigned.ushort((short) 1))
                .set(TUnsigned_U_INT(), Unsigned.uint(1))
                .set(TUnsigned_U_LONG(), Unsigned.ulong(1L))
                .execute());

        assertEquals(2, create().selectCount().from(TUnsigned()).fetchOne(0));
        u = create().selectFrom(TUnsigned()).where(TUnsigned_U_INT().equal(Unsigned.uint(1))).fetchOne();
        assertNotNull(u);
        assertEquals(Unsigned.ubyte("1"), u.getValue(TUnsigned_U_BYTE()));
        assertEquals(Unsigned.ushort("1"), u.getValue(TUnsigned_U_SHORT()));
        assertEquals(Unsigned.uint("1"), u.getValue(TUnsigned_U_INT()));
        assertEquals(Unsigned.ulong("1"), u.getValue(TUnsigned_U_LONG()));

        assertEquals("1", u.getValue(TUnsigned_U_BYTE(), String.class));
        assertEquals("1", u.getValue(TUnsigned_U_SHORT(), String.class));
        assertEquals("1", u.getValue(TUnsigned_U_INT(), String.class));
        assertEquals("1", u.getValue(TUnsigned_U_LONG(), String.class));

        assertEquals(Unsigned.ubyte("1"), u.getValue(TUnsigned_U_BYTE()));
        assertEquals(Unsigned.ushort("1"), u.getValue(TUnsigned_U_SHORT()));
        assertEquals(Unsigned.uint("1"), u.getValue(TUnsigned_U_INT()));
        assertEquals(Unsigned.ulong("1"), u.getValue(TUnsigned_U_LONG()));

        // unsigned max-values
        // -------------------
        assertEquals(1,
        create().insertInto(TUnsigned())
                .set(TUnsigned_U_BYTE(), Unsigned.ubyte((byte) -1))
                .set(TUnsigned_U_SHORT(), Unsigned.ushort((short) -1))
                .set(TUnsigned_U_INT(), Unsigned.uint(-1))
                .set(TUnsigned_U_LONG(), Unsigned.ulong(-1L))
                .execute());

        assertEquals(3, create().selectCount().from(TUnsigned()).fetchOne(0));
        u = create().selectFrom(TUnsigned()).where(TUnsigned_U_INT().equal(Unsigned.uint(-1))).fetchOne();
        assertNotNull(u);
        assertEquals(Unsigned.ubyte(UByte.MAX_VALUE), u.getValue(TUnsigned_U_BYTE()));
        assertEquals(Unsigned.ushort(UShort.MAX_VALUE), u.getValue(TUnsigned_U_SHORT()));
        assertEquals(Unsigned.uint(UInteger.MAX_VALUE), u.getValue(TUnsigned_U_INT()));
        assertEquals(Unsigned.ulong(ULong.MAX_VALUE), u.getValue(TUnsigned_U_LONG()));

        assertEquals((byte) -1, u.getValue(TUnsigned_U_BYTE()).byteValue());
        assertEquals((short) -1, u.getValue(TUnsigned_U_SHORT()).shortValue());
        assertEquals(-1, u.getValue(TUnsigned_U_INT()).intValue());
        assertEquals(-1L, u.getValue(TUnsigned_U_LONG()).longValue());
    }

    @Test
    public void testConversion() throws Exception {
        assertEquals(null, SQLDataType.TINYINT.convert(null));
        assertEquals(null, SQLDataType.TINYINTUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.SMALLINT.convert(null));
        assertEquals(null, SQLDataType.SMALLINTUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.INTEGER.convert(null));
        assertEquals(null, SQLDataType.INTEGERUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.BIGINT.convert(null));
        assertEquals(null, SQLDataType.BIGINTUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.REAL.convert(null));
        assertEquals(null, SQLDataType.DOUBLE.convert(null));
        assertEquals(null, SQLDataType.DECIMAL_INTEGER.convert(null));
        assertEquals(null, SQLDataType.NUMERIC.convert(null));
        assertEquals(null, SQLDataType.BOOLEAN.convert(null));
        assertEquals(null, SQLDataType.VARCHAR.convert(null));
        assertEquals(null, SQLDataType.DATE.convert(null));
        assertEquals(null, SQLDataType.TIME.convert(null));
        assertEquals(null, SQLDataType.TIMESTAMP.convert(null));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert('1'));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert('1'));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert('1'));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert('1'));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert('1'));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert('1'));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert('1'));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert('1'));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert('1'));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert('1'));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert('1'));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert('1'));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert('1'));
        assertEquals("1", SQLDataType.VARCHAR.convert('1'));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert("1"));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert("1"));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert("1"));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert("1"));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert("1"));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert("1"));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert("1"));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert("1"));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert("1"));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert("1"));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert("1"));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert("1"));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert("1"));
        assertEquals("1", SQLDataType.VARCHAR.convert("1"));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert("  1"));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert("  1"));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert("  1"));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert("  1"));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert("  1"));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert("  1"));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert("  1"));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert("  1"));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert("  1"));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert("  1"));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert("  1"));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert("  1"));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert("  1"));
        assertEquals("  1", SQLDataType.VARCHAR.convert("  1"));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert((byte) 1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert((byte) 1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert((byte) 1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert((byte) 1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert((byte) 1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert((byte) 1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert((byte) 1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert((byte) 1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert((byte) 1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert((byte) 1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert((byte) 1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert((byte) 1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert((byte) 1));
        assertEquals("1", SQLDataType.VARCHAR.convert((byte) 1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert((short) 1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert((short) 1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert((short) 1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert((short) 1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert((short) 1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert((short) 1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert((short) 1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert((short) 1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert((short) 1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert((short) 1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert((short) 1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert((short) 1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert((short) 1));
        assertEquals("1", SQLDataType.VARCHAR.convert((short) 1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert(1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert(1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert(1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert(1));
        assertEquals("1", SQLDataType.VARCHAR.convert(1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert((long) 1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert((long) 1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert((long) 1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert((long) 1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert((long) 1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert((long) 1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert((long) 1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert((long) 1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert((long) 1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert((long) 1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert((long) 1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert((long) 1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert((long) 1));
        assertEquals("1", SQLDataType.VARCHAR.convert((long) 1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1.1f));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1.1f));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1.1f));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1.1f));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1.1f));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1.1f));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1.1f));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1.1f));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(1.1f));
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(1.1f));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1.1f));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(1.1f));
        assertEquals(null, SQLDataType.BOOLEAN.convert(1.1f));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(1.1f));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1.1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1.1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1.1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1.1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1.1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1.1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1.1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1.1));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(1.1));
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(1.1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1.1));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(1.1));
        assertEquals(null, SQLDataType.BOOLEAN.convert(1.1));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(1.1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(new BigInteger("1")));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(new BigInteger("1")));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(new BigInteger("1")));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(new BigInteger("1")));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert(new BigInteger("1")));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert(new BigInteger("1")));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(new BigInteger("1")));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert(new BigInteger("1")));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert(new BigInteger("1")));
        assertEquals("1", SQLDataType.VARCHAR.convert(new BigInteger("1")));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(new BigDecimal("1.1")));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(new BigDecimal("1.1")));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(new BigDecimal("1.1")));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(new BigDecimal("1.1")));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(new BigDecimal("1.1")));
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(new BigDecimal("1.1")));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(new BigDecimal("1.1")));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(new BigDecimal("1.1")));
        assertEquals(null, SQLDataType.BOOLEAN.convert(new BigDecimal("1.1")));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(new BigDecimal("1.1")));

        assertEquals(new Date(1), SQLDataType.DATE.convert(new Date(1)));
        assertEquals(new Time(1), SQLDataType.TIME.convert(new Date(1)));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(new Date(1)));

        assertEquals(new Date(1), SQLDataType.DATE.convert(new Time(1)));
        assertEquals(new Time(1), SQLDataType.TIME.convert(new Time(1)));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(new Time(1)));

        assertEquals(new Date(1), SQLDataType.DATE.convert(new Timestamp(1)));
        assertEquals(new Time(1), SQLDataType.TIME.convert(new Timestamp(1)));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(new Timestamp(1)));

        assertEquals(new Date(1), SQLDataType.DATE.convert(1L));
        assertEquals(new Time(1), SQLDataType.TIME.convert(1L));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(1L));

        // [#936] Primitive type conversion
        A author1 = create().newRecord(TAuthor());
        assertEquals(Byte.valueOf("0"), author1.getValue(TAuthor_ID(), byte.class));
        assertEquals(Short.valueOf("0"), author1.getValue(TAuthor_ID(), short.class));
        assertEquals(Integer.valueOf("0"), author1.getValue(TAuthor_ID(), int.class));
        assertEquals(Long.valueOf("0"), author1.getValue(TAuthor_ID(), long.class));
        assertEquals(Float.valueOf("0"), author1.getValue(TAuthor_ID(), float.class));
        assertEquals(Double.valueOf("0"), author1.getValue(TAuthor_ID(), double.class));
        assertEquals(Boolean.FALSE, author1.getValue(TAuthor_ID(), boolean.class));
        assertEquals(Character.valueOf((char) 0), author1.getValue(TAuthor_ID(), char.class));

        author1.setValue(TAuthor_ID(), 1);
        assertEquals(Byte.valueOf("1"), author1.getValue(TAuthor_ID(), byte.class));
        assertEquals(Short.valueOf("1"), author1.getValue(TAuthor_ID(), short.class));
        assertEquals(Integer.valueOf("1"), author1.getValue(TAuthor_ID(), int.class));
        assertEquals(Long.valueOf("1"), author1.getValue(TAuthor_ID(), long.class));
        assertEquals(Float.valueOf("1"), author1.getValue(TAuthor_ID(), float.class));
        assertEquals(Double.valueOf("1"), author1.getValue(TAuthor_ID(), double.class));
        assertEquals(Boolean.TRUE, author1.getValue(TAuthor_ID(), boolean.class));
        assertEquals(Character.valueOf('1'), author1.getValue(TAuthor_ID(), char.class));

        // [#926] Some additional date conversion checks
        // ---------------------------------------------
        A author2 = create().newRecord(TAuthor());
        author2.setValue(TAuthor_DATE_OF_BIRTH(), new Date(1));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1);

        assertEquals(new Date(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Date.class));
        assertEquals(new Time(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Time.class));
        assertEquals(new Timestamp(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Timestamp.class));
        assertEquals(new java.util.Date(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), java.util.Date.class));
        assertEquals(calendar, author2.getValue(TAuthor_DATE_OF_BIRTH(), Calendar.class));
        assertEquals(Long.valueOf(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Long.class));
        assertEquals(Long.valueOf(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), long.class));
        assertEquals(ULong.valueOf(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), ULong.class));

        // [#933] Character conversion checks
        // ----------------------------------
        author2.setValue(TAuthor_ID(), 1);
        author2.setValue(TAuthor_LAST_NAME(), "a");
        assertEquals(Character.valueOf('1'), author2.getValue(TAuthor_ID(), Character.class));
        assertEquals(Character.valueOf('1'), author2.getValue(TAuthor_ID(), char.class));
        assertEquals(Character.valueOf('a'), author2.getValue(TAuthor_LAST_NAME(), Character.class));
        assertEquals(Character.valueOf('a'), author2.getValue(TAuthor_LAST_NAME(), char.class));
        assertEquals(null, author2.getValue(TAuthor_FIRST_NAME(), Character.class));
        assertEquals(Character.valueOf((char) 0), author2.getValue(TAuthor_FIRST_NAME(), char.class));

        author2.setValue(TAuthor_ID(), 12);
        author2.setValue(TAuthor_LAST_NAME(), "ab");
        try {
            author2.getValue(TAuthor_ID(), Character.class);
            fail();
        }
        catch (DataTypeException expected) {}
        try {
            author2.getValue(TAuthor_ID(), char.class);
            fail();
        }
        catch (DataTypeException expected) {}
        try {
            author2.getValue(TAuthor_LAST_NAME(), Character.class);
            fail();
        }
        catch (DataTypeException expected) {}
        try {
            author2.getValue(TAuthor_LAST_NAME(), char.class);
            fail();
        }
        catch (DataTypeException expected) {}
    }

    @Test
    public void testConversionResult() throws Exception {
        // .fetch(..., Class)
        // ------------------
        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Byte.class));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Short.class));
        assertEquals(
            Arrays.asList(1, 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Integer.class));
        assertEquals(
            Arrays.asList(1L, 2L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Long.class));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, UByte.class));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, UShort.class));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, UInteger.class));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, ULong.class));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Float.class));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Double.class));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, BigInteger.class));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, BigDecimal.class));


        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Byte.class));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Short.class));
        assertEquals(
            Arrays.asList(1, 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Integer.class));
        assertEquals(
            Arrays.asList(1L, 2L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Long.class));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), UByte.class));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), UShort.class));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), UInteger.class));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), ULong.class));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Float.class));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Double.class));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), BigInteger.class));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), BigDecimal.class));


        // .fetchArray(..., Class)
        // ------------------
        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Byte.class)));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Short.class)));
        assertEquals(
            Arrays.asList(1, 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Integer.class)));
        assertEquals(
            Arrays.asList(1L, 2L),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Long.class)));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, UByte.class)));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, UShort.class)));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, UInteger.class)));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, ULong.class)));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Float.class)));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Double.class)));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, BigInteger.class)));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, BigDecimal.class)));


        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Byte.class)));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Short.class)));
        assertEquals(
            Arrays.asList(1, 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Integer.class)));
        assertEquals(
            Arrays.asList(1L, 2L),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Long.class)));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), UByte.class)));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), UShort.class)));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), UInteger.class)));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), ULong.class)));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Float.class)));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Double.class)));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), BigInteger.class)));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), BigDecimal.class)));


        // .fetchOne(..., Class)
        // ---------------------
        assertEquals(
            (byte) 1,
            (byte) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Byte.class));
        assertEquals(
            (short) 1,
            (short) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Short.class));
        assertEquals(
            1,
            (int) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Integer.class));
        assertEquals(
            1L,
            (long) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Long.class));
        assertEquals(
            ubyte((byte) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, UByte.class));
        assertEquals(
            ushort((short) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, UShort.class));
        assertEquals(
            uint(1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, UInteger.class));
        assertEquals(
            ulong(1L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, ULong.class));
        assertEquals(
            1.0f,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Float.class));
        assertEquals(
            1.0,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Double.class));
        assertEquals(
            new BigInteger("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, BigInteger.class));
        assertEquals(
            new BigDecimal("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, BigDecimal.class));


        assertEquals(
            (byte) 1,
            (byte) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Byte.class));
        assertEquals(
            (short) 1,
            (short) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Short.class));
        assertEquals(
            1,
            (int) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Integer.class));
        assertEquals(
            1L,
            (long) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Long.class));
        assertEquals(
            ubyte((byte) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), UByte.class));
        assertEquals(
            ushort((short) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), UShort.class));
        assertEquals(
            uint(1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), UInteger.class));
        assertEquals(
            ulong(1L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), ULong.class));
        assertEquals(
            1.0f,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Float.class));
        assertEquals(
            1.0,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Double.class));
        assertEquals(
            new BigInteger("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), BigInteger.class));
        assertEquals(
            new BigDecimal("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), BigDecimal.class));

    }

    @Test
    public void testForUpdateClauses() throws Exception {
        switch (getDialect()) {
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "FOR UPDATE tests");
                return;
        }

        // Just checking for syntax correctness. Locking should be OK
        Result<Record> result = create().select(TAuthor_ID())
                                        .from(TAuthor())
                                        .forUpdate()
                                        .fetch();
        assertEquals(2, result.size());
        Result<A> result2 = create().selectFrom(TAuthor())
                .forUpdate()
                .fetch();
        assertEquals(2, result2.size());

        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case HSQLDB:
            case H2:
            case INGRES:
            case MYSQL:
            case POSTGRES:
            case SYBASE:
                log.info("SKIPPING", "FOR UPDATE .. WAIT/NOWAIT tests");
                break;

            default: {
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .wait(2)
                        .fetch();
                assertEquals(2, result.size());
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .noWait()
                        .fetch();
                assertEquals(2, result.size());
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .skipLocked()
                        .fetch();
                assertEquals(2, result.size());


                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .wait(2)
                        .fetch();
                assertEquals(2, result2.size());
                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .noWait()
                        .fetch();
                assertEquals(2, result2.size());
                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .skipLocked()
                        .fetch();
                assertEquals(2, result2.size());
            }
        }

        switch (getDialect()) {
            case MYSQL:
                log.info("SKIPPING", "FOR UPDATE OF tests");
                break;

            // Most dialects support the OF clause
            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case ORACLE:
            case SYBASE: {
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .fetch();
                assertEquals(2, result.size());

                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .fetch();
                assertEquals(2, result2.size());

                // NO BREAK: Fall through to POSTGRES
            }

            // Postgres only supports the OF clause with tables as parameters
            case POSTGRES: {
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .of(TAuthor())
                        .fetch();
                assertEquals(2, result.size());

                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor())
                        .fetch();
                assertEquals(2, result2.size());

                break;
            }
        }

        // Only few dialects support the FOR SHARE clause:
        switch (getDialect()) {
            case MYSQL:
            case POSTGRES: {
                result = create().select(TAuthor_ID())
                                 .from(TAuthor())
                                 .forShare()
                                 .fetch();
                assertEquals(2, result.size());

                result2 = create().selectFrom(TAuthor())
                                  .forShare()
                                  .fetch();
                assertEquals(2, result2.size());
                break;
            }

            default:
                log.info("SKIPPING", "FOR SHARE clause tests");
        }
    }

    @Test
    public void testCastingToJavaClass() throws Exception {
        if (getDialect() != SQLDialect.HSQLDB) {
            assertEquals(true, create().select(cast(1, Boolean.class)).fetchOne(0));

            if (getDialect() != SQLDialect.INGRES) {
                assertEquals(true, create().select(cast("1", Boolean.class)).fetchOne(0));
            }
        }

        assertEquals(BigInteger.ONE, create().select(cast("1", BigInteger.class)).fetchOne(0));
        assertEquals(BigInteger.ONE, create().select(cast(1, BigInteger.class)).fetchOne(0));

        // Sybase applies the wrong scale when casting. Force scale before comparing (Sybase returns 1.0000 when we expect 1)
        if (getDialect() == SQLDialect.SYBASE) {
            BigDecimal result = (BigDecimal)create().select(cast("1", BigDecimal.class)).fetchOne(0);
            result = result.setScale(0);
            assertEquals(BigDecimal.ONE, result);

            result = (BigDecimal)create().select(cast(1, BigDecimal.class)).fetchOne(0);
            result = result.setScale(0);
            assertEquals(BigDecimal.ONE, result);
        } else {
            assertEquals(0, BigDecimal.ONE.compareTo((BigDecimal) create().select(cast("1", BigDecimal.class)).fetchOne(0)));
            assertEquals(0, BigDecimal.ONE.compareTo((BigDecimal) create().select(cast(1, BigDecimal.class)).fetchOne(0)));
        }

        assertEquals((byte) 1, create().select(cast("1", Byte.class)).fetchOne(0));
        assertEquals((short) 1, create().select(cast("1", Short.class)).fetchOne(0));
        assertEquals(1, create().select(cast("1", Integer.class)).fetchOne(0));
        assertEquals(1L, create().select(cast("1", Long.class)).fetchOne(0));

        assertEquals(1.0f, create().select(cast("1", Float.class)).fetchOne(0));
        assertEquals(1.0, create().select(cast("1", Double.class)).fetchOne(0));
        assertEquals("1", create().select(cast("1", String.class)).fetchOne(0));

        assertEquals((byte) 1, create().select(cast(1, Byte.class)).fetchOne(0));
        assertEquals((short) 1, create().select(cast(1, Short.class)).fetchOne(0));
        assertEquals(1, create().select(cast(1, Integer.class)).fetchOne(0));
        assertEquals(1L, create().select(cast(1, Long.class)).fetchOne(0));
        assertEquals(1.0f, create().select(cast(1, Float.class)).fetchOne(0));
        assertEquals(1.0, create().select(cast(1, Double.class)).fetchOne(0));
        assertEquals("1", create().select(cast(1, String.class)).fetchOne(0));

        // Sybase ASE does not know null bits
        if (getDialect() != SQLDialect.ASE) {
            assertEquals(null, create().select(castNull(Boolean.class)).fetchOne(0));
        }

        assertEquals(null, create().select(castNull(Byte.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Short.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Integer.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Long.class)).fetchOne(0));

        // Not implemented by the driver
        if (getDialect() != SQLDialect.SQLITE) {
            assertEquals(null, create().select(castNull(BigInteger.class)).fetchOne(0));
            assertEquals(null, create().select(castNull(BigDecimal.class)).fetchOne(0));
        }

        assertEquals(null, create().select(castNull(Float.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Double.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(String.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Date.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Time.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Timestamp.class)).fetchOne(0));

        assertEquals(1984, create()
            .select(TBook_TITLE().cast(Integer.class))
            .from(TBook())
            .where(TBook_ID().equal(1))
            .fetch().getValue(0, 0));

        assertEquals(1984L, create()
            .select(TBook_TITLE().cast(Long.class))
            .from(TBook())
            .where(TBook_ID().equal(1))
            .fetch().getValue(0, 0));
    }

    @Test
    public void testCastingToSQLDataType() throws Exception {
        for (DataType<?> type : Arrays.<DataType<?>> asList(
            SQLDataType.BIGINT,
            SQLDataType.BINARY,
            SQLDataType.BIT,
            SQLDataType.BLOB,
            SQLDataType.BOOLEAN,
            SQLDataType.CHAR,
            SQLDataType.CLOB,
            SQLDataType.DATE,
            SQLDataType.DECIMAL,
            SQLDataType.DECIMAL_INTEGER,
            SQLDataType.DOUBLE,
            SQLDataType.FLOAT,
            SQLDataType.INTEGER,
            SQLDataType.LONGNVARCHAR,
            SQLDataType.LONGVARBINARY,
            SQLDataType.LONGVARCHAR,
            SQLDataType.NCHAR,
            SQLDataType.NCLOB,
            SQLDataType.NUMERIC,
            SQLDataType.NVARCHAR,

//          TODO: is there any meaningful cast for OTHER?
//          SQLDataType.OTHER,

            SQLDataType.REAL,
            SQLDataType.SMALLINT,
            SQLDataType.TIME,
            SQLDataType.TIMESTAMP,
            SQLDataType.TINYINT,
            SQLDataType.VARBINARY,
            SQLDataType.VARCHAR)) {

            if (getDialect() == SQLDialect.ORACLE) {
                if (type.getType() == byte[].class ||
                    type == SQLDataType.CLOB ||
                    type == SQLDataType.NCLOB) {

                    log.info("SKIPPING", "Casting to lob type in Oracle");
                    continue;
                }
            }

            if (getDialect() == SQLDialect.ASE) {
                if (type.getType() == Boolean.class) {
                    log.info("SKIPPING", "Casting to bit type in Sybase ASE");
                    continue;
                }
            }

            assertEquals(null, create().select(val(null).cast(type)).fetchOne(0));
        }
    }

    @Test
    public void testCastingToDialectDataType() throws Exception {
        for (DataType<?> type : getCastableDataTypes()) {
            assertEquals(null, create().select(val(null).cast(type)).fetchOne(0));
        }
    }

    @Test
    public void testSequences() throws Exception {
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
            return;
        }

        reset = false;

        @SuppressWarnings("unchecked")
        Sequence<? extends Number> sequence = (Sequence<? extends Number>) cSequences().getField("S_AUTHOR_ID").get(cSequences());
        Field<? extends Number> nextval = sequence.nextval();
        Field<? extends Number> currval = null;

        assertEquals("3", "" + create().select(nextval).fetchOne(nextval));
        assertEquals("4", "" + create().select(nextval).fetchOne(nextval));
        assertEquals("5", "" + create().select(nextval).fetchOne(nextval));

        switch (getDialect()) {
            // HSQLDB and DERBY don't support currval, so don't test it
            case HSQLDB:
            case DERBY:

            // Ingres has smoe weird issue, claiming that NEXT VALUE was not
            // requested before CURRENT VALUE
            case INGRES:
                log.info("SKIPPING", "Sequence CURRVAL tests");
                break;

            default:
                currval = sequence.currval();
                assertEquals("5", "" + create().select(currval).fetchOne(currval));
                assertEquals("5", "" + create().select(currval).fetchOne(currval));

                assertEquals(5, create().currval(sequence).intValue());
                assertEquals(5, create().currval(sequence).intValue());
        }

        assertEquals("6", "" + create().select(nextval).fetchOne(nextval));

        // Test convenience syntax
        assertEquals(7, create().nextval(sequence).intValue());
        assertEquals(8, create().nextval(sequence).intValue());
    }

    @Test
    public void testSelectSimpleQuery() throws Exception {
        SelectQuery q = create().selectQuery();
        Field<Integer> f1 = val(1).as("f1");
        Field<Double> f2 = val(2d).as("f2");
        Field<String> f3 = val("test").as("f3");

        q.addSelect(f1);
        q.addSelect(f2);
        q.addSelect(f3);

        int i = q.execute();
        Result<?> result = q.getResult();

        assertEquals(1, i);
        assertEquals(1, result.size());
        assertEquals(3, result.getFields().size());
        assertTrue(result.getFields().contains(f1));
        assertTrue(result.getFields().contains(f2));
        assertTrue(result.getFields().contains(f3));

        assertEquals(3, result.get(0).getFields().size());
        assertTrue(result.get(0).getFields().contains(f1));
        assertTrue(result.get(0).getFields().contains(f2));
        assertTrue(result.get(0).getFields().contains(f3));

        assertEquals(Integer.valueOf(1), result.get(0).getValue(f1));
        assertEquals(2d, result.get(0).getValue(f2));
        assertEquals("test", result.get(0).getValue(f3));
    }

    @Test
    public void testSelectCountQuery() throws Exception {
        assertEquals(4, create().selectCount().from(TBook()).fetchOne(0));
        assertEquals(2, create().selectCount().from(TAuthor()).fetchOne(0));
    }

    @Test
    public void testSelectQuery() throws Exception {
        SelectQuery q = create().selectQuery();
        q.addFrom(TAuthor());
        q.addSelect(TAuthor().getFields());
        q.addOrderBy(TAuthor_LAST_NAME());

        int rows = q.execute();
        Result<?> result = q.getResult();

        assertEquals(2, rows);
        assertEquals(2, result.size());
        assertEquals("Coelho", result.get(0).getValue(TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.get(1).getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testAccessInternalRepresentation() throws Exception {
        SelectQuery query =
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().in(1, 2, 3))
                .getQuery();

        query.addGroupBy(TBook_ID());
        query.addHaving(count().greaterOrEqual(1));
        query.addOrderBy(TBook_ID());
        query.execute();

        Result<Record> result = query.getResult();

        assertEquals(3, result.size());
        assertEquals(Arrays.asList(1, 2, 3), result.getValues(TBook_ID()));
    }

    @Test
    public void testLimit() throws Exception {
        SQLDialect dialect = getDialect();

        int lower = 0;

        // The following dialects don't support LIMIT 0 / TOP 0
        switch (dialect) {
            case DB2:
            case DERBY:
            case HSQLDB:
            case INGRES:
            case SYBASE:
                lower = 1;
        }

        for (int i = lower; i < 6; i++) {
            assertEquals(Math.min(i, 4),
                create().selectFrom(TBook()).limit(i).fetch().size());
            assertEquals(Math.min(i, 4),
                create().select().from(TBook()).limit(i).fetch().size());
        }

        if (getDialect() == SQLDialect.ASE) {
            log.info("SKIPPING", "LIMIT .. OFFSET tests");
            return;
        }

        for (int i = lower; i < 6; i++) {
            assertEquals(Math.min(i, 3),
                create().selectFrom(TBook()).limit(1, i).fetch().size());
            assertEquals(Math.min(i, 3),
                create().selectFrom(TBook()).limit(i).offset(1).fetch().size());
            assertEquals(Math.min(i, 3),
                create().select().from(TBook()).limit(1, i).fetch().size());
            assertEquals(Math.min(i, 3),
                create().select().from(TBook()).limit(i).offset(1).fetch().size());
        }

        Result<B> result = create()
            .selectFrom(TBook())
            .orderBy(TBook_ID(), TBook_AUTHOR_ID())
            .limit(1, 2)
            .fetch();

        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
    }

    @Test
    public void testLimitNested() throws Exception {
        // TODO [#780] This is not supported in Ingres
        if (getDialect() == SQLDialect.INGRES ||
            getDialect() == SQLDialect.ASE) {

            log.info("SKIPPING", "LIMIT clauses in nested SELECTs");
            return;
        }

        Table<B> nested = table(create()
            .selectFrom(TBook())
            .orderBy(TBook_ID().desc())
            .limit(2))
            .as("nested");

        Field<Integer> nestedID = nested.getField(TBook_AUTHOR_ID());
        Record record = create().select(nestedID, count())
            .from(nested)
            .groupBy(nestedID)
            .orderBy(nestedID)
            .fetchOne();

        assertEquals(Integer.valueOf(2), record.getValue(nestedID));
        assertEquals(Integer.valueOf(2), record.getValue(1));

        Result<Record> result = create().select(nestedID, count())
            .from(create().selectFrom(TBook())
                          .orderBy(TBook_ID().desc())
                          .limit(1, 2).asTable("nested"))
            .groupBy(nestedID)
            .orderBy(nestedID)
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.getValue(0, nestedID));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(2), result.getValue(1, nestedID));
        assertEquals(Integer.valueOf(1), result.getValue(1, 1));
    }

    @Test
    public void testTypeConversions() throws Exception {
        Record record = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Orwell"));

        assertEquals("George", record.getValue(TAuthor_FIRST_NAME()));
        assertEquals("George", record.getValueAsString(TAuthor_FIRST_NAME()));
        assertEquals("George", record.getValueAsString(TAuthor_FIRST_NAME(), "gnarf"));
        assertEquals("George", record.getValueAsString(1));
        assertEquals("George", record.getValueAsString(1, "gnarf"));

        assertEquals(Integer.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Integer.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), 123));
        assertEquals(Integer.valueOf("1903"), record.getValue(4));
        assertEquals(Integer.valueOf("1903"), record.getValue(4, 123));

        assertEquals(Short.valueOf("1903"), record.getValueAsShort(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Short.valueOf("1903"), record.getValueAsShort(TAuthor_YEAR_OF_BIRTH(), (short) 123));
        assertEquals(Short.valueOf("1903"), record.getValueAsShort(4));
        assertEquals(Short.valueOf("1903"), record.getValueAsShort(4, (short) 123));

        assertEquals(Long.valueOf("1903"), record.getValueAsLong(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Long.valueOf("1903"), record.getValueAsLong(TAuthor_YEAR_OF_BIRTH(), 123L));
        assertEquals(Long.valueOf("1903"), record.getValueAsLong(4));
        assertEquals(Long.valueOf("1903"), record.getValueAsLong(4, 123L));

        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(TAuthor_YEAR_OF_BIRTH(), new BigInteger("123")));
        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(4));
        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(4, new BigInteger("123")));

        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(TAuthor_YEAR_OF_BIRTH(), 123f));
        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(4));
        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(4, 123f));

        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(TAuthor_YEAR_OF_BIRTH(), 123d));
        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(4));
        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(4, 123d));

        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(TAuthor_YEAR_OF_BIRTH(), new BigDecimal("123")));
        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(4));
        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(4, new BigDecimal("123")));


        long dateOfBirth = record.getValue(TAuthor_DATE_OF_BIRTH()).getTime();
        assertEquals(dateOfBirth, record.getValueAsDate(TAuthor_DATE_OF_BIRTH()).getTime());
        assertEquals(dateOfBirth, record.getValueAsTimestamp(TAuthor_DATE_OF_BIRTH()).getTime());
        assertEquals(dateOfBirth, record.getValueAsTime(TAuthor_DATE_OF_BIRTH()).getTime());
    }

    @Test
    public void testConditionalSelect() throws Exception {
        Condition c = trueCondition();

        assertEquals(4, create().selectFrom(TBook()).where(c).execute());

        c = c.and(TBook_PUBLISHED_IN().greaterThan(1945));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());

        c = c.not();
        assertEquals(1, create().selectFrom(TBook()).where(c).execute());

        c = c.or(TBook_AUTHOR_ID().equal(
            create().select(TAuthor_ID()).from(TAuthor()).where(TAuthor_FIRST_NAME().equal("Paulo"))));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());
    }

    @Test
    public void testConditions() throws Exception {
        // The BETWEEN clause
        assertEquals(Arrays.asList(2, 3), create().select()
            .from(TBook())
            .where(TBook_ID().between(2, 3))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(3, 4), create().select()
            .from(TBook())
            .where(val(3).between(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // The IN clause
        // [#502] empty set checks
        assertEquals(Arrays.asList(), create().select()
            .from(TBook())
            .where(TBook_ID().in(new Integer[0]))
            .fetch(TBook_ID()));
        assertEquals(BOOK_IDS, create().select()
            .from(TBook())
            .where(TBook_ID().notIn(new Integer[0]))
            .orderBy(TBook_ID())
            .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2), create().select()
            .from(TBook())
            .where(TBook_ID().in(1, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(2, 3, 4), create().select()
            .from(TBook())
            .where(val(2).in(TBook_ID(), TBook_AUTHOR_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // = { ALL | ANY | SOME }
        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "= { ALL | ANY | SOME } tests");
                break;

            default: {

                // Testing = ALL(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAll(create().selectOne()))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAny(create().selectOne()))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // [#1048] TODO: Simulate this for other dialects
                if (asList(H2, HSQLDB, POSTGRES).contains(getDialect())) {
                    // Testing = ALL(array)
                    assertEquals(Arrays.asList(1), create().select(TBook_ID())
                        .from(TBook())
                        .where(TBook_ID().equalAll(1))
                        .orderBy(TBook_ID()).fetch(TBook_ID()));
                    assertEquals(Arrays.asList(), create().select(TBook_ID())
                        .from(TBook())
                        .where(TBook_ID().equalAll(1, 2))
                        .orderBy(TBook_ID()).fetch(TBook_ID()));

                    // Testing = ANY(array)
                    assertEquals(Arrays.asList(1), create().select(TBook_ID())
                        .from(TBook())
                        .where(TBook_ID().equalAny(1))
                        .orderBy(TBook_ID()).fetch(TBook_ID()));
                    assertEquals(Arrays.asList(1, 2), create().select(TBook_ID())
                        .from(TBook())
                        .where(TBook_ID().equalAny(1, 2))
                        .orderBy(TBook_ID()).fetch(TBook_ID()));
                }

                // Inducing the above to work the same way as all other operators
                // Check all operators in a single query
                assertEquals(Arrays.asList(3), create()
                    .select()
                    .from(TBook())
                    .where(TBook_ID().equal(create().select(val(3))))
                    .and(TBook_ID().equalAll(create().select(val(3))))
                    .and(TBook_ID().equalAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4))))
                    .and(TBook_ID().notEqual(create().select(val(1))))
                    .and(TBook_ID().notEqualAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().notEqualAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().greaterOrEqual(create().select(val(1))))
                    .and(TBook_ID().greaterOrEqualAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .and(TBook_ID().greaterOrEqualAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().greaterThan(create().select(val(1))))
                    .and(TBook_ID().greaterThanAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .and(TBook_ID().greaterThanAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().lessOrEqual(create().select(val(3))))
                    .and(TBook_ID().lessOrEqualAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4))))
                    .and(TBook_ID().lessOrEqualAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().lessThan(create().select(val(4))))
                    .and(TBook_ID().lessThanAll(create().select(val(4))))
                    .and(TBook_ID().lessThanAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .fetch(TBook_ID()));

                break;
            }
        }
    }

    @Test
    public void testLargeINCondition() throws Exception {
        Field<Integer> count = count();
        assertEquals(1, (int) create().select(count)
                                      .from(TBook())
                                      .where(TBook_ID().in(Collections.nCopies(999, 1)))
                                      .fetchOne(count));

        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "SQLite can't handle more than 999 variables");
                break;

            default:
                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1000, 1)))
                    .fetchOne(count));

                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1001, 1)))
                    .fetchOne(count));

                // SQL Server's is at 2100...
                // Sybase ASE's is at 2000...
                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1950, 1)))
                    .fetchOne(count));

                assertEquals(3, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().notIn(Collections.nCopies(1950, 1)))
                    .fetchOne(count));

                break;
        }
    }

    @Test
    public void testSubSelect() throws Exception {
        // ---------------------------------------------------------------------
        // Testing the IN condition
        // ---------------------------------------------------------------------
        assertEquals(3,
            create().selectFrom(TBook())
                .where(TBook_TITLE().notIn(create()
                    .select(TBook_TITLE())
                    .from(TBook())
                    .where(TBook_TITLE().in("1984"))))
                .execute());

        // ---------------------------------------------------------------------
        // Testing the EXISTS condition
        // ---------------------------------------------------------------------
        assertEquals(3,
            create()
                .selectFrom(TBook())
                .whereNotExists(create()
                    .selectOne()
                    .from(TAuthor())
                    .where(TAuthor_YEAR_OF_BIRTH().greaterOrEqual(TBook_PUBLISHED_IN())))

                // Add additional useless queries to check query correctness
                .orNotExists(create().select())
                .andExists(create().select()).execute());

        // ---------------------------------------------------------------------
        // Testing selecting from a select
        // ---------------------------------------------------------------------
        Table<Record> nested = create().select(TBook_AUTHOR_ID(), count().as("books"))
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID()).asTable("nested");

        Result<Record> records = create().select(nested.getFields())
            .from(nested)
            .orderBy(nested.getField("books"), nested.getField(TBook_AUTHOR_ID())).fetch();

        assertEquals(2, records.size());
        assertEquals(Integer.valueOf(1), records.getValue(0, nested.getField(TBook_AUTHOR_ID())));
        assertEquals(Integer.valueOf(2), records.getValue(0, nested.getField("books")));
        assertEquals(Integer.valueOf(2), records.getValue(1, nested.getField(TBook_AUTHOR_ID())));
        assertEquals(Integer.valueOf(2), records.getValue(1, nested.getField("books")));

        Field<Object> books = create().select(count())
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(TAuthor_ID())).asField("books");

        records = create().select(TAuthor_ID(), books)
                          .from(TAuthor())
                          .orderBy(books, TAuthor_ID()).fetch();

        assertEquals(2, records.size());
        assertEquals(Integer.valueOf(1), records.getValue(0, TAuthor_ID()));
        assertEquals(Integer.valueOf(2), records.getValue(0, books));
        assertEquals(Integer.valueOf(2), records.getValue(1, TAuthor_ID()));
        assertEquals(Integer.valueOf(2), records.getValue(1, books));

        // ---------------------------------------------------------------------
        // [#493, #632] Testing filtering by a select's outcome
        // ---------------------------------------------------------------------

        // TODO [#632] Handle this for Sybase
        assertEquals("Coelho",
        create().select(TAuthor_LAST_NAME())
                .from(TAuthor())
                .where(val(0).equal(create()
                             .select(count(TBook_ID()))
                             .from(TBook())
                             .where(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                             .and(TBook_TITLE().equal("1984"))))
                .limit(1)
                .fetchOne(TAuthor_LAST_NAME()));
    }

    @Test
    public void testDistinctQuery() throws Exception {
        Result<Record> result = create()
            .selectDistinct(TBook_AUTHOR_ID())
            .from(TBook())
            .orderBy(TBook_AUTHOR_ID())
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(Integer.valueOf(2), result.get(1).getValue(TBook_AUTHOR_ID()));

        assertEquals(2, create()
            .select(countDistinct(TBook_AUTHOR_ID()))
            .from(TBook())
            .fetchOne(0));
        assertEquals(2, create()
            .selectDistinct(TBook_AUTHOR_ID())
            .from(TBook())
            .fetch()
            .size());
    }

    @Test
    public void testFetch() throws Exception {
        SelectQuery q = create().selectQuery();
        q.addFrom(TAuthor());
        q.addSelect(TAuthor().getFields());
        q.addOrderBy(TAuthor_LAST_NAME());

        Result<?> result = q.fetch();

        assertEquals(2, result.size());
        assertEquals("Coelho", result.get(0).getValue(TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.get(1).getValue(TAuthor_LAST_NAME()));

        try {
            q.fetchOne();
            fail();
        }
        catch (InvalidResultException expected) {}

        Record record = q.fetchAny();
        assertEquals("Coelho", record.getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testFetchMany() throws Exception {
        switch (getDialect()) {
            case ORACLE:
            case SQLITE:
            case SYBASE:
                log.info("SKIPPING", "Fetch Many tests");
                return;
        }

        List<Result<Record>> results = create().fetchMany(
            "select * from t_book order by " + TBook_ID().getName());

        assertEquals(1, results.size());
        assertEquals(4, results.get(0).size());
        assertEquals(BOOK_IDS, results.get(0).getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_TITLES, results.get(0).getValues(TBook_TITLE()));
    }

    @Test
    public void testFetchIntoWithAnnotations() throws Exception {
        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        List<BookWithAnnotations> result =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetchInto(BookWithAnnotations.class);

        assertEquals(4, result.size());

        assertEquals(1, (int) result.get(0).id);
        assertEquals(2, (int) result.get(1).id);
        assertEquals(3, (int) result.get(2).id);
        assertEquals(4, (int) result.get(3).id);

        assertEquals(1, result.get(0).id2);
        assertEquals(2, result.get(1).id2);
        assertEquals(3, result.get(2).id2);
        assertEquals(4, result.get(3).id2);

        assertEquals(1, result.get(0).id3);
        assertEquals(2, result.get(1).id3);
        assertEquals(3, result.get(2).id3);
        assertEquals(4, result.get(3).id3);

        assertEquals(Long.valueOf(1), result.get(0).id4);
        assertEquals(Long.valueOf(2), result.get(1).id4);
        assertEquals(Long.valueOf(3), result.get(2).id4);
        assertEquals(Long.valueOf(4), result.get(3).id4);

        assertEquals(1L, result.get(0).id5);
        assertEquals(2L, result.get(1).id5);
        assertEquals(3L, result.get(2).id5);
        assertEquals(4L, result.get(3).id5);

        assertEquals("1984", result.get(0).title);
        assertEquals("Animal Farm", result.get(1).title);
        assertEquals("O Alquimista", result.get(2).title);
        assertEquals("Brida", result.get(3).title);

        assertEquals("George", result.get(0).firstName);
        assertEquals("George", result.get(1).firstName);
        assertEquals("Paulo", result.get(2).firstName);
        assertEquals("Paulo", result.get(3).firstName);

        assertEquals("George", result.get(0).firstName2);
        assertEquals("George", result.get(1).firstName2);
        assertEquals("Paulo", result.get(2).firstName2);
        assertEquals("Paulo", result.get(3).firstName2);

        assertEquals("Orwell", result.get(0).lastName);
        assertEquals("Orwell", result.get(1).lastName);
        assertEquals("Coelho", result.get(2).lastName);
        assertEquals("Coelho", result.get(3).lastName);

        assertEquals("Orwell", result.get(0).lastName2);
        assertEquals("Orwell", result.get(1).lastName2);
        assertEquals("Coelho", result.get(2).lastName2);
        assertEquals("Coelho", result.get(3).lastName2);

        try {
            // Cannot instanciate an abstract class
            create().selectFrom(TAuthor())
                    .fetchInto(AbstractList.class);
            fail();
        }
        catch (MappingException expected) {}

        try {
            // Cannot a class without default constructor
            create().selectFrom(TAuthor())
                    .fetchInto(Math.class);
            fail();
        }
        catch (MappingException expected) {}

        // [#930] Calendar/Date conversion checks
        // --------------------------------------
        List<DatesWithAnnotations> calendars =
        create().select(TAuthor_DATE_OF_BIRTH())
                .from(TAuthor())
                .orderBy(TAuthor_ID())
                .fetchInto(DatesWithAnnotations.class);

        assertEquals(2, calendars.size());

        for (int index : asList(0, 1)) {
            assertEquals(calendars.get(index).cal1, calendars.get(index).cal2);
            assertEquals(calendars.get(index).cal1, calendars.get(index).cal3);

            assertEquals(calendars.get(index).date1, calendars.get(index).date2);
            assertEquals(calendars.get(index).date1, calendars.get(index).date3);

            assertEquals(calendars.get(index).long1, calendars.get(index).long2);
            assertEquals(calendars.get(index).long1, calendars.get(index).long3);

            assertEquals(calendars.get(index).primitiveLong1, calendars.get(index).primitiveLong2);
            assertEquals(calendars.get(index).primitiveLong1, calendars.get(index).primitiveLong3);

            assertEquals(calendars.get(index).cal1.getTime(), calendars.get(index).date1);
            assertEquals(calendars.get(index).cal1.getTime().getTime(), calendars.get(index).date1.getTime());
            assertEquals(calendars.get(index).cal1.getTime().getTime(), calendars.get(index).long1.longValue());
            assertEquals(calendars.get(index).cal1.getTime().getTime(), calendars.get(index).primitiveLong1);
        }

        A author = create().newRecord(TAuthor());
        DatesWithAnnotations dates = author.into(DatesWithAnnotations.class);

        assertNull(dates.cal1);
        assertNull(dates.cal2);
        assertNull(dates.cal3);
        assertNull(dates.date1);
        assertNull(dates.date2);
        assertNull(dates.date3);
        assertNull(dates.long1);
        assertNull(dates.long2);
        assertNull(dates.long3);
        assertEquals(0L, dates.primitiveLong1);
        assertEquals(0L, dates.primitiveLong2);
        assertEquals(0L, dates.primitiveLong3);

        author = create().newRecord(TAuthor());
        author.setValue(TAuthor_DATE_OF_BIRTH(), new Date(1L));
        dates = author.into(DatesWithAnnotations.class);

        assertEquals(1L, dates.cal1.getTime().getTime());
        assertEquals(1L, dates.cal2.getTime().getTime());
        assertEquals(1L, dates.cal3.getTime().getTime());
        assertEquals(1L, dates.date1.getTime());
        assertEquals(1L, dates.date2.getTime());
        assertEquals(1L, dates.date3.getTime());
        assertEquals(1L, (long) dates.long1);
        assertEquals(1L, (long) dates.long2);
        assertEquals(1L, (long) dates.long3);
        assertEquals(1L, dates.primitiveLong1);
        assertEquals(1L, dates.primitiveLong2);
        assertEquals(1L, dates.primitiveLong3);
    }

    @Test
    public void testFetchIntoWithoutAnnotations() throws Exception {
        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        List<BookWithoutAnnotations> result =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetchInto(BookWithoutAnnotations.class);

        assertEquals(4, result.size());

        assertEquals(1, (int) result.get(0).id);
        assertEquals(2, (int) result.get(1).id);
        assertEquals(3, (int) result.get(2).id);
        assertEquals(4, (int) result.get(3).id);

        assertEquals(1, result.get(0).id2);
        assertEquals(2, result.get(1).id2);
        assertEquals(3, result.get(2).id2);
        assertEquals(4, result.get(3).id2);

        assertEquals(1, result.get(0).ID);
        assertEquals(2, result.get(1).ID);
        assertEquals(3, result.get(2).ID);
        assertEquals(4, result.get(3).ID);

        assertEquals("1984", result.get(0).title);
        assertEquals("Animal Farm", result.get(1).title);
        assertEquals("O Alquimista", result.get(2).title);
        assertEquals("Brida", result.get(3).title);

        assertEquals("George", result.get(0).firstName);
        assertEquals("George", result.get(1).firstName);
        assertEquals("Paulo", result.get(2).firstName);
        assertEquals("Paulo", result.get(3).firstName);

        assertEquals("George", result.get(0).firstName2);
        assertEquals("George", result.get(1).firstName2);
        assertEquals("Paulo", result.get(2).firstName2);
        assertEquals("Paulo", result.get(3).firstName2);

        assertEquals("Orwell", result.get(0).lastName);
        assertEquals("Orwell", result.get(1).lastName);
        assertEquals("Coelho", result.get(2).lastName);
        assertEquals("Coelho", result.get(3).lastName);

        assertEquals("Orwell", result.get(0).lastName2);
        assertEquals("Orwell", result.get(1).lastName2);
        assertEquals("Coelho", result.get(2).lastName2);
        assertEquals("Coelho", result.get(3).lastName2);

        assertEquals("Orwell", result.get(0).LAST_NAME);
        assertEquals("Orwell", result.get(1).LAST_NAME);
        assertEquals("Coelho", result.get(2).LAST_NAME);
        assertEquals("Coelho", result.get(3).LAST_NAME);
    }

    @Test
    public void testRecordFromWithAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        BookWithAnnotations b = new BookWithAnnotations();
        b.firstName = "Edgar Allen";
        b.lastName2 = "Poe";
        b.dateOfBirth = new Date(1);
        b.id = 17;
        b.title = "The Raven";

        // This data shouldn't be considered
        b.id2 = 18;
        b.lastName = "Poet";

        B book = create().newRecord(TBook(), b);
        A author = create().newRecord(TAuthor(), b);

        assertEquals(b.id, author.getValue(TAuthor_ID()));
        assertEquals(b.firstName, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals(b.lastName2, author.getValue(TAuthor_LAST_NAME()));
        assertEquals(b.dateOfBirth, author.getValue(TAuthor_DATE_OF_BIRTH()));
        assertNull(author.getValue(TAuthor_YEAR_OF_BIRTH()));

        assertEquals(b.id, book.getValue(TBook_ID()));
        assertEquals(b.title, book.getValue(TBook_TITLE()));
        assertNull(book.getValue(TBook_AUTHOR_ID()));
        assertNull(book.getValue(TBook_CONTENT_PDF()));
        assertNull(book.getValue(TBook_CONTENT_TEXT()));
        assertNull(book.getValue(TBook_LANGUAGE_ID()));
        assertNull(book.getValue(TBook_PUBLISHED_IN()));
    }

    @Test
    public void testRecordFromWithoutAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        BookWithoutAnnotations b = new BookWithoutAnnotations();
        b.firstName = "Edgar Allen";
        b.lastName = "Poe";
        b.DATE_OF_BIRTH = new Date(1);
        b.id = 17;
        b.title = "The Raven";

        // This data shouldn't be considered
        b.id2 = 18;
        b.ID = 19;
        b.LAST_NAME = "Poet";
        b.dateOfBirth = new Date(2);

        B book = create().newRecord(TBook(), b);
        A author = create().newRecord(TAuthor(), b);

        assertEquals(b.id, author.getValue(TAuthor_ID()));
        assertEquals(b.firstName, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals(b.lastName, author.getValue(TAuthor_LAST_NAME()));
        assertEquals(b.DATE_OF_BIRTH, author.getValue(TAuthor_DATE_OF_BIRTH()));
        assertNull(author.getValue(TAuthor_YEAR_OF_BIRTH()));

        assertEquals(b.id, book.getValue(TBook_ID()));
        assertEquals(b.title, book.getValue(TBook_TITLE()));
        assertNull(book.getValue(TBook_AUTHOR_ID()));
        assertNull(book.getValue(TBook_CONTENT_PDF()));
        assertNull(book.getValue(TBook_CONTENT_TEXT()));
        assertNull(book.getValue(TBook_LANGUAGE_ID()));
        assertNull(book.getValue(TBook_PUBLISHED_IN()));
    }

    @Test
    public void testRecordFromUpdatePK() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "testRecordFromUpdatePK() tests");
                return;
        }

        reset = false;

        // [#979] When using Record.from(), and the PK remains unchanged, there
        // must not result an INSERT on a subsequent call to .store()
        A author1 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(1)).fetchOne();
        AuthorWithoutAnnotations into1 = author1.into(AuthorWithoutAnnotations.class);
        into1.yearOfBirth = null;
        author1.from(into1);
        assertEquals(1, author1.store());

        A author2 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(1)).fetchOne();
        assertEquals(author1, author2);
        assertEquals(author1.getValue(TAuthor_ID()), author2.getValue(TAuthor_ID()));
        assertEquals(author1.getValue(TAuthor_FIRST_NAME()), author2.getValue(TAuthor_FIRST_NAME()));
        assertEquals(author1.getValue(TAuthor_LAST_NAME()), author2.getValue(TAuthor_LAST_NAME()));
        assertEquals(author1.getValue(TAuthor_DATE_OF_BIRTH()), author2.getValue(TAuthor_DATE_OF_BIRTH()));
        assertEquals(author1.getValue(TAuthor_YEAR_OF_BIRTH()), author2.getValue(TAuthor_YEAR_OF_BIRTH()));
        assertNull(author2.getValue(TAuthor_YEAR_OF_BIRTH()));

        // But when the PK is modified, be sure an INSERT is executed
        A author3 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(1)).fetchOne();
        AuthorWithoutAnnotations into2 = author3.into(AuthorWithoutAnnotations.class);
        into2.ID = 3;
        author3.from(into2);
        assertEquals(1, author3.store());

        A author4 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(3)).fetchOne();
        assertEquals(author3, author4);
    }

    @Test
    public void testReflectionWithAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        // [#933] Map values to char / Character
        A author1 = create().newRecord(TAuthor());
        CharWithAnnotations c1 = author1.into(CharWithAnnotations.class);
        assertEquals((char) 0, c1.id1);
        assertEquals(null, c1.id2);
        assertEquals((char) 0, c1.last1);
        assertEquals(null, c1.last2);

        author1.setValue(TAuthor_ID(), 1);
        author1.setValue(TAuthor_LAST_NAME(), "a");
        CharWithAnnotations c2 = author1.into(CharWithAnnotations.class);
        assertEquals('1', c2.id1);
        assertEquals('1', c2.id2.charValue());
        assertEquals('a', c2.last1);
        assertEquals('a', c2.last2.charValue());

        A author2 = create().newRecord(TAuthor(), c2);
        assertEquals('1', author2.getValue(TAuthor_ID(), char.class).charValue());
        assertEquals('1', author2.getValue(TAuthor_ID(), Character.class).charValue());
        assertEquals('a', author2.getValue(TAuthor_LAST_NAME(), char.class).charValue());
        assertEquals('a', author2.getValue(TAuthor_LAST_NAME(), Character.class).charValue());

        // [#934] Static members are not to be considered
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), new StaticWithAnnotations()));
        create().newRecord(TBook()).into(StaticWithAnnotations.class);
        assertEquals(13, StaticWithAnnotations.ID);

        // [#935] Final member fields are considered when reading
        B book = create().newRecord(TBook());
        book.setValue(TBook_ID(), new FinalWithAnnotations().ID);
        assertEquals(book, create().newRecord(TBook(), new FinalWithAnnotations()));

        // [#935] ... but not when writing
        FinalWithAnnotations f = create().newRecord(TBook()).into(FinalWithAnnotations.class);
        assertEquals(f.ID, new FinalWithAnnotations().ID);
    }

    @Test
    public void testReflectionWithoutAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        // Arbitrary sources should have no effect
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), (Object) null));
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), new Object()));

        // [#934] Static members are not to be considered
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), new StaticWithoutAnnotations()));
        create().newRecord(TBook()).into(StaticWithoutAnnotations.class);
        assertEquals(13, StaticWithoutAnnotations.ID);

        // [#935] Final member fields are considered when reading
        B book = create().newRecord(TBook());
        book.setValue(TBook_ID(), new FinalWithoutAnnotations().ID);
        assertEquals(book, create().newRecord(TBook(), new FinalWithoutAnnotations()));

        // [#935] ... but not when writing
        FinalWithoutAnnotations f = create().newRecord(TBook()).into(FinalWithoutAnnotations.class);
        assertEquals(f.ID, new FinalWithoutAnnotations().ID);
    }

    @Test
    public void testFetchIntoCustomTable() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        Result<BookRecord> result =
            create().select(
                        TBook_ID(),
                        TBook_TITLE(),
                        TAuthor_FIRST_NAME(),
                        TAuthor_LAST_NAME(),
                        TAuthor_DATE_OF_BIRTH())
                    .from(TBook())
                    .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                    .orderBy(TBook_ID())
                    .fetchInto(BookTable.BOOK);

        assertEquals(4, result.size());

        assertEquals(BOOK_IDS_SHORT, result.getValues(3));
        assertEquals(BOOK_IDS_SHORT, result.getValues(TBook_ID()));
        assertEquals(BOOK_IDS_SHORT, result.getValues(BookTable.ID));
        assertEquals(Short.valueOf((short) 1), result.getValue(0, BookTable.ID));
        assertEquals(Short.valueOf((short) 2), result.getValue(1, BookTable.ID));
        assertEquals(Short.valueOf((short) 3), result.getValue(2, BookTable.ID));
        assertEquals(Short.valueOf((short) 4), result.getValue(3, BookTable.ID));

        assertEquals(BOOK_TITLES, result.getValues(4));
        assertEquals(BOOK_TITLES, result.getValues(TBook_TITLE()));
        assertEquals(BOOK_TITLES, result.getValues(BookTable.TITLE));

        assertEquals(BOOK_FIRST_NAMES, result.getValues(0));
        assertEquals(BOOK_FIRST_NAMES, result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_FIRST_NAMES, result.getValues(BookTable.FIRST_NAME));

        assertEquals(BOOK_LAST_NAMES, result.getValues(2));
        assertEquals(BOOK_LAST_NAMES, result.getValues(TAuthor_LAST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result.getValues(BookTable.LAST_NAME));

        assertEquals(Collections.nCopies(4, null), result.getValues(1));
        assertEquals(Collections.nCopies(4, null), result.getValues(BookTable.UNMATCHED));
    }

    @Test
    public void testFetchIntoRecordHandler() throws Exception {

        // Test a simple query with typed records
        // --------------------------------------
        final Queue<Integer> ids = new LinkedList<Integer>();
        final Queue<String> titles = new LinkedList<String>();

        ids.addAll(BOOK_IDS);
        titles.addAll(BOOK_TITLES);

        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchInto(new RecordHandler<B>() {
                    @Override
                    public void next(B record) {
                        assertEquals(ids.poll(), record.getValue(TBook_ID()));
                        assertEquals(titles.poll(), record.getValue(TBook_TITLE()));
                    }
                });

        assertTrue(ids.isEmpty());
        assertTrue(titles.isEmpty());

        // Test lazy fetching
        // --------------------------------------
        ids.addAll(BOOK_IDS);
        titles.addAll(BOOK_TITLES);

        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchLazy()
                .fetchInto(new RecordHandler<B>() {
                    @Override
                    public void next(B record) {
                        assertEquals(ids.poll(), record.getValue(TBook_ID()));
                        assertEquals(titles.poll(), record.getValue(TBook_TITLE()));
                    }
                });

        assertTrue(ids.isEmpty());
        assertTrue(titles.isEmpty());

        // Test a generic query with any records
        // -------------------------------------
        final Queue<Integer> authorIDs = new LinkedList<Integer>();
        final Queue<Integer> count = new LinkedList<Integer>();

        authorIDs.addAll(Arrays.asList(1, 2));
        count.addAll(Arrays.asList(2, 2));

        create().select(TBook_AUTHOR_ID(), count())
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetchInto(new RecordHandler<Record>() {
                    @Override
                    public void next(Record record) {
                        assertEquals(authorIDs.poll(), record.getValue(TBook_AUTHOR_ID()));
                        assertEquals(count.poll(), record.getValue(count()));
                    }
                });
    }

    @Test
    public void testFetchLater() throws Exception {
        Future<Result<B>> later;
        Result<B> result;

        int activeCount = Thread.activeCount();

        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();

        // That's too fast for the query to be done, mostly
        assertFalse(later.isDone());
        assertFalse(later.isCancelled());
        assertEquals(activeCount + 1, Thread.activeCount());

        // Get should make sure the internal thread is terminated
        result = later.get();
        Thread.sleep(500);
        assertEquals(activeCount, Thread.activeCount());

        // Subsequent gets are ok
        result = later.get();
        result = later.get(1000, TimeUnit.MILLISECONDS);

        // Check the data
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));

        // Start new threads
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        assertEquals(activeCount + 4, Thread.activeCount());

        // This should be enough to ensure that GC will collect finished threads
        later = null;
        System.gc();
        System.gc();
        Thread.sleep(500);
        assertEquals(activeCount, Thread.activeCount());
    }

    @Test
    public void testGrouping() throws Exception {

        // Test a simple group by query
        Field<Integer> count = count().as("c");
        Result<Record> result = create()
            .select(TBook_AUTHOR_ID(), count)
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID()).fetch();

        assertEquals(2, result.size());
        assertEquals(2, (int) result.get(0).getValue(count));
        assertEquals(2, (int) result.get(1).getValue(count));

        // Test a group by query with a single HAVING clause
        result = create()
            .select(TAuthor_LAST_NAME(), count)
            .from(TBook())
            .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
            .where(TBook_TITLE().notEqual("1984"))
            .groupBy(TAuthor_LAST_NAME())
            .having(count().equal(2))
            .fetch();

        assertEquals(1, result.size());
        assertEquals(2, (int) result.getValue(0, count));
        assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));

        // Test a group by query with a combined HAVING clause
        result = create()
            .select(TAuthor_LAST_NAME(), count)
            .from(TBook())
            .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
            .where(TBook_TITLE().notEqual("1984"))
            .groupBy(TAuthor_LAST_NAME())
            .having(count().equal(2))
            .or(count().greaterOrEqual(2))
            .andExists(create().selectOne())
            .fetch();

        assertEquals(1, result.size());
        assertEquals(2, (int) result.getValue(0, count));
        assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));

        // Test a group by query with a plain SQL having clause
        result = create()
            .select(VLibrary_AUTHOR(), count)
            .from(VLibrary())
            .where(VLibrary_TITLE().notEqual("1984"))
            .groupBy(VLibrary_AUTHOR())

            // MySQL seems to have a bug with fully qualified view names in the
            // having clause. TODO: Fully analyse this issue
            // https://sourceforge.net/apps/trac/jooq/ticket/277
            .having("v_library.author like ?", "Paulo%")
            .fetch();

        assertEquals(1, result.size());
        assertEquals(2, (int) result.getValue(0, count));

        // SQLite loses type information when views select functions.
        // In this case: concatenation. So as a workaround, SQLlite only selects
        // FIRST_NAME in the view
        assertEquals("Paulo", result.getValue(0, VLibrary_AUTHOR()).substring(0, 5));
    }

    @Test
    public void testGroupByCubeRollup() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "Group by CUBE / ROLLUP tests");
                return;
        }

        Result<Record> result;

        // Simple ROLLUP clause
        // --------------------
        result = create().select(
                    TBook_ID(),
                    TBook_AUTHOR_ID())
                .from(TBook())
                .groupBy(rollup(
                    TBook_ID(),
                    TBook_AUTHOR_ID()))
                .fetch();

        System.out.println(result.format());
        assertEquals(9, result.size());

        if (getDialect() == DB2) {
            assertEquals(Arrays.asList(null, 1, 2, 3, 4, 1, 2, 3, 4), result.getValues(0));
            assertEquals(Arrays.asList(null, null, null, null, null, 1, 1, 2, 2), result.getValues(1));
        }
        else {
            assertEquals(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, null), result.getValues(0));
            assertEquals(Arrays.asList(1, null, 1, null, 2, null, 2, null, null), result.getValues(1));
        }

        if (getDialect() == MYSQL) {
            log.info("SKIPPING", "CUBE and GROUPING SETS tests");
            return;
        }

        // ROLLUP clause
        // -------------
        Field<Integer> groupingId = groupingId(TBook_ID(), TBook_AUTHOR_ID());
        if (asList(DB2, SYBASE).contains(getDialect()))
            groupingId = one();

        result = create().select(
                    TBook_ID(),
                    TBook_AUTHOR_ID(),
                    grouping(TBook_ID()),
                    groupingId)
                .from(TBook())
                .groupBy(rollup(
                    TBook_ID(),
                    TBook_AUTHOR_ID()))
                .orderBy(
                    TBook_ID().asc().nullsFirst(),
                    TBook_AUTHOR_ID().asc().nullsFirst()).fetch();

        assertEquals(9, result.size());
        assertEquals(Arrays.asList(null, 1, 1, 2, 2, 3, 3, 4, 4), result.getValues(0));
        assertEquals(Arrays.asList(null, null, 1, null, 1, null, 2, null, 2), result.getValues(1));
        assertEquals(Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0), result.getValues(2));

        if (!asList(DB2, SYBASE).contains(getDialect()))
            assertEquals(Arrays.asList(3, 1, 0, 1, 0, 1, 0, 1, 0), result.getValues(3));

        // CUBE clause
        // -----------
        result = create().select(
                    TBook_ID(),
                    TBook_AUTHOR_ID(),
                    grouping(TBook_ID()),
                    groupingId)
                .from(TBook())
                .groupBy(cube(
                    TBook_ID(),
                    TBook_AUTHOR_ID()))
                .orderBy(
                    TBook_ID().asc().nullsFirst(),
                    TBook_AUTHOR_ID().asc().nullsFirst()).fetch();

        assertEquals(11, result.size());
        assertEquals(Arrays.asList(null, null, null, 1, 1, 2, 2, 3, 3, 4, 4), result.getValues(0));
        assertEquals(Arrays.asList(null, 1, 2, null, 1, null, 1, null, 2, null, 2), result.getValues(1));
        assertEquals(Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0), result.getValues(2));

        if (!asList(DB2, SYBASE).contains(getDialect()))
            assertEquals(Arrays.asList(3, 2, 2, 1, 0, 1, 0, 1, 0, 1, 0), result.getValues(3));

        // GROUPING SETS clause
        // --------------------
        result = create().select(
                    TBook_ID(),
                    TBook_AUTHOR_ID(),
                    grouping(TBook_ID()),
                    groupingId)
                .from(TBook())
                .groupBy(groupingSets(
                    new Field<?>[] { TBook_AUTHOR_ID(), TBook_ID() },
                    new Field<?>[] { TBook_AUTHOR_ID(), TBook_LANGUAGE_ID() },
                    new Field<?>[0],
                    new Field<?>[0]))
                .orderBy(
                    TBook_ID().asc().nullsFirst(),
                    TBook_AUTHOR_ID().asc().nullsFirst()).fetch();

        assertEquals(9, result.size());
        assertEquals(Arrays.asList(null, null, null, null, null, 1, 2, 3, 4), result.getValues(0));
        assertEquals(Arrays.asList(null, null, 1, 2, 2, 1, 1, 2, 2), result.getValues(1));
        assertEquals(Arrays.asList(1, 1, 1, 1, 1, 0, 0, 0, 0), result.getValues(2));

        if (!asList(DB2, SYBASE).contains(getDialect()))
            assertEquals(Arrays.asList(3, 3, 2, 2, 2, 0, 0, 0, 0), result.getValues(3));
    }

    @Test
    public void testHavingWithoutGrouping() throws Exception {
        try {
            assertEquals(Integer.valueOf(1), create()
                .selectOne()
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(1))
                .having(count().greaterOrEqual(2))
                .fetchOne(0));
            assertEquals(null, create()
                .selectOne()
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(1))
                .having(count().greaterOrEqual(3))
                .fetchOne(0));
        }
        catch (DataAccessException e) {

            // HAVING without GROUP BY is not supported by some dialects,
            // So this exception is OK
            switch (getDialect()) {
                case SQLITE:
                    log.info("SKIPPING", "HAVING without GROUP BY is not supported: " + e.getMessage());
                    break;

                default:
                    throw e;
            }
        }
    }

    @Test
    public void testInsertUpdateDelete() throws Exception {
        reset = false;

        long time = System.currentTimeMillis();

        InsertQuery<A> i = create().insertQuery(TAuthor());
        i.addValue(TAuthor_ID(), 100);
        i.addValue(TAuthor_FIRST_NAME(), "Hermann");
        i.addValue(TAuthor_LAST_NAME(), "Hesse");
        i.addValue(TAuthor_DATE_OF_BIRTH(), new Date(time));
        i.addValue(TAuthor_YEAR_OF_BIRTH(), 2010);

        // Check insertion of UDTs and Enums if applicable
        if (TAuthor_ADDRESS() != null) {
            addAddressValue(i, TAuthor_ADDRESS());
        }

        assertEquals(1, i.execute());

        A author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Hermann"));
        assertEquals("Hermann", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Hesse", author.getValue(TAuthor_LAST_NAME()));

        // TODO [#1009] This doesn't work yet. Add more substantial tz tests
        // assertEquals(time, author.getValue(TAuthor_DATE_OF_BIRTH()).getTime());

        Map<Field<?>, String> map = new HashMap<Field<?>, String>();
        map.put(TAuthor_FIRST_NAME(), "Hermie");

        assertEquals(1, create()
            .update(TAuthor())
            .set(map)
            .where(TAuthor_ID().equal(100))
            .execute());

        author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Hermie"));
        assertEquals("Hermie", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Hesse", author.getValue(TAuthor_LAST_NAME()));

        if (TAuthor_ADDRESS() != null) {
            UDTRecord<?> address = author.getValue(TAuthor_ADDRESS());
            Object street1 = invoke(address, "getStreet");
            Object street2 = invoke(street1, "getStreet");
            assertEquals("Bahnhofstrasse", street2);
        }

        create().delete(TAuthor()).where(TAuthor_ID().equal(100)).execute();
        assertEquals(null, create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Hermie")));
    }

    // Generic type safety...
    private final <Z extends UDTRecord<?>> void addAddressValue(StoreQuery<?> q, Field<Z> field) throws Exception {
        Class<? extends Z> addressType = field.getType();
        Class<?> countryType = addressType.getMethod("getCountry").getReturnType();
        Class<?> streetType = addressType.getMethod("getStreet").getReturnType();

        Object country = null;
        try {
            countryType.getMethod("valueOf", String.class).invoke(countryType, "Germany");
        }
        catch (NoSuchMethodException e) {
            country = "Germany";
        }

        Object street = streetType.newInstance();
        Z address = addressType.newInstance();

        streetType.getMethod("setStreet", String.class).invoke(street, "Bahnhofstrasse");
        streetType.getMethod("setNo", String.class).invoke(street, "1");

        addressType.getMethod("setCountry", countryType).invoke(address, country);
        addressType.getMethod("setCity", String.class).invoke(address, "Calw");
        addressType.getMethod("setStreet", streetType).invoke(address, street);

        q.addValue(field, address);
    }

    @Test
    public void testInsertImplicit() throws Exception {
        reset = false;

        assertEquals(1,
        create().insertInto(TAuthor())
                .values(
                    37,
                    "Erich",
                    "Kästner",
                    null,
                    null,
                    null)
                .execute());

        A author = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(37)).fetchOne();
        assertNotNull(author);
        assertEquals(37, (int) author.getValue(TAuthor_ID()));
        assertEquals("Erich", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testInsertMultiple() throws Exception {
        reset = false;

        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())

                // API check. Object...
                .values(val(37), "Dürrenmatt")

                // Collection<?>
                .values(Arrays.<Object> asList(88, "Schmitt"))

                // Field<?>...
                .values(val(93), val("Kästner"))
                .execute();

        Result<A> authors =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().greaterThan(30))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(3, authors.size());
        assertEquals(Integer.valueOf(37), authors.getValue(0, TAuthor_ID()));
        assertEquals(Integer.valueOf(88), authors.getValue(1, TAuthor_ID()));
        assertEquals(Integer.valueOf(93), authors.getValue(2, TAuthor_ID()));
        assertEquals("Dürrenmatt", authors.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Schmitt", authors.getValue(1, TAuthor_LAST_NAME()));
        assertEquals("Kästner", authors.getValue(2, TAuthor_LAST_NAME()));

        // Another test for the SET API
        create().insertInto(TAuthor())
                .set(TAuthor_ID(), val(137))
                .set(TAuthor_LAST_NAME(), "Dürrenmatt 2")
                .newRecord()
                .set(TAuthor_ID(), 188)
                .set(TAuthor_LAST_NAME(), "Schmitt 2")
                .newRecord()
                .set(TAuthor_ID(), val(193))
                .set(TAuthor_LAST_NAME(), "Kästner 2")
                .execute();

        authors =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().greaterThan(130))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(3, authors.size());
        assertEquals(Integer.valueOf(137), authors.getValue(0, TAuthor_ID()));
        assertEquals(Integer.valueOf(188), authors.getValue(1, TAuthor_ID()));
        assertEquals(Integer.valueOf(193), authors.getValue(2, TAuthor_ID()));
        assertEquals("Dürrenmatt 2", authors.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Schmitt 2", authors.getValue(1, TAuthor_LAST_NAME()));
        assertEquals("Kästner 2", authors.getValue(2, TAuthor_LAST_NAME()));
    }

    @Test
    public void testInsertConvert() throws Exception {
        reset = false;

        // [#1005] With the INSERT .. VALUES syntax, typesafety cannot be
        // enforced. But the inserted values should at least be converted to the
        // right types

        // Explicit field list
        assertEquals(1,
        create().insertInto(TAuthor(),
                    TAuthor_ID(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH(),
                    TAuthor_YEAR_OF_BIRTH())
                .values(
                    "5",
                    "Smith",
                    0L,
                    new BigDecimal("1980"))
                .execute());

        A author1 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(5)).fetchOne();
        assertNotNull(author1);
        assertEquals(5, (int) author1.getValue(TAuthor_ID()));
        assertEquals("Smith", author1.getValue(TAuthor_LAST_NAME()));

        // TODO [#1009] This doesn't work yet. Add more substantial tz tests
        // assertEquals(0L, author1.getValue(TAuthor_DATE_OF_BIRTH()).getTime());
        assertEquals(1980, (int) author1.getValue(TAuthor_YEAR_OF_BIRTH()));

        // Implicit field list
        assertEquals(1,
        create().insertInto(TAuthor())
                .values(
                    "37",
                    "Erich",
                    "Kästner",
                    null,
                    null,
                    null)
                .execute());

        A author2 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(37)).fetchOne();
        assertNotNull(author2);
        assertEquals(37, (int) author2.getValue(TAuthor_ID()));
        assertEquals("Erich", author2.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Kästner", author2.getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testInsertSelect() throws Exception {
        reset = false;

        Field<?> nullField = null;
        switch (getDialect()) {
            case ORACLE:
            case POSTGRES:
                // TODO: cast this to the UDT type
                nullField = cast(null, TAuthor_ADDRESS());
                break;
            default:
                nullField = castNull(String.class);
                break;
        }

        Insert<A> i = create().insertInto(
            TAuthor(),
            create().select(vals(
                1000,
                val("Lukas")))
            .select(vals(
                "Eder",
                val(new Date(363589200000L)),
                castNull(Integer.class),
                nullField)));

        i.execute();

        A author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Lukas"));
        assertEquals("Lukas", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Eder", author.getValue(TAuthor_LAST_NAME()));
        assertEquals(null, author.getValue(TAuthor_YEAR_OF_BIRTH()));
    }

    @Test
    public void testInsertWithSelectAsField() throws Exception {
        reset = false;

        Field<Integer> ID3;
        Field<Integer> ID4;

        switch (getDialect()) {
            // Sybase ASE doesn't allow for selecting data inside VALUES()
            case ASE:

            // MySQL doesn't allow for selecting from the INSERT INTO table
            case MYSQL:
                ID3 = create().select(val(3)).asField();
                ID4 = create().select(val(4)).asField();
                break;
            default:
                ID3 = create()
                    .select(max(TAuthor_ID()).add(1))
                    .from(TAuthor()).asField();
                ID4 = create()
                    .select(max(TAuthor_ID()).add(1))
                    .from(TAuthor()).asField();
                break;
        }

        create().insertInto(TAuthor(),
                    TAuthor_ID(),
                    TAuthor_LAST_NAME())
                .values(
                    ID3,
                    create().select(val("Hornby")).asField())
                .execute();

        A author = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hornby"));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Hornby", author.getValue(TAuthor_LAST_NAME()));

        create().update(TAuthor())
                .set(TAuthor_ID(), ID4)
                .set(TAuthor_LAST_NAME(), create().select(val("Hitchcock")).<String> asField())
                .where(TAuthor_ID().equal(3))
                .execute();

        author = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hitchcock"));
        assertEquals(Integer.valueOf(4), author.getValue(TAuthor_ID()));
        assertEquals("Hitchcock", author.getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testUpdateSelect() throws Exception {
        switch (getDialect()) {
            case SQLITE:
            case MYSQL:
                log.info("SKIPPING", "UPDATE .. SET .. = (SELECT ..) integration test. This syntax is poorly supported by " + getDialect());
                return;
        }

        reset = false;

        Table<A> a1 = TAuthor();
        Table<A> a2 = TAuthor().as("a2");
        Field<String> f1 = a1.getField(TAuthor_FIRST_NAME());
        Field<String> f2 = a2.getField(TAuthor_FIRST_NAME());
        Field<String> f3 = a2.getField(TAuthor_LAST_NAME());

        UpdateQuery<A> u = create().updateQuery(a1);
        u.addValue(f1, create().select(max(f3)).from(a2).where(f1.equal(f2)).<String> asField());
        u.execute();

        Field<Integer> c = count();
        assertEquals(Integer.valueOf(2), create().select(c)
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal(TAuthor_LAST_NAME()))
            .fetchOne(c));
    }

    @Test
    public void testOnDuplicateKey() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case INGRES:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "ON DUPLICATE KEY UPDATE test");
                return;
        }

        reset = false;

        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Koontz")
                .onDuplicateKeyUpdate()
                .set(TAuthor_LAST_NAME(), "Koontz")
                .execute();
        A author =
        create().fetchOne(TAuthor(), TAuthor_ID().equal(3));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Koontz", author.getValue(TAuthor_LAST_NAME()));
        assertEquals(Integer.valueOf(3), create().select(count()).from(TAuthor()).fetchOne(0));

        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Rose")
                .onDuplicateKeyUpdate()
                .set(TAuthor_LAST_NAME(), "Christie")
                .execute();
        author =
        create().fetchOne(TAuthor(), TAuthor_ID().equal(3));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Christie", author.getValue(TAuthor_LAST_NAME()));
        assertEquals(Integer.valueOf(3), create().select(count()).from(TAuthor()).fetchOne(0));
    }

    @Test
    public void testMerge() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case INGRES:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "Merge tests");
                return;
        }

        reset = false;

        // Always do an update of everything
        // --------------------------------
        create().mergeInto(TAuthor())
                .using(create().selectOne())
                .on("1 = 1")
                .whenMatchedThenUpdate()
                .set(TAuthor_FIRST_NAME(), "Alfred")
                .whenNotMatchedThenInsert(TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Hitchcock")
                .execute();

        assertEquals(Arrays.asList("Alfred", "Alfred"),
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch(TAuthor_FIRST_NAME()));

        // Always do an update of the first author
        // --------------------------------
        create().mergeInto(TAuthor())
                .using(create().selectOne())
                .on(TAuthor_ID().equal(1))
                .whenMatchedThenUpdate()
                .set(TAuthor_FIRST_NAME(), "John")
                .whenNotMatchedThenInsert(TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Hitchcock")
                .execute();

        assertEquals(Arrays.asList("John", "Alfred"),
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch(TAuthor_FIRST_NAME()));

        Field<String> f = val("Dan").as("f");
        Field<String> l = val("Brown").as("l");

        // [#1000] Add a check for the alternative INSERT .. SET .. syntax
        // --------------------------------
        MergeFinalStep<A> q =
        create().mergeInto(TAuthor())
                .using(create().select(f, l))
                .on(TAuthor_LAST_NAME().equal(l))
                .whenMatchedThenUpdate()
                .set(TAuthor_FIRST_NAME(), "James")
                .whenNotMatchedThenInsert()
                .set(TAuthor_ID(), 3)
                .set(TAuthor_FIRST_NAME(), f)
                .set(TAuthor_LAST_NAME(), l);

        // Execute an insert
        q.execute();
        assertEquals(Arrays.asList("John", "Alfred", "Dan"),
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch(TAuthor_FIRST_NAME()));

        // Execute an update
        q.execute();
        assertEquals(Arrays.asList("John", "Alfred", "James"),
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch(TAuthor_FIRST_NAME()));

        f = val("Herman").as("f");
        l = val("Hesse").as("l");

        // Check if INSERT-only MERGE works
        // --------------------------------
        q =
        create().mergeInto(TAuthor())
                .using(create().select(f, l))
                .on(TAuthor_LAST_NAME().equal(l))
                .whenNotMatchedThenInsert(
                    TAuthor_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())

                // [#1010] Be sure that this type-unsafe clause can deal with
                // any convertable type
                .values("4", f, l, 0L);

        // Execute an insert
        q.execute();
        assertEquals(Arrays.asList("John", "Alfred", "James", "Herman"),
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch(TAuthor_FIRST_NAME()));

        // Execute nothing
        q.execute();
        assertEquals(Arrays.asList("John", "Alfred", "James", "Herman"),
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch(TAuthor_FIRST_NAME()));

        // TODO: Add more sophisticated MERGE statement tests
        // Especially for SQL Server and Sybase, some bugs could be expected
    }

    @Test
    public void testBlobAndClob() throws Exception {
        reset = false;

        // Superficial tests in T_BOOK table
        // ---------------------------------
        B book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        assertTrue(book.getValue(TBook_CONTENT_TEXT()).contains("doublethink"));
        assertEquals(null, book.getValue(TBook_CONTENT_PDF()));

        book.setValue(TBook_CONTENT_TEXT(), "Blah blah");
        book.setValue(TBook_CONTENT_PDF(), "Blah blah".getBytes());
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        assertEquals("Blah blah", book.getValue(TBook_CONTENT_TEXT()));
        assertEquals("Blah blah", new String(book.getValue(TBook_CONTENT_PDF())));

        // More in-depth tests in T_725_LOB_TEST table
        // -------------------------------------------
        T725 record = create().newRecord(T725());

        // Store and fetch NULL value
        record.setValue(T725_ID(), 1);
        assertEquals(1, record.store());
        record.refresh();
        assertNull(record.getValue(T725_LOB()));

        // Store and fetch empty byte[]. In some RDBMS, this is the same as null
        record.setValue(T725_LOB(), new byte[0]);
        assertEquals(1, record.store());
        record.refresh();

        switch (getDialect()) {

            // In ASE, there don't seem to be any empty byte[]
            case ASE:
                assertEquals(1, record.getValue(T725_LOB()).length);
                assertEquals(0, record.getValue(T725_LOB())[0]);
                break;

            // These don't make a difference between an empty byte[] and null
            case ORACLE:
            case SQLITE:
                assertNull(record.getValue(T725_LOB()));
                break;

            default:
                assertEquals(0, record.getValue(T725_LOB()).length);
                break;
        }

        // Store and fetch a filled byte[]
        record.setValue(T725_LOB(), "Blah".getBytes());
        assertEquals(1, record.store());
        record.refresh();
        assertEquals("Blah", new String(record.getValue(T725_LOB())));

        assertEquals(1, create().query("insert into " + T725().getName() + " values (?, ?)", 2, (Object) null).execute());
        assertEquals(1, create().query("insert into " + T725().getName() + " values (?, ?)", 3, new byte[0]).execute());
        assertEquals(1, create().query("insert into " + T725().getName() + " values (?, ?)", 4, "abc".getBytes()).execute());

        record.setValue(T725_ID(), 2);
        record.refresh();
        assertNull(record.getValue(T725_LOB()));

        record.setValue(T725_ID(), 3);
        record.refresh();

        switch (getDialect()) {
            case ASE:
                assertEquals(1, record.getValue(T725_LOB()).length);
                assertEquals(0, record.getValue(T725_LOB())[0]);
                break;

            case ORACLE:
            case SQLITE:
                assertNull(record.getValue(T725_LOB()));
                break;

            default:
                assertEquals(0, record.getValue(T725_LOB()).length);
                break;
        }

        record.setValue(T725_ID(), 4);
        record.refresh();
        assertEquals("abc", new String(record.getValue(T725_LOB())));

        Result<Record> result = create().fetch(
            "select " + T725_ID().getName() + ", " + T725_LOB().getName() +
            " from " + T725().getName() +
            " order by " + T725_ID().getName());
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(0));
        assertNull(result.getValue(1, 1));

        switch (getDialect()) {
            case ASE:
                assertEquals(1, result.getValue(2, T725_LOB()).length);
                assertEquals(0, result.getValue(2, T725_LOB())[0]);
                break;

            case ORACLE:
            case SQLITE:
                assertNull(result.getValue(2, T725_LOB()));
                break;

            default:
                assertEquals(0, result.getValue(2, T725_LOB()).length);
                break;
        }

        assertEquals("abc", new String((byte[]) result.getValue(3, T725_LOB().getName())));
    }

    @Test
    public void testManager() throws Exception {
        reset = false;

        List<A> select = create().fetch(TAuthor());
        assertEquals(2, select.size());

        select = create().fetch(TAuthor(), TAuthor_FIRST_NAME().equal("Paulo"));
        assertEquals(1, select.size());
        assertEquals("Paulo", select.get(0).getValue(TAuthor_FIRST_NAME()));

        try {
            create().fetchOne(TAuthor());
            fail();
        }
        catch (InvalidResultException expected) {}

        A selectOne = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Paulo"));
        assertEquals("Paulo", selectOne.getValue(TAuthor_FIRST_NAME()));

        // Some CRUD operations
        A author = create().newRecord(TAuthor());
        author.setValue(TAuthor_ID(), 15);
        author.setValue(TAuthor_LAST_NAME(), "Kästner");

        assertEquals(1, create().executeInsert(TAuthor(), author));
        author.refresh();
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        assertEquals(0, create().executeUpdate(TAuthor(), author, TAuthor_ID().equal(15)));
        author.setValue(TAuthor_FIRST_NAME(), "Erich");
        assertEquals(1, create().executeUpdate(TAuthor(), author, TAuthor_ID().equal(15)));
        author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Erich"));
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Erich", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        create().executeDelete(TAuthor(), TAuthor_LAST_NAME().equal("Kästner"));
        assertEquals(null, create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Erich")));
    }

    @Test
    public void testRelations() throws Exception {
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "referentials test");
            return;
        }

        reset = false;

        // Get the book 1984
        B book1984 = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        // Navigate to the book's author
        Record authorOrwell = (Record) invoke(book1984, "fetchTAuthorByAuthorId");
        assertEquals("Orwell", authorOrwell.getValue(TAuthor_LAST_NAME()));

        // Navigate back to the author's books
        List<?> books1 = (List<?>) invoke(authorOrwell, "fetchTBookListByAuthorId");
        assertEquals(2, books1.size());

        // Navigate through m:n relationships of books
        List<Object> booksToBookStores = new ArrayList<Object>();
        for (Object b : books1) {
            booksToBookStores.addAll((List<?>) invoke(b, "fetchTBookToBookStoreList"));
        }
        assertEquals(3, booksToBookStores.size());

        // Navigate to book stores
        Set<String> bookStoreNames = new TreeSet<String>();
        List<Object> bookStores = new ArrayList<Object>();
        for (Object b : booksToBookStores) {
            Object store = invoke(b, "fetchTBookStore");
            bookStores.add(store);
            bookStoreNames.add((String) invoke(store, "getName"));
        }
        assertEquals(Arrays.asList("Ex Libris", "Orell Füssli"), new ArrayList<String>(bookStoreNames));

        // Navigate through m:n relationships of book stores
        booksToBookStores = new ArrayList<Object>();
        for (Object b : bookStores) {
            booksToBookStores.addAll((List<?>) invoke(b, "fetchTBookToBookStoreList"));
        }

        // Navigate back to books
        Set<String> book2Names = new TreeSet<String>();
        List<Object> books2 = new ArrayList<Object>();
        for (Object b : booksToBookStores) {
            Object book = invoke(b, "fetchTBook");
            books2.add(book);
            book2Names.add((String) invoke(book, "getTitle"));
        }
        assertEquals(Arrays.asList("1984", "Animal Farm", "O Alquimista"), new ArrayList<String>(book2Names));

        // Navigate back to authors
        Set<String> authorNames = new TreeSet<String>();
        for (Object b : books2) {
            Object author = invoke(b, "fetchTAuthorByAuthorId");
            authorNames.add((String) invoke(author, "getLastName"));
        }
        assertEquals(Arrays.asList("Coelho", "Orwell"), new ArrayList<String>(authorNames));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testUpdatablesPK() throws Exception {
        reset = false;

        B book = create().newRecord(TBook());
        try {
            book.refresh();
        }
        catch (InvalidResultException expected) {}

        // Fetch the original record
        B book1 = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        // Another copy of the original record
        B book2 = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        // Immediately store the original record. That shouldn't have any effect
        assertEquals(0, book1.store());

        // Modify and store the original record
        Integer id = book1.getValue(TBook_ID());
        book1.setValue(TBook_TITLE(), "1985");
        assertEquals(1, book1.store());

        // Fetch the modified record
        book1 = create().fetchOne(TBook(), TBook_ID().equal(id));

        // Modify the record
        book1.setValue(TBook_TITLE(), "1999");
        assertEquals("1999", book1.getValue(TBook_TITLE()));

        // And refresh it again
        book1.refresh();
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals(0, book1.store());

        // Refresh the other copy of the original record
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1984", book2.getValue(TBook_TITLE()));
        book2.refresh();

        assertEquals(id, book1.getValue(TBook_ID()));
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // No ON DELETE CASCADE constraints for Sybase ASE
        if (getDialect() == SQLDialect.ASE) {
            create().truncate((Table) table("t_book_to_book_store")).execute();
        }

        // Delete the modified record
        assertEquals(1, book1.delete());
        assertEquals(0, book1.delete());
        assertEquals(0, book2.delete());

        // Fetch the remaining records
        assertEquals(null, create().fetchOne(TBook(), TBook_ID().equal(id)));

        // Store the record again from memory
        assertEquals(1, book1.store());
        book1.refresh();
        book2.refresh();

        assertEquals(id, book1.getValue(TBook_ID()));
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // Copy the records and store them again as another one
        book1 = book1.copy();
        book2 = book2.copy();
        assertNull(book1.getValue(TBook_ID()));
        assertNull(book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // Can't store the copies yet, as the primary key is null
        try {
            book1.store();
        } catch (DataAccessException expected) {}
        try {
            book2.store();
        } catch (DataAccessException expected) {}

        book1.setValue(TBook_ID(), 11);
        book2.setValue(TBook_ID(), 12);
        assertEquals(1, book1.store());
        assertEquals(1, book2.store());

        // Refresh the books
        book1 = create().newRecord(TBook());
        book2 = create().newRecord(TBook());

        book1.setValue(TBook_ID(), 11);
        book2.setValue(TBook_ID(), 12);

        book1.refresh();
        book2.refresh();

        assertEquals(Integer.valueOf(11), book1.getValue(TBook_ID()));
        assertEquals(Integer.valueOf(12), book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // Store a partial record
        A author = create().newRecord(TAuthor());
        author.setValue(TAuthor_ID(), 77);
        author.setValue(TAuthor_LAST_NAME(), "Döblin");
        assertEquals(1, author.store());
        assertEquals(Integer.valueOf(77),
            create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Döblin")).getValue(TAuthor_ID()));

        // Store an empty record
        S store = create().newRecord(TBookStore());
        assertEquals(0, store.store());

        // [#787] Store the same record twice.
        author = create().newRecord(TAuthor());
        author.setValue(TAuthor_ID(), 78);
        author.setValue(TAuthor_LAST_NAME(), "Cohen");
        assertEquals(1, author.store());
        assertEquals(0, author.store()); // No INSERT/UPDATE should be made

        author.setValue(TAuthor_FIRST_NAME(), "Arthur");
        assertEquals(1, author.store()); // This should produce an UPDATE
        assertEquals(1, create()
            .select(count())
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("Arthur"))
            .and(TAuthor_LAST_NAME().equal("Cohen"))
            .fetchOne(0));

        // [#945] Set the same value twice
        author = create().selectFrom(TAuthor())
                         .where(TAuthor_FIRST_NAME().equal("Arthur"))
                         .fetchOne();

        author.setValue(TAuthor_FIRST_NAME(), "Leonard");
        author.setValue(TAuthor_FIRST_NAME(), "Leonard");
        assertEquals(1, author.store());
        assertEquals(1, create()
            .select(count())
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("Leonard"))
            .and(TAuthor_LAST_NAME().equal("Cohen"))
            .fetchOne(0));
    }

    @Test
    public void testUpdatablesPKChangePK() throws Exception {
        reset = false;

        // [#979] some additional tests related to modifying an updatable's
        // primary key. Setting it to the same value shouldn't result in an
        // INSERT statement...

        // This will result in no query
        B book1 = create().fetchOne(TBook(), TBook_ID().equal(1));
        book1.setValue(TBook_ID(), 1);
        assertEquals(0, book1.store());

        // This will result in an UPDATE
        book1.setValue(TBook_ID(), 1);
        book1.setValue(TBook_TITLE(), "new title");
        assertEquals(1, book1.store());
        assertEquals(4, create().selectCount().from(TBook()).fetchOne(0));

        B book2 = create().fetchOne(TBook(), TBook_ID().equal(1));
        assertEquals(1, (int) book2.getValue(TBook_ID()));
        assertEquals("new title", book2.getValue(TBook_TITLE()));

        // This should now result in an INSERT
        book2.setValue(TBook_ID(), 5);
        assertEquals(1, book2.store());

        B book3 = create().fetchOne(TBook(), TBook_ID().equal(5));
        assertEquals(5, (int) book3.getValue(TBook_ID()));
        assertEquals("new title", book3.getValue(TBook_TITLE()));
    }

    @Test
    public void testUpdatablesUK() throws Exception {
        reset = false;

        S store = create().newRecord(TBookStore());
        try {
            store.refresh();
        }
        catch (InvalidResultException expected) {}

        store.setValue(TBookStore_NAME(), "Rösslitor");
        assertEquals(1, store.store());

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Rösslitor"));
        assertEquals("Rösslitor", store.getValue(TBookStore_NAME()));

        // Updating the main unique key should result in a new record
        store.setValue(TBookStore_NAME(), "Amazon");
        assertEquals(1, store.store());

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Amazon"));
        assertEquals("Amazon", store.getValue(TBookStore_NAME()));

        // Delete and re-create the store
        store.delete();
        assertEquals("Amazon", store.getValue(TBookStore_NAME()));
        assertEquals(null, create().fetchOne(TBookStore(), TBookStore_NAME().equal("Amazon")));

        switch (getDialect()) {
            // Sybase ASE and SQL server do not allow for explicitly setting
            // values on IDENTITY columns
            case ASE:
            case SQLSERVER:
                log.info("SKIPPING", "Storing previously deleted UpdatableRecords");
                break;

            default:
                store.store();
                assertEquals("Amazon", store.getValue(TBookStore_NAME()));

                store.refresh();
                assertEquals("Amazon", store.getValue(TBookStore_NAME()));
        }

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Rösslitor"));
        assertEquals("Rösslitor", store.getValue(TBookStore_NAME()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonUpdatables() throws Exception {
        reset = false;

        // Insert three records first
        T785 record = create().newRecord(T785());
        record.setValue(T785_ID(), 1);
        assertEquals(1, record.storeUsing(T785_ID()));
        assertEquals(0, record.storeUsing(T785_ID()));

        record.setValue(T785_ID(), 2);
        assertEquals(1, record.storeUsing(T785_ID()));
        record.setValue(T785_NAME(), "N");
        record.setValue(T785_VALUE(), "V");
        assertEquals(1, record.storeUsing(T785_ID()));

        record = create().newRecord(T785());
        record.setValue(T785_ID(), 3);
        record.setValue(T785_NAME(), "N");
        assertEquals(1, record.storeUsing(T785_ID()));
        assertEquals(0, record.storeUsing(T785_ID()));

        // Load data again
        record = create().newRecord(T785());
        record.setValue(T785_ID(), 2);
        record.refreshUsing(T785_ID());
        assertEquals("N", record.getValue(T785_NAME()));
        assertEquals("V", record.getValue(T785_VALUE()));

        // When NAME is used as the key, multiple updates may occur
        record.setValue(T785_VALUE(), "Some value");
        assertEquals(2, record.storeUsing(T785_NAME()));
        assertEquals(2, create().fetch(T785(), T785_VALUE().equal("Some value")).size());

        // Don't allow refreshing on multiple results
        try {
            record = create().newRecord(T785());
            record.setValue(T785_VALUE(), "Some value");
            record.refreshUsing(T785_VALUE());
            fail();
        }
        catch (InvalidResultException expected) {}


        // Don't allow refreshing on inexistent results
        try {
            record = create().newRecord(T785());
            record.setValue(T785_ID(), 4);
            record.refreshUsing(T785_ID());
            fail();
        }
        catch (InvalidResultException expected) {}

        // Delete records again
        record = create().newRecord(T785());
        record.setValue(T785_ID(), 1);
        assertEquals(1, record.deleteUsing(T785_ID()));
        assertEquals(2, create().fetch(T785()).size());
        assertEquals(0, create().fetch(T785(), T785_ID().equal(1)).size());

        record = create().newRecord(T785());
        record.setValue(T785_NAME(), "N");
        assertEquals(2, record.deleteUsing(T785_NAME()));
        assertEquals(0, create().fetch(T785()).size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInsertReturning() throws Exception {
        if (TTriggers() == null) {
            log.info("SKIPPING", "INSERT RETURNING tests");
            return;
        }

        // Non-DSL querying
        // ----------------

        InsertQuery<T> query;

        int ID = 0;

        // Without RETURNING clause
        query = create().insertQuery(TTriggers());
        query.addValue(TTriggers_ID(), null);
        query.addValue(TTriggers_COUNTER(), 0);
        assertEquals(1, query.execute());
        assertNull(query.getReturnedRecord());

        // Check if the trigger works correctly
        assertEquals(1, create().selectFrom(TTriggers()).fetch().size());
        assertEquals(++ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_ID_GENERATED()));
        assertEquals(  ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_ID()));
        assertEquals(2*ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_COUNTER()));

        // Returning all fields
        query = create().insertQuery(TTriggers());
        query.addValue(TTriggers_COUNTER(), null);
        query.addValue(TTriggers_COUNTER(), 0);
        query.setReturning();
        assertEquals(1, query.execute());
        assertNotNull(query.getReturnedRecord());
        assertEquals(++ID, (int) query.getReturnedRecord().getValue(TTriggers_ID_GENERATED()));
        assertEquals(  ID, (int) query.getReturnedRecord().getValue(TTriggers_ID()));
        assertEquals(2*ID, (int) query.getReturnedRecord().getValue(TTriggers_COUNTER()));

        // Returning only the ID field
        query = create().insertQuery(TTriggers());
        query.addValue(TTriggers_COUNTER(), 0);
        query.setReturning(TTriggers_ID_GENERATED());
        assertEquals(1, query.execute());
        assertNotNull(query.getReturnedRecord());
        assertEquals(++ID, (int) query.getReturnedRecord().getValue(TTriggers_ID_GENERATED()));
        assertNull(query.getReturnedRecord().getValue(TTriggers_ID()));
        assertNull(query.getReturnedRecord().getValue(TTriggers_COUNTER()));

        query.getReturnedRecord().refresh();
        assertEquals(  ID, (int) query.getReturnedRecord().getValue(TTriggers_ID_GENERATED()));
        assertEquals(  ID, (int) query.getReturnedRecord().getValue(TTriggers_ID()));
        assertEquals(2*ID, (int) query.getReturnedRecord().getValue(TTriggers_COUNTER()));

        // DSL querying
        // ------------
        TableRecord<T> returned = create().insertInto(TTriggers(), TTriggers_COUNTER())
                .values(0)
                .returning()
                .fetchOne();
        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(TTriggers_ID_GENERATED()));
        assertEquals(  ID, (int) returned.getValue(TTriggers_ID()));
        assertEquals(2*ID, (int) returned.getValue(TTriggers_COUNTER()));

        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case INGRES:
            case ORACLE:
            // TODO [#832] Fix this. This might be a driver issue for Sybase
            case SQLITE:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "Multiple INSERT RETURNING");
                break;

            default:
                Result<?> many =
                create().insertInto(TTriggers(), TTriggers_COUNTER())
                        .values(-1)
                        .values(-2)
                        .values(-3)
                        .returning()
                        .fetch();
                assertNotNull(many);
                assertEquals(3, many.size());
                assertEquals(++ID, (int) many.getValue(0, TTriggers_ID_GENERATED()));
                assertEquals(  ID, (int) many.getValue(0, TTriggers_ID()));
                assertEquals(2*ID, (int) many.getValue(0, TTriggers_COUNTER()));
                assertEquals(++ID, (int) many.getValue(1, TTriggers_ID_GENERATED()));
                assertEquals(  ID, (int) many.getValue(1, TTriggers_ID()));
                assertEquals(2*ID, (int) many.getValue(1, TTriggers_COUNTER()));
                assertEquals(++ID, (int) many.getValue(2, TTriggers_ID_GENERATED()));
                assertEquals(  ID, (int) many.getValue(2, TTriggers_ID()));
                assertEquals(2*ID, (int) many.getValue(2, TTriggers_COUNTER()));
                break;
        }


        returned = create().insertInto(TTriggers(), TTriggers_COUNTER())
                .values(0)
                .returning(TTriggers_ID())
                .fetchOne();
        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(TTriggers_ID()));
        assertNull(returned.getValue(TTriggers_ID_GENERATED()));
        assertNull(returned.getValue(TTriggers_COUNTER()));

        returned.refreshUsing(TTriggers_ID());
        assertEquals(  ID, (int) returned.getValue(TTriggers_ID_GENERATED()));
        assertEquals(2*ID, (int) returned.getValue(TTriggers_COUNTER()));

        // store() and similar methods
        T triggered = create().newRecord(TTriggers());
        triggered.setValue(TTriggers_COUNTER(), 0);
        assertEquals(1, triggered.store());
        assertEquals(++ID, (int) triggered.getValue(TTriggers_ID_GENERATED()));
        assertEquals(null, triggered.getValue(TTriggers_ID()));
        assertEquals(0, (int) triggered.getValue(TTriggers_COUNTER()));
        triggered.refresh();
        assertEquals(  ID, (int) triggered.getValue(TTriggers_ID()));
        assertEquals(2*ID, (int) triggered.getValue(TTriggers_COUNTER()));
    }

    @Test
    public void testFormatHTML() throws Exception {
        List<Field<?>> fields = TBook().getFields();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String html = books.formatHTML();
        InputStream is = new ByteArrayInputStream(html.getBytes());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);

        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xp = xpfactory.newXPath();

        assertEquals("1", xp.evaluate("count(/table)", doc));
        assertEquals("1", xp.evaluate("count(/table/thead)", doc));
        assertEquals("1", xp.evaluate("count(/table/thead/tr)", doc));
        assertEquals("0", xp.evaluate("count(/table/thead/tr/td)", doc));
        assertEquals("" + fields.size(),
                          xp.evaluate("count(/table/thead/tr/th)", doc));

        for (int i = 0; i < fields.size(); i++) {
            assertEquals(fields.get(i).getName(),
                          xp.evaluate("/table/thead/tr/th[" + (i + 1) + "]/text()", doc));
        }

        assertEquals("1", xp.evaluate("count(/table/tbody)", doc));
        assertEquals("4", xp.evaluate("count(/table/tbody/tr)", doc));
        assertEquals("" + 4 * fields.size(),
                          xp.evaluate("count(/table/tbody/tr/td)", doc));

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < fields.size(); i++) {
                assertEquals(books.getValueAsString(j, i, "{null}"),
                          xp.evaluate("/table/tbody/tr[" + (j + 1) + "]/td[" + (i + 1) + "]/text()", doc));
            }
        }
    }

    @Test
    public void testFormatCSV() throws Exception {
        List<Field<?>> fields = TBook().getFields();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String csv = books.formatCSV();

        String[] lines = csv.split("\n");
        String[] fieldNames = lines[0].split(",");

        assertEquals(5, lines.length);
        assertEquals(fields.size(), fieldNames.length);

        for (int i = 0; i < fields.size(); i++) {
            assertEquals(fields.get(i).getName(), fieldNames[i]);
        }

        for (int j = 1; j < lines.length; j++) {
            for (int i = 0; i < fields.size(); i++) {
                String value = books.getValueAsString(j - 1, i);

                if (value == null || "".equals(value)) {
                    value = "\"\"";
                }

                String regex1 = "";
                String regex2 = "";

                for (int x = 0; x < fields.size(); x++) {
                    if (x > 0) {
                        regex1 += ",";
                        regex2 += ",";
                    }

                    if (x == i) {
                        regex1 += value;
                        regex2 += "\"" + value.replaceAll("\"", "\"\"") + "\"";
                    }
                    else {
                        regex1 += "((?!\")[^,]+|\"[^\"]*\")";
                        regex2 += "((?!\")[^,]+|\"[^\"]*\")";
                    }
                }

                assertTrue(lines[j].matches(regex1) || lines[j].matches(regex2));
            }
        }
    }

    @Test
    public void testFormatJSON() throws Exception {
        List<Field<?>> fields = TBook().getFields();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String json = books.formatJSON();

        // Fields header
        String token1 = "{\"fields\":[";
        assertTrue(json.startsWith(token1));
        json = json.replace(token1, "");

        // Field names
        String token2 = "";
        String separator = "";
        for (Field<?> field : fields) {
            token2 += separator + "\"" + field.getName() + "\"";
            separator = ",";
        }
        assertTrue(json.startsWith(token2));
        json = json.replace(token2, "");

        // Records header
        String token3 = "],\"records\":[";
        assertTrue(json.startsWith(token3));
        json = json.replace(token3, "");

        // Record values
        int i = 0;
        for (Record record : books) {
            i++;
            String token4 = "[";

            if (i > 1) {
                token4 = ",[";
            }

            separator = "";
            for (Field<?> field : fields) {
                Object value = record.getValue(field);

                if (value == null) {
                    token4 += separator + null;
                }
                else if (value instanceof Number) {
                    token4 += separator + value;
                }
                else {
                    token4 += separator + "\"" + value.toString().replaceAll("\"", "\"\"") + "\"";
                }

                separator = ",";
            }
            token4 += "]";
            assertTrue(json.startsWith(token4));
            json = json.replace(token4, "");
        }

        assertEquals("]}", json);
    }

    @Test
    public void testFormatXML() throws Exception {
        Result<B> books = create().selectFrom(TBook()).fetch();
        String xml = books.formatXML();
        InputStream is = new ByteArrayInputStream(xml.getBytes());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);

        testXML(doc, books);
    }

    @Test
    public void testExportXML() throws Exception {
        Result<B> books = create().selectFrom(TBook()).fetch();
        testXML(books.exportXML(), books);
    }

    private void testXML(Document doc, Result<B> books) throws XPathExpressionException {
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xp = xpfactory.newXPath();

        List<Field<?>> fields = TBook().getFields();
        assertEquals("1", xp.evaluate("count(/result)", doc));
        assertEquals("1", xp.evaluate("count(/result/fields)", doc));
        assertEquals("" + fields.size(),
                          xp.evaluate("count(/result/fields/field)", doc));

        for (int i = 0; i < fields.size(); i++) {
            assertEquals(fields.get(i).getName(),
                          xp.evaluate("/result/fields/field[" + (i + 1) + "]/@name", doc));
        }

        assertEquals("1", xp.evaluate("count(/result/records)", doc));
        assertEquals("4", xp.evaluate("count(/result/records/record)", doc));
        assertEquals("" + 4 * fields.size(),
                          xp.evaluate("count(/result/records/record/value)", doc));

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < fields.size(); i++) {
                assertEquals(fields.get(i).getName(),
                          xp.evaluate("/result/records/record[" + (j + 1) + "]/value[" + (i + 1) + "]/@field", doc));
                assertEquals(books.getValueAsString(j, i, ""),
                          xp.evaluate("/result/records/record[" + (j + 1) + "]/value[" + (i + 1) + "]/text()", doc));
            }
        }
    }

    @Test
    public void testCombinedSelectQuery() throws Exception {
        SelectQuery q1 = create().selectQuery();
        SelectQuery q2 = create().selectQuery();

        q1.addFrom(TBook());
        q2.addFrom(TBook());

        q1.addConditions(TBook_AUTHOR_ID().equal(1));
        q2.addConditions(TBook_TITLE().equal("Brida"));

        // Use union all because of clob's
        Select<?> union = q1.unionAll(q2);
        int rows = union.execute();
        assertEquals(3, rows);

        // Use union all because of clob's
        rows = create().selectDistinct(union.getField(TBook_AUTHOR_ID()), TAuthor_FIRST_NAME())
            .from(union)
            .join(TAuthor())
            .on(union.getField(TBook_AUTHOR_ID()).equal(TAuthor_ID()))
            .orderBy(TAuthor_FIRST_NAME())
            .execute();

        assertEquals(2, rows);
    }

    @Test
    public void testComplexUnions() throws Exception {
        Select<Record> s1 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(1));
        Select<Record> s2 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(2));
        Select<Record> s3 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(3));
        Select<Record> s4 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(4));

        Result<Record> result = create().select().from(s1.union(s2).union(s3).union(s4)).fetch();
        assertEquals(4, result.size());

        result = create().select().from(s1.union(s2).union(s3.union(s4))).fetch();
        assertEquals(4, result.size());

        assertEquals(4, create().select().from(s1.union(
                            create().select().from(s2.unionAll(
                                create().select().from(s3.union(s4))))))
                                    .fetch().size());

        // [#289] Handle bad syntax scenario provided by user Gunther
        Select<Record> q = create().select(val(2008).as("y"));
        for (int year = 2009; year <= 2011; year++) {
            q = q.union(create().select(val(year).as("y")));
        }

        assertEquals(4, q.execute());
    }

    @Test
    public void testOrderByInSubquery() throws Exception {
        // TODO: [#780] Fix this for Ingres and Sybase ASE
        switch (getDialect()) {
            case ASE:
            case INGRES:
                log.info("SKIPPING", "Ordered subqueries");
                return;
        }

        // Some RDBMS don't accept ORDER BY clauses in subqueries without
        // TOP clause (e.g. SQL Server). jOOQ will synthetically add a
        // TOP 100 PERCENT clause, if necessary

        Select<?> nested =
        create().select(TBook_ID())
                .from(TBook())
                .orderBy(TBook_ID().asc());

        List<Integer> result =
        create().select(nested.getField(TBook_ID()))
                .from(nested)
                .orderBy(nested.getField(TBook_ID()).desc())
                .fetch(nested.getField(TBook_ID()));

        assertEquals(Arrays.asList(4, 3, 2, 1), result);
    }

    @Test
    public void testOrderByNulls() throws Exception {
        reset = false;

        // Make data a bit more meaningful, first
        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Döblin")
                .execute();

        Result<A> authors =
        create().selectFrom(TAuthor())
                .orderBy(
                    TAuthor_FIRST_NAME().asc().nullsFirst())
                .fetch();

        assertNull(authors.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals("George", authors.getValue(1, TAuthor_FIRST_NAME()));
        assertEquals("Paulo", authors.getValue(2, TAuthor_FIRST_NAME()));

        authors =
        create().selectFrom(TAuthor())
                .orderBy(
                    TAuthor_FIRST_NAME().asc().nullsLast())
                .fetch();

        assertEquals("George", authors.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals("Paulo", authors.getValue(1, TAuthor_FIRST_NAME()));
        assertNull(authors.getValue(2, TAuthor_FIRST_NAME()));
    }

    @Test
    public void testOrderByIndexes() throws Exception {
        assertEquals(Arrays.asList(1, 2, 3, 4),
            create().selectFrom(TBook())
                    .orderBy(1)
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2, 3, 4),
            create().select(TBook_ID(), TBook_TITLE())
                    .from(TBook())
                    .orderBy(1)
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2, 3, 4),
            create().select(TBook_TITLE(), TBook_ID())
                    .from(TBook())
                    .orderBy(2)
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 1, 2, 2),
            create().select(TBook_AUTHOR_ID(), TBook_ID())
                    .from(TBook())
                    .orderBy(2, 1)
                    .fetch(TBook_AUTHOR_ID()));
    }

    @Test
    public void testOrderByIndirection() throws Exception {
        assertEquals(BOOK_IDS,
            create().selectFrom(TBook())
            .orderBy(TBook_ID().sortAsc(), TBook_ID().asc())
            .fetch(TBook_ID()));

        assertEquals(Arrays.asList(3, 2, 4, 1),
            create().selectFrom(TBook())
                    .orderBy(TBook_ID().sortAsc(3, 2, 4, 1))
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 4, 2, 3),
            create().selectFrom(TBook())
                    .orderBy(TBook_ID().sortDesc(3, 2, 4, 1))
                    .fetch(TBook_ID()));

//        assertEquals(Arrays.asList(3, 2, 1, 4),
//            create().selectFrom(TBook())
//                    .orderBy(TBook_ID().sortAsc(3, 2).nullsLast(), TBook_ID().asc())
//                    .fetch(TBook_ID()));
//
//        assertEquals(Arrays.asList(1, 4, 3, 2),
//            create().selectFrom(TBook())
//                    .orderBy(TBook_ID().sortAsc(3, 2).nullsFirst(), TBook_ID().asc())
//                    .fetch(TBook_ID()));

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 3);
        map.put(2, 4);
        map.put(3, 1);
        map.put(4, 2);
        assertEquals(Arrays.asList(3, 4, 1, 2),
            create().selectFrom(TBook())
                    .orderBy(TBook_ID().sort(map))
                    .fetch(TBook_ID()));
    }

    @Test
    public void testJoinQuery() throws Exception {
        SimpleSelectQuery<L> q1 = create().selectQuery(VLibrary());

        // TODO: Fix this when funny issue is fixed in Derby:
        // https://sourceforge.net/apps/trac/jooq/ticket/238
        q1.addOrderBy(VLibrary_TITLE());

        // Oracle ordering behaviour is a bit different, so exclude "1984"
        q1.addConditions(VLibrary_TITLE().notEqual("1984"));

        Table<A> a = TAuthor().as("a");
        Table<B> b = TBook().as("b");

        Field<Integer> a_authorID = a.getField(TAuthor_ID());
        Field<Integer> b_authorID = b.getField(TBook_AUTHOR_ID());
        Field<String> b_title = b.getField(TBook_TITLE());

        SelectQuery q2 = create().selectQuery();
        q2.addFrom(a);
        q2.addJoin(b, b_authorID.equal(a_authorID));
        q2.addConditions(b_title.notEqual("1984"));
        q2.addOrderBy(lower(b_title));

        int rows1 = q1.execute();
        int rows2 = q2.execute();

        assertEquals(3, rows1);
        assertEquals(3, rows2);

        Result<L> result1 = q1.getResult();
        Result<?> result2 = q2.getResult();

        assertEquals("Animal Farm", result1.get(0).getValue(VLibrary_TITLE()));
        assertEquals("Animal Farm", result2.get(0).getValue(b_title));

        assertEquals("Brida", result1.get(1).getValue(VLibrary_TITLE()));
        assertEquals("Brida", result2.get(1).getValue(b_title));

        assertEquals("O Alquimista", result1.get(2).getValue(VLibrary_TITLE()));
        assertEquals("O Alquimista", result2.get(2).getValue(b_title));

        // DB2 does not allow subselects in join conditions:
        // http://publib.boulder.ibm.com/infocenter/dzichelp/v2r2/index.jsp?topic=/com.ibm.db29.doc.sqlref/db2z_sql_joincondition.htm

        // This query causes a failure in Ingres. Potentially a bug. See E_OP039F_BOOLFACT on
        // http://docs.ingres.com/ingres/9.2/ingres-92-message-guide/1283-errors-from-opf#E_OP039F_BOOLFACT
        if (getDialect() != SQLDialect.DB2 && getDialect() != SQLDialect.INGRES) {

            // Advanced JOIN usages with single JOIN condition
            Result<Record> result = create().select()
                .from(TAuthor())
                .join(TBook())
                .on(TAuthor_ID().equal(TBook_AUTHOR_ID())
                .and(TBook_LANGUAGE_ID().in(create().select(field("id"))
                                                    .from("t_language")
                                                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(create().selectOne().from(TAuthor()).where(falseCondition())))
                .orderBy(TBook_ID()).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

            // Advanced JOIN usages with several JOIN condition
            // ------------------------------------------------
            Select<A> author = create().selectFrom(TAuthor());
            result = create().select()
                .from(author)
                .join(TBook())
                .on(author.getField(TAuthor_ID()).equal(TBook_AUTHOR_ID()))
                .and(TBook_LANGUAGE_ID().in(create().select(field("id"))
                                                    .from("t_language")
                                                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(create().selectOne().where(falseCondition()))
                .orderBy(TBook_ID()).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

            Select<B> book = create().selectFrom(TBook());
            result = create().select()
                .from(TAuthor())
                .join(book)
                .on(TAuthor_ID().equal(book.getField(TBook_AUTHOR_ID())))
                .and(book.getField(TBook_LANGUAGE_ID()).in(create().select(field("id"))
                                                    .from("t_language")
                                                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(create().selectOne().where(falseCondition()))
                .orderBy(book.getField(TBook_ID())).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

        }
    }

    @Test
    public void testCrossJoin() throws Exception {
        Result<Record> result;

        // Using the CROSS JOIN clause
        assertEquals(Integer.valueOf(8),
        create().select(count())
                .from(TAuthor())
                .crossJoin(TBook())
                .fetchOne(0));

        result =
        create().select()
                .from(create().select(val(1).cast(Integer.class).as("a")))
                .crossJoin(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result.getValue(1, 1));


        // [#772] Using the FROM clause for regular cartesian products
        assertEquals(Integer.valueOf(8),
        create().select(count())
                .from(TAuthor(), TBook())
                .fetchOne(0));

        result =
        create().select()
                .from(create().select(val(1).cast(Integer.class).as("a")), TAuthor())
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result.getValue(1, 1));
    }

    @Test
    public void testNaturalJoin() throws Exception {
        // TODO [#577] Simulate this

        switch (getDialect()) {
            case ASE:
            case DB2:
            case INGRES:
            case SQLSERVER:
                log.info("SKIPPING", "NATURAL JOIN tests");
                return;
        }

        Result<Record> result =
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TBook())
                .naturalJoin(TAuthor())
                .orderBy(getDialect() == SQLDialect.ORACLE
                        ? field("id")
                        : TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        switch (getDialect()) {
            case H2:
                log.info("SKIPPING", "NATURAL OUTER JOIN tests");
                break;

            default: {

                // TODO [#574] allow for selecting all columns, including
                // the ones making up the join condition!
                result =
                // create().select()
                create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                        .from(TBook())
                        .naturalLeftOuterJoin(TAuthor())
                        .orderBy(getDialect() == SQLDialect.ORACLE
                            ? field("id")
                            : TBook_ID())
                        .fetch();

                assertEquals(4, result.size());
                assertEquals("1984", result.getValue(0, TBook_TITLE()));
                assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
                assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
                assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

                assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
                assertNull(result.getValue(2, TAuthor_LAST_NAME()));
                assertEquals("Brida", result.getValue(3, TBook_TITLE()));
                assertNull(result.getValue(3, TAuthor_LAST_NAME()));
            }
            break;
        }
    }

    @Test
    public void testJoinUsing() throws Exception {
        // TODO [#582] Simulate this

        switch (getDialect()) {
            case ASE:
            case DB2:
            case H2:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "JOIN USING tests");
                return;
        }

        Result<Record> result =
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TAuthor())
                .join(TBook())
                .using(TAuthor_ID())
                .orderBy(getDialect() == SQLDialect.ORACLE
                        ? field("id")
                        : TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        // TODO [#574] allow for selecting all columns, including
        // the ones making up the join condition!
        result =
        // create().select()
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TBook())
                .leftOuterJoin(TAuthor())
                .using(TAuthor_ID())
                .orderBy(getDialect() == SQLDialect.ORACLE
                    ? field("id")
                    : TBook_ID())
                .fetch();

        assertEquals(4, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertNull(result.getValue(2, TAuthor_LAST_NAME()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));
        assertNull(result.getValue(3, TAuthor_LAST_NAME()));
    }

    @Test
    public void testOuterJoin() throws Exception {
        // Test LEFT OUTER JOIN
        // --------------------
        Result<Record> result1 =
        create().select(
                    TAuthor_ID(),
                    TBook_ID(),
                    TBookToBookStore_BOOK_STORE_NAME())
                .from(TAuthor())
                .leftOuterJoin(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                .leftOuterJoin(TBookToBookStore()).on(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
                .orderBy(
                    TAuthor_ID().asc(),
                    TBook_ID().asc(),
                    TBookToBookStore_BOOK_STORE_NAME().asc().nullsLast())
                .fetch();

        assertEquals(
            asList(1, 1, 1, 2, 2, 2, 2),
            result1.getValues(0, Integer.class));
        assertEquals(
            asList(1, 1, 2, 3, 3, 3, 4),
            result1.getValues(1, Integer.class));
        assertEquals(
            asList("Ex Libris", "Orell Füssli", "Orell Füssli", "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli", null),
            result1.getValues(2));

        // Test RIGHT OUTER JOIN
        // ---------------------

        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "RIGHT OUTER JOIN tests");
                break;

            default: {
                Result<Record> result2 =
                    create().select(
                                TAuthor_ID(),
                                TBook_ID(),
                                TBookToBookStore_BOOK_STORE_NAME())
                            .from(TBookToBookStore())
                            .rightOuterJoin(TBook()).on(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
                            .rightOuterJoin(TAuthor()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                            .orderBy(
                                TAuthor_ID().asc(),
                                TBook_ID().asc(),
                                TBookToBookStore_BOOK_STORE_NAME().asc().nullsLast())
                            .fetch();

                assertEquals(result1, result2);
                assertEquals(
                    asList(1, 1, 1, 2, 2, 2, 2),
                    result2.getValues(0, Integer.class));
                assertEquals(
                    asList(1, 1, 2, 3, 3, 3, 4),
                    result2.getValues(1, Integer.class));
                assertEquals(
                    asList("Ex Libris", "Orell Füssli", "Orell Füssli", "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli", null),
                    result2.getValues(2));

                break;
            }
        }

        // Test FULL OUTER JOIN
        // --------------------

        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "FULL OUTER JOIN tests");
                break;

            default: {
                Select<?> z = create().select(zero().as("z"));
                Select<?> o = create().select(one().as("o"));

                Result<Record> result3 =
                create().select()
                        .from(z)
                        .fullOuterJoin(o).on(z.getField("z").cast(Integer.class).equal(o.getField("o").cast(Integer.class)))
                        .fetch();

                assertEquals("z", result3.getField(0).getName());
                assertEquals("o", result3.getField(1).getName());

                // Interestingly, ordering doesn't work with Oracle, in this
                // example... Seems to be an Oracle bug??
                @SuppressWarnings("unchecked")
                List<List<Integer>> list = asList(asList(0, null), asList(null, 1));
                assertTrue(list.contains(asList(result3.get(0).into(Integer[].class))));
                assertTrue(list.contains(asList(result3.get(1).into(Integer[].class))));
                break;
            }
        }
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
        // Test whether unaliased literals in subquery projections are correctly
        // handled
        Result<Record> result =
        create().select()
                .from(
                    create().selectOne(),
                    create().select(val(2)),
                    create().select(val(2)),
                    create().select(val(2)),
                    create().select(val(3).add(4)),
                    create().select(val(3).add(4)),
                    create().select(trim(" test ")),
                    create().select(trim(" test ")))
                .fetch();

        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(2), result.getValue(0, 1));
        assertEquals(Integer.valueOf(2), result.getValue(0, val(2)));
        assertEquals(Integer.valueOf(2), result.getValue(0, 3));
        assertEquals(Integer.valueOf(7), result.getValue(0, val(3).add(4)));
        assertEquals(Integer.valueOf(7), result.getValue(0, 5));
        assertEquals("test", result.getValue(0, trim(" test ")));
        assertEquals("test", result.getValue(0, 7));

        result =
        create().select(
                    create().selectOne().asField(),
                    create().select(val(2)).asField(),
                    create().select(val(2)).asField(),
                    create().select(val(2)).asField(),
                    create().select(val(3).add(4)).asField(),
                    create().select(val(3).add(4)).asField(),
                    create().select(trim(" test ")).asField(),
                    create().select(trim(" test ")).asField())
                .fetch();

        assertEquals(1, result.size());
        assertEquals(1, result.getValue(0, 0));
        assertEquals(2, result.getValue(0, 1));
        assertEquals(2, result.getValue(0, 2));
        assertEquals(2, result.getValue(0, 3));
        assertEquals(7, result.getValue(0, 4));
        assertEquals(7, result.getValue(0, 5));
        assertEquals("test", result.getValue(0, 6));
        assertEquals("test", result.getValue(0, 7));

    }

    @Test
    public void testArithmeticOperations() throws Exception {
        Field<Integer> f1 = val(1).add(2).add(3).div(2);
        Field<Integer> f2 = val(10).div(5).add(val(3).sub(2));
        Field<Integer> f3 = val(10).mod(3);

        SelectQuery q1 = create().selectQuery();
        q1.addSelect(f1, f2, f3);
        q1.execute();

        Result<?> result = q1.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(3), result.getValue(0, f1));
        assertEquals(Integer.valueOf(3), result.getValue(0, f2));
        assertEquals(Integer.valueOf(1), result.getValue(0, f3));

        Field<Integer> f4 = TBook_PUBLISHED_IN().add(3).div(7);
        Field<Integer> f5 = TBook_PUBLISHED_IN().sub(4).mul(8).neg();

        SelectQuery q2 = create().selectQuery();
        q2.addSelect(f4);
        q2.addSelect(f5);
        q2.addFrom(TBook());
        q2.addConditions(TBook_TITLE().equal("1984"));
        q2.execute();

        result = q2.getResult();
        assertEquals(Integer.valueOf((1948 + 3) / 7), result.getValue(0, f4));
        assertEquals(Integer.valueOf((1948 - 4) * -8), result.getValue(0, f5));
    }

    @Test
    public void testBitwiseOperations() throws Exception {
        switch (getDialect()) {
            case DERBY:
            case INGRES:
                log.info("SKIPPING", "Tests for bitwise operations");
                return;
        }

        Field<Integer> bitCount = bitCount(3);

        // TODO [#896] This somehow doesn't work on some dialects
        if (asList(ASE, DB2, SQLSERVER).contains(getDialect())) {
            bitCount = val(2);
        }

        Record result =
        create().select(
                    bitCount,
                    bitNot(bitNot(3)),
                    bitAnd(3, 5),
                    bitOr(3, 5),

                    bitXor(3, 5),
                    bitNot(bitNand(3, 5)),
                    bitNot(bitNor(3, 5)),
                    bitNot(bitXNor(3, 5)),

                    shl(333, 3),
                    shr(333, 3))
                .fetchOne();

        int index = 0;
        assertEquals(2, result.getValue(index++));
        assertEquals(~(~3), result.getValue(index++));
        assertEquals(3 & 5, result.getValue(index++));
        assertEquals(3 | 5, result.getValue(index++));

        assertEquals(3 ^ 5, result.getValue(index++));
        assertEquals(~(~(3 & 5)), result.getValue(index++));
        assertEquals(~(~(3 | 5)), result.getValue(index++));
        assertEquals(~(~(3 ^ 5)), result.getValue(index++));

        assertEquals(333 << 3, result.getValue(index++));
        assertEquals(333 >> 3, result.getValue(index++));
    }

    @Test
    public void testAggregateFunctions() throws Exception {

        // Standard aggregate functions, available in all dialects:
        // --------------------------------------------------------
        Field<BigDecimal> median = median(TBook_ID());

        // Some dialects don't support a median function or a simulation thereof
        // Use AVG instead, as in this example the values of MEDIAN and AVG
        // are the same
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case INGRES:
            case MYSQL:
            case SQLITE:

            // TODO [#871] This could be simulated
            case SQLSERVER:
            case POSTGRES:
            case DB2:
                median = avg(TBook_ID());
                break;
        }

        Result<Record> result = create()
            .select(
                TBook_AUTHOR_ID(),
                count(),
                count(TBook_ID()),
                countDistinct(TBook_AUTHOR_ID()),
                sum(TBook_ID()),
                avg(TBook_ID()),
                min(TBook_ID()),
                max(TBook_ID()),
                median)
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID())
            .orderBy(TBook_AUTHOR_ID())
            .fetch();

        assertEquals(2, (int) result.getValueAsInteger(0, 1));
        assertEquals(2, (int) result.getValueAsInteger(0, 2));
        assertEquals(1, (int) result.getValueAsInteger(0, 3));
        assertEquals(3d, result.getValueAsDouble(0, 4));
        assertEquals(1, (int) result.getValueAsInteger(0, 6));
        assertEquals(2, (int) result.getValueAsInteger(0, 7));

        assertEquals(2, (int) result.getValueAsInteger(1, 1));
        assertEquals(2, (int) result.getValueAsInteger(1, 2));
        assertEquals(1, (int) result.getValueAsInteger(1, 3));
        assertEquals(7d, result.getValueAsDouble(1, 4));
        assertEquals(3, (int) result.getValueAsInteger(1, 6));
        assertEquals(4, (int) result.getValueAsInteger(1, 7));

        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(result.getValueAsDouble(0, 5)));
        assertTrue(asList(1.0, 1.5, 2.0).contains(result.getValueAsDouble(0, 8)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result.getValueAsDouble(1, 5)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result.getValueAsDouble(1, 8)));

        // [#1042] DISTINCT keyword
        // ------------------------

        // DB2 doesn't support multiple DISTINCT keywords in the same query...
        int distinct1 = create().select(countDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct2 = create().select(minDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct3 = create().select(maxDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct4 = create().select(sumDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        double distinct5 = create().select(avgDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Double.class);

        assertEquals(2, distinct1);
        assertEquals(1, distinct2);
        assertEquals(2, distinct3);
        assertEquals(3, distinct4);
        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(distinct5));

        // Statistical aggregate functions, available in some dialects:
        // ------------------------------------------------------------
        switch (getDialect()) {
            case DERBY:
            case SQLITE:
                log.info("SKIPPING", "Statistical aggregate functions");
                break;

            default: {
                result = create()
                    .select(
                        TBook_AUTHOR_ID(),
                        stddevPop(TBook_ID()),
                        stddevSamp(TBook_ID()),
                        varPop(TBook_ID()),
                        varSamp(TBook_ID()))
                    .from(TBook())
                    .groupBy(TBook_AUTHOR_ID())
                    .orderBy(TBook_AUTHOR_ID())
                    .fetch();

                assertEquals(0.5, result.getValueAsDouble(0, 1));
                assertEquals(0.25, result.getValueAsDouble(0, 3));
                assertEquals(0.5, result.getValueAsDouble(1, 1));
                assertEquals(0.25, result.getValueAsDouble(1, 3));

                // DB2 only knows STDDEV_POP / VAR_POP
                if (getDialect() != SQLDialect.DB2) {
                    assertEquals("0.707", result.getValueAsString(0, 2).substring(0, 5));
                    assertEquals(0.5, result.getValueAsDouble(0, 4));
                    assertEquals("0.707", result.getValueAsString(1, 2).substring(0, 5));
                    assertEquals(0.5, result.getValueAsDouble(1, 4));
                }
            }
        }

        // [#873] Duplicate functions
        // --------------------------
        result =
        create().select(
                    TBook_AUTHOR_ID(),
                    max(TBook_ID()),
                    max(TBook_ID()))
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetch();

        assertEquals(2, (int) result.getValueAsInteger(0, 1));
        assertEquals(2, (int) result.getValueAsInteger(0, 2));
        assertEquals(4, (int) result.getValueAsInteger(1, 1));
        assertEquals(4, (int) result.getValueAsInteger(1, 2));
    }

    @Test
    public void testStoredFunctions() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "functions test");
            return;
        }

        reset = false;

        // ---------------------------------------------------------------------
        // Standalone calls
        // ---------------------------------------------------------------------
        assertEquals("0", "" + invoke(cRoutines(), "fAuthorExists", create(), null));
        assertEquals("1", "" + invoke(cRoutines(), "fAuthorExists", create(), "Paulo"));
        assertEquals("0", "" + invoke(cRoutines(), "fAuthorExists", create(), "Shakespeare"));
        assertEquals("1", "" + invoke(cRoutines(), "fOne", create()));
        assertEquals("1", "" + invoke(cRoutines(), "fNumber", create(), 1));
        assertEquals(null, invoke(cRoutines(), "fNumber", create(), null));
        assertEquals("1204", "" + invoke(cRoutines(), "f317", create(), 1, 2, 3, 4));
        assertEquals("1204", "" + invoke(cRoutines(), "f317", create(), 1, 2, null, 4));
        assertEquals("4301", "" + invoke(cRoutines(), "f317", create(), 4, 3, 2, 1));
        assertEquals("4301", "" + invoke(cRoutines(), "f317", create(), 4, 3, null, 1));
        assertEquals("1101", "" + invoke(cRoutines(), "f317", create(), 1, 1, 1, 1));
        assertEquals("1101", "" + invoke(cRoutines(), "f317", create(), 1, 1, null, 1));

        // ---------------------------------------------------------------------
        // Embedded calls
        // ---------------------------------------------------------------------
        Field<Integer> f1a = FAuthorExistsField("Paulo").cast(Integer.class);
        Field<Integer> f2a = FAuthorExistsField("Shakespeare").cast(Integer.class);
        Field<Integer> f3a = FOneField().cast(Integer.class);
        Field<Integer> f4a = FNumberField(42).cast(Integer.class);
        Field<Integer> f5a = FNumberField(FNumberField(FOneField())).cast(Integer.class);
        Field<Integer> f6a = F317Field(1, 2, null, 4).cast(Integer.class);
        Field<Integer> f7a = F317Field(4, 3, null, 1).cast(Integer.class);
        Field<Integer> f8a = F317Field(1, 1, null, 1).cast(Integer.class);
        Field<Integer> f9a = F317Field(FNumberField(1), FNumberField(2), FNumberField((Number) null), FNumberField(4)).cast(Integer.class);

        // Repeat fields to check correct fetching from resultset
        Field<Integer> f1b = FAuthorExistsField("Paulo").cast(Integer.class);
        Field<Integer> f2b = FAuthorExistsField("Shakespeare").cast(Integer.class);
        Field<Integer> f3b = FOneField().cast(Integer.class);
        Field<Integer> f4b = FNumberField(42).cast(Integer.class);
        Field<Integer> f5b = FNumberField(FNumberField(FOneField())).cast(Integer.class);
        Field<Integer> f6b = F317Field(1, 2, 3, 4).cast(Integer.class);
        Field<Integer> f7b = F317Field(4, 3, 2, 1).cast(Integer.class);
        Field<Integer> f8b = F317Field(1, 1, 1, 1).cast(Integer.class);
        Field<Integer> f9b = F317Field(FNumberField(1), FNumberField(2), FNumberField(3), FNumberField(4)).cast(Integer.class);

        // Null argument checks
        Field<Integer> f10 = FAuthorExistsField(null).cast(Integer.class);

        SelectQuery q = create().selectQuery();
        q.addSelect(
            f1a, f2a, f3a, f4a, f5a, f6a, f7a, f8a, f9a,
            f1b, f2b, f3b, f4b, f5b, f6b, f7b, f8b, f9b, f10);
        q.execute();
        Result<Record> result = q.getResult();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getValueAsString(f1a));
        assertEquals("0", result.get(0).getValueAsString(f2a));
        assertEquals("1", result.get(0).getValueAsString(f3a));
        assertEquals("42", result.get(0).getValueAsString(f4a));
        assertEquals("1", result.get(0).getValueAsString(f5a));
        assertEquals("1204", result.get(0).getValueAsString(f6a));
        assertEquals("4301", result.get(0).getValueAsString(f7a));
        assertEquals("1101", result.get(0).getValueAsString(f8a));
        assertEquals("1204", result.get(0).getValueAsString(f9a));

        assertEquals("1", result.get(0).getValueAsString(f1b));
        assertEquals("0", result.get(0).getValueAsString(f2b));
        assertEquals("1", result.get(0).getValueAsString(f3b));
        assertEquals("42", result.get(0).getValueAsString(f4b));
        assertEquals("1", result.get(0).getValueAsString(f5b));
        assertEquals("1204", result.get(0).getValueAsString(f6b));
        assertEquals("4301", result.get(0).getValueAsString(f7b));
        assertEquals("1101", result.get(0).getValueAsString(f8b));
        assertEquals("1204", result.get(0).getValueAsString(f9b));

        assertEquals("0", result.get(0).getValueAsString(f10));

        // ---------------------------------------------------------------------
        // Functions in conditions
        // ---------------------------------------------------------------------
        assertEquals(Integer.valueOf(1),
            create().selectOne().where(f4b.equal(1)).or(f1b.equal(1)).fetchOne(0));
        assertEquals(null,
            create().selectOne().where(f4b.equal(1)).and(f1b.equal(1)).fetchOne(0));
        assertEquals(null,
            create().selectOne().where(f4b.equal(1)).and(f1b.equal(1)).fetchOne());

        // ---------------------------------------------------------------------
        // Functions in SQL
        // ---------------------------------------------------------------------
        result = create().select(
                FNumberField(1).cast(Integer.class),
                FNumberField(TAuthor_ID()).cast(Integer.class),
                FNumberField(FNumberField(TAuthor_ID())).cast(Integer.class))
            .from(TAuthor())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result.getValue(0, 2));
        assertEquals(Integer.valueOf(1), result.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result.getValue(1, 1));
        assertEquals(Integer.valueOf(2), result.getValue(1, 2));
    }

    @Test
    public void testFunctionsOnDates() throws Exception {

        // Some checks on current_timestamp functions
        // ------------------------------------------
        SelectQuery q1 = create().selectQuery();
        Field<Timestamp> now = currentTimestamp();
        Field<Timestamp> ts = now.as("ts");
        Field<Date> date = currentDate().as("d");
        Field<Time> time = currentTime().as("t");

        // ... and the extract function
        // ----------------------------
        Field<Integer> year = extract(now, DatePart.YEAR).as("y");
        Field<Integer> month = extract(now, DatePart.MONTH).as("m");
        Field<Integer> day = extract(now, DatePart.DAY).as("dd");
        Field<Integer> hour = extract(now, DatePart.HOUR).as("h");
        Field<Integer> minute = extract(now, DatePart.MINUTE).as("mn");
        Field<Integer> second = extract(now, DatePart.SECOND).as("sec");

        q1.addSelect(ts, date, time, year, month, day, hour, minute, second);
        q1.execute();

        Record record = q1.getResult().get(0);
        String timestamp = record.getValue(ts).toString().replaceFirst("\\.\\d+$", "");

        assertEquals(timestamp.split(" ")[0], record.getValue(date).toString());

        // Weird behaviour in postgres
        // See also interesting thread:
        // http://archives.postgresql.org/pgsql-jdbc/2010-09/msg00037.php
        if (getDialect() != SQLDialect.POSTGRES) {
            assertEquals(timestamp.split(" ")[1], record.getValue(time).toString());
        }

        assertEquals(Integer.valueOf(timestamp.split(" ")[0].split("-")[0]), record.getValue(year));
        assertEquals(Integer.valueOf(timestamp.split(" ")[0].split("-")[1]), record.getValue(month));
        assertEquals(Integer.valueOf(timestamp.split(" ")[0].split("-")[2]), record.getValue(day));
        assertEquals(Integer.valueOf(timestamp.split(" ")[1].split(":")[0]), record.getValue(hour));
        assertEquals(Integer.valueOf(timestamp.split(" ")[1].split(":")[1]), record.getValue(minute));
        assertEquals(Integer.valueOf(timestamp.split(" ")[1].split(":")[2].split("\\.")[0]), record.getValue(second));

        // Timestamp arithmetic
        // --------------------
        Field<Timestamp> tomorrow = now.add(1);
        Field<Timestamp> yesterday = now.sub(1);
        record = create().select(tomorrow, ts, yesterday).fetchOne();

        // Ingres truncates milliseconds. Ignore this fact
        assertEquals(24 * 60 * 60,
            (record.getValue(ts).getTime() / 1000 - record.getValue(yesterday).getTime() / 1000));
        assertEquals(24 * 60 * 60,
            (record.getValue(tomorrow).getTime() / 1000 - record.getValue(ts).getTime() / 1000));
    }

    @Test
    public void testExtractInSubselect() throws Exception {
        Field<Timestamp> now = currentTimestamp();

        Field<Integer> year = extract(now, DatePart.YEAR).as("y");
        Field<Integer> month = extract(now, DatePart.MONTH).as("m");
        Field<Integer> day = extract(now, DatePart.DAY).as("d");

        Select<?> sub = create().select(year, month, day);
        Table<?> subTable = sub.asTable("subselect");

        Record reference = sub.fetchOne();
        Record result;

        result = create().select().from(sub).fetchOne();
        assertEquals(reference, result);

        result = create().select().from(subTable).fetchOne();
        assertEquals(reference, result);

        result = create().select(
            sub.getField("y"),
            sub.getField("m"),
            sub.getField("d")).from(sub).fetchOne();
        assertEquals(reference, result);

        result = create().select(
            subTable.getField("y"),
            subTable.getField("m"),
            subTable.getField("d")).from(subTable).fetchOne();
        assertEquals(reference, result);
    }

    @Test
    public void testFunctionsOnNumbers() throws Exception {

        // The random function
        BigDecimal rand = create().select(rand()).fetchOne(rand());
        assertNotNull(rand);

        // Some rounding functions
        Field<Float> f1a = round(1.111f);
        Field<Float> f2a = round(1.111f, 2);
        Field<Float> f3a = floor(1.111f);
        Field<Float> f4a = ceil(1.111f);
        Field<Double> f1b = round(-1.111);
        Field<Double> f2b = round(-1.111, 2);
        Field<Double> f3b = floor(-1.111);
        Field<Double> f4b = ceil(-1.111);

        Field<Float> f1c = round(2.0f);
        Field<Float> f2c = round(2.0f, 2);
        Field<Float> f3c = floor(2.0f);
        Field<Float> f4c = ceil(2.0f);
        Field<Double> f1d = round(-2.0);
        Field<Double> f2d = round(-2.0, 2);
        Field<Double> f3d = floor(-2.0);
        Field<Double> f4d = ceil(-2.0);

        // Some arbitrary checks on having multiple select clauses
        Record record =
        create().select(f1a)
                .select(f2a, f3a)
                .select(f4a)
                .select(f1b, f2b, f3b, f4b)
                .select(f1c, f2c, f3c, f4c)
                .select(f1d, f2d, f3d, f4d).fetchOne();

        assertNotNull(record);
        assertEquals("1.0", record.getValueAsString(f1a));
        assertEquals("1.11", record.getValueAsString(f2a));
        assertEquals("1.0", record.getValueAsString(f3a));
        assertEquals("2.0", record.getValueAsString(f4a));

        assertEquals("-1.0", record.getValueAsString(f1b));
        assertEquals("-1.11", record.getValueAsString(f2b));
        assertEquals("-2.0", record.getValueAsString(f3b));
        assertEquals("-1.0", record.getValueAsString(f4b));

        assertEquals("2.0", record.getValueAsString(f1c));
        assertEquals("2.0", record.getValueAsString(f2c));
        assertEquals("2.0", record.getValueAsString(f3c));
        assertEquals("2.0", record.getValueAsString(f4c));

        assertEquals("-2.0", record.getValueAsString(f1d));
        assertEquals("-2.0", record.getValueAsString(f2d));
        assertEquals("-2.0", record.getValueAsString(f3d));
        assertEquals("-2.0", record.getValueAsString(f4d));

        // Greatest and least
        record = create().select(
            greatest(1, 2, 3, 4),
            least(1, 2, 3),
            greatest("1", "2", "3", "4"),
            least("1", "2", "3")).fetchOne();

        assertEquals(Integer.valueOf(4), record.getValue(0));
        assertEquals(Integer.valueOf(1), record.getValue(1));
        assertEquals("4", record.getValue(2));
        assertEquals("1", record.getValue(3));

        // Mathematical functions
        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "Tests for mathematical functions");
                break;

            default: {
                // Exponentials, logarithms and roots
                // ----------------------------------
                Field<BigDecimal> m1 = sqrt(2);
                Field<BigDecimal> m2 = round(sqrt(4));
                Field<BigDecimal> m3 = exp(2);
                Field<BigDecimal> m4 = round(exp(0));
                Field<BigDecimal> m5 = exp(-2);
                Field<BigDecimal> m6 = ln(2);
                Field<BigDecimal> m7 = round(log(16, 4));
                Field<BigDecimal> m8 = round(power(2, 4));
                Field<BigDecimal> m9 = round(power(sqrt(power(sqrt(2), 2)), 2));

                record = create().select(m1, m2, m3, m4, m5, m6, m7, m8, m9).fetchOne();

                // Rounding issues are circumvented by using substring()
                assertNotNull(record);
                assertEquals("1.414", record.getValueAsString(m1).substring(0, 5));
                assertEquals("2", record.getValueAsString(m2).substring(0, 1));
                assertEquals("7.389", record.getValueAsString(m3).substring(0, 5));
                assertEquals("1", record.getValueAsString(m4).substring(0, 1));
                assertEquals("0.135", record.getValueAsString(m5).substring(0, 5));
                assertEquals("0.693", record.getValueAsString(m6).substring(0, 5));
                assertEquals("2", record.getValueAsString(m7).substring(0, 1));
                assertEquals("16", record.getValueAsString(m8).substring(0, 2));
                assertEquals("2", record.getValueAsString(m9).substring(0, 1));

                // Trigonometry
                // ------------
                Field<BigDecimal> t1 = sin(Math.PI / 6 + 0.00001);
                Field<BigDecimal> t2 = cos(Math.PI / 6);
                Field<BigDecimal> t3 = tan(Math.PI / 6);
                Field<BigDecimal> t4 = cot(Math.PI / 6);
                Field<BigDecimal> t6 = rad(deg(1.1));
                Field<BigDecimal> t7 = asin(Math.PI / 6);
                Field<BigDecimal> t8 = acos(Math.PI / 6);
                Field<BigDecimal> t9 = atan(Math.PI / 6);
                Field<BigDecimal> ta = round(deg(atan2(1, 1)));

                // Hyperbolic functions
                // --------------------
                Field<BigDecimal> tb = sinh(1.0)
                    .div(cosh(1.0))
                    .mul(tanh(1.0))
                    .mul(power(coth(1.0), 2).add(0.1));

                record = create().select(t1, t2, t3, t4, t6, t7, t8, t9, ta, tb).fetchOne();

                // Rounding issues are circumvented by using substring()
                assertNotNull(record);
                assertEquals("0.5", record.getValueAsString(t1).substring(0, 3));
                assertEquals("0.866", record.getValueAsString(t2).substring(0, 5));
                assertEquals("0.577", record.getValueAsString(t3).substring(0, 5));
                assertEquals("1.732", record.getValueAsString(t4).substring(0, 5));
                assertEquals("1", record.getValueAsString(t6).substring(0, 1));
                assertEquals("0.551", record.getValueAsString(t7).substring(0, 5));
                assertEquals("1.019", record.getValueAsString(t8).substring(0, 5));
                assertEquals("0.482", record.getValueAsString(t9).substring(0, 5));
                assertEquals("45", record.getValueAsString(ta).substring(0, 2));
                assertEquals("1", record.getValueAsString(tb).substring(0, 1));

                break;
            }
        }

        // The sign function
        record = create().select(
            sign(2),
            sign(1),
            sign(0),
            sign(-1),
            sign(-2)).fetchOne();

        assertNotNull(record);
        assertEquals(Integer.valueOf(1), record.getValue(0));
        assertEquals(Integer.valueOf(1), record.getValue(1));
        assertEquals(Integer.valueOf(0), record.getValue(2));
        assertEquals(Integer.valueOf(-1), record.getValue(3));
        assertEquals(Integer.valueOf(-1), record.getValue(4));

        // The abs function
        record = create().select(
            abs(2),
            abs(1),
            abs(0),
            abs(-1),
            abs(-2)).fetchOne();

        assertNotNull(record);
        assertEquals(Integer.valueOf(2), record.getValue(0));
        assertEquals(Integer.valueOf(1), record.getValue(1));
        assertEquals(Integer.valueOf(0), record.getValue(2));
        assertEquals(Integer.valueOf(1), record.getValue(3));
        assertEquals(Integer.valueOf(2), record.getValue(4));
    }

    @Test
    public void testFunctionsOnStrings() throws Exception {

        // Trimming
        assertEquals("abc", create().select(trim("abc")).fetchOne(0));
        assertEquals("abc", create().select(trim("abc  ")).fetchOne(0));
        assertEquals("abc", create().select(trim("  abc")).fetchOne(0));
        assertEquals("abc", create().select(trim("  abc  ")).fetchOne(0));
        assertEquals("  abc", create().select(rtrim("  abc  ")).fetchOne(0));
        assertEquals("abc  ", create().select(ltrim("  abc  ")).fetchOne(0));

        // Lower / Upper
        assertEquals("abc", create().select(lower("ABC")).fetchOne(0));
        assertEquals("ABC", create().select(upper("abc")).fetchOne(0));

        // String concatenation
        assertEquals("abc", create().select(concat("a", "b", "c")).fetchOne(0));
        assertEquals("George Orwell", create()
            .select(concat(TAuthor_FIRST_NAME(), val(" "), TAuthor_LAST_NAME()))
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("George")).fetchOne(0));

        assertEquals("1ab45", create().select(concat(val(1), val("ab"), val(45))).fetchOne(0));

        // Standard String functions
        SelectQuery q = create().selectQuery();
        Field<String> constant = val("abc");

        switch (getDialect()) {

            // DERBY does not have a replace function
            case DERBY:
                log.info("SKIPPING", "replace function test");
                break;

            // These two tests will validate #154
            default: {
                Field<String> x = replace(constant, "b", "x");
                Field<String> y = replace(constant, "b", "y");
                Field<String> z = replace(constant, "b");
                Record record = create().select(x, y, z).fetchOne();

                assertEquals("axc", record.getValue(x));
                assertEquals("ayc", record.getValue(y));
                assertEquals("ac", record.getValue(z));
            }
        }

        Field<Integer> length = length(constant);
        Field<Integer> charLength = charLength(constant);
        Field<Integer> bitLength = bitLength(constant);
        Field<Integer> octetLength = octetLength(constant);
        q.addSelect(length, charLength, bitLength, octetLength);
        q.execute();

        Record record = q.getResult().get(0);

        assertEquals(Integer.valueOf(3), record.getValue(length));
        assertEquals(Integer.valueOf(3), record.getValue(charLength));

        switch (getDialect()) {
            case HSQLDB:
            case H2:
                // HSQLDB and H2 uses Java-style characters (16 bit)
                assertEquals(Integer.valueOf(48), record.getValue(bitLength));
                assertEquals(Integer.valueOf(6), record.getValue(octetLength));
                break;
            default:
                assertEquals(Integer.valueOf(24), record.getValue(bitLength));
                assertEquals(Integer.valueOf(3), record.getValue(octetLength));
                break;
        }

        // RPAD, LPAD
        switch (getDialect()) {
            case DERBY:
            case SQLITE:
                log.info("SKIPPING", "RPAD and LPAD functions");
                break;

            default: {
                Record result = create().select(
                    rpad(val("aa"), 4),
                    rpad(val("aa"), 4, "-"),
                    lpad(val("aa"), 4),
                    lpad(val("aa"), 4, "-")).fetchOne();

                assertEquals("aa  ", result.getValue(0));
                assertEquals("aa--", result.getValue(1));
                assertEquals("  aa", result.getValue(2));
                assertEquals("--aa", result.getValue(3));

                break;
            }
        }

        // SUBSTRING
        Record result = create().select(
            substring(val("abcde"), 1),
            substring(val("abcde"), 1, 2),
            substring(val("abcde"), 3),
            substring(val("abcde"), 3, 2)).fetchOne();

        assertEquals("abcde", result.getValue(0));
        assertEquals("ab", result.getValue(1));
        assertEquals("cde", result.getValue(2));
        assertEquals("cd", result.getValue(3));

        result =
        create().select(
                    substring(TAuthor_FIRST_NAME(), 2),
                    substring(TAuthor_FIRST_NAME(), 2, 2))
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        assertEquals("eorge", result.getValue(substring(TAuthor_FIRST_NAME(), 2)));
        assertEquals("eo", result.getValue(substring(TAuthor_FIRST_NAME(), 2, 2)));

        // REPEAT
        switch (getDialect()) {
            case DERBY:
            case SQLITE:
                log.info("SKIPPING", "REPEAT function");
                break;

            default: {
                result = create().select(
                    repeat("a", 1),
                    repeat("ab", 2),
                    repeat("abc", 3)).fetchOne();
                assertEquals("a", result.getValue(0));
                assertEquals("abab", result.getValue(1));
                assertEquals("abcabcabc", result.getValue(2));
                break;
            }
        }

        // ASCII
        switch (getDialect()) {
            case DERBY:
            case INGRES: // TODO [#864]
            case SQLITE: // TODO [#862]
                log.info("SKIPPING", "ASCII function test");
                break;

            default:
                record =
                create().select(
                    ascii("A"),
                    ascii("a"),
                    ascii("-"),
                    ascii(" ")).fetchOne();
                assertEquals((int) 'A', (int) record.getValueAsInteger(0));
                assertEquals((int) 'a', (int) record.getValueAsInteger(1));
                assertEquals((int) '-', (int) record.getValueAsInteger(2));
                assertEquals((int) ' ', (int) record.getValueAsInteger(3));

                break;
        }
    }

    @Test
    public void testFunctionPosition() throws Exception {
        // SQLite does not have anything like the position function
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "position function test");
            return;
        }

        SelectQuery q = create().selectQuery();
        q.addFrom(VLibrary());

        Field<Integer> position = position(VLibrary_AUTHOR(), "o").as("p");
        q.addSelect(VLibrary_AUTHOR());
        q.addSelect(position);

        // https://issues.apache.org/jira/browse/DERBY-5005
        q.addOrderBy(field(VLibrary_AUTHOR().getName()));

        q.execute();
        Record r1 = q.getResult().get(1); // George Orwell
        Record r2 = q.getResult().get(2); // Paulo Coelho

        assertEquals(Integer.valueOf(3), r1.getValue(position));
        assertEquals(Integer.valueOf(5), r2.getValue(position));

        // Implicit check on the rownum function in oracle dialect
        L library = create().fetchAny(VLibrary());
        assertTrue(library != null);
    }

    @Test
    public void testFunctionsLikeDecode() throws Exception {
        Field<String> sNull = castNull(String.class);
        Field<Integer> iNull = castNull(Integer.class);

        // ---------------------------------------------------------------------
        // NULLIF
        // ---------------------------------------------------------------------
        assertEquals("1", create().select(nullif("1", "2")).fetchOne(0));
        assertEquals(null, create().select(nullif("1", "1")).fetchOne(0));
        assertEquals("1", "" + create().select(nullif(1, 2)).fetchOne(0));
        assertEquals(null, create().select(nullif(1, 1)).fetchOne(0));

        // ---------------------------------------------------------------------
        // NVL
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(nvl(sNull, sNull)).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(nvl(iNull, 1)).fetchOne(0));
        assertEquals("1", create().select(nvl(sNull, "1")).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(nvl(2, 1)).fetchOne(0));
        assertEquals("2", create().select(nvl("2", "1")).fetchOne(0));

        // TODO [#831] Fix this for Sybase ASE
        if (getDialect() != SQLDialect.ASE) {
            assertTrue(("" + create()
                .select(nvl(TBook_CONTENT_TEXT(), "abc"))
                .from(TBook())
                .where(TBook_ID().equal(1)).fetchOne(0)).startsWith("To know and"));
            assertEquals("abc", create()
                .select(nvl(TBook_CONTENT_TEXT(), "abc"))
                .from(TBook())
                .where(TBook_ID().equal(2)).fetchOne(0));
        }

        // ---------------------------------------------------------------------
        // NVL2
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(nvl2(sNull, sNull, sNull)).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(nvl2(iNull, 2, 1)).fetchOne(0));
        assertEquals("1", create().select(nvl2(sNull, "2", "1")).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(nvl2(val(2), 2, 1)).fetchOne(0));
        assertEquals("2", create().select(nvl2(val("2"), "2", "1")).fetchOne(0));

        // TODO [#831] Fix this for Sybase ASE
        if (getDialect() != SQLDialect.ASE) {
            assertEquals("abc", create()
                .select(nvl2(TBook_CONTENT_TEXT(), "abc", "xyz"))
                .from(TBook())
                .where(TBook_ID().equal(1)).fetchOne(0));
            assertEquals("xyz", create()
                .select(nvl2(TBook_CONTENT_TEXT(), "abc", "xyz"))
                .from(TBook())
                .where(TBook_ID().equal(2)).fetchOne(0));
        }

        // ---------------------------------------------------------------------
        // COALESCE
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(coalesce(sNull, sNull)).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(coalesce(iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(coalesce(iNull, iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(coalesce(iNull, iNull, iNull, val(1))).fetchOne(0));

        assertEquals("1", create().select(coalesce(sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(coalesce(sNull, sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(coalesce(sNull, sNull, sNull, val("1"))).fetchOne(0));

        assertEquals(Integer.valueOf(2), create().select(coalesce(2, 1)).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(coalesce(2, 1, 1)).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(coalesce(2, 1, 1, 1)).fetchOne(0));

        assertEquals("2", create().select(coalesce("2", "1")).fetchOne(0));
        assertEquals("2", create().select(coalesce("2", "1", "1")).fetchOne(0));
        assertEquals("2", create().select(coalesce("2", "1", "1", "1")).fetchOne(0));

        assertTrue(("" + create()
            .select(coalesce(TBook_CONTENT_TEXT().cast(String.class), sNull, val("abc")))
            .from(TBook())
            .where(TBook_ID().equal(1)).fetchOne(0)).startsWith("To know and"));
        assertEquals("abc", create()
            .select(coalesce(TBook_CONTENT_TEXT().cast(String.class), sNull, val("abc")))
            .from(TBook())
            .where(TBook_ID().equal(2)).fetchOne(0));

        // ---------------------------------------------------------------------
        // DECODE
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(decode(sNull, sNull, sNull)).fetchOne(0));
        assertEquals(null, create().select(decode(iNull, val(2), val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, val(2), val(1), val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, iNull, val(1), val(2))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, val(2), val(2), iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, val(2), val(2), iNull, val(1), val(3))).fetchOne(0));

        assertEquals(null, create().select(decode(sNull, "2", "1")).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, "2", "1", "1")).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, sNull, val("1"), val("2"))).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, val("2"), val("2"), sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, val("2"), val("2"), sNull, val("1"), val("3"))).fetchOne(0));

        Field<Integer> lang = TBook_LANGUAGE_ID().cast(Integer.class);
        Result<Record> result = create().select(
                decode(lang, 1, "EN"),
                decode(lang, 1, "EN", "Other"),
                decode(lang, 1, "EN", 2, "DE"),
                decode(lang, 1, "EN", 2, "DE", "Other"))
            .from(TBook())
            .orderBy(TBook_ID()).fetch();

        assertEquals("EN", result.getValue(0, 0));
        assertEquals("EN", result.getValue(1, 0));
        assertEquals(null, result.getValue(2, 0));
        assertEquals(null, result.getValue(3, 0));

        assertEquals("EN", result.getValue(0, 1));
        assertEquals("EN", result.getValue(1, 1));
        assertEquals("Other", result.getValue(2, 1));
        assertEquals("Other", result.getValue(3, 1));

        assertEquals("EN", result.getValue(0, 2));
        assertEquals("EN", result.getValue(1, 2));
        assertEquals(null, result.getValue(2, 2));
        assertEquals("DE", result.getValue(3, 2));

        assertEquals("EN", result.getValue(0, 3));
        assertEquals("EN", result.getValue(1, 3));
        assertEquals("Other", result.getValue(2, 3));
        assertEquals("DE", result.getValue(3, 3));
    }

    @Test
    public void testCaseStatement() throws Exception {
        Field<String> case1 = decode()
            .value(TBook_PUBLISHED_IN())
            .when(0, "ancient book")
            .as("case1");

        // Ingres does not allow sub selects in CASE expressions
        Field<?> case2 = getDialect() == SQLDialect.INGRES
            ? decode()
                .value(TBook_AUTHOR_ID())
                .when(1, "Orwell")
                .otherwise("unknown")
            : decode()
                .value(TBook_AUTHOR_ID())
                .when(1, create().select(TAuthor_LAST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().equal(TBook_AUTHOR_ID())).asField())
                .otherwise("unknown");

        Field<?> case3 = decode()
            .value(1)
            .when(1, "A")
            .when(2, "B")
            .otherwise("C");

        SelectQuery query = create().selectQuery();
        query.addSelect(case1, case2, case3);
        query.addFrom(TBook());
        query.addOrderBy(TBook_PUBLISHED_IN());
        query.execute();

        Result<Record> result = query.getResult();
        assertEquals(null, result.getValue(0, case1));
        assertEquals(null, result.getValue(1, case1));
        assertEquals(null, result.getValue(2, case1));
        assertEquals(null, result.getValue(3, case1));

        assertEquals("Orwell", result.getValue(0, case2));
        assertEquals("Orwell", result.getValue(1, case2));
        assertEquals("unknown", result.getValue(2, case2));
        assertEquals("unknown", result.getValue(3, case2));

        assertEquals("A", result.getValue(0, case3));
        assertEquals("A", result.getValue(1, case3));
        assertEquals("A", result.getValue(2, case3));
        assertEquals("A", result.getValue(3, case3));

        Field<String> case4 = decode()
            .when(TBook_PUBLISHED_IN().equal(1948), "probably orwell")
            .when(TBook_PUBLISHED_IN().equal(1988), "probably coelho")
            .otherwise("don't know").as("case3");

        query = create().selectQuery();
        query.addSelect(case4);
        query.addFrom(TBook());
        query.addOrderBy(TBook_PUBLISHED_IN());
        query.execute();

        result = query.getResult();

        // Note: trims are necessary, as certain databases use
        // CHAR datatype here, not VARCHAR
        assertEquals("don't know", result.getValue(0, case4).trim());
        assertEquals("probably orwell", result.getValue(1, case4).trim());
        assertEquals("probably coelho", result.getValue(2, case4).trim());
        assertEquals("don't know", result.getValue(3, case4).trim());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testEnums() throws Exception {
        if (TBook_STATUS() == null) {
            log.info("SKIPPING", "enums test");
            return;
        }

        reset = false;

        B book = create()
            .selectFrom(TBook())
            .where(TBook_PUBLISHED_IN().equal(1990))
            .fetchOne();
        Enum<?> value = book.getValue(TBook_STATUS());
        assertEquals("SOLD_OUT", value.name());
        assertEquals("SOLD OUT", ((EnumType) value).getLiteral());

        // Another copy of the original record
        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        book.setValue((Field) TBook_STATUS(), Enum.valueOf(value.getClass(), "ON_STOCK"));
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        value = book.getValue(TBook_STATUS());
        assertEquals("ON_STOCK", value.name());
        assertEquals("ON STOCK", ((EnumType) value).getLiteral());
    }

    public <R extends TableRecord<R>> void testCustomEnums() throws Exception {
        reset = false;

        // This does not yet work correctly for Sybase ASE, Postgres
        // Sybase: Is casting enums to unknown enum types
        // ASE: Cannot implicitly cast '1' to 1

        // TODO [#677] This doesn't work correctly yet for
        // Ingres, HSQLDB, H2, Derby, Sybase ASE
        // Double-check again for Postgres

        @SuppressWarnings("unchecked")
        Table<R> booleans = (Table<R>) getTable("T_BOOLEANS");

        @SuppressWarnings("unchecked")
        Field<Integer> id = (Field<Integer>) getField(booleans, "ID");

        @SuppressWarnings("unchecked")
        Field<EnumType> e1 = (Field<EnumType>) getField(booleans, "ONE_ZERO");
        EnumType e1False = (EnumType) e1.getType().getField("_0").get(e1.getType());
        EnumType e1True = (EnumType) e1.getType().getField("_1").get(e1.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e2 = (Field<EnumType>) getField(booleans, "TRUE_FALSE_LC");
        EnumType e2False = (EnumType) e2.getType().getField("false_").get(e2.getType());
        EnumType e2True = (EnumType) e2.getType().getField("true_").get(e2.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e3 = (Field<EnumType>) getField(booleans, "TRUE_FALSE_UC");
        EnumType e3False = (EnumType) e3.getType().getField("FALSE").get(e3.getType());
        EnumType e3True = (EnumType) e3.getType().getField("TRUE").get(e3.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e4 = (Field<EnumType>) getField(booleans, "YES_NO_LC");
        EnumType e4False = (EnumType) e4.getType().getField("no").get(e4.getType());
        EnumType e4True = (EnumType) e4.getType().getField("yes").get(e4.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e5 = (Field<EnumType>) getField(booleans, "YES_NO_UC");
        EnumType e5False = (EnumType) e5.getType().getField("NO").get(e5.getType());
        EnumType e5True = (EnumType) e5.getType().getField("YES").get(e5.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e6 = (Field<EnumType>) getField(booleans, "Y_N_LC");
        EnumType e6False = (EnumType) e6.getType().getField("n").get(e6.getType());
        EnumType e6True = (EnumType) e6.getType().getField("y").get(e6.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e7 = (Field<EnumType>) getField(booleans, "Y_N_UC");
        EnumType e7False = (EnumType) e7.getType().getField("N").get(e7.getType());
        EnumType e7True = (EnumType) e7.getType().getField("Y").get(e7.getType());

        @SuppressWarnings("unchecked")
        Field<Boolean> b1 = (Field<Boolean>) getField(booleans, "C_BOOLEAN");

        @SuppressWarnings("unchecked")
        Field<Boolean> b2 = (Field<Boolean>) getField(booleans, "VC_BOOLEAN");

        @SuppressWarnings("unchecked")
        Field<Boolean> b3 = (Field<Boolean>) getField(booleans, "N_BOOLEAN");

        assertEquals(1,
        create().insertInto(booleans)
                .set(id, 1)
                .set(e1, e1False)
                .set(e2, e2False)
                .set(e3, e3False)
                .set(e4, e4False)
                .set(e5, e5False)
                .set(e6, e6False)
                .set(e7, e7False)
                .set(b1, false)
                .set(b2, false)
                .set(b3, false)
                .execute());

        assertEquals(1,
        create().insertInto(booleans)
                .set(id, 2)
                .set(e1, e1True)
                .set(e2, e2True)
                .set(e3, e3True)
                .set(e4, e4True)
                .set(e5, e5True)
                .set(e6, e6True)
                .set(e7, e7True)
                .set(b1, true)
                .set(b2, true)
                .set(b3, true)
                .execute());

        Result<?> result =
        create().selectFrom(booleans).orderBy(id.asc()).fetch();

        assertEquals(1, (int) result.getValue(0, id));
        assertEquals(2, (int) result.getValue(1, id));

        assertEquals(e1False, result.getValue(0, e1));
        assertEquals(e1True, result.getValue(1, e1));

        assertEquals(e2False, result.getValue(0, e2));
        assertEquals(e2True, result.getValue(1, e2));

        assertEquals(e3False, result.getValue(0, e3));
        assertEquals(e3True, result.getValue(1, e3));

        assertEquals(e4False, result.getValue(0, e4));
        assertEquals(e4True, result.getValue(1, e4));

        assertEquals(e5False, result.getValue(0, e5));
        assertEquals(e5True, result.getValue(1, e5));

        assertEquals(e6False, result.getValue(0, e6));
        assertEquals(e6True, result.getValue(1, e6));

        assertEquals(e7False, result.getValue(0, e7));
        assertEquals(e7True, result.getValue(1, e7));

        assertFalse(result.getValue(0, b1));
        assertTrue(result.getValue(1, b1));

        assertFalse(result.getValue(0, b2));
        assertTrue(result.getValue(1, b2));

        assertFalse(result.getValue(0, b3));
        assertTrue(result.getValue(1, b3));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testMasterData() throws Exception {
        if (!supportsReferences()) {
            log.info("SKIPPING", "master data test");
            return;
        }

        reset = false;

        B book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        Enum<?> value = (Enum<?>) book.getValue(TBook_LANGUAGE_ID());
        assertEquals(Integer.valueOf(1), ((MasterDataType<?>) value).getPrimaryKey());
        assertEquals("en", value.name());

        book.setValue((Field) TBook_LANGUAGE_ID(), Enum.valueOf(value.getClass(), "de"));
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        value = (Enum<?>) book.getValue(TBook_LANGUAGE_ID());
        assertEquals(Integer.valueOf(2), ((MasterDataType<?>) value).getPrimaryKey());
        assertEquals("de", value.name());

        // [#658] - General master data test
        T658 master = create().fetchOne(T658());
        assertNotNull(master);
        assertEquals("A", invoke(master.getValue(0), "getPrimaryKey").toString().trim());
        assertEquals("A", invoke(master.getValue(0), "getId").toString().trim());
        assertEquals(1, invoke(master.getValue(1), "getPrimaryKey"));
        assertEquals(1, invoke(master.getValue(1), "getId"));
        assertEquals(1L, invoke(master.getValue(2), "getPrimaryKey"));
        assertEquals(1L, invoke(master.getValue(2), "getId"));

        assertEquals("B", invoke(master.getValue(3), "getPrimaryKey").toString().trim());
        assertEquals("B", invoke(master.getValue(3), "getId").toString().trim());
        assertEquals("B", invoke(master.getValue(3), "getCd").toString().trim());
        assertEquals(2, invoke(master.getValue(4), "getPrimaryKey"));
        assertEquals(2, invoke(master.getValue(4), "getId"));
        assertEquals(2, invoke(master.getValue(4), "getCd"));
        assertEquals(2L, invoke(master.getValue(5), "getPrimaryKey"));
        assertEquals(2L, invoke(master.getValue(5), "getId"));
        assertEquals(2L, invoke(master.getValue(5), "getCd"));
    }

    @Test
    public void testSerialisation() throws Exception {
        reset = false;

        Select<A> q = create().selectFrom(TAuthor()).orderBy(TAuthor_LAST_NAME());

        // Serialising the unexecuted query
        // ---------------------------------------------------------------------
        q = runSerialisation(q);

        try {
            q.execute();
            fail();
        } catch (DetachedException expected) {}

        // Serialising the executed query
        // ---------------------------------------------------------------------
        create().attach(q);
        assertEquals(2, q.execute());
        assertEquals("Coelho", q.getResult().getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", q.getResult().getValue(1, TAuthor_LAST_NAME()));

        q = runSerialisation(q);
        assertEquals("Coelho", q.getResult().getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", q.getResult().getValue(1, TAuthor_LAST_NAME()));

        Result<A> result = q.getResult();
        result = runSerialisation(result);
        assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

        try {
            result.get(1).setValue(TAuthor_FIRST_NAME(), "Georgie");
            result.get(1).store();
            fail();
        } catch (DetachedException expected) {}

        create().attach(result);
        assertEquals(1, result.get(1).store());
        assertEquals("Georgie", create()
                .fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Orwell"))
                .getValue(TAuthor_FIRST_NAME()));

        // Redoing the test with a ConfigurationRegistry
        // ---------------------------------------------------------------------
        register(create());
        try {
            q = create().selectFrom(TAuthor()).orderBy(TAuthor_LAST_NAME());
            q = runSerialisation(q);
            q.execute();

            result = q.getResult();
            result = runSerialisation(result);
            assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
            assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

            result.get(1).setValue(TAuthor_FIRST_NAME(), "Georgie");
            result.get(1).store();
        }
        finally {
            register(null);
        }


        // Redoing the test with a ConfigurationRegistry, registering after
        // deserialisation
        // ---------------------------------------------------------------------
        try {
            q = create().selectFrom(TAuthor()).orderBy(TAuthor_LAST_NAME());
            q = runSerialisation(q);

            register(create());
            q.execute();
            register(null);

            result = q.getResult();
            result = runSerialisation(result);
            assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
            assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

            result.get(1).setValue(TAuthor_FIRST_NAME(), "G");

            register(create());
            result.get(1).store();
        }
        finally {
            register(null);
        }

        // [#775] Test for proper lazy execution after deserialisation
        try {
            q = create().selectFrom(TAuthor()).orderBy(TAuthor_LAST_NAME());
            q = runSerialisation(q);

            register(create());
            Cursor<A> cursor = q.fetchLazy();
            register(null);

            assertEquals("Coelho", cursor.fetchOne().getValue(TAuthor_LAST_NAME()));
            assertEquals("Orwell", cursor.fetchOne().getValue(TAuthor_LAST_NAME()));
        }
        finally {
            register(null);
        }
    }

    @SuppressWarnings("unchecked")
    private <Z> Z runSerialisation(Z value) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        o.writeObject(value);
        o.flush();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream i = new ObjectInputStream(in);
        return (Z) i.readObject();
    }

    @Test
    public void testARRAYType() throws Exception {
        if (TArrays() == null) {
            log.info("SKIPPING", "ARRAY type test");
            return;
        }

        reset = false;

        if (TArrays_STRING_R() != null) {
            Result<?> arrays = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .orderBy(TArrays_ID())
            .fetch();

            assertEquals(null, arrays.getValue(0, TArrays_STRING_R()));
            assertEquals(null, arrays.getValue(0, TArrays_NUMBER_R()));
            assertEquals(null, arrays.getValue(0, TArrays_DATE_R()));

            assertEquals(Arrays.asList(), Arrays.asList(arrays.getValueAsArray(1, TArrays_STRING_R())));
            assertEquals(Arrays.asList(), Arrays.asList(arrays.getValueAsArray(1, TArrays_NUMBER_R())));
            assertEquals(Arrays.asList(), Arrays.asList(arrays.getValueAsArray(1, TArrays_DATE_R())));

            assertEquals(Arrays.asList("a"), Arrays.asList(arrays.getValueAsArray(2, TArrays_STRING_R())));
            assertEquals(Arrays.asList(1), Arrays.asList(arrays.getValueAsArray(2, TArrays_NUMBER_R())));
            assertEquals("[1981-07-10]", Arrays.asList(arrays.getValueAsArray(2, TArrays_DATE_R())).toString());

            assertEquals(Arrays.asList("a", "b"), Arrays.asList(arrays.getValueAsArray(3, TArrays_STRING_R())));
            assertEquals(Arrays.asList(1, 2), Arrays.asList(arrays.getValueAsArray(3, TArrays_NUMBER_R())));
            assertEquals("[1981-07-10, 2000-01-01]", Arrays.asList(arrays.getValueAsArray(3, TArrays_DATE_R())).toString());



            InsertQuery<?> insert = create().insertQuery(TArrays());
            insert.addValue(TArrays_ID(), 5);
            insert.addValueAsArray(TArrays_NUMBER_R(), 1, 2, 3);
            insert.addValueAsArray(TArrays_STRING_R(), "a", "b", "c");
            insert.addValueAsArray(TArrays_DATE_R(), new Date(0), new Date(84600 * 1000), new Date(84600 * 2000));
            insert.execute();

            Record array = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            assertEquals(Arrays.asList("a", "b", "c"), Arrays.asList(array.getValueAsArray(TArrays_STRING_R())));
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList(array.getValueAsArray(TArrays_NUMBER_R())));
            assertEquals("[1970-01-01, 1970-01-02, 1970-01-03]", Arrays.asList(array.getValueAsArray(TArrays_DATE_R())).toString());



            UpdateQuery<X> update = create().updateQuery(TArrays());
            update.addValueAsArray(TArrays_NUMBER_R(), 3, 2, 1);
            update.addValueAsArray(TArrays_STRING_R(), "c", "b", "a");
            update.addValueAsArray(TArrays_DATE_R(), new Date(84600 * 2000), new Date(84600 * 1000), new Date(0));
            update.addConditions(TArrays_ID().equal(5));
            update.execute();

            array = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            assertEquals(Arrays.asList("c", "b", "a"), Arrays.asList(array.getValueAsArray(TArrays_STRING_R())));
            assertEquals(Arrays.asList(3, 2, 1), Arrays.asList(array.getValueAsArray(TArrays_NUMBER_R())));
            assertEquals("[1970-01-03, 1970-01-02, 1970-01-01]", Arrays.asList(array.getValueAsArray(TArrays_DATE_R())).toString());
        }

        if (TArrays_STRING() != null) {
            Result<?> arrays = create().select(
                TArrays_STRING(),
                TArrays_NUMBER(),
                TArrays_DATE(),
                TArrays_UDT() == null ? val(0) : TArrays_UDT())
            .from(TArrays())
            .orderBy(TArrays_ID())
            .fetch();

            // First record: null
            // -----------------------------------------------------------------
            assertEquals(null, arrays.getValue(0, TArrays_STRING()));
            assertEquals(null, arrays.getValue(0, TArrays_NUMBER()));
            assertEquals(null, arrays.getValue(0, TArrays_DATE()));

            if (TArrays_UDT() != null) {
                assertEquals(null, arrays.getValue(0, TArrays_UDT()));
            }


            // Second record: empty
            // -----------------------------------------------------------------
            // These expressions are a bit verbose. Unfortunately, H2 does not
            // support typed arrays, hence the tests are kept general
            // http://groups.google.com/group/h2-database/browse_thread/thread/42e38afa682d4fc2
            Object[] s = (Object[]) arrays.getValue(1, 0);
            Object[] n = (Object[]) arrays.getValue(1, 1);
            Object[] d = (Object[]) arrays.getValue(1, 2);
            assertEquals(0, s.length);
            assertEquals(0, n.length);
            assertEquals(0, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());

            if (TArrays_UDT() != null) {
                UDTRecord<?>[] u = (UDTRecord<?>[]) arrays.getValue(1, 3);
                assertEquals(0, u.length);
                assertEquals(TArrays_UDT().getType(), u.getClass());
            }

            // Third record: one element
            // -----------------------------------------------------------------
            s = (Object[]) arrays.getValue(2, 0);
            n = (Object[]) arrays.getValue(2, 1);
            d = (Object[]) arrays.getValue(2, 2);
            assertEquals(1, s.length);
            assertEquals(1, n.length);
            assertEquals(1, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("1", n[0].toString());
            assertEquals("1981-07-10", d[0].toString());

            if (TArrays_UDT() != null) {
                UDTRecord<?>[] u = (UDTRecord<?>[]) arrays.getValue(2, 3);
                assertEquals(1, u.length);
                assertEquals(TArrays_UDT().getType(), u.getClass());
                assertEquals("Downing Street", u[0].getValue(0));
                assertEquals("10", u[0].getValue(1));
                assertNull(u[0].getValue(2));
            }

            // Fourth record: two elements
            // -----------------------------------------------------------------
            s = (Object[]) arrays.getValue(3, 0);
            n = (Object[]) arrays.getValue(3, 1);
            d = (Object[]) arrays.getValue(3, 2);
            assertEquals(2, s.length);
            assertEquals(2, n.length);
            assertEquals(2, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("1", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("1981-07-10", d[0].toString());
            assertEquals("2000-01-01", d[1].toString());

            if (TArrays_UDT() != null) {
                UDTRecord<?>[] u = (UDTRecord<?>[]) arrays.getValue(3, 3);
                assertEquals(2, u.length);
                assertEquals(TArrays_UDT().getType(), u.getClass());

                assertEquals("Downing Street", u[0].getValue(0));
                assertEquals("10", u[0].getValue(1));
                Integer[] floors = (Integer[]) u[0].getValue(2);
                assertEquals(0, floors.length);

                assertEquals("Bahnhofstrasse", u[1].getValue(0));
                assertEquals("12", u[1].getValue(1));
                floors = (Integer[]) u[1].getValue(2);
                assertEquals(2, floors.length);
                assertEquals(1, (int) floors[0]);
                assertEquals(2, (int) floors[1]);
            }


            // Insert again
            // -----------------------------------------------------------------
            InsertQuery<?> insert = create().insertQuery(TArrays());
            insert.addValue(TArrays_ID(), 5);
            insert.addValue(TArrays_NUMBER(), new Integer[] { 1, 2, 3 });
            insert.addValue(TArrays_STRING(), new String[] { "a", "b", "c" });
            insert.addValue(TArrays_DATE(), new Date[] { new Date(0), new Date(84600 * 1000), new Date(84600 * 2000)});

            insert.execute();

            Record array = create().select(
                    TArrays_STRING(),
                    TArrays_NUMBER(),
                    TArrays_DATE())
                .from(TArrays())
                .where(TArrays_ID().equal(5))
                .fetchOne();

            s = (Object[]) array.getValue(0);
            n = (Object[]) array.getValue(1);
            d = (Object[]) array.getValue(2);
            assertEquals(3, s.length);
            assertEquals(3, n.length);
            assertEquals(3, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("c", s[2].toString());
            assertEquals("1", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("3", n[2].toString());
            assertEquals("1970-01-01", d[0].toString());
            assertEquals("1970-01-02", d[1].toString());
            assertEquals("1970-01-03", d[2].toString());



            UpdateQuery<X> update = create().updateQuery(TArrays());
            update.addValue(TArrays_NUMBER(), new Integer[] { 3, 2, 1});
            update.addValue(TArrays_STRING(), new String[] { "c", "b", "a" });
            update.addValue(TArrays_DATE(), new Date[] { new Date(84600 * 2000), new Date(84600 * 1000), new Date(0) });
            update.addConditions(TArrays_ID().equal(5));
            update.execute();

            array = create().select(
                TArrays_STRING(),
                TArrays_NUMBER(),
                TArrays_DATE())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            s = (Object[]) array.getValue(0);
            n = (Object[]) array.getValue(1);
            d = (Object[]) array.getValue(2);
            assertEquals(3, s.length);
            assertEquals(3, n.length);
            assertEquals(3, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("c", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("a", s[2].toString());
            assertEquals("3", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("1", n[2].toString());
            assertEquals("1970-01-03", d[0].toString());
            assertEquals("1970-01-02", d[1].toString());
            assertEquals("1970-01-01", d[2].toString());
        }
    }

    @Test
    public void testARRAYProcedure() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "ARRAY procedure test (no procedure support)");
            return;
        }

        if (TArrays() == null) {
            log.info("SKIPPING", "ARRAY procedure test (no array support)");
            return;
        }

        reset = false;

        if (TArrays_STRING_R() != null) {
            ArrayRecord<Integer> i;
            ArrayRecord<Long> l;
            ArrayRecord<String> s;

            assertEquals(null, invoke(cRoutines(), "pArrays1", create(), null));
            assertEquals(null, invoke(cRoutines(), "pArrays2", create(), null));
            assertEquals(null, invoke(cRoutines(), "pArrays3", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays1", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays2", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays3", create(), null));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays3", create(), s)).get()));
            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays3", create(), s)).get()));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            i.set((Integer) null);
            l.set((Long) null);
            s.set((String) null);

            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays3", create(), s)).get()));
            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays3", create(), s)).get()));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            i.set(1, 2);
            l.set(1L, 2L);
            s.set("1", "2");

            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays3", create(), s)).get()));
            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays3", create(), s)).get()));
        }

        if (TArrays_STRING() != null) {
            if (supportsOUTParameters()) {
                assertEquals(null, invoke(cRoutines(), "pArrays1", create(), null));
                assertEquals(null, invoke(cRoutines(), "pArrays2", create(), null));
                assertEquals(null, invoke(cRoutines(), "pArrays3", create(), null));
            }

            assertEquals(null, invoke(cRoutines(), "fArrays1", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays2", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays3", create(), null));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList(new Integer[0]),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create(), new Integer[0])));
                assertEquals(
                    Arrays.asList(new Long[0]),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create(), new Long[0])));
                assertEquals(
                    Arrays.asList(new String[0]),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create(), new String[0])));
            }

            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create(), new Integer[0])));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create(), new Long[0])));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create(), new String[0])));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList((Integer) null),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create(), new Integer[] { null })));
                assertEquals(
                    Arrays.asList((Long) null),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create(), new Long[] { null })));
                assertEquals(
                    Arrays.asList((String) null),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create(), new String[] { null })));
            }

            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create(), new Integer[] { null })));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create(), new Long[] { null })));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create(), new String[] { null })));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList(1, 2),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create(), new Integer[] {1, 2})));
                assertEquals(
                    Arrays.asList(1L, 2L),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create(), new Long[] {1L, 2L})));
                assertEquals(
                    Arrays.asList("1", "2"),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create(), new String[] {"1", "2"})));
            }

            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create(), new Integer[] {1, 2})));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create(), new Long[] {1L, 2L})));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create(), new String[] {"1", "2"})));
        }
    }

    private ArrayRecord<Integer> newNUMBER_R() throws Exception {
        ArrayRecord<Integer> result = TArrays_NUMBER_R().getType().getConstructor(Configuration.class).newInstance(create());
        return result;
    }

    private ArrayRecord<Long> newNUMBER_LONG_R() throws Exception {
        ArrayRecord<Long> result = TArrays_NUMBER_LONG_R().getType().getConstructor(Configuration.class).newInstance(create());
        return result;
    }

    private ArrayRecord<String> newSTRING_R() throws Exception {
        ArrayRecord<String> result = TArrays_STRING_R().getType().getConstructor(Configuration.class).newInstance(create());
        return result;
    }

    @Test
    public void testUDTs() throws Exception {
        if (TAuthor_ADDRESS() == null) {
            log.info("SKIPPING", "UDT test");
            return;
        }

        reset = false;

        Result<A> authors = create().selectFrom(TAuthor()).fetch();
        UDTRecord<?> a1 = authors.get(0).getValue(TAuthor_ADDRESS());
        UDTRecord<?> a2 = authors.get(1).getValue(TAuthor_ADDRESS());

        Object street1 = a1.getClass().getMethod("getStreet").invoke(a1);
        assertEquals("77", street1.getClass().getMethod("getNo").invoke(street1));
        assertEquals("Parliament Hill", street1.getClass().getMethod("getStreet").invoke(street1));
        assertEquals("NW31A9", a1.getClass().getMethod("getZip").invoke(a1));
        assertEquals("Hampstead", a1.getClass().getMethod("getCity").invoke(a1));
        assertEquals("England", "" + a1.getClass().getMethod("getCountry").invoke(a1));
        assertEquals(null, a1.getClass().getMethod("getCode").invoke(a1));

        if (TArrays_NUMBER_R() != null) {
            assertEquals(Arrays.asList(1, 2, 3), invoke(invoke(street1, "getFloors"), "getList"));
        }
        if (TArrays_NUMBER() != null) {
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList((Object[]) invoke(street1, "getFloors")));
        }

        Object street2 = a2.getClass().getMethod("getStreet").invoke(a2);
        assertEquals("43.003", street1.getClass().getMethod("getNo").invoke(street2));
        assertEquals("Caixa Postal", street1.getClass().getMethod("getStreet").invoke(street2));
        assertEquals(null, a2.getClass().getMethod("getZip").invoke(a2));
        assertEquals("Rio de Janeiro", a2.getClass().getMethod("getCity").invoke(a2));
        assertEquals("Brazil", "" + a1.getClass().getMethod("getCountry").invoke(a2));
        assertEquals(2, a1.getClass().getMethod("getCode").invoke(a2));

        if (TArrays_NUMBER_R() != null) {
            assertEquals(null, invoke(street2, "getFloors"));
        }
        if (TArrays_NUMBER() != null) {
            assertEquals(null, invoke(street2, "getFloors"));
        }
    }

    @Test
    public void testUDTProcedure() throws Exception {
        if (cUAddressType() == null) {
            log.info("SKIPPING", "UDT procedure test (no UDT support)");
            return;
        }

        if (cRoutines() == null) {
            log.info("SKIPPING", "UDT procedure test (no procedure support)");
            return;
        }

        if (getDialect() == SQLDialect.POSTGRES) {
            log.info("SKIPPING", "UDT procedure test (Postgres JDBC driver flaw)");
            return;
        }

        reset = false;

        UDTRecord<?> address = cUAddressType().newInstance();
        UDTRecord<?> street = cUStreetType().newInstance();
        invoke(street, "setNo", "35");
        invoke(address, "setStreet", street);

        // First procedure
        Object result = invoke(cRoutines(), "pEnhanceAddress1", create(), address);
        assertEquals("35", result);

        // Second procedure
        address = invoke(cRoutines(), "pEnhanceAddress2", create());
        street = invoke(address, "getStreet");
        assertEquals("Parliament Hill", invoke(street, "getStreet"));
        assertEquals("77", invoke(street, "getNo"));

        if (TArrays_NUMBER_R() != null) {
            assertEquals(Arrays.asList(1, 2, 3), invoke(invoke(street, "getFloors"), "getList"));
        }
        if (TArrays_NUMBER() != null) {
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList((Object[]) invoke(street, "getFloors")));
        }

        // Third procedure
        address = (UDTRecord<?>) invoke(cRoutines(), "pEnhanceAddress3", create(), address);
        street = (UDTRecord<?>) invoke(address, "getStreet");
        assertEquals("Zwinglistrasse", invoke(street, "getStreet"));
        assertEquals("17", invoke(street, "getNo"));
    }

    @Test
    public void testAttachable() throws Exception {
        reset = false;

        Factory create = create();

        S store = create.newRecord(TBookStore());
        assertNotNull(store);

        store.setValue(TBookStore_NAME(), "Barnes and Noble");
        assertEquals(1, store.store());

        store = create.newRecord(TBookStore());
        store.setValue(TBookStore_NAME(), "Barnes and Noble");
        store.attach(null);

        try {
            store.store();
            fail();
        }
        catch (DetachedException expected) {}

        try {
            store.refresh();
            fail();
        }
        catch (DetachedException expected) {}

        try {
            store.delete();
            fail();
        }
        catch (DetachedException expected) {}

        store.attach(create);
        store.refresh();
        assertEquals(1, store.delete());
        assertNull(create.fetchOne(TBookStore(), TBookStore_NAME().equal("Barnes and Noble")));
    }

    @Test
    public void testNULL() throws Exception {
        reset = false;

        Field<Integer> n = castNull(Integer.class);
        Field<Integer> c = val(1);

        assertEquals(null, create().select(n).fetchOne(n));
        assertEquals(Integer.valueOf(1), create().select(c).from(TAuthor()).where(TAuthor_ID().equal(1)).and(n.isNull()).fetchOne(c));
        assertEquals(Integer.valueOf(1), create().select(c).from(TAuthor()).where(TAuthor_ID().equal(1)).and(n.equal(n)).fetchOne(c));
        assertEquals(null, create().selectOne().from(TAuthor()).where(n.isNotNull()).fetchAny());
        assertEquals(null, create().selectOne().from(TAuthor()).where(n.notEqual(n)).fetchAny());

        UpdateQuery<A> u = create().updateQuery(TAuthor());
        u.addValue(TAuthor_YEAR_OF_BIRTH(), (Integer) null);
        u.execute();

        Result<A> records = create()
            .selectFrom(TAuthor())
            .where(TAuthor_YEAR_OF_BIRTH().isNull())
            .fetch();
        assertEquals(2, records.size());
        assertEquals(null, records.getValue(0, TAuthor_YEAR_OF_BIRTH()));
    }

    @Test
    public void testIsTrue() throws Exception {
        assertEquals(0, create().select().where(val(null).isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("asdf").isTrue()).fetch().size());

        assertEquals(0, create().select().where(val(0).isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("false").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("n").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("no").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("0").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("disabled").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("off").isTrue()).fetch().size());

        assertEquals(1, create().select().where(val(1).isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("true").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("y").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("yes").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("1").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("enabled").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("on").isTrue()).fetch().size());

        assertEquals(0, create().select().where(val("asdf").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val(null).isFalse()).fetch().size());

        assertEquals(1, create().select().where(val(0).isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("false").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("n").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("no").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("0").isFalse()).fetch().size());

        assertEquals(1, create().select().where(val("disabled").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("off").isFalse()).fetch().size());

        assertEquals(0, create().select().where(val(1).isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("true").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("y").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("yes").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("1").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("enabled").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("on").isFalse()).fetch().size());

        // The below code throws an exception on Ingres when run once. When run
        // twice, the DB crashes... This seems to be a driver / database bug
        if (getDialect() != SQLDialect.INGRES) {
            assertEquals(0, create().select().where(val(false).isTrue()).fetch().size());
            assertEquals(1, create().select().where(val(false).isFalse()).fetch().size());
            assertEquals(1, create().select().where(val(true).isTrue()).fetch().size());
            assertEquals(0, create().select().where(val(true).isFalse()).fetch().size());
        }
    }

    @Test
    public void testLike() throws Exception {
        Field<String> notLike = TBook_PUBLISHED_IN().cast(String.class);

        // DB2 doesn't support this syntax
        if (getDialect() == DB2) {
            notLike = val("bbb");
        }

        Result<B> books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like("%a%"))
                .and(TBook_TITLE().notLike(notLike))
                .fetch();

        assertEquals(3, books.size());
    }

    @Test
    public void testDual() throws Exception {
        assertEquals(1, (int) create().selectOne().fetchOne(0, Integer.class));
        assertEquals(1, (int) create().selectOne().where(one().equal(1)).fetchOne(0, Integer.class));
    }

    @Test
    public void testWindowFunctions() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "Window function tests");
                return;
        }

        int column = 0;

        // ROW_NUMBER()
        Result<Record> result =
        create().select(TBook_ID(),
                        rowNumber().over()
                                   .partitionByOne()
                                   .orderBy(TBook_ID().desc()),
                        rowNumber().over()
                                   .partitionBy(TBook_AUTHOR_ID())
                                   .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Ordered ROW_NUMBER()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered ROW_NUMBER()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        column = 0;

        // COUNT()
        result =
        create().select(TBook_ID(),
                        count().over(),
                        count().over().partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Partitioned and ordered COUNT()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(4), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(2), result.getValue(3, column));

        column = 0;

        // RANK(), DENSE_RANK()
        result =
        create().select(TBook_ID(),
                        rank().over().orderBy(TBook_ID().desc()),
                        rank().over().partitionBy(TBook_AUTHOR_ID())
                                     .orderBy(TBook_ID().desc()),
                        denseRank().over().orderBy(TBook_ID().desc()),
                        denseRank().over().partitionBy(TBook_AUTHOR_ID())
                                          .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Ordered RANK()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered RANK()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Ordered DENSE_RANK()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered DENSE_RANK()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        switch (getDialect()) {
            case DB2:
            case SQLSERVER:
                log.info("SKIPPING", "PERCENT_RANK() and CUME_DIST() window function tests");
                break;

            default: {
                column = 0;

                // PERCENT_RANK() and CUME_DIST()
                result =
                create().select(TBook_ID(),
                                percentRank().over().orderBy(TBook_ID().desc()),
                                percentRank().over().partitionBy(TBook_AUTHOR_ID())
                                                    .orderBy(TBook_ID().desc()),
                                cumeDist().over().orderBy(TBook_ID().desc()),
                                cumeDist().over().partitionBy(TBook_AUTHOR_ID())
                                                 .orderBy(TBook_ID().desc()))
                        .from(TBook())
                        .orderBy(TBook_ID().asc())
                        .fetch();

                // Ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0.6", result.getValueAsString(1, column).substring(0, 3));
                assertEquals("0.3", result.getValueAsString(2, column).substring(0, 3));
                assertEquals("0", result.getValueAsString(3, column));

                // Partitioned and ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0", result.getValueAsString(1, column));
                assertEquals("1", result.getValueAsString(2, column));
                assertEquals("0", result.getValueAsString(3, column));

                // Ordered CUME_DIST()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0.75", result.getValueAsString(1, column));
                assertEquals("0.5", result.getValueAsString(2, column));
                assertEquals("0.25", result.getValueAsString(3, column));

                // Partitioned and ordered CUME_DIST()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0.5", result.getValueAsString(1, column));
                assertEquals("1", result.getValueAsString(2, column));
                assertEquals("0.5", result.getValueAsString(3, column));

                break;
            }
        }

        column = 0;

        // MAX()
        result =
        create().select(TBook_ID(),
                        max(TBook_ID()).over()
                                       .partitionByOne(),
                        max(TBook_ID()).over()
                                       .partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall MAX()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(4), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        // Partitioned MAX()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        column = 0;

        // STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        result =
        create().select(TBook_ID(),
                        stddevPop(TBook_ID()).over().partitionByOne(),
                        stddevSamp(TBook_ID()).over().partitionByOne(),
                        varPop(TBook_ID()).over().partitionByOne(),
                        varSamp(TBook_ID()).over().partitionByOne(),

                        stddevPop(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        stddevSamp(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        varPop(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        varSamp(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals("1.118", result.getValueAsString(0, 1).substring(0, 5));
        assertEquals(1.25, result.getValueAsDouble(0, 3));

        // Partitioned STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals(0.5, result.getValueAsDouble(0, 5));
        assertEquals(0.25, result.getValueAsDouble(0, 7));

        // DB2 only knows STDDEV_POP / VAR_POP
        if (getDialect() != SQLDialect.DB2) {
            assertEquals("1.290", result.getValueAsString(0, 2).substring(0, 5));
            assertEquals("1.666", result.getValueAsString(0, 4).substring(0, 5));
            assertEquals("0.707", result.getValueAsString(0, 6).substring(0, 5));
            assertEquals(0.5, result.getValueAsDouble(0, 8));
        }

        column = 0;
        if (getDialect() == SQLDialect.SQLSERVER) {
            log.info("SKIPPING", "ROWS UNBOUNDED PRECEDING and similar tests");
            return;
        }

        // SUM()
        result =
        create().select(TBook_ID(),
                        sum(TBook_ID()).over().partitionByOne(),
                        sum(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        sum(TBook_ID()).over().orderBy(TBook_ID().asc())
                                              .rowsBetweenUnboundedPreceding()
                                              .andPreceding(1))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall SUM()
        column++;
        assertEquals(new BigDecimal("10"), result.getValue(0, column));
        assertEquals(new BigDecimal("10"), result.getValue(1, column));
        assertEquals(new BigDecimal("10"), result.getValue(2, column));
        assertEquals(new BigDecimal("10"), result.getValue(3, column));

        // Partitioned SUM()
        column++;
        assertEquals(new BigDecimal("3"), result.getValue(0, column));
        assertEquals(new BigDecimal("3"), result.getValue(1, column));
        assertEquals(new BigDecimal("7"), result.getValue(2, column));
        assertEquals(new BigDecimal("7"), result.getValue(3, column));

        // Ordered SUM() with ROWS
        column++;
        assertEquals(null, result.getValue(0, column));
        assertEquals(new BigDecimal("1"), result.getValue(1, column));
        assertEquals(new BigDecimal("3"), result.getValue(2, column));
        assertEquals(new BigDecimal("6"), result.getValue(3, column));

        column = 0;

        // FIRST_VALUE()
        result =
        create().select(TBook_ID(),
                        firstValue(TBook_ID()).over()
                                              .partitionBy(TBook_AUTHOR_ID())
                                              .orderBy(TBook_PUBLISHED_IN().asc())
                                              .rowsBetweenUnboundedPreceding()
                                              .andUnboundedFollowing())
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Partitioned and ordered FIRST_VALUE() with ROWS
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(3), result.getValue(2, column));
        assertEquals(Integer.valueOf(3), result.getValue(3, column));

        switch (getDialect()) {
            case POSTGRES:
                log.info("SKIPPING", "FIRST_VALUE(... IGNORE NULLS) window function test");
                break;

            default: {
                column = 0;

                // FIRST_VALUE(... IGNORE NULLS)
                result = create().select(TBook_ID(),
                                         firstValue(TBook_ID()).ignoreNulls()
                                                               .over()
                                                               .partitionBy(TBook_AUTHOR_ID())
                                                               .orderBy(TBook_PUBLISHED_IN().asc())
                                                               .rowsBetweenUnboundedPreceding()
                                                               .andUnboundedFollowing())
                                 .from(TBook())
                                 .orderBy(TBook_ID().asc())
                                 .fetch();

                // Partitioned and ordered FIRST_VALUE(... IGNORE NULLS) with ROWS
                column++;
                assertEquals(Integer.valueOf(2), result.getValue(0, column));
                assertEquals(Integer.valueOf(2), result.getValue(1, column));
                assertEquals(Integer.valueOf(3), result.getValue(2, column));
                assertEquals(Integer.valueOf(3), result.getValue(3, column));

                break;
            }
        }

        switch (getDialect()) {
            case SYBASE:
                log.info("SKIPPING", "LEAD/LAG tests");
                break;

            default: {
                column = 0;

                // LEAD() and LAG()
                result =
                create().select(TBook_ID(),
                                lead(TBook_ID()).over()
                                                .partitionByOne()
                                                .orderBy(TBook_ID().asc()),
                                lead(TBook_ID()).over()
                                                .partitionBy(TBook_AUTHOR_ID())
                                                .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2).over()
                                                   .partitionByOne()
                                                   .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2).over()
                                                   .partitionBy(TBook_AUTHOR_ID())
                                                   .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2, 55).over()
                                                       .partitionByOne()
                                                       .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2, 55).over()
                                                       .partitionBy(TBook_AUTHOR_ID())
                                                       .orderBy(TBook_ID().asc()),

                                lag(TBook_ID()).over()
                                               .partitionByOne()
                                               .orderBy(TBook_ID().asc()),
                                lag(TBook_ID()).over()
                                               .partitionBy(TBook_AUTHOR_ID())
                                               .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2).over()
                                                  .partitionByOne()
                                                  .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2).over()
                                                  .partitionBy(TBook_AUTHOR_ID())
                                                  .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2, val(55)).over()
                                                           .partitionByOne()
                                                           .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2, val(55)).over()
                                                           .partitionBy(TBook_AUTHOR_ID())
                                                           .orderBy(TBook_ID().asc()))
                        .from(TBook())
                        .orderBy(TBook_ID().asc())
                        .fetch();

                // Overall LEAD()
                column++;
                assertEquals(2, result.getValue(0, column));
                assertEquals(3, result.getValue(1, column));
                assertEquals(4, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Partitioned LEAD()
                column++;
                assertEquals(2, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(4, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LEAD(2)
                column++;
                assertEquals(3, result.getValue(0, column));
                assertEquals(4, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Partitioned LEAD(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LEAD(2, 55)
                column++;
                assertEquals(3, result.getValue(0, column));
                assertEquals(4, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));

                // Partitioned LEAD(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));


                // Overall LAG()
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(1, result.getValue(1, column));
                assertEquals(2, result.getValue(2, column));
                assertEquals(3, result.getValue(3, column));

                // Partitioned LAG()
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(1, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(3, result.getValue(3, column));

                // Overall LAG(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(1, result.getValue(2, column));
                assertEquals(2, result.getValue(3, column));

                // Partitioned LAG(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LAG(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(1, result.getValue(2, column));
                assertEquals(2, result.getValue(3, column));

                // Partitioned LAG(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));

                break;
            }
        }
    }

    @Test
    public void testPackage() throws Exception {
        if (cLibrary() == null) {
            log.info("SKIPPING", "packages test");
            return;
        }

        reset = false;

        assertEquals("1", "" + invoke(cLibrary(), "pkgPAuthorExists1", create(), "Paulo"));
        assertEquals("0", "" + invoke(cLibrary(), "pkgPAuthorExists1", create(), "Shakespeare"));
        assertEquals("1", "" + invoke(cLibrary(), "pkgFAuthorExists1", create(), "Paulo"));
        assertEquals("0", "" + invoke(cLibrary(), "pkgFAuthorExists1", create(), "Shakespeare"));
    }

    @Test
    public void testStoredProcedure() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "procedure test");
            return;
        }

        reset = false;

        // P_AUTHOR_EXISTS
        // ---------------------------------------------------------------------
        if (supportsOUTParameters()) {
            assertEquals("0", "" + invoke(cRoutines(), "pAuthorExists", create(), null, DUMMY_OUT_INT));
            assertEquals("1", "" + invoke(cRoutines(), "pAuthorExists", create(), "Paulo", DUMMY_OUT_INT));
            assertEquals("0", "" + invoke(cRoutines(), "pAuthorExists", create(), "Shakespeare", DUMMY_OUT_INT));
        } else {
            log.info("SKIPPING", "procedure test for OUT parameters");
        }

        // P_CREATE_AUTHOR_*
        // ---------------------------------------------------------------------
        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("William")));
        invoke(cRoutines(), "pCreateAuthor", create());
        assertEquals("Shakespeare", create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("William")).getValue(TAuthor_LAST_NAME()));

        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("Hermann")));
        invoke(cRoutines(), "pCreateAuthorByName", create(), "Hermann", "Hesse");
        assertEquals("Hesse", create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("Hermann")).getValue(TAuthor_LAST_NAME()));

        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_LAST_NAME().equal("Kaestner")));
        invoke(cRoutines(), "pCreateAuthorByName", create(), null, "Kaestner");
        assertEquals("Kaestner", create().fetchOne(
            TAuthor(),
            TAuthor_LAST_NAME().equal("Kaestner")).getValue(TAuthor_LAST_NAME()));

        // P391, a test for properly binding and treating various IN, OUT, INOUT
        // parameters
        // ---------------------------------------------------------------------
        if (supportsOUTParameters()) {

            // TODO: [#396] MySQL seems to have a bug when passing null to IN/OUT
            // parameters. Check back on this, when this is fixed.
            if (getDialect() != SQLDialect.MYSQL) {
                Object p391a = invoke(cRoutines(), "p391", create(), null, null, DUMMY_OUT_INT, DUMMY_OUT_INT, null, null);
                assertEquals(null, invoke(p391a, "getIo1"));
                assertEquals(null, invoke(p391a, "getO1"));
                assertEquals(null, invoke(p391a, "getIo2"));
                assertEquals(null, invoke(p391a, "getO2"));
            }

            // TODO: [#459] Sybase messes up IN/OUT parameter orders.
            // Check back on this, when this is fixed.
            if (getDialect() != SQLDialect.SYBASE) {
                Object p391b = invoke(cRoutines(), "p391", create(), null, 2, DUMMY_OUT_INT, DUMMY_OUT_INT, 3, null);
                assertEquals(null, invoke(p391b, "getIo1"));
                assertEquals("2", "" + invoke(p391b, "getO1"));
                assertEquals(null, invoke(p391b, "getIo2"));
                assertEquals("3", "" + invoke(p391b, "getO2"));

                Object p391c = invoke(cRoutines(), "p391", create(), 1, 2, DUMMY_OUT_INT, DUMMY_OUT_INT, 3, 4);
                assertEquals("1", "" + invoke(p391c, "getIo1"));
                assertEquals("2", "" + invoke(p391c, "getO1"));
                assertEquals("4", "" + invoke(p391c, "getIo2"));
                assertEquals("3", "" + invoke(p391c, "getO2"));
            }
        }

        // F378, which is a stored function with OUT parameters
        // ---------------------------------------------------------------------
        switch (getDialect()) {

            // Currently, this is only supported for oracle
            case ORACLE:
                Object result1a = invoke(cRoutines(), "f378", create(), null, null);
                assertEquals(null, invoke(result1a, "getIo"));
                assertEquals(null, invoke(result1a, "getO"));
                assertEquals(null, invoke(result1a, "getReturnValue"));

                Object result2a = invoke(cRoutines(), "f378", create(), null, 2);
                assertEquals(null, invoke(result2a, "getIo"));
                assertEquals("2", "" + invoke(result2a, "getO"));
                assertEquals(null, invoke(result2a, "getReturnValue"));

                Object result3a = invoke(cRoutines(), "f378", create(), 1, 2);
                assertEquals("1", "" + invoke(result3a, "getIo"));
                assertEquals("2", "" + invoke(result3a, "getO"));
                assertEquals("3", "" + invoke(result3a, "getReturnValue"));

                Object result1b = invoke(cLibrary(), "pkgF378", create(), null, null);
                assertEquals(null, invoke(result1b, "getIo"));
                assertEquals(null, invoke(result1b, "getO"));
                assertEquals(null, invoke(result1b, "getReturnValue"));

                Object result2b = invoke(cLibrary(), "pkgF378", create(), null, 2);
                assertEquals(null, invoke(result2b, "getIo"));
                assertEquals("2", "" + invoke(result2b, "getO"));
                assertEquals(null, invoke(result2b, "getReturnValue"));

                Object result3b = invoke(cLibrary(), "pkgF378", create(), 1, 2);
                assertEquals("1", "" + invoke(result3b, "getIo"));
                assertEquals("2", "" + invoke(result3b, "getO"));
                assertEquals("3", "" + invoke(result3b, "getReturnValue"));
                break;
        }
    }

    @Test
    public void testOracleHints() throws Exception {
        if (getDialect() != SQLDialect.ORACLE) {
            log.info("SKIPPING", "Oracle hint tests");
            return;
        }

        assertEquals(1, create().selectOne().hint("/*+ALL_ROWS*/").fetchOne(0));
        assertEquals(1, create().select(val(1)).hint("/*+ALL_ROWS*/").fetchOne(0));
        assertEquals(1, create().selectDistinct(val(1)).hint("/*+ALL_ROWS*/").fetchOne(0));
    }

    @Test
    public void testArrayTables() throws Exception {
        if (TArrays_NUMBER_R() != null) {
            Result<Record> result;

            // An empty array
            // --------------
            ArrayRecord<Integer> array = newNUMBER_R();
            result = create().select().from(table(array)).fetch();

            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());
            // [#523] TODO use ArrayRecord meta data instead
//            assertEquals(array.getDataType(), result.getField(0).getDataType());

            // An array containing null
            // ------------------------
            array.set((Integer) null);
            result = create().select().from(table(array)).fetch();

            assertEquals(1, result.size());
            assertEquals(1, result.getFields().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));

            // An array containing two values
            // ------------------------------
            array.set((Integer) null, 1);
            result = create().select().from(table(array)).fetch();

            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            // An array containing three values
            // --------------------------------
            array.set((Integer) null, 1, 2);
            result = create().select().from(table(array)).fetch();

            assertEquals(3, result.size());
            assertEquals(1, result.getFields().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals("2", "" + result.getValue(2, 0));

            // Joining an unnested array table
            // -------------------------------
            array.set(2, 3);
            Table<?> table = table(array);
            result = create()
                .select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .join(table)
                .on(table.getField(0).cast(Integer.class).equal(TBook_ID()))
                .orderBy(TBook_ID())
                .fetch();

            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
            assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
            assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
            assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

            // Joining an aliased unnested array table
            // ---------------------------------------
            result = create()
                .select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .join(table.as("t"))
                .on(table.as("t").getField(0).cast(Integer.class).equal(TBook_ID()))
                .orderBy(TBook_ID())
                .fetch();

            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
            assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
            assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
            assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

            // Functions returning arrays
            // --------------------------
            result = create().select().from(table(FArrays1Field_R(null))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            array = newNUMBER_R();
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            array.set(null, 1);
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            array.set(null, 1, null, 2);
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(4, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals(null, result.getValue(2, 0));
            assertEquals("2", "" + result.getValue(3, 0));
        }
        else if (TArrays_NUMBER() != null) {
            Result<Record> result;

            // An empty array
            // --------------
            Integer[] array = new Integer[0];
            result = create().select().from(table(new Integer[0])).fetch();

            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            // An array containing null
            // ------------------------
            array = new Integer[] { null };
            result = create().select().from(table(array)).fetch();

            assertEquals(1, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));

            // An array containing two values
            // ------------------------------
            array = new Integer[] { null, 1 };
            result = create().select().from(table(array)).fetch();

            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals(1, result.getValue(1, 0));

            // An array containing three values
            // --------------------------------
            array = new Integer[] { null, 1, 2 };
            result = create().select().from(table(array)).fetch();

            assertEquals(3, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals(1, result.getValue(1, 0));
            assertEquals(2, result.getValue(2, 0));

            // Joining an unnested array table
            // -------------------------------
            array = new Integer[] { 2, 3 };
            Table<?> table = table(array);
            result = create()
                .select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .join(table)
                .on(table.getField(0).cast(Integer.class).equal(TBook_ID()))
                .fetch();

            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
            assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
            assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
            assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

            // Joining an aliased unnested array table
            // ---------------------------------------
            result = create()
                .select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .join(table.as("t"))
                .on(table.as("t").getField(0).cast(Integer.class).equal(TBook_ID()))
                .fetch();

            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
            assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
            assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
            assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

            // Cross join the array table with the unnested string array value
            // ---------------------------------------------------------------

            switch (getDialect()) {
                case POSTGRES:
                case H2:
                    log.info("SKIPPING", "Cross join of table with unnested array is not supported");
                    break;

                default:
                    table = table(TArrays_STRING()).as("t");
                    result = create()
                        .select(TArrays_ID(), table.getField(0))
                        .from(TArrays(), table)
                        .orderBy(TArrays_ID())
                        .fetch();

                    assertEquals(3, result.size());
                    assertEquals(Integer.valueOf(3), result.getValue(0, TArrays_ID()));
                    assertEquals(Integer.valueOf(4), result.getValue(1, TArrays_ID()));
                    assertEquals(Integer.valueOf(4), result.getValue(2, TArrays_ID()));

                    assertEquals("a", result.getValue(0, 1));
                    assertEquals("a", result.getValue(1, 1));
                    assertEquals("b", result.getValue(2, 1));
            }


            // Functions returning arrays
            // --------------------------
            result = create().select().from(table(FArrays1Field(null))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            array = new Integer[0];
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            array = new Integer[] { null, 1 };
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            array = new Integer[] { null, 1, null, 2 };
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(4, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals(null, result.getValue(2, 0));
            assertEquals("2", "" + result.getValue(3, 0));
        }
        else {
            log.info("SKIPPING", "ARRAY TABLE tests");
        }
    }

    @Test
    public void testStoredProceduresWithCursorParameters() throws Exception {
        switch (getDialect()) {
            case H2:
            case HSQLDB:
            case ORACLE:
            case POSTGRES:
                break;

            default:
                log.info("SKIPPING", "Stored procedures tests with CURSOR type parameters");
                return;
        }

        // ---------------------------------------------------------------------
        // The one cursor function
        // ---------------------------------------------------------------------
        {
            Object integerArray = null;

            // Get an empty cursor
            // -------------------
            Result<Record> bFromCursor = invoke(cRoutines(), "fGetOneCursor", create(), integerArray);

            assertNotNull(bFromCursor);
            assertTrue(bFromCursor.isEmpty());
            assertEquals(0, bFromCursor.size());

            // Get a filled cursor
            // -------------------
            if (TArrays_STRING_R() != null) {
                ArrayRecord<Integer> i = newNUMBER_R();
                i.set(1, 2, 4, 6);
                integerArray = i;
            }
            else if (TArrays_STRING() != null) {
                integerArray = new Integer[] { 1, 2, 4, 6 };
            }

            bFromCursor = invoke(cRoutines(), "fGetOneCursor", create(), integerArray);

            Result<B> bFromTable = create()
                .selectFrom(TBook())
                .where(TBook_ID().in(1, 2, 4))
                .orderBy(TBook_ID()).fetch();

            assertNotNull(bFromCursor);
            assertFalse(bFromCursor.isEmpty());
            assertEquals(3, bFromCursor.size());

            compareBookResults(bFromCursor, bFromTable);
        }

        // ---------------------------------------------------------------------
        // The one cursor function used in SQL
        // ---------------------------------------------------------------------
        {

            // Get an empty cursor
            // -------------------
            Field<Result<Record>> field = FGetOneCursorField(null);
            Result<Record> bFromCursor;

            switch (getDialect()) {
                case HSQLDB:
                    bFromCursor = create().select().from(table(field)).fetch();
                    break;

                default:
                    bFromCursor = create().select(field).fetchOne(field);
                    break;
            }

            assertNotNull(bFromCursor);
            assertTrue(bFromCursor.isEmpty());
            assertEquals(0, bFromCursor.size());

            // Get a filled cursor
            // -------------------
            field = FGetOneCursorField(new Integer[] { 1, 2, 4, 6 });

            switch (getDialect()) {
                case HSQLDB:
                    bFromCursor = create().select().from(table(field)).fetch();
                    break;

                default:
                    bFromCursor = create().select(field).fetchOne(field);
                    break;
            }

            Result<B> bFromTable = create()
                .selectFrom(TBook())
                .where(TBook_ID().in(1, 2, 4))
                .orderBy(TBook_ID()).fetch();

            assertNotNull(bFromCursor);
            assertFalse(bFromCursor.isEmpty());
            assertEquals(3, bFromCursor.size());

            compareBookResults(bFromCursor, bFromTable);
        }

        if (getDialect() == SQLDialect.HSQLDB) {
            log.info("SKIPPING", "Cursor OUT parameter tests");
            return;
        }

        // ---------------------------------------------------------------------
        // The one cursor procedure
        // ---------------------------------------------------------------------
        if (supportsOUTParameters()) {
            Object integerArray = null;

            // Get an empty cursor
            // -------------------
            Object result = invoke(cRoutines(), "pGetOneCursor", create(), integerArray);

            assertNotNull(result);
            assertEquals("0", "" + invoke(result, "getTotal"));

            Result<Record> bFromCursor = invoke(result, "getBooks");
            assertTrue(bFromCursor.isEmpty());
            assertEquals(0, bFromCursor.size());

            // Get a filled cursor
            // -------------------
            if (TArrays_STRING_R() != null) {
                ArrayRecord<Integer> i = newNUMBER_R();
                i.set(1, 2, 4, 6);
                integerArray = i;
            }
            else if (TArrays_STRING() != null) {
                integerArray = new Integer[] { 1, 2, 4, 6 };
            }

            result = invoke(cRoutines(), "pGetOneCursor", create(), integerArray);

            assertEquals("3", "" + invoke(result, "getTotal"));
            bFromCursor = invoke(result, "getBooks");

            Result<B> bFromTable = create()
                .selectFrom(TBook())
                .where(TBook_ID().in(1, 2, 4))
                .orderBy(TBook_ID()).fetch();

            assertNotNull(bFromCursor);
            assertFalse(bFromCursor.isEmpty());
            assertEquals(3, bFromCursor.size());

            compareBookResults(bFromCursor, bFromTable);
        }
        else {
            log.info("SKIPPING", "One cursor OUT parameter test");
        }

        // ---------------------------------------------------------------------
        // The two cursor procedure
        // ---------------------------------------------------------------------
        if (getDialect() == SQLDialect.POSTGRES) {

            // TODO [#707] This fails for Postgres, as UDT's are not correctly
            // deserialised
            log.info("SKIPPING", "UDT/Enum types returned in refcursor (see [#707])");
        }
        else if (supportsOUTParameters()) {
            Object result = invoke(cRoutines(), "pGetTwoCursors", create());
            assertNotNull(result);

            Result<A> aFromTable = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
            Result<B> bFromTable = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();

            Result<Record> aFromCursor = invoke(result, "getAuthors");
            Result<Record> bFromCursor = invoke(result, "getBooks");
            assertNotNull(aFromCursor);
            assertNotNull(bFromCursor);
            assertEquals(2, aFromCursor.size());
            assertEquals(4, bFromCursor.size());
            assertFalse(aFromCursor.isEmpty());
            assertFalse(bFromCursor.isEmpty());

            compareAuthorResults(aFromCursor, aFromTable);
            compareBookResults(bFromCursor, bFromTable);
        }
        else {
            log.info("SKIPPING", "Two cursor OUT parameter test");
        }
    }

    private void compareAuthorResults(Result<Record> aFromCursor, Result<A> aFromTable) {
        assertEquals(aFromTable.getValues(TAuthor_ID()), aFromCursor.getValues(TAuthor_ID()));
        assertEquals(aFromTable.getValues(TAuthor_ID().getName()), aFromCursor.getValues(TAuthor_ID().getName()));
        assertEquals(aFromTable.getValues(0), aFromCursor.getValues(0));

        assertEquals(aFromTable.getValues(TAuthor_FIRST_NAME()), aFromCursor.getValues(TAuthor_FIRST_NAME()));
        assertEquals(aFromTable.getValues(TAuthor_LAST_NAME()), aFromCursor.getValues(TAuthor_LAST_NAME()));
        assertEquals(aFromTable.getValues(TAuthor_YEAR_OF_BIRTH()), aFromCursor.getValues(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(aFromTable.getValues(TAuthor_DATE_OF_BIRTH()), aFromCursor.getValues(TAuthor_DATE_OF_BIRTH()));
        assertEquals(aFromTable.getValues(TAuthor_ADDRESS()), aFromCursor.getValues(TAuthor_ADDRESS()));
    }

    private void compareBookResults(Result<Record> bFromCursor, Result<B> bFromTable) {
        assertEquals(bFromTable.getValues(TBook_ID()), bFromCursor.getValues(TBook_ID()));
        assertEquals(bFromTable.getValues(TBook_ID().getName()), bFromCursor.getValues(TBook_ID().getName()));
        assertEquals(bFromTable.getValues(0), bFromCursor.getValues(0));

        assertEquals(bFromTable.getValues(TBook_AUTHOR_ID()), bFromCursor.getValues(TBook_AUTHOR_ID()));
        assertEquals(bFromTable.getValues(TBook_CONTENT_PDF()), bFromCursor.getValues(TBook_CONTENT_PDF()));
        assertEquals(bFromTable.getValues(TBook_CONTENT_TEXT()), bFromCursor.getValues(TBook_CONTENT_TEXT()));
        assertEquals(bFromTable.getValues(TBook_TITLE()), bFromCursor.getValues(TBook_TITLE()));
    }

    @Test
    public void testEquals() throws Exception {

        // Record.equals()
        // ---------------
        assertEquals(create().selectFrom(TBook()).fetchAny(),
                     create().selectFrom(TBook()).fetchAny());
        assertEquals(create().selectFrom   (TBook()).fetchAny(),
                     create().select().from(TBook()).fetchAny());

        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny(),
                     create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny());
        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny(),
                     create().select(TBook_ID(), trim(TBook_TITLE())).from(TBook()).fetchAny());

        assertFalse(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny().equals(
                    create().select(TBook_TITLE(), TBook_ID()).from(TBook()).fetchAny()));

        // Result.equals()
        // ---------------
        assertEquals(create().selectFrom(TBook()).fetch(),
                     create().selectFrom(TBook()).fetch());
        assertEquals(create().selectFrom   (TBook()).fetch(),
                     create().select().from(TBook()).fetch());
        assertEquals(create().selectFrom   (TBook()).limit(1).fetch(),
                     create().select().from(TBook()).limit(1).fetch());

        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch(),
                     create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch());
        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch(),
                     create().select(TBook_ID(), trim(TBook_TITLE())).from(TBook()).fetch());

        assertFalse(create().selectFrom(TBook()).orderBy(TBook_ID().asc()).fetch().equals(
                    create().selectFrom(TBook()).orderBy(TBook_ID().desc()).fetch()));

        assertFalse(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch().equals(
                    create().select(TBook_TITLE(), TBook_ID()).from(TBook()).fetch()));
    }

    @Test
    public void testBatchSingle() throws Exception {
        reset = false;

        int[] result = create().batch(create().insertInto(TAuthor())
                                              .set(TAuthor_ID(), 8)
                                              .set(TAuthor_LAST_NAME(), "Gamma"))
                               .bind(8, "Gamma")
                               .bind(9, "Helm")
                               .bind(10, "Johnson")
                               .execute();

        assertEquals(3, result.length);
        testBatchAuthors();
    }

    @Test
    public void testBatchMultiple() throws Exception {
        reset = false;

        int[] result = create().batch(
            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 8)
                    .set(TAuthor_LAST_NAME(), "Gamma"),

            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 9)
                    .set(TAuthor_LAST_NAME(), "Helm"),

            create().insertInto(TBook())
                    .set(TBook_ID(), 6)
                    .set(TBook_AUTHOR_ID(), 8)
                    .set(TBook_PUBLISHED_IN(), 1994)
                    .set((Field<Integer>)TBook_LANGUAGE_ID(), 1)
                    .set(TBook_CONTENT_TEXT(), "Design Patterns are awesome")
                    .set(TBook_TITLE(), "Design Patterns"),

            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 10)
                    .set(TAuthor_LAST_NAME(), "Johnson")).execute();

        assertEquals(4, result.length);
        assertEquals(5, create().fetch(TBook()).size());
        assertEquals(1, create().fetch(TBook(), TBook_AUTHOR_ID().equal(8)).size());
        testBatchAuthors();
    }

    private void testBatchAuthors() throws Exception {
        assertEquals(5, create().fetch(TAuthor()).size());

        assertEquals(Arrays.asList(8, 9, 10),
             create().select(TAuthor_ID())
                     .from(TAuthor())
                     .where(TAuthor_ID().in(8, 9, 10))
                     .orderBy(TAuthor_ID())
                     .fetch(TAuthor_ID()));

        assertEquals(Arrays.asList("Gamma", "Helm", "Johnson"),
            create().select(TAuthor_LAST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().in(8, 9, 10))
                    .orderBy(TAuthor_ID())
                    .fetch(TAuthor_LAST_NAME()));
    }

    @Test
    public void testNamedParams() throws Exception {
        Select<?> select =
        create().select(
                    TAuthor_ID(),
                    param("p1", String.class))
                .from(TAuthor())
                .where(TAuthor_ID().in(
                    param("p2", Integer.class),
                    param("p3", Integer.class)))
                .orderBy(TAuthor_ID().asc());

        // Should execute fine, but no results due to IN (null, null) filter
        assertEquals(0, select.fetch().size());

        // Set both parameters to the same value
        select.getParam("p2").setConverted(1L);
        select.getParam("p3").setConverted("1");
        Result<?> result1 = select.fetch();
        assertEquals(1, result1.size());
        assertEquals(1, result1.getValue(0, 0));
        assertNull(result1.getValue(0, 1));

        // Set more parameters
        select.getParam("p1").setConverted("asdf");
        select.getParam("p3").setConverted("2");
        Result<?> result2 = select.fetch();
        assertEquals(2, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(1, 0));
        assertEquals("asdf", result2.getValue(0, 1));
        assertEquals("asdf", result2.getValue(1, 1));
    }

    @Test
    public void testUnknownBindTypes() throws Exception {

        // [#1028] [#1029] Named params without any associated type information
        Select<?> select = create().select(
            param("p1"),
            param("p2"));

        select.bind(1, "10");
        select.bind(2, null);
        Result<?> result3 = select.fetch();

        assertEquals(1, result3.size());
        assertEquals("10", result3.getValue(0, 0));
        assertEquals(null, result3.getValue(0, 1));
    }

    @Test
    public void testQueryBindValues() throws Exception {
        Select<?> select =
        create().select(
                    TAuthor_ID(),
                    param("p1", String.class))
                .from(TAuthor())
                .where(TAuthor_ID().in(
                    param("p2", Integer.class),
                    param("p3", Integer.class)))
                .orderBy(TAuthor_ID().asc());

        // Should execute fine, but no results due to IN (null, null) filter
        assertEquals(0, select.fetch().size());

        // Set both condition parameters to the same value
        Result<?> result1 =
        select.bind("p2", 1L)
              .bind(3, "1")
              .fetch();
        assertEquals(1, result1.size());
        assertEquals(1, result1.getValue(0, 0));
        assertNull(result1.getValue(0, 1));

        // Set selection parameter, too
        Result<?> result2 =
        select.bind(1, "asdf")
              .bind("p3", "2")
              .fetch();
        assertEquals(2, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(1, 0));
        assertEquals("asdf", result2.getValue(0, 1));
        assertEquals("asdf", result2.getValue(1, 1));
    }

    @Test
    public void testQueryBindValuesWithPlainSQL() throws Exception {
        Select<?> select =
        create().select(TAuthor_ID())
                .from(TAuthor())
                .where(TAuthor_ID().in(

                    // [#724] Check for API misuse
                    field("?", Integer.class, (Object[]) null),
                    field("?", Integer.class, (Object[]) null)))
                .and(TAuthor_ID().getName() + " != ? or 'abc' = '???'", 37)
                .orderBy(TAuthor_ID().asc());

        // Should execute fine, but no results due to IN (null, null) filter
        assertEquals(0, select.fetch().size());

        // Set both parameters to the same value
        Result<?> result1 =
        select.bind(1, 1L)
              .bind(2, 1)
              .fetch();
        assertEquals(1, result1.size());
        assertEquals(1, result1.getValue(0, 0));

        // Set selection parameter, too
        Result<?> result2 =
        select.bind(2, 2)
              .fetch();
        assertEquals(2, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(1, 0));
    }

    @Test
    public void testPivotClause() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "PIVOT clause tests");
                return;
        }

        // Simple pivoting, no aliasing
        // ----------------------------

        Result<Record> result1 =
        create().select()
                .from(TBookToBookStore()
                .pivot(count())
                .on(TBookToBookStore_BOOK_STORE_NAME())
                .in("Orell Füssli",
                    "Ex Libris",
                    "Buchhandlung im Volkshaus"))
                .orderBy(
                    literal(1).asc(),
                    literal(2).asc())
                .fetch();

        assertEquals(6, result1.size());
        assertEquals(TBookToBookStore_BOOK_ID().getName(), result1.getField(0).getName());
        assertEquals(TBookToBookStore_STOCK().getName(), result1.getField(1).getName());
        assertTrue(result1.getField(2).getName().contains("Orell Füssli"));
        assertTrue(result1.getField(3).getName().contains("Ex Libris"));
        assertTrue(result1.getField(4).getName().contains("Buchhandlung im Volkshaus"));
        assertEquals(
            asList(1, 1, 0, 1, 0),
            asList(result1.get(0).into(Integer[].class)));
        assertEquals(
            asList(1, 10, 1, 0, 0),
            asList(result1.get(1).into(Integer[].class)));
        assertEquals(
            asList(2, 10, 1, 0, 0),
            asList(result1.get(2).into(Integer[].class)));
        assertEquals(
            asList(3, 1, 0, 0, 1),
            asList(result1.get(3).into(Integer[].class)));
        assertEquals(
            asList(3, 2, 0, 1, 0),
            asList(result1.get(4).into(Integer[].class)));
        assertEquals(
            asList(3, 10, 1, 0, 0),
            asList(result1.get(5).into(Integer[].class)));

        // Pivoting with plenty of aliasing and several aggregate functions
        // ----------------------------------------------------------------

        Result<Record> result2 =
        create().select()
                .from(TBookToBookStore()
                .pivot(avg(TBookToBookStore_STOCK()).as("AVG"),
                       max(TBookToBookStore_STOCK()).as("MAX"),
                       sum(TBookToBookStore_STOCK()).as("SUM"),
                       count(TBookToBookStore_STOCK()).as("CNT"))
                .on(TBookToBookStore_BOOK_STORE_NAME())
                .in(val("Orell Füssli").as("BS1"),
                    val("Ex Libris").as("BS2"),
                    val("Buchhandlung im Volkshaus").as("BS3")))
                .orderBy(val(1).asc())
                .fetch();

        assertEquals(3, result2.size());
        assertEquals(TBookToBookStore_BOOK_ID().getName(), result2.getField(0).getName());
        assertEquals("BS1_AVG", result2.getField(1).getName());
        assertEquals("BS1_MAX", result2.getField(2).getName());
        assertEquals("BS1_SUM", result2.getField(3).getName());
        assertEquals("BS1_CNT", result2.getField(4).getName());
        assertEquals("BS2_AVG", result2.getField(5).getName());
        assertEquals("BS2_MAX", result2.getField(6).getName());
        assertEquals("BS2_SUM", result2.getField(7).getName());
        assertEquals("BS2_CNT", result2.getField(8).getName());
        assertEquals("BS3_AVG", result2.getField(9).getName());
        assertEquals("BS3_MAX", result2.getField(10).getName());
        assertEquals("BS3_SUM", result2.getField(11).getName());
        assertEquals("BS3_CNT", result2.getField(12).getName());
        assertEquals(
            asList(1,
                   10, 10, 10, 1,
                   1, 1, 1, 1,
                   null, null, null, 0),
            asList(result2.get(0).into(Integer[].class)));
        assertEquals(
            asList(2,
                   10, 10, 10, 1,
                   null, null, null, 0,
                   null, null, null, 0),
            asList(result2.get(1).into(Integer[].class)));
        assertEquals(
            asList(3,
                   10, 10, 10, 1,
                   2, 2, 2, 1,
                   1, 1, 1, 1),
            asList(result2.get(2).into(Integer[].class)));


        // Check aliasing of fields in source table
        Field<Integer> lang = TBook_LANGUAGE_ID().cast(Integer.class).as("lang");
        Result<Record> result3 =
        create().select()
                .from(table(create().select(TBook_AUTHOR_ID(), lang)
                                    .from(TBook()))
                .pivot(count())
                .on(lang)
                .in(1, 2, 3, 4))
                .fetch();

        assertEquals(2, result3.size());
        assertEquals(5, result3.getFields().size());
        assertEquals(AUTHOR_IDS, result3.getValues(0));
        assertEquals(
            asList(1, 2, 0, 0, 0),
            asList(result3.get(0).into(Integer[].class)));
    }

    @Test
    public void testLoader() throws Exception {
        reset = false;
        connection.setAutoCommit(false);

        Field<Integer> count = count();

        // Empty CSV file
        // --------------
        Loader<A> loader =
        create().loadInto(TAuthor())
                .loadCSV("")
                .fields(TAuthor_ID())
                .execute();

        assertEquals(0, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(0, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Loading is aborted
        // ---------------------------------------------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "3\n" +
                    "4")
                .fields(TAuthor_ID())
                .ignoreRows(0)
                .execute();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(1, loader.processed());
        assertEquals(1, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Errors are ignored
        // ---------------------------------------------
        loader =
        create().loadInto(TAuthor())
                .onErrorIgnore()
                .loadCSV(
                    "3\n" +
                    "4")
                .fields(TAuthor_ID())
                .ignoreRows(0)
                .execute();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertNotNull(loader.errors().get(1));
        assertEquals(0, loader.stored());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (Duplicate records)
        // Loading is aborted
        // -----------------------------------------
        loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadCSV(
                    "1;'Kafka'\n" +
                    "2;Frisch")
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .quote('\'')
                .separator(';')
                .ignoreRows(0)
                .execute();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(1, loader.processed());
        assertEquals(1, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (Duplicate records)
        // Errors are ignored
        // -----------------------------------------
        loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadCSV(
                    "1,\"Kafka\"\n" +
                    "2,Frisch")
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .ignoreRows(0)
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Two records
        // -----------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "####Some Data####\n" +
                    "\"ID\",\"Last Name\"\r" +
                    "3,Hesse\n" +
                    "4,Frisch")
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .quote('"')
                .separator(',')
                .ignoreRows(2)
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(2, (int) create().select(count)
                                      .from(TAuthor())
                                      .where(TAuthor_ID().in(3, 4))
                                      .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                                      .fetchOne(count));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(3, 4)).execute());

        // Two records but don't load one column
        // -------------------------------------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "\"ID\",\"First Name\",\"Last Name\"\r" +
                    "5,Hermann,Hesse\n" +
                    "6,\"Max\",Frisch")
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());

        Result<A> result =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().in(5, 6))
                .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals(5, (int) result.getValue(0, TAuthor_ID()));
        assertEquals(6, (int) result.getValue(1, TAuthor_ID()));
        assertEquals("Hesse", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Frisch", result.getValue(1, TAuthor_LAST_NAME()));
        assertEquals(null, result.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals(null, result.getValue(1, TAuthor_FIRST_NAME()));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(5, 6)).execute());

        // Update duplicate records
        // ------------------------
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case H2:
            case INGRES:
            case POSTGRES:
            case SQLITE:
                // TODO [#558] Simulate this
                log.info("SKIPPING", "Duplicate record insertion");
                break;

            default: {
                loader =
                create().loadInto(TAuthor())
                        .onDuplicateKeyUpdate()
                        .loadCSV(
                            "\"ID\",\"First Name\",\"Last Name\"\r" +
                            "1,Hermann,Hesse\n" +
                            "7,\"Max\",Frisch")
                        .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                        .execute();

                assertEquals(2, loader.processed());
                assertEquals(2, loader.stored());
                assertEquals(0, loader.ignored());
                assertEquals(0, loader.errors().size());

                result =
                create().selectFrom(TAuthor())
                        .where(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                        .orderBy(TAuthor_ID())
                        .fetch();

                assertEquals(2, result.size());
                assertEquals(1, (int) result.getValue(0, TAuthor_ID()));
                assertEquals(7, (int) result.getValue(1, TAuthor_ID()));
                assertEquals("Hesse", result.getValue(0, TAuthor_LAST_NAME()));
                assertEquals("Frisch", result.getValue(1, TAuthor_LAST_NAME()));
                assertEquals("George", result.getValue(0, TAuthor_FIRST_NAME()));
                assertEquals(null, result.getValue(1, TAuthor_FIRST_NAME()));

                assertEquals(1, create().delete(TAuthor()).where(TAuthor_ID().in(7)).execute());
            }
        }

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        // Rollback on duplicate keys
        // --------------------------
        loader =
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadCSV(
                    "\"ID\",\"First Name\",\"Last Name\"\r" +
                    "8,Hermann,Hesse\n" +
                    "1,\"Max\",Frisch\n" +
                    "2,Friedrich,Dürrenmatt")
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(1, loader.errors().size());
        assertEquals(1, loader.errors().get(0).rowIndex());
        assertEquals(
            Arrays.asList("1", "Max", "Frisch"),
            Arrays.asList(loader.errors().get(0).row()));

        result =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().in(8))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(0, result.size());

        // Commit and ignore duplicates
        // ----------------------------
        loader =
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadCSV(
                    "\"ID\",\"First Name\",\"Last Name\"\r" +
                    "8,Hermann,Hesse\n" +
                    "1,\"Max\",Frisch\n" +
                    "2,Friedrich,Dürrenmatt")
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();

        assertEquals(3, loader.processed());
        assertEquals(1, loader.stored());
        assertEquals(2, loader.ignored());
        assertEquals(0, loader.errors().size());

        result =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().in(1, 2, 8))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(3, result.size());
        assertEquals(8, (int) result.getValue(2, TAuthor_ID()));
        assertNull(result.getValue(2, TAuthor_FIRST_NAME()));
        assertEquals("Hesse", result.getValue(2, TAuthor_LAST_NAME()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));
    }

    private void resetLoaderConnection() throws SQLException {
        connection.rollback();
        connection.close();
        connection = null;
        connection = getConnection();
        connection.setAutoCommit(false);
    }

    /**
     * Reflection helper
     */
    @SuppressWarnings("unchecked")
    protected <R> R invoke(Class<?> clazz, String methodName, Object... parameters) throws Exception {
        return (R) invoke0(clazz, clazz, methodName, parameters);
    }

    /**
     * Reflection helper
     */
    @SuppressWarnings("unchecked")
    protected <R> R  invoke(Object object, String methodName, Object... parameters) throws Exception {
        return (R) invoke0(object.getClass(), object, methodName, parameters);
    }

    /**
     * Reflection helper
     */
    private Object invoke0(Class<?> clazz, Object object, String methodName, Object... parameters) throws Exception {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                try {
                    return method.invoke(object, parameters);
                }
                catch (IllegalArgumentException ignore) {
                }
            }
        }

        // If there was no matching method and we have DUMMY parameters
        // Try removing them first. DUMMY parameters are used in SQL Server
        if (Arrays.asList(parameters).contains(DUMMY_OUT_INT)) {
            List<Object> alternative = new ArrayList<Object>(Arrays.asList(parameters));
            while (alternative.remove(DUMMY_OUT_INT));
            return invoke0(clazz, object, methodName, alternative.toArray());
        }

        throw new NoSuchMethodException();
    }

    // Dummy parameters for SQL Server
    private static Integer DUMMY_OUT_INT = new Integer(0);
}
