package com.sidofa.anbardar.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sidofa.anbardar.ui.navigation.Routes

@Composable
fun NavigationButtons(
    onHomeClicked: () -> Unit,
    onStocksClicked: () -> Unit = {},
    currentRoute: Routes
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.Home,
            onClick = onHomeClicked,
            icon = {},
            label = { Text("خانه") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.Stocks,
            onClick = onStocksClicked,
            icon = {},
            label = { Text("انبار") }
        )
    }
}
