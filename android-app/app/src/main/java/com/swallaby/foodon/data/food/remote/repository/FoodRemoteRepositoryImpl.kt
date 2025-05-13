package com.swallaby.foodon.data.food.remote.repository

import com.swallaby.foodon.core.data.remote.getOrThrow
import com.swallaby.foodon.core.data.remote.getOrThrowNull
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.safeApiCall
import com.swallaby.foodon.data.food.remote.api.FoodApi
import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest
import com.swallaby.foodon.data.food.remote.dto.request.RecordMealRequest
import com.swallaby.foodon.data.food.remote.dto.response.toDomain
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.repository.FoodRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class FoodRemoteRepositoryImpl @Inject constructor(
    private val foodApi: FoodApi,
) : FoodRepository {
    override suspend fun postMealImage(image: MultipartBody.Part): ApiResult<MealInfo> =
        safeApiCall {
            foodApi.postFoodImage(image).getOrThrow { it.toDomain() }
        }

    override suspend fun postFoodHistory(request: RecordMealRequest): ApiResult<Unit> =
        safeApiCall {
            foodApi.postFoodHistory(request).getOrThrowNull { }
        }

    override suspend fun postCustomFood(request: CustomFoodRequest): ApiResult<Unit> = safeApiCall {
        foodApi.postCustomFood(request).getOrThrowNull { }
    }

    override suspend fun getMealDetail(mealId: Long): ApiResult<MealInfo> = safeApiCall {
        foodApi.getMealDetail(mealId).getOrThrow { it.toDomain() }
    }
}