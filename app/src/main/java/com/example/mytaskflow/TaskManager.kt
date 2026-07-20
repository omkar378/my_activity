package com.example.mytaskflow

import android.content.Context

object TaskManager {
    private val tasks = mutableListOf<Task>()
    private const val PREFS_NAME = "task_prefs"
    private const val TASKS_KEY = "tasks_list"

    fun loadTasks(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedData = prefs.getStringSet(TASKS_KEY, emptySet()) ?: emptySet()
        tasks.clear()
        savedData.forEach { data ->
            val parts = data.split("|")
            if (parts.size >= 8) {
                tasks.add(Task(
                    id = parts[0].toLong(),
                    title = parts[1],
                    description = parts[2],
                    priority = parts[4],
                    date = parts[5],
                    time = parts[6],
                    status = parts[7]
                ))
            }
        }
    }

    private fun saveTasks(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dataSet = tasks.map { 
            "${it.id}|${it.title}|${it.description}|dummy|${it.priority}|${it.date}|${it.time}|${it.status}"
        }.toSet()
        prefs.edit().putStringSet(TASKS_KEY, dataSet).apply()
    }

    fun addTask(context: Context, task: Task) {
        tasks.add(task)
        saveTasks(context)
    }

    fun updateTask(context: Context) {
        saveTasks(context)
    }

    fun deleteTask(context: Context, taskId: Long) {
        tasks.removeAll { it.id == taskId }
        saveTasks(context)
    }

    fun getAllTasks(): List<Task> = tasks

    fun getTasksCountByStatus(status: String): Int {
        return tasks.count { it.status.equals(status, ignoreCase = true) }
    }

    fun getTotalTasksCount(): Int = tasks.size
    
    fun getOverdueCount(): Int = 0
}
