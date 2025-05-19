package com.swallaby.foodon

import android.app.Application
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import com.swallaby.foodon.core.di.DaoEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class FoodOnApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        KakaoSdk.init(
            context = this,
            appKey = BuildConfig.KAKAO_NATIVE_APP_KEY
        )

        CoroutineScope(Dispatchers.IO).launch {
            val dao = EntryPointAccessors.fromApplication(this@FoodOnApp, DaoEntryPoint::class.java)
                .foodSearchDao()

            Log.d("Room Init", "🚀 FTS 생성 작업 시작")
            dao.rebuildFts(dao.getAllFoods())
            Log.d("Room Init", "✅ FTS 테이블 Transaction 재생성 완료")
        }
    }
}