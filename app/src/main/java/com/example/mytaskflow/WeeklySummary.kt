package com.example.mytaskflow

data class WeeklySummary(
    val dateRange: String,
    val completedCount: Int,
    val totalCount: Int,
    val badgeName: String,
    val badgeColorRes: Int,
    val badgeIconRes: Int
)
