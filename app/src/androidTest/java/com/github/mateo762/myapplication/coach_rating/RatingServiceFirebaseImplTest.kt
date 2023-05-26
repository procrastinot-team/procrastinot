package com.github.mateo762.myapplication.coach_rating

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RatingServiceFirebaseImplTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var db: DatabaseReference

    private var userId: String = UUID.randomUUID().toString()

    @Before
    fun setup() {
        hiltRule.inject()

    }

    @Test
    fun getRatings() = runTest {
        //given
        db.child(RatingServiceFirebaseImpl.RATINGS_REF).child(userId).push().setValue(5).await()
        val service = RatingServiceFirebaseImpl(db)
        var result: List<Int>? = null

        //when
        service.getRatings(userId).collect {
            result = it
        }

        assertNotNull(result)
        assertEquals(1 , result?.size)
        assertEquals(5 , result?.get(0))
    }
}