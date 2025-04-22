package com.swallaby.foodon.presentation.foodDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography

@Composable
fun FoodCard(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = Border02, shape = RoundedCornerShape(10.dp))
            .padding(12.dp)
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
                    AsyncImage(
                        model = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740",
                        contentDescription = "음식 사진",
                        modifier
                            .height(64.dp)
                            .width(64.dp)
                    )
                    Spacer(modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "바질 피자",
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
                            Text("1조각", style = SpoqaTypography.SpoqaMedium13.copy(color = G750))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(Color(0xFFD9D9D9), shape = CircleShape)
                            )
                            Text("100kal", style = SpoqaTypography.SpoqaMedium13.copy(color = G750))
                        }

                    }

                }
                Spacer(modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NutritionalSmallInfo(modifier)
                    NutritionalSmallInfo(modifier)
                    NutritionalSmallInfo(modifier)
                }
            }
            Image(
                painter = painterResource(
                    R.drawable.icon_vertical_more,
                ), contentDescription = "more"
            )
        }

    }
}

@Preview
@Composable
fun FoodCardPreview() {
    FoodCard()
}