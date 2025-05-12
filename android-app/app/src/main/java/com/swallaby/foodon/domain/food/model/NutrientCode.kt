package com.swallaby.foodon.domain.food.model

import androidx.annotation.DrawableRes
import com.swallaby.foodon.R

enum class NutrientCode(@DrawableRes val icon: Int) {
    KCAL(R.drawable.icon_nutrient_empty),
    CARBS(R.drawable.icon_carbs),
    SUGAR(R.drawable.icon_candy),
    FIBER(R.drawable.icon_apple),
    PROTEIN(R.drawable.icon_protein),
    FAT(R.drawable.icon_pizza),
    SATURATED_FAT(R.drawable.icon_donut),
    TRANS_FAT(R.drawable.icon_butter),
    FATTY_ACID(R.drawable.icon_bacon),
    UNSATURATED_FAT(R.drawable.icon_peanut),
    CHOLESTEROL(R.drawable.icon_egg),
    SODIUM(R.drawable.icon_salt),
    POTASSIUM(R.drawable.icon_avocado),
    ALCOHOL(R.drawable.icon_bear),
    CAFFEINE(R.drawable.icon_coffee)
}