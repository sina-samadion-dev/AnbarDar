package com.sidofa.anbardar.logiclayer.repository

import com.sidofa.anbardar.datalayer.daos.CartDao
import com.sidofa.anbardar.datalayer.entities.Cart
import com.sidofa.anbardar.datalayer.entities.PurchasedProduct
import javax.inject.Inject

class CartsRepo @Inject constructor(
    private val cartsDao: CartDao
) {
    suspend fun addCart(cart: Cart, purchasedProducts: List<PurchasedProduct>) = cartsDao.addCart(cart, purchasedProducts)

    suspend fun deleteCart(cart: Cart) = cartsDao.deleteCart(cart)

    suspend fun getCartById(id: Int): Cart? = cartsDao.getCartById(id)

    suspend fun modifyCart(cart: Cart) = cartsDao.modifyCart(cart)

    suspend fun getCarts(): List<Cart> = cartsDao.getAllCarts()
}
