package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.domain.food.repository.FoodRepository
import javax.inject.Inject

class FetchMealDetailInfoUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(mealId: Long) = foodRepository.getMealDetail(mealId)
}