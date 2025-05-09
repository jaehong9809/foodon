package com.swallaby.foodon.presentation.nutritionedit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.NutritionTextField
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography


@Composable
fun NutrientField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    nutrient: String,
    unit: String,
    isChildField: Boolean = false,
    isLastField: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isChildField) Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(R.drawable.icon_inner), contentDescription = "inner",
            )
            Spacer(modifier = modifier.width(4.dp))
            Text(
                nutrient, style = NotoTypography.NotoMedium16.copy(color = G700)
            )
        }
        else Text(
            nutrient, style = NotoTypography.NotoMedium16.copy(color = G900)
        )
        NutritionTextField(
            value = value,
            onValueChange = onValueChange,
            isLastField = isLastField,
            unit = unit,
        )
    }
}
