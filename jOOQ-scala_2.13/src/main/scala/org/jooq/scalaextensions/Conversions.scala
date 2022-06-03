/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.scalaextensions

import java.sql.ResultSet
import org.jooq._
import org.jooq.impl._

import java.util.stream.Collector

// Avoid ambiguity with the internal org.jooq.impl.Array type.
import scala.Array
import scala.jdk.CollectionConverters

/**
 * jOOQ type conversions used to enhance the jOOQ Java API with Scala Traits
 * <p>
 * Import this object and all of its attributes to profit from an enhanced jOOQ
 * API in Scala client code. Here is an example:
 * <code><pre>
 * import java.sql.DriverManager
 * import org.jooq._
 * import org.jooq.impl._
 * import org.jooq.impl.DSL._
 * import org.jooq.examples.generated.h2.Tables._
 * import collection.JavaConversions._
 * import org.jooq.scalaextensions.Conversions._
 *
 * object Test {
 *   def main(args: Array[String]): Unit = {
 *     val c = DriverManager.getConnection("jdbc:h2:~/scala-test-", "sa", "");
 *     val f = DSL.using(c, SQLDialect.H2);
 *
 *     for (
 *       val r <- f
 *         select (
 *           T_BOOK.ID * T_BOOK.AUTHOR_ID,
 *           T_BOOK.ID + T_BOOK.AUTHOR_ID * 3 + 4,
 *           T_BOOK.TITLE || " abc" || " xy")
 *           from T_BOOK
 *           where (T_BOOK.ID === 3) fetch
 *     ) {
 *
 *       println(r)
 *     }
 *   }
 * }
 * </pre></code>
 *
 * @author Lukas Eder
 * @author Eric Peters
 */
object Conversions {

  // -------------------------------------------------------------------------
  // Enhanced jOOQ types
  // -------------------------------------------------------------------------

  implicit class SQLInterpolation(val sc : StringContext) extends AnyVal {

    @PlainSQL
    def sql(args: Any*) : SQL = DSL.sql(string(args), args.asInstanceOf[Seq[AnyRef]] : _*)

    @PlainSQL
    def condition(args : Any*) : Condition = DSL.condition(string(args), args.asInstanceOf[Seq[AnyRef]] : _*)

    @PlainSQL
    def table(args : Any*) : Table[Record] = DSL.table(string(args), args.asInstanceOf[Seq[AnyRef]] : _*)

    @PlainSQL
    def query(args : Any*) : Query = DSL.query(string(args), args.asInstanceOf[Seq[AnyRef]] : _*)

    @PlainSQL
    def resultQuery(args : Any*) : ResultQuery[Record] = DSL.resultQuery(string(args), args.asInstanceOf[Seq[AnyRef]] : _*)

    private def string(args : Any*) = {
      val pi = sc.parts.iterator
      val sb = new StringBuilder(pi.next())
      var i = 0;

      while (pi.hasNext) {
        sb += '{'
        sb ++= (i.toString())
        sb += '}'
        sb ++= pi.next()

        i = i + 1;
      }

      sb.result()
    }
  }

