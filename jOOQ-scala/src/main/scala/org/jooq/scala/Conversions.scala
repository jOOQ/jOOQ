/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.scala

import org.jooq._
import org.jooq.impl._
import org.jooq.impl.Factory._

/**
 * jOOQ type conversions used to enhance the jOOQ Java API with Scala Traits
 * <p>
 * Import this object and all of its attributes to profit from an enhanced jOOQ
 * API in Scala client code. Here is an example:
 * <code><pre>
 * import java.sql.DriverManager
 * import org.jooq._
 * import org.jooq.impl._
 * import org.jooq.scala.example.h2.Tables._
 * import collection.JavaConversions._
 * import org.jooq.scala.Conversions._
 *
 * object Test {
 *   def main(args: Array[String]): Unit = {
 *     val c = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
 *     val f = new Factory(c, SQLDialect.H2);
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
 */
object Conversions {

  // -------------------------------------------------------------------------
  // Traits
  // -------------------------------------------------------------------------

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, adding overloaded
   * operators for common jOOQ operations to arbitrary fields
   */
  trait SAnyField[T] extends Field[T] {

    // String operations
    // -----------------

    def ||(value : String)            : Field[String]
    def ||(value : Field[_])          : Field[String]

    // Comparison predicates
    // ---------------------

    def ===(value : T)                : Condition
    def ===(value : Field[T])         : Condition

    def !==(value : T)                : Condition
    def !==(value : Field[T])         : Condition

    def <>(value : T)                 : Condition
    def <>(value : Field[T])          : Condition

    def >(value : T)                  : Condition
    def >(value : Field[T])           : Condition

    def >=(value : T)                 : Condition
    def >=(value : Field[T])          : Condition

    def <(value : T)                  : Condition
    def <(value : Field[T])           : Condition

    def <=(value : T)                 : Condition
    def <=(value : Field[T])          : Condition

    def <=>(value : T)                : Condition
    def <=>(value : Field[T])         : Condition
  }

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, adding overloaded
   * operators for common jOOQ operations to numeric fields
   */
  trait SNumberField[T <: Number] extends SAnyField[T] {

    // Arithmetic operations
    // ---------------------

    def unary_-                       : Field[T]

    def +(value : Number)             : Field[T]
    def +(value : Field[_ <: Number]) : Field[T]

    def -(value : Number)             : Field[T]
    def -(value : Field[_ <: Number]) : Field[T]

    def *(value : Number)             : Field[T]
    def *(value : Field[_ <: Number]) : Field[T]

    def /(value : Number)             : Field[T]
    def /(value : Field[_ <: Number]) : Field[T]

    def %(value : Number)             : Field[T]
    def %(value : Field[_ <: Number]) : Field[T]

    // Bitwise operations
    // ------------------

    def unary_~                       : Field[T]

    def &(value : T)                  : Field[T]
    def &(value : Field[T])           : Field[T]

    def |(value : T)                  : Field[T]
    def |(value : Field[T])           : Field[T]

    def ^(value : T)                  : Field[T]
    def ^(value : Field[T])           : Field[T]

    def <<(value : T)                 : Field[T]
    def <<(value : Field[T])          : Field[T]

    def >>(value : T)                 : Field[T]
    def >>(value : Field[T])          : Field[T]
  }

