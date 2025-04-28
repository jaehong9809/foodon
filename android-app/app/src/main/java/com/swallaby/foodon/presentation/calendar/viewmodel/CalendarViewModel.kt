package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.model.CalendarWeight
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarUseCase: GetCalendarUseCase
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

    fun fetchCalendarData(type: CalendarType, date: String) {
        updateState { it.copy(calendarState = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getCalendarUseCase(type, date)
//            updateState { it.copy(calendarState = result.toResultState()) }

            val fakeData: List<CalendarItem> = when (type) {
                CalendarType.MEAL -> listOf(
                    CalendarItem.Meal(
                        data = CalendarMeal(
                            calendarType = CalendarType.MEAL,
                            calendarId = 1L,
                            date = "2025-04-01",
                            intakeKcal = 1800,
                            goalKcal = 2000
                        )
                    ),
                    CalendarItem.Meal(
                        data = CalendarMeal(
                            calendarType = CalendarType.MEAL,
                            calendarId = 2L,
                            date = "2025-04-05",
                            intakeKcal = 1950,
                            goalKcal = 2000
                        )
                    )
                )

                CalendarType.WEIGHT -> listOf(
                    CalendarItem.Weight(
                        data = CalendarWeight(
                            calendarType = CalendarType.WEIGHT,
                            calendarId = 3L,
                            date = "2025-04-03",
                            weight = 68
                        )
                    ),
                    CalendarItem.Weight(
                        data = CalendarWeight(
                            calendarType = CalendarType.WEIGHT,
                            calendarId = 4L,
                            date = "2025-04-09",
                            weight = 67
                        )
                    )
                )

                CalendarType.RECOMMENDATION -> listOf(
                    CalendarItem.Recommendation(
                        data = CalendarRecommendation(
                            calendarType = CalendarType.RECOMMENDATION,
                            calendarId = 5L,
                            date = "2025-04-07",
                            thumbnailImage = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740"
                        )
                    ),
                    CalendarItem.Recommendation(
                        data = CalendarRecommendation(
                            calendarType = CalendarType.RECOMMENDATION,
                            calendarId = 6L,
                            date = "2025-04-10",
                            thumbnailImage = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740"
                        )
                    )
                )
            }

            updateState {
                it.copy(calendarState = ResultState.Success(fakeData))
            }
        }
    }

    fun selectDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
    }

    fun updateState(block: (CalendarUiState) -> CalendarUiState) {
        _uiState.update(block)
    }

}