package com.swallaby.foodon.data.food.local

import androidx.room.Entity
import androidx.room.Fts4


@Fts4
@Entity(tableName = "foods_fts")
data class LocalFoodFtsEntity(
    val name: String,
    val searchTokens: String
)
