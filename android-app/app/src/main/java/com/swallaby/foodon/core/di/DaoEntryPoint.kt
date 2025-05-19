package com.swallaby.foodon.core.di

import com.swallaby.foodon.data.food.local.FoodSearchDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DaoEntryPoint {
    fun foodSearchDao(): FoodSearchDao
}