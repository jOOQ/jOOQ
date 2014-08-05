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
package org.jooq.parse

import java.math.BigInteger

import scala.util.parsing.combinator.{PackratParsers, RegexParsers}
import scala.util.parsing.input._

import org.jooq.parse.AST._

/**
 * @author Lukas Eder
 */
object StandardParser extends RegexParsers with PackratParsers {

  override protected val whiteSpace = """(\s|--.*|(?s:/\*.*?\*/))+""".r

  implicit def s(input: String): S = S(input)

  case class S(input: String) {
    def i: scala.util.matching.Regex = ("(?i:" + input + ")").r
  }

  lazy val parseables: PackratParser[Any] = predicate | row_value_expression


  lazy val equals_operator: Parser[Any] = "=".r
  lazy val not_equals_operator: Parser[Any] = ("!=".r | "<>".r)
  lazy val less_than_operator: Parser[Any] = "<".r
  lazy val greater_than_operator: Parser[Any] = ">".r
  lazy val less_than_or_equals_operator: Parser[Any] = "<=".r
  lazy val greater_than_or_equals_operator: Parser[Any] = ">=".r

  lazy val value_expression: PackratParser[_] = (
    common_value_expression
      //                                                                                                                  | boolean_value_expression
      | row_value_expression
    )
  lazy val common_value_expression: PackratParser[_] = (
    numeric_value_expression
    //                                                                                                                  | string_value_expression
    //                                                                                                                  | datetime_value_expression
    //                                                                                                                  | interval_value_expression
    //                                                                                                                  | user_defined_type_value_expression
    //                                                                                                                  | reference_value_expression
    //                                                                                                                  | collection_value_expression
    )
  lazy val numeric_value_expression: PackratParser[_] = (
    term
      | numeric_value_expression ~ plus_sign ~ term
      | numeric_value_expression ~ minus_sign ~ term
    )
  lazy val term: PackratParser[_] = (
    factor
      | term ~ asterisk ~ factor
      | term ~ solidus ~ factor
    )
  lazy val factor: PackratParser[_] = sign ~ numeric_primary
  lazy val numeric_primary: PackratParser[_] = value_expression_primary | numeric_value_function

  lazy val numeric_value_function: Parser[_] = "?".r


  lazy val unsigned_value_specification: PackratParser[_] = unsigned_literal | general_value_specification
  lazy val unsigned_literal: PackratParser[_] = unsigned_numeric_literal | general_literal
  lazy val unsigned_numeric_literal: PackratParser[_] = exact_numeric_literal | approximate_numeric_literal
  lazy val exact_numeric_literal: PackratParser[ExactNumericLiteral[_]] =
    unsigned_integer ~ opt(period ~ opt(unsigned_integer)) ^^ {
      case UnsignedInteger(i1) ~ None => ExactNumericLiteral(i1)
      case UnsignedInteger(i1) ~ Some(_ ~ None) => ExactNumericLiteral(i1)
      case UnsignedInteger(i1) ~ Some(_ ~ Some(UnsignedInteger(i2))) => ExactNumericLiteral(BigDecimal(new java.math.BigDecimal(i1 + "." + i2)))
    }
  lazy val general_literal: PackratParser[_] = ("?" ~ "?"
    //                                                                                                                    character_string_literal
    //                                                                                                                  | national_character_string_literal
    //                                                                                                                  | unicode_character_string_literal
    //                                                                                                                  | binary_string_literal
    //                                                                                                                  | datetime_literal
    //                                                                                                                  | interval_literal
    //                                                                                                                  | boolean_literal
    )
  lazy val general_value_specification: Parser[_] = "?"
  lazy val unsigned_integer: PackratParser[UnsignedInteger] = rep(digit) ^^ { case digits => UnsignedInteger(BigInt(new BigInteger(digits.mkString(""))))}
  lazy val signed_integer: PackratParser[_] = opt(sign) ~ unsigned_integer
  lazy val approximate_numeric_literal: PackratParser[_] = mantissa ~ "E" ~ exponent
  lazy val mantissa: PackratParser[_] = exact_numeric_literal
  lazy val exponent: PackratParser[_] = signed_integer

