package com.bizarrecoding.sleeptracker.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bizarrecoding.sleeptracker.R
import java.util.*

@Entity(tableName = "days")
data class Day(
    val day: Int,
    val alarm_id: Int
){
    @PrimaryKey(autoGenerate = true)
    var id_day: Int = 0

    fun getShortName(): String{
        val locale = Locale.getDefault().displayLanguage
        //Log.d("HRK LOCALE", locale)
        return if(locale=="español"){
            when(day%7){
                0->"Do"
                1->"Lu"
                2->"Ma"
                3->"Mi"
                4->"Ju"
                5->"Vi"
                6->"Sa"
                else -> "Do"
            }
        }else{
            when(day%7){
                0->"Su"
                1->"Mo"
                2->"Tu"
                3->"We"
                4->"Th"
                5->"Fr"
                6->"Sa"
                else -> "Su"
            }
        }
    }
}
