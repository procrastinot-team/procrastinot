package com.github.mateo762.myapplication.post

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.unit.dp
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.mateo762.myapplication.R
import com.google.accompanist.imageloading.LoadPainterDefaults

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postTitle = intent.getStringExtra("postTitle")
        val postBody = intent.getStringExtra("postBody")
        val postUsername = intent.getStringExtra("postUsername")
        val assocHabit = intent.getStringExtra("associatedHabit")
        val postImageUrl = intent.getStringExtra("imageUrl")
        setContent {
            if (postTitle != null && postBody != null && postUsername != null && assocHabit != null && postImageUrl != null) {
                ShowPost(postTitle, postBody, postUsername, assocHabit, postImageUrl)
            }
        }
    }

    @Composable
    fun ShowPost(
        postTitle: String,
        postBody: String,
        postUsername: String,
        assocHabit: String,
        postImageUrl: String
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(colorResource(R.color.white))
            ) {
                Column {
                    PostUserCard(username = postUsername)
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = rememberImagePainter(
                            data = postImageUrl,
                            imageLoader = LocalImageLoader.current,
                            builder = {
                                this.crossfade(LoadPainterDefaults.FadeInTransitionDuration)
                                placeholder(0)
                            }
                        ),
                        contentDescription = stringResource(id = R.string.sample_post_content),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(450.dp)
                            .width(400.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .testTag("post_image")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = postTitle,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.testTag("post_title")
                    )
                    Text(
                        text = assocHabit,
                        style = MaterialTheme.typography.h6,
                        fontStyle = Italic,
                        modifier = Modifier.testTag("associated_habit")
                    )
                    Text(
                        text = postBody,
                        modifier = Modifier.testTag("post_body")
                    )
                }
            }
        }
    }


    @Composable
    fun PostUserCard(username: String) {
        val context = LocalContext.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .testTag("post_user_card")
                .clickable {
                    Toast
                        .makeText(
                            context,
                            "This takes you to the poster's profile",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }) {
            Image(
                // TODO: replace with actual user's icon from Firebase
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
                    .testTag("user_card_avatar")
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = username,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.testTag("user_card_username")
                )
            }
        }
    }
}
