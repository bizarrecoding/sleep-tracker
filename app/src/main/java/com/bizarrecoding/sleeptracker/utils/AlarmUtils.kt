package com.bizarrecoding.sleeptracker.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bizarrecoding.sleeptracker.notif.AlarmReceiver
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import com.bizarrecoding.sleeptracker.models.Day
import java.util.*


object AlarmUtils {

    val ACTION_ALARM = "com.bizarrecoding.sleeptracker.wakeup"

    fun build(appContext: Context?, alarm: Calendar) {
        val alarmMgr = appContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(appContext, AlarmReceiver::class.java).apply {
            action = ACTION_ALARM
            putExtra("Hours", alarm.get(Calendar.HOUR_OF_DAY))
            putExtra("Minutes", alarm.get(Calendar.MINUTE))
        }.let { intent ->
            PendingIntent.getBroadcast(appContext, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.timeInMillis,
            alarmIntent
        )
    }

    fun toCalendar(alarm: AlarmWithDays): Calendar {
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY, alarm.alarm.hour)
            set(Calendar.MINUTE, alarm.alarm.min)
            set(Calendar.SECOND, 0)
        }

        var weekday = cal.get(Calendar.DAY_OF_WEEK)-1
        val validDays = activeDaysOf(alarm.days)
        //TODO: check actual date of parsed calendar
        while (cal.timeInMillis-now<0 || validDays[weekday%7]==0){
            weekday++
            cal.add(Calendar.DAY_OF_MONTH,1)
        }
        return cal
    }

    private fun activeDaysOf(days: List<Day>?): List<Int> {
        val activeDays = mutableListOf(0,0,0,0,0,0,0)
        days?.forEach {
            activeDays[it.day]=1
        }
        return activeDays
    }
}