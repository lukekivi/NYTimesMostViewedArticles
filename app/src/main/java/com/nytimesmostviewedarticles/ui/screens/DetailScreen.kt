package com.nytimesmostviewedarticles.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nytimesmostviewedarticles.Constants
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import com.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.nytimesmostviewedarticles.ui.components.FacetsLazyRow
import com.nytimesmostviewedarticles.ui.components.NyTimesTopBar

@ExperimentalCoilApi
@Composable
fun DetailScreen(
    appData: ArticleDataForUI,
    onNavClick: () -> Unit
) {
    Scaffold(
        topBar = {
            NyTimesTopBar(
                navigationIcon = { BackButtonIcon { onNavClick() } }
            )
        }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                DetailScreenContent(articleData = appData)
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun DetailScreenContent(
    articleData: ArticleDataForUI
) {
    Text(
        text = articleData.title,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 30.dp)
    )
    Divider(
        color = colorResource(id = R.color.black),
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 10.dp, bottom = 10.dp)
    )

    /*          Image and caption         */
    DetailScreenImage(mediaDataForUI = articleData.media)

    Divider(
        color = colorResource(id = R.color.black),
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 10.dp, bottom = 10.dp)
    )
    Text(
        text = stringResource(R.string.detail_screen_byline) + articleData.byline,
        textAlign = TextAlign.Left,
        fontFamily = FontFamily.Serif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 10.dp)
    )
    Text(
        text = stringResource(R.string.detail_screen_published_by) + articleData.publishedDate
                + stringResource(R.string.detail_screen_updated) + articleData.updated,
        textAlign = TextAlign.Left,
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        modifier = Modifier
            .fillMaxWidth(.9f)
    )
    Text(
        text = articleData.abstract,
        textAlign = TextAlign.Left,
        fontFamily = FontFamily.Serif,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 10.dp)
    )
    TitledFacetLazyRow(
        title = stringResource(R.string.detail_screen_details),
        facets = articleData.descriptionFacets,
        modifier = Modifier.padding(top = 20.dp)
    )
    TitledFacetLazyRow(
        title = stringResource(R.string.detail_screen_geography),
        facets = articleData.geographyFacets,
        modifier = Modifier.padding(top = 20.dp)
    )
    Divider(
        color = colorResource(id = R.color.black),
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 20.dp, bottom = 10.dp)
    )
    HyperlinkedText(
        url = articleData.url,
        text = stringResource(R.string.detail_screen_read_more)
    )
    Divider(
        color = colorResource(id = R.color.black),
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(top = 10.dp, bottom = 10.dp)
    )
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
                width = Constants.DEFAULT_DETAIL_IMAGE_WIDTH.dp,
                height = Constants.DEFAULT_DETAIL_IMAGE_HEIGHT.dp
            )
        )
        Text(
            text = mediaData.caption,
            fontFamily = FontFamily.Serif,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .width(Constants.DEFAULT_DETAIL_IMAGE_WIDTH.dp)
        )

    } ?: Image(
        painter = painterResource(id = R.drawable.ic_the_new_york_times_alt),
        contentDescription = null,
        modifier = Modifier.size(
            width = Constants.DEFAULT_DETAIL_IMAGE_WIDTH.dp,
            height = Constants.DEFAULT_DETAIL_IMAGE_HEIGHT.dp
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
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
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