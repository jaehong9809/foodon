package com.swallaby.foodon.presentation.foodedit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.food.model.FoodSimilar

@Composable
private fun DefaultChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .height(32.dp)
            .border(width = 1.dp, color = Border025, shape = RoundedCornerShape(100.dp))
            .padding(horizontal = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ), contentAlignment = Alignment.Center
    ) {
        content()
    }
}


@Composable
fun SearchChip(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    DefaultChip(modifier = modifier, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.icon_search), contentDescription = "search"
            )
            Spacer(modifier = modifier.width(4.dp))
            Text(
                stringResource(R.string.search_food),
                style = NotoTypography.NotoMedium14.copy(color = G800)
            )
        }
    }
}

@Composable
fun FoodChip(
    modifier: Modifier = Modifier,
    food: FoodSimilar,
    isSelected: Boolean = false,
    onClick: (foodId: Long) -> Unit = {},
) {
    DefaultChip(
        modifier = modifier.border(
            width = 1.dp,
            color = if (isSelected) WB500 else Border025,
            shape = RoundedCornerShape(100.dp)
        ), onClick = { onClick(food.foodId) }
    ) {
        Text(
            food.foodName,
            style = NotoTypography.NotoMedium14.copy(color = if (isSelected) WB500 else G800)
        )
    }
}

@Composable
fun UnitTypeChip(
    modifier: Modifier = Modifier,
    unit: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    DefaultChip(
        modifier = modifier.border(
            width = 1.dp,
            color = if (isSelected) WB500 else Border025,
            shape = RoundedCornerShape(100.dp)
        ), onClick = onClick
    ) {
        Text(
            unit,
            style = NotoTypography.NotoMedium14.copy(color = if (isSelected) WB500 else G800)
        )
    }
}