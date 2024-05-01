package org.fossify.commons.extensions

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.text.format.Time
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Locale

fun Long.formatSize(): String {
    if (this <= 0) {
        return "0 B"
    }

    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(toDouble()) / Math.log10(1024.0)).toInt()
    return "${DecimalFormat("#,##0.#").format(this / Math.pow(1024.0, digitGroups.toDouble()))} ${units[digitGroups]}"
}

fun Long.formatDate(context: Context, dateFormat: String? = null, timeFormat: String? = null): String {
    val useDateFormat = dateFormat ?: context.baseConfig.dateFormat
    val useTimeFormat = timeFormat ?: context.getTimeFormat()
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this
    return DateFormat.format("$useDateFormat, $useTimeFormat", cal).toString()
}

// if the given date is today, we show only the time. Else we show the date and optionally the time too
fun Long.formatDateOrTime(context: Context, hideTimeAtOtherDays: Boolean, showYearEvenIfCurrent: Boolean): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this

    return if (DateUtils.isToday(this)) {
        DateFormat.format(context.getTimeFormat(), cal).toString()
    } else {
        var format = context.baseConfig.dateFormat
        if (!showYearEvenIfCurrent && isThisYear()) {
            format = format.replace("y", "").trim().trim('-').trim('.').trim('/')
        }

        if (!hideTimeAtOtherDays) {
            format += ", ${context.getTimeFormat()}"
        }

        DateFormat.format(format, cal).toString()
    }
}

fun Long.isThisYear(): Boolean {
    val time = Time()
    time.set(this)

    val thenYear = time.year
    time.set(System.currentTimeMillis())

    return (thenYear == time.year)
}

fun Long.toDayCode(format: String = "ddMMyy"): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this
    return DateFormat.format(format, cal).toString()
}
