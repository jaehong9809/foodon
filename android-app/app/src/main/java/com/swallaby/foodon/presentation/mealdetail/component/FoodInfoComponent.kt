package com.swallaby.foodon.presentation.mealdetail.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.VerticalSlideAnimatedComponent
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.ImageCropManager
import com.swallaby.foodon.domain.food.model.MealItem

@Composable
fun FoodInfoComponent(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    foods: List<MealItem> = emptyList(),
    onClick: (foodId: Long) -> Unit,
    onDelete: (foodId: Long) -> Unit,
    onAdd: () -> Unit,
    enabledDeleteButton: Boolean = true,
) {
    val cropManager = ImageCropManager(LocalContext.current)
    val context = LocalContext.current

    val positions = foods.mapNotNull { mealItem ->
        mealItem.positions.firstOrNull()
    }
    var isLoad by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 이미지 로드 및 크롭
        cropManager.loadAndCropImage(
            imageUri.toString(), positions
        ) {
            isLoad = true
        }

    }

    if (isLoad) {
        Column(
            modifier = modifier
                .background(color = Color.White)
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.food_info_check),
                style = NotoTypography.NotoBold18.copy(color = G900)
            )
            Spacer(modifier = modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(foods.size) { index ->
                    VerticalSlideAnimatedComponent {
                        FoodCard(
                            food = foods[index],
                            onClick = onClick,
                            onDelete = onDelete,
                            imageUri = imageUri,
                            enabledDeleteButton = enabledDeleteButton
                        )
                    }
                }
            }


            if (enabledDeleteButton) {
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    onClick = onAdd,
                    colors = ButtonColors(
                        containerColor = WB500,
                        contentColor = Color.White,
                        disabledContainerColor = WB500.copy(alpha = 0.5f),
                        disabledContentColor = G900.copy(alpha = 0.5f),
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("음식 추가하기", style = NotoTypography.NotoMedium16)
                    }
                }

            }
        }
    }


}


@Preview
@Composable
fun FoodInfoComponentPreview() {
    FoodInfoComponent(onClick = {}, onDelete = {}, imageUri = null, onAdd = {})
}