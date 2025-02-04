package com.sun.wow.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        // restTemplate.interceptors.add(LoggingInterceptor())
        return restTemplate
    }
}