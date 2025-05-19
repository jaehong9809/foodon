package com.swallaby.foodon.data.food.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.swallaby.foodon.core.util.generateSearchTokens
import com.swallaby.foodon.data.food.local.dto.LocalFoodDto

@Dao
interface FoodSearchDao {

    @Query("SELECT COUNT(*) FROM foods")
    fun countFoods(): Int

    @Query("SELECT COUNT(*) FROM foods_fts")
    fun countFts(): Int

    @Query("SELECT * FROM foods")
    suspend fun getAllFoods(): List<LocalFoodEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: LocalFoodEntity): Long

    @Query("""
        INSERT INTO foods_fts(rowid, name, searchTokens)
        VALUES (:rowId, :name, :tokens)
    """)
    suspend fun insertFoodFts(rowId: Long, name: String, tokens: String)

    @Transaction
    suspend fun rebuildFts(allFoods: List<LocalFoodEntity>) {
        clearAllFts()
        allFoods.forEach { food ->
            insertFoodFts(food.id, food.name, food.name.generateSearchTokens())
        }
    }

    @Query("DELETE FROM foods WHERE id = :id")
    suspend fun deleteFood(id: Long)

    @Query("DELETE FROM foods_fts WHERE rowid = :id")
    suspend fun deleteFoodFts(id: Long)

    @Query("""
        SELECT f.*, 
            CASE WHEN f.name LIKE :query || '%' THEN 1 ELSE 0 END AS prefix_match
        FROM foods f
        JOIN foods_fts fts ON f.id = fts.rowid
        WHERE foods_fts MATCH :query || '*'
        ORDER BY f.isCustom DESC, prefix_match DESC, f.name ASC
    """)
    fun searchFoods(query: String): PagingSource<Int, LocalFoodDto>

    @Query("DELETE FROM foods")
    suspend fun clearAll()

    @Query("DELETE FROM foods_fts")
    suspend fun clearAllFts()
}