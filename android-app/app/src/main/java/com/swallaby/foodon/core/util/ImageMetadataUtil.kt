package com.swallaby.foodon.core.util

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import com.swallaby.foodon.core.domain.model.ImageMetadata
import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 이미지 파일에서 EXIF 메타데이터를 추출하는 유틸리티 클래스
 */
object ImageMetadataUtil {
    private const val TAG = "ImageMetadataUtil"

    // 날짜 형식 파서
    private val exifDateFormat =
        SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }

    /**
     * 이미지 파일 경로로부터 메타데이터 추출
     * @param filePath 이미지 파일 경로
     * @return 추출된 메타데이터 정보를 담은 ImageMetadata 객체
     */
    fun getMetadataFromPath(filePath: String): ImageMetadata? {
        return try {
            val exif = ExifInterface(filePath)
            extractMetadata(exif)
        } catch (e: IOException) {
            Log.e(TAG, "Error reading EXIF data from file path: $filePath", e)
            null
        }
    }

    /**
     * Uri로부터 메타데이터 추출 (안드로이드 Q 이상에서 필요)
     * @param context 컨텍스트
     * @param uri 이미지 Uri
     * @return 추출된 메타데이터 정보를 담은 ImageMetadata 객체
     */
    fun getMetadataFromUri(context: Context, uri: Uri): ImageMetadata? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val exif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ExifInterface(inputStream)
                } else {
                    // Android N 미만에서는 파일 경로가 필요
                    val path = UriUtils.getPathFromUri(context, uri)
                    if (path != null) ExifInterface(path) else return null
                }
                extractMetadata(exif)
            } else {
                null
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading EXIF data from URI: $uri", e)
            null
        } finally {
            inputStream?.close()
        }
    }

    /**
     * ExifInterface에서 메타데이터 추출
     * @param exif ExifInterface 객체
     * @return 추출된 메타데이터 정보를 담은 ImageMetadata 객체
     */
    private fun extractMetadata(exif: ExifInterface): ImageMetadata {
        // 날짜 시간 정보 추출
        val dateTimeOriginal = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
        val dateTimeDigitized = exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
        val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)

        // GPS 정보 추출
        val latLong = FloatArray(2)
        val hasLatLong = exif.getLatLong(latLong)

        // 기기 정보 추출
        val make = exif.getAttribute(ExifInterface.TAG_MAKE)
        val model = exif.getAttribute(ExifInterface.TAG_MODEL)

        // 이미지 방향 정보
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        )

        // 해상도 정보
        val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
        val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

        return ImageMetadata(
            dateTimeOriginal = parseExifDate(dateTimeOriginal),
            dateTimeDigitized = parseExifDate(dateTimeDigitized),
            dateTime = parseExifDate(dateTime),
            latitude = if (hasLatLong) latLong[0] else null,
            longitude = if (hasLatLong) latLong[1] else null,
            make = make,
            model = model,
            orientation = orientation,
            width = width,
            height = height,
            rawDateTime = dateTimeOriginal ?: dateTimeDigitized ?: dateTime
        )
    }

    /**
     * EXIF 날짜 문자열을 Date 객체로 파싱
     * @param dateString EXIF 형식의 날짜 문자열 (ex: "2023:05:20 14:30:45")
     * @return 파싱된 Date 객체 또는 null
     */
    private fun parseExifDate(dateString: String?): Date? {
        if (dateString.isNullOrEmpty()) return null

        return try {
            exifDateFormat.parse(dateString)
        } catch (e: ParseException) {
            Log.e(TAG, "Error parsing date: $dateString", e)
            null
        }
    }
}
