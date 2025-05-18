package com.swallaby.foodon.presentation.foodedit.component

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.util.ImageCropManager
import com.swallaby.foodon.domain.food.model.MealItem


@Composable
fun FoodThumbnailList(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    foods: List<MealItem>,
    selectedFoodId: Long = 0L,
    selectFood: (foodId: Long, foodName: String) -> Unit = { _, _ -> },
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val cropManager = ImageCropManager(context)


    val positions = foods.mapNotNull { mealItem ->
        mealItem.positions.firstOrNull()
    }
    var isLoad by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 이미지 로드 및 크롭
        cropManager.loadAndCropImage(
            imageUri.toString(),
            positions
        ) {
            isLoad = true
        }
    }



    if (isLoad) {
        Row(
            modifier = modifier
                .horizontalScroll(scrollState)
                .padding(top = 8.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(foods.size) { index ->
                FoodThumbnail(
                    modifier,
                    isSelected = foods[index].foodId == selectedFoodId,
                    mealItem = foods[index],
                    selectFood = selectFood,
                    imageUri = imageUri,
                    cropManager = cropManager
                )
            }
        }
    }

}

@Composable
private fun FoodThumbnail(
    modifier: Modifier,
    imageUri: Uri?,
    mealItem: MealItem,
    isSelected: Boolean = false,
    selectFood: (foodId: Long, foodName: String) -> Unit = { _, _ -> },
    cropManager: ImageCropManager = ImageCropManager(LocalContext.current),
) {
    val foodImage = mealItem.positions.firstOrNull()

    Column(modifier = modifier.clickable(onClick = {
        selectFood(mealItem.foodId, mealItem.foodName)
    }, indication = null, interactionSource = remember {
        MutableInteractionSource()
    }), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier
                .border(
                    2.dp,
                    color = if (isSelected) WB500 else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .size(72.dp), contentAlignment = Alignment.Center
        ) {
            foodImage?.let {
                AsyncImage(
                    model = cropManager.getCroppedImageRequest(
                        imageUri.toString(),
                        it
                    ),
                    contentDescription = "음식 사진",
                    contentScale = ContentScale.FillBounds,
                    modifier = modifier
                        .size(64.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                )
            }

        }
        Text(
            mealItem.foodName,
            style = NotoTypography.NotoBold14.copy(color = if (isSelected) WB500 else G900)
        )
    }
}

@Preview
@Composable
private fun FoodThumbnailListPreview() {
    FoodThumbnailList(
        foods = listOf(
            MealItem(
                foodId = 1L,
                foodName = "피자",
            ),
        ),
        selectedFoodId = 1L,
        imageUri = null,
    )
}