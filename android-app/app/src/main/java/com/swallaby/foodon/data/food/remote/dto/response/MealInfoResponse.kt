package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem

/*
    "imageFileName": "string",
    "totalKcal": 0,
    "totalCarbs": 0,
    "totalProtein": 0,
    "totalFat": 0,
    "mealItems": [
      {
        "type": "PUBLIC",
        "foodId": 0,
        "foodName": "string",
        "unit": "SLICE",
        "quantity": 0,
        "nutrientInfo": {
          "kcal": 0,
          "carbs": 0,
          "sugar": 0,
          "fiber": 0,
          "protein": 0,
          "fat": 0,
          "saturatedFat": 0,
          "transFat": 0,
          "fattyAcid": 0,
          "unsaturatedFat": 0,
          "cholesterol": 0,
          "sodium": 0,
          "potassium": 0,
          "alcohol": 0
        },
        "positions": [
          {
            "x": 0,
            "y": 0,
            "width": 0,
            "height": 0
          }
        ]
      }
    ]
 */
data class MealInfoResponse(
    val imageFileName: String = "",
//    val mealTime: String = "",
//    val mealTimeType: String = "",
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val totalKcal: Int = 0,
    val totalProtein: Int = 0,
    val mealItems: List<MealItem>,
)

fun MealInfoResponse.toDomain(): MealInfo = MealInfo(
    imageFileName = imageFileName,
//    mealTime = mealTime,
//    mealTimeType = mealTimeType,
    totalCarbs = totalCarbs,
    totalFat = totalFat,
    totalKcal = totalKcal,
    totalProtein = totalProtein,
    mealItems = mealItems
)