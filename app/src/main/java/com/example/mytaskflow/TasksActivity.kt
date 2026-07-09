package com.example.mytaskflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupFilters()
        setupNavigation()
        setupAddActions()
    }

    private fun setupFilters() {
        val filters = listOf(
            R.id.filterAll, R.id.filterPending, 
            R.id.filterInProgress, R.id.filterCompleted,
        )
        
        // Set 'All' as default selected
        findViewById<View>(R.id.filterAll).isSelected = true

        filters.forEach { id ->
            findViewById<TextView>(id).setOnClickListener { view ->
                filters.forEach { findViewById<View>(it).isSelected = false }
                view.isSelected = true
                Toast.makeText(this, "Filter: ${(view as TextView).text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigation() {
        findViewById<View>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        findViewById<View>(R.id.navTasks).setOnClickListener {
            // Already here
        }
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            Toast.makeText(this, "Calendar selected", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.navProfile).setOnClickListener {
            Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAddActions() {
        // Main FAB
        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        // Header '+' button
        findViewById<FloatingActionButton>(R.id.fabAddTaskSmall).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        // Empty state button
        findViewById<Button>(R.id.btnEmptyAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }
    }
}
