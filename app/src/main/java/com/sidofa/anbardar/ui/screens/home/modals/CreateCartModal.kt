package com.sidofa.anbardar.ui.screens.home.modals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sidofa.anbardar.logiclayer.models.CartDto
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.logiclayer.models.PurchasedProductDto
import com.sidofa.anbardar.ui.components.FindCartItemModal
import com.sidofa.anbardar.viewmodels.HomeViewModel
import com.sidofa.anbardar.ui.theme.Blue40
import com.sidofa.anbardar.ui.theme.Yellow40

@Composable
fun CreateCartModal(
    viewModel: HomeViewModel,
    onClose: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    val products = remember { mutableStateListOf<ProductDto>() }
    val productPurchasedCounts = remember { mutableStateListOf<MutableIntState>() }
    var findCartItemModalEnabled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(onClick = onClose)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .align(Alignment.Center)
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Blue40)
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "ایجاد سبد خرید",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("نام مشتری") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Blue40,
                                focusedLabelColor = Blue40,
                                cursorColor = Blue40
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { findCartItemModalEnabled = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Yellow40,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Text("+ افزودن محصول", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Blue40.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(8.dp))

                        if (products.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "هنوز محصولی اضافه نشده",
                                    color = Color.LightGray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("جمع کل:", fontWeight = FontWeight.Bold, color = Blue40)
                                Text(
                                    "${products.indices.sumOf { index ->
                                        products[index].unitRevenue *
                                                productPurchasedCounts[index].intValue
                                    }} تومان",
                                    fontWeight = FontWeight.Bold,
                                    color = Blue40
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                itemsIndexed(products) { index, item ->

                                    CartProductPres(
                                        product = item,
                                        purchasedAmount = productPurchasedCounts[index].intValue,
                                        index = index,
                                        onDelete = {
                                            products.remove(item)
                                            productPurchasedCounts.removeAt(index)
                                        },
                                        onIncrease = {
                                            if (item.amount == 0){
                                                viewModel.toastMessage = "اتمام موجودی در انبار"
                                            }else{
                                                item.amount--
                                                productPurchasedCounts[index].intValue ++
                                            }
                                        },
                                        onDecrease = {
                                            item.amount++
                                            productPurchasedCounts[index].intValue --
                                        }
                                    )

                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = onClose,
                                border = BorderStroke(1.dp, Blue40),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("انصراف")
                            }

                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.addCart(
                                        customerName = customerName,
                                        products = products,
                                        amounts = productPurchasedCounts
                                    )
                                    onClose()
                                },
                                enabled = customerName.isNotBlank() && products.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue40,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("ثبت سبد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (findCartItemModalEnabled) {
        FindCartItemModal(
            viewModel = viewModel,
            onAddProduct = { product ->
                if (products.any { it.id == product.id }) {
                    viewModel.toastMessage = "این محصول قبلا اضافه شده"
                } else {
                    if (product.amount == 0){
                        viewModel.toastMessage = "این محصول در انبار تمام شده"
                    }
                    else{
                        products.add(product)
                        productPurchasedCounts.add(mutableIntStateOf(1))
                        product.amount--
                    }
                }
            },
            onExit = { findCartItemModalEnabled = false }
        )
    }
}

@Composable
private fun CartProductPres(
    product: ProductDto,
    index: Int,
    purchasedAmount: Int,
    onDelete: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F4FF)
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Blue40,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "${index + 1}",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    "$purchasedAmount * ${product.unitRevenue} = " +
                            "${product.unitRevenue * purchasedAmount} تومان",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(7.dp),
                    color = Yellow40,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onIncrease()
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Text(
                    text = purchasedAmount.toString(),
                    modifier = Modifier.width(28.dp),
                    textAlign = TextAlign.Center,
                    color = Blue40,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(7.dp),
                    color = Yellow40,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onDecrease()
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "حذف",
                color = Color.Red.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        onDelete()
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}