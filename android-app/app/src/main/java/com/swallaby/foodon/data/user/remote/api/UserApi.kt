package com.swallaby.foodon.data.user.remote.api

import com.swallaby.foodon.data.user.remote.dto.response.UserProfileResponse
import retrofit2.http.GET

interface UserApi {
    @GET("/user")
    suspend fun getUserProfile(): UserProfileResponse
}