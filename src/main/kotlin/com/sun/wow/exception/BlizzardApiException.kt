package com.sun.wow.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class BlizzardApiException(val httpStatusCode: HttpStatusCode) : RuntimeException() {
}