package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.data.food.remote.dto.request.RecordMealRequest
import com.swallaby.foodon.domain.food.repository.FoodRepository
import javax.inject.Inject

class RecordMealUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    suspend operator fun invoke(request: RecordMealRequest) =
        foodRepository.postFoodHistory(request)
}