package com.github.mateo762.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*
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

        CoroutineScope(Dispatchers.IO).launch {
            val activityText = findViewById<TextView>(R.id.boredApiResponseText)
            val errorMessage = "Loading from saved activities..."
            val coroutineExceptionHandler = CoroutineExceptionHandler{ _, _ ->
                run {
                    activityText.text = errorMessage
                }
            }
            withContext(Dispatchers.Main + coroutineExceptionHandler) {
                try {
                    val response = boredApi.getActivity()
                    if (response.isSuccessful) {
                        val responseActivityText = response.body()?.activity
                        activityText.text = responseActivityText
                        val responseEntity = EntityActivity(Random.nextInt(), responseActivityText)
                        dao.addActivities(responseEntity)
                    }
                } catch (e: Throwable) {
                    println(dao.getAll().toString())
                    activityText.text = dao.getAll().random().activity.toString()

                }
            }
        }
    }
}
