package com.bizarrecoding.sleeptracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bizarrecoding.sleeptracker.utils.TimeDifference
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Nights")
data class Night(
    var startTime: Long
){
    @PrimaryKey(autoGenerate = true)
    var nightID: Long = 0
    var endTime: Long? = null
    var duration: Double = 0.0


    fun getStartingDate(): String{
        val locale = Locale.getDefault().displayLanguage
        return if (locale == "español") {
            SimpleDateFormat("MMM d, YYYY", Locale.getDefault()).format(startTime).toString()
        }else{
            SimpleDateFormat("MMM d, YYYY", Locale.US).format(startTime).toString()
        }
    }

    fun formattedStartTime(): String {
        val locale = Locale.getDefault().displayLanguage
        return if (locale == "español") {
            SimpleDateFormat("HH:mm a", Locale.getDefault()).format(startTime).toString()
        }else{
            SimpleDateFormat("HH:mm a", Locale.US).format(startTime).toString()
        }
    }

    fun formattedEndTime() : String {
        val locale = Locale.getDefault().displayLanguage
        return if (locale == "español") {
            SimpleDateFormat("HH:mm a", Locale.getDefault()).format(endTime).toString()
        }else{
            SimpleDateFormat("HH:mm a", Locale.US).format(endTime).toString()
        }
    }

    fun calcDuration(): Double{
        return if (endTime!=null){
            duration = TimeDifference.delta(endTime!!-startTime)
            return duration
        }else{
            0.0
        }
    }

    override fun toString(): String {
        val ft = SimpleDateFormat("HH:mm a", Locale.US)
        return if (endTime != null){
            "from ${ft.format(startTime)} to ${ft.format(endTime)}\nDuration: ${calcDuration()}"
        }else{
            "from ${ft.format(startTime)} \nDuration: 0"
        }
    }
}
