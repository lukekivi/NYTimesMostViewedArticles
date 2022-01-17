package com.example.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.nytimesmostviewedarticles.R

@Composable
fun NyTimesTopBar(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClickIcon: (() -> Unit) = {},
    navigationIcon: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_bar_title),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.black),
                modifier = modifier.fillMaxWidth()
            )
        },
        navigationIcon = navigationIcon,
        backgroundColor = colorResource(id = R.color.white),
        actions =
        {
            if (icon != null) {
                IconButton(
                    onClick = { onClickIcon() }
                ) {
                    Icon(
                        imageVector = icon,
                        tint = colorResource(id = R.color.white),
                        contentDescription = null,
                    )
                }
            }
        }
    )
}