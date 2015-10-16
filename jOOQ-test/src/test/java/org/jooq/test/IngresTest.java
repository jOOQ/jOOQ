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

package org.jooq.test;

/* [pro] */

import static org.jooq.test.ingres.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.ingres.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.ingres.generatedclasses.Tables.T_DATES;
import static org.jooq.test.ingres.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.ingres.generatedclasses.Tables.T_IDENTITY;
import static org.jooq.test.ingres.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.ingres.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.ingres.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.ingres.generatedclasses.Tables.V_BOOK;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.UUID;

import org.jooq.ArrayRecord;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.ingres.generatedclasses.Keys;
import org.jooq.test.ingres.generatedclasses.Sequences;
import org.jooq.test.ingres.generatedclasses.tables.TAuthor;
import org.jooq.test.ingres.generatedclasses.tables.TBook;
import org.jooq.test.ingres.generatedclasses.tables.TBookStore;
import org.jooq.test.ingres.generatedclasses.tables.TBookToBookStore;
import org.jooq.test.ingres.generatedclasses.tables.TBooleans;
import org.jooq.test.ingres.generatedclasses.tables.TExoticTypes;
import org.jooq.test.ingres.generatedclasses.tables.TIdentity;
import org.jooq.test.ingres.generatedclasses.tables.TIdentityPk;
import org.jooq.test.ingres.generatedclasses.tables.TTriggers;
import org.jooq.test.ingres.generatedclasses.tables.TUnsigned;
import org.jooq.test.ingres.generatedclasses.tables.T_639NumbersTable;
import org.jooq.test.ingres.generatedclasses.tables.T_725LobTest;
import org.jooq.test.ingres.generatedclasses.tables.T_785;
import org.jooq.test.ingres.generatedclasses.tables.VLibrary;
import org.jooq.test.ingres.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TIdentityRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.T_785Record;
import org.jooq.test.ingres.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.ingres.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.ingres.IngresDataType;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;


