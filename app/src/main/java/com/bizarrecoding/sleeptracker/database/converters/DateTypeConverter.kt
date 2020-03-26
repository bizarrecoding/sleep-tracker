package com.bizarrecoding.sleeptracker.database.converters

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(dateLong: Long?): Date?{
        return dateLong?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}