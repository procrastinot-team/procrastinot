package com.github.mateo762.myapplication.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.models.HabitEntity

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HabitListScreen(habits: List<HabitEntity>) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, HabitsActivity.HabitsEntryPoint::class.java)
                    context.startActivity(intent)
                },
                backgroundColor = Color.White,
                contentColor = colorResource(R.color.today_background)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_new),
                    contentDescription = "Add"
                )
            }
        },
        modifier = Modifier.testTag("btn_new")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(habits) { habit ->
                HabitListItem(habit = habit)
            }
        }
    }
}

@Composable
fun HabitListItem(habit: HabitEntity) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag(habit.name)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = habit.days.joinToString(),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Start time: ${habit.startTime}",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "End time: ${habit.endTime}",
                style = MaterialTheme.typography.body1
            )
        }
    }
}