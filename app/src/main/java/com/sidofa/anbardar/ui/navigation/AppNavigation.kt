package com.sidofa.anbardar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sidofa.anbardar.ui.screens.home.HomeScreen
import com.sidofa.anbardar.ui.screens.stocks.StocksScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen(
                onNavigateToStocks = { navController.navigate(Routes.Stocks) }
            )
        }

        composable<Routes.Stocks> {
            StocksScreen(
                onNavigateHome = { navController.navigate(Routes.Home) }
            )
        }
    }
}
