package com.example.mytaskflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Toast
import android.widget.TextView
import android.view.View

import android.content.Intent

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.profile)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
        setupBottomNav()
        setupUserDetails()
    }

    private fun setupUserDetails() {
        findViewById<TextView>(R.id.tvProfileName).text = UserManager.userName
        findViewById<TextView>(R.id.tvProfileEmail).text = UserManager.userEmail
        
        // Update the circular initial
        val initial = if (UserManager.userName.isNotEmpty()) UserManager.userName[0].uppercase() else "?"
        findViewById<TextView>(R.id.tvProfileInitial).text = initial
    }

    private fun setupBottomNav() {
        // Home
        findViewById<View>(R.id.navHome).setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Tasks, Calendar
        findViewById<View>(R.id.navTasks).setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("CATEGORY", "Tasks")
            startActivity(intent)
        }
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            Toast.makeText(this, "Calendar coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Profile (Current)
        findViewById<View>(R.id.navProfile).setOnClickListener {
            // Already here
        }

        // FAB Add
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.menu_projects).setOnClickListener {
            openTaskList("Projects")
        }
        findViewById<View>(R.id.menu_goals).setOnClickListener {
            openTaskList("Goals")
        }
        findViewById<View>(R.id.menu_reminders).setOnClickListener {
            openTaskList("Reminders")
        }
        findViewById<View>(R.id.menu_notes).setOnClickListener {
            openTaskList("Notes")
        }

        findViewById<View>(R.id.btnSignOut).setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        // Clear user data
        UserManager.setUser("Guest", "guest@example.com")
        
        // Navigate to MainActivity and clear task stack
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun openTaskList(category: String) {
        val intent = Intent(this, TaskListActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
}
