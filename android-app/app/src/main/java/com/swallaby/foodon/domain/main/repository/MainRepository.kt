package com.swallaby.foodon.domain.main.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage

interface MainRepository {

    suspend fun getMealRecord(day: String): ApiResult<List<MealRecord>>

    suspend fun getNutrientIntake(day: String): ApiResult<List<NutrientIntake>>

    suspend fun getNutrientManage(day: String): ApiResult<List<NutrientManage>>

}