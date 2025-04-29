package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.FloatingButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.presentation.main.component.MainCalendarHeader
import com.swallaby.foodon.presentation.main.component.MealRecordContent
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes
import org.threeten.bp.LocalDate

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

    val navController = LocalNavController.current
    val uiState by viewModel.uiState.collectAsState()

    val today = uiState.today
    val selectedDate = uiState.selectedDate
    val currentYearMonth = uiState.currentYearMonth

    LaunchedEffect(Unit) {
        viewModel.fetchRecordData(LocalDate.now().toString())
    }

    Scaffold(
        floatingActionButton = {
            FloatingButton(
                modifier = Modifier,
                icon = R.drawable.icon_ai_camera,
                text = stringResource(R.string.btn_record)
            ) {
                // TODO: 식사 기록 화면으로 이동 (카메라)
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

            HorizontalDivider(thickness = 8.dp, color = Bkg04)

            MealRecordContent(recordState = uiState.recordResult) { mealId ->
                navController.navigate(NavRoutes.FoodGraph.FoodDetail.createRoute(mealId))
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