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

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.union;
import static org.jooq.impl.DSL.currentUser;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.oracle.generatedclasses.multi_schema.Tables.T_BOOK_SALE;
import static org.jooq.test.oracle.generatedclasses.test.Routines.f691cursorIn;
import static org.jooq.test.oracle.generatedclasses.test.Routines.f691cursorOut;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fArrays1;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fArrays4;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fTables1;
import static org.jooq.test.oracle.generatedclasses.test.Routines.fTables4;
import static org.jooq.test.oracle.generatedclasses.test.Routines.pArrays1;
import static org.jooq.test.oracle.generatedclasses.test.Routines.pTables1;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_2155;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_3711;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_639_NUMBERS_TABLE;
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
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_TRIGGERS;
import static org.jooq.test.oracle.generatedclasses.test.Tables.T_UNSIGNED;
import static org.jooq.test.oracle.generatedclasses.test.Tables.V_AUTHOR;
import static org.jooq.test.oracle.generatedclasses.test.Tables.V_BOOK;
import static org.jooq.test.oracle.generatedclasses.test.Tables.V_LIBRARY;
import static org.jooq.test.oracle.generatedclasses.test.Test.TEST;
import static org.jooq.test.oracle.generatedclasses.test.UDTs.U_AUTHOR_TYPE;
import static org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.countBooks;
import static org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.load;
import static org.jooq.test.oracle2.generatedclasses.Tables.DATE_AS_TIMESTAMP_T_976;
import static org.jooq.test.oracle2.generatedclasses.Tables.DATE_AS_TIMESTAMP_T_DATES;
import static org.jooq.test.oracle2.generatedclasses.udt.DateAsTimestampT_976ObjectType.DATE_AS_TIMESTAMP_T_976_OBJECT_TYPE;
import static org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA;
import static org.jooq.util.oracle.OracleDSL.contains;
import static org.jooq.util.oracle.OracleDSL.rowscn;
import static org.jooq.util.oracle.OracleDSL.scnToTimestamp;
import static org.jooq.util.oracle.OracleDSL.score;
import static org.jooq.util.oracle.OracleDSL.sysContext;
import static org.jooq.util.oracle.OracleDSL.timestampToScn;
import static org.jooq.util.oracle.OracleDSL.toChar;
import static org.jooq.util.oracle.OracleDSL.toNumber;
import static org.jooq.util.oracle.OracleDSL.versionsEndscn;
import static org.jooq.util.oracle.OracleDSL.versionsEndtime;
import static org.jooq.util.oracle.OracleDSL.versionsOperation;
import static org.jooq.util.oracle.OracleDSL.versionsStartscn;
import static org.jooq.util.oracle.OracleDSL.versionsStarttime;
import static org.jooq.util.oracle.OracleDSL.versionsXid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.jooq.AggregateFunction;
import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.Context;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record14;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record5;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.CustomField;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.impl.SQLDataType;
import org.jooq.lambda.Unchecked;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;
import org.jooq.test.all.converters.Boolean_TF_UC;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.Boolean_YN_LC;
import org.jooq.test.all.converters.Boolean_YN_UC;
import org.jooq.test.oracle.generatedclasses.multi_schema.packages.MsSynonymPackage;
import org.jooq.test.oracle.generatedclasses.multi_schema.tables.records.TBookSaleRecord;
import org.jooq.test.oracle.generatedclasses.multi_schema.udt.records.NumberObjectRecord;
import org.jooq.test.oracle.generatedclasses.multi_schema.udt.records.NumberTableRecord;
import org.jooq.test.oracle.generatedclasses.multi_schema.udt.records.U_4311Record;
import org.jooq.test.oracle.generatedclasses.multi_schema.udt.records.U_4347Record;
import org.jooq.test.oracle.generatedclasses.multi_schema.udt.records.U_4347TableRecord;
import org.jooq.test.oracle.generatedclasses.sys.udt.Xmltype;
import org.jooq.test.oracle.generatedclasses.sys.udt.records.XmltypeRecord;
import org.jooq.test.oracle.generatedclasses.test.Keys;
import org.jooq.test.oracle.generatedclasses.test.Routines;
import org.jooq.test.oracle.generatedclasses.test.Sequences;
import org.jooq.test.oracle.generatedclasses.test.packages.Library;
import org.jooq.test.oracle.generatedclasses.test.packages.TestSynonymPackage;
import org.jooq.test.oracle.generatedclasses.test.routines.P2155;
import org.jooq.test.oracle.generatedclasses.test.routines.PNested;
import org.jooq.test.oracle.generatedclasses.test.tables.TExoticTypes;
import org.jooq.test.oracle.generatedclasses.test.tables.T_725LobTest;
import org.jooq.test.oracle.generatedclasses.test.tables.VIncomplete;
import org.jooq.test.oracle.generatedclasses.test.tables.pojos.TAuthor;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TArraysRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TAuthorRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBookRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBookStoreRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBookToBookStoreRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TBooleansRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TDatesRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TDirectoryRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TExoticTypesRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TTriggersRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.TUnsignedRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_3711Record;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_725LobTestRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.T_785Record;
import org.jooq.test.oracle.generatedclasses.test.tables.records.VIncompleteRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.VLibraryRecord;
import org.jooq.test.oracle.generatedclasses.test.tables.records.XUnusedRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.OInvalidType;
import org.jooq.test.oracle.generatedclasses.test.udt.UAddressType;
import org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType;
import org.jooq.test.oracle.generatedclasses.test.udt.UInvalidTable;
import org.jooq.test.oracle.generatedclasses.test.udt.UInvalidType;
import org.jooq.test.oracle.generatedclasses.test.udt.UStreetType;
import org.jooq.test.oracle.generatedclasses.test.udt.records.OInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UAddressTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UAddressTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UAuthorTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UBookArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UBookTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UBookTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UInvalidTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UInvalidTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNested_1Record;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNested_2Record;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNested_3Record;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNumberArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNumberLongArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UNumberTableRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UStreetTypeRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.UStringArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ArrayRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ObjectRecord;
import org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetBooks;
import org.jooq.test.oracle.generatedclasses.usr_2522_a.udt.records.U_2522Record;
import org.jooq.test.oracle2.generatedclasses.tables.records.DateAsTimestampTDatesRecord;
import org.jooq.test.oracle2.generatedclasses.tables.records.DateAsTimestampT_976Record;
import org.jooq.test.oracle2.generatedclasses.udt.records.DateAsTimestampT_976ObjectTypeRecord;
import org.jooq.test.oracle2.generatedclasses.udt.records.DateAsTimestampT_976VarrayTypeRecord;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.oracle.OracleDataType;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.jolbox.bonecp.BoneCPDataSource;

import oracle.sql.BLOB;
import oracle.sql.CLOB;


/**
 * @author Lukas Eder
 */
