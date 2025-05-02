package com.swallaby.foodon.presentation.foodEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.component.UpdateFoodButton
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.bottomBorder
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil
import com.swallaby.foodon.presentation.foodEdit.component.FoodAmountComponent
import com.swallaby.foodon.presentation.foodEdit.component.FoodChip
import com.swallaby.foodon.presentation.foodEdit.component.FoodThumbnailList
import com.swallaby.foodon.presentation.foodEdit.component.SearchChip

@Composable
fun FoodEditScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    foodId: Long = 0,
) {
    val scrollState = rememberScrollState()
    Column {
        CommonBackTopBar(
            title = stringResource(R.string.top_bar_food_info_update), onBackClick = onBackClick
        )
        Column(
            modifier = modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
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
            NutritionComponent(modifier)
        }
        UpdateFoodButton(
            modifier = modifier.padding(
                horizontal = 24.dp
            )
        )
    }


}

@Composable
fun NutritionComponent(modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.nutritional_ingredients),
            style = NotoTypography.NotoBold18.copy(color = G900)
        )
        ParentNutritionInfo(
            nutritionName = "열량",
            amount = stringResource(R.string.format_kcal, StringUtil.formatKcal(1000)),
            amountColor = WB500,
        )
        ParentNutritionInfo(
            nutritionName = "탄수화물", amount = StringUtil.formatNutrition(100), hasChild = true
        )
        ChildNutritionInfo(
            nutritionName = "당류",
            amount = StringUtil.formatNutrition(10),
        )
        ParentNutritionInfo(
            nutritionName = "단백질", amount = StringUtil.formatNutrition(50), hasChild = true
        )
        ParentNutritionInfo(
            nutritionName = "지방",
            amount = StringUtil.formatNutrition(50),
        )
        ChildNutritionInfo(
            nutritionName = "포화지방",
            amount = StringUtil.formatNutrition(10),
        )
        ChildNutritionInfo(
            nutritionName = "트랜스지방",
            amount = StringUtil.formatNutrition(10),
        )
        ParentNutritionInfo(
            nutritionName = "콜레스테롤",
            amount = StringUtil.formatNutrition(10),
        )
        ParentNutritionInfo(
            nutritionName = "나트륨",
            amount = StringUtil.formatNutrition(10),
        )

        Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(
                onClick = {},
                modifier = modifier
                    .height(48.dp)
                    .background(color = BG100, shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    stringResource(R.string.top_bar_nutrient_input),
                    style = NotoTypography.NotoMedium16.copy(color = G800)
                )
            }
        }
        Spacer(modifier = modifier.height(24.dp))

    }

}

@Composable
private fun BaseNutritionInfo(
    modifier: Modifier = Modifier,
    nutrition: @Composable () -> Unit,
    amount: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        nutrition()
        amount()
    }
}


@Composable
private fun ParentNutritionInfo(
    modifier: Modifier = Modifier,
    nutritionName: String,
    amount: String,
    amountColor: androidx.compose.ui.graphics.Color = G900,
    hasChild: Boolean = false,
) {
    BaseNutritionInfo(modifier = if (hasChild) modifier.bottomBorder() else modifier, nutrition = {
        Text(nutritionName, style = NotoTypography.NotoMedium16.copy(color = G800))
    }, amount = {
        Text(amount, style = SpoqaTypography.SpoqaBold16.copy(color = amountColor))
    })
}

@Composable
private fun ChildNutritionInfo(
    modifier: Modifier = Modifier,
    nutritionName: String,
    amount: String,
) {
    BaseNutritionInfo(modifier = modifier, nutrition = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_inner), contentDescription = "inner",
            )
            Spacer(modifier = modifier.width(2.dp))
            Text(nutritionName, style = NotoTypography.NotoMedium16.copy(color = G700))
        }
    }, amount = {
        Text(amount, style = SpoqaTypography.SpoqaBold16.copy(color = G500))
    })
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
            FoodChip(modifier, foodName = "피자", isSelected = true)
            FoodChip(modifier, foodName = "불고기 피자")
            FoodChip(modifier, foodName = "페페로니 피자")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodEditScreenPreview() {
    FoodonTheme {
        FoodEditScreen(onBackClick = {})
    }
}