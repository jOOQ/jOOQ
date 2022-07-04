package org.jooq.kotlin.coroutines

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.jooq.Configuration
import org.jooq.DSLContext

// ----------------------------------------------------------------------------
// Extensions to bridge between the reactive-streams and the coroutine world
// ----------------------------------------------------------------------------

suspend fun <T> DSLContext.transactionCoroutine(transactional: suspend (Configuration) -> T): T {
    @Suppress("UNCHECKED_CAST")
    return transactionPublisher { c ->
        mono {
            transactional.invoke(c)
        }
    }.awaitFirstOrNull() as T
}