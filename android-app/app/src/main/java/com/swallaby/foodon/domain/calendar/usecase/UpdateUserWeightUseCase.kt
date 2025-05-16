package com.swallaby.foodon.domain.calendar.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class UpdateUserWeightUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(weight: Int): ApiResult<Unit> {
        return repository.updateUserWeight(weight)
    }
}