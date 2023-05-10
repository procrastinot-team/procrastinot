package com.github.mateo762.myapplication.ui.coaching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R


@Composable
fun OffersScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.card_background_light), RoundedCornerShape(8.dp))
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = "TODO: Offers",
                style = MaterialTheme.typography.h1,
                modifier = Modifier.testTag("placeholder_text")
            )
        }
    }
}