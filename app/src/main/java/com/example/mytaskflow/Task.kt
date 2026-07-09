package com.example.mytaskflow

data class Task(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String = "",
    val category: List<String> = emptyList(),
    val priority: String = "Medium",
    val date: String = "",
    val time: String = "",
    var status: String = "Pending" // Pending, In Progress, Completed
)
