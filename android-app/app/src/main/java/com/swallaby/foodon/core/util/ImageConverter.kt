package com.swallaby.foodon.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class ImageConverter {
    companion object {
        private const val TAG = "ImageConverter"

        fun convertUriToWebP(context: Context, imageUri: Uri, quality: Int): File? {
            val startTime = System.currentTimeMillis()
            Log.d(TAG, "WebP 변환 시작: ${formatTimestamp(startTime)}")

            try {
                // URI에서 입력 스트림 얻기
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    // 단계별 시간 측정을 위한 타임스탬프
                    val decodeStartTime = System.currentTimeMillis()

                    // 비트맵으로 디코딩
                    val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null

                    val decodeEndTime = System.currentTimeMillis()
                    val decodeDuration = decodeEndTime - decodeStartTime
                    Log.d(
                        TAG, "비트맵 디코딩 완료: ${decodeDuration}ms (${formatDuration(decodeDuration)})"
                    )

                    // 이미지의 EXIF 방향 정보 읽기 시작
                    val exifStartTime = System.currentTimeMillis()
                    val orientation = getImageOrientation(context, imageUri)
                    val exifEndTime = System.currentTimeMillis()
                    val exifDuration = exifEndTime - exifStartTime
                    Log.d(TAG, "EXIF 정보 읽기 완료: ${exifDuration}ms (${formatDuration(exifDuration)})")

                    // EXIF 방향에 따라 비트맵 회전 시작
                    val rotateStartTime = System.currentTimeMillis()
                    val rotatedBitmap = rotateBitmapIfNeeded(originalBitmap, orientation)
                    val rotateEndTime = System.currentTimeMillis()
                    val rotateDuration = rotateEndTime - rotateStartTime
                    Log.d(TAG, "비트맵 회전 완료: ${rotateDuration}ms (${formatDuration(rotateDuration)})")

                    // 기존 비트맵 메모리 해제 (원본과 회전된 비트맵이 다른 경우에만)
                    if (originalBitmap != rotatedBitmap) {
                        originalBitmap.recycle()
                    }

                    // 임시 파일 생성
                    val tempFile = createTempWebPFile(context)

                    // WebP 파일로 저장 시작
                    val compressStartTime = System.currentTimeMillis()
                    FileOutputStream(tempFile).use { outputStream ->
                        rotatedBitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
                        outputStream.flush()
                    }
                    val compressEndTime = System.currentTimeMillis()
                    val compressDuration = compressEndTime - compressStartTime
                    Log.d(
                        TAG,
                        "WebP 압축 완료: ${compressDuration}ms (${formatDuration(compressDuration)})"
                    )

                    // 비트맵 크기 로그
                    Log.d(TAG, "비트맵 크기: ${rotatedBitmap.width} x ${rotatedBitmap.height} 픽셀")

                    // 회전된 비트맵 메모리 해제
                    rotatedBitmap.recycle()

                    // 파일 크기 로그
                    val fileSizeKB = tempFile.length() / 1024.0
                    Log.d(TAG, "WebP 파일 크기: ${String.format("%.2f", fileSizeKB)} KB")

                    val endTime = System.currentTimeMillis()
                    val totalDuration = endTime - startTime
                    Log.d(
                        TAG, "WebP 변환 완료: 총 ${totalDuration}ms (${formatDuration(totalDuration)})"
                    )

                    return tempFile
                }
                return null
            } catch (e: IOException) {
                val endTime = System.currentTimeMillis()
                val totalDuration = endTime - startTime
                Log.e(
                    TAG, "WebP 변환 실패: ${e.message}, 소요 시간: ${totalDuration}ms (${
                        formatDuration(totalDuration)
                    })"
                )
                e.printStackTrace()
                return null
            }
        }

        // 타임스탬프를 읽기 쉬운 형식으로 변환
        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

        // 밀리초를 초.밀리초 형식으로 변환
        private fun formatDuration(durationMs: Long): String {
            val seconds = durationMs / 1000
            val milliseconds = durationMs % 1000
            return "${seconds}.${milliseconds}초"
        }

        // EXIF 방향 정보 읽기
        private fun getImageOrientation(context: Context, imageUri: Uri): Int {
            var orientation = ExifInterface.ORIENTATION_NORMAL
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // Android N 이상: inputStream으로 직접 ExifInterface 초기화 가능
                    context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                        val exifInterface = ExifInterface(inputStream)
                        orientation = exifInterface.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
                        )
                    }
                } else {
                    // Android N 미만: 임시 파일을 생성하고 그 경로로 ExifInterface 초기화
                    val tempFile = copyUriToTempFile(context, imageUri)
                    if (tempFile != null && tempFile.exists()) {
                        val exifInterface = ExifInterface(tempFile.absolutePath)
                        orientation = exifInterface.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
                        )
                        // 사용 후 임시 파일 삭제
                        tempFile.delete()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "EXIF 데이터 읽기 실패: ${e.message}")
            }
            return orientation
        }

        // Uri에서 임시 파일로 복사 (Android N 미만용)
        private fun copyUriToTempFile(context: Context, uri: Uri): File? {
            try {
                val tempFile = File(context.cacheDir, "temp_exif_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        val buffer = ByteArray(4 * 1024) // 4K 버퍼
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                        }
                        output.flush()
                    }
                }
                return tempFile
            } catch (e: IOException) {
                Log.e(TAG, "임시 파일 생성 실패: ${e.message}")
                return null
            }
        }

        // 방향 정보에 따라 비트맵 회전
        private fun rotateBitmapIfNeeded(bitmap: Bitmap, orientation: Int): Bitmap {
            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.postRotate(90f)
                    matrix.preScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.postRotate(270f)
                    matrix.preScale(-1f, 1f)
                }

                else -> return bitmap // 회전이 필요 없으면 원본 반환
            }

            // 방향 정보가 정상(회전 없음)이 아닌 경우에만 새 비트맵 생성
            return if (orientation != ExifInterface.ORIENTATION_NORMAL && orientation != 0) {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
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