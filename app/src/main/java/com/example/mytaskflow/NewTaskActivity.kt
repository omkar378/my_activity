package com.example.mytaskflow

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class NewTaskActivity : AppCompatActivity() {

    private lateinit var tvDueDate: TextView
    private lateinit var etTime: EditText
    private var selectedDate: String = ""
    private var selectedCategory: String = "Other"
    private var selectedPriority: String = "Medium"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_task)

        val root = findViewById<View>(R.id.newTaskRoot)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Close button
        findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            finish()
        }

        // Category Selection (Single selection for simplicity in filtering)
        val categories = listOf(
            R.id.catProjects, R.id.catGoals, R.id.catReminders, 
            R.id.catNotes, R.id.catAnalytics, R.id.catOther
        )
        categories.forEach { id ->
            findViewById<TextView>(id).setOnClickListener { view ->
                categories.forEach { findViewById<View>(it).isSelected = false }
                view.isSelected = true
                selectedCategory = (view as TextView).text.toString()
            }
        }
        // Set default category
        findViewById<View>(R.id.catOther).isSelected = true

        // Priority Selection (Exclusive)
        val priorities = listOf(R.id.priLow, R.id.priMedium, R.id.priHigh)
        priorities.forEach { id ->
            findViewById<TextView>(id).setOnClickListener { view ->
                priorities.forEach { findViewById<View>(it).isSelected = false }
                view.isSelected = true
                selectedPriority = (view as TextView).text.toString()
            }
        }
        // Set default priority
        findViewById<View>(R.id.priMedium).isSelected = true

        // Date Picker
        tvDueDate = findViewById(R.id.tvDueDate)
        tvDueDate.setOnClickListener {
            showDatePicker()
        }

        // Time Picker (Optional)
        etTime = findViewById(R.id.etTime)
        etTime.setOnClickListener {
            showTimePicker()
        }
        // Also allow manual entry but clicking opens picker
        etTime.isFocusable = false 

        // Create Task
        findViewById<Button>(R.id.btnCreateTask).setOnClickListener {
            val title = findViewById<EditText>(R.id.etTaskTitle).text.toString()
            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            val timeValue = etTime.text.toString()

            if (title.isNotEmpty()) {
                val newTask = Task(
                    title = title,
                    description = description,
                    category = selectedCategory,
                    priority = selectedPriority,
                    date = selectedDate,
                    time = timeValue
                )
                TaskRepository.addTask(newTask)
                Toast.makeText(this, "Task '$title' Saved to $selectedCategory!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
            tvDueDate.text = selectedDate
            tvDueDate.setTextColor(resources.getColor(R.color.white, theme))
        }, year, month, day)

        dpd.show()
    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this, { _, h, m ->
            val time = String.format("%02d:%02d", h, m)
            etTime.setText(time)
        }, hour, minute, true)

        tpd.show()
    }
}
