package com.sun.wow.config

import org.slf4j.LoggerFactory
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class LoggingInterceptor : ClientHttpRequestInterceptor {
    private val logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    override fun intercept(
        request: org.springframework.http.HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        // Log Request
        logger.info("Request URI: {}", request.uri)
        logger.info("Request Method: {}", request.method)
        logger.info("Request Headers: {}", request.headers)
        logger.info("Request Body: {}", String(body, StandardCharsets.UTF_8))

        val response = execution.execute(request, body)

        // Log Response
        /*val responseBody = BufferedReader(InputStreamReader(response.body, StandardCharsets.UTF_8)).readText()
        logger.info("Response Status Code: {}", response.statusCode)
        logger.info("Response Headers: {}", response.headers)
        logger.info("Response Body: {}", responseBody)*/
        return response
    }
}