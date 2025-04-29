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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.main.model.MealRecord

@Composable
fun MealRecordContent(
    recordState: ResultState<List<MealRecord>>
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.main_meal_record_title),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (recordState) {
            is ResultState.Success -> {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    recordState.data.forEach { item ->
                        MealRecordItem(meal = item) {
                            // TODO: 식사 기록 수정 화면으로 이동
                        }
                    }
                }
            }
            else -> {
                Text(
                    text = stringResource(R.string.main_meal_record_title),
                    color = G900,
                    style = NotoTypography.NotoBold20,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

