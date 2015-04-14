/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oraclescala.generatedclasses.test.tables


import java.lang.Class
import java.lang.Integer
import java.lang.String
import java.time.LocalDate
import java.util.Arrays
import java.util.List

import org.jooq.Field
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UniqueKey
import org.jooq.impl.TableImpl
import org.jooq.test.all.converters.LocalDateConverter
import org.jooq.test.oraclescala.generatedclasses.test.Keys
import org.jooq.test.oraclescala.generatedclasses.test.Test
import org.jooq.test.oraclescala.generatedclasses.test.tables.records.T_2155Record
import org.jooq.test.oraclescala.generatedclasses.test.udt.records.U_2155ArrayRecord
import org.jooq.test.oraclescala.generatedclasses.test.udt.records.U_2155ObjectRecord


object T_2155 {

	/**
	 * The reference instance of <code>TEST.T_2155</code>
	 */
	val T_2155 = new T_2155
}

/**
 * This class is generated by jOOQ.
 */
class T_2155(alias : String, aliased : Table[T_2155Record], parameters : Array[ Field[_] ]) extends TableImpl[T_2155Record](alias, Test.TEST, aliased, parameters, "") {

	/**
	 * The class holding records for this type
	 */
	override def getRecordType : Class[T_2155Record] = {
		classOf[T_2155Record]
	}

	/**
	 * The column <code>TEST.T_2155.ID</code>.
	 */
	val ID : TableField[T_2155Record, Integer] = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

	/**
	 * The column <code>TEST.T_2155.D1</code>.
	 */
	val D1 : TableField[T_2155Record, LocalDate] = createField("D1", org.jooq.impl.SQLDataType.DATE, "", new LocalDateConverter())

	/**
	 * The column <code>TEST.T_2155.D2</code>.
	 */
	val D2 : TableField[T_2155Record, U_2155ObjectRecord] = createField("D2", org.jooq.test.oraclescala.generatedclasses.test.udt.U_2155Object.U_2155_OBJECT.getDataType(), "")

	/**
	 * The column <code>TEST.T_2155.D3</code>.
	 */
	val D3 : TableField[T_2155Record, U_2155ArrayRecord] = createField("D3", org.jooq.impl.SQLDataType.DATE.asArrayDataType(classOf[org.jooq.test.oraclescala.generatedclasses.test.udt.records.U_2155ArrayRecord]), "")

	/**
	 * Create a <code>TEST.T_2155</code> table reference
	 */
	def this() = {
		this("T_2155", null, null)
	}

	/**
	 * Create an aliased <code>TEST.T_2155</code> table reference
	 */
	def this(alias : String) = {
		this(alias, org.jooq.test.oraclescala.generatedclasses.test.tables.T_2155.T_2155, null)
	}

	private def this(alias : String, aliased : Table[T_2155Record]) = {
		this(alias, aliased, null)
	}

	override def getPrimaryKey : UniqueKey[T_2155Record] = {
		Keys.PK_T_2155
	}

	override def getKeys : List[ UniqueKey[T_2155Record] ] = {
		return Arrays.asList[ UniqueKey[T_2155Record] ](Keys.PK_T_2155)
	}

	override def as(alias : String) : T_2155 = {
		new T_2155(alias, this)
	}

	/**
	 * Rename this table
	 */
	def rename(name : String) : T_2155 = {
		new T_2155(name, null)
	}
}
