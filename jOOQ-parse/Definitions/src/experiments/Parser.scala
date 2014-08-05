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
 * either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 * choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
package experiments

import java.sql.{Date, Timestamp}

import experiments.AST._

import scala.util.parsing.combinator.{PackratParsers, RegexParsers}
import scala.util.parsing.input._

/**
  * @author Lukas Eder
 */
object Parser extends RegexParsers with PackratParsers {

  override protected val whiteSpace = """(\s|--.*|(?s:/\*.*?\*/))+""".r

  implicit def s(input : String) : S = S(input)

  case class S(input : String) {
    def i : scala.util.matching.Regex = ("(?i:" + input + ")").r
  }

  def main(args : Array[String]) = {
    println(parse("1 <= 2"));
  }

  lazy val sql                                       : PackratParser[Any]                        = predicate | row_value_expression

  lazy val predicate                                 : PackratParser[Any]                        = (
                                                                                                     comparison_predicate
//                                                                                                   | in_predicate
//                                                                                                   | like_predicate
//                                                                                                   | null_predicate
//                                                                                                   | quanitified_comparison_predicate
//                                                                                                   | exists_predicate
//                                                                                                   | overlaps_predicate
//                                                                                                   | distinct_predicate
                                                                                                   )

  lazy val comparison_predicate                      : PackratParser[Any]                        = row_value_predicand ~ comp_op ~ row_value_predicand

  lazy val comp_op                                   : PackratParser[Any]                        = (
                                                                                                     equals_operator
                                                                                                   | not_equals_operator
                                                                                                       | less_than_or_equals_operator
                                                                                                       | greater_than_or_equals_operator
                                                                                                   | less_than_operator
                                                                                                   | greater_than_operator
                                                                                                   )
  lazy val equals_operator                           : Parser[Any]                               = "=".r
  lazy val not_equals_operator                       : Parser[Any]                               = ( "!=".r | "<>".r )
  lazy val less_than_operator                        : Parser[Any]                               = "<".r
  lazy val greater_than_operator                     : Parser[Any]                               = ">".r
  lazy val less_than_or_equals_operator              : Parser[Any]                               = "<=".r
  lazy val greater_than_or_equals_operator           : Parser[Any]                               = ">=".r

  lazy val value_expression                          : PackratParser[_]                          = (
                                                                                                     common_value_expression
//                                                                                                   | boolean_value_expression
                                                                                                   | row_value_expression
                                                                                                   )
  lazy val common_value_expression                   : PackratParser[_]                          = (
                                                                                                     numeric_value_expression
//                                                                                                   | string_value_expression
//                                                                                                   | datetime_value_expression
//                                                                                                   | interval_value_expression
//                                                                                                   | user_defined_type_value_expression
//                                                                                                   | reference_value_expression
//                                                                                                   | collection_value_expression
                                                                                                   )
  lazy val numeric_value_expression                  : PackratParser[_]                          = (
                                                                                                     term
                                                                                                   | numeric_value_expression ~ plus_sign ~ term
                                                                                                   | numeric_value_expression ~ minus_sign ~ term
                                                                                                   )
  lazy val term                                      : PackratParser[_]                          = (
                                                                                                     factor
                                                                                                   | term ~ asterisk ~ factor
                                                                                                   | term ~ solidus ~ factor
                                                                                                   )
  lazy val factor                                    : PackratParser[_]                          = sign ~ numeric_primary
  lazy val numeric_primary                           : PackratParser[_]                          = value_expression_primary | numeric_value_function

  lazy val numeric_value_function                    : Parser[_]                                 = "?".r
/*
<numeric value function> ::=
<position expression>
| <regex occurrences function>
| <regex position expression>
| <extract expression>
| <length expression>
| <cardinality expression>
| <max cardinality expression>
| <absolute value expression>
| <modulus expression>
| <natural logarithm>
| <exponential function>
| <power function>
| <square root>
| <floor function>
| <ceiling function>
| <width bucket function>
 */

  lazy val row_value_expression                      : PackratParser[Any]                        = (
                                                                                                     row_value_special_case
//                                                                                                   | explicit_row_value_constructor
                                                                                                   )

  lazy val table_row_value_expression                : PackratParser[_]                          = (
                                                                                                     row_value_special_case
//                                                                                                   | row_value_constructor
                                                                                                   )

  lazy val row_value_predicand                       : PackratParser[_]                          = (
                                                                                                     row_value_special_case
//                                                                                                   | row_value_constructor_predicand
                                                                                                   )
  lazy val row_value_special_case                    : PackratParser[_]                          = nonparenthesized_value_expression_primary

