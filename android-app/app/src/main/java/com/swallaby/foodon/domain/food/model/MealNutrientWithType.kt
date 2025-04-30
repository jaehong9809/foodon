package com.swallaby.foodon.domain.food.model


data class MealNutrientWithType(
    override val alcohol: Int = 0,
    override val caffeine: Int = 0,
    override val carbs: Int = 0,
    override val cholesterol: Int = 0,
    override val fat: Int = 0,
    override val fattyAcid: Int = 0,
    override val fiber: Int = 0,
    override val foodId: Long = 0,
    override val foodName: String = "",
    override val kalium: Int = 0,
    override val kcal: Int = 0,
    override val protein: Int = 0,
    override val saturatedFat: Int = 0,
    override val sodium: Int = 0,
    override val sugar: Int = 0,
    override val transFat: Int = 0,
    override val unit: String = "",
    override val unsaturatedFat: Int = 0,
    val type: String = "",
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

fun MealNutrientWithType.toNutrient(): List<Nutrition> {
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