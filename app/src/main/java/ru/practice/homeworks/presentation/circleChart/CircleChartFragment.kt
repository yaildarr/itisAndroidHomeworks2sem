package ru.practice.homeworks.presentation.circleChart

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.app.view.ChartView
import dev.androidbroadcast.vbpd.viewBinding
import ru.practice.homeworks.R
import ru.practice.homeworks.databinding.FragmentCatBinding
import ru.practice.homeworks.databinding.FragmentCatBinding.bind
import ru.practice.homeworks.databinding.FragmentCircleChartBinding

class CircleChartFragment : Fragment(R.layout.fragment_circle_chart){


    private val binding: FragmentCircleChartBinding by viewBinding(FragmentCircleChartBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chartView = view.findViewById<ChartView>(R.id.chart)

        chartView.setup(
            sectorCount = 5,
            colors = listOf(
                ContextCompat.getColor(context, R.color.sector_1),
                ContextCompat.getColor(context, R.color.sector_2),
                ContextCompat.getColor(context, R.color.sector_3),
                ContextCompat.getColor(context, R.color.sector_4),
                ContextCompat.getColor(context, R.color.sector_5)
            )
        )
    }
}