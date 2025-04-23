package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.calendar.model.CalendarType
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
            val result = getCalendarUseCase(type, date)
            updateState { it.copy(calendarState = result.toResultState()) }
        }
    }

    fun selectDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
    }

    fun updateState(block: (CalendarUiState) -> CalendarUiState) {
        _uiState.update(block)
    }

}