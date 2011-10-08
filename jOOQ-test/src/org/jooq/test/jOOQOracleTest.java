/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

package org.jooq.test;

import static junit.framework.Assert.assertEquals;
import static org.jooq.test.oracle.generatedclasses.tables.VAuthor.V_AUTHOR;
import static org.jooq.test.oracle.generatedclasses.tables.VBook.V_BOOK;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SchemaMapping;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.UpdatableTable;
import org.jooq.test.oracle.generatedclasses.Routines;
import org.jooq.test.oracle.generatedclasses.Sequences;
import org.jooq.test.oracle.generatedclasses.TestFactory;
import org.jooq.test.oracle.generatedclasses.packages.Library;
import org.jooq.test.oracle.generatedclasses.routines.F377;
import org.jooq.test.oracle.generatedclasses.tables.TArrays;
import org.jooq.test.oracle.generatedclasses.tables.TAuthor;
import org.jooq.test.oracle.generatedclasses.tables.TBook;
import org.jooq.test.oracle.generatedclasses.tables.TBookStore;
import org.jooq.test.oracle.generatedclasses.tables.TDirectory;
import org.jooq.test.oracle.generatedclasses.tables.TTriggers;
import org.jooq.test.oracle.generatedclasses.tables.T_639NumbersTable;
import org.jooq.test.oracle.generatedclasses.tables.T_658Ref;
import org.jooq.test.oracle.generatedclasses.tables.T_725LobTest;
import org.jooq.test.oracle.generatedclasses.tables.T_785;
import org.jooq.test.oracle.generatedclasses.tables.VIncomplete;
import org.jooq.test.oracle.generatedclasses.tables.VLibrary;
import org.jooq.test.oracle.generatedclasses.tables.records.TArraysRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.TDirectoryRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.T_658RefRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.T_785Record;
import org.jooq.test.oracle.generatedclasses.tables.records.VIncompleteRecord;
import org.jooq.test.oracle.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.oracle.generatedclasses.udt.OInvalidType;
import org.jooq.test.oracle.generatedclasses.udt.TInvalidType;
import org.jooq.test.oracle.generatedclasses.udt.UAddressType;
import org.jooq.test.oracle.generatedclasses.udt.UInvalidType;
import org.jooq.test.oracle.generatedclasses.udt.UStreetType;
import org.jooq.test.oracle.generatedclasses.udt.records.OInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.udt.records.TInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.udt.records.UInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.udt.records.UNumberArrayRecord;
import org.jooq.test.oracle.generatedclasses.udt.records.UNumberLongArrayRecord;
import org.jooq.test.oracle.generatedclasses.udt.records.UStringArrayRecord;
import org.jooq.util.oracle.OracleDataType;
import org.jooq.util.oracle.OracleFactory;
import org.junit.Test;


/**
 * @author Lukas Eder
 */
