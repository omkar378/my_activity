package com.example.mytaskflow

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TaskManager {
    private const val PREFS_NAME = "task_prefs"
    private const val TASKS_KEY = "tasks_list"
    private val tasks = mutableListOf<Task>()

    fun loadTasks(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(TASKS_KEY, null)
        tasks.clear()
        if (json != null) {
            val type = object : TypeToken<List<Task>>() {}.type
            val loadedTasks: List<Task> = Gson().fromJson(json, type)
            tasks.addAll(loadedTasks)
        }
    }

    fun saveTasks(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(tasks)
        prefs.edit().putString(TASKS_KEY, json).apply()
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
