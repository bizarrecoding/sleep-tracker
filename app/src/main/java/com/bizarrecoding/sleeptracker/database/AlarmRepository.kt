package com.bizarrecoding.sleeptracker.database

import androidx.lifecycle.LiveData
import com.bizarrecoding.sleeptracker.models.Alarm
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import com.bizarrecoding.sleeptracker.models.Day
import com.bizarrecoding.sleeptracker.models.Night

interface AlarmRepository{
    // Alarms
    suspend fun getAlarm(id: Int): Alarm?

    // Days
    suspend fun getDaysOfAlarm(id: Int): List<Day>

    //DayAlarm
    suspend fun insertAlarmDays(alarm: Alarm, activeDays: ArrayList<Boolean>)
    suspend fun updateAlarmDays(alarm: Alarm, days: ArrayList<Day>)
    fun observeAlarmsByDateTime(day: Int, hour: Int, minute: Int) : LiveData<List<AlarmWithDays>>
    fun observeNextByDateTime(day: Int, hour: Int, minute: Int) : LiveData<AlarmWithDays?>
    suspend fun deleteAlarmWithDays(alarm: Alarm)

    //Nights
    suspend fun deleteIncompleteNights(): Int
    suspend fun insertNight(night: Night): Long
    suspend fun getLastNight(): Night?
    fun observeTopNights(limit: Int): LiveData<List<Night>>
    fun observeLastNight(): LiveData<Night?>
    fun observeAvgDuration(): LiveData<Double>
}