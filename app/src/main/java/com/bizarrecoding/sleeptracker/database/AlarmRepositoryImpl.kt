package com.bizarrecoding.sleeptracker.database

import com.bizarrecoding.sleeptracker.models.Alarm
import com.bizarrecoding.sleeptracker.models.Day
import com.bizarrecoding.sleeptracker.models.Night

class AlarmRepositoryImpl(private val dao: DatabaseDao) : AlarmRepository {

    // Alarms
    override suspend fun getAlarm(id: Int) = dao.getAlarm(id)

    // Days
    override suspend fun getDaysOfAlarm(id: Int) = dao.getDaysOfAlarm(id)

    //DayAlarm
    override suspend fun insertAlarmDays(alarm: Alarm, activeDays: ArrayList<Boolean>) = dao.insertAlarmDays(alarm,activeDays)

    override suspend fun updateAlarmDays(alarm: Alarm, days: ArrayList<Day>) = dao.updateAlarmDays(alarm,days)

    override fun observeAlarmsByDateTime(day: Int, hour: Int, minute: Int) = dao.observeAlarmsDateTime(day,hour,minute)

    override fun observeNextByDateTime(day: Int, hour: Int, minute: Int) = dao.observeNextAlarmByDateTime(day,hour,minute)

    override suspend fun deleteAlarmWithDays(alarm: Alarm) = dao.deleteAlarmWithDays(alarm)

    //Nights

    override suspend fun deleteIncompleteNights() = dao.deleteIncompleteNights()

    override suspend fun insertNight(night: Night) = dao.insertNight(night)

    override suspend fun getLastNight() = dao.getLastNight()

    override fun observeTopNights(limit: Int) = dao.observeTopNights(limit)

    override fun observeLastNight() = dao.observeLastNight()

    override fun observeAvgDuration() = dao.observeAvgDuration()
}