package com.github.mateo762.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BoredActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bored)
        val button = findViewById<Button>(R.id.boredGoBackButton)
        button.setOnClickListener {
            val returnIntent = Intent(this@BoredActivity, MainActivity::class.java)
            this@BoredActivity.startActivity(returnIntent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.boredapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val boredApi = retrofit.create(BoredApi::class.java)

        boredApi.getActivity().enqueue(object : Callback<BoredActivityData> {
            override fun onResponse(call: Call<BoredActivityData>, response: Response<BoredActivityData>) {
                // TODO: Handle the response
                val activityText = findViewById<TextView>(R.id.boredApiResponseText)
                activityText.text = response.body()?.activity
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<BoredActivityData>, t: Throwable) {
                // TODO: Handle the error
                val activityText = findViewById<TextView>(R.id.boredApiResponseText)
                activityText.text = "There has been an error reaching the server"
            }
        })
    }
}

data class BoredActivityData(
    val activity: String,
    val key: String
)