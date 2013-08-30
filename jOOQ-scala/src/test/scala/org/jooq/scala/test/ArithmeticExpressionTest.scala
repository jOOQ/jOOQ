/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.scala.test

import collection.JavaConversions._
import org.scalatest.FunSuite
import org.jooq._
import org.jooq.impl._
import org.jooq.impl.DSL._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArithmeticExpressionTest extends FunSuite {

  test("arithmetic expressions") {
    val add1 = T_BOOK.ID + T_BOOK.AUTHOR_ID
    val add2 = T_BOOK.ID + 2
    val sub1 = T_BOOK.ID - T_BOOK.AUTHOR_ID
    val sub2 = T_BOOK.ID - 2
    val mul1 = T_BOOK.ID * T_BOOK.AUTHOR_ID
    val mul2 = T_BOOK.ID * 2
    val div1 = T_BOOK.ID / T_BOOK.AUTHOR_ID
    val div2 = T_BOOK.ID / 2
    val mod1 = T_BOOK.ID % T_BOOK.AUTHOR_ID
    val mod2 = T_BOOK.ID % 2
    val neg1 = -T_BOOK.ID

    assert("""("PUBLIC"."T_BOOK"."ID" + "PUBLIC"."T_BOOK"."AUTHOR_ID")"""   == add1.toString(), add1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" + 2)"""                               == add2.toString(), add2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" - "PUBLIC"."T_BOOK"."AUTHOR_ID")"""   == sub1.toString(), sub1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" - 2)"""                               == sub2.toString(), sub2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" * "PUBLIC"."T_BOOK"."AUTHOR_ID")"""   == mul1.toString(), mul1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" * 2)"""                               == mul2.toString(), mul2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" / "PUBLIC"."T_BOOK"."AUTHOR_ID")"""   == div1.toString(), div1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" / 2)"""                               == div2.toString(), div2.toString())
    assert("""mod("PUBLIC"."T_BOOK"."ID", "PUBLIC"."T_BOOK"."AUTHOR_ID")""" == mod1.toString(), mod1.toString())
    assert("""mod("PUBLIC"."T_BOOK"."ID", 2)"""                             == mod2.toString(), mod2.toString())
    assert("""-("PUBLIC"."T_BOOK"."ID")"""                                  == neg1.toString(), neg1.toString())

    // Check for the correct application of operator precedence
    val combined1 = T_BOOK.ID + T_BOOK.AUTHOR_ID * 2
    val combined2 = T_BOOK.ID * T_BOOK.AUTHOR_ID + 2

    assert("""("PUBLIC"."T_BOOK"."ID" + ("PUBLIC"."T_BOOK"."AUTHOR_ID" * 2))""" == combined1.toString(), combined1.toString())
    assert("""(("PUBLIC"."T_BOOK"."ID" * "PUBLIC"."T_BOOK"."AUTHOR_ID") + 2)""" == combined2.toString(), combined2.toString())
  }

  test("bitwise") {
    val and1 = T_BOOK.ID & T_BOOK.AUTHOR_ID
    val and2 = T_BOOK.ID & 1
    val or1  = T_BOOK.ID | T_BOOK.AUTHOR_ID
    val or2  = T_BOOK.ID | 1
    val xor1 = T_BOOK.ID ^ T_BOOK.AUTHOR_ID
    val xor2 = T_BOOK.ID ^ 1
    val shl1 = T_BOOK.ID << T_BOOK.AUTHOR_ID
    val shl2 = T_BOOK.ID << 1
    val shr1 = T_BOOK.ID >> T_BOOK.AUTHOR_ID
    val shr2 = T_BOOK.ID >> 1
    val not1 = ~T_BOOK.ID

    assert("""("PUBLIC"."T_BOOK"."ID" & "PUBLIC"."T_BOOK"."AUTHOR_ID")"""  == and1.toString(), and1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" & 1)"""                              == and2.toString(), and2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" | "PUBLIC"."T_BOOK"."AUTHOR_ID")"""  == or1.toString(),  or1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" | 1)"""                              == or2.toString(),  or2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" ^ "PUBLIC"."T_BOOK"."AUTHOR_ID")"""  == xor1.toString(), xor1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" ^ 1)"""                              == xor2.toString(), xor2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" << "PUBLIC"."T_BOOK"."AUTHOR_ID")""" == shl1.toString(), shl1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" << 1)"""                             == shl2.toString(), shl2.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" >> "PUBLIC"."T_BOOK"."AUTHOR_ID")""" == shr1.toString(), shr1.toString())
    assert("""("PUBLIC"."T_BOOK"."ID" >> 1)"""                             == shr2.toString(), shr2.toString())
    assert("""~("PUBLIC"."T_BOOK"."ID")"""                                 == not1.toString(), not1.toString())
  }

  test("concat") {
    // [32597] TODO: Reactivate this test
//    val cat1 = T_BOOK.TITLE || T_BOOK.TITLE
//    val cat2 = T_BOOK.TITLE || " part 2"
//    val cat3 = T_BOOK.TITLE || " part 2" || " and 3"
//
//    assert("""("PUBLIC"."T_BOOK"."TITLE" || "PUBLIC"."T_BOOK"."TITLE")"""            == cat1.toString(), cat1.toString())
//    assert("""("PUBLIC"."T_BOOK"."TITLE" || ' part 2')"""                            == cat2.toString(), cat2.toString())
//    assert("""(("PUBLIC"."T_BOOK"."TITLE" || ' part 2') || ' and 3')"""              == cat3.toString(), cat3.toString())
  }
}
