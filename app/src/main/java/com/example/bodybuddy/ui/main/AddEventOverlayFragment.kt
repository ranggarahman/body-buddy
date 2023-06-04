package com.example.bodybuddy.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bodybuddy.databinding.FragmentAddEventOverlayBinding
import com.example.bodybuddy.ui.CameraActivity
import com.example.bodybuddy.util.afterTextChanged
import java.text.SimpleDateFormat
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

        binding.editTextName.afterTextChanged { checkFields() }
        binding.editTextCalorie.afterTextChanged { checkFields() }
        binding.editTextFat.afterTextChanged { checkFields() }
        binding.editTextCarbs.afterTextChanged { checkFields() }
        binding.editTextProtein.afterTextChanged { checkFields() }

        val argFoodName = arguments?.getString("foodName")
        val argCalories = arguments?.getDouble("calorie")
        val argCarbs = arguments?.getDouble("carbs")
        val argFats = arguments?.getDouble("fat")
        val argProtein = arguments?.getDouble("protein")
        val editDate = arguments?.getString("date")
        val argMealType = arguments?.getString("mealType")

        if (arguments != null){
            binding.btnSubmit.isEnabled = true

            binding.editTextName.text = Editable.Factory.getInstance().newEditable(argFoodName)
            binding.editTextCalorie.text = Editable.Factory.getInstance().newEditable(argCalories.toString())
            binding.editTextCarbs.text = Editable.Factory.getInstance().newEditable(argCarbs.toString())
            binding.editTextProtein.text = Editable.Factory.getInstance().newEditable(argProtein.toString())
            binding.editTextFat.text = Editable.Factory.getInstance().newEditable(argFats.toString())

            val spinnerAdapter = binding.spinnerMeal.adapter as ArrayAdapter<String>
            val position = spinnerAdapter.getPosition(argMealType)
            binding.spinnerMeal.setSelection(position)

            binding.spinnerMeal.isEnabled = false
            binding.editTextName.isEnabled = false

            binding.btnSubmit.setOnClickListener {
                addEventOverlayFragmentViewModel.overwriteUserData(
                    editDate!!,
                    argMealType!!,
                    binding.editTextName.text.toString(),
                    binding.editTextCalorie.text.toString().toDouble(),
                    binding.editTextCarbs.text.toString().toDouble(),
                    binding.editTextFat.text.toString().toDouble(),
                    binding.editTextProtein.text.toString().toDouble(),
                )
            }
        } else {
            setupTodayInput()
        }

        binding.fabCamera.setOnClickListener {
            val context = requireActivity()
            val intent = Intent(context, CameraActivity::class.java)

            context.startActivity(intent)
        }
    }

    private fun setupTodayInput() {
        binding.btnSubmit.isEnabled = false

        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = Date()
        val current = formatter.format(date)

        binding.spinnerMeal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedMealType = adapterView.getItemAtPosition(position).toString()
                selectedMealType.lowercase(Locale.ROOT)
                Toast.makeText(
                    requireContext(),
                    "Selected meal: $selectedMealType",
                    Toast.LENGTH_SHORT
                ).show()

                binding.btnSubmit.setOnClickListener {
                    addEventOverlayFragmentViewModel.saveUserData(
                        current,
                        selectedMealType,
                        binding.editTextName.text.toString(),
                        binding.editTextCalorie.text.toString().toDouble(),
                        binding.editTextCarbs.text.toString().toDouble(),
                        binding.editTextFat.text.toString().toDouble(),
                        binding.editTextProtein.text.toString().toDouble(),
                    )
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                binding.btnSubmit.isEnabled = false
            }
        }
    }

    private fun checkFields() {
        val name = binding.editTextName.text.toString()
        val calorie = binding.editTextCalorie.text.toString()
        val fat = binding.editTextFat.text.toString()
        val carbs = binding.editTextCarbs.text.toString()
        val protein = binding.editTextProtein.text.toString()

        val allFieldsFilled = name.isNotBlank() && calorie.isNotBlank() && fat.isNotBlank() && carbs.isNotBlank() && protein.isNotBlank()
        binding.btnSubmit.isEnabled = allFieldsFilled
    }

    override fun onResume() {
        super.onResume()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }
}