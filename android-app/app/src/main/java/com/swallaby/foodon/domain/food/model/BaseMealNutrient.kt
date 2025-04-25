package com.swallaby.foodon.domain.food.model

abstract class BaseMealNutrient(
    open val alcohol: Int = 0,
    open val caffeine: Int = 0,
    open val carbs: Int = 0,
    open val cholesterol: Int = 0,
    open val fat: Int = 0,
    open val fattyAcid: Int = 0,
    open val fiber: Int = 0,
    open val foodId: Long = 0,
    open val foodName: String = "",
    open val kalium: Int = 0,
    open val kcal: Int = 0,
    open val protein: Int = 0,
    open val saturatedFat: Int = 0,
    open val sodium: Int = 0,
    open val sugar: Int = 0,
    open val transFat: Int = 0,
    open val unit: String = "",
    open val unsaturatedFat: Int = 0,
)