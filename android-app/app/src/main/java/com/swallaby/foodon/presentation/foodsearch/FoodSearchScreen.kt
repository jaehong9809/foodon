package com.swallaby.foodon.presentation.foodsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.domain.food.model.Food
import com.swallaby.foodon.presentation.foodsearch.component.FoodRegisterBottomSheet
import com.swallaby.foodon.presentation.foodsearch.component.RecentFoodChips
import com.swallaby.foodon.presentation.foodsearch.component.SearchBar
import com.swallaby.foodon.presentation.foodsearch.component.SearchResultList

@Composable
fun FoodSearchScreen(
    query: String,
    recentFoods: List<String>,
    searchResults: List<Food>,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onChipClick: (String) -> Unit,
    onChipRemove: (String) -> Unit,
    onSearchResultClick: (Food) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MainWhite)
    ) {
        CommonBackTopBar(
            title = stringResource(R.string.search_food)
        ) {
            // TODO: 뒤로 가기 버튼 로직 체크 (PopStack 체크)
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
}


@Preview
@Composable
fun FoodSearchScreenPreview() {
    var query by remember { mutableStateOf("") }
    var recentFoods by remember {
        mutableStateOf(listOf("피자", "샐러드", "밥", "김치", "김치볶음밥"))
    }
    val foodItems = listOf(
        Food("햄버거", "1회분", 200, true),
        Food("햄버거", "1회분", 200, false)
    )

    var isBottomSheetVisible by remember { mutableStateOf(true) }
    var selectedFoodName by remember { mutableStateOf("연어 샐러드") }

    FoodSearchScreen(
        query = query,
        recentFoods = recentFoods,
        searchResults = foodItems,
        onQueryChange = { query = it },
        onClearClick = { query = "" },
        onChipClick = { clickedFood ->
            query = clickedFood
        },
        onChipRemove = { removedFood ->
            recentFoods = recentFoods - removedFood
        },
        onSearchResultClick = {},
    )
}
