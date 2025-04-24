package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarUseCase: GetCalendarUseCase
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

    private val _calorieDataMap = mutableStateOf<Map<LocalDate, Int>>(emptyMap())
    val calorieDataMap: androidx.compose.runtime.State<Map<LocalDate, Int>> get() = _calorieDataMap

    private val _selectedDate = mutableStateOf<LocalDate?>(null)
    val selectedDate: androidx.compose.runtime.State<LocalDate?> get() = _selectedDate

    fun fetchCalendarData(type: CalendarType, date: String) {
        _uiState.value = _uiState.value.copy(calendarState = ResultState.Loading)

        viewModelScope.launch {
            val result = getCalendarUseCase(type, date)
            _uiState.value = _uiState.value.copy(calendarState = result.toResultState())
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        updateSelectedDate(date)
    }

    fun updateSelectedDate(newDate: LocalDate) {
        _selectedDate.value = newDate
    }

    fun updateCalorieData(newCalorieMap: Map<LocalDate, Int>) {
        _calorieDataMap.value = newCalorieMap
    }
}