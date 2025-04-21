package com.swallaby.foodon.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.MainBlue
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.Typography
import com.swallaby.foodon.core.ui.theme.dropShadow

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String? = null,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .wrapContentHeight()
            .height(48.dp)
            .dropShadow(
                shape = RoundedCornerShape(100.dp),
                color = MainBlack.copy(alpha = 0.2f),
                blur = 8.dp,
                offsetY = 4.dp
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() }
            )
            .background(
                MainBlue,
                shape = RoundedCornerShape(100.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = icon),
                contentDescription = "ai camera",
            )

            text?.let {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier,
                    text = it,
                    style = Typography.titleLarge,
                    color = MainWhite
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingButton(modifier = Modifier, R.drawable.icon_ai_camera, "기록하기") {}
    }
}