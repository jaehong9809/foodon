package com.swallaby.foodon.presentation.foodsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.domain.food.model.Food
import com.swallaby.foodon.presentation.foodsearch.component.FoodRegisterBottomBanner
import com.swallaby.foodon.presentation.foodsearch.component.RecentFoodChips
import com.swallaby.foodon.presentation.foodsearch.component.SearchBar
import com.swallaby.foodon.presentation.foodsearch.component.SearchResultList
import com.swallaby.foodon.presentation.foodsearch.viewmodel.FoodSearchViewModel

@Composable
fun FoodSearchScreen(
    viewModel: FoodSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchResults = uiState.searchResults.collectAsLazyPagingItems()

    FoodSearchContent(
        query = uiState.query,
        recentFoods = uiState.recentFoods,
        searchResults = searchResults,
        onQueryChange = { viewModel.onQueryChange(it) },
        onClearClick = { viewModel.onClearClick() },
        onChipClick = { viewModel.onChipClick(it) },
        onChipRemove = { viewModel.onChipRemove(it) },
        onSearchResultClick = { /* TODO: 아이템 클릭 처리 */ },
        showBanner = uiState.showBanner,
        bannerFoodName = uiState.bannerFoodName,
        onBannerRegisterClick = { viewModel.onBannerRegisterClick() }
    )
}


@Composable
fun FoodSearchContent(
    query: String,
    recentFoods: List<String>,
    searchResults: LazyPagingItems<Food>,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onChipClick: (String) -> Unit,
    onChipRemove: (String) -> Unit,
    onSearchResultClick: (Food) -> Unit,
    showBanner: Boolean,
    bannerFoodName: String,
    onBannerRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MainWhite)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonBackTopBar(
                title = stringResource(R.string.search_food)
            ) {
                // TODO: 뒤로 가기 버튼 로직 (예: popBackStack)
            }

            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onClearClick = onClearClick,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            RecentFoodChips(
                recentFoods = recentFoods,
                onChipClick = onChipClick,
                onChipRemove = onChipRemove
            )

            SearchResultList(
                searchResults = searchResults,
                onClick = onSearchResultClick
            )
        }

        if (showBanner) {
            FoodRegisterBottomBanner(
                foodName = bannerFoodName,
                onRegisterClick = onBannerRegisterClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
