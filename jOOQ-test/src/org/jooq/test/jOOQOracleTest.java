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

package org.jooq.test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.jooq.impl.Factory.currentUser;
import static org.jooq.impl.Factory.falseCondition;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.val;
import static org.jooq.test.oracle.generatedclasses.multi_schema.Tables.T_BOOK_SALE;
import static org.jooq.test.oracle.generatedclasses.test.Routines.f691cursorIn;
import static org.jooq.test.oracle.generatedclasses.test.Routines.f691cursorOut;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fArrays1;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fArrays4;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fTables1;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fTables4;
import static org.jooq.test.oracle.generatedclasses.test.Routines.pArrays1;
import static org.jooq.test.oracle.generatedclasses.test.Routines.pTables1;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_639_NUMBERS_TABLE;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_658_REF;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_725_LOB_TEST;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_785;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_ARRAYS;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_AUTHOR;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_BOOK;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_BOOK_STORE;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_BOOLEANS;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_DATES;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_DIRECTORY;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_TRIGGERS;
import static org.jooq.test.oracle.generatedclasses.test.Tables.V_AUTHOR;
import static org.jooq.test.oracle.generatedclasses.test.Tables.V_BOOK;
import static org.jooq.test.oracle.generatedclasses.test.Tables.V_LIBRARY;
import static org.jooq.test.oracle.generatedclasses.test.UDTs.U_AUTHOR_TYPE;
import static org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.countBooks;
import static org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.load;
import static org.jooq.test.oracle2.generatedclasses.Tables.DATE_AS_TIMESTAMP_T_976;
import static org.jooq.test.oracle2.generatedclasses.udt.DateAsTimestampT_976ObjectType.DATE_AS_TIMESTAMP_T_976_OBJECT_TYPE;
import static org.jooq.util.oracle.OracleFactory.sysContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

