package com.swallaby.foodon.core.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat


// 숫자 포맷 패턴 정의
enum class NumberFormatPattern {
    PLAIN,           // 포맷 없음: "1234567"
    DOUBLE_THOUSAND_COMMA,  // 천 단위 콤마: "1,234,567"
    INT_THOUSAND_COMMA,  // 천 단위 콤마: "1,234,567"
}

/**
 * Double 타입을 위한 VisualTransformation
 * - 천 단위 구분자(,) 추가 (정수 부분만)
 * - 소수점 이하 유지
 * - 정확한 커서 위치 매핑
 */
// 입력값 검증 및 정리 함수
fun cleanDoubleInput(input: String): String {
    // 콤마 제거
    val withoutCommas = input.replace(",", "")

    // 이미 소수점이 있는지 확인
    val decimalIndex = withoutCommas.indexOf('.')

    // 입력값에 소수점이 여러 개 있는 경우 처리
    return if (decimalIndex != -1) {
        // 소수점 앞부분 (숫자만 허용)
        val beforeDecimal = withoutCommas.substring(0, decimalIndex)
            .filter { it.isDigit() }

        // 소수점 뒷부분 (최대 2자리까지만)
        val afterDecimal = withoutCommas.substring(decimalIndex + 1)
            .filter { it.isDigit() }
            .take(2)

        // 소수점 앞부분과 뒷부분이 모두 비어있지 않은 경우에만 소수점 추가
        if (beforeDecimal.isEmpty() && afterDecimal.isEmpty()) {
            ""
        } else if (beforeDecimal.isEmpty()) {
            "0.$afterDecimal"
        } else {
            "$beforeDecimal.$afterDecimal"
        }
    } else {
        // 소수점이 없는 경우, 숫자만 남김
        withoutCommas.filter { it.isDigit() || it == '.' }
    }
}


//class DoubleVisualTransformation(
//    private val pattern: String = "###,###.##",
//    locale: Locale = Locale.getDefault(),
//) : VisualTransformation {
//
//    private val formatter = DecimalFormat(pattern, DecimalFormatSymbols(locale))
//
//    override fun filter(text: AnnotatedString): TransformedText {
//        if (text.text.isEmpty() || text.text.isBlank()) return TransformedText(
//            AnnotatedString(""), OffsetMapping.Identity
//        )
//
//        // 유효한 문자만 필터링 (숫자와 소수점)
//        val filteredText = buildString {
//            var hasDecimal = false
//            for (char in text.text) {
//                when {
//                    char.isDigit() -> append(char)
//                    char == '.' && !hasDecimal -> {
//                        append(char)
//                        hasDecimal = true
//                    }
//                }
//            }
//        }
//
//        val formattedText = formatter.format(filteredText.toDouble())
//
//        val decimalIndex = formattedText.indexOf('.')
//
//        Log.d(
//            "NutrientField", "filter text = ${text.text}, formattedText = $formattedText"
//        )
//
//        val offsetMapping = object : OffsetMapping {
//            override fun originalToTransformed(offset: Int): Int {
//                Log.d("NutrientField", "originalToTransformed offset = $offset")
//
//                val commaCount = if (decimalIndex != -1) {
//                    Log.d(
//                        "NutrientField",
//                        "formattedText.take(decimalIndex) = ${formattedText.take(decimalIndex)}"
//                    )
//                    formattedText.take(decimalIndex).count { it == ',' }
//                } else formattedText.count { it == ',' }
//
//                val decimalCount = offset - decimalIndex.coerceAtLeast(0) - 1
//                Log.d("NutrientField", "decimalCount = $decimalCount")
//
//                val overDecimal = (decimalCount - 2).coerceAtLeast(0)
//                Log.d("NutrientField", "overDecimal = $overDecimal")
//                Log.d("NutrientField", "return = ${formattedText.length - overDecimal}")
//
//                return formattedText.length - overDecimal
//            }
//
//            override fun transformedToOriginal(offset: Int): Int {
//                Log.d("NutrientField", "transformedToOriginal offset = $offset")
//                if (offset <= 0) return 0
//                if (offset >= formattedText.length) return text.length
//
//                Log.d(
//                    "NutrientField",
//                    "originalToTransformed offset = $offset, formattedText = $formattedText"
//                )
//
//                val commaCount = if (decimalIndex != -1) {
//                    Log.d(
//                        "NutrientField",
//                        "formattedText.take(decimalIndex) = ${formattedText.take(decimalIndex)}"
//                    )
//                    formattedText.take(decimalIndex).count { it == ',' }
//                } else formattedText.count { it == ',' }
//                Log.d(
//                    "NutrientField",
//                    "formattedText.length = ${formattedText.length}, return = ${formattedText.length - commaCount}"
//                )
//
//                return formattedText.length - commaCount
//            }
//        }
//        return TransformedText(AnnotatedString(formattedText), offsetMapping)
//    }
//}

/**
 * 숫자 입력을 위한 VisualTransformation 클래스
 * @param pattern 적용할 숫자 포맷 패턴
 */
