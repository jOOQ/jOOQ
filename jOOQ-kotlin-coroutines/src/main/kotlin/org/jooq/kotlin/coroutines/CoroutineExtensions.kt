package org.jooq.kotlin.coroutines

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.jooq.*
import org.jooq.impl.DSL.*
import java.util.stream.Collector

// ----------------------------------------------------------------------------
// Extensions to bridge between the reactive-streams and the coroutine world
// ----------------------------------------------------------------------------

suspend fun DSLContext.transactionCoroutine(transactional: suspend (Configuration) -> Unit) {
    transactionPublisher { c ->
        mono {
            transactional.invoke(c)
        }
    }.awaitFirst()
}

suspend fun <T> DSLContext.transactionCoroutineResult(transactional: suspend (Configuration) -> T): T {
    return transactionPublisher { c ->
        mono {
            transactional.invoke(c)
        }
    }.awaitFirst()
}