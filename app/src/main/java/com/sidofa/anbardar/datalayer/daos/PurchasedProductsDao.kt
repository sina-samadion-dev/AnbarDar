package com.sidofa.anbardar.datalayer.daos

import androidx.room.Dao
import androidx.room.Delete
import com.sidofa.anbardar.datalayer.entities.PurchasedProduct


@Dao
interface PurchasedProductsDao {


    @Delete
    suspend fun deletePurchasedProduct(purchasedProduct: PurchasedProduct)



}