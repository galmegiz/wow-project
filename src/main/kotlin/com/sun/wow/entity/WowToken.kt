package com.sun.wow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(
    name = "wow_token",
    indexes = [
        jakarta.persistence.Index(name = "idx_wow_token_last_update", columnList = "last_update_timestamp DESC")
    ]
)
data class WowToken(
    @Id
    @Column(name = "last_update_timestamp")
    val lastUpdateTimestamp: Long,
    val price: Long
)
