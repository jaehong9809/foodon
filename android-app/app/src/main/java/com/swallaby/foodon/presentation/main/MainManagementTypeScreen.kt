package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.presentation.main.component.GoalInputLayout
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel

@Composable
fun MainManagementTypeScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    GoalInputLayout(
        title = stringResource(R.string.management_type_choice),
        subTitle = stringResource(R.string.management_type_description),
        isButtonEnabled = true,
        onBackClick = onBack,
        onButtonClick = {
            // TODO: 관리 유형 업데이트 api 호출
            onSubmit()
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
//            uiState.managementOptions.forEach { option ->
//                ManagementTypeOptionCard(
//                    option = option,
//                    selected = uiState.selectedManagementTypeId == option.id,
//                    onClick = { viewModel.selectManagementType(option.id) }
//                )
//            }
        }
    }

}