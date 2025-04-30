package com.swallaby.foodon.domain.food.model

import androidx.compose.ui.graphics.Color
import com.swallaby.foodon.core.ui.theme.BGFat
import com.swallaby.foodon.core.ui.theme.BGProtein
import com.swallaby.foodon.core.ui.theme.WB500

enum class NutrientNameType(
    val fullName: String,
    val shortName: String,
    val color: Color
) {
    CARBOHYDRATE("탄수화물", "탄", WB500),
    PROTEIN("단백질", "단", BGProtein),
    FAT("지방", "지", BGFat)
}
