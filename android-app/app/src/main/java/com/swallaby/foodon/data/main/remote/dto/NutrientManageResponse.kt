package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.domain.main.model.NutrientStatus
import com.swallaby.foodon.domain.main.model.NutrientType

data class NutrientManageResponse(
    val nutrientName: String = "",
    val nutrientType: String = "", // ESSENTIAL, LIMITED
    val unit: String = "",
    val intake: Int = 0,
    val minRecommend: Int = 0,
    val maxRecommend: Int = 0,
    val status: String = "" // 적정, 부족, 주의, 위험
)

fun NutrientManageResponse.toDomain(): NutrientManage {
    return NutrientManage(
        nutrientName = this.nutrientName,
        nutrientType = NutrientType.valueOf(this.nutrientType),
        unit = this.unit,
        intake = this.intake,
        minRecommend = this.minRecommend,
        maxRecommend = this.maxRecommend,
        status = NutrientStatus.fromKorean(this.status)
    )
}
