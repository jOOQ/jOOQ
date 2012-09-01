/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
  trait SAnyField[T] extends QueryPartInternal {

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

    def toSQL(context : RenderContext) = underlying.toSQL(context)
    def bind (context : BindContext)   = underlying.bind(context)

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

  implicit def asSNumberField[T <: Number](f : Field[T]): SNumberField[T] = f match {
    case AnyFieldWrapper(f) => f
    case NumberFieldWrapper(f) => f
    case _ => new NumberFieldWrapper(f)
  }

  implicit def asSAnyField[T](f : Field[T]): SAnyField[T] = f match {
    case AnyFieldWrapper(f) => f
    case _ => new AnyFieldWrapper(f)
  }
}
