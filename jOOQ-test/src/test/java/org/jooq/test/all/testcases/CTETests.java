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
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.iterate;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.VERTICA;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.tools.StringUtils.leftPad;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.jooq.CommonTableExpression;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

/**
 * @author Lukas Eder
 */
public class CTETests<
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

    public CTETests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testCTESimple() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        Result<Record> result1 =
        create().with("t", "f1", "f2").as(select(val(1, Integer.class), val("a")))
                .select()
                .from(table(name("t")))
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(2, result1.fields().length);
        assertEquals("f1", result1.field(0).getName());
        assertEquals("f2", result1.field(1).getName());
     // assertEquals(Integer.class, result1.field(0).getType());
        assertEquals(String.class, result1.field(1).getType());
        assertEquals(1, (int) result1.get(0).getValue(0, Integer.class));
        assertEquals("a", result1.getValue(0, 1));
    }

    public void testCTEMultiple() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        CommonTableExpression<Record2<Integer, String>> t1 = name("t1").fields("f1", "f2").as(select(val(1), val("a")));
        CommonTableExpression<Record2<Integer, String>> t2 = name("t2").fields("f3", "f4").as(select(val(2), val("b")));

        Result<Record2<Integer, String>> result2 =
        create().with(t1)
                .with(t2)
                .select(
                    t1.field("f1", Integer.class).add(t2.field("f3", Integer.class)).as("add"),
                    t1.field("f2", String.class).concat(t2.field("f4", String.class)).as("concat"))
                .from(t1, t2)
                .fetch();

        assertEquals(1, result2.size());
        assertEquals(2, result2.fields().length);
        assertEquals("add", result2.field(0).getName());
        assertEquals("concat", result2.field(1).getName());
        assertEquals(Integer.class, result2.field(0).getType());
        assertEquals(Short.class, result2.field(0, Short.class).getType());
        assertEquals(String.class, result2.field(1).getType());
        assertEquals(Integer.class, result2.field(1, Integer.class).getType());
        assertEquals(3, result2.getValue(0, 0));
        assertEquals("ab", result2.getValue(0, 1));


        // Try again but this time with varags CTE lists
        assertEquals(result2,
            create().with(t1, t2)
                    .select(
                        t1.field("f1").add(t2.field("f3")).as("add"),
                        t1.field("f2").concat(t2.field("f4")).as("concat"))
                    .from(t1, t2)
                    .fetch()
        );
    }

    public void testCTEAliasing() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        CommonTableExpression<Record2<Integer, String>> t1 = name("t1").fields("f1", "f2").as(select(val(1), val("a")));
        CommonTableExpression<Record2<Integer, String>> t2 = name("t2").fields("f3", "f4").as(select(val(2), val("b")));

        // Try renaming the CTEs when referencing them
        Table<Record2<Integer, String>> a1 = t1.as("a1");
        Table<Record2<Integer, String>> a2 = t2.as("a2");

        Result<?> result3 =
        create().with(t1)
                .with(t2)
                .select(
                    a1.field("f1").add(a2.field("f3")).as("add"),
                    a1.field("f2").concat(a2.field("f4")).as("concat"))
                .from(a1, a2)
                .fetch();

        assertEquals(1, result3.size());
        assertEquals(2, result3.fields().length);
        assertEquals("add", result3.field(0).getName());
        assertEquals("concat", result3.field(1).getName());
        assertEquals(Integer.class, result3.field(0).getType());
        assertEquals(String.class, result3.field(1).getType());
        assertEquals(3, result3.getValue(0, 0));
        assertEquals("ab", result3.getValue(0, 1));

        // Try renaming the CTEs and their columns when referencing them
        Table<Record2<Integer, String>> b1 = t1.as("a1", "i1", "s1");
        Table<Record2<Integer, String>> b2 = t2.as("a2", "i2", "s2");

        Result<?> result4 =
        create().with(t1)
                .with(t2)
                .select(
                    b1.field("i1").add(b2.field("i2")).as("add"),
                    b1.field("s1").concat(b2.field("s2")).as("concat"))
                .from(b1, b2)
                .fetch();

        assertEquals(1, result4.size());
        assertEquals(2, result4.fields().length);
        assertEquals("add", result4.field(0).getName());
        assertEquals("concat", result4.field(1).getName());
        assertEquals(Integer.class, result4.field(0).getType());
        assertEquals(String.class, result4.field(1).getType());
        assertEquals(3, result4.getValue(0, 0));
        assertEquals("ab", result4.getValue(0, 1));

    }

    public void testCTEWithNoExplicitColumnLists() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        Result<Record> result1 =
        create().with("a").as(select(
                                val(1, Integer.class).as("x"),
                                val("a").as("y")
                             ))
                .select()
                .from(table(name("a")))
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(2, result1.fields().length);
        assertEquals("x", result1.field(0).getName());
        assertEquals("y", result1.field(1).getName());
        // Not all databases will deserialise this as Integer, e.g. Oracle (BigDecimal)
        assertTrue(Number.class.isAssignableFrom(result1.field(0).getType()));
        assertEquals(String.class, result1.field(1).getType());
        assertEquals(1, (int) result1.get(0).getValue(0, Integer.class));
        assertEquals("a", result1.getValue(0, 1));
    }

    public void testRecursiveCTESimple() throws Exception {

        // This is currently the only use case supported by H2
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, HANA, INFORMIX, INGRES, MARIADB, MYSQL, REDSHIFT, SQLITE, VERTICA);

        CommonTableExpression<Record2<Integer, String>> t1 =
        name("t1").fields("f1", "f2").as(
            select(
                inline(1),

                // SQL Server is a bit restrictive, here:
                // Types don't match between the anchor and the recursive part in column "f2" of recursive query "t1".
                inline("a").cast(VARCHAR.length(15))
            )
            .unionAll(
                select(
                    field(name("t1", "f1"), Integer.class).add(inline(1)),
                    field(name("t1", "f2"), String.class).concat(inline("a")).cast(VARCHAR.length(15))
                )
                .from(table(name("t1")))
                // H2 support is *very* experimental...
                // https://groups.google.com/d/msg/h2-database/OJfqNF_Iqyo/brxu-Lu3c78J
                .where(field(name("t1", "f1")).lt(inline(10)))
            )
        );

        Result<Record> result=
        create().withRecursive(t1)
                .select()
                .from(t1)
                .fetch();

        assertEquals(10, result.size());
        assertEquals(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result.getValues(0, int.class));
        assertEquals(iterate(1, i -> i + 1)
                         .limit(10)
                         .map(i -> leftPad("", i, "a"))
                         .collect(toList()),
                     result.getValues(1));
    }

    public void testRecursiveCTEMultiple() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, REDSHIFT, SQLITE, VERTICA);

        CommonTableExpression<Record2<Integer, String>> t1 =
        name("t1").fields("f1", "f2").as(
            select(
                val(1),

                // And we don't want to reach the end of the DB2 mainframe tape, here...
                // A temporary table could not be created because there is no available system temporary table space that has a compatible page size.. SQLCODE=-1585, SQLSTATE=54048, DRIVER=4.7.85
                val("a").cast(VARCHAR.length(15))
            )
            .unionAll(
                select(
                    field(name("t1", "f1"), Integer.class).add(1),
                    field(name("t1", "f2"), String.class).concat("a").cast(VARCHAR.length(15))
                )
                .from(table(name("t1")))
                .where(field(name("t1", "f1")).lt(10))
            )
        );

        CommonTableExpression<Record2<Integer, String>> t2 =
        name("t2").fields("g1", "g2").as(
            select(
                val(1),
                val("b").cast(VARCHAR.length(15))
            )
            .unionAll(
                select(
                    field(name("t2", "g1"), Integer.class).add(1),
                    field(name("t2", "g2"), String.class).concat("b").cast(VARCHAR.length(15))
                )
                .from(table(name("t2")))
                .where(field(name("t2", "g1")).lt(10))
            )
        );

        Result<Record> result=
        create().withRecursive(t1, t2)
                .select()
                .from(t1)
                .join(t2)
                .on(field(name("t1", "f1")).eq(field(name("t2", "g1"))))
                .fetch();

        assertEquals(10, result.size());
        assertEquals(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result.getValues(0, int.class));
        assertEquals(iterate(1, i -> i + 1)
                         .limit(10)
                         .map(i -> leftPad("", i, "a"))
                         .collect(toList()),
                     result.getValues(1));
        assertEquals(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result.getValues(2, int.class));
        assertEquals(iterate(1, i -> i + 1)
                        .limit(10)
                        .map(i -> leftPad("", i, "b"))
                        .collect(toList()),
                     result.getValues(3));
    }

    public void testCTEWithLimit() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        CommonTableExpression<Record3<String, String, Integer>> t1 = name("t1").as(
            select(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME(), TBook_ID())
            .from(TAuthor())
            .join(TBook())
            .on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
            .orderBy(TBook_ID())
            .limit(3)
        );

        Result<Record> result =
        create().with(t1)
                .select(t1.fields())
                .from(t1)
                .orderBy(t1.field(2).desc())
                .limit(2)
                .fetch();

        assertEquals(2, result.size());
        assertEquals(asList("Paulo", "George"), result.getValues(0));
        assertEquals(asList("Coelho", "Orwell"), result.getValues(1));
        assertEquals(asList(3, 2), result.getValues(2, int.class));
    }

    public void testCTEWithLimitOffset() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        CommonTableExpression<Record3<String, String, Integer>> t1 = name("t1").as(
            select(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME(), TBook_ID())
            .from(TAuthor())
            .join(TBook())
            .on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
            .orderBy(TBook_ID())
            .limit(3)
            .offset(1)
        );

        Result<Record> result =
        create().with(t1)
                .select(t1.fields())
                .from(t1)
                .orderBy(t1.field(2).desc())
                .limit(2)
                .offset(1)
                .fetch();

        assertEquals(2, result.size());
        assertEquals(asList("Paulo", "George"), result.getValues(0));
        assertEquals(asList("Coelho", "Orwell"), result.getValues(1));
        assertEquals(asList(3, 2), result.getValues(2, int.class));
    }

    public void testCTEWithDML() {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DERBY, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE);

        clean(TDates());

        CommonTableExpression<Record1<Integer>> cte = name("t1").as(
            select(TBook_ID())
            .from(TBook())
            .where(TBook_AUTHOR_ID().eq(1))
        );

        assertEquals(2,
        create().with(cte)
                .insertInto(TDates())
                .columns(TDates_ID())
                .select(selectFrom(cte))
                .execute());

        assertEquals(asList(1, 2), create().fetchValues(select(TDates_ID()).from(TDates()).orderBy(TDates_ID())));

        assertEquals(2,
        create().with(cte)
                .update(TDates())
                .set(TDates_D(), Date.valueOf("2000-01-01"))
                .whereExists(selectOne().from(cte).where(TDates_ID().eq(cte.field(TBook_ID()))))
                .execute());

        assertEquals(nCopies(2, Date.valueOf("2000-01-01")), create().fetchValues(select(TDates_D()).from(TDates()).orderBy(TDates_ID())));

        assertEquals(2,
        create().with(cte)
                .delete(TDates())
                .whereExists(selectOne().from(cte).where(TDates_ID().eq(cte.field(TBook_ID()))))
                .execute());

        assertEquals(0, create().fetchCount(TDates()));
    }
}
