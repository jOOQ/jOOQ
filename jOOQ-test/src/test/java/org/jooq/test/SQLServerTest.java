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

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.sqlserver.generatedclasses.Routines.fTables1;
import static org.jooq.test.sqlserver.generatedclasses.Routines.fTables4;
import static org.jooq.test.sqlserver.generatedclasses.Routines.fTables5;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_3084;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_3084_TWO_UNIQUE_KEYS;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_3085;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_3090_B;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_639_NUMBERS_TABLE;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_725_LOB_TEST;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_785;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_AUTHOR;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_BOOK;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_BOOK_STORE;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_DATES;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_ERROR_ON_UPDATE;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_IDENTITY;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_TRIGGERS;
import static org.jooq.test.sqlserver.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.sqlserver.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.sqlserver.generatedclasses.Tables.V_BOOK;
import static org.jooq.test.sqlserver.generatedclasses.Tables.V_LIBRARY;
import static org.jooq.test.sqlserver.generatedclasses.tables.FTables1.F_TABLES1;
import static org.jooq.test.sqlserver.generatedclasses.tables.FTables4.F_TABLES4;
import static org.jooq.util.sqlserver.SQLServerDSL.difference;
import static org.jooq.util.sqlserver.SQLServerDSL.soundex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.all.pojos.jaxb.JAXBBook;
import org.jooq.test.sqlserver.generatedclasses.Keys;
import org.jooq.test.sqlserver.generatedclasses.Routines;
import org.jooq.test.sqlserver.generatedclasses.Sequences;
import org.jooq.test.sqlserver.generatedclasses.routines.P4106;
import org.jooq.test.sqlserver.generatedclasses.routines.PBooksAndAuthors;
import org.jooq.test.sqlserver.generatedclasses.routines.PResults;
import org.jooq.test.sqlserver.generatedclasses.routines.PResultsAndRowCounts;
import org.jooq.test.sqlserver.generatedclasses.tables.FTables4;
import org.jooq.test.sqlserver.generatedclasses.tables.FTables5;
import org.jooq.test.sqlserver.generatedclasses.tables.records.FTables1Record;
import org.jooq.test.sqlserver.generatedclasses.tables.records.FTables4Record;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TIdentityRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_3084Record;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_3084TwoUniqueKeysRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_3085Record;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_3090BRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.T_785Record;
import org.jooq.test.sqlserver.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.sqlserver.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.sqlserver.SQLServerDataType;

import org.junit.Test;
import org.w3c.dom.Node;

/**
 * @author Lukas Eder
 */
