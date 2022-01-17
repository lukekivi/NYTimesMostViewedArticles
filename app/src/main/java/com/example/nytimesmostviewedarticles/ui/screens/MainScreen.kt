package com.example.nytimesmostviewedarticles.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.nytimesmostviewedarticles.R
import com.example.nytimesmostviewedarticles.ui.components.ArticleCard
import com.example.nytimesmostviewedarticles.ui.components.NyTimesTopBar
import com.example.nytimesmostviewedarticles.ui.components.SectionsLazyRow
import com.example.nytimesmostviewedarticles.viewmodel.ArticleDataState
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoilApi
@Composable
fun MainScreen(
    articleDataState: StateFlow<ArticleDataState>,
    sectionNames: Array<String>,
    navController: NavController
) {
    val onNavClick = { index: Int -> }

    val articleData by articleDataState.collectAsState(ArticleDataState.Loading)

    Scaffold(
        topBar = { NyTimesTopBar() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            SectionsLazyRow(
                sectionNames = sectionNames,
                onSelected = {}
            )
            MainScreenPrimaryData(articleData = articleData, onNavClick = onNavClick)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MainScreenPrimaryData(
    articleData: ArticleDataState,
    onNavClick: (Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        articleData.let { dataState ->
            when (dataState) {
                is ArticleDataState.Loading -> {
                    CircularProgressIndicator(color = colorResource(id = R.color.black))
                }
                is ArticleDataState.Error -> {
                    Text(
                        text = dataState.message,
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp
                    )
                }
                is ArticleDataState.Success -> {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(dataState.data) { index, data ->
                            ArticleCard(articleData = data, onClick = { onNavClick(index) })
                            Divider(
                                color = colorResource(id = R.color.black),
                                modifier = Modifier.fillMaxWidth(.95f)
                            )
                        }
                    }
                }
            }
        }
    }
}