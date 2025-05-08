package com.swallaby.foodon.data.food.local.mapper

import com.swallaby.foodon.core.util.generateSearchTokens
import com.swallaby.foodon.data.food.local.LocalFoodEntity
import com.swallaby.foodon.domain.food.model.Food

fun LocalFoodEntity.toDomain(): Food {
    return Food(
        id = this.foodId,
        name = this.name,
        servingUnit = this.servingUnit,
        kcal = this.kcal,
        isCustom = this.isCustom
    )
}

fun Food.toEntity(): LocalFoodEntity {
    return LocalFoodEntity(
        foodId = this.id,
        name = this.name,
        servingUnit = this.servingUnit,
        kcal = this.kcal,
        isCustom = this.isCustom,
        searchTokens = this.name.generateSearchTokens()
    )
}