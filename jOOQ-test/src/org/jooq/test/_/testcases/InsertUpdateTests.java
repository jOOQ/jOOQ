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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.cast;
import static org.jooq.impl.Factory.castNull;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.inline;
import static org.jooq.impl.Factory.max;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.vals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;

import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.MergeFinalStep;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class InsertUpdateTests<
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

    public InsertUpdateTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testInsertIdentity() throws Exception {

        // Oracle and SQLite don't support identity columns
        if (TIdentity() == null && TIdentityPK() == null) {
            log.info("SKIPPING", "IDENTITY tests");
            return;
        }

        jOOQAbstractTest.reset = false;

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
        // TODO [#1260] This probably works for CUBRID
        // TODO [#832] Make this work for Sybase also
        // TODO [#1004] Make this work for SQL Server also
        // TODO ... and then, think about Ingres, H2 and Derby as well
        if (getDialect() == CUBRID ||
            getDialect() == SYBASE ||
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

    @Test
    public void testInsertImplicit() throws Exception {
        jOOQAbstractTest.reset = false;

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
        jOOQAbstractTest.reset = false;

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
        jOOQAbstractTest.reset = false;

        // [#1005] With the INSERT .. VALUES syntax, typesafety cannot be
        // enforced. But the inserted values should at least be converted to the
        // right types

        long timeIn = 0;
        long timeOut = -3600000;

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
                    timeIn,
                    new BigDecimal("1980"))
                .execute());

        A author1 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(5)).fetchOne();
        assertNotNull(author1);
        assertEquals(5, (int) author1.getValue(TAuthor_ID()));
        assertEquals("Smith", author1.getValue(TAuthor_LAST_NAME()));

        // [#1009] Somewhere on the way to the database and back, the CET time
        // zone is added, that's why there is a one-hour shift (except for SQLite)
        if (getDialect() != SQLITE)
            assertEquals(new Date(timeOut), author1.getValue(TAuthor_DATE_OF_BIRTH()));
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

        // [#1343] Conversion mustn't be done on jOOQ artefacts
        assertEquals(1,
        create().insertInto(TAuthor())
                .values(
                    create().select(vals(38)),
                    val("Alfred"),
                    inline("Hitchcock"),
                    val(null),
                    inline((Object) null),
                    create().select(val(null)).asField())
                .execute());
    }

    @Test
    public void testInsertSelect() throws Exception {
        jOOQAbstractTest.reset = false;

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

        Insert<A> i =
        create().insertInto(TAuthor())
                .select(create().select(vals(
                                    1000,
                                    val("Lukas")))
                                .select(vals(
                                    "Eder",
                                    val(new Date(363589200000L)),
                                    castNull(Integer.class),
                                    nullField)));

        assertEquals(1, i.execute());

        A author1 = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Lukas"));
        assertEquals(1000, (int) author1.getValue(TAuthor_ID()));
        assertEquals("Lukas", author1.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Eder", author1.getValue(TAuthor_LAST_NAME()));
        assertEquals(null, author1.getValue(TAuthor_YEAR_OF_BIRTH()));

        // [#1069] Run checks for INSERT INTO t(a, b) SELECT x, y syntax
        i = create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                    .select(create().select(vals(1001, "Hesse")));

        assertEquals(1, i.execute());
        A author2 = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hesse"));
        assertEquals(1001, (int) author2.getValue(TAuthor_ID()));
        assertEquals(null, author2.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Hesse", author2.getValue(TAuthor_LAST_NAME()));
        assertEquals(null, author2.getValue(TAuthor_YEAR_OF_BIRTH()));
    }

    @Test
    public void testInsertWithSelectAsField() throws Exception {
        jOOQAbstractTest.reset = false;

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

    @SuppressWarnings("unchecked")
    @Test
    public void testInsertReturning() throws Exception {
        if (TTriggers() == null) {
            log.info("SKIPPING", "INSERT RETURNING tests");
            return;
        }

        jOOQAbstractTest.reset = false;

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

        // Alternative syntax
        // ------------------
        returned = create().insertInto(TTriggers())
                           .set(TTriggers_COUNTER(), 0)
                           .returning(TTriggers_ID_GENERATED())
                           .fetchOne();
        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(TTriggers_ID_GENERATED()));
        assertNull(returned.getValue(TTriggers_ID()));
        assertNull(returned.getValue(TTriggers_COUNTER()));

        switch (getDialect()) {
            case ASE:
            // TODO [#1260] This should work eventually, when CUBRID fixes this
            // JDBC bug
            case CUBRID:
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
    public void testInsertOnDuplicateKey() throws Exception {
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

        jOOQAbstractTest.reset = false;

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
            case CUBRID:
            case DERBY:
            case H2:
            case INGRES:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "Merge tests");
                return;
        }

        jOOQAbstractTest.reset = false;

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
    public void testUpdateSelect() throws Exception {
        switch (getDialect()) {
            case SQLITE:
            case MYSQL:
                log.info("SKIPPING", "UPDATE .. SET .. = (SELECT ..) integration test. This syntax is poorly supported by " + getDialect());
                return;
        }

        jOOQAbstractTest.reset = false;

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
    public void testTruncate() throws Exception {
        jOOQAbstractTest.reset = false;

        try {
            create().truncate(TAuthor()).execute();

            // The above should fail if foreign keys are supported
            if (!Arrays.asList(CUBRID, INGRES, SQLITE).contains(getDialect())) {
                fail();
            }
        } catch (Exception expected) {
        }

        // This is being tested with an unreferenced table as some RDBMS don't
        // Allow this
        create().truncate(TDates()).execute();
        assertEquals(0, create().fetch(TDates()).size());
    }
}
