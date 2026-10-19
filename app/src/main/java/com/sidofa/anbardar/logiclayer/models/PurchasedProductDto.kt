package com.sidofa.anbardar.logiclayer.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sidofa.anbardar.datalayer.entities.PurchasedProduct
import com.sidofa.anbardar.utils.enums.ProductUnitTypes

class PurchasedProductDto(
    val id : Int = 0,
    val cartId : Long = 0,
    purchasedAmount : Int = 0,
    productName: String = "",
    unitType: ProductUnitTypes = ProductUnitTypes.ByCount,
    unitPrice: Int = 0,
    unitRevenue: Int = 0
) {
    var purchasedAmount by mutableIntStateOf(purchasedAmount)
    val productName by mutableStateOf(productName)
    val unitType by mutableStateOf(unitType)
    val unitPrice by mutableIntStateOf(unitPrice)
    val unitRevenue by  mutableIntStateOf(unitRevenue)
}



fun PurchasedProductDto.toEntity() : PurchasedProduct = PurchasedProduct(
    id = this.id,
    cartId = this.cartId,
    purchasedAmount = this.purchasedAmount,
    productName = this.productName,
    unitType = this.unitType,
    unitPrice = this.unitPrice,
    unitRevenue = this.unitRevenue,
)