package com.swallaby.foodon.domain.main.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.main.model.GoalManage
import com.swallaby.foodon.domain.main.repository.MainRepository
import javax.inject.Inject

class GetGoalManageUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(): ApiResult<GoalManage> {
        return repository.getGoalManage()
    }
}