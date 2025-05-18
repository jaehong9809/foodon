package com.swallaby.foodon.data.food.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalFoodEntity::class, LocalFoodFtsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodSearchDao(): FoodSearchDao
}