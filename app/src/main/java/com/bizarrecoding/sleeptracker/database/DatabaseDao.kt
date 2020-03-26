package com.bizarrecoding.sleeptracker.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.bizarrecoding.sleeptracker.models.Alarm
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import com.bizarrecoding.sleeptracker.models.Day
import com.bizarrecoding.sleeptracker.models.Night

@Dao
interface DatabaseDao {

    //Alarms
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Query("SELECT * FROM alarms WHERE id_alarm = :id")
    suspend fun getAlarm(id: Int): Alarm?

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    //Days

    @Query("SELECT * FROM days WHERE alarm_id = :id")
    suspend fun getDaysOfAlarm(id: Int): List<Day>

    @Transaction
    suspend fun updateAlarmDays(alarm: Alarm, days: ArrayList<Day>){
        insertDaysFor(alarm.id_alarm,days)
        insertAlarm(alarm)
    }

    @Transaction
    suspend fun insertAlarmDays(alarm: Alarm, days: ArrayList<Boolean>){
        val id = insertAlarm(alarm)
        val alarmDays = ArrayList<Day>()
        for ((key, value) in days.withIndex()) {
            if (value)
                alarmDays.add(Day(key, id.toInt()))
        }
        insertDay(*alarmDays.toTypedArray())
    }

    @Transaction
    suspend fun insertDaysFor(alarmId: Int, days: ArrayList<Day>){
        deleteDaysWithAlarm(alarmId)
        days.forEach {
            Log.d("HRK_INSERT","Days ${it.alarm_id}-${it.day}")
        }
        insertDay(*days.toTypedArray())
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(vararg day: Day): List<Long>

    @Query("DELETE FROM days WHERE alarm_id = :id")
    suspend fun deleteDaysWithAlarm(id: Int)

    // AlarmWithDay

    @Transaction
    @Query("SELECT id_alarm, day as time, hour, min, enabled, a.name " +
            "FROM alarms a JOIN days d ON a.id_alarm = d.alarm_id " +
            "WHERE day > :day OR day = :day AND (hour > :hour OR (hour = :hour AND min > :min)) " +
            "UNION " +
            "SELECT id_alarm, day+7, hour, min, enabled, a.name " +
            "FROM alarms a JOIN days d ON a.id_alarm = d.alarm_id " +
            "WHERE day < :day OR day = :day AND (hour < :hour OR (hour = :hour AND min < :min)) " +
            "ORDER BY day, hour, min ASC")
    fun observeNextAlarmByDateTime(day: Int,hour: Int,min: Int): LiveData<AlarmWithDays?>

    @Transaction
    @Query("SELECT id_alarm, min(time) as time, hour, min, enabled, name FROM ( " +
                    "SELECT id_alarm, min(day) as time, hour, min, enabled, a.name " +
                    "FROM alarms a JOIN days d ON a.id_alarm = d.alarm_id " +
                    "WHERE day > :day OR day = :day AND (hour > :hour OR (hour = :hour AND min > :min)) AND enabled = 1 " +
                    "GROUP BY hour, min " +
                    "UNION " +
                    "SELECT id_alarm, min(day+7) as time, hour, min, enabled, a.name " +
                    "FROM alarms a JOIN days d ON a.id_alarm = d.alarm_id " +
                    "WHERE day < :day OR day = :day AND (hour < :hour OR (hour = :hour AND min < :min)) AND enabled = 1 " +
                    "GROUP BY hour, min " +
                    "ORDER BY time, hour, min ASC ) as sub "+
                "GROUP BY hour, min " +
                "ORDER BY time, hour, min ASC")
    fun observeAlarmsDateTime(day: Int,hour: Int,min: Int): LiveData<List<AlarmWithDays>>


    @Transaction
    @Query("SELECT id_alarm, day as time, hour, min, enabled, a.name " +
            "FROM alarms a JOIN days d ON a.id_alarm = d.alarm_id " +
            "WHERE d.day = :day AND (hour > :hour OR (hour = :hour AND min > :min)) AND enabled = 1 " +
            "ORDER BY day, hour, min ASC")
    fun observeAlarmsToday(day: Int,hour: Int,min: Int): LiveData<List<AlarmWithDays>>


    @Transaction
    suspend fun deleteAlarmWithDays(alarm: Alarm) {
        deleteDaysWithAlarm(alarm.id_alarm)
        deleteAlarm(alarm)
    }

    //Nights
    @Query("DELETE FROM Nights WHERE endTime IS NULL")
    suspend fun deleteIncompleteNights(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNight(night: Night): Long

    @Query("SELECT * FROM Nights WHERE endTime IS NULL")
    suspend fun getLastNight(): Night?

    @Query("SELECT * FROM Nights WHERE endTime IS NOT NULL LIMIT :limit")
    fun observeTopNights(limit: Int): LiveData<List<Night>>

    @Query("SELECT * FROM Nights WHERE endTime IS NULL")
    fun observeLastNight(): LiveData<Night?>

    @Query("SELECT AVG(duration) FROM Nights WHERE endTime IS NOT NULL")
    fun observeAvgDuration(): LiveData<Double>
}




