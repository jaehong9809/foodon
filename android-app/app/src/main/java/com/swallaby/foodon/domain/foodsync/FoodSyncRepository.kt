package com.swallaby.foodon.domain.foodsync

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.food.local.LocalFoodEntity
import com.swallaby.foodon.data.foodsync.FoodLocalDbResponse
import com.swallaby.foodon.domain.food.model.Food

interface FoodSyncRemoteRepository {
    suspend fun getSyncFoods(sinceId: Long): ApiResult<List<FoodLocalDbResponse>>
}

interface FoodSyncLocalRepository {
    suspend fun getLastFoodId(): Long?
    suspend fun insertFoods(foods: List<LocalFoodEntity>)
}
