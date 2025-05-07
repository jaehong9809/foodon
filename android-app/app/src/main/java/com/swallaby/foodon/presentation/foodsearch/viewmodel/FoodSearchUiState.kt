package com.swallaby.foodon.presentation.foodsearch.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.domain.food.model.Food

data class FoodSearchUiState(
    val query: String = "",
    val recentFoods: List<String> = emptyList(),
    val searchResults: List<Food> = emptyList(),
    val showBanner: Boolean = false,
    val bannerFoodName: String = ""
) : UiState
