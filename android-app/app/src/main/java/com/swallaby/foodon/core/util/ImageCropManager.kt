package com.swallaby.foodon.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.swallaby.foodon.domain.food.model.Position

class ImageCropManager(private val context: Context) {
    private val TAG = "ImageCropManager"
    private val imageLoader = context.imageLoader

    // 원본 이미지 로드 및 여러 크롭 이미지 생성
    fun loadAndCropImage(
        imageUrl: String,
        cropRegions: List<Position>,
        onComplete: () -> Unit,
    ) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .target { result ->
                val bitmap = (result as? BitmapDrawable)?.bitmap ?: return@target

                // 원본 이미지 크기
                val imageWidth = bitmap.width
                val imageHeight = bitmap.height

                Log.d(TAG, "Original image size: ${imageWidth}x${imageHeight}")

                // 각 크롭 영역에 대해 이미지 생성 및 캐싱
                cropRegions.forEach { position ->
                    try {
                        // 비율 값을 실제 픽셀 값으로 변환
                        val xPixel = position.x.toInt()
                        val yPixel = position.y.toInt()

                        // width와 height가 비율로 들어오는 경우 (0.0~1.0) 이를 픽셀로 변환
                        // position.width가 1.0이면 이미지 전체 너비를 의미
                        val widthPixel = (position.width * imageWidth).toInt()
                        val heightPixel = (position.height * imageHeight).toInt()

                        // 경계 확인
                        if (xPixel < 0 || yPixel < 0 ||
                            xPixel + widthPixel > imageWidth ||
                            yPixel + heightPixel > imageHeight
                        ) {
                            Log.e(
                                TAG, "Crop region out of bounds: position=$position, " +
                                        "calculated: x=$xPixel, y=$yPixel, width=$widthPixel, height=$heightPixel"
                            )
                            return@forEach
                        }

                        Log.d(
                            TAG, "Cropping image at x=$xPixel, y=$yPixel, " +
                                    "width=$widthPixel, height=$heightPixel"
                        )

                        val croppedBitmap = Bitmap.createBitmap(
                            bitmap,
                            xPixel,
                            yPixel,
                            widthPixel,
                            heightPixel
                        )

                        // 캐시에 저장
                        val cacheKey = buildCacheKey(imageUrl, position)
                        imageLoader.memoryCache?.set(
                            MemoryCache.Key(cacheKey),
                            MemoryCache.Value(croppedBitmap)
                        )

                        Log.d(TAG, "Successfully cached cropped image with key: $cacheKey")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error cropping image", e)
                    }
                }

                onComplete()
            }
            .build()

        imageLoader.enqueue(request)
    }

    // 캐시 키 생성 함수
    private fun buildCacheKey(imageUrl: String, position: Position): String {
        return "$imageUrl-crop-${position.x}-${position.y}-${position.width}-${position.height}"
    }

    // 캐시된 크롭 이미지 가져오는 함수
    fun getCroppedImageRequest(imageUrl: String, position: Position): ImageRequest {
        val cacheKey = buildCacheKey(imageUrl, position)
        return ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(cacheKey)
            .diskCacheKey(cacheKey)
            .build()
    }
}