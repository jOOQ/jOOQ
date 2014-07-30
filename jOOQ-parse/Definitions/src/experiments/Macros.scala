package experiments

import java.sql.{Date, Timestamp}

import org.jooq.impl.{DefaultConfiguration, DSL}
import org.jooq._

import scala.language.experimental.macros
import scala.reflect.macros.Context

object Macros {

  import scala.reflect.macros.Context
  import language.experimental.macros
  import scala.util.parsing.combinator._
  import scala.util.parsing.combinator.syntactical._

  trait QueryPart

  trait TabExp extends QueryPart
  trait Join extends TabExp

  case class InnerJoin(lhs : TabExp, rhs : TabExp, spec : JoinSpec) extends Join
  case class LeftJoin(lhs : TabExp, rhs : TabExp, spec : JoinSpec) extends Join
  case class RightJoin(lhs : TabExp, rhs : TabExp, spec : JoinSpec) extends Join
  case class FullJoin(lhs : TabExp, rhs : TabExp, spec : JoinSpec) extends Join
  case class CrossJoin(lhs : TabExp, rhs : TabExp) extends Join
  case class CrossApply(lhs : TabExp, rhs : TabExp) extends Join
  case class OuterApply(lhs : TabExp, rhs : TabExp) extends Join

  trait JoinSpec extends QueryPart
  case class On(pred : Pred) extends JoinSpec
  case class Using(cols : List[ColExp]) extends JoinSpec

  case class TabAlias(table : TabExp, name : String) extends TabExp
  case class DerivedTab(select : Sel) extends TabExp
  case class TabIdent(name : List[String]) extends TabExp


  trait ColExp extends QueryPart
  case class ColIdent(name : List[String]) extends ColExp

  trait Literal extends ColExp
  case class NumberLiteral(value : java.math.BigDecimal) extends Literal
  case class StringLiteral(value : String) extends Literal
  case class TimestampLiteral(value : Timestamp) extends Literal
  case class DateLiteral(value : Date) extends Literal
  case class BooleanLiteral(value : Boolean) extends Literal
  case class NullLiteral() extends Literal

  trait Arith extends ColExp
  case class Add(lhs : ColExp, rhs : ColExp) extends Arith
  case class Sub(lhs : ColExp, rhs : ColExp) extends Arith
  case class Mul(lhs : ColExp, rhs : ColExp) extends Arith
  case class Div(lhs : ColExp, rhs : ColExp) extends Arith

  case class RVE(cols : List[ColExp]) extends QueryPart

  trait Pred extends QueryPart
  trait CombPred extends Pred
  case class And(lhs : Pred, rhs : Pred) extends CombPred
  case class Or(lhs : Pred, rhs : Pred) extends CombPred

  trait CompPred extends Pred
  case class Eq(lhs : RVE, rhs : RVE) extends CompPred
  case class Ne(lhs : RVE, rhs : RVE) extends CompPred
  case class Lt(lhs : RVE, rhs : RVE) extends CompPred
  case class Le(lhs : RVE, rhs : RVE) extends CompPred
  case class Gt(lhs : RVE, rhs : RVE) extends CompPred
  case class Ge(lhs : RVE, rhs : RVE) extends CompPred

  trait InPred extends Pred
  case class In(lhs : RVE, rhs : InPredicateValue) extends InPred
  case class NotIn(lhs : RVE, rhs : InPredicateValue) extends InPred

  trait InPredicateValue extends QueryPart
  case class TableSubquery(select : Sel) extends InPredicateValue
  case class InValueList(list : List[RVE]) extends InPredicateValue

  case class Not(pred : Pred) extends Pred
  case class LiteralPred(value : Boolean) extends Pred

  trait Statement extends QueryPart
  case class Sel(select : List[ColExp], from : List[TabExp], where : List[Pred]) extends Statement

  object SQLParser extends RegexParsers with PackratParsers {

    import scala.util.parsing.input._

    override protected val whiteSpace = """(\s|--.*|(?s:/\*.*?\*/))+""".r

