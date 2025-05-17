package com.swallaby.foodon.presentation.sharedstate

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealSharedState @Inject constructor() {

    private val _mealAddStatus = MutableStateFlow(false)
    val mealAddStatus: StateFlow<Boolean> = _mealAddStatus

    fun updateMealAddStatus(status: Boolean) {
        _mealAddStatus.value = status
    }

    private val _refreshDaily = MutableStateFlow(false)
    private val _refreshCalendar = MutableStateFlow(false)

    val refreshDaily: StateFlow<Boolean> = _refreshDaily
    val refreshCalendar: StateFlow<Boolean> = _refreshCalendar

    fun triggerRefreshForDaily() {
        _refreshDaily.value = true
    }

    fun triggerRefreshForCalendar() {
        _refreshCalendar.value = true
    }

    fun clearDaily() {
        _refreshDaily.value = false
    }

    fun clearCalendar() {
        _refreshCalendar.value = false
    }

}