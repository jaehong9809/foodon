package com.swallaby.foodon.domain.auth.repository


interface AuthRepository {
    suspend fun loginWithKakao() : Boolean
}