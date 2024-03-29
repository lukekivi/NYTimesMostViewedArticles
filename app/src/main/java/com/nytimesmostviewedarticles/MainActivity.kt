package com.nytimesmostviewedarticles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nytimesmostviewedarticles.network.NetworkConnectionService
import com.nytimesmostviewedarticles.ui.screens.DetailScreen
import com.nytimesmostviewedarticles.ui.screens.MainScreen
import com.nytimesmostviewedarticles.ui.theme.NYTimesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

sealed class Destinations(val route: String) {
    object MainScreen: Destinations("MainScreen")
    object DetailScreen: Destinations("DetailScreen/{id}") {
        /**
         * Pass article ID to DetailScreen.
         */
        fun createRoute(id: String):String {
            return "DetailScreen/$id"
        }

        const val ArgId = "id"
    }
}

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var networkConnectionService: NetworkConnectionService

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYTimesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ScreenDispatcher()
                }
            }
        }
    }

    override fun onResume() {
        networkConnectionService.registerCallback()
        super.onResume()
    }

    override fun onPause() {
        networkConnectionService.unRegisterCallback()
        super.onPause()
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
                arguments = listOf(navArgument(Destinations.DetailScreen.ArgId) { type = NavType.StringType }),
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
            ) {
                DetailScreen(
                    onNavClick = { navController.popBackStack() }
                )
            }
        }
    }
}
