package com.sidofa.anbardar.logiclayer.services

import com.sidofa.anbardar.datalayer.entities.Product
import com.sidofa.anbardar.datalayer.entities.toDto
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.logiclayer.models.toEntity
import com.sidofa.anbardar.logiclayer.repository.ProductsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductsServices @Inject constructor(
    private val productsRepo: ProductsRepo
) {
    fun getAllProducts(): Flow<List<ProductDto>> = productsRepo.getAllProducts().map { it.map { product -> product.toDto() } }

    suspend fun addProduct(product: ProductDto) = productsRepo.addProduct(product.toEntity())

    suspend fun createProduct(barcode: String) : ProductDto{
        val product = productsRepo.getProductByBarcode(barcode)
        if (product != null){
            return product.toDto()
        }else {
            return ProductDto()
        }
    }

    suspend fun updateProduct(product: ProductDto) = productsRepo.updateProduct(product.toEntity())

    suspend fun deleteProduct(product: ProductDto) = productsRepo.deleteProduct(product.toEntity())

    suspend fun getProductByBarcode(barcode: String): ProductDto? =
        productsRepo.getProductByBarcode(barcode)?.toDto()

    suspend fun searchByName(searchedName: String): List<ProductDto> =
        productsRepo.searchByName(searchedName).map { product -> product.toDto() }

    suspend fun deductStock(product: Product, amount: Int): Boolean =
        productsRepo.deductStock(product, amount)

    suspend fun updateProducts(products :List<ProductDto>) = productsRepo.updateProducts(products.map { it.toEntity() })
}
