package com.bizarrecoding.sleeptracker.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.ui.alarmList.AlarmListViewModel
import com.bizarrecoding.sleeptracker.ui.alarm.AlarmViewModel
import com.bizarrecoding.sleeptracker.ui.alarmWake.AlarmWakeUpViewModel
import com.bizarrecoding.sleeptracker.ui.main.MainViewModel
import com.bizarrecoding.sleeptracker.ui.night.NightViewModel
import com.bizarrecoding.sleeptracker.ui.stats.StatsViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repo: AlarmRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass){
        when{
            isAssignableFrom(MainViewModel::class.java)->
                MainViewModel(repo)
            isAssignableFrom(AlarmListViewModel::class.java)->
                AlarmListViewModel(repo)
            isAssignableFrom(AlarmViewModel::class.java)->
                AlarmViewModel(repo)
            isAssignableFrom(AlarmWakeUpViewModel::class.java)->
                AlarmWakeUpViewModel(repo)
            isAssignableFrom(NightViewModel::class.java)->
                NightViewModel(repo)
            isAssignableFrom(StatsViewModel::class.java)->
                StatsViewModel(repo)
            else->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

}