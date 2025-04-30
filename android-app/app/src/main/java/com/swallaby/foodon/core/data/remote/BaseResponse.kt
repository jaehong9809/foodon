package com.swallaby.foodon.core.data.remote

import com.swallaby.foodon.core.exception.GlobalException

data class BaseResponse<T> (
    val code: String,
    val message: String,
    val data: T?
)

// 비즈니스 로직 상 성공 코드: 00000
inline fun <T, R> BaseResponse<T>.getOrThrow(transform: (T) -> R): R {
    if (code == "00000" && data != null) {
        return transform(data)
    } else {
        throw GlobalException(message)
    }
}

inline fun <T> BaseResponse<T>.getOrThrowNull(onSuccess: () -> T): T {
    if (code == "00000") return onSuccess()
    else throw GlobalException(message)
}
