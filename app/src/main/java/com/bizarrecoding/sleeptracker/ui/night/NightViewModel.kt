package com.bizarrecoding.sleeptracker.ui.night

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.models.Night
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NightViewModel(private val alarmRepo: AlarmRepository): ViewModel() {

    private val _turnBack = MutableLiveData(false)
    val turnBack: LiveData<Boolean>
        get() = _turnBack

    val night: LiveData<Night?> = alarmRepo.observeLastNight()

    init {
        viewModelScope.launch(Dispatchers.IO){
            val newNight = Night(System.currentTimeMillis())
            Log.d("HRK_NIGHT_NEW", newNight.toString())
            alarmRepo.deleteIncompleteNights()
            alarmRepo.insertNight(newNight)
        }
    }

    fun updateNight(){
        viewModelScope.launch(Dispatchers.IO) {
            val finishedNight = alarmRepo.getLastNight()
            if (finishedNight!=null){
                finishedNight.endTime = System.currentTimeMillis()
                finishedNight.calcDuration()
                Log.d("HRK_NIGHT_UPDATE", finishedNight.toString())
                alarmRepo.insertNight(finishedNight)
            }else{
                Log.d("HRK_NIGHT_UPDATE", "NULL")
            }
            turnBackToMain()
        }
    }

    fun cancelNight(){
        viewModelScope.launch(Dispatchers.IO){
            val deleted = alarmRepo.deleteIncompleteNights()
            Log.d("HRK_NIGHT_CANCEL", "DELETE unfinished nights: $deleted")
            turnBackToMain()
        }
    }

    private suspend fun turnBackToMain(){
        delay(100)
        _turnBack.postValue(true)
    }
}