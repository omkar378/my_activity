package com.example.mytaskflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.view.LayoutInflater
import android.widget.LinearLayout

import android.widget.CheckBox
import android.widget.ImageView

class TasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks)

        currentFilter = intent.getStringExtra("FILTER") ?: "All"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNavigation()
        setupChips()
        
        // Set default selected chip based on filter
        val initialChip = when (currentFilter) {
            "Pending" -> findViewById<TextView>(R.id.chipPending)
            "In Progress" -> findViewById<TextView>(R.id.chipInProgress)
            "Completed" -> findViewById<TextView>(R.id.chipCompleted)
            else -> findViewById<TextView>(R.id.chipAll)
        }
        selectChip(initialChip)
        
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        findViewById<View>(R.id.btnAddFirstTask).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }
    }

    private var currentFilter = "All"

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        var tasks = TaskManager.getAllTasks()
        if (currentFilter != "All") {
            tasks = tasks.filter { it.status.equals(currentFilter, ignoreCase = true) }
        }

        val emptyState = findViewById<View>(R.id.emptyState)
        val taskListScroll = findViewById<View>(R.id.taskListScroll)
        val container = findViewById<LinearLayout>(R.id.taskListContainer)

        if (tasks.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            taskListScroll.visibility = View.GONE
            findViewById<TextView>(R.id.tvNoTasks).text = if (currentFilter == "All") "No tasks here." else "No $currentFilter tasks."
        } else {
            emptyState.visibility = View.GONE
            taskListScroll.visibility = View.VISIBLE
            
            container.removeAllViews()
            tasks.forEach { task ->
                val view = LayoutInflater.from(this).inflate(R.layout.item_task, container, false)
                view.findViewById<TextView>(R.id.tvTaskTitle).text = task.title
                view.findViewById<TextView>(R.id.tvTaskDesc).text = task.description
                view.findViewById<TextView>(R.id.tvPriority).text = task.priority
                view.findViewById<TextView>(R.id.tvDateTime).text = "${task.date} ${task.time}"
                
                val cbComplete = view.findViewById<CheckBox>(R.id.cbComplete)
                cbComplete.isChecked = task.status == "Completed"
                
                // Checkbox to mark as completed
                cbComplete.setOnCheckedChangeListener { _, isChecked ->
                    task.status = if (isChecked) "Completed" else "Pending"
                    TaskManager.updateTask(this@TasksActivity)
                    
                    // Strike through text if completed
                    if (isChecked) {
                        view.findViewById<TextView>(R.id.tvTaskTitle).paintFlags = 
                            view.findViewById<TextView>(R.id.tvTaskTitle).paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                        view.findViewById<TextView>(R.id.tvTaskTitle).setTextColor(resources.getColor(R.color.text_secondary, theme))
                    } else {
                        view.findViewById<TextView>(R.id.tvTaskTitle).paintFlags = 
                            view.findViewById<TextView>(R.id.tvTaskTitle).paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        view.findViewById<TextView>(R.id.tvTaskTitle).setTextColor(resources.getColor(R.color.white, theme))
                    }

                    // If we are in a filtered view, we might want to refresh immediately
                    if (currentFilter != "All") {
                        loadTasks()
                    }
                }

                // Initial strike through check
                if (task.status == "Completed") {
                    view.findViewById<TextView>(R.id.tvTaskTitle).paintFlags = 
                        view.findViewById<TextView>(R.id.tvTaskTitle).paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    view.findViewById<TextView>(R.id.tvTaskTitle).setTextColor(resources.getColor(R.color.text_secondary, theme))
                }

                // Delete Task
                view.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
                    TaskManager.deleteTask(this@TasksActivity, task.id)
                    loadTasks()
                }

                container.addView(view)
            }
        }
    }

    private fun setupBottomNavigation() {
        findViewById<View>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.navTasks).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
            finish()
        }
        
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    private fun setupChips() {
        val chips = listOf(
            findViewById<TextView>(R.id.chipAll),
            findViewById<TextView>(R.id.chipPending),
            findViewById<TextView>(R.id.chipInProgress),
            findViewById<TextView>(R.id.chipCompleted)
        )

        chips.forEach { chip ->
            chip.setOnClickListener {
                selectChip(chip)
                currentFilter = chip.text.toString()
                loadTasks()
            }
        }
    }

    private fun selectChip(chip: TextView) {
        val chips = listOf(
            findViewById<TextView>(R.id.chipAll),
            findViewById<TextView>(R.id.chipPending),
            findViewById<TextView>(R.id.chipInProgress),
            findViewById<TextView>(R.id.chipCompleted)
        )
        chips.forEach { 
            it.isSelected = false
            it.setTextColor(resources.getColor(R.color.text_secondary, theme)) 
        }
        chip.isSelected = true
        chip.setTextColor(resources.getColor(R.color.white, theme))
    }
}
