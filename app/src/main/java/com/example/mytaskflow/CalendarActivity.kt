package com.example.mytaskflow

import android.app.DatePickerDialog
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytaskflow.adapter.CalendarAdapter
import com.example.mytaskflow.adapter.CalendarDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var rvCalendar: RecyclerView
    private lateinit var tvMonthYear: TextView
    private lateinit var tvSelectedDateTasks: TextView
    private lateinit var btnPrevMonth: ImageView
    private lateinit var btnNextMonth: ImageView
    
    private val calendar = Calendar.getInstance()
    private var selectedDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupCalendar()
        setupMonthNavigation()
        setupBottomNavigation()
    }

    private fun initViews() {
        rvCalendar = findViewById(R.id.rvCalendar)
        tvMonthYear = findViewById(R.id.tvMonthYear)
        tvSelectedDateTasks = findViewById(R.id.tvSelectedDateTasks)
        btnPrevMonth = findViewById(R.id.btnPrevMonth)
        btnNextMonth = findViewById(R.id.btnNextMonth)
        
        // Initial text
        updateMonthYearText()
        updateSelectedDateText(selectedDate)
    }

    private fun updateMonthYearText() {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = sdf.format(calendar.time)
    }

    private fun updateSelectedDateText(date: Calendar) {
        val sdf = SimpleDateFormat("EEEE, d MMM", Locale.getDefault())
        tvSelectedDateTasks.text = "Tasks on ${sdf.format(date.time)}"
    }

    private fun setupCalendar() {
        val days = mutableListOf<CalendarDay>()
        
        val tempCal = calendar.clone() as Calendar
        tempCal.set(Calendar.DAY_OF_MONTH, 1)
        
        // Month start day (1=Sun, 2=Mon, ..., 7=Sat)
        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK)
        val emptyDays = firstDayOfWeek - 1
        
        for (i in 0 until emptyDays) {
            days.add(CalendarDay(""))
        }
        
        val maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        // In a real app, you'd fetch tasks for the current month here
        // Using some random task indicators for demo
        val taskDays = setOf(5, 12, 18, 25) 

        for (i in 1..maxDays) {
            val isSelected = i == selectedDate.get(Calendar.DAY_OF_MONTH) &&
                    calendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                    calendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
            
            days.add(
                CalendarDay(
                    day = i.toString(),
                    isSelected = isSelected,
                    hasTask = taskDays.contains(i)
                )
            )
        }
        
        val adapter = CalendarAdapter(days) { clickedDay, _ ->
            if (clickedDay.day.isNotEmpty()) {
                val dayInt = clickedDay.day.toInt()
                selectedDate = calendar.clone() as Calendar
                selectedDate.set(Calendar.DAY_OF_MONTH, dayInt)
                updateSelectedDateText(selectedDate)
            }
        }
        
        rvCalendar.layoutManager = GridLayoutManager(this, 7)
        rvCalendar.adapter = adapter
    }

    private fun setupMonthNavigation() {
        btnPrevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateMonthYearText()
            setupCalendar()
        }

        btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateMonthYearText()
            setupCalendar()
        }

        tvMonthYear.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                selectedDate.set(year, month, dayOfMonth)
                updateMonthYearText()
                updateSelectedDateText(selectedDate)
                setupCalendar()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setupBottomNavigation() {
        findViewById<View>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        
        findViewById<View>(R.id.navTasks).setOnClickListener {
            Toast.makeText(this, "Tasks selected", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<View>(R.id.navCalendar).setOnClickListener {
            // Already on Calendar
        }
        
        findViewById<View>(R.id.navProfile).setOnClickListener {
            Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<View>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }
    }
}