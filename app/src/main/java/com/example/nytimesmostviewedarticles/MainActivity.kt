package com.example.nytimesmostviewedarticles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.nytimesmostviewedarticles.ui.screens.MainScreen
import com.example.nytimesmostviewedarticles.viewmodel.ArticleDataViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val articleDataViewModel: ArticleDataViewModelImpl by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = colorResource(id = R.color.white)) {
                ScreenDispatcher()
            }
        }
    }

    @Composable
    fun ScreenDispatcher() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Constants.MAIN_SCREEN) {
            composable(Constants.MAIN_SCREEN) {
                MainScreen(
                    articleDataState = articleDataViewModel.articleDataState,
                    sectionNames = articleDataViewModel.sectionNames,
                    navController = navController
                )
            }
        }
    }
}
