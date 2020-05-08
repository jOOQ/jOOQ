package org.jooq.kotlin

import org.jooq.SQLDialect.H2
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.jooq.impl.SQLDataType.BOOLEAN
import org.jooq.impl.SQLDataType.INTEGER
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ExtensionsTest {

    val i = field(unquotedName("i"), INTEGER)
    val j = field(unquotedName("j"), INTEGER)
    val b1 = field(unquotedName("b1"), BOOLEAN)
    val b2 = field(unquotedName("b2"), BOOLEAN)
    val dsl = DSL.using(H2)

    @Test
    fun testBooleanFields() {
        assertEquals(condition(b1).and(b2), b1.and(b2))
        assertEquals(condition(b1).andNot(b2), b1.andNot(b2))
        assertEquals(condition(b1).or(b2), b1.or(b2))
        assertEquals(condition(b1).orNot(b2), b1.orNot(b2))
        assertEquals(condition(b1).not(), b1.not())
    }

    @Test
    fun testScalarSubqueries() {
        assertEquals(field(select(i)).`as`("i"), select(i).`as`("i"))
        assertEquals(field(select(i)).`as`(name("i")), select(i).`as`(name("i")))
        assertEquals(field(select(i)).`as`(i), select(i).`as`(i))

        assertEquals(field(select(i)).eq(1), select(i).eq(1))
        assertEquals(field(select(i)).eq(value(1)), select(i).eq(value(1)))
        assertEquals(field(select(i)).eq(select(value(1))), select(i).eq(select(value(1))))
        assertEquals(field(select(i)).eq(any(select(value(1)))), select(i).eq(any(select(value(1)))))

        assertEquals(field(select(i)).ne(1), select(i).ne(1))
        assertEquals(field(select(i)).ne(value(1)), select(i).ne(value(1)))
        assertEquals(field(select(i)).ne(select(value(1))), select(i).ne(select(value(1))))
        assertEquals(field(select(i)).ne(any(select(value(1)))), select(i).ne(any(select(value(1)))))

        assertEquals(field(select(i)).gt(1), select(i).gt(1))
        assertEquals(field(select(i)).gt(value(1)), select(i).gt(value(1)))
        assertEquals(field(select(i)).gt(select(value(1))), select(i).gt(select(value(1))))
        assertEquals(field(select(i)).gt(any(select(value(1)))), select(i).gt(any(select(value(1)))))

        assertEquals(field(select(i)).ge(1), select(i).ge(1))
        assertEquals(field(select(i)).ge(value(1)), select(i).ge(value(1)))
        assertEquals(field(select(i)).ge(select(value(1))), select(i).ge(select(value(1))))
        assertEquals(field(select(i)).ge(any(select(value(1)))), select(i).ge(any(select(value(1)))))

        assertEquals(field(select(i)).lt(1), select(i).lt(1))
        assertEquals(field(select(i)).lt(value(1)), select(i).lt(value(1)))
        assertEquals(field(select(i)).lt(select(value(1))), select(i).lt(select(value(1))))
        assertEquals(field(select(i)).lt(any(select(value(1)))), select(i).lt(any(select(value(1)))))

        assertEquals(field(select(i)).le(1), select(i).le(1))
        assertEquals(field(select(i)).le(value(1)), select(i).le(value(1)))
        assertEquals(field(select(i)).le(select(value(1))), select(i).le(select(value(1))))
        assertEquals(field(select(i)).le(any(select(value(1)))), select(i).le(any(select(value(1)))))

        assertEquals(field(select(i)).asc(), select(i).asc())
        assertEquals(field(select(i)).desc(), select(i).desc())

        assertEquals(field(select(i)).`in`(1, 2), select(i).`in`(1, 2))
        assertEquals(field(select(i)).`in`(value(1), value(2)), select(i).`in`(value(1), value(2)))
        assertEquals(field(select(i)).`in`(select(value(1))), select(i).`in`(select(value(1))))

        assertEquals(field(select(i)).notIn(1, 2), select(i).notIn(1, 2))
        assertEquals(field(select(i)).notIn(value(1), value(2)), select(i).notIn(value(1), value(2)))
        assertEquals(field(select(i)).notIn(select(value(1))), select(i).notIn(select(value(1))))
    }
}