//class NumberFormatTransformation(private val pattern: NumberFormatPattern) : VisualTransformation {
//    override fun filter(text: AnnotatedString): TransformedText {
//        // 숫자만 추출
//        val digitsOnly =
//            text.text.toBigDecimalOrNull()?.toString() ?: "0" // text.text.filter { it.isDigit() }
//
//
//        // 포맷팅된 텍스트와 커서 위치 매핑을 위한 객체
//        return when (pattern) {
//            NumberFormatPattern.PLAIN -> TransformedText(
//                AnnotatedString(digitsOnly), OffsetMapping.Identity
//            )
//
//            NumberFormatPattern.THOUSAND_COMMA -> {
//                if (digitsOnly.isEmpty()) {
//                    return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
//                }
//
//                val number = digitsOnly.toBigDecimalOrNull() ?: return TransformedText(
//                    AnnotatedString(digitsOnly), OffsetMapping.Identity
//                )
//
//                val formatted = DecimalFormat("#,###").format(number)
//
//                // 커서 위치 조정을 위한 오프셋 매핑
////                val offsetMapping = object : OffsetMapping {
////                    override fun originalToTransformed(offset: Int): Int {
////                        // 컴마 개수 계산
////                        val commasBeforeOffset = digitsOnly.take(offset).length / 3
////                        return minOf(offset + commasBeforeOffset, formatted.length)
////                    }
////
////                    override fun transformedToOriginal(offset: Int): Int {
////                        // 입력된 오프셋까지의 컴마 개수를 세어 원래 위치 계산
////                        var commas = 0
////                        for (i in 0 until minOf(offset, formatted.length)) {
////                            if (formatted[i] == ',') commas++
////                        }
////                        return maxOf(0, offset - commas)
////                    }
////                }
//
//                TransformedText(AnnotatedString(formatted), OffsetMapping.Identity)// offsetMapping
//            }
//        }
//    }
//}


class DecimalVisualTransformation : VisualTransformation {
    private val symbols = DecimalFormat().decimalFormatSymbols
    private val comma = symbols.groupingSeparator
    private val dot = symbols.decimalSeparator
    private val maxFractionDigits = 2

    // 천 단위 구분자 패턴
    private val commaPattern = Regex("\\B(?=(?:\\d{3})+(?!\\d))")

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // 첫 번째 소수점만 인식하도록 제한
        val parts = text.text.split(dot, limit = 2)
        val integerPart = parts[0]
        val fractionPart = if (parts.size > 1) parts[1] else ""

        // 천 단위 구분자 추가
        val formattedIntegerPart = integerPart.replace(commaPattern, comma.toString())

        // 소수점 이하 두 자리까지만 제한
        val limitedFractionPart = fractionPart.take(maxFractionDigits)

        // 결과 문자열 생성
        val formattedText = if (limitedFractionPart.isEmpty()) {
            formattedIntegerPart
        } else {
            "$formattedIntegerPart$dot$limitedFractionPart"
        }

        val newText = AnnotatedString(
            formattedText,
            text.spanStyles,
            text.paragraphStyles
        )

        return TransformedText(
            newText, DecimalOffsetMapping(
                originalText = text.text,
                formattedText = formattedText,
                integerPartLength = integerPart.length,
                formattedIntegerPartLength = formattedIntegerPart.length,
                hasFraction = limitedFractionPart.isNotEmpty()
            )
        )
    }

    private inner class DecimalOffsetMapping(
        val originalText: String,
        val formattedText: String,
        val integerPartLength: Int,
        val formattedIntegerPartLength: Int,
        val hasFraction: Boolean,
    ) : OffsetMapping {

        // 천 단위 구분자(콤마) 위치를 계산
        private val commaPositions = mutableListOf<Int>()

        init {
            // 포맷된 정수 부분에서 콤마 위치 찾기
            var commaCount = 0
            for (i in formattedText.indices) {
                if (i < formattedIntegerPartLength && formattedText[i] == comma) {
                    commaPositions.add(i)
                    commaCount++
                }
            }
        }

        override fun originalToTransformed(offset: Int): Int {
            // 소수점 앞 부분 (정수 부분)
            if (offset <= integerPartLength) {
                var result = offset
                for (commaPos in commaPositions) {
                    if (offset > commaPos - commaPositions.indexOf(commaPos) - 1) {
                        result++
                    }
                }
                return result
            }

            // 소수점 자체의 위치
            if (hasFraction && offset == integerPartLength + 1) {
                return formattedIntegerPartLength + 1
            }

            // 소수점 이후 부분
            if (hasFraction && offset > integerPartLength + 1) {
                val fractionOffset = offset - integerPartLength - 1
                if (fractionOffset <= maxFractionDigits) {
                    return formattedIntegerPartLength + 1 + fractionOffset
                }
                return formattedText.length
            }

            return formattedText.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            // 정수 부분
            if (offset <= formattedIntegerPartLength) {
                var result = offset
                for (commaPos in commaPositions) {
                    if (offset > commaPos) {
                        result--
                    }
                }
                return result
            }

            // 소수점 자체의 위치
            if (hasFraction && offset == formattedIntegerPartLength + 1) {
                return integerPartLength + 1
            }

            // 소수점 이후 부분
            if (hasFraction && offset > formattedIntegerPartLength + 1) {
                val fractionOffset = offset - formattedIntegerPartLength - 1
                return integerPartLength + 1 + fractionOffset
            }

            return originalText.length
        }
    }
}