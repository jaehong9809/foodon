package com.swallaby.foodon.data.food.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.swallaby.foodon.core.util.generateSearchTokens
import com.swallaby.foodon.data.food.local.FoodSearchDao
import com.swallaby.foodon.data.food.local.mapper.toDomain
import com.swallaby.foodon.data.food.local.mapper.toEntity
import com.swallaby.foodon.domain.food.model.Food
import com.swallaby.foodon.domain.food.repository.FoodSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FoodSearchLocalRepositoryImpl @Inject constructor(
    private val dao: FoodSearchDao
) : FoodSearchRepository {

    override suspend fun addFood(food: Food) {
        val id = dao.insertFood(food.toEntity())
        dao.insertFoodFts(id, food.name, food.name.generateSearchTokens())
    }

    override suspend fun deleteFood(id: Long) {
        dao.deleteFood(id)
        dao.deleteFoodFts(id)
    }

    override fun searchFoods(query: String): Flow<PagingData<Food>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { dao.searchFoods(query) }
        ).flow.map { it.map { entity -> entity.toDomain() } }
    }
}