    implicit def s(input : String) : S = S(input)
    case class S(input : String) {
      def i : scala.util.matching.Regex = ("(?i:" + input + ")").r
    }

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

    def parse(input : String) = {
      phrase(sql)(new PackratReader(new CharArrayReader(input.toCharArray))) match {
        case Success(result, _) => println(result); Some(result)
        case n @ _ => println(n); None
      }
    }
  }

  implicit class sql2ast(sc: StringContext) {
    def sql(parts: QueryPart*) = macro sqlImpl
  }

  def sqlImpl(c: Context)(parts: c.Expr[Any]*): c.Expr[org.jooq.QueryPart] = {
    import c.universe.{ Name => _, _ }

    c.prefix.tree match {
      case Apply(_, List(Apply(_, raw))) =>

        // It looks like this doesn not yet allow $variables
        val parts = raw map { case Literal(Constant(const: String)) => const }

        // Why is parts a List?
        val parsed = SQLParser.parse(parts(0))

        parsed match {
          case Some(expr) =>

            def tree[Q <: org.jooq.QueryPart](part: QueryPart) : Tree = {
              def select(identifier : String) : Tree = {
                val terms = identifier.split("\\.").map { TermName(_) }
                terms.tail.foldLeft[Tree](Ident(terms.head)) { Select(_, _) }
              }

              def treeCombCond(lhs : Pred, term : String, rhs : Pred) : Tree =
                Apply(
                  Select(tree[Condition](lhs), TermName(term)),
                  List(tree[Condition](rhs))
                )

              def treeCompPred(lhs : RVE, term : String, rhs : RVE) : Tree =
                if (lhs.cols.size == 1)
                  Apply(
                    Select(tree[Field[Object]](lhs.cols(0)), TermName(term)),
                    List(tree[Field[Object]](rhs.cols(0)))
                  )
                else
                  Apply(
                    Select(tree[Row2[Object, Object]](lhs), TermName(term)),
                    List(tree[Row2[Object, Object]](rhs))
                  )

              def treeInPred(lhs : RVE, term : String, rhs : InPredicateValue) : Tree =
                rhs match {
                  case InValueList(list) =>
                    Apply(
                      Select(tree[Field[Object]](lhs), TermName(term)),
                      list map { tree(_) }
                    )
                  case TableSubquery(sel) =>
                    Apply(
                      Select(tree[Field[Object]](lhs), TermName(term)),
                      List(tree[org.jooq.Select[Record1[Object]]](rhs))
                    )
                }

              def treeArith(lhs : ColExp, term : String, rhs : ColExp) : Tree =
                Apply(
                  Select(tree[Field[_]](lhs), TermName(term)),
                  List(tree[Field[_]](rhs))
                )

              def treeJoin(lhs : TabExp, term : String, rhs : TabExp) : Tree =
                Apply(
                  Select(tree[Table[_]](lhs), TermName(term)),
                  List(tree[Table[_]](rhs))
                )

              def treeJoinOn(lhs : TabExp, term : String, rhs : TabExp, pred : Pred) : Tree =
                Apply(
                  Select(
                    treeJoin(lhs, term, rhs),
                    TermName("on")
                  ),
                  List(tree[Condition](pred))
                )

              part match {
                case And   (lhs, rhs)                => treeCombCond(lhs, "and"     , rhs)
                case Or    (lhs, rhs)                => treeCombCond(lhs, "or"      , rhs)

                case Eq    (lhs, rhs)                => treeCompPred(lhs, "equal"   , rhs)
                case Ne    (lhs, rhs)                => treeCompPred(lhs, "notEqual", rhs)
                case Lt    (lhs, rhs)                => treeCompPred(lhs, "lt"      , rhs)
                case Le    (lhs, rhs)                => treeCompPred(lhs, "le"      , rhs)
                case Gt    (lhs, rhs)                => treeCompPred(lhs, "gt"      , rhs)
                case Ge    (lhs, rhs)                => treeCompPred(lhs, "ge"      , rhs)

                case In    (lhs, InValueList(rhs))   => treeInPred  (lhs, "in"      , InValueList(rhs))
                case NotIn (lhs, InValueList(rhs))   => treeInPred  (lhs, "notIn"   , InValueList(rhs))
                case In    (lhs, TableSubquery(rhs)) => treeInPred  (lhs, "in"      , TableSubquery(rhs))
                case NotIn (lhs, TableSubquery(rhs)) => treeInPred  (lhs, "notIn"   , TableSubquery(rhs))

                case Add   (lhs, rhs)                => treeArith   (lhs, "add"     , rhs)
                case Sub   (lhs, rhs)                => treeArith   (lhs, "sub"     , rhs)
                case Mul   (lhs, rhs)                => treeArith   (lhs, "mul"     , rhs)
                case Div   (lhs, rhs)                => treeArith   (lhs, "div"     , rhs)

                case LiteralPred(value)              =>
                  if (value)
                    Apply(select("org.jooq.impl.DSL.trueCondition"), List())
                  else
                    Apply(select("org.jooq.impl.DSL.falseCondition"), List())
                case TableSubquery(sel)              => tree(sel)

                case RVE(cols) =>
                  if (cols.size == 1)
                    tree(cols(0))
                  else
                    Apply(
                      select("org.jooq.impl.DSL.row"),
                      cols map { tree(_) }
                    )
                case ColIdent(name) =>
                  Apply(
                    select("org.jooq.impl.DSL.fieldByName"),
                    name map { n : String => Literal(Constant(n)) }
                  )
                case NumberLiteral(value) =>
                  Apply(
                    select("org.jooq.impl.DSL.inline"),
                    List(Literal(Constant(java.lang.Integer.valueOf(value.toString))))
                  )
                case StringLiteral(value) =>
                  Apply(
                    select("org.jooq.impl.DSL.inline"),
                    List(Literal(Constant(value)))
                  )
                case BooleanLiteral(value) =>
                  Apply(
                    select("org.jooq.impl.DSL.inline"),
                    List(Literal(Constant(java.lang.Boolean.valueOf(value))))
                  )

                case InnerJoin(lhs, rhs, On(pred)) => treeJoinOn(lhs, "join"          , rhs, pred)
                case LeftJoin(lhs, rhs, On(pred))  => treeJoinOn(lhs, "leftOuterJoin" , rhs, pred)
                case RightJoin(lhs, rhs, On(pred)) => treeJoinOn(lhs, "rightOuterJoin", rhs, pred)
                case FullJoin(lhs, rhs, On(pred))  => treeJoinOn(lhs, "fullOuterJoin" , rhs, pred)
                case CrossJoin(lhs, rhs)           => treeJoin  (lhs, "crossJoin"     , rhs)
                case CrossApply(lhs, rhs)          => treeJoin  (lhs, "crossApply"    , rhs)
                case OuterApply(lhs, rhs)          => treeJoin  (lhs, "outerApply"    , rhs)

                case TabIdent(name) =>
                  Apply(
                    select("org.jooq.impl.DSL.tableByName"),
                    name map { n : String => Literal(Constant(n)) }
                  )

                case DerivedTab(sel) =>
                  Apply(
                    select("org.jooq.impl.DSL.table"),
                    List(tree(sel))
                  )

                case Sel(s, f, w) =>
                  val s1 =
                    Apply(
                      select("org.jooq.impl.DSL.select"),
                      s map { tree(_) }
                    )

                  val s2 =
                    if (f.size > 0)
                      Apply(
                        Select(s1, TermName("from")),
                        f map { tree(_) }
                      )
                    else
                      s1

                  val s3 =
                    if (w.size > 0)
                      Apply(
                        Select(s2, TermName("where")),
                        w map { tree(_) }
                      )
                    else
                      s2

                  s3
                case _ =>
                  c.abort(c.enclosingPosition, s"Error. Unknown expression $part in expression $expr")
              }
            }

            c.Expr(tree(expr))
          case None =>
            c.abort(c.enclosingPosition, s"Error. Could not parse : ${parts(0)}")
        }

      case _ =>
        c.abort(c.enclosingPosition, "Error. Unmatchable tree : " + c.prefix.tree)
    }
  }
}