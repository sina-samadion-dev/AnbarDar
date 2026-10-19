package com.sidofa.anbardar.logiclayer.services

import com.sidofa.anbardar.datalayer.entities.toDto
import com.sidofa.anbardar.logiclayer.customexceptions.CartNotFound
import com.sidofa.anbardar.logiclayer.models.CartDto
import com.sidofa.anbardar.logiclayer.models.toEntity
import com.sidofa.anbardar.logiclayer.repository.CartsRepo
import javax.inject.Inject

class CartsServices @Inject constructor(
    private val cartsRepo: CartsRepo
) {
    suspend fun getAllCarts(): List<CartDto> = cartsRepo.getCarts().map { cart ->
        cart.toDto()
    }

    suspend fun getCartById(id: Int): CartDto = cartsRepo.getCartById(id)?.toDto() ?: throw CartNotFound()
    suspend fun addCart(cartDto: CartDto) = cartsRepo.addCart(cartDto.toEntity(), cartDto.purchasedProducts.map { it.toEntity() })

    suspend fun deleteCart(cartDto: CartDto) = cartsRepo.deleteCart(cartDto.toEntity())

    suspend fun modifyCart(cartDto: CartDto) = cartsRepo.modifyCart(cartDto.toEntity())
}
