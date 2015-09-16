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
import static org.jooq.SQLDialect.ORACLE10G;
import static org.jooq.SQLDialect.ORACLE11G;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.VERTICA;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.List;

import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class JoinTests<
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

    public JoinTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testJoinDuplicateFieldNames() throws Exception {

        // [#783] Result holds wrong data when tables in a cartesian self
        // product (or self cross join) are not aliased
        // --------------------------------------------

        // This query selects two different ID fields. It should be possible
        // to extract both from the resulting records
        Result<?> result =
        create().select()
                .from(TAuthor())
                .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                .crossJoin(select(one().as("one")))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(TAuthor_ID()));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(TBook_AUTHOR_ID()));
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));
    }

    public void testJoinQuery() throws Exception {
        SelectQuery<L> q1 = create().selectQuery(VLibrary());

        // TODO: Fix this when funny issue is fixed in Derby:
        // https://sourceforge.net/apps/trac/jooq/ticket/238
        q1.addOrderBy(VLibrary_TITLE());

        // Oracle ordering behaviour is a bit different, so exclude "1984"
        q1.addConditions(VLibrary_TITLE().notEqual("1984"));

        Table<A> a = TAuthor().as("a");
        Table<B> b = TBook().as("b");

        Field<Integer> a_authorID = a.field(TAuthor_ID());
        Field<Integer> b_authorID = b.field(TBook_AUTHOR_ID());
        Field<String> b_title = b.field(TBook_TITLE());

        SelectQuery<?> q2 = create().selectQuery();
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

        // https://github.com/jOOQ/jOOQ/issues/4297
        if (!asList(ACCESS, CUBRID, DB2, INGRES, VERTICA).contains(dialect())) {

            // Advanced JOIN usages with single JOIN condition
            Result<Record> result = create().select()
                .from(TAuthor())
                .join(TBook())
                .on(TAuthor_ID().equal(TBook_AUTHOR_ID())
                .and(TBook_LANGUAGE_ID().in(select(field("id", Integer.class))
                                           .from("t_language")
                                           .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(selectOne().from(TAuthor()).where(falseCondition())))
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
                .on(author.field(TAuthor_ID()).equal(TBook_AUTHOR_ID()))
                .and(TBook_LANGUAGE_ID().in(select(field("id", Integer.class))
                                           .from("t_language")
                                           .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(selectOne().where(falseCondition()))
                .orderBy(TBook_ID()).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

            Select<B> book = create().selectFrom(TBook());
            result = create().select()
                .from(TAuthor())
                .join(book)
                .on(TAuthor_ID().equal(book.field(TBook_AUTHOR_ID())))
                .and(book.field(TBook_LANGUAGE_ID()).in(
                    select(field("id", Integer.class))
                    .from("t_language")
                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(selectOne().where(falseCondition()))
                .orderBy(book.field(TBook_ID())).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

        }
    }

    public void testSemiAntiJoin() throws Exception {

        // a SEMI JOIN b ON a.id = b.author_id
        Result<A> a11 = create()
            .selectFrom(
                TAuthor()
            .semiJoin(TBook()).on(TAuthor_ID().eq(TBook_AUTHOR_ID())))
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(2, a11.size());
        assertEquals(AUTHOR_IDS, a11.getValues(TAuthor_ID()));

        // a ANTI JOIN b ON a.id = b.author_id
        Result<A> a12 = create()
            .selectFrom(
                TAuthor()
            .antiJoin(TBook()).on(TAuthor_ID().eq(TBook_AUTHOR_ID())))
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(0, a12.size());

        // a SEMI JOIN b ON KEY
        Result<Record> a21 = create()
            .select()
            .from(TAuthor())
            .semiJoin(TBook()).onKey(TBook_AUTHOR_ID())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(2, a21.size());
        assertEquals(AUTHOR_IDS, a21.getValues(TAuthor_ID()));

        // a ANTI JOIN b ON KEY
        Result<Record> a22 = create()
            .select()
            .from(TAuthor())
            .antiJoin(TBook()).onKey(TBook_AUTHOR_ID())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(0, a22.size());

        // a SEMI JOIN (SELECT 1) t(a) ON a.id = t.a
        Result<Record> a31 = create()
            .select()
            .from(TAuthor())
            .semiJoin(table(selectOne()).as("t", "a")).on(TAuthor_ID().eq(field(name("t", "a"), Integer.class)))
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(1, a31.size());
        assertEquals(1, a31.get(0).getValue(TAuthor_ID()));

        // a ANTI JOIN (SELECT 1) t(a) ON a.id = t.a
        Result<Record> a32 = create()
            .select()
            .from(TAuthor())
            .antiJoin(table(selectOne()).as("t", "a")).on(TAuthor_ID().eq(field(name("t", "a"), Integer.class)))
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(1, a32.size());
        assertEquals(2, a32.get(0).getValue(TAuthor_ID()));

        // a SEMI JOIN (SELECT 1) t1(a) ON a.id = t1.a ANTI JOIN (SELECT * FROM author WHERE author.id = 2) t2(a) ON a.id = t2.a
        Result<Record4<Integer, String, String, String>> a41 = create()
            .select(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME(), TBook_TITLE())
            .from(TAuthor())
            .join(TBook()).onKey(TBook_AUTHOR_ID())
                .semiJoin(table(selectOne()).as("t1", "a"))
                    .on(TAuthor_ID().eq(field(name("t1", "a"), Integer.class)))
                .antiJoin(table(select(TAuthor_ID()).from(TAuthor()).where(TAuthor_ID().eq(2))).as("t2", "a"))
                    .on(TAuthor_ID().eq(field(name("t2", "a"), Integer.class)))
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, a41.size());
        assertEquals(asList(1, 1), a41.getValues(TAuthor_ID()));
        assertEquals(asList(BOOK_TITLES.get(0), BOOK_TITLES.get(1)), a41.getValues(TBook_TITLE()));

        // a ANTI JOIN (SELECT 1) t1(a) ON a.id = t1.a SEMI JOIN (SELECT * FROM author WHERE author.id = 2) t2(a) ON a.id = t2.a
        Result<Record4<Integer, String, String, String>> a42 = create()
            .select(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME(), TBook_TITLE())
            .from(TAuthor())
            .join(TBook()).onKey(TBook_AUTHOR_ID())
                .antiJoin(table(selectOne()).as("t1", "a"))
                    .on(TAuthor_ID().eq(field(name("t1", "a"), Integer.class)))
                .semiJoin(table(select(TAuthor_ID()).from(TAuthor()).where(TAuthor_ID().eq(2))).as("t2", "a"))
                    .on(TAuthor_ID().eq(field(name("t2", "a"), Integer.class)))
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, a42.size());
        assertEquals(asList(2, 2), a42.getValues(TAuthor_ID()));
        assertEquals(asList(BOOK_TITLES.get(2), BOOK_TITLES.get(3)), a42.getValues(TBook_TITLE()));
    }

    public void testCrossJoin() throws Exception {
        /* [pro] */
        if (dialect().family() == ACCESS) {
            log.info("SKIPPING", "CROSS JOIN tests");
            return;
        }
        /* [/pro] */

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

        // [#783] Unqualified (unaliased) self-joins
        result =
        create().select()
                .from(TAuthor().as("x"))
                .crossJoin(TAuthor())
                .orderBy(1, 1 + TAuthor().fieldsRow().size())
                .fetch();

        assertEquals(4, result.size());
        assertEquals(
            asList(1, 1, 2, 2),
            result.getValues(0, Integer.class));
        assertEquals(
           asList(1, 2, 1, 2),
           result.getValues(0 + TAuthor().fieldsRow().size(), Integer.class));

        // [#1844] Cross joins can be achieved by omitting the ON clause, too
        assertEquals(8, (int)
        create().selectCount()
                .from(TAuthor()
                    .join(TBook(), JoinType.JOIN))
                .fetchOne(0, int.class));

        assertEquals(8, (int)
        create().selectCount()
                .from(TAuthor())
                .join(TBook(), JoinType.CROSS_JOIN)
                .fetchOne(0, int.class));
    }

    public void testCrossApply() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INFORMIX, INGRES, MARIADB, MYSQL, REDSHIFT, SQLITE, VERTICA);
        assumeDialectNotIn(ORACLE10G, ORACLE11G);

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor())
                    .crossApply(
                        select(count().as("c"))
                        .from(TBook())
                        .where(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                    )
                    .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor())
                    .outerApply(
                        select(count().as("c"))
                        .from(TBook())
                        .where(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                    )
                    .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor())
                    .join(
                        select(count().as("c"))
                        .from(TBook())
                        .where(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                        ,
                        JoinType.CROSS_APPLY
                    )
                    .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor().crossApply(
                            select(count().as("c"))
                            .from(TBook())
                            .where(TBook_AUTHOR_ID().eq(TAuthor_ID())))
                    )
                    .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor().outerApply(
                            select(count().as("c"))
                            .from(TBook())
                            .where(TBook_AUTHOR_ID().eq(TAuthor_ID())))
                    )
                    .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor().join(
                            select(count().as("c"))
                            .from(TBook())
                            .where(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                            ,
                            JoinType.OUTER_APPLY)
                    )
                    .fetch("c", int.class)
        );
    }

    public void testLateralJoin() throws Exception {
        switch (dialect()) {
            case ORACLE12C:
            case POSTGRES:
            case POSTGRES_9_3:
            case POSTGRES_9_4:
            case POSTGRES_9_5:
                break;

            default:
                log.info("SKIPPING", "LATERAL tests");
                return;
        }

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor(),
                        lateral(select(count().as("c"))
                                .from(TBook())
                                .where(TBook_AUTHOR_ID().eq(TAuthor_ID())))
                    )
                    .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor().crossJoin(
                        lateral(select(count().as("c"))
                            .from(TBook())
                            .where(TBook_AUTHOR_ID().eq(TAuthor_ID())))
                        )
                    )
                    .fetch("c", int.class)
            );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor(),
                        lateral(select(count())
                            .from(TBook())
                            .where(TBook_AUTHOR_ID().eq(TAuthor_ID()))).as("x", "c")
                        )
                        .fetch("c", int.class)
        );

        assertEquals(
            asList(2, 2),
            create().select()
                    .from(TAuthor(),
                        lateral(select(count())
                            .from(TBook())
                            .where(TBook_AUTHOR_ID().eq(TAuthor_ID())).asTable("x", "c"))
                        )
                        .fetch("c", int.class)
        );
    }

    public void testNaturalJoin() throws Exception {
        boolean unqualified = false;
        if (asList(HSQLDB, ORACLE).contains(dialect().family()))
            unqualified = true;

        Result<Record2<String, String>> result =
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TBook())
                .naturalJoin(TAuthor())
                .orderBy(unqualified
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
                .naturalLeftOuterJoin(TAuthor())
                .orderBy(unqualified
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

    public void testJoinUsing() throws Exception {
        boolean unqualified = false;
        if (asList(HSQLDB, ORACLE).contains(dialect().family()))
            unqualified = true;

        Result<Record2<String, String>> result =
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TAuthor())
                .join(TBook())
                .using(TAuthor_ID())
                .orderBy(unqualified
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
                .orderBy(unqualified
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

    public void testJoinOnKey() throws Exception {
        if (!supportsReferences()) {
            log.info("SKIPPING", "JOIN ON KEY tests");
            return;
        }

        try {
            create().select(TAuthor_ID(), TBook_TITLE())
                    .from(TAuthor().join(TBook()).onKey());
            fail();
        }
        catch (DataAccessException expected) {}

        try {
            create().select(TAuthor_ID(), TBook_TITLE())
                    .from(TAuthor().join(TBook()).onKey(TBook_TITLE()));
            fail();
        }
        catch (DataAccessException expected) {}

        try {
            create().select(TAuthor_ID(), TBook_TITLE())
                    .from(TAuthor().join(TBook()).onKey(TBook_AUTHOR_ID(), TBook_ID()));
            fail();
        }
        catch (DataAccessException expected) {}

        // Test the Table API
        // ------------------
        Result<Record2<Integer, String>> result1 =
        create().select(TAuthor_ID(), TBook_TITLE())
                .from(TAuthor().join(TBook()).onKey(TBook_AUTHOR_ID()))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(4, result1.size());
        assertEquals(BOOK_AUTHOR_IDS, result1.getValues(0));
        assertEquals(BOOK_TITLES, result1.getValues(1));

        Result<Record2<Integer, String>> result2 =
        create().select(TAuthor_ID(), TBook_TITLE())
                .from(TAuthor()
                    .join(TBook())
                    .onKey(TBook_AUTHOR_ID())
                    .and(TBook_ID().in(1, 2)))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, result2.size());
        assertEquals(BOOK_AUTHOR_IDS.subList(0, 2), result2.getValues(0));
        assertEquals(BOOK_TITLES.subList(0, 2), result2.getValues(1));

        // Test the Select API
        // -------------------
        Result<Record2<Integer, String>> result3 =
        create().select(TAuthor_ID(), TBook_TITLE())
                .from(TAuthor())
                .join(TBook()).onKey(TBook_AUTHOR_ID())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(4, result3.size());
        assertEquals(BOOK_AUTHOR_IDS, result3.getValues(0));
        assertEquals(BOOK_TITLES, result3.getValues(1));

        // Test using unambiguous keys
        // ---------------------------
        Result<Record2<Integer, String>> result4 =
        create().select(TBook_ID(), TBookStore_NAME())
                .from(TBook())
                .join(TBookToBookStore()).onKey()
                .join(TBookStore()).onKey()
                .orderBy(TBook_ID(), TBookStore_NAME())
                .fetch();

        assertEquals(6, result4.size());
        assertEquals(asList(1, 1, 2, 3, 3, 3), result4.getValues(0));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), result4.getValues(1));

        // [#671] Test inverse join relationship
        // -------------------------------------
        Result<Record2<Integer, String>> result5 =
        create().select(TBook_ID(), TBookStore_NAME())
                .from(TBook())
                .join(TBookToBookStore()
                    .join(TBookStore()).onKey())
                .onKey()
                .orderBy(TBook_ID(), TBookStore_NAME())
                .fetch();

        assertEquals(result4, result5);
    }

    public void testJoinOnKeyWithAlias() throws Exception {

        // Test using unambiguous keys
        // ---------------------------
        Table<B> b = TBook().as("b");
        Table<S> bs = TBookStore().as("bs");
        Result<Record2<Integer, String>> r1 =
        create().select(b.field(TBook_ID()), bs.field(TBookStore_NAME()))
                .from(b)
                .join(TBookToBookStore()).onKey()
                .join(bs).onKey()
                .orderBy(b.field(TBook_ID()), bs.field(TBookStore_NAME()))
                .fetch();

        assertEquals(6, r1.size());
        assertEquals(asList(1, 1, 2, 3, 3, 3), r1.getValues(0));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), r1.getValues(1));

        // Other join direction
        // --------------------
        Result<Record2<Integer, String>> r2 =
        create().select(b.field(TBook_ID()), bs.field(TBookStore_NAME()))
                .from(bs)
                .join(TBookToBookStore()).onKey()
                .join(b).onKey()
                .orderBy(b.field(TBook_ID()), bs.field(TBookStore_NAME()))
                .fetch();

        assertEquals(6, r2.size());
        assertEquals(asList(1, 1, 2, 3, 3, 3), r2.getValues(0));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), r2.getValues(1));
    }

    public void testInverseAndNestedJoin() throws Exception {

        // [#1086] TODO: Fix this for SQLite
        // In CUBRID, it is not suupported
        if (dialect() == SQLITE || dialect() == CUBRID) {
            log.info("SKIPPING", "Nested JOINs");
            return;
        }

        // Testing joining of nested joins
        // -------------------------------
        Result<Record3<Integer, Integer, String>> result1 = create()
            .select(
                TAuthor_ID(),
                TBook_ID(),
                TBookStore_NAME())
            .from(TAuthor()
                .join(TBook())
                .on(TAuthor_ID().equal(TBook_AUTHOR_ID())))
            .join(TBookToBookStore()
                .join(TBookStore())
                .on(TBookToBookStore_BOOK_STORE_NAME().equal(TBookStore_NAME())))
            .on(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
            .orderBy(TBook_ID(), TBookStore_NAME())
            .fetch();

        assertEquals(6, result1.size());
        assertEquals(asList(1, 1, 1, 2, 2, 2), result1.getValues(0));
        assertEquals(asList(1, 1, 2, 3, 3, 3), result1.getValues(1));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), result1.getValues(2));

        // Testing joining of cross products
        Result<Record3<Integer, Integer, String>> result2 = create()
            .select(
                TAuthor_ID(),
                TBook_ID(),
                TBookStore_NAME())
            .from(TAuthor()
                        .join(TBook())
                        .on(TAuthor_ID().equal(TBook_AUTHOR_ID())),
                  TBookToBookStore()
                        .join(TBookStore())
                        .on(TBookToBookStore_BOOK_STORE_NAME().equal(TBookStore_NAME())))
            .where(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
            .orderBy(TBook_ID(), TBookStore_NAME())
            .fetch();

        assertEquals(6, result2.size());
        assertEquals(asList(1, 1, 1, 2, 2, 2), result2.getValues(0));
        assertEquals(asList(1, 1, 2, 3, 3, 3), result2.getValues(1));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), result2.getValues(2));

        assertEquals(result1, result2);
    }

    public void testOuterJoin() throws Exception {
        // Test LEFT OUTER JOIN
        // --------------------
        Result<Record3<Integer, Integer, String>> result1 =
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

        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "RIGHT OUTER JOIN tests");
                break;

            default: {
                Result<Record3<Integer, Integer, String>> result2 =
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
    }

    public void testFullOuterJoin() throws Exception {

        // Test FULL OUTER JOIN
        // --------------------

        switch (dialect().family()) {
            /* [pro] */
            case ACCESS:
            case ASE:
            /* [/pro] */
            case CUBRID:
            case DERBY:
            case H2:
            case MARIADB:
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
                        .fullOuterJoin(o).on(z.field("z").cast(Integer.class).equal(o.field("o").cast(Integer.class)))
                        .fetch();

                assertEquals("z", result3.field(0).getName());
                assertEquals("o", result3.field(1).getName());

                // Interestingly, ordering doesn't work with Oracle, in this
                // example... Seems to be an Oracle bug??
                List<List<Integer>> list = asList(asList(0, null), asList(null, 1));
                assertTrue(list.contains(asList(result3.get(0).into(Integer[].class))));
                assertTrue(list.contains(asList(result3.get(1).into(Integer[].class))));
                break;
            }
        }
    }
}
