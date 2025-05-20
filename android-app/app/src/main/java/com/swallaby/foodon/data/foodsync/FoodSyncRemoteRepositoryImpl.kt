package com.swallaby.foodon.data.foodsync

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.food.remote.api.FoodApi
import com.swallaby.foodon.domain.foodsync.FoodSyncRemoteRepository
import javax.inject.Inject

class FoodSyncRemoteRepositoryImpl @Inject constructor(
    private val api: FoodApi
) : FoodSyncRemoteRepository {
    override suspend fun getSyncFoods(sinceId: Long): ApiResult<List<FoodLocalDbResponse>> {
        TODO("Not yet implemented")
    }
}