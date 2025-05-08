package com.swallaby.foodon.data.food.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodSearchDao {

    @Insert
    suspend fun insertAll(foods: List<LocalFoodEntity>)

    @Query("SELECT * FROM foods WHERE foods MATCH :query || '*'")
    fun searchFoods(query: String): PagingSource<Int, LocalFoodEntity>

    @Query("DELETE FROM foods")
    suspend fun clearAll()
}