package com.swallaby.foodon.core.util

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.min


// 숫자 포맷 패턴 정의
enum class NumberFormatPattern {
    PLAIN,           // 포맷 없음: "1234567"
    DOUBLE_THOUSAND_COMMA,  // 천 단위 콤마: "1,234,567"
    INT_THOUSAND_COMMA,  // 천 단위 콤마: "1,234,567"
}

/**
 * Int 값만 입력받고 최대값을 제한하는 VisualTransformation
 */
class IntegerVisualTransformation(private val maxValue: Int = Int.MAX_VALUE) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 숫자만 허용하는 정규식으로 필터링
        val filteredText = text.text.replace(Regex("[^0-9]"), "")

        // 빈 문자열이면 그대로 반환
        if (filteredText.isEmpty()) {
            return TransformedText(
                AnnotatedString(""),
                IdentityOffsetMapping
            )
        }

        // 최대값 제한 적용
        val intValue = try {
            filteredText.toInt()
        } catch (e: NumberFormatException) {
            // 숫자가 너무 크면 maxValue로 설정
            maxValue
        }

        val limitedValue = min(intValue, maxValue)
        val resultText = limitedValue.toString()

        // 변환된 텍스트를 AnnotatedString으로 생성
        val annotatedString = AnnotatedString(resultText)

        // 단순한 케이스를 위한 오프셋 매핑
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return min(offset, resultText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return min(offset, text.length)
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}

/**
 * Double 값만 입력받고 최대값을 제한하는 VisualTransformation
 */
class DoubleVisualTransformation(private val maxValue: Double = Double.MAX_VALUE) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text

        // 숫자와 소수점만 남기는 필터링 로직
        val filteredText = buildString {
            var hasDecimalPoint = false

            for (char in input) {
                if (char.isDigit()) {
                    append(char)
                } else if (char == '.' && !hasDecimalPoint) {
                    append(char)
                    hasDecimalPoint = true
                }
            }
        }

        // 빈 문자열이면 그대로 반환
        if (filteredText.isEmpty()) {
            return TransformedText(
                AnnotatedString(""),
                IdentityOffsetMapping
            )
        }

        // 최대값 제한 적용
        val doubleValue = try {
            filteredText.toDouble()
        } catch (e: NumberFormatException) {
            // 소수점만 있거나 숫자 변환에 문제가 있으면 0.0 반환
            if (filteredText == ".") 0.0 else maxValue
        }

        val limitedValue = min(doubleValue, maxValue)

        // 소수점 형식 유지를 위한 처리
        val resultText = if (filteredText.endsWith(".")) {
            "${limitedValue.toInt()}."
        } else if (filteredText.contains(".")) {
            // 원본 소수점 이하 자릿수를 유지
            val decimalParts = filteredText.split(".")
            if (decimalParts.size > 1) {
                val decimalPlaces = decimalParts[1].length
                String.format("%.${decimalPlaces}f", limitedValue)
            } else {
                limitedValue.toString()
            }
        } else {
            limitedValue.toString()
        }

        // 변환된 텍스트를 AnnotatedString으로 생성
        val annotatedString = AnnotatedString(resultText)

        // 오프셋 매핑 정의
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return min(offset, resultText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return min(offset, text.length)
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}

// 간단한 identity 오프셋 매핑 객체
private object IdentityOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = offset
    override fun transformedToOriginal(offset: Int): Int = offset
}