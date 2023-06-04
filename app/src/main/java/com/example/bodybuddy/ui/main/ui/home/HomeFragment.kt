package com.example.bodybuddy.ui.main.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import com.example.bodybuddy.R
import com.example.bodybuddy.databinding.FragmentHomeBinding
import com.example.bodybuddy.util.CustomTypefaceSpan
import com.example.bodybuddy.util.parties
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(), OnChartValueSelectedListener {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val homeViewModel by viewModels<HomeViewModel>()
    private val binding get() = _binding!!

    private lateinit var chart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = Date()
        val current = formatter.format(date)

        val assetPath = requireContext().assets
        val colorBlack = requireContext().getColor(R.color.black_rich)
        val typeface = Typeface.createFromAsset(assetPath, "youtube_sans_medium.ttf")

        setupPieChart(colorBlack, typeface)

        homeViewModel.fetchTotalMacronutrients(current)
    }

    private fun setupPieChart(colorBlack: Int, typeface: Typeface) {
        chart = binding.chart1

        //This Works
        setData(3, 5f, colorBlack)

        chart.setUsePercentValues(false)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.centerText = generateCenterSpannableText(typeface, colorBlack)

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)
        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

//         chart.setUnit(" grams");
//         chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        chart.animateY(1400, Easing.EaseInOutQuad)
//         chart.spin(2000, 0F, 360F, W);
        val l = chart.legend

        l.isEnabled = false

        chart.setEntryLabelColor(colorBlack)
        chart.setEntryLabelTypeface(typeface)
        chart.setEntryLabelTextSize(12f)
    }

    private fun generateCenterSpannableText(typeface: Typeface, colorBlack: Int): CharSequence {
        val s = SpannableString("Calorie Intake\ncalculated by Today")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.BOLD), 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(colorBlack), 14, s.length, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)

        val customTypefaceSpan = CustomTypefaceSpan(typeface)
        s.setSpan(customTypefaceSpan, 0, s.length, 0)

        return s
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setData(count: Int, range: Float, colorBlack: Int) {
        val entries = ArrayList<PieEntry>()
        val dataSet = PieDataSet(entries, "Daily Summary")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c: Int in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c: Int in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTextColor(colorBlack)

        chart.data = data

        val proteinLiveData = homeViewModel.totalProtein
        val carbsLiveData = homeViewModel.totalCarbs
        val fatLiveData = homeViewModel.totalFat

        val combinedLiveData = MediatorLiveData<Triple<Double?, Double?, Double?>>()
        combinedLiveData.addSource(proteinLiveData) { proteinValue ->
            combinedLiveData.value = Triple(proteinValue, carbsLiveData.value, fatLiveData.value)
        }
        combinedLiveData.addSource(carbsLiveData) { carbsValue ->
            combinedLiveData.value = Triple(proteinLiveData.value, carbsValue, fatLiveData.value)
        }
        combinedLiveData.addSource(fatLiveData) { fatValue ->
            combinedLiveData.value = Triple(proteinLiveData.value, carbsLiveData.value, fatValue)
        }

        combinedLiveData.observe(viewLifecycleOwner) { (proteinValue, carbsValue, fatValue) ->

            binding.imageviewUnavailable.visibility = View.VISIBLE
            binding.textviewUnavailable.visibility = View.VISIBLE

            Log.d("HomeFragment", "VALUE : $proteinValue, $carbsValue, $fatValue")

            if (proteinValue != null && carbsValue != null && fatValue != null) {
                if (proteinValue != 0.0 && carbsValue != 0.0 && fatValue != 0.0) {
                    binding.imageviewUnavailable.visibility = View.GONE
                    binding.textviewUnavailable.visibility = View.GONE

                    entries.clear()
                    entries.add(PieEntry(proteinValue.toFloat(), "Protein"))
                    entries.add(PieEntry(carbsValue.toFloat(), "Carbs"))
                    entries.add(PieEntry(fatValue.toFloat(), "Fat"))

                    dataSet.notifyDataSetChanged()
                    chart.notifyDataSetChanged()
                    chart.invalidate()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) return
        if (h != null) {
            Log.i(
                "VAL SELECTED",
                "Value: " + e.y + ", index: " + h.x
                        + ", DataSet index: " + h.dataSetIndex
            )
        }
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }
}