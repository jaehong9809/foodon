package com.swallaby.foodon.domain.main.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.repository.MainRepository
import javax.inject.Inject

class GetNutrientIntakeUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(day: String): ApiResult<List<NutrientIntake>> {
        return repository.getNutrientIntake(day)
    }
}