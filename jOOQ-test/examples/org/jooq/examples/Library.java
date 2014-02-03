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
package org.jooq.examples;

import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.test.mysql.generatedclasses.Keys.FK_T_BOOK_AUTHOR_ID;
import static org.jooq.test.mysql.generatedclasses.Tables.T_LANGUAGE;
import static org.jooq.test.mysql.generatedclasses.tables.TAuthor.T_AUTHOR;
import static org.jooq.test.mysql.generatedclasses.tables.TBook.T_BOOK;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.jooq.test.mysql.generatedclasses.enums.TBookStatus;
import org.jooq.test.mysql.generatedclasses.tables.TAuthor;
import org.jooq.test.mysql.generatedclasses.tables.TBook;
import org.jooq.test.mysql.generatedclasses.tables.TLanguage;
import org.jooq.test.mysql.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookRecord;

public class Library {

    private static DSLContext create() throws Exception {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }

	public static void main(String[] args) throws Exception {
		System.out.println("First run...");
		firstRun();

		System.out.println();
		System.out.println("Second run...");
		secondRun();

        System.out.println();
        System.out.println("Third run...");
        thirdRun();

        System.out.println();
        System.out.println("Fourth run...");
        fourthRun();
	}

	protected static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection ("jdbc:mysql://localhost/test", "root", "");
	}

	/**
	 * Run this code providing your own database connection.
	 */
	public static void firstRun() throws Exception {
		// Create the query
		SelectQuery<?> q = create().selectQuery();
		q.addFrom(T_AUTHOR);
		q.addJoin(T_BOOK, TAuthor.ID.equal(TBook.AUTHOR_ID));
		q.addConditions(TAuthor.YEAR_OF_BIRTH.greaterThan(1920));
		q.addConditions(TAuthor.FIRST_NAME.equal("Paulo"));
		q.addOrderBy(TBook.TITLE);

		// Execute the query and fetch the results
		q.execute();
		Result<?> result = q.getResult();

		// Loop over the resulting records
		for (Record record : result) {

			// Type safety assured with generics
			String firstName = record.getValue(TAuthor.FIRST_NAME);
			String lastName = record.getValue(TAuthor.LAST_NAME);
			String title = record.getValue(TBook.TITLE);
			Integer publishedIn = record.getValue(TBook.PUBLISHED_IN);

			System.out.println(title + " (published in " + publishedIn + ") by " + firstName + " " + lastName);
		}
	}

	/**
	 * Run this code providing your own database connection.
	 */
	public static void secondRun() throws Exception {
	    // Execute the query and fetch the results
	    Result<Record> result = create().select()
	        .from(T_AUTHOR)
	        .join(T_BOOK).on(TAuthor.ID.equal(TBook.AUTHOR_ID))
	        .where(TAuthor.YEAR_OF_BIRTH.greaterThan(1920)
	        .and(TAuthor.FIRST_NAME.equal("Paulo")))
	        .orderBy(TBook.TITLE).fetch();

		// Loop over the resulting records
		for (Record record : result) {

			// Type safety assured with generics
			String firstName = record.getValue(TAuthor.FIRST_NAME);
			String lastName = record.getValue(TAuthor.LAST_NAME);
			String title = record.getValue(TBook.TITLE);
			Integer publishedIn = record.getValue(TBook.PUBLISHED_IN);

			System.out.println(title + " (published in " + publishedIn + ") by " + firstName + " " + lastName);
		}
	}

	/**
	 * Run this code providing your own database connection.
	 */
	public static void thirdRun() throws Exception {
	    // Execute the query and fetch the results
	    Result<TAuthorRecord> result = create().selectFrom(T_AUTHOR)
            .where(TAuthor.YEAR_OF_BIRTH.greaterThan(1920)
            .and(TAuthor.FIRST_NAME.equal("Paulo")))
            .orderBy(TAuthor.LAST_NAME).fetch();

		// Loop over the resulting records
		for (TAuthorRecord record : result) {

			// Type safety assured with generics
			String firstName = record.getFirstName();
			String lastName = record.getLastName();

			System.out.println("Author : " + firstName + " " + lastName + " wrote : ");

			for (TBookRecord book : record.fetchChildren(FK_T_BOOK_AUTHOR_ID)) {
			    System.out.println("  Book : " + book.getTitle());
			}
		}
	}

    public static void fourthRun() throws Exception {
        // Select authors with books that are sold out
        // SELECT *
        //   FROM T_AUTHOR
        //  WHERE T_AUTHOR.ID IN (SELECT DISTINCT T_BOOK.AUTHOR_ID
        //                          FROM T_BOOK
        //                         WHERE T_BOOK.STATUS = 'SOLD OUT');

        for (TAuthorRecord record : create().selectFrom(T_AUTHOR)
                .where(TAuthor.ID.in(selectDistinct(TBook.AUTHOR_ID)
                    .from(T_BOOK).where(TBook.STATUS.equal(TBookStatus.SOLD_OUT)))).fetch()) {

            System.out.println("Author : " + record.getFirstName() + " " + record.getLastName() + " has sold out books");
        }

        for (TAuthorRecord record : create().selectFrom(T_AUTHOR)
                .where(TAuthor.ID.in(
                     selectDistinct(TBook.AUTHOR_ID)
                    .from(T_BOOK)
                    .where(TBook.LANGUAGE_ID.in(
                        select(TLanguage.ID)
                        .from(T_LANGUAGE)
                        .where(TLanguage.CD.in("pt", "en"))
                    )))).fetch()) {

            System.out.println("Author : " + record.getFirstName() + " " + record.getLastName() + " has english or portuguese books");
        }

        Select<?> union =
            create().select(TBook.TITLE, TBook.AUTHOR_ID).from(T_BOOK).where(TBook.PUBLISHED_IN.greaterThan(1990)).union(
            create().select(TBook.TITLE, TBook.AUTHOR_ID).from(T_BOOK).where(TBook.AUTHOR_ID.equal(1)));

        System.out.println(
          create().select(union.field(TBook.TITLE))
                .from(union)
                .orderBy(union.field(TBook.AUTHOR_ID).desc()));
        System.out.println(
            create().select(union.field(TBook.TITLE),union.fieldsRow().field(TBook.AUTHOR_ID))
            .from(union)
            .orderBy(union.field(TBook.AUTHOR_ID).desc()).fetch());

    }
}
