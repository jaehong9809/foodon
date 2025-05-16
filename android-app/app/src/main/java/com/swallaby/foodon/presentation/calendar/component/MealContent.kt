package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500F1A
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.domain.calendar.model.CalendarMeal

@Composable
fun MealContent(
    calendarMeal: CalendarMeal = CalendarMeal()
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TabContentLayout(
            title = stringResource(R.string.tab_content_title_meal),
            bgColor = Bkg04
        ) {
            Text(
                text = stringResource(R.string.format_kcal, formatKcal(calendarMeal.goalKcal)),
                color = G900,
                style = SpoqaTypography.SpoqaBold18,
            )
        }

        TabContentLayout(
            title = stringResource(R.string.tab_content_title_meal_intake),
            bgColor = WB500F1A,
            icon = R.drawable.icon_graph
        ) {
            Text(
                text = stringResource(R.string.format_kcal, formatKcal(calendarMeal.intakeKcal)),
                color = G900,
                style = SpoqaTypography.SpoqaBold18,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MealPreview() {
    MealContent()
}