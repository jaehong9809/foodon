package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium14

private val DefaultPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)

@Composable
fun DropButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = DefaultPadding,
    contentModifier: Modifier = Modifier,
    contentAlignment: Arrangement.Horizontal = Arrangement.spacedBy(4.dp),
    prefixIcon: @Composable () -> Unit = {},
    suffixIcon: @Composable () -> Unit = {},
    text: String,
    textStyle: TextStyle = SpoqaMedium14.copy(color = G800),
    onClick: () -> Unit,
) {
    TextButton(
        contentPadding = contentPadding,
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color = Border025)
    ) {
        Row(
            modifier = contentModifier,
            horizontalArrangement = contentAlignment,
            verticalAlignment = Alignment.CenterVertically
        ) {
            prefixIcon()
            Text(text = text, style = textStyle)
            suffixIcon()
        }

    }
}


