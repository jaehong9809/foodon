package com.swallaby.foodon.domain.food.model

// 기본 영양소 정보 클래스 (기존 제공된 클래스)
data class NutrientInfo(
    val alcohol: Double = 0.00,
    val carbs: Double = 0.00,
    val cholesterol: Double = 0.00,
    val fat: Double = 0.00,
    val fattyAcid: Double = 0.00,
    val fiber: Double = 0.00,
    val kcal: Int = 0,
    val potassium: Double = 0.00,
    val protein: Double = 0.00,
    val saturatedFat: Double = 0.00,
    val sodium: Double = 0.00,
    val sugar: Double = 0.00,
    val transFat: Double = 0.00,
    val unsaturatedFat: Double = 0.00,
)

// 영양소 항목을 표현하는 클래스
data class NutrientItem(
    val name: String,
    val value: Double,
    val unit: String,
    val childItems: List<NutrientItem> = emptyList(),
    val nutrientType: NutrientType,  // 어떤 NutrientInfo 필드에 해당하는지 나타냄
)

// 영양소 유형 (NutrientInfo의 필드와 매핑)
enum class NutrientType {
    KCAL, CARBS, SUGAR, FIBER, PROTEIN, FAT, SATURATED_FAT, TRANS_FAT, FATTY_ACID, UNSATURATED_FAT, CHOLESTEROL, SODIUM, ALCOHOL, POTASSIUM, NONE
}

// 영양소 데이터를 계층 구조로 변환하는 클래스
object NutrientConverter {

    // NutrientInfo를 계층 구조로 변환
    fun convertToHierarchy(nutrientInfo: NutrientInfo): List<NutrientItem> {
        return listOf(
            // 열량 (단일 항목)
            NutrientItem(
                name = "열량",
                value = nutrientInfo.kcal.toDouble(),
                unit = "kcal",
                nutrientType = NutrientType.KCAL
            ),

            // 탄수화물 (복합 항목)
            NutrientItem(
                name = "탄수화물", value = nutrientInfo.carbs, unit = "g", childItems = listOf(
                    NutrientItem(
                        name = "당류",
                        value = nutrientInfo.sugar,
                        unit = "g",
                        nutrientType = NutrientType.SUGAR
                    ), NutrientItem(
                        name = "식이섬유",
                        value = nutrientInfo.fiber,
                        unit = "g",
                        nutrientType = NutrientType.FIBER
                    )
                ), nutrientType = NutrientType.CARBS
            ),

            // 단백질 (단일 항목)
            NutrientItem(
                name = "단백질",
                value = nutrientInfo.protein,
                unit = "g",
                nutrientType = NutrientType.PROTEIN
            ),

            // 지방 (복합 항목)
            NutrientItem(
                name = "지방", value = nutrientInfo.fat, unit = "g", childItems = listOf(
                    NutrientItem(
                        name = "포화지방",
                        value = nutrientInfo.saturatedFat,
                        unit = "g",
                        nutrientType = NutrientType.SATURATED_FAT
                    ), NutrientItem(
                        name = "트랜스지방",
                        value = nutrientInfo.transFat,
                        unit = "g",
                        nutrientType = NutrientType.TRANS_FAT
                    ), NutrientItem(
                        name = "지방산",
                        value = nutrientInfo.fattyAcid.toDouble(),
                        unit = "g",
                        nutrientType = NutrientType.FATTY_ACID
                    ), NutrientItem(
                        name = "불포화지방산",
                        value = nutrientInfo.unsaturatedFat.toDouble(),
                        unit = "g",
                        nutrientType = NutrientType.UNSATURATED_FAT
                    )
                ), nutrientType = NutrientType.FAT
            ),

            // 콜레스테롤 (단일 항목)
            NutrientItem(
                name = "콜레스테롤",
                value = nutrientInfo.cholesterol,
                unit = "mg",
                nutrientType = NutrientType.CHOLESTEROL
            ),

            // 나트륨 (단일 항목)
            NutrientItem(
                name = "나트륨",
                value = nutrientInfo.sodium.toDouble(),
                unit = "mg",
                nutrientType = NutrientType.SODIUM
            ),

            // 알코올 (단일 항목)
            NutrientItem(
                name = "알코올",
                value = nutrientInfo.alcohol.toDouble(),
                unit = "g",
                nutrientType = NutrientType.ALCOHOL
            ),

            // 칼륨 (단일 항목)
            NutrientItem(
                name = "칼륨",
                value = nutrientInfo.potassium,
                unit = "mg",
                nutrientType = NutrientType.POTASSIUM
            )
        )
    }

    // 계층 구조를 업데이트된 NutrientInfo로 변환
    fun updateNutrientInfo(items: List<NutrientItem>, currentInfo: NutrientInfo): NutrientInfo {
        var updatedInfo = currentInfo

        for (item in items) {
            updatedInfo = when (item.nutrientType) {
                NutrientType.KCAL -> updatedInfo.copy(kcal = item.value.toInt())
                NutrientType.CARBS -> updatedInfo.copy(carbs = item.value)
                NutrientType.SUGAR -> updatedInfo.copy(sugar = item.value)
                NutrientType.FIBER -> updatedInfo.copy(fiber = item.value)
                NutrientType.PROTEIN -> updatedInfo.copy(protein = item.value)
                NutrientType.FAT -> updatedInfo.copy(fat = item.value)
                NutrientType.SATURATED_FAT -> updatedInfo.copy(saturatedFat = item.value)
                NutrientType.TRANS_FAT -> updatedInfo.copy(transFat = item.value)
                NutrientType.FATTY_ACID -> updatedInfo.copy(fattyAcid = item.value)
                NutrientType.UNSATURATED_FAT -> updatedInfo.copy(unsaturatedFat = item.value)
                NutrientType.CHOLESTEROL -> updatedInfo.copy(cholesterol = item.value)
                NutrientType.SODIUM -> updatedInfo.copy(sodium = item.value)
                NutrientType.ALCOHOL -> updatedInfo.copy(alcohol = item.value)
                NutrientType.POTASSIUM -> updatedInfo.copy(potassium = item.value)
                NutrientType.NONE -> updatedInfo
            }

            if (item.childItems.isNotEmpty()) {
                updatedInfo = updateNutrientInfo(item.childItems, updatedInfo)
            }
        }

        return updatedInfo
    }
}

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