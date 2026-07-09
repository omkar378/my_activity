package com.example.mytaskflow

object TaskRepository {
    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTasksByCategory(category: String): List<Task> {
        return tasks.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun getAllTasks(): List<Task> = tasks
}
