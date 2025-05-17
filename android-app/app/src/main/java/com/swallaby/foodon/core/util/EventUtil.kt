package com.swallaby.foodon.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration


fun <T> Flow<T>.throttleFirst(windowDuration: Duration) = flow {
    var lastEmissionTime = 0L
    collect { value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime > windowDuration.inWholeMilliseconds) {
            lastEmissionTime = currentTime
            emit(value)
        }
    }
}

@Composable
fun rememberThrottledFunction(
    throttleMillis: Long = 300,
    function: () -> Unit,
): () -> Unit {
    val throttledFunction = remember {
        object {
            private var lastInvokeTime = 0L
            fun invoke() {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastInvokeTime >= throttleMillis) {
                    lastInvokeTime = currentTime
                    function()
                }
            }
        }
    }
    return throttledFunction::invoke
}