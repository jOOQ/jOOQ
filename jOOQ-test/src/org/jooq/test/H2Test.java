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

package org.jooq.test;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.test.h2.generatedclasses.Tables.T_2698;
import static org.jooq.test.h2.generatedclasses.Tables.T_AUTHOR;
import static org.jooq.test.h2.generatedclasses.Tables.T_BOOK;
import static org.jooq.test.h2.generatedclasses.Tables.T_BOOK_STORE;
import static org.jooq.test.h2.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.h2.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.h2.generatedclasses.Tables.T_DATES;
import static org.jooq.test.h2.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.h2.generatedclasses.Tables.T_IDENTITY;
import static org.jooq.test.h2.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.h2.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.h2.generatedclasses.Tables.V_2603;
import static org.jooq.test.h2.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.h2.generatedclasses.Tables.V_BOOK;
import static org.jooq.test.h2.generatedclasses.tables.TAuthor.FIRST_NAME;
import static org.jooq.test.h2.generatedclasses.tables.TAuthor.LAST_NAME;
import static org.jooq.test.h2.generatedclasses.tables.TBook.AUTHOR_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

// ...
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;
import org.jooq.test.h2.generatedclasses.Keys;
import org.jooq.test.h2.generatedclasses.Routines;
import org.jooq.test.h2.generatedclasses.Sequences;
import org.jooq.test.h2.generatedclasses.Tables;
import org.jooq.test.h2.generatedclasses.tables.TArrays;
import org.jooq.test.h2.generatedclasses.tables.TAuthor;
import org.jooq.test.h2.generatedclasses.tables.TBook;
import org.jooq.test.h2.generatedclasses.tables.TBookStore;
import org.jooq.test.h2.generatedclasses.tables.TBookToBookStore;
import org.jooq.test.h2.generatedclasses.tables.TBooleans;
import org.jooq.test.h2.generatedclasses.tables.TExoticTypes;
import org.jooq.test.h2.generatedclasses.tables.TIdentity;
import org.jooq.test.h2.generatedclasses.tables.TIdentityPk;
import org.jooq.test.h2.generatedclasses.tables.TTriggers;
import org.jooq.test.h2.generatedclasses.tables.TUnsigned;
import org.jooq.test.h2.generatedclasses.tables.T_639NumbersTable;
import org.jooq.test.h2.generatedclasses.tables.T_725LobTest;
import org.jooq.test.h2.generatedclasses.tables.T_785;
import org.jooq.test.h2.generatedclasses.tables.VLibrary;
import org.jooq.test.h2.generatedclasses.tables.daos.TAuthorDao;
import org.jooq.test.h2.generatedclasses.tables.daos.T_2698Dao;
import org.jooq.test.h2.generatedclasses.tables.daos.XUnusedDao;
import org.jooq.test.h2.generatedclasses.tables.pojos.T_2698;
import org.jooq.test.h2.generatedclasses.tables.pojos.XUnused;
import org.jooq.test.h2.generatedclasses.tables.records.TArraysRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TIdentityRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.h2.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.h2.generatedclasses.tables.records.T_2698Record;
import org.jooq.test.h2.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.h2.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.h2.generatedclasses.tables.records.T_785Record;
import org.jooq.test.h2.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.h2.generatedclasses.tables.records.V_2603Record;
import org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.h2.H2DataType;

import org.junit.Test;

/**
 * Integration test for the H2 database
 *
 * @author Espen Stromsnes
 */
