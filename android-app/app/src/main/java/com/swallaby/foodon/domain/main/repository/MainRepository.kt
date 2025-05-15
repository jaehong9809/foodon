package com.swallaby.foodon.domain.main.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage
import org.threeten.bp.LocalDate

interface MainRepository {

    suspend fun getMealRecord(date: LocalDate): ApiResult<List<MealRecord>>

    suspend fun getNutrientIntake(date: LocalDate): ApiResult<NutrientIntake>

    suspend fun getNutrientManage(date: LocalDate): ApiResult<List<NutrientManage>>

}