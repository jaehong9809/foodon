package com.swallaby.foodon.presentation.foodEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.presentation.foodEdit.component.FoodChip
import com.swallaby.foodon.presentation.foodEdit.component.FoodThumbnailList
import com.swallaby.foodon.presentation.foodEdit.component.SearchChip

@Composable
fun FoodEditScreen(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            FoodThumbnailList()
            FoodSearch()
        }
    }
}

@Composable
fun FoodSearch(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 16.dp, bottom = 24.dp)) {
        Row {
            Text(
                "피자", style = NotoTypography.NotoBold14.copy(color = G900)
            )
            Text("가 아닌가요?", style = NotoTypography.NotoBold14.copy(color = G500))
        }
        Spacer(modifier = modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SearchChip(modifier)
            FoodChip(modifier, foodName = "피자")
            FoodChip(modifier, foodName = "피자", isSelected = true)

        }
    }
}


@Preview
@Composable
fun FoodEditScreenPreview() {
    FoodEditScreen()
}