package com.swallaby.foodon.domain.main.model

import androidx.compose.ui.graphics.Color
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.BGBlue
import com.swallaby.foodon.core.ui.theme.BGGreen
import com.swallaby.foodon.core.ui.theme.BGRed
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.TextGreen
import com.swallaby.foodon.core.ui.theme.TextRed
import com.swallaby.foodon.core.ui.theme.WB500

enum class ManageStatus(val statusName: String, val textColor: Color, val bgColor: Color) {
    NORMAL("적정", TextGreen, BGGreen),
    LACK("부족", WB500, BGBlue),
    CAUTION("주의", TextRed, BGRed),
    DANGER("위험", G750, BG100);
}
