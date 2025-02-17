package com.sun.wow.repository

import org.springframework.stereotype.Repository

@Repository
class WowTokenRepositoryImpl : WowTokenRepository {
    override fun insertTokenPrice(tokenPrice: Long, lastUpdateTime: Long) {
        println("inserted")
    }
}