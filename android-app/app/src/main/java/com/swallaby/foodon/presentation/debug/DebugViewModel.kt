package com.swallaby.foodon.presentation.debug

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.util.generateSearchTokens
import com.swallaby.foodon.data.food.local.FoodSearchDao
import com.swallaby.foodon.data.food.local.LocalFoodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val dao: FoodSearchDao
) : ViewModel() {

    val foodNames = listOf(
        "김치찌개", "된장찌개", "불고기", "비빔밥", "갈비탕", "삼계탕", "잡채", "떡볶이",
        "순두부찌개", "김밥", "라면", "칼국수", "수제비", "만두", "냉면", "쭈꾸미볶음",
        "닭갈비", "닭볶음탕", "족발", "보쌈", "오징어볶음", "제육볶음", "소불고기",
        "김치전", "부추전", "파전", "해물파전", "계란찜", "계란말이", "오므라이스",
        "카레라이스", "참치김밥", "치즈김밥", "스팸김밥", "치킨", "피자", "햄버거",
        "스파게티", "샐러드", "샌드위치", "우동", "돈까스", "탕수육", "깐풍기", "마라탕",
        "양꼬치", "훠궈", "샤브샤브", "곱창"
    )


    suspend fun insertDummyFoods(dao: FoodSearchDao) {
        val random = java.util.Random()
        val start = System.currentTimeMillis()
        for (i in 1..500) {
            val name = foodNames[random.nextInt(foodNames.size)] + i
            val entity = LocalFoodEntity(
                foodId = i.toLong(),
                name = name,
                servingUnit = "1인분",
                kcal = random.nextInt(500) + 100,
                isCustom = (i % 10 == 0) // i%10 == 0 일 때 true
            )
            val rowId = dao.insertFood(entity)
            dao.insertFoodFts(rowId, name, name.generateSearchTokens())
        }
        val end = System.currentTimeMillis()

        Log.d("RoomDB Dummy Insert Time for 500 rows", "소요 시간: ${end - start} ms")
    }

    private suspend fun clearAllFoods(dao: FoodSearchDao) {
        dao.clearAll()
        dao.clearAllFts()
    }

    fun insertDummyData() {
        viewModelScope.launch {
            insertDummyFoods(dao)
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            clearAllFoods(dao)
        }
    }
}
