package com.swallaby.foodon.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.component.MonthlyTabBar
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.presentation.calendar.component.TabContentPager

@Composable
fun CalendarScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // TODO: 탭 변경 시 캘린더 데이터 갱신
    LaunchedEffect(selectedTabIndex) {

    }

    Scaffold(
        floatingActionButton = {
            MonthlyTabBar(
                modifier = Modifier.padding(bottom = 16.dp),
                selectedIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 96.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.height(200.dp)
                ) {
                    Text(
                        text = "캘린더 영역",
                        style = NotoTypography.NotoBold20
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border025)
                )

                TabContentPager(
                    selectedTab = selectedTabIndex,
                    onTabChanged = {
                        selectedTabIndex = it

                        // TODO: 탭 바뀔 때마다 각 탭에 맞는 API 호출,  데이터 갱신
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FoodonTheme {
        CalendarScreen()
    }
}