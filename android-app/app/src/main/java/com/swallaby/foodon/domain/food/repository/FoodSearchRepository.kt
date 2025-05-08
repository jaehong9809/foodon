package com.swallaby.foodon.domain.food.repository

import com.swallaby.foodon.domain.food.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodSearchRepository {
    fun searchFoods(query: String): Flow<List<Food>>
    suspend fun insertAll(foods: List<Food>)
    suspend fun clearAll()
}