/**
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.example.kotlin

import java.util.Properties

import org.jooq.*
import org.jooq.impl.*
import org.jooq.impl.DSL.*

import org.jooq.example.db.h2.Tables.*
import java.sql.*

fun main(args: Array<String>) {

    // This example project simply uses a standalone JDBC connection, but you're free to use any other
    // means to connect to your database, including standard DataSources
    val properties = Properties();
    properties.load(properties::class.java.getResourceAsStream("/config.properties"));

    DriverManager.getConnection(
        properties.getProperty("db.url"),
        properties.getProperty("db.username"),
        properties.getProperty("db.password")
    ).use {
        val ctx = DSL.using(it)
        val a = AUTHOR
        val b = BOOK

        // This example shows just standard jOOQ API usage, which selects a couple of columns from a join expression
        // and then uses a closure to iterate over results. String interpolation is used, along with the implicit
        // it variable and the nice Record.get(Field) syntax sugar using it[field] syntax
        header("Books and their authors")
        ctx.select(a.FIRST_NAME, a.LAST_NAME, b.TITLE)
           .from(a)
           .join(b).on(a.ID.eq(b.AUTHOR_ID))
           .orderBy(1, 2, 3)
           .forEach {
               println("${it[b.TITLE]} by ${it[a.FIRST_NAME]} ${it[a.LAST_NAME]}")
           }

        // Kotlin allows for destructuring any type that has component1(), component2(), component3(), ... methods
        // into a tuple of local variables. These methods are currently added ad-hoc as operators further down in this
        // file (scroll down). Future support for these methods is on the roadmap:
        // https://github.com/jOOQ/jOOQ/issues/6245
        header("Books and their authors with destructuring")
        for ((first, last, title) in ctx.select(a.FIRST_NAME, a.LAST_NAME, b.TITLE)
           .from(a)
           .join(b).on(a.ID.eq(b.AUTHOR_ID))
           .orderBy(1, 2, 3))
               println("$title by $first $last")

        // Generated records (available through selectFrom()) contain getters (and setters) for each column.
        // Kotlin allows for property access syntax of Java getters!
        header("An author")
        val author = ctx.selectFrom(a).where(a.ID.eq(1)).fetchOne();
        println("${author.firstName} ${author.lastName}")

        // Setters can profit from this property access syntax as well.
        header("Creating a new author")
        val author2 = ctx.newRecord(a);
        author2.firstName = "Alice"
        author2.lastName = "Doe"
        author2.store()
        println("${author2.firstName} ${author2.lastName}")

        // With "imports" the namespace of an object for a closure, so we can avoid repeating the owner
        // reference of the properties (and methods) every time
        header("Using the with 'clause'")
        with (author2) {
            firstName = "Bob"
            lastName = "Doe"
            store()
        }

        // This also works with tables, and their columns!
        header("With can be used with statements too, to locally import tables")
        with (a) {
            ctx.select(FIRST_NAME, LAST_NAME)
               .from(a)
               .where(ID.lt(5))
               .orderBy(ID)
               .fetch {
                   println("${it[FIRST_NAME]} ${it[LAST_NAME]}")
               }
        }

        // Apply is a nice method that also closes over an object making all its methods available without the
        // need to explicitly reference the owner object. That's very very nice for this type of dynamic SQL!
        header("Conditional query clauses")
        val filtering = true;
        val joining = true;
        ctx.select(a.FIRST_NAME, a.LAST_NAME, if (joining) count() else value(""))
           .from(a)
           .apply { if (filtering) where(a.ID.eq(1)) }
           .apply { if (joining) join(b).on(a.ID.eq(b.AUTHOR_ID)) }
           .apply { if (joining) groupBy(a.FIRST_NAME, a.LAST_NAME) }
           .orderBy(a.ID)
           .fetch {
               println("${it[a.FIRST_NAME]} ${it[a.LAST_NAME]} ${if (joining) it[count()] else ""}")
           }

        // Map (key, value) destructuring in foreach loops!
        header("As a map")
        for ((k, v) in author2.intoMap())
            println("${k.padEnd(20)} = $v")

        // More destructuring
        header("As maps")
        for (r in ctx.fetch(b))
            for ((k, v) in r.intoMap())
                println("${r[b.ID]}: ${k.padEnd(20)} = $v")

        // We can use inline functions that we design ourselves very easily. E.g. ilike() is not part of the
        // jOOQ API. It's defined further down in this file
        header("Custom jOOQ API extensions")
        println("${ctx.select(b.TITLE).from(b).where(b.TITLE.ilike("%animal%")).fetchOne(b.TITLE)}")

        // Classic elvis operator, etc.
        header("Null safe dereferencing")
        println("${ctx.fetchOne(b, b.ID.eq(5))?.title ?: "book not found"}")

        // Some operators can profit from "overloading" in Kotlin. E.g.
        // - the unary minus operator "-" maps to Field.unaryMinus()
        // - the binary plus operator "+" maps to Field.plus(Number)
        // - the unary negation operator "!" maps to Condition.not()
        // - etc.
        header("Operator overloading")
        ctx.select(a.FIRST_NAME, a.LAST_NAME, -count(), a.ID + 3)
           .from(a)
           .join(b).on(a.ID.eq(b.AUTHOR_ID))
           .where(!a.ID.gt(5))
           .groupBy(a.FIRST_NAME, a.LAST_NAME, a.ID)
           .fetch()
           .forEach { (first, last, count, id) ->
               println("Actor ID ${id - 3}: $first $last wrote ${-count} books")
           }

        // Don't we wish for multiline strings in Java?
        header("Using multiline strings with the plain SQL API")
        ctx.resultQuery("""
            SELECT *
            FROM (
              VALUES (1, 'a'),
                     (2, 'b')
            ) AS t
            """)
            .fetch()
            .forEach {
                println("${it.intoMap()}")
            }

        // If you parse a SQL (multiline) string with jOOQ, jOOQ will try to translate the syntax to
        header("Using multiline strings with the parser")
        val colX = field("x")
        val colY = field("y")
        ctx.parser()
           .parseResultQuery("""
            SELECT *
            FROM (
              VALUES (1, 'a'),
                     (2, 'b')
            ) AS t(${colX.name}, ${colY.name}) -- This feature (derived column lists) isn't really available in H2!
            """)
           .fetch()
           .forEach {
               println("${it[colX]}, ${it[colY]}")
           }
    }
}

/**
 * Just some nice formatted header printing
 */