  // ------------------------------------------------------------------------
  // Trait implementations
  // ------------------------------------------------------------------------

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, implementing
   * overloaded operators for common jOOQ operations to arbitrary fields
   */
  abstract class AnyFieldBase[T](val underlying: Field[T])
        extends CustomField[T] (underlying.getName(), underlying.getDataType())
        with SAnyField[T] {

    // QueryPart API
    // -------------

    def toSQL(context : RenderContext) = underlying.asInstanceOf[QueryPartInternal].toSQL(context)
    def bind (context : BindContext)   = underlying.asInstanceOf[QueryPartInternal].bind(context)

    // String operations
    // -----------------

    def ||(value : String)            = underlying.concat(value)
    def ||(value : Field[_])          = underlying.concat(value)

    // Comparison predicates
    // ---------------------

    def ===(value : T)                = underlying.equal(value)
    def ===(value : Field[T])         = underlying.equal(value)

    def !==(value : T)                = underlying.notEqual(value)
    def !==(value : Field[T])         = underlying.notEqual(value)

    def <>(value : T)                 = underlying.notEqual(value)
    def <>(value : Field[T])          = underlying.notEqual(value)

    def >(value : T)                  = underlying.greaterThan(value)
    def >(value : Field[T])           = underlying.greaterThan(value)

    def >=(value : T)                 = underlying.greaterOrEqual(value)
    def >=(value : Field[T])          = underlying.greaterOrEqual(value)

    def <(value : T)                  = underlying.lessThan(value)
    def <(value : Field[T])           = underlying.lessThan(value)

    def <=(value : T)                 = underlying.lessOrEqual(value)
    def <=(value : Field[T])          = underlying.lessOrEqual(value)

    def <=>(value : T)                = underlying.isNotDistinctFrom(value)
    def <=>(value : Field[T])         = underlying.isNotDistinctFrom(value)
  }

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, implementing
   * overloaded operators for common jOOQ operations to numeric fields
   */
  abstract class NumberFieldBase[T <: Number](override val underlying: Field[T])
        extends AnyFieldBase[T] (underlying)
        with SNumberField[T] {

    // ------------------------------------------------------------------------
    // Arithmetic operations
    // ------------------------------------------------------------------------

    def unary_-                       = underlying.neg()

    def +(value : Number)             = underlying.add(value)
    def +(value : Field[_ <: Number]) = underlying.add(value)

    def -(value : Number)             = underlying.sub(value)
    def -(value : Field[_ <: Number]) = underlying.sub(value)

    def *(value : Number)             = underlying.mul(value)
    def *(value : Field[_ <: Number]) = underlying.mul(value)

    def /(value : Number)             = underlying.div(value)
    def /(value : Field[_ <: Number]) = underlying.div(value)

    def %(value : Number)             = underlying.mod(value)
    def %(value : Field[_ <: Number]) = underlying.mod(value)

    // -------------------------------------------------------------------------
    // Bitwise operations
    // -------------------------------------------------------------------------

    def unary_~                       = bitNot(underlying)

    def &(value : T)                  = bitAnd(underlying, value)
    def &(value : Field[T])           = bitAnd(underlying, value)

    def |(value : T)                  = bitOr (underlying, value)
    def |(value : Field[T])           = bitOr (underlying, value)

    def ^(value : T)                  = bitXor(underlying, value)
    def ^(value : Field[T])           = bitXor(underlying, value)

    def <<(value : T)                 = shl(underlying, value)
    def <<(value : Field[T])          = shl(underlying, value)

    def >>(value : T)                 = shr(underlying, value)
    def >>(value : Field[T])          = shr(underlying, value)
  }

  // ------------------------------------------------------------------------
  // Implicit conversions
  // ------------------------------------------------------------------------

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, implementing
   * overloaded operators for common jOOQ operations to arbitrary fields
   */
  case class AnyFieldWrapper[T] (override val underlying: Field[T])
        extends AnyFieldBase[T] (underlying) {}

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, implementing
   * overloaded operators for common jOOQ operations to numeric fields
   */
  case class NumberFieldWrapper[T <: Number](override val underlying: Field[T])
        extends NumberFieldBase[T] (underlying) {}

  /**
   * Enrich numeric {@link org.jooq.Field} with the {@link SNumberField} trait
   */
  implicit def asSNumberField[T <: Number](f : Field[T]): SNumberField[T] = f match {
    case AnyFieldWrapper(f) => f
    case NumberFieldWrapper(f) => f
    case _ => new NumberFieldWrapper(f)
  }

  /**
   * Enrich any {@link org.jooq.Field} with the {@link SAnyField} trait
   */
  implicit def asSAnyField[T](f : Field[T]): SAnyField[T] = f match {
    case AnyFieldWrapper(f) => f
    case _ => new AnyFieldWrapper(f)
  }

  // --------------------------------------------------------------------------
  // Conversions from jOOQ Record[N] types to Scala Tuple[N] types
  // --------------------------------------------------------------------------

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
  implicit def asTuple2[T1, T2](r : Record2[T1, T2]): Tuple2[T1, T2] = r match {
    case null => null
    case _ => Tuple2(r.value1, r.value2)
  }

  /**
   * Enrich any {@link org.jooq.Record3} with the {@link Tuple3} case class
   */
  implicit def asTuple3[T1, T2, T3](r : Record3[T1, T2, T3]): Tuple3[T1, T2, T3] = r match {
    case null => null
    case _ => Tuple3(r.value1, r.value2, r.value3)
  }

  /**
   * Enrich any {@link org.jooq.Record4} with the {@link Tuple4} case class
   */
  implicit def asTuple4[T1, T2, T3, T4](r : Record4[T1, T2, T3, T4]): Tuple4[T1, T2, T3, T4] = r match {
    case null => null
    case _ => Tuple4(r.value1, r.value2, r.value3, r.value4)
  }

  /**
   * Enrich any {@link org.jooq.Record5} with the {@link Tuple5} case class
   */
  implicit def asTuple5[T1, T2, T3, T4, T5](r : Record5[T1, T2, T3, T4, T5]): Tuple5[T1, T2, T3, T4, T5] = r match {
    case null => null
    case _ => Tuple5(r.value1, r.value2, r.value3, r.value4, r.value5)
  }