  lazy val value_expression_primary                  : PackratParser[_]                          = parenthesized_value_expression | nonparenthesized_value_expression_primary
  lazy val parenthesized_value_expression            : PackratParser[_]                          = left_paren ~ value_expression ~ right_paren
  lazy val nonparenthesized_value_expression_primary : PackratParser[_]                          = (
                                                                                                     unsigned_value_specification
//                                                                                                   | column_reference
//                                                                                                   | set_function_specification
//                                                                                                   | window_function
//                                                                                                   | nested_window_function
//                                                                                                   | scalar_subquery
//                                                                                                   | case_expression
//                                                                                                   | cast_specification
//                                                                                                   | field_reference
                                                                                                   )

  lazy val unsigned_value_specification              : PackratParser[_]                          = unsigned_literal | general_value_specification
  lazy val unsigned_literal                          : PackratParser[_]                          = unsigned_numeric_literal | general_literal
  lazy val unsigned_numeric_literal                  : PackratParser[_]                          = exact_numeric_literal | approximate_numeric_literal
  lazy val exact_numeric_literal                     : PackratParser[_]                          = unsigned_integer ~ opt(period ~ opt(unsigned_integer))
  lazy val general_literal                           : PackratParser[_]                          = (  "?"     ~ "?"
//                                                                                                     character_string_literal
//                                                                                                   | national_character_string_literal
//                                                                                                   | unicode_character_string_literal
//                                                                                                   | binary_string_literal
//                                                                                                   | datetime_literal
//                                                                                                   | interval_literal
//                                                                                                   | boolean_literal
                                                                                                   )
  lazy val general_value_specification               : Parser[_]                                 = "?"
  lazy val unsigned_integer                          : PackratParser[_]                          = rep(digit)
  lazy val signed_integer                            : PackratParser[_]                          = opt(sign) ~ unsigned_integer
  lazy val approximate_numeric_literal               : PackratParser[_]                          = mantissa ~ "E" ~ exponent
  lazy val mantissa                                  : PackratParser[_]                          = exact_numeric_literal
  lazy val exponent                                  : PackratParser[_]                          = signed_integer

  /*
  <general literal> ::=
    <character string literal>
      | <national character string literal>
      | <Unicode character string literal>
        | <binary string literal>
          | <datetime literal>
            | <interval literal>
              | <boolean literal>*/

  lazy val left_paren                                : Parser[_]                                 = "("
  lazy val right_paren                               : Parser[_]                                 = ")"
  lazy val sign                                      : Parser[_]                                 = plus_sign | minus_sign
  lazy val plus_sign                                 : Parser[_]                                 = "+"
  lazy val minus_sign                                : Parser[_]                                 = "-"
  lazy val asterisk                                  : Parser[_]                                 = "*"
  lazy val solidus                                   : Parser[_]                                 = "/"
  lazy val period                                    : Parser[_]                                 = "."
  lazy val digit                                     : Parser[_]                                 = "[0-9]".r
  /*
14 <nonparenthesized value expression primary> ::=
<unsigned value specification>
| <column reference>
| <set function specification>
| <window function>
| <nested window function>
| <scalar subquery>
| <case expression>
| <cast specification>
| <field reference>
| <subtype treatment>
| <method invocation>
| <static method invocation>
| <new specification>
| <attribute or method reference>
| <reference resolution>
| <collection value constructor>
| <array element reference>
| <multiset element reference>
| <next value expression>
| <routine invocation>


  <contextually typed row value expression> ::=
  <row value special case>
  | <contextually typed row value constructor>
  */


