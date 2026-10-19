package com.sidofa.anbardar.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    object Home : Routes()

    @Serializable
    object Stocks : Routes()
}