public class H2Test extends jOOQAbstractTest<
        TAuthorRecord,
        org.jooq.test.h2.generatedclasses.tables.pojos.TAuthor,
        TBookRecord,
        TBookStoreRecord,
        TBookToBookStoreRecord,
        XUnusedRecord,
        VLibraryRecord,
        TArraysRecord,
        TDatesRecord,
        TBooleansRecord,
        XUnusedRecord,
        TTriggersRecord,
        TUnsignedRecord,
        TExoticTypesRecord,
        TIdentityRecord,
        TIdentityPkRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record,
        XUnusedRecord> {

    @Override
    protected DSLContext create0(Settings settings) {
        return DSL.using(getConnection(), SQLDialect.H2, settings);
    }

    @Override
    protected DAO<TAuthorRecord, org.jooq.test.h2.generatedclasses.tables.pojos.TAuthor, Integer> TAuthorDao() {
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
    protected TableField<TBookRecord, Integer> TBook_REC_VERSION() {
        return TBook.REC_VERSION;
    }

    @Override
    protected TableField<TBookRecord, Timestamp> TBook_REC_TIMESTAMP() {
        return TBook.REC_TIMESTAMP;
    }

    @Override
    protected Table<TBookStoreRecord> TBookStore() {
        return T_BOOK_STORE;
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
    protected Table<TArraysRecord> TArrays() {
        return TArrays.T_ARRAYS;
    }

    @Override
    protected TableField<TArraysRecord, Integer> TArrays_ID() {
        return TArrays.ID;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected TableField<TArraysRecord, String[]> TArrays_STRING() {
        return (TableField) TArrays.STRING_ARRAY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected TableField<TArraysRecord, Integer[]> TArrays_NUMBER() {
        return (TableField) TArrays.NUMBER_ARRAY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected TableField<TArraysRecord, Date[]> TArrays_DATE() {
        return (TableField) TArrays.DATE_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends UDTRecord<?>[]> TArrays_UDT() {
        return null;
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx x
        xxxxxx xxxxx
    x

    xx [/pro] */
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
        return Routines.fGetOneCursor(array);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Field<Integer[]> FArrays1Field(Field<Integer[]> array) {
        return (Field) Routines.fArrays1((Field) array);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Field<Long[]> FArrays2Field(Field<Long[]> array) {
        return (Field) Routines.fArrays2((Field) array);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Field<String[]> FArrays3Field(Field<String[]> array) {
        return (Field) Routines.fArrays3((Field)array);
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxxxxx xx xxxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxxxxx xx xxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxxxxx xx xxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxx
    x

    xx [/pro] */
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
        return false;
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
            H2DataType.BIGINT,
            H2DataType.BINARY,
            H2DataType.BIT,
            H2DataType.BLOB,
            H2DataType.BOOL,
            H2DataType.BOOLEAN,
            H2DataType.BYTEA,
            H2DataType.CHAR,
            H2DataType.CHARACTER,
            H2DataType.CLOB,
            H2DataType.DATE,
            H2DataType.DATETIME,
            H2DataType.DEC,
            H2DataType.DECIMAL,
            H2DataType.DOUBLE,
            H2DataType.FLOAT,
            H2DataType.FLOAT4,
            H2DataType.FLOAT8,
            H2DataType.IDENTITY,
            H2DataType.IMAGE,
            H2DataType.INT,
            H2DataType.INT2,
            H2DataType.INT4,
            H2DataType.INT8,
            H2DataType.INTEGER,
            H2DataType.LONGBLOB,
            H2DataType.LONGTEXT,
            H2DataType.LONGVARBINARY,
            H2DataType.LONGVARCHAR,
            H2DataType.MEDIUMBLOB,
            H2DataType.MEDIUMINT,
            H2DataType.MEDIUMTEXT,
            H2DataType.NCHAR,
            H2DataType.NCLOB,
            H2DataType.NTEXT,
            H2DataType.NUMBER,
            H2DataType.NUMERIC,
            H2DataType.NVARCHAR,
            H2DataType.NVARCHAR2,
            H2DataType.OID,
            H2DataType.OTHER,
            H2DataType.RAW,
            H2DataType.REAL,
            H2DataType.SIGNED,
            H2DataType.SMALLDATETIME,
            H2DataType.SMALLINT,
            H2DataType.TEXT,
            H2DataType.TIME,
            H2DataType.TIMESTAMP,
            H2DataType.TINYBLOB,
            H2DataType.TINYINT,
            H2DataType.TINYTEXT,
            H2DataType.UUID,
            H2DataType.VARBINARY,
            H2DataType.VARCHAR,
            H2DataType.VARCHAR2,
            H2DataType.VARCHAR_CASESENSITIVE,
            H2DataType.VARCHAR_IGNORECASE,
        };
    }

    @SuppressWarnings("unused")
    private void testExtensibilityOfNonFinalClasses() {

        // [#2308] This test is not executed. It is a "compiler" test, checking
        // whether some generated artefacts can be extended

        class MyKeys extends Keys {}
        class MySequences extends Sequences {}
        class MyTables extends Tables {}
    }

    @Test
    public void testH2CreateViewPlainSQL() throws Exception {
        try {
            create().execute("CREATE VIEW my_view AS\n{0};",
                select(FIRST_NAME, LAST_NAME, count().cast(Long.class).as("Books Written"))
                .from(T_AUTHOR)
                .join(T_BOOK)
                .on(TAuthor.ID.eq(AUTHOR_ID))
                .groupBy(FIRST_NAME, LAST_NAME)
                .orderBy(FIRST_NAME, LAST_NAME)
            );

            Result<Record> result = create().fetch("SELECT * FROM my_view");
            assertEquals(2, result.size());
            assertEquals(AUTHOR_FIRST_NAMES, result.getValues(0));
            assertEquals(AUTHOR_LAST_NAMES, result.getValues(1));
            assertEquals(asList(2L, 2L), result.getValues("Books Written"));
        }

        finally {
            create().execute("DROP VIEW my_view");
        }
    }

    @Test
    public void testH2V2603WithExcludedColumns() throws Exception {

        // The generated table only has two columns
        V_2603Record record =
        create().selectFrom(V_2603)
                .fetchOne();

        assertEquals(2, record.size());
        assertEquals(1, (int) record.getCol1());
        assertEquals(4, (int) record.getCol4());

        // The actual table has four columns
        Record r =
        create().selectFrom(table(V_2603.getName()))
                .fetchOne();

        assertEquals(4, r.size());
        assertEquals(asList(1, 2, 3, 4), asList(r.intoArray()));
    }

    @Test
    public void testH2T2698InsertRecordWithDefault() throws Exception {
        jOOQAbstractTest.reset = false;

        T_2698Record record = create().newRecord(T_2698);
        record.setId(1);
        assertEquals(1, record.store());
        record.refresh();
        assertEquals(-1, (int) record.getXx());
    }

    // TODO [#2700] @Test
    public void testH2T2698InsertPojoThroughDaoWithDefault() throws Exception {
        jOOQAbstractTest.reset = false;

        T_2698Dao dao = new T_2698Dao(create().configuration());
        dao.insert(new T_2698(1, null));
        List<T_2698> list = dao.fetchById(1);
        assertEquals(1, list.size());
        assertEquals(1, (int) list.get(0).getId());
        assertEquals(-1, (int) list.get(0).getXx());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testH2DaoWithCompositeKey() throws Exception {
        jOOQAbstractTest.reset = false;

        Record2<Integer, String> key1 = create().newRecord(
            org.jooq.test.h2.generatedclasses.tables.XUnused.ID,
            org.jooq.test.h2.generatedclasses.tables.XUnused.NAME);

        Record2<Integer, String> key2 = create().newRecord(
            org.jooq.test.h2.generatedclasses.tables.XUnused.ID,
            org.jooq.test.h2.generatedclasses.tables.XUnused.NAME);
        key2.setValue(org.jooq.test.h2.generatedclasses.tables.XUnused.ID, 1);
        key2.setValue(org.jooq.test.h2.generatedclasses.tables.XUnused.NAME, "name");

        XUnusedDao dao = new XUnusedDao(create().configuration());
        dao.insert(new XUnused().setId(1).setName("name").setFields(1));
        assertNull(dao.findById(key1));

        XUnused pojo = dao.findById(key2);
        assertEquals(1, (int) pojo.getId());
        assertEquals("name", pojo.getName());
        assertEquals(1, (int) pojo.getFields());

        pojo.setFields(2);
        dao.update(pojo);

        pojo = dao.findById(key2);
        assertEquals(1, (int) pojo.getId());
        assertEquals("name", pojo.getName());
        assertEquals(2, (int) pojo.getFields());

        dao.deleteById(key2);
        assertNull(dao.findById(key2));
    }
}
