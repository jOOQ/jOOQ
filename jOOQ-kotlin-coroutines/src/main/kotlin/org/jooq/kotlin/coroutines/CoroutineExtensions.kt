package org.jooq.kotlin.coroutines

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import org.jooq.Configuration
import org.jooq.DSLContext

// ----------------------------------------------------------------------------
// Extensions to bridge between the reactive-streams and the coroutine world
// ----------------------------------------------------------------------------

suspend fun <T> DSLContext.transactionCoroutine(transactional: suspend (Configuration) -> T): T {
    return transactionPublisher { c ->
        mono {
            transactional.invoke(c)
        }
    }.awaitFirst()
}