package com.github.mateo762.myapplication

import retrofit2.Call
import retrofit2.http.GET

interface BoredApi {
    @GET("activity")
    fun getActivity(): Call<BoredActivityData>
}