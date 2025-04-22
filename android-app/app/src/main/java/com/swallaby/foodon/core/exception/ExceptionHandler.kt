package com.swallaby.foodon.core.exception

import com.swallaby.foodon.core.error.AppError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ExceptionHandler {
    fun map(e: Throwable): AppError {
        return when (e) {
            is IOException -> AppError.Network
            is SocketTimeoutException -> AppError.Timeout
            is HttpException -> {
                when (e.code()) {
                    401 -> AppError.Unauthorized
                    else -> AppError.Unknown(e)
                }
            }
            else -> AppError.Unknown(e)
        }
    }
}