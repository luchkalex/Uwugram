package com.uwugram.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes()

val SINGLE_MESSAGE = RoundedCornerShape(
    topEnd = 25.dp,
    topStart = 25.dp,
    bottomEnd = 25.dp,
    bottomStart = 25.dp
)

val OWN_FIRST_MESSAGE = RoundedCornerShape(
    topEnd = 25.dp,
    topStart = 25.dp,
    bottomEnd = 5.dp,
    bottomStart = 25.dp
)

val OWN_MIDDLE_MESSAGE = RoundedCornerShape(
    topEnd = 5.dp,
    topStart = 25.dp,
    bottomEnd = 5.dp,
    bottomStart = 25.dp
)

val OWN_LAST_MESSAGE = RoundedCornerShape(
    topEnd = 5.dp,
    topStart = 25.dp,
    bottomEnd = 25.dp,
    bottomStart = 25.dp
)

val OTHERS_FIRST_MESSAGE = RoundedCornerShape(
    topEnd = 25.dp,
    topStart = 25.dp,
    bottomEnd = 25.dp,
    bottomStart = 5.dp
)

val OTHERS_MIDDLE_MESSAGE = RoundedCornerShape(
    topEnd = 25.dp,
    topStart = 5.dp,
    bottomEnd = 25.dp,
    bottomStart = 5.dp
)

val OTHERS_LAST_MESSAGE = RoundedCornerShape(
    topEnd = 25.dp,
    topStart = 5.dp,
    bottomEnd = 25.dp,
    bottomStart = 25.dp
)
