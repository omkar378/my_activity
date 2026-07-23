package com.example.mytaskflow

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WeeklySummaryAdapter(private val summaries: List<WeeklySummary>) : 
    RecyclerView.Adapter<WeeklySummaryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDateRange: TextView = view.findViewById(R.id.tvDateRange)
        val tvTaskCount: TextView = view.findViewById(R.id.tvTaskCount)
        val tvBadgeName: TextView = view.findViewById(R.id.tvBadgeName)
        val ivBadgeIcon: ImageView = view.findViewById(R.id.ivBadgeIcon)
        val layoutBadge: View = view.findViewById(R.id.layoutBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weekly_summary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val summary = summaries[position]
        val context = holder.itemView.context
        
        holder.tvDateRange.text = summary.dateRange
        holder.tvTaskCount.text = context.getString(R.string.tasks_completed_format, summary.completedCount, summary.totalCount)
        holder.tvBadgeName.text = summary.badgeName
        
        val color = ContextCompat.getColor(context, summary.badgeColorRes)
        holder.tvBadgeName.setTextColor(color)
        holder.ivBadgeIcon.imageTintList = ColorStateList.valueOf(color)
        holder.ivBadgeIcon.setImageResource(summary.badgeIconRes)
        
        // Optional: Update badge background if needed
    }

    override fun getItemCount() = summaries.size
}
