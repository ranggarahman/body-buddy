package com.example.bodybuddy.ui.main.ui.food_info.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuddy.R
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.FoodInfoItemBinding
import com.example.bodybuddy.util.formatFoodName
import java.util.Locale

class FoodInfoAdapter(private val foodList: List<FoodListItem>) : RecyclerView.Adapter<FoodInfoAdapter.FoodInfoViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodInfoViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.food_info_item, parent, false)
        return FoodInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodInfoViewHolder, position: Int) {
        val food = foodList[position]

        holder.tvName.text = formatFoodName(food.foodName)

        holder.itemView.setOnClickListener {
            food.let { food ->
                onItemClickCallback.onItemClicked(food) }
        }
    }

    override fun getItemCount() = foodList.size

    class FoodInfoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FoodInfoItemBinding.bind(itemView)

        val tvName = binding.tvName
    }

    interface OnItemClickCallback{
        fun onItemClicked(food: FoodListItem)
    }

}