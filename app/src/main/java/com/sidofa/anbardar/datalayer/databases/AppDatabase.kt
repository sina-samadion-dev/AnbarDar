package com.sidofa.anbardar.datalayer.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sidofa.anbardar.datalayer.daos.CartDao
import com.sidofa.anbardar.datalayer.daos.ProductDao
import com.sidofa.anbardar.datalayer.daos.PurchasedProductsDao
import com.sidofa.anbardar.datalayer.entities.Cart
import com.sidofa.anbardar.datalayer.entities.Product
import com.sidofa.anbardar.datalayer.entities.PurchasedProduct
import com.sidofa.anbardar.datalayer.typeconverters.InstantTypeConverter
import com.sidofa.anbardar.datalayer.typeconverters.ProductUnitTypeConverter

@Database(
    entities = [Cart::class, Product::class, PurchasedProduct::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ProductUnitTypeConverter::class, InstantTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartsDao(): CartDao
    abstract fun productsDao(): ProductDao

    abstract fun purchasedProductDao(): PurchasedProductsDao
}
