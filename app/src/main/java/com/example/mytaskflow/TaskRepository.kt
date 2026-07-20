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

    fun deleteTask(task: Task) {
        tasks.remove(task)
    }

    fun updateTaskProgress(task: Task, delta: Int) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            val updatedTask = tasks[index]
            val newProgress = (updatedTask.progress + delta).coerceIn(0, 100)
            updatedTask.progress = newProgress
        }
    }
}
