package com.swallaby.foodon.core.result

import com.swallaby.foodon.core.exception.ExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// suspend 함수를 통해 코루틴 비동기 실행
// Dispatchers.IO Default 설정
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    call: suspend () -> T
): ApiResult<T> = withContext(dispatcher) {
    try {
        ApiResult.Success(call())
    } catch (e: Exception) {
        val error = ExceptionHandler.map(e)
        ApiResult.Failure(error)
    }
}