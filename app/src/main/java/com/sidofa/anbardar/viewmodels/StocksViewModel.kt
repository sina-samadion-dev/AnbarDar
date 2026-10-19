package com.sidofa.anbardar.viewmodels

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.sidofa.anbardar.datalayer.entities.Product
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.logiclayer.services.ProductsServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val productsServices: ProductsServices
) : ViewModel() {

    var products by mutableStateOf<List<ProductDto>>(emptyList())
        private set
    var modalProduct by mutableStateOf<ProductDto?>(null)

    var toastMessage by mutableStateOf<String?>(null)

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            productsServices.getAllProducts().collect { products = it }
        }
    }

    fun createNewProduct(){
        modalProduct = ProductDto()
    }

    fun addProduct(product: ProductDto){
        viewModelScope.launch {
            productsServices.addProduct(product)
            toastMessage = "محصول با موفقیت در انبار ثبت شد"
        }
    }

    fun deleteProduct(product: ProductDto){
        viewModelScope.launch {
            productsServices.deleteProduct(product)
            toastMessage = "محصول با موفقیت حذف شد"
        }
    }

    fun updateProduct(product: ProductDto){
        viewModelScope.launch {
            productsServices.updateProduct(product)
            toastMessage = "محصول با موفقیت ویرایش شد"
        }
    }
    fun scanBarcode(barcode: String) {
        viewModelScope.launch {
            val foundProduct = productsServices.getProductByBarcode(barcode)
            modalProduct = foundProduct ?: ProductDto(barcode = barcode)
        }
    }
}
