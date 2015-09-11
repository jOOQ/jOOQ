/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oraclescala.generatedclasses.test.tables.pojos


import java.io.Serializable
import java.lang.Integer
import java.lang.String
import java.lang.StringBuilder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


/**
 * This class is generated by jOOQ.
 */
@Entity
@Table(name = "T_DIRECTORY", schema = "TEST")
class TDirectory(
	  private var id : Integer
	, private var parentId : Integer
	, private var isDirectory : Integer
	, private var name : String 
) extends Serializable {

	def this() = {
		this(null, null, null, null)
	}

	def this (value : TDirectory) = {
		this(
			  value.id
			, value.parentId
			, value.isDirectory
			, value.name
		)
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 7)
	@NotNull
	def getId : Integer = {
		this.id
	}

	def setId(id : Integer) : Unit = {
		this.id = id
	}

	@Column(name = "PARENT_ID", precision = 7)
	def getParentId : Integer = {
		this.parentId
	}

	def setParentId(parentId : Integer) : Unit = {
		this.parentId = parentId
	}

	@Column(name = "IS_DIRECTORY", precision = 7)
	def getIsDirectory : Integer = {
		this.isDirectory
	}

	def setIsDirectory(isDirectory : Integer) : Unit = {
		this.isDirectory = isDirectory
	}

	@Column(name = "name", length = 50)
	@Size(max = 50)
	def getName : String = {
		this.name
	}

	def setName(name : String) : Unit = {
		this.name = name
	}

	override def equals(obj : Any) : scala.Boolean = {
		if (this == obj)
			return true
		if (obj == null)
			return false
		if (getClass() != obj.getClass())
			return false
		val other = obj.asInstanceOf[TDirectory]
		if (id == null) {
			if (other.id != null)
				return false
		}
		else if (!id.equals(other.id))
			return false
		if (parentId == null) {
			if (other.parentId != null)
				return false
		}
		else if (!parentId.equals(other.parentId))
			return false
		if (isDirectory == null) {
			if (other.isDirectory != null)
				return false
		}
		else if (!isDirectory.equals(other.isDirectory))
			return false
		if (name == null) {
			if (other.name != null)
				return false
		}
		else if (!name.equals(other.name))
			return false
		return true
	}

	override def hashCode : Int = {
		val prime = 31
		var result = 1
		result = prime * result + (if (id == null) 0 else id.hashCode())
		result = prime * result + (if (parentId == null) 0 else parentId.hashCode())
		result = prime * result + (if (isDirectory == null) 0 else isDirectory.hashCode())
		result = prime * result + (if (name == null) 0 else name.hashCode())
		return result
	}

	override def toString : String = {
		val sb = new StringBuilder("TDirectory (")

		sb.append(id)
		sb.append(", ").append(parentId)
		sb.append(", ").append(isDirectory)
		sb.append(", ").append(name)

		sb.append(")");
		return sb.toString
	}
}
