package com.example.mytaskflow

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val category = intent.getStringExtra("CATEGORY") ?: "Tasks"
        findViewById<TextView>(R.id.tvCategoryTitle).text = category

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        val rvTasks = findViewById<RecyclerView>(R.id.rvTasks)
        val tvNoTasks = findViewById<TextView>(R.id.tvNoTasks)

        val filteredTasks = TaskRepository.getTasksByCategory(category)

        if (filteredTasks.isEmpty()) {
            tvNoTasks.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            tvNoTasks.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
            rvTasks.layoutManager = LinearLayoutManager(this)
            rvTasks.adapter = TaskAdapter(filteredTasks)
        }
    }
}
