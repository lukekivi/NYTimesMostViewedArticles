package com.nytimesmostviewedarticles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nytimesmostviewedarticles.R

@Composable
fun NetworkDisconnectedAlert(
    isEnabled: Boolean
) {
    if (isEnabled) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        ) {
            Text(
                text = stringResource(R.string.no_network_connection),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}