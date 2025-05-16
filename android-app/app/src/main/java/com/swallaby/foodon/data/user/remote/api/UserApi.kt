package com.swallaby.foodon.data.user.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.user.remote.dto.request.UpdateProfileRequest
import com.swallaby.foodon.data.user.remote.dto.response.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("user")
    suspend fun getUserProfile(): BaseResponse<UserProfileResponse>

    @POST("members/profile")
    suspend fun updateUserProfile(
        @Body request: UpdateProfileRequest
    ): BaseResponse<Unit>
}