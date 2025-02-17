package com.sun.wow.repository

import com.sun.wow.entity.WowToken
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WowTokenRepository : JpaRepository<WowToken, Long> {
    fun findTop10ByOrderByLastUpdateTimestampDesc(pageable: Pageable): List<WowToken>
    fun findByLastUpdateTimestampBetween(startTimeMillis: Long, endTimeMillis: Long): List<WowToken>
}