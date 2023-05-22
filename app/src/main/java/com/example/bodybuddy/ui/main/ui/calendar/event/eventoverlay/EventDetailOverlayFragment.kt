package com.example.bodybuddy.ui.main.ui.calendar.event.eventoverlay

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.FragmentEventOverlayBinding

class EventDetailOverlayFragment() : DialogFragment() {

    private var _binding: FragmentEventOverlayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val eventDetailOverlayViewModel by viewModels<EventDetailOverlayViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEventOverlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = arguments?.getString("date").toString()
        val mealType = arguments?.getString("mealtype").toString()

        Log.d(TAG, "DATE : $date, MEALTYPE: $mealType")

        eventDetailOverlayViewModel.retrieveFoodListFromDatabase(mealType, date)

        eventDetailOverlayViewModel.foodList.observe(viewLifecycleOwner){

            val adapter = EventDetailOverlayAdapter(it)

            binding.foodRecyclerView.adapter = adapter
            binding.foodRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        }


        binding.mealtypeHeader.text = mealType
    }

    override fun onResume() {
        super.onResume()
        // Set the width and height of the dialog fragment to match_parent
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "EventDetailOverlayFragment"
    }
}