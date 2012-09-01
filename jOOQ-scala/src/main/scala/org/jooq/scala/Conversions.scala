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

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, implementing
   * overloaded operators for common jOOQ operations
   */
  case class JFieldWrapper[T](val underlying: Field[T])
        extends CustomField[T] (underlying.getName(), underlying.getDataType())
        with SField[T] {
    // ------------------------------------------------------------------------
    // QueryPart API
    // ------------------------------------------------------------------------

    def toSQL(context : RenderContext) = underlying.toSQL(context)
    def bind (context : BindContext)   = underlying.bind(context)

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


    def ||(value : String)            = underlying.concat(value)
    //def ||(value : Field[_]) : underlying.concat(value)

    // ------------------------------------------------------------------------
    // Comparison predicates
    // ------------------------------------------------------------------------

    def ===(value : T)        : Condition = underlying.equal(value)
    def ===(value : Field[T]) : Condition = underlying.equal(value)

    def !==(value : T)        : Condition = underlying.notEqual(value)
    def !==(value : Field[T]) : Condition = underlying.notEqual(value)

    def <>(value : T)         : Condition = underlying.notEqual(value)
    def <>(value : Field[T])  : Condition = underlying.notEqual(value)

    def >(value : T)          : Condition = underlying.greaterThan(value)
    def >(value : Field[T])   : Condition = underlying.greaterThan(value)

    def >=(value : T)         : Condition = underlying.greaterOrEqual(value)
    def >=(value : Field[T])  : Condition = underlying.greaterOrEqual(value)

    def <(value : T)          : Condition = underlying.lessThan(value)
    def <(value : Field[T])   : Condition = underlying.lessThan(value)

    def <=(value : T)         : Condition = underlying.lessOrEqual(value)
    def <=(value : Field[T])  : Condition = underlying.lessOrEqual(value)
  }

  /**
   * A Scala-esque representation of {@link org.jooq.Field}, adding overloaded
   * operators for common jOOQ operations
   */
  trait SField[T] extends QueryPartInternal {

    // ------------------------------------------------------------------------
    // Arithmetic operations
    // ------------------------------------------------------------------------

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

    def ||(value : String)            : Field[String]
    //def ||(value : Field[_])        : Field[String]

    // ------------------------------------------------------------------------
    // Comparison predicates
    // ------------------------------------------------------------------------

    def ===(value : T)               : Condition
    def ===(value : Field[T])        : Condition

    def !==(value : T)               : Condition
    def !==(value : Field[T])        : Condition

    def <>(value : T)                : Condition
    def <>(value : Field[T])         : Condition

    def >(value : T)                 : Condition
    def >(value : Field[T])          : Condition

    def >=(value : T)                : Condition
    def >=(value : Field[T])         : Condition

    def <(value : T)                 : Condition
    def <(value : Field[T])          : Condition

    def <=(value : T)                : Condition
    def <=(value : Field[T])         : Condition
  }

  implicit def asScalaField[T](f : Field[T]): SField[T] = f match {
    case JFieldWrapper(f) => f
    case _ => new JFieldWrapper(f)
  }
}
