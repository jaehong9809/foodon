package com.swallaby.foodon.core.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.swallaby.foodon.R

object ImageUtil {
    /**
     * 이미지 해상도를 검증하는 함수
     * @param uri 이미지 URI
     * @param context Context
     * @param minWidth 최소 너비 (기본값: 500)
     * @param minHeight 최소 높이 (기본값: 500)
     * @return Pair<Boolean, Int> 첫 번째 값은 유효성 여부, 두 번째 값은 실패 시 오류 메시지 리소스 ID
     */
    fun validateImageResolution(
        uri: Uri,
        context: Context,
        minWidth: Int = 500,
        minHeight: Int = 500,
    ): Pair<Boolean, Int> {
        try {
            // 이미지 크기 정보 가져오기
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true // 메모리에 실제 비트맵을 로드하지 않고 크기 정보만 가져옴
            }

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            val width = options.outWidth
            val height = options.outHeight

            // 최소 해상도 검증
            if (width < minWidth || height < minHeight) {
                Log.e(
                    "ImageValidator",
                    "이미지 해상도가 최소 ${minWidth}x${minHeight} 이상이어야 합니다. 현재: ${width}x${height}"
                )
                return Pair(false, R.string.invalid_image_resolution)
            }

            return Pair(true, 0)
        } catch (e: Exception) {
            Log.e("ImageValidator", "이미지 해상도 검증 중 오류 발생", e)
            return Pair(false, R.string.invalid_image_resolution)
        }
    }

    /**
     * 이미지 비율을 검증하는 함수
     * @param uri 이미지 URI
     * @param context Context
     * @param maxAspectRatio 최대 허용 가능한 가로세로 비율 (기본값: 3.0)
     * @return Pair<Boolean, Int> 첫 번째 값은 유효성 여부, 두 번째 값은 실패 시 오류 메시지 리소스 ID
     */
    fun validateImageAspectRatio(
        uri: Uri,
        context: Context,
        maxAspectRatio: Float = 3.0f,
    ): Pair<Boolean, Int> {
        try {
            // 이미지 크기 정보 가져오기
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            val width = options.outWidth
            val height = options.outHeight

            // 가로세로 비율 검증
            val aspectRatio =
                if (width > height) width.toFloat() / height else height.toFloat() / width
            if (aspectRatio > maxAspectRatio) {
                Log.e("ImageValidator", "이미지 비율이 비정상적입니다. 현재 비율: ${"%.1f".format(aspectRatio)}:1")
                return Pair(false, R.string.invalid_image_aspect_ratio)
            }

            return Pair(true, 0)
        } catch (e: Exception) {
            Log.e("ImageValidator", "이미지 비율 검증 중 오류 발생", e)
            return Pair(false, R.string.invalid_image_aspect_ratio)
        }
    }

    /**
     * 이미지 크기(파일 용량)를 검증하는 함수
     * @param uri 이미지 URI
     * @param context Context
     * @param maxSizeInBytes 최대 허용 크기 (바이트 단위, 기본값: 10MB)
     * @return Pair<Boolean, Int> 첫 번째 값은 유효성 여부, 두 번째 값은 실패 시 오류 메시지 리소스 ID
     */
    fun validateImageSize(
        uri: Uri,
        context: Context,
        maxSizeInBytes: Long = 10 * 1024 * 1024, // 기본 10MB
    ): Pair<Boolean, Int> {
        try {
            // 파일 크기 가져오기
            val fileSize = getFileSize(uri, context)

            if (fileSize > maxSizeInBytes) {
                val maxSizeMB = maxSizeInBytes / (1024 * 1024)
                Log.e(
                    "ImageValidator",
                    "이미지 크기가 ${maxSizeMB}MB를 초과합니다. 현재: ${fileSize / (1024 * 1024)}MB"
                )
                return Pair(false, R.string.over_size_image)
            }

            return Pair(true, 0)
        } catch (e: Exception) {
            Log.e("ImageValidator", "이미지 크기 검증 중 오류 발생", e)
            return Pair(false, R.string.over_size_image)
        }
    }

    /**
     * URI에서 파일 크기를 가져오는 함수
     */
    private fun getFileSize(uri: Uri, context: Context): Long {
        return try {
            // ContentResolver를 통해 파일 크기 가져오기
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val sizeColumnIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeColumnIndex != -1 && cursor.moveToFirst()) {
                    return cursor.getLong(sizeColumnIndex)
                }
            }

            // 쿼리로 크기를 가져올 수 없는 경우 스트림으로 측정
            var size = 0L
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                size = inputStream.available().toLong()
            }

            // 위 방법들로도 크기를 가져올 수 없는 경우, 직접 스트림을 읽어서 측정
            if (size <= 0) {
                size = 0L
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val buffer = ByteArray(8 * 1024) // 8KB 버퍼
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        size += bytesRead
                    }
                }
            }

            size
        } catch (e: Exception) {
            Log.e("ImageValidator", "파일 크기 가져오기 실패", e)
            0L // 크기를 확인할 수 없으면 0을 반환하여 검증 실패를 유도
        }
    }

    /**
     * 이미지에 대한 모든 검증을 수행하는 통합 함수
     * @param uri 이미지 URI
     * @param context Context
     * @param minWidth 최소 너비 (기본값: 500)
     * @param minHeight 최소 높이 (기본값: 500)
     * @param maxAspectRatio 최대 허용 가능한 가로세로 비율 (기본값: 3.0)
     * @param maxSizeInBytes 최대 허용 크기 (바이트 단위, 기본값: 10MB)
     * @return Pair<Boolean, Int> 첫 번째 값은 유효성 여부, 두 번째 값은 실패 시 오류 메시지 리소스 ID
     */
    fun validateImage(
        uri: Uri,
        context: Context,
        minWidth: Int = 500,
        minHeight: Int = 500,
        maxAspectRatio: Float = 3.0f,
        maxSizeInBytes: Long = 10 * 1024 * 1024, // 기본 10MB
    ): Pair<Boolean, Int> {
        // 이미지 크기(용량) 검증
        val sizeValidation = validateImageSize(uri, context, maxSizeInBytes)
        if (!sizeValidation.first) {
            return sizeValidation
        }

        // 이미지 해상도 검증
        val resolutionValidation = validateImageResolution(uri, context, minWidth, minHeight)
        if (!resolutionValidation.first) {
            return resolutionValidation
        }

        // 이미지 비율 검증
        val aspectRatioValidation = validateImageAspectRatio(uri, context, maxAspectRatio)
        if (!aspectRatioValidation.first) {
            return aspectRatioValidation
        }

        return Pair(true, 0)
    }
}