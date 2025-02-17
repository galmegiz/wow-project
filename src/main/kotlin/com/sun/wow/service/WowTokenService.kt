package com.sun.wow.service

import com.sun.wow.client.WowTokenClient
import com.sun.wow.dto.WowTokenResponse
import com.sun.wow.repository.WowTokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class WowTokenService(
    private val wowTokenClient: WowTokenClient,
    private val wowTokenRepositoryImpl: WowTokenRepository,
) {
    private var lastTokenUpdatedTime: Long = 0L

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    fun updateTokenPricePeriodically() {
        val tokenIndex: WowTokenResponse = wowTokenClient.getCurrentTokenIndex()
        if(tokenIndex.lastUpdateTimeStamp > lastTokenUpdatedTime){
            this.lastTokenUpdatedTime = tokenIndex.lastUpdateTimeStamp
            wowTokenRepositoryImpl.insertTokenPrice(tokenIndex.price, lastTokenUpdatedTime)
        }
    }

    fun getTokenPriceHistory(): List<WowTokenResponse> {
        return emptyList()
    }
}