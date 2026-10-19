package com.sidofa.anbardar.datalayer.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.utils.enums.ProductUnitTypes

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productName: String,
    val unitType: ProductUnitTypes,
    var amount: Int,
    var unitPrice: Int,
    var unitRevenue: Int,
    val barcode: String
)


fun Product.toDto() : ProductDto = ProductDto(
    id = this.id,
    productName = this.productName,
    unitType = this.unitType,
    amount = this.amount,
    unitPrice = this.unitPrice,
    unitRevenue = this.unitRevenue,
    barcode = this.barcode
)
