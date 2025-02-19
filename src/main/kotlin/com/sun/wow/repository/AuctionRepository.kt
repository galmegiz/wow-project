package com.sun.wow.repository

import com.sun.wow.entity.Auction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuctionRepository : JpaRepository<Auction, Long>{
}