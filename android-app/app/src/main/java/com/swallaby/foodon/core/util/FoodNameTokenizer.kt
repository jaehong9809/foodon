package com.swallaby.foodon.core.util

// n-gram like 토큰 수동 추출
fun String.generateSearchTokens(): String {
    val result = mutableListOf<String>()

    // 1. 부분 n-gram
    for (start in 0 until this.length) {
        for (end in start + 1..this.length) {
            result.add(this.substring(start, end))
        }
    }

    // 2. 초성 누적 n-gram
    val initials = this.extractInitials()
    for (i in 1..initials.length) {
        result.add(initials.substring(0, i))
    }

    return result.joinToString(" ")
}

// 한글 초성 추출
fun String.extractInitials(): String {
    val sb = StringBuilder()
    for (char in this) {
        if (char in '가'..'힣') {
            val unicode = char - '가'
            val nowCharCode = unicode / (21 * 28)
            sb.append("ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ"[nowCharCode])
        } else {
            sb.append(char)
        }
    }
    return sb.toString()
}
