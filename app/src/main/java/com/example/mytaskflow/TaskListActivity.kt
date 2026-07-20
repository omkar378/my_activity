package com.example.mytaskflow

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskListActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter
    private lateinit var category: String
    private var selectedColor: Int = R.drawable.circle_blue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        category = intent.getStringExtra("CATEGORY") ?: "Tasks"
        findViewById<TextView>(R.id.tvCategoryTitle).text = category
        findViewById<TextView>(R.id.tvListHeader).text = "YOUR ${category.uppercase()}"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        setupAddSection()
        setupRecyclerView()
    }

    private fun setupAddSection() {
        val sectionSubject = findViewById<View>(R.id.sectionAddSubject)
        val sectionGoal = findViewById<View>(R.id.sectionAddGoal)
        val sectionReminder = findViewById<View>(R.id.sectionAddReminder)
        val sectionNote = findViewById<View>(R.id.sectionAddNote)

        when (category.lowercase()) {
            "projects", "subjects" -> {
                sectionSubject.visibility = View.VISIBLE
                setupSubjectAdd()
            }
            "goals" -> {
                sectionGoal.visibility = View.VISIBLE
                setupGoalAdd()
            }
            "reminders" -> {
                sectionReminder.visibility = View.VISIBLE
                setupReminderAdd()
            }
            "notes" -> {
                sectionNote.visibility = View.VISIBLE
                setupNoteAdd()
            }
            else -> {
                findViewById<View>(R.id.cardAdd).visibility = View.GONE
            }
        }
    }

    private fun setupSubjectAdd() {
        val etName = findViewById<EditText>(R.id.etSubjectName)
        val btnAdd = findViewById<View>(R.id.btnAddSubject)
        
        // Color selection logic
        val colors = listOf(
            R.id.color1 to R.drawable.circle_purple,
            R.id.color2 to R.drawable.circle_blue,
            R.id.color3 to R.drawable.circle_orange,
            R.id.color4 to R.drawable.circle_green,
            R.id.color5 to R.drawable.circle_red,
            R.id.color6 to R.drawable.circle_pink
        )
        
        colors.forEach { (viewId, drawableId) ->
            findViewById<View>(viewId).setOnClickListener {
                selectedColor = drawableId
                Toast.makeText(this, "Color selected", Toast.LENGTH_SHORT).show()
            }
        }

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                val newTask = Task(title = name, category = category, color = selectedColor)
                TaskRepository.addTask(newTask)
                etName.text.clear()
                refreshList()
            }
        }
    }

    private fun setupGoalAdd() {
        val etName = findViewById<EditText>(R.id.etGoalName)
        val etTarget = findViewById<EditText>(R.id.etGoalTarget)
        val btnAdd = findViewById<View>(R.id.btnAddGoal)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val target = etTarget.text.toString()
            if (name.isNotEmpty()) {
                val newTask = Task(title = name, category = category, target = target)
                TaskRepository.addTask(newTask)
                etName.text.clear()
                etTarget.text.clear()
                refreshList()
            }
        }
    }

    private fun setupReminderAdd() {
        val etTitle = findViewById<EditText>(R.id.etReminderTitle)
        val etTime = findViewById<EditText>(R.id.etReminderTime)
        val btnAdd = findViewById<View>(R.id.btnAddReminder)

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString()
            val timeStr = etTime.text.toString()
            if (title.isNotEmpty()) {
                val newTask = Task(title = title, category = category, time = timeStr)
                TaskRepository.addTask(newTask)
                etTitle.text.clear()
                etTime.text.clear()
                refreshList()
            }
        }
    }

    private fun setupNoteAdd() {
        val etTitle = findViewById<EditText>(R.id.etNoteTitle)
        val etContent = findViewById<EditText>(R.id.etNoteContent)
        val btnSave = findViewById<View>(R.id.btnSaveNote)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (title.isNotEmpty() || content.isNotEmpty()) {
                val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val newTask = Task(title = if(title.isEmpty()) "Untitled" else title, 
                                  description = content, category = category, date = dateStr)
                TaskRepository.addTask(newTask)
                etTitle.text.clear()
                etContent.text.clear()
                refreshList()
            }
        }
    }

    private fun setupRecyclerView() {
        val rvTasks = findViewById<RecyclerView>(R.id.rvTasks)
        val filteredTasks = TaskRepository.getTasksByCategory(category)

        adapter = TaskAdapter(filteredTasks, category) { task, action ->
            when (action) {
                "plus" -> TaskRepository.updateTaskProgress(task, 10)
                "minus" -> TaskRepository.updateTaskProgress(task, -10)
                "delete" -> TaskRepository.deleteTask(task)
            }
            refreshList()
        }

        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter
        updateEmptyState(filteredTasks.isEmpty())
    }

    private fun refreshList() {
        val filteredTasks = TaskRepository.getTasksByCategory(category)
        adapter.updateData(filteredTasks)
        updateEmptyState(filteredTasks.isEmpty())
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        findViewById<View>(R.id.tvNoTasks).visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
