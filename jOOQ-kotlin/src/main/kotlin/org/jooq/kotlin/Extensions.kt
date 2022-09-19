package org.jooq.kotlin

import org.jooq.*
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
// Extensions to map Field<Result<Record[N]>> to more convenient Field<List<E>>
// As well as Field<Record[N]> to Field<E>
// ----------------------------------------------------------------------------

// [jooq-tools] START [mapping]

fun <T1, E> ignoreNulls(f: Function1<T1, E>): Function1<T1?, E> = Function1<T1?, E> { t1: T1? -> f.apply(t1!!) }

fun <T1, T2, E> ignoreNulls(f: Function2<T1, T2, E>): Function2<T1?, T2?, E> = Function2<T1?, T2?, E> { t1: T1?, t2: T2? -> f.apply(t1!!, t2!!) }

fun <T1, T2, T3, E> ignoreNulls(f: Function3<T1, T2, T3, E>): Function3<T1?, T2?, T3?, E> = Function3<T1?, T2?, T3?, E> { t1: T1?, t2: T2?, t3: T3? -> f.apply(t1!!, t2!!, t3!!) }

fun <T1, T2, T3, T4, E> ignoreNulls(f: Function4<T1, T2, T3, T4, E>): Function4<T1?, T2?, T3?, T4?, E> = Function4<T1?, T2?, T3?, T4?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4? -> f.apply(t1!!, t2!!, t3!!, t4!!) }

fun <T1, T2, T3, T4, T5, E> ignoreNulls(f: Function5<T1, T2, T3, T4, T5, E>): Function5<T1?, T2?, T3?, T4?, T5?, E> = Function5<T1?, T2?, T3?, T4?, T5?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!) }

fun <T1, T2, T3, T4, T5, T6, E> ignoreNulls(f: Function6<T1, T2, T3, T4, T5, T6, E>): Function6<T1?, T2?, T3?, T4?, T5?, T6?, E> = Function6<T1?, T2?, T3?, T4?, T5?, T6?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!) }

fun <T1, T2, T3, T4, T5, T6, T7, E> ignoreNulls(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): Function7<T1?, T2?, T3?, T4?, T5?, T6?, T7?, E> = Function7<T1?, T2?, T3?, T4?, T5?, T6?, T7?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, E> ignoreNulls(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): Function8<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, E> = Function8<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> ignoreNulls(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): Function9<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, E> = Function9<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> ignoreNulls(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): Function10<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, E> = Function10<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> ignoreNulls(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): Function11<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, E> = Function11<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> ignoreNulls(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): Function12<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, E> = Function12<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> ignoreNulls(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): Function13<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, E> = Function13<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> ignoreNulls(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): Function14<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, E> = Function14<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> ignoreNulls(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): Function15<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, E> = Function15<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> ignoreNulls(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): Function16<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, E> = Function16<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> ignoreNulls(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): Function17<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, E> = Function17<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16?, t17: T17? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!, t17!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> ignoreNulls(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): Function18<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, E> = Function18<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16?, t17: T17?, t18: T18? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!, t17!!, t18!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> ignoreNulls(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): Function19<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, E> = Function19<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16?, t17: T17?, t18: T18?, t19: T19? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!, t17!!, t18!!, t19!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> ignoreNulls(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): Function20<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, E> = Function20<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16?, t17: T17?, t18: T18?, t19: T19?, t20: T20? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!, t17!!, t18!!, t19!!, t20!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> ignoreNulls(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): Function21<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, E> = Function21<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16?, t17: T17?, t18: T18?, t19: T19?, t20: T20?, t21: T21? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!, t17!!, t18!!, t19!!, t20!!, t21!!) }

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> ignoreNulls(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): Function22<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, T22?, E> = Function22<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, T22?, E> { t1: T1?, t2: T2?, t3: T3?, t4: T4?, t5: T5?, t6: T6?, t7: T7?, t8: T8?, t9: T9?, t10: T10?, t11: T11?, t12: T12?, t13: T13?, t14: T14?, t15: T15?, t16: T16?, t17: T17?, t18: T18?, t19: T19?, t20: T20?, t21: T21?, t22: T22? -> f.apply(t1!!, t2!!, t3!!, t4!!, t5!!, t6!!, t7!!, t8!!, t9!!, t10!!, t11!!, t12!!, t13!!, t14!!, t15!!, t16!!, t17!!, t18!!, t19!!, t20!!, t21!!, t22!!) }

