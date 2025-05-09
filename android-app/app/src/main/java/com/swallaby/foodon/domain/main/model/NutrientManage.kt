package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.domain.food.model.NutrientCode
import com.swallaby.foodon.domain.food.model.UnitType

data class NutrientManage(
    val nutrientName: String = "",
    val nutrientCode: NutrientCode = NutrientCode.CARBS,
    val unit: UnitType = UnitType.GRAM,
    val intake: Double = 0.0,
    val minRecommend: Int = 0,
    val maxRecommend: Int = 0,
    val healthEffect: HealthEffect = HealthEffect.UNKNOWN,
    val status: ManageStatus = ManageStatus.ADEQUATE
)
