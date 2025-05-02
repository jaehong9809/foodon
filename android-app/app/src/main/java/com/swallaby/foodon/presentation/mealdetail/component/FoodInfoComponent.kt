package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.food.model.MealItem

@Composable
fun FoodInfoComponent(
    modifier: Modifier = Modifier,
    foods: List<MealItem> = emptyList(),
    onClick: (foodId: Long) -> Unit,
    onDelete: (foodId: Long) -> Unit,
) {

    Column(
        modifier = modifier
            .background(color = Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.food_info_check),
            style = NotoTypography.NotoBold18.copy(color = G900)
        )
        Spacer(modifier = modifier.height(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(foods.size) { index ->
                FoodCard(
                    food = foods[index], onClick = onClick, onDelete = onDelete,
                )
            }
        }
    }

}


@Preview
@Composable
fun FoodInfoComponentPreview() {
    FoodInfoComponent(onClick = {}, onDelete = {})
}