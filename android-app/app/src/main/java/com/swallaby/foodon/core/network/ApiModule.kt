package com.swallaby.foodon.core.network

import com.swallaby.foodon.data.calendar.remote.api.CalendarApi
import com.swallaby.foodon.data.food.remote.api.FoodApi
import com.swallaby.foodon.data.user.remote.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private inline fun <reified T> createApi(retrofit: Retrofit): T =
        retrofit.create(T::class.java)

    @Provides
    @Singleton
    fun provideUserApi(@NetworkModule.MainRetrofit retrofit: Retrofit): UserApi =
        createApi(retrofit)

    @Provides
    @Singleton
    fun provideCalendarApi(@NetworkModule.MainRetrofit retrofit: Retrofit): CalendarApi =
        createApi(retrofit)

    @Provides
    @Singleton
    fun provideFoodApi(@NetworkModule.MainRetrofit retrofit: Retrofit): FoodApi =
        createApi(retrofit)
}