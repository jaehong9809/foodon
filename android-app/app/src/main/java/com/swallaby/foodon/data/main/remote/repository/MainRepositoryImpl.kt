package com.swallaby.foodon.data.main.remote.repository

import com.swallaby.foodon.core.data.remote.getOrThrow
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.safeApiCall
import com.swallaby.foodon.data.main.remote.api.MainApi
import com.swallaby.foodon.data.main.remote.dto.toDomain
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.domain.main.repository.MainRepository
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi
): MainRepository {
    override suspend fun getMealRecord(date: LocalDate): ApiResult<List<MealRecord>> = safeApiCall {
        api.getMealRecord(date.toString()).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getNutrientIntake(date: LocalDate): ApiResult<NutrientIntake> = safeApiCall {
        api.getNutrientIntake(date.toString()).getOrThrow { it.toDomain() }
    }

    override suspend fun getNutrientManage(date: LocalDate): ApiResult<List<NutrientManage>> = safeApiCall {
        api.getNutrientManage(date.toString()).getOrThrow { it.map { data -> data.toDomain() } }
    }

}