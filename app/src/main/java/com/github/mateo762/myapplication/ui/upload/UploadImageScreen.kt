package com.github.mateo762.myapplication.ui.upload

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mateo762.myapplication.*
import com.github.mateo762.myapplication.models.Habit
import com.github.mateo762.myapplication.models.HabitImage
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*

// TODO: This class should be deleted ASAP and be replaced by 'taking a picture' logic

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UploadImageScreen(userId: String, habitId: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Upload Image", fontSize = 24.sp)
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = { onUploadButtonClick(context, coroutineScope, userId, habitId) }
        ) {
            Text(text = "Upload Picture")
        }
        Text(text = "Upload Hardcoded habits", fontSize = 24.sp)
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = { addHabitsToUser(userId, habits = getHardCodedHabits()) }
        ) {
            Text(text = "Upload Habits")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun onUploadButtonClick(context: Context, coroutineScope: CoroutineScope, userId: String, habitId: String) {
    coroutineScope.launch {
        uploadImageToFirebaseStorageAndSaveURL(context, userId, habitId)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType")
private suspend fun uploadImageToFirebaseStorageAndSaveURL(
    context: Context,
    userId: String,
    habitId: String
) {
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("users/$userId/images/${UUID.randomUUID()}.jpg")

    val inputStream = context.resources.openRawResource(R.drawable.exercise_hardcoded_image_3)
    val byteArrayOutputStream = ByteArrayOutputStream()
    inputStream.copyTo(byteArrayOutputStream)
    val data = byteArrayOutputStream.toByteArray()

    withContext(Dispatchers.IO) {
        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val db = Firebase.database.reference
                val habitImage = HabitImage(habitId = habitId, url = imageUrl, date = LocalDateTime.now().toString())
                db.child("users").child(userId).child("imagesPath").push().setValue(habitImage)
            }
        }.addOnFailureListener {
            // Handle failure
        }
    }
}

fun addHabitsToUser(userId: String, habits: List<Habit>) {
    val TAG = "addHabitsToUser"
    val db = Firebase.database.reference
    val habitsRef = db.child("users").child(userId).child("habitsPath")

    habits.forEach { habit ->
        val habitData = hashMapOf(
            "id" to habit.id,
            "name" to habit.name,
            "days" to habit.days,
            "startTime" to habit.startTime,
            "endTime" to habit.endTime
        )

        habitsRef.push().setValue(habitData)
            .addOnSuccessListener {
                Log.d(TAG, "Habit added successfully. $habit.id")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding habit: $e")
            }
    }
}