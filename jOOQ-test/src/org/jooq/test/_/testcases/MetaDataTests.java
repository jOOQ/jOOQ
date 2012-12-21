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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UpdatableRecord;
import org.jooq.UpdatableTable;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class MetaDataTests<
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
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> {

    public MetaDataTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
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
            if (getDialect() == CUBRID ||
                getDialect() == MYSQL ||
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

            // [#877] The T_877 table is only available in H2
            else if (getDialect() == H2) {
                assertEquals(tables + 2, schema.getTables().size());
            }

            // [#624] The V_INCOMPLETE view is only available in Oracle
            else if (getDialect() == ORACLE) {
                assertEquals(tables + 3, schema.getTables().size());
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
        assertEquals(Record1.class, TAuthor().getRecordType().getMethod("key").getReturnType());
        assertTrue(TAuthor().getRecordType().getMethod("key").toGenericString().contains("org.jooq.Record1<java.lang.Integer>"));
        assertEquals(Record2.class, TBookToBookStore().getRecordType().getMethod("key").getReturnType());
        assertTrue(TBookToBookStore().getRecordType().getMethod("key").toGenericString().contains("org.jooq.Record2<java.lang.String, java.lang.Integer>"));

        if (supportsReferences()) {

            // Without aliasing
            assertEquals(0, TAuthor().getReferences().size());
            assertEquals(2, TAuthor().getMainKey().getReferences().size());
            assertEquals(TBook(), TAuthor().getMainKey().getReferences().get(0).getTable());
            assertEquals(TBook(), TAuthor().getMainKey().getReferences().get(1).getTable());
            assertEquals(Arrays.asList(), TAuthor().getReferencesTo(TBook()));
            assertTrue(TBook().getReferences().containsAll(TAuthor().getReferencesFrom(TBook())));
            assertTrue(TBook().getReferences().containsAll(TBook().getReferencesFrom(TAuthor())));
            assertEquals(TBook().getReferencesTo(TAuthor()), TAuthor().getReferencesFrom(TBook()));

            // [#1460] With aliasing
            Table<A> a = TAuthor().as("a");
            Table<B> b = TBook().as("b");

            assertEquals(0, a.getReferences().size());
            assertEquals(Arrays.asList(), a.getReferencesTo(b));

            // This should work with both types of meta-models (static, non-static)
            assertEquals(TBook().getReferencesTo(TAuthor()), TBook().getReferencesTo(a));
            assertEquals(TBook().getReferencesTo(TAuthor()), b.getReferencesTo(a));
            assertEquals(TBook().getReferencesTo(TAuthor()), b.getReferencesTo(TAuthor()));

            // Only with a non-static meta model
            if (a instanceof UpdatableTable && b instanceof UpdatableTable) {
                UpdatableTable<A> ua = (UpdatableTable<A>) a;
                UpdatableTable<B> ub = (UpdatableTable<B>) b;

                assertEquals(2, ua.getMainKey().getReferences().size());
                assertEquals(TBook(), ua.getMainKey().getReferences().get(0).getTable());
                assertEquals(TBook(), ua.getMainKey().getReferences().get(1).getTable());
                assertTrue(b.getReferences().containsAll(ua.getReferencesFrom(b)));
                assertTrue(b.getReferences().containsAll(ub.getReferencesFrom(a)));
                assertEquals(b.getReferencesTo(a), ua.getReferencesFrom(b));
                assertEquals(TBook().getReferencesTo(a), ua.getReferencesFrom(b));
                assertEquals(b.getReferencesTo(a), TAuthor().getReferencesFrom(b));
            }
        }
        else {
            log.info("SKIPPING", "References tests");
        }

        // Some string data type tests
        // ---------------------------
        assertEquals(0, TAuthor_LAST_NAME().getDataType().precision());
        assertEquals(0, TAuthor_LAST_NAME().getDataType().scale());
        assertEquals(50, TAuthor_LAST_NAME().getDataType().length());

        // Some numeric data type tests
        // ----------------------------
        for (Field<?> field : T639().getFields()) {
            if ("BYTE".equalsIgnoreCase(field.getName())) {
                assertEquals(Byte.class, field.getType());
                assertEquals(SQLDataType.TINYINT, field.getDataType());
                assertEquals(3, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("SHORT".equalsIgnoreCase(field.getName())) {
                assertEquals(Short.class, field.getType());
                assertEquals(SQLDataType.SMALLINT, field.getDataType());
                assertEquals(5, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("INTEGER".equalsIgnoreCase(field.getName())) {
                assertEquals(Integer.class, field.getType());
                assertEquals(SQLDataType.INTEGER, field.getDataType());
                assertEquals(10, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("LONG".equalsIgnoreCase(field.getName())) {
                assertEquals(Long.class, field.getType());
                assertEquals(SQLDataType.BIGINT, field.getDataType());
                assertEquals(19, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("BYTE_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Byte.class, field.getType());
                assertEquals(SQLDataType.TINYINT, field.getDataType());
                assertEquals(3, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("SHORT_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Short.class, field.getType());
                assertEquals(SQLDataType.SMALLINT, field.getDataType());
                assertEquals(5, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("INTEGER_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Integer.class, field.getType());
                assertEquals(SQLDataType.INTEGER, field.getDataType());
                assertEquals(10, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("LONG_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(Long.class, field.getType());
                assertEquals(SQLDataType.BIGINT, field.getDataType());
                assertEquals(19, field.getDataType().precision());
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("BIG_INTEGER".equalsIgnoreCase(field.getName())) {
                assertEquals(BigInteger.class, field.getType());
                assertEquals(SQLDataType.DECIMAL_INTEGER.getType(), field.getDataType().getType());
                assertTrue(field.getDataType().precision() > 0);
                assertEquals(0, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }

            // [#745] TODO: Unify distinction between NUMERIC and DECIMAL
            else if ("BIG_DECIMAL".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.ORACLE
                    && getDialect() != SQLDialect.POSTGRES
                    && getDialect() != SQLDialect.SQLITE
                    && getDialect() != SQLDialect.SQLSERVER) {

                assertEquals(BigDecimal.class, field.getType());
                assertEquals(SQLDataType.DECIMAL.getType(), field.getDataType().getType());
                assertTrue(field.getDataType().precision() > 0);
                assertEquals(5, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }
            else if ("BIG_DECIMAL".equalsIgnoreCase(field.getName())) {
                assertEquals(BigDecimal.class, field.getType());
                assertEquals(SQLDataType.NUMERIC.getType(), field.getDataType().getType());
                assertTrue(field.getDataType().precision() > 0);
                assertEquals(5, field.getDataType().scale());
                assertEquals(0, field.getDataType().length());
            }

            // [#746] TODO: Interestingly, HSQLDB and MySQL match REAL with DOUBLE.
            // There is no matching type for java.lang.Float...

            // [#456] TODO: Should floating point numbers have precision and scale?
            else if ("FLOAT".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.HSQLDB
                    && getDialect() != SQLDialect.MYSQL
                    && getDialect() != SQLDialect.SYBASE) {

                assertEquals(Float.class, field.getType());
                assertEquals(SQLDataType.REAL, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
            else if ("FLOAT".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.MYSQL
                    && getDialect() != SQLDialect.SYBASE) {

                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.DOUBLE, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
            else if ("FLOAT".equalsIgnoreCase(field.getName())) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.FLOAT, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }

            // [#746] TODO: Fix this, too
            else if ("DOUBLE".equalsIgnoreCase(field.getName())
                    && getDialect() != SQLDialect.SQLSERVER
                    && getDialect() != SQLDialect.ASE) {

                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.DOUBLE, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
            else if ("DOUBLE".equalsIgnoreCase(field.getName())) {
                assertEquals(Double.class, field.getType());
                assertEquals(SQLDataType.FLOAT, field.getDataType());
                assertEquals(0, field.getDataType().length());
            }
        }
    }

    @Test
    public void testMetaData() throws Exception {
        Meta meta = create().meta();

        if (schema() != null) {

            // Catalog checks
            List<Catalog> metaCatalogs = meta.getCatalogs();
            List<Schema> metaSchemasFromCatalogs = new ArrayList<Schema>();

            for (Catalog metaCatalog : metaCatalogs) {
                metaSchemasFromCatalogs.addAll(metaCatalog.getSchemas());
            }

            assertTrue(metaSchemasFromCatalogs.contains(schema()));

            // The schema returned from meta should be equal to the
            // generated test schema
            List<Schema> metaSchemas = meta.getSchemas();
            assertTrue(metaSchemas.contains(schema()));
            assertEquals(metaSchemasFromCatalogs, metaSchemas);

            Schema metaSchema = metaSchemas.get(metaSchemas.indexOf(schema()));
            assertEquals(schema(), metaSchema);

            // The schema returned from meta should contain at least all the
            // generated test tables
            List<Table<?>> metaTables = metaSchema.getTables();
            assertTrue(metaTables.containsAll(schema().getTables()));
            assertTrue(metaTables.size() >= schema().getTables().size());

            metaTableChecks(metaTables);
        }

        // Some sample checks about tables returned from meta
        List<Table<?>> metaTables = meta.getTables();
        assertTrue(metaTables.contains(TAuthor()));
        assertTrue(metaTables.contains(TBook()));
        assertTrue(metaTables.contains(TBookStore()));
        assertTrue(metaTables.contains(TBookToBookStore()));
        assertTrue(metaTables.contains(VAuthor()));
        assertTrue(metaTables.contains(VBook()));
        assertTrue(metaTables.contains(VLibrary()));

        metaTableChecks(metaTables);
    }

    private void metaTableChecks(Collection<? extends Table<?>> metaTables) {
        for (Table<?> metaTable : metaTables) {
            Table<?> generatedTable = schema().getTable(metaTable.getName());

            // Every table returned from meta should have a corresponding
            // table by name in the generated test tables
            if (generatedTable != null) {
                assertNotNull(generatedTable);
                assertEquals(metaTable, generatedTable);

                // Check if fields match, as well
                List<Field<?>> metaFields = metaTable.getFields();
                assertTrue(metaFields.containsAll(generatedTable.getFields()));

                // Check if relations are correctly loaded (and typed) as well
                // [#1977] Fix this, once the "main key" concept has been removed
                if (generatedTable instanceof UpdatableTable && metaTable instanceof UpdatableTable) {
                    UpdatableTable<?> generatedUTable = (UpdatableTable<?>) generatedTable;
                    UpdatableTable<?> metaUTable = (UpdatableTable<?>) metaTable;

                    // [#1977] TODO: Add key checks
                }

                // Only truly updatable tables should be "Updatable"
                else {
                    assertFalse(metaTable instanceof UpdatableTable);
                }
            }
        }
    }
}
