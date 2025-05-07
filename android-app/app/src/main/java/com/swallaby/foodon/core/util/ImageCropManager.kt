package com.swallaby.foodon.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.swallaby.foodon.domain.food.model.Position

class ImageCropManager(private val context: Context) {
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

                // 각 크롭 영역에 대해 이미지 생성 및 캐싱
                cropRegions.forEach { position ->
                    val croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        position.x,
                        position.y,
                        position.width.toInt(),
                        position.height.toInt()
                    )

                    // 캐시에 저장
                    val cacheKey = buildCacheKey(imageUrl, position)
                    imageLoader.memoryCache?.set(
                        MemoryCache.Key(cacheKey),
                        MemoryCache.Value(croppedBitmap)
                    )
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