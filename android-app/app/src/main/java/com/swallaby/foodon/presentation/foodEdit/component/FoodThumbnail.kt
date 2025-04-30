package com.swallaby.foodon.presentation.foodEdit.component

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography


@Composable
fun FoodThumbnailList(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(top = 8.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodThumbnail(modifier, isSelected = true, foodName = "피자")
        FoodThumbnail(modifier, isSelected = false, foodName = "레드와인")
    }
}

@Composable
private fun FoodThumbnail(
    modifier: Modifier,
    foodName: String,
    isSelected: Boolean = false,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier
                .border(
                    2.dp,
                    color = if (isSelected) WB500 else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .size(72.dp), contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740",
                contentDescription = "음식 사진",
                contentScale = ContentScale.FillBounds,
                modifier = modifier
                    .size(64.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            )
        }
        Text(
            foodName,
            style = NotoTypography.NotoBold14.copy(color = if (isSelected) WB500 else G900)
        )
    }
}

@Preview
@Composable
private fun FoodThumbnailListPreview() {
    FoodThumbnailList()
}