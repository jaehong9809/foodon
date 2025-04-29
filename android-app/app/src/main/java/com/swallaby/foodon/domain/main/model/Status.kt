package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.domain.main.model.NutrientStatus.values

enum class NutrientStatus(val korean: String) {
    NORMAL("적정"),
    LACK("부족"),
    CAUTION("주의"),
    DANGER("위험");

    companion object {
        fun fromKorean(korean: String): NutrientStatus {
            return values().firstOrNull { it.korean == korean } ?: NORMAL
        }
    }
}
