package com.github.mateo762.myapplication.ui.home

import android.content.Intent
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.post.Post
import com.github.mateo762.myapplication.post.PostActivity
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainterDefaults


@Composable
fun FeedScreen(posts: List<Post>) {
    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(10.dp)
    ) {
        for (post in posts) {
            PostThumbnail(
                post.username,
                post.caption,
                post.description,
                post.habitImage.habitId,
                post.habitImage.url
            )
        }
    }
}

@Composable
// This is the actual post thumbnail, including the header (UserCard)
fun PostThumbnail(
    username: String,
    caption: String,
    body: String,
    assocHabit: String,
    imageUrl: String
) {
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
            .testTag("post_thumbnail")
    ) {
        Column {
            UserCard(caption, username, assocHabit)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(400.dp, 250.dp)
                    .clickable {
                        val intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("postTitle", caption)
                        intent.putExtra("postBody", body)
                        intent.putExtra("postUsername", username)
                        intent.putExtra("associatedHabit", assocHabit)
                        intent.putExtra("imageUrl", imageUrl)
                        startActivity(context, intent, null)
                    }
            ) {
                Image(
                    // This is the image to be displayed in the post thumbnail in the feed --
                    // currently mimicking today screen
                    painter = rememberImagePainter(
                        data = imageUrl,
                        imageLoader = LocalImageLoader.current,
                    ),
                    contentDescription = stringResource(id = R.string.sample_post_content),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(400.dp)
                        .width(400.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
// This is the post / thumbnail header that includes the post information
fun UserCard(caption: String, username: String, assocHabit: String) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            Toast.makeText(
                context,
                "This takes you to the poster's profile",
                Toast.LENGTH_SHORT
            ).show()
        }) {
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
                text = username,
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = assocHabit,
                style = MaterialTheme.typography.body1,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