  /**
   * Enrich any {@link org.jooq.Record6} with the {@link Tuple6} case class
   */
  implicit def asTuple6[T1, T2, T3, T4, T5, T6](r : Record6[T1, T2, T3, T4, T5, T6]): Tuple6[T1, T2, T3, T4, T5, T6] = r match {
    case null => null
    case _ => Tuple6(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6)
  }

  /**
   * Enrich any {@link org.jooq.Record7} with the {@link Tuple7} case class
   */
  implicit def asTuple7[T1, T2, T3, T4, T5, T6, T7](r : Record7[T1, T2, T3, T4, T5, T6, T7]): Tuple7[T1, T2, T3, T4, T5, T6, T7] = r match {
    case null => null
    case _ => Tuple7(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7)
  }

  /**
   * Enrich any {@link org.jooq.Record8} with the {@link Tuple8} case class
   */
  implicit def asTuple8[T1, T2, T3, T4, T5, T6, T7, T8](r : Record8[T1, T2, T3, T4, T5, T6, T7, T8]): Tuple8[T1, T2, T3, T4, T5, T6, T7, T8] = r match {
    case null => null
    case _ => Tuple8(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8)
  }

  /**
   * Enrich any {@link org.jooq.Record9} with the {@link Tuple9} case class
   */
  implicit def asTuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](r : Record9[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9] = r match {
    case null => null
    case _ => Tuple9(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9)
  }

  /**
   * Enrich any {@link org.jooq.Record10} with the {@link Tuple10} case class
   */
  implicit def asTuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](r : Record10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = r match {
    case null => null
    case _ => Tuple10(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10)
  }

  /**
   * Enrich any {@link org.jooq.Record11} with the {@link Tuple11} case class
   */
  implicit def asTuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](r : Record11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = r match {
    case null => null
    case _ => Tuple11(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11)
  }

  /**
   * Enrich any {@link org.jooq.Record12} with the {@link Tuple12} case class
   */
  implicit def asTuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](r : Record12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = r match {
    case null => null
    case _ => Tuple12(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12)
  }

  /**
   * Enrich any {@link org.jooq.Record13} with the {@link Tuple13} case class
   */
  implicit def asTuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](r : Record13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = r match {
    case null => null
    case _ => Tuple13(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13)
  }

  /**
   * Enrich any {@link org.jooq.Record14} with the {@link Tuple14} case class
   */
  implicit def asTuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](r : Record14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = r match {
    case null => null
    case _ => Tuple14(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14)
  }

  /**
   * Enrich any {@link org.jooq.Record15} with the {@link Tuple15} case class
   */
  implicit def asTuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](r : Record15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = r match {
    case null => null
    case _ => Tuple15(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15)
  }

  /**
   * Enrich any {@link org.jooq.Record16} with the {@link Tuple16} case class
   */
  implicit def asTuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](r : Record16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = r match {
    case null => null
    case _ => Tuple16(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16)
  }

  /**
   * Enrich any {@link org.jooq.Record17} with the {@link Tuple17} case class
   */
  implicit def asTuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](r : Record17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = r match {
    case null => null
    case _ => Tuple17(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17)
  }

  /**
   * Enrich any {@link org.jooq.Record18} with the {@link Tuple18} case class
   */
  implicit def asTuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](r : Record18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = r match {
    case null => null
    case _ => Tuple18(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18)
  }

  /**
   * Enrich any {@link org.jooq.Record19} with the {@link Tuple19} case class
   */
  implicit def asTuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](r : Record19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = r match {
    case null => null
    case _ => Tuple19(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19)
  }

  /**
   * Enrich any {@link org.jooq.Record20} with the {@link Tuple20} case class
   */
  implicit def asTuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](r : Record20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = r match {
    case null => null
    case _ => Tuple20(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20)
  }

  /**
   * Enrich any {@link org.jooq.Record21} with the {@link Tuple21} case class
   */
  implicit def asTuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](r : Record21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = r match {
    case null => null
    case _ => Tuple21(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21)
  }

  /**
   * Enrich any {@link org.jooq.Record22} with the {@link Tuple22} case class
   */
  implicit def asTuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](r : Record22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): Tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = r match {
    case null => null
    case _ => Tuple22(r.value1, r.value2, r.value3, r.value4, r.value5, r.value6, r.value7, r.value8, r.value9, r.value10, r.value11, r.value12, r.value13, r.value14, r.value15, r.value16, r.value17, r.value18, r.value19, r.value20, r.value21, r.value22)
  }

// [jooq-tools] END [tuples]

}
