package com.github.mateo762.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoredActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(BoredActivity::class.java)

    // Create MockWebServer, it will return the sample response
    private val mockWebServer = MockWebServer()

    // NOTE: this response is not properly encoded in the body, it will display some text, but not
    // these fields, to do so, we would need to recreate it in JSON format and parse it into bytes in
    // UTF-8 format.
    private val sampleResponse = BoredActivityData("Sample activity", "sample_key")

    @Before
    fun setup() {
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }


    @Test
    // Dispatch a valid response with the given text
    fun testSuccessfulResponseDisplaysData() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(sampleResponse.toString())
            }
        }
        onView(withId(R.id.boredApiResponseText))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    // Dispatch a failed response, and verify text is still displayed
    // NOTE: trying to check the displayed text is indeed in the database is not trivial
    // considering DB interaction must be async, and when making a suspend fun test, the
    // compiler crashes
    fun testFailedResponseDisplaysCachedData() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(503)
                    .setBody(sampleResponse.toString())
            }
        }
        onView(withId(R.id.boredApiResponseText))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

}