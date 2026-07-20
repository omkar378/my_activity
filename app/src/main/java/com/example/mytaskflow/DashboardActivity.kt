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

        findViewById<TextView>(R.id.tvGreeting).text = getString(R.string.greeting_format, UserManager.userName)

        setupStats()
        setupSections()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Subjects Card Click
        findViewById<View>(R.id.cardAddSubject).setOnClickListener {
            openTaskList("Projects")
        }

        // Deadlines Card Click
        findViewById<View>(R.id.cardAddDeadline).setOnClickListener {
            openTaskList("Reminders")
        }

        // Tasks Card Click
        findViewById<View>(R.id.cardAddTask).setOnClickListener {
            openTaskList("Tasks")
        }

        // Goals Card Click
        findViewById<View>(R.id.cardAddGoal).setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        // AI Study Planner Card Click
        findViewById<View>(R.id.aiIcon).parent?.let { parent ->
            (parent as? View)?.setOnClickListener {
                startActivity(Intent(this, AIPlannerActivity::class.java))
            }
        }
        
        // FAB Click
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        // Nav Items Click
        findViewById<View>(R.id.navHome).setOnClickListener {
            // Already here
        }
        findViewById<View>(R.id.navTasks).setOnClickListener {
            openTaskList("Tasks")
        }
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            Toast.makeText(this, "Calendar coming soon!", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Top Header Profile Click
        findViewById<View>(R.id.ivProfileHeader).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun openTaskList(category: String) {
        val intent = Intent(this, TaskListActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setupStats()
    }

    private fun setupStats() {
        // Total
        val totalView = findViewById<View>(R.id.statTotal)
        totalView.findViewById<TextView>(R.id.tvStatLabel).text = getString(R.string.stat_total)
        totalView.findViewById<TextView>(R.id.tvStatValue).text = TaskRepository.getCountTotal().toString()
        totalView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_total)

        // Completed
        val completedView = findViewById<View>(R.id.statCompleted)
        completedView.findViewById<TextView>(R.id.tvStatLabel).text = getString(R.string.stat_completed)
        completedView.findViewById<TextView>(R.id.tvStatValue).text = TaskRepository.getCountCompleted().toString()
        completedView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_completed)

        // In Progress
        val inProgressView = findViewById<View>(R.id.statInProgress)
        inProgressView.findViewById<TextView>(R.id.tvStatLabel).text = getString(R.string.stat_in_progress)
        inProgressView.findViewById<TextView>(R.id.tvStatValue).text = TaskRepository.getCountInProgress().toString()
        inProgressView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_in_progress)

        // Pending
        val pendingView = findViewById<View>(R.id.statPending)
        pendingView.findViewById<TextView>(R.id.tvStatLabel).text = getString(R.string.stat_pending)
        pendingView.findViewById<TextView>(R.id.tvStatValue).text = TaskRepository.getCountPending().toString()
        pendingView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_pending)

        // Overdue
        val overdueView = findViewById<View>(R.id.statOverdue)
        overdueView.findViewById<TextView>(R.id.tvStatLabel).text = getString(R.string.stat_overdue)
        overdueView.findViewById<TextView>(R.id.tvStatValue).text = TaskRepository.getCountOverdue().toString()
        overdueView.findViewById<ImageView>(R.id.ivStatIcon).setImageResource(R.drawable.ic_stat_overdue)
    }

    private fun setupSections() {
        // Subjects
        findViewById<View>(R.id.headerSubjects).findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.section_subjects)
        findViewById<View>(R.id.cardAddSubject).let {
            it.findViewById<TextView>(R.id.tvLabel).text = getString(R.string.add_subjects)
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_subjects)
        }

        // Deadlines
        findViewById<View>(R.id.headerDeadlines).findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.section_deadlines)
        findViewById<View>(R.id.cardAddDeadline).let {
            it.findViewById<TextView>(R.id.tvLabel).text = getString(R.string.no_deadlines)
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_deadlines)
        }

        // Tasks
        findViewById<View>(R.id.headerTasks).findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.section_tasks)
        findViewById<View>(R.id.cardAddTask).let {
            it.findViewById<TextView>(R.id.tvLabel).text = getString(R.string.all_caught_up)
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_tasks)
        }

        // Goals
        findViewById<View>(R.id.headerGoals).findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.section_goals)
        findViewById<View>(R.id.cardAddGoal).let {
            it.findViewById<TextView>(R.id.tvLabel).text = getString(R.string.set_first_goal)
            it.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.ic_section_goals)
        }
    }
}
