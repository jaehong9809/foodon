package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.MainBlue
import com.swallaby.foodon.core.ui.theme.MainGray
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun CommonBackTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit),
) {
    Box(
        modifier = modifier
            .background(MainWhite)
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

        title?.let {
            TopBarText(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                fontColor = MainBlack
            )
        }
    }
}

@Composable
fun OnBoardingTopBar(
    modifier: Modifier = Modifier,
    curIdx: Int = 0,
    total: Int = 0,
    onBackClick: (() -> Unit),
) {
    Box(
        modifier = modifier
            .background(MainWhite)
            .fillMaxWidth()
            .height(52.dp)
            .padding(start = 16.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIconImage(modifier = Modifier, onBackClick)
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            TopBarText(
                text = curIdx.toString(),
                fontColor = MainBlue
            )

            Spacer(modifier = Modifier.width(2.dp))

            TopBarText(
                text = "/",
                fontColor = MainGray
            )

            Spacer(modifier = Modifier.width(2.dp))

            TopBarText(
                text = total.toString(),
                fontColor = MainGray
            )
        }
    }
}

@Composable
fun BackIconImage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Image(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onBackClick() }
            ),
        painter = painterResource(id = R.drawable.icon_back),
        contentDescription = "back",
    )
}

@Composable
fun TopBarText(
    modifier: Modifier = Modifier,
    text: String,
    fontColor: Color
) {
    Text(
        modifier = modifier,
        text = text,
        color = fontColor,
        style = NotoTypography.NotoMedium16,
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    Column {
        CommonBackTopBar(title = "title") {}

        OnBoardingTopBar(Modifier, 1, 5) {}
    }
}