package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatNutritionNumber
import com.swallaby.foodon.core.util.StringUtil.formatNutritionOrigin
import com.swallaby.foodon.domain.food.model.NutritionType
import com.swallaby.foodon.domain.main.model.NutrientIntake

@Composable
fun IntakeDetail(
    intake: NutrientIntake
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = Border02, shape = RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IntakeItem(NutritionType.CARBOHYDRATE, intake.intakeCarbs, intake.goalCarbs)
        IntakeItem(NutritionType.PROTEIN, intake.intakeProtein, intake.goalProtein)
        IntakeItem(NutritionType.FAT, intake.intakeFat, intake.goalFat)
    }
}

@Composable
fun IntakeItem(
    nutritionType: NutritionType,
    nutrientIntake: Double,
    nutrientTarget: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .aspectRatio(1f)
                    .background(
                        color = nutritionType.color, shape = RoundedCornerShape(2.dp)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    nutritionType.displayName.slice(IntRange(0, 0)),
                    style = SpoqaTypography.SpoqaBold10.copy(color = MainWhite)
                )
            }

            Box(
                modifier = Modifier.padding(bottom = 2.dp),
            ) {
                Text(
                    nutritionType.displayName,
                    style = NotoTypography.NotoMedium16.copy(color = G800)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatNutritionNumber(nutrientIntake),
                style = SpoqaTypography.SpoqaBold16.copy(color = G900)
            )

            Text(
                text = "/${formatNutritionOrigin(nutrientTarget)}",
                style = SpoqaTypography.SpoqaMedium16.copy(color = G500)
            )
        }

    }
}