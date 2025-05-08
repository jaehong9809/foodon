package com.swallaby.foodon.data.food.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
@Fts4
data class LocalFoodEntity(

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Long = 0,
    val foodId: Long,
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isRegistered: Boolean,
    val namePrefixes: String
)