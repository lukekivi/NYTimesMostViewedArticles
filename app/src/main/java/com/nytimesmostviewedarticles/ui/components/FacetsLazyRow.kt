package com.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nytimesmostviewedarticles.ui.theme.NYTimesTheme

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
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(15.dp)
                )
                .background(color = MaterialTheme.colors.background)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
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
    NYTimesTheme {
        TextCircle(
            text = "Section",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FacetsLazyRowPreview() {
    NYTimesTheme {
        FacetsLazyRow(facets = listOf("World", "Space", "COVID-19; Omicron", "Milky Way", "Moon", "Universe"))
    }
}