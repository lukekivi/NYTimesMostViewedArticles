package com.nytimesmostviewedarticles.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleRowDataResponse
import com.nytimesmostviewedarticles.ui.components.ArticleCard
import com.nytimesmostviewedarticles.ui.components.NyTimesTopBar
import com.nytimesmostviewedarticles.ui.components.SectionsLazyRow
import com.nytimesmostviewedarticles.viewmodel.MainScreenViewModelImpl

@ExperimentalCoilApi
@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModelImpl = hiltViewModel(),
    onNavClick: (String) -> Unit
) {
    val articleData by mainScreenViewModel.articles.collectAsState(ArticleRowDataResponse.Loading)

    Scaffold(
        topBar = { NyTimesTopBar() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            SectionsLazyRow(
                sectionNames = stringArrayResource(id = R.array.section_names),
                onSelected = {}
            )

            Divider(
                color = MaterialTheme.colors.secondary,
                thickness = 1.dp
            )

            MainScreenPrimaryData(
                articleData = articleData,
                onNavClick = onNavClick
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MainScreenPrimaryData(
    articleData: ArticleRowDataResponse,
    onNavClick: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (articleData) {
            is ArticleRowDataResponse.Loading -> {
                CircularProgressIndicator(color = colorResource(id = R.color.black))
            }

            is ArticleRowDataResponse.Error -> {
                Text(
                    text = articleData.message,
                    style = MaterialTheme.typography.h6
                )
            }

            is ArticleRowDataResponse.Empty -> {
                Text(
                    text = stringResource(R.string.main_screen_empty_data),
                    style = MaterialTheme.typography.h6
                )
            }

            is ArticleRowDataResponse.Success -> {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(articleData.articleRowDataList) { data ->
                        ArticleCard(articleRowData = data, onClick = { onNavClick(data.id) })

                        Divider(
                            color = MaterialTheme.colors.secondary,
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