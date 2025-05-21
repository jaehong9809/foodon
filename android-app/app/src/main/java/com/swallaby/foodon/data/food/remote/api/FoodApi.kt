package com.swallaby.foodon.data.food.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest
import com.swallaby.foodon.data.food.remote.dto.request.RecordMealRequest
import com.swallaby.foodon.data.food.remote.dto.response.FoodResponse
import com.swallaby.foodon.data.food.remote.dto.response.FoodSimilarResponse
import com.swallaby.foodon.data.food.remote.dto.response.MealDetailInfoResponse
import com.swallaby.foodon.data.food.remote.dto.response.MealInfoResponse
import com.swallaby.foodon.domain.food.model.FoodType
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @Multipart
    @POST("meals/detect")
    suspend fun postFoodImage(
        @Part image: MultipartBody.Part,
    ): BaseResponse<MealInfoResponse>

    @POST("meals")
    suspend fun postFoodHistory(
        @Body request: RecordMealRequest,
    ): BaseResponse<Unit>

    @POST("foods/custom")
    suspend fun postCustomFood(
        @Body request: CustomFoodRequest,
    ): BaseResponse<Unit>

    @POST("foods/custom/modified")
    suspend fun postCustomFoodUpdate(
        @Body request: CustomFoodRequest,
    ): BaseResponse<FoodResponse>

    @GET("foods/{foodId}")
    suspend fun getFood(
        @Path("foodId") foodId: Long,
        @Query("type") type: FoodType,
    ): BaseResponse<FoodResponse>

    @GET("meals/detail/{mealId}")
    suspend fun getMealDetail(
        @Path("mealId") mealId: Long,
    ): BaseResponse<MealDetailInfoResponse>

    @GET("foods/similar")
    suspend fun getFoodSimilar(
        @Query("name") name: String,
    ): BaseResponse<List<FoodSimilarResponse>>

}