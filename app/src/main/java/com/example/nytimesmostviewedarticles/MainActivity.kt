package com.example.nytimesmostviewedarticles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.nytimesmostviewedarticles.ui.screens.MainScreen
import com.example.nytimesmostviewedarticles.ui.theme.NYTimesMostViewedArticlesTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYTimesMostViewedArticlesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ScreenDispatcher()
                }
            }
        }
    }
}

@Composable
fun ScreenDispatcher() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Constants.MAIN_SCREEN) {
        composable(Constants.MAIN_SCREEN) {
            MainScreen(navController = navController)
        }
    }
}