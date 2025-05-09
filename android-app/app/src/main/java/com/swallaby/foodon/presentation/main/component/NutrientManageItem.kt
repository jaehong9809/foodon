package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBox
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.domain.main.model.HealthEffect
import com.swallaby.foodon.domain.main.model.NutrientManage

@Composable
fun NutrientManageSummaryItem(
    modifier: Modifier = Modifier,
    nutrient: NutrientManage
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(1.dp, Border02, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = nutrient.nutrientCode.icon),
                    contentDescription = nutrient.nutrientName,
                    modifier = Modifier.size(32.dp)
                )
            }

            // 상태 뱃지
            Box(
                modifier = Modifier.offset(x = 9.dp, y = (-10).dp)
            ) {
                CommonBox(
                    content = nutrient.status.statusName,
                    bgColor = nutrient.status.bgColor,
                    textColor = nutrient.status.textColor,
                    horizontalPadding = 6.dp,
                    height = 20.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        NutrientInfo(nutrient.nutrientName, "${nutrient.intake}${nutrient.unit.value}")
    }
}

@Composable
fun NutrientManageDetailItem(
    nutrient: NutrientManage,
    type: HealthEffect
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = nutrient.nutrientCode.icon),
                contentDescription = nutrient.nutrientName,
                modifier = Modifier.size(32.dp)
            )

            val content = if (type == HealthEffect.BENEFICIAL) stringResource(
                R.string.main_pager_good_nutrient_range,
                nutrient.minRecommend,
                nutrient.maxRecommend,
                nutrient.unit.value
            )
            else stringResource(
                R.string.main_pager_bad_nutrient_range,
                nutrient.maxRecommend,
                nutrient.unit.value
            )

            NutrientInfo(
                name = nutrient.nutrientName,
                content = content,
                isDetail = true
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${nutrient.intake}${nutrient.unit.value}",
                style = SpoqaTypography.SpoqaBold16.copy(color = G900)
            )

            CommonBox(
                content = nutrient.status.statusName,
                bgColor = nutrient.status.bgColor,
                textColor = nutrient.status.textColor,
                textStyle = NotoTypography.NotoMedium13
            )
        }

    }
}

@Composable
fun NutrientInfo(
    name: String,
    content: String,
    isDetail: Boolean = false
) {
    Column(
        horizontalAlignment = if (isDetail) Alignment.Start else Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = NotoTypography.NotoMedium16.copy(color = G800)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 섭취량
        Text(
            text = content,
            style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
        )
    }
}