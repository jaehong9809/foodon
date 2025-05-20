package com.swallaby.foodon.domain.foodsync

import com.swallaby.foodon.data.food.local.LocalFoodEntity
import com.swallaby.foodon.domain.food.model.Food

interface FoodRemoteRepository {
    suspend fun getSyncFoods(sinceId: Long): List<Food>
}

interface FoodLocalRepository {
    suspend fun getLastFoodId(): Long?
    suspend fun insertFoods(foods: List<LocalFoodEntity>)
}
