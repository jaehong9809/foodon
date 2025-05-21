package com.swallaby.foodon.core.util

import android.util.Log
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
        val beforeDecimal = withoutCommas.substring(0, decimalIndex).filter { it.isDigit() }

        // 소수점 뒷부분 (최대 2자리까지만)
        val afterDecimal = withoutCommas.substring(decimalIndex + 1).filter { it.isDigit() }.take(2)

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


class DoubleVisualTransformation : VisualTransformation {
    private val decimalFormat = DecimalFormat("#,##0.00")

    override fun filter(text: AnnotatedString): TransformedText {
        // 입력 텍스트 처리
        val input = text.text

        // 텍스트가 비어있으면 기본 반환
        if (input.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // 소수점 처리를 위한 기본값 설정
        var cleanedInput = input

        // 소수점이 없는 경우 소수점 추가
        if (!input.contains(".")) {
            cleanedInput = "$input.00"
        } else {
            // 소수점이 있는 경우 소수점 이하 자릿수 조정
            val parts = input.split(".")
            val integerPart = parts[0]
            var decimalPart = if (parts.size > 1) parts[1] else "00"

            // 소수점 이하 2자리로 제한
            decimalPart =
                if (decimalPart.length > 2) decimalPart.substring(0, 2) else decimalPart.padEnd(
                    2, '0'
                )
            cleanedInput = "$integerPart.$decimalPart"
        }

        // 숫자 파싱 및 포맷팅
        val number = try {
            cleanedInput.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }

        // 포맷된 숫자 문자열 생성
        val formattedString = decimalFormat.format(number)
        val annotatedString = AnnotatedString(formattedString)

        // 오프셋 매핑 생성
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // 원본 텍스트에서 포맷된 텍스트로 오프셋 변환
                // 소수점 이하 2자리까지만 입력 가능하게 제한

                // 소수점 위치 찾기
                val decimalPointIndex = input.indexOf('.')

                // 소수점이 없거나 소수점 이전 위치면 컴마 고려하여 계산
                if (decimalPointIndex == -1 || offset <= decimalPointIndex) {
                    // 입력한 정수 부분에 해당하는 오프셋 계산 (컴마 고려)
                    val integerPart = if (decimalPointIndex == -1) input else input.substring(
                        0, decimalPointIndex
                    )
                    val commaCount = countCommasUpToPosition(formattedString, offset)
                    return offset + commaCount
                } else {
                    // 소수점 이후 부분
                    // 소수점 이하 2자리 이상 입력했는지 확인
                    val decimalPartLength = input.length - decimalPointIndex - 1

                    if (decimalPartLength > 2 && offset > decimalPointIndex + 2) {
                        // 소수점 이하 2자리 이상 입력한 경우, 소수점 이하 2자리 위치로 오프셋 조정
                        val integerCommaCount =
                            countCommasUpToPosition(formattedString, formattedString.indexOf('.'))
                        return formattedString.indexOf('.') + 3 // 소수점(1) + 소수점 이하 2자리(2)
                    } else {
                        // 일반적인 경우 (소수점 이하 2자리 이내)
                        val integerCommaCount =
                            countCommasUpToPosition(formattedString, formattedString.indexOf('.'))
                        return offset + integerCommaCount
                    }
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // 포맷된 텍스트에서 원본 텍스트로 오프셋 변환

                val commaCount = countCommasUpToPosition(formattedString, offset)
                Log.d("DoubleVisualTransformation", "offset - commaCount: ${offset - commaCount}")
                return maxOf(0, offset - commaCount)
            }

            // 특정 위치까지 컴마 개수 계산
            private fun countCommasUpToPosition(text: String, position: Int): Int {
                var count = 0
                for (i in 0 until minOf(position, text.length)) {
                    if (text[i] == ',') count++
                }
                return count
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}