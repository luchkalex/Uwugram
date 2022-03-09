package com.uwugram.presentation.components

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.uwugram.R

const val animationDuration = 800
const val positiveSlideHorizontalOffset = 1100
const val negativeSlideHorizontalOffset = -positiveSlideHorizontalOffset

@Composable
fun FadeInAnimation(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(animationDuration)),
        exit = fadeOut(animationSpec = tween(animationDuration)),
        content = content
    )
}

@Composable
fun SurfaceWithLoadingAnimation(visible: Boolean, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }
        .build()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        FadeInAnimation(visible = visible) {
            Image(
                painter = rememberImagePainter(
                    imageLoader = imageLoader,
                    data = R.drawable.app_loader_animation,
                ),
                contentDescription = ""
            )
        }
        FadeInAnimation(visible = !visible) {
            content()
        }
    }
}