package com.bizarrecoding.sleeptracker.ui.stats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizarrecoding.sleeptracker.R
import com.bizarrecoding.sleeptracker.databinding.FragmentStatsBinding
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_stats.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*


class StatsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val factory: ViewModelFactory by instance()

    private val vmodel: StatsViewModel by lazy {
        ViewModelProvider(this, factory).get(StatsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStatsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = vmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nightAdapter = NightsAdapter(this.requireContext())
        nightList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = nightAdapter
            addItemDecoration(DividerDecorator(requireContext()))
        }

        setupChart()

        vmodel.lastNights.observe(viewLifecycleOwner, Observer { recentNights ->
            Log.d("HRK_NIGHT_STATS", "updated list")
            val cal = Calendar.getInstance()
            val dataPoints = recentNights.map { night ->
                cal.timeInMillis = night.startTime
                DataPoint(cal.time, night.calcDuration())
            }
            val series = LineGraphSeries<DataPoint>(dataPoints.toTypedArray())
            series.color = resources.getColor(R.color.colorAccent2,null)
            sleepChart2.addSeries(series)

            nightAdapter.updateDataSet(recentNights)
        })
    }

    private fun setupChart(){
        sleepChart2.apply {
            val white = resources.getColor(android.R.color.white,null)
            //val accent = resources.getColor(R.color.colorAccent,null)
            setBackgroundColor(resources.getColor(R.color.backgroundCard,null))
            setPadding(10)
            gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(requireActivity())
            gridLabelRenderer.numHorizontalLabels = 3
            gridLabelRenderer.gridColor = white
            gridLabelRenderer.horizontalLabelsColor = white
            gridLabelRenderer.verticalLabelsColor = white
        }
    }
}
