package com.bizarrecoding.sleeptracker

import com.bizarrecoding.sleeptracker.models.Alarm
import com.bizarrecoding.sleeptracker.models.Day
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import com.bizarrecoding.sleeptracker.ui.alarmWake.AlarmWakeUpViewModel
import com.bizarrecoding.sleeptracker.utils.TimeDifference
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var alarmDays: AlarmWithDays

    @Before
    fun setupAlarm() {
        val cal = Calendar.getInstance().apply{
            add(Calendar.MINUTE, 1)
        }
        val alarm = Alarm(
            hour = cal.get(Calendar.HOUR_OF_DAY)+4 % 24,
            min = cal.get(Calendar.MINUTE)+25 % 60
        )
        val dayIndex = cal.get(Calendar.DAY_OF_WEEK).plus(2).rem(7)
        alarmDays = AlarmWithDays(
            alarm = alarm,
            days = listOf(
                Day(dayIndex,0)
            )
        )
    }


    @Test
    fun timeUntilNextAlarm() {
        assertEquals("(3days 4h 25min)", TimeDifference.getTimeUntilNextAlarm(alarmDays))
    }

    @Test
    fun generatesColor(){
        AlarmWakeUpViewModel.generateColorTest()
        assertNotNull(true)
    }


}
