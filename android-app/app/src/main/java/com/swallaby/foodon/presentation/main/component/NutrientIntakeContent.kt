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
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import com.swallaby.foodon.presentation.main.viewmodel.MainUiState

@Composable
fun NutrientIntakeContent(
    mainUiState: MainUiState,
    calendarUiState: CalendarUiState,
) {

    val today = calendarUiState.today
    val selectedDate = calendarUiState.selectedDate
    val intakeResult = mainUiState.intakeResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = if (selectedDate != today) stringResource(
                R.string.main_nutrient_intake_date_title,
                formatDate(selectedDate)
            )
            else stringResource(R.string.main_nutrient_intake_today_title),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val calorie: NutrientIntake? = (intakeResult as? ResultState.Success)?.data

            val nutrients = calorie?.let {
                val carbsRatio = it.intakeCarbs.toFloat() * 4 / it.goalKcal
                val proteinRatio = it.intakeProtein.toFloat() * 4 / it.goalKcal
                val fatRatio = it.intakeFat.toFloat() * 9 / it.goalKcal

                listOf(
                    Nutrition(NutritionType.CARBOHYDRATE, it.intakeCarbs, carbsRatio),
                    Nutrition(NutritionType.PROTEIN, it.intakeProtein, proteinRatio),
                    Nutrition(NutritionType.FAT, it.intakeFat, fatRatio),
                ).sortedByDescending { n -> n.amount }
            } ?: emptyList()

            CalorieProgressBar(
                nutrients = nutrients,
                consumed = calorie?.intakeKcal ?: 0,
                goal = calorie?.goalKcal ?: 0
            )

            Spacer(modifier = Modifier.height(12.dp))

            IntakeDetail(calorie ?: NutrientIntake())
        }
    }

}