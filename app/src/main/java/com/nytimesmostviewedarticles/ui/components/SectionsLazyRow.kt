package com.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.ui.theme.NYTimesTheme

@Composable
fun SectionsLazyRow(
    sectionNames: Array<String>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
    ){
        itemsIndexed(sectionNames) { index, section ->
            Column(
                modifier = modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .clickable { onSelected(index) }
            ) {
                Text(
                    text = section,
                    style = MaterialTheme.typography.h4
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionsLazyRowPreview() {
    NYTimesTheme {
        SectionsLazyRow(
            sectionNames = stringArrayResource(id = R.array.section_names),
            onSelected = {}
        )
    }
}