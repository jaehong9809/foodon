package com.swallaby.foodon.presentation.foodEdit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.presentation.mealDetail.component.DropButton

@Composable
fun FoodAmountComponent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        Column {
            Text(
                text = stringResource(R.string.food_amount),
                style = NotoTypography.NotoBold18.copy(color = G900)
            )
            Spacer(modifier = modifier.height(12.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DropButton(
                    modifier = modifier
                        .height(48.dp)
                        .weight(1f),

                    onClick = {}, text = "조각",
                    contentModifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    contentAlignment = Arrangement.SpaceBetween,
                    suffixIcon = {
                        Image(
                            painter = painterResource(R.drawable.icon_down_chevron),
                            contentDescription = "down_chevron",
                            colorFilter = ColorFilter.tint(color = G750)
                        )
                    },
                )
                DropButton(
                    modifier = modifier
                        .height(48.dp)
                        .weight(1f),
                    onClick = {}, text = "1 ½",
                    contentModifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    contentAlignment = Arrangement.SpaceBetween,
                    suffixIcon = {
                        Image(
                            painter = painterResource(R.drawable.icon_down_chevron),
                            contentDescription = "down_chevron",
                            colorFilter = ColorFilter.tint(color = G750)
                        )
                    },
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodAmountComponentPreview(modifier: Modifier = Modifier) {
    FoodAmountComponent()
}