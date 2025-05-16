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
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.domain.main.model.GoalManage
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes

@Composable
fun GoalManageContent(
    goalManageResult: ResultState<GoalManage>
) {

    val data = (goalManageResult as? ResultState.Success)?.data ?: GoalManage()

    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxSize()
    ) {
        ManageTitleItem(stringResource(R.string.main_goal_manage_title))

        InfoItem(
            title = stringResource(R.string.main_goal_manage_type),
            content = data.managementType.takeIf { it.isNotEmpty() } ?: "없음",
            navRoutes = NavRoutes.SignUpManagement
        )
        InfoItem(
            title = stringResource(R.string.main_goal_manage_calorie),
            content = stringResource(R.string.format_kcal, formatKcal(data.targetCalories)),
            navRoutes = NavRoutes.SignUpManagement
        )
        InfoItem(
            title = stringResource(R.string.main_goal_manage_nutrient),
            content = "${data.carbRatio}:${data.proteinRatio}:${data.fatRatio}",
            navRoutes = NavRoutes.SignUpManagement
        )

        Spacer(modifier = Modifier.height(11.5.dp))

        ManageTitleItem(stringResource(R.string.main_profile_manage_title))

        InfoItem(
            title = stringResource(R.string.main_goal_manage_height),
            content = stringResource(R.string.format_cm, data.height),
            navRoutes = NavRoutes.SignUpBodyInfo
        )
        InfoItem(
            title = stringResource(R.string.main_goal_manage_cur_weight),
            content = stringResource(R.string.format_kg, data.currentWeight),
            navRoutes = NavRoutes.SignUpBodyInfo
        )
        InfoItem(
            title = stringResource(R.string.main_goal_manage_goal_weight),
            content = stringResource(R.string.format_kg, data.goalWeight),
            navRoutes = NavRoutes.SignUpGoalWeight
        )
    }
}

@Composable
fun ManageTitleItem(title: String) {
    Text(
        text = title,
        style = NotoTypography.NotoBold18.copy(color = G900)
    )

    Spacer(modifier = Modifier.height(4.dp))
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