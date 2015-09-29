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
import static org.jooq.Clause.CUSTOM;
import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_WHERE;
import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TEMPLATE;
import static org.jooq.Clause.UPDATE;
import static org.jooq.Clause.UPDATE_UPDATE;
import static org.jooq.Clause.UPDATE_WHERE;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.update;
import static org.jooq.lambda.Unchecked.runnable;
import static org.jooq.test.h2.generatedclasses.public_.Tables.ACCOUNTS;
import static org.jooq.test.h2.generatedclasses.public_.Tables.TRANSACTIONS;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_2486;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_2698;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_3485;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_3571;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_3666;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_639_NUMBERS_TABLE;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_725_LOB_TEST;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_785;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_ARRAYS;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_AUTHOR;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_BOOK;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_BOOK_STORE;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_BOOLEANS;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_DATES;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_IDENTITY;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_IDENTITY_PK;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_TRIGGERS;
import static org.jooq.test.h2.generatedclasses.public_.Tables.T_UNSIGNED;
import static org.jooq.test.h2.generatedclasses.public_.Tables.V_2603;
import static org.jooq.test.h2.generatedclasses.public_.Tables.V_AUTHOR;
import static org.jooq.test.h2.generatedclasses.public_.Tables.V_BOOK;
import static org.jooq.test.h2.generatedclasses.public_.Tables.V_LIBRARY;
import static org.jooq.test.h2.generatedclasses.public_.Tables.X_UNUSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

