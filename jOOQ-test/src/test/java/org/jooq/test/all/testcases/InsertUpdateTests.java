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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.val;
import static org.jooq.lambda.Seq.seq;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;

import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.MergeFinalStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Assume;

public class InsertUpdateTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public InsertUpdateTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testInsertIntoView() throws Exception {
        assumeFamilyNotIn(H2, HANA);
        jOOQAbstractTest.reset = false;

        assertEquals(1,
        create().insertInto(table(selectFrom(TAuthor())))
                .columns(TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "abc")
                .execute());
        assertEquals("abc", create().fetchOne(TAuthor(), TAuthor_ID().eq(3)).getValue(TAuthor_LAST_NAME()));


        assertEquals(1,
        create().insertInto(selectFrom(TAuthor()).asTable("x"), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(4, "abc")
                .execute());
        assertEquals("abc", create().fetchOne(TAuthor(), TAuthor_ID().eq(4)).getValue(TAuthor_LAST_NAME()));
    }

    public void testInsertIdentity() throws Exception {

        // Oracle and SQLite don't support identity columns
        assumeNotNull(TIdentity());
        assumeNotNull(TIdentityPK());

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
    private <R extends TableRecord<R>> void testInsertIdentity0(Table<R> table, TableField<R, Integer> id, TableField<R, Integer> val) throws Exception {

        // Plain insert
        // ------------
        assertEquals(1,
        create().insertInto(table, val)
                .values(10)
                .execute());

        int firstId = create().select(max(id)).from(table).fetchOne(max(id));

        if (!asList(DB2, HANA, INFORMIX, POSTGRES).contains(family()))
            assertEquals(new BigInteger("" + firstId), create().lastID());

        R r1 = create().selectFrom(table).fetchOne();

        assertEquals(firstId, (int) r1.getValue(id));
        assertEquals(10, (int) r1.getValue(val));

        // INSERT .. RETURNING
        // -------------------
        R r2 =
        create().insertInto(table, val)
                .values(11)
                .returning()
                .fetchOne();

        if (!asList(DB2, HANA, INFORMIX, POSTGRES).contains(family())) {
            assertEquals(new BigInteger("" + (firstId + 1)), create().lastID());
            assertEquals(new BigInteger("" + (firstId + 1)), create().lastID());
        }

        assertEquals(firstId + 1, (int) r2.getValue(id));
        assertEquals(11, (int) r2.getValue(val));

        // INSERT MULTIPLE .. RETURNING
        // ----------------------------
        // TODO [#1260] This probably works for CUBRID
        // TODO [#832] Make this work for Sybase also
        // TODO [#1004] Make this work for SQL Server also
        // TODO ... and then, think about Ingres, H2 and Derby as well
        if (dialect() == CUBRID ||
            /* [pro] */
            dialect() == ASE ||
            dialect() == SYBASE ||
            dialect().family() == SQLSERVER ||
            dialect() == INGRES ||
            /* [/pro] */
            dialect() == H2 ||
            dialect() == DERBY) {

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
            assertEquals(firstId + 2, (int) r3.getValue(0, id));
            assertEquals(firstId + 3, (int) r3.getValue(1, id));
        }
    }

    public void testInsertDefaultValues() throws Exception {
        assumeFamilyNotIn(ACCESS, HANA, INFORMIX);
        assumeNotNull(TTriggers());
        jOOQAbstractTest.reset = false;

        assertEquals(1,
        create().insertInto(TTriggers())
                .defaultValues()
                .execute());

        assertEquals(1, (int) create().fetchOne(selectCount().from(TTriggers())).getValue(0, int.class));
        assertEquals(1, create().delete(TTriggers()).execute());
    }

    public void testInsertDefaultValue() throws Exception {
        assumeFamilyNotIn(ACCESS, HANA, INFORMIX, FIREBIRD, SQLITE);
        jOOQAbstractTest.reset = false;

        assertEquals(1,
        create().insertInto(TBook())
                .set(newBook(5))
                .set(TBook_LANGUAGE_ID(), defaultValue(int.class))
                .execute());

        assertEquals(1, (int) create().fetchOne(TBook(), TBook_ID().eq(5)).getValue(TBook_LANGUAGE_ID()));
    }

    public void testInsertSetWithNulls() throws Exception {
        Assume.assumeNotNull(TIdentityPK());
        jOOQAbstractTest.reset = false;

        IPK record = create().newRecord(TIdentityPK());
        record.setValue(TIdentityPK_ID(), null);
        record.setValue(TIdentityPK_VAL(), 1);

        assertEquals(1,
        create().insertInto(TIdentityPK())
                .set(record)
                .execute());

        assertEquals(1, (int) create().fetchOne(TIdentityPK()).getValue(TIdentityPK_VAL()));
    }

    public void testUpdateDefaultValue() throws Exception {
        assumeFamilyNotIn(ACCESS, HANA, INFORMIX, FIREBIRD, SQLITE);
        jOOQAbstractTest.reset = false;

        assertEquals(4,
        create().update(TBook())
                .set(TBook_LANGUAGE_ID(), defaultValue(int.class))
                .execute());

        assertEquals(nCopies(4, 1), create().fetch(TBook()).getValues(TBook_LANGUAGE_ID()));
    }

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

    public void testInsertMultiple() throws Exception {
        jOOQAbstractTest.reset = false;

        create().insertInto(TAuthor(), Arrays.<Field<?>>asList(TAuthor_ID(), TAuthor_LAST_NAME()))

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

    public void testInsertMultipleWithDifferentChangedFlags() throws Exception {
        jOOQAbstractTest.reset = false;

        A a1 = create().newRecord(TAuthor());
        A a2 = create().newRecord(TAuthor());
        A a3 = create().newRecord(TAuthor());

        // [#4402] Bulk insertion must work regardless of the "changed" flag
        //         configuration of individual records
        a1.setValue(TAuthor_ID(), 3);
        a1.setValue(TAuthor_LAST_NAME(), "a1");

        a2.setValue(TAuthor_ID(), 4);
        a2.setValue(TAuthor_LAST_NAME(), "a2");
        a2.setValue(TAuthor_FIRST_NAME(), "a2");

        a3.setValue(TAuthor_ID(), 5);
        a3.setValue(TAuthor_LAST_NAME(), "a3");
        a3.setValue(TAuthor_YEAR_OF_BIRTH(), 3);

        assertEquals(2,
        create().insertInto(TAuthor())
                .set(a1)
                .newRecord()
                .set(a2)
                .newRecord()
                .set(a3)
                .execute());

        Result<A> authors = create()
            .selectFrom(TAuthor())
            .where(TAuthor_ID().in(3, 4, 5))
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(asList(3, 4, 5), authors.getValues(TAuthor_ID()));
        assertEquals(asList("a1", "a2", "a3"), authors.getValues(TAuthor_LAST_NAME()));
        assertEquals(asList(null, "a2", null), authors.getValues(TAuthor_FIRST_NAME()));
        assertEquals(asList(null, null, null), authors.getValues(TAuthor_DATE_OF_BIRTH()));
        assertEquals(asList(null, null, 3), authors.getValues(TAuthor_YEAR_OF_BIRTH()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testInsertConvert() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1005] With the INSERT .. VALUES syntax, typesafety cannot be
        // enforced. But the inserted values should at least be converted to the
        // right types

        // Explicit field list
        assertEquals(1,
        create().insertInto(TAuthor(),
                    (Field<String>) (Field) TAuthor_ID(),
                    TAuthor_LAST_NAME(),
                    (Field<String>) (Field) TAuthor_DATE_OF_BIRTH(),
                    (Field<BigDecimal>) (Field) TAuthor_YEAR_OF_BIRTH())
                .values(
                    "5",
                    "Smith",
                    zeroDate(),
                    new BigDecimal("1980"))
                .execute());

        A author1 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(5)).fetchOne();
        assertNotNull(author1);
        assertEquals(5, (int) author1.getValue(TAuthor_ID()));
        assertEquals("Smith", author1.getValue(TAuthor_LAST_NAME()));

        // [#1009] Somewhere on the way to the database and back, the CET time
        // zone is added, that's why there is a one-hour shift (except for SQLite)
        if (dialect() != SQLITE)
            assertEquals(Date.valueOf(zeroDate()), author1.getValue(TAuthor_DATE_OF_BIRTH()));
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
        // [#1370] But be sure the arguments to .values() are correctly typed
        if (false)
        assertEquals(1,
        create().insertInto(TAuthor())
                .values(
                    create().select(val(38)),
                    val("Alfred"),
                    inline("Hitchcock"),
                    val(null),
                    inline((Object) null),
                    create().select(val(null)).asField())
                .execute());
    }

    public void testInsertSelect() throws Exception {
        jOOQAbstractTest.reset = false;

        Field<?> nullField = null;
        switch (family()) {
            /* [pro] */
            case ORACLE:
            /* [/pro] */
            case POSTGRES:
                nullField = val(null, TAuthor_ADDRESS());
                break;
            default:
                nullField = castNull(String.class);
                break;
        }

        Insert<A> i =
        create().insertInto(TAuthor())
                .select(select(
                            val(1000),
                            val("Lukas"))
                        .select(
                            val("Eder"),
                            val(new Date(363589200000L)),
                            castNull(Integer.class),
                            nullField));

        assertEquals(1, i.execute());

        A author1 = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Lukas"));
        assertEquals(1000, (int) author1.getValue(TAuthor_ID()));
        assertEquals("Lukas", author1.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Eder", author1.getValue(TAuthor_LAST_NAME()));
        assertEquals(null, author1.getValue(TAuthor_YEAR_OF_BIRTH()));

        // [#1069] Run checks for INSERT INTO t(a, b) SELECT x, y syntax
        i = create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                    .select(select(val(1001), val("Hesse")));

        assertEquals(1, i.execute());
        A author2 = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hesse"));
        assertEquals(1001, (int) author2.getValue(TAuthor_ID()));
        assertEquals(null, author2.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Hesse", author2.getValue(TAuthor_LAST_NAME()));
        assertEquals(null, author2.getValue(TAuthor_YEAR_OF_BIRTH()));
    }

    public void testInsertWithSelectAsField() throws Exception {

        Field<Integer> ID3;
        Field<Integer> ID4;

        switch (dialect()) {
            /* [pro] */
            case ACCESS:
                // [#3029] Re-enable this test for MS Access, once the feature is implemented.
                log.info("SKIPPING", "Insert with subselects");
                return;

            // Sybase ASE doesn't allow for selecting data inside VALUES()
            case ASE:

            /* [/pro] */
            // MySQL doesn't allow for selecting from the INSERT INTO table
            case MARIADB:
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

        jOOQAbstractTest.reset = false;

        create().insertInto(TAuthor(),
                    TAuthor_ID(),
                    TAuthor_LAST_NAME())
                .values(
                    ID3,
                    create().select(val("Hornby")).<String>asField())
                .execute();

        A author = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hornby"));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Hornby", author.getValue(TAuthor_LAST_NAME()));

        assertEquals(1,
        create().update(TAuthor())
                .set(TAuthor_ID(), ID4)
                .set(TAuthor_LAST_NAME(), create().select(val("Hitchcock")).<String> asField())
                .where(TAuthor_ID().equal(3))
                .execute());

        author = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hitchcock"));
        assertEquals(Integer.valueOf(4), author.getValue(TAuthor_ID()));
        assertEquals("Hitchcock", author.getValue(TAuthor_LAST_NAME()));

        assertEquals(1,
        create().update(TAuthor())
                .set(TAuthor_ID(), select(inline(5)))
                .set(TAuthor_LAST_NAME(), select(val("Hesse")))
                .where(TAuthor_ID().equal(4))
                .execute());

        author = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Hesse"));
        assertEquals(Integer.valueOf(5), author.getValue(TAuthor_ID()));
        assertEquals("Hesse", author.getValue(TAuthor_LAST_NAME()));
    }

    public void testUpdateWithRowValueExpression() throws Exception {
        assumeFamilyNotIn(ASE, CUBRID, DERBY, FIREBIRD, INFORMIX, MARIADB, MYSQL, SQLSERVER, SQLITE, SYBASE);

        jOOQAbstractTest.reset = false;
        A author;

        // Multi-row UPDATE with degree 1
        // ------------------------------
        assertEquals(1,
        create().update(TAuthor())
                .set(row(TAuthor_FIRST_NAME()), row(val("row1")))
                .where(TAuthor_ID().equal(1))
                .execute());

        author = getAuthor(1);
        assertEquals("row1", author.getValue(TAuthor_FIRST_NAME()));

        // Postgres doesn't support subselects here
        if (!asList(POSTGRES).contains(family())) {
            assertEquals(1,
            create().update(TAuthor())
                    .set(row(TAuthor_FIRST_NAME()), select(val("select1")))
                    .where(TAuthor_ID().equal(1))
                    .execute());

            author = getAuthor(1);
            assertEquals("select1", author.getValue(TAuthor_FIRST_NAME()));
        }

        // Multi-row UPDATE with degree 2
        // ------------------------------
        assertEquals(1,
        create().update(TAuthor())
                .set(row(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME()),
                     row(val("row2a"), val("row2b")))
                .where(TAuthor_ID().equal(1))
                .execute());

        author = getAuthor(1);
        assertEquals("row2a", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("row2b", author.getValue(TAuthor_LAST_NAME()));

        // Postgres doesn't support subselects here
        if (!asList(POSTGRES).contains(dialect())) {
            assertEquals(1,
            create().update(TAuthor())
                    .set(row(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME()),
                         select(val("select2a"), val("select2b")))
                    .where(TAuthor_ID().equal(1))
                    .execute());

            author = getAuthor(1);
            assertEquals("select2a", author.getValue(TAuthor_FIRST_NAME()));
            assertEquals("select2b", author.getValue(TAuthor_LAST_NAME()));
        }

        // Multi-row UPDATE with degree 3 (higher degrees are currently not tested)
        // ------------------------------------------------------------------------
        assertEquals(1,
        create().update(TAuthor())
                .set(row(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME(), TAuthor_YEAR_OF_BIRTH()),
                     row(val("row3a"), val("row3b"), val(3)))
                .where(TAuthor_ID().equal(1))
                .execute());

        author = getAuthor(1);
        assertEquals("row3a", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("row3b", author.getValue(TAuthor_LAST_NAME()));
        assertEquals(3, (int) author.getValue(TAuthor_YEAR_OF_BIRTH()));

        // Postgres doesn't support subselects here
        if (!asList(POSTGRES).contains(dialect())) {
            assertEquals(1,
            create().update(TAuthor())
                    .set(row(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME(), TAuthor_YEAR_OF_BIRTH()),
                         select(val("select3a"), val("select3b"), val(33)))
                    .where(TAuthor_ID().equal(1))
                    .execute());

            author = getAuthor(1);
            assertEquals("select3a", author.getValue(TAuthor_FIRST_NAME()));
            assertEquals("select3b", author.getValue(TAuthor_LAST_NAME()));
            assertEquals(33, (int) author.getValue(TAuthor_YEAR_OF_BIRTH()));
        }
    }

    public void testInsertReturning() throws Exception {
        assumeNotNull(TTriggers());
        jOOQAbstractTest.reset = false;

        // Non-DSL querying
        // ----------------

        // Create a dummy record, generating the original ID
        int ID = testInsertReturningCreateDummyRecord();

        assertEquals(  ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_ID()));
        assertEquals(2*ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_COUNTER()));

        // Returning all fields
        InsertQuery<T> query;
        query = create().insertQuery(TTriggers());
        query.addValue(TTriggers_COUNTER(), val(null, int.class));
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
        TableRecord<T> returned = create()
            .insertInto(TTriggers())
            .columns(TTriggers_COUNTER())
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

        switch (dialect().family()) {
            /* [pro] */
            case ASE:
            case INGRES:
            case ORACLE:
            case SQLSERVER:
            case SYBASE:
            /* [/pro] */
            // TODO [#1260] This should work eventually, when CUBRID fixes this
            // JDBC bug
            case CUBRID:
            case DERBY:

            // TODO Firebird supports the INSERT .. RETURNING syntax, but doesn't
            // support true multi-record inserts. This should be fixed in Firebird
            case FIREBIRD:
            case H2:

            // TODO [#832] Fix this. This might be a driver issue for Sybase
            case SQLITE:
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


        returned =
        create().insertInto(TTriggers(), TTriggers_COUNTER())
                .values(0)
                .returning(TTriggers_ID())
                .fetchOne();
        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(TTriggers_ID()));
        assertNull(returned.getValue(TTriggers_ID_GENERATED()));
        assertNull(returned.getValue(TTriggers_COUNTER()));

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

    public void testInsertSelectReturning() throws Exception {
        assumeNotNull(TTriggers());
        jOOQAbstractTest.reset = false;

        // Create a dummy record, generating the original ID
        int ID = testInsertReturningCreateDummyRecord();

        assertEquals(  ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_ID()));
        assertEquals(2*ID, (int) create().selectFrom(TTriggers()).fetchOne(TTriggers_COUNTER()));

        TableRecord<T> returned = create()
            .insertInto(TTriggers())
            .columns(TTriggers_COUNTER())
            .select(select(val(0)))
            .returning()
            .fetchOne();
        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(TTriggers_ID_GENERATED()));
        assertEquals(  ID, (int) returned.getValue(TTriggers_ID()));
        assertEquals(2*ID, (int) returned.getValue(TTriggers_COUNTER()));

        switch (dialect().family()) {
            /* [pro] */
            case ASE:
            case INGRES:
            case ORACLE:
            case SQLSERVER:
            case SYBASE:
            /* [/pro] */
            // TODO [#1260] This should work eventually, when CUBRID fixes this
            // JDBC bug
            case CUBRID:
            case DERBY:

            // TODO Firebird supports the INSERT .. RETURNING syntax, but doesn't
            // support true multi-record inserts. This should be fixed in Firebird
            case FIREBIRD:
            case H2:

            // TODO [#832] Fix this. This might be a driver issue for Sybase
            case SQLITE:
                log.info("SKIPPING", "Multiple INSERT RETURNING");
                break;

            default:
                Result<?> many =
                create().insertInto(TTriggers(), TTriggers_COUNTER())
                        .select(select(val(-1))
                         .union(select(val(-2)))
                         .union(select(val(-3))))
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

        returned =
        create().insertInto(TTriggers(), TTriggers_COUNTER())
                .select(select(val(0)))
                .returning(TTriggers_ID())
                .fetchOne();
        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(TTriggers_ID()));
        assertNull(returned.getValue(TTriggers_ID_GENERATED()));
        assertNull(returned.getValue(TTriggers_COUNTER()));
    }

    private int testInsertReturningCreateDummyRecord() {
        InsertQuery<T> query;

        // Without RETURNING clause
        query = create().insertQuery(TTriggers());
        query.addValue(TTriggers_ID(), val(null, int.class));
        query.addValue(TTriggers_COUNTER(), 0);
        assertEquals(1, query.execute());
        assertNull(query.getReturnedRecord());

        // Check if the trigger works correctly
        assertEquals(1, create().selectFrom(TTriggers()).fetch().size());

        // Other test cases may have influenced this value
        return create().selectFrom(TTriggers()).fetchOne(TTriggers_ID_GENERATED());
    }

    public void testInsertReturningWithModelAPI() throws Exception {
        assumeNotNull(TTriggers());
        jOOQAbstractTest.reset = false;

        InsertQuery<T> i1 = create().insertQuery(TTriggers());
        i1.addValue(TTriggers_ID(), 1);
        i1.addValue(TTriggers_COUNTER(), 1);
        i1.setReturning(TTriggers_ID_GENERATED(), TTriggers_COUNTER());
        assertEquals(1, i1.execute());
        assertEquals(1, i1.getReturnedRecords().size());
        assertNull(i1.getReturnedRecord().getValue(TTriggers_ID()));
        assertEquals(
            2 * i1.getReturnedRecord().getValue(TTriggers_ID_GENERATED()),
            1 * i1.getReturnedRecord().getValue(TTriggers_COUNTER()));


        InsertQuery<?> i2 = create().insertQuery(tableByName(TTriggers().getName()));
        i2.addValue(fieldByName(TTriggers_ID().getName()), 1);
        i2.addValue(fieldByName(TTriggers_COUNTER().getName()), 1);
        i2.setReturning(fieldByName(TTriggers_ID_GENERATED().getName()), fieldByName(TTriggers_COUNTER().getName()));
        assertEquals(1, i2.execute());
        assertEquals(1, i2.getReturnedRecords().size());
        assertEquals(
            2 * i2.getReturnedRecord().getValue(TTriggers_ID_GENERATED().getName(), int.class),
            1 * i2.getReturnedRecord().getValue(TTriggers_COUNTER().getName(), int.class));
    }

    public void testInsertReturningWithSetClause() throws Exception {
        assumeNotNull(TTriggers());
        jOOQAbstractTest.reset = false;

        {
            T result =
            create().insertInto(TTriggers())
                    .set(TTriggers_ID(), 1)
                    .set(TTriggers_COUNTER(), 1)
                    .returning()
                    .fetchOne();

            assertEquals(1, (int) result.getValue(TTriggers_ID()));
            assertEquals(1, (int) result.getValue(TTriggers_ID_GENERATED()));
            assertEquals(2, (int) result.getValue(TTriggers_COUNTER()));
        }

        {
            T record = create().newRecord(TTriggers());
            record.setValue(TTriggers_ID(), 2);
            record.setValue(TTriggers_COUNTER(), 2);

            T result =
            create().insertInto(TTriggers())
                    .set(record)
                    .returning()
                    .fetchOne();

            assertEquals(2, (int) result.getValue(TTriggers_ID()));
            assertEquals(2, (int) result.getValue(TTriggers_ID_GENERATED()));
            assertEquals(4, (int) result.getValue(TTriggers_COUNTER()));
        }
    }

    public void testInsertReturningWithCaseSensitiveColumns() throws Exception {
        assumeNotNull(CASE());
        jOOQAbstractTest.reset = false;

        CASE c =
        create().insertInto(CASE(), CASE_ID(), CASE_insensitive(), CASE_lower(), CASE_Mixed(), CASE_UPPER())
                .values(1, 2, 3, 4, 5)
                .returning()
                .fetchOne();

        assertEquals(1, (int) c.getValue(CASE_ID()));
        assertEquals(2, (int) c.getValue(CASE_insensitive()));
        assertEquals(3, (int) c.getValue(CASE_lower()));
        assertEquals(4, (int) c.getValue(CASE_Mixed()));
        assertEquals(5, (int) c.getValue(CASE_UPPER()));
    }

    public void testInsertReturningWithRenderNameStyleAS_IS() throws Exception {
        // [#3035] TODO: Re-enable this test
        switch (dialect().family()) {
            case DERBY:
            case H2:
                log.info("SKIPPING", "Insert Returning Test");
                return;
        }

        jOOQAbstractTest.reset = false;

        // [#2845] Some SQL dialects use Connection.prepareStatement(String, String[])
        // in case of which column names should be transformed according to RenderNameStyle
        A author =
        create(new Settings().withRenderNameStyle(RenderNameStyle.AS_IS))
                .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(5, "XMF")
                .returning()
                .fetchOne();

        assertEquals(5, (int) author.getValue(TAuthor_ID()));
        assertEquals("XMF", author.getValue(TAuthor_LAST_NAME()));
    }

    public void testInsertReturningWithPlainSQL() throws Exception {
        assumeNotNull(TTriggers());
        jOOQAbstractTest.reset = false;

        // Create a dummy record, generating the original ID
        int ID = testInsertReturningCreateDummyRecord();

        Record returned = create()
            .insertInto(table(name(TTriggers().getName())))
            .set(field(name(TTriggers_COUNTER().getName())), 0)
            .returning(field(name(TTriggers_ID_GENERATED().getName())))
            .fetchOne();

        assertNotNull(returned);
        assertEquals(++ID, (int) returned.getValue(0, int.class));
    }

    public void testUpdateReturning() throws Exception {
        assumeFamilyNotIn(ASE, INGRES, ORACLE, REDSHIFT, SQLSERVER, SYBASE, CUBRID, DERBY, HANA, H2, HSQLDB, MARIADB,
            MYSQL, SQLITE);

        jOOQAbstractTest.reset = false;

        Result<?> result1 =
        create().update(TBook())
                .set(TBook_TITLE(), "XYZ")
                .where(TBook_ID().eq(1))
                .returning(TBook_ID(), TBook_TITLE())
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(1, (int) result1.get(0).getValue(TBook_ID()));
        assertEquals("XYZ", result1.get(0).getValue(TBook_TITLE()));

        switch (dialect()) {
            case FIREBIRD: {
                break;
            }

            // Some databases do not support RETURNING clauses that affect more
            // than one row.
            default: {
                Result<?> result2 =
                create().update(TBook())
                        .set(TBook_TITLE(), decode().value(TBook_ID()).when(1, "ABC").otherwise(TBook_TITLE()))
                        .where(TBook_ID().in(1, 2))
                        .returning(TBook_ID(), TBook_TITLE())
                        .fetch();

                assertEquals(2, result2.size());

                // The order is not guaranteed in UPDATE .. RETURNING clauses
                assertSame(asList(1, 2), result2.getValues(TBook_ID()));
                assertSame(asList("ABC", "Animal Farm"), result2.getValues(TBook_TITLE()));
            }
        }
    }

    public void testDeleteReturning() throws Exception {
        switch (dialect().family()) {
            /* [pro] */
            case ASE:
            case HANA:
            case INGRES:
            case ORACLE:
            case SQLSERVER:
            case SYBASE:
            case VERTICA:
            /* [/pro] */
            case CUBRID:
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "DELETE .. RETURNING tests");
                return;
        }

        jOOQAbstractTest.reset = false;
        Result<?> result1 =
        create().delete(TBook())
                .where(TBook_ID().eq(1))
                .returning(TBook_ID(), TBook_TITLE())
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(1, (int) result1.get(0).getValue(TBook_ID()));
        assertEquals(BOOK_TITLES.get(0), result1.get(0).getValue(TBook_TITLE()));

        switch (dialect()) {
//            case FIREBIRD: {
//                break;
//            }

            // Some databases do not support RETURNING clauses that affect more
            // than one row.
            default: {
                Result<?> result2 =
                create().delete(TBook())
                        .where(TBook_ID().in(2, 3))
                        .returning(TBook_ID(), TBook_TITLE())
                        .fetch();

                assertEquals(2, result2.size());

                // The order is not guaranteed in UPDATE .. RETURNING clauses
                assertSame(asList(2, 3), result2.getValues(TBook_ID()));
                assertSame(BOOK_TITLES.subList(1, 3), result2.getValues(TBook_TITLE()));
            }
        }
    }

    public void testInsertOnDuplicateKeyUpdate() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DERBY, FIREBIRD, H2, HANA, INGRES, POSTGRES, REDSHIFT, SQLITE);

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

    public void testInsertOnDuplicateKeyIgnore() throws Exception {
        // assumeFamilyNotIn(ASE, DERBY, FIREBIRD, H2, HANA, INGRES, REDSHIFT, POSTGRES, SQLITE);

        jOOQAbstractTest.reset = false;

        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Koontz")
                .onDuplicateKeyIgnore()
                .execute();
        A author =
        create().fetchOne(TAuthor(), TAuthor_ID().equal(3));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Koontz", author.getValue(TAuthor_LAST_NAME()));
        assertEquals(Integer.valueOf(3), create().select(count()).from(TAuthor()).fetchOne(0));

        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Rose")
                .onDuplicateKeyIgnore()
                .execute();
        author =
        create().fetchOne(TAuthor(), TAuthor_ID().equal(3));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Koontz", author.getValue(TAuthor_LAST_NAME()));
        assertEquals(Integer.valueOf(3), create().select(count()).from(TAuthor()).fetchOne(0));

    }

    public void testMerge() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DERBY, FIREBIRD, H2, HANA, INGRES, MARIADB, MYSQL, POSTGRES, REDSHIFT, SQLITE);

