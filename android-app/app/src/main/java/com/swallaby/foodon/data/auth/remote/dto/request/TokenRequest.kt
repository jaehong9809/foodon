package com.swallaby.foodon.data.auth.remote.dto.request

data class TokenRequest(
    val accessToken: String?,
    val refreshToken: String?
)