fun header(text : String) {
    println()
    println(text)
    println(text.toCharArray().map { _ -> '-' }.joinToString(separator = ""))
}

// Operators for Kotlin language-supported operator overloading
// Support for these will be added to jOOQ where not already available:
//   https://github.com/jOOQ/jOOQ/issues/6246
// ------------------------------------------------------------
operator fun <T, F : Field<T>> F.unaryMinus() : F {
    return this.neg() as F;
}

operator fun <T, F : Field<T>> F.times(n : Number) : F {
    return this.mul(n) as F;
}

// Conveniently enhance jOOQ with your own custom DSL API
// ------------------------------------------------------
inline fun <F : Field<String>> F.ilike(field : String): Condition {
    return condition("{0} ilike {1}", this, field);
}

inline fun <F : Field<String>> F.ilike(field : Field<String>): Condition {
    return condition("{0} ilike {1}", this, field);
}

// Destructuring records into sets of local variables
// Will be supported by jOOQ out-of-the-box:
//   https://github.com/jOOQ/jOOQ/issues/6245
// --------------------------------------------------
operator fun <T, R : Record1<T>> R.component1() : T {
    return this.value1();
}

operator fun <T, R : Record2<T, *>> R.component1() : T {
    return this.value1();
}

operator fun <T, R : Record2<*, T>> R.component2() : T {
    return this.value2();
}

operator fun <T, R : Record3<T, *, *>> R.component1() : T {
    return this.value1();
}

operator fun <T, R : Record3<*, T, *>> R.component2() : T {
    return this.value2();
}

operator fun <T, R : Record3<*, *, T>> R.component3() : T {
    return this.value3();
}

operator fun <T, R : Record4<T, *, *, *>> R.component1() : T {
    return this.value1();
}

operator fun <T, R : Record4<*, T, *, *>> R.component2() : T {
    return this.value2();
}

operator fun <T, R : Record4<*, *, T, *>> R.component3() : T {
    return this.value3();
}

operator fun <T, R : Record4<*, *, *, T>> R.component4() : T {
    return this.value4();
}

// ... more methods ...
