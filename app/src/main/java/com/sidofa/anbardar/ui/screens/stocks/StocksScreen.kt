package com.sidofa.anbardar.ui.screens.stocks

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidofa.anbardar.logiclayer.models.ProductDto
import com.sidofa.anbardar.ui.components.BarcodeScannerOverlay
import com.sidofa.anbardar.ui.components.NavigationButtons
import com.sidofa.anbardar.ui.navigation.Routes
import com.sidofa.anbardar.ui.screens.stocks.modals.ProductModal
import com.sidofa.anbardar.ui.theme.Blue40
import com.sidofa.anbardar.ui.theme.ErrorRed
import com.sidofa.anbardar.ui.theme.Yellow40
import com.sidofa.anbardar.utils.enums.ProductUnitTypes
import com.sidofa.anbardar.viewmodels.StocksViewModel

@Composable
fun StocksScreen(
    onNavigateHome: () -> Unit,
    viewModel: StocksViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }

    val sheetHeight by animateDpAsState(
        targetValue = if (expanded) LocalConfiguration.current.screenHeightDp.dp else 200.dp,
        label = "sheetHeight"
    )

    Scaffold (
        bottomBar = {
            NavigationButtons(
                onHomeClicked = onNavigateHome,
                onStocksClicked = {},
                currentRoute = Routes.Stocks
            )
        }
    ){ innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F8FF))
                .padding(innerPadding)
        ) {

            if (!expanded) {
                Button(
                    onClick = { viewModel.createNewProduct() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 180.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Yellow40,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        "+ افزودن محصول به انبار",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(sheetHeight),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                shadowElevation = 12.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .background(Blue40)
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = if (expanded) "▼  محصولات انبار" else "▲  محصولات انبار",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            shape = CircleShape,
                            color = Yellow40
                        ) {
                            Text(
                                text = "${viewModel.products.size}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    if (expanded) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.createNewProduct() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Yellow40,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("+ افزودن محصول", fontWeight = FontWeight.Bold)
                            }
                        }
                        HorizontalDivider(color = Blue40.copy(alpha = 0.15f))
                    }

                    if (viewModel.products.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "انبار خالی است",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(viewModel.products) { product ->
                                ProductStockItem(
                                    product = product,
                                    onClick = {product ->
                                        viewModel.modalProduct = product
                                    },
                                    onDelete = { product ->
                                        viewModel.deleteProduct(product)
                                    }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(8.dp)) }
                        }
                    }
                }
            }
        }
    }

    viewModel.modalProduct?.let {
        ProductModal(
            viewModel = viewModel,
            product = it,
            onClose = {viewModel.modalProduct = null}
        )
    }


}

@Composable
private fun ProductStockItem(
    product: ProductDto,
    onClick: (product : ProductDto) -> Unit,
    onDelete: (product : ProductDto) -> Unit
) {
    val isLow = product.amount <= 5
    val isZero = product.amount == 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(product) }),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isZero -> Color(0xFFFFEBEE)
                isLow -> Color(0xFFFFFDE7)
                else -> Color(0xFFF0F4FF)
            }
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(10.dp),
                shape = CircleShape,
                color = when {
                    isZero -> Color.Red
                    isLow -> Yellow40
                    else -> Color(0xFF2E7D32)
                }
            ) {}

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "بارکد: ${product.barcode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "قیمت فروش: ${product.unitRevenue} تومان",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${product.amount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isZero -> Color.Red
                        isLow -> Color(0xFFE65100)
                        else -> Blue40
                    }
                )
                Text(
                    text = if (product.unitType == ProductUnitTypes.ByWeight) "کیلو" else "عدد",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                if (isZero) {
                    Text(
                        text = "ناموجود",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                } else if (isLow) {
                    Text(
                        text = "رو به اتمام",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = { onDelete(product) },
                modifier = Modifier.size(42.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErrorRed
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "×",
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }
}
