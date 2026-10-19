package com.sidofa.anbardar.datalayer.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sidofa.anbardar.datalayer.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM product WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): Product?

    @Query("SELECT * FROM product WHERE productName LIKE '%' || :searchedName || '%' ORDER BY productName ASC")
    suspend fun searchByName(searchedName: String): List<Product>

    @Query("SELECT * FROM product ORDER BY productName ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Update
    suspend fun updateProducts(products: List<Product>)
}
