package com.nytimesmostviewedarticles.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.nytimesmostviewedarticles.ui.components.FacetsLazyRow
import com.nytimesmostviewedarticles.ui.components.NyTimesTopBar
import com.nytimesmostviewedarticles.viewmodel.DetailScreenViewModelImpl

@ExperimentalCoilApi
@Composable
fun DetailScreen(
    detailsScreenViewModel: DetailScreenViewModelImpl = hiltViewModel(),
    onNavClick: () -> Unit
) {
    val articleDetailResponse by detailsScreenViewModel.getArticleDetail.collectAsState(ArticleDataResponse.Loading)

    Scaffold(
        topBar = {
            NyTimesTopBar(
                navigationIcon = { BackButtonIcon { onNavClick() } }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            DetailScreenContent(articleDataResponse = articleDetailResponse)
        }
    }
}


@ExperimentalCoilApi
@Composable
fun DetailScreenContent(
    articleDataResponse: ArticleDataResponse
) {
    when (articleDataResponse) {

        is ArticleDataResponse.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(top = 30.dp)
            )
        }

        is ArticleDataResponse.NoMatch -> {
            Text(
                text = stringResource(R.string.detail_screen_no_match),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 30.dp)
            )
        }

        is ArticleDataResponse.Error -> {
            Text(
                text = articleDataResponse.message,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 30.dp)
            )
        }

        is ArticleDataResponse.Success -> {
            val articleData = articleDataResponse.articleData

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                item {

                    Text(
                        text = articleData.title,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 30.dp)
                    )

                    Divider(
                        color = MaterialTheme.colors.secondary,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 20.dp, bottom = 20.dp)
                    )

                    /*          Image and caption         */
                    DetailScreenImage(mediaDataForUI = articleData.media)

                    Text(
                        text = stringResource(R.string.detail_screen_byline) + articleData.byline,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 20.dp)
                    )

                    Text(
                        text = stringResource(R.string.detail_screen_published_by) + articleData.publishedDate
                                + stringResource(R.string.detail_screen_updated) + articleData.updated,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                    )

                    Text(
                        text = articleData.abstract,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 20.dp)
                    )

                    TitledFacetLazyRow(
                        title = stringResource(R.string.detail_screen_details),
                        facets = articleData.descriptors,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    TitledFacetLazyRow(
                        title = stringResource(R.string.detail_screen_geography),
                        facets = articleData.geographyFacets,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    Divider(
                        color = MaterialTheme.colors.secondary,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(top = 30.dp, bottom = 30.dp)
                    )

                    HyperlinkedText(
                        url = articleData.url,
                        text = stringResource(R.string.detail_screen_read_more)
                    )
                }
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun DetailScreenImage(
    mediaDataForUI: MediaDataForUI?
) {
    mediaDataForUI?.let { mediaData ->
        Image(
            painter = rememberImagePainter(
                data = mediaData.url,
                builder = { placeholder(R.drawable.loading_animation) }
            ),
            contentDescription = null,
            modifier = Modifier.size(
                width = dimensionResource(id = R.dimen.detail_screen_image_width),
                height = dimensionResource(id = R.dimen.detail_screen_image_height),
            )
        )

        Text(
            text = mediaData.caption,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .width(dimensionResource(id = R.dimen.detail_screen_image_width))
        )

    } ?: Image(
        painter = painterResource(id = R.drawable.ic_the_new_york_times_alt),
        contentDescription = null,
        modifier = Modifier.size(
            width = dimensionResource(id = R.dimen.detail_screen_image_width),
            height = dimensionResource(id = R.dimen.detail_screen_image_height)
        )
    )
}


@Composable
fun TitledFacetLazyRow(
    title: String,
    facets: List<String>,
    modifier: Modifier = Modifier
) {
    if (facets.isNotEmpty()) {
        Text(
            text = title,
            style = MaterialTheme.typography.h3,
            modifier = modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(.9f)
        )

        FacetsLazyRow(
            facets = facets,
            Modifier.fillMaxWidth(.9f)
        )
    }
}

@Composable
fun HyperlinkedText(
    url: String,
    text: String,
) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        append(text)

        // attach a string annotation that stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = 0,
            end = text.length
        )

    }

    // UriHandler parse and opens URI inside AnnotatedString Item in Browse
    val uriHandler = LocalUriHandler.current

    ClickableText(
        text = annotatedLinkString,
        style = TextStyle(
            color = Color.Blue,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            textDecoration = TextDecoration.Underline
        ),
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}


@Composable
fun BackButtonIcon(
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null
        )
    }
}