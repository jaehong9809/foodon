package com.swallaby.foodon.data.food.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodSearchDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: LocalFoodEntity): Long

    @Query("""
        INSERT INTO foods_fts(rowid, name, searchTokens)
        VALUES (:rowId, :name, :tokens)
    """)
    suspend fun insertFoodFts(rowId: Long, name: String, tokens: String)

    @Query("DELETE FROM foods WHERE id = :id")
    suspend fun deleteFood(id: Long)

    @Query("DELETE FROM foods_fts WHERE rowid = :id")
    suspend fun deleteFoodFts(id: Long)

    @Query("""
        SELECT f.*
        FROM foods f
        JOIN foods_fts fts ON f.id = fts.rowid
        WHERE foods_fts MATCH :query || '*'
    """)
    fun searchFoods(query: String): PagingSource<Int, LocalFoodEntity>

    @Query("DELETE FROM foods")
    suspend fun clearAll()

    @Query("DELETE FROM foods_fts")
    suspend fun clearAllFts()
}