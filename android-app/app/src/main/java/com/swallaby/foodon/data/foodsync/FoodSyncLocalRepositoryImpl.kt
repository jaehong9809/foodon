package com.swallaby.foodon.data.foodsync

import com.swallaby.foodon.data.food.local.FoodSearchDao
import com.swallaby.foodon.data.food.local.LocalFoodEntity
import com.swallaby.foodon.domain.foodsync.FoodSyncLocalRepository
import javax.inject.Inject

class FoodSyncLocalRepositoryImpl @Inject constructor(
    private val dao: FoodSearchDao
) : FoodSyncLocalRepository {

    override suspend fun getLastFoodId(): Long? {
        return dao.getLastFoodId()
    }

    override suspend fun insertFoods(foods: List<LocalFoodEntity>) {
        foods.forEach { dao.insertFood(it) }
    }
}
