package org.jooq.kotlin

import org.jetbrains.annotations.Blocking
import org.jooq.*
import org.jooq.SQLDialect.*
import org.jooq.impl.DSL.*
import java.util.stream.Collector

// ----------------------------------------------------------------------------
// Extensions to collect Field<Result<Record[N]>> into other types
// ----------------------------------------------------------------------------

fun <E, R : Record> Field<Result<R>>.collecting(collector: Collector<R, *, E>): Field<E> = convertFrom { it.collect(collector) }

inline fun <reified E> Field<Result<Record1<E>>>.intoArray(): Field<Array<E>> = collecting(Records.intoArray(E::class.java))

inline fun <R : Record, reified E> Field<Result<R>>.intoArray(noinline mapper: (R) -> E): Field<Array<E>> = collecting(Records.intoArray(E::class.java, mapper))

fun <K, V, R : Record2<K, V>> Field<Result<R>>.intoGroups(): Field<Map<K, List<V>>> = collecting(Records.intoGroups())

fun <K, R : Record> Field<Result<R>>.intoGroups(keyMapper: (R) -> K): Field<Map<K, List<R>>> = collecting(Records.intoGroups(keyMapper))

fun <K, V, R : Record> Field<Result<R>>.intoGroups(keyMapper: (R) -> K, valueMapper: (R) -> V): Field<Map<K, List<V>>> = collecting(Records.intoGroups(keyMapper, valueMapper))

fun <E, R : Record1<E>> Field<Result<R>>.intoList(): Field<List<E>> = collecting(Records.intoList())

fun <E, R : Record> Field<Result<R>>.intoList(mapper: (R) -> E): Field<List<E>> = collecting(Records.intoList(mapper))

fun <K, V> Field<Result<Record2<K, V>>>.intoMap(): Field<Map<K, V>> = collecting(Records.intoMap())

fun <K, R : Record> Field<Result<R>>.intoMap(keyMapper: (R) -> K): Field<Map<K, R>> = collecting(Records.intoMap(keyMapper))

fun <K, V, R : Record> Field<Result<R>>.intoMap(keyMapper: (R) -> K, valueMapper: (R) -> V): Field<Map<K, V>> = collecting(Records.intoMap(keyMapper, valueMapper))

fun <K, R : Record> Field<Result<R>>.intoResultGroups(keyMapper: (R) -> K): Field<Map<K, Result<R>>> = collecting(Records.intoResultGroups(keyMapper))

fun <K, V : Record, R : Record> Field<Result<R>>.intoResultGroups(keyMapper: (R) -> K, valueMapper: (R) -> V): Field<Map<K, Result<V>>> = collecting(Records.intoResultGroups(keyMapper, valueMapper))

fun <E, R : Record1<E>> Field<Result<R>>.intoSet(): Field<Set<E>> = collecting(Records.intoSet())

fun <E, R : Record> Field<Result<R>>.intoSet(mapper: (R) -> E): Field<Set<E>> = collecting(Records.intoSet(mapper))

// ----------------------------------------------------------------------------
// Extensions to collect ResultQuery<Record[N]> into other types
// ----------------------------------------------------------------------------

@Blocking
inline fun <reified E> ResultQuery<Record1<E>>.fetchArray(): Array<E> = collect(Records.intoArray(E::class.java))

@Blocking
fun <K, V, R : Record2<K, V>> ResultQuery<R>.fetchGroups(): Map<K, List<V>> = collect(Records.intoGroups())

@Blocking
fun <E, R : Record1<E>> ResultQuery<R>.fetchList(): List<E> = collect(Records.intoList())

@Blocking
fun <K, V> ResultQuery<Record2<K, V>>.fetchMap(): Map<K, V> = collect(Records.intoMap())

@Blocking
fun <E, R : Record1<E>> ResultQuery<R>.fetchSet(): Set<E> = collect(Records.intoSet())

// ----------------------------------------------------------------------------
// Extensions to collect Result<Record[N]> into other types
// ----------------------------------------------------------------------------

inline fun <reified E> Result<Record1<E>>.intoArray(): Array<E> = collect(Records.intoArray(E::class.java))

fun <K, V, R : Record2<K, V>> Result<R>.intoGroups(): Map<K, List<V>> = collect(Records.intoGroups())

