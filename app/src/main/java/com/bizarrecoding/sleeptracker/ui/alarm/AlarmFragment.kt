package com.bizarrecoding.sleeptracker.ui.alarm

import android.animation.Animator
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bizarrecoding.sleeptracker.R
import com.bizarrecoding.sleeptracker.databinding.FragmentAlarmBinding
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_alarm.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AlarmFragment : Fragment(), KodeinAware, CompoundButton.OnCheckedChangeListener {

    override val kodein by closestKodein()
    private val factory: ViewModelFactory by instance()

    private val vmodel: AlarmViewModel by lazy {
        ViewModelProvider(this, factory).get(AlarmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val id: Int = arguments?.getInt("alarm_id") ?: 0
        val binding = FragmentAlarmBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = vmodel
        vmodel.setAlarmId(id)
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmodel.alarm.observe(viewLifecycleOwner, Observer {
            Log.d("HRK LOADED ALARM","time: ${it.getFormattedTime()} ${it.enabled}")
        })
        title.setOnClickListener {
            with(AlertDialog.Builder(this.requireContext())){
                setTitle("Alert")
                setMessage("this is a message")
                setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
            }.show()
        }
        time.setOnClickListener {
            val (cHour, cMin) = vmodel.alarm.value!!
            val tpDialog = TimePickerDialog(this.context,TimePickerDialog.OnTimeSetListener { _, hour, min ->
                vmodel.setTime(hour,min)
            }, cHour, cMin,false)
            tpDialog.show()
        }
        day0.setOnCheckedChangeListener(this)
        day1.setOnCheckedChangeListener(this)
        day2.setOnCheckedChangeListener(this)
        day3.setOnCheckedChangeListener(this)
        day4.setOnCheckedChangeListener(this)
        day5.setOnCheckedChangeListener(this)
        day6.setOnCheckedChangeListener(this)
        enabled.setOnCheckedChangeListener(this)
        save.setOnClickListener {
            vmodel.saveAlarm()
        }
        delete.setOnClickListener {
            vmodel.deleteAlarm()
        }
        vmodel.taskComplete.observe(viewLifecycleOwner, Observer{ complete ->
            if (complete) findNavController().popBackStack()
        })
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView!!.id){
            R.id.day0 -> vmodel.setDay(0, isChecked)
            R.id.day1 -> vmodel.setDay(1, isChecked)
            R.id.day2 -> vmodel.setDay(2, isChecked)
            R.id.day3 -> vmodel.setDay(3, isChecked)
            R.id.day4 -> vmodel.setDay(4, isChecked)
            R.id.day5 -> vmodel.setDay(5, isChecked)
            R.id.day6 -> vmodel.setDay(6, isChecked)
            R.id.enabled -> vmodel.setEnabled(isChecked)
        }
    }
}
