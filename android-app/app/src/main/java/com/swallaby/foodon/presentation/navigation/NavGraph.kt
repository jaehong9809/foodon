package com.swallaby.foodon.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavControllerProvider(navController = navController) {
//        val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
//            initialValue = navController.currentBackStackEntry
//        )
//
//        val isMealDetailScreen = remember(currentBackStackEntry) {
//            navController.currentDestination?.let {
//                Log.d("NavGraph", "currentDestination: $it")
//                it.route == NavRoutes.FoodGraph.MealDetail.route
//            } ?: false
//        }
//
//        val allInsets =
//            WindowInsets.statusBars.union(WindowInsets.navigationBars).union(WindowInsets.ime)
//                .asPaddingValues()
//
//        Log.d("NavGraph", "isMealDetailScreen: $isMealDetailScreen")


        val mainViewModel: MainViewModel = hiltViewModel()
        val calendarViewModel: CalendarViewModel = hiltViewModel()
        val signUpViewModel: SignUpViewModel = hiltViewModel()
        val mealEditViewModel: MealEditViewModel = hiltViewModel()

        // todo 수정 예정
        val mainUiState by mainViewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(mainUiState) {
            Log.d("MealEditViewModel", "LaunchedEffect called")
            val recordResult = mainViewModel.uiState.value.recordResult
            Log.d("MealEditViewModel", "LaunchedEffect called = $recordResult")
            if (recordResult is ResultState.Success) {
                val nextMealType =
                    recordResult.data.filter { record -> record.mealTimeType != MealType.SNACK }
                        .maxOfOrNull { it.mealTimeType }?.let { maxMealType ->
                            when (maxMealType) {
                                MealType.BREAKFAST -> MealType.LUNCH       // 아침 식사 다음은 점심 식사
                                MealType.LUNCH -> MealType.DINNER          // 점심 식사 다음은 저녁 식사
                                MealType.DINNER -> MealType.SNACK          // 저녁 식사 다음은 간식
                                MealType.SNACK -> MealType.BREAKFAST       // 간식 다음은 아침 식사 (이 케이스는 필터링으로 제외됨)
                            }
                        } ?: MealType.BREAKFAST  // 필터링된 결과가 없을 경우 기본값으로 아침 식사
                Log.d("MealEditViewModel", "Next meal type: $nextMealType")
                mealEditViewModel.updateMealType(nextMealType)
            }
        }

        NavHost(
            navController = navController,
            startDestination = NavRoutes.MainGraph.route,
//            modifier = modifier.padding(
//                top = if (isMealDetailScreen) 0.dp else allInsets.calculateTopPadding(),
//                bottom = allInsets.calculateBottomPadding()
//            )
        ) {
            mainGraph(navController, mainViewModel)
            loginGraph(navController)
            calendarGraph(calendarViewModel)
            signUpGraph(navController, signUpViewModel)
            mealGraph(navController, mealEditViewModel)
        }
    }
}