fun <E, R : Record1<E>> Result<R>.intoList(): List<E> = collect(Records.intoList())

fun <K, V> Result<Record2<K, V>>.intoMap(): Map<K, V> = collect(Records.intoMap())

fun <E, R : Record1<E>> Result<R>.intoSet(): Set<E> = collect(Records.intoSet())



// ----------------------------------------------------------------------------
// Extensions to map Field<Result<Record[N]>> to more convenient Field<List<E>>
// As well as Field<Record[N]> to Field<E>
// ----------------------------------------------------------------------------

// [jooq-tools] START [mapping]

@JvmName("mappingRecord")
fun <T1, E> Field<Record1<T1>>.mapping(f: Function1<T1, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, E> Field<Record2<T1, T2>>.mapping(f: Function2<T1, T2, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, E> Field<Record3<T1, T2, T3>>.mapping(f: Function3<T1, T2, T3, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, E> Field<Record4<T1, T2, T3, T4>>.mapping(f: Function4<T1, T2, T3, T4, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, E> Field<Record5<T1, T2, T3, T4, T5>>.mapping(f: Function5<T1, T2, T3, T4, T5, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, E> Field<Record6<T1, T2, T3, T4, T5, T6>>.mapping(f: Function6<T1, T2, T3, T4, T5, T6, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, E> Field<Record7<T1, T2, T3, T4, T5, T6, T7>>.mapping(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, E> Field<Record8<T1, T2, T3, T4, T5, T6, T7, T8>>.mapping(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> Field<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>.mapping(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> Field<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>.mapping(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> Field<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>.mapping(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> Field<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>.mapping(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> Field<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>.mapping(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> Field<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>.mapping(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> Field<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>.mapping(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> Field<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>.mapping(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> Field<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>.mapping(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> Field<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>.mapping(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> Field<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>.mapping(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> Field<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>.mapping(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> Field<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>.mapping(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

@JvmName("mappingRecord")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> Field<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>.mapping(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): Field<E> = convertFrom { Records.mapping(f).apply(it) }

fun <T1, E> Field<Result<Record1<T1>>>.mapping(f: Function1<T1, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, E> Field<Result<Record2<T1, T2>>>.mapping(f: Function2<T1, T2, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, E> Field<Result<Record3<T1, T2, T3>>>.mapping(f: Function3<T1, T2, T3, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, E> Field<Result<Record4<T1, T2, T3, T4>>>.mapping(f: Function4<T1, T2, T3, T4, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, E> Field<Result<Record5<T1, T2, T3, T4, T5>>>.mapping(f: Function5<T1, T2, T3, T4, T5, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, E> Field<Result<Record6<T1, T2, T3, T4, T5, T6>>>.mapping(f: Function6<T1, T2, T3, T4, T5, T6, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, E> Field<Result<Record7<T1, T2, T3, T4, T5, T6, T7>>>.mapping(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, E> Field<Result<Record8<T1, T2, T3, T4, T5, T6, T7, T8>>>.mapping(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> Field<Result<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>>.mapping(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> Field<Result<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>>.mapping(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> Field<Result<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>>.mapping(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> Field<Result<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>>.mapping(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> Field<Result<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>>.mapping(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> Field<Result<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>>.mapping(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> Field<Result<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>>.mapping(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> Field<Result<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>>.mapping(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> Field<Result<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>>.mapping(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> Field<Result<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>>.mapping(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> Field<Result<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>>.mapping(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> Field<Result<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>>.mapping(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> Field<Result<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>>.mapping(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> Field<Result<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>>.mapping(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): Field<List<E>> = convertFrom { it.map(Records.mapping(f)) }

// [jooq-tools] END [mapping]

// ----------------------------------------------------------------------------
// Extensions to make Field<Boolean> a Condition
// ----------------------------------------------------------------------------

@Support
fun Field<Boolean>.and(other: Condition): Condition = condition(this).and(other)

@Support
fun Field<Boolean>.and(other: Field<Boolean>): Condition = condition(this).and(other)

@Support
@PlainSQL
fun Field<Boolean>.and(sql: SQL): Condition = condition(this).and(sql)

@Support
@PlainSQL
fun Field<Boolean>.and(sql: String): Condition = condition(this).and(sql)

@Support
@PlainSQL
fun Field<Boolean>.and(sql: String, vararg bindings: Any): Condition = condition(this).and(sql, bindings)

@Support
@PlainSQL
fun Field<Boolean>.and(sql: String, vararg parts: QueryPart): Condition = condition(this).and(sql, parts)

@Support
fun Field<Boolean>.andNot(other: Condition): Condition = condition(this).andNot(other)

@Support
fun Field<Boolean>.andNot(other: Field<Boolean>): Condition = condition(this).andNot(other)

@Support
fun Field<Boolean>.andExists(select: Select<*>): Condition = condition(this).andExists(select)

@Support
fun Field<Boolean>.andNotExists(select: Select<*>): Condition = condition(this).andNotExists(select)

@Support
fun Field<Boolean>.or(other: Condition): Condition = condition(this).or(other)

@Support
fun Field<Boolean>.or(other: Field<Boolean>): Condition = condition(this).or(other)

@Support
@PlainSQL
fun Field<Boolean>.or(sql: SQL): Condition = condition(this).or(sql)

@Support
@PlainSQL
fun Field<Boolean>.or(sql: String): Condition = condition(this).or(sql)

@Support
@PlainSQL
fun Field<Boolean>.or(sql: String, vararg bindings: Any): Condition = condition(this).or(sql)

@Support
@PlainSQL
fun Field<Boolean>.or(sql: String, vararg parts: QueryPart): Condition = condition(this).or(sql, parts)

@Support
fun Field<Boolean>.orNot(other: Condition): Condition = condition(this).orNot(other)

@Support
fun Field<Boolean>.orNot(other: Field<Boolean>): Condition = condition(this).orNot(other)

@Support
fun Field<Boolean>.orExists(select: Select<*>): Condition = condition(this).orExists(select)

@Support
fun Field<Boolean>.orNotExists(select: Select<*>): Condition = condition(this).orNotExists(select)

@Support
fun Field<Boolean>.not(): Condition = condition(this).not()


// ----------------------------------------------------------------------------
// Extensions to extract fields from Tables
// ----------------------------------------------------------------------------

@Support
operator fun TableLike<*>.get(index: Int) = this.field(index)

@Support
operator fun TableLike<*>.get(name: Name) = this.field(name)

@Support
operator fun TableLike<*>.get(name: String) = this.field(name)

@Support
operator fun <T> TableLike<*>.get(field: Field<T>) = this.field(field)

// ----------------------------------------------------------------------------
// Extensions to make Field<T[]> aware of its being an array
// ----------------------------------------------------------------------------

@Support
operator fun <T> Field<Array<T>?>.get(index: Int) = arrayGet(this, index)

@Support
operator fun <T> Field<Array<T>?>.get(index: Field<Int?>) = arrayGet(this, index)

// ----------------------------------------------------------------------------
// Extensions to make Field<JSON> and Field<JSONB> aware of its being JSON
// ----------------------------------------------------------------------------

@Support
@JvmName("jsonGetElement")
operator fun Field<JSON?>.get(index: Int) = jsonGetElement(this, index)

@Support
@JvmName("jsonGetElement")
operator fun Field<JSON?>.get(index: Field<Int?>) = jsonGetElement(this, index)

@Support
@JvmName("jsonGetAttribute")
operator fun Field<JSON?>.get(name: String) = jsonGetAttribute(this, name)

@Support
@JvmName("jsonGetAttribute")
operator fun Field<JSON?>.get(name: Field<String?>) = jsonGetAttribute(this, name)

@Support
@JvmName("jsonbGetElement")
operator fun Field<JSONB?>.get(index: Int) = jsonbGetElement(this, index)

@Support
@JvmName("jsonbGetElement")
operator fun Field<JSONB?>.get(index: Field<Int?>) = jsonbGetElement(this, index)

@Support
@JvmName("jsonbGetAttribute")
operator fun Field<JSONB?>.get(name: String) = jsonbGetAttribute(this, name)

@Support
@JvmName("jsonbGetAttribute")
operator fun Field<JSONB?>.get(name: Field<String?>) = jsonbGetAttribute(this, name)

// ----------------------------------------------------------------------------
// Extensions to make Select<Record1<T>> a scalar subquery of type Field<T>
// ----------------------------------------------------------------------------

@Support
fun <T> Select<Record1<T>>.`as`(alias: String): Field<T> = field(this).`as`(alias)

@Support
fun <T> Select<Record1<T>>.`as`(alias: Name): Field<T> = field(this).`as`(alias)

@Support
fun <T> Select<Record1<T>>.`as`(alias: Field<*>): Field<T> = field(this).`as`(alias)

@Support
fun <T> Select<Record1<T>>.eq(value: T): Condition = field(this).eq(value)

@Support
fun <T> Select<Record1<T>>.eq(value: Field<T>): Condition = field(this).eq(value)

@Support
fun <T> Select<Record1<T>>.equal(value: T): Condition = field(this).equal(value)

@Support
fun <T> Select<Record1<T>>.equal(value: Field<T>): Condition = field(this).equal(value)

@Support
fun <T> Select<Record1<T>>.ne(value: T): Condition = field(this).ne(value)

@Support
fun <T> Select<Record1<T>>.ne(value: Field<T>): Condition = field(this).ne(value)

@Support
fun <T> Select<Record1<T>>.notEqual(value: T): Condition = field(this).notEqual(value)

@Support
fun <T> Select<Record1<T>>.notEqual(value: Field<T>): Condition = field(this).notEqual(value)

@Support
fun <T> Select<Record1<T>>.gt(value: T): Condition = field(this).gt(value)

@Support
fun <T> Select<Record1<T>>.gt(value: Field<T>): Condition = field(this).gt(value)

@Support
fun <T> Select<Record1<T>>.greaterThan(value: T): Condition = field(this).greaterThan(value)

@Support
fun <T> Select<Record1<T>>.greaterThan(value: Field<T>): Condition = field(this).greaterThan(value)

@Support
fun <T> Select<Record1<T>>.ge(value: T): Condition = field(this).ge(value)

@Support
fun <T> Select<Record1<T>>.ge(value: Field<T>): Condition = field(this).ge(value)

@Support
fun <T> Select<Record1<T>>.greaterOrEqual(value: T): Condition = field(this).greaterOrEqual(value)

@Support
fun <T> Select<Record1<T>>.greaterOrEqual(value: Field<T>): Condition =
        field(this).greaterOrEqual(value)

@Support
fun <T> Select<Record1<T>>.lt(value: T): Condition = field(this).lt(value)

@Support
fun <T> Select<Record1<T>>.lt(value: Field<T>): Condition = field(this).lt(value)

@Support
fun <T> Select<Record1<T>>.lessThan(value: T): Condition = field(this).lessThan(value)

@Support
fun <T> Select<Record1<T>>.lessThan(value: Field<T>): Condition = field(this).lessThan(value)

@Support
fun <T> Select<Record1<T>>.le(value: T): Condition = field(this).le(value)

@Support
fun <T> Select<Record1<T>>.le(value: Field<T>): Condition = field(this).le(value)

@Support
fun <T> Select<Record1<T>>.lessOrEqual(value: T): Condition = field(this).lessOrEqual(value)

@Support
fun <T> Select<Record1<T>>.lessOrEqual(value: Field<T>): Condition = field(this).lessOrEqual(value)

@Support
fun <T> Select<Record1<T>>.asc(): SortField<T> = field(this).asc()

@Support
fun <T> Select<Record1<T>>.desc(): SortField<T> = field(this).desc()

@Support
fun <T> Select<Record1<T>>.`in`(values: Collection<*>): Condition = field(this).`in`(values)

@Support
fun <T> Select<Record1<T>>.`in`(values: Result<out Record1<T>>): Condition = field(this).`in`(values)

@Support
fun <T> Select<Record1<T>>.`in`(vararg values: T): Condition = field(this).`in`(values.asList())

@Support
fun <T> Select<Record1<T>>.`in`(vararg values: Field<*>): Condition = field(this).`in`(values.asList())

@Support
fun <T> Select<Record1<T>>.notIn(values: Collection<*>): Condition = field(this).notIn(values)

@Support
fun <T> Select<Record1<T>>.notIn(values: Result<out Record1<T>>): Condition = field(this).notIn(values)

@Support
fun <T> Select<Record1<T>>.notIn(vararg values: T): Condition = field(this).notIn(values.asList())

@Support
fun <T> Select<Record1<T>>.notIn(vararg values: Field<*>): Condition = field(this).notIn(values.asList())
