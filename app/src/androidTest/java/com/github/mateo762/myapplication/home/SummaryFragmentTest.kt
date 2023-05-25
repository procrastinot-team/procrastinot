package com.github.mateo762.myapplication.home

import android.net.ConnectivityManager
import android.net.Network
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.room.HabitRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SummaryFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    // Set up an executor test rule
    @get:Rule
    val executorTestRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private lateinit var habitsRef: DatabaseReference

    @MockK(relaxed = true)
    private lateinit var dataSnapshot: DataSnapshot

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var summaryFragment: SummaryFragment

    private lateinit var testCoroutineScope: TestCoroutineScope


    @Before
    fun setup() {
        hiltRule.inject()

        testCoroutineScope = TestCoroutineScope()

        testCoroutineScope.runBlockingTest {
            withContext(Dispatchers.Main) {
                val activityScenario = activityRule.scenario

                activityScenario.onActivity { activity ->
                    val fragmentFactory = activity.supportFragmentManager.fragmentFactory

                    launchFragmentInContainer<SummaryFragment>(
                        factory = fragmentFactory,
                        fragmentArgs = null,
                        initialState = Lifecycle.State.RESUMED
                    ).onFragment { fragment ->
                        summaryFragment = fragment
                    }
                }
            }
        }


        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        mockkStatic(FirebaseDatabase::class)
        every { summaryFragment.requireContext() } returns ApplicationProvider.getApplicationContext()
        every { FirebaseAuth.getInstance().currentUser } returns mockk(relaxed = true)
        every { summaryFragment.viewLifecycleOwner } returns mockk(relaxed = true)
        every { summaryFragment.lifecycle } returns mockk {
            every { currentState } returns Lifecycle.State.STARTED
        }
        every { summaryFragment.habitRepository } returns habitRepository
        every { summaryFragment.privateCall<Any>("getFirebaseHabitsFromPath", any()) } just awaits
        every { summaryFragment.privateCall<Any>("getLocalHabits") } just awaits
        every { summaryFragment.privateCall<Any>("updateHabitsCache", any()) } just awaits
        //every { summaryFragment.habitRepository.insertAllHabits(any()) } returns Unit
        habitRepository = mockk()

        // Set the habitRepository field in summaryFragment to use the mock object
        summaryFragment.habitRepository = habitRepository
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testHabitsFetch() {
        // Mock the active network and network capabilities
        val activeNetwork = mockk<Network>()
        every { connectivityManager.activeNetwork } returns activeNetwork

        // Mock the Firebase habits response
        val habitEntity = mockk<HabitEntity>()
        every { dataSnapshot.children } returns listOf(mockk { every { getValue(HabitEntity::class.java) } returns habitEntity })
        every { dataSnapshot.getValue(ArgumentMatchers.any<Class<MutableList<HabitEntity>>>()) } returns mutableListOf(
            habitEntity
        )

        // Mock the Firebase database reference
        every { FirebaseDatabase.getInstance().getReference(any()) } returns habitsRef
        every { habitsRef.addListenerForSingleValueEvent(any()) } answers {
            val listener = arg<ValueEventListener>(0)
            listener.onDataChange(dataSnapshot)
        }

        // Call the method under test
        summaryFragment.onViewCreated(mockk(), mockk())

        // Verify that the Firebase habits were fetched and cached
        verify {
            summaryFragment.privateCall<Any>("getFirebaseHabitsFromPath", any())
            summaryFragment.privateCall<Any>("updateHabitsCache", any())
        }

        // Verify that the local habits were not fetched
        verify(inverse = true) {
            summaryFragment.privateCall<Any>("getLocalHabits")
        }
    }

    @Test
    fun testNoConnection() {
        // Mock the active network and network capabilities
        every { connectivityManager.activeNetwork } returns null

        // Call the method under test
        summaryFragment.onViewCreated(mockk(), mockk())

        // Verify that the local habits were fetched
        verify {
            summaryFragment.privateCall<Any>("getLocalHabits")
        }

        // Verify that the Firebase habits were not fetched
        verify(inverse = true) {
            summaryFragment.privateCall<Any>("getFirebaseHabitsFromPath", any())
            summaryFragment.privateCall<Any>("updateHabitsCache", any())
        }
    }

    @Test
    fun testDeleteHabit() = testCoroutineScope.runBlockingTest {
        // Mock the habit and current user
        val habitEntity = mockk<HabitEntity>()
        val currentUser = mockk<FirebaseUser>()
        every { FirebaseAuth.getInstance().currentUser } returns currentUser
        every { currentUser.uid } returns "user_id"
        every { habitEntity.id } returns "habit_id"


        // Mock the Firebase database reference
        every { FirebaseDatabase.getInstance().getReference(any()) } returns habitsRef
        every { habitsRef.orderByChild(any()).equalTo(any<String>()) } returns habitsRef
        every { habitsRef.addListenerForSingleValueEvent(any()) } answers {
            val listener = arg<ValueEventListener>(0)
            listener.onDataChange(dataSnapshot)
        }
        every { habitsRef.ref } returns mockk(relaxed = true)
        every { habitsRef.ref.removeValue() } returns mockk(relaxed = true)
        every { summaryFragment.privateCall<Any>("getFirebaseHabitsFromPath", any()) } just awaits
        coEvery { habitRepository.insertAllHabits(any()) } returns Unit

        // Call the method under test
        runBlocking {
            summaryFragment.deleteHabit(habitEntity)
        }
        // Verify that the habit was deleted and Firebase was updated
        verify {
            runBlocking { habitRepository.deleteHabit(habitEntity) }
            habitsRef.orderByChild("id").equalTo("habit_id")
            habitsRef.addListenerForSingleValueEvent(any())
            habitsRef.ref.removeValue()
            summaryFragment.privateCall<Any>("getFirebaseHabitsFromPath", any())
        }
    }
}

inline fun <reified T> Any.privateCall(functionName: String, vararg args: Any?): T? {
    val function = javaClass.getDeclaredMethod(
        functionName,
        *args.map { it?.javaClass ?: Unit::class.java }.toTypedArray()
    )
    function.isAccessible = true
    return function.invoke(this, *args) as? T
}

