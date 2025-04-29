package com.swallaby.foodon.presentation.foodDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.domain.food.model.NutrientNameType

@Composable
fun NutritionalSmallInfo(
    modifier: Modifier = Modifier,
    nutrientType: NutrientNameType,
    number: Int
) {
    Row {
        Box(
            modifier = modifier
                .height(16.dp)
                .aspectRatio(1f)
                .background(
                    color = nutrientType.color, shape = RoundedCornerShape(2.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Text(nutrientType.shortName, style = SpoqaTypography.SpoqaBold9.copy(color = Color.White))
        }

        Spacer(modifier.width(4.dp))

        Text(
            stringResource(R.string.format_g, number),
            style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionalSmallInfoPreview() {
    NutritionalSmallInfo(nutrientType = NutrientNameType.CARBOHYDRATE, number = 10)
}