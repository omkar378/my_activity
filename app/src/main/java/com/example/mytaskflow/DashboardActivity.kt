package com.example.mytaskflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupStats()
        setupSections()
        setupClickListeners()

        if (intent.getBooleanExtra("SHOW_AI_PLANNER", false)) {
            val aiCard = findViewById<View>(R.id.aiIcon).parent as? View
            aiCard?.let { card ->
                card.post {
                    findViewById<androidx.core.widget.NestedScrollView>(R.id.dashboard_scroll)?.smoothScrollTo(0, card.top)
                    Toast.makeText(this, "AI Study Planner is here!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Subjects Card Click
        findViewById<View>(R.id.cardAddSubject).setOnClickListener {
            Toast.makeText(this, "Opening Subject Progress...", Toast.LENGTH_SHORT).show()
        }

        // Deadlines Card Click
        findViewById<View>(R.id.cardAddDeadline).setOnClickListener {
            startActivity(Intent(this, DeadlinesActivity::class.java))
        }

        // Tasks Card Click
        findViewById<View>(R.id.cardAddTask).setOnClickListener {
            val intent = Intent(this, TodayTasksActivity::class.java)
            startActivity(intent)
        }

        // Stat Cards Clicks
        findViewById<View>(R.id.statTotal).setOnClickListener {
            openTasksWithFilter("All")
        }
        findViewById<View>(R.id.statCompleted).setOnClickListener {
            openTasksWithFilter("Completed")
        }
        findViewById<View>(R.id.statInProgress).setOnClickListener {
            openTasksWithFilter("In Progress")
        }
        findViewById<View>(R.id.statPending).setOnClickListener {
            openTasksWithFilter("Pending")
        }

        // Goals Card Click
        findViewById<View>(R.id.cardAddGoal).setOnClickListener {
            Toast.makeText(this, "Opening Study Goals...", Toast.LENGTH_SHORT).show()
        }

        // AI Study Planner Card Click
        findViewById<View>(R.id.aiIcon).parent?.let { parent ->
            (parent as? View)?.setOnClickListener {
                startActivity(Intent(this, CalendarActivity::class.java))
            }
        }
        
        // FAB Click
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        // Nav Items Click
        findViewById<View>(R.id.navHome).setOnClickListener {
            // Already home
        }
        findViewById<View>(R.id.navTasks).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
        findViewById<View>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun openTasksWithFilter(filter: String) {
        val intent = Intent(this, TasksActivity::class.java)
        intent.putExtra("FILTER", filter)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setupStats()
    }

    private fun setupStats() {
        // Update the top right counter
        findViewById<TextView>(R.id.tvHeaderTaskCount).text = TaskManager.getTotalTasksCount().toString()
        
        // Total
        val totalView = findViewById<View>(R.id.statTotal)
        totalView.findViewById<TextView>(R.id.tvStatLabel).text = "Total"
        totalView.findViewById<TextView>(R.id.tvStatValue).text = TaskManager.getTotalTasksCount().toString()
        totalView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_total)

        // Completed
        val completedView = findViewById<View>(R.id.statCompleted)
        completedView.findViewById<TextView>(R.id.tvStatLabel).text = "Completed"
        completedView.findViewById<TextView>(R.id.tvStatValue).text = TaskManager.getTasksCountByStatus("Completed").toString()
        completedView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_completed)

        // In Progress
        val inProgressView = findViewById<View>(R.id.statInProgress)
        inProgressView.findViewById<TextView>(R.id.tvStatLabel).text = "In Progress"
        inProgressView.findViewById<TextView>(R.id.tvStatValue).text = TaskManager.getTasksCountByStatus("In Progress").toString()
        inProgressView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_in_progress)

        // Pending
        val pendingView = findViewById<View>(R.id.statPending)
        pendingView.findViewById<TextView>(R.id.tvStatLabel).text = "Pending"
        pendingView.findViewById<TextView>(R.id.tvStatValue).text = TaskManager.getTasksCountByStatus("Pending").toString()
        pendingView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_pending)

        // Overdue
        val overdueView = findViewById<View>(R.id.statOverdue)
        overdueView.findViewById<TextView>(R.id.tvStatLabel).text = "Overdue"
        overdueView.findViewById<TextView>(R.id.tvStatValue).text = TaskManager.getOverdueCount().toString()
        overdueView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_overdue)
    }

    private fun setupSections() {
        // Subjects
        findViewById<View>(R.id.headerSubjects).findViewById<TextView>(R.id.tvSectionTitle).text = "Subject Progress"
        findViewById<View>(R.id.cardAddSubject).let {
            it.findViewById<TextView>(R.id.tvLabel).text = "Add your subjects"
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_subjects)
        }

        // Deadlines
        findViewById<View>(R.id.headerDeadlines).findViewById<TextView>(R.id.tvSectionTitle).text = "Upcoming Deadlines"
        findViewById<View>(R.id.cardAddDeadline).let {
            it.findViewById<TextView>(R.id.tvLabel).text = "No upcoming deadlines"
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_deadlines)
        }

        // Tasks
        findViewById<View>(R.id.headerTasks).findViewById<TextView>(R.id.tvSectionTitle).text = "Today\'s Tasks"
        findViewById<View>(R.id.cardAddTask).let {
            it.findViewById<TextView>(R.id.tvLabel).text = "You\'re all caught up!"
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_tasks)
        }

        // Goals
        findViewById<View>(R.id.headerGoals).findViewById<TextView>(R.id.tvSectionTitle).text = "Study Goals"
        findViewById<View>(R.id.cardAddGoal).let {
            it.findViewById<TextView>(R.id.tvLabel).text = "Set your first goal"
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_goals)
        }
    }
}
