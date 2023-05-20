package com.example.bodybuddy.ui.main.ui.food_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.FragmentFoodInfoBinding
import com.example.bodybuddy.ui.main.ui.food_info.adapter.FoodInfoAdapter
import com.example.bodybuddy.ui.main.ui.food_info.overlay.PopupOverlayFragment
import com.example.bodybuddy.util.formatFoodName

class FoodInfoFragment : Fragment() {

    private var _binding: FragmentFoodInfoBinding? = null

    private val foodInfoViewModel by viewModels<FoodInfoViewModel>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodInfoViewModel.retrieveFoodListFromDatabase()

        foodInfoViewModel.foodList.observe(viewLifecycleOwner){
            val foodListAdapter = FoodInfoAdapter(it)
            binding.rvInfo.adapter = foodListAdapter
            binding.rvInfo.layoutManager = LinearLayoutManager(requireActivity())

            foodListAdapter.setOnItemClickCallback(object : FoodInfoAdapter.OnItemClickCallback{
                override fun onItemClicked(food: FoodListItem) {
                    val dialog = PopupOverlayFragment()
                    val args = Bundle().apply {
                        putString("foodName", formatFoodName(food.foodName))
                        putInt("calories", food.calories)
                        putInt("carbs", food.carbs)
                        putInt("fats", food.fats)
                        putInt("protein", food.protein)
                        // Put any additional data you need to pass to the fragment
                    }
                    dialog.arguments = args
                    dialog.show(parentFragmentManager, "popup_dialog")
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}