package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarUseCase: GetCalendarUseCase
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

    fun fetchCalendarData(type: CalendarType, date: String) {
        _uiState.value = _uiState.value.copy(calendarState = ResultState.Loading)

        viewModelScope.launch {
            val result = getCalendarUseCase(type, date)
            _uiState.value = _uiState.value.copy(calendarState = result.toResultState())
        }
    }
}