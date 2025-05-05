package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.RoundedCircularProgress
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import org.threeten.bp.LocalDate

@Composable
fun CalendarDayItem(
    calendarType: CalendarType = CalendarType.MEAL,
    calendarItem: CalendarItem?,
    date: LocalDate,
    today: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isFutureDay = date.isAfter(today)
    val recommendationImageUrl = (calendarItem as? CalendarItem.Recommendation)?.data?.thumbnailImage.orEmpty()
    val hasRecommendationImage = recommendationImageUrl.isNotBlank()

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isFutureDay,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (calendarType == CalendarType.RECOMMENDATION) 41.dp else 30.dp),
            contentAlignment = Alignment.Center
        ) {
            when (calendarType) {
                CalendarType.MEAL -> {
                    val progress = (calendarItem as? CalendarItem.Meal)?.data?.let { meal ->
                        if (meal.goalKcal > 0) (meal.intakeKcal.toFloat() / meal.goalKcal).coerceIn(0f, 1f)
                        else 0f
                    } ?: 0f

                    RoundedCircularProgress(
                        progress = progress,
                        modifier = Modifier.size(30.dp)
                    )
                }
                CalendarType.RECOMMENDATION -> {
                    if (hasRecommendationImage) {
                        RecommendationFoodImage(recommendationImageUrl)
                    }
                }
                else -> {
                    Unit
                }
            }

            DayText(
                day = date.dayOfMonth,
                isSelected = isSelected,
                isFutureDay = isFutureDay,
                textColor = if (hasRecommendationImage) MainWhite else G900
            )
        }

        DayBottomContent(calendarItem)
    }
}

@Composable
fun DayText(
    day: Int,
    isSelected: Boolean,
    isFutureDay: Boolean,
    textColor: Color
) {
    val fontColor = when {
        isSelected -> MainWhite
        isFutureDay -> G500
        else -> textColor
    }

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp)
            .background(
                color = if (isSelected) WB500 else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$day",
            style = SpoqaTypography.SpoqaBold13,
            color = fontColor,
            modifier = Modifier.padding(horizontal = 1.dp, vertical = 0.5.dp)
        )
    }
}

@Composable
fun DayBottomContent(calendarItem: CalendarItem?) {
    calendarItem?.let {
        Spacer(modifier = Modifier.height(8.dp))

        when (it) {
            is CalendarItem.Meal -> {
                Text(
                    text = formatKcal(it.data.intakeKcal),
                    style = SpoqaTypography.SpoqaMedium11,
                    color = G700
                )
            }
            is CalendarItem.Weight -> {
                WeightBox(
                    text = stringResource(R.string.format_kg, it.data.weight),
                    fontStyle = SpoqaTypography.SpoqaMedium11,
                    isSmall = true
                )
            }
            else -> {
                Unit
            }
        }
    }
}

@Composable
fun RecommendationFoodImage(
    imageUrl: String
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "추천 음식 사진",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MainBlack.copy(alpha = 0.2f), shape = CircleShape)
        )
    }
}
