package org.jooq.kotlin.coroutines

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.jooq.Configuration
import org.jooq.DSLContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// ----------------------------------------------------------------------------
// Extensions to bridge between the reactive-streams and the coroutine world
// ----------------------------------------------------------------------------

suspend fun <T> DSLContext.transactionCoroutine(context: CoroutineContext = EmptyCoroutineContext, transactional: suspend (Configuration) -> T): T {
    @Suppress("UNCHECKED_CAST")
    return transactionPublisher { c ->
        mono(context) {
            transactional.invoke(c)
        }
    }.awaitFirstOrNull() as T
}