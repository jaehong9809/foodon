package com.swallaby.foodon.presentation.foodsearch.viewmodel

import android.util.Log
import androidx.paging.PagingData
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.domain.food.usecase.SearchFoodNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val searchFoodNameUseCase: SearchFoodNameUseCase
) : BaseViewModel<FoodSearchUiState>(FoodSearchUiState()) {

    private fun updateState(reducer: (FoodSearchUiState) -> FoodSearchUiState) {
        _uiState.value = reducer(_uiState.value)
    }

    fun onQueryChange(query: String) {
        val start = System.currentTimeMillis()
        updateState { it.copy(
            query = query,
            searchResults = searchFoodNameUseCase(query))
        }
        val end = System.currentTimeMillis()

        Log.d("SearchPerformance", "검색 소요 시간: ${end - start} ms")
    }

    fun onClearClick() {
        updateState { it.copy(query = "", searchResults = flowOf(PagingData.empty())) }
    }

    fun onChipClick(chip: String) {
        onQueryChange(chip)
    }

    fun onChipRemove(chip: String) {
        updateState { it.copy(recentFoods = it.recentFoods - chip) }
    }

    fun onBannerRegisterClick() {
        // TODO: 배너 클릭 처리 로직 (예: 등록 API 호출)
    }
}