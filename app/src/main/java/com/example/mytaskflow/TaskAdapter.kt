package com.example.mytaskflow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: List<Task>,
    private val category: String,
    private val onAction: (Task, String) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DEFAULT = 0
        private const val TYPE_SUBJECT = 1
        private const val TYPE_GOAL = 2
        private const val TYPE_REMINDER = 3
        private const val TYPE_NOTE = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (category.lowercase()) {
            "projects", "subjects" -> TYPE_SUBJECT
            "goals" -> TYPE_GOAL
            "reminders" -> TYPE_REMINDER
            "notes" -> TYPE_NOTE
            else -> TYPE_DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SUBJECT -> SubjectViewHolder(inflater.inflate(R.layout.item_subject, parent, false))
            TYPE_GOAL -> GoalViewHolder(inflater.inflate(R.layout.item_goal, parent, false))
            TYPE_REMINDER -> ReminderViewHolder(inflater.inflate(R.layout.item_reminder, parent, false))
            TYPE_NOTE -> NoteViewHolder(inflater.inflate(R.layout.item_note, parent, false))
            else -> TaskViewHolder(inflater.inflate(R.layout.item_task, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position]
        when (holder) {
            is SubjectViewHolder -> {
                holder.tvName.text = task.title
                holder.tvPercent.text = "${task.progress}%"
                holder.progressBar.progress = task.progress
                if (task.color != 0) {
                    holder.ivColor.setBackgroundResource(task.color)
                }
                holder.btnPlus.setOnClickListener { onAction(task, "plus") }
                holder.btnMinus.setOnClickListener { onAction(task, "minus") }
                holder.ivDelete.setOnClickListener { onAction(task, "delete") }
            }
            is GoalViewHolder -> {
                holder.tvName.text = task.title
                holder.tvTarget.text = task.target
                holder.tvPercent.text = "${task.progress}%"
                holder.progressBar.progress = task.progress
                holder.btnPlus.setOnClickListener { onAction(task, "plus") }
                holder.btnMinus.setOnClickListener { onAction(task, "minus") }
                holder.ivDelete.setOnClickListener { onAction(task, "delete") }
            }
            is ReminderViewHolder -> {
                holder.tvTitle.text = task.title
                holder.tvDateTime.text = "${task.date} ${task.time}"
                holder.ivDelete.setOnClickListener { onAction(task, "delete") }
            }
            is NoteViewHolder -> {
                holder.tvTitle.text = task.title
                holder.tvContent.text = task.description
                holder.tvDate.text = task.date
                holder.ivDelete.setOnClickListener { onAction(task, "delete") }
            }
            is TaskViewHolder -> {
                holder.tvTitle.text = task.title
                holder.tvDesc.text = task.description
                holder.tvDateTime.text = "${task.date} ${task.time}"
                holder.tvPriority.text = task.priority
                val colorRes = when (task.priority) {
                    "High" -> R.color.status_red
                    "Medium" -> R.color.status_orange
                    "Low" -> R.color.status_blue
                    else -> R.color.accent_blue
                }
                holder.tvPriority.setTextColor(holder.itemView.context.getColor(colorRes))
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateData(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTaskTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvTaskDesc)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        val tvPriority: TextView = view.findViewById(R.id.tvPriority)
    }

    class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivColor: View = view.findViewById(R.id.ivSubjectColor)
        val tvName: TextView = view.findViewById(R.id.tvSubjectName)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        val progressBar: ProgressBar = view.findViewById(R.id.pbSubject)
        val tvPercent: TextView = view.findViewById(R.id.tvProgressPercent)
        val btnPlus: TextView = view.findViewById(R.id.btnPlus)
        val btnMinus: TextView = view.findViewById(R.id.btnMinus)
    }

    class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvGoalName)
        val tvTarget: TextView = view.findViewById(R.id.tvGoalTarget)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        val progressBar: ProgressBar = view.findViewById(R.id.pbGoal)
        val tvPercent: TextView = view.findViewById(R.id.tvProgressPercent)
        val btnPlus: TextView = view.findViewById(R.id.btnPlus)
        val btnMinus: TextView = view.findViewById(R.id.btnMinus)
    }

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvReminderTitle)
        val tvDateTime: TextView = view.findViewById(R.id.tvReminderDateTime)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvNoteTitle)
        val tvContent: TextView = view.findViewById(R.id.tvNoteContent)
        val tvDate: TextView = view.findViewById(R.id.tvNoteDate)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }
}
