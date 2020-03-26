package com.bizarrecoding.sleeptracker.ui.alarmWake

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bizarrecoding.sleeptracker.R
import com.bizarrecoding.sleeptracker.database.AlarmDB
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.databinding.ActivityAlarmWakeUpBinding
import com.bizarrecoding.sleeptracker.notif.AlarmReceiver
import com.bizarrecoding.sleeptracker.notif.AlarmService
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import com.bizarrecoding.sleeptracker.utils.AlarmUtils
import com.bizarrecoding.sleeptracker.utils.TimeDifference
import kotlinx.android.synthetic.main.activity_alarm_wake_up.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer


class AlarmWakeUpActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val factory: ViewModelFactory by instance()

    private val alarmTask = Job()
    private val scope = CoroutineScope(Dispatchers.IO+alarmTask)

    //private lateinit var alarm: Ringtone

    private val vmodel  by lazy {
        ViewModelProvider(this, factory).get(AlarmWakeUpViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAlarmWakeUpBinding>(this,R.layout.activity_alarm_wake_up)
        binding.lifecycleOwner = this@AlarmWakeUpActivity
        binding.model = vmodel

        target_color.setBackgroundColor(vmodel.targetColorInt.value!!)
        intent.run {
            val hour = extras!!.getInt("Hours",0)
            val min = extras!!.getInt("Minutes",0)
            wakeTime.text = TimeDifference.formatTime(hour, min,false)
        }

        /*alarm = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM).let { uri ->
           RingtoneManager.getRingtone(this, uri)
        }
        playSound()*/
        wakeBtn.apply {
            isEnabled = false
            isClickable = false
            setOnClickListener {
                //alarm.stop()
                vmodel.endPendingNights()
                scheduleNext()
                stopService(Intent(applicationContext, AlarmService::class.java))
                cancelNotifications()
            }
        }
        snoozeBtn.setOnClickListener {
            //alarm.stop()
            stopService(Intent(applicationContext, AlarmService::class.java))
            cancelNotifications()
        }
        vmodel.nextAlarm.observe(this, Observer {
            if(it!=null) {
                val alarm = AlarmUtils.toCalendar(it).time
                Log.d("HRK_RESCHEDULE",SimpleDateFormat("HH:mm aa - dd", Locale.US).format(alarm))
            }
        })
        vmodel.redProgress.observe(this, Observer {
            vmodel.calculateProgressColor()
        })
        vmodel.greenProgress.observe(this, Observer {
            vmodel.calculateProgressColor()
        })
        vmodel.blueProgress.observe(this, Observer {
            vmodel.calculateProgressColor()
        })
        vmodel.currentProgressColorInt.observe(this, Observer {
            if (it==vmodel.targetColorInt.value ) {
                wakeBtn.apply{
                    isEnabled = true
                    isClickable = true
                    val accent= resources.getColor(R.color.colorAccent,null)
                    backgroundTintList = ColorStateList.valueOf(accent)
                    Log.d("HRK_WAKE","Enabled")
                }
            }else{
                wakeBtn.apply{
                    isEnabled = false
                    isClickable = false
                }
            }
        })
        vmodel.finish.observe(this, Observer { mustFinish ->
            if (mustFinish) finish()
        })
    }

    private fun cancelNotifications() {
        val alarmNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmNotificationManager.cancel(AlarmReceiver.NOTIFICATION_ID)
    }

    private fun scheduleNext() {
        scope.launch {
            val nextAlarm = vmodel.nextAlarm.value
            Log.d("HRK_Schedule","Time for alarm is ${nextAlarm?.alarm?.getFormattedTime()}")
            if (nextAlarm!=null){
                //AlarmUtils.toCalendar(nextAlarm)
                val cal = AlarmUtils.toCalendar(nextAlarm)
                //cal.add(Calendar.MINUTE,2)
                AlarmUtils.build(applicationContext,cal)
                vmodel.finishEvent()
            }else {
                Log.d("HRK_Schedule","NULL ALARM")
            }
        }
    }

    /*private fun playSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            alarm.isLooping = true
            alarm.play()
        }else{
            timer(null,false,0,5000){
                alarm.play()
            }
        }
    }*/
}