package com.swallaby.foodon.domain.calendar.model

import com.swallaby.foodon.domain.food.model.Position
import org.threeten.bp.LocalDateTime

data class MealThumbnailInfo(
    val mealId: Long = 0,
    val mealItemId: Long = 0,
    val mealTime: LocalDateTime = LocalDateTime.now(),
    val foodName: String = "",
    val positionInfo: Position = Position()
)
