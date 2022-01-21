package com.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleDataForRow
import com.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.nytimesmostviewedarticles.ui.theme.NYTimesMostViewedArticlesTheme

@ExperimentalCoilApi
@Composable
fun ArticleCard(
    articleDataForRow: ArticleDataForRow,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(dimensionResource(id = R.dimen.article_card_row_height))
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
                text = articleDataForRow.title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .clickable { onClick() }
            )

            Text(
                text = stringResource(R.string.article_card_published_line) + articleDataForRow.publishedDate,
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
                FacetsLazyRow(facets = articleDataForRow.descriptors)
            }

        }

        Image(
            painter = articleDataForRow.media?.let {
                rememberImagePainter(
                    data = articleDataForRow.media.url,
                    builder = {
                        placeholder(R.drawable.loading_animation)
                        transformations(CircleCropTransformation())
                    })
            } ?: painterResource(id = R.drawable.ic_the_new_york_times_alt),
            contentDescription = null,
            modifier = modifier
                .size(
                    width = dimensionResource(id = R.dimen.article_card_image_width),
                    height = dimensionResource(id = R.dimen.article_card_image_height)
                )
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
            articleDataForRow = ArticleDataForRow(
                id = "",
                publishedDate = "March, 14 2022",
                section = "World",
                title = "First Cases of COVID-19 Have Reached the Moon.",
                descriptors = listOf("COVID-19; Omicron"),
                media = MediaDataForUI(
                    url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                    caption = ""
                )
            ),
            {}
        )
    }
}