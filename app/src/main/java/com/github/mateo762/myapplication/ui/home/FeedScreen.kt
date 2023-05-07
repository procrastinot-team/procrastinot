package com.github.mateo762.myapplication.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.post.PostActivity
import com.github.mateo762.myapplication.profile.ProfileActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Composable
fun FeedScreen(posts: List<PostEntity>) {
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
                post.assocHabit,
                post.imageUrl
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
                    contentDescription = "image_$imageUrl",
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
    val userId = remember { mutableStateOf("") }

    LaunchedEffect(username) {
        fetchUserId(username) { fetchedUserId ->
            userId.value = fetchedUserId
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            Log.d(TAG, "mateo:  ${userId.value}")

            if (userId.value.isNotEmpty()) {
                onUserItemClick(context, userId.value)
            }
        }) {
        Image(
            painter = painterResource(R.drawable.ic_android),
            contentDescription = "avatar_$username",
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
                text = "@$username",
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

private fun fetchUserId(username: String, onUserIdFetched: (String) -> Unit) {
    val usernamesRef = FirebaseDatabase.getInstance().getReference("/usernames/$username")

    usernamesRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val userId = snapshot.getValue(String::class.java) ?: ""
            onUserIdFetched(userId)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("FeedScreen", "Error: ${error.message}")
        }
    })
}

private fun onUserItemClick(context: Context, userId: String) {
    val intent = Intent(context, ProfileActivity.EntryPoint::class.java)
    intent.putExtra("userId", userId)
    startActivity(context, intent, null)
}
