package com.swallaby.foodon.domain.food.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.food.remote.dto.request.RecordMealRequest
import com.swallaby.foodon.domain.food.model.MealInfo
import okhttp3.MultipartBody

interface FoodRepository {
    suspend fun postMealImage(
        image: MultipartBody.Part,
    ): ApiResult<MealInfo>

    suspend fun postFoodHistory(
        request: RecordMealRequest,
    ): ApiResult<Unit>
}