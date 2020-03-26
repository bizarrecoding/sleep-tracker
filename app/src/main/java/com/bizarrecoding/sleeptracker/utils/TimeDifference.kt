package com.bizarrecoding.sleeptracker.utils

import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class TimeDifference {
    companion object {
        private fun calculate(timeDifference: Long): List<Int> {
            val days = timeDifference / (1000 * 60 * 60 * 24)
            val hours = timeDifference / (1000 * 60 * 60) - days * 24
            val minutes = timeDifference / (1000 * 60) - days * 24 * 60 - hours * 60
            return listOf(days.toInt(), hours.toInt(), minutes.toInt())
        }

        fun delta(timeDifference: Long): Double = timeDifference.toDouble() / (1000 * 60 * 60)

        fun toDateString(date: Long): String = SimpleDateFormat("yyyy-MM-dd HH:mm aa",Locale.US).format(Date(date))

        fun getTimeUntilNextAlarm(alarm: AlarmWithDays): String {
            val now = System.currentTimeMillis()//Date()
            val cal = AlarmUtils.toCalendar(alarm)
            val diff = calculate(cal.timeInMillis - now)
            return formatDiff(diff)
        }

        fun formatTime(hours: Int, minutes: Int, h24: Boolean): String {
            val min = if (minutes < 10) "0$minutes" else minutes.toString()
            return when {
                h24 -> {
                    "$hours:$min"
                }
                hours > 12 -> {
                    val hour12 = hours - 12
                    "$hour12:$min pm"
                }
                else -> {
                    "$hours:$min am"
                }
            }
        }

        private fun formatDiff(diff: List<Int>): String {
            val labels = listOf("D", "H", "M")
            val results = StringBuilder().apply {
                append("(")
                for (i in diff.indices) {
                    append(formatDiffPiece(diff[i], labels[i]))
                }
            }
            if (results.toString() == "(") {
                results.append("1m")
            }
            return "${results.trim()})"
        }

        private fun formatDiffPiece(count: Int, term: String): String {
            return if (count >= 1) {
                "$count$term "
            } else {
                ""
            }
        }
    }
}