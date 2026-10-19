package com.sidofa.anbardar.datalayer.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sidofa.anbardar.logiclayer.models.CartDto
import java.time.Instant

@Entity
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var customerName: String,
    var priceAmount: Long,
    val creationDate: Instant
)

fun Cart.toDto() : CartDto = CartDto(
    id = this.id,
    customerName = this.customerName,
    priceAmount = this.priceAmount,
    creationTime = this.creationDate
)
