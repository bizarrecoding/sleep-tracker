package com.bizarrecoding.sleeptracker.notif

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bizarrecoding.sleeptracker.R
import com.bizarrecoding.sleeptracker.ui.alarmWake.AlarmWakeUpActivity
import com.bizarrecoding.sleeptracker.ui.stats.StatsFragment
import com.bizarrecoding.sleeptracker.utils.AlarmUtils
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_ALARM = "com.bizarrecoding.sleeptracker.wakeup"
        const val CHANNEL_NAME = "Alarm_Service"
        const val NOTIFICATION_ID = 1709
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("HRK_AlarmReceiver","Alarm Intent Received: ${intent.action}")
        if (intent.action == ACTION_ALARM) {
            context.startService(Intent(context, AlarmService::class.java))
            val wakeIntent =  getWakeUpIntent(context,intent)
            val notification = getNotification(context,wakeIntent)
            setNotification(context,notification)
            context.startActivity(wakeIntent)
        }else{
            Log.d("HRK_AlarmReceiver","ACTION MISSED: ${intent.action}")
        }
    }

    private fun setNotification(ctx: Context, notification: Notification){
        val alarmNotificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "${ctx.packageName}-${CHANNEL_NAME}",
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            }
            alarmNotificationManager.createNotificationChannel(channel)
        }
        alarmNotificationManager.notify(NOTIFICATION_ID,notification)
    }

    private fun getNotification(ctx: Context, wakeIntent: Intent): Notification {
        val nBuilder = NotificationCompat.Builder(
            ctx,
            "${ctx.packageName}-${CHANNEL_NAME}"
        )
        return nBuilder.apply {
            val title = ctx.resources.getString(R.string.notification_title)
            val message = ctx.resources.getString(R.string.notification_msg)
            setContentTitle(title)
            setSmallIcon(R.drawable.android_icon)
            setStyle(NotificationCompat.BigTextStyle().bigText(message))
            setContentText(message)
            setOngoing(true)
            setAutoCancel(false)
            setContentIntent(
                PendingIntent.getActivity(
                    ctx,
                    Activity.RESULT_OK,
                    wakeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            priority = NotificationCompat.PRIORITY_DEFAULT
        }.build()
    }

    private fun getWakeUpIntent(ctx: Context, intent: Intent): Intent{
        val bundle = intent.extras
        val cal = Calendar.getInstance()
        var hours: Int = cal.get(Calendar.HOUR_OF_DAY)
        var mins: Int = cal.get(Calendar.MINUTE)
        if(bundle != null){
            hours = bundle.getInt("Hours",0)
            mins = bundle.getInt("Minutes",0)
        }
        return Intent(ctx, AlarmWakeUpActivity::class.java).apply {
            putExtra("Hours", hours)
            putExtra("Minutes", mins)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        }
    }
}