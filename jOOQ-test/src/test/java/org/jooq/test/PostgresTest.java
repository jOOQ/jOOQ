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
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.toList;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.rowsFrom;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import static org.jooq.lambda.tuple.Tuple.range;
import static org.jooq.test.postgres.generatedclasses.Tables.F_ARRAY_TABLES;
import static org.jooq.test.postgres.generatedclasses.Tables.F_SEARCH_BOOKS;
import static org.jooq.test.postgres.generatedclasses.Tables.F_SEARCH_BOOK_IDS;
import static org.jooq.test.postgres.generatedclasses.Tables.F_SEARCH_BOOK_TITLES;
import static org.jooq.test.postgres.generatedclasses.Tables.F_TABLES1;
import static org.jooq.test.postgres.generatedclasses.Tables.F_TABLES2;
import static org.jooq.test.postgres.generatedclasses.Tables.F_TABLES3;
import static org.jooq.test.postgres.generatedclasses.Tables.F_TABLES4;
import static org.jooq.test.postgres.generatedclasses.Tables.F_TABLES5;
import static org.jooq.test.postgres.generatedclasses.Tables.T_3111;
import static org.jooq.test.postgres.generatedclasses.Tables.T_639_NUMBERS_TABLE;
import static org.jooq.test.postgres.generatedclasses.Tables.T_725_LOB_TEST;
import static org.jooq.test.postgres.generatedclasses.Tables.T_785;
import static org.jooq.test.postgres.generatedclasses.Tables.T_959;
import static org.jooq.test.postgres.generatedclasses.Tables.T_986_1;
import static org.jooq.test.postgres.generatedclasses.Tables.T_986_2;
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
import static org.jooq.test.postgres.generatedclasses.Tables.T_TRIGGERS;
import static org.jooq.test.postgres.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.postgres.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.postgres.generatedclasses.Tables.V_BOOK;
import static org.jooq.test.postgres.generatedclasses.Tables.V_LIBRARY;
import static org.jooq.test.postgres.generatedclasses.Tables.X_UNUSED;
import static org.jooq.util.postgres.PostgresDSL.array;
import static org.jooq.util.postgres.PostgresDSL.arrayAppend;
import static org.jooq.util.postgres.PostgresDSL.arrayCat;
import static org.jooq.util.postgres.PostgresDSL.arrayFill;
import static org.jooq.util.postgres.PostgresDSL.arrayLength;
import static org.jooq.util.postgres.PostgresDSL.arrayPrepend;
import static org.jooq.util.postgres.PostgresDSL.arrayRemove;
import static org.jooq.util.postgres.PostgresDSL.arrayReplace;
import static org.jooq.util.postgres.PostgresDSL.arrayToString;
import static org.jooq.util.postgres.PostgresDSL.only;
import static org.jooq.util.postgres.PostgresDSL.stringToArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.jooq.AggregateFunction;
import org.jooq.ArrayRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record8;
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
import org.jooq.lambda.tuple.Range;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.all.types.JSONJacksonHelloWorld;
import org.jooq.test.all.types.Position;
import org.jooq.test.all.types.UUIDWrapper;
import org.jooq.test.postgres.generatedclasses.Keys;
import org.jooq.test.postgres.generatedclasses.Routines;
import org.jooq.test.postgres.generatedclasses.Sequences;
import org.jooq.test.postgres.generatedclasses.enums.UCountry;
import org.jooq.test.postgres.generatedclasses.enums.U_959;
import org.jooq.test.postgres.generatedclasses.tables.FTables1;
import org.jooq.test.postgres.generatedclasses.tables.FTables2;
import org.jooq.test.postgres.generatedclasses.tables.FTables3;
import org.jooq.test.postgres.generatedclasses.tables.FTables4;
import org.jooq.test.postgres.generatedclasses.tables.TArrays;
import org.jooq.test.postgres.generatedclasses.tables.records.FArrayTablesRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.FSearchBookIdsRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.FSearchBookTitlesRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.FSearchBooksRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.FTables2Record;
import org.jooq.test.postgres.generatedclasses.tables.records.FTables3Record;
import org.jooq.test.postgres.generatedclasses.tables.records.FTables4Record;
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
import org.jooq.test.postgres.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.T_3111Record;
import org.jooq.test.postgres.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.T_785Record;
import org.jooq.test.postgres.generatedclasses.tables.records.T_986_1Record;
import org.jooq.test.postgres.generatedclasses.tables.records.T_986_2Record;
import org.jooq.test.postgres.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.postgres.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.test.postgres.generatedclasses.udt.UAddressType;
import org.jooq.test.postgres.generatedclasses.udt.UStreetType;
import org.jooq.test.postgres.generatedclasses.udt.records.UAddressTypeRecord;
import org.jooq.test.postgres.generatedclasses.udt.records.UStreetTypeRecord;
import org.jooq.test.postgres.generatedclasses.udt.records.UUuidsRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.postgres.PostgresDataType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;


