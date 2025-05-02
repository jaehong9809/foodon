package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.main.model.NutrientManage

@Composable
fun NutrientManageContent(
    manageResult: ResultState<List<NutrientManage>>
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.main_nutrient_manage_title),
                color = G900,
                style = NotoTypography.NotoBold18
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.main_nutrient_manage_detail_title),
                        color = G700,
                        style = NotoTypography.NotoMedium16
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.icon_right_chevron),
                    contentDescription = null,
                    tint = G500
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        when (manageResult) {
            is ResultState.Success -> {
                NutrientGrid(manageResult.data)
            }
            else -> {}
        }

    }

}

@Composable
fun NutrientGrid(items: List<NutrientManage>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (rowItems in items.chunked(3)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach {
                    NutrientManageItem(modifier = Modifier.weight(1f), it)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}