package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.repository.FoodRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadMealUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    suspend operator fun invoke(image: MultipartBody.Part): ApiResult<MealInfo> =
        foodRepository.postMealImage(image)
}