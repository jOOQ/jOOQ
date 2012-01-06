/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.test;

import static org.jooq.impl.Factory.val;
import static org.jooq.test.mysql.generatedclasses.Tables.T_BOOK_TO_BOOK_STORE;
import static org.jooq.test.mysql.generatedclasses.Tables.T_IDENTITY_PK;
import static org.jooq.test.mysql.generatedclasses.Tables.T_UNSIGNED;
import static org.jooq.test.mysql.generatedclasses.Tables.V_AUTHOR;
import static org.jooq.test.mysql.generatedclasses.Tables.V_BOOK;
import static org.jooq.util.mysql.MySQLFactory.aesDecrypt;
import static org.jooq.util.mysql.MySQLFactory.aesEncrypt;
import static org.jooq.util.mysql.MySQLFactory.compress;
import static org.jooq.util.mysql.MySQLFactory.decode;
import static org.jooq.util.mysql.MySQLFactory.desDecrypt;
import static org.jooq.util.mysql.MySQLFactory.desEncrypt;
import static org.jooq.util.mysql.MySQLFactory.encode;
import static org.jooq.util.mysql.MySQLFactory.md5;
import static org.jooq.util.mysql.MySQLFactory.password;
import static org.jooq.util.mysql.MySQLFactory.sha1;
import static org.jooq.util.mysql.MySQLFactory.sha2;
import static org.jooq.util.mysql.MySQLFactory.uncompress;
import static org.jooq.util.mysql.MySQLFactory.uncompressedLength;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SchemaMapping;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDTRecord;
import org.jooq.UpdatableTable;
import org.jooq.test.mysql.generatedclasses.Routines;
import org.jooq.test.mysql.generatedclasses.TestFactory;
import org.jooq.test.mysql.generatedclasses.enums.T_959JavaKeywords;
import org.jooq.test.mysql.generatedclasses.tables.TAuthor;
import org.jooq.test.mysql.generatedclasses.tables.TBook;
import org.jooq.test.mysql.generatedclasses.tables.TBookStore;
import org.jooq.test.mysql.generatedclasses.tables.TBookToBookStore;
import org.jooq.test.mysql.generatedclasses.tables.TIdentityPk;
import org.jooq.test.mysql.generatedclasses.tables.TTriggers;
import org.jooq.test.mysql.generatedclasses.tables.TUnsigned;
import org.jooq.test.mysql.generatedclasses.tables.T_639NumbersTable;
import org.jooq.test.mysql.generatedclasses.tables.T_658Ref;
import org.jooq.test.mysql.generatedclasses.tables.T_725LobTest;
import org.jooq.test.mysql.generatedclasses.tables.T_785;
import org.jooq.test.mysql.generatedclasses.tables.T_959;
import org.jooq.test.mysql.generatedclasses.tables.VLibrary;
import org.jooq.test.mysql.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookStoreRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookToBookStoreRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TIdentityPkRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TTriggersRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TUnsignedRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_639NumbersTableRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_658RefRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_725LobTestRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.T_785Record;
import org.jooq.test.mysql.generatedclasses.tables.records.VLibraryRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.XUnusedRecord;
import org.jooq.tools.unsigned.UByte;
import org.jooq.tools.unsigned.UInteger;
import org.jooq.tools.unsigned.ULong;
import org.jooq.tools.unsigned.UShort;
import org.jooq.util.mysql.MySQLDataType;
import org.jooq.util.mysql.MySQLFactory;

import org.junit.Test;


/**
 * @author Lukas Eder
 */
public class jOOQMySQLTest extends jOOQAbstractTest<
        TAuthorRecord,
        TBookRecord,
        TBookStoreRecord,
        TBookToBookStoreRecord,
        VLibraryRecord,
        XUnusedRecord,
        XUnusedRecord,
        TTriggersRecord,
        TUnsignedRecord,
        XUnusedRecord,
        TIdentityPkRecord,
        T_658RefRecord,
        T_725LobTestRecord,
        T_639NumbersTableRecord,
        T_785Record> {

    @Override
    protected TestFactory create(SchemaMapping mapping) {
        return new TestFactory(getConnection(), mapping);
    }

    @Override
    protected UpdatableTable<TAuthorRecord> TAuthor() {
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
    protected UpdatableTable<TBookRecord> TBook() {
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
    protected TableField<TBookRecord, String> TBook_TITLE() {
        return TBook.TITLE;
    }

    @Override
    protected UpdatableTable<TBookStoreRecord> TBookStore() {
        return TBookStore.T_BOOK_STORE;
    }

    @Override
    protected TableField<TBookStoreRecord, String> TBookStore_NAME() {
        return TBookStore.NAME;
    }

    @Override
    protected UpdatableTable<TBookToBookStoreRecord> TBookToBookStore() {
        return T_BOOK_TO_BOOK_STORE;
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
    protected Table<T_658RefRecord> T658() {
        return T_658Ref.T_658_REF;
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

    @Override
    protected TableField<TBookRecord, ? extends Enum<?>> TBook_LANGUAGE_ID() {
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
    protected UpdatableTable<XUnusedRecord> TDirectory() {
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
    protected TableField<XUnusedRecord, Byte> TDirectory_IS_DIRECTORY() {
        return null;
    }

    @Override
    protected TableField<XUnusedRecord, String> TDirectory_NAME() {
        return null;
    }

    @Override
    protected UpdatableTable<TTriggersRecord> TTriggers() {
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
    protected UpdatableTable<TIdentityPkRecord> TIdentityPK() {
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
    public void testMySQLEncryptionFunctions() throws Exception {
        MySQLFactory create = (MySQLFactory) create();

        assertNotNull(create.select(password("abc")).fetchOne(0));
        assertNotNull(create.select(md5("abc")).fetchOne(0));
        assertNotNull(create.select(sha1("abc")).fetchOne(0));
        assertNotNull(create.select(sha2("abc", 256)).fetchOne(0));
        assertEquals("abc", create.select(decode(encode("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create.select(aesDecrypt(aesEncrypt("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create.select(desDecrypt(desEncrypt("abc", "pw"), val("pw"))).fetchOne(0));
        assertEquals("abc", create.select(desDecrypt(desEncrypt("abc"))).fetchOne(0));
        assertEquals("abc", create.select(uncompress(compress("abc"))).fetchOne(0));
        assertEquals(3, create.select(uncompressedLength(compress("abc"))).fetchOne(0));
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
}
