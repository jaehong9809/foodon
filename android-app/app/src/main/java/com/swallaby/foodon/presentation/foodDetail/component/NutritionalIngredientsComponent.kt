package com.swallaby.foodon.presentation.foodDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg03
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaBold24
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium16
import java.text.NumberFormat
import java.util.Locale

enum class MealType(val displayName: String) {
    BREAKFAST("아침 식사"), LUNCH("점심 식사"), DINNER("저녁 식사"), SNACK("간식");
}

@Composable
fun NutritionalIngredientsComponent(
    modifier: Modifier,
    mealType: MealType,
    mealTime: String,
) {
    Box(
        modifier = modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(24.dp),
    ) {
        Column {
            Row(modifier = modifier.fillMaxWidth()) {
                DropButton(
                    modifier = modifier
                        .wrapContentWidth()
                        .height(32.dp),
                    onClick = {},
                    text = mealType.displayName,
                    suffixIcon = {
                        Image(
                            painter = painterResource(R.drawable.icon_down_chevron),
                            contentDescription = "down_chevron"
                        )
                    },
                )
                Spacer(modifier = modifier.width(6.dp))
                DropButton(
                    modifier = modifier
                        .wrapContentWidth()
                        .height(32.dp),
                    onClick = {},
                    text = mealTime,
                    prefixIcon = {
                        Image(
                            modifier = modifier.size(12.dp),
                            painter = painterResource(R.drawable.icon_time),
                            contentDescription = "time"
                        )
                    },
                    suffixIcon = {
                        Image(
                            painter = painterResource(R.drawable.icon_down_chevron),
                            contentDescription = "down_chevron"
                        )
                    },

                    )
            }
            Spacer(modifier.height(24.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.total_intake), style = SpoqaMedium16.copy(color = G900)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        NumberFormat.getNumberInstance(Locale.KOREA).format(1000),
                        style = SpoqaBold24.copy(color = G900)
                    )
                    Spacer(modifier = modifier.width(2.dp))
                    Text("kal", style = SpoqaMedium16.copy(color = G700))

                }
            }
            Spacer(modifier.height(16.dp))
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Bkg03, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = modifier.fillMaxWidth()) {
                    NutritionalIngredientPercentage(modifier = modifier)
                    Spacer(modifier = modifier.height(16.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    ) {
                        NutritionalMediumInfo(modifier.weight(1f))
                        NutritionalMediumInfo(modifier.weight(1f))
                        NutritionalMediumInfo(modifier.weight(1f))
                    }
                }
            }

        }

    }

}


@Preview
@Composable
fun NutritionalIngredientsComponentPreview() {
    NutritionalIngredientsComponent(
        modifier = Modifier,
        mealType = MealType.BREAKFAST,
        mealTime = "12:00",
    )
}