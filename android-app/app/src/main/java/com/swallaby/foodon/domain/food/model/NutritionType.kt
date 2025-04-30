package com.swallaby.foodon.domain.food.model

import androidx.compose.ui.graphics.Color
import com.swallaby.foodon.core.ui.theme.BGFat
import com.swallaby.foodon.core.ui.theme.BGProtein
import com.swallaby.foodon.core.ui.theme.WB500

enum class NutritionType(val displayName: String, val color: Color) {
    CARBOHYDRATE(
        displayName = "탄수화물", color = WB500
    ),
    PROTEIN(
        displayName = "단백질", color = BGProtein
    ),
    FAT(
        displayName = "지방", color = BGFat
    ),
    CHOLESTEROL(
        // todo 색 변경
        // domain 계층에서 UI 와 관련된 내용이 들어가는게 맞을까요?
        displayName = "콜레스테롤", color = Color(0xFF4CAF90)
    ),
    SODIUM(
        // todo 색 변경
        displayName = "나트륨", color = Color(0xFFFFEB3B)
    );
}
