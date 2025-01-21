package com.sun.wow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WowApplication

fun main(args: Array<String>) {
	runApplication<WowApplication>(*args)
}
