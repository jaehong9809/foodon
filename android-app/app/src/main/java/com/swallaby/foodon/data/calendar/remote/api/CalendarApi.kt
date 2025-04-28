package com.swallaby.foodon.data.calendar.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.calendar.remote.dto.MealResponse
import com.swallaby.foodon.data.calendar.remote.dto.RecommendFoodResponse
import com.swallaby.foodon.data.calendar.remote.dto.RecommendationResponse
import com.swallaby.foodon.data.calendar.remote.dto.UserWeightResponse
import com.swallaby.foodon.data.calendar.remote.dto.WeightResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApi {

    @GET("calendar/meals")
    suspend fun getCalendarMeals(@Query(value = "date") date: String): BaseResponse<List<MealResponse>>

    @GET("calendar/weight")
    suspend fun getCalendarWeights(date: String): BaseResponse<List<WeightResponse>>

    @GET("calendar/recommendations")
    suspend fun getCalendarRecommendations(date: String): BaseResponse<List<RecommendationResponse>>

    @GET("members/profile/weight")
    suspend fun getUserWeight(): BaseResponse<UserWeightResponse>

    @GET("recommendations")
    suspend fun getRecommendFoods(yearMonth: String, week: Int?): BaseResponse<List<RecommendFoodResponse>>

}