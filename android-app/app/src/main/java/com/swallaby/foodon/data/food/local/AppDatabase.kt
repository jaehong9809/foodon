package com.swallaby.foodon.data.food.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalFoodEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodSearchDao(): FoodSearchDao
}