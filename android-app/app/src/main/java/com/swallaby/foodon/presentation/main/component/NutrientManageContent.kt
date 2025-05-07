package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.EmptyContentText
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes

@Composable
fun NutrientManageContent(
    manageResult: ResultState<List<NutrientManage>>
) {

    val navController = LocalNavController.current
    val manageItems = (manageResult as? ResultState.Success)?.data.orEmpty()

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

            if (manageItems.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                navController.navigate(NavRoutes.NutrientDetail.route)
                            }
                        ),
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
        }

        Spacer(modifier = Modifier.height(36.dp))

        if (manageItems.isEmpty()) {
            EmptyContentText(
                modifier = Modifier.weight(1f),
                emptyText = stringResource(R.string.main_nutrient_manage_empty))
        } else {
            NutrientGrid(manageItems)
        }
    }

}

@Composable
fun NutrientGrid(items: List<NutrientManage>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        for (rowItems in items.chunked(3)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach {
                    NutrientManageSummaryItem(modifier = Modifier.weight(1f), it)
                }
            }
        }
    }
}