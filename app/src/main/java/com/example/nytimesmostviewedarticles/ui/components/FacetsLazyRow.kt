package com.example.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nytimesmostviewedarticles.R
import com.example.nytimesmostviewedarticles.ui.theme.NYTimesMostViewedArticlesTheme

@Composable
fun FacetsLazyRow(
    facets: List<String>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        items(facets) { facet ->
            TextCircle(
                text = facet,
                modifier.padding(end = 15.dp)
            )
        }
    }
}

@Composable
fun TextCircle(
    text: String,
    modifier: Modifier = Modifier
) {
    if (text.isNotBlank()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .wrapContentSize()
                .border(
                    width = .5.dp,
                    color = colorResource(id = R.color.black),
                    shape = RoundedCornerShape(15.dp)
                )
                .background(color = Color.White)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FacetsLazyRowPreview() {
    NYTimesMostViewedArticlesTheme {
        FacetsLazyRow(facets = listOf("World", "Space", "COVID-19; Omicron", "Milky Way", "Moon", "Universe"))
    }
}