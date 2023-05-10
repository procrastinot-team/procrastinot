package com.github.mateo762.myapplication.ui.coaching

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.HabitEntity


@Composable
fun RequestsScreen(coachableHabits: List<HabitEntity>) {
    // coachableHabits: list of habits created with the "Request coach" option
    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        for (habit in coachableHabits) {
            DisplayCoachSelection(habit)
        }
    }
}

@Composable
fun DisplayCoachSelection(habit: HabitEntity) {
    val candidateUsernames = habit.coachOffers
    val rating = 4.7
    // TODO: FETCH USER-ENTITIES OF EACH USERNAME HERE
    // FROM THEM, GET THE RATING, USERNAME...
    Box(
        modifier = Modifier
            .background(
                colorResource(R.color.card_background_light),
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
            //.size(400.dp, 300.dp)
            .testTag("habit_selection_box")

    ) {
        Column {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.SemiBold
            )
            for (candidate in candidateUsernames) {
                CandidateCard(
                    "candidate.name",
                    "candidate.username",
                    rating,
                    "candidate.email"
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CandidateCard(name: String, username: String, rating: Any, email: String) {
    val context = LocalContext.current
    Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_android),
            contentDescription = "avatar_$username",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(37.dp)
                .clip(CircleShape)
                .border(
                    1.dp,
                    colorResource(R.color.today_background),
                    CircleShape
                )
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
            )
            Text(
                text = username,
                style = MaterialTheme.typography.body1,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = email,
                style = MaterialTheme.typography.body1,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = "star_icon",
                    modifier = Modifier
                        .size(23.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = rating.toString(),
                    style = MaterialTheme.typography.h6,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Selected as coach",
                    Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier.align(Alignment.CenterVertically),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.today_background),
                contentColor = colorResource(R.color.white)
            )
        ) {
            Text(text = "Select")
        }
    }
}



