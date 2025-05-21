package com.swallaby.foodon.domain.calendar.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.calendar.model.UserWeight
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class GetUserWeightUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(): ApiResult<UserWeight> {
        return repository.getUserWeight()
    }
}