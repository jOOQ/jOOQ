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
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.postgres.generatedclasses.Routines.fSearchBook;
import static org.jooq.test.postgres.generatedclasses.Tables.T_639_NUMBERS_TABLE;
import static org.jooq.test.postgres.generatedclasses.Tables.T_725_LOB_TEST;
import static org.jooq.test.postgres.generatedclasses.Tables.T_785;
import static org.jooq.test.postgres.generatedclasses.Tables.T_959;
import static org.jooq.test.postgres.generatedclasses.Tables.T_ARRAYS;
import static org.jooq.test.postgres.generatedclasses.Tables.T_AUTHOR;
import static org.jooq.test.postgres.generatedclasses.Tables.T_BOOK;
import static org.jooq.test.postgres.generatedclasses.Tables.T_BOOK_STORE;
import static org.jooq.test.postgres.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.postgres.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.postgres.generatedclasses.Tables.T_DATES;
import static org.jooq.test.postgres.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.postgres.generatedclasses.Tables.T_IDENTITY;
import static org.jooq.test.postgres.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.postgres.generatedclasses.Tables.T_INHERITANCE_1;
import static org.jooq.test.postgres.generatedclasses.Tables.T_PG_EXTENSIONS;
import static org.jooq.test.postgres.generatedclasses.Tables.T_TRIGGERS;
import static org.jooq.test.postgres.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.postgres.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.postgres.generatedclasses.Tables.V_BOOK;
import static org.jooq.test.postgres.generatedclasses.Tables.V_LIBRARY;
import static org.jooq.util.postgres.PostgresDSL.arrayAppend;
import static org.jooq.util.postgres.PostgresDSL.arrayCat;
import static org.jooq.util.postgres.PostgresDSL.arrayPrepend;
import static org.jooq.util.postgres.PostgresDSL.arrayToString;
import static org.jooq.util.postgres.PostgresDSL.only;
import static org.jooq.util.postgres.PostgresDSL.stringToArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

// ...
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;
import org.jooq.test.postgres.generatedclasses.Keys;
import org.jooq.test.postgres.generatedclasses.Routines;
import org.jooq.test.postgres.generatedclasses.Sequences;
import org.jooq.test.postgres.generatedclasses.enums.UCountry;
import org.jooq.test.postgres.generatedclasses.enums.U_959;
import org.jooq.test.postgres.generatedclasses.tables.TArrays;
import org.jooq.test.postgres.generatedclasses.tables.records.TArraysRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TIdentityRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TPgExtensionsRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.T_785Record;
import org.jooq.test.postgres.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.test.postgres.generatedclasses.udt.UAddressType;
import org.jooq.test.postgres.generatedclasses.udt.UStreetType;
import org.jooq.test.postgres.generatedclasses.udt.records.UUuidsRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.postgres.PostgresDataType;

import org.junit.Test;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.geometric.PGbox;
import org.postgresql.util.PGInterval;


/**
 * @author Lukas Eder
 */
