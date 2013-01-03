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
package org.jooq.scala.example

import collection.JavaConversions._

import java.sql.DriverManager

import org.jooq._
import org.jooq.impl._
import org.jooq.impl.Factory._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._

object Test {
  def main(args: Array[String]): Unit = {
    val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    val f = new Executor(c, SQLDialect.H2);
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