package com.swallaby.foodon.presentation.mealdetail.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.food.model.Position

@Composable
fun FoodLabelButton(
    position: Position,
    centerPosition: Size,
    foodName: String,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        // 내부에 버튼 컴포넌트 직접 구현
        Box(modifier = Modifier
            .layout { measurable, constraints ->
                // 자식 컴포넌트를 측정
                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = 0,
                        minHeight = 0,
                        maxWidth = constraints.maxWidth,
                        maxHeight = constraints.maxHeight
                    )
                )

                // 컨테이너 크기 (Box 크기)
                val containerWidth = constraints.maxWidth.toFloat()
                val containerHeight = constraints.maxHeight.toFloat()

                Log.d(
                    "FoodLabelButton",
                    "Container size: ${position.x + centerPosition.width} x ${position.y + centerPosition.height}"
                )

                // 중앙 위치 계산 (버튼 자체 크기의 절반을 빼서 중앙 정렬)
                val x =
                    ((position.x + centerPosition.width) * containerWidth).toInt() - (placeable.width / 2)
                val y =
                    ((position.y + centerPosition.height) * containerHeight).toInt() - (placeable.height / 2)

                // 원래 크기를 유지하면서 위치만 조정
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(x, y)
                }
            }
            .clickable(onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
        ) {
            // 블러 처리된 배경 박스
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = Color.Black.copy(alpha = .5f), shape = RoundedCornerShape(4.dp)
                    )
                    .blur(20.dp)
            )

            // 콘텐츠 (블러 없음)
            Row(
                modifier = Modifier
                    .height(29.dp)
                    .border(
                        1.dp,
                        color = Color.White.copy(alpha = .2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = foodName, style = NotoTypography.NotoMedium13.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(R.drawable.icon_right_chevron),
                    contentDescription = "right_chevron",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodLabelButtonPreview() {
    FoodLabelButton(
        foodName = "과일 케이크",
        position = Position(),
        centerPosition = Size(100f, 100f)
    )
}