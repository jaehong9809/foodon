package com.swallaby.foodon.data.food.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.food.remote.dto.request.MealInfoRequest
import com.swallaby.foodon.data.food.remote.dto.response.MealInfoResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FoodApi {
    @Multipart
    @POST(POST_FOOD_IMAGE)
    suspend fun postFoodImage(
        @Part image: MultipartBody.Part,
    ): BaseResponse<MealInfoResponse>

    @POST(POST_FOOD_HISTORY)
    suspend fun postFoodHistory(
        @Body request: MealInfoRequest,
    ): BaseResponse<Unit>

    @POST(POST_FOOD)
    suspend fun postFood(): BaseResponse<MealInfoResponse>

    @GET(GET_FOOD)
    suspend fun getFood(
        @Query("foodId") foodId: Long,
    ): BaseResponse<MealInfoResponse>


    companion object {
        private const val POST_FOOD_IMAGE = "/image"
        private const val POST_FOOD_HISTORY = "/meals"
        private const val POST_FOOD = "/custom"
        private const val GET_FOOD = "/foods/{foodId}"
    }
}