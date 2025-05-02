package com.swallaby.foodon.presentation.mealdetail.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.util.DateUtil
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealType
import org.threeten.bp.LocalDateTime

data class MealEditUiState(
    val mealEditState: ResultState<MealInfo> = ResultState.Success(
        MealInfo()
    ),
    val mealType: MealType = MealType.BREAKFAST,
    val mealTime: String = DateUtil.formatTimeToHHmm(LocalDateTime.now()),
) : UiState