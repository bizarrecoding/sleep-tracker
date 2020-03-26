package com.bizarrecoding.sleeptracker.notif

import android.app.*
import android.content.Intent
import android.media.*
import android.util.Log

class AlarmService : IntentService("AlarmService") {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("HRK_AlarmService","Intent Received")
        val uri = RingtoneManager.getActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_ALARM)
        val mediaPlayer = MediaPlayer().apply {
            val attr = AudioAttributes.Builder().apply {
                setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                setUsage(AudioAttributes.USAGE_ALARM)
            }.build()
            setAudioAttributes(attr)
            setDataSource(applicationContext, uri)
            isLooping = true
        }
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}