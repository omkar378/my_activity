package com.example.mytaskflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodayTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_today_tasks)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNavigation()
        
        findViewById<View>(R.id.fabAddToday).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        findViewById<View>(R.id.btnAddTodayTask).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodayTasks()
    }

    private fun loadTodayTasks() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val tasks = TaskManager.getAllTasks().filter { it.date == today }

        val emptyState = findViewById<View>(R.id.emptyStateToday)
        val taskListScroll = findViewById<View>(R.id.todayTasksScroll)
        val container = findViewById<LinearLayout>(R.id.todayTasksContainer)
        val progressBar = findViewById<ProgressBar>(R.id.todayProgressBar)
        val tvProgressPercent = findViewById<TextView>(R.id.tvProgressPercent)

        if (tasks.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            taskListScroll.visibility = View.GONE
            progressBar.progress = 0
            tvProgressPercent.text = "0% Completed"
        } else {
            emptyState.visibility = View.GONE
            taskListScroll.visibility = View.VISIBLE
            
            container.removeAllViews()
            var completedCount = 0
            
            tasks.forEach { task ->
                if (task.status == "Completed") completedCount++
                
                val view = LayoutInflater.from(this).inflate(R.layout.item_task, container, false)
                view.findViewById<TextView>(R.id.tvTaskTitle).text = task.title
                view.findViewById<TextView>(R.id.tvTaskDesc).text = task.description
                view.findViewById<TextView>(R.id.tvPriority).text = task.priority
                view.findViewById<TextView>(R.id.tvDateTime).text = "${task.date} ${task.time}"
                
                val cbComplete = view.findViewById<CheckBox>(R.id.cbComplete)
                cbComplete.isChecked = task.status == "Completed"
                
                cbComplete.setOnCheckedChangeListener { _, isChecked ->
                    task.status = if (isChecked) "Completed" else "Pending"
                    TaskManager.updateTask(this@TodayTasksActivity)
                    updateUI(view, isChecked)
                    loadTodayTasks() // Refresh to update progress bar
                }

                updateUI(view, task.status == "Completed")

                view.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
                    TaskManager.deleteTask(this@TodayTasksActivity, task.id)
                    loadTodayTasks()
                }

                container.addView(view)
            }
            
            val percent = (completedCount.toFloat() / tasks.size * 100).toInt()
            progressBar.progress = percent
            tvProgressPercent.text = "$percent% Completed"
        }
    }

    private fun updateUI(view: View, isCompleted: Boolean) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTaskTitle)
        if (isCompleted) {
            tvTitle.paintFlags = tvTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            tvTitle.setTextColor(resources.getColor(R.color.text_secondary, theme))
        } else {
            tvTitle.paintFlags = tvTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            tvTitle.setTextColor(resources.getColor(R.color.white, theme))
        }
    }

    private fun setupBottomNavigation() {
        findViewById<View>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.navTasks).setOnClickListener {
            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.navCalendar).setOnClickListener {
            startActivity(Intent(this, DeadlinesActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.navProfile).setOnClickListener {
            Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
        }
    }
}
