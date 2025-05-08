package com.swallaby.foodon.data.food.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foods: List<LocalFoodEntity>)

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' LIMIT 10")
    fun searchFoods(query: String): Flow<List<LocalFoodEntity>>

    @Query("DELETE FROM foods")
    suspend fun clearAll()
}