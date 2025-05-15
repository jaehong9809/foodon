package com.swallaby.foodon.data.calendar.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.calendar.remote.dto.MealResponse
import com.swallaby.foodon.data.calendar.remote.dto.RecommendFoodResponse
import com.swallaby.foodon.data.calendar.remote.dto.RecommendationResponse
import com.swallaby.foodon.data.calendar.remote.dto.UserWeightResponse
import com.swallaby.foodon.data.calendar.remote.dto.WeightResponse
import org.threeten.bp.YearMonth
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarApi {

    @GET("intake/calendar")
    suspend fun getCalendarMeals(@Query(value = "yearMonth") yearMonth: YearMonth): BaseResponse<List<MealResponse>>

    @GET("members/weights/calendar/{yearMonth}")
    suspend fun getCalendarWeights(@Path("yearMonth") date: YearMonth): BaseResponse<List<WeightResponse>>

    @GET("members/profile/weight")
    suspend fun getUserWeight(): BaseResponse<UserWeightResponse>

    @GET("meals/calendar/recommendations")
    suspend fun getCalendarRecommendations(@Query(value = "yearMonth") yearMonth: YearMonth): BaseResponse<List<RecommendationResponse>>

    @GET("recommend-foods")
    suspend fun getRecommendFoods(
        @Query(value = "yearMonth") yearMonth: YearMonth,
        @Query(value = "week") week: Int?
    ): BaseResponse<List<RecommendFoodResponse>>

}