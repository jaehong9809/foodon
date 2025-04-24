package com.swallaby.foodon.core.util

import java.text.NumberFormat
import java.util.Locale

object StringUtil {
    fun formatKcal(kcal: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        val formattedKcal = formatter.format(kcal)

        return formattedKcal
    }
}