  implicit class ScalaDSLContext (val ctx : DSLContext) {
    def fetchAnyOption[R <: Record]          (table : Table[R])                        : Option[R]      = Option(ctx.fetchAny(table))
    def fetchAnyOption[R <: Record]          (table : Table[R], condition : Condition) : Option[R]      = Option(ctx.fetchAny(table, condition))

    def fetchOneOption[R <: Record]          (query : ResultQuery[R])                  : Option[R]      = Option(ctx.fetchOne(query))
    def fetchOneOption                       (rs : ResultSet)                          : Option[Record] = Option(ctx.fetchOne(rs))

    def fetchOneOption                       (rs : ResultSet, types : Class[_]*)       : Option[Record] = Option(ctx.fetchOne(rs, types:_*))
    def fetchOneOption                       (rs : ResultSet, types : DataType[_]*)
      (implicit d: DummyImplicit)                                                      : Option[Record] = Option(ctx.fetchOne(rs, types:_*))
    def fetchOneOption                       (rs : ResultSet, fields : Field[_]*)
      (implicit d1: DummyImplicit, d2: DummyImplicit)                                  : Option[Record] = Option(ctx.fetchOne(rs, fields:_*))
    def fetchOneOption                       (sql : String)                            : Option[Record] = Option(ctx.fetchOne(sql))
    def fetchOneOption                       (sql : String, bindings : AnyRef*)        : Option[Record] = Option(ctx.fetchOne(sql, bindings:_*))
    def fetchOneOption                       (sql : String, parts : QueryPart*)
      (implicit d: DummyImplicit)                                                      : Option[Record] = Option(ctx.fetchOne(sql, parts:_*))
    def fetchOneOption[R <: Record]          (table : Table[R])                        : Option[R]      = Option(ctx.fetchOne(table))
    def fetchOneOption[R <: Record]          (table : Table[R], condition : Condition) : Option[R]      = Option(ctx.fetchOne(table, condition))

    def fetchValueOption[T, R <: Record1[T]] (query : ResultQuery[R])                  : Option[T]      = Option(ctx.fetchValue[T, R](query))
    def fetchValueOption                     (rs : ResultSet)                          : Option[AnyRef] = Option(ctx.fetchValue(rs))
    def fetchValueOption[T]                  (rs : ResultSet, newType : Class[T])      : Option[T]      = Option(ctx.fetchValue(rs, newType))
    def fetchValueOption[T]                  (rs : ResultSet, newType : DataType[T])   : Option[T]      = Option(ctx.fetchValue(rs, newType))
    def fetchValueOption[T]                  (rs : ResultSet, field : Field[T])        : Option[T]      = Option(ctx.fetchValue(rs, field))
    def fetchValueOption                     (sql : String)                            : Option[AnyRef] = Option(ctx.fetchValue(sql))
    def fetchValueOption                     (sql : String, bindings : AnyRef*)        : Option[AnyRef] = Option(ctx.fetchValue(sql, bindings:_*))
    def fetchValueOption                     (sql : String, parts : QueryPart*)
      (implicit d: DummyImplicit)                                                      : Option[AnyRef] = Option(ctx.fetchValue(sql, parts:_*))
  }

  implicit class ScalaResultQuery[R <: Record](val query : ResultQuery[R]) {
    import scala.collection.mutable._

    def fetchAnyOption                 ()                                                         : Option[R]                   = Option(query.fetchAny)
    def fetchAnyOption[E]              (mapper : RecordMapper[_ >: R, E])                         : Option[E]                   = Option(query.fetchAny(mapper))
    def fetchAnyOption[T]              (field : Field[T])                                         : Option[T]                   = Option(query.fetchAny(field))
    def fetchAnyOption[T]              (field : Field[_], newType : Class[_ <: T])                : Option[T]                   = Option(query.fetchAny(field, newType))
    def fetchAnyOption[T, U]           (field : Field[T], converter : Converter[_ >: T, _ <: U])  : Option[U]                   = Option(query.fetchAny[T, U](field, converter))
    def fetchAnyOption                 (fieldIndex : Int)                                         : Option[_]                   = Option(query.fetchAny(fieldIndex))
    def fetchAnyOption[T]              (fieldIndex : Int, newType : Class[_ <: T])                : Option[T]                   = Option(query.fetchAny(fieldIndex, newType))
    def fetchAnyOption[T, U]           (fieldIndex : Int, converter : Converter[_ >: T, _ <: U])  : Option[U]                   = Option(query.fetchAny(fieldIndex, converter))
    def fetchAnyOption                 (fieldName : String)                                       : Option[_]                   = Option(query.fetchAny(fieldName))
    def fetchAnyOption[T]              (fieldName : String, newType : Class[_ <: T])              : Option[T]                   = Option(query.fetchAny(fieldName, newType))
    def fetchAnyOption[T, U]           (fieldName : String, converter : Converter[_ >: T, _ <: U]): Option[U]                   = Option(query.fetchAny(fieldName, converter))
    def fetchAnyOptionArray            ()                                                         : Option[Array[AnyRef]]       = Option(query.fetchAnyArray)
    def fetchAnyOptionInto[E]          (newType : Class[_ <: E])                                  : Option[E]                   = Option(query.fetchAnyInto(newType))
    def fetchAnyOptionInto[Z <: Record](table : Table[Z])                                         : Option[Z]                   = Option(query.fetchAnyInto(table))
    def fetchAnyOptionMap              ()                                                         : Option[Map[String, AnyRef]] = Option(query.fetchAnyMap).map((m: java.util.Map[String, AnyRef]) => CollectionConverters.MapHasAsScala(m).asScala)

    def fetchOneOption                 ()                                                         : Option[R]                   = Option(query.fetchOne)
    def fetchOneOption[E]              (mapper : RecordMapper[_ >: R, E])                         : Option[E]                   = Option(query.fetchOne(mapper))
    def fetchOneOption[T]              (field : Field[T])                                         : Option[T]                   = Option(query.fetchOne(field))
    def fetchOneOption[T]              (field : Field[_], newType : Class[_ <: T])                : Option[T]                   = Option(query.fetchOne(field, newType))
    def fetchOneOption[T, U]           (field : Field[T], converter : Converter[_ >: T, _ <: U])  : Option[U]                   = Option(query.fetchOne[T, U](field, converter))
    def fetchOneOption                 (fieldIndex : Int)                                         : Option[_]                   = Option(query.fetchOne(fieldIndex))
    def fetchOneOption[T]              (fieldIndex : Int, newType : Class[_ <: T])                : Option[T]                   = Option(query.fetchOne(fieldIndex, newType))
    def fetchOneOption[T, U]           (fieldIndex : Int, converter : Converter[_ >: T, _ <: U])  : Option[U]                   = Option(query.fetchOne(fieldIndex, converter))
    def fetchOneOption                 (fieldName : String)                                       : Option[_]                   = Option(query.fetchOne(fieldName))
    def fetchOneOption[T]              (fieldName : String, newType : Class[_ <: T])              : Option[T]                   = Option(query.fetchOne(fieldName, newType))
    def fetchOneOption[T, U]           (fieldName : String, converter : Converter[_ >: T, _ <: U]): Option[U]                   = Option(query.fetchOne(fieldName, converter))
    def fetchOneOptionArray            ()                                                         : Option[Array[AnyRef]]       = Option(query.fetchOneArray)
    def fetchOneOptionInto[E]          (newType : Class[_ <: E])                                  : Option[E]                   = Option(query.fetchOneInto(newType))
    def fetchOneOptionInto[Z <: Record](table : Table[Z])                                         : Option[Z]                   = Option(query.fetchOneInto(table))
    def fetchOneOptionMap              ()                                                         : Option[Map[String, AnyRef]] = Option(query.fetchOneMap).map((m: java.util.Map[String, AnyRef]) => CollectionConverters.MapHasAsScala(m).asScala)
  }

