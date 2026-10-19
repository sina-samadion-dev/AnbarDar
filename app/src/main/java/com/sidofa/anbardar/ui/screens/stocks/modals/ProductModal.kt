package com.sidofa.anbardar.ui.screens.stocks.modals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.ui.components.BarcodeScannerOverlay
import com.sidofa.anbardar.ui.theme.Blue40
import com.sidofa.anbardar.ui.theme.Yellow40
import com.sidofa.anbardar.utils.enums.ProductUnitTypes
import com.sidofa.anbardar.viewmodels.StocksViewModel

@Composable
fun ProductModal(
    viewModel: StocksViewModel,
    product: ProductDto,
    onClose: () -> Unit
) {

    var showBarcodeScanner by remember { mutableStateOf(false) }


    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Blue40,
        focusedLabelColor = Blue40,
        cursorColor = Blue40
    )

    val isValid = product.barcode.isNotBlank()
            && product.amount != 0
            && (product.productName.isNotBlank()
            && product.unitPrice != 0
            && product.unitRevenue != 0)
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(onClick = onClose)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
                    .align(Alignment.Center)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Blue40)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "افزودن محصول به انبار",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(onClick = {
                                onClose()
                            }) {
                                Text("بستن", color = Yellow40)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(1f),
                                    value = product.barcode,
                                    onValueChange = { product.barcode = it },
                                    label = { Text("بارکد") },
                                    singleLine = true,
                                    colors = fieldColors
                                )
                                Button(
                                    onClick = { showBarcodeScanner = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Blue40,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 14.dp,
                                        vertical = 14.dp
                                    )
                                ) {
                                    Text("📷", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = product.productName,
                                onValueChange = { product.productName = it },
                                label = { Text("نام محصول") },
                                singleLine = true,
                                colors = fieldColors
                            )
                            Text(
                                "نوع واحد:",
                                style = MaterialTheme.typography.labelLarge,
                                color = Blue40
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = product.unitType == ProductUnitTypes.ByCount,
                                    onClick = { product.unitType = ProductUnitTypes.ByCount },
                                    label = { Text("تعداد") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Blue40,
                                        selectedLabelColor = Color.White
                                    )
                                )
                                FilterChip(
                                    selected = product.unitType == ProductUnitTypes.ByWeight,
                                    onClick = { product.unitType = ProductUnitTypes.ByWeight },
                                    label = { Text("وزنی (کیلو)") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Blue40,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = product.amount.toString(),
                                onValueChange = {new -> if (new.all { it.isDigit() }) {
                                    product.amount = new.toIntOrNull() ?: 0
                                }},
                                label = {
                                    Text(
                                        if (product.unitType == ProductUnitTypes.ByWeight)
                                            "مقدار (کیلو)"
                                        else
                                            "تعداد"
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = fieldColors
                            )

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = product.unitPrice.toString(),
                                onValueChange = {new -> if (new.all { it.isDigit() }) {
                                    product.unitPrice = new.toIntOrNull() ?: 0
                                }},
                                label = { Text("قیمت خرید (تومان)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = fieldColors
                            )

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = product.unitRevenue.toString(),
                                onValueChange = {new -> if (new.all { it.isDigit() }) {
                                    product.unitRevenue = new.toIntOrNull() ?: 0
                                }},
                                label = { Text("قیمت فروش (تومان)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = fieldColors
                            )
                        }

                        HorizontalDivider(color = Blue40.copy(alpha = 0.15f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onClose()
                                },
                                border = BorderStroke(1.dp, Blue40),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("انصراف")
                            }

                            Button(
                                modifier = Modifier.weight(1f),
                                enabled = isValid,
                                onClick = {
                                    if (product.id == 0){
                                        viewModel.addProduct(product)
                                    }
                                    else{
                                        viewModel.updateProduct(product)
                                    }
                                    onClose()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue40,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("ثبت محصول", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBarcodeScanner){
        BarcodeScannerOverlay(
            onBarcodeDetected = { barcode ->
                viewModel.scanBarcode(barcode)
            },
            onClose = {showBarcodeScanner = false}
        )
    }

}
