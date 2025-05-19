package com.swallaby.foodon.core.exception

import android.util.Log
import android.database.sqlite.SQLiteConstraintException
import com.swallaby.foodon.core.error.AppError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ExceptionHandler {
    fun map(e: Throwable): AppError {
        // 에러 로그가 안떠서 확인용으로 추가했습니다
        Log.e("ExceptionHandler", "Throwable", e)
        return when (e) {
            is IOException -> AppError.Network
            is SocketTimeoutException -> AppError.Timeout
            is HttpException -> {
                when (e.code()) {
                    401 -> AppError.Unauthorized
                    else -> AppError.Unknown(e)
                }
            }
            is SQLiteConstraintException -> AppError.DuplicateFoodName

            else -> AppError.Unknown(e)
        }
    }
}