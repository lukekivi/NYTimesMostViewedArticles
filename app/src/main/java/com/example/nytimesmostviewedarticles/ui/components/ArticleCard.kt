package com.example.nytimesmostviewedarticles.ui.components

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
            .clickable { onClick() }
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
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.article_card_published_line) + articleData.publishedDate,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                TextCircle(
                    text = articleData.section,
                    borderWidth = 2.0,
                    colorResourceId = R.color.black,
                    modifier = modifier.padding(end = 10.dp)
                )
                TextCircle(
                    text = articleData.subsection,
                    borderWidth = 1.5,
                    colorResourceId = R.color.black_half,
                    modifier = modifier.padding(start = 10.dp, end = 10.dp)
                )
                if (articleData.descriptionFacets.isNotEmpty()) {
                    TextCircle(
                        text = articleData.descriptionFacets.first(),
                        borderWidth = 1.0,
                        colorResourceId = R.color.black_third,
                        modifier = modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }
            }

        }
        Image(
            painter = when (articleData.media) {
                is MediaDataForUI.Available -> rememberImagePainter(
                    data = articleData.media.url,
                    builder = {
                        placeholder(R.drawable.loading_animation)
                        transformations(CircleCropTransformation())
                    })
                is MediaDataForUI.Unavailable -> painterResource(id = R.drawable.ic_the_new_york_times_alt)
            },
            contentDescription = null,
            modifier = modifier
                .size(width = 252.dp, height = 168.dp)
                .weight(1f)
        )
    }
}

@Composable
fun TextCircle(
    text: String,
    borderWidth: Double,
    colorResourceId: Int,
    modifier: Modifier = Modifier
) {
    if (text.isNotBlank()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .wrapContentSize()
                .border(
                    width = borderWidth.dp,
                    color = colorResource(id = colorResourceId),
                    shape = RoundedCornerShape(15.dp)
                )
                .background(color = Color.White)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                modifier = modifier.padding(start = 10.dp, end = 10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextCirclePreview() {
    NYTimesMostViewedArticlesTheme {
        TextCircle(
            text = "Section",
            borderWidth = 2.0,
            colorResourceId = R.color.black
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
                section = "World",
                subsection = "Space",
                byline = "",
                type = "",
                title = "First Cases of COVID-19 Have Reached the Moon.",
                abstract = "",
                descriptionFacets = listOf("COVID-19; Omicron"),
                media = MediaDataForUI.Available(
                    url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                    caption = ""
                )
            ),
            {}
        )
    }
}