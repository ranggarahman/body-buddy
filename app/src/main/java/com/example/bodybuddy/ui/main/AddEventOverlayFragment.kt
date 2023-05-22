package com.example.bodybuddy.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bodybuddy.R
import com.example.bodybuddy.databinding.FragmentAddEventOverlayBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class AddEventOverlayFragment : DialogFragment() {

    private var _binding: FragmentAddEventOverlayBinding? = null

    private val addEventOverlayFragmentViewModel by viewModels<AddEventOverlayFragmentViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddEventOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = Date()
        val current = formatter.format(date)

        binding.spinnerMeal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMeal = adapterView.getItemAtPosition(position).toString()
                selectedMeal.lowercase(Locale.ROOT)
                Toast.makeText(requireContext(), "Selected meal: $selectedMeal", Toast.LENGTH_SHORT).show()

                binding.btnSubmit.setOnClickListener {

                    val name = binding.editTextName.text.toString()
                    val calorie = binding.editTextCalorie.text.toString().toDouble()
                    val fat = binding.editTextFat.text.toString().toDouble()
                    val carbs = binding.editTextCarbs.text.toString().toDouble()
                    val protein = binding.editTextProtein.text.toString().toDouble()

                    addEventOverlayFragmentViewModel.saveUserData(
                        current,
                        selectedMeal,
                        name,
                        calorie,
                        carbs,
                        fat,
                        protein
                    )
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                binding.btnSubmit.isEnabled = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }
}