package com.bizarrecoding.sleeptracker.ui.alarm

import android.util.Log
import androidx.lifecycle.*
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.models.Alarm
import com.bizarrecoding.sleeptracker.models.Day
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class AlarmViewModel(private val alarmRepo: AlarmRepository): ViewModel() {

    private val workScope = CoroutineScope(Dispatchers.IO + viewModelScope.coroutineContext)

    val name = MutableLiveData<String>(" Alarm 0")

    private val _alarm = MutableLiveData<Alarm>()

    val alarm: LiveData<Alarm>
        get() = _alarm
    private val _days = MutableLiveData<ArrayList<Boolean>>()
    val days: LiveData<ArrayList<Boolean>>
        get() = _days
    private val _taskComplete = MutableLiveData<Boolean>(false)
    val taskComplete: LiveData<Boolean>
        get() = _taskComplete

    init {
        val cal = Calendar.getInstance()
        val dayList = arrayListOf(false,false,false,false,false,false,false)
        dayList[cal.get(Calendar.DAY_OF_WEEK)-1] = true
        _days.value = dayList
        _alarm.value = Alarm(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
    }

    fun setAlarmId(id: Int) {
        workScope.launch {
            val foundAlarm = alarmRepo.getAlarm(id)
            if (foundAlarm != null) {
                val loadedState= arrayListOf(false,false,false,false,false,false,false)
                alarmRepo.getDaysOfAlarm(id).forEach {
                    loadedState[it.day] = true
                }
                _days.value = loadedState
                _alarm.value = foundAlarm
                name.value = foundAlarm.name
            }
        }
    }

    fun saveAlarm(){
        workScope.launch {
            if(alarm.value?.id_alarm==0){
                insertAlarm()
            }else{
                updateAlarm()
            }
            _taskComplete.value = true
        }
    }

    private suspend fun insertAlarm(){
        if(alarm.value!=null && name.value != null){
            _alarm.value?.name = name.value!!
            alarmRepo.insertAlarmDays(alarm.value!!, days.value!!)
        }
    }

    private suspend fun updateAlarm() {
        val alarmDays = ArrayList<Day>()
        for ((key, value) in days.value!!.withIndex()) {
            if (value)
                alarmDays.add(Day(key, alarm.value!!.id_alarm))
        }
        if(name.value != null){
            _alarm.value?.name = name.value!!
        }
        alarmRepo.updateAlarmDays(alarm.value!!, alarmDays)
    }

    fun deleteAlarm() {
        workScope.launch {
            if (alarm.value?.id_alarm != 0) alarmRepo.deleteAlarmWithDays(alarm.value!!)
            _taskComplete.value = true
        }
    }

    fun setDay(day: Int, isChecked: Boolean) {
        _days.value!![day] = isChecked
        if(_days.value!!.all { value-> !value }){
            _days.value = arrayListOf(true,true,true,true,true,true,true)
        }
    }

    fun setEnabled(enabled: Boolean) {
        val alarm = _alarm.value
        alarm?.enabled = enabled
        _alarm.value = alarm
    }

    fun setTime(newHour: Int, newMin: Int) {
        val alarm = _alarm.value
        alarm.apply {
            if (this != null) {
                hour = newHour
                min = newMin
            }
        }
        _alarm.value = alarm
    }

}