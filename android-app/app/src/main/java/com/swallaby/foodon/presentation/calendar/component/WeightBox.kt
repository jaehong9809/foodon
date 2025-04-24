package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun WeightBox(
    modifier: Modifier = Modifier,
    text: String,
    fontStyle: TextStyle = NotoTypography.NotoMedium13,
    isSmall: Boolean = false
) {

    Row(
        modifier = modifier
            .background(BG100, shape = RoundedCornerShape(4.dp))
            .padding(vertical = 4.dp, horizontal = if (isSmall) 5.dp else 8.dp),
        horizontalArrangement = Arrangement.spacedBy(if (isSmall) 2.dp else 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(if (isSmall) 6.dp else 8.dp)
                .background(color = WB500, shape = CircleShape)
        )

        Text(
            text = text,
            style = fontStyle,
            color = G750
        )
    }


}