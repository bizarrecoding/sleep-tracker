package com.bizarrecoding.sleeptracker.models

import android.util.Log
import androidx.room.*
import com.bizarrecoding.sleeptracker.utils.TimeDifference
import java.text.SimpleDateFormat
import java.util.*


@Entity(tableName = "alarms", indices = [
    Index(value = ["hour", "min"], unique = true)]
)
data class Alarm(
    var hour: Int,
    var min: Int
){
    @PrimaryKey(autoGenerate = true)
    var id_alarm: Int = 0
    var time: Int = 0
    var enabled: Boolean = true
    var name: String = "Alarm $id_alarm"

    fun getFormattedTime(): String {
        val cal= Calendar.getInstance().apply {
            this.set(Calendar.HOUR_OF_DAY, hour)
            this.set(Calendar.MINUTE, min)
        }
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(cal.time)
    }
}

data class AlarmWithDays(
    @Embedded
    var alarm: Alarm,
    @Relation(parentColumn = "id_alarm", entityColumn = "alarm_id", entity = Day::class)
    var days: List<Day>
){
    fun timeRemaining() = TimeDifference.getTimeUntilNextAlarm(this)

    fun activeDays(): String{
        return days.joinToString {
            it.getShortName()
        }
    }
}