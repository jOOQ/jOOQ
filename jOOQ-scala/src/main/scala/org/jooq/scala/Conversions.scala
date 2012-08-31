package org.jooq.scala

import org.jooq._
import org.jooq.impl._
import org.jooq.impl.Factory._

object Conversions {
  import org.{ jooq => j }
  import j. { impl => ji }
  
  case class JFieldWrapper[T](val underlying : j.Field[T]) 
        extends ji.CustomField[T] (underlying.getName(), underlying.getDataType())
        with Field[T] {
    def toSQL(context : RenderContext) = underlying.toSQL(context)
    def bind(context : BindContext) = underlying.bind(context)
    
    def *(value : Number) = underlying.mul(value)
    def *(value : Field[_ <: Number]) = underlying.mul(value)
    
    def +(value : Number) = underlying.add(value)
    def +(value : Field[_ <: Number]) = underlying.add(value)
        
    def ||(value : String) = underlying.concat(value)
    //def ||(value : Field[_]) : underlying.concat(value)

    def ===(value : T) : Condition = underlying.equal(value)
    def ===(value : Field[T]) : Condition = underlying.equal(value)
  }
  
  case class SFieldWrapper[T](underlying : Field[T])
            extends ji.CustomField[T] (underlying.getName(), underlying.getDataType()) {
    def toSQL(context : RenderContext) = underlying.toSQL(context)
    def bind(context : BindContext) = underlying.bind(context)
  }

  implicit def asScalaField[T](f : j.Field[T]): Field[T] = f match {
    case JFieldWrapper(f) => f
    case _ => new JFieldWrapper(f)
  }
  
  implicit def asJavaField[T](f : Field[T]): j.Field[T] = f match {
    case JFieldWrapper(f) => f
    case _ => new SFieldWrapper(f)
  }
  
  trait Field[T] extends QueryPartInternal {
    def *(value : Number) : Field[T]
    def *(value : Field[_ <: Number]) : Field[T]
    
    def +(value : Number) : Field[T]
    def +(value : Field[_ <: Number]) : Field[T]
        
    def ||(value : String) : Field[String]
    //def ||(value : Field[_]) : Field[String]

    def ===(value : T) : Condition
    def ===(value : Field[T]) : Condition
  }
}
