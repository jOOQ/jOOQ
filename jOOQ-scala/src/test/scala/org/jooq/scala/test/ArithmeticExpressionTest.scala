package org.jooq.scala.test

import collection.JavaConversions._
import org.scalatest.FunSuite
import org.jooq._
import org.jooq.impl._
import org.jooq.impl.Factory._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._

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

    assert("(t_book.id + t_book.author_id)" == add1.toString(), add1.toString())
    assert("(t_book.id + 2)"                == add2.toString(), add2.toString())
    assert("(t_book.id - t_book.author_id)" == sub1.toString(), sub1.toString())
    assert("(t_book.id - 2)"                == sub2.toString(), sub2.toString())
    assert("(t_book.id * t_book.author_id)" == mul1.toString(), mul1.toString())
    assert("(t_book.id * 2)"                == mul2.toString(), mul2.toString())
    assert("(t_book.id / t_book.author_id)" == div1.toString(), div1.toString())
    assert("(t_book.id / 2)"                == div2.toString(), div2.toString())
    assert("mod(t_book.id, t_book.author_id)" == mod1.toString(), mod1.toString())
    assert("mod(t_book.id, 2)"                == mod2.toString(), mod2.toString())

    // Check for the correct application of operator precedence
    val combined1 = T_BOOK.ID + T_BOOK.AUTHOR_ID * 2
    val combined2 = T_BOOK.ID * T_BOOK.AUTHOR_ID + 2

    assert("(t_book.id + (t_book.author_id * 2))" == combined1.toString(), combined1.toString())
    assert("((t_book.id * t_book.author_id) + 2)" == combined2.toString(), combined2.toString())
  }

  test("concat") {
    val cat1 = T_BOOK.TITLE || T_BOOK.TITLE
    val cat2 = T_BOOK.TITLE || " part 2"
    val cat3 = T_BOOK.TITLE || " part 2" || " and 3"

    assert("(t_book.title || t_book.title)"            == cat1.toString(), cat1.toString())
    assert("(t_book.title || ' part 2')"               == cat2.toString(), cat2.toString())
    assert("((t_book.title || ' part 2') || ' and 3')" == cat3.toString(), cat3.toString())
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

    assert("(t_book.id & t_book.author_id)"  == and1.toString(), and1.toString())
    assert("(t_book.id & 1)"                 == and2.toString(), and2.toString())
    assert("(t_book.id | t_book.author_id)"  == or1.toString(),  or1.toString())
    assert("(t_book.id | 1)"                 == or2.toString(),  or2.toString())
    assert("(t_book.id ^ t_book.author_id)"  == xor1.toString(), xor1.toString())
    assert("(t_book.id ^ 1)"                 == xor2.toString(), xor2.toString())
    assert("(t_book.id << t_book.author_id)" == shl1.toString(), shl1.toString())
    assert("(t_book.id << 1)"                == shl2.toString(), shl2.toString())
    assert("(t_book.id >> t_book.author_id)" == shr1.toString(), shr1.toString())
    assert("(t_book.id >> 1)"                == shr2.toString(), shr2.toString())
    assert("~(t_book.id)"                    == not1.toString(), not1.toString())
  }
}
