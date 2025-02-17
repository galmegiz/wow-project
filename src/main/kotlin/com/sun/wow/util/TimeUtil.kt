package com.sun.wow.util

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.convertToMillis() = this.toInstant(ZoneOffset.UTC).toEpochMilli()