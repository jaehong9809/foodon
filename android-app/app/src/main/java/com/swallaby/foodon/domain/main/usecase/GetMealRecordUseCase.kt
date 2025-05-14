package com.swallaby.foodon.domain.main.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.repository.MainRepository
import org.threeten.bp.LocalDate
import javax.inject.Inject

class GetMealRecordUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(date: LocalDate): ApiResult<List<MealRecord>> {
        return repository.getMealRecord(date)
    }
}