  // 5.1 <SQL terminal character>
  lazy val left_paren: Parser[_] = "("
  lazy val right_paren: Parser[_] = ")"
  lazy val comma: Parser[_] = ","
  lazy val sign: Parser[_] = plus_sign | minus_sign
  lazy val plus_sign: Parser[_] = "+"
  lazy val minus_sign: Parser[_] = "-"
  lazy val asterisk: Parser[_] = "*"
  lazy val solidus: Parser[_] = "/"
  lazy val period: Parser[_] = "."
  lazy val digit: Parser[_] = "[0-9]".r
  lazy val double_quote: Parser[_] = "\""
  lazy val nondoublequote_character = "[A-Za-z0-9]"
  lazy val doublequote_symbol: Parser[_] = "\"\""
  lazy val left_bracket_or_trigraph: Parser[_] = left_bracket | left_bracket_trigraph
  lazy val right_bracket_or_trigraph: Parser[_] = right_bracket | right_bracket_trigraph
  lazy val left_bracket: Parser[_] = "["
  lazy val left_bracket_trigraph: Parser[_] = "??("
  lazy val right_bracket: Parser[_] = "]"
  lazy val right_bracket_trigraph: Parser[_] = "??)"

  // 5.2 <token> and <separator>
  lazy val delimited_identifier: PackratParser[_] = double_quote ~ delimited_identifier_body ~ double_quote
  lazy val delimited_identifier_body: PackratParser[_] = rep(delimited_identifier_part)
  lazy val delimited_identifier_part: PackratParser[_] = nondoublequote_character | doublequote_symbol

  // 5.4 Names and identifiers
  lazy val identifier: PackratParser[_] = actual_identifier
  // TODO
  lazy val actual_identifier: PackratParser[_] = "regular_identifier" | delimited_identifier | "unicode_delimited_identifier"
  lazy val qualified_identifier: PackratParser[_] = identifier
  lazy val column_name: PackratParser[_] = identifier
  /*
  <delimited identifier> ::=
<double quote> <delimited identifier body> <double quote>
<delimited identifier body> ::=
<delimited identifier part>...
   */

  // 6.3 <value expression primary>
  lazy val value_expression_primary: PackratParser[_] = parenthesized_value_expression | nonparenthesized_value_expression_primary
  lazy val parenthesized_value_expression: PackratParser[_] = left_paren ~ value_expression ~ right_paren
  lazy val nonparenthesized_value_expression_primary: PackratParser[_] = (
    unsigned_value_specification
      | column_reference
    //                                                                                                                  | set_function_specification
    //                                                                                                                  | window_function
    //                                                                                                                  | nested_window_function
    //                                                                                                                  | scalar_subquery
    //                                                                                                                  | case_expression
    //                                                                                                                  | cast_specification
    //                                                                                                                  | field_reference
    //                                                                                                                  | subtype_treatment
    //                                                                                                                  | method_invocation
    //                                                                                                                  | static_method_invocation
    //                                                                                                                  | new_specification
    //                                                                                                                  | attribute_or_method_reference
    //                                                                                                                  | reference_resolution
    //                                                                                                                  | collection_value_constructor
    //                                                                                                                  | array_element_reference
    //                                                                                                                  | multiset_element_reference
    //                                                                                                                  | next_value_expression
    //                                                                                                                  | routine_invocation
    )
  // TODO
  lazy val collection_value_constructor: PackratParser[_] = "array_value_constructor" | "multiset_value_constructor"

  // 6.5 <contextually typed value specification>
  lazy val contextually_typed_value_specification: PackratParser[_] = implicitly_typed_value_specification | default_specification
  lazy val implicitly_typed_value_specification: PackratParser[_] = null_specification | empty_specification
  lazy val null_specification: Parser[_] = "null".i
  lazy val empty_specification: PackratParser[_] = (
    "array".i ~ left_bracket_or_trigraph ~ right_bracket_or_trigraph
      | "multiset".i ~ left_bracket_or_trigraph ~ right_bracket_or_trigraph
    )
  lazy val default_specification: Parser[_] = "default".i

  // 6.6 <identifier chain>
  lazy val identifier_chain: PackratParser[_] = identifier ~ opt(rep(period ~ identifier))
  lazy val basic_identifier_chain: PackratParser[_] = identifier_chain

  // 6.7 <column reference>
  lazy val column_reference: PackratParser[_] = (
    basic_identifier_chain
      | "module".i ~ period ~ qualified_identifier ~ period ~ column_name
    )

  // 6.35 <boolean value expression>
  lazy val boolean_value_expression: PackratParser[_] = (
    boolean_term
      | boolean_value_expression ~ "or".i ~ boolean_term
    )
  lazy val boolean_term: PackratParser[_] = (
    boolean_factor
      | boolean_term ~ "and".i ~ boolean_factor
    )
  lazy val boolean_factor: PackratParser[_] = opt("not".i) ~ boolean_test
  lazy val boolean_test: PackratParser[_] = boolean_primary ~ opt("is".i ~ opt("not".i) ~ truth_value)
  lazy val truth_value: PackratParser[_] = "true".i | "false".i | "unknown".i
  lazy val boolean_primary: PackratParser[_] = predicate | boolean_predicand
  lazy val boolean_predicand: PackratParser[_] = parenthesized_boolean_value_expression | nonparenthesized_value_expression_primary
  lazy val parenthesized_boolean_value_expression: PackratParser[_] = left_paren ~ boolean_value_expression ~ right_paren

