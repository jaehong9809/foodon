package com.swallaby.foodon.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.swallaby.foodon.core.ui.component.OnBoardingTopBar
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

    Box(modifier = Modifier.fillMaxSize()) {
        OnBoardingTopBar(
            curIdx = 4,
            total = 5,
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 68.dp, start = 24.dp, end = 24.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text =  stringResource(R.string.body_info_input),
                style = Typography.displayLarge,
                color = G900
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

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