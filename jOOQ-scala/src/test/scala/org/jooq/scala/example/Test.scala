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
package org.jooq.scala.example

import org.jooq.scala.Conversions

import collection.JavaConversions._

import java.sql.DriverManager

import org.jooq._
import org.jooq.impl.DSL
import org.jooq.impl.DSL._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._

object Test {
  def main(args: Array[String]): Unit = {
    val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    val f = DSL.using(c, SQLDialect.H2);
    val x = T_AUTHOR as "x"

    for (r <- f
        select (
          T_BOOK.ID * T_BOOK.AUTHOR_ID,
          T_BOOK.ID + T_BOOK.AUTHOR_ID * 3 + 4,
          T_BOOK.TITLE || " abc" || " xy")
        from T_BOOK
        leftOuterJoin (
          f select (x.ID, x.YEAR_OF_BIRTH)
          from x
          limit 1
          asTable x.getName()
        )
        on T_BOOK.AUTHOR_ID === x.ID
        where (T_BOOK.ID <> 2)
        or (T_BOOK.TITLE in ("O Alquimista", "Brida"))
        fetch
    ) {

      println(r)
    }

    // Tuple assignment
    val tuple =
    f fetchOne (
      select (
        T_AUTHOR.FIRST_NAME,
        T_AUTHOR.LAST_NAME
      )
      from T_AUTHOR
      where T_AUTHOR.ID === 1
    )


    println("first name : " + tuple._1);
    println("last name : " + tuple._2);

    val predicate =
      if (1 == 1)
        "AND a.id = 1"
      else
        ""

    // Plain SQL
    println(f fetch (
      s"""
      SELECT a.first_name, a.last_name, b.title
      FROM t_author a
      JOIN t_book b ON a.id = b.author_id
      WHERE 1 = 1 $predicate
      ORDER BY a.id, b.id
      """)
    )

    // Option conversions
    for (i <- 1 to 5) {
      f.select (T_BOOK.TITLE)
        .from (T_BOOK)
        .where (T_BOOK.ID === i)
        .fetchOneOption() match {
          case Some(record) =>
            println("Book found: " + record.value1)
          case None =>
            println("No book found for ID: " + i)
        }
    }

    for (i <- 1 to 3) {
      f fetchOneOption (
          select (T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
          .from (T_AUTHOR)
          .where (T_AUTHOR.ID === i)
        ) match {
          case Some(record) =>
            println("Author found: " + record.value1 + " " + record.value2)
          case None =>
            println("No author found for ID: " + i)
        }
    }
  }
}