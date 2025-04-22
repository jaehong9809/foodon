package com.swallaby.foodon.data.user.remote.dto.response

import com.swallaby.foodon.domain.user.model.User

data class UserProfileResponse(
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val userProfileImg: String
)

fun UserProfileResponse.toDomain(): User {
    return User(
        id = userId,
        name = userName,
        email = userEmail,
        profileImgUrl = userProfileImg
    )
}