fun <T1, E> mappingIgnoreNulls(f: Function1<T1, E>): RecordMapper<Record1<T1?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, E> mappingIgnoreNulls(f: Function2<T1, T2, E>): RecordMapper<Record2<T1?, T2?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, E> mappingIgnoreNulls(f: Function3<T1, T2, T3, E>): RecordMapper<Record3<T1?, T2?, T3?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, E> mappingIgnoreNulls(f: Function4<T1, T2, T3, T4, E>): RecordMapper<Record4<T1?, T2?, T3?, T4?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, E> mappingIgnoreNulls(f: Function5<T1, T2, T3, T4, T5, E>): RecordMapper<Record5<T1?, T2?, T3?, T4?, T5?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, E> mappingIgnoreNulls(f: Function6<T1, T2, T3, T4, T5, T6, E>): RecordMapper<Record6<T1?, T2?, T3?, T4?, T5?, T6?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, E> mappingIgnoreNulls(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): RecordMapper<Record7<T1?, T2?, T3?, T4?, T5?, T6?, T7?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, E> mappingIgnoreNulls(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): RecordMapper<Record8<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> mappingIgnoreNulls(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): RecordMapper<Record9<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> mappingIgnoreNulls(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): RecordMapper<Record10<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> mappingIgnoreNulls(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): RecordMapper<Record11<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> mappingIgnoreNulls(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): RecordMapper<Record12<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> mappingIgnoreNulls(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): RecordMapper<Record13<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> mappingIgnoreNulls(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): RecordMapper<Record14<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> mappingIgnoreNulls(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): RecordMapper<Record15<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> mappingIgnoreNulls(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): RecordMapper<Record16<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> mappingIgnoreNulls(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): RecordMapper<Record17<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> mappingIgnoreNulls(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): RecordMapper<Record18<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> mappingIgnoreNulls(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): RecordMapper<Record19<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> mappingIgnoreNulls(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): RecordMapper<Record20<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> mappingIgnoreNulls(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): RecordMapper<Record21<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?>, E> = Records.mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> mappingIgnoreNulls(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): RecordMapper<Record22<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, T22?>, E> = Records.mapping(ignoreNulls(f))

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

