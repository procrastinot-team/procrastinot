package com.github.mateo762.myapplication

import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.home.HomeActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.collections.HashMap
import kotlin.concurrent.schedule
import kotlin.math.log


@RunWith(AndroidJUnit4::class)
class CreateHabitTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(HabitsActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var habitName: String
    private lateinit var habitDays: List<DayOfWeek>
    private lateinit var habitStartTime: String
    private lateinit var habitEndTime: String
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    @Before
    fun setUp() {
        habitName = "Drink Water"
        habitDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        habitStartTime = "07:30"
        habitEndTime = "08:00"

        // DB setup
        db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)
        dbRef = db.reference

        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun createHabit() {

        // Enter habit name
        composeTestRule.onNodeWithTag("txt_name")
            .performScrollTo()
            .performTextInput(habitName)

        // Check checkboxes for habit days
        habitDays.forEach { day ->
            composeTestRule.onNodeWithTag("checkbox_${day.name}")
                .performScrollTo()
                .performClick()
        }
        // Clear start and end time
        composeTestRule.onNodeWithTag("txt_time_start")
            .performScrollTo()
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTag("txt_time_end")
            .performScrollTo()
            .performClick()
            .performTextClearance()

        // Enter start and end time
        composeTestRule.onNodeWithTag("txt_time_start")
            .performScrollTo()
            .performTextInput(habitStartTime)
        composeTestRule.onNodeWithTag("txt_time_end")
            .performScrollTo()
            .performTextInput(habitEndTime)

        // Click save button
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .performClick()
    }

    @Test
    fun testCreateHabitSwitchActivity() {
        createHabit()
        // Verify that the intent was sent correctly
        intended(
            allOf(
                hasComponent(HomeActivity::class.java.name),
            )
        )
    }

    @Test
    fun testCreateHabitSaveToFirebase() {
        // count references in the db
        var countbefore: Int = 0
        var countafter: Int = 0

        // fetch for the count of habits before the creation of habit
        val userRefBefore = dbRef.child("users").child("makfazlic")
        userRefBefore.get().addOnSuccessListener {
            val resp: HashMap<String, Any> = it.value as HashMap<String, Any>
            countbefore = resp.size
            Log.i("firebase", "Got value ${countbefore}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        // run the create habit and save to firebase
        createHabit()

        // Wait for the write to the firebase and check the count of habits after the creation
        Timer().schedule(500){
            val userRefAfter = dbRef.child("users").child("makfazlic")
            userRefAfter.get().addOnSuccessListener {
                val resp: HashMap<String, Any> = it.value as HashMap<String, Any>
                countafter = resp.size
                Log.i("firebase", "Got value ${countafter}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

        // Wait for the data of after creation and assert if there is a one more after
        Timer().schedule(1000) {
            Log.i("firebase assert", "${countbefore}, ${countafter}")
            assert((countbefore + 1) == countafter)
        }

    }

    @Test
    fun testCreateHabitDontSaveToFirebase() {
        // count references in the db
        var countbefore: Int = 0
        var countafter: Int = 0
        habitName = ""

        // fetch for the count of habits before the creation of habit
        val userRefBefore = dbRef.child("users").child("makfazlic")
        userRefBefore.get().addOnSuccessListener {
            val resp: HashMap<String, Any> = it.value as HashMap<String, Any>
            countbefore = resp.size
            Log.i("firebase", "Got value ${countbefore}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        // run the create habit and save to firebase
        createHabit()

        // Wait for the write to the firebase and check the count of habits after the creation
        Timer().schedule(500){
            val userRefAfter = dbRef.child("users").child("makfazlic")
            userRefAfter.get().addOnSuccessListener {
                val resp: HashMap<String, Any> = it.value as HashMap<String, Any>
                countafter = resp.size
                Log.i("firebase", "Got value ${countafter}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

        // Wait for the data of after creation and assert if there is the same count
        Timer().schedule(1000) {
            Log.i("firebase assert", "${countbefore}, ${countafter}")
            assert(countbefore == countafter)
        }

    }

    @Test
    fun clickSaveButton_noNameInput_noIntentSent() {
        habitName = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noDaysSelected_noIntentSent() {
        habitDays = listOf()
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noStartTime_noIntentSent() {
        habitStartTime = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_startTimeInvalid_noIntentSent() {
        habitStartTime = "25:00"
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noEndTime_noIntentSent() {
        habitEndTime = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_endTimeInvalid_noIntentSent() {
        habitEndTime = "08:69"
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

}