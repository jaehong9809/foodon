package com.swallaby.foodon.presentation.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.KakaoYellow
import com.swallaby.foodon.core.ui.theme.MainBlack
import com.swallaby.foodon.core.ui.theme.Typography

@Composable
fun KakaoLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(KakaoYellow)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.kakao_login_large_wide),
            contentDescription = "Kakao Logo",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.kakao_login),
            color = MainBlack,
            fontSize = 16.sp,
            style = Typography.bodyLarge
        )
    }
}