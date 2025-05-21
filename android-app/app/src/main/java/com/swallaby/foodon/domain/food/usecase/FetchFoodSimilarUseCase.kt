package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.domain.food.repository.FoodRepository
import javax.inject.Inject

class FetchFoodSimilarUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
) {
    suspend operator fun invoke(name: String) = foodRepository.getFoodSimilar(name)
}