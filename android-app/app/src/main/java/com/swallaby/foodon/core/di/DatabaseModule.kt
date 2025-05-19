package com.swallaby.foodon.core.di

import android.app.Application
import androidx.room.Room
import com.swallaby.foodon.data.food.local.AppDatabase
import com.swallaby.foodon.data.food.local.FoodSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "foods.db")
            .createFromAsset("foods_init_final.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFoodSearchDao(db: AppDatabase): FoodSearchDao {
        return db.foodSearchDao()
    }
}
