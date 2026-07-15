package com.example.mytaskflow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mytaskflow.R

data class CalendarDay(
    val day: String,
    var isSelected: Boolean = false,
    val hasTask: Boolean = false,
    val isCurrentMonth: Boolean = true
)

class CalendarAdapter(
    private val days: List<CalendarDay>,
    private val onDayClick: (CalendarDay, Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition = days.indexOfFirst { it.isSelected }

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDay)
        val dotView: View = view.findViewById(R.id.dotView)
        val container: View = view.findViewById(R.id.dayContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = days[position]
        holder.tvDay.text = item.day
        
        if (item.day.isEmpty()) {
            holder.container.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener(null)
            return
        } else {
            holder.container.visibility = View.VISIBLE
        }

        val isSelected = position == selectedPosition

        if (isSelected) {
            holder.tvDay.setBackgroundResource(R.drawable.bg_selected_day)
            holder.tvDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.tvDay.background = null
            holder.tvDay.setTextColor(
                if (item.isCurrentMonth) ContextCompat.getColor(holder.itemView.context, R.color.white)
                else ContextCompat.getColor(holder.itemView.context, R.color.text_secondary)
            )
        }

        holder.dotView.visibility = if (item.hasTask) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            if (oldPosition != -1) notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
            onDayClick(item, selectedPosition)
        }
    }

    override fun getItemCount() = days.size
}