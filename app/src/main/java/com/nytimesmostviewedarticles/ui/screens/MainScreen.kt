package com.nytimesmostviewedarticles.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.network.NetworkStatus
import com.nytimesmostviewedarticles.ui.components.*
import com.nytimesmostviewedarticles.viewmodel.MainScreenViewModelImpl

@ExperimentalCoilApi
@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModelImpl = hiltViewModel(),
    onNavClick: (String) -> Unit
) {
    val mainScreenContent by mainScreenViewModel.mainScreenContent.collectAsState(
        DEFAULT_MAIN_SCREEN_CONTENT
    )

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = mainScreenContent.isLoading),
        onRefresh = { mainScreenViewModel.userRefreshArticles() },
        indicatorPadding = PaddingValues(top = 200.dp)
    ) {

        Scaffold(
            topBar = { NyTimesTopBar() }
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                FilterItemLazyRow(
                    filterItems = mainScreenContent.filterItemList,
                    onSelected = { mainScreenViewModel.userChangedFilter(it) }
                )

                Divider(
                    color = MaterialTheme.colors.primaryVariant,
                    thickness = 1.dp
                )

                NetworkDisconnectedAlert(isEnabled = mainScreenContent.networkStatus != NetworkStatus.CONNECTED)

                MainScreenCoreContent(
                    mainScreenData = mainScreenContent.mainScreenData,
                    networkStatus = mainScreenContent.networkStatus,
                    onNavClick = onNavClick
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MainScreenCoreContent(
    mainScreenData: MainScreenData,
    networkStatus: NetworkStatus,
    onNavClick: (String) -> Unit
) {

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        when (mainScreenData) {
            is MainScreenData.Uninitialized -> {
                LinearProgressIndicator(
                    modifier = Modifier.padding(top = 60.dp)
                )
            }

            is MainScreenData.Error -> {
                Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
                    /**
                     * Network connection loss is an expected error and is handled in MainScreen()
                     */
                    if (networkStatus == NetworkStatus.CONNECTED) {
                        Text(
                            text = mainScreenData.message,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                    }
                }
            }

            is MainScreenData.Empty -> {
                Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
                    Text(
                        text = stringResource(R.string.main_screen_empty_data),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
            }

            is MainScreenData.Success -> {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(mainScreenData.articleRowDataList) { data ->
                        ArticleCard(articleCardData = data, onClick = { onNavClick(data.id) })

                        Divider(
                            color = MaterialTheme.colors.primaryVariant,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}

private val DEFAULT_MAIN_SCREEN_CONTENT = MainScreenContent(
    filterItemList = listOf(),
    mainScreenData = MainScreenData.Uninitialized,
    isLoading = false,
    networkStatus = NetworkStatus.CONNECTED
)

data class MainScreenContent(
    val filterItemList: List<FilterItem>,
    val mainScreenData: MainScreenData,
    val isLoading: Boolean,
    val networkStatus: NetworkStatus
)

sealed class MainScreenData {
    object Empty: MainScreenData()
    object Uninitialized: MainScreenData()
    /**
     * Valid, non-empty data is available to be displayed.
     */
    class Success(val articleRowDataList: List<ArticleCardData>) : MainScreenData()
    class Error(val message: String) : MainScreenData()
}