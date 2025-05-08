package com.swallaby.foodon.domain.food.usecase

import com.swallaby.foodon.domain.food.model.Food
import com.swallaby.foodon.domain.food.repository.FoodSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchFoodNameUseCase @Inject constructor(
    private val repository: FoodSearchRepository
) {
    operator fun invoke(query: String): Flow<List<Food>> {
        return repository.searchFoods(query)
    }
}