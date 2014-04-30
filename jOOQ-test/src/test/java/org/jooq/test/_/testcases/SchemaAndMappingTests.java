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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.jooq.impl.DSL.sum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.test.BaseTest;
import org.jooq.test.MySQLTestSchemaRewrite;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class SchemaAndMappingTests<
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

    public SchemaAndMappingTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testTableMapping() throws Exception {
        Settings settings = new Settings()
            .withRenderMapping(new RenderMapping()
            .withSchemata(new MappedSchema()
            .withInput(TAuthor().getSchema() == null ? "" : TAuthor().getSchema().getName())
            .withTables(
                new MappedTable().withInput(TAuthor().getName()).withOutput(VAuthor().getName()),
                new MappedTable().withInput(TBook().getName()).withOutput(VBook().getName()))));

        Select<Record1<String>> q =
        create(settings).select(TBook_TITLE())
                        .from(TAuthor())
                        .join(TBook())
                        .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                        .orderBy(TBook_ID().asc());

        // Assure T_* is replaced by V_*
        assertTrue(create(settings).render(q).contains(VAuthor().getName()));
        assertTrue(create(settings).render(q).contains(VBook().getName()));
        assertFalse(create(settings).render(q).contains(TAuthor().getName()));
        assertFalse(create(settings).render(q).contains(TBook().getName()));

        // Assure that results are correct
        Result<Record1<String>> result = q.fetch();
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));
    }

    public void testSchemaMapping() throws Exception {
        switch (dialect()) {
            case FIREBIRD:
            case SQLITE:
                log.info("SKIPPING", "SchemaMapping tests");
                return;
        }

        // Map to self. This will work even for single-schema RDBMS
        // ---------------------------------------------------------------------
        Settings settings = new Settings()
            .withRenderMapping(new RenderMapping()
            .withSchemata(new MappedSchema()
            .withInput(TAuthor().getSchema().getName())
            .withOutput(TAuthor().getSchema().getName())
            .withTables(
                new MappedTable().withInput(TAuthor().getName()).withOutput(TAuthor().getName()),
                new MappedTable().withInput(TBook().getName()).withOutput(TBook().getName()))));

        Select<Record1<String>> query =
        create(settings).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        Result<Record1<String>> result = query.fetch();

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
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case CUBRID:
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "Schema mapping test");
                return;

            // Currently, only MySQL is tested with SchemaMapping
            case MYSQL:

                // But not when the schema is already re-written
                if (delegate.getClass() == MySQLTestSchemaRewrite.class) {
                    log.info("SKIPPING", "Schema mapping test");
                    return;
                }
        }

        // Map to a second schema
        // ---------------------------------------------------------------------
        settings = new Settings()
            .withRenderMapping(new RenderMapping()
            .withSchemata(new MappedSchema()
            .withInput(TAuthor().getSchema().getName())
            .withOutput(TAuthor().getSchema().getName() + "2")));

        Select<Record1<String>> q1 =
        create(settings).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        // Assure that schema is replaced
        assertTrue(create(settings).render(q1).contains(TAuthor().getSchema().getName() + "2"));
        assertTrue(q1.getSQL().contains(TAuthor().getSchema().getName() + "2"));
        assertEquals(create(settings).render(q1), q1.getSQL());

        // Assure that results are correct
        Result<Record1<String>> result1 = q1.fetch();
        assertEquals("1984", result1.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result1.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result1.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result1.getValue(3, TBook_TITLE()));

        // [#995] Schema mapping in stored functions
        // -----------------------------------------
        Field<Integer> f1 = FOneField().cast(Integer.class);
        Field<Integer> f2 = FNumberField(42).cast(Integer.class);

        Select<Record2<Integer, Integer>> q2 =
        create(settings).select(f1, f2);

        // Assure that schema is replaced
        assertTrue(create(settings).render(q2).contains(TAuthor().getSchema().getName() + "2"));
        assertTrue(q2.getSQL().contains(TAuthor().getSchema().getName() + "2"));
        assertEquals(create(settings).render(q2), q2.getSQL());

        // Assure that results are correct
        Record record = q2.fetchOne();
        assertEquals(1, (int) record.getValue(f1));
        assertEquals(42, (int) record.getValue(f2));

        // Map both schema AND tables
        // --------------------------
        settings = new Settings()
            .withRenderMapping(new RenderMapping()
            .withSchemata(new MappedSchema()
            .withInput(TAuthor().getSchema().getName())
            .withOutput(TAuthor().getSchema().getName() + "2")
            .withTables(
                new MappedTable().withInput(TAuthor().getName()).withOutput(VAuthor().getName()),
                new MappedTable().withInput(TBook().getName()).withOutput(VBook().getName()))));

        Select<Record1<String>> q3 =
        create(settings).select(TBook_TITLE())
                       .from(TAuthor())
                       .join(TBook())
                       .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                       .orderBy(TBook_ID().asc());

        // Assure T_* is replaced by V_*
        assertTrue(create(settings).render(q3).contains(VAuthor().getName()));
        assertTrue(create(settings).render(q3).contains(VBook().getName()));
        assertTrue(create(settings).render(q3).contains("test2"));
        assertFalse(create(settings).render(q3).contains(TAuthor().getName()));
        assertFalse(create(settings).render(q3).contains(TBook().getName()));

        // Assure that results are correct
        Result<Record1<String>> result3 = q3.fetch();
        assertEquals("1984", result3.getValue(0, TBook_TITLE()));
        assertEquals("Animal Farm", result3.getValue(1, TBook_TITLE()));
        assertEquals("O Alquimista", result3.getValue(2, TBook_TITLE()));
        assertEquals("Brida", result3.getValue(3, TBook_TITLE()));
    }

    public void testMultiSchemaQueries() {
        if (TBookSale() == null) {
            log.info("SKIPPING", "Multi-schema query tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        // Intial test, table is empty
        // ---------------------------
        assertEquals(0, create().selectCount().from(TBookSale()).fetchOne(0));

        // Testing adding records to another schema's table
        // ------------------------------------------------
        BS sale = create().newRecord(TBookSale());
        sale.setValue(TBookSale_ID(), 1);
        sale.setValue(TBookSale_BOOK_ID(), 1);
        sale.setValue(TBookSale_BOOK_STORE_NAME(), "Orell Füssli");
        sale.setValue(TBookSale_SOLD_AT(), new Date(0));
        sale.setValue(TBookSale_SOLD_FOR(), new BigDecimal("3.50"));
        assertEquals(1, sale.store());
        assertEquals(1, create().selectCount().from(TBookSale()).fetchOne(0));

        sale = sale.copy();
        sale.setValue(TBookSale_ID(), 2);
        sale.setValue(TBookSale_SOLD_FOR(), new BigDecimal("7.50"));
        assertEquals(1, sale.store());
        assertEquals(2, create().selectCount().from(TBookSale()).fetchOne(0));

        sale = sale.copy();
        sale.setValue(TBookSale_ID(), 3);
        sale.setValue(TBookSale_BOOK_STORE_NAME(), "Ex Libris");
        sale.setValue(TBookSale_SOLD_FOR(), new BigDecimal("8.50"));
        assertEquals(1, sale.store());
        assertEquals(3, create().selectCount().from(TBookSale()).fetchOne(0));

        // Test joining tables across schemas
        // ----------------------------------
        Result<Record3<String, String, BigDecimal>> result1 =
        create().select(
                    TBook_TITLE(),
                    TBookSale_BOOK_STORE_NAME(),
                    sum(TBookSale_SOLD_FOR()))
                .from(TBook()
                    .join(TBookSale())
                    .on(TBook_ID().equal(TBookSale_BOOK_ID())))
                .groupBy(
                    TBook_ID(),
                    TBook_TITLE(),
                    TBookSale_BOOK_STORE_NAME())
                .orderBy(
                    TBook_ID().asc(),
                    sum(TBookSale_SOLD_FOR()).desc())
                .fetch();

        assertEquals(2, result1.size());
        assertEquals(nCopies(2, "1984"), result1.getValues(TBook_TITLE()));
        assertEquals(asList("Orell Füssli", "Ex Libris"), result1.getValues(TBookSale_BOOK_STORE_NAME()));
        assertEquals(asList(new BigDecimal("11"), new BigDecimal("8.5")), result1.getValues(sum(TBookSale_SOLD_FOR())));

        // Test joining "onKey"
        // --------------------
        Result<Record3<String, String, BigDecimal>> result2 =
        create().select(
                    TBook_TITLE(),
                    TBookSale_BOOK_STORE_NAME(),
                    sum(TBookSale_SOLD_FOR()))
                .from(TBook()
                    .join(TBookToBookStore()
                        .join(TBookSale())
                        .onKey())
                    .onKey())
                .groupBy(
                    TBook_ID(),
                    TBook_TITLE(),
                    TBookSale_BOOK_STORE_NAME())
                .orderBy(
                    TBook_ID().asc(),
                    sum(TBookSale_SOLD_FOR()).desc())
                .fetch();

        assertEquals(result1, result2);
    }
}
