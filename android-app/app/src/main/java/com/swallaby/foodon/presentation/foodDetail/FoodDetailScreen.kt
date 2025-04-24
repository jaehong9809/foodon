package com.swallaby.foodon.presentation.foodDetail


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.BackIconImage
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.presentation.foodDetail.component.FoodInfoComponent
import com.swallaby.foodon.presentation.foodDetail.component.MealType
import com.swallaby.foodon.presentation.foodDetail.component.NutritionalIngredientsComponent

@Composable
fun FoodDetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFoodClick: (foodId: Long) -> Unit,
) {

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740")
                    .crossfade(true).listener(onError = { _, result ->
                        Log.e("ImageLoading", "Error loading image: ${result.throwable}")
                    }, onSuccess = { _, _ ->
                        Log.d("ImageLoading", "Image loaded successfully")
                    }).build(),
                contentDescription = "음식 사진",
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
//                contentScale = ContentScale.Crop,
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.icon_time), // 에러 시 표시할 이미지
                placeholder = painterResource(R.drawable.icon_search) // 로딩 중 표시할 이미지
            )

            // todo icon_time 의 크기가 피그마와 일치하지 않음
            //  피그마보다 좀 더 작음
            NutritionalIngredientsComponent(
                modifier = modifier, mealType = MealType.BREAKFAST, mealTime = "12:00"
            )

            Spacer(
                modifier
                    .height(8.dp)
                    .background(Bkg04)
            )

            FoodInfoComponent(onClick = onFoodClick)

        }

        CommonWideButton(modifier.padding(horizontal = 24.dp), text = "기록 완료", onClick = {})
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIconImage(modifier = Modifier, onBackClick)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodDetailScreenPreview() {
    FoodonTheme {
        FoodDetailScreen(modifier = Modifier, onBackClick = {}, onFoodClick = {})
    }
}