package com.swallaby.foodon.presentation.foodsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.swallaby.foodon.presentation.foodsearch.component.RecentFoodChips
import com.swallaby.foodon.presentation.foodsearch.component.SearchBar

@Composable
fun FoodSearchScreen(

){
    var query by remember { mutableStateOf("") }
    var fakeRecentFoods by remember {
        mutableStateOf(listOf("피자", "샐러드", "밥", "김치", "김치볶음밥"))
    }

    Column(
        modifier = Modifier
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
            onQueryChange = { query = it },
            onClearClick = { query = "" },
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        RecentFoodChips(
            recentFoods = fakeRecentFoods,
            onChipClick = { clickedFood ->
                query = clickedFood
            },
            onChipRemove = { removedFood ->
                fakeRecentFoods = fakeRecentFoods - removedFood
            }
        )
    }
}

@Preview
@Composable
fun FoodSearchScreenPreview() {
    FoodSearchScreen()
}