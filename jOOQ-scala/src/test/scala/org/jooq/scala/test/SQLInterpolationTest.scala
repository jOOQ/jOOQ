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
package org.jooq.scala.test

import java.sql.DriverManager

import org.jooq._
import org.jooq.impl.DSL
import org.jooq.impl.DSL._
import org.jooq.scala.Conversions._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.test.pojo.{BookCase, BookCaseWithConstructorProperties}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SQLInterpolationTest extends FunSuite {

  def dsl() = {
    val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    val f = DSL.using(c, SQLDialect.H2);

    f;
  }

  test("sql") {
    val a = 1;
    val b = 2;
    val c = value(a) plus value(b)

    val sql = sql"""SELECT ${a} AS "a", ${b} AS "b", ${c} AS "c""""
    val r1 = dsl fetch sql
    assert(Array("a", "b", "c") === r1.fields.map(f => f.getName), "Record field name check")
    assert(1 == r1.size(), "Result size check")
    assert(3 == r1.get(0).size(), "Record size check")
    assert(1 == r1.get(0).getValue(0), "Content check")
    assert(2 == r1.get(0).getValue(1), "Content check")
    assert(3 == r1.get(0).getValue(2), "Content check")

    val r2 = dsl fetch sql"""SELECT * FROM (${sql}) AS t"""
    assert(Array("a", "b", "c") === r2.fields.map(f => f.getName), "Record field name check")
    assert(1 == r2.size(), "Result size check")
    assert(3 == r2.get(0).size(), "Record size check")
    assert(1 == r2.get(0).getValue(0), "Content check")
    assert(2 == r2.get(0).getValue(1), "Content check")
    assert(3 == r2.get(0).getValue(2), "Content check")
  }

  test("resultQuery") {
    val a = 1;
    val b = 2;
    val c = value(a) plus value(b)

    val query = resultQuery"""SELECT ${a} AS "a", ${b} AS "b", ${c} AS "c""""
    val r1 = dsl fetch query
    assert(Array("a", "b", "c") === r1.fields.map(f => f.getName), "Record field name check")
    assert(1 == r1.size(), "Result size check")
    assert(3 == r1.get(0).size(), "Record size check")
    assert(1 == r1.get(0).getValue(0), "Content check")
    assert(2 == r1.get(0).getValue(1), "Content check")
    assert(3 == r1.get(0).getValue(2), "Content check")
  }

  test("condition") {
    val a = 1;
    val b = 2;

    val condition = condition"1 = $a AND 2 = $b"
    val r1 = dsl select(one as "a") where(condition) fetch()
    assert(Array("a") === r1.fields.map(f => f.getName), "Record field name check")
    assert(1 == r1.size(), "Result size check")
    assert(1 == r1.get(0).size(), "Record size check")
    assert(1 == r1.get(0).getValue(0), "Content check")
  }
}
