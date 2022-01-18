package com.example.nytimesmostviewedarticles.ui.components

import android.media.FaceDetector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.nytimesmostviewedarticles.R
import com.example.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import com.example.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.example.nytimesmostviewedarticles.ui.theme.NYTimesMostViewedArticlesTheme

@ExperimentalCoilApi
@Composable
fun ArticleCard(
    articleData: ArticleDataForUI,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .size(width = 750.dp, height = 225.dp)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxHeight()
                .weight(2.5f)
        ) {
            Text(
                text = articleData.title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .clickable { onClick() }
            )
            Text(
                text = stringResource(R.string.article_card_published_line) + articleData.publishedDate,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                FacetsLazyRow(
                    facets = listOf(
                        articleData.section,
                        articleData.subsection
                    ) + articleData.descriptionFacets
                )
            }

        }
        Image(
            painter = articleData.media?.let {
                rememberImagePainter(
                    data = articleData.media.url,
                    builder = {
                        placeholder(R.drawable.loading_animation)
                        transformations(CircleCropTransformation())
                    })
            } ?: painterResource(id = R.drawable.ic_the_new_york_times_alt),
            contentDescription = null,
            modifier = modifier
                .size(width = 252.dp, height = 168.dp)
                .weight(1f)
                .clickable { onClick() }
        )
    }
}

@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    NYTimesMostViewedArticlesTheme {
        ArticleCard(
            articleData = ArticleDataForUI(
                url = "",
                publishedDate = "March, 14 2022",
                updated = "",
                section = "World",
                subsection = "Space",
                byline = "",
                type = "",
                title = "First Cases of COVID-19 Have Reached the Moon.",
                abstract = "",
                descriptionFacets = listOf("COVID-19; Omicron"),
                geographyFacets = listOf(""),
                media = MediaDataForUI(
                    url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                    caption = "",
                    0
                )
            ),
            {}
        )
    }
}