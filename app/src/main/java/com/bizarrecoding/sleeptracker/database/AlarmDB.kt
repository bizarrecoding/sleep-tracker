package com.bizarrecoding.sleeptracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bizarrecoding.sleeptracker.models.Alarm
import com.bizarrecoding.sleeptracker.models.Day
import com.bizarrecoding.sleeptracker.models.Night

@Database(entities = [Alarm::class, Day::class, Night::class], version = 1, exportSchema = false)
abstract class AlarmDB: RoomDatabase() {

    abstract fun alarmDao(): DatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: AlarmDB? = null

        fun getInstance(ctx: Context): AlarmDB {
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        ctx.applicationContext,
                        AlarmDB::class.java,
                        "SleepTrackerDB"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                }
                return INSTANCE!!
            }
        }
    }
}