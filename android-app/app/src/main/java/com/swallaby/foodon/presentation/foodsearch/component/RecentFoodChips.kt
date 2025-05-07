package com.swallaby.foodon.presentation.foodsearch.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.Typography



@Composable
fun RecentFoodChips(
    recentFoods: List<String>,
    onChipClick: (String) -> Unit,
    onChipRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 16.dp,
                bottom = 16.dp
            )
    ) {
        Text(
            text = stringResource(R.string.recent_register_food),
            style = Typography.titleMedium,
            color = G800
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recentFoods.forEach { food ->
                FoodChip(
                    food = food,
                    onClick = { onChipClick(food) },
                    onRemoveClick = { onChipRemove(food) }
                )
            }
        }
    }
}



@Composable
fun FoodChip(
    food: String,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MainWhite,
        border = BorderStroke(1.dp, Border02),
        modifier = modifier
            .height(36.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = food,
                style = Typography.titleMedium,
                color = G800,
            )
            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = R.drawable.icon_search_chip_close),
                contentDescription = "delete FoodChip",
                tint = G500,
                modifier = Modifier
                    .size(10.dp)
                    .clickable {
                        onRemoveClick()
                    }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecentFoodChipsPreview() {
    val sampleFoods = listOf("피자", "샐러드", "밥", "김치", "김치볶음밥")
    RecentFoodChips(
        recentFoods = sampleFoods,
        onChipClick = {},
        onChipRemove = {}
    )
}
