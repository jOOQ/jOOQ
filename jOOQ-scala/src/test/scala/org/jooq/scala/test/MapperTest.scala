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

@RunWith(classOf[JUnitRunner])
class MapperTest extends FunSuite {

  test("RecordMapper") {
    val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    val f = DSL.using(c, SQLDialect.H2);

    val r =
    f.select(
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

}