public class SQLServerTest extends jOOQAbstractTest<
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
        return SQLDialect.SQLSERVER2014;
    }

    @Override
    protected Table<TAuthorRecord> TAuthor() {
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
        return null;
    }

    @Override
    protected Table<TBookRecord> TBook() {
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
    protected TableField<TBookRecord, Integer> TBook_CO_AUTHOR_ID() {
        return T_BOOK.CO_AUTHOR_ID;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_TITLE() {
        return T_BOOK.TITLE;
    }

    @Override
    protected Table<TBookStoreRecord> TBookStore() {
        return T_BOOK_STORE;
    }

    @Override
    protected TableField<TBookStoreRecord, String> TBookStore_NAME() {
        return T_BOOK_STORE.NAME;
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
        return T_639_NUMBERS_TABLE.DOUBLE;
    }

    @Override
    protected TableField<T_639NumbersTableRecord, Float> T639_FLOAT() {
        return T_639_NUMBERS_TABLE.FLOAT;
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
    protected Table<XUnusedRecord> CASE() {
        return null;
    }

    @Override
    protected Table<TUnsignedRecord> TUnsigned() {
        return T_UNSIGNED;
    }

    @Override
    protected TableField<TUnsignedRecord, UByte> TUnsigned_U_BYTE() {
        return T_UNSIGNED.U_BYTE;
    }

    @Override
    protected TableField<TUnsignedRecord, UShort> TUnsigned_U_SHORT() {
        return T_UNSIGNED.U_SHORT;
    }

    @Override
    protected TableField<TUnsignedRecord, UInteger> TUnsigned_U_INT() {
        return T_UNSIGNED.U_INT;
    }

    @Override
    protected TableField<TUnsignedRecord, ULong> TUnsigned_U_LONG() {
        return T_UNSIGNED.U_LONG;
    }

    @Override
    protected Table<TExoticTypesRecord> TExoticTypes() {
        return T_EXOTIC_TYPES;
    }

    @Override
    protected TableField<TExoticTypesRecord, Integer> TExoticTypes_ID() {
        return T_EXOTIC_TYPES.ID;
    }

    @Override
    protected TableField<TExoticTypesRecord, UUID> TExoticTypes_UU() {
        return T_EXOTIC_TYPES.UU;
    }

    @Override
    protected TableField<TExoticTypesRecord, Node> TExoticTypes_UNTYPED_XML_AS_DOM() {
        return T_EXOTIC_TYPES.UNTYPED_XML_AS_DOM;
    }

    @Override
    protected TableField<TExoticTypesRecord, JAXBBook> TExoticTypes_UNTYPED_XML_AS_JAXB() {
        return T_EXOTIC_TYPES.UNTYPED_XML_AS_JAXB;
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
    protected ForeignKey<TBookRecord, TAuthorRecord> FK_T_BOOK_AUTHOR_ID() {
        return Keys.FK_T_BOOK_AUTHOR_ID;
    }

    @Override
    protected ForeignKey<TBookRecord, TAuthorRecord> FK_T_BOOK_CO_AUTHOR_ID() {
        return Keys.FK_T_BOOK_CO_AUTHOR_ID;
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
    protected Table<TIdentityRecord> TIdentity() {
        return T_IDENTITY;
    }

    @Override
    protected TableField<TIdentityRecord, Integer> TIdentity_ID() {
        return T_IDENTITY.ID;
    }

    @Override
    protected TableField<TIdentityRecord, Integer> TIdentity_VAL() {
        return T_IDENTITY.VAL;
    }

    @Override
    protected Table<TIdentityPkRecord> TIdentityPK() {
        return T_IDENTITY_PK;
    }

    @Override
    protected TableField<TIdentityPkRecord, Integer> TIdentityPK_ID() {
        return T_IDENTITY_PK.ID;
    }

    @Override
    protected TableField<TIdentityPkRecord, Integer> TIdentityPK_VAL() {
        return T_IDENTITY_PK.VAL;
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
        return Sequences.class;
    }

    @Override
    protected DataType<?>[] getCastableDataTypes() {
        return new DataType<?>[] {
            SQLServerDataType.BIGINT,
            SQLServerDataType.BINARY,
            SQLServerDataType.BIT,
            SQLServerDataType.CHAR,
            SQLServerDataType.DATE,
            SQLServerDataType.DATETIME,
            SQLServerDataType.DECIMAL,
            SQLServerDataType.FLOAT,
            SQLServerDataType.IMAGE,
            SQLServerDataType.INT,
            SQLServerDataType.MONEY,
            SQLServerDataType.NCHAR,
            SQLServerDataType.NTEXT,
            SQLServerDataType.NUMERIC,
            SQLServerDataType.NVARCHAR,
            SQLServerDataType.REAL,
            SQLServerDataType.SMALLDATETIME,
            SQLServerDataType.SMALLINT,
            SQLServerDataType.SMALLMONEY,
            SQLServerDataType.TEXT,
            SQLServerDataType.TINYINT,
            SQLServerDataType.VARBINARY,
            SQLServerDataType.VARCHAR,
        };
    }

    @Test
    public void testSQLServerOptionHint() throws Exception {
        assertEquals(4, create()
            .selectFrom(T_BOOK)
            .option("OPTION(OPTIMIZE FOR UNKNOWN)")
            .fetch()
            .size());
    }

    @Test
    public void testSQLServerTableValuedFunctions() throws Exception {
        Result<FTables1Record> r1 = create().selectFrom(fTables1()).fetch();
        assertEquals(1, r1.size());
        assertEquals(1, (int) r1.get(0).value1());
        assertEquals(1, (int) r1.get(0).getColumnValue());
        assertEquals(1, (int) r1.get(0).getValue(F_TABLES1.COLUMN_VALUE));

        Result<FTables4Record> r2 = create().selectFrom(fTables4((Integer) null)).fetch();
        assertEquals(4, r2.size());
        assertEquals(BOOK_IDS, r2.getValues(F_TABLES4.ID));
        assertEquals(BOOK_TITLES, r2.getValues(F_TABLES4.TITLE));

        FTables4Record r3 = create().selectFrom(fTables4(1)).fetchOne();
        assertEquals(BOOK_IDS.get(0), r3.getId());
        assertEquals(BOOK_TITLES.get(0), r3.getTitle());

        FTables4 ft4 = fTables4((Integer) null).as("t");
        FTables5 ft5 = fTables5(ft4.ID, val(1), ft4.ID.add(1));

        assertEquals(
            asList(1, 1, 2, 2, 1, 3, 3, 1, 4, 4, 1, 5),
            create().select(ft4.TITLE, ft5.V)
                .from(ft4)
                .crossApply(ft5)
                .orderBy(ft4.ID)
                .fetch(ft5.V));
    }

    @Test
    public void testSQLServerStringFunctions() throws Exception {
        assertEquals(4,
        create().select(soundex("abc"), soundex(T_BOOK.TITLE))
                .from(T_BOOK)
                .fetch()
                .size());

        assertEquals(4,
        create().select(
                    difference("abc", "abcd"),
                    difference("abc", T_BOOK.TITLE),
                    difference(T_BOOK.TITLE, "abcd"),
                    difference(T_BOOK.TITLE, T_BOOK.TITLE)
                )
                .from(T_BOOK)
                .fetch()
                .size());
    }

    @Test
    public void testSQLServerRaiserror() throws Exception {

        // [#3011] TODO Fix this for sqljdbc_4.0. It works with jTDS
        try {
            Routines.pRaise(create().configuration(), 0);
            fail();
        }
        catch (DataAccessException e) {
            SQLException cause = (SQLException) e.getCause();
            assertEquals("message", cause.getMessage());
        }

        try {
            Routines.pRaise(create().configuration(), 1);
            fail();
        }
        catch (DataAccessException e) {
            SQLException cause = (SQLException) e.getCause();

            assertEquals("message 1", cause.getMessage());
            assertEquals("message 2", cause.getNextException().getMessage());
        }
    }

    @Test
    public void testSQLServerRaiserrorNTimes() throws Exception {
        try {
            Routines.pRaise_3696(create().configuration(), 3);
            fail();
        }
        catch (DataAccessException e) {
            SQLException cause = (SQLException) e.getCause();

            assertEquals("message 3", cause.getMessage());
            assertEquals("message 2", cause.getNextException().getMessage());
            assertEquals("message 1", cause.getNextException().getNextException().getMessage());

            assertNull(cause.getNextException().getNextException().getNextException());
        }

        try {
            Routines.pRaise_3696(create().configuration(), 300);
            fail();
        }
        catch (DataAccessException e) {
            SQLException cause = (SQLException) e.getCause();

            for (int i = 300; i >= 300 - 256; i--) {
                assertEquals("message " + i, cause.getMessage());

                cause = cause.getNextException();
            }

            assertNull(cause);
        }
    }

    @Test
    public void testSQLServerRaiserrorInTrigger() throws Exception {
        jOOQAbstractTest.reset = false;

        assertEquals(1,
        create().insertInto(T_ERROR_ON_UPDATE, T_ERROR_ON_UPDATE.ID)
                .values(1)
                .execute());

        // This update only generates warnings (error level <=  10)
        ExceptionListener l1 = new ExceptionListener();
        assertEquals(1,
        create(l1).update(T_ERROR_ON_UPDATE)
                  .set(T_ERROR_ON_UPDATE.ID, 2)
                  .where(T_ERROR_ON_UPDATE.ID.eq(1))
                  .execute());

        assertNull(l1.exception);
        assertEquals("t_error_on_update_trigger 1", l1.warning.getMessage());
        assertEquals("t_error_on_update_trigger 2", l1.warning.getNextException().getMessage());

        ExceptionListener l2 = new ExceptionListener();
        try {
            // This update generates SQLExceptions (error level > 10)
            create(l2).update(T_ERROR_ON_UPDATE)
                      .set(T_ERROR_ON_UPDATE.ID, 3)
                      .where(T_ERROR_ON_UPDATE.ID.eq(2))
                      .execute();
            fail();
        }
        catch (DataAccessException e) {
            SQLException cause = (SQLException) e.getCause();

            assertEquals("t_error_on_update_trigger 3", cause.getMessage());
            assertEquals("t_error_on_update_trigger 4", cause.getNextException().getMessage());
            assertEquals("t_error_on_update_trigger 5", cause.getNextException().getNextException().getMessage());
            assertNull(cause.getNextException().getNextException().getNextException());

            assertEquals("t_error_on_update_trigger 1", l2.warning.getMessage());
            assertEquals("t_error_on_update_trigger 2", l2.warning.getNextException().getMessage());
            assertEquals("t_error_on_update_trigger 3", l2.exception.getMessage());
            assertEquals("t_error_on_update_trigger 4", l2.exception.getNextException().getMessage());
            assertEquals("t_error_on_update_trigger 5", l2.exception.getNextException().getNextException().getMessage());
        }
        finally {
            create().fetch(T_ERROR_ON_UPDATE);
        }
    }

    static class ExceptionListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -8750749773616243579L;

        SQLException exception;
        SQLWarning warning;

        @Override
        public void exception(ExecuteContext ctx) {
            exception = ctx.sqlException();
        }

        @Override
        public void warning(ExecuteContext ctx) {
            warning = ctx.sqlWarning();
        }
    }

    @Test
    public void testSQLServerStoreNullableUniqueKey() {
        clean(T_3090_B);

        T_3090BRecord record = create().newRecord(T_3090_B);
        record.setId1(1);
        record.setId2(null);
        assertEquals(1, record.insert());
        assertNull(record.getData());
        assertEquals(1, create().fetchCount(T_3090_B));

        // REFRESH()
        record.setData(2);
        record.refresh();
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertNull(record.getData());
        assertEquals(1, create().fetchCount(T_3090_B));

        record.setData(2);
        record.refresh(T_3090_B.DATA);
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertNull(record.getData());
        assertEquals(1, create().fetchCount(T_3090_B));

        // STORE()
        record.setData(2);
        assertEquals(1, record.store());
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertEquals(2, (int) record.getData());
        assertEquals(1, create().fetchCount(T_3090_B));

        // UPDATE()
        record.setData(2);
        assertEquals(1, record.update());
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertEquals(2, (int) record.getData());
        assertEquals(1, create().fetchCount(T_3090_B));

        // DELETE()
        assertEquals(1, record.delete());
        assertNull(create().fetchOne(T_3090_B));
        assertEquals(0, create().fetchCount(T_3090_B));

        // INSERT()
        assertEquals(1, record.store());
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertEquals(2, (int) record.getData());
        assertEquals(1, create().fetchCount(T_3090_B));
    }

    @Test
    public void testSQLServerStoreNullableUniqueKeyAndUpdatablePrimaryKeys() {
        clean(T_3090_B);

        DSLContext create = create();
        create.configuration().settings().setUpdatablePrimaryKeys(true);

        T_3090BRecord record = create.newRecord(T_3090_B);
        record.setId1(1);
        record.setId2(null);
        assertEquals(1, record.insert());
        assertNull(record.getData());
        assertEquals(1, create.fetchCount(T_3090_B));

        // REFRESH()
        record.setData(2);
        record.refresh();
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertNull(record.getData());
        assertEquals(1, create.fetchCount(T_3090_B));

        record.setData(2);
        record.refresh(T_3090_B.DATA);
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertNull(record.getData());
        assertEquals(1, create.fetchCount(T_3090_B));

        // STORE()
        record.setData(2);
        assertEquals(1, record.store());
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertEquals(2, (int) record.getData());
        assertEquals(1, create.fetchCount(T_3090_B));

        // UPDATE()
        record.setData(2);
        assertEquals(1, record.update());
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertEquals(2, (int) record.getData());
        assertEquals(1, create.fetchCount(T_3090_B));

        // DELETE()
        assertEquals(1, record.delete());
        assertNull(create.fetchOne(T_3090_B));
        assertEquals(0, create.fetchCount(T_3090_B));

        // INSERT()
        assertEquals(1, record.store());
        assertEquals(1, (int) record.getId1());
        assertNull(record.getId2());
        assertEquals(2, (int) record.getData());
        assertEquals(1, create.fetchCount(T_3090_B));
    }

    @Test
    public void testSQLServerStoreNullableUniqueKeyWithBatchStore() {
        clean(T_3084);

        T_3084Record r1 = create().newRecord(T_3084);
        T_3084Record r2 = create().newRecord(T_3084);

        // STORE() means INSERT, no batch
        r1.setId(null);
        r1.setData(1);
        r2.setId(2);
        r2.setData(2);

        assertEquals(1, r1.store());
        assertEquals(1, r2.store());
        assertEquals(asList(1, 2), create().select(T_3084.DATA).from(T_3084).orderBy(T_3084.ID).fetchInto(int.class));

        // STORE() means UPDATE, no batch
        r1.setData(11);
        r2.setData(12);

        assertEquals(1, r1.store());
        assertEquals(1, r2.store());
        assertEquals(asList(11, 12), create().select(T_3084.DATA).from(T_3084).orderBy(T_3084.ID).fetchInto(int.class));

        // STORE() means UPDATE, with batch
        r1.setData(21);
        r2.setData(22);
        int[] result = create().batchStore(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(21, 22), create().select(T_3084.DATA).from(T_3084).orderBy(T_3084.ID).fetchInto(int.class));

        // DELETE()
        result = create().batchDelete(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(0, create().fetchCount(T_3084));

        // STORE() means INSERT, with batch
        result = create().batchStore(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(21, 22), create().select(T_3084.DATA).from(T_3084).orderBy(T_3084.ID).fetchInto(int.class));
    }

    @Test
    public void testSQLServerStoreNullableUniqueKeyWithMultipleUniqueKeysAndBatchStore() {
        clean(T_3084_TWO_UNIQUE_KEYS);

        T_3084TwoUniqueKeysRecord r1 = create().newRecord(T_3084_TWO_UNIQUE_KEYS);
        T_3084TwoUniqueKeysRecord r2 = create().newRecord(T_3084_TWO_UNIQUE_KEYS);

        // STORE() means INSERT, no batch
        r1.setId1(1);
        r1.setId2(null);
        r1.setId3(1);
        r1.setId4(null);
        r1.setData(1);

        r2.setId1(2);
        r2.setId2(null);
        r2.setId3(2);
        r2.setId4(null);
        r2.setData(2);

        assertEquals(1, r1.store());
        assertEquals(1, r2.store());
        assertEquals(asList(1, 2), create().select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));

        Result<T_3084TwoUniqueKeysRecord> records;

        // STORE() means UPDATE, no batch
        records = create().selectFrom(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetch();
        r1 = records.get(0);
        r2 = records.get(1);
        r1.setData(11);
        r2.setData(12);

        assertEquals(1, r1.store());
        assertEquals(1, r2.store());
        assertEquals(asList(11, 12), create().select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));

        // STORE() means UPDATE, with batch
        records = create().selectFrom(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetch();
        r1 = records.get(0);
        r2 = records.get(1);

        r1.setData(21);
        r2.setData(22);
        int[] result = create().batchStore(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(21, 22), create().select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));
    }

    @Test
    public void testSQLServerStoreNullableUniqueKeyWithMultipleUniqueKeysAndBatchStoreAndUpdatablePrimaryKeys() {
        //[#3300] ;-) What a method name
        clean(T_3084_TWO_UNIQUE_KEYS);

        DSLContext create = create();
        create.configuration().settings().setUpdatablePrimaryKeys(true);

        T_3084TwoUniqueKeysRecord r1 = create.newRecord(T_3084_TWO_UNIQUE_KEYS);
        T_3084TwoUniqueKeysRecord r2 = create.newRecord(T_3084_TWO_UNIQUE_KEYS);

        // STORE() means INSERT, no batch
        r1.setId1(1);
        r1.setId2(null);
        r1.setId3(1);
        r1.setId4(null);
        r1.setData(1);

        r2.setId1(2);
        r2.setId2(null);
        r2.setId3(2);
        r2.setId4(null);
        r2.setData(2);

        assertEquals(1, r1.store());
        assertEquals(1, r2.store());
        assertEquals(asList(1, 2), create.select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));

        Result<T_3084TwoUniqueKeysRecord> records;

        // STORE() means UPDATE, no batch
        records = create.selectFrom(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetch();
        r1 = records.get(0);
        r2 = records.get(1);
        r1.setData(11);
        r2.setData(12);

        assertEquals(1, r1.store());
        assertEquals(1, r2.store());
        assertEquals(asList(11, 12), create.select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));

        // STORE() means UPDATE, with batch
        records = create.selectFrom(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetch();
        r1 = records.get(0);
        r2 = records.get(1);

        r1.setData(21);
        r2.setData(22);
        int[] result = create.batchStore(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(21, 22), create.select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));

        // DELETE()
        result = create.batchDelete(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(0, create.fetchCount(T_3084_TWO_UNIQUE_KEYS));

        // STORE() means INSERT, with batch
        result = create.batchStore(r1, r2).execute();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(21, 22), create.select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));
    }

    @Test
    public void testSQLServerStoreNullableUniqueKeyWithMultipleUniqueKeysAndBatchStoreAndUpdatablePrimaryKeysNewRecords() {
        //[#3300] ;-) What a method name
        clean(T_3084_TWO_UNIQUE_KEYS);

        DSLContext create = create();
        create.configuration().settings().setUpdatablePrimaryKeys(true);

        T_3084TwoUniqueKeysRecord r1 = create.newRecord(T_3084_TWO_UNIQUE_KEYS);
        T_3084TwoUniqueKeysRecord r2 = create.newRecord(T_3084_TWO_UNIQUE_KEYS);

        // STORE() means INSERT, no batch
        r1.setId1(1);
        r1.setId2(null);
        r1.setId3(1);
        r1.setId4(null);
        r1.setData(1);

        r2.setId1(2);
        r2.setId2(null);
        r2.setId3(2);
        r2.setId4(null);
        r2.setData(2);

        int[] result = create.batchStore(r1, r2).execute();

        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(1, 2), create.select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));

        r1.setData(11);
        r2.setData(12);
        result = create.batchStore(r1, r2).execute();

        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(asList(11, 12), create.select(T_3084_TWO_UNIQUE_KEYS.DATA).from(T_3084_TWO_UNIQUE_KEYS).orderBy(T_3084_TWO_UNIQUE_KEYS.ID1).fetchInto(int.class));
    }

    @Test
    public void testSQLServerInsertNullableUniqueKeysAfterCopy() {
        clean(T_3084_TWO_UNIQUE_KEYS);

        DSLContext create = create();
        create.configuration().settings().setUpdatablePrimaryKeys(true);

        T_3084TwoUniqueKeysRecord r1 = create.newRecord(T_3084_TWO_UNIQUE_KEYS);

        r1.setId1(1);
        r1.setId2(null);
        r1.setId3(1);
        r1.setId4(null);
        r1.setData(1);
        assertEquals(1, r1.insert());
        assertEquals(1, create.fetchCount(T_3084_TWO_UNIQUE_KEYS));

        r1 = create.fetchOne(T_3084_TWO_UNIQUE_KEYS);
        T_3084TwoUniqueKeysRecord r2 = r1.copy();
        r2.setId1(2);
        r2.setId2(null);
        r2.setId3(2);
        r2.setId4(null);

        assertEquals(1, r2.insert());
        assertEquals(2, create.fetchCount(T_3084_TWO_UNIQUE_KEYS));

        T_3084TwoUniqueKeysRecord r3 = r1.copy();
        r3.setId1(3);
        r3.setId2(null);
        r3.setId3(3);
        r3.setId4(null);

        assertEquals(1, r3.store());
        assertEquals(3, create.fetchCount(T_3084_TWO_UNIQUE_KEYS));
    }

    @Test
    public void testSQLServerPlainSQLWithSpecialTypes() {
        jOOQAbstractTest.reset = false;

        create().execute("insert into t_3085 values (null, null)");
        T_3085Record record = create().resultQuery("select * from t_3085").fetchAnyInto(T_3085);
        assertEquals(asList(null, null), asList(record.intoArray()));
    }

    @Test
    public void testSQLServerProceduresWithReturnValues() {
        P4106 r1 = Routines.p4106(create().configuration(), 1, 0);
        assertEquals(0, (int) r1.getReturnValue());
        assertEquals(1, (int) r1.getParam2());

        P4106 r2 = Routines.p4106(create().configuration(), 2, 0);
        assertEquals(0, (int) r2.getReturnValue());
        assertEquals(2, (int) r2.getParam2());

        P4106 r3 = Routines.p4106(create().configuration(), 5, 0);
        assertEquals(42, (int) r3.getReturnValue());
        assertEquals(5, (int) r3.getParam2());
    }

    @Test
    public void testSQLServerProceduresWithResults() {
        PResults r1 = new PResults();
        r1.setPResultSets(0);
        r1.execute(create().configuration());
        assertEquals(0, r1.getResults().size());

        PResults r2 = new PResults();
        r2.setPResultSets(1);
        r2.execute(create().configuration());
        assertEquals(1, r2.getResults().size());
        assertEquals(asList(1), r2.getResults().get(0).getValues(0, int.class));

        PResults r3 = new PResults();
        r3.setPResultSets(2);
        r3.execute(create().configuration());
        assertEquals(2, r3.getResults().size());
        assertEquals(asList(1), r3.getResults().get(0).getValues(0, int.class));
        assertEquals(asList(1, 2), r3.getResults().get(1).getValues(0, int.class));

        PResults r4 = new PResults();
        r4.setPResultSets(3);
        r4.execute(create().configuration());
        assertEquals(3, r4.getResults().size());
        assertEquals(asList(1), r4.getResults().get(0).getValues(0, int.class));
        assertEquals(asList(1, 2), r4.getResults().get(1).getValues(0, int.class));
        assertEquals(asList(1, 2, 3), r4.getResults().get(2).getValues(0, int.class));
    }

    @Test
    public void testSQLServerProceduresWithResultsAndRowCounts() {
        PResultsAndRowCounts r1 = new PResultsAndRowCounts();
        r1.setPResultSets(0);
        r1.execute(create().configuration());
        assertEquals(0, r1.getResults().size());

        PResultsAndRowCounts r2 = new PResultsAndRowCounts();
        r2.setPResultSets(1);
        r2.execute(create().configuration());
        assertEquals(1, r2.getResults().size());
        assertEquals(asList(1), r2.getResults().get(0).getValues(0, int.class));

        PResultsAndRowCounts r3 = new PResultsAndRowCounts();
        r3.setPResultSets(2);
        r3.execute(create().configuration());
        assertEquals(2, r3.getResults().size());
        assertEquals(asList(1), r3.getResults().get(0).getValues(0, int.class));
        assertEquals(asList(1, 2), r3.getResults().get(1).getValues(0, int.class));

        PResultsAndRowCounts r4 = new PResultsAndRowCounts();
        r4.setPResultSets(3);
        r4.execute(create().configuration());
        assertEquals(3, r4.getResults().size());
        assertEquals(asList(1), r4.getResults().get(0).getValues(0, int.class));
        assertEquals(asList(1, 2), r4.getResults().get(1).getValues(0, int.class));
        assertEquals(asList(1, 2, 3), r4.getResults().get(2).getValues(0, int.class));
    }

    @Test
    public void testSQLServerProcedureBooksAndAuthors() {
        Configuration configuration = create().configuration();

        // This is an ordinary stored procedure call
        PBooksAndAuthors p = new PBooksAndAuthors();
        p.execute(configuration);
        Results results = p.getResults();

        // We know there are two results. The first one contains authors
        // and the second one contains books
        List<Author> authors = results.get(0).into(Author.class);
        List<Book> books = results.get(1).into(Book.class);

        // The following tests show how author and book POJOs can be accessed type safely
        assertEquals(2, results.size());
        assertEquals(Arrays.asList(1, 2),
            authors.stream().map(a -> a.id).collect(Collectors.toList()));
        assertEquals(Arrays.asList("George", "Paulo"),
            authors.stream().map(a -> a.firstName).collect(Collectors.toList()));
        assertEquals(Arrays.asList("Orwell", "Coelho"),
            authors.stream().map(a -> a.lastName).collect(Collectors.toList()));

        assertEquals(Arrays.asList(1, 2, 3, 4),
            books.stream().map(b -> b.id).collect(Collectors.toList()));
        assertEquals(Arrays.asList("1984", "Animal Farm", "O Alquimista", "Brida"),
            books.stream().map(b -> b.title).collect(Collectors.toList()));
    }

    static class Author {
        int id;
        String firstName;
        String lastName;
    }

    static class Book {
        int id;
        String title;
    }

    @Test
    public void testSQLServerMixResultsWithUpdateCounts() {
        Results many =
        create().fetchMany(
            "declare @t table(i int);"
          + "insert into @t values (1);"
          + "insert into @t values (1), (2);"
          + "select * from @t;"
          + "insert into @t values (3);"
          + "insert into @t values (3), (3);"
          + "select * from @t;"
          + "select * from @t;"
        );


    }
}

/* [/pro] */