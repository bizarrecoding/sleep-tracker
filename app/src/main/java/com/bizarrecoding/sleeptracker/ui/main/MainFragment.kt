package com.bizarrecoding.sleeptracker.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bizarrecoding.sleeptracker.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import com.bizarrecoding.sleeptracker.databinding.FragmentMainBinding
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import com.bizarrecoding.sleeptracker.utils.AlarmUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class MainFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    private val vmodel: MainViewModel by lazy {
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private var lastAlarm: Int? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = vmodel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //DataObservers
        vmodel.alarmWithDays.observe(viewLifecycleOwner, Observer { alarm ->
            if(alarm != null && alarm.hashCode()!=lastAlarm){
                AlarmUtils.build(this.requireContext(), AlarmUtils.toCalendar(alarm))
                Log.d("HRK_alarm", "Alarm at "+vmodel.alarmWithDays.value?.alarm?.getFormattedTime())
                lastAlarm = alarm.hashCode()
                vmodel.showSnackBar()
            }else{
                Log.d("HRK_ALARM","CHANGE NOT TRIGGERED")
            }
        })
        vmodel.displayMsg.observe(viewLifecycleOwner, Observer { display->
            if(display) {
                val alarm = vmodel.alarmWithDays.value!!.alarm
                val snackbarText = resources.getText(R.string.notification_snack)
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    String.format(snackbarText.toString(),alarm.getFormattedTime()),
                    Snackbar.LENGTH_SHORT
                ).show()
                vmodel.doneShowingSnackBar()
            }
        })

        //Click listeners
        stats.setOnClickListener{
            val action = MainFragmentDirections.toStatsFragment()
            findNavController().navigate(action)
        }
        CurrentAlarm.setOnClickListener {
            val action = MainFragmentDirections.toAlarmsList()
            findNavController().navigate(action)
        }
        edit.setOnClickListener {
            val alarm = vmodel.alarmWithDays.value!!.alarm
            val action = MainFragmentDirections.toAlarmEdit(alarm.id_alarm)
            findNavController().navigate(action)
        }
        newAlarm.setOnClickListener { view ->
            val action = MainFragmentDirections.toAlarmEdit(0)
            findNavController().navigate(action)
        }
        nightBtn.setOnClickListener {
            val action = MainFragmentDirections.startNight()
            findNavController().navigate(action)
        }
        test.setOnClickListener {
            setAlarm()
        }
    }


    private fun setAlarm(){
        val alarm = Calendar.getInstance().apply {
            add(Calendar.MINUTE,1)
        }
        AlarmUtils.build(context,alarm)
    }
}
