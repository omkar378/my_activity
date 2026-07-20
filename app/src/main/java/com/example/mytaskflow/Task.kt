package com.example.mytaskflow

data class Task(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String = "",
    val category: String = "Tasks",
    val priority: String = "Medium",
    val date: String = "",
    val time: String = "",
    var status: String = "Pending",
    var progress: Int = 0,
    var target: String = "",
    var color: Int = 0
)