    /*


    <predicate> ::=
      <comparison predicate>
      | <between predicate>
      | <in predicate>
      | <like predicate>
      | <similar predicate>
      | <regex like predicate>
      | <null predicate>
      | <quantified comparison predicate>
      | <exists predicate>
      | <unique predicate>
      | <normalized predicate>
      | <match predicate>
      | <overlaps predicate>
      | <distinct predicate>
      | <member predicate>
      | <submultiset predicate>
      | <set predicate>
      | <type predicate>
      | <period predicate>


  lazy val sql        : PackratParser[_ <: QueryPart]  = select | pred | rve
  lazy val colExpr    : PackratParser[ColExp]          = arith | literal | colIdent | "(" ~ colExpr ~ ")"                                ^^ { case _ ~ expr ~ _ => expr }
  lazy val arith      : PackratParser[ColExp]          = (
    colExpr ~ "*" ~ colExpr                                                       ^^ { case lhs ~ _ ~ rhs => Mul(lhs, rhs) }
      | colExpr ~ "/" ~ colExpr                                                       ^^ { case lhs ~ _ ~ rhs => Div(lhs, rhs) }
      | colExpr ~ "+" ~ colExpr                                                       ^^ { case lhs ~ _ ~ rhs => Add(lhs, rhs) }
      | colExpr ~ "-" ~ colExpr                                                       ^^ { case lhs ~ _ ~ rhs => Sub(lhs, rhs) }
    )
  lazy val literal    : PackratParser[Literal]         = numeric | varchar | timestamp | date | boolean | nul
  lazy val numeric    : Parser[NumberLiteral]          = """-?\d+(\.\d+)?""".r                                                           ^^ { v => NumberLiteral(new java.math.BigDecimal(v)) }
  lazy val varchar    : Parser[StringLiteral]          = """'.*'""".r                                                                    ^^ { v => StringLiteral(v.replace("'", "")) }
  lazy val timestamp  : Parser[TimestampLiteral]       = "timestamp".i ~ """'\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}(.\d+)?'""".r            ^^ { case _ ~ v => TimestampLiteral(Timestamp.valueOf(v.replace("'", ""))) }
  lazy val date       : Parser[DateLiteral]            = "date".i ~ """'\d{4}-\d{2}-\d{2}'""".r                                          ^^ { case _ ~ v => DateLiteral(Date.valueOf(v.replace("'", ""))) }
  lazy val boolean    : Parser[BooleanLiteral]         = """true|false""".i                                                              ^^ { v => BooleanLiteral(v.equals("true")) }
  lazy val nul        : Parser[NullLiteral]            = """null""".i                                                                    ^^ { _ => NullLiteral() }
  lazy val colIdent   : PackratParser[ColExp]          = qualified                                                                       ^^ { case name : List[String] => ColIdent(name) }

  lazy val rve        : PackratParser[RVE]             = (
    opt("row".i) ~ "(" ~ repsep(colExpr, ",") ~ ")"                               ^^ { case _ ~ _ ~ cols ~ _ => RVE(cols) }
      | colExpr                                                                       ^^ { cols => RVE(List(cols)) }
    )

  lazy val pred       : PackratParser[Pred]            = (
    combPred
      | compPred
      | inPred
      | negPred
      | literalPred
      | "(" ~ pred ~ ")"                                                              ^^ { case _ ~ pred ~ _ => pred }
    )
  lazy val compPred   : PackratParser[Pred]            = (
    rve ~ "=" ~ rve                                                               ^^ { case lhs ~ _ ~ rhs => Eq(lhs, rhs) }
      | rve ~ ("!=" | "<>") ~ rve                                                     ^^ { case lhs ~ _ ~ rhs => Ne(lhs, rhs) }
      | rve ~ "<" ~ rve                                                               ^^ { case lhs ~ _ ~ rhs => Lt(lhs, rhs) }
      | rve ~ "<=" ~ rve                                                              ^^ { case lhs ~ _ ~ rhs => Le(lhs, rhs) }
      | rve ~ ">" ~ rve                                                               ^^ { case lhs ~ _ ~ rhs => Gt(lhs, rhs) }
      | rve ~ ">=" ~ rve                                                              ^^ { case lhs ~ _ ~ rhs => Ge(lhs, rhs) }
    )
  lazy val combPred   : PackratParser[Pred]            = (
    pred ~ "and".i ~ pred                                                         ^^ { case lhs ~ _ ~ rhs => And(lhs, rhs) }
      | pred ~ "or".i  ~ pred                                                         ^^ { case lhs ~ _ ~ rhs => Or(lhs, rhs) }
    )
  lazy val inPred     : PackratParser[Pred]            = (
    rve ~           "in".i ~ "(" ~ inlist ~ ")"                                   ^^ { case lhs ~ _ ~ _ ~ list ~ _ => In(lhs, InValueList(list)) }
      | rve ~ "not".i ~ "in".i ~ "(" ~ inlist ~ ")"                                   ^^ { case lhs ~ _ ~ _ ~ _ ~ list ~ _ => NotIn(lhs, InValueList(list)) }
      | rve ~           "in".i ~ "(" ~ select ~ ")"                                   ^^ { case lhs ~ _ ~ _ ~ select ~ _ => In(lhs, TableSubquery(select)) }
      | rve ~ "not".i ~ "in".i ~ "(" ~ select ~ ")"                                   ^^ { case lhs ~ _ ~ _ ~ _ ~ select ~ _ => NotIn(lhs, TableSubquery(select)) }
    )

  lazy val inlist     : PackratParser[List[RVE]]       = rve ~ "," ~ repsep(rve, ",")                                                    ^^ { case first ~ _ ~ remaining => List(first) ++ remaining }

  lazy val negPred    : PackratParser[Pred]            = "not".i ~ "(" ~ pred ~ ")"                                                      ^^ { case _ ~ _ ~ pred ~ _ => Not(pred) }
  lazy val literalPred: PackratParser[Pred]            = boolean                                                                         ^^ { case pred => LiteralPred(pred.value) }
  lazy val select     : PackratParser[Sel]             = "select".i ~ selectList ~ opt(from) ~ opt(where)                                ^^ { case _ ~ select ~ from ~ where => Sel(select, from.getOrElse(List()), where.toList)}

  lazy val selectList : PackratParser[List[ColExp]]    = asterisk | repsep(selectCol, ",")
  lazy val asterisk   : PackratParser[List[ColExp]]    = "*"                                                                             ^^ { case _ => List() }
  lazy val selectCol  : PackratParser[ColExp]          = colExpr

  lazy val from       : PackratParser[List[TabExp]]    = "from".i ~ tabList                                                              ^^ { case _ ~ from => from }
  lazy val where      : PackratParser[Pred]            = "where".i ~ pred                                                                ^^ { case _ ~ pred => pred }

  lazy val tabList    : PackratParser[List[TabExp]]    = repsep(tabExpr, ",")
  lazy val tabExpr    : PackratParser[TabExp]          = (
    joinedTab | derivedTab | tabIdent
    )
  lazy val joinedTab  : PackratParser[TabExp]          = (
    tabExpr ~ opt("inner".i) ~ "join".i ~ tabExpr ~ "on".i ~ pred                 ^^ { case lhs ~ _ ~ _ ~ rhs ~ _ ~ pred => InnerJoin(lhs, rhs, On(pred)) }
      | tabExpr ~ "left".i ~ opt("outer".i) ~ "join".i ~ tabExpr ~ "on".i ~ pred      ^^ { case lhs ~ _ ~ _ ~ _ ~ rhs ~ _ ~ pred => LeftJoin(lhs, rhs, On(pred)) }
      | tabExpr ~ "right".i ~ opt("outer".i) ~ "join".i ~ tabExpr ~ "on".i ~ pred     ^^ { case lhs ~ _ ~ _ ~ _ ~ rhs ~ _ ~ pred => RightJoin(lhs, rhs, On(pred)) }
      | tabExpr ~ "full".i ~ opt("outer".i) ~ "join".i ~ tabExpr ~ "on".i ~ pred      ^^ { case lhs ~ _ ~ _ ~ _ ~ rhs ~ _ ~ pred => FullJoin(lhs, rhs, On(pred)) }
      | tabExpr ~ "cross".i ~ "join".i ~ tabExpr                                      ^^ { case lhs ~ _ ~ _ ~ rhs => CrossJoin(lhs, rhs) }
      | "(" ~ joinedTab ~ ")"                                                         ^^ { case _ ~ expr ~ _ => expr }
    )

  lazy val derivedTab : PackratParser[TabExp]          = "(" ~ select ~ ")"                                                              ^^ { case _ ~ select ~ _ => DerivedTab(select) }
  lazy val tabIdent   : PackratParser[TabExp]          = qualified                                                                       ^^ { case name : List[String] => TabIdent(name) }


  lazy val as         : PackratParser[String]          = opt("as".i) ~ ident                                                             ^^ { case _ ~ ident => ident }
  lazy val ident      : PackratParser[String]          = (
    """\w+""".r                                                                   ^^ { case value : String => value }
      | "\"" ~ """\w+""".r ~ "\""                                                     ^^ { case _ ~ value ~ _ => value }
      | "[" ~ """\w+""".r ~ "]"                                                       ^^ { case _ ~ value ~ _ => value }
      | "`" ~ """\w+""".r ~ "`"                                                       ^^ { case _ ~ value ~ _ => value }
    )
  lazy val qualified  : PackratParser[List[String]]    = repsep(ident, ".")                                                              ^^ { case value : List[String] => value }
  */

  def parse(input : String) = {
    phrase(sql)(new PackratReader(new CharArrayReader(input.toCharArray))) match {
      case Success(result, _) => println(result); Some(result)
      case n @ _ => println(n); None
    }
  }
}
