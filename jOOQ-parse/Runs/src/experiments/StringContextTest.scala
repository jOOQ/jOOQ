package experiments

import java.sql.{Date, Timestamp}

import experiments.Macros._
import org.jooq.{QueryPart, SQLDialect}
import org.jooq.conf.Settings
import org.jooq.impl.{DefaultConfiguration, DSL}

import scala.collection.JavaConversions

object StringContextTest {

  def main(args: Array[String]) {
    //val rve = sql"""(1, 2)"""
    //val predicate = sql"""$rve not in (select 3, 4)"""
    val query =
      sql"""
         select first_name, last_name, title
         from
           (select 1),
         /*(select 2) x,
           (SELECT 3) as y,*/
           [author]
             join (book cross join hello) on author . id =  "book".author_id where
         /* more comments */
         a = 1 and (b = 2 or (b + 3 = 3 - c or b in (1, 2, 3, 4)) and 1 = 1)

         and 1 in (select 1 from some_other_table where x = y)
         and  true
         and true != (false)
         and (2, 3) <> (4, 5)
         and (3, 4) in (select 4, 5)
         and (3, 4) in ((3, 4), row (4, 5))
         -- comments here
         """
    println(DSL.using(SQLDialect.POSTGRES, new Settings().withRenderFormatted(true)).render(query))
    println
/*
    val place: Piece = Place("world")
    val name: Piece = Name("Eric")
    val pieces = s2"""
    Hello $place $place
    How are you, $name?
  """
    pieces.located foreach println
    println(pieces.stuff);
    */
  }
}