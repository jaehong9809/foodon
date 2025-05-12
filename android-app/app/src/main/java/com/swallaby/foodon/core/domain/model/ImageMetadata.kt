package com.swallaby.foodon.core.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * 이미지 메타데이터를 담는 데이터 클래스
 */
data class ImageMetadata(
    val dateTimeOriginal: Date? = null,   // 원본 이미지 촬영 시간
    val dateTimeDigitized: Date? = null,  // 디지털 변환 시간
    val dateTime: Date? = null,           // 파일 수정 시간
    val latitude: Float? = null,          // 위도
    val longitude: Float? = null,         // 경도
    val make: String? = null,             // 제조사
    val model: String? = null,            // 기기 모델
    val orientation: Int = 0,             // 이미지 방향
    val width: Int = 0,                   // 이미지 너비
    val height: Int = 0,                  // 이미지 높이
    val rawDateTime: String? = null,       // 원본 날짜 문자열
) {
    // 날짜 형식 지정
    private val displayDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    /**
     * 포맷팅된 촬영 시간을 반환
     * 원본 > 디지털 변환 > 파일 수정 순으로 우선시
     */
    fun getFormattedCaptureTime(): String? {
        val date = dateTimeOriginal ?: dateTimeDigitized ?: dateTime
        return date?.let { displayDateFormat.format(it) }
    }

    /**
     * GPS 좌표가 있는지 확인
     */
    fun hasLocation(): Boolean {
        return latitude != null && longitude != null
    }

    /**
     * 이미지에 촬영 시간 정보가 있는지 확인
     */
    fun hasCaptureTime(): Boolean {
        return dateTimeOriginal != null || dateTimeDigitized != null || dateTime != null
    }
}

