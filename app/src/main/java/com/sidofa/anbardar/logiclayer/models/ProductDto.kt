package com.sidofa.anbardar.logiclayer.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sidofa.anbardar.datalayer.entities.Product
import com.sidofa.anbardar.logiclayer.services.ProductsServices
import com.sidofa.anbardar.utils.enums.ProductUnitTypes

class ProductDto (
    val id : Int = 0,
    productName: String = "",
    unitType : ProductUnitTypes = ProductUnitTypes.ByCount,
    amount: Int = 0,
    unitPrice: Int = 0,
    unitRevenue: Int = 0,
    barcode: String = ""

){
    var productName by mutableStateOf(productName)
    var unitType by mutableStateOf(unitType)
    var amount by mutableIntStateOf(amount)
    var unitPrice by mutableIntStateOf(unitPrice)
    var unitRevenue by mutableIntStateOf(unitRevenue)
    var barcode by mutableStateOf(barcode)
}


fun ProductDto.toEntity() : Product = Product(
    id = this.id,
    productName = this.productName,
    unitType = this.unitType,
    amount = this.amount,
    unitPrice = this.unitPrice,
    unitRevenue = this.unitRevenue,
    barcode = this.barcode
)