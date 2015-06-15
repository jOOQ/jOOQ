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

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.test.BaseTest.ignoreThrows;
import static org.jooq.test.mysql.generatedclasses.Tables.T_785;
import static org.jooq.test.mysql.generatedclasses.Tables.T_959;
import static org.jooq.test.mysql.generatedclasses.Tables.T_BOOK;
import static org.jooq.test.mysql.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.mysql.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.mysql.generatedclasses.Tables.T_CHARSETS;
import static org.jooq.test.mysql.generatedclasses.Tables.T_DATES;
import static org.jooq.test.mysql.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.mysql.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.mysql.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.mysql.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.mysql.generatedclasses.Tables.V_BOOK;
import static org.jooq.util.mysql.MySQLDSL.aesDecrypt;
import static org.jooq.util.mysql.MySQLDSL.aesEncrypt;
import static org.jooq.util.mysql.MySQLDSL.compress;
import static org.jooq.util.mysql.MySQLDSL.decode;
import static org.jooq.util.mysql.MySQLDSL.desDecrypt;
import static org.jooq.util.mysql.MySQLDSL.desEncrypt;
import static org.jooq.util.mysql.MySQLDSL.encode;
import static org.jooq.util.mysql.MySQLDSL.password;
import static org.jooq.util.mysql.MySQLDSL.sha1;
import static org.jooq.util.mysql.MySQLDSL.sha2;
import static org.jooq.util.mysql.MySQLDSL.uncompress;
import static org.jooq.util.mysql.MySQLDSL.uncompressedLength;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.jooq.ArrayRecord;
import org.jooq.CreateTableColumnStep;
import org.jooq.DAO;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.conf.BackslashEscaping;
import org.jooq.conf.Settings;
import org.jooq.lambda.Seq;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.mysql.generatedclasses.Keys;
import org.jooq.test.mysql.generatedclasses.Routines;
import org.jooq.test.mysql.generatedclasses.enums.TBookStatus;
import org.jooq.test.mysql.generatedclasses.enums.T_959JavaKeywords;
import org.jooq.test.mysql.generatedclasses.tables.TAuthor;
import org.jooq.test.mysql.generatedclasses.tables.TBook;
import org.jooq.test.mysql.generatedclasses.tables.TBookStore;
import org.jooq.test.mysql.generatedclasses.tables.TBookToBookStore;
import org.jooq.test.mysql.generatedclasses.tables.TBooleans;
import org.jooq.test.mysql.generatedclasses.tables.TCharsets;
import org.jooq.test.mysql.generatedclasses.tables.TDates;
import org.jooq.test.mysql.generatedclasses.tables.TExoticTypes;
import org.jooq.test.mysql.generatedclasses.tables.TIdentityPk;
import org.jooq.test.mysql.generatedclasses.tables.TTriggers;
import org.jooq.test.mysql.generatedclasses.tables.TUnsigned;
import org.jooq.test.mysql.generatedclasses.tables.T_639NumbersTable;
import org.jooq.test.mysql.generatedclasses.tables.T_725LobTest;
import org.jooq.test.mysql.generatedclasses.tables.VLibrary;
import org.jooq.test.mysql.generatedclasses.tables.daos.TAuthorDao;
import org.jooq.test.mysql.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TCharsetsRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_785Record;
import org.jooq.test.mysql.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.mysql.MySQLDSL;
import org.jooq.util.mysql.MySQLDataType;

import org.junit.Test;


/**
 * @author Lukas Eder
 */
