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

import static org.jooq.test.hsqldb.generatedclasses.Tables.T_2845_CASE_SENSITIVITY;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_639_NUMBERS_TABLE;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_725_LOB_TEST;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_785;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_ARRAYS;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_AUTHOR;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_BOOK;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_BOOK_STORE;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_BOOLEANS;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_DATES;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_EXOTIC_TYPES;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_IDENTITY;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_TRIGGERS;
import static org.jooq.test.hsqldb.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.hsqldb.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.hsqldb.generatedclasses.Tables.V_BOOK;
import static org.jooq.test.hsqldb.generatedclasses.Tables.V_LIBRARY;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import org.jooq.AggregateFunction;
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
import org.jooq.test.hsqldb.generatedclasses.Keys;
import org.jooq.test.hsqldb.generatedclasses.Routines;
import org.jooq.test.hsqldb.generatedclasses.Sequences;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TArraysRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TBooleansRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TDatesRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TIdentityRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.T_2845CaseSensitivityRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.T_785Record;
import org.jooq.test.hsqldb.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.hsqldb.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.util.hsqldb.HSQLDBDataType;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HSQLDBTest extends jOOQAbstractTest<
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
        T_2845CaseSensitivityRecord> {

	@Override
    protected SQLDialect dialect() {
        return SQLDialect.HSQLDB;
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
    protected TableField<TBookRecord, Integer> TBook_REC_VERSION() {
        return T_BOOK.REC_VERSION;
    }

    @Override
    protected TableField<TBookRecord, Timestamp> TBook_REC_TIMESTAMP() {
        return super.TBook_REC_TIMESTAMP();
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
        return null;
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
    protected Table<T_2845CaseSensitivityRecord> CASE() {
        return T_2845_CASE_SENSITIVITY;
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
            HSQLDBDataType.BIGINT,
            HSQLDBDataType.BINARY,
            HSQLDBDataType.BINARYLARGEOBJECT,
            HSQLDBDataType.BIT,
            HSQLDBDataType.BOOLEAN,
            HSQLDBDataType.CHAR,
            HSQLDBDataType.CHARACTER,
            HSQLDBDataType.CHARACTERLARGEOBJECT,
            HSQLDBDataType.CHARACTERVARYING,
            HSQLDBDataType.CHARLARGEOBJECT,
            HSQLDBDataType.DATE,
            HSQLDBDataType.DATETIME,
            HSQLDBDataType.DECIMAL,
            HSQLDBDataType.DOUBLE,
            HSQLDBDataType.DOUBLEPRECISION,
            HSQLDBDataType.FLOAT,
            HSQLDBDataType.INT,
            HSQLDBDataType.INTEGER,
            HSQLDBDataType.LONGVARBINARY,
            HSQLDBDataType.LONGVARCHAR,
            HSQLDBDataType.NUMERIC,
            HSQLDBDataType.OBJECT,
            HSQLDBDataType.OTHER,
            HSQLDBDataType.REAL,
            HSQLDBDataType.SMALLINT,
            HSQLDBDataType.TIME,
            HSQLDBDataType.TIMESTAMP,
            HSQLDBDataType.TINYINT,
            HSQLDBDataType.VARBINARY,
            HSQLDBDataType.VARCHAR,
            HSQLDBDataType.VARCHARIGNORECASE,
        };
    }
}
