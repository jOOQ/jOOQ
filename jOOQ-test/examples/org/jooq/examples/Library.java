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
package org.jooq.examples;

import static org.jooq.test.mysql.generatedclasses.tables.TAuthor.T_AUTHOR;
import static org.jooq.test.mysql.generatedclasses.tables.TBook.T_BOOK;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.impl.Factory;
import org.jooq.test.mysql.generatedclasses.enums.TBookStatus;
import org.jooq.test.mysql.generatedclasses.enums.TLanguage;
import org.jooq.test.mysql.generatedclasses.tables.TAuthor;
import org.jooq.test.mysql.generatedclasses.tables.TBook;
import org.jooq.test.mysql.generatedclasses.tables.records.TAuthorRecord;
import org.jooq.test.mysql.generatedclasses.tables.records.TBookRecord;

public class Library {

    private static Factory create() throws Exception {
        return new Factory(getConnection(), SQLDialect.MYSQL);
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
		SelectQuery q = create().selectQuery();
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

			for (TBookRecord book : record.fetchTBookListByAuthorId()) {
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
                .where(TAuthor.ID.in(create().selectDistinct(TBook.AUTHOR_ID)
                    .from(T_BOOK).where(TBook.STATUS.equal(TBookStatus.SOLD_OUT)))).fetch()) {

            System.out.println("Author : " + record.getFirstName() + " " + record.getLastName() + " has sold out books");
        }

        for (TAuthorRecord record : create().selectFrom(T_AUTHOR)
                .where(TAuthor.ID.in(create().selectDistinct(TBook.AUTHOR_ID)
                    .from(T_BOOK).where(TBook.LANGUAGE_ID.in(TLanguage.en, TLanguage.pt)))).fetch()) {

            System.out.println("Author : " + record.getFirstName() + " " + record.getLastName() + " have english or portuguese books");
        }

        Select<?> union =
            create().select(TBook.TITLE, TBook.AUTHOR_ID).from(T_BOOK).where(TBook.PUBLISHED_IN.greaterThan(1990)).union(
            create().select(TBook.TITLE, TBook.AUTHOR_ID).from(T_BOOK).where(TBook.AUTHOR_ID.equal(1)));

        System.out.println(
          create().select(union.getField(TBook.TITLE))
                .from(union)
                .orderBy(union.getField(TBook.AUTHOR_ID).desc()));
        System.out.println(
            create().select(union.getField(TBook.TITLE),union.getField(TBook.AUTHOR_ID))
            .from(union)
            .orderBy(union.getField(TBook.AUTHOR_ID).desc()).fetch());

    }
}
