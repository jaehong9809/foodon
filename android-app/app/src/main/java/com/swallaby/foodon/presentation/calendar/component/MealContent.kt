package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography

@Composable
fun MealContent() {
    TabContentLayout(
        title = stringResource(R.string.tab_content_title_meal),
        bgColor = Bkg04
    ) {
        // TODO: 하루 목표 칼로리
        Text(
            text = stringResource(R.string.format_kcal, "1,600"),
            color = G900,
            style = SpoqaTypography.SpoqaBold18,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MealPreview() {
    MealContent()
}