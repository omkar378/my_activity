package com.example.mytaskflow

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class GoalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goals)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        setupCurrentWeekProgress()
        setupWeeklySummary()
    }

    private fun setupCurrentWeekProgress() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        
        // Start of week (Sunday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val startStr = dateFormat.format(calendar.time)
        
        // End of week (Saturday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endStr = dateFormat.format(calendar.time)
        
        findViewById<TextView>(R.id.tvDateRange).text = getString(R.string.date_range_format, startStr, endStr)

        // Calculate progress for Goals category
        val goals = TaskRepository.getTasksByCategory("Goals")
        val completedThisWeek = goals.count { it.isCompleted } // Ideally filter by date too
        val totalGoals = goals.size
        
        val progressPercent = if (totalGoals > 0) (completedThisWeek.toFloat() / totalGoals * 100).toInt() else 0
        updateStars(progressPercent)
    }

    private fun updateStars(percent: Int) {
        val stars = listOf(
            findViewById<ImageView>(R.id.star1),
            findViewById<ImageView>(R.id.star2),
            findViewById<ImageView>(R.id.star3),
            findViewById<ImageView>(R.id.star4),
            findViewById<ImageView>(R.id.star5)
        )

        val filledCount = when {
            percent >= 100 -> 5
            percent >= 80 -> 4
            percent >= 60 -> 3
            percent >= 40 -> 2
            percent >= 20 -> 1
            else -> 0
        }

        for (i in stars.indices) {
            if (i < filledCount) {
                stars[i].setImageResource(android.R.drawable.btn_star_big_on)
                stars[i].imageTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(R.color.status_orange, theme)
                )
            } else {
                stars[i].setImageResource(android.R.drawable.btn_star_big_off)
                stars[i].imageTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(R.color.text_secondary, theme)
                )
            }
        }
    }

    private fun setupWeeklySummary() {
        val rvHistory = findViewById<RecyclerView>(R.id.rvWeeklySummary)
        
        // Mock history data as per design
        val history = listOf(
            WeeklySummary("11 May - 17 May 2025", 5, 5, "Excellent", R.color.status_green, android.R.drawable.btn_star_big_on),
            WeeklySummary("04 May - 10 May 2025", 3, 5, "Better", R.color.status_blue, android.R.drawable.btn_star_big_on),
            WeeklySummary("27 Apr - 03 May 2025", 2, 5, "Good", R.color.status_orange, android.R.drawable.btn_star_big_on)
        )

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = WeeklySummaryAdapter(history)
    }
}
