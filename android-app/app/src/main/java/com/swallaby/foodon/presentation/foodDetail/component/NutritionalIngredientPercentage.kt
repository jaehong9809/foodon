package com.swallaby.foodon.presentation.foodDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NutritionalIngredientPercentage(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PercentageBar(
            modifier
                .height(24.dp)
                .weight(.5f)
                .background(color = Color.Blue, RoundedCornerShape(4.dp)), percentage = .5f
        )
        PercentageBar(
            modifier
                .height(24.dp)
                .weight(.25f)
                .background(color = Color.Blue, RoundedCornerShape(4.dp)), percentage = .25f
        )
        PercentageBar(
            modifier
                .height(24.dp)
                .weight(.25f)
                .background(color = Color.Blue, RoundedCornerShape(4.dp)), percentage = .25f
        )

    }
}

@Preview
@Composable
fun NutritionalIngredientPercentagePreview() {
    NutritionalIngredientPercentage()
}