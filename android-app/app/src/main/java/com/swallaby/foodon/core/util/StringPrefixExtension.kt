package com.swallaby.foodon.core.util

fun String.generatePrefixes(): String {
    val result = mutableListOf<String>()
    for (i in 1..this.length) {
        result.add(this.substring(0, i))
    }
    return result.joinToString(",")
}