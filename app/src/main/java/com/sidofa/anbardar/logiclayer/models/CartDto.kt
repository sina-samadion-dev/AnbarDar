package com.sidofa.anbardar.logiclayer.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.sidofa.anbardar.datalayer.entities.Cart
import java.time.Instant

class CartDto (
    val id : Int = 0,
    customerName : String = "",
    creationTime : Instant = Instant.now(),
    cartProducts : List<PurchasedProductDto> = emptyList<PurchasedProductDto>(),
    priceAmount : Long = 0
){
    var customerName by mutableStateOf(customerName)

    val creationDate by mutableStateOf(creationTime)

    val purchasedProducts = cartProducts.toMutableStateList()
    var priceAmount by mutableLongStateOf(priceAmount)
}

fun CartDto.toEntity(): Cart = Cart(
    id = this.id,
    customerName = this.customerName,
    creationDate = this.creationDate,
    priceAmount = this.priceAmount
)