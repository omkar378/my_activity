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

        // Category Selection (Multiple allowed)
        val categories = listOf(
            R.id.catWebDev, R.id.catDSA, R.id.catDBMS, 
            R.id.catOS, R.id.catMath, R.id.catOther
        )
        categories.forEach { id ->
            findViewById<TextView>(id).setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }

        // Priority Selection (Exclusive)
        val priorities: List<Int> = listOf(R.id.priLow, R.id.priMedium, R.id.priHigh)
        priorities.forEach { id ->
            findViewById<TextView>(id).setOnClickListener { view ->
                priorities.forEach { findViewById<View>(it).isSelected = false }
                view.isSelected = true
            }
        }
        // Set default priority
        findViewById<View>(R.id.priMedium).isSelected = true

        // Status Selection (Exclusive)
        val statuses: List<Int> = listOf(R.id.statusPending, R.id.statusInProgress, R.id.statusCompleted)
        statuses.forEach { id ->
            findViewById<TextView>(id).setOnClickListener { view ->
                statuses.forEach { findViewById<View>(it).isSelected = false }
                view.isSelected = true
            }
        }
        // Set default status
        findViewById<View>(R.id.statusPending).isSelected = true

        // Date Picker
        tvDueDate = findViewById(R.id.tvDueDate)
        
        // Handle Prefilled Date
        val prefilledDate = intent.getStringExtra("PREFILL_DATE")
        if (!prefilledDate.isNullOrEmpty()) {
            selectedDate = prefilledDate
            tvDueDate.text = selectedDate
            tvDueDate.setTextColor(resources.getColor(R.color.white, theme))
        }

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
            
            if (title.isNotEmpty()) {
                val newTask = Task(
                    title = title,
                    description = description,
                    date = selectedDate,
                    time = etTime.text.toString(),
                    priority = getSelectedPriority(),
                    status = getSelectedStatus()
                )
                TaskManager.addTask(this, newTask)
                
                Toast.makeText(this, "Task '$title' Created!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getSelectedPriority(): String {
        if (findViewById<View>(R.id.priHigh).isSelected) return "High"
        if (findViewById<View>(R.id.priLow).isSelected) return "Low"
        return "Medium"
    }

    private fun getSelectedStatus(): String {
        if (findViewById<View>(R.id.statusCompleted).isSelected) return "Completed"
        if (findViewById<View>(R.id.statusInProgress).isSelected) return "In Progress"
        return "Pending"
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
