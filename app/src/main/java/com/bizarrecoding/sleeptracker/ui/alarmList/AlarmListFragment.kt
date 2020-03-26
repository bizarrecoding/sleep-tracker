package com.bizarrecoding.sleeptracker.ui.alarmList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bizarrecoding.sleeptracker.database.AlarmDB
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.databinding.FragmentAlarmListBinding
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_alarm_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AlarmListFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val factory: ViewModelFactory by instance()

    private val alarmsViewModel: AlarmListViewModel by lazy{
        ViewModelProvider(this, factory).get(AlarmListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding =  FragmentAlarmListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = alarmsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AlarmListAdapter(requireContext()).apply {
            deleteListener = AlarmListAdapter.AlarmClickListener{
                alarmsViewModel.deleteAlarm(it)
            }
        }
        alarmList.layoutManager = LinearLayoutManager(requireContext())
        alarmList.adapter = adapter

        //TODO delete:
        alarmsViewModel.alarms.observe(viewLifecycleOwner, Observer {
            adapter.setAlarms(it)
        })
    }
}