  // -------------------------------------------------------------------------
  // Traits
  // -------------------------------------------------------------------------

  /**
   * A Scala-esque representation of {@link org.jooq.Table}, adding overloaded
   * operators for common jOOQ operations to arbitrary tables
   */
  implicit class ScalaTable[R <: Record](val table : Table[R]) extends AnyVal {
    def * : QualifiedAsterisk = table.asterisk
  }

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, adding overloaded
   * operators for common jOOQ operations to arbitrary fields
   */
  implicit class ScalaField[T](val field : Field[T]) extends AnyVal {

    // String operations
    // -----------------

    def ||(value : String)                             : Field[String] = field.concat(value)
    def ||(value : Field[_])                           : Field[String] = field.concat(value)

    // Comparison predicates
    // ---------------------

    def ===(value : T)                                 : Condition = field.equal(value)
    def ===(value : Field[T])                          : Condition = field.equal(value)
    def ===(value : Select[_ <: Record1[T]])           : Condition = field.equal(value)
    def ===(value : QuantifiedSelect[_ <: Record1[T]]) : Condition = field.equal(value)

    def !==(value : T)                                 : Condition = field.notEqual(value)
    def !==(value : Field[T])                          : Condition = field.notEqual(value)
    def !==(value : Select[_ <: Record1[T]])           : Condition = field.notEqual(value)
    def !==(value : QuantifiedSelect[_ <: Record1[T]]) : Condition = field.notEqual(value)

    def <>(value : T)                                  : Condition = field.notEqual(value)
    def <>(value : Field[T])                           : Condition = field.notEqual(value)
    def <>(value : Select[_ <: Record1[T]])            : Condition = field.notEqual(value)
    def <>(value : QuantifiedSelect[_ <: Record1[T]])  : Condition = field.notEqual(value)

    def >(value : T)                                   : Condition = field.greaterThan(value)
    def >(value : Field[T])                            : Condition = field.greaterThan(value)
    def >(value : Select[_ <: Record1[T]])             : Condition = field.greaterThan(value)
    def >(value : QuantifiedSelect[_ <: Record1[T]])   : Condition = field.greaterThan(value)

    def >=(value : T)                                  : Condition = field.greaterOrEqual(value)
    def >=(value : Field[T])                           : Condition = field.greaterOrEqual(value)
    def >=(value : Select[_ <: Record1[T]])            : Condition = field.greaterOrEqual(value)
    def >=(value : QuantifiedSelect[_ <: Record1[T]])  : Condition = field.greaterOrEqual(value)

    def <(value : T)                                   : Condition = field.lessThan(value)
    def <(value : Field[T])                            : Condition = field.lessThan(value)
    def <(value : Select[_ <: Record1[T]])             : Condition = field.lessThan(value)
    def <(value : QuantifiedSelect[_ <: Record1[T]])   : Condition = field.lessThan(value)

    def <=(value : T)                                  : Condition = field.lessOrEqual(value)
    def <=(value : Field[T])                           : Condition = field.lessOrEqual(value)
    def <=(value : Select[_ <: Record1[T]])            : Condition = field.lessOrEqual(value)
    def <=(value : QuantifiedSelect[_ <: Record1[T]])  : Condition = field.lessOrEqual(value)

    def <=>(value : T)                                 : Condition = field.isNotDistinctFrom(value)
    def <=>(value : Field[T])                          : Condition = field.isNotDistinctFrom(value)




  }

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, adding overloaded
   * operators for common jOOQ operations to numeric fields
   */
  implicit class ScalaNumberField[T <: Number](val field : Field[T]) extends AnyVal {

    // ------------------------------------------------------------------------
    // Arithmetic operations
    // ------------------------------------------------------------------------

    def unary_-                       = field.neg()

    def +(value : Number)             = field.add(value)
    def +(value : Field[_ <: Number]) = field.add(value)

    def -(value : Number)             = field.sub(value)
    def -(value : Field[_ <: Number]) = field.sub(value)

    def *(value : Number)             = field.mul(value)
    def *(value : Field[_ <: Number]) = field.mul(value)

    def /(value : Number)             = field.div(value)
    def /(value : Field[_ <: Number]) = field.div(value)

    def %(value : Number)             = field.mod(value)
    def %(value : Field[_ <: Number]) = field.mod(value)

    // -------------------------------------------------------------------------
    // Bitwise operations
    // -------------------------------------------------------------------------

    def unary_~                       = DSL.bitNot(field)

    def &(value : T)                  = DSL.bitAnd(field, value)
    def &(value : Field[T])           = DSL.bitAnd(field, value)

    def |(value : T)                  = DSL.bitOr (field, value)
    def |(value : Field[T])           = DSL.bitOr (field, value)

    def ^(value : T)                  = DSL.bitXor(field, value)
    def ^(value : Field[T])           = DSL.bitXor(field, value)

    def <<(value : T)                 = DSL.shl(field, value)
    def <<(value : Field[T])          = DSL.shl(field, value)

    def >>(value : T)                 = DSL.shr(field, value)
    def >>(value : Field[T])          = DSL.shr(field, value)
  }