public class OracleTest extends jOOQAbstractTest<
        TAuthorRecord,
        org.jooq.test.oracle.generatedclasses.test.tables.pojos.TAuthor,
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
        TUnsignedRecord,
        TExoticTypesRecord,
        XUnusedRecord,
        XUnusedRecord,
        XUnusedRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record,
        XUnusedRecord> {

    static {
        // [#624] Incomplete or erroneous artefacts must be generated too. This
        // won't compile, if there is anything wrong with code generation of
        // incomplete or erroneous artefacts

        try {
            Class.forName(VIncomplete.class.getName());
            Class.forName(VIncompleteRecord.class.getName());
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
    protected SQLDialect dialect() {
        return SQLDialect.ORACLE;
    }

    @Override
    protected DAO<TAuthorRecord, org.jooq.test.oracle.generatedclasses.test.tables.pojos.TAuthor, Integer> TAuthorDao() {
        return new org.jooq.test.oracle.generatedclasses.test.tables.daos.TAuthorDao(create().configuration());
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
    protected Table<TBookSaleRecord> TBookSale() {
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
    protected TableField<TExoticTypesRecord, String> TExoticTypes_PLAIN_SQL_CONVERTER_XML() {
        return T_EXOTIC_TYPES.PLAIN_SQL_CONVERTER_XML;
    }

    @Override
    protected TableField<TExoticTypesRecord, String> TExoticTypes_PLAIN_SQL_BINDING_XML() {
        return T_EXOTIC_TYPES.PLAIN_SQL_BINDING_XML;
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
    protected Table<TDirectoryRecord> TDirectory() {
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
    protected Table<XUnusedRecord> TIdentityPK() {
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
            return Routines.fGetOneCursor(new UNumberArrayRecord(array));
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
    public void testOracleHints() throws Exception {
        assertEquals(1, create().selectOne().hint("/*+ALL_ROWS*/").fetchOne(0));
        assertEquals(1, create().select(val(1)).hint("/*+ALL_ROWS*/").fetchOne(0));
        assertEquals(1, create().selectDistinct(val(1)).hint("/*+ALL_ROWS*/").fetchOne(0));
    }

    // @Test [#1119] TODO reactivate this test
    public void testOraclePipelinedFunctions() throws Exception {
        // TODO [#1113] [#1119] Standalone calls to pipelined functions should
        // be possible too
        System.out.println(Routines.fPipelinedArray1(create().configuration()));
    }

    @Test
    public void testOracleTableTypes() throws Exception {

        // FIRST, check unnesting of VARRAY/TABLE of NUMBER
        // ------------------------------------------------

        // Unnesting arrays
        assertEquals(emptyList(),
            create().select().from(table(new UNumberArrayRecord((Integer[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UNumberArrayRecord())).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UNumberArrayRecord(1))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(new UNumberArrayRecord(1, 2))).fetch(0));

        // Unnesting tables
        assertEquals(emptyList(),
            create().select().from(table(new UNumberTableRecord((Integer[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UNumberTableRecord())).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UNumberTableRecord(1))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(new UNumberTableRecord(1, 2))).fetch(0));

        // Unnesting arrays from functions
        assertEquals(emptyList(),
            create().select().from(table(fArrays1((UNumberArrayRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays1(new UNumberArrayRecord((Integer[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays1(new UNumberArrayRecord()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fArrays1(fArrays1(new UNumberArrayRecord(1))))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(fArrays1(fArrays1(new UNumberArrayRecord(1, 2))))).fetch(0));

        // Unnesting tables from functions
        assertEquals(emptyList(),
            create().select().from(table(fTables1((UNumberTableRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables1(new UNumberTableRecord((Integer[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables1(new UNumberTableRecord()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fTables1(fTables1(new UNumberTableRecord(1))))).fetch(0));
        assertEquals(asList(1, 2),
            create().select().from(table(fTables1(fTables1(new UNumberTableRecord(1, 2))))).fetch(0));

        // Retrieving arrays from functions
        assertNull(fArrays1(create().configuration(), null));
        assertEquals(emptyList(),
            fArrays1(create().configuration(), new UNumberArrayRecord((Integer[]) null)).getList());
        assertEquals(emptyList(),
            fArrays1(create().configuration(), new UNumberArrayRecord()).getList());
        assertEquals(asList(1),
            fArrays1(create().configuration(), fArrays1(create().configuration(), new UNumberArrayRecord(1))).getList());
        assertEquals(asList(1, 2),
            fArrays1(create().configuration(), fArrays1(create().configuration(), new UNumberArrayRecord(1, 2))).getList());

        // Retrieving tables from functions
        assertNull(fTables1(create().configuration(), null));
        assertEquals(emptyList(),
            fTables1(create().configuration(), new UNumberTableRecord((Integer[]) null)).getList());
        assertEquals(emptyList(),
            fTables1(create().configuration(), new UNumberTableRecord()).getList());
        assertEquals(asList(1),
            fTables1(create().configuration(), fTables1(create().configuration(), new UNumberTableRecord(1))).getList());
        assertEquals(asList(1, 2),
            fTables1(create().configuration(), fTables1(create().configuration(), new UNumberTableRecord(1, 2))).getList());

        // Retrieving arrays from procedures
        assertNull(pArrays1(create().configuration(), null));
        assertEquals(emptyList(),
            pArrays1(create().configuration(), new UNumberArrayRecord((Integer[]) null)).getList());
        assertEquals(emptyList(),
            pArrays1(create().configuration(), new UNumberArrayRecord()).getList());
        assertEquals(asList(1),
            pArrays1(create().configuration(), pArrays1(create().configuration(), new UNumberArrayRecord(1))).getList());
        assertEquals(asList(1, 2),
            pArrays1(create().configuration(), pArrays1(create().configuration(), new UNumberArrayRecord(1, 2))).getList());

        // Retrieving tables from procedures
        assertNull(pTables1(create().configuration(), null));
        assertEquals(emptyList(),
            pTables1(create().configuration(), new UNumberTableRecord((Integer[]) null)).getList());
        assertEquals(emptyList(),
            pTables1(create().configuration(), new UNumberTableRecord()).getList());
        assertEquals(asList(1),
            pTables1(create().configuration(), pTables1(create().configuration(), new UNumberTableRecord(1))).getList());
        assertEquals(asList(1, 2),
            pTables1(create().configuration(), pTables1(create().configuration(), new UNumberTableRecord(1, 2))).getList());

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
            create().select().from(table(new UBookArrayRecord((UBookTypeRecord[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UBookArrayRecord())).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UBookArrayRecord(r1))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(new UBookArrayRecord(r1))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(new UBookArrayRecord(r1, r2))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(new UBookArrayRecord(r1, r2))).fetch(1));

        // Unnesting tables
        assertEquals(emptyList(),
            create().select().from(table(new UBookTableRecord((UBookTypeRecord[]) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(new UBookTableRecord())).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(new UBookTableRecord(r1))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(new UBookTableRecord(r1))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(new UBookTableRecord(r1, r2))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(new UBookTableRecord(r1, r2))).fetch(1));

        // Unnesting arrays from functions
        assertEquals(emptyList(),
            create().select().from(table(fArrays4((UBookArrayRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays4(new UBookArrayRecord((UBookTypeRecord[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fArrays4(new UBookArrayRecord()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fArrays4(new UBookArrayRecord(r1)))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(fArrays4(new UBookArrayRecord(r1)))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(fArrays4(fArrays4(new UBookArrayRecord(r1, r2))))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(fArrays4(fArrays4(new UBookArrayRecord(r1, r2))))).fetch(1));

        // Unnesting tables from functions
        assertEquals(emptyList(),
            create().select().from(table(fTables4((UBookTableRecord) null))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables4(new UBookTableRecord((UBookTypeRecord[]) null)))).fetch(0));
        assertEquals(emptyList(),
            create().select().from(table(fTables4(new UBookTableRecord()))).fetch(0));
        assertEquals(asList(1),
            create().select().from(table(fTables4(new UBookTableRecord(r1)))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 1),
            create().select().from(table(fTables4(new UBookTableRecord(r1)))).fetch(1));
        assertEquals(asList(1, 2),
            create().select().from(table(fTables4(fTables4(new UBookTableRecord(r1, r2))))).fetch(0));
        assertEquals(BOOK_TITLES.subList(0, 2),
            create().select().from(table(fTables4(fTables4(new UBookTableRecord(r1, r2))))).fetch(1));

        // Retrieving arrays from functions
        assertNull(fArrays4(create().configuration(), null));
        assertEquals(emptyList(),
            fArrays4(create().configuration(), new UBookArrayRecord((UBookTypeRecord[]) null)).getList());
        assertEquals(emptyList(),
            fArrays4(create().configuration(), new UBookArrayRecord()).getList());
        assertEquals(asList(r1),
            fArrays4(create().configuration(), fArrays4(create().configuration(), new UBookArrayRecord(r1))).getList());
        assertEquals(asList(r1, r2),
            fArrays4(create().configuration(), fArrays4(create().configuration(), new UBookArrayRecord(r1, r2))).getList());

        // Retrieving tables from functions
        assertNull(fTables4(create().configuration(), null));
        assertEquals(emptyList(),
            fTables4(create().configuration(), new UBookTableRecord((UBookTypeRecord[]) null)).getList());
        assertEquals(emptyList(),
            fTables4(create().configuration(), new UBookTableRecord()).getList());
        assertEquals(asList(r1),
            fTables4(create().configuration(), fTables4(create().configuration(), new UBookTableRecord(r1))).getList());
        assertEquals(asList(r1, r2),
            fTables4(create().configuration(), fTables4(create().configuration(), new UBookTableRecord(r1, r2))).getList());


    }

    @Test
    public void testOracleMemberProcedures() throws Exception {
        jOOQAbstractTest.reset = false;

        UAuthorTypeRecord author1;
        UAuthorTypeRecord author2;

        // Unattached:
        author1 = new UAuthorTypeRecord();
        author1.setId(1);
        author2 = load(create().configuration(), author1);
        assertEquals(1, (int) author1.getId());
        assertEquals(1, (int) author2.getId());
        assertNull(author1.getFirstName());
        assertEquals("George", author2.getFirstName());
        assertNull(author1.getLastName());
        assertEquals("Orwell", author2.getLastName());

        // Attached
        author1 = create().newRecord(U_AUTHOR_TYPE);
        author1.setId(1);
        author2 = author1.load();
        assertEquals(1, (int) author1.getId());
        assertEquals(1, (int) author2.getId());
        assertEquals("George", author1.getFirstName());
        assertEquals("George", author2.getFirstName());
        assertEquals("Orwell", author1.getLastName());
        assertEquals("Orwell", author2.getLastName());

        // Count books
        author1 = create().newRecord(U_AUTHOR_TYPE);
        assertEquals(BigDecimal.ZERO, author1.countBooks());
        assertEquals(BigDecimal.ZERO, create().select(countBooks(author1)).fetchOne(0));

        author1 = create().newRecord(U_AUTHOR_TYPE);
        author1.setId(1);
        assertEquals(new BigDecimal("2"), author1.countBooks());
        assertEquals(new BigDecimal("2"), create().select(countBooks(author1)).fetchOne(0));

        // Get books
        author1 = create().newRecord(U_AUTHOR_TYPE);
        GetBooks noBooks = author1.getBooks();
        assertNull(noBooks.getBook1().getId());
        assertNull(noBooks.getBook1().getTitle());
        assertNull(noBooks.getBook2().getId());
        assertNull(noBooks.getBook2().getTitle());

        author1 = create().newRecord(U_AUTHOR_TYPE);
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

        // [#1584] Test STATIC MEMBER procedure calls
        UAuthorTypeRecord author3 = UAuthorType.newAuthor(create().configuration(), 3, "first", "last");
        assertEquals(3, (int) author3.getId());
        assertEquals("first", author3.getFirstName());
        assertEquals("last", author3.getLastName());

        UAuthorTypeRecord author4 = UAuthorType.getAuthor(create().configuration(), 3);
        assertEquals(author3, author4);
        assertEquals(3, (int) author4.getId());
        assertEquals("first", author4.getFirstName());
        assertEquals("last", author4.getLastName());

        // [#4372] Record must be attached for the following to work
        GetBooks getBooks4 = author4.getBooks();
        assertNull(getBooks4.getBook1().getId());
        assertNull(getBooks4.getBook1().getTitle());
        assertNull(getBooks4.getBook2().getId());
        assertNull(getBooks4.getBook2().getTitle());
        assertEquals(0, getBooks4.getBooks().size());

        UAuthorTypeRecord author5 = create().select(UAuthorType.getAuthor(3)).fetchOne(UAuthorType.getAuthor(3));
        assertEquals(author3, author5);
        assertEquals(3, (int) author5.getId());
        assertEquals("first", author5.getFirstName());
        assertEquals("last", author5.getLastName());

        // [#4372] Record must be attached for the following to work
        GetBooks getBooks5 = author5.getBooks();
        assertNull(getBooks5.getBook1().getId());
        assertNull(getBooks5.getBook1().getTitle());
        assertNull(getBooks5.getBook2().getId());
        assertNull(getBooks5.getBook2().getTitle());
        assertEquals(0, getBooks5.getBooks().size());
    }

    @Test
    public void testOracleCursorINOUT() throws Exception {
        assertEquals(4, (int) create().select(f691cursorIn(f691cursorOut())).fetchOne(0, Integer.class));
    }

    @Test
    public void testOracleTypedSequences() throws Exception {
        assertEquals(Byte.valueOf("1"), create().nextval(Sequences.S_961_BYTE));
        assertEquals(Short.valueOf("1"), create().nextval(Sequences.S_961_SHORT));
        assertEquals(Integer.valueOf("1"), create().nextval(Sequences.S_961_INT));
        assertEquals(Long.valueOf("1"), create().nextval(Sequences.S_961_LONG));
        assertEquals(BigInteger.valueOf(1), create().nextval(Sequences.S_961_BIG_INTEGER));
    }

    @Test
    public void testOracleDateAsTimestamp() throws Exception {
        jOOQAbstractTest.reset = false;

        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000 * 1000);
        Timestamp later = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 1000);

        GregorianCalendar calNow = new GregorianCalendar();
        GregorianCalendar calLater = new GregorianCalendar();
        calNow.setTime(now);
        calLater.setTime(later);

        // A record with nulls
        // -------------------
        DateAsTimestampT_976Record record = create().newRecord(DATE_AS_TIMESTAMP_T_976);
        record.setId(1);
        assertEquals(1, record.store());
        assertNull(record.getD());
        assertNull(record.getT());
        assertNull(record.getO());

        record.refresh();
        assertNull(record.getD());
        assertNull(record.getT());
        assertNull(record.getO());
        assertEquals(record, create().fetchOne(DATE_AS_TIMESTAMP_T_976, DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_ID.equal(1)));

        // A record with values
        // --------------------
        DateAsTimestampT_976ObjectTypeRecord o = create().newRecord(DATE_AS_TIMESTAMP_T_976_OBJECT_TYPE);
        o.setD(now);
        DateAsTimestampT_976VarrayTypeRecord t = new DateAsTimestampT_976VarrayTypeRecord();
        t.set(now, now);

        record = create().newRecord(DATE_AS_TIMESTAMP_T_976);
        record.setId(2);
        record.setD(now);
        record.setO(o);
        record.setT(t);
        record.store();
        assertEquals(record, create().fetchOne(DATE_AS_TIMESTAMP_T_976, DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_ID.equal(2)));

        // Check updates, too
        record.setD(later);
        o.setD(later);
        t.set(later, later, later);
        record.changed(DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_O, true);
        record.changed(DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_T, true);
        record.store();
        assertEquals(record, create().fetchOne(DATE_AS_TIMESTAMP_T_976, DATE_AS_TIMESTAMP_T_976.DATE_AS_TIMESTAMP_ID.equal(2)));

        // Procedures and packages
        // -----------------------
        assertEquals(now, org.jooq.test.oracle2.generatedclasses.Routines.p_976(create().configuration(), now));
        assertEquals(now, org.jooq.test.oracle2.generatedclasses.Routines.f_976(create().configuration(), now));
        assertEquals(now, create().select(org.jooq.test.oracle2.generatedclasses.Routines.f_976(now)).fetchOne(0));

        assertEquals(now, org.jooq.test.oracle2.generatedclasses.packages.DateAsTimestampPkg_976.p_976(create().configuration(), now));
        assertEquals(now, org.jooq.test.oracle2.generatedclasses.packages.DateAsTimestampPkg_976.f_976(create().configuration(), now));
        assertEquals(now, create().select(org.jooq.test.oracle2.generatedclasses.packages.DateAsTimestampPkg_976.f_976(now)).fetchOne(0));

        // [#2404] Some tests combining DATE as TIMESTAMP with converters
        DateAsTimestampTDatesRecord dates = create().newRecord(DATE_AS_TIMESTAMP_T_DATES);
        dates.setTs(calNow);
        dates.setD(calNow);
        dates.setT(calNow);
        dates.setId(1);
        assertEquals(1, dates.store());

        // This test might break again, depending on CET / CEST
        dates = create().newRecord(DATE_AS_TIMESTAMP_T_DATES);
        dates.setId(1);
        dates.refresh();
        assertEqualCalendar(calNow, dates.getTs(),
            Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
            Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND,
            Calendar.MILLISECOND);
        assertEqualCalendar(calNow, dates.getD(),
            Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        assertEqualCalendar(calNow, dates.getT(),
            Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND,
            Calendar.MILLISECOND);
    }

    private void assertEqualCalendar(Calendar c1, Calendar c2, int... fields) {
        for (int field : fields) {
            assertEquals(c1.get(field), c2.get(field));
        }
    }

    @Test
    public void testOracleWithDefaultSchema() throws Exception {

        // [#2133] Some checks verifying that the default schema generated code
        // is similar to the test schema generated code
        // [#3899] The TEST generation has a sequence less, because of a naming
        // collision caused by naming sequences 3899_$ and 3899$
        assertEquals(TEST.getSequences().size() + 1, DEFAULT_SCHEMA.getSequences().size());
        assertEquals(TEST.getTables().size(), DEFAULT_SCHEMA.getTables().size());
        assertEquals(TEST.getUDTs().size(), DEFAULT_SCHEMA.getUDTs().size());

        Select<Record3<String, String, String>> select =
        create().select(
                    org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR.FIRST_NAME,
                    org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR.LAST_NAME,
                    org.jooq.test.oracle3.generatedclasses.tables.T_BOOK.T_BOOK.TITLE)
                .from(org.jooq.test.oracle3.generatedclasses.tables.T_BOOK.T_BOOK)
                .join(org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR)
                .on(org.jooq.test.oracle3.generatedclasses.tables.T_BOOK.T_BOOK.AUTHOR_ID.eq(
                    org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR.ID))
                .orderBy(org.jooq.test.oracle3.generatedclasses.tables.T_BOOK.T_BOOK.ID);

        // No reference to the TEST schema should be contained
        assertFalse(select.getSQL().contains("TEST"));

        Result<Record3<String, String, String>> result = select.fetch();
        assertEquals(4, result.size());
        assertEquals(BOOK_FIRST_NAMES, result.getValues(org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR.FIRST_NAME));
        assertEquals(BOOK_LAST_NAMES, result.getValues(org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR.LAST_NAME));
        assertEquals(BOOK_TITLES, result.getValues(org.jooq.test.oracle3.generatedclasses.tables.T_BOOK.T_BOOK.TITLE));
    }

    @Test
    public void testOracleFunctions() {
        Record user = create().select(
            sysContext("USERENV", "SESSION_USER"),
            currentUser()).fetchOne();

        assertEquals(user.getValue(0), user.getValue(1));
    }

    @Test
    public void testOracleKeepDenseRank() {
        assertEquals(
            Arrays.asList(3, 7),
            Arrays.asList(
            create().select(
                        sum(TBook_ID()).keepDenseRankFirstOrderBy(TBook_AUTHOR_ID()),
                        sum(TBook_ID()).keepDenseRankLastOrderBy(TBook_AUTHOR_ID()))
                    .from(TBook())
                    .fetchOne()
                    .into(Integer[].class)));
    }

    @Test
    public void testOraclePartitionedOuterJoin() {

        // Maybe, find a more sensible query for the test case...?
        Result<Record2<String, String>> result1 =
        create().select(
                    TAuthor_FIRST_NAME(),
                    TBook_TITLE())
                .from(TAuthor()
                    .leftOuterJoin(TBook())
                    .partitionBy(TBook_TITLE())
                    .on(TAuthor_ID().equal(TBook_AUTHOR_ID())))
                .orderBy(
                    TAuthor_FIRST_NAME(),
                    TBook_ID())
                .fetch();

        assertEquals(8, result1.size());
        assertEquals(BOOK_TITLES, result1.getValues(TBook_TITLE()).subList(0, 4));
        assertEquals(Collections.nCopies(4, "George"), result1.getValues(TAuthor_FIRST_NAME()).subList(0, 4));
        assertEquals(Collections.nCopies(4, "Paulo"), result1.getValues(TAuthor_FIRST_NAME()).subList(4, 8));

        Result<Record2<String, String>> result2 =
        create().select(
                    TAuthor_FIRST_NAME(),
                    TBook_TITLE())
                .from(TAuthor())
                .leftOuterJoin(TBook())
                .partitionBy(TBook_TITLE())
                .on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                .orderBy(
                    TAuthor_FIRST_NAME(),
                    TBook_ID())
                .fetch();

        assertEquals(result1, result2);
    }

    @Test
    public void testOracleText() throws Exception {

        // [#816] CONTAINS() tests
        Result<Record2<String, BigDecimal>> result1 =
        create().select(TBook_TITLE(), score(2))
                .from(TBook())
                .where(contains(TBook_TITLE(), "Alq%").greaterThan(BigDecimal.ZERO))
                .or(contains(TBook_TITLE(), "O%", 2).greaterThan(BigDecimal.ZERO))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(1, result1.size());
        assertEquals("O Alquimista", result1.getValue(0, TBook_TITLE()));
        assertEquals(1, result1.getValue(0, score(2)).compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testOracleMultiSchemaFactories() throws Exception {
        DSLContext create = DSL.using(getConnectionMultiSchema(), SQLDialect.ORACLE);

        UAddressTypeRecord address = new UAddressTypeRecord();
        address.setStreet(new UStreetTypeRecord());
        address.getStreet().setNo("15");
        assertEquals("15", Routines.pEnhanceAddress1(create.configuration(), address));
    }

    @Test
    public void testOracleFlashbackQuery() throws Exception {
        // Just checking syntactic integrity
        Result<TAuthorRecord> a1 =
        create().selectFrom(T_AUTHOR.versionsBetweenScnMinvalue()
                                    .andMaxvalue())
                .orderBy(T_AUTHOR.ID)
                .fetch();
        assertTrue(a1.size() >= 2);

        create().select(
                    max(versionsStartscn()),
                    max(versionsStarttime()),
                    max(versionsEndscn()),
                    max(versionsEndtime()),
                    max(versionsXid()),
                    max(versionsOperation())
                )
                .from(T_AUTHOR.versionsBetweenTimestampMinvalue()
                              .andMaxvalue())
                .fetch();
    }

    @Test
    public void testOracleCursorResultsIntoUDTRecords() throws Exception {
        Result<Record> result = Routines.f691cursorOut(create().configuration());
        List<UBookTypeRecord> list = result.into(UBookTypeRecord.class);

        assertEquals(4, list.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(BOOK_IDS.get(0), list.get(0).getId());
            assertEquals(BOOK_TITLES.get(0), list.get(0).getTitle());
        }
    }

    @Test
    public void testOracleComments() throws Exception {
        assertEquals("An entity holding books", T_BOOK.getComment());
        assertEquals("The book's title", T_BOOK.TITLE.getComment());
        assertEquals("The year the book was published in", T_BOOK.PUBLISHED_IN.getComment());
        assertEquals("An entity holding authors of books", T_AUTHOR.getComment());
    }

    static class Street1 {
        public String street;
        public String no;
        public Integer[] floors;
    }

    static class Address1 {
        public Street1 street;
        public String city;
        public String country;
    }

    static class Author1 {
        public String firstName;
        public String lastName;
        public Address1 address;
    }


    static class Street2 {
        public String street;
        public String no;
        public List<Integer> floors;
    }

    static class Address2 {
        public Street2 street;
        public String city;
        public String country;
    }

    static class Author2 {
        public String firstName;
        public String lastName;
        public Address2 address;
    }

    @Test
    public void testOracleUDTRecordMapping() throws Exception {
        TAuthorRecord record = create()
            .selectFrom(T_AUTHOR)
            .where(T_AUTHOR.ID.eq(1))
            .fetchOne();

        Author1 author1 = record.into(Author1.class);
        assertEquals("George", author1.firstName);
        assertEquals("Orwell", author1.lastName);
        assertEquals("Hampstead", author1.address.city);
        assertEquals("England", author1.address.country);
        assertEquals("Parliament Hill", author1.address.street.street);
        assertEquals(asList(1, 2, 3), asList(author1.address.street.floors));
        assertEquals("77", author1.address.street.no);

        Author2 author2 = record.into(Author2.class);
        assertEquals("George", author2.firstName);
        assertEquals("Orwell", author2.lastName);
        assertEquals("Hampstead", author2.address.city);
        assertEquals("England", author2.address.country);
        assertEquals("Parliament Hill", author2.address.street.street);
        assertEquals(asList(1, 2, 3), author2.address.street.floors);
        assertEquals("77", author2.address.street.no);
    }

    @Test
    public void testOracleUDTRecordMappingWithGeneratedUDTs() throws Exception {
        org.jooq.test.oracle.generatedclasses.test.tables.pojos.TAuthor author =
        create().selectFrom(T_AUTHOR)
                .where(T_AUTHOR.ID.eq(1))
                .fetchOneInto(org.jooq.test.oracle.generatedclasses.test.tables.pojos.TAuthor.class);

        assertEquals("George", author.getFirstName());
        assertEquals("Orwell", author.getLastName());
        assertEquals("Hampstead", author.getAddress().getCity());
        assertEquals("England", author.getAddress().getCountry());
        assertEquals("Parliament Hill", author.getAddress().getStreet().getStreet());
        assertEquals(asList(1, 2, 3), asList(author.getAddress().getStreet().getFloors().get()));
        assertEquals("77", author.getAddress().getStreet().getNo());
    }

    @Test
    public void testOracleUDTRecordMappingWithCustomRecordMapperProvider() throws Exception {
        final AtomicInteger i = new AtomicInteger();

        DSLContext ctx = create();
        ctx.configuration().set(new RecordMapperProvider() {
            @Override
            public <R extends Record, E> RecordMapper<R, E> provide(final RecordType<R> recordType, final Class<? extends E> type) {
                return record -> {

                    // Don't map this type for this test
                    if (record instanceof UStreetTypeRecord) {
                        i.incrementAndGet();
                        return null;
                    }

                    return new DefaultRecordMapper<R, E>(recordType, type).map(record);
                };
            }
        });

        TAuthorRecord record = ctx
            .selectFrom(T_AUTHOR)
            .where(T_AUTHOR.ID.eq(1))
            .fetchOne();

        Author1 author = record.into(Author1.class);
        assertEquals("George", author.firstName);
        assertEquals("Orwell", author.lastName);
        assertEquals("Hampstead", author.address.city);
        assertEquals("England", author.address.country);

        // The UStreetType type should have been encountered more than once (logger.format, map, etc.)
        assertTrue(i.intValue() > 0);
        assertNull(author.address.street);
    }

    @Test
    public void testOracleNestedTypes() throws Exception {
        Configuration configuration = create().configuration();

        {
            PNested result = Routines.pNested(configuration, null, null);
            assertNotNull(result);
            assertNull(result.getP3());
            assertNull(result.getP4());
        }

        {
            UNested_3Record u3 = new UNested_3Record();
            PNested result = Routines.pNested(configuration, u3, u3);
            assertNotNull(result);
            assertEquals(u3, result.getP3());
            assertNull(result.getP3().getId());
            assertNull(result.getP3().getNested());
            assertEquals(u3, result.getP4());
            assertNull(result.getP4().getId());
            assertNull(result.getP4().getNested());
        }


        {
            UNested_3Record u3 = new UNested_3Record();
            u3.setId(1);
            u3.setNested(new UNested_2Record());
            PNested result = Routines.pNested(configuration, u3, u3);
            assertNotNull(result);
            assertEquals(u3, result.getP3());
            assertEquals(1, (int) result.getP3().getId());
            assertEquals(0, result.getP3().getNested().size());
            assertEquals(u3, result.getP4());
            assertEquals(1, (int) result.getP4().getId());
            assertEquals(0, result.getP4().getNested().size());
        }


        {
            UNested_1Record u1 = new UNested_1Record();
            UNested_2Record u2 = new UNested_2Record(u1, u1);
            UNested_3Record u3 = new UNested_3Record();
            u3.setId(1);
            u3.setNested(u2);
            PNested result = Routines.pNested(configuration, u3, u3);
            assertNotNull(result);
            assertEquals(u3, result.getP3());
            assertEquals(1, (int) result.getP3().getId());
            assertEquals(2, result.getP3().getNested().size());
            assertEquals(u1, result.getP3().getNested().get()[0]);
            assertNull(result.getP3().getNested().get()[0].getId());
            assertNull(result.getP3().getNested().get()[0].getNested());
            assertEquals(u1, result.getP3().getNested().get()[1]);
            assertEquals(u3, result.getP4());
            assertEquals(1, (int) result.getP4().getId());
            assertEquals(2, result.getP4().getNested().size());
            assertEquals(u1, result.getP4().getNested().get()[0]);
            assertNull(result.getP4().getNested().get()[0].getId());
            assertNull(result.getP4().getNested().get()[0].getNested());
            assertEquals(u1, result.getP4().getNested().get()[1]);
        }


        {
            UNested_1Record u1 = new UNested_1Record();
            UNested_2Record u2 = new UNested_2Record(u1, u1);
            UNested_3Record u3 = new UNested_3Record();
            u1.setId(2);
            u1.setNested(new UNumberTableRecord());
            u3.setId(1);
            u3.setNested(u2);
            PNested result = Routines.pNested(configuration, u3, u3);
            assertNotNull(result);
            assertEquals(u3, result.getP3());
            assertEquals(1, (int) result.getP3().getId());
            assertEquals(2, result.getP3().getNested().size());
            assertEquals(u1, result.getP3().getNested().get()[0]);
            assertEquals(2, (int) result.getP3().getNested().get()[0].getId());
            assertEquals(0, result.getP3().getNested().get()[0].getNested().size());
            assertEquals(u1, result.getP3().getNested().get()[1]);
            assertEquals(u3, result.getP4());
            assertEquals(1, (int) result.getP4().getId());
            assertEquals(2, result.getP4().getNested().size());
            assertEquals(u1, result.getP4().getNested().get()[0]);
            assertEquals(2, (int) result.getP4().getNested().get()[0].getId());
            assertEquals(0, result.getP4().getNested().get()[0].getNested().size());
            assertEquals(u1, result.getP4().getNested().get()[1]);
        }


        {
            UNested_3Record u3 = new UNested_3Record(1,
                new UNested_2Record(
                    new UNested_1Record(2, new UNumberTableRecord(3, 4)),
                    new UNested_1Record(2, new UNumberTableRecord(3, 4))
                )
            );

            PNested result = Routines.pNested(configuration, u3, u3);
            assertNotNull(result);
            assertEquals(u3, result.getP3());
            assertEquals(1, (int) result.getP3().getId());
            assertEquals(2, result.getP3().getNested().size());
            assertEquals(new UNested_1Record(2, new UNumberTableRecord(3, 4)), result.getP3().getNested().get()[0]);
            assertEquals(2, (int) result.getP3().getNested().get()[0].getId());
            assertEquals(asList(3, 4), result.getP3().getNested().get()[0].getNested().getList());
            assertEquals(new UNested_1Record(2, new UNumberTableRecord(3, 4)), result.getP3().getNested().get()[1]);
            assertEquals(u3, result.getP4());
            assertEquals(1, (int) result.getP4().getId());
            assertEquals(2, result.getP4().getNested().size());
            assertEquals(new UNested_1Record(2, new UNumberTableRecord(3, 4)), result.getP4().getNested().get()[0]);
            assertEquals(2, (int) result.getP4().getNested().get()[0].getId());
            assertEquals(asList(3, 4), result.getP4().getNested().get()[0].getNested().getList());
            assertEquals(new UNested_1Record(2, new UNumberTableRecord(3, 4)), result.getP4().getNested().get()[1]);
        }
    }

    @Test
    public void testOracleProceduresReturningNULLObjectTypes() {
        assertNull(Routines.p3005(create().configuration()));
    }

    @Test
    public void testOracleOuterJoin() {
        jOOQAbstractTest.reset = false;

        create().insertInto(T_AUTHOR, T_AUTHOR.ID, T_AUTHOR.LAST_NAME)
                .values(3, "XX")
                .execute();

        Result<Record3<Integer, String, String>> result =
        create().select(T_AUTHOR.ID, T_AUTHOR.LAST_NAME, T_BOOK.TITLE)
                .from(T_AUTHOR, T_BOOK)
                .where(T_AUTHOR.ID.eq(T_BOOK.AUTHOR_ID.plus()))
                .and(T_BOOK.AUTHOR_ID.plus().eq(T_AUTHOR.ID))
                .orderBy(T_BOOK.ID.asc().nullsLast())
                .fetch();

        assertEquals(5, result.size());
        assertEquals(asList(1, 1, 2, 2, 3), result.getValues(0));
        assertEquals(union(BOOK_LAST_NAMES, singletonList("XX")), result.getValues(1));
        assertEquals(union(BOOK_TITLES, singletonList(null)), result.getValues(2));
    }

    @Test
    public void testOracleSynonymAndCrossSchemaTypeReferences() {
        jOOQAbstractTest.reset = false;

        Configuration conf = create().configuration();

        // Tables:
        // -------
        NumberTableRecord t = new NumberTableRecord(1, 2, 3);
        NumberObjectRecord o = new NumberObjectRecord(4, 5, 6);

        T_3711Record r = create().newRecord(T_3711);
        r.setV1(t);
        r.setV3(t);
        r.setV7(t);
        r.setV2(o);
        r.setV4(o);
        r.setV8(o);

        assertEquals(1,
        create().insertInto(T_3711)
                .set(r)
                .execute());

        assertEquals(r, create().fetchOne(T_3711));


        // Standalone procedure calls:
        // ---------------------------
        NumberTableRecord t11 = MsSynonymPackage.actualTable(conf);
        NumberTableRecord t12 = MsSynonymPackage.publicTable(conf);
        NumberTableRecord t13 = MsSynonymPackage.testTable(conf);

        NumberTableRecord t21 = TestSynonymPackage.actualTable(conf);
        NumberTableRecord t22 = TestSynonymPackage.publicTable(conf);
        NumberTableRecord t23 = TestSynonymPackage.testTable(conf);

        NumberObjectRecord o11 = MsSynonymPackage.actualObject(conf);
        NumberObjectRecord o12 = MsSynonymPackage.publicObject(conf);
        NumberObjectRecord o13 = MsSynonymPackage.testObject(conf);
        NumberObjectRecord o14 = MsSynonymPackage.testTransitive(conf);

        NumberObjectRecord o21 = TestSynonymPackage.actualObject(conf);
        NumberObjectRecord o22 = TestSynonymPackage.publicObject(conf);
        NumberObjectRecord o23 = TestSynonymPackage.testObject(conf);
        NumberObjectRecord o24 = TestSynonymPackage.testTransitive(conf);

        // In SQL
        Record14<
            NumberTableRecord,
            NumberTableRecord,
            NumberTableRecord,

            NumberTableRecord,
            NumberTableRecord,
            NumberTableRecord,

            NumberObjectRecord,
            NumberObjectRecord,
            NumberObjectRecord,
            NumberObjectRecord,

            NumberObjectRecord,
            NumberObjectRecord,
            NumberObjectRecord,
            NumberObjectRecord
        > record =
        create().select(
            MsSynonymPackage.actualTable(),
            MsSynonymPackage.publicTable(),
            MsSynonymPackage.testTable(),

            TestSynonymPackage.actualTable(),
            TestSynonymPackage.publicTable(),
            TestSynonymPackage.testTable(),

            MsSynonymPackage.actualObject(),
            MsSynonymPackage.publicObject(),
            MsSynonymPackage.testObject(),
            MsSynonymPackage.testTransitive(),

            TestSynonymPackage.actualObject(),
            TestSynonymPackage.publicObject(),
            TestSynonymPackage.testObject(),
            TestSynonymPackage.testTransitive()
        )
        .fetchOne();

        assertEquals(asList(1, 2, 3), t11.getList());
        assertEquals(asList(4, 5, 6), t12.getList());
        assertEquals(asList(7, 8, 9), t13.getList());
        assertEquals(asList(1, 2, 3), t21.getList());
        assertEquals(asList(4, 5, 6), t22.getList());
        assertEquals(asList(7, 8, 9), t23.getList());

        assertEquals(asList(1, 2, 3), record.value1().getList());
        assertEquals(asList(4, 5, 6), record.value2().getList());
        assertEquals(asList(7, 8, 9), record.value3().getList());
        assertEquals(asList(1, 2, 3), record.value4().getList());
        assertEquals(asList(4, 5, 6), record.value5().getList());
        assertEquals(asList(7, 8, 9), record.value6().getList());

        assertEquals(asList(1, 2, 3), asList(o11.into(Integer[].class)));
        assertEquals(asList(4, 5, 6), asList(o12.into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(o13.into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(o14.into(Integer[].class)));
        assertEquals(asList(1, 2, 3), asList(o21.into(Integer[].class)));
        assertEquals(asList(4, 5, 6), asList(o22.into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(o23.into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(o24.into(Integer[].class)));

        assertEquals(asList(1, 2, 3), asList(record.value7().into(Integer[].class)));
        assertEquals(asList(4, 5, 6), asList(record.value8().into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(record.value9().into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(record.value10().into(Integer[].class)));
        assertEquals(asList(1, 2, 3), asList(record.value11().into(Integer[].class)));
        assertEquals(asList(4, 5, 6), asList(record.value12().into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(record.value13().into(Integer[].class)));
        assertEquals(asList(7, 8, 9), asList(record.value14().into(Integer[].class)));
    }

    @Test
    public void testOraclePojosEqualsAndHashCode() {
        Set<TAuthor> set = new LinkedHashSet<>();

        for (int i = 0; i < 3; i++)
            set.add(new TAuthor());

        assertEquals(1, set.size());


        for (int i = 0; i < 3; i++)
            set.add(
                new TAuthor(1, "a", "a", null, 1,
                    new org.jooq.test.oracle.generatedclasses.test.udt.pojos.UAddressType(
                        new org.jooq.test.oracle.generatedclasses.test.udt.pojos.UStreetType("street", "no", new UNumberArrayRecord(1, 2, 3), new byte[0], "x"),
                        "zip", "city", "country", Date.valueOf("2000-01-01"), null, null, null
                    )
                )
            );

        assertEquals(2, set.size());
    }

    @Test
    public void testOracleConverterOnProceduresAndUDTs() {
        LocalDate zero = LocalDate.ofEpochDay(0);
        LocalDate one = LocalDate.ofEpochDay(1);
        U_2155ObjectRecord record = new U_2155ObjectRecord(zero, new U_2155ArrayRecord(zero, zero));
        U_2155ArrayRecord array = new U_2155ArrayRecord();

        try {

            // Interaction with tables
            // -----------------------
            assertEquals(1,
            create().insertInto(T_2155, T_2155.ID, T_2155.D1, T_2155.D2, T_2155.D3)
                    .values(1, null, null, null)
                    .execute());

            assertEquals(1,
            create().insertInto(T_2155, T_2155.ID, T_2155.D1, T_2155.D2, T_2155.D3)
                    .values(2, zero, record, array)
                    .execute());

            Result<T_2155Record> result =
            create().selectFrom(T_2155)
                    .orderBy(T_2155.ID)
                    .fetch();

            create().fetch("select * from t_2155");

            assertEquals(1, (int) result.get(0).getId());
            assertNull(result.get(0).getD1());
            assertNull(result.get(0).getD2());
            assertNull(result.get(0).getD3());

            assertEquals(2, (int) result.get(1).getId());
            assertEquals(zero, result.get(1).getD1());
            assertEquals(record, result.get(1).getD2());
            assertEquals(array, result.get(1).getD3());

            // Interaction with procedures / functions
            // ---------------------------------------

            assertEquals(zero, Routines.f2155(create().configuration(), null, zero, null, one));
            assertEquals(one, Routines.f2155(create().configuration(), 1, zero, null, one));

            P2155 p1 = Routines.p2155(create().configuration(), 0, zero);
            assertEquals(zero, p1.getP4());
            assertEquals(zero, p1.getP5());

            P2155 p2 = Routines.p2155(create().configuration(), 0, one);
            assertEquals(one, p2.getP4());
            assertEquals(one, p2.getP5());
        }
        finally {
            create().delete(T_2155).execute();
        }
    }

    @Test
    public void testOracleArraysWithSpringDataSources() {
        UNumberArrayRecord numberArray = new UNumberArrayRecord(1, 2, 3);
        UBookArrayRecord bookArray = new UBookArrayRecord(new UBookTypeRecord(1, "A"), new UBookTypeRecord(2, "B"));

        Configuration c1 = new DefaultConfiguration().set(dialect()).set(
            new TransactionAwareDataSourceProxy(
                new SingleConnectionDataSource(getConnection(), true)
            )
        );

        assertEquals(numberArray, Routines.fArrays1(c1, numberArray));
        assertEquals(bookArray, Routines.fArrays4(c1, bookArray));

        BasicDataSource ds2 = new BasicDataSource();
        ds2.setDriverClassName(getDriver());
        ds2.setUrl(getURL());
        ds2.setUsername(getUsername());
        ds2.setPassword(getPassword());
        ds2.setAccessToUnderlyingConnectionAllowed(true);

        Configuration c2 = new DefaultConfiguration().set(dialect()).set(
            new TransactionAwareDataSourceProxy(ds2)
        );

        assertEquals(numberArray, Routines.fArrays1(c2, numberArray));
        assertEquals(bookArray, Routines.fArrays4(c2, bookArray));

        BoneCPDataSource ds3 = new BoneCPDataSource();
        ds3.setDriverClass(getDriver());
        ds3.setJdbcUrl(getURL());
        ds3.setUsername(getUsername());
        ds3.setPassword(getPassword());

        Configuration c31 = new DefaultConfiguration().set(dialect()).set(
            new TransactionAwareDataSourceProxy(ds3)
        );

        assertEquals(numberArray, Routines.fArrays1(c31, numberArray));
        assertEquals(bookArray, Routines.fArrays4(c31, bookArray));

        Configuration c32 = new DefaultConfiguration().set(dialect()).set(ds3);

        assertEquals(numberArray, Routines.fArrays1(c32, numberArray));
        assertEquals(bookArray, Routines.fArrays4(c32, bookArray));
    }

    @Test
    public void testOracleToNumberToChar() {
        Record5<String, String, String, BigDecimal, BigDecimal> result =
        create().select(
                    toChar(1210.73, "9999.9").trim(),
                    toChar(21, "000099").trim(),
                    toChar(new Timestamp(0), "yyyy-mm-dd"),
                    toNumber("1210.73", "9999.99"),
                    toNumber("546")
                )
                .fetchOne();

        assertEquals("1210.7", result.value1());
        assertEquals("000021", result.value2());
        assertEquals("1970-01-01", result.value3());
        assertEquals(new BigDecimal("1210.73"), result.value4());
        assertEquals(new BigDecimal("546"), result.value5());
    }

    @Test
    public void testOracleUDTRecordToString() {
        // [#3707] UDTRecord.toString() should generate an inlined version of the UDT

        UStreetTypeRecord r1 = new UStreetTypeRecord("street", "no", null, null, null);
        r1.attach(create().configuration());
        assertEquals(r1, create().fetchOne("select " + r1 + " from dual").getValue(0));

        UAddressTypeRecord r2 = new UAddressTypeRecord(
            new UStreetTypeRecord("street", "no", new UNumberArrayRecord(1, 2, 3), "abc".getBytes(), null),
            "zip", "city", "country", Date.valueOf("2000-01-01"), null, null, null
        );
        r2.attach(create().configuration());
        assertEquals(r2, create().fetchOne("select " + r2 + " from dual").getValue(0));
    }

    @Test
    public void testOraclePlainSQLUDTs() {
        try {
            create().execute("create type plain_sql_o1 as object(a int, b clob)");
            create().execute("create type plain_sql_t1 as table of plain_sql_o1");
            create().execute("create type plain_sql_o2 as object(x plain_sql_o1, y plain_sql_t1)");
            create().execute("create type plain_sql_t2 as table of plain_sql_t1");

            Result<Record> result = create().fetch(
                " SELECT "
              + "    cast(null as u_address_table),"
              + "    u_address_table(),"
              + "    u_address_table(null),"
              + "    u_address_table(u_address_type(null, null, null, null, null, null, null, null)),"
              + "    u_address_table(u_address_type(u_street_type('z', 'y', null, null, null), 'a', 'b', 'c', DATE '2000-01-01', 1, 'abc', 'xyz')),"
              + "    u_address_table(u_address_type(u_street_type('z', 'y', u_number_array(1, 2, 3), 'abc', 'xyz'), 'a', 'b', 'c', DATE '2000-01-01', 1, 'abc', 'xyz'))"
              + " FROM dual");
        }
        finally {
            create().execute("drop type plain_sql_t2");
            create().execute("drop type plain_sql_o2");
            create().execute("drop type plain_sql_t1");
            create().execute("drop type plain_sql_o1");
        }
    }

    @Test
    public void testOracleSerializedBinding() {
        clean(T_EXOTIC_TYPES);

        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.JAVA_IO_SERIALIZABLE)
                .values(1, null)
                .execute());

        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.JAVA_IO_SERIALIZABLE)
                .values(2, new S(1))
                .execute());

        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES, T_EXOTIC_TYPES.ID, T_EXOTIC_TYPES.JAVA_IO_SERIALIZABLE)
                .values(3, "abc".getBytes())
                .execute());

        Result<TExoticTypesRecord> result = create().selectFrom(T_EXOTIC_TYPES).orderBy(T_EXOTIC_TYPES.ID).fetch();

        assertNull(result.get(0).getJavaIoSerializable());
        assertEquals(1, ((S) result.get(1).getJavaIoSerializable()).value);
        assertEquals("abc", new String((byte[]) result.get(2).getJavaIoSerializable()));
    }

    @SuppressWarnings("serial")
    static class S implements Serializable {
        int value;

        S(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "S [value=" + value + "]";
        }
    }

    @Test
    public void testOracleRowSCN() throws Exception {
        Result<Record3<Long, Timestamp, Long>> result =
        create().select(
                    rowscn(),
                    scnToTimestamp(rowscn()),
                    timestampToScn(new Timestamp(currentTimeMillis())))
                .from(T_BOOK)
                .fetch();

        assertEquals(4, result.size());
    }

    @Test
    public void testOracleConstants() throws Exception {
        // PlsObjects.plsP(create().configuration(), PlsObjects.PLS_I_C, PlsObjects.PLS_V_C)
    }

    @Test
    public void testOracleTableFunctionInNestedSelect() throws Exception {

        // Crealogix IN-List use-case
        UNumberTableRecord ids = new UNumberTableRecord(1, 2, 3);
        Select<Record1<Integer>> selectIds = (Select<Record1<Integer>>) DSL.selectFrom(DSL.table(ids));

        create()
        .select()
        .from(DSL.select(T_BOOK.ID,
                         T_BOOK.AUTHOR_ID,
                         T_BOOK.TITLE)
                 .from(T_BOOK)
                 .join(T_AUTHOR)
                 .on(T_BOOK.AUTHOR_ID.eq(T_AUTHOR.ID))
                 .where(T_BOOK.ID.in(selectIds)))
        .fetch();
    }

    @Test
    public void testOracleBooleanInPLSQL() throws Exception {
        Connection c = getConnection();
        CallableStatement call = c.prepareCall(
            " DECLARE"
          + "   vi1 BOOLEAN := CASE ? WHEN 1 THEN TRUE WHEN 0 THEN FALSE ELSE NULL END;"
          + "   vi2 BOOLEAN := CASE ? WHEN 1 THEN TRUE WHEN 0 THEN FALSE ELSE NULL END;"
          + "   vi3 BOOLEAN;"
          + " BEGIN"
          + "   pls_objects.p_bool(vi1, vi2, vi3);"
          + ""
          + "   ? := CASE vi2 WHEN TRUE THEN 1 WHEN FALSE THEN 0 ELSE NULL END;"
          + "   ? := CASE vi3 WHEN TRUE THEN 1 WHEN FALSE THEN 0 ELSE NULL END;"
          + " END;");

        call.setObject(1, 1);
        call.setObject(2, null);
        call.registerOutParameter(3, Types.INTEGER);
        call.registerOutParameter(4, Types.INTEGER);
        call.execute();

        System.out.println(call.getBoolean(3) + " (was null: " + call.wasNull() + ")");
        System.out.println(call.getBoolean(4) + " (was null: " + call.wasNull() + ")");
    }

    @Test
    public void testOracleXMLTYPE() throws Exception {
        clean(T_EXOTIC_TYPES);

        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES)
                .set(T_EXOTIC_TYPES.ID, 1)
                .set(T_EXOTIC_TYPES.ORACLE_XML_AS_IS, (XmltypeRecord) null)
                .execute());

        assertEquals(1,
        create().insertInto(T_EXOTIC_TYPES)
                .set(T_EXOTIC_TYPES.ID, 2)
                .set(T_EXOTIC_TYPES.ORACLE_XML_AS_IS, Xmltype.createxml1("<a><b/></a>"))
                .execute());

        Result<TExoticTypesRecord> result =
        create().selectFrom(T_EXOTIC_TYPES)
                .orderBy(T_EXOTIC_TYPES.ID)
                .fetch();

        assertEquals(2, result.size());
        assertNull(result.get(0).getOracleXmlAsIs());

        XmltypeRecord record = result.get(1).getOracleXmlAsIs();
        assertEquals("<a><b/></a>", record.getstringval1());
    }

    @Test
    public void testOracleStaticMemberProcedure() throws Exception {
        jOOQAbstractTest.reset = false;

        UAuthorTypeRecord a1 = UAuthorType.newAuthor(create().configuration(), 3, "Alfred", "Hitchcock");
        assertEquals(3, (int) a1.getId());
        assertEquals("Alfred", a1.getFirstName());
        assertEquals("Hitchcock", a1.getLastName());

        TAuthorRecord a2 = create().fetchOne(T_AUTHOR, T_AUTHOR.ID.eq(3));
        assertEquals(3, (int) a2.getId());
        assertEquals("Alfred", a2.getFirstName());
        assertEquals("Hitchcock", a2.getLastName());
    }

    @Test
    public void testOracleNativeLOBs() throws Exception {
        T_725LobTest t = T_725_LOB_TEST;
        clean(t);

        assertEquals(1,
        create().insertInto(t, t.ID, t.USER_JAVA_SQL_BLOB, t.USER_JAVA_SQL_CLOB)
                .values(1, null, null)
                .execute());

        BLOB blob = null;
        CLOB clob = null;

        try {
            blob = BLOB.createTemporary(connection, false, BLOB.DURATION_SESSION);
            clob = CLOB.createTemporary(connection, false, CLOB.DURATION_SESSION);

            blob.setBytes(1, "abc".getBytes());
            clob.setString(1, "xyz");

            assertEquals(1,
            create().insertInto(
                      t
                    , t.ID
                    , t.USER_JAVA_SQL_BLOB
                    , t.JOOQ_JAVA_SQL_BLOB
                    , t.USER_JAVA_SQL_CLOB
                    , t.JOOQ_JAVA_SQL_CLOB
                    )
                    .values(
                      2
                    , blob
                    , "ABC".getBytes()
                    , clob
                    , "XYZ"
                    )
                    .execute());

            Result<T_725LobTestRecord> records =
            create().selectFrom(t)
                    .orderBy(t.ID)
                    .fetch();

            assertEquals(
                asList(1, 2),
                records.stream()
                       .map(T_725LobTestRecord::getId)
                       .collect(toList()));

            assertEquals(
                asList(null, "abc"),
                records.stream()
                       .map(Unchecked.function(
                           (T_725LobTestRecord r) -> r.getUserJavaSqlBlob() == null
                               ? null
                               : new String(r.getUserJavaSqlBlob().getBytes(1, (int) r.getUserJavaSqlBlob().length()))
                       ))
                       .collect(toList()));

            assertEquals(
                asList(null, "ABC"),
                records.stream()
                       .map(Unchecked.function(
                           (T_725LobTestRecord r) -> r.getJooqJavaSqlBlob() == null
                               ? null
                               : new String(r.getJooqJavaSqlBlob())
                       ))
                       .collect(toList()));

            assertEquals(
                asList(null, "xyz"),
                records.stream()
                       .map(Unchecked.function(
                           (T_725LobTestRecord r) -> r.getUserJavaSqlClob() == null
                               ? null
                               : r.getUserJavaSqlClob().getSubString(1, (int) r.getUserJavaSqlClob().length())
                       ))
                       .collect(toList()));

            assertEquals(
                asList(null, "XYZ"),
                records.stream()
                       .map(Unchecked.function(
                           (T_725LobTestRecord r) -> r.getJooqJavaSqlClob()
                       ))
                       .collect(toList()));
        }
        finally {
            JDBCUtils.safeFree(blob);
            JDBCUtils.safeFree(clob);
        }
    }

    @Test
    public void testOracleNativeXML() throws Exception {
        TExoticTypes t = T_EXOTIC_TYPES;
        clean(t);

        assertEquals(1,
        create().insertInto(t, t.ID, t.ORACLE_XML_AS_SQLXML)
                .values(1, null)
                .execute());

        SQLXML xml = null;

        try {
            xml = connection.createSQLXML();
            xml.setString("<a><b>text</b></a>");

            assertEquals(1,
            create().insertInto(t, t.ID, t.ORACLE_XML_AS_SQLXML)
                    .values(2, xml)
                    .execute());

            Result<TExoticTypesRecord> records =
            create().selectFrom(t)
                    .orderBy(t.ID)
                    .fetch();

            assertEquals(
                asList(1, 2),
                records.stream()
                       .map(TExoticTypesRecord::getId)
                       .collect(toList()));

            assertEquals(
                asList(null, "<a><b>text</b></a>"),
                records.stream()
                       .map(Unchecked.function(
                           (TExoticTypesRecord r) -> r.getOracleXmlAsSqlxml() == null
                               ? null
                               : r.getOracleXmlAsSqlxml().getString().replaceAll("\\s", "")
                       ))
                       .collect(toList()));

            Result<Record2<Integer, String>> result =
            create().select(t.ID, xpath(t.ORACLE_XML_AS_SQLXML, "//b/text()"))
                    .from(t)
                    .fetch();

            assertEquals(asList(1, 2), result.getValues(t.ID));
            assertEquals(asList(null, "text"), result.getValues(1));
        }

        finally {
            JDBCUtils.safeFree(xml);
        }
    }

    static final Field<String> xpath(Field<SQLXML> xml, String xpath) {
        return new CustomField<String>("xpath", SQLDataType.VARCHAR) {
            @Override
            public void accept(Context<?> ctx) {
                ctx.visit(DSL.field("extract({0}, {1}).getStringVal()", String.class, xml, inline(xpath)));
            }
        };
    }

    @Test
    public void testOracleUDTCodeGenSchemaMapping() {
        U_2522Record record = org.jooq.test.oracle.generatedclasses.usr_2522_a.Routines.f_2522(create().configuration());
        assertEquals(2, (int) record.getV());
    }

    @Test
    public void testOracleConnectionProviderInitStoredProcedureCall() {
        Configuration old = create().configuration();

        // [#4311] All usage of ThreadLocal must be recursion-safe
        DSLContext create = create(old.derive(new ConnectionProvider() {
            @Override
            public Connection acquire() throws DataAccessException {
                Connection c = old.connectionProvider().acquire();
                DSL.using(c).selectOne().fetch();
                return c;
            }

            @Override
            public void release(Connection c) throws DataAccessException {
                DSL.using(c).selectOne().fetch();
                old.connectionProvider().release(c);
            }
        }));

        U_4311Record record = org.jooq.test.oracle.generatedclasses.multi_schema.Routines.p4311(create.configuration(), new U_4311Record(42));
        assertEquals(42, (int) record.getId());
    }

    @Test
    public void testOracleMultiSchemaObjectTypes() {
        U_4347Record v1 = new U_4347Record(1, null, null);
        U_4347Record v2 = new U_4347Record(2, new UAddressTableRecord(new UAddressTypeRecord(), new UAddressTypeRecord()), new UAddressTypeRecord());
        U_4347TableRecord v3 = new U_4347TableRecord(new UAddressTypeRecord());

        Record3<U_4347Record, U_4347Record, U_4347TableRecord> result =
        create().select(val(v1), val(v2), val(v3))
                .fetchOne();

        assertEquals(asList((Object) v1, v2, v3), result.intoList());
    }

    @Test
    public void testOracleDatabaseLinks() {
        clean(T_BOOK_SALE);

        Table<Record> linked = table(name(T_BOOK_SALE.getName())).at("PUBLIC_LINK");
        assertEquals(1,
            create().insertInto(linked)
                    .set(T_BOOK_SALE.ID, 1)
                    .set(T_BOOK_SALE.BOOK_ID, 1)
                    .set(T_BOOK_SALE.BOOK_STORE_NAME, "Orell Füssli")
                    .set(T_BOOK_SALE.SOLD_AT, new Date(0))
                    .set(T_BOOK_SALE.SOLD_FOR, BigDecimal.ONE)
                    .execute()
        );

        assertEquals(1, create().fetchCount(linked));
        assertEquals(1, create().fetchCount(T_BOOK_SALE));
    }
}
