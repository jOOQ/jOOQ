package org.jooq.example.flyway;

import org.jooq.example.flyway.s.db.h2.Tables._;

import org.jooq._;
import org.jooq.impl.DSL;
import org.jooq.impl.DSL._;

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * @author Lukas Eder
 */
@RunWith(classOf[JUnitRunner])
class ScalaPostgresTest extends FunSuite {

  def dsl() = {
    val c = java.sql.DriverManager.getConnection("jdbc:h2:~/flyway-test", "sa", "");
    val f = DSL.using(c, SQLDialect.H2);

    f;
  }

  test("testQueryingAfterMigration") {
    val result = dsl()
      .select(
        AUTHOR.FIRST_NAME,
        AUTHOR.LAST_NAME,
        BOOK.ID,
        BOOK.TITLE
      )
      .from (AUTHOR)
      .join (BOOK)
      .on (AUTHOR.ID eq BOOK.AUTHOR_ID)
      .orderBy (BOOK.ID asc)
      .fetch

    assert(4 == result.size)
    assert(1 == result.get(0).get(BOOK.ID))
    assert(2 == result.get(1).get(BOOK.ID))
    assert(3 == result.get(2).get(BOOK.ID))
    assert(4 == result.get(3).get(BOOK.ID))
  }
}
