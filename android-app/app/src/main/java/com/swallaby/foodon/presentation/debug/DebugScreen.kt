package com.swallaby.foodon.presentation.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DebugControlScreen(
    onInsertDummyData: () -> Unit,
    onClearDb: () -> Unit,
    onGoToFoodSearch: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onInsertDummyData) {
            Text("더미 데이터 삽입 (500개)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onClearDb) {
            Text("DB 초기화")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGoToFoodSearch) {
            Text("FoodSearch UI")
        }
    }
}
