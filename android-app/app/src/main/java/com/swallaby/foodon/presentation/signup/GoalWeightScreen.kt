package com.swallaby.foodon.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.component.NumberWheelPicker
import com.swallaby.foodon.core.ui.component.OnBoardingTopBar
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel


@Composable
fun GoalWeightScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val current = uiState.weight
    val goal = uiState.goalWeight ?: current

    val labelRes = when {
        goal < current -> R.string.loss_weight
        goal > current -> R.string.gain_weight
        else           -> R.string.maintain_weight
    }

    Box(modifier = Modifier.fillMaxSize()) {
        OnBoardingTopBar(
            curIdx = 5,
            total = 5,
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 68.dp, bottom = 96.dp),
        ) {
            Text(
                text =  stringResource(R.string.goal_weight_input),
                style = Typography.displayLarge,
                color = G900,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            // 목표 체중
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(labelRes),
                    style = Typography.displaySmall,
                    color = WB500
                )
                Spacer(modifier = Modifier.height(8.dp))

                NumberWheelPicker(
                    valueRange = 30..200,
                    initialValue = goal,
                    unit = "kg",
                    onValueChange = { viewModel.onGoalWeightChange(it) }
                )
            }
        }

        CommonWideButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.btn_complete),
            isEnabled = uiState.goalWeight != null,
            onClick = onSubmit
        )
    }
}