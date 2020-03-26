package com.bizarrecoding.sleeptracker.ui.alarmList

import android.util.Log
import androidx.lifecycle.*
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import kotlinx.coroutines.launch
import java.util.*

class AlarmListViewModel(private val alarmRepo: AlarmRepository): ViewModel() {

    val alarms: LiveData<List<AlarmWithDays>>

    init {
        val cal = Calendar.getInstance().apply{
            time = Date(System.currentTimeMillis())
        }
        alarms = alarmRepo.observeAlarmsByDateTime(
            cal.get(Calendar.DAY_OF_WEEK)-1,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
        if(alarms.value!=null){
            alarms.value!!.forEach {
                Log.d("HRK_QUERY",it.alarm.toString() + it.activeDays())
            }
        }
    }

    fun deleteAlarm(alarm: AlarmWithDays){
        viewModelScope.launch {
            alarmRepo.deleteAlarmWithDays(alarm.alarm)
        }
    }
}
