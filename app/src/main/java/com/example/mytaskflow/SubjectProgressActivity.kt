package com.example.mytaskflow

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SubjectProgressActivity : AppCompatActivity() {

    private lateinit var calendar: Calendar
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayFormatter = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subject_progress)

        calendar = Calendar.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupInteractions()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun setupInteractions() {
        findViewById<View>(R.id.ivHistory).setOnClickListener {
            Toast.makeText(this, "History feature coming soon with AI!", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnPrevDay).setOnClickListener {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            refreshUI()
        }

        findViewById<View>(R.id.btnNextDay).setOnClickListener {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            refreshUI()
        }

        findViewById<View>(R.id.tvDateDisplay).setOnClickListener {
            showDatePicker()
        }

        findViewById<View>(R.id.tvStudyTimeFilter).setOnClickListener {
            Toast.makeText(this, "Filtering by Study Time", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.tvWeekFilter).setOnClickListener {
            Toast.makeText(this, "Weekly Range Selected", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.cvMainChart).setOnClickListener {
            Toast.makeText(this, "Weekly Overview", Toast.LENGTH_SHORT).show()
        }

        // New Task Actions
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            openNewTask()
        }

        findViewById<Button>(R.id.btnDirectAdd).setOnClickListener {
            openNewTask()
        }

        findViewById<View>(R.id.cardAddSubjectTask).setOnClickListener {
            openNewTask()
        }

        // Title and Subtitle as well
        findViewById<View>(R.id.tvTitle).setOnClickListener {
            Toast.makeText(this, "Study Progress Dashboard", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.tvSubtitle).setOnClickListener {
            Toast.makeText(this, "Keep track of your learning!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openNewTask() {
        val intent = Intent(this, NewTaskActivity::class.java)
        intent.putExtra("PREFILL_DATE", dateFormatter.format(calendar.time))
        startActivity(intent)
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            calendar.set(y, m, d)
            refreshUI()
        }, year, month, day).show()
    }

    private fun refreshUI() {
        updateDateDisplay()
        updateWeeklyChart()
        updateGoalProgress()
        updateTaskList()
    }

    private fun updateDateDisplay() {
        findViewById<TextView>(R.id.tvDateDisplay).text = displayFormatter.format(calendar.time)
    }

    private fun updateWeeklyChart() {
        val weekCalendar = calendar.clone() as Calendar
        weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val barIds = listOf(
            R.id.barSun, R.id.barMon, R.id.barTue, R.id.barWed, R.id.barThu, R.id.barFri, R.id.barSat
        )

        var totalStudyMinutes = 0

        barIds.forEach { barId ->
            val dateStr = dateFormatter.format(weekCalendar.time)
            val dayTasks = TaskManager.getAllTasks().filter { it.date == dateStr }
            val taskCount = dayTasks.size
            
            val barView = findViewById<View>(barId)
            
            // Fixed height logic: 
            // Min height = 15dp (so always visible)
            // 1 task = 25dp height increase
            val baseHeightPx = dpToPx(15)
            val taskHeightPx = dpToPx(taskCount * 25)
            
            val params = barView.layoutParams
            params.height = (baseHeightPx + taskHeightPx).coerceAtMost(dpToPx(140))
            barView.layoutParams = params
            
            // Show toast when clicking bar
            barView.setOnClickListener {
                Toast.makeText(this, "$dateStr: $taskCount tasks", Toast.LENGTH_SHORT).show()
            }
            
            totalStudyMinutes += taskCount * 45
            weekCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val hours = totalStudyMinutes / 60
        val mins = totalStudyMinutes % 60
        findViewById<TextView>(R.id.tvTotalStudyTime).text = "$hours hr, $mins min"
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun updateGoalProgress() {
        val weekCalendar = calendar.clone() as Calendar
        weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        
        var completed = 0
        var total = 0
        
        repeat(7) {
            val dateStr = dateFormatter.format(weekCalendar.time)
            val tasks = TaskManager.getAllTasks().filter { it.date == dateStr }
            total += tasks.size
            completed += tasks.count { it.status == "Completed" }
            weekCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        val percent = if (total > 0) ((completed * 100) / total) else 0
        
        findViewById<ProgressBar>(R.id.pbWeeklyGoal).progress = percent
        findViewById<TextView>(R.id.tvGoalPercent).text = "$percent%"
    }

    private fun updateTaskList() {
        val container = findViewById<LinearLayout>(R.id.taskListContainer)
        val emptyState = findViewById<View>(R.id.cardAddSubjectTask)
        container.removeAllViews()

        val selectedDateStr = dateFormatter.format(calendar.time)
        val tasks = TaskManager.getAllTasks().filter { it.date == selectedDateStr }

        if (tasks.isEmpty()) {
            emptyState.visibility = View.VISIBLE
        } else {
            emptyState.visibility = View.GONE
            tasks.forEach { task ->
                val taskView = LayoutInflater.from(this).inflate(R.layout.item_task, container, false)
                
                taskView.findViewById<TextView>(R.id.tvTaskTitle).text = task.title
                taskView.findViewById<TextView>(R.id.tvTaskDesc).text = task.description
                taskView.findViewById<TextView>(R.id.tvPriority).text = task.priority
                taskView.findViewById<TextView>(R.id.tvTaskDate).text = task.date
                taskView.findViewById<TextView>(R.id.tvTaskTime).text = task.time
                
                val cb = taskView.findViewById<CheckBox>(R.id.cbComplete)
                cb.isChecked = task.status == "Completed"
                updateTaskTextStyle(taskView, task.status == "Completed")

                cb.setOnCheckedChangeListener { _, isChecked ->
                    task.status = if (isChecked) "Completed" else "Pending"
                    TaskManager.updateTask(this)
                    updateTaskTextStyle(taskView, isChecked)
                    updateGoalProgress() // Update goal circle
                }

                taskView.findViewById<View>(R.id.ivDelete).setOnClickListener {
                    TaskManager.deleteTask(this, task.id)
                    updateTaskList()
                    updateWeeklyChart()
                    updateGoalProgress()
                }

                container.addView(taskView)
            }
        }
    }

    private fun updateTaskTextStyle(view: View, isCompleted: Boolean) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTaskTitle)
        if (isCompleted) {
            tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvTitle.setTextColor(resources.getColor(R.color.text_secondary, theme))
        } else {
            tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            tvTitle.setTextColor(resources.getColor(R.color.white, theme))
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
        findViewById<View>(R.id.navAI).setOnClickListener {
            startActivity(Intent(this, AIPlannerActivity::class.java))
            finish()
        }
        findViewById<View>(R.id.navProfile).setOnClickListener {
            Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
        }
    }
}
