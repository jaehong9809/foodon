package com.swallaby.foodon.data.user.remote.dto.request

data class UpdateProfileRequest(
    val gender: String,
    val managementType: Int,
    val activityType: Int,
    val height: Int,
    val weight: Int,
    val goalWeight: Int
)
