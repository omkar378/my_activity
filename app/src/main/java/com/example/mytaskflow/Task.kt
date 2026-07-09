package com.example.mytaskflow

data class Task(
    val title: String,
    val description: String = "",
    val category: String, // Projects, Goals, Reminders, Analytics, Notes
    val priority: String = "Medium",
    val date: String = "",
    val time: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