public class PostgresTest extends jOOQAbstractTest<
        TAuthorRecord,
        Object,
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
        return DSL.using(getConnection(), SQLDialect.POSTGRES, settings);
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
        return T_AUTHOR.ADDRESS;
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
        return null;
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
    protected Table<TArraysRecord> TArrays() {
        return T_ARRAYS;
    }

    @Override
    protected TableField<TArraysRecord, Integer> TArrays_ID() {
        return T_ARRAYS.ID;
    }

    @Override
    protected TableField<TArraysRecord, String[]> TArrays_STRING() {
        return T_ARRAYS.STRING_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, Integer[]> TArrays_NUMBER() {
        return T_ARRAYS.NUMBER_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, Date[]> TArrays_DATE() {
        return T_ARRAYS.DATE_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends UDTRecord<?>[]> TArrays_UDT() {
        return T_ARRAYS.UDT_ARRAY;
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
        return T_BOOK.LANGUAGE_ID;
    }

    @Override
    protected TableField<TBookRecord, String> TBook_CONTENT_TEXT() {
        return T_BOOK.CONTENT_TEXT;
    }

    @Override
    protected TableField<TBookRecord, Integer> TBook_PUBLISHED_IN() {
        return T_BOOK.PUBLISHED_IN;
    }

    @Override
    protected TableField<TBookRecord, byte[]> TBook_CONTENT_PDF() {
        return T_BOOK.CONTENT_PDF;
    }

    @Override
    protected TableField<TBookRecord, ? extends Enum<?>> TBook_STATUS() {
        return T_BOOK.STATUS;
    }

    @Override
    protected ForeignKey<TBookRecord, TAuthorRecord> FK_T_BOOK_AUTHOR_ID() {
        return Keys.T_BOOK__FK_T_BOOK_AUTHOR_ID;
    }

    @Override
    protected ForeignKey<TBookRecord, TAuthorRecord> FK_T_BOOK_CO_AUTHOR_ID() {
        return Keys.T_BOOK__FK_T_BOOK_CO_AUTHOR_ID;
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
        return Routines.fGetOneCursor(array);
    }

    @Override
    protected Field<Integer[]> FArrays1Field(Field<Integer[]> array) {
        return Routines.fArrays1(array);
    }

    @Override
    protected Field<Long[]> FArrays2Field(Field<Long[]> array) {
        return Routines.fArrays2(array);
    }

    @Override
    protected Field<String[]> FArrays3Field(Field<String[]> array) {
        return Routines.fArrays3(array);
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
            PostgresDataType.ACLITEM,
            PostgresDataType.BIGINT,
            PostgresDataType.BIT,
            PostgresDataType.BITVARYING,
            PostgresDataType.BOOL,
            PostgresDataType.BOOLEAN,
            PostgresDataType.BYTEA,
            PostgresDataType.CHAR,
            PostgresDataType.CHARACTER,
            PostgresDataType.CHARACTERVARYING,
            PostgresDataType.CID,
            PostgresDataType.DATE,
            PostgresDataType.DECIMAL,
            PostgresDataType.DOUBLEPRECISION,
            PostgresDataType.FLOAT4,
            PostgresDataType.FLOAT8,
            PostgresDataType.INT,
            PostgresDataType.INT2,
            PostgresDataType.INT4,
            PostgresDataType.INT8,
            PostgresDataType.INTEGER,
            PostgresDataType.MONEY,
            PostgresDataType.NAME,
            PostgresDataType.NUMERIC,
            PostgresDataType.OID,
            PostgresDataType.OIDVECTOR,
            PostgresDataType.REAL,
            PostgresDataType.REGPROC,
            PostgresDataType.SMALLINT,
            PostgresDataType.TEXT,
            PostgresDataType.TID,
            PostgresDataType.TIME,
            PostgresDataType.TIMESTAMP,
            PostgresDataType.TIMESTAMPTZ,
            PostgresDataType.TIMESTAMPWITHOUTTIMEZONE,
            PostgresDataType.TIMESTAMPWITHTIMEZONE,
            PostgresDataType.TIMETZ,
            PostgresDataType.TIMEWITHOUTTIMEZONE,
            PostgresDataType.TIMEWITHTIMEZONE,
        };
    }

    @Test
    public void testPostgresJavaKeywordEnums() throws Exception {
        reset = false;

        assertEquals(3,
        create().insertInto(T_959)
                .set(T_959.JAVA_KEYWORDS, U_959.public_)
                .newRecord()
                .set(T_959.JAVA_KEYWORDS, U_959.abstract_)
                .newRecord()
                .set(T_959.JAVA_KEYWORDS, U_959.class_)
                .execute());

        List<U_959> result =
        create().selectFrom(T_959)
                .orderBy(T_959.JAVA_KEYWORDS)
                .fetch(T_959.JAVA_KEYWORDS);

        assertEquals(3, result.size());
        assertEquals(U_959.abstract_, result.get(0));
        assertEquals(U_959.class_, result.get(1));
        assertEquals(U_959.public_, result.get(2));
    }

    @Test
    public void testPostgresArrayOperations() throws Exception {

        // [#1107] The contains operator @> is implemented in Field.contains()
        // -------------------------------------------------------------------
        assertEquals(0,
        create().selectCount()
                .from(T_ARRAYS)
                .where(T_ARRAYS.NUMBER_ARRAY.contains((Integer[])null))
                .fetchOne(0));

        assertEquals(3,
        create().selectCount()
                .from(T_ARRAYS)
                .where(T_ARRAYS.NUMBER_ARRAY.contains(new Integer[0]))
                .fetchOne(0));

        assertEquals(2,
        create().selectCount()
                .from(T_ARRAYS)
                .where(T_ARRAYS.NUMBER_ARRAY.contains(new Integer[] { 1 }))
                .fetchOne(0));

        assertEquals(1,
        create().selectCount()
                .from(T_ARRAYS)
                .where(T_ARRAYS.NUMBER_ARRAY.contains(new Integer[] { 1, 2 }))
                .fetchOne(0));

        assertEquals(0,
        create().selectCount()
                .from(T_ARRAYS)
                .where(T_ARRAYS.NUMBER_ARRAY.contains(new Integer[] { 1, 2, 3 }))
                .fetchOne(0));

    }

    @Test
    public void testPostgresArrayFunctions() throws Exception {
        String[] a_ab = new String[] { "a", "b" };
        Param<String[]> v_ab = val(a_ab);
        Param<String[]> v_n = val(null, v_ab);

        // array_append()
        // ---------------------------------------------------------------------
        Record5<String[], String[], String[], String[], String[]> r1 = create()
        .select(
            arrayAppend(v_ab, "c"),
            arrayAppend(v_ab, val("c")),
            arrayAppend(a_ab, "c"),
            arrayAppend(a_ab, val("c")),
            arrayAppend(v_n, "c"))
        .fetchOne();

        assertEquals(asList("a", "b", "c"), asList(r1.value1()));
        assertEquals(asList("a", "b", "c"), asList(r1.value2()));
        assertEquals(asList("a", "b", "c"), asList(r1.value3()));
        assertEquals(asList("a", "b", "c"), asList(r1.value4()));
        assertEquals(asList("c"), asList(r1.value5()));

        // array_prepend()
        // ---------------------------------------------------------------------
        Record5<String[], String[], String[], String[], String[]> r2 = create()
        .select(
            arrayPrepend("c", v_ab),
            arrayPrepend(val("c"), v_ab),
            arrayPrepend("c", a_ab),
            arrayPrepend(val("c"), a_ab),
            arrayPrepend("c", v_n))
        .fetchOne();

        assertEquals(asList("c", "a", "b"), asList(r2.value1()));
        assertEquals(asList("c", "a", "b"), asList(r2.value2()));
        assertEquals(asList("c", "a", "b"), asList(r2.value3()));
        assertEquals(asList("c", "a", "b"), asList(r2.value4()));
        assertEquals(asList("c"), asList(r2.value5()));

        // array_cat()
        // ---------------------------------------------------------------------
        Record5<String[], String[], String[], String[], String[]> r3 = create()
        .select(
            arrayCat(v_ab, v_ab),
            arrayCat(v_ab, a_ab),
            arrayCat(a_ab, v_ab),
            arrayCat(a_ab, a_ab),
            arrayCat(v_n, v_ab))
        .fetchOne();

        assertEquals(asList("a", "b", "a", "b"), asList(r3.value1()));
        assertEquals(asList("a", "b", "a", "b"), asList(r3.value2()));
        assertEquals(asList("a", "b", "a", "b"), asList(r3.value3()));
        assertEquals(asList("a", "b", "a", "b"), asList(r3.value4()));
        assertEquals(asList("a", "b"), asList(r3.value5()));

        // array_to_string()
        // ---------------------------------------------------------------------
        Record5<String, String, String, String, String> r4 = create()
        .select(
            arrayToString(v_ab, "--"),
            arrayToString(v_ab, val("--")),
            arrayToString(a_ab, "--"),
            arrayToString(a_ab, val("--")),
            arrayToString(v_n, "--"))
        .fetchOne();

        assertEquals("a--b", r4.value1());
        assertEquals("a--b", r4.value2());
        assertEquals("a--b", r4.value3());
        assertEquals("a--b", r4.value4());
        assertNull(r4.value5());

        // string_to_array()
        // ---------------------------------------------------------------------
        Record5<String[], String[], String[], String[], String[]> r5 = create()
        .select(
            stringToArray("a--b", "--"),
            stringToArray("a--b", val("--")),
            stringToArray(val("a--b"), "--"),
            stringToArray(val("a--b"), val("--")),
            stringToArray(val(null, String.class), "--"))
        .fetchOne();

        assertEquals(asList("a", "b"), asList(r5.value1()));
        assertEquals(asList("a", "b"), asList(r5.value2()));
        assertEquals(asList("a", "b"), asList(r5.value3()));
        assertEquals(asList("a", "b"), asList(r5.value4()));
        assertNull(r5.value5());
    }

    @Test
    public void testPostgresTableFunction() throws Exception {

        // TODO [#1139] Further elaborate this test
        create().select().from(fSearchBook("Animal", 1L, 0L).toString()).fetch();
        System.out.println(create().select(fSearchBook("Animal", 1L, 0L)).fetch());

        // This doesn't work, as jOOQ doesn't know how to correctly register
        // OUT parameters for the returned cursor
        // Object result = Routines.fSearchBook(create(), "Animal", 1L, 0L);
    }

    @Test
    public void testPostgresExtensions() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#2267] Try to execute some basic CRUD operations on PostGIS data
        // types. Even without formal support, jOOQ should allow to pass these
        // objects through to JDBC
        Point point = new Point(0, 0);
        point.setSrid(4326);
        PGgeometry geometry = new PGgeometry(point);

        // [#2267] Non-PostGIS geometry objects should work, too
        PGbox box = new PGbox(1.0, 1.0, 2.0, 2.0);
        PGInterval interval = new PGInterval(1, 2, 3, 4, 5, 6);

        TPgExtensionsRecord r1 = create().newRecord(T_PG_EXTENSIONS);
        r1.setPgGeometry(geometry);
        r1.setPgInterval(interval);
        r1.setPgBox(box);
        assertEquals(1, r1.store());
        assertEquals(1, (int) create().selectCount().from(T_PG_EXTENSIONS).fetchOne(0, int.class));

        TPgExtensionsRecord r2 = create().selectFrom(T_PG_EXTENSIONS).fetchOne();
        assertEquals(r1, r2);

        assertEquals(box, r2.getPgBox());
        assertEquals(geometry, r2.getPgGeometry());
        assertEquals(interval, r2.getPgInterval());
    }

    @Test
    public void testPostgresUDTTypes() throws Exception {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        UUID[] array = new UUID[] { uuid1, uuid2 };

        UUuidsRecord uuids = new UUuidsRecord();
        uuids.setU1(uuid1);
        uuids.setU2(array);

        Field<UUuidsRecord> val = val(uuids).as("val");
        Record1<UUuidsRecord> record = create().select(val).fetchOne();
        assertEquals(uuids, record.getValue(val));
        assertEquals(uuid1, record.getValue(val).getU1());
        assertEquals(uuid1, record.getValue(val).getU2()[0]);
        assertEquals(uuid2, record.getValue(val).getU2()[1]);
    }

    @Test
    public void testPostgresEnumType() throws Exception {
        // [#2549] TODO: Re-enable this
        // Param<UCountry> val = val(UCountry.England);
        Param<UCountry> val = val(UCountry.England, UAddressType.COUNTRY.getDataType());

        // [#2135] Be sure that all settings are applied to explicit casts to
        // PostgreSQL enum types
        assertEquals("'England'", create().renderInlined(val));
        assertEquals("?::\"public\".\"u_country\"",
            create().render(val));
        assertEquals("?::\"u_country\"",
            create(new Settings().withRenderSchema(false)).render(val));
        assertEquals("?::PUBLIC.U_COUNTRY",
            create(new Settings().withRenderNameStyle(RenderNameStyle.UPPER)).render(val));
        assertEquals("?::\"u_country\"",
            create(new Settings().withRenderMapping(
                new RenderMapping().withDefaultSchema("public"))).render(val));
        assertEquals("?::\"test\".\"u_country\"",
            create(new Settings().withRenderMapping(
                   new RenderMapping().withSchemata(
                   new MappedSchema().withInput("public")
                                     .withOutput("test")))).render(val));


    }

    @Test
    public void testPostgresOnlyClause() throws Exception {
        assertEquals(3, create().fetchCount(selectOne().from(T_INHERITANCE_1)));
        assertEquals(2, create().fetchCount(selectOne().from(only(T_INHERITANCE_1))));
    }

    @Test
    public void testPostgresJsonDataType() throws Exception {
        jOOQAbstractTest.reset = false;

        create().insertInto(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.JS)
                .values(1, null)
                .values(2, "{}")
                .values(3, "[]")
                .values(4, "{\"hello\":\"world\"}")
                .execute();
    }

    @Test
    public void testPostgresTableRename() throws Exception {
        Name b1 = name("b1");
        Name b2 = name("b2");

        String create = "create or replace view {0} as select * from {1} where {2} = {3}";
        String drop = "drop view if exists {0}";

        try {
            create().execute(create, b1, T_BOOK, T_BOOK.ID, inline(1));
            create().execute(create, b2, T_BOOK, T_BOOK.ID, inline(2));

            org.jooq.test.postgres.generatedclasses.tables.TBook book1 = T_BOOK.rename("b1");
            org.jooq.test.postgres.generatedclasses.tables.TBook book2 = T_BOOK.rename("b2");

            org.jooq.test.postgres.generatedclasses.tables.TBook x1 = book1.as("x1");
            org.jooq.test.postgres.generatedclasses.tables.TBook x2 = book2.as("x2");

            assertEquals(2,
            create().select()
                    .from(book1)
                    .union(
                     select()
                    .from(book2))
                    .fetchCount());

            assertEquals(
                asList(1, 2),
                create().select()
                        .from(
                             select(book1.ID)
                            .from(book1)
                            .union(
                             select(book2.ID)
                            .from(book2))
                        )
                        .orderBy(1)
                        .fetch(0, int.class));

            assertEquals(
                asList(1, 2),
                create().select()
                        .from(
                             select(x1.ID)
                            .from(x1)
                            .union(
                             select(x2.ID)
                            .from(x2))
                        )
                        .orderBy(1)
                        .fetch(0, int.class));
        }
        finally {
            create().execute(drop, b1);
            create().execute(drop, b2);
        }
    }


    static class Street {
        public String street;
        public String no;
    }

    static class Address {
        public Street street;
        public String city;
        public String country;
    }

    static class Author {
        public String firstName;
        public String lastName;
        public Address address;
    }

    @Test
    public void testPostgresUDTRecordMapping() throws Exception {
        TAuthorRecord record = create()
            .selectFrom(T_AUTHOR)
            .where(T_AUTHOR.ID.eq(1))
            .fetchOne();

        Author author = record.into(Author.class);
        assertEquals("George", author.firstName);
        assertEquals("Orwell", author.lastName);
        assertEquals("Hampstead", author.address.city);
        assertEquals("England", author.address.country);
        assertEquals("Parliament Hill", author.address.street.street);
        assertEquals("77", author.address.street.no);
        System.out.println(author);
    }

    @Test
    public void testPostgresEnumArrayCRUD() throws Exception {
        testPostgresEnumArrayCRUD0(create());
    }


    @Test
    public void testPostgresEnumArrayCRUDWithInline() throws Exception {
        testPostgresEnumArrayCRUD0(create(new Settings().withStatementType(STATIC_STATEMENT)));
    }

    private void testPostgresEnumArrayCRUD0(DSLContext create) throws Exception {
        jOOQAbstractTest.reset = false;

        TArrays a = T_ARRAYS;

        assertEquals(4,
        create.insertInto(a, a.ID, a.ENUM_ARRAY)
              .values(11, null)
              .values(12, new UCountry[0])
              .values(13, new UCountry[] { null })
              .values(14, new UCountry[] { UCountry.Brazil })
              .execute());

        List<UCountry[]> countries =
        create.select(a.ENUM_ARRAY)
              .from(a)
              .where(a.ID.gt(10))
              .orderBy(a.ID)
              .fetch(a.ENUM_ARRAY);

        assertNull(countries.get(0));
        assertEquals(0, countries.get(1).length);
        assertEquals(1, countries.get(2).length);
        assertNull(countries.get(2)[0]);
        assertEquals(1, countries.get(3).length);
        assertEquals(UCountry.Brazil, countries.get(3)[0]);
    }
}
