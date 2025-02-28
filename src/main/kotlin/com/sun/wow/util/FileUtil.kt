package com.sun.wow.util

import java.io.File

fun readItemIdsFromFile(filePath: String): List<Long> {
    return File(filePath).readLines()
        .drop(1)
        .mapNotNull { line ->
            line.split(",")
                .getOrNull(1)
                ?.replace("\"", "")
                ?.trim()
                ?.toLongOrNull()
        }
}