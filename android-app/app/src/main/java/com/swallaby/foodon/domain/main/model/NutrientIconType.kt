package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.R
import com.swallaby.foodon.domain.main.model.NutrientIconType.values

enum class NutrientIconType(val nutrientName: String, val iconRes: Int) {
    SUGAR("당류", R.drawable.icon_candy),
    SODIUM("나트륨", R.drawable.icon_salt),
    SATURATED_FAT("포화지방", R.drawable.icon_butter),
    TRANS_FAT("트랜스지방", R.drawable.icon_fries),
    CAFFEINE("카페인", R.drawable.icon_coffee),
    ALCOHOL("알코올", R.drawable.icon_bear);

    companion object {
        private val nameMap = values().associateBy { it.nutrientName }

        fun fromName(nutrientName: String): NutrientIconType? = nameMap[nutrientName]
        fun getDrawable(nutrientName: String): Int = fromName(nutrientName)?.iconRes ?: R.drawable.icon_candy
    }
}