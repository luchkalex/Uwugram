package com.uwugram.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.uwugram.R
import com.uwugram.theme.ContactImageSize
import com.uwugram.theme.DefaultPadding

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun ContactItem(
    image: String,
    title: String,
    subtitle: String,
    onItemClick: () -> Unit
) {
    val painter = rememberImagePainter(
        data = image,
        builder = {
            placeholder(R.drawable.default_avatar)
            error(R.drawable.default_avatar)
            crossfade(1000)
            transformations(
                CircleCropTransformation()
            )
        }
    )

    Box(Modifier.clickable { onItemClick() }) {
        Row(
            modifier = Modifier
                .padding(DefaultPadding)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .size(ContactImageSize)
                    .clip(CircleShape),
                painter = painter,
                contentDescription = "Avatar"
            )
            Column(
                modifier = Modifier
                    .height(ContactImageSize)
                    .padding(horizontal = DefaultPadding),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = title, style = MaterialTheme.typography.subtitle1)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}