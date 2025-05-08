package com.swallaby.foodon.domain.food.repository

import androidx.paging.PagingData
import com.swallaby.foodon.domain.food.model.Food
import com.swallaby.foodon.domain.food.model.InsertSummary
import kotlinx.coroutines.flow.Flow

interface FoodSearchRepository {
    fun searchFoods(query: String): Flow<PagingData<Food>>
    suspend fun syncFoods(foods: List<Food>): InsertSummary
    suspend fun insertAll(foods: List<Food>)
    suspend fun clearAll()
}