package com.swallaby.foodon.core.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
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

/**
 * Uri에서 파일 경로를 가져오는 유틸리티 (Android N 미만용)
 */
object UriUtils {
    /**
     * Uri에서 파일 경로를 추출하는 메서드
     * @param context 컨텍스트
     * @param uri 파일 경로를 추출할 Uri
     * @return 파일 경로 문자열 또는 null
     */
    fun getPathFromUri(context: Context, uri: Uri): String? {
        // URI 스키마 확인
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]

                        if ("primary".equals(type, ignoreCase = true)) {
                            return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                        }

                        // 일부 기기에서는 SD 카드 등의 추가 저장소에 대해 다른 경로를 사용할 수 있음
                        // 예: /storage/sdcard1/
                    }

                    // DownloadsProvider
                    isDownloadsDocument(uri) -> {
                        val id = DocumentsContract.getDocumentId(uri)

                        if (id.startsWith("raw:")) {
                            // Android 8.0 이상에서 raw: 접두사를 사용하는 경우
                            return id.substring(4)
                        }

                        try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), id.toLong()
                            )
                            return getDataColumn(context, contentUri, null, null)
                        } catch (e: NumberFormatException) {
                            // id가 숫자가 아닌 경우 다른 방법 시도
                            val contentUri = Uri.parse(
                                "content://downloads/all_downloads/document/$id"
                            )
                            return getDataColumn(context, contentUri, null, null)
                        }
                    }

                    // MediaProvider
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]

                        val contentUri = when (type) {
                            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            else -> null
                        }

                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])

                        return contentUri?.let {
                            getDataColumn(
                                context, it, selection, selectionArgs
                            )
                        }
                    }
                }
            }

            // MediaStore (및 일반 ContentProvider)
            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Google Photos/Google Drive에서 선택한 이미지 처리
                return if (isGooglePhotosUri(uri) || isGoogleDriveUri(uri)) {
                    // Google Photos에서는 직접 경로를 얻기 어려움
                    // 임시 파일로 복사하는 방법 사용
                    copyFileToTemp(context, uri)
                } else {
                    // 일반 콘텐츠 URI
                    getDataColumn(context, uri, null, null)
                }
            }

            // 파일 URI
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }

        return null
    }

    /**
     * ContentResolver를 통해 데이터 컬럼 값 획득
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.MediaColumns.DATA
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )

            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return null
    }

    /**
     * 파일을 임시 파일로 복사
     * Google Photos 등에서 직접 파일 경로를 얻을 수 없는 경우 사용
     */
    private fun copyFileToTemp(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri) ?: "temp_file"
        val tempFile = File(context.cacheDir, fileName)

        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(4 * 1024) // 4KB 버퍼
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            return tempFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Uri에서 파일 이름 추출
     */
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null

        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        fileName = it.getString(displayNameIndex)
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = uri.path
            val cut = fileName?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                fileName = fileName?.substring(cut + 1)
            }
        }

        return fileName
    }

    /**
     * URI 유형 확인 함수들
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority || "com.google.android.apps.photos.contentprovider" == uri.authority
    }

    private fun isGoogleDriveUri(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
    }
}