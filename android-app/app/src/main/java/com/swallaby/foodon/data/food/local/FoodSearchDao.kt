package com.swallaby.foodon.data.food.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodSearchDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(food: LocalFoodEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(foods: List<LocalFoodEntity>)

    @Query("DELETE FROM foods")
    suspend fun clearAll()

    @Query("SELECT * FROM foods WHERE foods MATCH :query || '*'")
    fun searchFoods(query: String): PagingSource<Int, LocalFoodEntity>
}