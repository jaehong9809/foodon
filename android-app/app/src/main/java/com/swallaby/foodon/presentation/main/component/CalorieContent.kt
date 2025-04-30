package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.swallaby.foodon.domain.food.model.Nutrition
import com.swallaby.foodon.domain.food.model.NutritionType
import com.swallaby.foodon.domain.main.model.NutrientIntake

@Composable
fun CalorieContent(
    intakeResult: ResultState<NutrientIntake>
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // TODO: 오늘 -> 오늘의 섭취량, 다른 날 -> 날짜 표시
        Text(
            text = stringResource(R.string.main_nutrient_intake_title),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
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
                }
                else -> {
                    CalorieProgressBar()
                }
            }
        }
    }

}