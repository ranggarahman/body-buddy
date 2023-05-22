package com.example.bodybuddy.ui.main.ui.calendar

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bodybuddy.R
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.databinding.CalendarDayBinding
import com.example.bodybuddy.databinding.CalendarHeaderBinding
import com.example.bodybuddy.databinding.FragmentCalendarBinding
import com.example.bodybuddy.ui.main.ui.calendar.event.Event
import com.example.bodybuddy.ui.main.ui.calendar.event.EventAdapter
import com.example.bodybuddy.ui.main.ui.calendar.event.eventoverlay.EventDetailOverlayFragment
import com.example.bodybuddy.util.makeInVisible
import com.example.bodybuddy.util.makeVisible
import com.example.bodybuddy.util.setTextColorRes
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    private val calendarViewModel by viewModels<CalendarViewModel>()
    private val binding get() = _binding!!

    private val events = mutableMapOf<LocalDate, List<Event>>()
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val today = LocalDate.now()

    private var selectedDate: LocalDate? = null
    private var previouslySelectedDate: LocalDate? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exThreeCalendar.monthScrollListener = { calendarMonth ->
            binding.exThreeMonthText.text = if (calendarMonth.yearMonth.year == today.year) {
                titleSameYearFormatter.format(calendarMonth.yearMonth)
            } else {
                titleFormatter.format(calendarMonth.yearMonth)
            }
            selectDate(calendarMonth.yearMonth.atDay(1))
        }

        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(50)
        val endMonth = currentMonth.plusMonths(50)
        configureBinders(daysOfWeek)
        binding.exThreeCalendar.apply {
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.exThreeCalendar.post { selectDate(today) }
        }
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        selectDate(day.date)
                    }
                }
            }
        }

        binding.exThreeCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.exThreeDayText
                val dotView = container.binding.exThreeDotView

                textView.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) {
                    textView.makeVisible()
                    when (data.date) {
                        today -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.blue_air_force)
                            textView.setBackgroundResource(R.drawable.example_3_selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black_rich)
                            textView.background = null
                            dotView.isVisible = events.containsKey(data.date) && events[data.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }

        binding.exThreeCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].name.first().toString()
                                tv.setTextColorRes(R.color.black_rich)
                            }
                    }
                }
            }
    }

    private fun selectDate(date: LocalDate) {

        Log.d(TAG, "SELECT DATE CALLED")

        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.exThreeCalendar.notifyDateChanged(it) }
            binding.exThreeCalendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        calendarViewModel.updateAdapterForDate(date)

        val mediatorLiveData = MediatorLiveData<Pair<List<Event>?, Boolean>>()
        mediatorLiveData.addSource(calendarViewModel.eventList) { eventList ->
            mediatorLiveData.value = eventList to (calendarViewModel.isSuccess.value ?: false)
        }

        mediatorLiveData.addSource(calendarViewModel.isSuccess) { isSuccess ->
            mediatorLiveData.value = calendarViewModel.eventList.value to isSuccess
        }

        mediatorLiveData.observe(viewLifecycleOwner) { (eventList, isSuccess) ->
            if (isSuccess) {
                val foodListAdapter = eventList?.let { EventAdapter(it) }

                binding.exThreeRv.adapter = foodListAdapter
                binding.exThreeRv.layoutManager = LinearLayoutManager(requireActivity())

                foodListAdapter?.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
                    override fun onItemClicked(event: Event) {
                        val dialog = EventDetailOverlayFragment()

                        dialog.arguments = Bundle().apply {
                            putString("date", date.format(DateTimeFormatter
                                .ofPattern("yyyyMMdd")))
                            putString("mealtype", event.text)
                        }

                        dialog.show(parentFragmentManager, "detail_popup")

//                        val observer = object : Observer<List<FoodListItem>> {
//                            override fun onChanged(value: List<FoodListItem> ) {
//                                Log.d(TAG, "MAPLIST : $value")
//
//                                dialog.arguments = Bundle().apply {
//                                    putSerializable("FoodListItem", value as Serializable)
//                                    putString("mealtype", event.text)
//                                }
//                                calendarViewModel.foodList.removeObserver(this)
//                            }
//                        }
//
//                        calendarViewModel.foodList.observe(viewLifecycleOwner, observer)
                    }
                })

                eventList?.let { events[date] = it }
            }

            binding.exThreeSelectedDateText.text = selectionFormatter.format(date)
        }
    }

//    private fun updateAdapterForDate(date: LocalDate){
//        calendarViewModel.updateAdapterForDate(date)
//
//        calendarViewModel.eventList.observe(viewLifecycleOwner){eventList ->
//            calendarViewModel.isSuccess.observe(viewLifecycleOwner) {
//                if (it) {
//
//                    val foodListAdapter = eventList?.let { data -> EventAdapter(data) }
//
//                    binding.exThreeRv.adapter = foodListAdapter
//                    binding.exThreeRv.layoutManager = LinearLayoutManager(requireActivity())
//
//                    foodListAdapter?.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback{
//                        override fun onItemClicked(event: List<Event>) {
//                            val dialog = EventDetailOverlayFragment()
//
//                            calendarViewModel.eventMapList.observe(viewLifecycleOwner){eventMapList ->
//                                val args = Bundle()
//                                args.putSerializable("eventMapList", eventMapList as Serializable)
//
//                                dialog.arguments = args
//                                dialog.show(parentFragmentManager, "event_dialog")
//                            }
//                        }
//                    })
//
//                    if (eventList != null) {
//                        events[date] = eventList
//                    }
//                }
//
//                binding.exThreeSelectedDateText.text = selectionFormatter.format(date)
//            }
//        }
//
//    }

    private fun deleteEvent(event: Event) {
        val date = event.date
        events[date] = events[date].orEmpty().minus(event)
        updateAdapterForDate(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TAG ="CalendarFragment"
    }
}