/**
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        XUnusedRecord,
        TIdentityRecord,
        TIdentityPkRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record,
        XUnusedRecord> {

    @Override
    protected SQLDialect dialect() {
        return SQLDialect.POSTGRES;
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

    /* [pro] */
    @Override
    protected TableField<TArraysRecord, ArrayRecord<String>> TArrays_STRING_R() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, ArrayRecord<Integer>> TArrays_NUMBER_R() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, ArrayRecord<Date>> TArrays_DATE_R() {
        return null;
    }

    @Override
    protected TableField<TArraysRecord, ArrayRecord<Long>> TArrays_NUMBER_LONG_R() {
        return null;
    }

    /* [/pro] */
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected AggregateFunction secondMax(Field val) {
        return Routines.secondMax(val);
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
        Record6<String[], String[], String[], String[], String[], String[]> r5 = create()
        .select(
            stringToArray("a--b", "--"),
            stringToArray("a--b", val("--")),
            stringToArray(val("a--b"), "--"),
            stringToArray(val("a--b"), val("--")),
            stringToArray(val(null, String.class), "--"),
            stringToArray("a-b-c", "-", "b")
        )
        .fetchOne();

        assertEquals(asList("a", "b"), asList(r5.value1()));
        assertEquals(asList("a", "b"), asList(r5.value2()));
        assertEquals(asList("a", "b"), asList(r5.value3()));
        assertEquals(asList("a", "b"), asList(r5.value4()));
        assertNull(r5.value5());
        assertEquals(asList("a", null, "c"), asList(r5.value6()));

        // array_fill()
        // ---------------------------------------------------------------------
        Record4<Integer[], Integer[], Integer[], Integer[]> r6 = create()
        .select(
            arrayFill(1, new Integer[] { 3 }),
            arrayFill(val(1), new Integer[] { 3 }),
            arrayFill(1, val(new Integer[] { 3 })),
            arrayFill(val(1), val(new Integer[] { 3 }))
        )
        .fetchOne();

        assertEquals(nCopies(3, 1), asList(r6.value1()));
        assertEquals(nCopies(3, 1), asList(r6.value2()));
        assertEquals(nCopies(3, 1), asList(r6.value3()));
        assertEquals(nCopies(3, 1), asList(r6.value4()));

        // array_length()
        // ---------------------------------------------------------------------
        Record2<Integer, Integer> r7 = create()
        .select(
            arrayLength(new Integer[] { 1, 2, 3 }),
            arrayLength(val(new Integer[] { 1, 2, 3 }))
        )
        .fetchOne();

        assertEquals(3, (int) r7.value1());
        assertEquals(3, (int) r7.value2());

        // array(select)
        // ---------------------------------------------------------------------
        Result<Record2<Integer, Integer[]>> r8 = create()
        .select(
            T_AUTHOR.ID,
            array(select(T_BOOK.ID)
                .from(T_BOOK)
                .where(T_BOOK.AUTHOR_ID.eq(T_AUTHOR.ID))
                .orderBy(T_BOOK.ID))
        )
        .from(T_AUTHOR)
        .orderBy(T_AUTHOR.ID)
        .fetch();

        assertEquals(2, r8.size());
        assertEquals(asList(1, 2), r8.getValues(0));
        assertEquals(asList(1, 2), asList(r8.get(0).getValue(1, Integer[].class)));
        assertEquals(asList(3, 4), asList(r8.get(1).getValue(1, Integer[].class)));

        // array_remove()
        // ---------------------------------------------------------------------
        Record4<Integer[], Integer[], Integer[], Integer[]> r9 = create()
        .select(
            arrayRemove(new Integer[] { 1, 2, 3, 2 }, 2),
            arrayRemove(new Integer[] { 1, 2, 3, 2 }, val(2)),
            arrayRemove(val(new Integer[] { 1, 2, 3, 2 }), 2),
            arrayRemove(DSL.<Integer[]>val(new Integer[] { 1, 2, 3, 2 }), val(2))
        )
        .fetchOne();
        assertEquals(asList(1, 3), asList(r9.value1()));
        assertEquals(asList(1, 3), asList(r9.value2()));
        assertEquals(asList(1, 3), asList(r9.value3()));
        assertEquals(asList(1, 3), asList(r9.value4()));

        // array_replace()
        // ---------------------------------------------------------------------
        Record4<Integer[], Integer[], Integer[], Integer[]> r10 = create()
        .select(
            arrayReplace(new Integer[] { 1, 2, 3, 5 }, 5, 4),
            arrayReplace(new Integer[] { 1, 2, 3, 5 }, val(5), val(4)),
            arrayReplace(val(new Integer[] { 1, 2, 3, 5 }), 5, 4),
            arrayReplace(DSL.<Integer[]>val(new Integer[] { 1, 2, 3, 5 }), val(5), val(4))
        )
        .fetchOne();
        assertEquals(asList(1, 2, 3, 4), asList(r10.value1()));
        assertEquals(asList(1, 2, 3, 4), asList(r10.value2()));
        assertEquals(asList(1, 2, 3, 4), asList(r10.value3()));
        assertEquals(asList(1, 2, 3, 4), asList(r10.value4()));
    }

    @Test
    public void testPostgresTableFunction() throws Exception {

        // [#1139] [#3375] [#3376] PostgreSQL knows two types of table-valued
        // functions:
        // - Those returning a SETOF [ table type ]
        // - Those returning a TABLE type

        Result<Record2<Integer, String>> books1 =
        create().select(
                    F_SEARCH_BOOKS.ID,
                    F_SEARCH_BOOKS.TITLE)
                .from(F_SEARCH_BOOKS("A", 2L, 0L))
                .orderBy(F_SEARCH_BOOKS.ID)
                .fetch();

        assertEquals(2, books1.size());
        assertEquals(asList(2, 3), books1.getValues(F_SEARCH_BOOKS.ID));
        assertEquals(BOOK_TITLES.subList(1, 3), books1.getValues(F_SEARCH_BOOKS.TITLE));

        Result<FSearchBooksRecord> books2 =
        create().selectFrom(F_SEARCH_BOOKS("A", 2L, 0L))
                .orderBy(F_SEARCH_BOOKS.ID)
                .fetch();

        assertEquals(2, books2.size());
        assertEquals(asList(2, 3), books2.getValues(F_SEARCH_BOOKS.ID));
        assertEquals(asList(2, 3), books2.stream().map(FSearchBooksRecord::getId).collect(toList()));
        assertEquals(BOOK_TITLES.subList(1, 3), books2.getValues(F_SEARCH_BOOKS.TITLE));
        assertEquals(BOOK_TITLES.subList(1, 3), books2.stream().map(FSearchBooksRecord::getTitle).collect(toList()));

        // [#3378] PostgreSQL has issues with fully qualified references to
        // columns of table-valued functions
        FTables1 t1 = F_TABLES1.call().as("t1");
        FTables2 t2 = F_TABLES2.call();
        FTables3 t3 = F_TABLES3();
        FTables4 t4a = F_TABLES4(val(null, Integer.class)).as("t4");
        FTables4 t4b = F_TABLES4(T_BOOK.ID).as("t4");

        // Simple call with SELECT clause
        Result<Record1<Integer>> result1 =
        create().select(t1.COLUMN_VALUE)
                .from(t1)
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(1, result1.get(0).size());
        assertEquals(1, (int) result1.getValue(0, t1.COLUMN_VALUE));

        // Typesafe call to get a TableRecord
        FTables2Record result2 =
        create().selectFrom(t2)
                .fetchOne();

        assertEquals(1L, (long) result2.getColumnValue());

        FTables3Record result3 =
        create().selectFrom(t3)
                .fetchOne();

        assertEquals("1", result3.getColumnValue());

        // In parameters
        Result<FTables4Record> result4a =
        create().selectFrom(t4a)
                .fetch();

        assertEquals(BOOK_IDS, result4a.getValues(t4a.ID));
        assertEquals(BOOK_TITLES, result4a.getValues(t4a.TITLE));

        // Lateral JOIN
        Result<Record2<Integer, String>> result4b =
        create().select(t4b.ID, t4b.TITLE)
                .from(T_BOOK, lateral(t4b))
                .where(t4b.TITLE.like("%a%"))
                .orderBy(t4b.ID)
                .fetch();

        assertEquals(BOOK_IDS.subList(1, 4), result4b.getValues(t4b.ID));
        assertEquals(BOOK_TITLES.subList(1, 4), result4b.getValues(t4b.TITLE));
    }

    @Test
    public void testPostgresSetofIntFunction() throws Exception {
        Result<FSearchBookIdsRecord> r1 =
        create().selectFrom(Routines.fSearchBookIds("", 3L, 0L))
                .fetch();

        assertEquals(BOOK_IDS.subList(0, 3), r1.getValues(F_SEARCH_BOOK_IDS.F_SEARCH_BOOK_IDS_, int.class));

        Result<FSearchBookTitlesRecord> r2 =
        create().selectFrom(Routines.fSearchBookTitles("", 3L, 0L))
                .fetch();

        assertEquals(BOOK_TITLES.subList(0, 3), r2.getValues(F_SEARCH_BOOK_TITLES.F_SEARCH_BOOK_TITLES_, String.class));
    }

