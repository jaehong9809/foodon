package com.swallaby.foodon.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.domain.main.model.NutrientManageType
import com.swallaby.foodon.presentation.main.component.NutrientManageDetailItem
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel

@Composable
fun NutrientDetailScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onBackClick: (() -> Unit),
) {

    val uiState by viewModel.uiState.collectAsState()

    val nutrientList = (uiState.manageResult as? ResultState.Success)?.data.orEmpty()
    val essentialList = nutrientList.filter { it.manageType == NutrientManageType.ESSENTIAL }
    val limitedList = nutrientList.filter { it.manageType == NutrientManageType.LIMITED }

    Scaffold(
        topBar = {
            CommonBackTopBar(
                title = stringResource(R.string.top_bar_nutrient_detail)) {
                onBackClick()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            NutrientDetailInfo(essentialList, NutrientManageType.ESSENTIAL)

            HorizontalDivider(thickness = 8.dp, color = Bkg04)

            NutrientDetailInfo(limitedList, NutrientManageType.LIMITED)
        }
    }

}

@Composable
fun NutrientDetailInfo(
    manageItems: List<NutrientManage>,
    type: NutrientManageType
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(
                if (type == NutrientManageType.ESSENTIAL) R.string.main_nutrient_manage_good_title
                else R.string.main_nutrient_manage_bad_title
            ),
            color = G900,
            style = NotoTypography.NotoBold18
        )

        manageItems.forEach { item ->
            NutrientManageDetailItem(item, type)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutrientDetailPreview() {
    FoodonTheme {
        NutrientDetailScreen(
            onBackClick = { }
        )
    }
}