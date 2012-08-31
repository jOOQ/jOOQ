import java.sql.DriverManager
import org.jooq._
import org.jooq.impl._
import org.jooq.scala.example.h2.Tables._
import collection.JavaConversions._
import org.jooq.scala.Conversions._

object Test {
  def main(args: Array[String]): Unit = {
    val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    val f = new Factory(c, SQLDialect.H2);

    for (
      val r <- f
        select (
          T_BOOK.ID * T_BOOK.AUTHOR_ID,
          T_BOOK.ID + T_BOOK.AUTHOR_ID * 3 + 4,
          T_BOOK.TITLE || " abc" || " xy")
          from T_BOOK
          where (T_BOOK.ID === 3) fetch
    ) {

      println(r)
    }
  }
}