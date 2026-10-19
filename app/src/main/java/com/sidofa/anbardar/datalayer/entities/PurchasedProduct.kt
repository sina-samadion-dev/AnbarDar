package com.sidofa.anbardar.datalayer.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sidofa.anbardar.logiclayer.models.PurchasedProductDto
import com.sidofa.anbardar.utils.enums.ProductUnitTypes


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Cart::class,
            parentColumns = ["id"],
            childColumns = ["cartId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchasedProduct (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var cartId : Long,
    val productName: String,
    val unitType: ProductUnitTypes,
    val unitPrice: Int,
    val unitRevenue: Int,
    val purchasedAmount : Int
)


fun PurchasedProduct.toDto() : PurchasedProductDto = PurchasedProductDto(
    id = this.id,
    cartId = this.cartId,
    purchasedAmount = this.purchasedAmount,
    productName = this.productName,
    unitType = this.unitType,
    unitPrice = this.unitPrice,
    unitRevenue = this.unitRevenue
)