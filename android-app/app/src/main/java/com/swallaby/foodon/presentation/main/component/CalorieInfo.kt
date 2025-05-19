package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal

@Composable
fun CalorieInfo(
    goal: Int = 0,
    consumed: Int = 0
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Spacer(modifier = Modifier.height(42.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatKcal(consumed),
                style = SpoqaTypography.SpoqaBold24.copy(color = G900)
            )

            Text(
                text = stringResource(R.string.main_kcal_unit),
                style = SpoqaTypography.SpoqaMedium16.copy(color = G900)
            )
        }

        Text(
            text = stringResource(R.string.main_pager_kcal_unit, formatKcal(goal)),
            style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
        )
    }
}