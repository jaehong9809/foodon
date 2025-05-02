package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.domain.main.model.NutrientManageType
import com.swallaby.foodon.domain.main.model.NutrientStatus

data class NutrientManageResponse(
    val nutrientName: String = "",
    val nutrientType: NutrientManageType = NutrientManageType.ESSENTIAL,
    val unit: String = "",
    val intake: Int = 0,
    val minRecommend: Int = 0,
    val maxRecommend: Int = 0,
    val status: NutrientStatus = NutrientStatus.NORMAL
)

fun NutrientManageResponse.toDomain(): NutrientManage {
    return NutrientManage(
        nutrientName = this.nutrientName,
        nutrientType = this.nutrientType,
        unit = this.unit,
        intake = this.intake,
        minRecommend = this.minRecommend,
        maxRecommend = this.maxRecommend,
        status = this.status
    )
}
