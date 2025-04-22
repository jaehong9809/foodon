package com.swallaby.foodon.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.theme.FoodonTheme

@Composable
fun NutrientDetailScreen(
    onBackClick: (() -> Unit),
) {
    CommonBackTopBar(title = stringResource(R.string.top_bar_nutrient_detail)) {
        onBackClick()
    }


}

@Preview(showBackground = true)
@Composable
fun NutrientDetailPreview() {
    FoodonTheme {
        NutrientDetailScreen(
            onBackClick = { }
        )
    }
}