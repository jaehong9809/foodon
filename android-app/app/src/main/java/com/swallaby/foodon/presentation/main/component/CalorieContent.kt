package com.swallaby.foodon.presentation.main.component

import androidx.compose.runtime.Composable
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.CalorieProgressBar
import com.swallaby.foodon.domain.food.model.NutrientNameType
import com.swallaby.foodon.domain.main.model.NutrientIntake

@Composable
fun CalorieContent(
    intakeResult: ResultState<NutrientIntake>
) {

    val carbsRatio = 0.4f
    val proteinRatio = 0.3f
    val fatRatio = 0.1f

    val nutrients = listOf(
        carbsRatio to NutrientNameType.CARBOHYDRATE,
        proteinRatio to NutrientNameType.PROTEIN,
        fatRatio to NutrientNameType.FAT
    )

    when (intakeResult) {
        is ResultState.Success ->  {
            CalorieProgressBar(
                nutrients = nutrients,
                consumed = intakeResult.data.intakeKcal,
                goal = intakeResult.data.goalKcal
            )
        }
        else -> {
            CalorieProgressBar()
        }
    }
}