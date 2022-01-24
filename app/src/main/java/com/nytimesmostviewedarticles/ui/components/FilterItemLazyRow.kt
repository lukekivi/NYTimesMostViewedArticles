package com.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nytimesmostviewedarticles.ui.theme.NYTimesTheme
import com.nytimesmostviewedarticles.viewmodel.FilterOptions

@Composable
fun FilterItemLazyRow(
    filterItems: List<FilterItem>,
    onSelected: (FilterOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
    ){
        items(filterItems) { filterItem ->
            Column(
                modifier = modifier
                    .background(
                        color = if (filterItem.isSelected)
                            MaterialTheme.colors.secondary
                         else
                             MaterialTheme.colors.background,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(start = 10.dp, end = 10.dp)
                    .clickable { onSelected(filterItem.filter) }
            ) {
                Text(
                    text = stringResource(id = filterItem.filter.label),
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
        FilterItemLazyRow(
            filterItems = FilterOptions.values().map { FilterItem(it, false) },
            onSelected = {}
        )
    }
}

data class FilterItem(
    val filter: FilterOptions,
    val isSelected: Boolean = false
)