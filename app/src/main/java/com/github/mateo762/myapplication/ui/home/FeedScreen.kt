package com.github.mateo762.myapplication.ui.home

import android.content.Intent
import android.media.Image
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.post.PostActivity


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
        PostThumbnail(image = null, "Went to social event for the first time")
        PostThumbnail(image = null, "Practiced ballroom dancing")
    }
}

@Composable
fun PostThumbnail(image: Image?, caption : String) {
    val context = LocalContext.current
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
                UserCard(caption)
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(400.dp, 250.dp)
                        .clickable{ Toast.makeText(context, "This opens the post in PostActivity", Toast.LENGTH_SHORT).show()}
                        .clickable{
                            val intent = Intent(context, PostActivity::class.java)
                            startActivity(context, intent, null)
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.window),
                        contentDescription = stringResource(id = R.string.sample_post_content),
                        contentScale = ContentScale.Crop)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }


@Composable
fun UserCard(caption : String) {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable{ Toast.makeText(context, "This takes you to the poster's profile", Toast.LENGTH_SHORT).show()}) {
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
                text = caption,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "@berserk_man",
                style = MaterialTheme.typography.body1,
            )
        }
    }
}
