package com.swallaby.foodon

import android.app.Application
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import com.swallaby.foodon.data.food.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodOnApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        KakaoSdk.init(
            context = this,
            appKey = BuildConfig.KAKAO_NATIVE_APP_KEY
        )

        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "foods.db"
        )
            .createFromAsset("foods_init_final.db")
            .fallbackToDestructiveMigration()
            .build()

        Thread {
            try {
                val count = database.foodSearchDao().countFoods()
                android.util.Log.d("RoomInit", "🍽️ Food row count: $count")
            } catch (e: Exception) {
                android.util.Log.e("RoomInit", "🔥 Room DB init failed", e)
            }
        }.start()
    }
}