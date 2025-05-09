package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.domain.food.model.Nutrition

@Composable
fun NutritionalIngredientPercentage(
    modifier: Modifier = Modifier,
    nutritions: List<Nutrition> = emptyList(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(nutritions.size, action = { index ->
            PercentageBar(
                modifier
                    .height(24.dp)
                    .weight(nutritions[index].ratio)
                    .background(
                        color = nutritions[index].nutritionType.color, RoundedCornerShape(4.dp)
                    ),
                percentage = nutritions[index].ratio,
            )
        })
    }
}

@Preview
@Composable
fun NutritionalIngredientPercentagePreview() {
    NutritionalIngredientPercentage()
}