package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.repository.FoodRepository
import javax.inject.Inject

class FetchFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    suspend operator fun invoke(foodId: Long, type: FoodType) = foodRepository.getFood(foodId, type)
}