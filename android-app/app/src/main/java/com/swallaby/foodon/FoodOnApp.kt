package com.swallaby.foodon

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import com.swallaby.foodon.core.util.generateSearchTokens
import com.swallaby.foodon.data.food.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("RoomInit", "🚀 FTS 생성 작업 시작")
            val dao = database.foodSearchDao()
            dao.rebuildFts(dao.getAllFoods())
            Log.d("RoomInit", "✅ FTS 테이블 Transaction 재생성 완료")
        }
    }
}