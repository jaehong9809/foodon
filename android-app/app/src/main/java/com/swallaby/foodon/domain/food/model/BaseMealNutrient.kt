package com.swallaby.foodon.domain.food.model

abstract class BaseMealNutrient(
    open val alcohol: Int,
    open val caffeine: Int,
    open val carbs: Int,
    open val cholesterol: Int,
    open val fat: Int,
    open val fattyAcid: Int,
    open val fiber: Int,
    open val foodId: Long,
    open val foodName: String,
    open val kalium: Int,
    open val kcal: Int,
    open val protein: Int,
    open val saturatedFat: Int,
    open val sodium: Int,
    open val sugar: Int,
    open val transFat: Int,
    open val unit: String,
    open val unsaturatedFat: Int,
)