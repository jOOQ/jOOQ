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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.castNull;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.deg;
import static org.jooq.impl.Factory.e;
import static org.jooq.impl.Factory.one;
import static org.jooq.impl.Factory.pi;
import static org.jooq.impl.Factory.rad;
import static org.jooq.impl.Factory.trim;
import static org.jooq.impl.Factory.two;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.zero;
import static org.jooq.tools.reflect.Reflect.on;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.Arrays;

import org.jooq.Configuration;
import org.jooq.ConfigurationProvider;
import org.jooq.ConfigurationRegistry;
import org.jooq.Cursor;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.conf.Settings;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.Factory;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class GeneralTests<
    A    extends UpdatableRecord<A>,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public GeneralTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
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
    public void testSequences() throws Exception {
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
            return;
        }

        jOOQAbstractTest.reset = false;

        Sequence<? extends Number> sequence = SAuthorID();
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
    public void testSerialisation() throws Exception {
        jOOQAbstractTest.reset = false;

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

        // [#1191] Check execution capabilities with new features in ExecuteListener
        ConnectionProviderListener.c = create().getConnection();
        try {
            q = create(new Settings().withExecuteListeners(ConnectionProviderListener.class.getName()))
                    .selectFrom(TAuthor())
                    .orderBy(TAuthor_LAST_NAME());
            q = runSerialisation(q);
            q.execute();

            result = q.getResult();
            result = runSerialisation(result);
            assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
            assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

            result.get(1).setValue(TAuthor_FIRST_NAME(), "Gee-Gee");
            result.get(1).store();
        }
        finally {
            ConnectionProviderListener.c = null;
        }

        // [#1071] Check sequences
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
        }
        else {
            Select<?> s;

            s = create().select(SAuthorID().nextval(), SAuthorID().currval());
            s = runSerialisation(s);
        }
    }

    public static class ConnectionProviderListener extends DefaultExecuteListener {

        static Connection c;

        @Override
        public void start(ExecuteContext ctx) {
            ctx.setConnection(c);
        }
    }

    @SuppressWarnings("deprecation")
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
    public void testAttachable() throws Exception {
        jOOQAbstractTest.reset = false;

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
    public void testDual() throws Exception {
        assertEquals(1, (int) create().selectOne().fetchOne(0, Integer.class));
        assertEquals(1, (int) create().selectOne().where(one().equal(1)).fetchOne(0, Integer.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNULL() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1083] There is a subtle difference in inlining NULL or binding it
        Field<Integer> n1 = castNull(Integer.class);
        Field<Integer> n2 = val(null, Integer.class);
        Field<Integer> c = val(1);

        for (Field<Integer> n : asList(n1, n2)) {
            assertEquals(null, create().select(n).fetchOne(n));
            assertEquals(Integer.valueOf(1), create().select(c).from(TAuthor()).where(TAuthor_ID().equal(1)).and(n.isNull()).fetchOne(c));
            assertEquals(Integer.valueOf(1), create().select(c).from(TAuthor()).where(TAuthor_ID().equal(1)).and(n.equal(n)).fetchOne(c));
            assertEquals(null, create().selectOne().from(TAuthor()).where(n.isNotNull()).fetchAny());
            assertEquals(null, create().selectOne().from(TAuthor()).where(n.notEqual(n)).fetchAny());
        }

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
    public void testMetaModel() throws Exception {

        // Test correct source code generation for the meta model
        Schema schema = TAuthor().getSchema();
        if (schema != null) {
            int sequences = 0;

            if (cSequences() != null) {
                sequences++;

                // DB2 has an additional sequence for the T_TRIGGERS table
                if (getDialect() == DB2 ||
                    getDialect() == H2) {

                    sequences++;
                }

                // CUBRID generates sequences for AUTO_INCREMENT columns
                else if (getDialect() == CUBRID) {
                    sequences += 3;
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

            int tables = 17;

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
                getDialect() == CUBRID ||
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
                assertEquals(tables + 11, schema.getTables().size());
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
    public void testBatchSingle() throws Exception {
        jOOQAbstractTest.reset = false;

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
        jOOQAbstractTest.reset = false;

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
                    .set((Field<Object>)TBook_LANGUAGE_ID(), on(TBook_LANGUAGE_ID().getDataType().getType()).get("en"))
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
}
