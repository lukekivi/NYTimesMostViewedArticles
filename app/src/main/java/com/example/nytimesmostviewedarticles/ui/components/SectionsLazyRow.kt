package com.example.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nytimesmostviewedarticles.R
import com.example.nytimesmostviewedarticles.ui.theme.NYTimesMostViewedArticlesTheme

@Composable
fun SectionsLazyRow(
    sectionsList: Array<String>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ){
        itemsIndexed(sectionsList) { index, section ->
            Column(
                modifier = modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .clickable {onSelected(index)}
            ) {
                Text(
                    text = section,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionsLazyRowPreview() {
    NYTimesMostViewedArticlesTheme {
        SectionsLazyRow(
            sectionsList = stringArrayResource(id = R.array.section_names),
            onSelected = {}
        )
    }
}