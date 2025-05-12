package com.swallaby.foodon.domain.main.model

data class GoalSection(
    val title: String = "",
    val items: List<GoalInfo> = emptyList()
)
