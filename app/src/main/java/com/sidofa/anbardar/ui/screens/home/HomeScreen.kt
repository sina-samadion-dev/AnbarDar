package com.sidofa.anbardar.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidofa.anbardar.ui.screens.home.modals.CreateCartModal
import com.sidofa.anbardar.ui.components.NavigationButtons
import com.sidofa.anbardar.ui.navigation.Routes
import com.sidofa.anbardar.ui.theme.Blue40
import com.sidofa.anbardar.ui.theme.Yellow40
import com.sidofa.anbardar.viewmodels.HomeViewModel
import com.sidofa.anbardar.ui.screens.home.components.CartItem

@Composable
fun HomeScreen(
    onNavigateToStocks: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var isCreateModalOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(viewModel.toastMessage) {
        viewModel.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF5F8FF),
        bottomBar = {
            NavigationButtons(
                currentRoute = Routes.Home,
                onHomeClicked = {},
                onStocksClicked = onNavigateToStocks
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue40)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "سبد های خرید",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { isCreateModalOpen = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Yellow40,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("+ ایجاد سبد", fontWeight = FontWeight.Bold)
                }
            }

            if (viewModel.allCarts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "هیچ سبد خریدی ثبت نشده",
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
                    items(viewModel.allCarts) { cart ->
                        CartItem(
                            cart = cart,
                            onDeleteCart = { cart ->
                            }
                        )
                    }
                }
            }
        }
    }

    if (isCreateModalOpen) {
        CreateCartModal(
            viewModel = viewModel,
            onClose = { isCreateModalOpen = false }
        )
    }
}
