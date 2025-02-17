package com.sun.wow.service

import com.sun.wow.client.WowTokenClient
import com.sun.wow.entity.WowToken
import com.sun.wow.repository.WowTokenRepository
import com.sun.wow.util.convertToMillis
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class WowTokenService(
    private val wowTokenClient: WowTokenClient,
    private val wowTokenRepository: WowTokenRepository,
) {
    private var lastTokenUpdatedTime: Long = 0L

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    fun updateTokenPricePeriodically() {
        val token: WowToken = wowTokenClient.getCurrentTokenIndex()
        if(token.lastUpdateTimestamp > lastTokenUpdatedTime){
            this.lastTokenUpdatedTime = token.lastUpdateTimestamp
            wowTokenRepository.save(token)
        }
    }

    fun getTokenPriceBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<WowToken> {
        val startTimeMillis: Long = startDateTime.convertToMillis()
        val endTimeMillis: Long = endDateTime.convertToMillis()
        return wowTokenRepository.findByLastUpdateTimestampBetween(startTimeMillis, endTimeMillis)
    }

    fun getTokenPriceHistory(pageable: Pageable): List<WowToken> {
        return wowTokenRepository.findTop10ByOrderByLastUpdateTimestampDesc(pageable)
    }
}