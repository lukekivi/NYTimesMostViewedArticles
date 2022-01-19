package com.example.nytimesmostviewedarticles

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.google.accompanist.navigation.animation.composable
import coil.annotation.ExperimentalCoilApi
import com.example.nytimesmostviewedarticles.ui.screens.DetailScreen
import com.example.nytimesmostviewedarticles.ui.screens.MainScreen
import com.example.nytimesmostviewedarticles.viewmodel.ArticleDataViewModelImpl
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val articleDataViewModel: ArticleDataViewModelImpl by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = colorResource(id = R.color.white)) {
                ScreenDispatcher()
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun ScreenDispatcher() {
        val navController = rememberAnimatedNavController()

        AnimatedNavHost(navController = navController, startDestination = Constants.MAIN_SCREEN) {
            composable(route = Constants.MAIN_SCREEN) {
                MainScreen(
                    articleDataState = articleDataViewModel.articleDataState,
                    sectionNames = articleDataViewModel.sectionNames,
                    onNavClick = { articleData ->
                        Log.d("Navigation", "onNavClick")
                        articleDataViewModel.selectedArticle = articleData
                        navController.navigate(Constants.DETAILS_SCREEN)
                    }
                )
            }

            composable(
                route = Constants.DETAILS_SCREEN,
                enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) }
            ) {
                Log.d("Navigation", "Composable launched")
                DetailScreen(
                    appData = articleDataViewModel.selectedArticle,
                    onNavClick = { navController.popBackStack() }
                )
            }
        }
    }
}