  // --------------------------------------------------------------------------
  // Row convenience
  // --------------------------------------------------------------------------

  // [jooq-tools] START [record]

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField1[T1](val field: Field[Record1[T1]]) {
    def mapping[E](f: (T1) => E): Field[E] = field.convertFrom(Records.mapping((t1) => f(t1))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField2[T1, T2](val field: Field[Record2[T1, T2]]) {
    def mapping[E](f: (T1, T2) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2) => f(t1, t2))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField3[T1, T2, T3](val field: Field[Record3[T1, T2, T3]]) {
    def mapping[E](f: (T1, T2, T3) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3) => f(t1, t2, t3))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField4[T1, T2, T3, T4](val field: Field[Record4[T1, T2, T3, T4]]) {
    def mapping[E](f: (T1, T2, T3, T4) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4) => f(t1, t2, t3, t4))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField5[T1, T2, T3, T4, T5](val field: Field[Record5[T1, T2, T3, T4, T5]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5) => f(t1, t2, t3, t4, t5))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField6[T1, T2, T3, T4, T5, T6](val field: Field[Record6[T1, T2, T3, T4, T5, T6]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6) => f(t1, t2, t3, t4, t5, t6))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField7[T1, T2, T3, T4, T5, T6, T7](val field: Field[Record7[T1, T2, T3, T4, T5, T6, T7]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7) => f(t1, t2, t3, t4, t5, t6, t7))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField8[T1, T2, T3, T4, T5, T6, T7, T8](val field: Field[Record8[T1, T2, T3, T4, T5, T6, T7, T8]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8) => f(t1, t2, t3, t4, t5, t6, t7, t8))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField9[T1, T2, T3, T4, T5, T6, T7, T8, T9](val field: Field[Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](val field: Field[Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](val field: Field[Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](val field: Field[Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](val field: Field[Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](val field: Field[Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](val field: Field[Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](val field: Field[Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](val field: Field[Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](val field: Field[Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](val field: Field[Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](val field: Field[Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](val field: Field[Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21))(_))
  }

  /**
   * Enrich record expressions with convenience methods
   */
  implicit class ScalaRecordField22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](val field: Field[Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]]) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => E): Field[E] = field.convertFrom(Records.mapping((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) => f(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22))(_))
  }

  // [jooq-tools] END [record]

  // --------------------------------------------------------------------------
  // Multiset convenience
  // --------------------------------------------------------------------------

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField[R <: Record](val field: Field[Result[R]]) {
    def collecting[E](collector: Collector[R, _, E]): Field[E] = field.convertFrom(_.collect(collector))
    def intoGroups[K](keyMapper: R => K): Field[java.util.Map[K, java.util.List[R]]] = collecting(Records.intoGroups[K, R](keyMapper))
    def intoGroups[K, V](keyMapper: R => K, valueMapper: R => V): Field[java.util.Map[K, java.util.List[V]]] = collecting(Records.intoGroups[K, V, R](keyMapper, valueMapper))
    def intoList[E](mapper: R => E): Field[java.util.List[E]] = collecting(Records.intoList[E, R](mapper))
    def intoMap[K](keyMapper: R => K): Field[java.util.Map[K, R]] = collecting(Records.intoMap[K, R](keyMapper))
    def intoMap[K, V](keyMapper: R => K, valueMapper: R => V): Field[java.util.Map[K, V]] = collecting(Records.intoMap[K, V, R](keyMapper, valueMapper))
    def intoResultGroups[K](keyMapper: R => K): Field[java.util.Map[K, Result[R]]] = collecting(Records.intoResultGroups[K, R](keyMapper))
    def intoResultGroups[K, V <: Record](keyMapper: R => K, valueMapper: R => V): Field[java.util.Map[K, Result[V]]] = collecting(Records.intoResultGroups[K, V, R](keyMapper, valueMapper))
    def intoSet[E](mapper: R => E): Field[java.util.Set[E]] = collecting(Records.intoSet[E, R](mapper))
  }

  // [jooq-tools] START [multiset]

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField1[T1](override val field: Field[Result[Record1[T1]]]) extends ScalaResultField[Record1[T1]](field) {
    def mapping[E](f: (T1) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
    def intoList(): Field[java.util.List[T1]] = collecting(Records.intoList[T1, Record1[T1]]())
    def intoSet(): Field[java.util.Set[T1]] = collecting(Records.intoSet[T1, Record1[T1]]())
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField2[T1, T2](override val field: Field[Result[Record2[T1, T2]]]) extends ScalaResultField[Record2[T1, T2]](field) {
    def mapping[E](f: (T1, T2) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
    def intoGroups(): Field[java.util.Map[T1, java.util.List[T2]]] = collecting(Records.intoGroups[T1, T2, Record2[T1, T2]]())
    def intoMap(): Field[java.util.Map[T1, T2]] = collecting(Records.intoMap[T1, T2, Record2[T1, T2]]())
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField3[T1, T2, T3](override val field: Field[Result[Record3[T1, T2, T3]]]) extends ScalaResultField[Record3[T1, T2, T3]](field) {
    def mapping[E](f: (T1, T2, T3) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField4[T1, T2, T3, T4](override val field: Field[Result[Record4[T1, T2, T3, T4]]]) extends ScalaResultField[Record4[T1, T2, T3, T4]](field) {
    def mapping[E](f: (T1, T2, T3, T4) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField5[T1, T2, T3, T4, T5](override val field: Field[Result[Record5[T1, T2, T3, T4, T5]]]) extends ScalaResultField[Record5[T1, T2, T3, T4, T5]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField6[T1, T2, T3, T4, T5, T6](override val field: Field[Result[Record6[T1, T2, T3, T4, T5, T6]]]) extends ScalaResultField[Record6[T1, T2, T3, T4, T5, T6]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField7[T1, T2, T3, T4, T5, T6, T7](override val field: Field[Result[Record7[T1, T2, T3, T4, T5, T6, T7]]]) extends ScalaResultField[Record7[T1, T2, T3, T4, T5, T6, T7]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField8[T1, T2, T3, T4, T5, T6, T7, T8](override val field: Field[Result[Record8[T1, T2, T3, T4, T5, T6, T7, T8]]]) extends ScalaResultField[Record8[T1, T2, T3, T4, T5, T6, T7, T8]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField9[T1, T2, T3, T4, T5, T6, T7, T8, T9](override val field: Field[Result[Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9]]]) extends ScalaResultField[Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](override val field: Field[Result[Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]]]) extends ScalaResultField[Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](override val field: Field[Result[Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]]]) extends ScalaResultField[Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](override val field: Field[Result[Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]]]) extends ScalaResultField[Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](override val field: Field[Result[Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]]]) extends ScalaResultField[Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](override val field: Field[Result[Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]]]) extends ScalaResultField[Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](override val field: Field[Result[Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]]]) extends ScalaResultField[Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](override val field: Field[Result[Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]]]) extends ScalaResultField[Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](override val field: Field[Result[Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]]]) extends ScalaResultField[Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](override val field: Field[Result[Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]]]) extends ScalaResultField[Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](override val field: Field[Result[Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]]]) extends ScalaResultField[Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](override val field: Field[Result[Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]]]) extends ScalaResultField[Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](override val field: Field[Result[Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]]]) extends ScalaResultField[Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  /**
   * Enrich multiset expressions with convenience methods
   */
  implicit class ScalaResultField22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](override val field: Field[Result[Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]]]) extends ScalaResultField[Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]](field) {
    def mapping[E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => E): Field[java.util.List[E]] = field.convertFrom(r => r.map(f))
  }

  // [jooq-tools] END [multiset]

  // --------------------------------------------------------------------------
  // Conversions from jOOQ Record[N] types to Scala Tuple[N] types
  // --------------------------------------------------------------------------

  import scala.language.implicitConversions

// [jooq-tools] START [tuples]

  /**
   * Enrich any {@link org.jooq.Record1} with the {@link Tuple1} case class
   */
  implicit def asTuple1[T1](r : Record1[T1]): Tuple1[T1] = r match {
    case null => null
    case _ => Tuple1(r.value1)
  }

  /**
   * Enrich any {@link org.jooq.Record2} with the {@link Tuple2} case class
   */
  implicit def asTuple2[T1, T2](r : Record2[T1, T2]): (T1, T2) = r match {
    case null => null
    case _ => (r.value1, r.value2)
  }

  /**
   * Enrich any {@link org.jooq.Record3} with the {@link Tuple3} case class
   */
  implicit def asTuple3[T1, T2, T3](r : Record3[T1, T2, T3]): (T1, T2, T3) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3)
  }

  /**
   * Enrich any {@link org.jooq.Record4} with the {@link Tuple4} case class
   */
  implicit def asTuple4[T1, T2, T3, T4](r : Record4[T1, T2, T3, T4]): (T1, T2, T3, T4) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4)
  }

  /**
   * Enrich any {@link org.jooq.Record5} with the {@link Tuple5} case class
   */
  implicit def asTuple5[T1, T2, T3, T4, T5](r : Record5[T1, T2, T3, T4, T5]): (T1, T2, T3, T4, T5) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5)
  }

  /**
   * Enrich any {@link org.jooq.Record6} with the {@link Tuple6} case class
   */
  implicit def asTuple6[T1, T2, T3, T4, T5, T6](r : Record6[T1, T2, T3, T4, T5, T6]): (T1, T2, T3, T4, T5, T6) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6)
  }

  /**
   * Enrich any {@link org.jooq.Record7} with the {@link Tuple7} case class
   */
  implicit def asTuple7[T1, T2, T3, T4, T5, T6, T7](r : Record7[T1, T2, T3, T4, T5, T6, T7]): (T1, T2, T3, T4, T5, T6, T7) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7)
  }

  /**
   * Enrich any {@link org.jooq.Record8} with the {@link Tuple8} case class
   */
  implicit def asTuple8[T1, T2, T3, T4, T5, T6, T7, T8](r : Record8[T1, T2, T3, T4, T5, T6, T7, T8]): (T1, T2, T3, T4, T5, T6, T7, T8) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8)
  }

  /**
   * Enrich any {@link org.jooq.Record9} with the {@link Tuple9} case class
   */
  implicit def asTuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](r : Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9]): (T1, T2, T3, T4, T5, T6, T7, T8, T9) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9)
  }

  /**
   * Enrich any {@link org.jooq.Record10} with the {@link Tuple10} case class
   */
  implicit def asTuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](r : Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10)
  }

  /**
   * Enrich any {@link org.jooq.Record11} with the {@link Tuple11} case class
   */
  implicit def asTuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](r : Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11)
  }

  /**
   * Enrich any {@link org.jooq.Record12} with the {@link Tuple12} case class
   */
  implicit def asTuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](r : Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12)
  }

  /**
   * Enrich any {@link org.jooq.Record13} with the {@link Tuple13} case class
   */
  implicit def asTuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](r : Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13)
  }

  /**
   * Enrich any {@link org.jooq.Record14} with the {@link Tuple14} case class
   */
  implicit def asTuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](r : Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14)
  }

  /**
   * Enrich any {@link org.jooq.Record15} with the {@link Tuple15} case class
   */
  implicit def asTuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](r : Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15)
  }

  /**
   * Enrich any {@link org.jooq.Record16} with the {@link Tuple16} case class
   */
  implicit def asTuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](r : Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16)
  }

  /**
   * Enrich any {@link org.jooq.Record17} with the {@link Tuple17} case class
   */
  implicit def asTuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](r : Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17)
  }

  /**
   * Enrich any {@link org.jooq.Record18} with the {@link Tuple18} case class
   */
  implicit def asTuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](r : Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18)
  }

  /**
   * Enrich any {@link org.jooq.Record19} with the {@link Tuple19} case class
   */
  implicit def asTuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](r : Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19)
  }

  /**
   * Enrich any {@link org.jooq.Record20} with the {@link Tuple20} case class
   */
  implicit def asTuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](r : Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20)
  }

  /**
   * Enrich any {@link org.jooq.Record21} with the {@link Tuple21} case class
   */
  implicit def asTuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](r : Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21)
  }

  /**
   * Enrich any {@link org.jooq.Record22} with the {@link Tuple22} case class
   */
  implicit def asTuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](r : Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) = r match {
    case null => null
    case _ => (r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21, r.value22)
  }

