package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes

@Composable
fun GoalManageContent() {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.main_goal_manage_title),
            style = NotoTypography.NotoBold18.copy(color = G900)
        )

        Spacer(modifier = Modifier.height(4.dp))

        InfoItem(stringResource(R.string.main_goal_manage_type), "고단백형", NavRoutes.FoodGraph)
        InfoItem(stringResource(R.string.main_goal_manage_calorie), stringResource(R.string.format_kcal, formatKcal(1000)), NavRoutes.FoodGraph)
        InfoItem(stringResource(R.string.main_goal_manage_nutrient), "48:28:24", NavRoutes.FoodGraph)

        Spacer(modifier = Modifier.height((11.5).dp))

        Text(
            text = stringResource(R.string.main_profile_manage_title),
            style = NotoTypography.NotoBold18.copy(color = G900)
        )

        Spacer(modifier = Modifier.height(4.dp))

        InfoItem(stringResource(R.string.main_goal_manage_height), stringResource(R.string.format_cm, 174), NavRoutes.FoodGraph)
        InfoItem(stringResource(R.string.main_goal_manage_cur_weight), stringResource(R.string.format_kg, 60), NavRoutes.FoodGraph)
        InfoItem(stringResource(R.string.main_goal_manage_goal_weight), stringResource(R.string.format_kg, 60), NavRoutes.FoodGraph)

        Spacer(modifier = Modifier.height((11.5).dp))
    }
}

@Composable
fun InfoItem(
    title: String,
    content: String,
    navRoutes: NavRoutes
) {

    val navController = LocalNavController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = SpoqaTypography.SpoqaMedium16.copy(color = G800)
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        navController.navigate(navRoutes.route)
                    }
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = content,
                style = SpoqaTypography.SpoqaMedium16.copy(color = G800)
            )

            Box(
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_right_chevron),
                    contentDescription = null,
                )
            }
        }
    }
}