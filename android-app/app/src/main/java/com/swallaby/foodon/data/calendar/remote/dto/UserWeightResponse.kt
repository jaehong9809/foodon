package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.UserWeight

data class UserWeightResponse(
    val goalWeight: Int = 0,
    val currentWeight: Int = 0
)

fun UserWeightResponse.toDomain(): UserWeight {
    return UserWeight(
        goalWeight = this.goalWeight,
        currentWeight = this.currentWeight
    )
}