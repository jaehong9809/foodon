package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.CalorieProgressBar
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.DateUtil.formatDate
import com.swallaby.foodon.domain.food.model.Nutrition
import com.swallaby.foodon.domain.food.model.NutritionType
import com.swallaby.foodon.presentation.main.viewmodel.MainUiState

@Composable
fun NutrientIntakeContent(
    uiState: MainUiState,
) {

    val today = uiState.today
    val selectedDate = uiState.selectedDate
    val intakeResult = uiState.intakeResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = if (selectedDate != today) stringResource(R.string.main_nutrient_intake_date_title, formatDate(selectedDate))
                    else stringResource(R.string.main_nutrient_intake_today_title),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (intakeResult) {
                is ResultState.Success ->  {
                    val calorie = intakeResult.data

                    val carbsRatio = calorie.intakeCarbs.toFloat() * 4 / calorie.goalKcal
                    val proteinRatio = calorie.intakeProtein.toFloat() * 4 / calorie.goalKcal
                    val fatRatio = calorie.intakeFat.toFloat() * 9 / calorie.goalKcal

                    val nutrients = listOf(
                        Nutrition(NutritionType.CARBOHYDRATE, calorie.intakeCarbs, carbsRatio),
                        Nutrition(NutritionType.PROTEIN, calorie.intakeProtein, proteinRatio),
                        Nutrition(NutritionType.FAT, calorie.intakeFat, fatRatio),
                    ).sortedByDescending { it.amount }

                    CalorieProgressBar(
                        nutrients = nutrients,
                        consumed = calorie.intakeKcal,
                        goal = calorie.goalKcal
                    )

                    IntakeDetail(calorie)
                }
                else -> {
                    CalorieProgressBar()
                }
            }
        }
    }

}