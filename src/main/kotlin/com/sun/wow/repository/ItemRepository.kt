package com.sun.wow.repository

import com.sun.wow.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, Long> {
    // fun findByNameOrNull(name: String): Item?
}