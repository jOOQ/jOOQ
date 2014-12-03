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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;

import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Sequence;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class DDLTests<
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
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public DDLTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testCreateView() throws Exception {
        try {
            create().createView("v1").as(select(one().as("one"))).execute();
            create().createView("v2", "two").as(select(two())).execute();

            assertEquals(1, (int) create().fetchValue(select(fieldByName(Integer.class, "one")).from(tableByName("v1"))));
            assertEquals(2, (int) create().fetchValue(select(fieldByName(Integer.class, "two")).from(tableByName("v2"))));
        }
        finally {
            create().dropView(tableByName("v1")).execute();
            create().dropViewIfExists(tableByName("v2")).execute();
            create().dropViewIfExists(tableByName("v2")).execute();

            assertThrows(DataAccessException.class, () -> {
                create().fetch("select * from {0}", name("v1"));
            });

            assertThrows(DataAccessException.class, () -> {
                create().fetch("select * from {0}", name("v2"));
            });
        }
    }

    public void testCreateIndex() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} int)", name("t"), name("a"), name("b"));
            create().createIndex("idx1").on("t", "a").execute();
            create().createIndex("idx2").on("t", "a", "b").execute();

            try {
                // The easiest way to validate that index creation has worked in all dialects is to
                // create another index by the same name
                create().createIndex("idx1").on("t", "b").execute();
                fail();
            }
            catch (DataAccessException expected) {}
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testDropIndex() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} int)", name("t"), name("a"), name("b"));
            create().createIndex("idx1").on("t", "a").execute();
            create().createIndex("idx2").on("t", "a", "b").execute();
            create().dropIndex("idx2").execute();
            create().createIndex("idx2").on("t", "b").execute();
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testDropIndexIfExists() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} int)", name("t"), name("a"), name("b"));
            create().createIndex("idx1").on("t", "a").execute();
            create().dropIndexIfExists("idx1").execute();
            create().dropIndexIfExists("idx1").execute();
            create().createIndex("idx1").on("t", "a").execute();
            create().dropIndexIfExists("idx2").execute();
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testCreateSequence() throws Exception {
        assumeNotNull(cSequences());

        try {
            create().createSequence("s").execute();

            // Some databases create sequences that start with ZERO
            assertTrue(Arrays.asList(BigInteger.ZERO, BigInteger.ONE).contains(create().nextval("s")));
        }
        finally {
            ignoreThrows(() -> create().dropSequence("s").execute());
        }
    }

    public void testDropSequence() throws Exception {
        assumeNotNull(SAuthorID());

        try {
            create().dropSequence(SAuthorID()).execute();
            create().nextval(SAuthorID());

            fail();
        }
        catch (DataAccessException expected) {}
    }

    public void testDropSequenceIfExists() throws Exception {
        assumeNotNull(SAuthorID());

        create().dropSequenceIfExists(SAuthorID()).execute();
        create().dropSequenceIfExists(SAuthorID()).execute();

        try {
            create().nextval(SAuthorID());
            fail();
        }
        catch (DataAccessException expected) {}
    }

    @SuppressWarnings("unchecked")
    public void testAlterSequence() throws Exception {
        assumeNotNull(cSequences());
        assumeFamilyNotIn(DERBY, ORACLE);

        jOOQAbstractTest.reset = false;
        Sequence<Number> S_AUTHOR_ID = (Sequence<Number>) SAuthorID();

        switch (dialect().family()) {

            // These dialects have a mandatory WITH clause
            /* [pro] */
            case INGRES:
            case SYBASE:
            /* [/pro] */

            case FIREBIRD:
            case H2:
                break;

            default:
                create().alterSequence(S_AUTHOR_ID).restart().execute();
                assertEquals(1, create().nextval(S_AUTHOR_ID).intValue());
                assertEquals(2, create().nextval(S_AUTHOR_ID).intValue());

                create().alterSequence(S_AUTHOR_ID).restart().execute();
                assertEquals(1, create().nextval(S_AUTHOR_ID).intValue());
                assertEquals(2, create().nextval(S_AUTHOR_ID).intValue());
        }

        // Work around this Firebird bug: http://tracker.firebirdsql.org/browse/CORE-4349
        int i = 5;
        if (dialect().family() == FIREBIRD)
            i++;

        create().alterSequence(S_AUTHOR_ID).restartWith(5).execute();
        assertEquals(i++, create().nextval(S_AUTHOR_ID).intValue());
        assertEquals(i++, create().nextval(S_AUTHOR_ID).intValue());
    }

    public void testAlterTableAdd() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} " + varchar() + ")", name("t"), name("a"));
            create().insertInto(tableByName("t"), fieldByName("a")).values(1).execute();
            assertEquals(asList("1"), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").add("b", SQLDataType.INTEGER).execute();
            assertEquals(asList("1", null), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").add("c", SQLDataType.NUMERIC).execute();
            assertEquals(asList("1", null, null), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").add("d", SQLDataType.NUMERIC.precision(5)).execute();
            assertEquals(asList("1", null, null, null), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").add("e", SQLDataType.NUMERIC.precision(5, 2)).execute();
            assertEquals(asList("1", null, null, null, null), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").add("f", SQLDataType.VARCHAR).execute();
            assertEquals(asList("1", null, null, null, null, null), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").add("g", SQLDataType.VARCHAR.length(5)).execute();
            assertEquals(asList("1", null, null, null, null, null, null), asList(create().fetchOne(tableByName("t")).intoArray()));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableAlterType() throws Exception {
        assumeFamilyNotIn(FIREBIRD, SQLITE);

        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int)", name("t"), name("a"));
            create().alterTable("t").alter("a").set(SQLDataType.VARCHAR).execute();
            create().insertInto(tableByName("t"), fieldByName("a")).values("1").execute();
            assertEquals("1", create().fetchOne("select * from {0}", name("t")).getValue(0));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableAlterDefault() throws Exception {
        assumeFamilyNotIn(INFORMIX, SQLITE);

        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} " + varchar() + ")", name("t"), name("a"), name("b"));

            create().alterTable("t").alter("b").defaultValue("empty").execute();
            create().insertInto(tableByName("t"), fieldByName("a")).values(1).execute();
            assertEquals("empty", create().fetchValue("select {0} from {1}", name("b"), name("t")));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableDrop() throws Exception {
        assumeFamilyNotIn(SQLITE);

        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} " + varchar() + ", {2} " + varchar() + ", {3} " + varchar() + ")", name("t"), name("a"), name("b"), name("c"));
            create().insertInto(tableByName("t"), fieldByName("a"), fieldByName("b"), fieldByName("c")).values("1", "2", "3").execute();
            assertEquals(asList("1", "2", "3"), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").drop("c").execute();
            assertEquals(asList("1", "2"), asList(create().fetchOne(tableByName("t")).intoArray()));

            create().alterTable("t").drop("b").execute();
            assertEquals(asList("1"), asList(create().fetchOne(tableByName("t")).intoArray()));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testDropTable() throws Exception {

        // TODO: Re-use jOOQ API for this
        create().execute("create table {0} ({1} " + varchar() + ", {2} " + varchar() + ", {3} " + varchar() + ")", name("t"), name("a"), name("b"), name("c"));
        create().insertInto(tableByName("t"), fieldByName("a"), fieldByName("b"), fieldByName("c")).values("1", "2", "3").execute();
        assertEquals(asList("1", "2", "3"), asList(create().fetchOne(tableByName("t")).intoArray()));

        create().dropTable("t").execute();
        try {
            create().fetch(tableByName("t"));
            fail();
        }
        catch (DataAccessException expected) {}
    }

    public void testDropTableIfExists() throws Exception {
        assumeFamilyNotIn(INFORMIX);

        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} int)", name("t"), name("a"), name("b"));
            create().dropTableIfExists("t").execute();
            create().dropTableIfExists("t").execute();
            create().execute("create table {0} ({1} int, {2} int)", name("t"), name("a"), name("b"));
            create().dropTableIfExists("t2").execute();
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    private String varchar() {
        return SQLDataType.VARCHAR.length(10).getCastTypeName(create().configuration());
    }

    public void testCreateTable() throws Exception {
        try {
            create().createTable("t")
                    .column(fieldByName(Integer.class, "t", "i"), SQLDataType.INTEGER)
                    .column("n", SQLDataType.DECIMAL.precision(3, 1).nullable(true))
                    .column("s", SQLDataType.VARCHAR.length(5).nullable(false))
                    .execute();

            assertEquals(1,
            create().insertInto(tableByName("t"), fieldByName("i"), fieldByName("n"), fieldByName("s"))
                    .values(1, new BigDecimal("10.5"), "abcde")
                    .execute());

            Result<Record> r1 = create().selectFrom(tableByName("t")).fetch();
            assertEquals(1, r1.size());
            assertEquals(3, r1.fields().length);
            assertEquals("i", r1.field(0).getName());
            assertEquals("n", r1.field(1).getName());
            assertEquals("s", r1.field(2).getName());
            assertEquals(asList(1, new BigDecimal("10.5"), "abcde"), r1.get(0).intoList());

            // Checking of NOT NULL constraints
            assertEquals(1,
            create().insertInto(tableByName("t"), fieldByName(int.class, "i"), fieldByName(BigDecimal.class, "n"), fieldByName(String.class, "s"))
                    .values(null, null, "abcde")
                    .execute());

            try {
                create().insertInto(tableByName("t"), fieldByName("i"), fieldByName("n"), fieldByName("s"))
                        .values(1, new BigDecimal("10.5"), null)
                        .execute();
                fail();
            }
            catch (DataAccessException expected) {}
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testCreateTableAsSelect() throws Exception {
        assumeFamilyNotIn(SYBASE);

        try {
            create().createTable("t").as(
                select(val("value").as("value"))
            ).execute();
            Result<Record> r1 = create().selectFrom(tableByName("t")).fetch();

            assertEquals(1, r1.size());
            assertEquals(1, r1.fields().length);
            assertEquals("value", r1.field(0).getName());
            assertEquals("value", r1.get(0).getValue(0));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testSelectInto() throws Exception {
        assumeFamilyNotIn(SYBASE);

        try {
            create().select(val("value").as("value")).into(tableByName("t")).execute();
            Result<Record> result = create().selectFrom(tableByName("t")).fetch();

            assertEquals(1, result.size());
            assertEquals(1, result.fields().length);
            assertEquals("value", result.field(0).getName());
            assertEquals("value", result.get(0).getValue(0));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }
}
