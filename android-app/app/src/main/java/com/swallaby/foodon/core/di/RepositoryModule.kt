package com.swallaby.foodon.core.di

import com.swallaby.foodon.data.auth.remote.repository.AuthRemoteRepositoryImpl
import com.swallaby.foodon.data.calendar.remote.repository.CalendarRepositoryImpl
import com.swallaby.foodon.data.food.local.repository.FoodSearchLocalRepositoryImpl
import com.swallaby.foodon.data.food.remote.repository.FoodRemoteRepositoryImpl
import com.swallaby.foodon.data.main.remote.repository.MainRepositoryImpl
import com.swallaby.foodon.data.user.remote.repository.UserRemoteRepositoryImpl
import com.swallaby.foodon.domain.auth.repository.AuthRepository
import com.swallaby.foodon.domain.calendar.repository.CalendarRepository
import com.swallaby.foodon.domain.food.repository.FoodRepository
import com.swallaby.foodon.domain.food.repository.FoodSearchRepository
import com.swallaby.foodon.domain.main.repository.MainRepository
import com.swallaby.foodon.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    // Remote
    @Binds
    @Singleton
    abstract fun bindUserRemoteRepository(
        userRepositoryImpl: UserRemoteRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRemoteRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl
    ): CalendarRepository

    @Binds
    @Singleton
    abstract fun bindFoodRemoteRepository(
        foodRepositoryImpl: FoodRemoteRepositoryImpl
    ): FoodRepository

    @Binds
    @Singleton
    abstract fun bindAuthRemoteRepository(
        authRemoteRepositoryImpl: AuthRemoteRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMainRemoteRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    // Local
    @Binds
    @Singleton
    abstract fun bindFoodSearchLocalRepository(
        foodSearchLocalRepositoryImpl: FoodSearchLocalRepositoryImpl
    ) : FoodSearchRepository

}