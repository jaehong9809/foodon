package com.swallaby.foodon.presentation.calendar.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun TabContentLayout(
    modifier: Modifier = Modifier,
    title: String,
    bgColor: Color,
    @DrawableRes icon: Int = R.drawable.icon_mountain,
    bottomPadding: Dp = 16.dp,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = bgColor, shape = RoundedCornerShape(10.dp))
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = bottomPadding)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                color = G800,
                style = NotoTypography.NotoMedium16,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        content()
    }
}