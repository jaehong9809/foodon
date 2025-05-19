package com.swallaby.foodon.presentation.mealdetail.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.VerticalSlideAnimatedComponent
import com.swallaby.foodon.core.ui.theme.G900
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

            if (foods.isNotEmpty()) {
                IconButton(onClick = onAdd) {
                    Icon(
                        painter = painterResource(R.drawable.icon_check), contentDescription = "add"
                    )
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