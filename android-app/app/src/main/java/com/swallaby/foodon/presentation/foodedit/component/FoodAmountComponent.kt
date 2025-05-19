package com.swallaby.foodon.presentation.foodedit.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.OutLineTextField
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.IntegerVisualTransformation
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.presentation.mealdetail.component.DropButton
import kotlin.math.min

@Composable
fun FoodAmountComponent(
    modifier: Modifier = Modifier,
    food: MealItem,
    enabledUpdate: Boolean = true,
    onClickUnitType: () -> Unit = {},
    onUpdateQuantity: (foodId: Long, quantity: Int) -> Unit = { _, _ -> },
) {
    var quantity by remember { mutableStateOf(food.quantity.toString()) }

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
                    enabled = enabledUpdate,
                    onClick = onClickUnitType, text = food.unit.value,
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
                OutLineTextField(
                    modifier = modifier
                        .height(48.dp)
                        .weight(1f),
                    value = quantity,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                    enabled = enabledUpdate,
                    visualTransformation = IntegerVisualTransformation(maxValue = 999999),
                    onValueChange = { newValue ->
                        // 숫자만 필터링
                        val filterValue = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                        quantity = min(filterValue, 999999).toString()

                        onUpdateQuantity(food.foodId, filterValue)
                    },
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodAmountComponentPreview(modifier: Modifier = Modifier) {
    FoodAmountComponent(food = MealItem())
}