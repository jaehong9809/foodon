package com.swallaby.foodon.domain.food.model

data class Position(
    val height: Double = 0.0, // 비율
    val width: Double = 0.0, // 비율
    val x: Double = 0.0,
    val y: Double = 0.0,
    val confidence: Double = 0.0,
)