package com.swallaby.foodon.core.result

fun <T> ApiResult<T>.toResultState(): ResultState<T> {
    return when (this) {
        is ApiResult.Success -> ResultState.Success(data)
        is ApiResult.Failure -> ResultState.Error(error.messageRes)
    }
}

fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(this.data))
        is ApiResult.Failure -> this
    }