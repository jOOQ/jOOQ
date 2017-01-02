/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.example.javaee.controller;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SortOrder.ASC;
import static org.jooq.example.db.h2.tables.Author.AUTHOR;
import static org.jooq.example.db.h2.tables.Book.BOOK;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.jooq.example.db.h2.tables.Author;
import org.jooq.example.db.h2.tables.records.AuthorRecord;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.example.javaee.ejb.LibraryEJB;
import org.jooq.impl.DSL;

/**
 * A bean to be used from JSF pages.
 * <p>
 * The bean is session scoped such that we can have session-based caches of
 * database content such as authors or books in this bean. In this simple
 * example, we haven't gone into concurrency situations where multiple sessions
 * update the library database at the same time, in case of which we might need
 * to turn on optimistic locking in jOOQ.
 *
 * @author Lukas Eder
 */
@Named("library")
@SessionScoped
public class Library implements Serializable {

    private static final long           serialVersionUID = 1L;

    @EJB
    private LibraryEJB                  ejb;

    // Various caches from the DB
    // -------------------------------------------------------------------------

    /**
     * An empty {@link AuthorRecord} that can be used to insert new authors.
     */
    private AuthorRecord                newAuthor;

    /**
     * An empty {@link BookRecord} that can be used to insert new books.
     */
    private BookRecord                  newBook;

    /**
     * The reference to the {@link Record} that is currently being edited.
     */
    private Record                      edit;

    /**
     * The sort field for each {@link Table}.
     */
    private Map<Table<?>, SortField<?>> sort             = Stream.of(AUTHOR.ID, BOOK.ID).collect(
                                                             toMap(f -> f.getTable(), f -> f.asc()));

    /**
     * A cache of all {@link AuthorRecord}s currently in the database.
     */
    private Result<AuthorRecord>        authors;

    /**
     * A copy of {@link #authors} that is always sorted alphanumerically.
     */
    private Result<AuthorRecord>        authorsAlphanumeric;

    /**
     * A cache of all {@link BookRecord}s currently in the database.
     */
    private Result<BookRecord>          books;

    // Data access methods
    // -------------------------------------------------------------------------

    public Result<AuthorRecord> getAuthors() {
        if (authors == null)
            authors = ejb.fetchAuthors(sort.get(AUTHOR));

        return authors;
    }

    public Result<AuthorRecord> getAuthorsAlphanumeric() {
        if (authorsAlphanumeric == null) {
            authorsAlphanumeric = DSL.using(H2).newResult(AUTHOR);
            authorsAlphanumeric.addAll(getAuthors());
            authorsAlphanumeric.sortAsc(comparing(a -> a.getFirstName() + " " + a.getLastName()));
        }

        return authorsAlphanumeric;
    }

    public Map<Integer, AuthorRecord> getAuthorById() {
        return getAuthors().intoMap(AUTHOR.ID);
    }

    public Result<BookRecord> getBooks() {
        if (books == null)
            books = ejb.fetchBooks(sort.get(BOOK));

        return books;
    }

    public AuthorRecord getNewAuthor() {
        if (newAuthor == null)
            newAuthor = DSL.using(H2).newRecord(AUTHOR);

        return newAuthor;
    }

    public BookRecord getNewBook() {
        if (newBook == null)
            newBook = DSL.using(H2).newRecord(BOOK);

        return newBook;
    }

    // UI state methods
    // -------------------------------------------------------------------------

    /**
     * The record being edited.
     */
    public Record getEdit() {
        return edit;
    }

    /**
     * A map containing <code>column name -> column</code> pairs of the
     * {@link Author} table.
     */
    public Map<String, Field<?>> getAuthorColumns() {
        return getColumns(AUTHOR);
    }

    /**
     * A map containing <code>column name -> column</code> pairs of the
     * {@link Author} table.
     */
    public Map<String, Field<?>> getBookColumns() {
        return getColumns(BOOK);
    }

    /**
     * Get a map containing <code>table name -> sort field</code> pairs.
     */
    public Map<String, SortField<?>> getSort() {
        return sort.entrySet().stream().collect(toMap(e -> e.getKey().getName(), e -> e.getValue()));
    }

    private Map<String, Field<?>> getColumns(Table<?> t) {
        return Stream.of(t.fields()).collect(toMap(f -> f.getName(), f -> f));
    }

    private void reset() {
        authors = null;
        authorsAlphanumeric = null;
        books = null;
        edit = null;
    }

    // Actions
    // -------------------------------------------------------------------------

    /**
     * Sort a table by a new field.
     */
    public void sort(TableField<?, ?> field) {
        SortField<?> previous = sort.get(field.getTable());
        sort.put(field.getTable(),
            ! previous.getName().equals(field.getName())
            ? field.asc()
            : previous.getOrder() == ASC
            ? field.desc()
            : field.asc()
        );

        reset();
    }

    /**
     * Mark a record as the one being currently edited.
     */
    public void edit(UpdatableRecord<?> record) {
        edit = record;
    }

    /**
     * Save a record back to the database.
     */
    public void save(UpdatableRecord<?> author) {
        ejb.store(author);
        reset();
    }

    /**
     * Delete a record from the database.
     */
    public void delete(UpdatableRecord<?> author) {
        ejb.delete(author);
        reset();
    }
}
