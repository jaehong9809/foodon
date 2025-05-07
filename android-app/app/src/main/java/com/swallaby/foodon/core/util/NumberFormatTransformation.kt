package com.swallaby.foodon.core.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat


// 숫자 포맷 패턴 정의
enum class NumberFormatPattern {
    PLAIN,           // 포맷 없음: "1234567"
    THOUSAND_COMMA,  // 천 단위 콤마: "1,234,567"
}

/**
 * 숫자 입력을 위한 VisualTransformation 클래스
 * @param pattern 적용할 숫자 포맷 패턴
 */
class NumberFormatTransformation(private val pattern: NumberFormatPattern) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 숫자만 추출
        val digitsOnly = text.text.filter { it.isDigit() }

        // 포맷팅된 텍스트와 커서 위치 매핑을 위한 객체
        return when (pattern) {
            NumberFormatPattern.PLAIN -> TransformedText(
                AnnotatedString(digitsOnly), OffsetMapping.Identity
            )

            NumberFormatPattern.THOUSAND_COMMA -> {
                if (digitsOnly.isEmpty()) {
                    return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
                }

                val number = digitsOnly.toBigDecimalOrNull() ?: return TransformedText(
                    AnnotatedString(digitsOnly), OffsetMapping.Identity
                )

                val formatted = DecimalFormat("#,###").format(number)

                // 커서 위치 조정을 위한 오프셋 매핑
                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        // 컴마 개수 계산
                        val commasBeforeOffset = digitsOnly.take(offset).length / 3
                        return minOf(offset + commasBeforeOffset, formatted.length)
                    }

                    override fun transformedToOriginal(offset: Int): Int {
                        // 입력된 오프셋까지의 컴마 개수를 세어 원래 위치 계산
                        var commas = 0
                        for (i in 0 until minOf(offset, formatted.length)) {
                            if (formatted[i] == ',') commas++
                        }
                        return maxOf(0, offset - commas)
                    }
                }

                TransformedText(AnnotatedString(formatted), offsetMapping)
            }
        }
    }
}
