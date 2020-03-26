package com.bizarrecoding.sleeptracker.ui.main

import android.util.Log
import androidx.core.math.MathUtils
import java.util.*
import androidx.lifecycle.*
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import kotlin.math.roundToInt

class MainViewModel(alarmRepo: AlarmRepository): ViewModel() {

    val alarmWithDays: LiveData<AlarmWithDays?>

    val average: LiveData<Double>

    val progress: LiveData<Int>
        get() = Transformations.map(average){
            if (it != null) (it*100/8).roundToInt() else 0
        }

    val deficit: LiveData<String>
        get() = Transformations.map(average){
            if (it != null) (it-8).toString() else "8"
        }

    val present: LiveData<Boolean>
        get() = Transformations.switchMap(alarmWithDays){
            return@switchMap MutableLiveData(it != null)
        }

    val statsPresent: LiveData<Boolean>
        get() = Transformations.switchMap(average){
            return@switchMap MutableLiveData(it != null && it > 0)
        }

    private val _displayMsg = MutableLiveData<Boolean>(false)
    val displayMsg: LiveData<Boolean>
        get() = _displayMsg

    init {
        val cal = Calendar.getInstance()
        Log.d("HRK NEAREST","next from ${cal.get(Calendar.DAY_OF_WEEK)} ${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}")
        alarmWithDays = alarmRepo.observeNextByDateTime(
            day = cal.get(Calendar.DAY_OF_WEEK)-1,
            hour = cal.get(Calendar.HOUR_OF_DAY),
            minute = cal.get(Calendar.MINUTE)
        )
        average = alarmRepo.observeAvgDuration()

    }

    fun doneShowingSnackBar() {
        _displayMsg.value=false
    }

    fun showSnackBar() {
        _displayMsg.value=true
    }
}