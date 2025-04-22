package com.swallaby.foodon.presentation.fooddetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium14

@Composable
fun DropButton(
    modifier: Modifier = Modifier,
    prefixIcon: @Composable () -> Unit,
    onClick: () -> Unit,
    text: String,
) {
    TextButton(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color = Border025)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            prefixIcon()
            Text(text = text, style = SpoqaMedium14.copy(color = G800))
            Image(
                painter = painterResource(R.drawable.icon_down_chevron),
                contentDescription = "down_chevron"
            )
        }

    }
}


