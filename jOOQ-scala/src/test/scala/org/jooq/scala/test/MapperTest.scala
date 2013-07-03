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
