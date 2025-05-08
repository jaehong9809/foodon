package com.swallaby.foodon.domain.food.repository

import androidx.paging.PagingData
import com.swallaby.foodon.domain.food.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodSearchRepository {
    fun searchFoods(query: String): Flow<PagingData<Food>>
    suspend fun insertAll(foods: List<Food>)
}