        jOOQAbstractTest.reset = false;

        // Always do an update of everything
        // --------------------------------
        create().mergeInto(TAuthor())
                .using(selectOne())
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
                .using(selectOne())
                // Inline this bind value. In this particular case, INFORMIX doesn't like bind values...
                .on(TAuthor_ID().equal(inline(1)))
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
                .using(select(f, l))
                .on(TAuthor_ID().eq(3))
                .whenMatchedThenUpdate()
                .set(TAuthor_FIRST_NAME(), "James")
                .set(TAuthor_LAST_NAME(), "Dean")
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
                .whenNotMatchedThenInsert(Arrays.<Field<?>>asList(
                    TAuthor_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())
                )

                // [#1010] Be sure that this type-unsafe clause can deal with
                // any convertable type
                .values(
                    "4",
                    f,
                    l,
                    0L);

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

    public void testMergeWithOracleSyntaxExtension() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DB2, INGRES, REDSHIFT, SQLSERVER, SYBASE, DERBY, FIREBIRD, H2, HANA, HSQLDB,
            MARIADB, MYSQL, POSTGRES, SQLITE);

        jOOQAbstractTest.reset = false;

        A author;

        // Test updating with a positive condition
        // ---------------------------------------
        assertEquals(1,
        create().mergeInto(TAuthor())
                .usingDual()
                .on(TAuthor_ID().equal(1))
                .whenMatchedThenUpdate()
                .set(TAuthor_LAST_NAME(), "Frisch")
                .where(TAuthor_ID().equal(1))
                .execute());

        author = create().fetchOne(TAuthor(), TAuthor_ID().equal(1));
        assertEquals(2, create().selectCount().from(TAuthor()).fetchOne(0));
        assertEquals(1, (int) author.getValue(TAuthor_ID()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0), author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Frisch", author.getValue(TAuthor_LAST_NAME()));

        // Test updating with a negative condition
        // ---------------------------------------
        assertEquals(0,
        create().mergeInto(TAuthor())
                .usingDual()
                .on(TAuthor_ID().equal(1))
                .whenMatchedThenUpdate()
                .set(TAuthor_LAST_NAME(), "Frisch")
                .where(TAuthor_ID().equal(3))
                .execute());

        author = create().fetchOne(TAuthor(), TAuthor_ID().equal(1));
        assertEquals(2, create().selectCount().from(TAuthor()).fetchOne(0));
        assertEquals(1, (int) author.getValue(TAuthor_ID()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0), author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Frisch", author.getValue(TAuthor_LAST_NAME()));

        // Test deleting
        // -------------
        // ON DELETE CASCADE doesn't work with MERGE...?
        create().delete(TBook()).execute();

        assertEquals(1,
        create().mergeInto(TAuthor())
                .usingDual()
                .on(trueCondition())
                .whenMatchedThenUpdate()
                .set(TAuthor_LAST_NAME(), "Frisch")
                .where(TAuthor_ID().equal(2))
                .deleteWhere(TAuthor_ID().equal(2))
                .execute());

        author = create().fetchOne(TAuthor(), TAuthor_ID().equal(1));
        assertEquals(1, create().selectCount().from(TAuthor()).fetchOne(0));
        assertEquals(1, (int) author.getValue(TAuthor_ID()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0), author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Frisch", author.getValue(TAuthor_LAST_NAME()));

        // Test inserting
        // --------------
        assertEquals(0,
        create().mergeInto(TAuthor())
                .usingDual()
                .on(trueCondition())
                .whenNotMatchedThenInsert(
                    TAuthor_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .values(3, "Yvette", "Z'Graggen")
                .where(falseCondition())
                .execute());

        // No tests on results
    }

    public void testMergeWithH2SyntaxExtension() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DERBY, FIREBIRD, HANA, INGRES, MARIADB, MYSQL, POSTGRES, REDSHIFT, SQLITE);

        jOOQAbstractTest.reset = false;

        // H2 MERGE test leading to a single INSERT .. VALUES
        // -------------------------------------------------------------
        assertEquals(1,
        create().mergeInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Hesse")
                .execute());

        Result<A> authors1 = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        assertEquals(3, authors1.size());
        assertEquals(3, (int) authors1.get(2).getValue(TAuthor_ID()));
        assertEquals("Hesse", authors1.get(2).getValue(TAuthor_LAST_NAME()));
        assertNull(authors1.get(2).getValue(TAuthor_FIRST_NAME()));

        // H2 MERGE test leading to a single UPDATE
        // -------------------------------------------------------------
        assertEquals(1,
        create().mergeInto(TAuthor(), TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .values(3, "Hermann", "Hesse")
                .execute());

        Result<A> authors2 = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        assertEquals(3, authors2.size());
        assertEquals(3, (int) authors2.get(2).getValue(TAuthor_ID()));
        assertEquals("Hesse", authors2.get(2).getValue(TAuthor_LAST_NAME()));
        assertEquals("Hermann", authors2.get(2).getValue(TAuthor_FIRST_NAME()));

        // H2 MERGE test specifying a custom KEY clause
        // -------------------------------------------------------------
        assertEquals(1,
        create().mergeInto(TAuthor(), TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .key(TAuthor_LAST_NAME())
                .values(3, "Lukas", "Hesse")
                .execute());

        Result<A> authors3 = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        assertEquals(3, authors3.size());
        assertEquals(3, (int) authors3.get(2).getValue(TAuthor_ID()));
        assertEquals("Hesse", authors3.get(2).getValue(TAuthor_LAST_NAME()));
        assertEquals("Lukas", authors3.get(2).getValue(TAuthor_FIRST_NAME()));

        // H2 MERGE test specifying a subselect
        // -------------------------------------------------------------
        assertEquals(2,
        create().mergeInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .key(TAuthor_ID())

                // inline() strings here. It seems that DB2 will lack page size
                // in the system temporary table space, otherwise

                // [#579] TODO: Aliasing shouldn't be necessary
                .select(select(val(3).as("a"), inline("Eder").as("b")).unionAll(
                        select(val(4).as("a"), inline("Eder").as("b"))))
                .execute());

        Result<A> authors4 = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        assertEquals(4, authors4.size());
        assertEquals(3, (int) authors4.get(2).getValue(TAuthor_ID()));
        assertEquals("Eder", authors4.get(2).getValue(TAuthor_LAST_NAME()));
        assertEquals("Lukas", authors4.get(2).getValue(TAuthor_FIRST_NAME()));
        assertEquals(4, (int) authors4.get(3).getValue(TAuthor_ID()));
        assertEquals("Eder", authors4.get(3).getValue(TAuthor_LAST_NAME()));
        assertNull(authors4.get(3).getValue(TAuthor_FIRST_NAME()));

        // H2 MERGE test specifying a subselect
        // -------------------------------------------------------------
        assertEquals(2,
        create().mergeInto(TAuthor(), TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())

                // inline() strings here. It seems that DB2 will lack page size
                // in the system temporary table space, otherwise

                // [#579] TODO: Aliasing shouldn't be necessary
                .select(select(val(3).as("a"), inline("John").as("b"), inline("Eder").as("c")).unionAll(
                        select(val(4).as("a"), inline("John").as("b"), inline("Eder").as("c"))))
                .execute());

        Result<A> authors5 = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        assertEquals(4, authors5.size());
        assertEquals(3, (int) authors5.get(2).getValue(TAuthor_ID()));
        assertEquals("Eder", authors5.get(2).getValue(TAuthor_LAST_NAME()));
        assertEquals("John", authors5.get(2).getValue(TAuthor_FIRST_NAME()));
        assertEquals(4, (int) authors5.get(3).getValue(TAuthor_ID()));
        assertEquals("Eder", authors5.get(3).getValue(TAuthor_LAST_NAME()));
        assertEquals("John", authors5.get(3).getValue(TAuthor_FIRST_NAME()));
    }

    public void testUpdateSelect() throws Exception {
        switch (dialect()) {
            /* [pro] */
            case ACCESS:
            /* [/pro] */

            case SQLITE:
            case MARIADB:
            case MYSQL:
                log.info("SKIPPING", "UPDATE .. SET .. = (SELECT ..) integration test. This syntax is poorly supported by " + dialect());
                return;
        }

        jOOQAbstractTest.reset = false;

        Table<A> a1 = TAuthor();
        Table<A> a2 = TAuthor().as("a2");
        Field<String> f1 = a1.field(TAuthor_FIRST_NAME());
        Field<String> f2 = a2.field(TAuthor_FIRST_NAME());
        Field<String> f3 = a2.field(TAuthor_LAST_NAME());

        UpdateQuery<A> u = create().updateQuery(a1);
        u.addValue(f1, create().select(max(f3)).from(a2).where(f1.equal(f2)).<String> asField());
        u.execute();

        Field<Integer> c = count();
        assertEquals(Integer.valueOf(2), create().select(c)
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal(TAuthor_LAST_NAME()))
            .fetchOne(c));
    }

    public void testUpdateSetRecord() throws Exception {
        jOOQAbstractTest.reset = false;

        B record = create().newRecord(TBook());
        record.setValue(TBook_TITLE(), "abc");
        record.changed(TBook_ID(), false);

        assertEquals(1,
        create().update(TBook())
                .set(record)
                .where(TBook_ID().eq(2))
                .execute());

        Result<B> books =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(BOOK_AUTHOR_IDS, books.getValues(TBook_AUTHOR_ID()));
        assertEquals(seq(BOOK_TITLES).zipWithIndex().map(t -> t.v2 == 1 ? "abc" : t.v1).toList(), books.getValues(TBook_TITLE()));
    }

    public void testUpdateJoin() throws Exception {
        assumeFamilyNotIn(DB2, INGRES, ORACLE, REDSHIFT, SQLSERVER, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE);

        jOOQAbstractTest.reset = false;

        create().update(TBook().join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID())))
                .set(TAuthor_LAST_NAME(), "XX")
                .set(TBook_TITLE(), "YY")
                .where(TBook_ID().eq(1))
                .execute();

        A a1 = getAuthor(1);
        B b1 = getBook(1);

        assertEquals("XX", a1.getValue(TAuthor_LAST_NAME()));
        assertEquals("YY", b1.getValue(TBook_TITLE()));

        // [#2982] Check if variable binding takes place correctly in this situation
        Select<?> subquery = select(TAuthor_ID()).from(TAuthor()).where(TAuthor_ID().eq(1));
        assertEquals(2,
        create().update(TBook()
                    .join(subquery)
                    .on(TBook_AUTHOR_ID().eq(subquery.field(TAuthor_ID()))))
                .set(TBook_TITLE(), "ABC")
                .execute());

        A a2 = getAuthor(1);
        B b21 = getBook(1);
        B b22 = getBook(2);
        assertEquals(a1, a2);
        assertEquals("ABC", b21.getValue(TBook_TITLE()));
        assertEquals("ABC", b22.getValue(TBook_TITLE()));
    }

    public void testUpdateFrom() throws Exception {
        assumeFamilyNotIn(ACCESS, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLITE);
        jOOQAbstractTest.reset = false;

        assertEquals(2,
        create().update(TBook())
                .set(TBook_TITLE(), concat(TAuthor_FIRST_NAME(), inline(" "), TAuthor_LAST_NAME(), inline(": "), TBook_TITLE()))
                .from(TAuthor())
                .where(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                .and(TBook_ID().lt(3))
                .execute());

        Result<B> result =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(BOOK_AUTHOR_IDS, result.getValues(TBook_AUTHOR_ID()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0) + " " + AUTHOR_LAST_NAMES.get(0) + ": " + BOOK_TITLES.get(0), result.get(0).getValue(TBook_TITLE()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0) + " " + AUTHOR_LAST_NAMES.get(0) + ": " + BOOK_TITLES.get(1), result.get(1).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(2), result.get(2).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(3), result.get(3).getValue(TBook_TITLE()));
    }

    public void testUpdateFromWithAlias() throws Exception {
        assumeFamilyNotIn(ACCESS, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLITE);
        jOOQAbstractTest.reset = false;

        Table<B> b1 = TBook().as("b1");
        Table<B> b2 = TBook().as("b2");


        // [#3455] Ensure also that this works for derived (aliased) tables
        Table<A> a1 = selectFrom(TAuthor()).asTable("a1");

        switch (family()) {
            case POSTGRES:
            case INGRES:
            case SYBASE:
                assertEquals(2,
                create().update(b1)
                        .set(b1.field(TBook_TITLE()), concat(
                            a1.field(TAuthor_FIRST_NAME()),
                            inline(" "),
                            a1.field(TAuthor_LAST_NAME()),
                            inline(": "),
                            b1.field(TBook_TITLE())
                        ))
                        .from(b2.join(a1)
                                .on(b2.field(TBook_AUTHOR_ID()).eq(a1.field(TAuthor_ID()))
                                .and(b2.field(TBook_ID()).lt(3))))
                        .where(b1.field(TBook_ID()).eq(b2.field(TBook_ID())))
                        .execute());
                break;

            // [#4314] SQL Server works a bit differently from PostgreSQL and
            // the others. In fact, it's not allowed to declare aliases in the
            // UPDATE clause.
            case SQLSERVER:
                assertEquals(2,
                create().update(b1)
                        .set(b1.field(TBook_TITLE()), concat(
                            a1.field(TAuthor_FIRST_NAME()),
                            inline(" "),
                            a1.field(TAuthor_LAST_NAME()),
                            inline(": "),
                            b1.field(TBook_TITLE())
                        ))
                        .from(b1.join(a1)
                                .on(b1.field(TBook_AUTHOR_ID()).eq(a1.field(TAuthor_ID()))
                                .and(b1.field(TBook_ID()).lt(3))))
                        .execute());
                break;
        }

        Result<B> result =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(BOOK_AUTHOR_IDS, result.getValues(TBook_AUTHOR_ID()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0) + " " + AUTHOR_LAST_NAMES.get(0) + ": " + BOOK_TITLES.get(0), result.get(0).getValue(TBook_TITLE()));
        assertEquals(AUTHOR_FIRST_NAMES.get(0) + " " + AUTHOR_LAST_NAMES.get(0) + ": " + BOOK_TITLES.get(1), result.get(1).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(2), result.get(2).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(3), result.get(3).getValue(TBook_TITLE()));
    }
}
