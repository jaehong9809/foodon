package com.swallaby.foodon.data.main.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.main.remote.dto.MealRecordResponse
import com.swallaby.foodon.data.main.remote.dto.NutrientIntakeResponse
import com.swallaby.foodon.data.main.remote.dto.NutrientManageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {

    @GET("meals/{date}")
    suspend fun getMealRecord(@Path("date") date: String): BaseResponse<List<MealRecordResponse>>

    @GET("intake/{date}")
    suspend fun getNutrientIntake(@Path("date") date: String): BaseResponse<NutrientIntakeResponse>

    @GET("nutrient/manage")
    suspend fun getNutrientManage(@Query("date") date: String): BaseResponse<List<NutrientManageResponse>>

}