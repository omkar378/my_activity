package com.example.mytaskflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DeadlinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_deadlines)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNavigation()
        updateProgressSection()
        
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        findViewById<View>(R.id.btnGeneratePlan).setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("SHOW_AI_PLANNER", true)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDeadlines()
        updateProgressSection()
    }

    private fun updateProgressSection() {
        findViewById<TextView>(R.id.tvCompletedCount).text = TaskManager.getTasksCountByStatus("Completed").toString()
        findViewById<TextView>(R.id.tvPendingCount).text = TaskManager.getTasksCountByStatus("Pending").toString()
        // Mocking due today for UI
        findViewById<TextView>(R.id.tvDueTodayCount).text = "1"
    }

    private fun loadDeadlines() {
        val tasksWithDates = TaskManager.getAllTasks()
            .filter { it.date.isNotEmpty() && it.status != "Completed" }
            .sortedBy { it.date }

        val emptyState = findViewById<View>(R.id.emptyStateDeadlines)
        val container = findViewById<LinearLayout>(R.id.deadlineListContainer)
        val tvAiText = findViewById<TextView>(R.id.tvAiText)
        val tvAiSubtitle = findViewById<TextView>(R.id.tvAiSubtitle)

        // Update AI Suggestion Text
        if (tasksWithDates.isNotEmpty()) {
            val firstTask = tasksWithDates[0].title
            val count = tasksWithDates.size
            tvAiText.text = "You have $count deadlines this week. Finish $firstTask today with the help of AI study Planner."
            tvAiSubtitle.text = "With the help of AI, you can complete your assignments and master subjects faster!"
        } else {
            tvAiText.text = "You have no upcoming deadlines. Add your subjects to stay on track."
            tvAiSubtitle.text = "Use the AI Study Planner to create a roadmap for your learning journey."
        }

        // Show real tasks or empty state
        if (tasksWithDates.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            container.removeAllViews()
        } else {
            emptyState.visibility = View.GONE
            container.removeAllViews()
            tasksWithDates.forEach { task ->
                val view = LayoutInflater.from(this).inflate(R.layout.item_deadline, container, false)
                
                view.findViewById<TextView>(R.id.tvDeadlineTitle).text = task.title
                view.findViewById<TextView>(R.id.tvDeadlineTime).text = "Due: ${task.date} • ${task.time}"
                
                val priorityBadge = view.findViewById<TextView>(R.id.tvPriorityBadge)
                priorityBadge.text = "${task.priority} Priority"
                
                when(task.priority) {
                    "High" -> priorityBadge.setTextColor(resources.getColor(R.color.status_red, theme))
                    "Medium" -> priorityBadge.setTextColor(resources.getColor(R.color.status_orange, theme))
                    else -> priorityBadge.setTextColor(resources.getColor(R.color.status_green, theme))
                }

                view.findViewById<TextView>(R.id.tvDaysLeft).text = "Upcoming"

                val btnComplete = view.findViewById<View>(R.id.btnComplete)
                val ivCheckmark = view.findViewById<View>(R.id.ivCheckmark)

                btnComplete.setOnClickListener {
                    task.status = "Completed"
                    TaskManager.updateTask(this)
                    btnComplete.visibility = View.GONE
                    ivCheckmark.visibility = View.VISIBLE
                    updateProgressSection()
                    Toast.makeText(this, "Task Completed!", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.navCalendar).setOnClickListener {
            // Already here
        }

        findViewById<View>(R.id.navProfile).setOnClickListener {
            Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
        }
    }
}
