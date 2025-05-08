package com.swallaby.foodon.data.food.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.swallaby.foodon.data.food.local.FoodSearchDao
import com.swallaby.foodon.data.food.local.mapper.toDomain
import com.swallaby.foodon.data.food.local.mapper.toEntity
import com.swallaby.foodon.domain.food.model.Food
import com.swallaby.foodon.domain.food.repository.FoodSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FoodSearchLocalRepositoryImpl @Inject constructor(
    private val foodSearchDao: FoodSearchDao
) : FoodSearchRepository {

    override fun searchFoods(query: String): Flow<PagingData<Food>> {
        return Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { foodSearchDao.searchFoods(query) }
        ).flow
        .map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun insertAll(foods: List<Food>) {
        foodSearchDao.insertAll(foods.map { it.toEntity() })
    }

    override suspend fun clearAll() {
        foodSearchDao.clearAll()
    }
}
