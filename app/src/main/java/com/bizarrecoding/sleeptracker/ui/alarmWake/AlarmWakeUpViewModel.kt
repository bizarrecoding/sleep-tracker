package com.bizarrecoding.sleeptracker.ui.alarmWake

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class AlarmWakeUpViewModel(private val repo: AlarmRepository) : ViewModel() {
    val redProgress = MutableLiveData(0x8)
    val greenProgress = MutableLiveData(0x8)
    val blueProgress = MutableLiveData(0x8)

    val nextAlarm: LiveData<AlarmWithDays?>

    private val _targetColorInt = MutableLiveData<Int>(Color.WHITE)
    val targetColorInt: LiveData<Int>
        get() = _targetColorInt

    private val _currentProgressColor = MutableLiveData<String>()
    val currentProgressColor: LiveData<String>
        get() = _currentProgressColor

    private val _currentProgressColorInt = MutableLiveData<Int>(Color.WHITE)
    val currentProgressColorInt: LiveData<Int>
        get() = _currentProgressColorInt

    private val _finish = MutableLiveData(false)
    val finish: LiveData<Boolean>
        get() = _finish

    init{
        generateColor()
        calculateProgressColor()
        val cal = Calendar.getInstance()
        nextAlarm = repo.observeNextByDateTime(
            day = cal.get(Calendar.DAY_OF_WEEK) - 1,
            hour = cal.get(Calendar.HOUR_OF_DAY),
            minute = cal.get(Calendar.MINUTE)
        )
    }

    private fun generateColor(){
        val rng = Random(System.currentTimeMillis())
        val red = rng.nextInt(16)*0x11
        val green = rng.nextInt(16)*0x11
        val blue = rng.nextInt(16)*0x11
        _targetColorInt.value = Color.argb(255, red, green, blue)
        Log.d("HRK_AlarmColor", "${toHexString(red)}-${toHexString(green)}-${toHexString(blue)}")
    }

    fun calculateProgressColor(){
        val red = toHexString(redProgress.value!!*0x11)
        val green = toHexString(greenProgress.value!!*0x11)
        val blue = toHexString(blueProgress.value!!*0x11)
        _currentProgressColor.value = "#$red$green$blue"
        _currentProgressColorInt.value = Color.argb(
            255,
            redProgress.value!!*0x11,
            greenProgress.value!!*0x11,
            blueProgress.value!!*0x11
        )
        if(_currentProgressColorInt.value == _targetColorInt.value){
            Log.d("HRK_FOUND","#$red$green$blue")
        }
    }

    fun toHexString(color: Int): String{
        return "%2x".format(color).let { hex ->
            if (hex == " 0") "00" else hex
        }
    }

    fun finishEvent() {
        _finish.postValue(true)
    }

    fun endPendingNights() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastNight = repo.getLastNight()
            if(lastNight!=null){
                lastNight.endTime=System.currentTimeMillis()
                lastNight.calcDuration()
                repo.insertNight(lastNight)
            }
        }
    }

    companion object {
        fun generateColorTest() {
            val rng = Random(System.currentTimeMillis())
            val red = rng.nextInt(16)*0x11
            val green = rng.nextInt(16)*0x11
            val blue = rng.nextInt(16)*0x11
            println("(%d,%d,%d)".format(red,green,blue))
            println("#%x%x%x".format(red,green,blue))
        }
    }
}