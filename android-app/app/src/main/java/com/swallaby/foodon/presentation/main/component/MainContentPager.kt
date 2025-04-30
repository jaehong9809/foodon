package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.BG300
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.presentation.main.viewmodel.MainUiState

@Composable
fun MainContentPager(
    uiState: MainUiState
) {

    val selectedTabIndex = 0

    val pagerState = rememberPagerState(initialPage = selectedTabIndex, pageCount = { 4 })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(383.dp)
        ) { page ->
            when (page) {
                0 -> CalorieContent(uiState.intakeResult)
                1 -> Text("Page 2")
                2 -> Text("Page 3")
                3 -> Text("Page 4")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            repeat(pagerState.pageCount) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .background(
                            color = if (isSelected) WB500 else BG300,
                            shape = CircleShape
                        )
                )
            }
        }

    }
}