@JvmName("mappingRecordIgnoreNulls")
fun <T1, E> Field<Record1<T1?>>.mappingIgnoreNulls(f: Function1<T1, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, E> Field<Record2<T1?, T2?>>.mappingIgnoreNulls(f: Function2<T1, T2, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, E> Field<Record3<T1?, T2?, T3?>>.mappingIgnoreNulls(f: Function3<T1, T2, T3, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, E> Field<Record4<T1?, T2?, T3?, T4?>>.mappingIgnoreNulls(f: Function4<T1, T2, T3, T4, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, E> Field<Record5<T1?, T2?, T3?, T4?, T5?>>.mappingIgnoreNulls(f: Function5<T1, T2, T3, T4, T5, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, E> Field<Record6<T1?, T2?, T3?, T4?, T5?, T6?>>.mappingIgnoreNulls(f: Function6<T1, T2, T3, T4, T5, T6, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, E> Field<Record7<T1?, T2?, T3?, T4?, T5?, T6?, T7?>>.mappingIgnoreNulls(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, E> Field<Record8<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?>>.mappingIgnoreNulls(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> Field<Record9<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?>>.mappingIgnoreNulls(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> Field<Record10<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?>>.mappingIgnoreNulls(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> Field<Record11<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?>>.mappingIgnoreNulls(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> Field<Record12<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?>>.mappingIgnoreNulls(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> Field<Record13<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?>>.mappingIgnoreNulls(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> Field<Record14<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?>>.mappingIgnoreNulls(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> Field<Record15<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?>>.mappingIgnoreNulls(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> Field<Record16<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?>>.mappingIgnoreNulls(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> Field<Record17<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?>>.mappingIgnoreNulls(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> Field<Record18<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?>>.mappingIgnoreNulls(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> Field<Record19<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?>>.mappingIgnoreNulls(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> Field<Record20<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?>>.mappingIgnoreNulls(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> Field<Record21<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?>>.mappingIgnoreNulls(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): Field<E> = mapping(ignoreNulls(f))

@JvmName("mappingRecordIgnoreNulls")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> Field<Record22<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, T22?>>.mappingIgnoreNulls(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): Field<E> = mapping(ignoreNulls(f))

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

fun <T1, E> Field<Result<Record1<T1?>>>.mappingIgnoreNulls(f: Function1<T1, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, E> Field<Result<Record2<T1?, T2?>>>.mappingIgnoreNulls(f: Function2<T1, T2, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, E> Field<Result<Record3<T1?, T2?, T3?>>>.mappingIgnoreNulls(f: Function3<T1, T2, T3, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, E> Field<Result<Record4<T1?, T2?, T3?, T4?>>>.mappingIgnoreNulls(f: Function4<T1, T2, T3, T4, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, E> Field<Result<Record5<T1?, T2?, T3?, T4?, T5?>>>.mappingIgnoreNulls(f: Function5<T1, T2, T3, T4, T5, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, E> Field<Result<Record6<T1?, T2?, T3?, T4?, T5?, T6?>>>.mappingIgnoreNulls(f: Function6<T1, T2, T3, T4, T5, T6, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, E> Field<Result<Record7<T1?, T2?, T3?, T4?, T5?, T6?, T7?>>>.mappingIgnoreNulls(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, E> Field<Result<Record8<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?>>>.mappingIgnoreNulls(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> Field<Result<Record9<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?>>>.mappingIgnoreNulls(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> Field<Result<Record10<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?>>>.mappingIgnoreNulls(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> Field<Result<Record11<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?>>>.mappingIgnoreNulls(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> Field<Result<Record12<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?>>>.mappingIgnoreNulls(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> Field<Result<Record13<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?>>>.mappingIgnoreNulls(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> Field<Result<Record14<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?>>>.mappingIgnoreNulls(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> Field<Result<Record15<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?>>>.mappingIgnoreNulls(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> Field<Result<Record16<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?>>>.mappingIgnoreNulls(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> Field<Result<Record17<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?>>>.mappingIgnoreNulls(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> Field<Result<Record18<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?>>>.mappingIgnoreNulls(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> Field<Result<Record19<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?>>>.mappingIgnoreNulls(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> Field<Result<Record20<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?>>>.mappingIgnoreNulls(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> Field<Result<Record21<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?>>>.mappingIgnoreNulls(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> Field<Result<Record22<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, T22?>>>.mappingIgnoreNulls(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): Field<List<E>> = mapping(ignoreNulls(f))

fun <T1, E> Row1<T1?>.mappingIgnoreNulls(f: Function1<T1, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, E> Row2<T1?, T2?>.mappingIgnoreNulls(f: Function2<T1, T2, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, E> Row3<T1?, T2?, T3?>.mappingIgnoreNulls(f: Function3<T1, T2, T3, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, E> Row4<T1?, T2?, T3?, T4?>.mappingIgnoreNulls(f: Function4<T1, T2, T3, T4, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, E> Row5<T1?, T2?, T3?, T4?, T5?>.mappingIgnoreNulls(f: Function5<T1, T2, T3, T4, T5, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, E> Row6<T1?, T2?, T3?, T4?, T5?, T6?>.mappingIgnoreNulls(f: Function6<T1, T2, T3, T4, T5, T6, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, E> Row7<T1?, T2?, T3?, T4?, T5?, T6?, T7?>.mappingIgnoreNulls(f: Function7<T1, T2, T3, T4, T5, T6, T7, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, E> Row8<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?>.mappingIgnoreNulls(f: Function8<T1, T2, T3, T4, T5, T6, T7, T8, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, E> Row9<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?>.mappingIgnoreNulls(f: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E> Row10<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?>.mappingIgnoreNulls(f: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E> Row11<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?>.mappingIgnoreNulls(f: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E> Row12<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?>.mappingIgnoreNulls(f: Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E> Row13<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?>.mappingIgnoreNulls(f: Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E> Row14<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?>.mappingIgnoreNulls(f: Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E> Row15<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?>.mappingIgnoreNulls(f: Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E> Row16<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?>.mappingIgnoreNulls(f: Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E> Row17<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?>.mappingIgnoreNulls(f: Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E> Row18<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?>.mappingIgnoreNulls(f: Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E> Row19<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?>.mappingIgnoreNulls(f: Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E> Row20<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?>.mappingIgnoreNulls(f: Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E> Row21<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?>.mappingIgnoreNulls(f: Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, E>): SelectField<E> = mapping(ignoreNulls(f))

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E> Row22<T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?, T11?, T12?, T13?, T14?, T15?, T16?, T17?, T18?, T19?, T20?, T21?, T22?>.mappingIgnoreNulls(f: Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, E>): SelectField<E> = mapping(ignoreNulls(f))

// [jooq-tools] END [mapping]

// ----------------------------------------------------------------------------
// Extensions to make Field<Boolean> a Condition
// ----------------------------------------------------------------------------

@Support
fun Field<out Boolean?>.and(other: Condition): Condition = condition(this as Boolean?).and(other)

@Support
fun Field<out Boolean?>.and(other: Field<out Boolean?>): Condition = condition(this as Boolean?).and(other)

@Support
@PlainSQL
fun Field<out Boolean?>.and(sql: SQL): Condition = condition(this as Boolean?).and(sql)

@Support
@PlainSQL
fun Field<out Boolean?>.and(sql: String): Condition = condition(this as Boolean?).and(sql)

@Support
@PlainSQL
fun Field<out Boolean?>.and(sql: String, vararg bindings: Any): Condition = condition(this as Boolean?).and(sql, bindings)

@Support
@PlainSQL
fun Field<out Boolean?>.and(sql: String, vararg parts: QueryPart): Condition = condition(this as Boolean?).and(sql, parts)

@Support
fun Field<out Boolean?>.andNot(other: Condition): Condition = condition(this as Boolean?).andNot(other)

@Support
fun Field<out Boolean?>.andNot(other: Field<out Boolean?>): Condition = condition(this as Boolean?).andNot(other)

@Support
fun Field<out Boolean?>.andExists(select: Select<*>): Condition = condition(this as Boolean?).andExists(select)

@Support
fun Field<out Boolean?>.andNotExists(select: Select<*>): Condition = condition(this as Boolean?).andNotExists(select)

@Support
fun Field<out Boolean?>.or(other: Condition): Condition = condition(this as Boolean?).or(other)

@Support
fun Field<out Boolean?>.or(other: Field<out Boolean?>): Condition = condition(this as Boolean?).or(other)

@Support
@PlainSQL
fun Field<out Boolean?>.or(sql: SQL): Condition = condition(this as Boolean?).or(sql)

@Support
@PlainSQL
fun Field<out Boolean?>.or(sql: String): Condition = condition(this as Boolean?).or(sql)

@Support
@PlainSQL
fun Field<out Boolean?>.or(sql: String, vararg bindings: Any): Condition = condition(this as Boolean?).or(sql)

@Support
@PlainSQL
fun Field<out Boolean?>.or(sql: String, vararg parts: QueryPart): Condition = condition(this as Boolean?).or(sql, parts)

@Support
fun Field<out Boolean?>.orNot(other: Condition): Condition = condition(this as Boolean?).orNot(other)

@Support
fun Field<out Boolean?>.orNot(other: Field<Boolean>): Condition = condition(this as Boolean?).orNot(other)

@Support
fun Field<out Boolean?>.orExists(select: Select<*>): Condition = condition(this as Boolean?).orExists(select)

@Support
fun Field<out Boolean?>.orNotExists(select: Select<*>): Condition = condition(this as Boolean?).orNotExists(select)

@Support
fun Field<out Boolean?>.not(): Condition = condition(this as Boolean?).not()


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
operator fun <T> Field<out Array<T>?>.get(index: Int) = arrayGet(this as Field<Array<T>?>, index)

@Support
operator fun <T> Field<out Array<T>?>.get(index: Field<out Int?>) = arrayGet(this as Field<Array<T>?>, index as Field<Int?>)

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
