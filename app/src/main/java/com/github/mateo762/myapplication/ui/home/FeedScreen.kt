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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.post.PostActivity


@Composable
fun FeedScreen(images: List<Image>) {
    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // This is a for loop once everything is set correctly and the parameters are passed  to create the post
        //for(image in images){
        //   PostThumbnail(image = image, ... )
        //}
        // Like this
        PostThumbnail(
            "@test_username",
            image = null,
            "Test post 1",
            "Sample body text..."
        )
        PostThumbnail(
            "@test_username",
            image = null,
            "Second test post but it has a longer title",
            "Sample body text..."
        )
    }
}

@Composable
// This is the actual post thumbnail, including the header (UserCard)
fun PostThumbnail(username: String, image: Image?, caption: String, body: String) {
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
            UserCard(caption, username)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(400.dp, 250.dp)
                    .clickable {
                        val intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("postTitle", caption)
                        intent.putExtra("postBody", body)
                        intent.putExtra("postUsername", username)
                        // TODO: replace with actual image content from Firebase
                        // Unused for now, load sample image for testing
                        intent.putExtra("imageContents", ByteArray(512))
                        startActivity(context, intent, null)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.window),
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
fun UserCard(caption: String, username: String) {
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
        }
    }
}
