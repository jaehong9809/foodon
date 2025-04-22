package com.swallaby.foodon.core.data.remote

import com.swallaby.foodon.core.exception.GlobalException

data class BaseResponse<T> (
    val code: String,
    val message: String,
    val data: T?
)

// TODO: code 변경
inline fun <T, R> BaseResponse<T>.getOrThrow(transform: (T) -> R): R {
    if (code == "200" && data != null) {
        return transform(data)
    } else {
        throw GlobalException(message)
    }
}

inline fun <T> BaseResponse<T>.getOrThrowNull(onSuccess: () -> T): T {
    if (code == "200") return onSuccess()
    else throw GlobalException(message)
}
