package com.swallaby.foodon.data.calendar.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.calendar.dto.MealResponse
import com.swallaby.foodon.data.calendar.dto.RecommendationResponse
import com.swallaby.foodon.data.calendar.dto.WeightResponse
import retrofit2.http.GET

interface CalendarApi {

    @GET("calendar/meals")
    suspend fun getCalendarMeals(date: String): BaseResponse<List<MealResponse>>

    @GET("calendar/weight")
    suspend fun getCalendarWeights(date: String): BaseResponse<List<WeightResponse>>

    @GET("calendar/recommendations")
    suspend fun getCalendarRecommendations(date: String): BaseResponse<List<RecommendationResponse>>

}