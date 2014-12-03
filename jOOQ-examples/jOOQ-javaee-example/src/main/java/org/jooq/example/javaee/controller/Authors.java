/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
import org.jooq.example.db.h2.tables.records.AuthorRecord;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.example.javaee.ejb.AuthorsEJB;
import org.jooq.impl.DSL;

/**
 * A bean to be used from JSF pages.
 *
 * @author Lukas Eder
 */
@Named("authors")
@SessionScoped
public class Authors implements Serializable {

    private static final long           serialVersionUID = 1L;

    @EJB
    private AuthorsEJB                  ejb;

    // Caches from the DB
    private AuthorRecord                newAuthor;
    private BookRecord                  newBook;
    private Record                      edit;
    private Map<Table<?>, SortField<?>> sort             = Stream.of(AUTHOR.ID, BOOK.ID).collect(
                                                             toMap(f -> f.getTable(), f -> f.asc()));

    private Result<AuthorRecord>        authors;
    private Result<AuthorRecord>        authorsAlphanumeric;
    private Result<BookRecord>          books;

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

    public Map<String, Field<?>> getAuthorColumns() {
        return getColumns(AUTHOR);
    }

    public Result<BookRecord> getBooks() {
        if (books == null)
            books = ejb.fetchBooks(sort.get(BOOK));

        return books;
    }

    public Map<String, Field<?>> getBookColumns() {
        return getColumns(BOOK);
    }

    private Map<String, Field<?>> getColumns(Table<?> t) {
        return Stream.of(t.fields()).collect(toMap(f -> f.getName(), f -> f));
    }

    public Record getEdit() {
        return edit;
    }

    public Map<String, SortField<?>> getSort() {
        return sort.entrySet().stream().collect(toMap(e -> e.getKey().getName(), e -> e.getValue()));
    }

    public AuthorRecord getNewAuthor() {
        if (newAuthor == null)
            newAuthor = ejb.newAuthor();

        return newAuthor;
    }

    public BookRecord getNewBook() {
        if (newBook == null)
            newBook = ejb.newBook();

        return newBook;
    }

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

    public void edit(UpdatableRecord<?> record) {
        edit = record;
    }

    public void save(UpdatableRecord<?> author) {
        ejb.store(author);
        reset();
    }

    public void delete(UpdatableRecord<?> author) {
        ejb.delete(author);
        reset();
    }

    private void reset() {
        authors = null;
        authorsAlphanumeric = null;
        books = null;
        edit = null;
        newAuthor = ejb.newAuthor();
        newBook = ejb.newBook();
    }
}
