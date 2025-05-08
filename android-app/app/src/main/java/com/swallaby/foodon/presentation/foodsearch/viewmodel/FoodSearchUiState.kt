package com.swallaby.foodon.presentation.foodsearch.viewmodel

import androidx.paging.PagingData
import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.domain.food.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class FoodSearchUiState(
    val query: String = "",
    val recentFoods: List<String> = emptyList(),
    val searchResults: Flow<PagingData<Food>> = emptyFlow(),
    val showBanner: Boolean = false,
    val bannerFoodName: String = ""
) : UiState
