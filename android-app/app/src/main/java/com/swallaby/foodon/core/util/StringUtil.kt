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
    fun formatNutrition(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return if (amount >= 1000) {
            val kgWeight = amount / 1000.0
            stringResource(R.string.format_nutrition_kg, formatter.format(kgWeight))
        } else {
            stringResource(R.string.format_nutrition_g, formatter.format(amount))
        }
    }

    @Composable
    fun formatNutrition(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return if (amount >= 1000) {
            val kgWeight = amount / 1000.0
            stringResource(R.string.format_nutrition_kg, formatter.format(kgWeight))
        } else {
            stringResource(R.string.format_nutrition_g, formatter.format(amount))
        }
    }

    fun formatNutritionNumber(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return formatter.format(amount)
    }

    @Composable
    fun formatNutritionOrigin(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        return stringResource(R.string.format_nutrition_g, formatter.format(amount))
    }
}