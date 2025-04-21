package com.swallaby.foodon.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.swallaby.foodon.R

val NotoWithSpoqaFallback = FontFamily(
    // NotoSans 먼저 (한글)
    Font(R.font.noto_sans_kr_regular, FontWeight.Normal),
    Font(R.font.noto_sans_kr_medium, FontWeight.Medium),
    Font(R.font.noto_sans_kr_semibold, FontWeight.SemiBold),
    Font(R.font.noto_sans_kr_bold, FontWeight.Bold),

    // SpoqaSans fallback (영어/숫자)
    Font(R.font.spoqa_han_sans_neo_regular, FontWeight.Normal),
    Font(R.font.spoqa_han_sans_neo_medium, FontWeight.Medium),
    Font(R.font.spoqa_han_sans_neo_bold, FontWeight.Bold),
)

object CustomTypography {
    val displayLarge = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 30 .sp,
    )

    val displayMedium = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )

    val displaySmall = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )

    val headlineLarge = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp
    )

    val headlineMedium = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )

    val headlineSmall = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    val titleLarge = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    val titleMedium = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp
    )

    val titleSmall = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    val bodyLarge = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 16.sp
    )
    val bodyMedium = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    val bodySmall = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22 .sp
    )

    val labelLarge = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )
    val labelSmall = TextStyle(
        fontFamily = NotoWithSpoqaFallback,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 16.sp
    )
}

val Typography = Typography(
    displayLarge = CustomTypography.displayLarge,
    displayMedium = CustomTypography.displayMedium,
    displaySmall = CustomTypography.displaySmall,

    headlineLarge = CustomTypography.headlineLarge,
    headlineMedium = CustomTypography.headlineMedium,
    headlineSmall = CustomTypography.headlineSmall,

    titleLarge = CustomTypography.titleLarge,
    titleMedium = CustomTypography.titleMedium,
    titleSmall = CustomTypography.titleSmall,

    bodyLarge = CustomTypography.bodyLarge,
    bodyMedium = CustomTypography.bodyMedium,
    bodySmall = CustomTypography.bodySmall,

    labelLarge = CustomTypography.labelLarge,
    labelSmall = CustomTypography.labelSmall
)