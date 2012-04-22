/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.table;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class CRUDTests<
    A    extends UpdatableRecord<A>,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public CRUDTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testInsertUpdateDelete() throws Exception {
        jOOQAbstractTest.reset = false;

        long timeIn = 0;
        long timeOut = -3600000;

        InsertQuery<A> i = create().insertQuery(TAuthor());
        i.addValue(TAuthor_ID(), 100);
        i.addValue(TAuthor_FIRST_NAME(), "Hermann");
        i.addValue(TAuthor_LAST_NAME(), "Hesse");
        i.addValue(TAuthor_DATE_OF_BIRTH(), new Date(timeIn));
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
        if (getDialect() != SQLITE)
            assertEquals(new Date(timeOut), author.getValue(TAuthor_DATE_OF_BIRTH()));

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

    @Test
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

        assertEquals(1, create().executeInsert(TAuthor(), author));
        author.refresh();
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        assertEquals(0, create().executeUpdate(TAuthor(), author, TAuthor_ID().equal(15)));
        author.setValue(TAuthor_FIRST_NAME(), "Erich");
        assertEquals(1, create().executeUpdate(TAuthor(), author, TAuthor_ID().equal(15)));
        author = create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Erich"));
        assertEquals(Integer.valueOf(15), author.getValue(TAuthor_ID()));
        assertEquals("Erich", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Kästner", author.getValue(TAuthor_LAST_NAME()));

        create().executeDelete(TAuthor(), TAuthor_LAST_NAME().equal("Kästner"));
        assertEquals(null, create().fetchOne(TAuthor(), TAuthor_FIRST_NAME().equal("Erich")));
    }

    @Test
    public void testRelations() throws Exception {
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "referentials test");
            return;
        }

        jOOQAbstractTest.reset = false;

        // Get the book 1984
        B book1984 = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        // Navigate to the book's author
        Record authorOrwell = (Record) invoke(book1984, "fetchTAuthorByAuthorId");
        assertEquals("Orwell", authorOrwell.getValue(TAuthor_LAST_NAME()));

        // Navigate back to the author's books
        List<?> books1 = (List<?>) invoke(authorOrwell, "fetchTBookListByAuthorId");
        assertEquals(2, books1.size());

        // Navigate through m:n relationships of books
        List<Object> booksToBookStores = new ArrayList<Object>();
        for (Object b : books1) {
            booksToBookStores.addAll((List<?>) invoke(b, "fetchTBookToBookStoreList"));
        }
        assertEquals(3, booksToBookStores.size());

        // Navigate to book stores
        Set<String> bookStoreNames = new TreeSet<String>();
        List<Object> bookStores = new ArrayList<Object>();
        for (Object b : booksToBookStores) {
            Object store = invoke(b, "fetchTBookStore");
            bookStores.add(store);
            bookStoreNames.add((String) invoke(store, "getName"));
        }
        assertEquals(Arrays.asList("Ex Libris", "Orell Füssli"), new ArrayList<String>(bookStoreNames));

        // Navigate through m:n relationships of book stores
        booksToBookStores = new ArrayList<Object>();
        for (Object b : bookStores) {
            booksToBookStores.addAll((List<?>) invoke(b, "fetchTBookToBookStoreList"));
        }

        // Navigate back to books
        Set<String> book2Names = new TreeSet<String>();
        List<Object> books2 = new ArrayList<Object>();
        for (Object b : booksToBookStores) {
            Object book = invoke(b, "fetchTBook");
            books2.add(book);
            book2Names.add((String) invoke(book, "getTitle"));
        }
        assertEquals(Arrays.asList("1984", "Animal Farm", "O Alquimista"), new ArrayList<String>(book2Names));

        // Navigate back to authors
        Set<String> authorNames = new TreeSet<String>();
        for (Object b : books2) {
            Object author = invoke(b, "fetchTAuthorByAuthorId");
            authorNames.add((String) invoke(author, "getLastName"));
        }
        assertEquals(Arrays.asList("Coelho", "Orwell"), new ArrayList<String>(authorNames));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
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
        Integer id = book1.getValue(TBook_ID());
        book1.setValue(TBook_TITLE(), "1985");
        assertEquals(1, book1.store());

        // Fetch the modified record
        book1 = create().fetchOne(TBook(), TBook_ID().equal(id));

        // Modify the record
        book1.setValue(TBook_TITLE(), "1999");
        assertEquals("1999", book1.getValue(TBook_TITLE()));

        // And refresh it again
        book1.refresh();
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals(0, book1.store());

        // Refresh the other copy of the original record
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1984", book2.getValue(TBook_TITLE()));
        book2.refresh();

        assertEquals(id, book1.getValue(TBook_ID()));
        assertEquals(id, book2.getValue(TBook_ID()));
        assertEquals("1985", book1.getValue(TBook_TITLE()));
        assertEquals("1985", book2.getValue(TBook_TITLE()));

        // No ON DELETE CASCADE constraints for Sybase ASE
        if (getDialect() == SQLDialect.ASE) {
            create().truncate((Table) table("t_book_to_book_store")).execute();
        }

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

    @Test
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

    @Test
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

        switch (getDialect()) {
            // Sybase ASE and SQL server do not allow for explicitly setting
            // values on IDENTITY columns
            case ASE:
            case SQLSERVER:
                log.info("SKIPPING", "Storing previously deleted UpdatableRecords");
                break;

            default:
                store.store();
                assertEquals("Amazon", store.getValue(TBookStore_NAME()));

                store.refresh();
                assertEquals("Amazon", store.getValue(TBookStore_NAME()));
        }

        store = create().fetchOne(TBookStore(), TBookStore_NAME().equal("Rösslitor"));
        assertEquals("Rösslitor", store.getValue(TBookStore_NAME()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonUpdatables() throws Exception {
        jOOQAbstractTest.reset = false;

        // Insert three records first
        T785 record = create().newRecord(T785());
        record.setValue(T785_ID(), 1);
        assertEquals(1, record.storeUsing(T785_ID()));
        assertEquals(0, record.storeUsing(T785_ID()));

        record.setValue(T785_ID(), 2);
        assertEquals(1, record.storeUsing(T785_ID()));
        record.setValue(T785_NAME(), "N");
        record.setValue(T785_VALUE(), "V");
        assertEquals(1, record.storeUsing(T785_ID()));

        record = create().newRecord(T785());
        record.setValue(T785_ID(), 3);
        record.setValue(T785_NAME(), "N");
        assertEquals(1, record.storeUsing(T785_ID()));
        assertEquals(0, record.storeUsing(T785_ID()));

        // Load data again
        record = create().newRecord(T785());
        record.setValue(T785_ID(), 2);
        record.refreshUsing(T785_ID());
        assertEquals("N", record.getValue(T785_NAME()));
        assertEquals("V", record.getValue(T785_VALUE()));

        // When NAME is used as the key, multiple updates may occur
        record.setValue(T785_VALUE(), "Some value");
        assertEquals(2, record.storeUsing(T785_NAME()));
        assertEquals(2, create().fetch(T785(), T785_VALUE().equal("Some value")).size());

        // Don't allow refreshing on multiple results
        try {
            record = create().newRecord(T785());
            record.setValue(T785_VALUE(), "Some value");
            record.refreshUsing(T785_VALUE());
            fail();
        }
        catch (InvalidResultException expected) {}


        // Don't allow refreshing on inexistent results
        try {
            record = create().newRecord(T785());
            record.setValue(T785_ID(), 4);
            record.refreshUsing(T785_ID());
            fail();
        }
        catch (InvalidResultException expected) {}

        // Delete records again
        record = create().newRecord(T785());
        record.setValue(T785_ID(), 1);
        assertEquals(1, record.deleteUsing(T785_ID()));
        assertEquals(2, create().fetch(T785()).size());
        assertEquals(0, create().fetch(T785(), T785_ID().equal(1)).size());

        record = create().newRecord(T785());
        record.setValue(T785_NAME(), "N");
        assertEquals(2, record.deleteUsing(T785_NAME()));
        assertEquals(0, create().fetch(T785()).size());
    }
}
