package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil
import com.swallaby.foodon.domain.food.model.NutritionType

@Composable
fun NutritionalMediumInfo(
    modifier: Modifier = Modifier,
    nutritionType: NutritionType,
    amount: Double = 0.0,
) {
    val childModifier = Modifier

    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = childModifier
                    .size(10.dp)
                    .background(color = nutritionType.color, shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = childModifier.width(4.dp))
            Text(
                nutritionType.displayName, style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
            )
        }
        Spacer(modifier = childModifier.height(4.dp))
        Text(
            StringUtil.formatNutrition(amount),
            style = SpoqaTypography.SpoqaBold16.copy(color = G900)
        )
    }

}

@Preview
@Composable
fun NutritionalIngredientInfoPreview() {
    NutritionalMediumInfo(
        nutritionType = NutritionType.CARBOHYDRATE, amount = 100.0
    )
}