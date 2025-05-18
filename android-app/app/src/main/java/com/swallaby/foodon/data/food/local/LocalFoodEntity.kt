package com.swallaby.foodon.data.food.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "foods",
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalFoodEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodId: Long,
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isCustom: Boolean
)