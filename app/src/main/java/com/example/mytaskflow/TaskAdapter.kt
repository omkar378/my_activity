package com.example.mytaskflow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTaskTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvTaskDesc)
        val tvTime: TextView = view.findViewById(R.id.tvTaskTime)
        val tvDate: TextView = view.findViewById(R.id.tvTaskDate)
        val priorityIndicator: View = view.findViewById(R.id.priorityIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTitle.text = task.title
        holder.tvDesc.text = task.description
        holder.tvTime.text = task.time
        holder.tvDate.text = task.date

        // Optional: Color the indicator based on priority
        val colorRes = when (task.priority) {
            "High" -> R.color.status_red
            "Medium" -> R.color.status_orange
            "Low" -> R.color.status_blue
            else -> R.color.accent_blue
        }
        holder.priorityIndicator.setBackgroundResource(colorRes)
    }

    override fun getItemCount() = tasks.size
}
