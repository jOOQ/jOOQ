/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oraclescala.generatedclasses.test.udt.pojos


import java.io.Serializable
import java.lang.StringBuilder


/**
 * This class is generated by jOOQ.
 */
class UInvalidType(
) extends Serializable {


	def this (value : UInvalidType) = {
		this(
		)
	}

	override def equals(obj : Any) : scala.Boolean = {
		if (this == obj)
			return true
		if (obj == null)
			return false
		if (getClass() != obj.getClass())
			return false
		val other = obj.asInstanceOf[UInvalidType]
		return true
	}

	override def hashCode : Int = {
		val prime = 31
		var result = 1
		return result
	}

	override def toString : String = {
		val sb = new StringBuilder("UInvalidType (")


		sb.append(")");
		return sb.toString
	}
}
