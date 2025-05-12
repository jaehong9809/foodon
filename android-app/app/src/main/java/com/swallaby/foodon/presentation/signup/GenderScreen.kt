package com.swallaby.foodon.presentation.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.component.OnBoardingTopBar
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.domain.user.model.GenderOption


@Composable
fun GenderScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val genderOptions = listOf(
        GenderOption(
            displayTextResId = R.string.gender_male_kr,
            iconResId = R.drawable.icon_signup_man,
            textValue = "MALE"
        ),
        GenderOption(
            displayTextResId = R.string.gender_female_kr,
            iconResId = R.drawable.icon_signup_woman,
            textValue = "FEMALE"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        OnBoardingTopBar(
            curIdx = 1,
            total = 5,
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "성별을 선택해 주세요.",
            style = Typography.displayLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                genderOptions.forEach { option ->
                    GenderOptionCard(
                        option = option,
                        selected = uiState.selectedGender == option,
                        onClick = { viewModel.selectGender(option) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CommonWideButton(
            modifier = Modifier,
            text = stringResource(R.string.btn_next),
            isEnabled = uiState.selectedGender != null,
            onClick = onNext
        )
    }
}


@Composable
fun GenderOptionCard(
    option: GenderOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    val border = if (selected) BorderStroke(1.dp, WB500) else null
    val backgroundColor = Color.White

    Surface(
        modifier = Modifier
            .width(159.5.dp)
            .height(192.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = border,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GenderIconWithBackground(imageResId = option.iconResId)
            Text(
                text = stringResource(option.displayTextResId),
                style = Typography.displaySmall
            )
        }
    }
}



@Composable
fun GenderIconWithBackground(
    imageResId: Int,
    backgroundColor: Color = Bkg04
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
    }
}