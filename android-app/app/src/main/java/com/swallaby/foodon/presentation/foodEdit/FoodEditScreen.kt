package com.swallaby.foodon.presentation.foodEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.presentation.foodEdit.component.FoodAmountComponent
import com.swallaby.foodon.presentation.foodEdit.component.FoodChip
import com.swallaby.foodon.presentation.foodEdit.component.FoodThumbnailList
import com.swallaby.foodon.presentation.foodEdit.component.SearchChip

@Composable
fun FoodEditScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit, foodId: Long = 0) {
    Column {
        FoodThumbnailList()
        HorizontalDivider(
            modifier = modifier.padding(horizontal = 24.dp), thickness = 1.dp, color = Border02
        )
        FoodSearch()
        Spacer(
            modifier = modifier
                .height(8.dp)
                .background(color = Bkg04)
        )
        FoodAmountComponent()

    }
}

@Composable
fun FoodSearch(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.padding(top = 16.dp, bottom = 24.dp)) {
        Row(
            modifier = modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                "피자", style = NotoTypography.NotoBold14.copy(color = G900)
            )
            Text("가 아닌가요?", style = NotoTypography.NotoBold14.copy(color = G500))
        }
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SearchChip(modifier)
            FoodChip(modifier, foodName = "피자")
            FoodChip(modifier, foodName = "피자", isSelected = true)
            FoodChip(modifier, foodName = "피자", isSelected = true)
            FoodChip(modifier, foodName = "피자", isSelected = true)
            FoodChip(modifier, foodName = "피자", isSelected = true)
            FoodChip(modifier, foodName = "피자", isSelected = true)
            FoodChip(modifier, foodName = "피자", isSelected = true)

        }
    }
}


@Preview
@Composable
fun FoodEditScreenPreview() {
    FoodonTheme {
        FoodEditScreen(onBackClick = {})
    }
}