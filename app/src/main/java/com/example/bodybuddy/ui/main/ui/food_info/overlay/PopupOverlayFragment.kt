package com.example.bodybuddy.ui.main.ui.food_info.overlay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bodybuddy.R
import com.example.bodybuddy.databinding.FragmentFoodInfoBinding
import com.example.bodybuddy.databinding.FragmentFoodInfoPopupOverlayBinding
import com.example.bodybuddy.ui.main.ui.food_info.FoodInfoViewModel

class PopupOverlayFragment : DialogFragment() {

    private var _binding: FragmentFoodInfoPopupOverlayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFoodInfoPopupOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the food data from arguments
        val foodName = arguments?.getString("foodName")
        val calories = arguments?.getInt("calories")
        val carbs = arguments?.getInt("carbs")
        val fats = arguments?.getInt("fats")
        val protein = arguments?.getInt("protein")

        // Set the food name and macronutrient values
        binding.textViewName.text = foodName
        binding.textViewCalories.text = getString(R.string.text_calories, calories)
        binding.textViewCarbs.text = getString(R.string.text_carbs, carbs)
        binding.textViewFats.text = getString(R.string.text_fats, fats)
        binding.textViewProtein.text = getString(R.string.text_protein, protein)

//        // Set the food image (replace foodImage with your actual image)
//        viewBinding.imageViewFood.setImageResource(R.drawable.foodImage)
    }

    override fun onResume() {
        super.onResume()
        // Set the width and height of the dialog fragment to match_parent
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
