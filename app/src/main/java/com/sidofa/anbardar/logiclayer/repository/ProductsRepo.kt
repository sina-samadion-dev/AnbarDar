package com.sidofa.anbardar.logiclayer.repository

import com.sidofa.anbardar.datalayer.daos.ProductDao
import com.sidofa.anbardar.datalayer.entities.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRepo @Inject constructor(
    private val productDao: ProductDao
) {
    suspend fun addProduct(product: Product) = productDao.insertProduct(product)

    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    suspend fun getProductByBarcode(barcode: String): Product? =
        productDao.getProductByBarcode(barcode)

    suspend fun searchByName(searchedName: String): List<Product> =
        productDao.searchByName(searchedName)

    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    suspend fun deductStock(product: Product, amount: Int): Boolean {
        if (product.amount < amount) return false
        productDao.updateProduct(product.copy(amount = product.amount - amount))
        return true
    }

    suspend fun updateProducts(products: List<Product>) = productDao.updateProducts(products)
}