//    @Test
//    public void testPostgresExtensions() throws Exception {
//        jOOQAbstractTest.reset = false;
//
//        // [#2267] Try to execute some basic CRUD operations on PostGIS data
//        // types. Even without formal support, jOOQ should allow to pass these
//        // objects through to JDBC
//        Point point = new Point(0, 0);
//        point.setSrid(4326);
//        PGgeometry geometry = new PGgeometry(point);
//
//        // [#2267] Non-PostGIS geometry objects should work, too
//        PGbox box = new PGbox(1.0, 1.0, 2.0, 2.0);
//        PGInterval interval = new PGInterval(1, 2, 3, 4, 5, 6);
//
//        TPgExtensionsRecord r1 = create().newRecord(T_PG_EXTENSIONS);
//        r1.setPgGeometry(geometry);
//        r1.setPgInterval(interval);
//        r1.setPgBox(box);
//        assertEquals(1, r1.store());
//        assertEquals(1, (int) create().selectCount().from(T_PG_EXTENSIONS).fetchOne(0, int.class));
//
//        TPgExtensionsRecord r2 = create().selectFrom(T_PG_EXTENSIONS).fetchOne();
//        assertEquals(r1, r2);
//
//        assertEquals(box, r2.getPgBox());
//        assertEquals(geometry, r2.getPgGeometry());
//        assertEquals(interval, r2.getPgInterval());
//    }

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
    public void testPostgresUUIDArrays() {
        clean(T_EXOTIC_TYPES);

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();


        // INSERT statement
        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES)
                .set(T_EXOTIC_TYPES.ID, 10)
                .set(T_EXOTIC_TYPES.UU, uuid1)
                .set(T_EXOTIC_TYPES.UU_ARRAY, new UUID[] { uuid1, uuid2 } )
                .execute());

        TExoticTypesRecord r1 = create().fetchOne(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID.eq(10));

        assertEquals(uuid1, r1.getUu());
        assertEquals(asList(uuid1, uuid2), asList(r1.getUuArray()));


        // executeInsert() call
        TExoticTypesRecord r2a = new TExoticTypesRecord();
        r2a.setId(11);
        r2a.setUu(uuid1);
        r2a.setUuArray(new UUID[] { uuid1, uuid2 });
        assertEquals(1, create().executeInsert(r2a));

        TExoticTypesRecord r2b = create().fetchOne(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID.eq(11));

        assertEquals(uuid1, r2b.getUu());
        assertEquals(asList(uuid1, uuid2), asList(r2b.getUuArray()));


        // Store call
        TExoticTypesRecord r3a = create().newRecord(T_EXOTIC_TYPES);
        r3a.setId(12);
        r3a.setUu(uuid1);
        r3a.setUuArray(new UUID[] { uuid1, uuid2 });
        assertEquals(1, r3a.store());

        TExoticTypesRecord r3b = create().fetchOne(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID.eq(12));

        assertEquals(uuid1, r3b.getUu());
        assertEquals(asList(uuid1, uuid2), asList(r3b.getUuArray()));
    }

    @Test
    public void testPostgresUUIDArraysWithBindings() {
        clean(T_EXOTIC_TYPES);

        UUIDWrapper uuid1 = new UUIDWrapper(UUID.randomUUID());
        UUIDWrapper uuid2 = new UUIDWrapper(UUID.randomUUID());


        // INSERT statement
        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES)
                .set(T_EXOTIC_TYPES.ID, 10)
                .set(T_EXOTIC_TYPES.UU_WRAPPER, uuid1)
                .set(T_EXOTIC_TYPES.UU_WRAPPER_ARRAY, new UUIDWrapper[] { uuid1, uuid2 } )
                .execute());

        TExoticTypesRecord r1 = create().fetchOne(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID.eq(10));

        assertEquals(uuid1, r1.getUu());
        assertEquals(asList(uuid1, uuid2), asList(r1.getUuArray()));


        // executeInsert() call
        TExoticTypesRecord r2a = new TExoticTypesRecord();
        r2a.setId(11);
        r2a.setUuWrapper(uuid1);
        r2a.setUuWrapperArray(new UUIDWrapper[] { uuid1, uuid2 });
        assertEquals(1, create().executeInsert(r2a));

        TExoticTypesRecord r2b = create().fetchOne(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID.eq(11));

        assertEquals(uuid1, r2b.getUuWrapper());
        assertEquals(asList(uuid1, uuid2), asList(r2b.getUuWrapperArray()));


        // Store call
        TExoticTypesRecord r3a = create().newRecord(T_EXOTIC_TYPES);
        r3a.setId(12);
        r3a.setUuWrapper(uuid1);
        r3a.setUuWrapperArray(new UUIDWrapper[] { uuid1, uuid2 });
        assertEquals(1, r3a.store());

        TExoticTypesRecord r3b = create().fetchOne(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID.eq(12));

        assertEquals(uuid1, r3b.getUuWrapper());
        assertEquals(asList(uuid1, uuid2), asList(r3b.getUuWrapperArray()));
    }


    @Test
    public void testPostgresEnumType() throws Exception {
        // [#2549] TODO: Re-enable this
        // Param<UCountry> val = val(UCountry.England);
        Param<UCountry> val = val(UCountry.England, UAddressType.COUNTRY.getDataType());
        Param<UCountry[]> array = val(new UCountry[] { UCountry.England }, UAddressType.COUNTRY.getDataType().getArrayDataType());

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

        // [#3778] Be sure that all settings are applied to explicit casts to
        // PostgreSQL enum array types
        assertEquals("'{\"England\"}'", create().renderInlined(array));
        assertEquals("?::\"public\".\"u_country\"[]",
            create().render(array));
        assertEquals("?::\"u_country\"[]",
            create(new Settings().withRenderSchema(false)).render(array));
        assertEquals("?::PUBLIC.U_COUNTRY[]",
            create(new Settings().withRenderNameStyle(RenderNameStyle.UPPER)).render(array));
        assertEquals("?::\"u_country\"[]",
            create(new Settings().withRenderMapping(
                new RenderMapping().withDefaultSchema("public"))).render(array));
        assertEquals("?::\"test\".\"u_country\"[]",
            create(new Settings().withRenderMapping(
                   new RenderMapping().withSchemata(
                   new MappedSchema().withInput("public")
                                     .withOutput("test")))).render(array));

    }

    @Test
    public void testPostgresOnlyClause() throws Exception {
        assertEquals(3, create().fetchCount(selectOne().from(T_INHERITANCE_1)));
        assertEquals(2, create().fetchCount(selectOne().from(only(T_INHERITANCE_1))));
    }

    @Test
    public void testPostgresJsonDataType() throws Exception {
        jOOQAbstractTest.reset = false;

        assertEquals(4,
        create().insertInto(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.JS_GSON, T_EXOTIC_TYPES.JS_JACKSON, T_EXOTIC_TYPES.JS_JACKSON_JSON_NODE)
                .values(1, null, null, null)
                .values(2,
                    new JsonObject(),
                    new JSONJacksonHelloWorld(),
                    new ObjectNode(JsonNodeFactory.instance))
                .values(3,
                    new JsonArray(),
                    new JSONJacksonHelloWorld("hello", "world"),
                    new ArrayNode(JsonNodeFactory.instance))
                .values(4,
                    new Gson().fromJson("{\"hello\":\"world\"}", JsonObject.class),
                    new JSONJacksonHelloWorld("hello", "world", "a", "b", "c"),
                    new ObjectMapper().readTree("{\"hello\":\"world\"}"))
                .execute());

        Result<TExoticTypesRecord> r1 =
        create().selectFrom(T_EXOTIC_TYPES)
                .orderBy(TExoticTypes_ID())
                .fetch();

        assertEquals(4, r1.size());
        assertEquals(JsonNull.INSTANCE, r1.get(0).getJsGson());
        assertNull(r1.get(0).getJsJackson());
        assertEquals(NullNode.instance, r1.get(0).getJsJacksonJsonNode());

        assertEquals(new JsonObject(), r1.get(1).getJsGson());
        assertNull(r1.get(1).getJsJackson().hello);
        assertNull(r1.get(1).getJsJackson().world);
        assertNull(r1.get(1).getJsJackson().greetings);
        assertEquals(new ObjectNode(JsonNodeFactory.instance), r1.get(1).getJsJacksonJsonNode());

        assertEquals(new JsonArray(), r1.get(2).getJsGson());
        assertEquals("hello", r1.get(2).getJsJackson().hello);
        assertEquals("world", r1.get(2).getJsJackson().world);
        assertEquals(0, r1.get(2).getJsJackson().greetings.size());
        assertEquals(new ArrayNode(JsonNodeFactory.instance), r1.get(2).getJsJacksonJsonNode());

        JsonObject js1gson = (JsonObject) r1.get(3).getJsGson();
        assertEquals(1, js1gson.entrySet().size());
        assertEquals("world", js1gson.get("hello").getAsString());
        assertEquals("hello", r1.get(3).getJsJackson().hello);
        assertEquals("world", r1.get(3).getJsJackson().world);
        assertEquals(Arrays.asList("a", "b", "c"), r1.get(3).getJsJackson().greetings);
        ObjectNode js1jackson = (ObjectNode) r1.get(3).getJsJacksonJsonNode();
        assertEquals(1, js1jackson.size());
        assertEquals("world", js1jackson.get("hello").getTextValue());
    }

    @SuppressWarnings("serial")
    @Test
    public void testPostgresHstoreDataType() throws Exception {
        jOOQAbstractTest.reset = false;

        assertEquals(4,
        create().insertInto(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.HSTORE_MAP)
                .values(1, null)
                .values(2, Collections.emptyMap())
                .values(3, new LinkedHashMap<String, String>() {{ put("a", "1"); }})
                .values(4, new LinkedHashMap<String, String>() {{ put("a", "1"); put("b", "2"); }})
                .execute());

        Result<TExoticTypesRecord> r1 =
        create().selectFrom(T_EXOTIC_TYPES)
                .orderBy(TExoticTypes_ID())
                .fetch();

        assertNull(r1.get(0).getHstoreMap());
        assertEquals(0, r1.get(1).getHstoreMap().size());
        assertEquals(1, r1.get(2).getHstoreMap().size());
        assertEquals("1", r1.get(2).getHstoreMap().get("a"));
        assertEquals(2, r1.get(3).getHstoreMap().size());
        assertEquals("1", r1.get(3).getHstoreMap().get("a"));
        assertEquals("2", r1.get(3).getHstoreMap().get("b"));
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
            create().fetchCount(
                 select()
                .from(book1)
                .union(
                 select()
                .from(book2))
            ));

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

    @Test
    public void testPostgresUDTArrays() {
        jOOQAbstractTest.reset = false;

        create().delete(T_ARRAYS).execute();
        TArraysRecord record;

        record = create().newRecord(T_ARRAYS);
        record.setId(1);
        record.setUdtArray(null);
        record.setAddressArray(null);
        assertEquals(1, record.insert());

        record = create().newRecord(T_ARRAYS);
        record.setId(2);
        record.setUdtArray(new UStreetTypeRecord[0]);
        record.setAddressArray(new UAddressTypeRecord[0]);
        assertEquals(1, record.insert());

        record = create().newRecord(T_ARRAYS);
        record.setId(3);
        record.setUdtArray(new UStreetTypeRecord[2]);
        record.setAddressArray(new UAddressTypeRecord[2]);
        assertEquals(1, record.insert());

        record = create().newRecord(T_ARRAYS);
        record.setId(4);
        record.setUdtArray(new UStreetTypeRecord[] {
            new UStreetTypeRecord(),
            new UStreetTypeRecord("X", "Y", new Integer[] { 1, 2 }, "xyz".getBytes()),
        });
        record.setAddressArray(new UAddressTypeRecord[] {
            new UAddressTypeRecord(),
            new UAddressTypeRecord(new UStreetTypeRecord("A", "B", new Integer[] { 1, 2 }, "abc".getBytes()), "zip", "city", UCountry.England, Date.valueOf("1999-12-31"), 1, "xyz".getBytes())
        });
        assertEquals(1, record.insert());

        Result<TArraysRecord> result =
        create().selectFrom(T_ARRAYS)
                .orderBy(T_ARRAYS.ID)
                .fetch();

        assertEquals(asList(1, 2, 3, 4), result.getValues(T_ARRAYS.ID));
        assertNull(result.get(0).getUdtArray());
        assertNull(result.get(0).getAddressArray());

        assertEquals(0, result.get(1).getUdtArray().length);
        assertEquals(0, result.get(1).getAddressArray().length);

        assertEquals(2, result.get(2).getUdtArray().length);
        assertNull(result.get(2).getUdtArray()[0]);
        assertNull(result.get(2).getUdtArray()[1]);
        assertEquals(2, result.get(2).getAddressArray().length);
        assertNull(result.get(2).getAddressArray()[0]);
        assertNull(result.get(2).getAddressArray()[1]);

        assertEquals(2, result.get(3).getUdtArray().length);
        assertEquals(new UStreetTypeRecord(), result.get(3).getUdtArray()[0]);
        assertEquals(new UStreetTypeRecord("X", "Y", new Integer[] { 1, 2 }, "xyz".getBytes()), result.get(3).getUdtArray()[1]);
    }

    @Test
    public void testPostgresMultipleConvertersForJavaLangInteger() {
        try {
            T_3111Record record;

            record = create().newRecord(T_3111);
            record.setId(1);
            assertEquals(1, record.store());

            record = create().newRecord(T_3111);
            record.setId(2);
            record.setInverse(0);
            record.setBool1(0);
            record.setBool2(0);
            assertEquals(1, record.store());

            record = create().newRecord(T_3111);
            record.setId(3);
            record.setInverse(1);
            record.setBool1(1);
            record.setBool2(-1);
            assertEquals(1, record.store());

            Result<Record3<Integer, Integer, Integer>> r1 =
            create().select(T_3111.INVERSE, T_3111.BOOL1, T_3111.BOOL2)
                    .from(T_3111)
                    .orderBy(T_3111.ID)
                    .fetch();

            assertEquals(3, r1.size());
            assertEquals(asList(null, 0, 1), r1.getValues(T_3111.INVERSE));
            assertEquals(asList(null, 0, 1), r1.getValues(T_3111.BOOL1));
            assertEquals(asList(null, 0, -1), r1.getValues(T_3111.BOOL2));

            // Check if actual data in database are the correct boolean values:
            Result<?> r2 =
            create().select(field("inverse"), field("bool1"), field("bool2"))
                    .from("t_3111")
                    .orderBy(field("id"))
                    .fetch();

            assertEquals(3, r2.size());
            assertEquals(asList(null, 0, -1), r2.getValues(0));
            assertEquals(asList(null, false, true), r2.getValues(1));
            assertEquals(asList(null, false, true), r2.getValues(2));
        }
        finally {
            create().delete(T_3111).execute();
        }
    }

    @Test
    public void testPostgresMetaReferencesWithSameNameForeignKey() {

        // [#986] [#3520] Be sure that shared foreign key names do not produce any irregular behaviour
        List<ForeignKey<T_986_1Record, XUnusedRecord>> r1 = T_986_1.getReferencesTo(X_UNUSED);
        List<ForeignKey<T_986_2Record, XUnusedRecord>> r2 = T_986_2.getReferencesTo(X_UNUSED);

        assertEquals(1, r1.size());
        assertEquals(1, r2.size());

        assertEquals(T_986_1, r1.get(0).getTable());
        assertEquals(T_986_2, r2.get(0).getTable());

        assertEquals(asList(T_986_1.REF), r1.get(0).getFields());
        assertEquals(asList(T_986_2.REF), r2.get(0).getFields());

        assertEquals(0, (int) create().selectCount().from(T_986_1.join(X_UNUSED).onKey()).fetchOne(0, int.class));
        assertEquals(0, (int) create().selectCount().from(T_986_2.join(X_UNUSED).onKey()).fetchOne(0, int.class));
    }

    @Test
    public void testPostgresCountTable() {

        // [#3512] PostgreSQL also supports counting values that originate from one of the table expressions.
        org.jooq.test.postgres.generatedclasses.tables.TAuthor a = T_AUTHOR.as("a");
        Record4<Integer, Integer, Integer, Integer> record =
        create().select(
                    count(T_BOOK), countDistinct(T_BOOK),
                    count(a), countDistinct(a))
                .from(T_BOOK)
                .join(a).on(a.ID.eq(T_BOOK.AUTHOR_ID))
                .fetchOne();

        assertEquals(4, (int) record.value1());
        assertEquals(4, (int) record.value2());
        assertEquals(4, (int) record.value3());
        assertEquals(2, (int) record.value4());
    }

    @Test
    public void testPostgresDistinctOnTest() {
        Result<Record2<Integer, Integer>> r1 =
        create().selectDistinct(T_BOOK.ID, T_BOOK.AUTHOR_ID)
                .on(T_BOOK.AUTHOR_ID)
                .from(T_BOOK)
                .orderBy(T_BOOK.AUTHOR_ID)
                .fetch();

        Result<Record2<Integer, Integer>> r2 =
        create().select(T_BOOK.ID, T_BOOK.AUTHOR_ID)
                .distinctOn(T_BOOK.AUTHOR_ID)
                .from(T_BOOK)
                .orderBy(T_BOOK.AUTHOR_ID)
                .fetch();

        assertEquals(r1, r2);
        assertEquals(asList(1, 3), r1.getValues(T_BOOK.ID));
        assertEquals(asList(1, 2), r1.getValues(T_BOOK.AUTHOR_ID));
    }

    @Test
    public void testPostgresOrderedAggregateFunctions() throws Exception {
        Record8<BigDecimal, BigDecimal, Integer, Integer, Integer, Integer, Integer, Integer> result =
        create().select(
                    cumeDist(val(1)).withinGroupOrderBy(T_BOOK.ID.desc()),
                    cumeDist(val(1), val(1)).withinGroupOrderBy(T_BOOK.ID.desc(), T_BOOK.AUTHOR_ID.desc()),

                    rank(val(1)).withinGroupOrderBy(T_BOOK.ID.asc()),
                    rank(val(1), val(1)).withinGroupOrderBy(T_BOOK.ID.asc(), T_BOOK.AUTHOR_ID.desc()),

                    denseRank(val(1)).withinGroupOrderBy(T_BOOK.ID.asc()),
                    denseRank(val(1), val(1)).withinGroupOrderBy(T_BOOK.ID.asc(), T_BOOK.AUTHOR_ID.desc()),

                    percentRank(val(1)).withinGroupOrderBy(T_BOOK.ID.asc()),
                    percentRank(val(1), val(1)).withinGroupOrderBy(T_BOOK.ID.asc(), T_BOOK.AUTHOR_ID.desc()))
                .from(T_BOOK)
                .fetchOne();

        assertEquals(BigDecimal.ONE, result.value1());
        assertEquals(BigDecimal.ONE, result.value2());

        assertEquals(1, (int) result.value3());
        assertEquals(1, (int) result.value4());

        assertEquals(1, (int) result.value5());
        assertEquals(1, (int) result.value6());

        assertEquals(0, (int) result.value7());
        assertEquals(0, (int) result.value8());
    }

    @Test
    public void testPostgresArraysInTableValuedFunctions() throws Exception {
        Result<FArrayTablesRecord> result =
        create().selectFrom(F_ARRAY_TABLES(new String[] { "abc" }, new Integer[] { 123 }))
                .fetch();

        assertEquals(4, result.size());
        assertNull(result.get(0).getOutText());
        assertNull(result.get(0).getOutInteger());
        assertEquals(0, result.get(1).getOutText().length);
        assertEquals(0, result.get(1).getOutInteger().length);
        assertEquals(asList("a"), asList(result.get(2).getOutText()));
        assertEquals(asList(1), asList(result.get(2).getOutInteger()));
        assertEquals(asList("a", "b"), asList(result.get(3).getOutText()));
        assertEquals(asList(1, 2), asList(result.get(3).getOutInteger()));
    }

    @Test
    public void testPostgresArraysInTableValuedFunctionResults() throws Exception {

        // [#4065] Plain SQL
        // UDT and Enum type arrays cannot be recognised via JDBC's ResultSetMetaData...
        Record record = create().fetchOne("select id, string_array, number_array, date_array from f_get_arrays(?)", 4);
        TArraysRecord into = record.into(TArraysRecord.class);

        assertEquals(4, (int) record.getValue(TArrays_ID()));
        assertEquals(4, (int) into.getId());

        assertEquals(asList("a", "b"), asList(record.getValue(TArrays_STRING().getName(), String[].class)));
        assertEquals(asList("a", "b"), asList(into.getStringArray()));

        assertEquals(asList(1, 2), asList(record.getValue(TArrays_NUMBER().getName(), Integer[].class)));
        assertEquals(asList(1, 2), asList(into.getNumberArray()));

        assertEquals(
            asList(Date.valueOf("1981-07-10"), Date.valueOf("2000-01-01")),
            asList(record.getValue(TArrays_DATE().getName(), Date[].class)));
        assertEquals(
            asList(Date.valueOf("1981-07-10"), Date.valueOf("2000-01-01")),
            asList(into.getDateArray()));

    }

    @Test
    public void testPostgresRangeTypes() throws Exception {
        clean(T_EXOTIC_TYPES);

        assertEquals(1,
            create().insertInto(T_EXOTIC_TYPES)
                    .columns(T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.RANGE_INT4)
                    .values(1, null)
                    .execute()
        );

        assertEquals(1,
            create().insertInto(T_EXOTIC_TYPES)
                    .columns(T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.RANGE_INT4)
                    .values(2, range(1, 5))
                    .execute()
        );

        assertEquals(1,
            create().insertInto(T_EXOTIC_TYPES)
                    .columns(T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.RANGE_INT4)
                    .values(3, range(3, 7))
                    .execute()
        );

        Result<TExoticTypesRecord> result1 =
        create().selectFrom(T_EXOTIC_TYPES)
                .orderBy(T_EXOTIC_TYPES.ID)
                .fetch();

        assertEquals(3, result1.size());
        assertEquals(asList(1, 2, 3), result1.getValues(T_EXOTIC_TYPES.ID));
        assertEquals(asList(null, range(1, 5), range(3, 7)), result1.getValues(T_EXOTIC_TYPES.RANGE_INT4));
        assertEquals(range(1, 5), create().fetchValue(
            select(T_EXOTIC_TYPES.RANGE_INT4)
            .from(T_EXOTIC_TYPES)
            .where(rangeOverlaps(T_EXOTIC_TYPES.RANGE_INT4, range(0, 2))))
        );
    }

    private static <T extends Comparable<T>> Condition rangeOverlaps(Field<Range<T>> f1, Range<T> f2) {
        return DSL.condition("range_overlaps({0}, {1})", f1, val(f2, f1.getDataType()));
    }

    @Test
    public void testPostgresRowsFrom() {
        Result<Record> r1 =
        create().select()
                .from(rowsFrom(F_TABLES1(), F_TABLES5(1, 2, 3)))
                .orderBy(field(name("s")))
                .fetch();

        assertEquals(asList(1, 2, 3), r1.getValues(F_TABLES5.V));
        assertEquals(asList(1, 3, 6), r1.getValues(F_TABLES5.S));
        assertEquals(asList(1, null, null), r1.getValues(F_TABLES1.COLUMN_VALUE));

        Result<Record> r2 =
        create().select()
                .from(rowsFrom(F_TABLES1(), F_TABLES5(1, 2, 3)).as("a"))
                .orderBy(field(name("a", "s")))
                .fetch();

        assertEquals(asList(1, 2, 3), r2.getValues(F_TABLES5.V));
        assertEquals(asList(1, 3, 6), r2.getValues(F_TABLES5.S));
        assertEquals(asList(1, null, null), r2.getValues(F_TABLES1.COLUMN_VALUE));
    }

