package com.sun.wow.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.convertToMillis() = this.toInstant(ZoneOffset.UTC).toEpochMilli()
fun String.convertToLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.RFC_1123_DATE_TIME
    val zoneDateTime = ZonedDateTime.parse(this, formatter)
    return zoneDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
}