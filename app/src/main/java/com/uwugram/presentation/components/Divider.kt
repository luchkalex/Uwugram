package com.uwugram.presentation.components

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ListDivider() {
    Divider(
        thickness = 0.5.dp,
        color = MaterialTheme.colors.primaryVariant
    )
}