package com.example.mytaskflow

data class Task(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String = "",
    var category: String = "Other", // Projects, Goals, Reminders, Analytics, Notes
    val priority: String = "Medium",
    val date: String = "",
    val time: String = "",
    var status: String = "Pending", // Pending, In Progress, Completed
    var progress: Int = 0,
    var target: String = "",
    var color: Int = 0
)
