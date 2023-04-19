package com.github.mateo762.myapplication.ui.upload

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

// TODO: This class should be deleted ASAP, it's only purpose it's to display an image in case it's needed to debug or test


@Composable
fun DisplayImage(imageUrl: String) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
        }
    )
    Image(
        painter = painter,
        contentDescription = "User image",
        modifier = Modifier,
        contentScale = ContentScale.Crop
    )
}