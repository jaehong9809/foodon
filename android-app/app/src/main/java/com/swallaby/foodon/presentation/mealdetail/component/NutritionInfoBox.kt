package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Bkg03
import com.swallaby.foodon.domain.food.model.Nutrition
import kotlinx.coroutines.delay

@Composable
fun NutritionInfoBox(
    modifier: Modifier = Modifier,
    nutritions: List<Nutrition> = emptyList(),
) {
    val animationPlayed = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        animationPlayed.value = true
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Bkg03, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            NutritionalIngredientPercentage(
                modifier = modifier,
                nutritions = nutritions,
                animationPlayed = animationPlayed.value
            )

            Spacer(modifier = modifier.height(16.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                nutritions.forEachIndexed { index, nutrition ->
                    val delayMillis = index * 300L

                    var infoAnimationPlayed by remember { mutableStateOf(false) }

                    LaunchedEffect(animationPlayed.value) {
                        if (animationPlayed.value) {
                            delay(delayMillis)
                            infoAnimationPlayed = true
                        }
                    }

                    AnimatedNutritionalInfo(
                        modifier = modifier.weight(1f),
                        nutrition = nutrition,
                        visible = infoAnimationPlayed
                    )
                }
            }
        }
    }
}

@Composable
private fun NutritionalIngredientPercentage(
    modifier: Modifier = Modifier,
    nutritions: List<Nutrition> = emptyList(),
    animationPlayed: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        nutritions.forEachIndexed { index, nutrition ->
            val delayMillis = index * 300L // 300ms씩 지연

            // 애니메이션 진행 상태
            var currentAnimationPlayed by remember { mutableStateOf(false) }

            // 지연 시작을 위한 LaunchedEffect
            LaunchedEffect(animationPlayed) {
                if (animationPlayed) {
                    delay(delayMillis)
                    currentAnimationPlayed = true
                }
            }

            // 애니메이션된 비율 값
            val animatedRatio by animateFloatAsState(
                targetValue = if (currentAnimationPlayed) nutrition.ratio else 0f,
                animationSpec = tween(
                    durationMillis = 500, easing = FastOutSlowInEasing
                ),
                label = "BarAnimation"
            )

            PercentageBar(
                modifier = modifier
                    .height(24.dp)
                    .weight(nutrition.ratio),
                percentage = nutrition.ratio,
                animatedPercentage = animatedRatio,
                backgroundColor = nutrition.nutritionType.color
            )
        }
    }
}

@Composable
private fun AnimatedNutritionalInfo(
    modifier: Modifier = Modifier,
    nutrition: Nutrition,
    visible: Boolean,
) {
    val animationSpec = tween<Float>(
        durationMillis = 400, easing = FastOutSlowInEasing
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f, animationSpec = animationSpec, label = "Alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f, animationSpec = animationSpec, label = "Scale"
    )

    Box(
        modifier = modifier
            .alpha(alpha)
            .scale(scale)
    ) {
        NutritionalMediumInfo(
            modifier = Modifier.fillMaxWidth(),
            nutritionType = nutrition.nutritionType,
            amount = nutrition.amount
        )
    }
}


@Preview
@Composable
fun NutritionalIngredientPercentagePreview() {
    NutritionalIngredientPercentage()
}