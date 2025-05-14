package com.swallaby.foodon.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID


class ImageConverter {
    companion object {
        private const val TAG = "ImageConverter" // 로그 태그

        /**
         * PNG, JPG 또는 JPEG 이미지를 WebP로 변환하고 임시 파일 반환
         * @param sourceFilePath 원본 이미지 파일 경로
         * @param quality WebP 품질 (0-100) 낮을수록 파일 크기 작아짐
         * @return 변환된 WebP 임시 파일, 실패 시 null
         */
        fun convertToWebP(context: Context, sourceFilePath: String, quality: Int): File? {
            try {
                // 원본 파일 가져오기
                val sourceFile = File(sourceFilePath)
                val originalSizeKb = sourceFile.length() / 1024.0

                // 원본 파일 크기 로그 출력
                Log.d(TAG, "원본 파일 경로: $sourceFilePath")
                Log.d(TAG, "원본 파일 크기: ${String.format("%.2f", originalSizeKb)} KB")

                // 이미지 파일 로드
                val bitmap = BitmapFactory.decodeFile(sourceFilePath) ?: return null

                // 비트맵 정보 로그
                Log.d(TAG, "원본 이미지 크기: ${bitmap.width} x ${bitmap.height} 픽셀")

                // 임시 파일 생성
                val tempFile = createTempWebPFile(context)

                // WebP 파일로 저장
                FileOutputStream(tempFile).use { outputStream ->
                    // compress 메서드로 WebP 형식으로 저장 (WEBP 형식, 품질, 출력 스트림)
                    bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
                    outputStream.flush()
                }

                // 메모리 해제
                bitmap.recycle()

                // 변환된 파일 크기 로그 출력
                val webpSizeKb = tempFile.length() / 1024.0
                Log.d(TAG, "WebP 변환 파일 경로: ${tempFile.absolutePath}")
                Log.d(TAG, "WebP 변환 파일 크기: ${String.format("%.2f", webpSizeKb)} KB")

                // 압축률 계산 및 로그 출력
                val compressionRatio = (1 - (webpSizeKb / originalSizeKb)) * 100
                Log.d(TAG, "압축률: ${String.format("%.2f", compressionRatio)}% 감소")

                return tempFile
            } catch (e: IOException) {
                Log.e(TAG, "WebP 변환 실패: ${e.message}")
                e.printStackTrace()
                return null
            }
        }

        /**
         * URI에서 이미지를 WebP로 변환하고 임시 파일 반환
         * @param context Context 객체
         * @param imageUri 이미지 URI
         * @param quality WebP 품질 (0-100)
         * @return 변환된 WebP 임시 파일, 실패 시 null
         */
        fun convertUriToWebP(context: Context, imageUri: Uri, quality: Int): File? {
            try {
                // URI에서 파일 크기 가져오기 (가능한 경우)
                var originalSizeKb = -1.0
                try {
                    context.contentResolver.query(imageUri, null, null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val sizeIndex = cursor.getColumnIndex("_size")
                            if (sizeIndex != -1) {
                                val size = cursor.getLong(sizeIndex)
                                originalSizeKb = size / 1024.0
                                Log.d(TAG, "원본 파일 URI: $imageUri")
                                Log.d(TAG, "원본 파일 크기: ${String.format("%.2f", originalSizeKb)} KB")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "URI 파일 크기를 가져올 수 없음: ${e.message}")
                }

                // URI에서 입력 스트림 얻기
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    // 비트맵으로 디코딩
                    val bitmap = BitmapFactory.decodeStream(inputStream) ?: return null

                    // 비트맵 정보 로그
                    Log.d(TAG, "원본 이미지 크기: ${bitmap.width} x ${bitmap.height} 픽셀")

                    // 임시 파일 생성
                    val tempFile = createTempWebPFile(context)

                    // WebP 파일로 저장
                    FileOutputStream(tempFile).use { outputStream ->
                        // WEBP 형식으로 압축
                        bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
                        outputStream.flush()
                    }

                    // 메모리 해제
                    bitmap.recycle()

                    // 변환된 파일 크기 로그 출력
                    val webpSizeKb = tempFile.length() / 1024.0
                    Log.d(TAG, "WebP 변환 파일 경로: ${tempFile.absolutePath}")
                    Log.d(TAG, "WebP 변환 파일 크기: ${String.format("%.2f", webpSizeKb)} KB")

                    // 압축률 계산 및 로그 출력 (원본 크기를 알 수 있는 경우에만)
                    if (originalSizeKb > 0) {
                        val compressionRatio = (1 - (webpSizeKb / originalSizeKb)) * 100
                        Log.d(TAG, "압축률: ${String.format("%.2f", compressionRatio)}% 감소")
                    }

                    return tempFile
                }
                return null
            } catch (e: IOException) {
                Log.e(TAG, "WebP 변환 실패: ${e.message}")
                e.printStackTrace()
                return null
            }
        }

        /**
         * 임시 WebP 파일 생성
         */
        private fun createTempWebPFile(context: Context): File {
            val fileName = "temp_${UUID.randomUUID()}.webp"

            // 앱 캐시 디렉터리에 임시 파일 생성
            return File(context.cacheDir, fileName)
        }
    }
}