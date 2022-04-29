/*
 * This file is generated by jOOQ.
 */
package org.jooq.example.kotlin.db.h2.tables.pojos


import java.io.Serializable
import java.time.LocalDateTime


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class Book(
    var id: Int? = null, 
    var authorId: Int? = null, 
    var coAuthorId: Int? = null, 
    var detailsId: Int? = null, 
    var title: String? = null, 
    var publishedIn: Int? = null, 
    var languageId: Int? = null, 
    var contentText: String? = null, 
    var contentPdf: ByteArray? = null, 
    var recVersion: Int? = null, 
    var recTimestamp: LocalDateTime? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("Book (")

        sb.append(id)
        sb.append(", ").append(authorId)
        sb.append(", ").append(coAuthorId)
        sb.append(", ").append(detailsId)
        sb.append(", ").append(title)
        sb.append(", ").append(publishedIn)
        sb.append(", ").append(languageId)
        sb.append(", ").append(contentText)
        sb.append(", ").append("[binary...]")
        sb.append(", ").append(recVersion)
        sb.append(", ").append(recTimestamp)

        sb.append(")")
        return sb.toString()
    }
}