package com.example.bodybuddy.ui.main.ui.calendar.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuddy.databinding.EventItemViewBinding
import java.time.LocalDate

data class Event(val id: String, val text: String, val date: LocalDate)

class EventAdapter(val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            EventItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(viewHolder: EventViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) {
            binding.itemEventText.text = event.text
        }
    }
}