package com.example.bodybuddy.ui.main.ui.calendar.event.eventoverlay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuddy.R
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.FoodInfoItemBinding
import com.example.bodybuddy.databinding.ItemEventDetailBinding
import com.example.bodybuddy.util.formatFoodName

class EventDetailOverlayAdapter(private val foodList: List<FoodListItem>) : RecyclerView.Adapter<EventDetailOverlayAdapter.FoodInfoViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodInfoViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_event_detail, parent, false)
        return FoodInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodInfoViewHolder, position: Int) {
        val food = foodList[position]

        holder.tvName.text = formatFoodName(food.foodName)
        holder.tvCalorie.text = food.calories.toString()
        holder.tvProtein.text = food.protein.toString()
        holder.tvFat.text = food.fats.toString()
        holder.tvCarbs.text = food.carbs.toString()

        holder.itemView.setOnClickListener {
            food.let { food ->
                onItemClickCallback.onItemClicked(food) }
        }
    }

    override fun getItemCount() = foodList.size

    inner class FoodInfoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemEventDetailBinding.bind(itemView)

        val tvName = binding.textviewFoodname
        val tvCalorie = binding.textviewCalorie
        val tvProtein = binding.textviewProtein
        val tvFat = binding.textviewFat
        val tvCarbs = binding.textviewCarbs
    }

    interface OnItemClickCallback{
        fun onItemClicked(food: FoodListItem)
    }

}