// [jooq-tools] END [tuples]

  /**
   * Wrap a Scala <code>R => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapper[R <: Record, E](f: R => E): RecordMapper[R, E] = new RecordMapper[R, E] {
    def map(record: R) = f(record)
  }

// [jooq-tools] START [mapper]

  /**
   * Wrap a Scala <code>Tuple1 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList1[T1, E](f: (T1) => E): RecordMapper[Record1[T1], E] = r => f(r.value1)

  /**
   * Wrap a Scala <code>Tuple2 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList2[T1, T2, E](f: (T1, T2) => E): RecordMapper[Record2[T1, T2], E] = r => f(r.value1, r.value2)

  /**
   * Wrap a Scala <code>Tuple3 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList3[T1, T2, T3, E](f: (T1, T2, T3) => E): RecordMapper[Record3[T1, T2, T3], E] = r => f(r.value1, r.value2, r.value3)

  /**
   * Wrap a Scala <code>Tuple4 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList4[T1, T2, T3, T4, E](f: (T1, T2, T3, T4) => E): RecordMapper[Record4[T1, T2, T3, T4], E] = r => f(r.value1, r.value2, r.value3, r.value4)

  /**
   * Wrap a Scala <code>Tuple5 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList5[T1, T2, T3, T4, T5, E](f: (T1, T2, T3, T4, T5) => E): RecordMapper[Record5[T1, T2, T3, T4, T5], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5)

  /**
   * Wrap a Scala <code>Tuple6 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList6[T1, T2, T3, T4, T5, T6, E](f: (T1, T2, T3, T4, T5, T6) => E): RecordMapper[Record6[T1, T2, T3, T4, T5, T6], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6)

  /**
   * Wrap a Scala <code>Tuple7 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList7[T1, T2, T3, T4, T5, T6, T7, E](f: (T1, T2, T3, T4, T5, T6, T7) => E): RecordMapper[Record7[T1, T2, T3, T4, T5, T6, T7], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7)

  /**
   * Wrap a Scala <code>Tuple8 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList8[T1, T2, T3, T4, T5, T6, T7, T8, E](f: (T1, T2, T3, T4, T5, T6, T7, T8) => E): RecordMapper[Record8[T1, T2, T3, T4, T5, T6, T7, T8], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8)

  /**
   * Wrap a Scala <code>Tuple9 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList9[T1, T2, T3, T4, T5, T6, T7, T8, T9, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => E): RecordMapper[Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9)

  /**
   * Wrap a Scala <code>Tuple10 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => E): RecordMapper[Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10)

  /**
   * Wrap a Scala <code>Tuple11 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => E): RecordMapper[Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11)

  /**
   * Wrap a Scala <code>Tuple12 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => E): RecordMapper[Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12)

  /**
   * Wrap a Scala <code>Tuple13 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => E): RecordMapper[Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13)

  /**
   * Wrap a Scala <code>Tuple14 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => E): RecordMapper[Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14)

  /**
   * Wrap a Scala <code>Tuple15 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => E): RecordMapper[Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15)

  /**
   * Wrap a Scala <code>Tuple16 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => E): RecordMapper[Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16)

  /**
   * Wrap a Scala <code>Tuple17 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => E): RecordMapper[Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17)

  /**
   * Wrap a Scala <code>Tuple18 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => E): RecordMapper[Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18)

  /**
   * Wrap a Scala <code>Tuple19 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => E): RecordMapper[Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19)

  /**
   * Wrap a Scala <code>Tuple20 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => E): RecordMapper[Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20)

  /**
   * Wrap a Scala <code>Tuple21 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => E): RecordMapper[Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21)

  /**
   * Wrap a Scala <code>Tuple22 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromArgList22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => E): RecordMapper[Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22], E] = r => f(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21, r.value22)

  /**
   * Wrap a Scala <code>Tuple1 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple1[T1, E](f: Tuple1[T1] => E): RecordMapper[Record1[T1], E] = r => f(Tuple1(r.value1))

  /**
   * Wrap a Scala <code>Tuple2 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple2[T1, T2, E](f: ((T1, T2)) => E): RecordMapper[Record2[T1, T2], E] = r => f((r.value1, r.value2))

  /**
   * Wrap a Scala <code>Tuple3 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple3[T1, T2, T3, E](f: ((T1, T2, T3)) => E): RecordMapper[Record3[T1, T2, T3], E] = r => f((r.value1, r.value2, r.value3))

  /**
   * Wrap a Scala <code>Tuple4 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple4[T1, T2, T3, T4, E](f: ((T1, T2, T3, T4)) => E): RecordMapper[Record4[T1, T2, T3, T4], E] = r => f((r.value1, r.value2, r.value3, r.value4))

  /**
   * Wrap a Scala <code>Tuple5 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple5[T1, T2, T3, T4, T5, E](f: ((T1, T2, T3, T4, T5)) => E): RecordMapper[Record5[T1, T2, T3, T4, T5], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5))

  /**
   * Wrap a Scala <code>Tuple6 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple6[T1, T2, T3, T4, T5, T6, E](f: ((T1, T2, T3, T4, T5, T6)) => E): RecordMapper[Record6[T1, T2, T3, T4, T5, T6], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6))

  /**
   * Wrap a Scala <code>Tuple7 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple7[T1, T2, T3, T4, T5, T6, T7, E](f: ((T1, T2, T3, T4, T5, T6, T7)) => E): RecordMapper[Record7[T1, T2, T3, T4, T5, T6, T7], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7))

  /**
   * Wrap a Scala <code>Tuple8 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple8[T1, T2, T3, T4, T5, T6, T7, T8, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8)) => E): RecordMapper[Record8[T1, T2, T3, T4, T5, T6, T7, T8], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8))

  /**
   * Wrap a Scala <code>Tuple9 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9)) => E): RecordMapper[Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9))

  /**
   * Wrap a Scala <code>Tuple10 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)) => E): RecordMapper[Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10))

  /**
   * Wrap a Scala <code>Tuple11 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)) => E): RecordMapper[Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11))

  /**
   * Wrap a Scala <code>Tuple12 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)) => E): RecordMapper[Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12))

  /**
   * Wrap a Scala <code>Tuple13 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)) => E): RecordMapper[Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13))

  /**
   * Wrap a Scala <code>Tuple14 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)) => E): RecordMapper[Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14))

  /**
   * Wrap a Scala <code>Tuple15 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)) => E): RecordMapper[Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15))

  /**
   * Wrap a Scala <code>Tuple16 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)) => E): RecordMapper[Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16))

  /**
   * Wrap a Scala <code>Tuple17 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)) => E): RecordMapper[Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17))

  /**
   * Wrap a Scala <code>Tuple18 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)) => E): RecordMapper[Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18))

  /**
   * Wrap a Scala <code>Tuple19 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)) => E): RecordMapper[Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19))

  /**
   * Wrap a Scala <code>Tuple20 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)) => E): RecordMapper[Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20))

  /**
   * Wrap a Scala <code>Tuple21 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)) => E): RecordMapper[Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21))

  /**
   * Wrap a Scala <code>Tuple22 => E</code> function in a jOOQ <code>RecordMapper</code> type.
   */
  implicit def asMapperFromTuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)) => E): RecordMapper[Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22], E] = r => f((r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21, r.value22))

// [jooq-tools] END [mapper]

//  /**
//   * Wrap a Scala <code>R => Unit</code> function in a jOOQ <code>RecordHandler</code> type.
//   */
//  implicit def asHandler[R <: Record](f: R => Unit): RecordHandler[R] = new RecordHandler[R] {
//    def next(record: R) = f(record)
//  }

// [jooq-tools] START [handler]
// [jooq-tools] END [handler]
}
