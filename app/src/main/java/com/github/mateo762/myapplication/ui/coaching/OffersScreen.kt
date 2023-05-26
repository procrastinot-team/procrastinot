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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    currentUserId: String,
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

                    //Call DisplayCoachingOffer if currentUser hasn't already applied
                    //to coach this habit
                    if (!habit.coachOffers.contains(currentUserId)) {
                        DisplayCoachingOffer(habit = habit) {
                            onCoachingOffered(habit)
                        }
                    }
                    //The current user has already applied to coach this habit
                    else{
                        DisplayAppliedCoachOffer(habit = habit)
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
            .padding(vertical = 0.dp, horizontal = 16.dp)
            //.size(400.dp, 300.dp)
            .testTag("coaching_habits_box")

    ) {
        Column {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("coachable_habit_${habit.name}")
            )
            Row(modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp)) {
                //Retrieve the name of the habit Owner from Firebase
                CoacheeCard(habit)
            }
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

    }

    //Add a spacing
    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
fun DisplayAppliedCoachOffer(
    habit: HabitEntity
) {
    Box(
        modifier = Modifier
            .background(
                colorResource(R.color.card_background_light),
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 16.dp)
            //.size(400.dp, 300.dp)
            .testTag("coaching_habits_box_gray")

    ) {
        Column {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("coachable_habit_gray_${habit.name}")
            )
            Row(modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp)) {
                //Retrieve the name of the habit Owner from Firebase
                CoacheeCard(habit)
            }
            //Display a disabled greyed out button
            Button(
                onClick = {
                },
                enabled = false,
                modifier = Modifier
                    .testTag("habit_button"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.gray),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text(text = "Applied")
            }
        }

    }

    //Add a spacing
    Spacer(modifier = Modifier.height(16.dp))

}
@Composable
fun CoacheeCard(
    habit: HabitEntity
) {
    LocalContext.current
    Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = habit.habitOwnerName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.testTag("candidate_card_name_${habit.habitOwnerName}"),
            )
            Text(
                text = habit.days.joinToString(),
                style = MaterialTheme.typography.body1,
                )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Start time: ${habit.startTime}",
                style = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "End time: ${habit.endTime}",
                style = MaterialTheme.typography.body1,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
