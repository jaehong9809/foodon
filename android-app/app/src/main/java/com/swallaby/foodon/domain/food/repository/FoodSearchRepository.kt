package com.swallaby.foodon.domain.food.repository

import androidx.paging.PagingData
import com.swallaby.foodon.domain.food.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodSearchRepository {
    suspend fun addFood(food: Food)
    suspend fun deleteFood(id: Long)
    fun searchFoods(query: String): Flow<PagingData<Food>>
}