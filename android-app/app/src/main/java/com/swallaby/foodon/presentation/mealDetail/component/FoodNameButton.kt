package com.swallaby.foodon.presentation.mealDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun FoodNameButton(modifier: Modifier = Modifier) {
    Box {
        // 블러 처리된 배경 박스
        Box(
            modifier = modifier
                .matchParentSize()
                .blur(20.dp)
                .background(color = Color.Black.copy(alpha = .5f), shape = RoundedCornerShape(4.dp))
        )

        // 콘텐츠 (블러 없음)
        Row(
            modifier = modifier
                .height(29.dp)
                .border(
                    1.dp, color = Color.White.copy(alpha = .2f), shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("과일 케이크", style = NotoTypography.NotoMedium13.copy(color = Color.White))
            Spacer(modifier = modifier.width(4.dp))
            Image(
                painter = painterResource(R.drawable.icon_right_chevron),
                contentDescription = "right_chevron",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }

}


@Preview
@Composable
fun FoodNameButtonPreview() {
    FoodNameButton()
}