package com.sidofa.anbardar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sidofa.anbardar.ui.navigation.AppNavigation
import com.sidofa.anbardar.ui.theme.AnbarDarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnbarDarTheme {
                AppNavigation()
            }
        }
    }
}
