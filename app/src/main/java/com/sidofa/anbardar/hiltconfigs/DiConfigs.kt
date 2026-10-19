package com.sidofa.anbardar.hiltconfigs

import android.content.Context
import androidx.room.Room
import com.sidofa.anbardar.datalayer.daos.CartDao
import com.sidofa.anbardar.datalayer.daos.ProductDao
import com.sidofa.anbardar.datalayer.daos.PurchasedProductsDao
import com.sidofa.anbardar.datalayer.databases.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "anbar_dar.db"
        ).build()
    }

    @Provides
    fun provideProductDao(
        database: AppDatabase
    ): ProductDao {
        return database.productsDao()
    }

    @Provides
    fun provideCartDao(
        database: AppDatabase
    ):  CartDao{
        return database.cartsDao()
    }

    @Provides
    fun providePurchasedProductsDao(
        database: AppDatabase
    ):  PurchasedProductsDao{
        return database.purchasedProductDao()
    }

}