import org.jooq.ArrayRecord;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.UpdatableTable;
import org.jooq.conf.Settings;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;
import org.jooq.test.oracle.generatedclasses.multi_schema.tables.records.TBookSaleRecord;
import org.jooq.test.oracle.generatedclasses.test.Routines;
import org.jooq.test.oracle.generatedclasses.test.Sequences;
import org.jooq.test.oracle.generatedclasses.test.TestFactory;
import org.jooq.test.oracle.generatedclasses.test.packages.Library;
import org.jooq.test.oracle.generatedclasses.test.routines.F377;
import org.jooq.test.oracle.generatedclasses.test.tables.VIncomplete;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TArraysRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TAuthorRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBookRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBookStoreRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBookToBookStoreRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBooleansRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TDatesRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TDirectoryRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TTriggersRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_658RefRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_725LobTestRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_785Record;
import org.jooq.test.oracle.generatedclasses.test.tables.records.VIncompleteRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.VLibraryRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.XUnusedRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.OInvalidType;
import org.jooq.test.oracle.generatedclasses.test.udt.UAddressType;
import org.jooq.test.oracle.generatedclasses.test.udt.UInvalidTable;
import org.jooq.test.oracle.generatedclasses.test.udt.UInvalidType;
import org.jooq.test.oracle.generatedclasses.test.udt.UStreetType;
import org.jooq.test.oracle.generatedclasses.test.udt.records.OInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UAuthorTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UBookArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UBookTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UBookTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UInvalidTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNumberArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNumberLongArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNumberTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UStringArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetBooks;
import org.jooq.test.oracle2.generatedclasses.tables.records.DateAsTimestampT_976Record;
import org.jooq.test.oracle2.generatedclasses.udt.records.DateAsTimestampT_976ObjectTypeRecord;
import org.jooq.test.oracle2.generatedclasses.udt.records.DateAsTimestampT_976VarrayTypeRecord;
import org.jooq.tools.unsigned.UByte;
import org.jooq.tools.unsigned.UInteger;
import org.jooq.tools.unsigned.ULong;
import org.jooq.tools.unsigned.UShort;
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
        TBookToBookStoreRecord,
        TBookSaleRecord,
        VLibraryRecord,
        TArraysRecord,
        TDatesRecord,
        TBooleansRecord,
        TDirectoryRecord,
        TTriggersRecord,
        XUnusedRecord,
        XUnusedRecord,
        XUnusedRecord,
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
            Class.forName(UInvalidTable.class.getName());
            Class.forName(UInvalidTableRecord.class.getName());
            Class.forName(OInvalidType.class.getName());
            Class.forName(OInvalidTypeRecord.class.getName());
        }
        catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected TestFactory create(Settings settings) {
        return new TestFactory(getConnection(), settings);
    }

    @Override
    protected UpdatableTable<TAuthorRecord> TAuthor() {
        return T_AUTHOR;
    }

    @Override
    protected TableField<TAuthorRecord, String> TAuthor_LAST_NAME() {
        return T_AUTHOR.LAST_NAME;
    }

    @Override
    protected TableField<TAuthorRecord, String> TAuthor_FIRST_NAME() {
        return T_AUTHOR.FIRST_NAME;
    }

    @Override
    protected TableField<TAuthorRecord, Date> TAuthor_DATE_OF_BIRTH() {
        return T_AUTHOR.DATE_OF_BIRTH;
    }

    @Override
    protected TableField<TAuthorRecord, Integer> TAuthor_YEAR_OF_BIRTH() {
        return T_AUTHOR.YEAR_OF_BIRTH;
    }

    @Override
    protected TableField<TAuthorRecord, Integer> TAuthor_ID() {
        return T_AUTHOR.ID;
    }

    @Override
    protected TableField<TAuthorRecord, ? extends UDTRecord<?>> TAuthor_ADDRESS() {
        return T_AUTHOR.ADDRESS;
    }

    @Override
    protected UpdatableTable<TBookRecord> TBook() {
        return T_BOOK;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_ID() {
        return T_BOOK.ID;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_AUTHOR_ID() {
        return T_BOOK.AUTHOR_ID;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_TITLE() {
        return T_BOOK.TITLE;
    }

    @Override
    protected UpdatableTable<TBookStoreRecord> TBookStore() {
        return T_BOOK_STORE;
    }

    @Override
    protected TableField<TBookStoreRecord, String> TBookStore_NAME() {
        return T_BOOK_STORE.NAME;
    }

    @Override
    protected UpdatableTable<TBookToBookStoreRecord> TBookToBookStore() {
        return T_BOOK_TO_BOOK_STORE;
    }

    @Override
    protected UpdatableTable<TBookSaleRecord> TBookSale() {
        return T_BOOK_SALE;
    }

    @Override
    protected TableField<TBookToBookStoreRecord, Integer> TBookToBookStore_BOOK_ID() {
        return T_BOOK_TO_BOOK_STORE.BOOK_ID;
    }

    @Override
    protected TableField<TBookToBookStoreRecord, String> TBookToBookStore_BOOK_STORE_NAME() {
        return T_BOOK_TO_BOOK_STORE.BOOK_STORE_NAME;
    }

    @Override
    protected TableField<TBookToBookStoreRecord, Integer> TBookToBookStore_STOCK() {
        return T_BOOK_TO_BOOK_STORE.STOCK;
    }

    @Override
    protected Table<T_725LobTestRecord> T725() {
        return T_725_LOB_TEST;
    }

    @Override
    protected TableField<T_725LobTestRecord, Integer> T725_ID() {
        return T_725_LOB_TEST.ID;
    }

    @Override
    protected TableField<T_725LobTestRecord, byte[]> T725_LOB() {
        return T_725_LOB_TEST.LOB;
    }

    @Override
    protected Table<T_785Record> T785() {
        return T_785;
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
    protected Table<XUnusedRecord> TUnsigned() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, UByte> TUnsigned_U_BYTE() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, UShort> TUnsigned_U_SHORT() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, UInteger> TUnsigned_U_INT() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ULong> TUnsigned_U_LONG() {
        return null;
    }

    @Override
    protected Table<TDatesRecord> TDates() {
        return T_DATES;
    }

    @Override
    protected UpdatableTable<TBooleansRecord> TBooleans() {
        return T_BOOLEANS;
    }

    @Override
    protected TableField<TBooleansRecord, Integer> TBooleans_ID() {
        return T_BOOLEANS.ID;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_10> TBooleans_BOOLEAN_10() {
        return T_BOOLEANS.ONE_ZERO;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_TF_LC> TBooleans_Boolean_TF_LC() {
        return T_BOOLEANS.TRUE_FALSE_LC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_TF_UC> TBooleans_Boolean_TF_UC() {
        return T_BOOLEANS.TRUE_FALSE_UC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YN_LC> TBooleans_Boolean_YN_LC() {
        return T_BOOLEANS.Y_N_LC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YN_UC> TBooleans_Boolean_YN_UC() {
        return T_BOOLEANS.Y_N_UC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YES_NO_LC> TBooleans_Boolean_YES_NO_LC() {
        return T_BOOLEANS.YES_NO_LC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YES_NO_UC> TBooleans_Boolean_YES_NO_UC() {
        return T_BOOLEANS.YES_NO_UC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean> TBooleans_VC() {
        return T_BOOLEANS.VC_BOOLEAN;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean> TBooleans_C() {
        return T_BOOLEANS.C_BOOLEAN;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean> TBooleans_N() {
        return T_BOOLEANS.N_BOOLEAN;
    }

    @Override
    protected Table<T_658RefRecord> T658() {
        return T_658_REF;
    }

    @Override
    protected Table<T_639NumbersTableRecord> T639() {
        return T_639_NUMBERS_TABLE;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Integer> T639_ID() {
        return T_639_NUMBERS_TABLE.ID;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, BigDecimal> T639_BIG_DECIMAL() {
        return T_639_NUMBERS_TABLE.BIG_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, BigInteger> T639_BIG_INTEGER() {
        return T_639_NUMBERS_TABLE.BIG_INTEGER;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Byte> T639_BYTE() {
        return T_639_NUMBERS_TABLE.BYTE;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Byte> T639_BYTE_DECIMAL() {
        return T_639_NUMBERS_TABLE.BYTE_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Short> T639_SHORT() {
        return T_639_NUMBERS_TABLE.SHORT;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Short> T639_SHORT_DECIMAL() {
        return T_639_NUMBERS_TABLE.SHORT_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Integer> T639_INTEGER() {
        return T_639_NUMBERS_TABLE.INTEGER;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Integer> T639_INTEGER_DECIMAL() {
        return T_639_NUMBERS_TABLE.INTEGER_DECIMAL;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Long> T639_LONG() {
        return T_639_NUMBERS_TABLE.LONG;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Long> T639_LONG_DECIMAL() {
        return T_639_NUMBERS_TABLE.LONG_DECIMAL;
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
        return T_ARRAYS;
    }

    @Override
    protected TableField<TArraysRecord, Integer> TArrays_ID() {
        return T_ARRAYS.ID;
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
        return T_ARRAYS.STRING_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<Integer>> TArrays_NUMBER_R() {
        return T_ARRAYS.NUMBER_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<Date>> TArrays_DATE_R() {
        return T_ARRAYS.DATE_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends ArrayRecord<Long>> TArrays_NUMBER_LONG_R() {
        return T_ARRAYS.NUMBER_LONG_ARRAY;
    }

    @Override
    protected TableField<TBookRecord, ? extends Enum<?>> TBook_LANGUAGE_ID() {
        return T_BOOK.LANGUAGE_ID;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_PUBLISHED_IN() {
        return T_BOOK.PUBLISHED_IN;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_CONTENT_TEXT() {
        return T_BOOK.CONTENT_TEXT;
    }

    @Override
    protected TableField<TBookRecord, byte[]> TBook_CONTENT_PDF() {
        return T_BOOK.CONTENT_PDF;
    }

    @Override
    protected TableField<TBookRecord, ? extends Enum<?>> TBook_STATUS() {
        return null;
    }

    @Override
    protected Table<VLibraryRecord> VLibrary() {
        return V_LIBRARY;
    }

    @Override
    protected TableField<VLibraryRecord, String> VLibrary_TITLE() {
        return V_LIBRARY.TITLE;
    }

    @Override
    protected TableField<VLibraryRecord, String> VLibrary_AUTHOR() {
        return V_LIBRARY.AUTHOR;
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
        return T_DIRECTORY;
    }

    @Override
    protected TableField<TDirectoryRecord, Integer> TDirectory_ID() {
        return T_DIRECTORY.ID;
    }

    @Override
    protected TableField<TDirectoryRecord, Integer> TDirectory_PARENT_ID() {
        return T_DIRECTORY.PARENT_ID;
    }

    @Override
    protected TableField<TDirectoryRecord, Integer> TDirectory_IS_DIRECTORY() {
        return T_DIRECTORY.IS_DIRECTORY;
    }

    @Override
    protected TableField<TDirectoryRecord, String> TDirectory_NAME() {
        return T_DIRECTORY.NAME;
    }

    @Override
    protected UpdatableTable<TTriggersRecord> TTriggers() {
        return T_TRIGGERS;
    }

    @Override
    protected TableField<TTriggersRecord, Integer> TTriggers_ID_GENERATED() {
        return T_TRIGGERS.ID_GENERATED;
    }

    @Override
    protected TableField<TTriggersRecord, Integer> TTriggers_ID() {
        return T_TRIGGERS.ID;
    }

    @Override
    protected TableField<TTriggersRecord, Integer> TTriggers_COUNTER() {
        return T_TRIGGERS.COUNTER;
    }

    @Override
    protected Table<XUnusedRecord> TIdentity() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TIdentity_ID() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TIdentity_VAL() {
        return null;
    }

    @Override
    protected UpdatableTable<XUnusedRecord> TIdentityPK() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TIdentityPK_ID() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TIdentityPK_VAL() {
        return null;
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

    private OracleFactory ora() {
        return new OracleFactory(create().getConnection(), create().getSettings());
    }

    @Test
    public void testOracleHints() throws Exception {
        assertEquals(1, create().selectOne().hint("/*+ALL_ROWS*/").fetchOne(0));
        assertEquals(1, create().select(val(1)).hint("/*+ALL_ROWS*/").fetchOne(0));
        assertEquals(1, create().selectDistinct(val(1)).hint("/*+ALL_ROWS*/").fetchOne(0));
    }

    // @Test [#1119] TODO reactivate this test
    public void testOraclePipelinedFunctions() throws Exception {
        // TODO [#1113] [#1119] Standalone calls to pipelined functions should
        // be possible too
        System.out.println(Routines.fPipelinedArray1(ora()));
    }

    @Test
    public void testOracleTableTypes() throws Exception {

        // FIRST, check unnesting of VARRAY/TABLE of NUMBER
        // ------------------------------------------------

        // Unnesting arrays
        assertEquals(emptyList(),
            create().select().from(table(new UNumberArrayRecord(ora(), (Integer[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UNumberArrayRecord(ora()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UNumberArrayRecord(ora(), 1))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(new UNumberArrayRecord(ora(), 1, 2))).fetch(0));

        // Unnesting tables
        assertEquals(emptyList(),
            create().select().from(table(new UNumberTableRecord(ora(), (Integer[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UNumberTableRecord(ora()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UNumberTableRecord(ora(), 1))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(new UNumberTableRecord(ora(), 1, 2))).fetch(0));

        // Unnesting arrays from functions
        assertEquals(emptyList(),
            create().select().from(table(fArrays1((UNumberArrayRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays1(new UNumberArrayRecord(ora(), (Integer[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays1(new UNumberArrayRecord(ora())))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fArrays1(fArrays1(new UNumberArrayRecord(ora(), 1))))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(fArrays1(fArrays1(new UNumberArrayRecord(ora(), 1, 2))))).fetch(0));

        // Unnesting tables from functions
        assertEquals(emptyList(),
            create().select().from(table(fTables1((UNumberTableRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables1(new UNumberTableRecord(ora(), (Integer[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables1(new UNumberTableRecord(ora())))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fTables1(fTables1(new UNumberTableRecord(ora(), 1))))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(fTables1(fTables1(new UNumberTableRecord(ora(), 1, 2))))).fetch(0));

        // Retrieving arrays from functions
        assertNull(fArrays1(ora(), null));
        assertEquals(emptyList(),
            fArrays1(ora(), new UNumberArrayRecord(ora(), (Integer[]) null)).getList());
        assertEquals(emptyList(),
            fArrays1(ora(), new UNumberArrayRecord(ora())).getList());
        assertEquals(asList(1),
            fArrays1(ora(), fArrays1(ora(), new UNumberArrayRecord(ora(), 1))).getList());
        assertEquals(asList(1, 2),
            fArrays1(ora(), fArrays1(ora(), new UNumberArrayRecord(ora(), 1, 2))).getList());

        // Retrieving tables from functions
        assertNull(fTables1(ora(), null));
        assertEquals(emptyList(),
            fTables1(ora(), new UNumberTableRecord(ora(), (Integer[]) null)).getList());
        assertEquals(emptyList(),
            fTables1(ora(), new UNumberTableRecord(ora())).getList());
        assertEquals(asList(1),
            fTables1(ora(), fTables1(ora(), new UNumberTableRecord(ora(), 1))).getList());
        assertEquals(asList(1, 2),
            fTables1(ora(), fTables1(ora(), new UNumberTableRecord(ora(), 1, 2))).getList());

        // Retrieving arrays from procedures
        assertNull(pArrays1(ora(), null));
        assertEquals(emptyList(),
            pArrays1(ora(), new UNumberArrayRecord(ora(), (Integer[]) null)).getList());
        assertEquals(emptyList(),
            pArrays1(ora(), new UNumberArrayRecord(ora())).getList());
        assertEquals(asList(1),
            pArrays1(ora(), pArrays1(ora(), new UNumberArrayRecord(ora(), 1))).getList());
        assertEquals(asList(1, 2),
            pArrays1(ora(), pArrays1(ora(), new UNumberArrayRecord(ora(), 1, 2))).getList());

        // Retrieving tables from procedures
        assertNull(pTables1(ora(), null));
        assertEquals(emptyList(),
            pTables1(ora(), new UNumberTableRecord(ora(), (Integer[]) null)).getList());
        assertEquals(emptyList(),
            pTables1(ora(), new UNumberTableRecord(ora())).getList());
        assertEquals(asList(1),
            pTables1(ora(), pTables1(ora(), new UNumberTableRecord(ora(), 1))).getList());
        assertEquals(asList(1, 2),
            pTables1(ora(), pTables1(ora(), new UNumberTableRecord(ora(), 1, 2))).getList());

        // THEN, check unnesting of VARRAY/TABLE of OBJECT
        // -----------------------------------------------
        UBookTypeRecord r1 = new UBookTypeRecord();
        UBookTypeRecord r2 = new UBookTypeRecord();

        r1.setId(1);
        r1.setTitle(BOOK_TITLES.get(0));

        r2.setId(2);
        r2.setTitle(BOOK_TITLES.get(1));

        // Unnesting arrays
        assertEquals(emptyList(),
            create().select().from(table(new UBookArrayRecord(ora(), (UBookTypeRecord[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UBookArrayRecord(ora()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UBookArrayRecord(ora(), r1))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(new UBookArrayRecord(ora(), r1))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(new UBookArrayRecord(ora(), r1, r2))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(new UBookArrayRecord(ora(), r1, r2))).fetch(1));

        // Unnesting tables
        assertEquals(emptyList(),
            create().select().from(table(new UBookTableRecord(ora(), (UBookTypeRecord[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UBookTableRecord(ora()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UBookTableRecord(ora(), r1))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(new UBookTableRecord(ora(), r1))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(new UBookTableRecord(ora(), r1, r2))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(new UBookTableRecord(ora(), r1, r2))).fetch(1));

        // Unnesting arrays from functions
        assertEquals(emptyList(),
            create().select().from(table(fArrays4((UBookArrayRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays4(new UBookArrayRecord(ora(), (UBookTypeRecord[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays4(new UBookArrayRecord(ora())))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fArrays4(new UBookArrayRecord(ora(), r1)))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(fArrays4(new UBookArrayRecord(ora(), r1)))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(fArrays4(fArrays4(new UBookArrayRecord(ora(), r1, r2))))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(fArrays4(fArrays4(new UBookArrayRecord(ora(), r1, r2))))).fetch(1));

        // Unnesting tables from functions
        assertEquals(emptyList(),
            create().select().from(table(fTables4((UBookTableRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables4(new UBookTableRecord(ora(), (UBookTypeRecord[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables4(new UBookTableRecord(ora())))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fTables4(new UBookTableRecord(ora(), r1)))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(fTables4(new UBookTableRecord(ora(), r1)))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(fTables4(fTables4(new UBookTableRecord(ora(), r1, r2))))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(fTables4(fTables4(new UBookTableRecord(ora(), r1, r2))))).fetch(1));

        // Retrieving arrays from functions
        assertNull(fArrays4(ora(), null));
        assertEquals(emptyList(),
            fArrays4(ora(), new UBookArrayRecord(ora(), (UBookTypeRecord[]) null)).getList());
        assertEquals(emptyList(),
            fArrays4(ora(), new UBookArrayRecord(ora())).getList());
        assertEquals(asList(r1),
            fArrays4(ora(), fArrays4(ora(), new UBookArrayRecord(ora(), r1))).getList());
        assertEquals(asList(r1, r2),
            fArrays4(ora(), fArrays4(ora(), new UBookArrayRecord(ora(), r1, r2))).getList());

        // Retrieving tables from functions
        assertNull(fTables4(ora(), null));
        assertEquals(emptyList(),
            fTables4(ora(), new UBookTableRecord(ora(), (UBookTypeRecord[]) null)).getList());
        assertEquals(emptyList(),
            fTables4(ora(), new UBookTableRecord(ora())).getList());
        assertEquals(asList(r1),
            fTables4(ora(), fTables4(ora(), new UBookTableRecord(ora(), r1))).getList());
        assertEquals(asList(r1, r2),
            fTables4(ora(), fTables4(ora(), new UBookTableRecord(ora(), r1, r2))).getList());


    }

    @Test
    public void testOracleMemberProcedures() throws Exception {
        UAuthorTypeRecord author1;
        UAuthorTypeRecord author2;

        // Unattached:
        author1 = new UAuthorTypeRecord();
        author1.setId(1);
        author2 = load(ora(), author1);
        assertEquals(1, (int) author1.getId());
        assertEquals(1, (int) author2.getId());
        assertNull(author1.getFirstName());
        assertEquals("George", author2.getFirstName());
        assertNull(author1.getLastName());
        assertEquals("Orwell", author2.getLastName());

        // Attached
        author1 = ora().newRecord(U_AUTHOR_TYPE);
        author1.setId(1);
        author2 = author1.load();
        assertEquals(1, (int) author1.getId());
        assertEquals(1, (int) author2.getId());
        assertEquals("George", author1.getFirstName());
        assertEquals("George", author2.getFirstName());
        assertEquals("Orwell", author1.getLastName());
        assertEquals("Orwell", author2.getLastName());

        // Count books
        author1 = ora().newRecord(U_AUTHOR_TYPE);
        assertEquals(BigDecimal.ZERO, author1.countBooks());
        assertEquals(BigDecimal.ZERO, ora().select(countBooks(author1)).fetchOne(0));

        author1 = ora().newRecord(U_AUTHOR_TYPE);
        author1.setId(1);
        assertEquals(new BigDecimal("2"), author1.countBooks());
        assertEquals(new BigDecimal("2"), ora().select(countBooks(author1)).fetchOne(0));

        // Get books
        author1 = ora().newRecord(U_AUTHOR_TYPE);
        GetBooks noBooks = author1.getBooks();
        assertNull(noBooks.getBook1().getId());
        assertNull(noBooks.getBook1().getTitle());
        assertNull(noBooks.getBook2().getId());
        assertNull(noBooks.getBook2().getTitle());

        author1 = ora().newRecord(U_AUTHOR_TYPE);
        author1.setId(1);
        GetBooks books = author1.getBooks();
        assertEquals(1, (int) books.getBook1().getId());
        assertEquals("1984", books.getBook1().getTitle());
        assertEquals(2, (int) books.getBook2().getId());
        assertEquals("Animal Farm", books.getBook2().getTitle());

        // Get books also calls upon load, internally. Check if that's reflected
        assertEquals(1, (int) author1.getId());
        assertEquals("George", author1.getFirstName());
        assertEquals("Orwell", author1.getLastName());
    }

    @Test
    public void testOracleCursorINOUT() throws Exception {
        assertEquals(4, (int) create().select(f691cursorIn(f691cursorOut())).fetchOne(0, Integer.class));
    }

    @Test
    public void testOracleTypedSequences() throws Exception {
        assertEquals(Byte.valueOf("1"), ora().nextval(Sequences.S_961_BYTE));
        assertEquals(Short.valueOf("1"), ora().nextval(Sequences.S_961_SHORT));
        assertEquals(Integer.valueOf("1"), ora().nextval(Sequences.S_961_INT));
        assertEquals(Long.valueOf("1"), ora().nextval(Sequences.S_961_LONG));
        assertEquals(BigInteger.valueOf(1), ora().nextval(Sequences.S_961_BIG_INTEGER));
    }

    @Test
    public void testOracleMergeStatementExtensions() throws Exception {
        reset = false;
        TAuthorRecord author;

        // Test updating with a positive condition
        // ---------------------------------------
        assertEquals(1,
        ora().mergeInto(T_AUTHOR)
             .usingDual()
             .on(T_AUTHOR.ID.equal(1))
             .whenMatchedThenUpdate()
             .set(T_AUTHOR.LAST_NAME, "Frisch")
             .where(T_AUTHOR.ID.equal(1))
             .execute());

        author = create().fetchOne(T_AUTHOR, T_AUTHOR.ID.equal(1));
        assertEquals(2, create().selectCount().from(T_AUTHOR).fetchOne(0));
        assertEquals(1, (int) author.getId());
        assertEquals(AUTHOR_FIRST_NAMES.get(0), author.getFirstName());
        assertEquals("Frisch", author.getLastName());

        // Test updating with a negative condition
        // ---------------------------------------
        assertEquals(0,
        ora().mergeInto(T_AUTHOR)
             .usingDual()
             .on(T_AUTHOR.ID.equal(1))
             .whenMatchedThenUpdate()
             .set(T_AUTHOR.LAST_NAME, "Frisch")
             .where(T_AUTHOR.ID.equal(3))
             .execute());

        author = create().fetchOne(T_AUTHOR, T_AUTHOR.ID.equal(1));
        assertEquals(2, create().selectCount().from(T_AUTHOR).fetchOne(0));
        assertEquals(1, (int) author.getId());
        assertEquals(AUTHOR_FIRST_NAMES.get(0), author.getFirstName());
        assertEquals("Frisch", author.getLastName());

        // Test deleting
        // -------------
        // ON DELETE CASCADE doesn't work with MERGE...?
        ora().delete(T_BOOK).execute();

        assertEquals(1,
        ora().mergeInto(T_AUTHOR)
             .usingDual()
             .on(trueCondition())
             .whenMatchedThenUpdate()
             .set(T_AUTHOR.LAST_NAME, "Frisch")
             .where(T_AUTHOR.ID.equal(2))
             .deleteWhere(T_AUTHOR.ID.equal(2))
             .execute());

        author = create().fetchOne(T_AUTHOR, T_AUTHOR.ID.equal(1));
        assertEquals(1, create().selectCount().from(T_AUTHOR).fetchOne(0));
        assertEquals(1, (int) author.getId());
        assertEquals(AUTHOR_FIRST_NAMES.get(0), author.getFirstName());
        assertEquals("Frisch", author.getLastName());

        // Test inserting
        // --------------
        assertEquals(0,
        ora().mergeInto(T_AUTHOR)
             .usingDual()
             .on(trueCondition())
             .whenNotMatchedThenInsert(
                 T_AUTHOR.ID,
                 T_AUTHOR.FIRST_NAME,
                 T_AUTHOR.LAST_NAME)
             .values(3, "Yvette", "Z'Graggen")
             .where(falseCondition())
             .execute());

        // No tests on results
    }

    @Test
    public void testOracleDateAsTimestamp() throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

        // A record with nulls
        // -------------------
        DateAsTimestampT_976Record record = ora().newRecord(DATE_AS_TIMESTAMP_T_976);
        record.setId(1);
        assertEquals(1, record.store());
        assertNull(record.getD());
        assertNull(record.getT());
        assertNull(record.getO());

        record.refresh();
        assertNull(record.getD());
        assertNull(record.getT());
        assertNull(record.getO());
        assertEquals(record, ora().fetchOne(DATE_AS_TIMESTAMP_T_976, DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_ID.equal(1)));

        // A record with values
        // --------------------
        DateAsTimestampT_976ObjectTypeRecord o = ora().newRecord(DATE_AS_TIMESTAMP_T_976_OBJECT_TYPE);
        o.setD(now);
        // [#1034] TODO: Check proper use of Timestamp in array records
        DateAsTimestampT_976VarrayTypeRecord t = new DateAsTimestampT_976VarrayTypeRecord(ora());
//        t.set(now, now);

        record = ora().newRecord(DATE_AS_TIMESTAMP_T_976);
        record.setId(2);
        record.setD(now);
        record.setO(o);
        record.setT(t);
        record.store();
        assertEquals(record, ora().fetchOne(DATE_AS_TIMESTAMP_T_976, DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_ID.equal(2)));

        // Procedures and packages
        // -----------------------
        assertEquals(now, org.jooq.test.oracle2.generatedclasses.Routines.p_976(ora(), now));
        assertEquals(now, org.jooq.test.oracle2.generatedclasses.Routines.f_976(ora(), now));
        assertEquals(now, ora().select(org.jooq.test.oracle2.generatedclasses.Routines.f_976(now)).fetchOne(0));

        assertEquals(now, org.jooq.test.oracle2.generatedclasses.packages.DateAsTimestampPkg_976.p_976(ora(), now));
        assertEquals(now, org.jooq.test.oracle2.generatedclasses.packages.DateAsTimestampPkg_976.f_976(ora(), now));
        assertEquals(now, ora().select(org.jooq.test.oracle2.generatedclasses.packages.DateAsTimestampPkg_976.f_976(now)).fetchOne(0));
    }

    @Test
    public void testOracleFunctions() {
        Record user = ora().select(
            sysContext("USERENV", "SESSION_USER"),
            currentUser()).fetchOne();

        assertEquals(user.getValue(0), user.getValue(1));
    }
}
