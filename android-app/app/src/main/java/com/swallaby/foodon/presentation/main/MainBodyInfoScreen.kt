package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.swallaby.foodon.core.ui.component.NumberWheelPicker
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.presentation.main.component.GoalInputLayout
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel

@Composable
fun MainBodyInfoScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val height = 170
    val weight = 60

    GoalInputLayout(
        title = stringResource(R.string.body_info_input),
        isButtonEnabled = true,
        onBackClick = onBack,
        onButtonClick = {
            // TODO: 키, 현재 체중 업데이트 api 호출
            onSubmit()
        }
    ) {
        Column {
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
                    onValueChange = {
                        // TODO: 키 수정
                    }
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
                    onValueChange = {
                        // TODO: 현재 체중 수정
                    }
                )
            }
        }
    }
}