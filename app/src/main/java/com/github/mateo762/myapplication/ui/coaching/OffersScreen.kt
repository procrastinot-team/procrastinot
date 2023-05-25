package com.github.mateo762.myapplication.ui.coaching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity


@Composable
fun OffersScreen(
    coachingHabits: List<HabitEntity>,
    onCoachingOffered: (HabitEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
    ) {
        //If there are no coaching requests, then display an empty screen
        if (coachingHabits.isEmpty()) {
            DisplayNoOffers()
        }
        //Display each habit where coaching is requested
        else {
            LazyColumn {
                items(coachingHabits) { habit ->
                    DisplayCoachingOffer(habit) {
                        // Invoke the callback with coach and habit
                        onCoachingOffered(habit)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayNoOffers() {
    Box(
        modifier = Modifier
            .background(
                colorResource(R.color.white),
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .testTag("nothing_to_see_box")
    ) {
        Text(
            text = "There are no requests for coaching",
            style = MaterialTheme.typography.h6,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.testTag("nothing_to_see_text")

        )
    }
}

@Composable
fun DisplayCoachingOffer(
    habit: HabitEntity,
    onCoachingOffered: () -> Unit // Callback for coach selection
) {
    Box(
        modifier = Modifier
            .background(
                colorResource(R.color.card_background_light),
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            //.size(400.dp, 300.dp)
            .testTag("habit_selection_box")

    ) {
        Column {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("habit_name_${habit.name}")
            )
            Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
                //Retrieve the name of the habit Owner from Firebase
                CoacheeCard(name = habit.habitOwnerName, date=habit.habitOwnerId)
            }
        }
    }
    Spacer(modifier = Modifier
        .height(16.dp)
        .testTag("habit_spacer"))
    Button(
        onClick = {
            onCoachingOffered()
        },
        modifier = Modifier
            .testTag("habit_button"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.today_background),
            contentColor = colorResource(R.color.white)
        )
    ) {
        Text(text = "Apply")
    }
}


@Composable
fun CoacheeCard(
    name: String, date: String,
) {
    LocalContext.current
    Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.testTag("candidate_card_name_$name"),
            )
            Text(
                text = name,
                style = MaterialTheme.typography.body1,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.testTag("candidate_card_username_$name"),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
