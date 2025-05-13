package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.EmptyContentText
import com.swallaby.foodon.core.ui.component.LoadingProgress
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.DateUtil.formatDate
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import com.swallaby.foodon.presentation.main.viewmodel.MainUiState

@Composable
fun MealRecordContent(
    mainUiState: MainUiState,
    calendarUiState: CalendarUiState,
    onMealClick: (Long) -> Unit
) {

    val today = calendarUiState.today
    val selectedDate = calendarUiState.selectedDate
    val recordState = mainUiState.recordResult

    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp)
    ) {
        Text(
            text = if (selectedDate != today) stringResource(R.string.main_meal_record_date_title, formatDate(selectedDate))
                else stringResource(R.string.main_meal_record_today_title),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (recordState) {
            is ResultState.Loading -> {
                LoadingProgress()
            }
            is ResultState.Success -> {
                if (recordState.data.isEmpty()) {
                    EmptyContentText(emptyText = stringResource(R.string.main_meal_record_empty))
                } else {
                    Column(
                        modifier = Modifier.wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        recordState.data.forEach { item ->
                            MealRecordItem(meal = item, onClick = onMealClick)
                        }
                    }
                }
            }
            else -> {
                EmptyContentText(emptyText = stringResource(R.string.main_meal_record_empty))
            }
        }
    }
}
