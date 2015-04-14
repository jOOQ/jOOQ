/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oraclescala.generatedclasses.multi_schema.tables


import java.lang.Class
import java.lang.Integer
import java.lang.String
import java.util.Arrays
import java.util.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UniqueKey
import org.jooq.impl.TableImpl
import org.jooq.test.oraclescala.generatedclasses.multi_schema.Keys
import org.jooq.test.oraclescala.generatedclasses.multi_schema.MultiSchema
import org.jooq.test.oraclescala.generatedclasses.multi_schema.tables.records.TBookRecord

import scala.Array


object TBook {

	/**
	 * The reference instance of <code>MULTI_SCHEMA.T_BOOK</code>
	 */
	val T_BOOK = new TBook
}

/**
 * This class is generated by jOOQ.
 */
class TBook(alias : String, aliased : Table[TBookRecord], parameters : Array[ Field[_] ]) extends TableImpl[TBookRecord](alias, MultiSchema.MULTI_SCHEMA, aliased, parameters, "") {

	/**
	 * The class holding records for this type
	 */
	override def getRecordType : Class[TBookRecord] = {
		classOf[TBookRecord]
	}

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.ID</code>.
	 */
	val ID : TableField[TBookRecord, Integer] = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.AUTHOR_ID</code>.
	 */
	val AUTHOR_ID : TableField[TBookRecord, Integer] = createField("AUTHOR_ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.CO_AUTHOR_ID</code>.
	 */
	val CO_AUTHOR_ID : TableField[TBookRecord, Integer] = createField("CO_AUTHOR_ID", org.jooq.impl.SQLDataType.INTEGER, "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.DETAILS_ID</code>.
	 */
	val DETAILS_ID : TableField[TBookRecord, Integer] = createField("DETAILS_ID", org.jooq.impl.SQLDataType.INTEGER, "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.TITLE</code>.
	 */
	val TITLE : TableField[TBookRecord, String] = createField("TITLE", org.jooq.impl.SQLDataType.VARCHAR.length(400).nullable(false), "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.PUBLISHED_IN</code>.
	 */
	val PUBLISHED_IN : TableField[TBookRecord, Integer] = createField("PUBLISHED_IN", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.LANGUAGE_ID</code>.
	 */
	val LANGUAGE_ID : TableField[TBookRecord, Integer] = createField("LANGUAGE_ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.CONTENT_TEXT</code>.
	 */
	val CONTENT_TEXT : TableField[TBookRecord, String] = createField("CONTENT_TEXT", org.jooq.impl.SQLDataType.CLOB, "")

	/**
	 * The column <code>MULTI_SCHEMA.T_BOOK.CONTENT_PDF</code>.
	 */
	val CONTENT_PDF : TableField[TBookRecord, Array[scala.Byte]] = createField("CONTENT_PDF", org.jooq.impl.SQLDataType.BLOB, "")

	/**
	 * Create a <code>MULTI_SCHEMA.T_BOOK</code> table reference
	 */
	def this() = {
		this("T_BOOK", null, null)
	}

	/**
	 * Create an aliased <code>MULTI_SCHEMA.T_BOOK</code> table reference
	 */
	def this(alias : String) = {
		this(alias, org.jooq.test.oraclescala.generatedclasses.multi_schema.tables.TBook.T_BOOK, null)
	}

	private def this(alias : String, aliased : Table[TBookRecord]) = {
		this(alias, aliased, null)
	}

	override def getPrimaryKey : UniqueKey[TBookRecord] = {
		Keys.PK_T_BOOK
	}

	override def getKeys : List[ UniqueKey[TBookRecord] ] = {
		return Arrays.asList[ UniqueKey[TBookRecord] ](Keys.PK_T_BOOK)
	}

	override def getReferences : List[ ForeignKey[TBookRecord, _] ] = {
		return Arrays.asList[ ForeignKey[TBookRecord, _] ](Keys.FK_T_BOOK_AUTHOR_ID, Keys.FK_T_BOOK_CO_AUTHOR_ID, Keys.FK_T_BOOK_LANGUAGE_ID)
	}

	override def as(alias : String) : TBook = {
		new TBook(alias, this)
	}

	/**
	 * Rename this table
	 */
	def rename(name : String) : TBook = {
		new TBook(name, null)
	}
}
