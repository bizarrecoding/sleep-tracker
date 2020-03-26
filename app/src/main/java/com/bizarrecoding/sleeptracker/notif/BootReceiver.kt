package com.bizarrecoding.sleeptracker.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bizarrecoding.sleeptracker.database.AlarmDB
import com.bizarrecoding.sleeptracker.utils.AlarmUtils
import java.util.*

class BootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //TODO Schedule Alarms
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("HRK_BOOT", "BOOT COMPLETED Setting next alarm")
            val alarmDAO = AlarmDB.getInstance(context).alarmDao()
            val cal =  Calendar.getInstance()
            val alarm= alarmDAO.observeNextAlarmByDateTime(
                cal.get(Calendar.DAY_OF_WEEK),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
            )
            if(alarm.value!=null){
                AlarmUtils.build(context, AlarmUtils.toCalendar(alarm.value!!))
            }
        }
    }
}