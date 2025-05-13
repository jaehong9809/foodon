package com.swallaby.foodon.presentation.mealdetail.component

import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import coil.compose.AsyncImage
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.ui.theme.uiCardShadow
import com.swallaby.foodon.core.util.ImageCropManager
import com.swallaby.foodon.core.util.StringUtil
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.Nutrition
import com.swallaby.foodon.domain.food.model.toNutrient
import kotlin.math.roundToInt

@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    food: MealItem,
    imageUri: Uri?,
    onClick: (foodId: Long) -> Unit,
    onDelete: (foodId: Long) -> Unit,
    enabledDeleteButton: Boolean = true,
    cropManager: ImageCropManager = ImageCropManager(LocalContext.current),
) {
    val nutrients: List<Nutrition> = food.nutrientInfo.toNutrient()
    var showDeletePopup by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    var iconPosition by remember { mutableStateOf(IntOffset.Zero) }
    var iconSize by remember { mutableStateOf(IntSize.Zero) }

    val foodImage = food.positions.firstOrNull()




    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = Border02, shape = RoundedCornerShape(10.dp))
            .padding(12.dp, 12.dp, 0.dp, 12.dp)
    ) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    foodImage?.let { image ->
                        AsyncImage(
                            model = cropManager.getCroppedImageRequest(
                                imageUri.toString(), image
                            ),
                            contentDescription = "음식 사진",
                            contentScale = ContentScale.FillBounds,
                            modifier = modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    } ?: Box(
                        modifier = modifier.size(64.dp)
                    )

                    Spacer(modifier.width(12.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.clickable(interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onClick(food.foodId) })
                        ) {
                            Text(
                                text = food.foodName,
                                style = NotoTypography.NotoMedium16.copy(color = G900)
                            )
                            Spacer(modifier.width(8.dp))
                            Image(
                                painter = painterResource(R.drawable.icon_right_chevron),
                                contentDescription = "right_chevron"
                            )
                        }
                        Spacer(modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                food.unit, style = SpoqaTypography.SpoqaMedium13.copy(color = G750)
                            )
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(Color(0xFFD9D9D9), shape = CircleShape)
                            )
                            Text(
                                text = stringResource(
                                    R.string.format_kcal,
                                    StringUtil.formatKcal(food.nutrientInfo.kcal)
                                ), style = SpoqaTypography.SpoqaMedium13.copy(color = G750)
                            )
                        }

                    }

                }
                Spacer(modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(nutrients.size) { index ->
                        NutritionalSmallInfo(
                            modifier = modifier, nutrition = nutrients[index]
                        )
                    }
                }
            }
            if (enabledDeleteButton) Box {
                Box(
                    modifier = modifier
                        .padding(end = 2.dp)
                        .size(32.dp)
                        .clickable(interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!showDeletePopup) showDeletePopup = true
                            })
                        .onGloballyPositioned { coordinates ->
                            // 아이콘 버튼의 위치와 크기를 저장
                            iconPosition = IntOffset(
                                coordinates.positionInWindow().x.roundToInt(),
                                coordinates.positionInWindow().y.roundToInt()
                            )
                            iconSize = coordinates.size
                        }, contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_vertical_more),
                        contentDescription = "more"
                    )
                }

                if (showDeletePopup) {
                    val popupWidth = 200.dp
                    val popupHeight = 48.dp

                    // 커스텀 PopupPositionProvider 생성
                    val popupPositionProvider = remember(iconPosition, iconSize) {
                        object : PopupPositionProvider {
                            override fun calculatePosition(
                                parentBounds: IntRect,
                                windowSize: IntSize,
                                layoutDirection: LayoutDirection,
                                popupContentSize: IntSize,
                            ): IntOffset {
                                // 팝업의 너비와 높이
                                val popupWidthPx = popupContentSize.width
                                val popupHeightPx = popupContentSize.height

                                // 아이콘의 x 위치 계산 (오른쪽 정렬)
                                val x = iconPosition.x + iconSize.width - popupWidthPx

                                // 화면 아래에 표시할 공간이 충분한지 확인
                                val isBelowVisible =
                                    iconPosition.y + iconSize.height + popupHeightPx <= windowSize.height

                                // 아이콘 버튼 위에 표시할 공간이 충분한지 확인
                                val isAboveVisible = iconPosition.y - popupHeightPx >= 0

                                val y = when {
                                    // 아래에 공간이 충분하면 아이콘 아래에 표시
                                    isBelowVisible -> iconPosition.y + iconSize.height
                                    // 위에 공간이 충분하면 아이콘 위에 표시
                                    isAboveVisible -> iconPosition.y - popupHeightPx
                                    // 둘 다 안 되면 일단 아래에 가능한 만큼 표시
                                    else -> windowSize.height - popupHeightPx
                                }

                                return IntOffset(x, y)
                            }
                        }
                    }

                    Popup(popupPositionProvider = popupPositionProvider,
                        onDismissRequest = { showDeletePopup = false }) {
                        Box(
                            modifier = Modifier
                                .width(popupWidth)
                                .height(popupHeight)
                                .uiCardShadow()
                                .border(
                                    1.dp, color = Border02, shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = Color.White, shape = RoundedCornerShape(10.dp)
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onDelete(food.foodId)
                                        showDeletePopup = false
                                    },
                                ), contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                text = stringResource(R.string.btn_delete),
                                style = NotoTypography.NotoNormal16.copy(color = G900)
                            )
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FoodCardPreview() {
    FoodCard(onClick = {}, food = MealItem(), onDelete = {}, imageUri = null)
}