package com.swallaby.foodon.presentation.main.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

data class MainUiState(
    val recordState: ResultState<List<MealRecord>> = ResultState.Loading,
    val intakeState: ResultState<List<NutrientIntake>> = ResultState.Loading,
    val manageState: ResultState<List<NutrientManage>> = ResultState.Loading,
    val currentYearMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now(),
): UiState