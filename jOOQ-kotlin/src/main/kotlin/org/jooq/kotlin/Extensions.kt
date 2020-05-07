package org.jooq.kotlin

import org.jooq.*
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import java.util.*

@Support
inline fun <reified T: Any, R: Record> Result<R>.into(): MutableList<T> {
    return this.into(T::class.java)
}

// ----------------------------------------------------------------------------
// Class reification extensions
// ----------------------------------------------------------------------------

// ----------------------------------------------------------------------------
// Field extensions
// ----------------------------------------------------------------------------

@Support
inline fun <reified T: Any> Select<Record1<T>>.`as`(alias: String): Field<T> {
    return field(this).`as`(alias);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`as`(alias: Name): Field<T> {
    return field(this).`as`(alias);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`as`(alias: Field<*>): Field<T> {
    return field(this).`as`(alias);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.eq(value: T): Condition {
    return field(this).eq(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.eq(value: Field<T>): Condition {
    return field(this).eq(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.eq(value: Select<out Record1<T>>): Condition {
    return field(this).eq(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.eq(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).eq(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.equal(value: T): Condition {
    return field(this).equal(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.equal(value: Field<T>): Condition {
    return field(this).equal(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.equal(value: Select<out Record1<T>>): Condition {
    return field(this).equal(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.equal(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).equal(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ne(value: T): Condition {
    return field(this).ne(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ne(value: Field<T>): Condition {
    return field(this).ne(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ne(value: Select<out Record1<T>>): Condition {
    return field(this).ne(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ne(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).ne(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notEqual(value: T): Condition {
    return field(this).notEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notEqual(value: Field<T>): Condition {
    return field(this).notEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notEqual(value: Select<out Record1<T>>): Condition {
    return field(this).notEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notEqual(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).notEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.gt(value: T): Condition {
    return field(this).gt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.gt(value: Field<T>): Condition {
    return field(this).gt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.gt(value: Select<out Record1<T>>): Condition {
    return field(this).gt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.gt(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).gt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterThan(value: T): Condition {
    return field(this).greaterThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterThan(value: Field<T>): Condition {
    return field(this).greaterThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterThan(value: Select<out Record1<T>>): Condition {
    return field(this).greaterThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterThan(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).greaterThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ge(value: T): Condition {
    return field(this).ge(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ge(value: Field<T>): Condition {
    return field(this).ge(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ge(value: Select<out Record1<T>>): Condition {
    return field(this).ge(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.ge(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).ge(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterOrEqual(value: T): Condition {
    return field(this).greaterOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterOrEqual(value: Field<T>): Condition {
    return field(this).greaterOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterOrEqual(value: Select<out Record1<T>>): Condition {
    return field(this).greaterOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.greaterOrEqual(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).greaterOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lt(value: T): Condition {
    return field(this).lt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lt(value: Field<T>): Condition {
    return field(this).lt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lt(value: Select<out Record1<T>>): Condition {
    return field(this).lt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lt(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).lt(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessThan(value: T): Condition {
    return field(this).lessThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessThan(value: Field<T>): Condition {
    return field(this).lessThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessThan(value: Select<out Record1<T>>): Condition {
    return field(this).lessThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessThan(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).lessThan(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.le(value: T): Condition {
    return field(this).le(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.le(value: Field<T>): Condition {
    return field(this).le(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.le(value: Select<out Record1<T>>): Condition {
    return field(this).le(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.le(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).le(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessOrEqual(value: T): Condition {
    return field(this).lessOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessOrEqual(value: Field<T>): Condition {
    return field(this).lessOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessOrEqual(value: Select<out Record1<T>>): Condition {
    return field(this).lessOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.lessOrEqual(value: QuantifiedSelect<out Record1<T>>): Condition {
    return field(this).lessOrEqual(value);
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.asc(): SortField<T> {
    return field(this).asc();
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.desc(): SortField<T> {
    return field(this).desc();
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(values: Collection<*>): Condition {
    return field(this).`in`(values)
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(values: Result<out Record1<T>>): Condition {
    return field(this).`in`(values)
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(vararg values: T): Condition {
    return field(this).`in`(values.asList())
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(vararg values: Field<*>): Condition {
    return field(this).`in`(values.asList())
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.`in`(query: Select<out Record1<T>>): Condition {
    return field(this).`in`(query)
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(values: Collection<*>): Condition {
    return field(this).notIn(values)
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(values: Result<out Record1<T>>): Condition {
    return field(this).notIn(values)
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(vararg values: T): Condition {
    return field(this).notIn(values.asList())
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(vararg values: Field<*>): Condition {
    return field(this).notIn(values.asList())
}

@Support
inline fun <reified T: Any> Select<Record1<T>>.notIn(query: Select<out Record1<T>>): Condition {
    return field(this).notIn(query)
}

