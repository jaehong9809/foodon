package com.swallaby.foodon.data.main.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.main.remote.dto.MealRecordResponse
import com.swallaby.foodon.data.main.remote.dto.NutrientIntakeResponse
import com.swallaby.foodon.data.main.remote.dto.NutrientManageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {

    @GET("meals/record")
    suspend fun getMealRecord(@Query("day") day: String): BaseResponse<List<MealRecordResponse>>

    @GET("nutrient/intake")
    suspend fun getNutrientIntake(@Query("day") day: String): BaseResponse<NutrientIntakeResponse>

    @GET("nutrient/manage")
    suspend fun getNutrientManage(@Query("day") day: String): BaseResponse<List<NutrientManageResponse>>

}