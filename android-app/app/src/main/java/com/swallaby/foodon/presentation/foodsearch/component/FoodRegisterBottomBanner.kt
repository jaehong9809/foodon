package com.swallaby.foodon.presentation.foodsearch.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.Typography


@Composable
fun FoodRegisterBottomBanner(
    foodName: String,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRegisterClick() }
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                clip = true
            )
            .background(
                color = MainWhite,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_search_register_notice),
            contentDescription = "Info Icon",
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier =  Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.no_food_found),
                style = Typography.bodySmall,
                color = G700
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "'$foodName' 직접 등록하기",
                style = Typography.titleMedium,
                color = MainBlack,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_search_register_next),
            contentDescription = "Next Icon",
            modifier = Modifier.size(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FoodRegisterBannerPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(G700)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        FoodRegisterBottomBanner(
            foodName = "연어 샐러드",
            onRegisterClick = { }
        )
    }
}


@Composable
fun FoodRegisterContent(
    foodName: String,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(77.dp)
            .background(
                color = MainWhite,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_search_register_notice),
            contentDescription = "Info Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.no_food_found),
                style = Typography.bodySmall,
                color = G700
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "'$foodName' 직접 등록하기",
                style = Typography.titleMedium,
                color = MainBlack,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.icon_search_register_next),
            contentDescription = "Info Icon",
            modifier = Modifier.size(12.dp)
        )
    }
}


