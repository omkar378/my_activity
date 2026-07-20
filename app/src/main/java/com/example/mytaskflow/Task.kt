package com.example.mytaskflow

data class Task(
    val title: String,
    val description: String = "",
    val category: String, // Projects, Goals, Reminders, Analytics, Notes
    val priority: String = "Medium",
    val date: String = "",
    val time: String = "",
    var progress: Int = 0,
    val target: String = "",
    val color: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
