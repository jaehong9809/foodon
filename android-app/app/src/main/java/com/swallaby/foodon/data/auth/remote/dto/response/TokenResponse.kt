package com.swallaby.foodon.data.auth.remote.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val profileUpdated: Boolean
)
