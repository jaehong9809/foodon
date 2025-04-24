package com.swallaby.foodon.core.network

import com.swallaby.foodon.data.calendar.api.CalendarApi
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

    @Provides
    @Singleton
    fun provideUserApi(@NetworkModule.MainRetrofit retrofit: Retrofit)
            : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCalendarApi(@NetworkModule.MainRetrofit retrofit: Retrofit)
            : CalendarApi {
        return retrofit.create(CalendarApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFoodApi(@NetworkModule.MainRetrofit retrofit: Retrofit)
            : FoodApi =
        retrofit.create(FoodApi::class.java)

}
