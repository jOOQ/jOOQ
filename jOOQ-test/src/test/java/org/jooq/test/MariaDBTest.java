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

import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.BaseTest.ignoreThrows;
import static org.jooq.test.mariadb.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.mariadb.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.mariadb.generatedclasses.Tables.T_DATES;
import static org.jooq.test.mariadb.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.mariadb.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.mariadb.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.mariadb.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.mariadb.generatedclasses.Tables.V_BOOK;
import static org.jooq.util.mariadb.MariaDBDSL.aesDecrypt;
import static org.jooq.util.mariadb.MariaDBDSL.aesEncrypt;
import static org.jooq.util.mariadb.MariaDBDSL.compress;
import static org.jooq.util.mariadb.MariaDBDSL.decode;
import static org.jooq.util.mariadb.MariaDBDSL.desDecrypt;
import static org.jooq.util.mariadb.MariaDBDSL.desEncrypt;
import static org.jooq.util.mariadb.MariaDBDSL.encode;
import static org.jooq.util.mariadb.MariaDBDSL.password;
import static org.jooq.util.mariadb.MariaDBDSL.sha1;
import static org.jooq.util.mariadb.MariaDBDSL.uncompress;
import static org.jooq.util.mariadb.MariaDBDSL.uncompressedLength;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.jooq.ArrayRecord;
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
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.mariadb.generatedclasses.Keys;
import org.jooq.test.mariadb.generatedclasses.Routines;
import org.jooq.test.mariadb.generatedclasses.enums.TBookStatus;
import org.jooq.test.mariadb.generatedclasses.enums.T_959JavaKeywords;
import org.jooq.test.mariadb.generatedclasses.tables.TAuthor;
import org.jooq.test.mariadb.generatedclasses.tables.TBook;
import org.jooq.test.mariadb.generatedclasses.tables.TBookStore;
import org.jooq.test.mariadb.generatedclasses.tables.TBookToBookStore;
import org.jooq.test.mariadb.generatedclasses.tables.TBooleans;
import org.jooq.test.mariadb.generatedclasses.tables.TDates;
import org.jooq.test.mariadb.generatedclasses.tables.TExoticTypes;
import org.jooq.test.mariadb.generatedclasses.tables.TIdentityPk;
import org.jooq.test.mariadb.generatedclasses.tables.TTriggers;
import org.jooq.test.mariadb.generatedclasses.tables.TUnsigned;
import org.jooq.test.mariadb.generatedclasses.tables.T_639NumbersTable;
import org.jooq.test.mariadb.generatedclasses.tables.T_725LobTest;
import org.jooq.test.mariadb.generatedclasses.tables.T_785;
import org.jooq.test.mariadb.generatedclasses.tables.T_959;
import org.jooq.test.mariadb.generatedclasses.tables.VLibrary;
import org.jooq.test.mariadb.generatedclasses.tables.daos.TAuthorDao;
import org.jooq.test.mariadb.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.T_785Record;
import org.jooq.test.mariadb.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.mariadb.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.mariadb.MariaDBDSL;
import org.jooq.util.mariadb.MariaDBDataType;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MariaDBTest extends jOOQAbstractTest<
        TAuthorRecord,
        org.jooq.test.mariadb.generatedclasses.tables.pojos.TAuthor,
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
        XUnusedRecord,
        TIdentityPkRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record,
        XUnusedRecord> {

    @Override
    protected SQLDialect dialect() {
        return SQLDialect.MARIADB;
    }

    @Override
    protected DAO<TAuthorRecord, org.jooq.test.mariadb.generatedclasses.tables.pojos.TAuthor, Integer> TAuthorDao() {
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
            MariaDBDataType.BIGINT,
            MariaDBDataType.BINARY,
            MariaDBDataType.BIT,
            MariaDBDataType.BLOB,
            MariaDBDataType.BOOL,
            MariaDBDataType.BOOLEAN,
            MariaDBDataType.CHAR,
            MariaDBDataType.DATE,
            MariaDBDataType.DATETIME,
            MariaDBDataType.DEC,
            MariaDBDataType.DECIMAL,
            MariaDBDataType.DOUBLE,
            MariaDBDataType.FLOAT,
            MariaDBDataType.INT,
            MariaDBDataType.INTEGER,
            MariaDBDataType.LONGBLOB,
            MariaDBDataType.LONGTEXT,
            MariaDBDataType.MEDIUMBLOB,
            MariaDBDataType.MEDIUMINT,
            MariaDBDataType.MEDIUMTEXT,
            MariaDBDataType.SET,
            MariaDBDataType.SMALLINT,
            MariaDBDataType.TEXT,
            MariaDBDataType.TIME,
            MariaDBDataType.TIMESTAMP,
            MariaDBDataType.TINYBLOB,
            MariaDBDataType.TINYINT,
            MariaDBDataType.VARBINARY,
            MariaDBDataType.VARCHAR,
            MariaDBDataType.YEAR,
        };
    }

    // IMPORTANT! Make this the first test, to prevent side-effects
    @Override
    @Test
    public void testInsertIdentity() throws Exception {
        super.testInsertIdentity();
    }

    @Test
    public void testMariaDBEncryptionFunctions() throws Exception {
        assertNotNull(create().select(password("abc")).fetchOne(0));
        assertNotNull(create().select(md5("abc")).fetchOne(0));
        assertNotNull(create().select(sha1("abc")).fetchOne(0));
        assertEquals("abc", create().select(decode(encode("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create().select(aesDecrypt(aesEncrypt("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create().select(desDecrypt(desEncrypt("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create().select(desDecrypt(desEncrypt("abc"))).fetchOne(0));
        assertEquals("abc", create().select(uncompress(compress("abc"))).fetchOne(0));
        assertEquals(3, create().select(uncompressedLength(compress("abc"))).fetchOne(0));
    }

    @Test
    public void testMariaDBJavaKeywordEnums() throws Exception {
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
    public void testMariaDBEnumIndex() throws Exception {
        assertNull(MariaDBDSL.enumType(TBookStatus.class, -1));
        assertNull(MariaDBDSL.enumType(TBookStatus.class, 0));
        assertEquals(TBookStatus.SOLD_OUT, MariaDBDSL.enumType(TBookStatus.class, 1));
        assertEquals(TBookStatus.ORDERED, MariaDBDSL.enumType(TBookStatus.class, 2));
        assertEquals(TBookStatus.ON_STOCK, MariaDBDSL.enumType(TBookStatus.class, 3));
        assertNull(MariaDBDSL.enumType(TBookStatus.class, 4));
    }

    @Test
    public void testMariaDBYearType() throws Exception {
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
    public void testMariaDBDataTypeDDL() {
        try {
            int i = 1;

            create().createTable("t")

                    // [#4117] Unsigned data types:
                    .column("id", MariaDBDataType.INTEGERUNSIGNED)
                    .column("n" + i++, MariaDBDataType.TINYINTUNSIGNED)
                    .column("n" + i++, MariaDBDataType.SMALLINTUNSIGNED)
                    .column("n" + i++, MariaDBDataType.MEDIUMINTUNSIGNED)
                    .column("n" + i++, MariaDBDataType.INTUNSIGNED)
                    .column("n" + i++, MariaDBDataType.BIGINTUNSIGNED)

                    // [#4120] Text data types:
                    .column("t" + i++, MariaDBDataType.TINYTEXT)
                    .column("t" + i++, MariaDBDataType.MEDIUMTEXT)
                    .column("t" + i++, MariaDBDataType.TEXT)
                    .column("t" + i++, MariaDBDataType.LONGTEXT)
                    .execute();

            Result<Record> result1 = create().selectFrom(table(name("t"))).fetch();
            assertEquals(0, result1.size());
            assertEquals(i, result1.fields().length);

            int j = 0;
            assertEquals(UInteger.class, result1.fieldsRow().dataType(j++).getType());
            // Looks like this doesn't work this way...?
            assertEquals(Byte.class, result1.fieldsRow().dataType(j++).getType());
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
}
