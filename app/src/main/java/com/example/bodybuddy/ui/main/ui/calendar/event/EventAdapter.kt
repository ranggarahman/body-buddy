package com.example.bodybuddy.ui.main.ui.calendar.event

import android.provider.CalendarContract.Events
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.EventItemViewBinding
import com.example.bodybuddy.ui.main.ui.food_info.adapter.FoodInfoAdapter
import java.time.LocalDate

data class Event(val text: String, val date: LocalDate)

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val boundPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            EventItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: EventViewHolder, position: Int) {
        viewHolder.bind(events[position])
        //boundPositions.add(position)

        viewHolder.itemView.setOnClickListener {
            events[position].let {event ->
                onItemClickCallback.onItemClicked(event)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: EventViewHolder, position: Int, payloads: MutableList<Any>) {
        if (boundPositions.contains(position)) {
            return  // Item already bound, no need to bind again
        }
        super.onBindViewHolder(viewHolder, position, payloads)
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.itemEventText.text = event.text
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(event: Event)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
}
