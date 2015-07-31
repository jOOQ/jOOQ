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
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.SQLDialect.VERTICA;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;

import org.jooq.CreateTableColumnStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;
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
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public DDLTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    boolean enforcesConstraints() {
        return !asList(REDSHIFT).contains(family());
    }

    public void testCreateView() throws Exception {
        try {
            create().createView("v1").as(select(one().as("one"))).execute();
            create().createView("v2", "two").as(select(two())).execute();

            assertEquals(1, (int) create().fetchValue(select(field(name("one"), Integer.class)).from(table(name("v1")))));
            assertEquals(2, (int) create().fetchValue(select(field(name("two"), Integer.class)).from(table(name("v2")))));
        }
        finally {
            create().dropView(table(name("v1"))).execute();
            create().dropView(table(name("v2"))).execute();

            assertThrows(DataAccessException.class, () -> create().fetch(table(name("v1"))));
            assertThrows(DataAccessException.class, () -> create().fetch(table(name("v2"))));
        }
    }

    public void testDropViewIfExists() throws Exception {
        assumeFamilyNotIn(DERBY);

        create().createView("v1").as(select(one().as("one"))).execute();
        assertEquals(1, (int) create().fetchValue(select(field(name("one"), Integer.class)).from(table(name("v1")))));

        create().dropViewIfExists(table(name("v1"))).execute();
        assertThrows(DataAccessException.class, () -> create().fetch(table(name("v1"))));

        create().dropViewIfExists(table(name("v1"))).execute();
        assertThrows(DataAccessException.class, () -> create().fetch(table(name("v1"))));
    }

    public void testCreateIndex() throws Exception {
        assumeFamilyNotIn(REDSHIFT, VERTICA);

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
        assumeFamilyNotIn(REDSHIFT, VERTICA);

        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} int)", name("t"), name("a"), name("b"));
            create().createIndex("idx1").on("t", "a").execute();
            create().createIndex("idx2").on("t", "a", "b").execute();

            if (asList(MARIADB, MYSQL).contains(family()))
                create().dropIndex("idx2").on("t").execute();
            else
                create().dropIndex("idx2").execute();

            create().createIndex("idx2").on("t", "b").execute();
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testDropIndexIfExists() throws Exception {
        assumeFamilyNotIn(DERBY, HANA, REDSHIFT, VERTICA);

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
        create().dropSequence(SAuthorID()).execute();

        assertThrows(DataAccessException.class, () -> create().nextval(SAuthorID()));
    }

    public void testDropSequenceIfExists() throws Exception {
        assumeNotNull(SAuthorID());
        assumeFamilyNotIn(DERBY, HANA);

        create().dropSequenceIfExists(SAuthorID()).execute();
        create().dropSequenceIfExists(SAuthorID()).execute();

        assertThrows(DataAccessException.class, () -> create().nextval(SAuthorID()));
    }

    @SuppressWarnings("unchecked")
    public void testAlterSequence() throws Exception {
        assumeNotNull(cSequences());
        assumeFamilyNotIn(DERBY, ORACLE);

        jOOQAbstractTest.reset = false;
        Sequence<Number> S_AUTHOR_ID = (Sequence<Number>) SAuthorID();

        switch (family()) {

            // These dialects have a mandatory WITH clause
            case CUBRID:
            case FIREBIRD:
            case H2:
            case INGRES:
            case SYBASE:
            case VERTICA:
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
            create().createTable("t").column("a", SQLDataType.VARCHAR.length(10)).execute();
            create().insertInto(table(name("t")), field(name("a"))).values("A").execute();
            assertEquals(asList("A"), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").add("b", SQLDataType.INTEGER).execute();
            assertEquals(asList("A", null), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").add("c", SQLDataType.NUMERIC).execute();
            assertEquals(asList("A", null, null), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").add("d", SQLDataType.NUMERIC.precision(5)).execute();
            assertEquals(asList("A", null, null, null), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").add("e", SQLDataType.NUMERIC.precision(5, 2)).execute();
            assertEquals(asList("A", null, null, null, null), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").add("f", SQLDataType.VARCHAR).execute();
            assertEquals(asList("A", null, null, null, null, null), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").add("g", SQLDataType.VARCHAR.length(5)).execute();
            assertEquals(asList("A", null, null, null, null, null, null), asList(create().fetchOne(table(name("t"))).intoArray()));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableAlterType() throws Exception {
        assumeFamilyNotIn(FIREBIRD, HANA, SQLITE);

        try {
            // TODO: Re-use jOOQ API for this
            // Derby / Vertica doesn't support changing data types if a conversion would be needed
            create().execute("create table {0} ({1} {2})",
                name("t"),
                name("a"),
                sql(SQLDataType.VARCHAR.length(3).getCastTypeName(create().configuration()))
            );

            create().alterTable("t").alter("a").set(SQLDataType.VARCHAR.length(10)).execute();
            create().insertInto(table(name("t")), field(name("a"))).values("1234567890").execute();
            assertEquals("1234567890", create().fetchOne("select * from {0}", name("t")).getValue(0));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableAlterDefault() throws Exception {
        assumeFamilyNotIn(HANA, INFORMIX, REDSHIFT, SQLITE);

        try {
            // TODO: Re-use jOOQ API for this
            create().execute("create table {0} ({1} int, {2} " + varchar() + ")", name("t"), name("a"), name("b"));

            create().alterTable("t").alter("b").defaultValue("empty").execute();
            create().insertInto(table(name("t")), field(name("a"))).values(1).execute();
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
            create().insertInto(table(name("t")), field(name("a")), field(name("b")), field(name("c"))).values("1", "2", "3").execute();
            assertEquals(asList("1", "2", "3"), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").drop("c").execute();
            assertEquals(asList("1", "2"), asList(create().fetchOne(table(name("t"))).intoArray()));

            create().alterTable("t").drop("b").execute();
            assertEquals(asList("1"), asList(create().fetchOne(table(name("t"))).intoArray()));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testDropTable() throws Exception {

        // TODO: Re-use jOOQ API for this
        create().execute("create table {0} ({1} " + varchar() + ", {2} " + varchar() + ", {3} " + varchar() + ")", name("t"), name("a"), name("b"), name("c"));
        create().insertInto(table(name("t")), field(name("a")), field(name("b")), field(name("c"))).values("1", "2", "3").execute();
        assertEquals(asList("1", "2", "3"), asList(create().fetchOne(table(name("t"))).intoArray()));

        create().dropTable("t").execute();
        try {
            create().fetch(table(name("t")));
            fail();
        }
        catch (DataAccessException expected) {}
    }

    public void testAlterTableAddConstraint_UNIQUE() throws Exception {
        try {
            create().createTable("t")
                    .column("v1", INTEGER.nullable(false))
                    .column("v2", INTEGER.nullable(false))
                    .column("v3", INTEGER.nullable(false))
                    .execute();

            create().alterTable("t").add(constraint("u1").unique("v1")).execute();
            create().alterTable("t").add(constraint("u2").unique("v2", "v3")).execute();

            assertEquals(2,
            create().insertInto(table(name("t")), field(name("v1")), field(name("v2")), field(name("v3")))
                    .values(1, 1, 1)
                    .values(2, 1, 2)
                    .execute());

            // TODO: Query meta data to find UNIQUE keys, when this is available
            // from org.jooq.Meta

            // Violating u1
            if (enforcesConstraints()) {
                assertThrows(DataAccessException.class, () -> {
                    create().insertInto(table(name("t")), field(name("v1")), field(name("v2")), field(name("v3")))
                            .values(1, 2, 3)
                            .execute();
                });

                // Violating u2
                assertThrows(DataAccessException.class, () -> {
                    create().insertInto(table(name("t")), field(name("v1")), field(name("v2")), field(name("v3")))
                            .values(3, 1, 2)
                            .execute();
                });
            }
        }

        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableAddConstraint_CHECK() throws Exception {
        assumeFamilyNotIn(CUBRID, MARIADB, MYSQL, REDSHIFT, SQLITE, VERTICA);

        try {
            create().createTable("t").column("v", INTEGER).execute();

            create().alterTable("t").add(constraint("c").check(field(name("v")).in(inline(1), inline(2)))).execute();

            assertEquals(2,
            create().insertInto(table(name("t")), field(name("v")))
                    .values(1)
                    .values(2)
                    .execute());

            assertThrows(DataAccessException.class, () -> {
                create().insertInto(table(name("t")), field(name("v")))
                        .values(3)
                        .execute();
            });
        }

        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testAlterTableAddConstraint_PRIMARY_KEY() throws Exception {
        assumeFamilyNotIn(SQLITE);

        try {
            create().createTable("t")
                    .column("v", INTEGER.nullable(false))
                    .execute();

            create().alterTable("t").add(constraint("pk").primaryKey("v")).execute();

            assertEquals(2,
            create().insertInto(table(name("t")), field(name("v")))
                    .values(1)
                    .values(2)
                    .execute());

            UniqueKey<?> key =
            create().meta()
                .getTables()
                .stream()
                .filter(t -> "t".equals(t.getName()))
                .map(Table::getPrimaryKey)
                .findFirst()
                .get();

            assertEquals(1, key.getFields().size());
            assertEquals("v", key.getFields().get(0).getName());

            if (enforcesConstraints())
                assertThrows(DataAccessException.class, () -> {
                    create().insertInto(table(name("t")), field(name("v")))
                            .values(1)
                            .execute();
                });
        }

        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    private void foreignKeys(Runnable runnable)  {
        try {
            create().createTable("t1").column("v", INTEGER.nullable(false)).execute();
            create().createTable("t2").column("w", INTEGER).execute();
            create().alterTable("t1").add(constraint("pk").primaryKey("v")).execute();

            assertEquals(2,
            create().insertInto(table(name("t1")), field(name("v")))
                    .values(1)
                    .values(2)
                    .execute());

            assertEquals(1,
            create().insertInto(table(name("t2")), field(name("w")))
                    .values(1)
                    .execute());

            runnable.run();
        }

        finally {
            ignoreThrows(() -> create().dropTable("t2").execute());
            ignoreThrows(() -> create().dropTable("t1").execute());
        }
    }

    public void testAlterTableAddConstraint_FOREIGN_KEY() throws Exception {
        assumeFamilyNotIn(SQLITE);

        foreignKeys(() -> {
            create().alterTable("t2").add(
                constraint("t2_fk").foreignKey("w").references("t1", "v")
            ).execute();

            assertThrows(DataAccessException.class, () -> {
                create().insertInto(table(name("t2")), field(name("w")))
                        .values(5)
                        .execute();
            });
        });
    }

    public void testAlterTableAddConstraint_FOREIGN_KEY_ON_CLAUSES() throws Exception {
        assumeFamilyNotIn(SQLITE);

        foreignKeys(() -> {
            create().alterTable("t2").add(
                constraint("t2_fk").foreignKey("w").references("t1", "v").onDeleteCascade()
            ).execute();

            create().delete(table(name("t1"))).execute();
            assertEquals(0, create().fetchCount(table(name("t1"))));
            assertEquals(0, create().fetchCount(table(name("t2"))));
        });

        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onDeleteRestrict()
                ).execute();

                assertThrows(DataAccessException.class, () -> {
                    create().delete(table(name("t1"))).execute();
                });
            });
        }, ORACLE, SQLSERVER);

        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onDeleteNoAction()
                ).execute();

                assertThrows(DataAccessException.class, () -> {
                    create().delete(table(name("t1"))).execute();
                });
            });
        }, ORACLE);

        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").alterColumn("w").defaultValue(2).execute();
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onDeleteSetDefault()
                ).execute();

                create().delete(table(name("t1"))).where(field(name("v")).eq(1)).execute();
                assertEquals(1, create().fetchCount(table(name("t1"))));
                assertEquals(2, create().fetchOne(table(name("t2"))).getValue(0));
            });

        // Currently not supported by Derby (https://issues.apache.org/jira/browse/DERBY-6813)
        }, DERBY, ORACLE);

        foreignKeys(() -> {
            create().alterTable("t2").add(
                constraint("t2_fk").foreignKey("w").references("t1", "v").onDeleteSetNull()
            ).execute();

            create().delete(table(name("t1"))).where(field(name("v")).eq(1)).execute();
            assertEquals(1, create().fetchCount(table(name("t1"))));
            assertNull(create().fetchOne(table(name("t2"))).getValue(0));
        });

        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onUpdateCascade()
                ).execute();

                create().update(table(name("t1"))).set(field(name("v")), 0).where(field(name("v")).eq(1)).execute();
                assertEquals(2, create().fetchCount(table(name("t1"))));
                assertEquals(0, create().fetchOne(table(name("t2"))).getValue(0, int.class));
            });
        }, DERBY, ORACLE);


        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onUpdateRestrict()
                ).execute();

                assertThrows(DataAccessException.class, () -> {
                    create().update(table(name("t1"))).set(field(name("v")), 2).where(field(name("v")).eq(1)).execute();
                });
            });
        }, ORACLE, SQLSERVER);


        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onUpdateNoAction()
                ).execute();

                assertThrows(DataAccessException.class, () -> {
                    create().update(table(name("t1"))).set(field(name("v")), 2).where(field(name("v")).eq(1)).execute();
                });
            });
        }, ORACLE);

        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").alterColumn("w").defaultValue(2).execute();
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onUpdateSetDefault()
                ).execute();

                create().update(table(name("t1"))).set(field(name("v")), 0).where(field(name("v")).eq(1)).execute();
                assertEquals(2, create().fetchCount(table(name("t1"))));
                assertEquals(2, create().fetchOne(table(name("t2"))).getValue(0, int.class));
            });
        }, DERBY, ORACLE);

        skipForFamilies(() -> {
            foreignKeys(() -> {
                create().alterTable("t2").add(
                    constraint("t2_fk").foreignKey("w").references("t1", "v").onUpdateSetNull()
                ).execute();

                create().update(table(name("t1"))).set(field(name("v")), 0).where(field(name("v")).eq(1)).execute();
                assertEquals(2, create().fetchCount(table(name("t1"))));
                assertNull(create().fetchOne(table(name("t2"))).getValue(0));
            });
        }, DERBY, ORACLE);
    }

    public void testAlterTableDropConstraint() throws Exception {
        assumeFamilyNotIn(SQLITE);

        try {
            create().createTable("t").column("v", INTEGER.nullable(false)).execute();
            create().alterTable("t").add(constraint("x").unique("v")).execute();

            assertThrows(DataAccessException.class, () -> {
                create().insertInto(table(name("t")), field(name("v")))
                        .values(1)
                        .values(1)
                        .execute();
            });

            create().alterTable("t").dropConstraint("x").execute();
            assertEquals(2,
            create().insertInto(table(name("t")), field(name("v")))
                    .values(1)
                    .values(1)
                    .execute());

            assertSame(asList(1, 1), create().fetch(table(name("t"))).getValues(0, int.class));
        }

        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testDropTableIfExists() throws Exception {
        assumeFamilyNotIn(DERBY, HANA, INFORMIX);

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
                    .column(field(name("t", "i"), Integer.class), SQLDataType.INTEGER)
                    .column("n", SQLDataType.DECIMAL.precision(3, 1).nullable(true))
                    .column("s", SQLDataType.VARCHAR.length(5).nullable(false))
                    .execute();

            assertEquals(1,
            create().insertInto(table(name("t")), field(name("i")), field(name("n")), field(name("s")))
                    .values(1, new BigDecimal("10.5"), "abcde")
                    .execute());

            Result<Record> r1 = create().selectFrom(table(name("t"))).fetch();
            assertEquals(1, r1.size());
            assertEquals(3, r1.fields().length);
            assertEquals("i", r1.field(0).getName());
            assertEquals("n", r1.field(1).getName());
            assertEquals("s", r1.field(2).getName());
            assertEquals(asList("1", "10.5", "abcde"), asList(r1.get(0).into(String[].class)));

            // Checking of NOT NULL constraints
            assertEquals(1,
            create().insertInto(table(name("t")), field(name("i"), int.class), field(name("n"), BigDecimal.class), field(name("s"), String.class))
                    .values(null, null, "abcde")
                    .execute());

            try {
                create().insertInto(table(name("t")), field(name("i")), field(name("n")), field(name("s")))
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

    public void testCreateTableAllDataTypes() throws Exception {
        try {
            CreateTableColumnStep step =
            create().createTable("t")
                    .column("id", SQLDataType.INTEGER);

            int i = 1;

            step = step.column("c" + i++, SQLDataType.BIGINT);
            step = step.column("c" + i++, SQLDataType.BIGINTUNSIGNED);
            step = step.column("c" + i++, SQLDataType.BINARY);
            step = step.column("c" + i++, SQLDataType.BIT);
            step = step.column("c" + i++, SQLDataType.BLOB);
            step = step.column("c" + i++, SQLDataType.BOOLEAN);
            step = step.column("c" + i++, SQLDataType.CHAR);
            step = step.column("c" + i++, SQLDataType.CLOB);
            step = step.column("c" + i++, SQLDataType.BLOB);
            step = step.column("c" + i++, SQLDataType.DATE);
            step = step.column("c" + i++, SQLDataType.DECIMAL);
            step = step.column("c" + i++, SQLDataType.DECIMAL_INTEGER);
            step = step.column("c" + i++, SQLDataType.DOUBLE);
            step = step.column("c" + i++, SQLDataType.FLOAT);
            step = step.column("c" + i++, SQLDataType.INTEGER);
            step = step.column("c" + i++, SQLDataType.INTEGERUNSIGNED);
            step = step.column("c" + i++, SQLDataType.REAL);
            step = step.column("c" + i++, SQLDataType.SMALLINT);
            step = step.column("c" + i++, SQLDataType.SMALLINTUNSIGNED);
            step = step.column("c" + i++, SQLDataType.TIME);
            step = step.column("c" + i++, SQLDataType.TIMESTAMP);
            step = step.column("c" + i++, SQLDataType.TINYINT);
            step = step.column("c" + i++, SQLDataType.TINYINTUNSIGNED);
            step = step.column("c" + i++, SQLDataType.VARBINARY);
            step = step.column("c" + i++, SQLDataType.VARCHAR);

            step.execute();

            Result<?> result = create().fetch(table(name("t")));
            assertEquals(0, result.size());
            assertEquals(i, result.fields().length);
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testCreateTableAsSelect() throws Exception {
        assumeFamilyNotIn(DERBY, FIREBIRD, SYBASE);

        try {
            create().createTable("t").as(
                select(val("value").as("value"))
            ).execute();
            Result<Record> r1 = create().selectFrom(table(name("t"))).fetch();

            assertEquals(1, r1.size());
            assertEquals(1, r1.fields().length);
            assertEquals("value", r1.field(0).getName());
            assertEquals("value", r1.get(0).getValue(0));
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    public void testCreateGlobalTemporaryTable() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INFORMIX, INGRES, MARIADB, MYSQL, SQLITE, SQLSERVER, SYBASE);

        try {
            create().createGlobalTemporaryTable("t1").column("f1", SQLDataType.INTEGER).execute();
            create().createGlobalTemporaryTable("s1").as(select(one().as("x1"))).execute();
            assertEquals("f1", create().fetch(table(name("t1"))).field(0).getName());
            assertEquals("x1", create().fetch(table(name("s1"))).field(0).getName());

            if (asList(ORACLE, POSTGRES).contains(family())) {
                create().createGlobalTemporaryTable("t2").column("f2", SQLDataType.INTEGER).onCommitDeleteRows().execute();
                create().createGlobalTemporaryTable("t3").column("f3", SQLDataType.INTEGER).onCommitPreserveRows().execute();

                create().createGlobalTemporaryTable("s2").as(select(one().as("x2"))).onCommitDeleteRows().execute();
                create().createGlobalTemporaryTable("s3").as(select(one().as("x3"))).onCommitPreserveRows().execute();

                assertEquals("f2", create().fetch(table(name("t2"))).field(0).getName());
                assertEquals("f3", create().fetch(table(name("t3"))).field(0).getName());
                assertEquals("x2", create().fetch(table(name("s2"))).field(0).getName());
                assertEquals("x3", create().fetch(table(name("s3"))).field(0).getName());
            }

        }
        finally {
            ignoreThrows(() -> create().dropTable("t1").execute());
            ignoreThrows(() -> create().dropTable("s1").execute());

            if (asList(ORACLE, POSTGRES).contains(family())) {
                ignoreThrows(() -> create().dropTable("t2").execute());
                ignoreThrows(() -> create().dropTable("t3").execute());

                ignoreThrows(() -> create().dropTable("s2").execute());

                // Oracle's ON COMMIT PRESERVE ROWS GTTs need truncation before dropping
                ignoreThrows(() -> create().truncate("s3").execute());
                ignoreThrows(() -> create().dropTable("s3").execute());
            }
        }
    }

    public void testSelectInto() throws Exception {
        assumeFamilyNotIn(DERBY, FIREBIRD, SYBASE);

        try {
            create().select(inline("value").as("value")).into(table(name("t"))).execute();
            Result<Record> result = create().selectFrom(table(name("t"))).fetch();

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
