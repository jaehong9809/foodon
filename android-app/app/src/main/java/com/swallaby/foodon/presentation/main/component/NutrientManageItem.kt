package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.component.CommonBox
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.domain.main.model.NutrientIconType
import com.swallaby.foodon.domain.main.model.NutrientManage

@Composable
fun NutrientManageItem(
    modifier: Modifier = Modifier,
    nutrientManage: NutrientManage
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
                    painter = painterResource(id = NutrientIconType.getDrawable(nutrientManage.nutrientName)),
                    contentDescription = nutrientManage.nutrientName,
                    modifier = Modifier.size(32.dp)
                )
            }

            // 상태 뱃지
            Box(
                modifier = Modifier.offset(x = 9.dp, y = (-10).dp)
            ) {
                CommonBox(
                    content = nutrientManage.status.statusName,
                    bgColor = nutrientManage.status.bgColor,
                    textColor = nutrientManage.status.textColor,
                    horizontalPadding = 6.dp,
                    height = 20.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 이름
        Text(
            text = nutrientManage.nutrientName,
            style = NotoTypography.NotoMedium16.copy(color = G800)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 섭취량
        Text(
            text = "${nutrientManage.intake}${nutrientManage.unit}",
            style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
        )
    }
}