package com.swallaby.foodon.presentation.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.component.OnBoardingTopBar
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel


@Composable
fun GenderScreen(
    onNext: () -> Unit,
    viewModel: SignUpViewModel
) {

    OnBoardingTopBar(
        modifier = Modifier,
        curIdx = 1,
        total = 5,
        onBackClick = { }
    )

    CommonWideButton(
        modifier = Modifier,
        text = "다음",
        isEnabled = true,
        onClick = onNext
    )
}