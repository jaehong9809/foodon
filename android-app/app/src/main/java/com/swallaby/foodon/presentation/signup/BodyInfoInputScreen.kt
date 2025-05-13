package com.swallaby.foodon.presentation.signup

import androidx.compose.foundation.background
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
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel


@Composable
fun BodyInfoInputScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val height = uiState.height
    val weight = uiState.weight

    Box(modifier = Modifier.fillMaxSize()) {
        OnBoardingTopBar(
            curIdx = 4,
            total = 5,
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 68.dp, bottom = 96.dp),
        ) {
            Text(
                text =  stringResource(R.string.body_info_input),
                style = Typography.displayLarge,
                color = G900,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                // 키 입력
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.height),
                        style = Typography.displaySmall,
                        color = G900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    NumberWheelPicker(
                        valueRange = 100..300,
                        initialValue = height,
                        unit = "cm",
                        onValueChange = { viewModel.onHeightChange(it) }
                    )
                }

                // 구분선
                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border02)
                )

                // 체중 입력
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.weight),
                        style = Typography.displaySmall,
                        color = G900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    NumberWheelPicker(
                        valueRange = 30..200,
                        initialValue = weight,
                        unit = "kg",
                        onValueChange = { viewModel.onWeightChange(it) }
                    )
                }
            }
        }

        CommonWideButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.btn_next),
            isEnabled = uiState.selectedActivityTypeId != null,
            onClick = onNext
        )
    }
}