public class MySQLTest extends jOOQAbstractTest<
        TAuthorRecord,
        org.jooq.test.mysql.generatedclasses.tables.pojos.TAuthor,
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
        TCharsetsRecord,
        XUnusedRecord,
        TIdentityPkRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record,
        XUnusedRecord> {

    @Override
    protected SQLDialect dialect() {
        return SQLDialect.MYSQL;
    }

    @Override
    protected DAO<TAuthorRecord, org.jooq.test.mysql.generatedclasses.tables.pojos.TAuthor, Integer> TAuthorDao() {
        return new TAuthorDao(create().configuration());
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
        return null;
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
    protected Table<TCharsetsRecord> TCharsets() {
        return T_CHARSETS;
    }

    @Override
    protected TableField<TCharsetsRecord, Integer> TCharsets_ID() {
        return TCharsets.ID;
    }

    @Override
    protected TableField<TCharsetsRecord, String> TCharsets_UTF8() {
        return TCharsets.UTF8;
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

    /* [pro] */
    @Override
    protected TableField<XUnusedRecord, ArrayRecord<String>> TArrays_STRING_R() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ArrayRecord<Integer>> TArrays_NUMBER_R() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ArrayRecord<Date>> TArrays_DATE_R() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, ArrayRecord<Long>> TArrays_NUMBER_LONG_R() {
        return null;
    }

    /* [/pro] */
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
        return TBook.STATUS;
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
        return Routines.fAuthorExists(authorName);
    }

    @Override
    protected Field<? extends Number> FOneField() {
        return Routines.fOne();
    }

    @Override
    protected Field<? extends Number> FNumberField(Number n) {
        return Routines.fNumber((Integer) n);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Field<? extends Number> FNumberField(Field<? extends Number> n) {
        return Routines.fNumber((Field<Integer>) n);
    }

    @Override
    protected Field<? extends Number> F317Field(Number n1, Number n2, Number n3, Number n4) {
        return Routines.f317((Integer) n1, (Integer) n2, (Integer) n3, (Integer) n4);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Field<? extends Number> F317Field(Field<? extends Number> n1, Field<? extends Number> n2,
        Field<? extends Number> n3, Field<? extends Number> n4) {
        return Routines.f317((Field<Integer>) n1, (Field<Integer>) n2, (Field<Integer>) n3, (Field<Integer>) n4);
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

    /* [pro] */
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

    /* [/pro] */
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
        return false;
    }

    @Override
    protected Class<?> cLibrary() {
        return null;
    }

    @Override
    protected Class<?> cSequences() {
        return null;
    }

    @Override
    protected DataType<?>[] getCastableDataTypes() {
        return new DataType<?>[] {
            MySQLDataType.BIGINT,
            MySQLDataType.BINARY,
            MySQLDataType.BIT,
            MySQLDataType.BLOB,
            MySQLDataType.BOOL,
            MySQLDataType.BOOLEAN,
            MySQLDataType.CHAR,
            MySQLDataType.DATE,
            MySQLDataType.DATETIME,
            MySQLDataType.DEC,
            MySQLDataType.DECIMAL,
            MySQLDataType.DOUBLE,
            MySQLDataType.FLOAT,
            MySQLDataType.INT,
            MySQLDataType.INTEGER,
            MySQLDataType.LONGBLOB,
            MySQLDataType.LONGTEXT,
            MySQLDataType.MEDIUMBLOB,
            MySQLDataType.MEDIUMINT,
            MySQLDataType.MEDIUMTEXT,
            MySQLDataType.SET,
            MySQLDataType.SMALLINT,
            MySQLDataType.TEXT,
            MySQLDataType.TIME,
            MySQLDataType.TIMESTAMP,
            MySQLDataType.TINYBLOB,
            MySQLDataType.TINYINT,
            MySQLDataType.VARBINARY,
            MySQLDataType.VARCHAR,
            MySQLDataType.YEAR,
        };
    }

    // IMPORTANT! Make this the first test, to prevent side-effects
    @Override
    @Test
    public void testInsertIdentity() throws Exception {
        super.testInsertIdentity();
    }

    @Test
    public void testMySQLStringEncryptionFunctions() throws Exception {
        assertNotNull(create().select(password("abc")).fetchOne(0));
        assertNotNull(create().select(md5("abc")).fetchOne(0));
        assertNotNull(create().select(sha1("abc")).fetchOne(0));
        assertNotNull(create().select(sha2("abc", 256)).fetchOne(0));
        assertEquals("abc", create().select(decode(encode("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create().select(aesDecrypt(aesEncrypt("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create().select(desDecrypt(desEncrypt("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create().select(desDecrypt(desEncrypt("abc"))).fetchOne(0));
        assertEquals("abc", create().select(uncompress(compress("abc"))).fetchOne(0));
        assertEquals(3, create().select(uncompressedLength(compress("abc"))).fetchOne(0));
    }

    @Test
    public void testMySQLByteArrayEncryptionFunctions() throws Exception {
        final byte[] MESSAGE = new byte[] {-74, 71, -79, -124, -58};
        final byte[] SECRET = new byte[] {-122, -123, 4, -12, -37};

        assertNotNull(create().select(password(MESSAGE)).fetchOne(0));
        assertNotNull(create().select(sha1(MESSAGE)).fetchOne(0));
        assertNotNull(create().select(sha2(MESSAGE, 256)).fetchOne(0));
        assertArrayEquals(MESSAGE, create().select(decode(encode(MESSAGE, SECRET), val(SECRET))).fetchOne().value1());
        assertArrayEquals(MESSAGE, create().select(aesDecrypt(aesEncrypt(MESSAGE, SECRET), val(SECRET))).fetchOne().value1());
        assertArrayEquals(MESSAGE, create().select(desDecrypt(desEncrypt(MESSAGE, SECRET), val(SECRET))).fetchOne().value1());
        assertArrayEquals(MESSAGE, create().select(desDecrypt(desEncrypt(MESSAGE))).fetchOne().value1());
        assertArrayEquals(MESSAGE, create().select(uncompress(compress(MESSAGE))).fetchOne().value1());
        assertEquals(5, create().select(uncompressedLength(compress(MESSAGE))).fetchOne(0));
    }

    @Test
    public void testMySQLJavaKeywordEnums() throws Exception {
        reset = false;

        assertEquals(3,
        create().insertInto(T_959.T_959)
                .set(T_959.JAVA_KEYWORDS, T_959JavaKeywords.public_)
                .newRecord()
                .set(T_959.JAVA_KEYWORDS, T_959JavaKeywords.abstract_)
                .newRecord()
                .set(T_959.JAVA_KEYWORDS, T_959JavaKeywords.class_)
                .execute());

        List<T_959JavaKeywords> result =
        create().selectFrom(T_959.T_959)
                .orderBy(T_959.JAVA_KEYWORDS)
                .fetch(T_959.JAVA_KEYWORDS);

        assertEquals(3, result.size());
        assertEquals(T_959JavaKeywords.abstract_, result.get(0));
        assertEquals(T_959JavaKeywords.class_, result.get(1));
        assertEquals(T_959JavaKeywords.public_, result.get(2));
    }

    @Test
    public void testMySQLEnumIndex() throws Exception {
        assertNull(MySQLDSL.enumType(TBookStatus.class, -1));
        assertNull(MySQLDSL.enumType(TBookStatus.class, 0));
        assertEquals(TBookStatus.SOLD_OUT, MySQLDSL.enumType(TBookStatus.class, 1));
        assertEquals(TBookStatus.ORDERED, MySQLDSL.enumType(TBookStatus.class, 2));
        assertEquals(TBookStatus.ON_STOCK, MySQLDSL.enumType(TBookStatus.class, 3));
        assertNull(MySQLDSL.enumType(TBookStatus.class, 4));
    }

    @Test
    public void testMySQLYearType() throws Exception {
        jOOQAbstractTest.reset = false;

        Date d1 = Date.valueOf("2012-01-01");
        Date d2 = Date.valueOf("1999-01-01");

        assertEquals(2,
        create().insertInto(T_DATES,
                    TDates.ID,
                    TDates.Y2,
                    TDates.Y4)
                .values(1, d1, d1)
                .values(2, d2, d2)
                .execute());

        Result<?> dates =
        create().select(TDates.Y2, TDates.Y4)
                .from(T_DATES)
                .orderBy(TDates.ID)
                .fetch();

        assertEquals(2, dates.size());
        assertEquals(d1, dates.getValue(0, 0));
        assertEquals(d1, dates.getValue(0, 1));
        assertEquals(d2, dates.getValue(1, 0));
        assertEquals(d2, dates.getValue(1, 1));
    }

    @Test
    public void testMySQLOverloadedProcedures() throws Exception {
        assertEquals(1, (int) Routines.fp1908_FUNCTION(create().configuration(), 1));
        assertEquals(2, (int) Routines.fp1908_PROCEDURE(create().configuration(), 1));
    }

    @Test
    public void testMySQLIndexHints() throws Exception {
        assertEquals(4, create().selectFrom(TBook().useIndex("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().useIndexForJoin("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().useIndexForOrderBy("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().useIndexForGroupBy("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().forceIndex("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().forceIndexForJoin("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().forceIndexForOrderBy("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().forceIndexForGroupBy("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().ignoreIndex("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().ignoreIndexForJoin("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().ignoreIndexForOrderBy("i_book_a", "i_book_b")).fetch().size());
        assertEquals(4, create().selectFrom(TBook().ignoreIndexForGroupBy("i_book_a", "i_book_b")).fetch().size());

        // Combine random hints
        assertEquals(4, create().selectFrom(
            TBook().useIndexForGroupBy("i_book_a")
                   .useIndexForJoin("i_book_b")
                   .ignoreIndexForOrderBy("i_book_a")).fetch().size());
    }

    @Test
    public void testMySQLPlainSQLWithBackslashEscaping() throws Exception {
        assertEquals("A ' ' \\ B", create().fetchValue("select 'A '' \\' \\\\ B' from dual"));
        assertEquals("A ' ' \\ B", create(new Settings().withBackslashEscaping(BackslashEscaping.ON))
                                           .fetchValue("select 'A '' \\' \\\\ B' from dual"));
        assertEquals("A ' ' \\ B", create().fetchValue("select {0} from dual", inline("A ' ' \\ B")));
        assertEquals("A ' ' \\ B", create(new Settings().withBackslashEscaping(BackslashEscaping.ON))
                                           .fetchValue("select {0} from dual", inline("A ' ' \\ B")));

        String sqlMode = (String) create().fetchValue("SELECT @@SESSION.sql_mode");

        try {
            create().execute("SET @@SESSION.sql_mode = CONCAT(@@SESSION.sql_mode, ',' ,'NO_BACKSLASH_ESCAPES')");
            assertEquals("A ' \\' \\\\ B", create(new Settings().withBackslashEscaping(BackslashEscaping.OFF)).fetchValue("select 'A '' \\'' \\\\ B' from dual"));
            assertEquals("A ' \\' \\\\ B", create(new Settings().withBackslashEscaping(BackslashEscaping.OFF)).fetchValue("select {0} from dual", inline("A ' \\' \\\\ B")));
        }
        finally {
            create().execute("SET @@SESSION.sql_mode = ?", sqlMode);
        }
    }

    @Test
    public void testMySQLBatchWithEnumTypes() throws Exception {
        jOOQAbstractTest.reset = false;

        int[] result =
        create().batch(insertInto(T_BOOK, TBook.ID, TBook.AUTHOR_ID, TBook.TITLE, TBook.PUBLISHED_IN, TBook.LANGUAGE_ID, TBook.STATUS)
                          .values((Integer) null, null, null, null, null, null))
                .bind(5, 1, "a", 1980, 1, null)
                .bind(6, 1, "b", 1980, 1, TBookStatus.ON_STOCK)
                .execute();

        assertEquals(2, result.length);
        assertEquals(asList(null, TBookStatus.ON_STOCK), create().fetchValues(select(TBook.STATUS).from(T_BOOK).where(TBook.ID.in(5, 6)).orderBy(TBook.ID)));
    }

    @Test
    public void testMySQLDataTypeDDL() {
        try {
            int i = 1;

            create().createTable("t")

                    // [#4117] Unsigned data types:
                    .column("id", MySQLDataType.INTEGERUNSIGNED)
                    .column("n" + i++, MySQLDataType.TINYINTUNSIGNED)
                    .column("n" + i++, MySQLDataType.SMALLINTUNSIGNED)
                    .column("n" + i++, MySQLDataType.MEDIUMINTUNSIGNED)
                    .column("n" + i++, MySQLDataType.INTUNSIGNED)
                    .column("n" + i++, MySQLDataType.BIGINTUNSIGNED)

                    // [#4120] Text data types:
                    .column("t" + i++, MySQLDataType.TINYTEXT)
                    .column("t" + i++, MySQLDataType.MEDIUMTEXT)
                    .column("t" + i++, MySQLDataType.TEXT)
                    .column("t" + i++, MySQLDataType.LONGTEXT)
                    .execute();

            Result<Record> result1 = create().selectFrom(table(name("t"))).fetch();
            assertEquals(0, result1.size());
            assertEquals(i, result1.fields().length);

            int j = 0;
            assertEquals(UInteger.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(UByte.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(UShort.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(UInteger.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(UInteger.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(ULong.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(String.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(String.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(String.class, result1.fieldsRow().dataType(j++).getType());
            assertEquals(String.class, result1.fieldsRow().dataType(j++).getType());
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    @Test
    public void testMySQLDDLFromMeta() {
        // [#4120] Ensure DDL can be executed from types obtained from org.jooq.Meta

        try {
            int i = 1;

            Table<?> table =
            Seq.seq(create().meta().getTables())
               .filter(t -> "t_2926".equals(t.getName().toLowerCase()))
               .findFirst()
               .get();

            CreateTableColumnStep step =
            create().createTable("t")
                    .column("id", MySQLDataType.INTEGERUNSIGNED);

            for (Field<?> field : table.fields()) {
                step = step.column("n" + i++, field.getDataType(create().configuration()));
            }

            step.execute();

            Result<Record> result1 = create().selectFrom(table(name("t"))).fetch();
            assertEquals(0, result1.size());
            assertEquals(i, result1.fields().length);
        }
        finally {
            ignoreThrows(() -> create().dropTable("t").execute());
        }
    }

    @Test
    public void testMySQLPlainSQLAndComments() {
        // [#4182] MySQL also supports # as comment character
        Result<Record> r1 = create().fetch(
            "# comment\n"
          + "# comment\n"
          + "select 1 a from dual"
        );

        assertEquals(1, r1.size());
        assertEquals(1, (int) r1.get(0).getValue(0, int.class));

        Result<Record> r2 = create().fetch(
            "# comment\n"
          + "# funky '\"` characters\n"
          + "# comment\n"
          + "select 1 a from dual"
        );

        assertEquals(1, r2.size());
        assertEquals(1, (int) r2.get(0).getValue(0, int.class));
    }

    @Test
    public void testMySQLBinaryCast() {

        // [#3255]
        assertEquals("1984",
        create().selectFrom(T_BOOK)
                .where(cast(TBook.TITLE, MySQLDataType.BINARY)
                   .eq(cast("1984", MySQLDataType.BINARY)))
                .fetchOne(TBook.TITLE));
    }

    @Test
    public void testMySQLStraightJoin() {
        assertEquals(BOOK_IDS, create()
            .select(TBook_ID())
            .from(TBook()).straightJoin(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
            .orderBy(TBook_ID())
            .fetch(TBook_ID()));
    }

    @Test
    public void testMySQLInsertOnDuplicateKeyUpdateWithValues() {
        jOOQAbstractTest.reset = false;

        assertEquals(2,
        create().insertInto(TAuthor())
                .columns(TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "A")
                .values(4, "B")
                .onDuplicateKeyUpdate()
                .set(TAuthor_ID(), MySQLDSL.values(TAuthor_ID()))
                .set(TAuthor_LAST_NAME(), MySQLDSL.values(TAuthor_LAST_NAME()).concat("-"))
                .execute());

        assertEquals(
            asList(1, 2, 3, 4),
            create().fetchValues(select(TAuthor_ID()).from(TAuthor()).orderBy(TAuthor_ID())));
        assertEquals(
            seq(AUTHOR_LAST_NAMES).concat("A", "B").toList(),
            create().fetchValues(select(TAuthor_LAST_NAME()).from(TAuthor()).orderBy(TAuthor_ID())));


        assertEquals(4,
        create().insertInto(TAuthor())
                .columns(TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "A")
                .values(4, "B")
                .onDuplicateKeyUpdate()
                .set(TAuthor_ID(), MySQLDSL.values(TAuthor_ID()))
                .set(TAuthor_LAST_NAME(), MySQLDSL.values(TAuthor_LAST_NAME()).concat("-"))
                .execute());

        assertEquals(
            asList(1, 2, 3, 4),
            create().fetchValues(select(TAuthor_ID()).from(TAuthor()).orderBy(TAuthor_ID())));
        assertEquals(
            seq(AUTHOR_LAST_NAMES).concat("A-", "B-").toList(),
            create().fetchValues(select(TAuthor_LAST_NAME()).from(TAuthor()).orderBy(TAuthor_ID())));
    }
}
