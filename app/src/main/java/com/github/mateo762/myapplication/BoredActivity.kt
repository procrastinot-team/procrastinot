package com.github.mateo762.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

data class BoredActivityData(
    val activity: String,
    val key: String
)

class BoredActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bored)

        // Return button
        val returnButton = findViewById<Button>(R.id.boredGoBackButton)
        returnButton.setOnClickListener {
            val returnIntent = Intent(this@BoredActivity, MainActivity::class.java)
            this@BoredActivity.startActivity(returnIntent)
        }

        // Retrofit API
        val boredApi = Retrofit.Builder()
            .baseUrl("https://www.boredapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BoredApi::class.java)

        // Room DB
        val db = Room
            .databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "activity-database"
            )
            .build()
        val dao = db.activityDao()

        // Start coroutine for Retrofit
        CoroutineScope(Dispatchers.IO).launch {
            val activityText = findViewById<TextView>(R.id.boredApiResponseText)
            withContext(Dispatchers.Main) {
                try {
                    // Successful response
                    val response = boredApi.getActivity()
                    if (response.isSuccessful) {
                        val responseActivityText = response.body()?.activity
                        activityText.text = responseActivityText
                        // Construct and insert DB Entity from the response data
                        val responseEntity = EntityActivity(Random.nextInt(), responseActivityText)
                        dao.addActivities(responseEntity)
                    }
                } catch (e: Throwable) {
                    // Response failed, go offline for results
                    activityText.text = dao.getAll().random().activity.toString()
                    // Show toast message to inform user we use offline results cached
                    val text = "Connection error: using previously cached results"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
            }
        }
    }
}
