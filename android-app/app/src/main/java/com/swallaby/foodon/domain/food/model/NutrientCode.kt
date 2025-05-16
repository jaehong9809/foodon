package com.swallaby.foodon.domain.food.model

import androidx.annotation.DrawableRes
import com.swallaby.foodon.R

enum class NutrientCode(@DrawableRes val icon: Int, val displayName: String) {
    KCAL(R.drawable.icon_nutrient_empty, "칼로리"),
    CARBS(R.drawable.icon_carbs, "탄수화물"),
    SUGAR(R.drawable.icon_candy, "당류"),
    FIBER(R.drawable.icon_apple, "섬유질"),
    PROTEIN(R.drawable.icon_protein, "단백질"),
    FAT(R.drawable.icon_pizza, "지방"),
    SATURATED_FAT(R.drawable.icon_donut, "포화지방"),
    TRANS_FAT(R.drawable.icon_butter, "트랜스지방"),
    FATTY_ACID(R.drawable.icon_bacon, "지방산"),
    UNSATURATED_FAT(R.drawable.icon_peanut, "불포화지방"),
    CHOLESTEROL(R.drawable.icon_egg, "콜레스테롤"),
    SODIUM(R.drawable.icon_salt, "나트륨"),
    POTASSIUM(R.drawable.icon_avocado, "칼륨"),
    ALCOHOL(R.drawable.icon_bear, "알코올"),
    CAFFEINE(R.drawable.icon_coffee, "카페인")
}