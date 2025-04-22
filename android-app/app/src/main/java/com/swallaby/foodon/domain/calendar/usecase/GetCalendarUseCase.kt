package com.swallaby.foodon.domain.calendar.usecase

import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class GetCalendarUseCase @Inject constructor(
    private val repository: CalendarRepository
) {

}
