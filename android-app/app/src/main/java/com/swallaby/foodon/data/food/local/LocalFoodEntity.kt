package com.swallaby.foodon.data.food.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class LocalFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val kcalPerServing: Int
)
