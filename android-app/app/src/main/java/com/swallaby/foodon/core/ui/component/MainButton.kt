package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.Border025
import com.swallaby.foodon.core.ui.theme.G000
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

// vertical padding은 각 화면에서 알맞게 조절
@Composable
fun CommonWideButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(52.dp)
            .clickable(
                enabled = isEnabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() })
            .background(
                if (isEnabled) WB500 else Bkg04, shape = RoundedCornerShape(8.dp)
            ), contentAlignment = Alignment.Center
    ) {
        text?.let {
            Text(
                text = it,
                style = NotoTypography.NotoMedium16,
                color = if (isEnabled) MainWhite else G000,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CancelWideButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(52.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() })
            .border(1.dp, Border025, shape = RoundedCornerShape(8.dp))
            .background(MainWhite, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        text?.let {
            Text(
                modifier = Modifier, text = it, style = NotoTypography.NotoMedium16, color = G800
            )
        }
    }
}

// 음식 수정 버튼
@Composable
fun UpdateFoodButton(
    modifier: Modifier = Modifier, onDeleteClick: () -> Unit = {}, onUpdateClick: () -> Unit = {}
) {
    Row(modifier = modifier) {
        CancelWideButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.btn_delete),
            onClick = onDeleteClick,
        )

        Spacer(modifier = Modifier.width(8.dp))

        CommonWideButton(
            modifier = Modifier.weight(2f),
            text = stringResource(R.string.btn_update),
            isEnabled = true,
            onClick = onUpdateClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    Column {
        CommonWideButton(modifier = Modifier, "다음", true) {

        }

        CancelWideButton(modifier = Modifier, "삭제") {

        }

        UpdateFoodButton()
    }
}