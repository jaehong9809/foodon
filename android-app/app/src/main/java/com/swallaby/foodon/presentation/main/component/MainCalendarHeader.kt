package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import org.threeten.bp.YearMonth

@Composable
fun MainCalendarHeader(
    modifier: Modifier = Modifier,
    currentYearMonth: YearMonth,
    onMonthlyClick: () -> Unit,
    onTodayClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .background(MainWhite)
            .fillMaxWidth()
            .height(52.dp)
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = modifier
                    .wrapContentWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onMonthlyClick
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Box(
                    modifier = Modifier.padding(bottom = 2.5.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.format_calendar_date,
                            currentYearMonth.year,
                            currentYearMonth.monthValue
                        ),
                        style = NotoTypography.NotoBold16,
                        color = G900,
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.icon_chevron_black),
                    contentDescription = "달력",
                    modifier = Modifier.rotate(-90f)
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 50.dp, height = 32.dp)
                    .background(Bkg04, shape = RoundedCornerShape(100.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTodayClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.main_today),
                    color = G750,
                    style = SpoqaTypography.SpoqaMedium14,
                )
            }

        }

    }

}

