package com.swallaby.foodon.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun EmptyContentText(
    modifier: Modifier = Modifier,
    emptyText: String,
    @DrawableRes icon: Int = R.drawable.icon_meal_empty,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(80.dp).alpha(0.5f),
            painter = painterResource(id = icon),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = emptyText,
            style = NotoTypography.NotoMedium16.copy(color = G700),
            textAlign = TextAlign.Center
        )
    }
}