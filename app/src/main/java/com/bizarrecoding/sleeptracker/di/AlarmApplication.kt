package com.bizarrecoding.sleeptracker.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.bizarrecoding.sleeptracker.database.AlarmDB
import com.bizarrecoding.sleeptracker.database.AlarmRepository
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.database.DatabaseDao
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AlarmApplication: Application(), KodeinAware{
    companion object {
        const val CHANNEL_NAME = "Alarm_Service"
    }

    override val kodein = Kodein.lazy {
        setupModule(this)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val channel = NotificationChannel(
                "${context.packageName}-$CHANNEL_NAME",
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setShowBadge(true)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun setupModule(builder: Kodein.MainBuilder): Kodein.MainBuilder{
        Log.d("KODEIN", "SETUP: Injection")
        return builder.apply{
            import(androidXModule(this@AlarmApplication))
            bind<AlarmDB>() with singleton { AlarmDB.getInstance(applicationContext) }
            bind<DatabaseDao>() with singleton { instance<AlarmDB>().alarmDao() }
            bind<AlarmRepository>() with singleton { AlarmRepositoryImpl(instance()) }
            bind() from provider { ViewModelFactory(instance()) }
        }
    }

    private fun setupTestModule(builder: Kodein.MainBuilder): Kodein.MainBuilder{
        Log.d("KODEIN", "SETUP: Testing injection")
        return builder.apply{
            bind<AlarmDB>() with singleton { AlarmDB.getInstance(applicationContext) }
            bind<DatabaseDao>() with singleton { instance<AlarmDB>().alarmDao() }
            bind<AlarmRepository>() with singleton { AlarmRepositoryImpl(instance()) }
        }
    }
}