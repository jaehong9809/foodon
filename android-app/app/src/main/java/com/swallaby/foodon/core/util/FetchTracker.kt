package com.swallaby.foodon.core.util

class FetchTracker<T>(initialValue: T? = null) {
    private var lastValue = initialValue

    fun shouldFetch(current: T): Boolean {
        return if (lastValue != current) {
            lastValue = current
            true
        } else {
            false
        }
    }
}