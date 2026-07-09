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
                
                val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
                tvStatus.text = task.status
                
                // Allow clicking the task to cycle status (Pending -> In Progress -> Completed -> Pending)
                view.setOnClickListener {
                    task.status = when (task.status) {
                        "Pending" -> "In Progress"
                        "In Progress" -> "Completed"
                        else -> "Pending"
                    }
                    TaskManager.updateTask(this@TasksActivity)
                    loadTasks() // Refresh list
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

        // Current page is Tasks, so we don't need to do anything for navTasks
        
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            // startActivity(Intent(this, CalendarActivity::class.java))
        }

        findViewById<View>(R.id.navProfile).setOnClickListener {
            // startActivity(Intent(this, ProfileActivity::class.java))
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
