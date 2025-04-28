package com.swallaby.foodon.core.di

import com.swallaby.foodon.data.calendar.remote.repository.CalendarRepositoryImpl
import com.swallaby.foodon.data.food.remote.repository.FoodRemoteRepositoryImpl
import com.swallaby.foodon.data.user.remote.repository.UserRemoteRepositoryImpl
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import com.swallaby.foodon.domain.food.repository.FoodRepository
import com.swallaby.foodon.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRemoteRepository(
        userRepositoryImpl: UserRemoteRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRemoteRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl,
    ): CalendarRepository

    @Binds
    @Singleton
    abstract fun bindFoodRemoteRepository(
        foodRepositoryImpl: FoodRemoteRepositoryImpl,
    ): FoodRepository

}