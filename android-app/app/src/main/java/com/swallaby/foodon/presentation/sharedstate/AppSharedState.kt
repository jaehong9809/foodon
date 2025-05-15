package com.swallaby.foodon.presentation.sharedstate

import com.swallaby.foodon.core.data.TokenDataStore
import com.swallaby.foodon.core.util.FetchTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedState @Inject constructor() {
    val isLoggedIn = MutableStateFlow(false)

    suspend fun observeToken(tokenDataStore: TokenDataStore) {
        tokenDataStore.accessTokenFlow
            .map { !it.isNullOrBlank() }
            .collect { isLoggedIn.value = it }
    }

    inline fun ifLoggedIn(action: () -> Unit) {
        if (isLoggedIn.value) action()
    }

    suspend inline fun <T> withLogin(crossinline block: suspend () -> T): T? {
        return if (isLoggedIn.value) block() else null
    }

    suspend inline fun <K, T> withLoginAndFetch(
        key: K,
        tracker: FetchTracker<K>,
        crossinline block: suspend () -> T
    ): T? {
        return if (isLoggedIn.value && tracker.fetch(key)) {
            tracker.fetch(key)
            block()
        } else null
    }
}