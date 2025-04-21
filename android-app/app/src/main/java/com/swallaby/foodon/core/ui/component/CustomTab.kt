package com.swallaby.foodon.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.MainBlue
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.SubBlack
import com.swallaby.foodon.core.ui.theme.TabBgGray
import com.swallaby.foodon.core.ui.theme.TabBorderGray
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.dropShadow

@Composable
fun MonthlyTabBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val icons = listOf(
        R.drawable.icon_rice_white,
        R.drawable.icon_weight_white,
        R.drawable.icon_sparkle_white
    )

    val labels = listOf(
        stringResource(R.string.tab_rice),
        stringResource(R.string.tab_weight),
        stringResource(R.string.tab_recommend)
    )

    Row(
        modifier = Modifier
            .dropShadow(
                shape = RoundedCornerShape(100.dp),
                color = MainBlack.copy(alpha = 0.2f),
                blur = 16.dp,
                offsetX = 4.dp,
                offsetY = 4.dp
            )
            .background(Color.White, shape = RoundedCornerShape(100.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icons.forEachIndexed { index, icon ->
            val isSelected = selectedIndex == index

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MainBlue else TabBgGray,
                label = "BackgroundColor"
            )

            val iconTint by animateColorAsState(
                targetValue = if (isSelected) MainWhite else MainBlue,
                label = "IconTint"
            )

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .defaultMinSize(minWidth = if (isSelected) 80.dp else 48.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(backgroundColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(index) }
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(iconTint),
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = labels[index],
                            color = MainWhite,
                            style = Typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeekTabBar(
    weeks: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(weeks) { index, label ->
            val isSelected = index == selectedIndex

            val borderColor by animateColorAsState(
                if (isSelected) MainBlue else TabBorderGray
            )
            val textColor by animateColorAsState(
                if (isSelected) MainBlue else SubBlack
            )
            val backgroundColor by animateColorAsState(
                MainWhite
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(100.dp))
                    .background(backgroundColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(index) }
                    )
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = label,
                    color = textColor,
                    style = Typography.titleMedium,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TabPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(1) }

        MonthlyTabBar(
            selectedIndex = selectedTabIndex,
            onTabSelected = { index -> selectedTabIndex = index }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "선택된 탭 인덱스: $selectedTabIndex")

        Spacer(modifier = Modifier.height(40.dp))

        // 주차별 탭
        var selectedWeek by remember { mutableIntStateOf(0) }

        WeekTabBar(
            weeks = (1..5).map { stringResource(R.string.tab_weekly, it) },
            selectedIndex = selectedWeek,
            onTabSelected = { selectedWeek = it }
        )
    }

}