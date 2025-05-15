package com.swallaby.foodon.presentation.main.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.main.model.GoalManage
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage

data class MainUiState(
    val recordResult: ResultState<List<MealRecord>> = ResultState.Loading,
    val intakeResult: ResultState<NutrientIntake> = ResultState.Loading,
    val nutrientManageResult: ResultState<List<NutrientManage>> = ResultState.Loading,
    val goalManageResult: ResultState<GoalManage> = ResultState.Loading
): UiState