//    @Test
//    public void testPostgresCrossJoinLateralUnnest() {
//        Table<?> unnest = unnest(T_ARRAYS.STRING_ARRAY);
//
//        Result<Record2<Integer, String>> result =
//        create().select(T_ARRAYS.ID, field("unnest", String.class))
//                .from(T_ARRAYS)
//                .crossJoin(lateral(unnest))
//                .orderBy(1, 2)
//                .fetch();
//
//        assertEquals(asList(3, 4, 4), result.getValues(T_ARRAYS.STRING_ARRAY));
//        assertEquals(asList("a", "a", "B"), result.getValues("unnest"));
//     }

//    @Test
//    public void testXML() throws SQLException {
//        Statement s = connection.createStatement();
//        s.executeUpdate("insert into t_exotic_types(id, pg_xml_as_is) values(1, '<a><b/></a>'::xml)");
//
//        ResultSet rs = s.executeQuery("select PG_XML_AS_IS from t_exotic_types");
//        rs.next();
//        System.out.println(rs.getSQLXML(1).getString());
//    }

    @Test
    public void testPostgresGISGeographyTypeBinding() {
        clean(T_PG_EXTENSIONS);

        assertEquals(1,
        create().insertInto(T_PG_EXTENSIONS)
                .columns(T_PG_EXTENSIONS.ID, T_PG_EXTENSIONS.PG_POSITION)
                .values(1, new Position(BigDecimal.ZERO, BigDecimal.ONE))
                .execute());

        Position position = create().fetchOne(T_PG_EXTENSIONS).getPgPosition();
        assertEquals(0, BigDecimal.ZERO.compareTo(position.latitude));
        assertEquals(0, BigDecimal.ONE.compareTo(position.longitude));
    }
}
