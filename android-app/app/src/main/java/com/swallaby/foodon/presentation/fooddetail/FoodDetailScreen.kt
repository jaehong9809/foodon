package com.swallaby.foodon.presentation.fooddetail


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.presentation.fooddetail.component.FoodInfoComponent
import com.swallaby.foodon.presentation.fooddetail.component.FoodNameButton
import com.swallaby.foodon.presentation.fooddetail.component.MealType
import com.swallaby.foodon.presentation.fooddetail.component.NutritionalIngredientsComponent

@Composable
fun FoodDetailScreen(modifier: Modifier) {
    val scrollState = rememberScrollState()
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = Color.Red)
            ) {
                FoodNameButton()
                Text("이미지")
            }

            NutritionalIngredientsComponent(
                modifier = modifier, mealType = MealType.BREAKFAST, mealTime = "12:00"
            )

            Spacer(
                modifier
                    .height(8.dp)
                    .background(Bkg04)
            )
            FoodInfoComponent()

        }
    }
}


@Preview()
@Composable
fun FoodDetailScreenPreview() {
    FoodDetailScreen(modifier = Modifier)
}