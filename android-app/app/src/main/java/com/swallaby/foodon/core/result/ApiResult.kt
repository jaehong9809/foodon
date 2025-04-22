package com.swallaby.foodon.core.result

import com.swallaby.foodon.core.error.AppError

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val error: AppError) : ApiResult<Nothing>()
}