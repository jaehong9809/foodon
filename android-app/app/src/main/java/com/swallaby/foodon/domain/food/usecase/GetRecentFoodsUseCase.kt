package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.food.repository.FoodRepository
import javax.inject.Inject

class GetRecentFoodsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(): List<String> {
        return when (val result = foodRepository.getRecentFoods()) {
            is ApiResult.Success -> result.data.map { it.foodName }
            is ApiResult.Failure -> emptyList()
        }
    }
}
