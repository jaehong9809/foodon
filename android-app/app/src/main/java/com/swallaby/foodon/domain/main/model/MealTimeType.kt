package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.domain.main.model.MealTimeType.values

enum class MealTimeType(val korean: String) {
    BREAKFAST("아침 식사"),
    LUNCH("점심 식사"),
    DINNER("저녁 식사"),
    SNACK("간식");

    companion object {
        fun fromKorean(korean: String): MealTimeType {
            return values().firstOrNull { it.korean == korean } ?: BREAKFAST
        }
    }
}