package com.sun.wow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
class WowApplication

fun main(args: Array<String>) {
	runApplication<WowApplication>(*args)
}
