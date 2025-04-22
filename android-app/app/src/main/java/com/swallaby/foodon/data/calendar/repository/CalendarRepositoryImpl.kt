package com.swallaby.foodon.data.calendar.repository

import com.swallaby.foodon.data.calendar.remote.api.CalendarApi
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api: CalendarApi
): CalendarRepository {

}