public class jOOQOracleTest extends jOOQAbstractTest<
        TAuthorRecord,
        TBookRecord,
        TBookStoreRecord,
        VLibraryRecord,
        TArraysRecord,
        TDirectoryRecord,
        TTriggersRecord,
        T_658RefRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record> {

    static {
        // [#624] Incomplete or erroneous artefacts must be generated too. This
        // won't compile, if there is anything wrong with code generation of
        // incomplete or erroneous artefacts

        try {
            Class.forName(VIncomplete.class.getName());
            Class.forName(VIncompleteRecord.class.getName());
            Class.forName(F377.class.getName());
            Class.forName(UInvalidType.class.getName());
            Class.forName(UInvalidTypeRecord.class.getName());
            Class.forName(TInvalidType.class.getName());
            Class.forName(TInvalidTypeRecord.class.getName());
            Class.forName(OInvalidType.class.getName());
            Class.forName(OInvalidTypeRecord.class.getName());
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected TestFactory create(SchemaMapping mapping) {
        return new TestFactory(getConnection(), mapping);
    }

    @Override
    protected UpdatableTable<TAuthorRecord> TAuthor() {
        return TAuthor.T_AUTHOR;
    }

    @Override
    protected TableField<TAuthorRecord, String> TAuthor_LAST_NAME() {
        return TAuthor.LAST_NAME;
    }

    @Override
    protected TableField<TAuthorRecord, String> TAuthor_FIRST_NAME() {
        return TAuthor.FIRST_NAME;
    }

    @Override
    protected TableField<TAuthorRecord, Date> TAuthor_DATE_OF_BIRTH() {
        return TAuthor.DATE_OF_BIRTH;
    }

    @Override
    protected TableField<TAuthorRecord, Integer> TAuthor_YEAR_OF_BIRTH() {
        return TAuthor.YEAR_OF_BIRTH;
    }

    @Override
    protected TableField<TAuthorRecord, Integer> TAuthor_ID() {
        return TAuthor.ID;
    }

    @Override
    protected TableField<TAuthorRecord, ? extends UDTRecord<?>> TAuthor_ADDRESS() {
        return TAuthor.ADDRESS;
    }

    @Override
    protected UpdatableTable<TBookRecord> TBook() {
        return TBook.T_BOOK;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_ID() {
        return TBook.ID;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_AUTHOR_ID() {
        return TBook.AUTHOR_ID;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_TITLE() {
        return TBook.TITLE;
    }

    @Override
    protected UpdatableTable<TBookStoreRecord> TBookStore() {
        return TBookStore.T_BOOK_STORE;
    }

    @Override
    protected TableField<TBookStoreRecord, String> TBookStore_NAME() {
        return TBookStore.NAME;
    }

    @Override
    protected Table<T_725LobTestRecord> T725() {
        return T_725LobTest.T_725_LOB_TEST;
    }

    @Override
    protected TableField<T_725LobTestRecord, Integer> T725_ID() {
        return T_725LobTest.ID;
    }

    @Override
    protected TableField<T_725LobTestRecord, byte[]> T725_LOB() {
        return T_725LobTest.LOB;
    }

    @Override
    protected Table<T_785Record> T785() {
        return T_785.T_785;
    }

    @Override
    protected TableField<T_785Record, Integer> T785_ID() {
        return T_785.ID;
    }

    @Override
    protected TableField<T_785Record, String> T785_NAME() {
        return T_785.NAME;
    }

    @Override
    protected TableField<T_785Record, String> T785_VALUE() {
        return T_785.VALUE;
    }

    @Override
    protected Table<T_658RefRecord> T658() {
        return T_658Ref.T_658_REF;
    }

    @Override
    protected Table<T_639NumbersTableRecord> T639() {
        return T_639NumbersTable.T_639_NUMBERS_TABLE;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Integer> T639_ID() {
        return T_639NumbersTable.ID;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, BigDecimal> T639_BIG_DECIMAL() {
        return T_639NumbersTable.BIG_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, BigInteger> T639_BIG_INTEGER() {
        return T_639NumbersTable.BIG_INTEGER;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Byte> T639_BYTE() {
        return T_639NumbersTable.BYTE;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Byte> T639_BYTE_DECIMAL() {
        return T_639NumbersTable.BYTE_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Short> T639_SHORT() {
        return T_639NumbersTable.SHORT;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Short> T639_SHORT_DECIMAL() {
        return T_639NumbersTable.SHORT_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Integer> T639_INTEGER() {
        return T_639NumbersTable.INTEGER;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Integer> T639_INTEGER_DECIMAL() {
        return T_639NumbersTable.INTEGER_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Long> T639_LONG() {
        return T_639NumbersTable.LONG;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Long> T639_LONG_DECIMAL() {
        return T_639NumbersTable.LONG_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Double> T639_DOUBLE() {
        return null;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Float> T639_FLOAT() {
        return null;
    }

    @Override
    protected Table<TArraysRecord> TArrays() {
        return TArrays.T_ARRAYS;
    }

    @Override
    protected TableField<TArraysRecord, Integer> TArrays_ID() {
        return TArrays.ID;
    }

    @Override
    protected TableField<TArraysRecord, String[]> TArrays_STRING() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, Integer[]> TArrays_NUMBER() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, Date[]> TArrays_DATE() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, ? extends UDTRecord<?>[]> TArrays_UDT() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<String>> TArrays_STRING_R() {
        return TArrays.STRING_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<Integer>> TArrays_NUMBER_R() {
        return TArrays.NUMBER_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<Date>> TArrays_DATE_R() {
        return TArrays.DATE_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<Long>> TArrays_NUMBER_LONG_R() {
        return TArrays.NUMBER_LONG_ARRAY;
    }

    @Override
    protected TableField<TBookRecord, ? extends Enum<?>> TBook_LANGUAGE_ID() {
        return TBook.LANGUAGE_ID;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_PUBLISHED_IN() {
        return TBook.PUBLISHED_IN;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_CONTENT_TEXT() {
        return TBook.CONTENT_TEXT;
    }

    @Override
    protected TableField<TBookRecord, byte[]> TBook_CONTENT_PDF() {
        return TBook.CONTENT_PDF;
    }

    @Override
    protected TableField<TBookRecord, ? extends Enum<?>> TBook_STATUS() {
        return null;
    }

    @Override
    protected Table<VLibraryRecord> VLibrary() {
        return VLibrary.V_LIBRARY;
    }

    @Override
    protected TableField<VLibraryRecord, String> VLibrary_TITLE() {
        return VLibrary.TITLE;
    }

    @Override
    protected TableField<VLibraryRecord, String> VLibrary_AUTHOR() {
        return VLibrary.AUTHOR;
    }

    @Override
    protected Table<?> VAuthor() {
        return V_AUTHOR;
    }

    @Override
    protected Table<?> VBook() {
        return V_BOOK;
    }

    @Override
    protected UpdatableTable<TDirectoryRecord> TDirectory() {
        return TDirectory.T_DIRECTORY;
    }

    @Override
    protected TableField<TDirectoryRecord, Integer> TDirectory_ID() {
        return TDirectory.ID;
    }

    @Override
    protected TableField<TDirectoryRecord, Integer> TDirectory_PARENT_ID() {
        return TDirectory.PARENT_ID;
    }

    @Override
    protected TableField<TDirectoryRecord, Byte> TDirectory_IS_DIRECTORY() {
        return TDirectory.IS_DIRECTORY;
    }

    @Override
    protected TableField<TDirectoryRecord, String> TDirectory_NAME() {
        return TDirectory.NAME;
    }

    @Override
    protected UpdatableTable<TTriggersRecord> TTriggers() {
        return TTriggers.T_TRIGGERS;
    }

    @Override
    protected TableField<TTriggersRecord, Integer> TTriggers_ID_GENERATED() {
        return TTriggers.ID_GENERATED;
    }

    @Override
    protected TableField<TTriggersRecord, Integer> TTriggers_ID() {
        return TTriggers.ID;
    }

    @Override
    protected TableField<TTriggersRecord, Integer> TTriggers_COUNTER() {
        return TTriggers.COUNTER;
    }

    @Override
    protected Field<? extends Number> FAuthorExistsField(String authorName) {
        return Routines.fAuthorExists(authorName);
    }

    @Override
    protected Field<? extends Number> FOneField() {
        return Routines.fOne();
    }

    @Override
    protected Field<? extends Number> FNumberField(Number n) {
        return Routines.fNumber(n);
    }

    @Override
    protected Field<? extends Number> FNumberField(Field<? extends Number> n) {
        return Routines.fNumber(n);
    }

    @Override
    protected Field<? extends Number> F317Field(Number n1, Number n2, Number n3, Number n4) {
        return Routines.f317(n1, n2, n3, n4);
    }

    @Override
    protected Field<? extends Number> F317Field(Field<? extends Number> n1, Field<? extends Number> n2,
        Field<? extends Number> n3, Field<? extends Number> n4) {
        return Routines.f317(n1, n2, n3, n4);
    }

    @Override
    protected Field<Result<Record>> FGetOneCursorField(Integer[] array) {
        if (array == null) {
            return Routines.fGetOneCursor((UNumberArrayRecord) null);
        }
        else {
            return Routines.fGetOneCursor(new UNumberArrayRecord(create(), array));
        }
    }

    @Override
    protected Field<Integer[]> FArrays1Field(Field<Integer[]> array) {
        return null;
    }

    @Override
    protected Field<Long[]> FArrays2Field(Field<Long[]> array) {
        return null;
    }

    @Override
    protected Field<String[]> FArrays3Field(Field<String[]> array) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends ArrayRecord<Integer>> Field<T> FArrays1Field_R(Field<T> array) {
        return (Field<T>) Routines.fArrays1((Field<UNumberArrayRecord>) array);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends ArrayRecord<Long>> Field<T> FArrays2Field_R(Field<T> array) {
        return (Field<T>) Routines.fArrays2((Field<UNumberLongArrayRecord>) array);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends ArrayRecord<String>> Field<T> FArrays3Field_R(Field<T> array) {
        return (Field<T>) Routines.fArrays3((Field<UStringArrayRecord>) array);
    }

    @Override
    protected Class<? extends UDTRecord<?>> cUAddressType() {
        return UAddressType.U_ADDRESS_TYPE.getRecordType();
    }

    @Override
    protected Class<? extends UDTRecord<?>> cUStreetType() {
        return UStreetType.U_STREET_TYPE.getRecordType();
    }

    @Override
    protected Class<?> cRoutines() {
        return Routines.class;
    }

    @Override
    protected boolean supportsOUTParameters() {
        return true;
    }

    @Override
    protected boolean supportsReferences() {
        return true;
    }

    @Override
    protected boolean supportsRecursiveQueries() {
        return true;
    }

    @Override
    protected Class<?> cLibrary() {
        return Library.class;
    }

    @Override
    protected Class<?> cSequences() {
        return Sequences.class;
    }

    @Override
    protected DataType<?>[] getCastableDataTypes() {
        return new DataType<?>[] {
            OracleDataType.CHAR,
            OracleDataType.DATE,
            OracleDataType.DEC,
            OracleDataType.DECIMAL,
            OracleDataType.FLOAT,
            OracleDataType.INT,
            OracleDataType.INTEGER,
            OracleDataType.NCHAR,
            OracleDataType.NUMBER,
            OracleDataType.NUMERIC,
            OracleDataType.NVARCHAR,
            OracleDataType.NVARCHAR2,
            OracleDataType.REAL,
            OracleDataType.SMALLINT,
            OracleDataType.TIMESTAMP,
            OracleDataType.VARCHAR,
            OracleDataType.VARCHAR2,
        };
    }

    // -------------------------------------------------------------------------
    // Oracle-specific tests
    // -------------------------------------------------------------------------

    @Test
    public void testOracleConnectBySimple() throws Exception {
        OracleFactory ora = new OracleFactory(create().getConnection(), create().getSchemaMapping());

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            ora.select(ora.rownum())
               .connectBy(ora.level().lessThan(10))
               .fetch(ora.rownum()));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            ora.select(ora.rownum())
               .connectByNoCycle(ora.level().lessThan(10))
               .fetch(ora.rownum()));

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            ora.select(ora.rownum())
               .connectBy(ora.level().lessThan(10))
               .and("1 = ?", 1)
               .startWith("? = ?", 1, 1)
               .fetch(ora.rownum()));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            ora.select(ora.rownum())
               .connectByNoCycle(ora.level().lessThan(10))
               .and("1 = ?", 1)
               .startWith("? = ?", 1, 1)
               .fetch(ora.rownum()));

        Result<Record> result =
        ora.select(ora.rownum(), ora.connectByIsCycle(), ora.connectByIsLeaf())
           .connectByNoCycle(ora.level().lessThan(4))
           .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, ora.rownum()));
        assertEquals(Integer.valueOf(2), result.getValue(1, ora.rownum()));
        assertEquals(Integer.valueOf(3), result.getValue(2, ora.rownum()));

        assertEquals(Boolean.FALSE, result.getValue(0, ora.connectByIsLeaf()));
        assertEquals(Boolean.FALSE, result.getValue(1, ora.connectByIsLeaf()));
        assertEquals(Boolean.TRUE, result.getValue(2, ora.connectByIsLeaf()));

        assertEquals(Boolean.FALSE, result.getValue(0, ora.connectByIsCycle()));
        assertEquals(Boolean.FALSE, result.getValue(1, ora.connectByIsCycle()));
        assertEquals(Boolean.FALSE, result.getValue(2, ora.connectByIsCycle()));
    }

    @Test
    public void testOracleConnectByDirectory() throws Exception {
        OracleFactory ora = new OracleFactory(create().getConnection(), create().getSchemaMapping());

        List<?> paths =
        ora.select(ora.sysConnectByPath(TDirectory_NAME(), "/").substring(2))
           .from(TDirectory())
           .connectBy(ora.prior(TDirectory_ID()).equal(TDirectory_PARENT_ID()))
           .startWith(TDirectory_PARENT_ID().isNull())
           .orderBy(ora.literal(1))
           .fetch(0);

        assertEquals(26, paths.size());
        assertEquals(Arrays.asList(
            "C:",
            "C:/eclipse",
            "C:/eclipse/configuration",
            "C:/eclipse/dropins",
            "C:/eclipse/eclipse.exe",
            "C:/eclipse/eclipse.ini",
            "C:/eclipse/features",
            "C:/eclipse/plugins",
            "C:/eclipse/p2",
            "C:/eclipse/readme",
            "C:/eclipse/readme/readme_eclipse.html",
            "C:/eclipse/src",
            "C:/Program Files",
            "C:/Program Files/Internet Explorer",
            "C:/Program Files/Internet Explorer/de-DE",
            "C:/Program Files/Internet Explorer/ielowutil.exe",
            "C:/Program Files/Internet Explorer/iexplore.exe",
            "C:/Program Files/Java",
            "C:/Program Files/Java/jre6",
            "C:/Program Files/Java/jre6/bin",
            "C:/Program Files/Java/jre6/bin/java.exe",
            "C:/Program Files/Java/jre6/bin/javaw.exe",
            "C:/Program Files/Java/jre6/bin/javaws.exe",
            "C:/Program Files/Java/jre6/lib",
            "C:/Program Files/Java/jre6/lib/javaws.jar",
            "C:/Program Files/Java/jre6/lib/rt.jar"), paths);
    }
}
