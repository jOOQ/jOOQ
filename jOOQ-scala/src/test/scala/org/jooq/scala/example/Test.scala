/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.scala.example

import collection.JavaConversions._

import java.sql.DriverManager

import org.jooq._
import org.jooq.impl._
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
  }
}