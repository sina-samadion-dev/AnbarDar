package com.sidofa.anbardar.viewmodels

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidofa.anbardar.datalayer.entities.Product
import com.sidofa.anbardar.logiclayer.models.CartDto
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.logiclayer.models.PurchasedProductDto
import com.sidofa.anbardar.logiclayer.services.CartsServices
import com.sidofa.anbardar.logiclayer.services.ProductsServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cartService: CartsServices,
    private val productsServices: ProductsServices
) : ViewModel() {

    var allCarts by mutableStateOf<List<CartDto>>(emptyList())
        private set

    var searchResults by mutableStateOf<List<ProductDto>>(emptyList())
        private set

    var toastMessage by mutableStateOf<String?>(null)

    init {
        getAllCarts()
    }


    fun getAllCarts() {
        viewModelScope.launch {
            allCarts = cartService.getAllCarts()
        }
    }
    fun addCart(customerName: String, products: List<ProductDto>, amounts: List<MutableIntState>) {
        viewModelScope.launch {
            productsServices.updateProducts(products)

            val newCart = CartDto(
                id = 0,
                customerName = customerName
            )
            var priceAmount : Long = 0
            products.forEachIndexed { index, dto ->
                newCart.purchasedProducts.add(
                    PurchasedProductDto(
                        purchasedAmount = amounts[index].intValue,
                        productName = dto.productName,
                        unitType = dto.unitType,
                        unitPrice = dto.unitPrice,
                        unitRevenue = dto.unitRevenue
                    )
                )
                priceAmount += dto.unitRevenue * amounts[index].intValue
            }

            newCart.priceAmount = priceAmount
            cartService.addCart(newCart)
            getAllCarts()
        }
    }

    fun searchProductByName(searchedName: String) {
        viewModelScope.launch {
            searchResults = productsServices.searchByName(searchedName)
        }
    }

    fun searchProductByBarcode(barcode: String) {
        viewModelScope.launch {
            val foundProduct = productsServices.getProductByBarcode(barcode)
            foundProduct?.let {
                searchResults = mutableListOf(it)
            }
            if (foundProduct == null){
                toastMessage = "این محصول در انبار موجود نیست"
            }
        }
    }

    fun clearSearchResults() {
        searchResults = emptyList()
    }

    fun clearToast() {
        toastMessage = null
    }
}
