package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.MainBlue
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes

@Composable
fun MainScreen() {

    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier,
            text = "Main Screen",
            color = MainBlack,
            style = Typography.displayLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .background(MainBlue)
                .clickable {
                    navController.navigate(NavRoutes.NutrientDetail.route)
                }
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "영양소 화면 이동",
                color = MainWhite,
                style = Typography.displayLarge
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    FoodonTheme {
        MainScreen()
    }
}