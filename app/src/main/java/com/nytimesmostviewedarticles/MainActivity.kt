package com.nytimesmostviewedarticles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nytimesmostviewedarticles.ui.screens.DetailScreen
import com.nytimesmostviewedarticles.ui.screens.MainScreen
import dagger.hilt.android.AndroidEntryPoint

sealed class Destinations(val route: String) {
    object MainScreen: Destinations("MainScreen")
    object DetailScreen: Destinations("DetailScreen/{id}") {
        fun createRoute(id: String):String {
            return "DetailScreen/$id"
        }
    }
}

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

        AnimatedNavHost(navController = navController, startDestination = Destinations.MainScreen.route) {
            composable(route = Destinations.MainScreen.route) {
                MainScreen(
                    onNavClick = { id ->
                        navController.navigate(Destinations.DetailScreen.createRoute(id))
                    }
                )
            }

            composable(
                route = Destinations.DetailScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) }
            ) { backStackEntry ->
                DetailScreen(
                    articleId = backStackEntry.arguments?.getString("id"),
                    onNavClick = { navController.popBackStack() }
                )
            }
        }
    }
}
