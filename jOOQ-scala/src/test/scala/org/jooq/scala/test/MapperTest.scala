/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */
package org.jooq.scala.test

import collection.JavaConversions._
import org.scalatest.FunSuite
import org.jooq._
import org.jooq.impl._
import org.jooq.impl.DSL._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._
import java.sql.DriverManager
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.ArrayList
import org.jooq.scala.test.pojo.BookCaseWithConstructorProperties
import org.jooq.scala.test.pojo.BookCase

@RunWith(classOf[JUnitRunner])
class MapperTest extends FunSuite {

  def dsl() = {
    val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    val f = DSL.using(c, SQLDialect.H2);

    f;
  }

  test("RecordMapper") {
    val r =
    dsl().select(
            field("A", classOf[java.lang.Integer]),
            field("B", classOf[java.lang.Integer])
         )
         .from(
            values(
              row(1, 2),
              row(1, 3),
              row(2, 3)
            )
            as("T", "A", "B")
          )
         .fetch();

    val mapped1 = r.map((x1 : java.lang.Integer, x2 : java.lang.Integer) => (x1 + ", " + x2))
    val mapped2 = r.map((x : (java.lang.Integer, java.lang.Integer)) => (x._1 + ", " + x._2))

    assert(3 == mapped1.size(), "Size check")
    assert("1, 2" == mapped1.get(0), "Value 1")
    assert("1, 3" == mapped1.get(1), "Value 2")
    assert("2, 3" == mapped1.get(2), "Value 3")
    assert(mapped1 == mapped2, "Tuple vs ArgumentList")
  }

  test("MapCaseClass") {
    val books1 =
    dsl().select(T_BOOK.ID, T_BOOK.AUTHOR_ID, T_BOOK.TITLE)
         .from(
            values(
              row(1, 1, "1984"),
              row(2, 1, "Animal Farm"),
              row(3, 2, "O Alquimista"),
              row(4, 2, "Brida")
            )
            as("T_BOOK", "ID", "AUTHOR_ID", "TITLE"))
         .orderBy(T_BOOK.ID asc)
         .fetchInto(classOf[BookCase])

    assert(4 == books1.size());
    assert(BookCase(1, 1, "1984")         == books1.get(0));
    assert(BookCase(2, 1, "Animal Farm")  == books1.get(1));
    assert(BookCase(3, 2, "O Alquimista") == books1.get(2));
    assert(BookCase(4, 2, "Brida")        == books1.get(3));

    val books2 =
    dsl().select()
         .from(
            values(
              row(1, 1, "1984"),
              row(2, 1, "Animal Farm"),
              row(3, 2, "O Alquimista"),
              row(4, 2, "Brida")
            )
            as("T_BOOK", "ID", "AUTHOR_ID", "TITLE"))
         .orderBy(T_BOOK.ID asc)
         .fetchInto(classOf[BookCaseWithConstructorProperties])

    assert(4 == books2.size());
    assert(BookCaseWithConstructorProperties(1, 1, "1984")         == books2.get(0));
    assert(BookCaseWithConstructorProperties(2, 1, "Animal Farm")  == books2.get(1));
    assert(BookCaseWithConstructorProperties(3, 2, "O Alquimista") == books2.get(2));
    assert(BookCaseWithConstructorProperties(4, 2, "Brida")        == books2.get(3));
  }
}
