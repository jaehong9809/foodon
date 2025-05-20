package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest
import com.swallaby.foodon.domain.food.model.FoodInfoWithId
import com.swallaby.foodon.domain.food.repository.FoodRepository
import javax.inject.Inject

class RegisterCustomFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    suspend operator fun invoke(request: CustomFoodRequest): ApiResult<FoodInfoWithId> =
        foodRepository.postCustomFood(request)
}