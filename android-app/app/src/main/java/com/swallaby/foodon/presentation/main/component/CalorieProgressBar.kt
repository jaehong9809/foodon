package com.swallaby.foodon.presentation.main.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.component.BouncingAnimatedComponent
import com.swallaby.foodon.core.ui.theme.Bkg05
import com.swallaby.foodon.domain.food.model.Nutrition
import com.swallaby.foodon.domain.food.model.NutritionType
import kotlin.math.sin

@Composable
fun CalorieProgressBar(
    modifier: Modifier = Modifier,
    nutrients: List<Nutrition> = emptyList(),
    goal: Int = 0,
    consumed: Int = 0,
    strokeWidth: Dp = 12.dp
) {
    val startAngle = 165f
    val sweepAngle = 210f
    val backgroundColor = Bkg05
    val stroke = with(LocalDensity.current) { strokeWidth.toPx() }

    val targetKcalRatio = if (goal > 0) consumed.toFloat() / goal else 0f
    val animatedKcalRatio by animateFloatAsState(
        targetValue = targetKcalRatio.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500),
        label = "KcalRatio"
    )

    val nutrientAnimations = remember {
        mutableMapOf<NutritionType, Animatable<Float, *>>()
    }

    nutrients.forEach { nutrient ->
        if (nutrientAnimations[nutrient.nutritionType] == null) {
            nutrientAnimations[nutrient.nutritionType] = Animatable(nutrient.ratio)
        }
    }

//    LaunchedEffect(nutrients) {
//        nutrients.forEach { nutrient ->
//            nutrientAnimations[nutrient.nutritionType]?.let { anim ->
//                if (anim.value != nutrient.ratio) {
//                    anim.animateTo(
//                        targetValue = nutrient.ratio,
//                        animationSpec = tween(durationMillis = 500)
//                    )
//                }
//            }
//        }
//    }

//    LaunchedEffect(nutrients) {
//        nutrients.forEachIndexed { index, nutrient ->
//            nutrientAnimations[nutrient.nutritionType]?.let { anim ->
//                if (anim.value != nutrient.ratio) {
//                    kotlinx.coroutines.delay(index * 80L)
//
//                    anim.animateTo(
//                        targetValue = nutrient.ratio,
//                        animationSpec = tween(durationMillis = 500)
//                    )
//                }
//            }
//        }
//    }

    LaunchedEffect(nutrients) {
        nutrients.forEach { nutrient ->
            nutrientAnimations[nutrient.nutritionType]?.snapTo(nutrientAnimations[nutrient.nutritionType]?.value ?: 0f)
        }

        nutrients.forEachIndexed { index, nutrient ->
            nutrientAnimations[nutrient.nutritionType]?.let { anim ->
                if (anim.value != nutrient.ratio) {
                    kotlinx.coroutines.delay(index * 80L)
                    anim.animateTo(
                        targetValue = nutrient.ratio,
                        animationSpec = tween(durationMillis = 500)
                    )
                }
            }
        }
    }

//    val totalRatio = nutrients.sumOf {
//        nutrientAnimations[it.nutritionType]?.value?.toDouble() ?: 0.0
//    }.toFloat().coerceAtLeast(1f)

    val safeTotalRatio = maxOf(
        1f,
        nutrients.sumOf {
            nutrientAnimations[it.nutritionType]?.value?.toDouble() ?: 0.0
        }.toFloat()
    )


    val animatedNutrientRatios = nutrients.map {
        val animatedRatio = nutrientAnimations[it.nutritionType]?.value ?: it.ratio
        val normalized = animatedRatio / safeTotalRatio
        val adjusted = normalized * animatedKcalRatio
        it.copy(ratio = adjusted)
    }

    val arcHeightRatio = sin(Math.toRadians((sweepAngle / 2).toDouble())).toFloat()

    Box(
        modifier = modifier.size(width = 165.dp, height = 116.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.width / 2f
            val arcHeight = radius * arcHeightRatio
            val arcSize = Size(radius * 2, radius * 2)
            val topLeft = Offset(0f, radius - arcHeight)

            drawArc(
                color = backgroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = arcSize,
                topLeft = topLeft
            )

            var currentAngle = startAngle

            animatedNutrientRatios.forEach { nutrient ->
                val segmentSweep = sweepAngle * nutrient.ratio
                if (segmentSweep > 0.5f) {
                    drawArc(
                        color = nutrient.nutritionType.color,
                        startAngle = currentAngle,
                        sweepAngle = segmentSweep,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        size = arcSize,
                        topLeft = topLeft
                    )
                    currentAngle += segmentSweep
                }
            }
        }

        BouncingAnimatedComponent {
            CalorieInfo(goal = goal, consumed = consumed)
        }
    }
}
