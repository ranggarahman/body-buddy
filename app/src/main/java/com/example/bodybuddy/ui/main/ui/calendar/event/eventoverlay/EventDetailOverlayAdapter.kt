package com.example.bodybuddy.ui.main.ui.calendar.event.eventoverlay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuddy.R
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.ItemEventDetailBinding
import com.example.bodybuddy.util.formatFoodName

class EventDetailOverlayAdapter(private val foodList: List<FoodListItem>, private val context: Context) : RecyclerView.Adapter<EventDetailOverlayAdapter.FoodInfoViewHolder>() {

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
        holder.tvCalorie.text = context.getString(R.string.text_calories, food.calorie)
        holder.tvProtein.text = context.getString(R.string.text_protein, food.protein)
        holder.tvFat.text = context.getString(R.string.text_fats, food.fat)
        holder.tvCarbs.text = context.getString(R.string.text_carbs, food.carbs)

        holder.btnEdit.setOnClickListener {
            food.let { food ->
                onItemClickCallback.onItemClickedEdit(food) }
        }

        holder.btnDelete.setOnClickListener {
            food.let { food ->
                onItemClickCallback.onItemClickedDelete(food.foodName) }
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

        val btnEdit = binding.buttonEdit
        val btnDelete = binding.buttonDelete
    }

    interface OnItemClickCallback{
        fun onItemClickedEdit(food: FoodListItem)
        fun onItemClickedDelete(foodName: String)
    }

}