import org.jooq.ArrayRecord;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.VisitContext;
import org.jooq.impl.CustomQueryPart;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.impl.DefaultVisitListenerProvider;
import org.jooq.lambda.fi.lang.CheckedRunnable;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.h2.generatedclasses.public_.Keys;
import org.jooq.test.h2.generatedclasses.public_.Routines;
import org.jooq.test.h2.generatedclasses.public_.Sequences;
import org.jooq.test.h2.generatedclasses.public_.Tables;
import org.jooq.test.h2.generatedclasses.public_.tables.Accounts;
import org.jooq.test.h2.generatedclasses.public_.tables.TArrays;
import org.jooq.test.h2.generatedclasses.public_.tables.TAuthor;
import org.jooq.test.h2.generatedclasses.public_.tables.TBook;
import org.jooq.test.h2.generatedclasses.public_.tables.TTriggers;
import org.jooq.test.h2.generatedclasses.public_.tables.T_639NumbersTable;
import org.jooq.test.h2.generatedclasses.public_.tables.T_725LobTest;
import org.jooq.test.h2.generatedclasses.public_.tables.Transactions;
import org.jooq.test.h2.generatedclasses.public_.tables.VLibrary;
import org.jooq.test.h2.generatedclasses.public_.tables.daos.TAuthorDao;
import org.jooq.test.h2.generatedclasses.public_.tables.daos.T_2698Dao;
import org.jooq.test.h2.generatedclasses.public_.tables.daos.XUnusedDao;
import org.jooq.test.h2.generatedclasses.public_.tables.pojos.T_2698;
import org.jooq.test.h2.generatedclasses.public_.tables.pojos.XUnused;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TArraysRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TAuthorRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TBookRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TBookStoreRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TBookToBookStoreRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TBooleansRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TDatesRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TExoticTypesRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TIdentityPkRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TIdentityRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TTriggersRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.TUnsignedRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_2486Record;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_2698Record;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_3485Record;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_3666Record;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_639NumbersTableRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_725LobTestRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.T_785Record;
import org.jooq.test.h2.generatedclasses.public_.tables.records.VLibraryRecord;
import org.jooq.test.h2.generatedclasses.public_.tables.records.V_2603Record;
import org.jooq.test.h2.generatedclasses.public_.tables.records.XUnusedRecord;
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
        org.jooq.test.h2.generatedclasses.public_.tables.pojos.TAuthor,
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
        return SQLDialect.H2;
    }

    @Override
    protected DAO<TAuthorRecord, org.jooq.test.h2.generatedclasses.public_.tables.pojos.TAuthor, Integer> TAuthorDao() {
        return new TAuthorDao(create().configuration());
    }

    @Override
    protected Table<TAuthorRecord> TAuthor() {
        return TAuthor.T_AUTHOR;
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
        return TBook.T_BOOK;
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
    protected TableField<TBookRecord, Integer> TBook_REC_VERSION() {
        return T_BOOK.REC_VERSION;
    }

    @Override
    protected TableField<TBookRecord, Timestamp> TBook_REC_TIMESTAMP() {
        return T_BOOK.REC_TIMESTAMP;
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
    protected Table<T_639NumbersTableRecord> T639() {
        return T_639NumbersTable.T_639_NUMBERS_TABLE;
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
    protected Table<T_725LobTestRecord> T725() {
        return T_725LobTest.T_725_LOB_TEST;
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
    protected Table<TArraysRecord> TArrays() {
        return TArrays.T_ARRAYS;
    }

    @Override
    protected TableField<TArraysRecord, Integer> TArrays_ID() {
        return T_ARRAYS.ID;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected TableField<TArraysRecord, String[]> TArrays_STRING() {
        return (TableField) T_ARRAYS.STRING_ARRAY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected TableField<TArraysRecord, Integer[]> TArrays_NUMBER() {
        return (TableField) T_ARRAYS.NUMBER_ARRAY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected TableField<TArraysRecord, Date[]> TArrays_DATE() {
        return (TableField) T_ARRAYS.DATE_ARRAY;
    }

    @Override
    protected TableField<TArraysRecord, ? extends UDTRecord<?>[]> TArrays_UDT() {
        return null;
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
        return VLibrary.V_LIBRARY;
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
        return TTriggers.T_TRIGGERS;
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
                select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count().cast(Long.class).as("Books Written"))
                .from(T_AUTHOR)
                .join(T_BOOK)
                .on(T_AUTHOR.ID.eq(T_BOOK.AUTHOR_ID))
                .groupBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
                .orderBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
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

    @Test
    public void testH2T2698InsertPojoThroughDaoWithDefault() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#2700] Check if DEFAULT NOT NULL columns are used sensibly for
        // INSERT and UPDATE statements through DAOs
        T_2698Dao dao = new T_2698Dao(create().configuration());
        List<T_2698> list;

        dao.insert(new T_2698(1, null, null));
        list = dao.fetchById(1);
        assertEquals(1, list.size());
        assertEquals(1, (int) list.get(0).getId());
        assertEquals(-1, (int) list.get(0).getXx());
        assertEquals(-2, (int) list.get(0).getYy());

        dao.update(new T_2698(1, 42, 42));
        list = dao.fetchById(1);
        assertEquals(1, list.size());
        assertEquals(1, (int) list.get(0).getId());
        assertEquals(42, (int) list.get(0).getXx());
        assertEquals(42, (int) list.get(0).getYy());

        assertEquals(1,
        create().update(T_2698)
                .set(T_2698.XX, defaultValue(Integer.class))
                .set(T_2698.YY, defaultValue(Integer.class))
                .execute());

        dao.update(new T_2698(1, null, 42));
        list = dao.fetchById(1);
        assertEquals(1, list.size());
        assertEquals(1, (int) list.get(0).getId());
        assertEquals(-1, (int) list.get(0).getXx());
        assertEquals(42, (int) list.get(0).getYy());

        assertEquals(1,
        create().update(T_2698)
                .set(T_2698.XX, defaultValue(Integer.class))
                .set(T_2698.YY, defaultValue(Integer.class))
                .execute());

        dao.update(new T_2698(1, 42, null));
        list = dao.fetchById(1);
        assertEquals(1, list.size());
        assertEquals(1, (int) list.get(0).getId());
        assertEquals(42, (int) list.get(0).getXx());
        assertEquals(-2, (int) list.get(0).getYy());
    }

    @Test
    public void testH2DaoWithCompositeKey() throws Exception {
        jOOQAbstractTest.reset = false;

        Record2<Integer, String> key1 = create().newRecord(
            X_UNUSED.ID,
            X_UNUSED.NAME);

        Record2<Integer, String> key2 = create().newRecord(
            X_UNUSED.ID,
            X_UNUSED.NAME);
        key2.setValue(X_UNUSED.ID, 1);
        key2.setValue(X_UNUSED.NAME, "name");

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

    @Test
    @SuppressWarnings("unused")
    public void testH2ForcedTypes2486() {

        // This should compile
        TableField<T_2486Record, BigDecimal> val1 = T_2486.VAL1;
        TableField<T_2486Record, BigDecimal> val2 = T_2486.VAL2;
        TableField<T_2486Record, BigDecimal> val3 = T_2486.VAL3;
        TableField<T_2486Record, BigDecimal> val4 = T_2486.VAL4;
        TableField<T_2486Record, BigDecimal> val5 = T_2486.VAL5;
        TableField<T_2486Record, BigDecimal> val6 = T_2486.VAL6;
        TableField<T_2486Record, BigInteger> val7 = T_2486.VAL7;
        TableField<T_2486Record, BigInteger> val8 = T_2486.VAL8;

        assertEquals(BigDecimal.class, T_2486.VAL1.getType());
        assertEquals(BigDecimal.class, T_2486.VAL2.getType());
        assertEquals(BigDecimal.class, T_2486.VAL3.getType());
        assertEquals(BigDecimal.class, T_2486.VAL4.getType());
        assertEquals(BigDecimal.class, T_2486.VAL5.getType());
        assertEquals(BigDecimal.class, T_2486.VAL6.getType());
        assertEquals(BigInteger.class, T_2486.VAL7.getType());
        assertEquals(BigInteger.class, T_2486.VAL8.getType());

        assertEquals(0, T_2486.VAL1.getDataType().precision());
        assertEquals(21, T_2486.VAL2.getDataType().precision());
        assertEquals(0, T_2486.VAL3.getDataType().precision());
        assertEquals(21, T_2486.VAL4.getDataType().precision());
        assertEquals(0, T_2486.VAL5.getDataType().precision());
        assertEquals(21, T_2486.VAL6.getDataType().precision());
        assertEquals(20, T_2486.VAL7.getDataType().precision());
        assertEquals(20, T_2486.VAL8.getDataType().precision());

        assertEquals(0, T_2486.VAL1.getDataType().scale());
        assertEquals(4, T_2486.VAL2.getDataType().scale());
        assertEquals(0, T_2486.VAL3.getDataType().scale());
        assertEquals(4, T_2486.VAL4.getDataType().scale());
        assertEquals(0, T_2486.VAL5.getDataType().scale());
        assertEquals(4, T_2486.VAL6.getDataType().scale());
        assertEquals(0, T_2486.VAL7.getDataType().scale());
        assertEquals(0, T_2486.VAL8.getDataType().scale());
    }

    @Test
    public void testH2MetaData3571() {
        assertTrue(T_3571.E1.getDataType().nullable());
        assertFalse(T_3571.E1.getDataType().defaulted());
        assertFalse(T_3571.E2.getDataType().nullable());
        assertFalse(T_3571.E2.getDataType().defaulted());
        assertTrue(T_3571.E3.getDataType().nullable());
        assertTrue(T_3571.E3.getDataType().defaulted());
        assertFalse(T_3571.E4.getDataType().nullable());
        assertTrue(T_3571.E4.getDataType().defaulted());
    }

    @Test
    public void testH2MetaData3666() {
        TableField<T_3666Record, Byte> e5 = T_3666.E5;
        TableField<T_3666Record, Short> e3 = T_3666.E3;
        TableField<T_3666Record, Short> e4 = T_3666.E4;
        TableField<T_3666Record, Long> e1 = T_3666.E1;
        TableField<T_3666Record, BigDecimal> e2 = T_3666.E2;

        assertEquals(20, T_3666.E2.getDataType().precision());
        assertEquals(5, T_3666.E2.getDataType().scale());
    }

    @Test
    public void testH2PasswordHash3485() {
        clean(T_3485);

        T_3485Record record = create().newRecord(T_3485);
        record.setId(1);
        record.setPw("pw");
        assertEquals(1, create().executeInsert(record));

        record = create().fetchOne(T_3485, T_3485.PW.eq("pw"));
        assertEquals("", record.getPw());
        assertEquals("" + "pw".hashCode(), create().fetchValue("select {0} from {1}", T_3485.PW, T_3485));
    }

    @Test
    public void testH2StreamsReduceResultsIntoBatch() {
        jOOQAbstractTest.reset = false;

        int[] result =
        create().selectFrom(T_BOOK)
                .where(T_BOOK.ID.in(2, 3))
                .orderBy(T_BOOK.ID)
                .fetch()
                .stream()
                .map(book -> book.setTitle(book.getTitle().toUpperCase()))
                .reduce(
                    create().batch(update(T_BOOK).set(T_BOOK.TITLE, (String) null).where(T_BOOK.ID.eq((Integer) null))),
                    (batch, book) -> batch.bind(book.getTitle(), book.getId()),
                    (b1, b2) -> b1
                )
                .execute();

        assertEquals(2, result.length);
        assertEquals(
            asList(
                BOOK_TITLES.get(0),
                BOOK_TITLES.get(1).toUpperCase(),
                BOOK_TITLES.get(2).toUpperCase(),
                BOOK_TITLES.get(3)),
            create().fetchValues(select(T_BOOK.TITLE).from(T_BOOK).orderBy(T_BOOK.ID))
        );
    }

    @Test
    public void testPerformance_INSERT() {

    }

    private void testPerformance(CheckedRunnable jdbc, CheckedRunnable jooq, int repetitions) {

        // Bootstrapping
        runnable(jdbc).run();
        runnable(jooq).run();

        for (int i = 0; i < repetitions; i++) {

        }
    }

    @Test
    public void testH2RowLevelSecurity() throws Exception {
        Configuration c = create().configuration();

        DSLContext fullaccess = create();
        DSLContext restricted = DSL.using(c.derive(DefaultVisitListenerProvider.providers(new AccountIDFilter(1, 2))));

        assertEquals(3, fullaccess.fetch(ACCOUNTS).size());
        assertEquals(2, restricted.fetch(ACCOUNTS).size());

        assertEquals(6, fullaccess.fetch(TRANSACTIONS).size());
        assertEquals(5, restricted.fetch(TRANSACTIONS).size());

        assertEquals(asList(1, 3), fullaccess.fetchValues(select(ACCOUNTS.ID).from(ACCOUNTS).where(ACCOUNTS.ACCOUNT_OWNER.eq("John")).orderBy(1)));
        assertEquals(asList(1   ), restricted.fetchValues(select(ACCOUNTS.ID).from(ACCOUNTS).where(ACCOUNTS.ACCOUNT_OWNER.eq("John")).orderBy(1)));

        Accounts a = ACCOUNTS.as("a");
        assertEquals(asList(1, 3), fullaccess.fetchValues(select(a.ID).from(a).where(a.ACCOUNT_OWNER.eq("John")).orderBy(1)));
        assertEquals(asList(1   ), restricted.fetchValues(select(a.ID).from(a).where(a.ACCOUNT_OWNER.eq("John")).orderBy(1)));

        Transactions t = TRANSACTIONS.as("t");
        assertEquals(asList(1, 2, 6), fullaccess.fetchValues(select(t.ID).from(t).where(t.ACCOUNT_ID.in(select(a.ID).from(a).where(a.ACCOUNT_OWNER.eq("John")))).orderBy(1)));
        assertEquals(asList(1, 2   ), restricted.fetchValues(select(t.ID).from(t).where(t.ACCOUNT_ID.in(select(a.ID).from(a).where(a.ACCOUNT_OWNER.eq("John")))).orderBy(1)));
    }

    @SuppressWarnings("unchecked")
    public class AccountIDFilter extends DefaultVisitListener {

        final Integer[] ids;

        public AccountIDFilter(Integer... ids) {
            this.ids = ids;
        }

        void push(VisitContext context) {
            conditionStack(context).push(new ArrayList<>());
            whereStack(context).push(false);
        }

        void pop(VisitContext context) {
            whereStack(context).pop();
            conditionStack(context).pop();
        }

        Deque<List<Condition>> conditionStack(VisitContext context) {
            Deque<List<Condition>> data = (Deque<List<Condition>>) context.data("conditions");

            if (data == null) {
                data = new ArrayDeque<>();
                context.data("conditions", data);
            }

            return data;
        }

        Deque<Boolean> whereStack(VisitContext context) {
            Deque<Boolean> data = (Deque<Boolean>) context.data("predicates");

            if (data == null) {
                data = new ArrayDeque<>();
                context.data("predicates", data);
            }

            return data;
        }

        List<Condition> conditions(VisitContext context) {
            return conditionStack(context).peek();
        }

        boolean where(VisitContext context) {
            return whereStack(context).peek();
        }

        void where(VisitContext context, boolean value) {
            whereStack(context).pop();
            whereStack(context).push(value);
        }

        <E> void pushConditions(VisitContext context, Table<?> table, Field<E> field, E... values) {

            // Check if we're visiting the given table
            if (context.queryPart() == table) {
                List<Clause> clauses = clauses(context);

                // ... and if we're in the context of the current subselect's
                // FROM clause
                if (clauses.contains(SELECT_FROM) ||
                    clauses.contains(UPDATE_UPDATE) ||
                    clauses.contains(DELETE_DELETE)) {

                    // If we're declaring a TABLE_ALIAS... (e.g. "T_BOOK" as "b")
                    if (clauses.contains(TABLE_ALIAS)) {
                        QueryPart[] parts = context.queryParts();

                        // ... move up the QueryPart visit path to find the
                        // defining aliased table, and extract the aliased
                        // field from it. (i.e. the "b" reference)
                        for (int i = parts.length - 2; i >= 0; i--) {
                            if (parts[i] instanceof Table) {
                                field = ((Table<?>) parts[i]).field(field);
                                break;
                            }
                        }
                    }

                    // Push a condition for the field of the (potentially aliased) table
                    conditions(context).add(field.in(values));
                }
            }
        }

        /**
         * Retrieve all clauses for the current subselect level, starting with
         * the last {@link Clause#SELECT}.
         */
        List<Clause> clauses(VisitContext context) {
            List<Clause> result = asList(context.clauses());
            int index = result.lastIndexOf(SELECT);

            if (index > 0)
                return result.subList(index, result.size() - 1);
            else
                return result;
        }

        @Override
        public void clauseStart(VisitContext context) {

            // Enter a new SELECT clause / nested select, or DML statement
            if (context.clause() == SELECT ||
                context.clause() == UPDATE ||
                context.clause() == DELETE ||
                context.clause() == INSERT) {
                push(context);
            }
        }

        @Override
        public void clauseEnd(VisitContext context) {

            // Append all collected predicates to the WHERE clause if any
            if (context.clause() == SELECT_WHERE ||
                context.clause() == UPDATE_WHERE ||
                context.clause() == DELETE_WHERE) {
                List<Condition> conditions = conditions(context);

                if (conditions.size() > 0) {
                    context.context()
                           .formatSeparator()
                           .keyword(where(context) ? "and" : "where")
                           .sql(' ');

                    context.context().visit(DSL.condition(Operator.AND, conditions));
                }
            }

            // Leave a SELECT clause / nested select, or DML statement
            if (context.clause() == SELECT ||
                context.clause() == UPDATE ||
                context.clause() == DELETE ||
                context.clause() == INSERT) {
                pop(context);
            }
        }

        @Override
        public void visitEnd(VisitContext context) {
            pushConditions(context, ACCOUNTS, ACCOUNTS.ID, ids);
            pushConditions(context, TRANSACTIONS, TRANSACTIONS.ACCOUNT_ID, ids);

            // Check if we're rendering any condition within the WHERE clause
            // In this case, we can be sure that jOOQ will render a WHERE keyword
            if (context.queryPart() instanceof Condition) {
                List<Clause> clauses = clauses(context);

                if (clauses.contains(SELECT_WHERE) ||
                    clauses.contains(UPDATE_WHERE) ||
                    clauses.contains(DELETE_WHERE)) {
                    where(context, true);
                }
            }
        }
    }

    public class PatchCheckOption extends AccountIDFilter {

        public PatchCheckOption(Integer... ids) {
            super(ids);
        }

        @Override
        public void visitStart(VisitContext context) {
            super.visitStart(context);

            // Add Oracle CHECK OPTIONs to INSERT statements, if applicable
            if (context.family() == ORACLE) {
                patchCheckOption(context, ACCOUNTS, ACCOUNTS.ID, ids);
                patchCheckOption(context, TRANSACTIONS, TRANSACTIONS.ACCOUNT_ID, ids);
            }
        }

        private <E> void patchCheckOption(
                final VisitContext context,
                final Table<?> table,
                final Field<E> field,
                final E... values)
        {
            if (context.queryPart() == table) {

                // ... within a SQL INSERT INTO clause
                List<Clause> clauses = clauses(context);
                if (clauses.contains(INSERT_INSERT_INTO)

                    // But avoid recursion!
                    && !clauses.contains(CUSTOM)
                    && !clauses.contains(TEMPLATE)) {

                    // ... then, replace the table by an equivalent
                    // view with a CHECK OPTION clause
                    context.queryPart(new CustomQueryPart() {
                        @Override
                        public void accept(Context<?> ctx) {
                            ctx.visit(sql(
                                   "(SELECT * FROM {0} WHERE {1} WITH CHECK OPTION)",
                                   table,
                                   field.in(values).or(field.isNull())
                                ));
                        }
                    });
                }
            }
        }
    }
}
