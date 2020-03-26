package com.bizarrecoding.sleeptracker.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.models.Night

class StatsViewModel (private val alarmRepo: AlarmRepository): ViewModel() {

    val lastNights: LiveData<List<Night>>
            get() = alarmRepo.observeTopNights(14)

}