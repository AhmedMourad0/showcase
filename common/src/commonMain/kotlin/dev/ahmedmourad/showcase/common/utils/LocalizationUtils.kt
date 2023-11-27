package dev.ahmedmourad.showcase.common.utils

import dev.ahmedmourad.showcase.common.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

fun Int.format(): String = RR.strings.d.desc(this).get()
fun Long.format(): String = RR.strings.d.desc(this).get()

fun LocalDateTime.format(): String {
    return listOf(
        date.format(),
        "  ",
        time.format()
    ).let {
        if (isRtl()) it.asReversed() else it
    }.joinToString(separator = "")
}

fun LocalDate.format(): String {
    return listOf(
        year.format().padStart(4, 0.format().first()),
        '-',
        monthNumber.format().padStart(2, 0.format().first()),
        '-',
        dayOfMonth.format().padStart(2, 0.format().first())
    ).let {
        if (isRtl()) it.asReversed() else it
    }.joinToString(separator = "")
}

fun LocalTime.format(): String {
    return listOf(
        if (hour < 12) {
            if (hour == 0) 12 else hour
        } else {
            if (hour == 12) 12 else hour - 12
        }.format().padStart(2, 0.format().first()),
        ":",
        minute.format().padStart(2, 0.format().first())
    ).joinToString(separator = "").plus(" ").plus(if (hour < 12) {
        RR.strings.am.desc().get()
    } else {
        RR.strings.pm.desc().get()
    })
}
