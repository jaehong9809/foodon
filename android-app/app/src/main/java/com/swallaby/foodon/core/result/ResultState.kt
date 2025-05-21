package com.swallaby.foodon.core.result

import androidx.annotation.StringRes

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(@StringRes val messageRes: Int) : ResultState<Nothing>()
}