package com.swallaby.foodon.data.food.local.mapper

import com.swallaby.foodon.data.food.local.LocalFoodEntity
import com.swallaby.foodon.domain.food.model.Food

fun LocalFoodEntity.toDomain(): Food {
    return Food(
        id = this.id,
        name = this.name,
        servingUnit = this.servingUnit,
        kcal = this.kcal,
        isRegistered = this.isRegistered
    )
}

fun Food.toEntity(): LocalFoodEntity {
    return LocalFoodEntity(
        id = this.id,
        name = this.name,
        servingUnit = this.servingUnit,
        kcal = this.kcal,
        isRegistered = this.isRegistered
    )
}