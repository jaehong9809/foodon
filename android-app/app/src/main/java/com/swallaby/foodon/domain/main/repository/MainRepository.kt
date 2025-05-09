package com.swallaby.foodon.domain.main.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage

interface MainRepository {

    suspend fun getMealRecord(date: String): ApiResult<List<MealRecord>>

    suspend fun getNutrientIntake(date: String): ApiResult<NutrientIntake>

    suspend fun getNutrientManage(date: String): ApiResult<List<NutrientManage>>

}