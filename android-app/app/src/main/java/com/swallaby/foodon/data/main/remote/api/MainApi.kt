package com.swallaby.foodon.data.main.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.main.remote.dto.GoalManageResponse
import com.swallaby.foodon.data.main.remote.dto.MealRecordResponse
import com.swallaby.foodon.data.main.remote.dto.NutrientIntakeResponse
import com.swallaby.foodon.data.main.remote.dto.NutrientManageResponse
import org.threeten.bp.LocalDate
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {

    @GET("meals/{date}")
    suspend fun getMealRecord(@Path("date") date: LocalDate): BaseResponse<List<MealRecordResponse>>

    @GET("intake/{date}")
    suspend fun getNutrientIntake(@Path("date") date: LocalDate): BaseResponse<NutrientIntakeResponse>

    @GET("meals/manage-nutrient/{date}")
    suspend fun getNutrientManage(@Path("date") date: LocalDate): BaseResponse<List<NutrientManageResponse>>

    @GET("members/profile/goal-management")
    suspend fun getGoalManage(): BaseResponse<GoalManageResponse>
}