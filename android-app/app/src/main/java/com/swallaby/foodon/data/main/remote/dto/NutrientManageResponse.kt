package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.food.model.NutrientCode
import com.swallaby.foodon.domain.food.model.UnitType
import com.swallaby.foodon.domain.main.model.HealthEffect
import com.swallaby.foodon.domain.main.model.ManageStatus
import com.swallaby.foodon.domain.main.model.NutrientManage

data class NutrientManageResponse(
    val nutrientName: String = "",
    val nutrientCode: NutrientCode = NutrientCode.CARBS,
    val unit: UnitType = UnitType.GRAM,
    val intake: Double = 0.0,
    val minRecommended: Double = 0.0,
    val maxRecommended: Double = 0.0,
    val healthEffect: HealthEffect = HealthEffect.UNKNOWN,
    val status: ManageStatus = ManageStatus.ADEQUATE
)

fun NutrientManageResponse.toDomain(): NutrientManage {
    return NutrientManage(
        nutrientName = this.nutrientName,
        nutrientCode = this.nutrientCode,
        unit = this.unit,
        intake = this.intake,
        minRecommend = this.minRecommended,
        maxRecommend = this.maxRecommended,
        healthEffect = this.healthEffect,
        status = this.status
    )
}
