package com.swallaby.foodon.domain.main.model

data class NutrientManage(
    val nutrientName: String = "",
    val nutrientType: NutrientManageType = NutrientManageType.ESSENTIAL,
    val unit: String = "",
    val intake: Int = 0,
    val minRecommend: Int = 0,
    val maxRecommend: Int = 0,
    val status: NutrientStatus = NutrientStatus.NORMAL
)
