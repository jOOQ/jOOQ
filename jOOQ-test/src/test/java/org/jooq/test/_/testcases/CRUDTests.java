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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.SQLDialect;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataChangedException;
import org.jooq.exception.InvalidResultException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class CRUDTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public CRUDTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testInsertUpdateDelete() throws Exception {
        jOOQAbstractTest.reset = false;

        InsertQuery<A> i = create().insertQuery(TAuthor());
        i.addValue(TAuthor_ID(), 100);
        i.addValue(TAuthor_FIRST_NAME(), "Hermann");
        i.addValue(TAuthor_LAST_NAME(), "Hesse");
        i.addValue(TAuthor_DATE_OF_BIRTH(), Date.valueOf(zeroDate()));
        i.addValue(TAuthor_YEAR_OF_BIRTH(), 2010);

        // Check insertion of UDTs and Enums if applicable
        if (TAuthor_ADDRESS() != null) {
            addAddressValue(i, TAuthor_ADDRESS());
        }

        assertEquals(1, i.execute());

        A author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Hermann"));
        assertEquals("Hermann", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Hesse", author.getValue(TAuthor_LAST_NAME()));

        // [#1009] Somewhere on the way to the database and back, the CET time
        // zone is added, that's why there is a one-hour shift (except for SQLite)
        if (dialect() != SQLITE)
            assertEquals(Date.valueOf(zeroDate()), author.getValue(TAuthor_DATE_OF_BIRTH()));

        Map<Field<?>, String> map = new HashMap<Field<?>, String>();
        map.put(TAuthor_FIRST_NAME(), "Hermie");

        assertEquals(1, create()
            .update(TAuthor())
            .set(map)
            .where(TAuthor_ID().equal(100))
            .execute());

        author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Hermie"));
        assertEquals("Hermie", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Hesse", author.getValue(TAuthor_LAST_NAME()));

        if (TAuthor_ADDRESS() != null) {
            UDTRecord<?> address = author.getValue(TAuthor_ADDRESS());
            Object street1 = invoke(address, "getStreet");
            Object street2 = invoke(street1, "getStreet");
            assertEquals("Bahnhofstrasse", street2);
        }

        create().delete(TAuthor()).where(TAuthor_ID().equal(100)).execute();
        assertEquals(null, create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Hermie")));
    }

    // Generic type safety...
    private final <Z extends UDTRecord<?>> void addAddressValue(StoreQuery<?> q, Field<Z> field) throws Exception {
        Class<? extends Z> addressType = field.getType();
        Class<?> countryType = addressType.getMethod("getCountry").getReturnType();
        Class<?> streetType = addressType.getMethod("getStreet").getReturnType();

        Object country = null;
        try {
            countryType.getMethod("valueOf", String.class).invoke(countryType, "Germany");
        }
        catch (NoSuchMethodException e) {
            country = "Germany";
        }

        Object street = streetType.newInstance();
        Z address = addressType.newInstance();

        streetType.getMethod("setStreet", String.class).invoke(street, "Bahnhofstrasse");
        streetType.getMethod("setNo", String.class).invoke(street, "1");

        addressType.getMethod("setCountry", countryType).invoke(address, country);
        addressType.getMethod("setCity", String.class).invoke(address, "Calw");
        addressType.getMethod("setStreet", streetType).invoke(address, street);

        q.addValue(field, address);
    }

    public void testManager() throws Exception {
        jOOQAbstractTest.reset = false;

        List<A> select = create().fetch(TAuthor());
        assertEquals(2, select.size());

        select = create().fetch(TAuthor(), TAuthor_FIRST_NAME().equal("Paulo"));
        assertEquals(1, select.size());
        assertEquals("Paulo", select.get(0).getValue(TAuthor_FIRST_NAME()));

        try {
            create().fetchOne(TAuthor());
            fail();
        }
        catch (InvalidResultException expected) {}

        A selectOne = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Paulo"));
        assertEquals("Paulo", selectOne.getValue(TAuthor_FIRST_NAME()));

        // Some CRUD operations
        A author = create().newRecord(TAuthor());
        author.setValue(TAuthor_ID(), 15);
        author.setValue(TAuthor_LAST_NAME(), "Kästner");

        assertEquals(1, create().executeInsert(author));
        author.refresh();
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        assertEquals(0, create().executeUpdate(author, TAuthor_ID().equal(15)));
        author.setValue(TAuthor_FIRST_NAME(), "Erich");
        assertEquals(1, create().executeUpdate(author, TAuthor_ID().equal(15)));
        author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Erich"));
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Erich", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        // [#1692] Check for new simplified update method
        author.setValue(TAuthor_FIRST_NAME(), "Fritz");
        assertEquals(1, create().executeUpdate(author));
        author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Fritz"));
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Fritz", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        create().executeDelete(author);
        assertEquals(null, create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Erich")));
    }

    public void testUpdatablesCopy() throws Exception {
        if (TTriggers() == null) {
            log.info("SKIPPING", "UpdatableRecord.copy() tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        T r1 = create().newRecord(TTriggers());
        r1.setValue(TTriggers_COUNTER(), 1);
        assertEquals(1, r1.store());

        T r2 = r1.copy();
        T r3 = create().fetchOne(TTriggers());
        T r4 = r3.copy();

        // [#1874] Be sure that copies use "default" values for primary keys
        // in order to have sequence values generated.
        assertEquals(1, r2.store());
        assertEquals(0, r3.store());
        assertEquals(1, r4.store());

        assertEquals(3, (int) create().selectCount().from(TTriggers()).fetchOne(0, int.class));
    }

    @SuppressWarnings("unchecked")

    public void testUpdatablesKeysMethod() throws Exception {
        B b = create().selectFrom(TBook())
                      .where(TBook_ID().eq(1))
                      .fetchOne();
        B2S b2s = create()
                      .selectFrom(TBookToBookStore())
                      .where(TBookToBookStore_BOOK_ID().eq(1))
                      .and(TBookToBookStore_BOOK_STORE_NAME().eq("Ex Libris"))
                      .fetchOne();

        // [#1690] Check if values returned by the key() record are correct
        Record1<Integer> bKey = (Record1<Integer>) b.key();
        Record2<String, Integer> b2sKey = (Record2<String, Integer>) b2s.key();

        assertEquals(1, (int) bKey.value1());
        assertEquals("Ex Libris", b2sKey.value1());
        assertEquals(1, (int) b2sKey.value2());

        assertEquals(TBook_ID(), bKey.field1());
        assertEquals(TBookToBookStore_BOOK_STORE_NAME(), b2sKey.field1());
        assertEquals(TBookToBookStore_BOOK_ID(), b2sKey.field2());

        // [#1690] Check if modifications to the key() record are reflected in
        // the original record, and vice versa
        bKey.setValue(TBook_ID(), 5);
        assertEquals(5, (int) b.getValue(TBook_ID()));

        b.setValue(TBook_ID(), 6);
        assertEquals(6, (int) bKey.value1());
    }

    public void testUpdatablesInsertUpdate() throws Exception {
        jOOQAbstractTest.reset = false;

        A author = create().newRecord(TAuthor());

        //  No actions on unchanged authors
        assertFalse(author.changed());
        assertEquals(0, author.insert());
        assertCountAuthors(2);
        assertFalse(author.changed());
        assertEquals(0, author.update());
        assertCountAuthors(2);
        assertFalse(author.changed());
        assertEquals(0, author.store());
        assertCountAuthors(2);

        author.setValue(TAuthor_ID(), 3);
        author.setValue(TAuthor_LAST_NAME(), "XX");
        assertTrue(author.changed());
        assertEquals(0, author.update());
        assertTrue(author.changed());
        assertCountAuthors(2);

        assertEquals(1, author.insert());
        assertFalse(author.changed());
        assertCountAuthors(3);
        A test = getAuthor(3);
        assertEquals(3, (int) test.getValue(TAuthor_ID()));
        assertEquals("XX", test.getValue(TAuthor_LAST_NAME()));

        assertEquals(0, author.insert());
        assertFalse(author.changed());
        assertCountAuthors(3);

        assertEquals(0, author.update());
        assertFalse(author.changed());
        assertCountAuthors(3);

        author.setValue(TAuthor_LAST_NAME(), "YY");
        assertTrue(author.changed());
        try {
            author.insert();
            fail();
        }
        catch (DataAccessException expected) {}

        assertEquals(1, author.update());
        assertFalse(author.changed());
        test.refresh();
        assertEquals(3, (int) test.getValue(TAuthor_ID()));
        assertEquals("YY", test.getValue(TAuthor_LAST_NAME()));
    }

    public void testUpdatablesPK() throws Exception {
        jOOQAbstractTest.reset = false;

        B book = create().newRecord(TBook());
        try {
            book.refresh();
        }
        catch (InvalidResultException expected) {}

        // Fetch the original record
        B book1 = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        // Another copy of the original record
        B book2 = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        // Immediately store the original record. That shouldn't have any effect
        assertEquals(0, book1.store());

        // Modify and store the original record
        // [#1995] Check for correct changed() and original() values, too
        Integer id = book1.getValue(TBook_ID());
        book1.setValue(TBook_TITLE(), "1985");
        assertEquals("1984", book1.original().getValue(TBook_TITLE()));
        assertEquals("1984", book1.original(TBook_TITLE()));
        assertTrue(book1.changed());
        assertTrue(book1.changed(TBook_TITLE()));
        assertEquals(1, book1.store());
        assertEquals("1985", book1.original().getValue(TBook_TITLE()));
        assertEquals("1985", book1.original(TBook_TITLE()));
        assertFalse(book1.changed());
        assertFalse(book1.changed(TBook_TITLE()));

        // Fetch the modified record
        book1 = create().fetchOne(TBook(), TBook_ID().equal(id));

        // Modify the record
        book1.setValue(TBook_TITLE(), "1999");
        book1.setValue(TBook_AUTHOR_ID(), 3);
        assertEquals("1999", book1.getValue(TBook_TITLE()));

        // And refresh it again
        book1.refresh(TBook_TITLE());
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals(3, (int) book1.getValue(TBook_AUTHOR_ID()));

        book1.refresh();
        assertEquals(1, (int) book1.getValue(TBook_AUTHOR_ID()));
        assertEquals(0, book1.store());

        // Refresh the other copy of the original record
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1984", book2.getValue(TBook_TITLE()));
        book2.refresh();

        assertEquals(id, book1.getValue(TBook_ID()));
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        /* [pro] xx
        xx xx xx xxxxxx xxxxxxx xxxxxxxxxxx xxx xxxxxx xxx xx xx xxxxxx
        xx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
        xx [/pro] */

        // Delete the modified record
        assertEquals(1, book1.delete());
        assertEquals(0, book1.delete());
        assertEquals(0, book2.delete());

        // Fetch the remaining records
        assertEquals(null, create().fetchOne(TBook(), TBook_ID().equal(id)));

        // Store the record again from memory
        assertEquals(1, book1.store());
        book1.refresh();
        book2.refresh();

        assertEquals(id, book1.getValue(TBook_ID()));
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // Copy the records and store them again as another one
        book1 = book1.copy();
        book2 = book2.copy();
        assertNull(book1.getValue(TBook_ID()));
        assertNull(book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // Can't store the copies yet, as the primary key is null
        try {
            book1.store();
        } catch (DataAccessException expected) {}
        try {
            book2.store();
        } catch (DataAccessException expected) {}

        book1.setValue(TBook_ID(), 11);
        book2.setValue(TBook_ID(), 12);
        assertEquals(1, book1.store());
        assertEquals(1, book2.store());

        // Refresh the books
        book1 = create().newRecord(TBook());
        book2 = create().newRecord(TBook());

        book1.setValue(TBook_ID(), 11);
        book2.setValue(TBook_ID(), 12);

        book1.refresh();
        book2.refresh();

        assertEquals(Integer.valueOf(11), book1.getValue(TBook_ID()));
        assertEquals(Integer.valueOf(12), book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // Store a partial record
        A author = create().newRecord(TAuthor());
        author.setValue(TAuthor_ID(), 77);
        author.setValue(TAuthor_LAST_NAME(), "Döblin");
        assertEquals(1, author.store());
        assertEquals(Integer.valueOf(77),
            create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Döblin")).getValue(TAuthor_ID()));

        // Store an empty record
        S store = create().newRecord(TBookStore());
        assertEquals(0, store.store());

        // [#787] Store the same record twice.
        author = create().newRecord(TAuthor());
        author.setValue(TAuthor_ID(), 78);
        author.setValue(TAuthor_LAST_NAME(), "Cohen");
        assertEquals(1, author.store());
        assertEquals(0, author.store()); // No INSERT/UPDATE should be made

        author.setValue(TAuthor_FIRST_NAME(), "Arthur");
        assertEquals(1, author.store()); // This should produce an UPDATE
        assertEquals(1, create()
            .select(count())
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("Arthur"))
            .and(TAuthor_LAST_NAME().equal("Cohen"))
            .fetchOne(0));

        // [#945] Set the same value twice
        author = create().selectFrom(TAuthor())
                         .where(TAuthor_FIRST_NAME().equal("Arthur"))
                         .fetchOne();

        author.setValue(TAuthor_FIRST_NAME(), "Leonard");
        author.setValue(TAuthor_FIRST_NAME(), "Leonard");
        assertEquals(1, author.store());
        assertEquals(1, create()
            .select(count())
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("Leonard"))
            .and(TAuthor_LAST_NAME().equal("Cohen"))
            .fetchOne(0));
    }

    public void testUpdatablesPKChangePK() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#979] some additional tests related to modifying an updatable's
        // primary key. Setting it to the same value shouldn't result in an
        // INSERT statement...

        // This will result in no query
        B book1 = create().fetchOne(TBook(), TBook_ID().equal(1));
        book1.setValue(TBook_ID(), 1);
        assertEquals(0, book1.store());

        // This will result in an UPDATE
        book1.setValue(TBook_ID(), 1);
        book1.setValue(TBook_TITLE(), "new title");
        assertEquals(1, book1.store());
        assertEquals(4, create().selectCount().from(TBook()).fetchOne(0));

        B book2 = create().fetchOne(TBook(), TBook_ID().equal(1));
        assertEquals(1, (int) book2.getValue(TBook_ID()));
        assertEquals("new title", book2.getValue(TBook_TITLE()));

        // This should now result in an INSERT
        book2.setValue(TBook_ID(), 5);
        assertEquals(1, book2.store());

        B book3 = create().fetchOne(TBook(), TBook_ID().equal(5));
        assertEquals(5, (int) book3.getValue(TBook_ID()));
        assertEquals("new title", book3.getValue(TBook_TITLE()));
    }

    public void testUpdatablesUK() throws Exception {
        jOOQAbstractTest.reset = false;

        S store = create().newRecord(TBookStore());
        try {
            store.refresh();
        }
        catch (InvalidResultException expected) {}

        store.setValue(TBookStore_NAME(), "Rösslitor");
        assertEquals(1, store.store());

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Rösslitor"));
        assertEquals("Rösslitor", store.getValue(TBookStore_NAME()));

        // Updating the main unique key should result in a new record
        store.setValue(TBookStore_NAME(), "Amazon");
        assertEquals(1, store.store());

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Amazon"));
        assertEquals("Amazon", store.getValue(TBookStore_NAME()));

        // Delete and re-create the store
        store.delete();
        assertEquals("Amazon", store.getValue(TBookStore_NAME()));
        assertEquals(null, create().fetchOne(TBookStore(), TBookStore_NAME().equal("Amazon")));

        switch (dialect().family()) {
            /* [pro] xx
            xx xxxxxx xxx xxx xxx xxxxxx xx xxx xxxxx xxx xxxxxxxxxx xxxxxxx
            xx xxxxxx xx xxxxxxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxx
                xxxxxx

            xx [/pro] */
            default:
                store.store();
                assertEquals("Amazon", store.getValue(TBookStore_NAME()));

                store.refresh();
                assertEquals("Amazon", store.getValue(TBookStore_NAME()));
        }

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Rösslitor"));
        assertEquals("Rösslitor", store.getValue(TBookStore_NAME()));
    }

    public void testUpdatablesVersionAndTimestamp() throws Exception {
        if (TBook_REC_TIMESTAMP() == null && TBook_REC_VERSION() == null) {
            log.info("SKIPPING", "Record version and timestamp tests");
        }

        jOOQAbstractTest.reset = false;

        DSLContext create = create(new Settings().withExecuteWithOptimisticLocking(true));
        boolean t = TBook_REC_TIMESTAMP() != null;
        boolean v = TBook_REC_VERSION() != null;

        // Test data integrity check
        // -------------------------
        if (t) assertEquals(2, create.selectCount().from(TBook()).where(TBook_REC_TIMESTAMP().isNotNull()).fetchOne(0));
        if (v) assertEquals(2, create.selectCount().from(TBook()).where(TBook_REC_VERSION().isNotNull()).fetchOne(0));

        // Version and timestamp shouldn't change when there are constraint violations
        // -------------------------
        B book1 = create.newRecord(TBook());
        book1.setValue(TBook_ID(), 5);
        try {
            book1.store();
            fail();
        }
        catch (DataAccessException expected) {}
        if (t) assertNull(book1.getValue(TBook_REC_TIMESTAMP()));
        if (v) assertNull(book1.getValue(TBook_REC_VERSION()));

        // Test non-nullability of version and timestamp for new books
        // -------------------------
        B book2 = newBook(5);
        assertEquals(1, book2.store());
        Timestamp t2 = t ? book2.getValue(TBook_REC_TIMESTAMP()) : null;
        Integer v2 = v ? book2.getValue(TBook_REC_VERSION()) : null;
        if (t) assertNotNull(t2);
        if (v) assertNotNull(v2);

        // Test immutability of version and timestamp for non-stored books
        // -------------------------
        book2.refresh();
        assertEquals(0, book2.store());
        assertEquals(t2, t ? book2.getValue(TBook_REC_TIMESTAMP()) : null);
        assertEquals(v2, v ? book2.getValue(TBook_REC_VERSION()) : null);

        // Test resetting of version and timestamp for copied books
        // -------------------------
        B book3 = book2.copy();
        book3.setValue(TBook_ID(), 6);
        assertEquals(1, book3.store());
        Timestamp t3 = t ? book3.getValue(TBook_REC_TIMESTAMP()) : null;
        Integer v3 = v ? book3.getValue(TBook_REC_VERSION()) : null;
        if (t) assertNotNull(t3);
        if (v) assertNotNull(v3);
        if (t && t2 != null) assertFalse(t2.equals(t3));
        if (v && v2 != null) assertFalse(v2.equals(v3));

        // Check if updating all records will lead to updated version and timestamp values
        // -------------------------
        // BOOK[ID=4] has version and timestamp set to null
        B book4 = create().fetchOne(TBook(), TBook_ID().equal(4));
        book4.setValue(TBook_TITLE(), "Blah");
        assertEquals(1, book4.store());
        Timestamp t4 = t ? book4.getValue(TBook_REC_TIMESTAMP()) : null;
        Integer v4 = v ? book4.getValue(TBook_REC_VERSION()) : null;
        if (t) assertNotNull(t4);
        if (v) assertEquals(Integer.valueOf(1), v4);
        book4.refresh();
        if (t) assertEquals(t4, book4.getValue(TBook_REC_TIMESTAMP()));
        if (v) assertEquals(v4, book4.getValue(TBook_REC_VERSION()));

        // Increment both values
        book4.setValue(TBook_TITLE(), "Blah 1");
        assertEquals(1, book4.store());
        Timestamp t4a = t ? book4.getValue(TBook_REC_TIMESTAMP()) : null;
        Integer v4a = v ? book4.getValue(TBook_REC_VERSION()) : null;
        if (t) assertNotNull(t4a);
        if (v) assertEquals(Integer.valueOf(2), v4a);
        book4.refresh();
        if (t) assertEquals(t4a, book4.getValue(TBook_REC_TIMESTAMP()));
        if (v) assertEquals(v4a, book4.getValue(TBook_REC_VERSION()));

        // Don't change the book
        assertEquals(0, book4.store());
        if (t) assertEquals(t4a, book4.getValue(TBook_REC_TIMESTAMP()));
        if (v) assertEquals(v4a, book4.getValue(TBook_REC_VERSION()));
        book4.refresh();
        if (t) assertEquals(t4a, book4.getValue(TBook_REC_TIMESTAMP()));
        if (v) assertEquals(v4a, book4.getValue(TBook_REC_VERSION()));
    }

    public void testStoreWithOptimisticLock() throws Exception {
        jOOQAbstractTest.reset = false;

        /* [pro] xx
        xx xx xx xxxxxx xxxxxxx xxxxxxxxxxx xxx xxxxxx xxx
        xx xxxxxxxxxx xx xxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
        xx [/pro] */

        testStoreWithOptimisticLock0(TBook(), TBook_ID(), TBook_TITLE());

        // Avoid referential integrity problems for subsequent test
        create().delete(TBook()).execute();
        testStoreWithOptimisticLock0(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME());
    }

    private <R extends UpdatableRecord<R>> void testStoreWithOptimisticLock0(
        Table<R> table, TableField<R, Integer> id, TableField<R, String> string) throws Exception {

        DSLContext create = create(new Settings().withExecuteWithOptimisticLocking(true));

        // Storing without changing shouldn't execute any queries
        R record1 = create.fetchOne(table, id.equal(1));
        assertEquals(0, record1.store());
        assertEquals(0, record1.store());

        // Succeed if there are no concurrency issues
        record1.setValue(string, "New Title 1");
        assertEquals(1, record1.store());
        assertEquals("New Title 1", create.fetchOne(table, id.equal(1)).getValue(string));

        // Get new books
        R record2 = create.fetchOne(table, id.equal(1));
        R record3 = create.fetchOne(table, id.equal(1));

        // Still won't fail, but this will cause record3 to be stale
        record2.setValue(string, "New Title 2");
        assertEquals(1, record2.store());
        assertEquals("New Title 2", create.fetchOne(table, id.equal(1)).getValue(string));

        // Storing without changing shouldn't execute any queries
        assertEquals(0, record3.store());

        // This should fail as record3 is stale
        record3.setValue(string, "New Title 3");
        try {
            record3.store();
            fail();
        }
        catch (DataChangedException expected) {}
        assertEquals("New Title 2", create.fetchOne(table, id.equal(1)).getValue(string));

        // Refreshing first will work, though
        record3.refresh();
        record3.setValue(string, "New Title 3");
        assertEquals(1, record3.store());
        assertEquals("New Title 3", create.fetchOne(table, id.equal(1)).getValue(string));

        // Get new books
        R record4 = create.fetchOne(table, id.equal(1));
        R record5 = create.fetchOne(table, id.equal(1));

        // Delete the book
        assertEquals(1, record4.delete());

        // Storing without changing shouldn't execute any queries
        assertEquals(0, record5.store());

        // This should fail, as the database record no longer exists
        record5.setValue(string, "New Title 5");
        try {
            record5.store();
            fail();
        }
        catch (DataChangedException expected) {}

        // Restore the book, refresh the copy, then it should work
        assertEquals(1, record4.store());
        record5.refresh();
        record5.setValue(string, "New Title 5");
        assertEquals(1, record5.store());
        assertEquals("New Title 5", create.fetchOne(table, id.equal(1)).getValue(string));

        // Deleting the original should no longer be possible
        try {
            record4.delete();
            fail();
        }
        catch (DataChangedException expected) {}

        // Refreshing and deleting should work
        record4.refresh();
        assertEquals(1, record4.delete());

        // Now the other record cannot be deleted anymore
        try {
            record5.delete();
            fail();
        }
        catch (DataChangedException expected) {}
    }

    public void testStoreVsExecuteInsert() throws Exception {
        if (TIdentityPK() == null) {
            log.info("SKIPPING", "store() vs. executeInsert() tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        // [#2835] The two means of data insertion should show exactly the same behaviour
        // with respect to NULL values for identity columns.
        IPK i1 = create().newRecord(TIdentityPK());
        i1.setValue(TIdentityPK_VAL(), 1);
        assertEquals(1, create().executeInsert(i1));

        IPK i2 = create().newRecord(TIdentityPK());
        i2.setValue(TIdentityPK_VAL(), 1);
        assertEquals(1, i2.store());
    }

    public void testUpdatablesWithUpdatablePK() throws Exception {
        DSLContext create = create();
        create.configuration().settings().setUpdatablePrimaryKeys(true);

        // Create and insert
        T639 r1 = create.newRecord(T639());
        r1.setValue(T639_ID(), 1);
        assertEquals(1, (int) r1.getValue(T639_ID()));
        assertNull(r1.original(T639_ID()));
        assertTrue(r1.changed(T639_ID()));
        assertEquals(1, r1.store());
        assertEquals(1, (int) r1.getValue(T639_ID()));
        assertEquals(1, (int) r1.original(T639_ID()));
        assertFalse(r1.changed(T639_ID()));
        assertEquals(1, (int) create.select(T639_ID()).from(T639()).fetchOne().value1());

        // Refresh will not consider the new PK value
        r1.setValue(T639_ID(), 2);
        assertEquals(2, (int) r1.getValue(T639_ID()));
        assertEquals(1, (int) r1.original(T639_ID()));
        assertTrue(r1.changed(T639_ID()));
        r1.refresh();
        assertEquals(1, (int) r1.getValue(T639_ID()));
        assertEquals(1, (int) r1.original(T639_ID()));
        assertFalse(r1.changed(T639_ID()));

        // Update with store() will update to the new PK value
        r1.setValue(T639_ID(), 2);
        assertEquals(1, r1.store());
        assertEquals(2, (int) r1.getValue(T639_ID()));
        assertEquals(2, (int) r1.original(T639_ID()));
        assertFalse(r1.changed(T639_ID()));
        assertEquals(2, (int) create.select(T639_ID()).from(T639()).fetchOne().value1());

        // Force update with update() will update to the new PK value
        r1.setValue(T639_ID(), 3);
        assertEquals(1, r1.update());
        assertEquals(3, (int) r1.getValue(T639_ID()));
        assertEquals(3, (int) r1.original(T639_ID()));
        assertFalse(r1.changed(T639_ID()));
        assertEquals(3, (int) create.select(T639_ID()).from(T639()).fetchOne().value1());

        // Force insert with insert() will create a new record
        r1.setValue(T639_ID(), 4);
        assertEquals(1, r1.insert());
        assertEquals(4, (int) r1.getValue(T639_ID()));
        assertEquals(4, (int) r1.original(T639_ID()));
        assertFalse(r1.changed(T639_ID()));
        assertEquals(asList(3, 4), create.select(T639_ID()).from(T639()).orderBy(T639_ID()).fetch(T639_ID()));

        // Copy is not affected
        T639 r2 = r1.copy();
        assertNull(r2.getValue(T639_ID()));
        assertNull(r2.original(T639_ID()));
        assertFalse(r2.changed(T639_ID()));

        // Delete will not consider the new PK value
        r1.setValue(T639_ID(), 5);
        assertEquals(1, r1.delete());
        assertEquals(5, (int) r1.getValue(T639_ID()));
        assertEquals(4, (int) r1.original(T639_ID()));
        assertTrue(r1.changed(T639_ID()));
        assertEquals(3, (int) create.select(T639_ID()).from(T639()).fetchOne().value1());
    }
}
