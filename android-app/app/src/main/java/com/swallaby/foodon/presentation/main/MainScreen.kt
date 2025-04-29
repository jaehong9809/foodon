package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.FloatingButton
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.presentation.main.component.MainCalendarHeader
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {

    val navController = LocalNavController.current
    val uiState by viewModel.uiState.collectAsState()

    val today = uiState.today
    val selectedDate = uiState.selectedDate
    val currentYearMonth = uiState.currentYearMonth

    Scaffold(
        floatingActionButton = {
            FloatingButton(
                modifier = Modifier,
                icon = R.drawable.icon_ai_camera,
                text = stringResource(R.string.btn_record)
            ) {
                // TODO: 식사 기록 화면으로 이동
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {

            MainCalendarHeader(
                currentYearMonth = currentYearMonth,
                onMonthlyClick = {
                    navController.navigate(NavRoutes.Calendar.route)
                },
                onTodayClick = {
                    viewModel.selectDate(today)
                }
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .background(WB500)
                    .clickable {
                        navController.navigate(NavRoutes.FoodGraph.FoodDetail.route)
                    }) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "음식 화면 이동",
                        color = MainWhite,
                        style = Typography.displayLarge
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(modifier = Modifier
                    .background(WB500)
                    .clickable {
                        navController.navigate(NavRoutes.FoodGraph.FoodEdit.createRoute(0))
                    }) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "음식 정보 수정 화면 이동",
                        color = MainWhite,
                        style = Typography.displayLarge
                    )
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    FoodonTheme {
        MainScreen()
    }
}