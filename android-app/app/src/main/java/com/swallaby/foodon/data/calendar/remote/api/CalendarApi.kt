package com.swallaby.foodon.data.calendar.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.calendar.remote.dto.MealResponse
import com.swallaby.foodon.data.calendar.remote.dto.RecommendFoodResponse
import com.swallaby.foodon.data.calendar.remote.dto.RecommendationResponse
import com.swallaby.foodon.data.calendar.remote.dto.UserWeightResponse
import com.swallaby.foodon.data.calendar.remote.dto.WeightResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarApi {

    @GET("intake/calendar")
    suspend fun getCalendarMeals(@Query(value = "yearMonth") date: String): BaseResponse<List<MealResponse>>

    @GET("members/weights/calendar/{yearMonth}")
    suspend fun getCalendarWeights(@Path("yearMonth") date: String): BaseResponse<List<WeightResponse>>

    @GET("members/profile/weight")
    suspend fun getUserWeight(): BaseResponse<UserWeightResponse>

    @GET("calendar/recommendations")
    suspend fun getCalendarRecommendations(@Query(value = "date") date: String): BaseResponse<List<RecommendationResponse>>

    @GET("recommendations")
    suspend fun getRecommendFoods(
        @Query(value = "yearMonth") yearMonth: String,
        @Query(value = "week") week: Int?
    ): BaseResponse<List<RecommendFoodResponse>>

}