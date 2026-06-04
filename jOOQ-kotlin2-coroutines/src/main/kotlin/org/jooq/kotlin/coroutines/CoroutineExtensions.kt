package org.jooq.kotlin.coroutines

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.jooq.Configuration
import org.jooq.DSLContext

// ----------------------------------------------------------------------------
// Extensions to bridge between the reactive-streams and the coroutine world
// ----------------------------------------------------------------------------

suspend fun <T> DSLContext.transactionCoroutine(transactional: suspend (Configuration) -> T): T =
    transactionCoroutine(EmptyCoroutineContext, transactional)


suspend fun <T> DSLContext.transactionCoroutine(context: CoroutineContext, transactional: suspend (Configuration) -> T): T {
    // [#14997] Wrap values in an auxiliary class in order to allow nulls, which awaitSingle()
    //          doesn't allow, otherwise
    data class Wrap<T>(val t: T)

    return transactionPublisher { c ->
        mono(context) {
            Wrap(transactional.invoke(c))
        }
    }.awaitSingle().t
}