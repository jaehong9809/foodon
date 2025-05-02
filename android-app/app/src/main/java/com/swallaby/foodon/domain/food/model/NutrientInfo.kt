package com.swallaby.foodon.domain.food.model

data class NutrientInfo(
    val alcohol: Int = 0,
    val carbs: Int = 0,
    val cholesterol: Int = 0,
    val fat: Int = 0,
    val fattyAcid: Int = 0,
    val fiber: Int = 0,
    val kcal: Int = 0,
    val potassium: Int = 0,
    val protein: Int = 0,
    val saturatedFat: Int = 0,
    val sodium: Int = 0,
    val sugar: Int = 0,
    val transFat: Int = 0,
    val unsaturatedFat: Int = 0,
)

fun NutrientInfo.toNutrient(): List<Nutrition> {
    val nutritionMap = mapOf(
        NutritionType.CARBOHYDRATE to carbs,
        NutritionType.PROTEIN to protein,
        NutritionType.FAT to fat,
        NutritionType.CHOLESTEROL to cholesterol,
        NutritionType.SODIUM to sodium,
    )

    return nutritionMap.map { (type, amount) ->
        Nutrition(
            nutritionType = type, amount = amount, ratio = 0f  // 필요하다면 여기서 계산 로직 추가
        )
    }
}