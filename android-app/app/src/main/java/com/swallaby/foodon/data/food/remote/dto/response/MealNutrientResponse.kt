package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.BaseMealNutrient
import com.swallaby.foodon.domain.food.model.MealNutrientWithType


data class MealNutrientResponse(
    override val alcohol: Int,
    override val caffeine: Int,
    override val carbs: Int,
    override val cholesterol: Int,
    override val fat: Int,
    override val fattyAcid: Int,
    override val fiber: Int,
    override val foodId: Long,
    override val foodName: String,
    override val kalium: Int,
    override val kcal: Int,
    override val protein: Int,
    override val saturatedFat: Int,
    override val sodium: Int,
    override val sugar: Int,
    override val transFat: Int,
    override val unit: String,
    override val unsaturatedFat: Int,
    val type: String,
) : BaseMealNutrient(
    alcohol,
    caffeine,
    carbs,
    cholesterol,
    fat,
    fattyAcid,
    fiber,
    foodId,
    foodName,
    kalium,
    kcal,
    protein,
    saturatedFat,
    sodium,
    sugar,
    transFat,
    unit,
    unsaturatedFat
)


fun MealNutrientResponse.toDomain(): MealNutrientWithType = MealNutrientWithType(
    alcohol = this.alcohol,
    caffeine = this.caffeine,
    carbs = this.carbs,
    cholesterol = this.cholesterol,
    fat = this.fat,
    fattyAcid = this.fattyAcid,
    fiber = this.fiber,
    foodId = this.foodId,
    foodName = this.foodName,
    kalium = this.kalium,
    kcal = this.kcal,
    protein = this.protein,
    saturatedFat = this.saturatedFat,
    sodium = this.sodium,
    sugar = this.sugar,
    transFat = this.transFat,
    unit = this.unit,
    unsaturatedFat = this.unsaturatedFat,
    type = this.type
)