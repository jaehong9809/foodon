package com.swallaby.foodon.data.food.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swallaby.foodon.domain.food.model.Food

@Entity(tableName = "foods")
data class LocalFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isRegistered: Boolean,
    val namePrefixes: String
)