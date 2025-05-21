package com.swallaby.foodon.domain.user.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val profileImgUrl: String
)
