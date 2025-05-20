package com.swallaby.foodon.domain.foodsync

interface SyncFoodsUseCase {
    suspend fun execute()
}