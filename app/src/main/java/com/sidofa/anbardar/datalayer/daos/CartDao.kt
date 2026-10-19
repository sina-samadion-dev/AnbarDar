package com.sidofa.anbardar.datalayer.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sidofa.anbardar.datalayer.entities.Cart
import com.sidofa.anbardar.datalayer.entities.PurchasedProduct
import com.sidofa.anbardar.logiclayer.models.CartDto

@Dao
interface CartDao {
    @Insert
    suspend fun insertCart(cart: Cart) : Long

    @Insert
    suspend fun insertPurchasedProducts(purchasedProducts: List<PurchasedProduct>)

    @Transaction
    open suspend fun addCart(cart: Cart, purchasedProducts: List<PurchasedProduct>){
        val cartId = insertCart(cart)
        insertPurchasedProducts(purchasedProducts.onEach { it.cartId = cartId })
    }

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Query("SELECT * FROM cart WHERE id = :cartId")
    suspend fun getCartById(cartId: Int): Cart?

    @Update
    suspend fun modifyCart(cart: Cart)

    @Query("SELECT * FROM cart")
    suspend fun getAllCarts(): List<Cart>
}