/**
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IngresTest extends jOOQAbstractTest<
        TAuthorRecord,
        Object,
        TBookRecord,
        TBookStoreRecord,
        TBookToBookStoreRecord,
        XUnusedRecord,
        VLibraryRecord,
        XUnusedRecord,
        TDatesRecord,
        TBooleansRecord,
        XUnusedRecord,
        TTriggersRecord,
        TUnsignedRecord,
        TExoticTypesRecord,
        XUnusedRecord,
        TIdentityRecord,
        TIdentityPkRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record,
        XUnusedRecord> {

    @Override
    protected SQLDialect dialect() {
        return SQLDialect.INGRES;
    }

    @Override
    protected Table<TAuthorRecord> TAuthor() {
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
        return null;
    }

    @Override
    protected Table<TBookRecord> TBook() {
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
    protected TableField<TBookRecord, Integer> TBook_CO_AUTHOR_ID() {
        return TBook.CO_AUTHOR_ID;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_TITLE() {
        return TBook.TITLE;
    }

    @Override
    protected Table<TBookStoreRecord> TBookStore() {
        return TBookStore.T_BOOK_STORE;
    }

    @Override
    protected TableField<TBookStoreRecord, String> TBookStore_NAME() {
        return TBookStore.NAME;
    }

    @Override
    protected Table<TBookToBookStoreRecord> TBookToBookStore() {
        return T_BOOK_TO_BOOK_STORE;
    }

    @Override
    protected Table<XUnusedRecord> TBookSale() {
        return null;
    }

    @Override
    protected TableField<TBookToBookStoreRecord, Integer> TBookToBookStore_BOOK_ID() {
        return TBookToBookStore.BOOK_ID;
    }

    @Override
    protected TableField<TBookToBookStoreRecord, String> TBookToBookStore_BOOK_STORE_NAME() {
        return TBookToBookStore.BOOK_STORE_NAME;
    }

    @Override
    protected TableField<TBookToBookStoreRecord, Integer> TBookToBookStore_STOCK() {
        return TBookToBookStore.STOCK;
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
        return T_639NumbersTable.DOUBLE;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Float> T639_FLOAT() {
        return T_639NumbersTable.FLOAT;
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
    protected Table<XUnusedRecord> CASE() {
        return null;
    }

    @Override
    protected Table<TUnsignedRecord> TUnsigned() {
        return T_UNSIGNED;
    }

    @Override
    protected TableField<TUnsignedRecord, UByte> TUnsigned_U_BYTE() {
        return TUnsigned.U_BYTE;
    }

    @Override
    protected TableField<TUnsignedRecord, UShort> TUnsigned_U_SHORT() {
        return TUnsigned.U_SHORT;
    }

    @Override
    protected TableField<TUnsignedRecord, UInteger> TUnsigned_U_INT() {
        return TUnsigned.U_INT;
    }

    @Override
    protected TableField<TUnsignedRecord, ULong> TUnsigned_U_LONG() {
        return TUnsigned.U_LONG;
    }

    @Override
    protected Table<TExoticTypesRecord> TExoticTypes() {
        return T_EXOTIC_TYPES;
    }

    @Override
    protected TableField<TExoticTypesRecord, Integer> TExoticTypes_ID() {
        return TExoticTypes.ID;
    }

    @Override
    protected TableField<TExoticTypesRecord, UUID> TExoticTypes_UU() {
        return TExoticTypes.UU;
    }

    @Override
    protected Table<TDatesRecord> TDates() {
        return T_DATES;
    }

    @Override
    protected Table<TBooleansRecord> TBooleans() {
        return T_BOOLEANS;
    }

    @Override
    protected TableField<TBooleansRecord, Integer> TBooleans_ID() {
        return TBooleans.ID;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_10> TBooleans_BOOLEAN_10() {
        return TBooleans.ONE_ZERO;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_TF_LC> TBooleans_Boolean_TF_LC() {
        return TBooleans.TRUE_FALSE_LC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_TF_UC> TBooleans_Boolean_TF_UC() {
        return TBooleans.TRUE_FALSE_UC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YN_LC> TBooleans_Boolean_YN_LC() {
        return TBooleans.Y_N_LC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YN_UC> TBooleans_Boolean_YN_UC() {
        return TBooleans.Y_N_UC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YES_NO_LC> TBooleans_Boolean_YES_NO_LC() {
        return TBooleans.YES_NO_LC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean_YES_NO_UC> TBooleans_Boolean_YES_NO_UC() {
        return TBooleans.YES_NO_UC;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean> TBooleans_VC() {
        return TBooleans.VC_BOOLEAN;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean> TBooleans_C() {
        return TBooleans.C_BOOLEAN;
    }

    @Override
    protected TableField<TBooleansRecord, Boolean> TBooleans_N() {
        return TBooleans.N_BOOLEAN;
    }

    @Override
    protected Table<XUnusedRecord> TArrays() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TArrays_ID() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, String[]> TArrays_STRING() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer[]> TArrays_NUMBER() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Date[]> TArrays_DATE() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ? extends UDTRecord<?>[]> TArrays_UDT() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ? extends ArrayRecord<String>> TArrays_STRING_R() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ? extends ArrayRecord<Integer>> TArrays_NUMBER_R() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ? extends ArrayRecord<Date>> TArrays_DATE_R() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ? extends ArrayRecord<Long>> TArrays_NUMBER_LONG_R() {
        return null;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_LANGUAGE_ID() {
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
    protected ForeignKey<TBookRecord, TAuthorRecord> FK_T_BOOK_AUTHOR_ID() {
        return Keys.FK_T_BOOK_AUTHOR_ID;
    }

    @Override
    protected ForeignKey<TBookRecord, TAuthorRecord> FK_T_BOOK_CO_AUTHOR_ID() {
        return Keys.FK_T_BOOK_CO_AUTHOR_ID;
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
    protected Table<XUnusedRecord> TDirectory() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TDirectory_ID() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TDirectory_PARENT_ID() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, Integer> TDirectory_IS_DIRECTORY() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, String> TDirectory_NAME() {
        return null;
    }

    @Override
    protected Table<TTriggersRecord> TTriggers() {
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
    protected Table<TIdentityRecord> TIdentity() {
        return T_IDENTITY;
    }

    @Override
    protected TableField<TIdentityRecord, Integer> TIdentity_ID() {
        return TIdentity.ID;
    }

    @Override
    protected TableField<TIdentityRecord, Integer> TIdentity_VAL() {
        return TIdentity.VAL;
    }

    @Override
    protected Table<TIdentityPkRecord> TIdentityPK() {
        return T_IDENTITY_PK;
    }

    @Override
    protected TableField<TIdentityPkRecord, Integer> TIdentityPK_ID() {
        return TIdentityPk.ID;
    }

    @Override
    protected TableField<TIdentityPkRecord, Integer> TIdentityPK_VAL() {
        return TIdentityPk.VAL;
    }

    @Override
    protected Field<? extends Number> FAuthorExistsField(String authorName) {
        return null;
    }

    @Override
    protected Field<? extends Number> FOneField() {
        return null;
    }

    @Override
    protected Field<? extends Number> FNumberField(Number n) {
        return null;
    }

    @Override
    protected Field<? extends Number> FNumberField(Field<? extends Number> n) {
        return null;
    }

    @Override
    protected Field<? extends Number> F317Field(Number n1, Number n2, Number n3, Number n4) {
        return null;
    }

    @Override
    protected Field<? extends Number> F317Field(Field<? extends Number> n1, Field<? extends Number> n2,
        Field<? extends Number> n3, Field<? extends Number> n4) {
        return null;
    }

    @Override
    protected Field<Result<Record>> FGetOneCursorField(Integer[] array) {
        return null;
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

    @Override
    protected <T extends ArrayRecord<Integer>> Field<T> FArrays1Field_R(Field<T> array) {
        return null;
    }

    @Override
    protected <T extends ArrayRecord<Long>> Field<T> FArrays2Field_R(Field<T> array) {
        return null;
    }

    @Override
    protected <T extends ArrayRecord<String>> Field<T> FArrays3Field_R(Field<T> array) {
        return null;
    }

    @Override
    protected Class<? extends UDTRecord<?>> cUAddressType() {
        return null;
    }

    @Override
    protected Class<? extends UDTRecord<?>> cUStreetType() {
        return null;
    }

    @Override
    protected Class<?> cRoutines() {
        return null;
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
        return false;
    }

    @Override
    protected Class<?> cLibrary() {
        return null;
    }

    @Override
    protected Class<?> cSequences() {
        return Sequences.class;
    }

    @Override
    protected DataType<?>[] getCastableDataTypes() {
        return new DataType<?>[] {
            IngresDataType.ANSIDATE,
            IngresDataType.BIGINT,
            IngresDataType.BINARY,
            IngresDataType.BINARYLARGEOBJECT,
            IngresDataType.BLOB,
            IngresDataType.BOOLEAN,
            IngresDataType.BYTE,
            IngresDataType.C,
            IngresDataType.CHAR,
            IngresDataType.CHARACTER,
            IngresDataType.CHARACTERLARGEOBJECT,
            IngresDataType.CHARACTERVARYING,
            IngresDataType.CHARLARGEOBJECT,
            IngresDataType.CLOB,
            IngresDataType.DATE,
            IngresDataType.DECIMAL,
            IngresDataType.DOUBLEPRECISION,
            IngresDataType.FLOAT,
            IngresDataType.FLOAT4,
            IngresDataType.FLOAT8,
            IngresDataType.I1,
            IngresDataType.I2,
            IngresDataType.I4,
            IngresDataType.I8,
            IngresDataType.INGRESDATE,
            IngresDataType.INTEGER,
            IngresDataType.INTEGER1,
            IngresDataType.INTEGER2,
            IngresDataType.INTEGER4,
            IngresDataType.INTEGER8,
            IngresDataType.LONGBYTE,
            IngresDataType.LONGNVARCHAR,
            IngresDataType.LONGVARCHAR,
            IngresDataType.NATIONALCHARACTERLARGEOBJECT,
            IngresDataType.NCHAR,
            IngresDataType.NCHARLARGEOBJECT,
            IngresDataType.NCLOB,
            IngresDataType.NVARCHAR,
            IngresDataType.REAL,
            IngresDataType.SMALLINT,
            IngresDataType.TEXT,
            IngresDataType.TIME,
            IngresDataType.TIMESTAMP,
            IngresDataType.TINYINT,
            IngresDataType.VARBINARY,
            IngresDataType.VARBYTE,
            IngresDataType.VARCHAR,
        };
    }
}

/* [/pro] */