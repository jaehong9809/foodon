package com.swallaby.foodon.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.swallaby.foodon.R
import java.text.NumberFormat
import java.util.Locale

object StringUtil {
    fun formatKcal(kcal: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        val formattedKcal = formatter.format(kcal)

        return formattedKcal
    }

    @Composable
    fun formatNutrition(amount: Double, defaultUnit: Int = R.string.format_nutrition_g): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return stringResource(defaultUnit, formatter.format(amount))
    }

    fun formatNutritionNumber(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return formatter.format(amount)
    }

    @Composable
    fun formatNutritionOrigin(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return stringResource(R.string.format_nutrition_g, formatter.format(amount))
    }
}