/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.demo;

import static org.jooq.demo.sakila.Tables.*;
import static org.jooq.impl.DSL.*;

import org.jooq.*;
import org.jooq.demo.sakila.tables.*;
import org.jooq.demo.sakila.tables.records.*;
import org.jooq.impl.*;

import org.junit.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Demo_SELECT extends Demo {

    // @Test
    public void SELECT_FROM_ACTOR() {
        // 1. JOIN FILM_ACTOR, FILM
        // 2. Create a Table Alias
        // 3. GROUP BY ACTOR_ID, FIRST_NAME, LAST_NAME
        //    SELECT FIRST_NAME, LAST_NAME, count(*)
        // 4. HAVING count(*) > 30
        // 5. ORDER BY count(*) DESC
        // 6. JOIN FILM_CATEGORY, CATEGORY
        // 7. GROUP_CONCAT category names

        Actor a = ACTOR.as("a");
        FilmActor fa = FILM_ACTOR.as("fa");

        dsl().select(
                a.FIRST_NAME,
                a.LAST_NAME,
                count(),
                groupConcatDistinct(CATEGORY.NAME).separator(", "))
             .from(a)
             .join(FILM_ACTOR)
             .on(FILM_ACTOR.ACTOR_ID.eq(a.ACTOR_ID))
             .join(FILM)
             .on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
             .join(FILM_CATEGORY)
             .on(FILM_CATEGORY.FILM_ID.eq(FILM.FILM_ID))
             .join(CATEGORY)
             .on(CATEGORY.CATEGORY_ID.eq(FILM_CATEGORY.CATEGORY_ID))
             .groupBy(a.ACTOR_ID, a.FIRST_NAME, a.LAST_NAME)
             .having(count().gt(40))
             .orderBy(count().desc())
             .fetch();

//      SELECT
//        a.first_name,
//        a.last_name,
//        count(*),
//        group_concat(DISTINCT category.name ORDER BY category.name ASC SEPARATOR ', ')
//      FROM actor AS a
//        JOIN film_actor AS fa
//        ON fa.actor_id = a.actor_id
//        JOIN film AS f
//        ON f.film_id = fa.film_id
//        JOIN film_category
//        ON film_category.film_id = f.film_id
//        JOIN category
//        ON film_category.category_id = category.category_id
//      GROUP BY
//        a.actor_id,
//        a.first_name,
//        a.last_name
//      HAVING count(*) > 35
//      ORDER BY count(*) DESC

    }

    @Test
    public void SELECT_FROM_ACTOR_UNION_CUSTOMER_UNION_STAFF() {
        System.out.println(
        dsl().select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
             .from(ACTOR)
             .union(
              select(CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME)
             .from(CUSTOMER)
             )
             .fetch());

//    SELECT
//      actor.first_name,
//      actor.last_name
//    FROM actor
//    UNION
//    SELECT
//      customer.first_name,
//      customer.last_name
//    FROM customer
//    UNION
//    SELECT
//      staff.first_name,
//      staff.last_name
//    FROM staff
    }

    // @Test
    public void SELECT_CUSTOMERS_WHO_ARE_ACTORS() {

        dsl().select(CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME)
             .from(CUSTOMER)
             .where(row(CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME)
                 .in(select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                     .from(ACTOR)))
             .fetch();

//      SELECT
//        customer.first_name,
//        customer.last_name
//      FROM customer
//      WHERE (customer.first_name, customer.last_name) IN (
//        SELECT
//          actor.first_name,
//          actor.last_name
//        FROM actor
//      )
    }

    // @Test
    public void SELECT_GET_CUSTOMER_BALANCE() {
        dsl().select(...)
             .from(CUSTOMER)
             .orderBy(3)
             .fetch();

//      SELECT
//        customer.first_name,
//        customer.last_name,
//        get_customer_balance(
//          customer.customer_id,
//          current_timestamp()
//        )
//      FROM customer
//      ORDER BY 3 ASC
    }
}
