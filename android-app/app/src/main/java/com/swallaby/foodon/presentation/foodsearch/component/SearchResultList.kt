package com.swallaby.foodon.presentation.foodsearch.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.food.model.Food


@Composable
fun SearchResultList(
    searchResults: LazyPagingItems<Food>,
    onClick: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        items(count = searchResults.itemCount) { index ->
            searchResults[index]?.let { item ->
                SearchResultItem(
                    foodItem = item,
                    onClick = { onClick(item) },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}


@Composable
fun SearchResultItem(
    foodItem: Food,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = foodItem.name,
            style = Typography.titleLarge,
            color = G900
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${foodItem.servingUnit} • ${foodItem.kcal}kcal",
            style = Typography.titleMedium,
            color = G700
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (foodItem.isCustom) {
            RegisterBox("직접 등록", WB500.copy(alpha = 0.12f), WB500)
        }
    }

    HorizontalDivider(
        color = Border02,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
}



// TODO: MainBox 컴포저블로 추후 변경
@Composable
fun RegisterBox(
    content: String,
    bgColor: Color,
    textColor: Color,
    textStyle: TextStyle = NotoTypography.NotoMedium13,
    horizontalPadding: Dp = 8.dp,
    height: Dp = 24.dp
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(height)
            .background(color = bgColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = content,
            color = textColor,
            style = textStyle,
        )
    }
}
