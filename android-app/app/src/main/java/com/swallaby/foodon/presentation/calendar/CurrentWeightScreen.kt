package com.swallaby.foodon.presentation.calendar

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.component.NumberWheelPicker
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.domain.calendar.model.UserWeight
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel

@Composable
fun CurrentWeightScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    viewModel: CalendarViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val userWeight = (uiState.weightResult as? ResultState.Success)?.data ?: UserWeight()

    val current = userWeight.currentWeight
    val inputWeight = uiState.inputWeight

    val context = LocalContext.current

    LaunchedEffect(userWeight.currentWeight) {
        viewModel.onCurrentWeightChange(userWeight.currentWeight)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CommonBackTopBar(
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 68.dp, bottom = 96.dp),
        ) {
            Text(
                text =  stringResource(R.string.current_weight_input),
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
                NumberWheelPicker(
                    valueRange = 30..200,
                    initialValue = current,
                    unit = "kg",
                    onValueChange = {
                        viewModel.onCurrentWeightChange(it)
                    }
                )
            }
        }

        CommonWideButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.btn_complete),
            isEnabled = current != 0,
            onClick = {
                viewModel.updateUserWeight(
                    weight = inputWeight,
                    onSuccess = { onSubmit() },
                    onError = { msgRes ->
                        Toast.makeText(context, context.getString(msgRes), Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }

}
