package com.github.mateo762.myapplication.ui.home

import android.media.Image
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R


@Composable
fun FeedScreen(images: List<Image>){
    //for(image in images){
     //   PostThumbnail(image = image)
    //}
    Column(modifier = Modifier
        .background(colorResource(R.color.white))
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .padding(10.dp)) {
        PostThumbnail(image = null)
        //Spacer(modifier = Modifier.height(1.dp))
        PostThumbnail(image = null)
    }
}

@Composable
fun PostThumbnail(image: Image?) {
        Box(
            // Post box
            modifier = Modifier
                .background(
                    colorResource(R.color.card_background_light),
                    RoundedCornerShape(8.dp)
                )
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .size(400.dp, 300.dp)
        ) {
            Column {
                UserCard()
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(400.dp, 250.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.window),
                        contentDescription = stringResource(id = R.string.sample_post_content),
                        contentScale = ContentScale.FillBounds)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }


@Composable
fun UserCard() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.ic_android),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .size(37.dp)
                .clip(CircleShape)                       // clip to the circle shape
                .border(
                    1.dp,
                    colorResource(R.color.today_background),
                    CircleShape
                )   // add a border (optional)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "Post Caption", style = MaterialTheme.typography.h6,
            )
            Text(
                text = "@username",
                style = MaterialTheme.typography.body1,
            )
        }
    }
}
