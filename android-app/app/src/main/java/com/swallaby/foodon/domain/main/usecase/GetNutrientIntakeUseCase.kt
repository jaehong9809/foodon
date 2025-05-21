package com.swallaby.foodon.domain.main.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.repository.MainRepository
import org.threeten.bp.LocalDate
import javax.inject.Inject

class GetNutrientIntakeUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(date: LocalDate): ApiResult<NutrientIntake> {
        return repository.getNutrientIntake(date)
    }
}