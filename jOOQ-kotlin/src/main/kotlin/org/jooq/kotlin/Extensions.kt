package org.jooq.kotlin

import org.jooq.*
import org.jooq.impl.DSL.*

// ----------------------------------------------------------------------------
// Extensions to make Field<Boolean> a Condition
// ----------------------------------------------------------------------------

@Support
inline fun Field<Boolean>.and(other: Condition): Condition = condition(this).and(other)

@Support
inline fun Field<Boolean>.and(other: Field<Boolean>): Condition = condition(this).and(other)

@Support
@PlainSQL
inline fun Field<Boolean>.and(sql: SQL): Condition = condition(this).and(sql)

@Support
@PlainSQL
inline fun Field<Boolean>.and(sql: String): Condition = condition(this).and(sql)

@Support
@PlainSQL
inline fun Field<Boolean>.and(sql: String, vararg bindings: Any): Condition = condition(this).and(sql, bindings)

@Support
@PlainSQL
inline fun Field<Boolean>.and(sql: String, vararg parts: QueryPart): Condition = condition(this).and(sql, parts)

@Support
inline fun Field<Boolean>.andNot(other: Condition): Condition = condition(this).andNot(other)

@Support
inline fun Field<Boolean>.andNot(other: Field<Boolean>): Condition = condition(this).andNot(other)

@Support
inline fun Field<Boolean>.andExists(select: Select<*>): Condition = condition(this).andExists(select)

@Support
inline fun Field<Boolean>.andNotExists(select: Select<*>): Condition = condition(this).andNotExists(select)

@Support
inline fun Field<Boolean>.or(other: Condition): Condition = condition(this).or(other)

@Support
inline fun Field<Boolean>.or(other: Field<Boolean>): Condition = condition(this).or(other)

@Support
@PlainSQL
inline fun Field<Boolean>.or(sql: SQL): Condition = condition(this).or(sql)

@Support
@PlainSQL
inline fun Field<Boolean>.or(sql: String): Condition = condition(this).or(sql)

@Support
@PlainSQL
inline fun Field<Boolean>.or(sql: String, vararg bindings: Any): Condition = condition(this).or(sql)

@Support
@PlainSQL
inline fun Field<Boolean>.or(sql: String, vararg parts: QueryPart): Condition = condition(this).or(sql, parts)

@Support
inline fun Field<Boolean>.orNot(other: Condition): Condition = condition(this).orNot(other)

@Support
inline fun Field<Boolean>.orNot(other: Field<Boolean>): Condition = condition(this).orNot(other)

@Support
inline fun Field<Boolean>.orExists(select: Select<*>): Condition = condition(this).orExists(select)

@Support
inline fun Field<Boolean>.orNotExists(select: Select<*>): Condition = condition(this).orNotExists(select)

@Support
inline fun Field<Boolean>.not(): Condition = condition(this).not()


// ----------------------------------------------------------------------------
// Extensions to extract fields from Tables
// ----------------------------------------------------------------------------

@Support
inline operator fun TableLike<*>.get(index: Int) = this.field(index)

@Support
inline operator fun TableLike<*>.get(name: Name) = this.field(name)

@Support
inline operator fun TableLike<*>.get(name: String) = this.field(name)

@Support
inline operator fun <T> TableLike<*>.get(field: Field<T>) = this.field(field)

// ----------------------------------------------------------------------------
// Extensions to make Field<T[]> aware of its being an array
// ----------------------------------------------------------------------------

@Support
inline operator fun <T> Field<Array<T>>.get(index: Int) = arrayGet(this, index)

@Support
inline operator fun <T> Field<Array<T>>.get(index: Field<Int>) = arrayGet(this, index)

// ----------------------------------------------------------------------------
// Extensions to make Select<Record1<T>> a scalar subquery of type Field<T>
// ----------------------------------------------------------------------------

@Support
inline fun <reified T: Any> Select<Record1<T>>.`as`(alias: String): Field<T> = field(this).`as`(alias)

@Support
inline fun <reified T: Any> Select<Record1<T>>.`as`(alias: Name): Field<T> = field(this).`as`(alias)

@Support
inline fun <reified T: Any> Select<Record1<T>>.`as`(alias: Field<*>): Field<T> = field(this).`as`(alias)

@Support
inline fun <reified T: Any> Select<Record1<T>>.eq(value: T): Condition = field(this).eq(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.eq(value: Field<T>): Condition = field(this).eq(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.equal(value: T): Condition = field(this).equal(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.equal(value: Field<T>): Condition = field(this).equal(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.ne(value: T): Condition = field(this).ne(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.ne(value: Field<T>): Condition = field(this).ne(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.notEqual(value: T): Condition = field(this).notEqual(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.notEqual(value: Field<T>): Condition = field(this).notEqual(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.gt(value: T): Condition = field(this).gt(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.gt(value: Field<T>): Condition = field(this).gt(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterThan(value: T): Condition = field(this).greaterThan(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterThan(value: Field<T>): Condition = field(this).greaterThan(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.ge(value: T): Condition = field(this).ge(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.ge(value: Field<T>): Condition = field(this).ge(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterOrEqual(value: T): Condition = field(this).greaterOrEqual(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterOrEqual(value: Field<T>): Condition =
        field(this).greaterOrEqual(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.lt(value: T): Condition = field(this).lt(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.lt(value: Field<T>): Condition = field(this).lt(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessThan(value: T): Condition = field(this).lessThan(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessThan(value: Field<T>): Condition = field(this).lessThan(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.le(value: T): Condition = field(this).le(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.le(value: Field<T>): Condition = field(this).le(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessOrEqual(value: T): Condition = field(this).lessOrEqual(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessOrEqual(value: Field<T>): Condition = field(this).lessOrEqual(value)

@Support
inline fun <reified T: Any> Select<Record1<T>>.asc(): SortField<T> = field(this).asc()

@Support
inline fun <reified T: Any> Select<Record1<T>>.desc(): SortField<T> = field(this).desc()

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(values: Collection<*>): Condition = field(this).`in`(values)

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(values: Result<out Record1<T>>): Condition = field(this).`in`(values)

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(vararg values: T): Condition = field(this).`in`(values.asList())

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(vararg values: Field<*>): Condition = field(this).`in`(values.asList())

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(values: Collection<*>): Condition = field(this).notIn(values)

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(values: Result<out Record1<T>>): Condition = field(this).notIn(values)

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(vararg values: T): Condition = field(this).notIn(values.asList())

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(vararg values: Field<*>): Condition = field(this).notIn(values.asList())
