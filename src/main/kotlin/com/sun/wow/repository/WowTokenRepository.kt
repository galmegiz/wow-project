package com.sun.wow.repository

interface WowTokenRepository {
    fun insertTokenPrice(tokenPrice: Long, lastUpdateTime: Long)
}