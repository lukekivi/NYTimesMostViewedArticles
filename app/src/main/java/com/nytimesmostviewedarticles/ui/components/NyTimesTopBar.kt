package com.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nytimesmostviewedarticles.R

@Composable
fun NyTimesTopBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_bar_title),
                style = MaterialTheme.typography.h1,
                modifier = modifier.fillMaxWidth()
            )
        },
        navigationIcon = navigationIcon,
        backgroundColor = MaterialTheme.colors.background
    )
}