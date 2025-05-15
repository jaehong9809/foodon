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
import com.swallaby.foodon.presentation.main.model.GoalInfo
import com.swallaby.foodon.domain.main.model.GoalManage
import com.swallaby.foodon.presentation.main.model.GoalSection
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes

@Composable
fun GoalManageContent(
    goalManageResult: ResultState<GoalManage>
) {

    val sections = when (goalManageResult) {
        is ResultState.Success -> {
            val data = goalManageResult.data

            listOf(
                GoalSection(
                    title = stringResource(R.string.main_goal_manage_title),
                    items = listOf(
                        GoalInfo(stringResource(R.string.main_goal_manage_type), data.managementType, NavRoutes.SignUpManagement),
                        GoalInfo(
                            stringResource(R.string.main_goal_manage_calorie),
                            stringResource(R.string.format_kcal, formatKcal(data.targetCalories)),
                            NavRoutes.SignUpManagement
                        ),
                        GoalInfo(
                            stringResource(R.string.main_goal_manage_nutrient),
                            "${data.carbRatio}:${data.proteinRatio}:${data.fatRatio}",
                            NavRoutes.SignUpManagement
                        )
                    )
                ),
                GoalSection(
                    title = stringResource(R.string.main_profile_manage_title),
                    items = listOf(
                        GoalInfo(stringResource(R.string.main_goal_manage_height), stringResource(R.string.format_cm, data.height), NavRoutes.SignUpBodyInfo),
                        GoalInfo(stringResource(R.string.main_goal_manage_cur_weight), stringResource(R.string.format_kg, data.currentWeight), NavRoutes.SignUpBodyInfo),
                        GoalInfo(stringResource(R.string.main_goal_manage_goal_weight), stringResource(R.string.format_kg, data.goalWeight), NavRoutes.SignUpGoalWeight)
                    )
                )
            )
        }

        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxSize()
    ) {
        sections.forEach { section ->
            Section(title = section.title, items = section.items)
            Spacer(modifier = Modifier.height(11.5.dp))
        }
    }
}

@Composable
fun Section(title: String, items: List<GoalInfo>) {
    Text(
        text = title,
        style = NotoTypography.NotoBold18.copy(color = G900)
    )

    Spacer(modifier = Modifier.height(4.dp))

    items.forEach { item ->
        InfoItem(title = item.title, content = item.content, navRoutes = item.route)
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