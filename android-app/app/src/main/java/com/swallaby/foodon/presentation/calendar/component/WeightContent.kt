package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500F1A
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.domain.calendar.model.UserWeight

@Composable
fun WeightContent(
    weightResult: ResultState<UserWeight>,
    onUpdateWeight: () -> Unit
) {
    val userWeight = when (weightResult) {
        is ResultState.Success -> weightResult.data
        else -> UserWeight()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GoalWeight(modifier = Modifier.weight(1f), userWeight.goalWeight)
        CurrentWeight(
            modifier = Modifier.weight(1f),
            weight = userWeight.currentWeight,
            onUpdateWeight = onUpdateWeight
        )
    }
}

@Composable
fun GoalWeight(modifier: Modifier = Modifier, weight: Int) {
    TabContentLayout(
        modifier = modifier,
        title = stringResource(R.string.tab_content_title_goal_weight),
        isWeight = true,
        bgColor = Bkg04
    ) {
        Text(
            text = stringResource(R.string.format_kg, weight),
            color = G900,
            style = SpoqaTypography.SpoqaBold18,
        )
    }
}

@Composable
fun CurrentWeight(
    modifier: Modifier = Modifier,
    weight: Int,
    onUpdateWeight: () -> Unit
) {
    TabContentLayout(
        modifier = modifier,
        title = stringResource(R.string.tab_content_title_cur_weight),
        bgColor = WB500F1A,
        contentPadding = 5.dp,
        isWeight = true,
        icon = R.drawable.icon_cur_weight
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.format_kg, weight),
                color = G900,
                style = SpoqaTypography.SpoqaBold18,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onUpdateWeight
                    ),
                painter = painterResource(id = R.drawable.icon_bg_pencil),
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeightPreview() {
    WeightContent(weightResult = ResultState.Loading, onUpdateWeight = {})
}