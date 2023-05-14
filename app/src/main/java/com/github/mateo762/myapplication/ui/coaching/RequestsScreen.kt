package com.github.mateo762.myapplication.ui.coaching

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RequestsScreen(coachableHabits: List<HabitEntity>) {
    val uncoachedHabits = coachableHabits.filter { !it.isCoached }.toMutableList()
    val coachedHabits = coachableHabits.filter { it.isCoached }.toMutableList()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val habitsPath = "/users/${currentUser?.uid}/habitsPath"
    val habitsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(habitsPath)

    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
    ) {
        if (uncoachedHabits.isEmpty() && coachedHabits.isEmpty()) {
            DisplayNothing()
        } else {
            LazyColumn {
                items(uncoachedHabits) { habit ->
                    DisplayCoachSelection(habit) { coach ->
                        habitsRef.orderByChild("id").equalTo(habit.id)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.children.forEach { childSnapshot ->
                                        val habitSnapshot =
                                            childSnapshot.getValue(HabitEntity::class.java)
                                        if (habitSnapshot != null && !habitSnapshot.isCoached) {
                                            // Update the child with the matching ID
                                            childSnapshot.ref.updateChildren(
                                                mapOf(
                                                    "isCoached" to true,
                                                    "coach" to coach.uid
                                                )
                                            )
                                        }
                                    }
                                    // Remove the habit from the list
                                    uncoachedHabits -= habit
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle cancellation
                                }
                            })
                    }
                }
            }
        }
        LazyColumn {
            items(coachedHabits) { coachedHabit ->
                val scope = rememberCoroutineScope()
                var coach by remember { mutableStateOf(UserEntity()) }
                scope.launch {
                    coach = UserRepository().getUser(coachedHabit.coach)!!
                }
                DisplayCurrentCoach(coachedHabit, coach)
            }
        }
    }
}

@Composable
fun DisplayNothing() {
    Box(
        modifier = Modifier
            .background(
                colorResource(R.color.white),
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .testTag("nothing_to_see_box")
    ){
        Text(
            text = "Nothing to see here for now...",
            style = MaterialTheme.typography.h6,
            fontStyle = Italic,
            modifier = Modifier.testTag("nothing_to_see_text")

        )
    }
}

@Composable
fun DisplayCoachSelection(habit: HabitEntity, onCoachSelected: (UserEntity) -> Unit) {
    val candidateUsernames = habit.coachOffers
    val rating = 4.5
    val candidateUserEntities = remember { mutableStateListOf<UserEntity>() }
    val userRepository = UserRepository()
    LaunchedEffect(candidateUsernames) {
        candidateUsernames.forEach { candidateUid ->
            val candidateCoach = userRepository.getUser(candidateUid)
            if (candidateCoach != null) {
                candidateUserEntities.add(candidateCoach)
            }
        }
    }
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
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("habit_name")
            )
            if (candidateUserEntities.size == 0) {
                EmptyCandidateCard()
            } else {
                for (candidate in candidateUserEntities) {
                    if (candidate.name != null && candidate.username != null && candidate.email != null) {
                        CandidateCard(
                            candidate.name!!,
                            candidate.username!!,
                            rating, // TODO
                            candidate.email!!,
                            onSelected = { onCoachSelected(candidate) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EmptyCandidateCard() {
    Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Text(
            text = "No candidates applied yet!",
            style = MaterialTheme.typography.h6,
            fontStyle = Italic,
            modifier = Modifier.testTag("no_candidates_text")
        )
    }
}

@Composable
fun CandidateCard(
    name: String, username: String, rating: Any, email: String,
    onSelected: () -> Unit
) {
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
                .testTag("candidate_card_image")
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.testTag("candidate_card_name_$name"),
            )
            Text(
                text = username,
                style = MaterialTheme.typography.body1,
                fontStyle = Italic,
                modifier = Modifier.testTag("candidate_card_username_$username"),
                )
            Text(
                text = email,
                style = MaterialTheme.typography.body1,
                fontStyle = Italic,
                modifier = Modifier.testTag("candidate_card_email_$email"),
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
                    fontStyle = Italic,
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .testTag("candidate_card_rating")
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                onSelected()
                Toast.makeText(
                    context,
                    "Selected as coach",
                    Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier.align(Alignment.CenterVertically)
                .testTag("candidate_card_button"),
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

    @Composable
    fun DisplayCurrentCoach(habit: HabitEntity, coach: UserEntity) {
        Box(
            modifier = Modifier
                .background(
                    colorResource(R.color.card_background_light),
                    RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                //.size(400.dp, 300.dp)
                .testTag("current_coach_display_box")

        ) {
            Column {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold
                )
                coach.name?.let {
                    coach.username?.let { it1 ->
                        coach.email?.let { it2 ->
                            CoachCard(
                                it,
                                it1,
                                it2,
                            )
                        }
                    }
                }
                        }
                    }
            Spacer(modifier = Modifier.height(16.dp))
        }

@Composable
fun CoachCard(name: String, username: String, email: String) {
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
                text = "Coach: $name",
                style = MaterialTheme.typography.h6,
            )
            Text(
                text = "@$username",
                style = MaterialTheme.typography.body1,
                fontStyle = Italic
            )
            Text(
                text = email,
                style = MaterialTheme.typography.body1,
                fontStyle = Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



