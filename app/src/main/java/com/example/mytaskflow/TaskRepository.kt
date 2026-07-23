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

    fun getCountTotal(): Int = tasks.size
    fun getCountCompleted(): Int = tasks.count { it.progress == 100 }
    fun getCountInProgress(): Int = tasks.count { it.progress in 1..99 }
    fun getCountPending(): Int = tasks.count { it.progress == 0 }
    fun getCountOverdue(): Int = 0 // Placeholder for now

    fun deleteTask(task: Task) {
        tasks.remove(task)
    }

    fun updateTaskProgress(task: Task, delta: Int) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            val updatedTask = tasks[index]
            updatedTask.progress = (updatedTask.progress + delta).coerceIn(0, 100)
        }
    }
}