  // 7.1 <row value constructor>
  lazy val row_value_constructor: PackratParser[_] = (
    common_value_expression
      | boolean_value_expression
      | explicit_row_value_constructor
    )
  lazy val explicit_row_value_constructor: PackratParser[_] = (
    left_paren ~ row_value_constructor_element ~ comma ~ row_value_constructor_element_list ~ right_paren
      | "row".i ~ left_paren ~ row_value_constructor_element_list ~ right_paren
      | row_subquery
    )
  lazy val row_value_constructor_element_list: PackratParser[_] = row_value_constructor_element ~ opt(rep(comma ~ row_value_constructor_element))
  lazy val row_value_constructor_element: PackratParser[_] = value_expression
  lazy val contextually_typed_row_value_constructor: PackratParser[_] = (
    common_value_expression
      | boolean_value_expression
      | contextually_typed_value_specification
      | left_paren ~ contextually_typed_value_specification ~ right_paren
      | left_paren ~ contextually_typed_row_value_constructor_element ~ comma ~ contextually_typed_row_value_constructor_element_list ~ right_paren
      | "row".i ~ left_paren ~ contextually_typed_row_value_constructor_element_list ~ right_paren
    )
  lazy val contextually_typed_row_value_constructor_element_list: PackratParser[_] = contextually_typed_row_value_constructor_element ~ opt(rep(comma ~ contextually_typed_row_value_constructor_element))
  lazy val contextually_typed_row_value_constructor_element: PackratParser[_] = value_expression | contextually_typed_value_specification
  lazy val row_value_constructor_predicand: PackratParser[_] = (
    common_value_expression
      | boolean_predicand
      | explicit_row_value_constructor
    )

  // 7.2 <row value expression>
  lazy val row_value_expression: PackratParser[Any] = (
    row_value_special_case
      | explicit_row_value_constructor
    )

  lazy val table_row_value_expression: PackratParser[_] = (
    row_value_special_case
      | row_value_constructor
    )

  lazy val contextually_typed_row_value_expression: PackratParser[_] = (
    row_value_special_case
      | contextually_typed_row_value_constructor
    )

  lazy val row_value_predicand: PackratParser[_] = (
    row_value_special_case
      | row_value_constructor_predicand
    )
  lazy val row_value_special_case: PackratParser[_] = nonparenthesized_value_expression_primary

  // 7.15 <subquery>
  lazy val scalar_subquery: PackratParser[_] = subquery
  lazy val row_subquery: PackratParser[_] = subquery
  lazy val table_subquery: PackratParser[_] = subquery
  // TODO
  lazy val subquery: PackratParser[_] = left_paren ~ "query_expression" ~ right_paren

  // 8.1 <predicate>
  lazy val predicate: PackratParser[_] = (
    comparison_predicate
    //                                                                                                                  | between_predicate
    //                                                                                                                  | in_predicate
    //                                                                                                                  | like_predicate
    //                                                                                                                  | similar_predicate
    //                                                                                                                  | regex_like_predicate
    //                                                                                                                  | null_predicate
    //                                                                                                                  | quanitified_comparison_predicate
    //                                                                                                                  | exists_predicate
    //                                                                                                                  | unique_predicate
    //                                                                                                                  | normalized_predicate
    //                                                                                                                  | match_predicate
    //                                                                                                                  | overlaps_predicate
    //                                                                                                                  | distinct_predicate
    //                                                                                                                  | member_predicate
    //                                                                                                                  | submultiset_predicate
    //                                                                                                                  | set_predicate
    //                                                                                                                  | type_predicate
    //                                                                                                                  | period_predicate
    )

  // 8.2 <comparison predicate>
  lazy val comparison_predicate: PackratParser[_] = row_value_predicand ~ comparison_predicate_part_2
  lazy val comparison_predicate_part_2: PackratParser[_] = comp_op ~ row_value_predicand
  lazy val comp_op: PackratParser[Any] = (
    equals_operator
      | not_equals_operator
      | less_than_or_equals_operator
      | greater_than_or_equals_operator
      | less_than_operator
      | greater_than_operator
    )

  def parse(input: String) = {
    phrase(parseables)(new PackratReader(new CharArrayReader(input.toCharArray))) match {
      case Success(result, _) => Some(result)
      case n@_ => println(n); None
    }
  }
}
