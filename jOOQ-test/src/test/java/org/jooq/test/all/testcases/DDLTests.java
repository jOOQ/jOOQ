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
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
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

    @SuppressWarnings("unchecked")
    public void testAlterSequence() throws Exception {
        if (cSequences() == null || asList(DERBY, ORACLE).contains(dialect().family())) {
            log.info("SKIPPING", "Skipping ALTER SEQUENCE test");
            return;
        }

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
            create().execute("create table t (a int)");
            create().execute("insert into t values (1)");
            assertEquals(asList(1), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").add("b", SQLDataType.INTEGER).execute();
            assertEquals(asList(1, null), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").add("c", SQLDataType.NUMERIC).execute();
            assertEquals(asList(1, null, null), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").add("d", SQLDataType.NUMERIC.precision(5)).execute();
            assertEquals(asList(1, null, null, null), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").add("e", SQLDataType.NUMERIC.precision(5, 2)).execute();
            assertEquals(asList(1, null, null, null, null), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").add("f", SQLDataType.VARCHAR).execute();
            assertEquals(asList(1, null, null, null, null, null), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").add("g", SQLDataType.VARCHAR.length(5)).execute();
            assertEquals(asList(1, null, null, null, null, null, null), asList(create().fetchOne(table("t")).intoArray()));

            try {
                create().alterTable("t").add("h", SQLDataType.INTEGER.nullable(false)).execute();
                fail();
            }
            catch (DataAccessException expected) {}

            assertEquals(1, create().delete(table("t")).execute());
            create().alterTable("t").add("h", SQLDataType.INTEGER.nullable(false)).execute();
        }
        finally {
            create().dropTable("t").execute();
        }
    }

    public void testAlterTableAlterType() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table t (a int)");
            create().execute("insert into t values (1)");

            create().alterTable("t").alter("a").set(SQLDataType.VARCHAR).execute();
            assertEquals("1", create().fetchOne("select * from t").getValue(0));

            create().alterTable("t").alter("a").set(SQLDataType.VARCHAR.nullable(false)).execute();
            try {
                create().update(table("t")).set(field("a"), (Object) null).execute();
                fail();
            }
            catch (DataAccessException expected) {}
        }
        finally {
            create().dropTable("t").execute();
        }
    }

    public void testAlterTableAlterDefault() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table t (a int, b varchar)");

            create().alterTable("t").alter("b").defaultValue("empty").execute();
            create().execute("insert into t (a) values (1)");
            assertEquals("empty", create().fetchOne("select b from t").getValue(0));
        }
        finally {
            create().dropTable("t").execute();
        }
    }

    public void testAlterTableDrop() throws Exception {
        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table t (a int, b int, c int)");
            create().execute("insert into t values (1, 2, 3)");
            assertEquals(asList(1, 2, 3), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").drop("c").execute();
            assertEquals(asList(1, 2), asList(create().fetchOne(table("t")).intoArray()));

            create().alterTable("t").drop("b").execute();
            assertEquals(asList(1), asList(create().fetchOne(table("t")).intoArray()));
        }
        finally {
            create().dropTable("t").execute();
        }
    }

    public void testDropTable() throws Exception {

        // TODO: Re-use jOOQ API for this
        create().execute("create table t (a int, b int, c int)");
        create().execute("insert into t values (1, 2, 3)");
        assertEquals(asList(1, 2, 3), asList(create().fetchOne(table("t")).intoArray()));

        create().dropTable("t").execute();
        try {
            create().fetch(table("t"));
            fail();
        }
        catch (DataAccessException expected) {}
    }
}
