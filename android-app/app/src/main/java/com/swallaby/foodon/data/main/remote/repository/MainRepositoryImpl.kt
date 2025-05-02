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
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi
): MainRepository {
    override suspend fun getMealRecord(day: String): ApiResult<List<MealRecord>> = safeApiCall {
        api.getMealRecord(day).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getNutrientIntake(day: String): ApiResult<List<NutrientIntake>> = safeApiCall {
        api.getNutrientIntake(day).getOrThrow { it.map { data -> data.toDomain() } }
    }

    override suspend fun getNutrientManage(day: String): ApiResult<List<NutrientManage>> = safeApiCall {
        api.getNutrientManage(day).getOrThrow { it.map { data -> data.toDomain() } }
    }

}