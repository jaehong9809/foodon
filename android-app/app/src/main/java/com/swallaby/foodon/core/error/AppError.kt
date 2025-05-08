package com.swallaby.foodon.core.error

import androidx.annotation.StringRes
import com.swallaby.foodon.R

sealed class AppError(@StringRes val messageRes: Int) {
    object Network : AppError(R.string.error_network)
    object Timeout : AppError(R.string.error_timeout)
    object Unauthorized : AppError(R.string.error_unauthorized)
    object DuplicateFoodName : AppError(R.string.error_duplicated_name)
    data class Unknown(val throwable: Throwable? = null) : AppError(R.string.error_unknown)
}