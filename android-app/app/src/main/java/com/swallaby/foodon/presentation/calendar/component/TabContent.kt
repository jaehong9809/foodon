package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun MealContent() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "식사 Content", style = NotoTypography.NotoBold20)
    }
}

@Composable
fun WeightContent() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "체중 Content", style = NotoTypography.NotoBold20)
    }
}

@Composable
fun RecommendationContent() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "추천 Content", style = NotoTypography.NotoBold20)
    }
}
