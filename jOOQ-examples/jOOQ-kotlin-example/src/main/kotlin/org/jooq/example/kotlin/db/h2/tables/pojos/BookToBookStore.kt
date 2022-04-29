/*
 * This file is generated by jOOQ.
 */
package org.jooq.example.kotlin.db.h2.tables.pojos


import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class BookToBookStore(
    var bookStoreName: String? = null, 
    var bookId: Int? = null, 
    var stock: Int? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("BookToBookStore (")

        sb.append(bookStoreName)
        sb.append(", ").append(bookId)
        sb.append(", ").append(stock)

        sb.append(")")
        return sb.toString()
    }
}