package com.swallaby.foodon.presentation.signup

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.domain.user.model.ActivityTypeOption
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel


@Composable
fun ActivityTypeScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: SignUpViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        OnBoardingTopBar(
            curIdx = 3,
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
                text =  stringResource(R.string.activity_level_choice),
                style = Typography.displayLarge,
                color = G900
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                uiState.activityTypeOptions.forEach { option ->
                    ActivityLevelOptionCard(
                        option = option,
                        selected = uiState.selectedActivityTypeId == option.id,
                        onClick = { viewModel.selectActivityType(option.id) }
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

@Composable
fun ActivityLevelOptionCard(
    option: ActivityTypeOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) WB500 else Border02

    Surface(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable { onClick() }
            .border(2.dp, borderColor, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = MainWhite
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = option.title, style = Typography.titleLarge, color = G900)
        }
    }
}