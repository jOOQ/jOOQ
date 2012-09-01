package org.jooq.scala.test

import collection.JavaConversions._
import org.scalatest.FunSuite
import org.jooq._
import org.jooq.impl._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._
import org.jooq.conf.Settings
import javax.xml.bind.JAXB
import org.jooq.conf.SettingsTools

class OperatorOverloadingTest extends FunSuite {

  test("arithmetic") {
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
}